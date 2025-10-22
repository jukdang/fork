package egovframework.theimc.config.login;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import egovframework.theimc.login.service.LoginSessionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SessionActivityFilter implements Filter {

    @Resource(name = "loginSessionService")
    private LoginSessionService loginSessionService;
    
    // 세션 활동 업데이트 간격 (1분 = 60000ms)
    private static final long UPDATE_INTERVAL = 60000;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);
        
        if (session != null) {
            String sessionId = session.getId();
            Date lastUpdate = (Date) session.getAttribute("lastActivityUpdate");
            Date now = new Date();
            
            // 마지막 업데이트 시간이 없거나 지정된 간격보다 오래된 경우에만 업데이트
            if (lastUpdate == null || (now.getTime() - lastUpdate.getTime() > UPDATE_INTERVAL)) {
                try {
                    loginSessionService.updateLastActivityTime(sessionId);
                    session.setAttribute("lastActivityUpdate", now);
                } catch (DataAccessException e) {
                    if (log.isErrorEnabled()) {
                        log.error("[ERROR] SessionFilter doFilter DataAccessException: ", e);
                    }
                } catch (NullPointerException e) {
                    if (log.isErrorEnabled()) {
                        log.error("[ERROR] SessionFilter doFilter NullPointerException: ", e);
                    }
                } catch (IllegalArgumentException e) {
                    if (log.isErrorEnabled()) {
                        log.error("[ERROR] SessionFilter doFilter IllegalArgumentException: ", e);
                    }
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("[ERROR] SessionFilter doFilter Exception: ", e);
                    }
                }
            }
        }
        
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}