package spring.framework.beans.factory.support;

import spring.framework.beans.factory.config.BeanDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wws
 * @Date: 2020-07-18 11:45
 */
public class DefaultListableBeanFactory {

    //伪ioc容器，保存beanDefinition
    public Map<String, BeanDefinition> beanDefinitionMap = new HashMap<String, BeanDefinition>();

}
