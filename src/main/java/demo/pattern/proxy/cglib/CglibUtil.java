package demo.pattern.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * @author wjj
 * @version 1.0
 * @date 2020/7/16 17:09
 */
public class CglibUtil {
    public static <T> T createProxy(T targetObject, MethodInterceptor methodInterceptor){
        return (T)Enhancer.create(targetObject.getClass(),methodInterceptor);
    }
}
