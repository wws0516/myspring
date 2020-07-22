package spring.framework.aop;

import org.junit.Test;
import spring.demo.action.MyAction;
import spring.framework.aop.annotation.Aspect;
import spring.framework.aop.annotation.Order;
import spring.framework.aop.aspect.AspectInfo;
import spring.framework.aop.aspect.DefaultAspect;
import spring.framework.aop.cglib.ProxyCreator;
import spring.framework.aop.cglib.interceptor.AspectListExecutor;
import spring.framework.beans.BeanContainer;
import spring.framework.beans.BeanWrapper;
import spring.framework.beans.factory.support.BeanDefinitionReader;

import java.util.*;

/**
 * @Author: wws
 * @Date: 2020-07-21 08:21
 */
public class AspectWeaver {

    Map<Class<?>, List<AspectInfo>> map = new HashMap<>();

    @Test
    public void Test(){
        new AspectWeaver().doAop();
        BeanContainer beanContainer = BeanContainer.getBeanContainer();
        MyAction myAction = (MyAction) beanContainer.getBeanMap().get("myAction").getWrappedInstance();
        myAction.print();
    }

    public void doAop() {

        List<AspectInfo> aspectInfos = new ArrayList<>();

        BeanContainer beanContainer = BeanContainer.getBeanContainer();
        //1.遍历容器，找到切面类
        Set<BeanWrapper> aspects = beanContainer.getBeanByAnnotationType(Aspect.class);

        //将aspect封装成aspectInfo,重新存放到list
        for (BeanWrapper beanWrapper : aspects) {
            int order = beanWrapper.getWrappedClass().getAnnotation(Order.class).value();
            DefaultAspect wrappedInstance = (DefaultAspect) beanWrapper.getWrappedInstance();
            aspectInfos.add(new AspectInfo(wrappedInstance, order));
        }
        /*
            引入aspectJ后
         */

        //通过初筛，粗略获得 bean 对应的 aspectInfoList

        for (BeanWrapper beanWrapper : beanContainer.getAllBeans()) {
            List<AspectInfo> aspectInfoList = new ArrayList<>();
            for (AspectInfo aspectInfo : aspectInfos) {
                PointcutLocator.parse(aspectInfo.getAspect().getClass().getAnnotation(Aspect.class).pointcut());
                if (PointcutLocator.roughMatches(beanWrapper.getWrappedClass()))
                    aspectInfoList.add(aspectInfo);
            }

            Collections.sort(aspectInfoList);
            AspectListExecutor aspectListExecutor = new AspectListExecutor(new ArrayList<>(aspectInfoList), beanWrapper.getWrappedInstance());

            Object proxy = ProxyCreator.createProxy(beanWrapper.getWrappedClass(), aspectListExecutor);
            //没有真正放入容器

            BeanWrapper proxyBeanWrapper = new BeanWrapper(proxy);

            beanContainer.addToContainer(beanWrapper.getWrappedClass().getName(), proxyBeanWrapper);
            beanContainer.addToContainer(BeanDefinitionReader.toLowerFirstCase(beanWrapper.getWrappedClass().getSimpleName()), proxyBeanWrapper);

            }
        }
        /*
            没引入aspectJ之前

        //2.遍历aspectInfos，取出@Aspect()中的pointcut，将符合pointcut的bean 和 aspectInfo 放入 map<Class<bean>, aspectInfo> 中, 也就是将aspectInfos分类放
        for (AspectInfo aspectInfo : aspectInfos){
                for (BeanWrapper beanWrapper : beanContainer.getBeanByAnnotationType(aspectInfo.getAspect().getClass().getAnnotation(Aspect.class).value())) {
                    map.computeIfAbsent(beanWrapper.getWrappedClass(), k -> new ArrayList<AspectInfo>());
                    map.get(beanWrapper.getWrappedClass()).add(aspectInfo);
                }
        }


        //3.将bean的动态代理类放入容器，代替bean
        for (Class clazz : map.keySet()) {

            Collections.sort(map.get(clazz));
            AspectListExecutor aspectListExecutor = new AspectListExecutor(map.get(clazz));

            Object proxy = ProxyCreator.createProxy(clazz, aspectListExecutor);
            //没有真正放入容器
            beanContainer.addToContainer("myAction", new BeanWrapper(proxy));

        }
        */
}
