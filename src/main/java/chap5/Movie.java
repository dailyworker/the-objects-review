package chap5;

import java.time.Duration;
import java.util.List;

public class Movie {
  private String title;
  private Duration runningTime;
  private Money fee;
  // 리팩토링 변경 전
  private List<DiscountCondition> discountConditions;

  // 리팩토링 후 변경 사항
  private List<PeriodCondition> periodConditions;
  private List<SequenceCondition> sequenceConditions;

  private MovieType movieType;
  private Money discountAmount;
  private double discountPercent;

  public Money calculateMovieFee(Screening screening) {
    if(isDiscountable(screening)) {
      return fee.minus(calculateDiscountAmount());
    }
    return fee;
  }

  private Money calculateDiscountAmount() {
    switch (movieType) {
      case AMOUNT_DISCOUNT:
        return calculateAmountDiscountAmount();
      case PERCENT_DISCOUNT:
        return calculatePercentDiscountAmount();
      case NONE_DISCOUNT:
        return calculateNoneDiscountAmount();
    }
    throw new IllegalArgumentException();
  }

  private Money calculateAmountDiscountAmount() {
    return discountAmount;
  }

  private Money calculatePercentDiscountAmount() {
    return fee.times(discountPercent);
  }

  private Money calculateNoneDiscountAmount() {
    return Money.ZERO;
  }

  private boolean isDiscountable(Screening screening) {
    // 리팩토링 전
    //return discountConditions.stream()
    //    .anyMatch(condition -> condition.isSatisfiedBy(screening));
    return checkPeriodCondition(screening) || checkSequenceConditions(screening);
  }

  private boolean checkSequenceConditions(Screening screening) {
    return sequenceConditions.stream()
        .anyMatch(condition -> condition.isSatisfiedBy(screening));
  }

  private boolean checkPeriodCondition(Screening screening) {
    return periodConditions.stream()
        .anyMatch(condition -> condition.isSatisfiedBy(screening));
  }
}
