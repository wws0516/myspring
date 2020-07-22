package spring.framework.context;

import org.junit.Test;
import spring.demo.action.MyAction;
import spring.framework.annotation.Autowired;
import spring.framework.annotation.Component;
import spring.framework.annotation.Controller;
import spring.framework.annotation.Service;
import spring.framework.aop.annotation.Aspect;
import spring.framework.beans.BeanContainer;
import spring.framework.beans.BeanWrapper;
import spring.framework.beans.factory.support.BeanDefinitionReader;
import spring.framework.beans.factory.config.BeanDefinition;
import spring.framework.beans.factory.support.DefaultListableBeanFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


/**
 * @Author: wws
 * @Date: 2020-07-18 05:54
 */
public class ApplicationContext extends DefaultListableBeanFactory {

    String[] configLocations;

    private final Map<String, Object> singletonObjects = new HashMap<String, Object>(256);


    private final Map<String, BeanWrapper> factoryBeanInstanceCache = new HashMap<String, BeanWrapper>(256);

    //存储接口及其对应的实现类
    private final Map<String, List<Class>> interfaceImpls = new HashMap(256);

    public void setConfigLocations(String ... configLocations) {
        this.configLocations = configLocations;
    }

    public void onRefresh(){
        //1、定位，定位配置文件
        BeanDefinitionReader reader = new BeanDefinitionReader(configLocations);

        //2、加载配置文件，扫描相关的类，把它们封装成BeanDefinition，存放到list中
        List<BeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

        //3、把bean放到容器里面(IOC容器)，这方法命名不太准确
        doRegisterBeanDefinition(beanDefinitions);

        //4、把非延时加载的类提前初始化
        doAutowired(beanDefinitions);
    }

    private void doAutowired(List<BeanDefinition> beanDefinitions) {

        for (BeanDefinition beanDefinition : beanDefinitions){
            if (!beanDefinition.isLazyInit())
                getBean(beanDefinition.getFactoryBeanName(), beanDefinition);
        }

//        for (Map.Entry<String, BeanDefinition> beanDefinition : beanDefinitionMap.entrySet()){
//            if (!beanDefinition.getValue().isLazyInit())
//                getBean(beanDefinition.getKey());
//        }

    }

    private void getBean(String factoryBeanName, BeanDefinition beanDefinition) {

        //通过factoryName获取到beanDefinition
//        BeanDefinition beanDefinition = beanDefinitionMap.get(factoryBeanName);

//        BeanWrapper beanWrapper = instantiateBean(factoryBeanName, beanDefinition);
//
//        factoryBeanInstanceCache.put(beanDefinition.getBeanClassName(), beanWrapper);
//        factoryBeanInstanceCache.put(factoryBeanName, beanWrapper);
        BeanWrapper beanWrapper = getBeanWrapper(beanDefinition.getFactoryBeanName(), beanDefinition);

        populateBean(beanDefinition.getBeanClassName(), beanDefinition, beanWrapper);

    }

    //根据beanDefinition从容器中取出对应的beanWrapper
    protected BeanWrapper getBeanWrapper(final String beanName, final BeanDefinition beanDefinition) {

        Class<?> clazz = null;
        Object o = null;
        try {
            clazz = Class.forName(beanDefinition.getBeanClassName());
            o = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Aspect.class)) {
            BeanWrapper beanWrapper = factoryBeanInstanceCache.get(beanName);

            return beanWrapper;


//        Object instance = null;
//
//        //如果单例容器中已经有这个bean
//        if (singletonObjects.containsKey(beanName)){
//            instance = singletonObjects.get(beanName);
//            return new BeanWrapper(instance);
//        }
        }
        return null;
    }


    // 对指定的bean进行依赖注入
    protected void populateBean(String beanName, BeanDefinition bd, BeanWrapper bw) {

        Class<?> wrappedClass = bw.getWrappedClass();

        Object instance = bw.getWrappedInstance();
        if ((wrappedClass.isAnnotationPresent(Controller.class) || wrappedClass.isAnnotationPresent(Service.class) || wrappedClass.isAnnotationPresent(Component.class))) {

            Field[] fields = wrappedClass.getDeclaredFields();
            for (Field field : fields) {
                String autowiredBeanName = field.getAnnotation(Autowired.class).value().trim();
                if (autowiredBeanName.equals("")) {
                    autowiredBeanName = field.getType().getName();
                    Class i = null;
                    try {
                        i = Class.forName(autowiredBeanName);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (i.isInterface()) {
                        for (Class clz : interfaceImpls.get(autowiredBeanName)) {
                            //检测是否是第二个实现类
                            try {
                                if (!Class.forName(autowiredBeanName).isInterface())
                                    throw new Exception(wrappedClass.getSimpleName()+"注入"+field.getName()+"时发现有多个实现类，请指定一个实现类");
                            } catch (Exception e) {
                                e.printStackTrace();
                                break;
                            }
                            //找到实现类，将autowiredBeanName改为该实现类的全限定名
                            if (i.isAssignableFrom(clz)) {
                                autowiredBeanName = clz.getName();
                            }
                        }
                    }
                }
                field.setAccessible(true);
                try {
                    field.set(instance, factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }

        public void doRegisterBeanDefinition(List<BeanDefinition> beanDefinitions) {

            for (BeanDefinition beanDefinition : beanDefinitions) {

                Class<?> clazz = null;
                Object o = null;
                try {
                    clazz = Class.forName(beanDefinition.getBeanClassName());
                    o = clazz.getConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Annotation annotation = null;
                String value = "";

                if(clazz.isAnnotationPresent(Controller.class)) {
                    annotation = clazz.getAnnotation(Controller.class);
                    value = ((Controller) annotation).value();
                }
                else if(clazz.isAnnotationPresent(Service.class)) {
                    annotation = clazz.getAnnotation(Service.class);
                    value = ((Service) annotation).value();
                }
                else if (clazz.isAnnotationPresent(Component.class)){
                    annotation =  clazz.getAnnotation(Component.class);
                    value = ((Component) annotation).value();
                }
                else if (clazz.isAnnotationPresent(Aspect.class)){
                    annotation =  clazz.getAnnotation(Aspect.class);
                }

                BeanWrapper beanWrapper = new BeanWrapper(o);

                if (annotation != null) {
                    for (Class i : clazz.getInterfaces()) {
                        interfaceImpls.computeIfAbsent(i.getName(), k -> new ArrayList<Class>());
                        interfaceImpls.get(i.getName()).add(clazz);

                    }
                    if (!value.equals(""))
                        factoryBeanInstanceCache.put(value, beanWrapper);
                    factoryBeanInstanceCache.putIfAbsent(beanDefinition.getFactoryBeanName(), beanWrapper);
                    factoryBeanInstanceCache.put(beanDefinition.getBeanClassName(), beanWrapper);
                }

            }
        }

    public Map<String, BeanWrapper> getFactoryBeanInstanceCache() {
        return factoryBeanInstanceCache;
    }

    @Test
    public void test() {
        ApplicationContext appContextText = new ApplicationContext();
        appContextText.setConfigLocations("classpath: application.properties");
        appContextText.onRefresh();

        MyAction myAction = (MyAction) appContextText.factoryBeanInstanceCache.get("myAction").getWrappedInstance();
        System.out.println(myAction.getQueryService().query("a"));
    }
}
