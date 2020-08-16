package org.simpleframework.aop;

import lombok.Getter;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.simpleframework.aop.aspect.AspectInfo;
import org.simpleframework.util.ValidationUtil;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author wjj
 * @version 1.0
 * @date 2020/7/16 18:56
 */
public class AspectListExecutor implements MethodInterceptor {
    //被代理的类的class对象
    private Class<?> targetClass;
    @Getter
    private List<AspectInfo> sortAspectInfoList;

    public AspectListExecutor(Class<?> targetClass, List<AspectInfo> aspectInfoList) {
        this.targetClass = targetClass;
        this.sortAspectInfoList = sortAspectInfoList(aspectInfoList);
    }

    /**
     * 按照order的值进行升序排序，确保order值最小的aspect先被织入
     * @param aspectInfoList
     * @return
     */
    private List<AspectInfo> sortAspectInfoList(List<AspectInfo> aspectInfoList) {
        Collections.sort(aspectInfoList, new Comparator<AspectInfo>() {
            @Override
            public int compare(AspectInfo o1, AspectInfo o2) {
                return o1.getOrderIndex()-o2.getOrderIndex();
            }
        });
        return aspectInfoList;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object returnValue=null;
        collectAccurateMatchedAspectList(method);
        if (ValidationUtil.isEmpty(sortAspectInfoList)){
            return methodProxy.invokeSuper(o, args);
        }
        //1.按照order的顺序升序执行完Aspect的before方法
        invokeBeforeAdivces(method,args);
        try {
            //2.执行被代理类的方法
            returnValue = methodProxy.invokeSuper(o, args);
            //3.如果被代理方法正常返回，则按照order的顺序降序执行完所有Aspect的afterReturning方法
            invokeAfterReturningAdvices(method,args,returnValue);
        }catch (Exception e){
            //4.如果被代理方法抛出异常，则按照order的顺序执行完所有Aspect的afterThrowing方法
            invokeAfterThrowingAdvices(method,args,e);
        }


        return returnValue;
    }

    /**
     * 精筛
     * @param method
     */
    private void collectAccurateMatchedAspectList(Method method) {
        if (ValidationUtil.isEmpty(sortAspectInfoList)){return;}
        Iterator<AspectInfo> it = sortAspectInfoList.iterator();
        while (it.hasNext()){
            AspectInfo aspectInfo = it.next();
            if (!aspectInfo.getPointcutLocator().accurateMatches(method)){
                it.remove();
            }
        }
    }


    //1.按照order的顺序升序执行完Aspect的before方法
    private void invokeBeforeAdivces(Method method, Object[] args) throws Throwable {
        for (AspectInfo aspectInfo : sortAspectInfoList) {
            aspectInfo.getAspectObject().before(targetClass,method,args);
        }
    }
    //3.如果被代理方法正常返回，则按照order的顺序降序执行完所有Aspect的afterReturning方法
    private Object invokeAfterReturningAdvices(Method method, Object[] args, Object returnValue) throws Throwable {
        Object result=null;
        for (int i=sortAspectInfoList.size()-1;i>=0;i--){
            result=sortAspectInfoList.get(i).getAspectObject().afterReturning(targetClass,method,args,returnValue);
        }
        return result;
    }
    private void invokeAfterThrowingAdvices(Method method, Object[] args, Exception e) throws Throwable {

        for (int i=sortAspectInfoList.size()-1;i>=0;i--){
            sortAspectInfoList.get(i).getAspectObject().afterThrowing(targetClass,method,args,e);
        }
    }
}
