package chap2.policy.condition;

import chap2.Screening;

public interface DiscountCondition {
    boolean isSatisfiedBy(Screening screening);
}
