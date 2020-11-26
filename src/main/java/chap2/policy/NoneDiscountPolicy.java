package chap2.policy;

import chap2.Money;
import chap2.Screening;

public class NoneDiscountPolicy implements DiscountPolicy {
    @Override
    public Money calculateDiscount(Screening screening) {
        return Money.ZERO;
    }
}
