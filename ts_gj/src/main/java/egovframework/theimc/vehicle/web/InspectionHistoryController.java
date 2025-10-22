package egovframework.theimc.vehicle.web;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.theimc.config.login.AuthHelper;
import egovframework.theimc.pagination.PageInfoUtil;
import egovframework.theimc.util.StatusMsg;
import egovframework.theimc.vehicle.service.VehicleService;
import egovframework.theimc.vehicle.service.InspectionHistoryService;
/**
 * 차량 정비
 */
@Controller
@RequestMapping(value="/vehicle")
public class InspectionHistoryController {
	
	@Autowired
	private AuthHelper authHelper;
	
	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private InspectionHistoryService inspectionHistoryService;
	
	/**
	 * 정기검사 이력
	 */
	@RequestMapping(value = "/inspectionHistory")
	public String inspectionHistory(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMapAndModel(requestMap, model);
		EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "정기검사 이력");
	    pageInfo.put("pageMenuName", "vehicle");
	    pageInfo.put("pageSubMenuName", "inspectionHistory");
	    
	    model.addAttribute("pageInfo", pageInfo);
	    return "/vehicle//inspectionHistory/inspectionHistory";
	}
	
	/**
	 * 진단 내역 - 목록 조회
	 */
	@RequestMapping(value = "/getInspectionHist")
	public ResponseEntity<StatusMsg> getDiagnosisHist(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = inspectionHistoryService.selectInspectionHist(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	
	/**
	 * 정기검사 이력
	 */
	@RequestMapping(value = "/inspectionHistoryResult")
	public String inspectionHistoryResult(HttpSession session, ModelMap model) throws Exception {
	    EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "정기검사 이력");
	    pageInfo.put("pageMenuName", "vehicle");
	    pageInfo.put("pageSubMenuName", "inspectionHistory");
	    
	    model.addAttribute("pageInfo", pageInfo);
	    return "/vehicle//inspectionHistory/inspectionHistoryResult";
	}
}
