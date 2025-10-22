package egovframework.theimc.config.interceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.theimc.login.vo.CustomUserDetails;
import egovframework.theimc.util.CryptoUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StatisticsInterceptor implements HandlerInterceptor {

    private final SqlSession sqlSession;

    public StatisticsInterceptor(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            // CustomStat 어노테이션이 있는지 확인
            CustomStat stat = handlerMethod.getMethodAnnotation(CustomStat.class);
            if (stat != null) {
                // 요청 시작 시간 저장
                request.setAttribute("startTime", System.currentTimeMillis());
                request.setAttribute("statAnnotation", stat);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) {
        if (handler instanceof HandlerMethod && request.getAttribute("startTime") != null) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            CustomStat stat = (CustomStat) request.getAttribute("statAnnotation");

            if (stat != null) {
                long startTime = (Long) request.getAttribute("startTime");
                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;

                // 현재 로그인한 사용자 정보 가져오기
                String userId = "";
                String userRole = "";
                String ownrNm = "";
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                    CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
                    userId = auth.getName();
                    ownrNm = userDetails.getOwnrNm();
                    userRole = auth.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(","));
                }

                // 통계 데이터 생성
                Map<String, Object> params = new HashMap<>();

                String statValue = stat.value();
                String statValue2 = stat.subValue();
                // 파라미터를 JSON으로 변환
                String reqParamsJson = convertRequestParamsToJson(request);
                // 요청 속성에서 카테고리 정보 가져오기
                Object value2 = request.getAttribute("value2");
                if (value2 != null) {
                    statValue = statValue + "_" + value2.toString();
                }
                params.put("svcNm", statValue);
                params.put("svcNm2", statValue2);
                params.put("ctrlNm", handlerMethod.getBeanType().getSimpleName());
                params.put("mthdNm", handlerMethod.getMethod().getName());
                params.put("ownrNm", ownrNm);
                params.put("mbrId", userId);
                params.put("mbrRole", userRole);
                params.put("processTime", executionTime);
                params.put("actTypeCd", ex == null ? "S" : "E"); // S: 성공, E: 오류
                params.put("reqUrl", request.getRequestURI());
                params.put("reqInfo", stat.description());
                params.put("reqParams", reqParamsJson);
                params.put("ipAddr", getClientIp(request));
                params.put("brwsrInfo", request.getHeader("User-Agent"));
                params.put("sessionId", request.getSession().getId());
                params.put("deviceType", getDeviceType(request));

                if (ex != null) {
                    params.put("errCd", ex.getClass().getSimpleName());
                    params.put("errMsg",
                            ex.getMessage() != null
                                    ? ex.getMessage().substring(0, Math.min(ex.getMessage().length(), 2000))
                                    : "");
                }

                // MyBatis로 통계 데이터 저장
                sqlSession.insert("egovframework.theimc.stat.service.impl.StatMapper.insertStat", params);
            }
        }
    }

    /**
     * 요청 파라미터를 JSON 문자열로 변환
     */
    private String convertRequestParamsToJson(HttpServletRequest request) {
        Map<String, Object> paramMap = new HashMap<>();

        // 1. GET 및 form-data POST 파라미터 처리
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (!parameterMap.isEmpty()) {
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String key = entry.getKey();
                String[] values = entry.getValue();

                // 민감한 정보 마스킹
                if (key.toLowerCase().contains("pswd")) {
                    paramMap.put(key, "******");
                }
                // sn이나 fileSn 파라미터는 복호화 처리
                else if (key.equals("sn") || key.equals("fileSn")) {
                    if (values.length == 1) {
                        try {
                            // 복호화 처리
                            String decryptedValue = CryptoUtils.decrypt(values[0]);
                            paramMap.put(key, decryptedValue);
                        } catch (IllegalArgumentException e) {
                            if (log.isErrorEnabled()) {
                                String exceptionType = e.getClass().getSimpleName();
                                log.error("[ERROR] StatisticsInterceptor convertRequestParamsToJson1 {}: ",
                                        exceptionType, e);
                            }
                            // 복호화 실패 시 원본 값 사용
                            paramMap.put(key, values[0]);
                        } catch (Exception e) {
                            if (log.isErrorEnabled()) {
                                String exceptionType = e.getClass().getSimpleName();
                                log.error("[ERROR] StatisticsInterceptor convertRequestParamsToJson2 {}: ",
                                        exceptionType, e);
                            }
                            // 복호화 실패 시 원본 값 사용
                            paramMap.put(key, values[0]);
                        }
                    } else {
                        // 여러 값이 있는 경우 각각 복호화
                        List<String> decryptedValues = new ArrayList<>();
                        for (String value : values) {
                            try {
                                decryptedValues.add(CryptoUtils.decrypt(value));
                            } catch (IllegalArgumentException e) {
                                if (log.isErrorEnabled()) {
                                    String exceptionType = e.getClass().getSimpleName();
                                    log.error("[ERROR] StatisticsInterceptor convertRequestParamsToJson3 {}: ",
                                            exceptionType, e);
                                }
                                // null 값 안전하게 처리
                                decryptedValues.add(value != null ? value : "");
                            } catch (Exception e) {
                                if (log.isErrorEnabled()) {
                                    String exceptionType = e.getClass().getSimpleName();
                                    log.error("[ERROR] StatisticsInterceptor convertRequestParamsToJson4 {}: ",
                                            exceptionType, e);
                                }
                                // null 값 안전하게 처리
                                decryptedValues.add(value != null ? value : "");
                            }
                        }
                        paramMap.put(key, decryptedValues);
                    }
                } else {
                    paramMap.put(key, values.length == 1 ? values[0] : Arrays.asList(values));
                }
            }
        }

        // 2. JSON 요청 바디 처리 (별도 필터에서 캐싱된 경우)
        Object jsonBody = request.getAttribute("jsonBody");
        if (jsonBody != null) {
            paramMap.put("_jsonBody", jsonBody);
        }

        try {
            // Jackson 라이브러리 사용
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(paramMap);
        } catch (IllegalArgumentException e) {
            if (log.isErrorEnabled()) {
                String exceptionType = e.getClass().getSimpleName();
                log.error("[ERROR] StatisticsInterceptor convertRequestParamsToJson5 {}: ", exceptionType, e);
            }
            // JSON 변환 실패 시 기본 toString 사용
            return paramMap.toString();
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                String exceptionType = e.getClass().getSimpleName();
                log.error("[ERROR] StatisticsInterceptor convertRequestParamsToJson6 {}: ", exceptionType, e);
            }
            // JSON 변환 실패 시 기본 toString 사용
            return paramMap.toString();
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private String getDeviceType(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent").toLowerCase();
        if (userAgent.contains("mobile") || userAgent.contains("android") || userAgent.contains("iphone")
                || userAgent.contains("ipad")) {
            return "MOBILE";
        } else {
            return "PC";
        }
    }
}