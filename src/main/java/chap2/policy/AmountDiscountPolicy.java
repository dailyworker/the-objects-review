package chap2.policy;

import chap2.Money;
import chap2.Screening;
import chap2.policy.condition.DiscountCondition;

public class AmountDiscountPolicy extends DiscountPoilcy {
    private Money discountAmount;

    public AmountDiscountPolicy(Money discountAmount, DiscountCondition... conditions) {
        super(conditions);
        this.discountAmount = discountAmount;
    }

    @Override
    protected Money getDiscountAmount(Screening screening) {
        return discountAmount;
    }
}
