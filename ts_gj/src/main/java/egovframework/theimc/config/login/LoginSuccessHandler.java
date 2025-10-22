package egovframework.theimc.config.login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.web.savedrequest.SavedRequest;

import egovframework.theimc.login.service.LoginService;
import egovframework.theimc.login.service.LoginSessionService;
import egovframework.theimc.util.UserAgentUtils;

import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Resource(name = "loginService")
    private LoginService loginService;
    
    @Resource(name = "loginSessionService")
    private LoginSessionService loginSessionService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // application.properties에서 설정한 현재 사용할 인코더 ID
    @Value("${security.password.encoder.id:sha512}")
    private String currentEncoderId;
    
    private RequestCache requestCache = new HttpSessionRequestCache();
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
        String userCheckForm_flag = request.getParameter("user_check") != null ? 
                                  request.getParameter("user_check") : "";
        
        // 회원정보 확인페이지인 경우
        if(userCheckForm_flag.equals("chk")) {
            response.sendRedirect(request.getContextPath() + "/member/userUpdateForm.do");
            return;
        }
        
        // 사용자 정보 가져오기
        Object principal = authentication.getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String mbrId = userDetails.getUsername();
        
        // 권한 목록 가져오기
        List<String> rolenames = new ArrayList<>();
        authentication.getAuthorities().forEach(authority -> {
            rolenames.add(authority.getAuthority());
        });
        
        // 접속 정보 수집
        String ipAddr = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String sessionId = request.getSession().getId();
        String brwsrInfo = UserAgentUtils.detectBrwsrInfo(userAgent);
        String deviceType = UserAgentUtils.detectDeviceType(userAgent);
        String osInfo = UserAgentUtils.detectOsInfo(userAgent);
        
        log.info("####################### 로그인 성공 ########################");
        log.info("사용자: {}, IP: {}, 디바이스: {}, OS: {}", mbrId, ipAddr, deviceType, osInfo);
        
        HashMap<String, Object> statMap = new HashMap<>();
        statMap.put("mbrId", mbrId);
        statMap.put("ipAddr", ipAddr);
        statMap.put("userAgent", userAgent);
        statMap.put("brwsrInfo", brwsrInfo);
        statMap.put("osInfo", osInfo);
        statMap.put("sessionId", sessionId);
        statMap.put("deviceType", deviceType);
        
        try {
            // 로그인 실패 횟수 확인
            int lgnFailCnt = loginService.getLoginFailCount(mbrId);
            
            // 로그인 제한 여부 확인
            if(lgnFailCnt >= 5) {
                String errorMessage = "로그인실패 횟수: " + lgnFailCnt + "회. 로그인이 제한되었습니다. 관리자에게 문의하세요.";
                
                // 로그인 제한 통계 저장 (실패 로그)
                
                statMap.put("actTypeCd", "F"); // F: 실패
                statMap.put("failRsnCd", "MAX"); // 최대 실패 횟수 초과
                statMap.put("failRsn", "Maximum login attempts exceeded");
                loginService.insertLoginStat(statMap);
                
                // 세션에 정보 저장
                HttpSession session = request.getSession();
                session.setAttribute("loginErrorMessage", errorMessage);
                session.setAttribute("lgnFailCnt", lgnFailCnt);
                session.setAttribute("mbrId", mbrId);
                
                response.sendRedirect(request.getContextPath() + "/login/loginForm?error=true");
                return;
            }
            
            // 암호화 알고리즘 업그레이드 확인 및 처리
            try {
                checkAndUpgradePasswordEncoding(mbrId, request.getParameter("mbrPswd"));
            } catch (IllegalArgumentException e) {
                // 비밀번호 업그레이드 실패 시에도 로그인은 계속 진행
            	if (log.isErrorEnabled()) {
            		log.error("[Error] Password upgrade IllegalArgumentException: " + e.getMessage());
            	}
            } catch (Exception e) {
                // 비밀번호 업그레이드 실패 시에도 로그인은 계속 진행
            	if (log.isErrorEnabled()) {
            		log.error("[Error] Password upgrade Exception: " + e.getMessage());
            	}
            }
            
            // 로그인 성공 통계 저장
            statMap.put("actTypeCd", "L"); // L: 로그인
            loginService.insertLoginStat(statMap);
            
            // 로그인 실패 횟수 초기화
            HashMap<String, Object> failCountMap = new HashMap<>();
            failCountMap.put("mbrId", mbrId);
            failCountMap.put("lgnFailCnt", 0);
            loginService.updateLoginFailCount(failCountMap);
            
            boolean isRememberMe = false;
            if (request.getParameter("remember-me") != null) {
                isRememberMe = true;
            }
            
            // 세션 테이블에 정보 저장
            HashMap<String, Object> sessionMap = new HashMap<>();
            sessionMap.put("sessionId", sessionId);
            sessionMap.put("mbrId", mbrId);
            sessionMap.put("ipAddr", ipAddr);
            sessionMap.put("userAgent", userAgent);
            sessionMap.put("brwsrInfo", brwsrInfo);
            sessionMap.put("deviceType", deviceType);
            sessionMap.put("osInfo", osInfo);
            sessionMap.put("rememberMe", isRememberMe ? "Y" : "N");  // Remember-me 여부 저장
            
            // 기존 활성 세션이 있으면 종료 처리
            EgovMap activeSession = loginSessionService.getActiveSessionByMbrId(mbrId);
            if (activeSession != null && activeSession.get("session_id") != null) {
                String oldSessionId = (String) activeSession.get("session_id");
                
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("oldSessionId", oldSessionId);
                paramMap.put("actTypeCd", "E");
                
                if (!oldSessionId.equals(sessionId)) {
                    // 다른 세션이 있으면 종료처리
                    loginSessionService.updateSessionEnd(paramMap);
                    log.info("기존 세션 종료 처리: {}", oldSessionId);
                }
            }
            
            // 새 세션 정보 저장
            int sessionSn = loginSessionService.insertMbrSession(sessionMap);
            log.info("세션 정보 저장 완료: SN={}", sessionSn);
            
            // 세션에 유용한 정보 저장
            HttpSession session = request.getSession();
            session.setAttribute("loginSuccessMessage", "로그인에 성공했습니다.");
            session.setAttribute("mbrId", mbrId);
            session.setAttribute("sessionSn", sessionSn);
            session.setAttribute("userAgent", userAgent);
            session.setAttribute("deviceType", deviceType);
            session.setAttribute("osInfo", osInfo);
            session.setAttribute("ipAddr", ipAddr);
            session.setAttribute("brwsrInfo", brwsrInfo);
            
            // 권한에 따른 리다이렉트
            SavedRequest savedRequest = requestCache.getRequest(request, response);
            if (savedRequest != null) {
                String targetUrl = savedRequest.getRedirectUrl();
                // 로그인 처리 URL 등은 제외
                if (targetUrl != null && !targetUrl.contains("/login/loginProc")) {
                    response.sendRedirect(targetUrl);
                    return;
                }
            } else {
                // 기본 리다이렉트 (권한별 또는 홈으로)
                if (rolenames.contains("ROLE_ADMIN")) {
                    response.sendRedirect(request.getContextPath() + "/");
                } else if (rolenames.contains("ROLE_MECHANIC")) {
                    response.sendRedirect(request.getContextPath() + "/vehicle/diagnosisRealtime");
                } else {
                    response.sendRedirect(request.getContextPath() + "/");
                }
                
                return;
            }
            
            response.sendRedirect(request.getContextPath() + "/");
            
        } catch(IllegalArgumentException e) {
        	if (log.isErrorEnabled()) {
        		log.error("[Error] LoginSuccessHandler IllegalArgumentException : " + e);
        	}
            // 오류 발생 시 통계 저장
            try {
                statMap.put("actTypeCd", "F"); // F: 실패
                statMap.put("failRsnCd", "ERR"); // 시스템 오류
                statMap.put("failRsn", "System error: " + e.getMessage());
                loginService.insertLoginStat(statMap);
            } catch(Exception ex) {
            	if (log.isErrorEnabled()) {
            		log.error("[Error] Failed to log login error: " + ex.getMessage());
            	}
            }
            redirectWithError(request, response, "로그인 처리 중 오류가 발생했습니다.");
        } catch(Exception e) {
        	if (log.isErrorEnabled()) {
        		log.error("[Error] LoginSuccessHandler Exception : " + e);
        	}
            // 오류 발생 시 통계 저장
            try {
                statMap.put("actTypeCd", "F"); // F: 실패
                statMap.put("failRsnCd", "ERR"); // 시스템 오류
                statMap.put("failRsn", "System error: " + e.getMessage());
                loginService.insertLoginStat(statMap);
            } catch(Exception ex) {
                log.error("[Error] Failed to log login error: " + ex.getMessage());
            }
            
            redirectWithError(request, response, "로그인 처리 중 오류가 발생했습니다.");
        }
    }
    
    /**
     * 비밀번호 암호화 알고리즘 체크 및 업그레이드
     * @param mbrId 회원ID
     * @param rawPassword 평문 비밀번호
     */
    private void checkAndUpgradePasswordEncoding(String mbrId, String rawPassword) throws Exception {
        // 사용자의 현재 암호화된 비밀번호 조회
        String encodedPassword = loginService.getMbrPswd(mbrId);
        
        if (encodedPassword == null) {
            log.warn("사용자의 비밀번호를 조회할 수 없습니다: {}", mbrId);
            return;
        }
        
        // 현재 인코딩 방식 확인
        String currentEncoding = extractEncodingType(encodedPassword);
        
        // 인코딩 방식이 일치하지 않으면 업그레이드
        if (!currentEncoding.equals(currentEncoderId)) {
            log.info("비밀번호 암호화 방식 업그레이드: {} -> {}", currentEncoding, currentEncoderId);
            
            // 새로운 암호화 알고리즘으로 비밀번호 인코딩
            String newEncodedPassword = passwordEncoder.encode(rawPassword);
            
            // 비밀번호 업데이트
            HashMap<String, Object> passwordUpdateMap = new HashMap<>();
            passwordUpdateMap.put("mbrId", mbrId);
            passwordUpdateMap.put("mbrPswd", newEncodedPassword);
            loginService.updateMbrPswd(passwordUpdateMap);
            
            log.info("비밀번호 암호화 업그레이드 완료: {}", mbrId);
        }
    }
    
    /**
     * 인코딩된 비밀번호에서 암호화 방식 추출
     * @param encodedPassword 인코딩된 비밀번호
     * @return 암호화 방식 ID (bcrypt, pbkdf2, sha512 등)
     */
    private String extractEncodingType(String encodedPassword) {
        if (encodedPassword == null) {
            return "null";
        }
        
        // Spring Security의 DelegatingPasswordEncoder는 '{id}encodedPassword' 형식 사용
        if (encodedPassword.startsWith("{") && encodedPassword.contains("}")) {
            return encodedPassword.substring(1, encodedPassword.indexOf("}"));
        }
        
        // 접두사가 없는 경우 (레거시 비밀번호) 'null'로 처리
        return "null";
    }
    
    /**
     * 오류 발생 시 메시지와 함께 리다이렉트
     */
    private void redirectWithError(HttpServletRequest request, HttpServletResponse response, String errorMessage) 
            throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute("loginErrorMessage", errorMessage);
        response.sendRedirect(request.getContextPath() + "/login/loginForm?error=true");
    }
}