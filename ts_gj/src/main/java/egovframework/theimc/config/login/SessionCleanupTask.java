package egovframework.theimc.config.login;

import java.sql.SQLException;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import javax.annotation.Resource;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import egovframework.theimc.login.service.LoginService;
import egovframework.theimc.login.service.LoginSessionService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SessionCleanupTask {
    @Resource(name = "loginSessionService")
    private LoginSessionService loginSessionService;
    
    @Resource(name = "loginService")
    private LoginService loginService;
    
    @Autowired
    @Qualifier("customPersistentTokenRepository")
    private CustomPersistentTokenRepository tokenRepository;
    
    @Value("${security.remember-me.token-validity-seconds:1200}")
    private int rememberMeTokenValiditySeconds;
    
    @Value("${server.servlet.session.timeout:30m}")
    private String sessionTimeout;
    
    // 5분마다 실행 (밀리초: 5 * 60 * 1000)
    @Scheduled(fixedRate = 300000)
    public void cleanupStaleSessions() {
        try {
            log.info("세션 및 Remember-me 토큰 정리 작업 시작");
            
            // 1. 만료된 Remember-me 토큰 강제 정리
            cleanupExpiredRememberMeTokens();
            
            int timeoutMinutes = parseSessionTimeout(sessionTimeout);
            
            // 2. 30분 이상 활동이 없는 활성 세션 찾기
            List<EgovMap> staleSessions = loginSessionService.findInactiveSessions(timeoutMinutes);
            
            if (staleSessions != null && !staleSessions.isEmpty()) {
                int count = 0;
                for (EgovMap session : staleSessions) {
                    Integer sn = (Integer) session.get("sn");
                    String sessionId = (String) session.get("sessionId");
                    String mbrId = (String) session.get("mbrId");
                    String ipAddr = (String) session.get("ipAddr");
                    String userAgent = (String) session.get("userAgent");
                    String brwsrInfo = (String) session.get("brwsrInfo");
                    String deviceType = (String) session.get("deviceType");
                    String osInfo = (String) session.get("osInfo");
                    
                    // 세션 ID로 유효한 Remember-me 토큰이 있는지 확인
                    boolean hasValidToken = false;
                    if (sessionId != null && !sessionId.isEmpty()) {
                        hasValidToken = tokenRepository.isTokenValidForSession(sessionId, rememberMeTokenValiditySeconds);
                    }
                    
                    // 유효한 토큰이 없는 경우에만 세션 종료 처리
                    if (!hasValidToken) {
                        // 비정상 종료 세션으로 처리
                        HashMap<String, Object> paramMap = new HashMap<>();
                        paramMap.put("sn", sn);
                        paramMap.put("endType", "S"); // S: 세션타임아웃 외 스케쥴로 인한 정리
                        
                        loginSessionService.updateSessionEndWithType(paramMap);
                        
                        // 로그인 통계 테이블에 직접 기록
                        HashMap<String, Object> statMap = new HashMap<>();
                        statMap.put("mbrId", mbrId);
                        statMap.put("actTypeCd", "S"); // S: 세션타임아웃 외 스케쥴로 인한 정리
                        statMap.put("ipAddr", ipAddr != null ? ipAddr : "Unknown");
                        statMap.put("userAgent", userAgent != null ? userAgent : "Unknown");
                        statMap.put("brwsrInfo", brwsrInfo != null ? brwsrInfo : "Unknown");
                        statMap.put("sessionId", sessionId);
                        statMap.put("deviceType", deviceType != null ? deviceType : "Unknown");
                        statMap.put("osInfo", osInfo != null ? osInfo : "Unknown");
                        loginService.insertLoginStat(statMap);
                        
                        count++;
                    } else {
                        log.debug("세션 ID {}는 유효한 Remember-me 토큰이 있어 유지됨", sessionId);
                    }
                }
                
                log.info("비활성 세션 정리 완료: {}개 세션 처리됨", count);
            } else {
                log.info("처리할 비활성 세션이 없음");
            }
        }catch (DataAccessException e) {
                if (log.isErrorEnabled()) {
                    log.error("[ERROR] SessionCleanupTask cleanupStaleSessions DataAccessException: ", e);
                }
            } catch (IllegalArgumentException e) {
                if (log.isErrorEnabled()) {
                    log.error("[ERROR] SessionCleanupTask cleanupStaleSessions IllegalArgumentException: ", e);
                }
            } catch (NullPointerException e) {
                if (log.isErrorEnabled()) {
                    log.error("[ERROR] SessionCleanupTask cleanupStaleSessions NullPointerException: ", e);
                }
            } catch (SecurityException e) {
                if (log.isErrorEnabled()) {
                    log.error("[ERROR] SessionCleanupTask cleanupStaleSessions SecurityException: ", e);
                }
            } catch (UnsupportedOperationException e) {
                if (log.isErrorEnabled()) {
                    log.error("[ERROR] SessionCleanupTask cleanupStaleSessions UnsupportedOperationException: ", e);
                }
            } catch (ConcurrentModificationException e) {
                if (log.isErrorEnabled()) {
                    log.error("[ERROR] SessionCleanupTask cleanupStaleSessions ConcurrentModificationException: ", e);
                }
            } catch (IllegalStateException e) {
                if (log.isErrorEnabled()) {
                    log.error("[ERROR] SessionCleanupTask cleanupStaleSessions IllegalStateException: ", e);
                }
            } catch (RuntimeException e) {
                if (log.isErrorEnabled()) {
                    log.error("[ERROR] SessionCleanupTask cleanupStaleSessions RuntimeException: ", e);
                }
            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error("[ERROR] SessionCleanupTask cleanupStaleSessions Exception: ", e);
                }
            }
    }
    
    /**
     * 세션 타임아웃 문자열을 분 단위로 파싱
     * @param timeout 타임아웃 문자열 (예: "30m", "1h")
     * @return 분 단위 타임아웃 값
     */
    private int parseSessionTimeout(String timeout) {
        // 기본값 30분
        int minutes = 30;
        
        try {
            if (timeout != null && !timeout.isEmpty()) {
                // 숫자 부분과 단위 부분 분리
                String numericPart = timeout.replaceAll("[^0-9]", "");
                String unitPart = timeout.replaceAll("[0-9]", "").toLowerCase();
                
                int value = Integer.parseInt(numericPart);
                
                // 단위에 따라 분으로 변환
                switch (unitPart) {
                    case "h":  // 시간
                        minutes = value * 60;
                        break;
                    case "s":  // 초
                        minutes = value / 60;
                        if (minutes < 1) minutes = 1;  // 최소 1분
                        break;
                    case "m":  // 분
                    default:   // 단위가 없으면 분으로 간주
                        minutes = value;
                        break;
                }
            }
        } catch (NumberFormatException e) {
            if (log.isWarnEnabled()) {
                log.warn("[WARN] SessionCleanupTask parseSessionTimeout NumberFormatException: ", e);
            }
        } catch (NullPointerException e) {
            if (log.isWarnEnabled()) {
                log.warn("[WARN] SessionCleanupTask parseSessionTimeout NullPointerException: ", e);
            }
        } catch (PatternSyntaxException e) {
            if (log.isWarnEnabled()) {
                log.warn("[WARN] SessionCleanupTask parseSessionTimeout PatternSyntaxException: ", e);
            }
        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("[WARN] SessionCleanupTask parseSessionTimeout Exception: ", e);
            }
        }
        
        log.debug("세션 타임아웃 설정값: {}, 파싱된 분 단위: {}", timeout, minutes);
        return minutes;
    }
    
    /**
     * 만료된 Remember-me 토큰 정리 및 관련 세션 처리
     */
    private void cleanupExpiredRememberMeTokens() {
        try {
            // 1. 만료 기준 시간 계산
            Date expiryDate = new Date(System.currentTimeMillis() - (rememberMeTokenValiditySeconds * 1000L));
            
            // 2. 만료된 토큰 목록 조회 (삭제하기 전에 정보 저장)
            String findExpiredTokensSql = "SELECT username, series, token, last_used, session_id FROM persistent_logins WHERE last_used < ?";
            List<EgovMap> expiredTokens = tokenRepository.getJdbcTemplate().query(
                findExpiredTokensSql, 
                (rs, rowNum) -> {
                    EgovMap map = new EgovMap();
                    map.put("username", rs.getString("username"));
                    map.put("series", rs.getString("series"));
                    map.put("session_id", rs.getString("session_id"));
                    map.put("last_used", rs.getTimestamp("last_used"));
                    return map;
                },
                expiryDate
            );
            
            // 3. 만료된 토큰 삭제
            String deleteExpiredTokensSql = "DELETE FROM persistent_logins WHERE last_used < ?";
            int deletedCount = tokenRepository.getJdbcTemplate().update(deleteExpiredTokensSql, expiryDate);
            
            if (deletedCount > 0) {
                log.info("만료된 Remember-me 토큰 정리: {}개 삭제됨", deletedCount);
                
                // 4. 삭제된 토큰의 세션에 대한 처리
                for (EgovMap token : expiredTokens) {
                    String username = (String) token.get("username");
                    String sessionId = (String) token.get("session_id");
                    
                    if (sessionId != null && !sessionId.isEmpty()) {
                        // 세션 테이블에서 해당 세션 ID로 정보 조회
                        String findSessionSql = "SELECT sn, mbr_id, ip_addr, brwsr_info, device_type FROM svc_mbr_session WHERE session_id = ? AND actv_yn = 'Y'";
                        try {
                            EgovMap sessionInfo = tokenRepository.getJdbcTemplate().queryForObject(
                                findSessionSql,
                                (rs, rowNum) -> {
                                    EgovMap map = new EgovMap();
                                    map.put("sn", rs.getInt("sn"));
                                    map.put("mbr_id", rs.getString("mbr_id"));
                                    map.put("ip_addr", rs.getString("ip_addr"));
                                    map.put("brwsr_info", rs.getString("brwsr_info"));
                                    map.put("device_type", rs.getString("device_type"));
                                    return map;
                                },
                                sessionId
                            );
                            
                            if (sessionInfo != null) {
                                Integer sn = (Integer) sessionInfo.get("sn");
                                String mbrId = (String) sessionInfo.get("mbrId");
                                String ipAddr = (String) sessionInfo.get("ipAddr");
                                String userAgent = (String) sessionInfo.get("userAgent");
                                String brwsrInfo = (String) sessionInfo.get("brwsrInfo");
                                String deviceType = (String) sessionInfo.get("deviceType");
                                String osInfo = (String) sessionInfo.get("osInfo");
                                
                                // 세션 종료 처리
                                HashMap<String, Object> paramMap = new HashMap<>();
                                paramMap.put("sn", sn);
                                paramMap.put("endType", "R"); // R: Remember-me 토큰 만료
                                
                                loginSessionService.updateSessionEndWithType(paramMap);
                                
                                // 로그인 통계 테이블에 기록
                                HashMap<String, Object> statMap = new HashMap<>();
                                statMap.put("mbrId", mbrId != null ? mbrId : username);
                                statMap.put("actTypeCd", "R"); // R: Remember-me 토큰 만료
                                statMap.put("ipAddr", ipAddr != null ? ipAddr : "Unknown");
                                statMap.put("userAgent", userAgent != null ? userAgent : "Unknown");
                                statMap.put("brwsrInfo", brwsrInfo != null ? brwsrInfo : "Unknown");
                                statMap.put("sessionId", sessionId);
                                statMap.put("deviceType", deviceType != null ? deviceType : "Unknown");
                                statMap.put("osInfo", osInfo != null ? osInfo : "Unknown");
                                loginService.insertLoginStat(statMap);
                                
                                log.info("Remember-me 토큰 만료로 세션 종료 처리: 세션 ID={}, 사용자={}", sessionId, username);
                            }
                        } catch (DataAccessException e) {
                            if (log.isErrorEnabled()) {
                                log.error("[ERROR] SessionCleanupTask cleanupExpiredRememberMeTokens DataAccessException: ", e);
                            }
                        } catch (IllegalArgumentException e) {
                            if (log.isErrorEnabled()) {
                                log.error("[ERROR] SessionCleanupTask cleanupExpiredRememberMeTokens IllegalArgumentException: ", e);
                            }
                        } catch (NullPointerException e) {
                            if (log.isErrorEnabled()) {
                                log.error("[ERROR] SessionCleanupTask cleanupExpiredRememberMeTokens NullPointerException: ", e);
                            }
                        } catch (SecurityException e) {
                            if (log.isErrorEnabled()) {
                                log.error("[ERROR] SessionCleanupTask cleanupExpiredRememberMeTokens SecurityException: ", e);
                            }
                        } catch (SQLException e) {
                            if (log.isErrorEnabled()) {
                                log.error("[ERROR] SessionCleanupTask cleanupExpiredRememberMeTokens SQLException: ", e);
                            }
                        } catch (RuntimeException e) {
                            if (log.isErrorEnabled()) {
                                log.error("[ERROR] SessionCleanupTask cleanupExpiredRememberMeTokens RuntimeException: ", e);
                            }
                        } catch (Exception e) {
                            if (log.isErrorEnabled()) {
                                log.error("[ERROR] SessionCleanupTask cleanupExpiredRememberMeTokens Exception: ", e);
                            }
                        }
                    }
                }
            } else {
                log.debug("만료된 Remember-me 토큰 없음");
            }
        } catch (DataAccessException e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] SessionCleanupTask cleanupExpiredRememberMeTokens DataAccessException: ", e);
            }
        } catch (IllegalArgumentException e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] SessionCleanupTask cleanupExpiredRememberMeTokens IllegalArgumentException: ", e);
            }
        } catch (NullPointerException e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] SessionCleanupTask cleanupExpiredRememberMeTokens NullPointerException: ", e);
            }
        } catch (SecurityException e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] SessionCleanupTask cleanupExpiredRememberMeTokens SecurityException: ", e);
            }
        } catch (RuntimeException e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] SessionCleanupTask cleanupExpiredRememberMeTokens RuntimeException: ", e);
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] SessionCleanupTask cleanupExpiredRememberMeTokens Exception: ", e);
            }
        }
    }
}