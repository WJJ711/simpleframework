package org.simpleframework.aop.aspect;

import java.lang.reflect.Method;

/**
 * @author wjj
 * @version 1.0
 * @date 2020/7/16 17:53
 */
public abstract class DefaultAspect {

    /**
     * 事前拦截
     * @param targetClass 被代理的目标类
     * @param method 被代理的目标方法
     * @param args 被代理的目标方法对应的参数列表
     * @throws Exception
     */
    public void before(Class<?> targetClass, Method method,Object[] args) throws Throwable{

    }

    /**
     * 事后拦截
     * @param targetClass 被代理的目标类
     * @param method 被代理的目标方法
     * @param args 被代理的目标方法对应的参数列表
     * @param returnValue
     * @return
     * @throws Throwable
     */
    public Object afterReturning(Class<?> targetClass,Method method,Object args,Object returnValue) throws Throwable{
        return returnValue;
    }

    /**
     *
     * @param targetClass 被代理的目标类
     * @param method 被代理的目标方法
     * @param args 被代理的目标方法对应的参数列表
     * @param e 被代理的目标方法抛出的异常
     * @throws Throwable
     */
    public void afterThrowing(Class<?> targetClass,Method method,Object[] args,Throwable e) throws Throwable{

    }
}
