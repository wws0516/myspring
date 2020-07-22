package spring.framework.aop.aspect;

/**
 * @Author: wws
 * @Date: 2020-07-22 06:42
 */
public class AspectInfo implements Comparable<AspectInfo>{

    private DefaultAspect aspect;

    private int order;

    public DefaultAspect getAspect() {
        return aspect;
    }

    public AspectInfo(DefaultAspect aspect, int order) {
        this.aspect = aspect;
        this.order = order;
    }

    @Override
    public int compareTo(AspectInfo o) {
        return this.order - o.order;
    }
}
