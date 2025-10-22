package egovframework.theimc.config.login;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("customPersistentTokenRepository")
public class CustomPersistentTokenRepository implements PersistentTokenRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Value("${security.remember-me.token-validity-seconds:604800}")
    private int rememberMeTokenValiditySeconds;
    
    private final String tokensBySeriesSql = "SELECT username, series, token, last_used, session_id FROM persistent_logins WHERE series = ?";
    private final String insertTokenSql = "INSERT INTO persistent_logins (username, series, token, last_used, session_id) VALUES (?, ?, ?, ?, ?)";
    private final String updateTokenSql = "UPDATE persistent_logins SET token = ?, last_used = ? WHERE series = ?";
    private final String removeUserTokensSql = "DELETE FROM persistent_logins WHERE username = ?";
    
    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        // 현재 세션 ID 가져오기
        String sessionId = getSessionId();
        
        jdbcTemplate.update(insertTokenSql, 
                token.getUsername(), 
                token.getSeries(), 
                token.getTokenValue(), 
                token.getDate(),
                sessionId);
        
        // 토큰 생성 시 기존의 만료된 토큰들 정리
        removeExpiredTokens();
    }
    
    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        jdbcTemplate.update(updateTokenSql, tokenValue, lastUsed, series);
    }
    
    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        try {
            return jdbcTemplate.queryForObject(tokensBySeriesSql, (rs, rowNum) -> {
                return new PersistentRememberMeToken(
                        rs.getString(1), // username
                        rs.getString(2), // series
                        rs.getString(3), // token
                        rs.getTimestamp(4) // last_used
                );
            }, seriesId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    @Override
    public void removeUserTokens(String username) {
        // username 기준 삭제는 사용하지 않음
        // jdbcTemplate.update(removeUserTokensSql, username);
    }
    
    /**
     * 세션 ID로 토큰 삭제
     */
    public void removeSessionTokens(String sessionId) {
        if (sessionId != null && !sessionId.isEmpty()) {
            String sql = "DELETE FROM persistent_logins WHERE session_id = ?";
            int deletedCount = jdbcTemplate.update(sql, sessionId);
            
            if (deletedCount > 0) {
                log.info("세션 ID에 해당하는 Remember-me 토큰 삭제: 세션 ID={}, 삭제된 토큰 수={}", 
                        sessionId, deletedCount);
            }
        }
    }
    
    /**
     * 만료된 토큰 삭제
     * @return 삭제된 토큰 수
     */
    public int removeExpiredTokens() {
        try {
            Date expiryDate = new Date(System.currentTimeMillis() - (rememberMeTokenValiditySeconds * 1000L));
            String sql = "DELETE FROM persistent_logins WHERE last_used < ?";
            
            int deletedCount = jdbcTemplate.update(sql, expiryDate);
            
            if (deletedCount > 0) {
                log.info("만료된 Remember-me 토큰 정리: 삭제된 토큰 수={}", deletedCount);
            }
            
            return deletedCount;
        } catch (DataAccessException e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] CustomPersistentTokenRepository removeExpiredTokens DataAccessException: ", e);
            }
        } catch (IllegalArgumentException e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] CustomPersistentTokenRepository removeExpiredTokens IllegalArgumentException: ", e);
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] CustomPersistentTokenRepository removeExpiredTokens Exception: ", e);
            }
        }
        
        return 0;
    }
    
    /**
     * 특정 세션 ID에 대한 만료된 토큰 삭제
     */
    public int removeExpiredTokensForSession(String sessionId) {
        try {
            if (sessionId == null || sessionId.isEmpty()) {
                return 0;
            }
            
            Date expiryDate = new Date(System.currentTimeMillis() - (rememberMeTokenValiditySeconds * 1000L));
            String sql = "DELETE FROM persistent_logins WHERE session_id = ? AND last_used < ?";
            
            int deletedCount = jdbcTemplate.update(sql, sessionId, expiryDate);
            
            if (deletedCount > 0) {
                log.info("세션 ID의 만료된 Remember-me 토큰 삭제: 세션 ID={}, 삭제된 토큰 수={}", 
                        sessionId, deletedCount);
            }
            
            return deletedCount;
        } catch (DataAccessException e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] CustomPersistentTokenRepository removeExpiredTokensForSession DataAccessException: ", e);
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] CustomPersistentTokenRepository removeExpiredTokensForSession Exception: ", e);
            }
        }
        
        return 0;
    }
    
    // 세션 ID 가져오기
    private String getSessionId() {
        try {
            RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
            if (attributes instanceof ServletRequestAttributes) {
                HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
                return request.getSession().getId();
            }
        } catch (IllegalStateException e) {
            if (log.isDebugEnabled()) {
                log.error("[DEBUG] CustomPersistentTokenRepository getSessionId IllegalStateException: 현재 요청 컨텍스트가 없습니다 (배치 처리 중 또는 비-웹 환경)", e);
            }
        } catch (NullPointerException e) {
            if (log.isDebugEnabled()) {
                log.error("[DEBUG] CustomPersistentTokenRepository getSessionId NullPointerException: 요청 또는 세션이 null입니다", e);
            }
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.error("[DEBUG] CustomPersistentTokenRepository getSessionId Exception: ", e);
            }
        }
        return null;
    }
    
    /**
     * 세션 ID로 토큰 조회
     */
    public PersistentRememberMeToken getTokenForSessionId(String sessionId) {
        String sql = "SELECT username, series, token, last_used FROM persistent_logins WHERE session_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                return new PersistentRememberMeToken(
                        rs.getString(1), // username
                        rs.getString(2), // series
                        rs.getString(3), // token
                        rs.getTimestamp(4) // last_used
                );
            }, sessionId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    /**
     * 특정 세션 ID에 대한 토큰의 유효성 확인
     */
    public boolean isTokenValidForSession(String sessionId, int validitySeconds) {
        String sql = "SELECT COUNT(*) FROM persistent_logins " +
                    "WHERE session_id = ? AND last_used > ?";
        Date tokenCutoff = new Date(System.currentTimeMillis() - (validitySeconds * 1000L));
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, sessionId, tokenCutoff);
        return count != null && count > 0;
    }
    
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}