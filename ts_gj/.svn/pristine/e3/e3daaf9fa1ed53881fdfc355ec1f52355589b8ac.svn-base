package egovframework.theimc.operation.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.theimc.config.interceptor.CustomStat;
import egovframework.theimc.config.login.AuthHelper;
import egovframework.theimc.operation.service.DrivingStatusService;
import egovframework.theimc.util.StatusMsg;

/**
 * 운행 행태 - 운행현황판
 */
@Controller
@RequestMapping(value="/operation")
public class DrivingStatusController {

	@Autowired
	private AuthHelper authHelper;
	
	@Autowired
	private DrivingStatusService drivingStatusService;

	/**
	 * 운행 행태 - 운행현황판 - 노선 운행현황
	 */
	@CustomStat(value = "운행 행태", subValue = "운행현황판", description = "운행 행태 - 운행현황판")
	@RequestMapping(value = "/routeOprHist")
	public String routeOprHist(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		// HashMap에 권한 정보 추가
        authHelper.addAuthToMapAndModel(requestMap, model);
	    EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "노선 운행 현황");
	    pageInfo.put("pageMenuName", "operation");
	    pageInfo.put("pageSubMenuName", "status");
	    
	    model.addAttribute("pageInfo", pageInfo);
	    
	    EgovMap rteList = drivingStatusService.selectRteList(requestMap);
	    
	    model.addAttribute("rteList", rteList);
	    
	    return "/operation/drivingStatus/routeOprHist";
	}
	
	/**
	 * 운행 행태 - 운행현황판 - 위험 운행 지도
	 */
	@CustomStat(value = "운행 행태", subValue = "운행현황판", description = "운행 행태 - 운행현황판")
	@RequestMapping(value = "/dangerOprHist")
	public String dangerOprHist(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		// HashMap에 권한 정보 추가
        authHelper.addAuthToMapAndModel(requestMap, model);
	    EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "위험 운행 지도");
	    pageInfo.put("pageMenuName", "operation");
	    pageInfo.put("pageSubMenuName", "status");
	    
	    model.addAttribute("pageInfo", pageInfo);
	    return "/operation/drivingStatus/dangerOprHist";
	}
	
	/**
	 * 운행 행태 - 운행현황판 - 노선 목록
	 */
	@RequestMapping(value = "/getRteList")
	public ResponseEntity<StatusMsg> getRteList(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		// HashMap에 권한 정보 추가
        authHelper.addAuthToMapAndModel(requestMap, model);
	    EgovMap resultList = drivingStatusService.selectRteList(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	
	/**
	 * 운행현황판 - 노선 운행 현황 - 노선 운행 목록
	 */
	@RequestMapping(value = "/getRouteHistList")
	public ResponseEntity<StatusMsg> getRouteHistList(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = drivingStatusService.selectDrivingHistList(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	
	/**
	 * 운행현황판 - 노선 운행 현황 - 노선 운행 상세 목록
	 */
	@RequestMapping(value = "/getRouteHistDetailList")
	public ResponseEntity<StatusMsg> getRouteHistDetailList(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = drivingStatusService.selectRouteHistDetailList(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	
	/**
	 * 운행현황판 - 노선 운행 현황 - 위험운행좌표
	 */
	@RequestMapping(value = "/getDgrCrdList")
	public ResponseEntity<StatusMsg> getDgrCrdList(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = drivingStatusService.selectDgrCrdList(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
}
