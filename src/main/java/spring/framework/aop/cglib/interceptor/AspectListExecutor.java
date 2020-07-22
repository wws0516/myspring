package spring.framework.aop.cglib.interceptor;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import spring.framework.aop.aspect.AspectInfo;
import spring.framework.aop.PointcutLocator;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author: wws
 * @Date: 2020-07-22 06:39
 */
public class AspectListExecutor implements MethodInterceptor {

    private Object obj;

    List<AspectInfo> aspectInfoList;

    public AspectListExecutor(List<AspectInfo> aspectInfoList, Object o){
        this.aspectInfoList = aspectInfoList;
        obj = o;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        //对aspectInfoList精筛
        PointcutLocator.accurateMatch(aspectInfoList, method);

        if (aspectInfoList.isEmpty()){
            method.invoke(obj, args);
            return null;
        }

        //按order顺序执行aspect的before方法
        invokeAspectsBefore();

        try {
            //执行被代理的方法
            method.invoke(obj, args);
        }catch (Exception e){
            //如果抛出异常，按order逆序执行aspect的afterThrowing方法
            invokeAspectsAfterThrowing();
        }
        //如果正常返回，按order逆序执行aspect的afterRunning方法
        invokeAspectsAfterRunning();

        return null;
    }

    private void invokeAspectsAfterRunning() {
        for (int i=aspectInfoList.size()-1; i>=0; i--)
            aspectInfoList.get(i).getAspect().afterRunning();
    }

    private void invokeAspectsAfterThrowing() {
        for (int i=aspectInfoList.size()-1; i>=0; i--)
            aspectInfoList.get(i).getAspect().afterThrowing();
    }

    private void invokeAspectsBefore() {

        for (AspectInfo aspectInfo : aspectInfoList)
            aspectInfo.getAspect().before();

    }
}
