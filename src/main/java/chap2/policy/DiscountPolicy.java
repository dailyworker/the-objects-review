package chap2.policy;

import chap2.Money;
import chap2.Screening;
import chap2.policy.condition.DiscountCondition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface DiscountPolicy {
    Money calculateDiscountAmount(Screening screening);
}
