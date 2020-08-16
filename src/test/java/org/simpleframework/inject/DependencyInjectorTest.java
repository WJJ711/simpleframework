package org.simpleframework.inject;

import com.baixiao.controller.frontend.MainPageController;
import com.baixiao.service.combine.impl.HeadLineShopCategoryCombineServiceImpl;
import com.baixiao.service.combine.impl.HeadLineShopCategoryCombineServiceImpl2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.simpleframework.core.BeanContriner;
import org.simpleframework.core.inject.DependencyInjector;


/**
 * @author wjj
 * @version 1.0
 * @date 2020/6/26 18:25
 */
public class DependencyInjectorTest {

    @Test
    @DisplayName("依赖注入doIoc")
    public void doIocTest(){
        BeanContriner beanContriner = BeanContriner.getInstance();
        beanContriner.loadBeans("com.baixiao");
        Assertions.assertEquals(true,beanContriner.isLoaded());
        MainPageController mainPageController = (MainPageController) beanContriner.getBean(MainPageController.class);
        Assertions.assertEquals(true,mainPageController instanceof MainPageController);
        Assertions.assertEquals(null,mainPageController.getHeadLineShopCategoryCombineService());
        new DependencyInjector().doIoc();
        Assertions.assertNotEquals(null,mainPageController.getHeadLineShopCategoryCombineService());
        Assertions.assertEquals(true,mainPageController.getHeadLineShopCategoryCombineService() instanceof HeadLineShopCategoryCombineServiceImpl);
        Assertions.assertNotEquals(true,mainPageController.getHeadLineShopCategoryCombineService() instanceof HeadLineShopCategoryCombineServiceImpl2);
    }
}
