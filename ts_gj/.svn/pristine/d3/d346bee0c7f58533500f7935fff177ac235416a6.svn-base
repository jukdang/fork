package egovframework.theimc.operation.web;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.theimc.config.interceptor.CustomStat;
import egovframework.theimc.config.login.AuthHelper;
import egovframework.theimc.operation.service.AiReportService;
import egovframework.theimc.util.StatusMsg;

/**
 * 운행 행태 - AI + 리포트
 */
@Controller
@RequestMapping(value="/operation")
public class AiReportController {

	@Autowired
	private AuthHelper authHelper;
	
	@Autowired
	private AiReportService aiReportService;

	/**
	 * 운행행태 - AI + 리포트
	 */
	@CustomStat(value = "운행 행태", subValue = "AI + 리포트", description = "운행행태 - AI + 리포트")
	@RequestMapping(value = "/aiReportList")
	public String aiReportList(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		// HashMap에 권한 정보 추가
        authHelper.addAuthToMapAndModel(requestMap, model);
	    EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "AI + 리포트 목록");
	    pageInfo.put("pageMenuName", "operation");
	    pageInfo.put("pageSubMenuName", "aiReport");
	    
	    model.addAttribute("pageInfo", pageInfo);
	    return "/operation/aiReport/aiReportList";
	}

	/**
	 * 운행행태 - AI + 리포트 상세
	 */
	@CustomStat(value = "운행 행태", subValue = "AI + 리포트", description = "운행행태 - AI + 리포트")
	@RequestMapping(value = "/aiReport")
	public String aiReport(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		// HashMap에 권한 정보 추가
        authHelper.addAuthToMapAndModel(requestMap, model);
	    EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "AI + 리포트");
	    pageInfo.put("pageMenuName", "operation");
	    pageInfo.put("pageSubMenuName", "aiReport");
	    
	    model.addAttribute("pageInfo", pageInfo);
	    return "/operation/aiReport/aiReport";
	}

	/**
	 * 운행행태 - AI + 리포트 목록
	 */
	@RequestMapping(value = "/getAiReportList")
	public ResponseEntity<StatusMsg> getAiReportList(@RequestParam HashMap<String, Object> requestMap, ModelMap model, HttpServletRequest request) throws Exception {
		// HashMap에 권한 정보 추가
        authHelper.addAuthToMapAndModel(requestMap, model);
		EgovMap resultList = aiReportService.selectAiReportList(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}

	/**
	 * 운행행태 - AI + 리포트 상세 데이터
	 */
	@RequestMapping(value = "/getAiReportResult")
	public ResponseEntity<StatusMsg> getAiReportResult(@RequestParam HashMap<String, Object> requestMap, ModelMap model, HttpServletRequest request) throws Exception {
		// HashMap에 권한 정보 추가
        authHelper.addAuthToMapAndModel(requestMap, model);
		EgovMap resultList = aiReportService.selectAiReportResult(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
}
