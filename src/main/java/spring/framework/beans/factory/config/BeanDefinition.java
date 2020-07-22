package spring.framework.beans.factory.config;

/**
 * 可以认为是 bean 和 加入ioc容器的BeanWrapper 的中间产物
 * @Author: wws
 * @Date: 2020-07-18 11:42
 */
public class BeanDefinition {

    private String beanClassName;
    private String factoryBeanName;

    private boolean isSingleton;
    private boolean isLazyInit;

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    public boolean isSingleton() {
        return isSingleton;
    }

    public void setSingleton(boolean singleton) {
        isSingleton = singleton;
    }

    public boolean isLazyInit() {
        return isLazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        isLazyInit = lazyInit;
    }
}
