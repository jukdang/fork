package egovframework.theimc.main.web;

import java.util.HashMap;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.theimc.config.login.AuthHelper;

/**
 * 메인
 */
@Controller
public class MainController {

	@Autowired
	private AuthHelper authHelper;
	
	@GetMapping("/")
	public String main(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMapAndModel(requestMap, model);
	    EgovMap pageInfo = new EgovMap();
    	pageInfo.put("pageTitle", "메인 페이지");
    	pageInfo.put("mainTitle", "메인 제목");
    	
    	model.addAttribute("pageInfo", pageInfo);
    	
		return "/main/main";
	}
}