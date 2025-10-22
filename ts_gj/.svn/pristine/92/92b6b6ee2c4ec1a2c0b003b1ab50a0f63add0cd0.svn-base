package egovframework.theimc.dashboard.web;

import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.theimc.config.login.AuthHelper;
import egovframework.theimc.dashboard.service.DashboardService;
import egovframework.theimc.util.StatusMsg;

/**
 * 대시보드
 */
@Controller
@RequestMapping(value="/dashboard")
public class DashboardController {
	@Autowired
	private AuthHelper authHelper;
	
	
	@Autowired
	private DashboardService dashboardService;
	
	/**
	 * 대시보드
	 */
	@RequestMapping(value = "/dashboard")
	public String dashboard(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMapAndModel(requestMap, model);
		EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "통합 대시보드");
	    pageInfo.put("pageMenuName", "dashboard");
	    pageInfo.put("pageSubMenuName", "dashboard");
	    
	    model.addAttribute("pageInfo", pageInfo);
	    return "/dashboard/dashboard";
	}

	/**
	 * 대시보드 - 금일 운행현황 - 현재 운행 차량
	 */
	@RequestMapping(value = "/getOprVhclCnt")
	public ResponseEntity<StatusMsg> getOprVhclCnt(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = dashboardService.selectOprVhclCnt(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	
	/**
	 * 대시보드 - 운행현황 추이
	 */
	@RequestMapping(value = "/getOprStatusTrend")
	public ResponseEntity<StatusMsg> getOprStatusTrend(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = dashboardService.selectOprStatusTrend(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	

	/**
	 * 대시보드 - 차량 정보
	 */
	@RequestMapping(value = "/getVhclInfo")
	public ResponseEntity<StatusMsg> getVhclInfo(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = dashboardService.selectVhclInfo(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	

	/**
	 * 대시보드 - 차량 실시간 위치
	 */
	@RequestMapping(value = "/getLatestGpsCoordinates")
	public ResponseEntity<StatusMsg> getLatestGpsCoordinates(@RequestBody HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMapAndModel(requestMap, model);
		EgovMap resultList = dashboardService.selectLatestGpsCoordinates(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	

	/**
	 * 대시보드 - 서비스 이용현황
	 */
	@RequestMapping(value = "/getServiceUsageStatusByUsers")
	public ResponseEntity<StatusMsg> getServiceUsageStatusByUsers(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = dashboardService.selectServiceUsageStatusByUsers(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	
}
