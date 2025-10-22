package egovframework.theimc.vehicle.web;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.theimc.config.interceptor.CustomStat;
import egovframework.theimc.config.login.AuthHelper;
import egovframework.theimc.util.StatusMsg;
import egovframework.theimc.vehicle.service.RepairAssistService;
import egovframework.theimc.vehicle.service.VehicleService;

/**
 * 차량 정비
 */
@Controller
@RequestMapping(value="/vehicle")
public class RepairAssistController {
	
	@Autowired
	private AuthHelper authHelper;
	
	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private RepairAssistService repairAssistService;
    
	@Value("${svc.repairAssist.api}")
    private String apiUrl;
	
	/**
	 * AI + 정비 도우미
	 */
	@CustomStat(value = "차량정비", subValue = "AI 정비 도우미", description = "차량정비 - AI + 정비 도우미 차량 조회 페이지")
	@RequestMapping(value = "/repairAssist")
	public String repairAssist(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
	    EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "AI + 정비 도우미");
	    pageInfo.put("pageMenuName", "vehicle");
	    pageInfo.put("pageSubMenuName", "repairAssist");
	    authHelper.addAuthToMapAndModel(requestMap, model);
	    
	    model.addAttribute("pageInfo", pageInfo);
	    return "/vehicle/repairAssist/repairAssist";
	}
	
	/**
	 * AI + 정비 도우미
	 */
	@RequestMapping(value = "/repairAssistCreateChat")
	public String repairAssistCreateChat(@RequestParam HashMap<String, Object> requestMap, ModelMap model, RedirectAttributes redirectAttributes) throws Exception {
	    authHelper.addAuthToMapAndModel(requestMap, model);
	    EgovMap vehicleInfo = vehicleService.selectVehicleInfo(requestMap);
	    if(vehicleInfo != null) {
	    	requestMap.put("vhclSn",vehicleInfo.get("vhclSn"));
	    	EgovMap chatInfo = repairAssistService.insertChatList(requestMap);
	        redirectAttributes.addFlashAttribute("chatInfo", chatInfo);
	    	model.addAttribute("chatInfo", chatInfo);
	    }else {
	    	redirectAttributes.addFlashAttribute("message", "검색 결과가 없습니다. 차량번호를 정확하게 입력해주세요.");
		    return "redirect:/vehicle/repairAssist";
	    }
	    
	    model.addAttribute("vehicleInfo", vehicleInfo);
	    
        redirectAttributes.addFlashAttribute("regNoInput", requestMap.get("regNoInput"));
	    return "redirect:/vehicle/repairAssistChat?chatSn=" + requestMap.get("chat_sn");
	}
	
	
	/**
	 * AI + 정비 도우미
	 */
	@CustomStat(value = "차량정비", subValue = "AI 정비 도우미", description = "차량정비 - AI + 정비 도우미 채팅 페이지")
	@RequestMapping(value = "/repairAssistChat")
	public String repairAssistChat(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		EgovMap pageInfo = new EgovMap();
		pageInfo.put("pageTitle", "AI + 정비 도우미");
		pageInfo.put("pageMenuName", "vehicle");
		pageInfo.put("pageSubMenuName", "repairAssist");

		authHelper.addAuthToMapAndModel(requestMap, model);
	    
	    if(requestMap.get("chatSn") != null) {
	    	EgovMap chatInfo = repairAssistService.selectChatInfo(requestMap);
	    	model.addAttribute("chatInfo", chatInfo.get("chatInfo"));
	    	model.addAttribute("chatCnList", chatInfo.get("chatCnList"));
	    	Object chatInfo2 = chatInfo.get("chatInfo");
	    	
	    	if(chatInfo2 instanceof Map) {
	    	    requestMap.put("vhclSn", ((Map<String, Object>) chatInfo2).get("vhclSn"));
	    	    EgovMap vehicleInfo = vehicleService.selectVehicleInfo(requestMap);
	    	    model.addAttribute("vehicleInfo", vehicleInfo);
	    	}
	    	
	    }
	    model.addAttribute("apiUrl", apiUrl);
	    model.addAttribute("pageInfo", pageInfo);
	    return "/vehicle/repairAssist/repairAssistChat";
	}
	
	
	/**
	 * AI + 정비 도우미 - 채팅 내용 저장
	 */
	@RequestMapping(value = "/saveChatContent")
	public ResponseEntity<StatusMsg> saveChatContent(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = repairAssistService.insertChatContent(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("실패하였습니다.");
		}
	}
	
	/**
	 * AI + 정비 도우미 - 채팅 내용 피드백 수정
	 */
	@RequestMapping(value = "/updateChatCnFeedBack")
	public ResponseEntity<StatusMsg> updateChatCnFeedBack(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = repairAssistService.updateChatCnFeedBack(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("실패하였습니다.");
		}
	}
}
