package spring.framework.aop.annotation;

import java.lang.annotation.*;

/**
 * @Author: wws
 * @Date: 2020-07-20 22:08
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    String pointcut() ;

}
