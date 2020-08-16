package demo.pattern.proxy.impl;

import demo.pattern.proxy.ToCPayment;

/**
 * @author wjj
 * @version 1.0
 * @date 2020/7/16 15:27
 */
public class AlipayToC implements ToCPayment {
    ToCPayment toCPayment;

    public AlipayToC(ToCPayment toCPayment) {
        this.toCPayment = toCPayment;
    }

    @Override
    public void pay() {
        beforePay();
        toCPayment.pay();
        afterPay();
    }

    private void afterPay() {
        System.out.println("支付给慕课网");
    }

    private void beforePay() {
        System.out.println("从招行取款");
    }
}
