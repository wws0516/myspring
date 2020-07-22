package spring.framework.annotation;

import java.lang.annotation.*;

/**
 * @Author: wws
 * @Date: 2020-07-19 05:45
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    String value() default "";
}