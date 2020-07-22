package spring.framework.aop.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import spring.framework.beans.BeanWrapper;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @Author: wws
 * @Date: 2020-07-21 08:17
 */
public class ProxyCreator {


    public static Object createProxy(Class<?> clazz,  MethodInterceptor methodInterceptor){

        return Enhancer.create(clazz, methodInterceptor);

    }



}
