package egovframework.theimc.vehicle.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

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
import egovframework.theimc.vehicle.service.DiagnosisRealtimeService;
import egovframework.theimc.vehicle.service.VehicleService;

/**
 * 차량 정비 - AI 실시간 진단 컨트롤러 클래스
 */
@Controller
@RequestMapping(value="/vehicle")
public class DiagnosisRealtimeController {
	
	@Autowired
	private AuthHelper authHelper;
	
	@Autowired
	private DiagnosisRealtimeService diagnosisRealtimeService;
	
	@Autowired
	private VehicleService vehicleService;

	/**
	 * AI 실시간 진단
	 */
	@CustomStat(value = "차량정비", subValue = "AI 실시간 진단", description = "차량정비 - AI 실시간 진단 목록 조회 페이지")
	@RequestMapping(value = "/diagnosisRealtime")
	public String diagnosisRealtime(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
	    EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "AI 실시간 진단");
	    pageInfo.put("pageMenuName", "vehicle");
	    pageInfo.put("pageSubMenuName", "diagnosisRealtime");
	    
	    // HashMap에 권한 정보 추가
        authHelper.addAuthToMapAndModel(requestMap, model);
	    
	    model.addAttribute("pageInfo", pageInfo);
	    return "/vehicle/diagnosisRealtime/diagnosisRealtime";
	}
	
	/**
	 * AI 실시간 진단
	 */
	@RequestMapping(value = "/getVehicleSttsList")
	public ResponseEntity<StatusMsg> getVehicleSttsList(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = diagnosisRealtimeService.selectVehicleSttsList(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}

	/**
	 * AI 실시간 진단 결과
	 */
	@CustomStat(value = "차량정비", subValue = "AI 실시간 진단", description = "차량정비 - AI 실시간 진단 상세 페이지")
	@RequestMapping(value = "/diagnosisRealtimeResult")
	public String diagnosisRealtimeResult(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
	    EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "AI 실시간 진단 상세");
	    pageInfo.put("pageMenuName", "vehicle");
	    pageInfo.put("pageSubMenuName", "diagnosisRealtime");

	    authHelper.addAuthToMapAndModel(requestMap, model);

	    EgovMap vehicleInfo = vehicleService.selectVehicleInfo(requestMap);
	    if(vehicleInfo != null) {
	        requestMap.put("vhclType", vehicleInfo.get("vhclType"));
	    }

	    EgovMap sensorInfo = vehicleService.selectSensorInfo(requestMap);

	    // EgovMap에서 list를 추출하여 2중 그룹화
	    List<EgovMap> sensorList = (List<EgovMap>) sensorInfo.get("list");
	    Map<String, Map<String, List<EgovMap>>> groupedSensors = new LinkedHashMap<>();

	    if(sensorList != null && !sensorList.isEmpty()) {
	        for(EgovMap sensor : sensorList) {
	            String sysNm = (String) sensor.get("sysNm");
	            String sysNm2 = (String) sensor.get("sysNm2");
	            
	            // sysNm2가 없으면 null로 처리
	            String subGroup = (sysNm2 != null && !sysNm2.trim().isEmpty()) ? sysNm2 : null;
	            
	            groupedSensors.computeIfAbsent(sysNm, k -> new LinkedHashMap<>())
	                         .computeIfAbsent(subGroup, k -> new ArrayList<>())
	                         .add(sensor);
	        }
	    }

	    model.addAttribute("groupedSensors", groupedSensors);
	    model.addAttribute("pageInfo", pageInfo);
	    model.addAttribute("vehicleInfo", vehicleInfo);
	    model.addAttribute("sensorList", sensorInfo);

	    return "/vehicle/diagnosisRealtime/diagnosisRealtimeResult";
	}
	
	/**
	 * AI 실시간 진단 상세
	 */
	@RequestMapping(value = "/getVehicleChckBkDnList")
	public ResponseEntity<StatusMsg> getVehicleChckBkDnList(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap chckList = diagnosisRealtimeService.selectVehicleChkList(requestMap);
		EgovMap bkdnList = diagnosisRealtimeService.selectVehicleBkdnList(requestMap);
		
		EgovMap resultList = new EgovMap();
		resultList.put("chckList", chckList);
		resultList.put("bkdnList", bkdnList);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	
	/**
	 * AI 실시간 진단 - 점검 지침서
	 */
	@CustomStat(value = "차량정비", subValue = "AI 실시간 진단", description = "차량정비 - AI 실시간 점검 지침서")
	@RequestMapping(value = "/chckManual")
	public String chkManual(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap result = diagnosisRealtimeService.selectChckManual(requestMap);
		model.addAttribute("result", result);
		return "/vehicle/diagnosisRealtime/chkManual";
	}
	
	/**
	 * AI 실시간 진단 - 고장 코드 지침서
	 */
	@CustomStat(value = "차량정비", subValue = "AI 실시간 진단", description = "차량정비 - AI 실시간 고장코드 지침서")
	@RequestMapping(value = "/bkDnManual")
	public String bkDnManual(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap result = diagnosisRealtimeService.selectBkDnManual(requestMap);
		model.addAttribute("result", result);
		return "/vehicle/diagnosisRealtime/bkDnManual";
	}
	
	/**
	 * AI 실시간 진단 - 알림 정보 조회
	 */
	@RequestMapping(value = "/getVehicleChckAlarm")
	public ResponseEntity<StatusMsg> getVehicleChckAlarm(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap chckList = diagnosisRealtimeService.selectVehicleChckAlarmInfo(requestMap);
		
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (chckList != null && !chckList.isEmpty()) {
			return StatusMsg.ok(chckList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	
}
