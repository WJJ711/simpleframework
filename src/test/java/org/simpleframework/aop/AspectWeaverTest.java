package org.simpleframework.aop;

import com.baixiao.controller.frontend.MainPageController;
import com.baixiao.controller.superadmin.HeadLineOperationController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.simpleframework.core.BeanContriner;
import org.simpleframework.core.inject.DependencyInjector;

/**
 * @author wjj
 * @version 1.0
 * @date 2020/7/17 2:49
 */
public class AspectWeaverTest {

    @DisplayName("织入通用逻辑测试:doAop")
    @Test
    public void doAopTest(){
        BeanContriner beanContriner = BeanContriner.getInstance();
        beanContriner.loadBeans("com.baixiao");
        //先进行aop
        new AspectWeaver().doAop();
        //再依赖注入
        new DependencyInjector().doIoc();
        //MainPageController mainPageController = (MainPageController)beanContriner.getBean(MainPageController.class);
        //mainPageController.getMainPageInfo(null,null);
        HeadLineOperationController headLineOperationController = (HeadLineOperationController) beanContriner.getBean(HeadLineOperationController.class);
        headLineOperationController.addHeadLine(null,null);



    }
}
