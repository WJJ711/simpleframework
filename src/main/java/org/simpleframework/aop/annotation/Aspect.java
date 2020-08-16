package org.simpleframework.aop.annotation;

import java.lang.annotation.*;

/**
 * @author wjj
 * @version 1.0
 * @date 2020/7/16 17:48
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
 /*   //表明将改Aspect切面类织入到哪些业务逻辑类中
    Class<?extends Annotation> value();*/
    //"within(com.baixiao.controller.superdmin.*)"
    String pointcut();
}
