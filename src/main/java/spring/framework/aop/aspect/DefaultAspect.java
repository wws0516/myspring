package spring.framework.aop.aspect;

/**
 * @Author: wws
 * @Date: 2020-07-20 22:08
 */
public abstract class DefaultAspect {

    public void before(){}

    public Object afterRunning(){
        return null;
    }

    public Object afterThrowing(){
        return null;
    }

}
