package egovframework.theimc.config.login;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import egovframework.theimc.login.service.LoginService;
import egovframework.theimc.util.UserAgentUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginFailHandler implements AuthenticationFailureHandler {
	
	@Resource(name = "loginService")
	private LoginService loginService;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
		
        String mbrId = request.getParameter("mbrId");
        String userCheckForm_flag = request.getParameter("user_check") != null ? 
                                  request.getParameter("user_check") : "";
        String errorMessage = "";
        
        // 접속 정보 수집
        String ipAddr = request.getRemoteAddr();
        String sessionId = request.getSession().getId();
        String userAgent = request.getHeader("User-Agent");
        String brwsrInfo = UserAgentUtils.detectBrwsrInfo(userAgent);
        String deviceType = UserAgentUtils.detectDeviceType(brwsrInfo);
        String osInfo = UserAgentUtils.detectOsInfo(userAgent);
        
        int lgnFailCnt = 0;
        String failRsnCd = "";
        String failRsn = "";
		
		try {
			if(lgnFailCnt < 100) {
				lgnFailCnt = loginService.getLoginFailCount(mbrId)+1;	//로그인실패 횟수 조회
			}
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("mbrId", mbrId);
			paramMap.put("lgnFailCnt", lgnFailCnt);
			
			loginService.updateLoginFailCount(paramMap);	//로그인실패 횟수 +1
			
			// 실패 사유 결정
			if(exception.getMessage() != null && exception.getMessage().contains("Bad credentials")) {
				failRsnCd = "PW";  // 비밀번호 오류
				failRsn = "Password incorrect";
			} else if(exception.getMessage() != null && exception.getMessage().contains("User is disabled")) {
				failRsnCd = "DI";  // 계정 비활성화
				failRsn = "Account disabled";
			} else if(exception.getMessage() != null && exception.getMessage().contains("User account has expired")) {
				failRsnCd = "EX";  // 계정 만료
				failRsn = "Account expired";
			} else {
				failRsnCd = "OT";  // 기타 오류
				failRsn = exception.getMessage();
			}
			
			// 로그인 실패 통계 저장
			HashMap<String, Object> statMap = new HashMap<>();
			statMap.put("mbrId", mbrId);
			statMap.put("actTypeCd", "F");  // F: 실패
			statMap.put("ipAddr", ipAddr);
			statMap.put("userAgent", userAgent);
			statMap.put("brwsrInfo", brwsrInfo);
			statMap.put("sessionId", sessionId);
			statMap.put("deviceType", deviceType);
			statMap.put("osInfo", osInfo);
			statMap.put("failRsnCd", failRsnCd);
			statMap.put("failRsn", failRsn);
			
			loginService.insertLoginStat(statMap);
			
		} catch(DataAccessException e) {
	        if (log.isErrorEnabled()) {
	            log.error("[ERROR] LoginFailHandler onAuthenticationFailure DataAccessException: ", e);
	        }
	    } catch(NullPointerException e) {
	        if (log.isErrorEnabled()) {
	            log.error("[ERROR] LoginFailHandler onAuthenticationFailure NullPointerException: ", e);
	        }
	    } catch(Exception e) {
	        if (log.isErrorEnabled()) {
	            log.error("[ERROR] LoginFailHandler onAuthenticationFailure Exception: ", e);
	        }
	    }
		
		// 오류 메시지 설정
		if(userCheckForm_flag.equals("chk")) {
		    errorMessage = "비밀번호를 확인해주세요";
		} else if(lgnFailCnt == 0) {
		    errorMessage = "로그인 정보를 확인해주세요.";
		} else if(lgnFailCnt > 0 && lgnFailCnt < 5) {
		    errorMessage = "로그인 정보를 확인해주세요. 로그인실패 횟수: " + lgnFailCnt + "회 (5회 실패 시 로그인 제한)";
		} else if(lgnFailCnt >= 5) {
		    errorMessage = "로그인실패 횟수: " + lgnFailCnt + "회. 로그인이 제한되었습니다. 관리자에게 문의하세요.";
		}
		
		// 세션에 정보 저장
		HttpSession session = request.getSession();
		session.setAttribute("loginErrorMessage", errorMessage);
		session.setAttribute("lgnFailCnt", lgnFailCnt);
		session.setAttribute("mbrId", mbrId);
		
		// 리다이렉트
		response.sendRedirect(request.getContextPath() + "/login/loginForm?error=true");
	}
}