package spring.framework.annotation;

import java.lang.annotation.*;

/**
 * @Author: wws
 * @Date: 2020-07-19 06:19
 */
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Service{

    String value() default "";

}
