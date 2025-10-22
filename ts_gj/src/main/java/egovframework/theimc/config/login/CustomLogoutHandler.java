package egovframework.theimc.config.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomLogoutHandler implements LogoutHandler {
    
    @Autowired
    @Qualifier("customPersistentTokenRepository")
    private CustomPersistentTokenRepository tokenRepository;
    
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            // 세션에 명시적 로그아웃 플래그 설정 - 세션 리스너에서 사용
            session.setAttribute("isExplicitLogout", true);
            
            String sessionId = session.getId();
            log.info("로그아웃 처리 시작: 세션 ID={}", sessionId);
            
            // 세션 ID를 기준으로 Remember Me 토큰 삭제
            tokenRepository.removeSessionTokens(sessionId);
            log.info("세션 ID 기준으로 Remember Me 토큰 삭제 완료: {}", sessionId);
            
            // 세션 정보가 세션에 없는 경우를 대비해 현재 요청의 정보 저장
            if (session.getAttribute("ipAddr") == null) {
                session.setAttribute("ipAddr", request.getRemoteAddr());
            }
            
            if (session.getAttribute("brwsrInfo") == null) {
                session.setAttribute("brwsrInfo", request.getHeader("User-Agent"));
            }
            
            // Authentication 객체에서 사용자 ID 가져오기
            if (authentication != null && session.getAttribute("mbrId") == null) {
                session.setAttribute("mbrId", authentication.getName());
            }
        } else {
            log.warn("로그아웃 처리: 세션이 이미 없음");
        }
        
        // 이후 Spring Security의 기본 로그아웃 처리가 진행됨
    }
}