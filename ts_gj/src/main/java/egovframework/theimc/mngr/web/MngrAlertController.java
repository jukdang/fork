package egovframework.theimc.mngr.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.theimc.config.login.AuthHelper;
import egovframework.theimc.login.vo.CustomUserDetails;
import egovframework.theimc.mngr.service.MngrAlertService;
import egovframework.theimc.mngr.service.MngrStatService;
import egovframework.theimc.util.StatusMsg;

/**
 * 관리자
 */
@Controller
@RequestMapping(value="/mngr")
public class MngrAlertController {
	
	@Autowired
	private AuthHelper authHelper;
	
	@Autowired
	private MngrAlertService mngrAlertService;
	
	/**
	 * 알림 수신관리
	 */
	@RequestMapping(value = "/alertManage")
	public String alertManage(@RequestParam HashMap<String, Object> requestMap, ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
		authHelper.addAuthToMapAndModel(requestMap, model);
		EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "알림 수신관리");
	    pageInfo.put("pageMenuName", "alert");
	    pageInfo.put("pageSubMenuName", "alertManage");
	    
    	if(!(getClientIp(request).equals(requestMap.get("prvcIpAddr")))) {
    		redirectAttributes.addFlashAttribute("message", "허용된 아이피만 이용이 가능합니다.");
    		return "redirect:/mngr/mbr/mbrList";
    	}
	    	    
	    model.addAttribute("pageInfo", pageInfo);
	    return "/mngr/alert/alertManage";
	}
	
	/**
	 * 알림 수신관리 - 수신자 목록
	 */
	@RequestMapping(value = "/getRecipientList")
	public ResponseEntity<StatusMsg> getRecipientList(@RequestParam HashMap<String, Object> requestMap, HttpServletRequest request) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = mngrAlertService.selectRecipientList(requestMap, request);
		
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	
	/**
	 * 알림 수신관리 - 등록 페이지
	 */
	@RequestMapping(value = "/alertReceptionInsertForm")
	public String rfidOprtrInsertForm(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMapAndModel(requestMap, model);
	    EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "알림 수신관리");
	    pageInfo.put("pageMenuName", "alert");
	    pageInfo.put("pageSubMenuName", "alertManage");
	    
	    model.addAttribute("pageInfo", pageInfo);
	    return "/mngr/alert/alertReceptionInsertForm";
	}
	
    /**
     *  알림 수신관리 - 등록
     */
    @PostMapping(value = "/alertReceptionInsert")
    public String alertReceptionInsert(@RequestParam HashMap<String, Object> requestMap, ModelMap model, RedirectAttributes redirectAttributes) throws Exception {
    	authHelper.addAuthToMapAndModel(requestMap, model);
    	int result = mngrAlertService.insertRecipient(requestMap);
    	
    	if(result == 1) {
    		redirectAttributes.addFlashAttribute("message", "수신자 등록에 성공하셨습니다.");
    	} else if(result == 2) {
    		redirectAttributes.addFlashAttribute("message", "차량 진단 알림은 최대 3명 등록 할 수 있습니다.");
    	} else if(result == 3) {
    		redirectAttributes.addFlashAttribute("message", "AI 리포트 알림은 최대 3명 등록 할 수 있습니다.");
    	} else {
    		redirectAttributes.addFlashAttribute("message", "오류가 발생했습니다.");
    	}
        return "redirect:/mngr/alertManage";
    }
	
	/**
	* 알림 수신관리 - 삭제
	*/
	@RequestMapping(value = "/deleteRecipientList", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<StatusMsg> deleteRecipientList(@RequestParam HashMap<String, Object> requestMap) throws Exception {
        String selectedSn = (String) requestMap.get("selectedSn");
        if (selectedSn != null && !selectedSn.trim().isEmpty()) {
            String[] rcvrSnsArray = selectedSn.split(",");
            List<String> rcvrSns = Arrays.asList(rcvrSnsArray);
            requestMap.put("rcvrSns", rcvrSns);
            int result = mngrAlertService.deleteRecipientList(requestMap);
            
            if (result > 0) {
    			return StatusMsg.ok(result);
    		}else {
    			return StatusMsg.fail("조회 결과가 없습니다.");
    		}
		}
        return StatusMsg.fail("조회 결과가 없습니다.");
	}
    
	/**
	 * 알림 수신관리 - 수정 페이지
	 */
	@RequestMapping(value = "/alertReceptionUpdateForm")
	public String alertReceptionUpdateForm(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMapAndModel(requestMap, model);
	    EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "알림 수신관리");
	    pageInfo.put("pageMenuName", "alert");
	    pageInfo.put("pageSubMenuName", "alertManage");
	    EgovMap recipient = mngrAlertService.selectRecipient(requestMap);
	    model.addAttribute("pageInfo", pageInfo);
	    model.addAttribute("selOwnr", recipient.get("ownrNm"));
	    model.addAttribute("recipient", recipient);
	    return "/mngr/alert/alertReceptionUpdateForm";
	}
	
    /**
     *  알림 수신관리 - 등록
     */
    @PostMapping(value = "/alertReceptionUpdate")
    public String alertReceptionUpdate(@RequestParam HashMap<String, Object> requestMap, ModelMap model, RedirectAttributes redirectAttributes) throws Exception {
    	authHelper.addAuthToMapAndModel(requestMap, model);
    	int result = mngrAlertService.updateRecipient(requestMap);
    	
    	if(result == 1) {
    		redirectAttributes.addFlashAttribute("message", "수신자 수정에 성공하셨습니다.");
    	} else if(result == 2) {
    		redirectAttributes.addFlashAttribute("message", "차량 진단 알림은 최대 3명 등록 할 수 있습니다.");
    	} else if(result == 3) {
    		redirectAttributes.addFlashAttribute("message", "AI 리포트 알림은 최대 3명 등록 할 수 있습니다.");
    	} else {
    		redirectAttributes.addFlashAttribute("message", "오류가 발생했습니다.");
    	}
        return "redirect:/mngr/alertManage";
    }
	
	/**
	 * 알림 내역
	 */
	@RequestMapping(value = "/alertHistory")
	public String alertHistory(@RequestParam HashMap<String, Object> requestMap, ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
		authHelper.addAuthToMapAndModel(requestMap, model);
		EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "알림 내역");
	    pageInfo.put("pageMenuName", "alert");
	    pageInfo.put("pageSubMenuName", "alertHistory");
	    
    	if(!(getClientIp(request).equals(requestMap.get("prvcIpAddr")))) {
    		redirectAttributes.addFlashAttribute("message", "허용된 아이피만 이용이 가능합니다.");
    		return "redirect:/mngr/mbr/mbrList";
    	}
	    
	    model.addAttribute("pageInfo", pageInfo);
	    return "/mngr/alert/alertHistory";
	}
	
	/**
	 * 알림 내역 - 목록 조회
	 */
	@RequestMapping(value = "/getAlertReceptionHstoryList")
	public ResponseEntity<StatusMsg> getAlertReceptionHstoryList(@RequestParam HashMap<String, Object> requestMap, HttpServletRequest request) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = mngrAlertService.selectAlertReceptionHstoryList(requestMap, request);
		
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	
	private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
	
}