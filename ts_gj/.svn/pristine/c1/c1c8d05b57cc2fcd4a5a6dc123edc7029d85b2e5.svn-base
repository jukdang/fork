package egovframework.theimc.blackbox.web;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import egovframework.theimc.cmmn.influx.web.InfluxQueryController;
import egovframework.theimc.config.interceptor.CustomStat;
import egovframework.theimc.config.login.AuthHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value="/blackbox")
public class BlackboxController {

	@Autowired
	private AuthHelper authHelper;

	@Autowired
    private InfluxQueryController influxQueryController;
	
	/**
	 * 데이터 블랙박스
	 */
	@CustomStat(value = "데이터 블랙박스", subValue = "데이터 블랙박스", description = "데이터 블랙박스 - 데이터 블랙박스")
	@RequestMapping(value = "/blackbox")
	public String blackbox(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		// HashMap에 권한 정보 추가
        authHelper.addAuthToMapAndModel(requestMap, model);
	    EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "데이터 블랙박스");
	    pageInfo.put("pageMenuName", "blackbox");
	    pageInfo.put("pageSubMenuName", "blackbox");
	    
	    model.addAttribute("pageInfo", pageInfo);
	    return "/blackbox/blackbox";
	}
	
	/**
	 * 데이터 블랙박스 - 다운로드
	 */
	@RequestMapping(value = "/dataDownload", method = RequestMethod.POST)
	@ResponseBody
	public void dataDownload(@RequestParam HashMap<String, Object> requestMap, HttpServletResponse response) {
	    try {
	        // getSensorHistory 호출
	        ModelAndView result = influxQueryController.getSensorHistory(requestMap);

	        @SuppressWarnings("unchecked")
	        List<HashMap<String, Object>> resultList = (List<HashMap<String, Object>>) result.getModel().get("result");

	        String influxColumns = (String) requestMap.get("influx_column"); // "_time,ECU_BatteryVoltage,..."
	        String influxColumnNames = (String) requestMap.get("influx_column_name");

	        // 컬럼 순서 배열
	        String[] orderedColumns = Arrays.stream(influxColumns.split(","))
                    .filter(col -> !col.equals("_time"))
                    .toArray(String[]::new);

	        // 순서대로 재정렬
	        List<LinkedHashMap<String, Object>> sortedResultList = resultList.stream()
	            .map(row -> {
	                LinkedHashMap<String, Object> sortedRow = new LinkedHashMap<>();
	                // 첫 컬럼 Time
	                sortedRow.put("Time", row.getOrDefault("time", ""));
	                for (String col : orderedColumns) {
	                    sortedRow.put(col, row.getOrDefault(col, ""));
	                }
	                return sortedRow;
	            })
	            .collect(Collectors.toList());

	        // CSV 작성
	        StringBuilder csvContent = new StringBuilder();

	        // 첫 번째 줄: 컬럼 한글명
	        csvContent.append("Time,").append(influxColumnNames.replaceFirst("_time,", "")).append("\n");

	        for (LinkedHashMap<String, Object> row : sortedResultList) {
	            csvContent.append(row.values().stream()
	                .map(v -> v != null ? v.toString() : "")
	                .collect(Collectors.joining(",")));
	            csvContent.append("\n");
	        }

	        // CSV 응답
	        response.setContentType("text/csv; charset=UTF-8");
	        response.setHeader("Content-Disposition", "attachment; filename=\"sensor_data.csv\"");

	        try (OutputStream out = response.getOutputStream()) {
	            // BOM 추가 (Excel 한글 깨짐 방지)
	            out.write(0xEF);
	            out.write(0xBB);
	            out.write(0xBF);

	            // CSV 내용 쓰기
	            out.write(csvContent.toString().getBytes(StandardCharsets.UTF_8));
	            out.flush();
	        }
	        
	    } catch (IOException e) {
	        log.error("[ERROR] BlackboxController dataDownload IOException : ", e);
	    } catch (Exception e) {
	        log.error("[ERROR] BlackboxController dataDownload Exception : ", e);
	    }
	}
}
