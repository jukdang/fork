package egovframework.theimc.vehicle.web;

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
import egovframework.theimc.util.StatusMsg;
import egovframework.theimc.vehicle.service.DiagnosisHistoryService;
import egovframework.theimc.vehicle.service.VehicleService;

/**
 * 차량 정비 - 진단내역 컨트롤러 클래스
 */
@Controller
@RequestMapping(value="/vehicle")
public class DiagnosisHistoryController {
	
	@Autowired
	private AuthHelper authHelper;

	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private DiagnosisHistoryService diagnosisHistoryService;
	
	/**
	 * 진단 내역 - 목록 페이지
	 */
	@CustomStat(value = "차량정비", subValue = "진단내역", description = "진단내역 - 진단내역 목록 페이지")
	@RequestMapping(value = "/diagnosisHistory")
	public String diagnosisHistory(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMapAndModel(requestMap, model);
		EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "진단 내역");
	    pageInfo.put("pageMenuName", "vehicle");
	    pageInfo.put("pageSubMenuName", "diagnosisHistory");
	    model.addAttribute("pageInfo", pageInfo);
	    return "/vehicle/diagnosisHistory/diagnosisHistory";
	}
	
	/**
	 * 진단 내역 - 목록 조회
	 */
	@RequestMapping(value = "/getDiagnosisHist")
	public ResponseEntity<StatusMsg> getDiagnosisHist(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = diagnosisHistoryService.selectDiagnosisHist(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	/**
	 * 진단 내역 - 상세 페이지
	 */
	@CustomStat(value = "차량정비", subValue = "진단내역", description = "진단내역 - 진단내역 상세 페이지")
	@RequestMapping(value = "/diagnosisHistoryResult")
	public String diagnosisHistoryResult(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMapAndModel(requestMap, model);
		EgovMap vehicleInfo = vehicleService.selectVehicleInfo(requestMap);
		EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "진단 내역");
	    pageInfo.put("pageMenuName", "vehicle");
	    pageInfo.put("pageSubMenuName", "diagnosisHistory");
	    model.addAttribute("pageInfo", pageInfo);
	    model.addAttribute("vehicleInfo", vehicleInfo);
	    return "/vehicle/diagnosisHistory/diagnosisHistoryResult";
	}
	
	/**
	 * 진단 내역 - 상세 조회
	 */
	@RequestMapping(value = "/getDiagnosisHistResult")
	public ResponseEntity<StatusMsg> getDiagnosisHistResult(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = diagnosisHistoryService.selectDiagnosisHistResult(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
}
