package org.simpleframework.core;

import com.baixiao.controller.frontend.MainPageController;
import com.baixiao.service.solo.HeadLineService;
import com.baixiao.service.solo.impl.HeadLineServiceImpl;
import org.junit.jupiter.api.*;
import org.simpleframework.core.annotation.Controller;

/**
 * @author wjj
 * @version 1.0
 * @date 2020/6/23 2:21
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BeanContainerTest {
    private static BeanContriner beanContriner;

    @BeforeAll
    static void init(){
        beanContriner=BeanContriner.getInstance();
    }

    @DisplayName("加载目标类及其实例到BeanContainer：loadBeansTest")
    @Test
    @Order(1)
    public void loadBeanTest(){
        Assertions.assertEquals(false,beanContriner.isLoaded());
        beanContriner.loadBeans("com.baixiao");
        Assertions.assertEquals(6,beanContriner.size());
        Assertions.assertEquals(true,beanContriner.isLoaded());
    }

    @DisplayName("根据类获取其实例:getBeanTest")
    @Order(2)
    @Test
    public void getBeanTest(){
        Object bean = beanContriner.getBean(MainPageController.class);
        Assertions.assertEquals(true,bean instanceof  MainPageController);
    }

    @DisplayName("根据注解获取对应的实例：getClassesByAnnotationTest")
    @Order(3)
    @Test
    public void getClassesByAnnotationTest(){
        Assertions.assertEquals(true,beanContriner.isLoaded());
        Assertions.assertEquals(3,beanContriner.getClassesByAnnotation(Controller.class).size());
    }

    @DisplayName("根据接口获取实现类：getClassesBySuperTest")
    @Test
    @Order(4)
    public void getClassesBySuperTest(){
        Assertions.assertEquals(true,beanContriner.isLoaded());
        Assertions.assertEquals(true,beanContriner.getClassesBySuper(HeadLineService.class).contains(HeadLineServiceImpl.class));
    }
}
