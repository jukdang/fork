package egovframework.theimc.mngr.web;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.theimc.config.login.AuthHelper;
import egovframework.theimc.util.CommonExcelDownload;
import egovframework.theimc.util.StatusMsg;
import egovframework.theimc.vehicle.service.RepairAssistService;

/**
 * 관리자
 */
@Controller
@RequestMapping(value="/mngr")
public class MngrPromptController {
	
	@Autowired
	private AuthHelper authHelper;
	
	@Autowired
	private RepairAssistService repairAssistService;
	
	/**
	 * 프롬프트 현황
	 */
	@RequestMapping(value = "/promptStatus")
	public String promptStatus(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMapAndModel(requestMap, model);
		EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "프롬프트 현황");
	    pageInfo.put("pageMenuName", "status");
	    pageInfo.put("pageSubMenuName", "promptStatus");
	    
	    model.addAttribute("pageInfo", pageInfo);
	    return "/mngr/prompt/promptStatus";
	}
	
	/**
	 * 프롬프트 현황 - 목록 조회
	 */
	@RequestMapping(value = "/getPromptStatusList")
	public ResponseEntity<StatusMsg> getOperatorRfidList(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = repairAssistService.selectChatPromptList(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	
	/**
	 * 프롬프트 현황 - 데이터 다운로드
	 */
	@RequestMapping(value = "/promptDataDownload", method = RequestMethod.POST)
	@ResponseBody
	public void promptDataDownload(@RequestParam HashMap<String, Object> requestMap, HttpServletResponse response) 
	        throws Exception {
	    
	    // 전체 데이터 조회를 위해 searchType을 download로 설정
	    requestMap.put("searchType", "download");
	    authHelper.addAuthToMap(requestMap);
	    
	    // 데이터 조회
	    EgovMap resultMap = repairAssistService.selectChatPromptList(requestMap);
	    List<EgovMap> dataList = (List<EgovMap>) resultMap.get("list");
	    
	    // 컬럼 매핑 설정 (DB컬럼명 -> 한글명)
	    Map<String, String> columnMapping = new LinkedHashMap<>();
	    columnMapping.put("ownrNm", "소속");          // 소속
	    columnMapping.put("mbrId", "ID");            // ID
	    columnMapping.put("qstnCn", "질문");          // 질문
	    columnMapping.put("ansCn", "답변");           // 답변
	    columnMapping.put("evl", "평가");             // 평가
	    columnMapping.put("regDt", "등록일시");        // 등록일시
	    
	    // 컬럼 너비 설정
	    Map<String, Integer> columnWidths = new HashMap<>();
	    columnWidths.put("ownrNm", 3000);    // 소속
	    columnWidths.put("mbrId", 3000);     // ID
	    columnWidths.put("qstnCn", 10000);   // 질문 (넓게)
	    columnWidths.put("ansCn", 15000);    // 답변 (가장 넓게)
	    columnWidths.put("evl", 2500);       // 평가
	    columnWidths.put("regDt", 4000);     // 등록일시
	    
	    // Excel 생성 및 다운로드
	    CommonExcelDownload excelUtil = new CommonExcelDownload();
	    excelUtil.generateExcelDownload(
	        dataList,           // 데이터
	        columnMapping,      // 컬럼 매핑
	        columnWidths,       // 컬럼 너비
	        "프롬프트 현황",     // 시트명
	        "프롬프트현황",      // 파일명
	        response           // 응답 객체
	    );
	}
	
	/**
	 * 프롬프트 현황 - 일별 건수 조회
	 */
	@RequestMapping(value = "/getPromptStatusLineList")
	public ResponseEntity<StatusMsg> getPromptStatusLineList(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = repairAssistService.selectChatPromptLineList(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
}