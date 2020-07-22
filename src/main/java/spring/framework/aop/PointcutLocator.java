package spring.framework.aop;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import spring.framework.aop.annotation.Aspect;
import spring.framework.aop.aspect.AspectInfo;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: wws
 * @Date: 2020-07-22 11:42
 */
public class PointcutLocator {


    private static PointcutParser parser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingContextClassloaderForResolution(
            PointcutParser.getAllSupportedPointcutPrimitives()
    );

//    public PointcutParser getInstance(){
//
//    }

    private static PointcutExpression pointcutExpression;

    //解析器解析 表达式 获得 pointcutExpression对象
    static void parse(String expression) {
        pointcutExpression = parser.parsePointcutExpression(expression);
    }

    //判断 targetClass 是否符合 pointcutExpression
    static boolean roughMatches(Class<?> clz) {
        return pointcutExpression.couldMatchJoinPointsInType(clz);
    }

    //对aspectInfoList 进行方法层面的精筛
    public static void accurateMatch(List<AspectInfo> aspectInfoList, Method method) {
        Iterator<AspectInfo> iterator = aspectInfoList.iterator();
        while(iterator.hasNext()){
            parse(iterator.next().getAspect().getClass().getAnnotation(Aspect.class).pointcut());
            if (!pointcutExpression.matchesMethodExecution(method).alwaysMatches())
                iterator.remove();
        }
    }
}
