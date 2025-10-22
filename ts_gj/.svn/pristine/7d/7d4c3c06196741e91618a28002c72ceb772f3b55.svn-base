package egovframework.theimc.config.aop;

import lombok.extern.slf4j.Slf4j;

/**
 * 공통 Exception 클래스
 * 모든 예외를 이 클래스로 래핑하여 catch(CommonException e)로 사용
 */
@Slf4j
public class CommonException extends RuntimeException {
    
    private final String className;
    private final String methodName;
    private final String originalExceptionType;

    /**
     * CommonException 생성자
     * 
     * @param className 발생한 클래스명
     * @param methodName 발생한 메소드명
     * @param originalException 원본 예외
     */
    public CommonException(String className, String methodName, Exception originalException) {
        super(originalException.getMessage(), originalException);
        this.className = className;
        this.methodName = methodName;
        this.originalExceptionType = originalException.getClass().getSimpleName();
        
        // 생성과 동시에 로그 출력
        log.error("[ERROR] {} {} {}: {}", className, methodName, originalExceptionType, originalException.getMessage());
    }

    /**
     * 클래스명 자동 추출 생성자
     * 
     * @param caller 호출 객체 (this)
     * @param methodName 메소드명
     * @param originalException 원본 예외
     */
    public CommonException(Object caller, String methodName, Exception originalException) {
        this(caller.getClass().getSimpleName(), methodName, originalException);
    }

    // Getter 메소드들
    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getOriginalExceptionType() {
        return originalExceptionType;
    }
}