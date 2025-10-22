package egovframework.theimc.config.aop;

/**
 * BusinessException
 * 
 * 비즈니스 로직에서 의도적으로 발생시키는 예외
 * 이 예외는 AOP에서 처리하지 않고 컨트롤러로 전파됩니다.
 */
public class BusinessException extends RuntimeException {
    
    private String errorCode;
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public BusinessException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
