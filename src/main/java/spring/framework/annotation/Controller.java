package spring.framework.annotation;

import java.lang.annotation.*;

/**
 * @Author: wws
 * @Date: 2020-07-19 05:21
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {

    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     * @return the suggested component name, if any (or empty String otherwise)
     */
    String value() default "";

}
