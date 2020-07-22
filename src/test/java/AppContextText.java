import spring.demo.action.MyAction;
import spring.framework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;

/**
 * @Author: wws
 * @Date: 2020-07-19 07:35
 */
public class AppContextText {

    private String a = "";

    public AppContextText(String a) {
        this.a = a;
    }

    public void pring(){
        System.out.println(a);
    }

    public static void main(String[] args) {

        AppContextText ap = new AppContextText("a");
        try {
            ap.getClass().getMethod("pring").invoke(ap, null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

}
