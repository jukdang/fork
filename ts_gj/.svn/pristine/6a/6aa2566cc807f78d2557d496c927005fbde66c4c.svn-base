package egovframework.theimc.scheduler;

import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.theimc.config.login.AuthHelper;
import egovframework.theimc.mngr.service.MngrAlertService;
import egovframework.theimc.scheduler.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
@Component
@Controller
public class ScheduledTasks {
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	@Value("${smsApi.url}")
    private String smsApiUrl;
	
	@Autowired
	private AuthHelper authHelper;
	
	@Autowired
	private SchedulerService schedulerService;
	
	@Autowired
	private MngrAlertService mngrAlertService;

	/**
     * 매월 1일 13:00시
     * 문자 알림 - AI 월간 보고서
     */
    @Scheduled(cron = "0 0 13 1 * ?") // 초 분 시 일 월 요일
    public void sendAiRptRcptn() throws Exception {
		//authHelper.addAuthToMap(requestMap);

	    // DB 조회
	    EgovMap resultMap = schedulerService.selectAiRptRcptnList();

	    // list 추출 및 캐스팅
	    List<EgovMap> resultList = (List<EgovMap>) resultMap.get("list");
	    if (resultList == null || resultList.isEmpty()) {
	        log.info("문자 전송 대상이 없습니다.");
	        return;
	    }

        for (EgovMap map : resultList) {
            try {
                String telno = (String) map.get("telno");
            	String reportTitle = (String) map.get("reportTitle");
                String msgCn = (String) map.get("msgCn");
                String fixedLink = "링크 : https://gsafebus.or.kr/operation/aiReportList";
                String message = "[AI + 리포트 알림]\n" + reportTitle + "\n" + msgCn + "\n\n" + fixedLink;

                if (telno == null || message == null) {
                    log.warn("전화번호나 메시지가 비어있습니다. skip → " + map);
                    continue;
                }

                // 바로 Map으로 요청 payload 구성
                HashMap<String, Object> requestBody = new HashMap<>();
                requestBody.put("tel_no", telno);
                requestBody.put("message", message);

                String response = restTemplate.postForObject(
                        smsApiUrl,
                        requestBody,
                        String.class
                );
                
                // JSON 파싱
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response);

                String code = rootNode.path("response").path("code").asText();
                if ("200".equals(code)) {
                    
                    HashMap<String, Object> param = new HashMap<>();
                    param.put("rcptnType", "rpt");
                    param.put("ownrNm", map.get("ownrNm"));
                    param.put("telno", telno);
                    param.put("msgCn", message);
                    
                    mngrAlertService.insertRecipientHstry(param);
                    
                    log.info("문자 알림 - AI 월간 보고서 문자 전송 성공 → 번호: {}, 응답: {}", telno, response);
                } else {
                    log.warn("문자 알림 - AI 월간 보고서 SMS API 응답 code != 200 → {}", code);
                }

            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error("[ERROR] ScheduledTasks sendAiRpt Exception: ", e);
                }
            }
        }
    }
    
    /**
     * 매일 11:00시, 16:00시
     * 문자 알림 - 차량점검
     */
    @Scheduled(cron = "0 0 11,16 * * *") // 초 분 시 일 월 요일
    public void sendChckRcptn() throws Exception {
		//authHelper.addAuthToMap(requestMap);

	    // DB 조회
	    EgovMap resultMap = schedulerService.selectChckRcptnList();

	    // list 추출 및 캐스팅
	    List<EgovMap> resultList = (List<EgovMap>) resultMap.get("list");
	    if (resultList == null || resultList.isEmpty()) {
	        log.info("문자 전송 대상이 없습니다.");
	        return;
	    }

        for (EgovMap map : resultList) {
            try {
                String telno = (String) map.get("telno");
            	String regNoList = (String) map.get("regNoList");
            	String fixedLink = "링크 : https://gsafebus.or.kr/vehicle/diagnosisRealtime";
                String message = "[정비알림]\n" + regNoList + "\n차량 점검필요\n\n" + fixedLink;;

                if (telno == null || message == null) {
                    log.warn("전화번호나 메시지가 비어있습니다. skip → " + map);
                    continue;
                }

                // 바로 Map으로 요청 payload 구성
                HashMap<String, Object> requestBody = new HashMap<>();
                requestBody.put("tel_no", telno);
                requestBody.put("message", message);

                String response = restTemplate.postForObject(
                        smsApiUrl,
                        requestBody,
                        String.class
                );
                
                // JSON 파싱
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response);

                String code = rootNode.path("response").path("code").asText();
                if ("200".equals(code)) {
                    
                    HashMap<String, Object> param = new HashMap<>();
                    param.put("rcptnType", "chck");
                    param.put("ownrNm", map.get("ownrNm"));
                    param.put("telno", telno);
                    param.put("msgCn", message);
                    
                    mngrAlertService.insertRecipientHstry(param);
                    
                    log.info("문자 알림 - 차량점검 문자 전송 성공 → 번호: {}, 응답: {}", telno, response);
                } else {
                    log.warn("문자 알림 - 차량점검 SMS API 응답 code != 200 → {}, {}", code, response);
                }

            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error("[ERROR] ScheduledTasks sendChckRcptn Exception: ", e);
                }
            }
        }
    }
}
