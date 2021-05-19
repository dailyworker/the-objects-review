package chap5;

import java.time.Duration;

public class AmountDiscountMovie extends DiscountPolicy {
  private final Money discountAmount;

  public AmountDiscountMovie(String title, Duration runningTime, Money fee,
      Money discountAmount, DiscountCondition... discountConditions) {
    super(title, runningTime, fee, discountConditions);
    this.discountAmount = discountAmount;
  }

  @Override
  protected Money calculateDiscountAmount() {
    return discountAmount;
  }
}
