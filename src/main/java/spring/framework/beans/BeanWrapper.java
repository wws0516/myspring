package spring.framework.beans;

import java.beans.PropertyDescriptor;

/**
 * @Author: wws
 * @Date: 2020-07-18 11:47
 */
public class BeanWrapper {


    /**
     * the bean instance wrapped by this object.
     */
    Object wrappedInstance;

    /**
     * the type of the wrapped bean instance.
     */
    Class<?> wrappedClass;

    public Object getWrappedInstance() {
        return wrappedInstance;
    }

    public void setWrappedInstance(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }

    public Class<?> getWrappedClass() {
        return wrappedClass;
    }

    public void setGetWrappedClass(Class<?> wrappedClass) {
        this.wrappedClass = wrappedClass;
    }

    public BeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
        this.wrappedClass = wrappedInstance.getClass();
    }
}
