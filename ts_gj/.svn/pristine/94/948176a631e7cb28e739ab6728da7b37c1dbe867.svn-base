package egovframework.theimc.login.web;

import javax.servlet.http.HttpSession;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

/**
 * 로그인 관련 요청을 처리하는 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping(value="/login")
public class LoginController {

    /**
     * 로그인 폼 페이지를 보여주는 메서드
     * @return login 템플릿 페이지
     */
    @RequestMapping(value = "/loginForm")
    public String loginForm(HttpSession session, Model model) throws Exception {
    	EgovMap pageInfo = new EgovMap();
    	pageInfo.put("pageTitle", "로그인 페이지");
    	
    	// 로그인한 경우
		if (isAuthenticated()) {
			return "redirect:/";
		}

		String loginErrorMessage = (String) session.getAttribute("loginErrorMessage");
	    if (loginErrorMessage != null) {
	        model.addAttribute("loginErrorMessage", loginErrorMessage);
	        session.removeAttribute("loginErrorMessage"); // 여기서 세션 값 제거
	    }
		
	    model.addAttribute("pageInfo", pageInfo);
        return "/login/loginForm";
    }
	
	/*
	 * 로그인 여부
	 * */
	private boolean isAuthenticated() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
	        return false;
	    }
	    return authentication.isAuthenticated();
	}
	
    
    /**
     * 마이페이지
     */
    @RequestMapping(value = "/mypage")
    public String mypage(HttpSession session, Model model) throws Exception {
    	EgovMap pageInfo = new EgovMap();
    	pageInfo.put("pageTitle", "마이페이지");
    	
	    model.addAttribute("pageInfo", pageInfo);
        return "/login/mypage";
    }
}