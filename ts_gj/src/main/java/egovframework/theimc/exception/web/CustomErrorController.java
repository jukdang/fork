package egovframework.theimc.exception.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

/**
 * CustomErrorController
 * 
 * 작성일: 2025-04-17
 * 
 * 설명: 
 * 이 클래스는 모든 오류를 하나의 통일된 에러 페이지로 처리합니다.
 * HTTP 상태 코드에 관계없이 동일한 에러 페이지가 표시됩니다.
 */
@Controller
@Slf4j
public class CustomErrorController implements ErrorController {

    /**
     * 에러 처리 핸들러 메서드
     * 모든 오류를 하나의 에러 페이지로 처리합니다.
     * 
     * @param request HTTP 요청 객체
     * @param model 뷰에 전달할 모델 객체
     * @return 에러 페이지의 뷰 이름
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // 에러 상태 코드 가져오기 (로깅 목적으로만 사용)
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String requestUri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        String queryString = request.getQueryString();
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            // 에러 정보 로깅
            if (log.isErrorEnabled()) {
            	log.error("[ERROR] status code: {}", statusCode);
            	log.error("[ERROR] request URI: {}", requestUri);
                log.error("[ERROR] query string: {}", queryString);
            }
        }
        
        // 예외 정보 가져오기
        Throwable throwable = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        if (throwable != null) {
            // AOP에서 이미 로깅되었을 가능성이 높으므로 여기서는 간단하게만 로깅
        	if (log.isErrorEnabled()) {
        		//log.error("[ERROR] handleError : {}", throwable.getMessage());
        	}
        }
        
        // 고정된 에러 메시지 설정
        model.addAttribute("errorTitle", "오류가 발생했습니다");
        model.addAttribute("errorMessage", "요청하신 작업을 처리하는 중 오류가 발생했습니다.");
        
        // 모든 에러를 동일한 페이지로 처리 (Thymeleaf 템플릿)
        return "/error/error";
    }
}