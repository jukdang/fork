package egovframework.theimc.config.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomStat {
    String value() default "";  // 통계 식별자
    String subValue() default "";  // 통계 식별자2
    String value2() default "";  // 통계 식별자2
    String description() default "";  // 통계 설명
}
