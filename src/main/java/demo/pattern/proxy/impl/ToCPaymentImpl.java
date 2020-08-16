package demo.pattern.proxy.impl;

import demo.pattern.proxy.ToCPayment;

/**
 * @author wjj
 * @version 1.0
 * @date 2020/7/16 15:08
 */
public class ToCPaymentImpl implements ToCPayment {
    @Override
    public void pay() {
        System.out.println("以用户名义进行支付");
    }
}
