package egovframework.theimc.config.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class RequestSizeLimitFilter implements Filter {
    private static final int MAX_PARAM_LENGTH = 1000;
    // 제외할 URL 패턴들
    private static final String[] EXCLUDED_URLS = {
        "/influx/getSensorHistory",
        "/influx/",  // influx로 시작하는 모든 URL
        "/vehicle/saveChatContent"
    };
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        
        // 제외 URL 확인
        boolean isExcluded = false;
        for (String excludedUrl : EXCLUDED_URLS) {
            if (requestURI.contains(excludedUrl)) {
                isExcluded = true;
                break;
            }
        }
        
        // 제외된 URL이면 검사하지 않고 바로 통과
        if (isExcluded) {
            chain.doFilter(request, response);
            return;
        }
        
        // 나머지는 기존 로직대로 파라미터 길이 검사
        Map<String, String[]> parameterMap = httpRequest.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            for (String value : values) {
                if (value != null && value.length() > MAX_PARAM_LENGTH) {
                    httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, 
                            "Request parameter '" + key + "' is too long.");
                    return;
                }
            }
        }
        
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 종료 시 리소스 정리 필요 시 작성
    }
}
