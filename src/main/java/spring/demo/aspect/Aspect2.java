package spring.demo.aspect;

import spring.framework.aop.aspect.DefaultAspect;
import spring.framework.aop.annotation.Aspect;
import spring.framework.aop.annotation.Order;

/**
 * @Author: wws
 * @Date: 2020-07-22 08:13
 */
@Aspect(pointcut = "within(spring.demo.action.*)")
@Order(2)
public class Aspect2 extends DefaultAspect {

    public void before(){
        System.out.println("Aspect2织入前");
        return;
    }

    public Object afterRunning(){
        System.out.println("Aspect2织入后");
        return null;
    }

    public Object afterThrowing(){

        System.out.println("Aspect2异常织入");
        return null;
    }

}
