package org.simpleframework.mvc.render;

import org.simpleframework.mvc.RequestProcessorChain;

/**
 * @author wjj
 * @version 1.0
 * @date 2020/7/21 2:42
 * 渲染请求结果
 */
public interface ResultRender {
    //执行渲染
    void render(RequestProcessorChain requestProcessorChain)throws Exception;
}
