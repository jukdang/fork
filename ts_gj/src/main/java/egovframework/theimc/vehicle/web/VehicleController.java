package egovframework.theimc.vehicle.web;

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

import egovframework.theimc.config.login.AuthHelper;
import egovframework.theimc.util.StatusMsg;
import egovframework.theimc.vehicle.service.VehicleService;

/**
 * 차량 정비
 */
@Controller
@RequestMapping(value="/vehicle")
public class VehicleController {
	
	@Autowired
	private AuthHelper authHelper;
	
	@Autowired
	private VehicleService vehicleService;
	
	/**
	 * AI 실시간 진단
	 */
	@RequestMapping(value = "/setRegNoItem")
	public ResponseEntity<StatusMsg> setRegNoItem(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = vehicleService.selectRegNoList(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}

	/**
	 * 차량유형으로 센서 검색
	 */
	@RequestMapping(value = "/getSensorInfo")
	public ResponseEntity<StatusMsg> getSensorInfo(@RequestParam HashMap<String, Object> requestMap, HttpServletRequest request) throws Exception {
		authHelper.addAuthToMap(requestMap);
		// 1. 호출한 페이지(리퍼러) 확인
		String referer = request.getHeader("Referer");
		if (referer != null && referer.contains("blackbox")) {
			requestMap.put("referer", "blackbox");  // 구분자 추가
		}
		EgovMap resultList = vehicleService.selectSensorInfo(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}

}
