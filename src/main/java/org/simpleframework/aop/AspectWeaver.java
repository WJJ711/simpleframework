package org.simpleframework.aop;

import org.simpleframework.aop.annotation.Aspect;
import org.simpleframework.aop.annotation.Order;
import org.simpleframework.aop.aspect.AspectInfo;
import org.simpleframework.aop.aspect.DefaultAspect;
import org.simpleframework.core.BeanContriner;
import org.simpleframework.util.ValidationUtil;
import java.util.*;

/**
 * @author wjj
 * @version 1.0
 * @date 2020/7/16 20:38
 */
public class AspectWeaver {
    private BeanContriner beanContriner;

    public AspectWeaver() {
        this.beanContriner=BeanContriner.getInstance();
    }
    public void doAop(){
        //1.获取所有的切面类
        Set<Class<?>> aspectSet = beanContriner.getClassesByAnnotation(Aspect.class);
        if (ValidationUtil.isEmpty(aspectSet)){
            return;
        }
        //2.拼装AspectInfoList
        List<AspectInfo> aspectInfoList=packAspectInfoList(aspectSet);
        //3.遍历容器里的类
        Set<Class<?>> classSet = beanContriner.getClasses();
        for (Class<?> targetClass : classSet) {
            //排除AspectClass自身
            if (targetClass.isAnnotationPresent(Aspect.class)){
                continue;
            }
            //4.粗筛符合条件的Aspect
            List<AspectInfo> roughMatchedAspectList=collectRoughMatchedAspectListForSpecificClass(aspectInfoList,targetClass);

            //5.尝试进行Aspect的织入
            wrapIfNecessary(roughMatchedAspectList,targetClass);
        }

    }

    private void wrapIfNecessary(List<AspectInfo> roughMatchedAspectList, Class<?> targetClass) {
        if (ValidationUtil.isEmpty(roughMatchedAspectList)){
            return;
        }
        //创建动态代理对象
        AspectListExecutor aspectListExecutor = new AspectListExecutor(targetClass, roughMatchedAspectList);
        Object proxyBean = ProxyCreator.createProxy(targetClass, aspectListExecutor);
        //3.将动态代理对象实例添加到容器里，取代未被代理前的类实例
        beanContriner.addBean(targetClass,proxyBean);
    }

    private List<AspectInfo> collectRoughMatchedAspectListForSpecificClass(List<AspectInfo> aspectInfoList, Class<?> targetClass) {
        List<AspectInfo> roughMatchedAspectList=new ArrayList<>();
        for (AspectInfo aspectInfo : aspectInfoList) {
            if (aspectInfo.getPointcutLocator().roughMatches(targetClass)){
                roughMatchedAspectList.add(aspectInfo);
            }
        }
        return roughMatchedAspectList;
    }

    private List<AspectInfo> packAspectInfoList(Set<Class<?>> aspectSet) {
        List<AspectInfo> list=new ArrayList<>();
        for (Class<?> aspectClass : aspectSet) {
            Order orderTag = aspectClass.getAnnotation(Order.class);
            Aspect aspectTag = aspectClass.getAnnotation(Aspect.class);
            DefaultAspect aspectImpl = (DefaultAspect) beanContriner.getBean(aspectClass);
            //传入切面表达式
            PointcutLocator pointcutLocator=new PointcutLocator(aspectTag.pointcut());
            list.add(new AspectInfo(orderTag.value(),aspectImpl,pointcutLocator));
        }

        return list;
    }

  // private void weaveByCategory(Class<? extends Annotation> category, List<AspectInfo> aspectInfos) {
  //     //1.获取被代理类的集合
  //     Set<Class<?>> classSet = beanContriner.getClassesByAnnotation(category);
  //     if (ValidationUtil.isEmpty(classSet)){
  //         return;
  //     }
  //     //2.遍历被代理类，分别为每个代理类生成动态代理实例
  //     for (Class<?> targetClass : classSet) {
  //         //创建动态代理对象
  //         AspectListExecutor aspectListExecutor = new AspectListExecutor(targetClass, aspectInfos);
  //         Object proxyBean = ProxyCreator.createProxy(targetClass, aspectListExecutor);
  //         //3.将动态代理对象实例添加到容器里，取代未被代理前的类实例
  //         beanContriner.addBean(targetClass,proxyBean);
  //     }
  // }

  // /**
  //  * 将切面类按照不同的织入目标进行切分
  //  * @param categorizedMap
  //  * @param aspectClass
  //  */
  // private void categorizeAspect(Map<Class<? extends Annotation>, List<AspectInfo>> categorizedMap, Class<?> aspectClass) {
  //     Order orderTag = aspectClass.getAnnotation(Order.class);
  //     Aspect aspectTag = aspectClass.getAnnotation(Aspect.class);
  //     DefaultAspect aspect = (DefaultAspect) beanContriner.getBean(aspectClass);
  //     AspectInfo aspectInfo = new AspectInfo(orderTag.value(), aspect);
  //     if (!categorizedMap.containsKey(aspectTag.value())){
  //         //如果织入的joinpoint第一次出现，则以该joinpoint为key，以新创建的List<AspectInfo>为value
  //         List<AspectInfo> aspectInfoList=new ArrayList<>();
  //         aspectInfoList.add(aspectInfo);
  //         categorizedMap.put(aspectTag.value(),aspectInfoList);
  //     }else {
  //         //如果织入的joinpoint不是第一次出现，则往joinpoint对应的value里添加新的Aspect逻辑
  //         List<AspectInfo> aspectInfoList = categorizedMap.get(aspectTag.value());
  //         aspectInfoList.add(aspectInfo);
  //     }
  // }

  // /**
  //  * 框架中一定要遵守Aspect类添加@Aspect和@Order标签的规范，同时，必须继承自DefaultAspect.class
  //  * 此外，@Apect的属性值不能是它本身
  //  * @param aspectClass
  //  * @return
  //  */
  // private boolean verifyAspect(Class<?> aspectClass) {
  //     return aspectClass.isAnnotationPresent(Aspect.class)&&
  //             aspectClass.isAnnotationPresent(Order.class)&&
  //             DefaultAspect.class.isAssignableFrom(aspectClass)&&
  //             aspectClass.getAnnotation(Aspect.class).value()!=Aspect.class;
  // }
}
