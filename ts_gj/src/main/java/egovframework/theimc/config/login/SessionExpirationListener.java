package egovframework.theimc.config.login;

import java.security.SecureRandom;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import egovframework.theimc.login.service.LoginService;
import egovframework.theimc.login.service.LoginSessionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SessionExpirationListener implements HttpSessionListener {

    @Resource(name = "loginService")
    private LoginService loginService;
    
    @Resource(name = "loginSessionService")
    private LoginSessionService loginSessionService;
    
    @Value("${security.remember-me.token-validity-seconds:604800}")
    private int rememberMeTokenValiditySeconds;
    
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        log.debug("세션 생성됨: {}", se.getSession().getId());
    }

    @Autowired
    private CustomPersistentTokenRepository tokenRepository;
    
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        // 세션 만료 시 세션 테이블에서 종료 처리
        HttpSession session = se.getSession();
        if (session != null) {
            String sessionId = session.getId();
            String mbrId = null;
            Integer sessionSn = null;
            boolean isExplicitLogout = false;
            
            try {
                // 세션에서 필요한 정보 추출
                if (session.getAttribute("mbrId") != null) {
                    mbrId = (String) session.getAttribute("mbrId");
                }
                
                if (session.getAttribute("sessionSn") != null) {
                    sessionSn = (Integer) session.getAttribute("sessionSn");
                }
                
                if (session.getAttribute("isExplicitLogout") != null) {
                    isExplicitLogout = (Boolean) session.getAttribute("isExplicitLogout");
                }
                
                // 명시적 로그아웃이 아닌 경우, remember-me 토큰 유효성 체크
                if (!isExplicitLogout) {
                    // remember-me 토큰이 유효한지 확인
                    boolean hasValidRememberMeToken = checkValidRememberMeToken(sessionId);
                    
                    // 유효한 remember-me 토큰이 있으면 세션 종료 처리를 하지 않음
                    if (hasValidRememberMeToken) {
                        log.info("세션 타임아웃 발생했으나 유효한 Remember-me 토큰이 있어 세션 종료 처리 건너뜀: SessionID={}, MbrID={}", 
                                sessionId, mbrId);
                        return;
                    } else {
                        // 유효하지 않은 remember-me 토큰이 있다면 삭제
                        tokenRepository.removeExpiredTokensForSession(sessionId);
                    }
                } else {
                    // 명시적 로그아웃 시에는 세션 ID로 토큰 삭제
                    tokenRepository.removeSessionTokens(sessionId);
                }
                
                // 주기적으로 전체 만료된 토큰 정리 (5% 확률로 실행)
                if (new SecureRandom().nextDouble() < 0.05) {
                    tokenRepository.removeExpiredTokens();
                }
                
                // 세션 테이블 업데이트 (일련번호로 시도)
                boolean sessionUpdated = false;
                if (sessionSn != null) {
                    HashMap<String, Object> paramMap = new HashMap<>();
                    paramMap.put("sn", sessionSn);
                    paramMap.put("endType", isExplicitLogout ? "O" : "T"); // O: 로그아웃, T: 타임아웃
                    
                    // 명시적 로그아웃인 경우는 항상 처리, 타임아웃인 경우만 remember-me 토큰 체크
                    if (isExplicitLogout) {
                        int result = loginSessionService.updateSessionEndWithoutRememberMeCheck(paramMap);
                        if (result > 0) {
                            log.info("세션 종료 처리 완료 (명시적 로그아웃, SN 사용): {}", sessionSn);
                            sessionUpdated = true;
                        }
                    } else {
                        int result = loginSessionService.updateSessionEndBySn(paramMap);
                        if (result > 0) {
                            log.info("세션 종료 처리 완료 (타임아웃, SN 사용): {}", sessionSn);
                            sessionUpdated = true;
                        }
                    }
                }
                
                // 세션 ID로 시도 (일련번호로 실패한 경우)
                if (!sessionUpdated && sessionId != null) {
                    HashMap<String, Object> paramMap = new HashMap<>();
                    paramMap.put("sessionId", sessionId);
                    paramMap.put("endType", isExplicitLogout ? "O" : "T"); // O: 로그아웃, T: 타임아웃
                    
                    // 명시적 로그아웃인 경우는 항상 처리, 타임아웃인 경우만 remember-me 토큰 체크
                    if (isExplicitLogout) {
                        int result = loginSessionService.updateSessionEndWithoutRememberMeCheck(paramMap);
                        if (result > 0) {
                            log.info("세션 종료 처리 완료 (명시적 로그아웃, 세션 ID 사용): {}", sessionId);
                            sessionUpdated = true;
                        }
                    } else {
                        int result = loginSessionService.updateSessionEnd(paramMap);
                        if (result > 0) {
                            log.info("세션 종료 처리 완료 (타임아웃, 세션 ID 사용): {}", sessionId);
                            sessionUpdated = true;
                        }
                    }
                }
                
                if (!sessionUpdated) {
                    log.warn("세션 종료 처리 실패: 세션을 찾을 수 없음 (ID: {}, MbrID: {})", sessionId, mbrId);
                    return; // 세션 업데이트가 실패했으면 통계도 저장하지 않음
                }
                
                // 로그아웃/타임아웃 통계 저장
                if (mbrId != null && !mbrId.isEmpty()) {
                    String ipAddr = (String) session.getAttribute("ipAddr");
                    String userAgent = (String) session.getAttribute("userAgent");
                    String brwsrInfo = (String) session.getAttribute("brwsrInfo");
                    String deviceType = (String) session.getAttribute("deviceType");
                    String osInfo = (String) session.getAttribute("osInfo");
                    
                    if (ipAddr == null) ipAddr = "Unknown";
                    if (brwsrInfo == null) brwsrInfo = "Unknown";
                    if (deviceType == null) deviceType = "Unknown";
                    
                    HashMap<String, Object> statMap = new HashMap<>();
                    statMap.put("mbrId", mbrId);
                    statMap.put("actTypeCd", isExplicitLogout ? "O" : "T"); // O: 로그아웃, T: 타임아웃
                    statMap.put("ipAddr", ipAddr);
                    statMap.put("userAgent", userAgent);
                    statMap.put("brwsrInfo", brwsrInfo);
                    statMap.put("sessionId", sessionId);
                    statMap.put("deviceType", deviceType);
                    statMap.put("osInfo", osInfo);
                    
                    loginService.insertLoginStat(statMap);
                    log.info("세션 종료 이벤트 기록: 회원ID={}, 유형={}", 
                            mbrId, isExplicitLogout ? "로그아웃" : "타임아웃");
                }
                
            } catch (DataAccessException e) {
                if (log.isErrorEnabled()) {
                    log.error("[ERROR] 클래스명 메서드명 DataAccessException: ", e);
                }
            } catch (ClassCastException e) {
                if (log.isErrorEnabled()) {
                    log.error("[ERROR] 클래스명 메서드명 ClassCastException: ", e);
                }
            } catch (NullPointerException e) {
                if (log.isErrorEnabled()) {
                    log.error("[ERROR] 클래스명 메서드명 NullPointerException: ", e);
                }
            } catch (IllegalArgumentException e) {
                if (log.isErrorEnabled()) {
                    log.error("[ERROR] 클래스명 메서드명 IllegalArgumentException: ", e);
                }
            } catch (IllegalStateException e) {
                if (log.isErrorEnabled()) {
                    log.error("[ERROR] 클래스명 메서드명 IllegalStateException: ", e);
                }
            } catch (ConcurrentModificationException e) {
                if (log.isErrorEnabled()) {
                    log.error("[ERROR] 클래스명 메서드명 ConcurrentModificationException: ", e);
                }
            } catch (SecurityException e) {
                if (log.isErrorEnabled()) {
                    log.error("[ERROR] 클래스명 메서드명 SecurityException: ", e);
                }
            } catch (UnsupportedOperationException e) {
                if (log.isErrorEnabled()) {
                    log.error("[ERROR] 클래스명 메서드명 UnsupportedOperationException: ", e);
                }
            } catch (RuntimeException e) {
                if (log.isErrorEnabled()) {
                    log.error("[ERROR] 클래스명 메서드명 RuntimeException: ", e);
                }
            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error("[ERROR] 클래스명 메서드명 Exception: ", e);
                }
            }
        }
    }
    
    /**
     * 특정 세션 ID에 대한 유효한 Remember-me 토큰이 있는지 확인
     */
    private boolean checkValidRememberMeToken(String sessionId) {
        try {
            return tokenRepository.isTokenValidForSession(sessionId, rememberMeTokenValiditySeconds);
        } catch (DataAccessException e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] 클래스명 checkValidRememberMeToken DataAccessException: ", e);
            }
            return false;
        } catch (IllegalArgumentException e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] 클래스명 checkValidRememberMeToken IllegalArgumentException: ", e);
            }
            return false;
        } catch (NullPointerException e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] 클래스명 checkValidRememberMeToken NullPointerException: ", e);
            }
            return false;
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] 클래스명 checkValidRememberMeToken Exception: ", e);
            }
            return false;
        }
    }
}