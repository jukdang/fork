package egovframework.theimc.operation.web;

import java.util.HashMap;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.theimc.config.interceptor.CustomStat;
import egovframework.theimc.config.login.AuthHelper;
import egovframework.theimc.operation.service.ReportService;
import egovframework.theimc.util.StatusMsg;

/**
 * 운행 행태 - 운전습관 리포트
 */
@Controller
@RequestMapping(value = "/operation")
public class ReportController {

	@Autowired
	private AuthHelper authHelper;

	@Autowired
	private ReportService reportService;

	/**
	 * 운행 행태 - 운전습관 리포트
	 */
	@CustomStat(value = "운행 행태", subValue = "운전습관 리포트", description = "운행 행태 - 운전습관 리포트")
	@RequestMapping(value = "/driverList")
	public String driverList(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		// HashMap에 권한 정보 추가
		authHelper.addAuthToMapAndModel(requestMap, model);
		EgovMap pageInfo = new EgovMap();
		pageInfo.put("pageTitle", "운전습관 리포트");
		pageInfo.put("pageMenuName", "operation");
		pageInfo.put("pageSubMenuName", "report");

		model.addAttribute("pageInfo", pageInfo);
		return "/operation/report/driverList";
	}

	/**
	 * 운행 행태 - 운전습관 리포트 상세
	 */
	@CustomStat(value = "운행 행태", subValue = "운전습관 리포트", description = "운행 행태 - 운전습관 리포트")
	@RequestMapping(value = "/report")
	public String report(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMapAndModel(requestMap, model);
		EgovMap pageInfo = new EgovMap();
		pageInfo.put("pageTitle", "운전습관 리포트 상세");
		pageInfo.put("pageMenuName", "operation");
		pageInfo.put("pageSubMenuName", "report");
		model.addAttribute("pageInfo", pageInfo);
		return "/operation/report/report";
	}

	/**
	 * 운전습관 리포트 - 운전원별 현황
	 */
	@RequestMapping(value = "/getDriverList")
	public ResponseEntity<StatusMsg> getDriverList(@RequestParam HashMap<String, Object> requestMap, ModelMap model)
			throws Exception {
		// HashMap에 권한 정보 추가
		authHelper.addAuthToMapAndModel(requestMap, model);
		EgovMap resultList = reportService.selectDriverList(requestMap);
		// try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		} else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}

	/**
	 * 운전습관 리포트 - 상세 리포트 데이터
	 */
	@RequestMapping(value = "/getDriverReport")
	public ResponseEntity<StatusMsg> getDriverReport(@RequestParam HashMap<String, Object> requestMap, ModelMap model)
			throws Exception {
		// HashMap에 권한 정보 추가
		authHelper.addAuthToMapAndModel(requestMap, model);
		EgovMap resultList = reportService.selectDriverReport(requestMap);
		// try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		} else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
}
