package spring.demo.aspect;

import spring.framework.aop.aspect.DefaultAspect;
import spring.framework.aop.annotation.Aspect;
import spring.framework.aop.annotation.Order;

/**
 * @Author: wws
 * @Date: 2020-07-22 08:13
 */
@Aspect(pointcut = "within(spring.demo.action.*)")
@Order(1)
public class Aspect1 extends DefaultAspect {

    public void before(){
        System.out.println("Aspect1织入前");
    }

    public Object afterRunning(){
        System.out.println("Aspect1织入后");
        return null;
    }

    public Object afterThrowing(){

        System.out.println("Aspect1异常织入");
        return null;
    }

}
