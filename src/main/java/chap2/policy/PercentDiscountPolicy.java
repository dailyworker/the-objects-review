package chap2.policy;

import chap2.Money;
import chap2.Screening;
import chap2.policy.condition.DiscountCondition;

public class PercentDiscountPolicy extends DiscountPoilcy{
    private double percent;

    public PercentDiscountPolicy(double percent, DiscountCondition... conditions) {
        super(conditions);
        this.percent = percent;
    }

    @Override
    protected Money getDiscountAmount(Screening screening) {
        return screening.getMovieFee().times(percent);
    }
}
