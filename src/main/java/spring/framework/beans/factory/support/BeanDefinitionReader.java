package spring.framework.beans.factory.support;

import jdk.nashorn.internal.runtime.Property;
import spring.framework.beans.factory.config.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Author: wws
 * @Date: 2020-07-18 11:48
 */
public class BeanDefinitionReader {

    Properties properties = new Properties();

    List<String> registyBeanClasses = new ArrayList<String>();
    
    public BeanDefinitionReader(String... locations) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath: ", ""));
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = properties.getProperty("packageScan");
        doScanner("/" + path.replace(".", "/"));
        
    }

    private void doScanner(String path) {

        URL url = this.getClass().getResource(path);

        File files = new File(url.getFile());
        
        for (File file : files.listFiles())
            if (file.isDirectory())
                doScanner(path + "/" + file.getName());
            else if (file.getName().endsWith(".class"))
                registyBeanClasses.add((path.substring(1) + "/" + file.getName().replaceAll(".class", "")).replace("/", "."));

    }

    public List<BeanDefinition> loadBeanDefinitions(){
        List<BeanDefinition> result = new ArrayList<BeanDefinition>();
        try {
            for (String className : registyBeanClasses) {
                Class<?> beanClass = Class.forName(className);
                //如果是一个接口，是不能实例化的
                //用它实现类来实例化
                if(beanClass.isInterface()) { continue; }

                //beanName有三种情况:
                //1、默认是类名首字母小写
                //2、自定义名字
                //3、接口注入
                result.add(doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()),beanClass.getName()));
//                result.add(doCreateBeanDefinition(beanClass.getName(),beanClass.getName()));

//                Class<?> [] interfaces = beanClass.getInterfaces();
//                for (Class<?> i : interfaces) {
//                    //如果是多个实现类，只能覆盖
//                    //为什么？因为Spring没那么智能，就是这么傻
//                    //这个时候，可以自定义名字
//                    result.add(doCreateBeanDefinition(i.getName(),beanClass.getName()));
//                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    //把每一个配信息解析成一个BeanDefinition
    private BeanDefinition doCreateBeanDefinition(String factoryBeanName,String beanClassName){
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }

    //如果类名本身是小写字母，确实会出问题
    //默认传入的值，存在首字母小写的情况，也不可能出现非字母的情况

    public static String toLowerFirstCase(String simpleName) {
        char [] chars = simpleName.toCharArray();
        //之所以加，是因为大小写字母的ASCII码相差32，
        // 而且大写字母的ASCII码要小于小写字母的ASCII码
        //在Java中，对char做算学运算，实际上就是对ASCII码做算学运算
        chars[0] += 32;
        return String.valueOf(chars);
    }


}
