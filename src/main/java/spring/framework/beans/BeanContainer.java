package spring.framework.beans;

import java.lang.annotation.Annotation;
import java.util.*;

import spring.framework.annotation.Component;
import spring.framework.annotation.Controller;
import spring.framework.annotation.Service;
import spring.framework.aop.annotation.Aspect;
import spring.framework.context.ApplicationContext;


/**
 * 对ApplicationContext的封装，方便对容器操作
 * @Author: wws
 * @Date: 2020-07-21 08:36
 */
public class BeanContainer {

    private Map<String, BeanWrapper> beanMap = new HashMap<String, BeanWrapper>();

    /**
     * 加载 包含下面这些注解的bean 进容器
     */
    public static final List<Class<? extends Annotation>> BEAN_ANNOTATION
            = Arrays.asList(Component.class, Controller.class, Service.class, Aspect.class);

    private enum ContainerHolder{
        HOLDER;
        private BeanContainer container;

        private ContainerHolder(){
            container = new BeanContainer();

            ApplicationContext applicationContext = new ApplicationContext();
            applicationContext.setConfigLocations("classpath: application.properties");
            applicationContext.onRefresh();
            //这里相当于创建了容器副本，不能对原容器增删改
            container.beanMap = applicationContext.getFactoryBeanInstanceCache();

        }
    }

    public Map<String, BeanWrapper> getBeanMap() {
        return beanMap;
    }

    public static BeanContainer getBeanContainer(){
        return ContainerHolder.HOLDER.container;
    }


    //获得容器中所有的beanWrapper
    public Set<BeanWrapper> getAllBeans(){
        Set<BeanWrapper> beanWrappers = new HashSet<>();

        for (Map.Entry entry : beanMap.entrySet()){
            BeanWrapper beanWrapper = (BeanWrapper) entry.getValue();
                beanWrappers.add(beanWrapper);
        }
        return beanWrappers;
    }

    //通过注解类型获得beans
    public Set<BeanWrapper> getBeanByAnnotationType(Class<? extends Annotation> annotation){
        Set<BeanWrapper> beanWrappers = new HashSet<>();

        for (Map.Entry entry : beanMap.entrySet()){
            BeanWrapper beanWrapper = (BeanWrapper) entry.getValue();
            Class<?> wrappedClass =  beanWrapper.getWrappedClass();
            if (wrappedClass.isAnnotationPresent(annotation))
                beanWrappers.add(beanWrapper);
        }
        return beanWrappers;
    }

    public void addToContainer(String name, BeanWrapper beanWrapper){
        beanMap.put(name, beanWrapper);

    }

    private BeanContainer(){}
}
