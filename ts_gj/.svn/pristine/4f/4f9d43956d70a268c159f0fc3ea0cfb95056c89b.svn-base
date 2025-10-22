package egovframework.theimc.config.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * ExceptionHandlingAspect
 * 
 * 작성일: 2024-08-14
 * 
 * 설명:
 * 이 클래스는 Spring AOP를 사용하여 서비스 레이어의 예외를 처리하기 위한 Aspect입니다.
 * BusinessException은 비즈니스 로직상 의도적으로 발생시키는 예외로, 이는 처리하지 않고 그대로 전파합니다.
 * 그 외의 예외들은 로깅 후 기본값을 반환합니다.
 */
@Aspect
@Component
@Slf4j
public class ExceptionHandlingAspect {

    @Pointcut("execution(* egovframework.theimc.*.service.*.*(..)) && !execution(* egovframework.theimc.mbr.service..*.*(..))") 
    public void serviceMethods() {}

    /**
     * 서비스 메서드의 예외 처리
     * 
     * BusinessException은 그대로 전파하고,
     * 그 외의 예외는 로깅 후 기본값을 반환합니다.
     * 
     * @param joinPoint AOP 조인 포인트
     * @return 메서드 실행 결과 또는 기본값
     * @throws Throwable BusinessException이나 기타 체크 예외
     */
    @Around("serviceMethods()")
    public Object handleException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (BusinessException ex) {
            // BusinessException은 그대로 전파 (로깅은 선택사항)
            String methodName = joinPoint.getSignature().getName();
            String className = joinPoint.getTarget().getClass().getSimpleName();
            
            if (log.isWarnEnabled()) {
                log.warn("[BUSINESS_ERROR] {} {}: {}", className, methodName, ex);
            }
            
            throw ex; // 예외를 그대로 전파
            
        } catch (Exception ex) {
            // 그 외의 예외는 처리하고 기본값 반환
            String methodName = joinPoint.getSignature().getName();
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String exceptionName = ex.getClass().getSimpleName();

            if (log.isErrorEnabled()) {
                log.error("[ERROR] {} {} {}: ", className, methodName, exceptionName, ex);
            }

            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            Class<?> returnType = method.getReturnType();
            return getDefaultValue(returnType);
        }
    }

    @Pointcut("execution(* egovframework.theimc.*.web.*.*(..))")
    public void controllerMethods() {}

    /**
     * 컨트롤러 메서드의 예외 로깅
     * 
     * @param joinPoint AOP 조인 포인트
     * @param ex 발생한 예외
     */
    @AfterThrowing(pointcut = "controllerMethods()", throwing = "ex")
    public void controllerMethodsHandleException(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String exceptionName = ex.getClass().getSimpleName();

        // BusinessException은 WARN 레벨로, 그 외는 ERROR 레벨로 로깅
        if (ex instanceof BusinessException) {
            if (log.isWarnEnabled()) {
                log.warn("[BUSINESS_ERROR] {} {} {}: {}", className, methodName, exceptionName, ex.getMessage());
            }
        } else {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] {} {} {}: ", className, methodName, exceptionName, ex);
            }
        }
    }

    /**
     * 반환 타입에 따라 기본값을 생성하여 반환합니다.
     * 
     * @param returnType 메서드의 반환 타입
     * @return 반환 타입에 맞는 기본값
     */
    private Object getDefaultValue(Class<?> returnType) {
        if (returnType.isAssignableFrom(List.class)) {
            return Collections.emptyList();
        } else if (returnType.isAssignableFrom(Map.class)) {
            return Collections.emptyMap();
        } else if (returnType.isAssignableFrom(String.class)) {
            return "";
        } else if (returnType.isPrimitive()) {
            if (returnType.equals(int.class)) return 0;
            if (returnType.equals(long.class)) return 0L;
            if (returnType.equals(double.class)) return 0.0;
            if (returnType.equals(float.class)) return 0.0f;
            if (returnType.equals(boolean.class)) return false;
            if (returnType.equals(char.class)) return '\0';
        }
        return null;
    }
}