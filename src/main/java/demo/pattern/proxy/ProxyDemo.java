package demo.pattern.proxy;

import demo.pattern.proxy.cglib.AlipayMethodInterceptor;
import demo.pattern.proxy.cglib.CglibUtil;
import demo.pattern.proxy.impl.AlipayToC;
import demo.pattern.proxy.impl.CommPayment;
import demo.pattern.proxy.impl.ToCPaymentImpl;
import demo.pattern.proxy.jdkproxy.AlipayInvocationHandler;
import demo.pattern.proxy.jdkproxy.JdkDynamicProxyUtil;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author wjj
 * @version 1.0
 * @date 2020/7/16 15:39
 */
public class ProxyDemo {
    public static void main(String[] args) {
        //静态代理
       //ToCPayment toCPayment=new AlipayToC(new ToCPaymentImpl());
       //toCPayment.pay();

        //JDK动态代理
       // ToCPayment toCPayment = new ToCPaymentImpl();
       // InvocationHandler invocationHandler = new AlipayInvocationHandler(toCPayment);
       // ToCPayment paymentProxy = JdkDynamicProxyUtil.newProxyInstance(toCPayment, invocationHandler);
       // paymentProxy.pay();

        //因为commPayment没有实现接口，用JDK动态代理必须实现接口
        //JDK动态代理是用被代理类和代理类实现同一个接口，代理类去替代被代理类工作
       //CommPayment commPayment = new CommPayment();
       //AlipayInvocationHandler invocationHandler = new AlipayInvocationHandler(commPayment);
       //CommPayment commPaymentProxy = JdkDynamicProxyUtil.newProxyInstance(commPayment, invocationHandler);
       // commPaymentProxy.pay();

        //使用cglib
        CommPayment commPayment = new CommPayment();
        MethodInterceptor methodInterceptor = new AlipayMethodInterceptor();
        CommPayment proxy = CglibUtil.createProxy(commPayment, methodInterceptor);
        proxy.pay();
    }
}
