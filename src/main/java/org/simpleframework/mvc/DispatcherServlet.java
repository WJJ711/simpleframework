package org.simpleframework.mvc;


import com.baixiao.controller.frontend.MainPageController;
import com.baixiao.controller.superadmin.HeadLineOperationController;
import org.simpleframework.aop.AspectWeaver;
import org.simpleframework.core.BeanContriner;
import org.simpleframework.core.inject.DependencyInjector;
import org.simpleframework.mvc.processor.RequestProcessor;
import org.simpleframework.mvc.processor.impl.ControllerRequestProcessor;
import org.simpleframework.mvc.processor.impl.JspRequestProcessor;
import org.simpleframework.mvc.processor.impl.PreRequestProcessor;
import org.simpleframework.mvc.processor.impl.StaticResourceRequestProcessor;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/*")
public class DispatcherServlet extends HttpServlet {
    List<RequestProcessor> PROCESSOR=new ArrayList<>();
    @Override
    public void init(){
        //1.初始化容器
        BeanContriner beanContriner = BeanContriner.getInstance();
        beanContriner.loadBeans("com.baixiao");
        //先进行aop
        new AspectWeaver().doAop();
        //再依赖注入
        new DependencyInjector().doIoc();
        //2.初始化请求处理器责任链
        PROCESSOR.add(new PreRequestProcessor());
        PROCESSOR.add(new StaticResourceRequestProcessor());
        PROCESSOR.add(new JspRequestProcessor());
        PROCESSOR.add(new ControllerRequestProcessor());
    }
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        //1.创建责任链对象实例
        RequestProcessorChain requestProcessorChain=new RequestProcessorChain(PROCESSOR.iterator(),req,resp);
        //2.通过责任链模式来依次调用请求处理器对请求进行处理
        requestProcessorChain.doRequestProcessorChain();
        //3.对处理结果进行渲染
        requestProcessorChain.doRender();
    }
}
