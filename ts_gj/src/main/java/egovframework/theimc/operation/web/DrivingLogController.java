package egovframework.theimc.operation.web;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.theimc.config.interceptor.CustomStat;
import egovframework.theimc.config.login.AuthHelper;
import egovframework.theimc.operation.service.DrivingLogService;
import egovframework.theimc.util.CommonExcelDownload;
import egovframework.theimc.util.StatusMsg;

/**
 * 운행 행태 - 운행 일지
 */
@Controller
@RequestMapping(value="/operation")
public class DrivingLogController {

	@Autowired
	private AuthHelper authHelper;
	
	@Autowired
	private DrivingLogService drivingLogService;
	
	/**
	 * 운행 행태 - 운행 일지
	 */
	@CustomStat(value = "운행 행태", subValue = "운행 일지", description = "운행 행태 - 운행 일지")
	@RequestMapping(value = "/drivingLogList")
	public String logList(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		// HashMap에 권한 정보 추가
        authHelper.addAuthToMapAndModel(requestMap, model);
	    EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "운행 일지");
	    pageInfo.put("pageMenuName", "operation");
	    pageInfo.put("pageSubMenuName", "drivingLog");
        
		model.addAttribute("pageInfo", pageInfo);
	    return "/operation/drivingLog/drivingLogList";
	}

	/**
	 * 운행 일지 - 운행 일지 상세
	 */
	@CustomStat(value = "운행 행태", subValue = "운행 일지", description = "운행 행태 - 운행 일지")
	@RequestMapping(value = "/drivingLog")
	public String drivingLog(HttpServletRequest request, HttpSession session, ModelMap model, HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMapAndModel(requestMap, model);
		EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "운행 일지 상세");
	    pageInfo.put("pageMenuName", "operation");
	    pageInfo.put("pageSubMenuName", "drivingLog");
	    
	    model.addAttribute("pageInfo", pageInfo);
	    
	    String oprHstrySn = request.getParameter("oprHstrySn");
        requestMap.put("oprHstrySn", oprHstrySn);
	    
	    EgovMap result = drivingLogService.selectDrivingLogDetail(requestMap);
	    
	    model.addAttribute("result", result);
	    
	    return "/operation/drivingLog/drivingLog";
	}
	
	/**
	 * 운행 일지 - 목록 조회
	 */
	@RequestMapping(value = "/getDrivingLogList")
	public ResponseEntity<StatusMsg> getDrivingLogList(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = drivingLogService.selectDrivingLogList(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	
	/**
	 * 운행 일지 - 상세
	 */
	@RequestMapping(value = "/getDrivingLogDetail")
	public ResponseEntity<StatusMsg> getDrivingLogDetail(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = drivingLogService.selectDrivingLogDetail(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	
	/**
	 * 운행 일지 - 목록 다운로드
	 */
	@RequestMapping(value = "/drivingLogListDownload", method = RequestMethod.POST)
	@ResponseBody
	public void drivingLogListDownload(@RequestBody HashMap<String, Object> requestMap, HttpServletResponse response) 
	        throws Exception {
	    
		authHelper.addAuthToMap(requestMap);
	    
	    // 데이터 조회
	    EgovMap resultMap = drivingLogService.selectDrivingLogDetailList(requestMap);
	    List<EgovMap> dataList = (List<EgovMap>) resultMap.get("list");
	    
	    // 컬럼 매핑 설정 (DB컬럼명 -> 한글명)
	    Map<String, String> columnMapping = new LinkedHashMap<>();
	    columnMapping.put("운행 일자", "운행 일자");          
	    columnMapping.put("운수 회사", "운수 회사");          
	    columnMapping.put("운전원 성명", "운전원 성명");           
	    columnMapping.put("승무 개시시간", "승무 개시시간");          
	    columnMapping.put("승무 종료시간", "승무 종료시간");            
	    columnMapping.put("회차", "회차");         
	    columnMapping.put("기점", "기점");
	    columnMapping.put("종점", "종점");
	    columnMapping.put("기점출발", "기점출발");
	    columnMapping.put("종점도착", "종점도착");
	    columnMapping.put("소요시간1", "소요시간(분)");
	    columnMapping.put("종점출발", "종점출발");
	    columnMapping.put("기점도착", "기점도착");
	    columnMapping.put("소요시간2", "소요시간(분)");
	    columnMapping.put("노선명", "노선명");
	    columnMapping.put("차량번호", "차량번호");
	    
	    // 컬럼 너비 설정
	    Map<String, Integer> columnWidths = new HashMap<>();
	    columnWidths.put("운행 일자", 3000);
	    columnWidths.put("운수 회사", 3000);
	    columnWidths.put("운전원 성명", 3000);
	    columnWidths.put("승무 개시시간", 4000);
	    columnWidths.put("승무 종료시간", 4000);
	    columnWidths.put("회차", 1500);
	    columnWidths.put("기점", 5000);
	    columnWidths.put("종점", 5000);
	    columnWidths.put("기점출발", 2500);
	    columnWidths.put("종점도착", 2500);
	    columnWidths.put("소요시간(분)", 4000);
	    columnWidths.put("종점출발", 2500);
	    columnWidths.put("기점도착", 2500);
	    columnWidths.put("소요시간(분)", 4000);
	    columnWidths.put("노선명", 5500);
	    columnWidths.put("차량번호", 4000);
	    
	    // Excel 생성 및 다운로드
	    CommonExcelDownload excelUtil = new CommonExcelDownload();
	    excelUtil.generateExcelDownload(
	        dataList,           // 데이터
	        columnMapping,      // 컬럼 매핑
	        columnWidths,       // 컬럼 너비
	        "운행일지",     		// 시트명
	        "운행일지",      		// 파일명
	        response           	// 응답 객체
	    );
	}
	
}
