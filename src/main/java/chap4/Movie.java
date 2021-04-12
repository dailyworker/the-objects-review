package chap4;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Movie {
  private String title;
  private Duration runningTime;
  private Money fee;
  private List<DiscountCondition> discountConditions;

  private MovieType movieType;
  private Money discountAmount;
  private double discountPercent;

  public MovieType getMovieType() {
    return movieType;
  }

  public Money calculateAmountDiscountedFee() {
    if(movieType != MovieType.AMOUNT_DISCOUNT) {
      throw new IllegalArgumentException("올바르지 않은 값입니다.");
    }
    return fee.minus(discountAmount);
  }

  public Money calculatePercentDiscountedFee() {
    if(movieType != MovieType.PERCENT_DISCOUNT) {
      throw new IllegalArgumentException("올바르지 않은 값입니다.");
    }
    return fee.minus(fee.times(discountPercent));
  }

  public Money calculateNoneDiscountFee() {
    if(movieType != MovieType.NONE_DISCOUNT) {
      throw new IllegalArgumentException("올바르지 않은 값입니다.");
    }
    return fee;
  }

  public boolean isDiscountable(LocalDateTime whenScreened, int sequence) {
    for(DiscountCondition condition : discountConditions) {
      if (discountableByCondition(whenScreened, sequence, condition)) {
        return true;
      }
    }
    return false;
  }

  private boolean discountableByCondition(LocalDateTime whenScreened, int sequence, DiscountCondition condition) {
    if(condition.getType() == DiscountConditionType.PERIOD) {
      return condition.isCountable(whenScreened.getDayOfWeek(), whenScreened.toLocalTime());
    } else {
      return condition.isCountable(sequence);
    }
  }

  public void setMovieType(MovieType movieType) {
    this.movieType = movieType;
  }

  public Money getFee() {
    return fee;
  }

  public void setFee(Money fee) {
    this.fee = fee;
  }

  public List<DiscountCondition> getDiscountConditions() {
    return discountConditions;
  }

  public void setDiscountConditions(List<DiscountCondition> discountConditions) {
    this.discountConditions = discountConditions;
  }

  public Money getDiscountAmount() {
    return discountAmount;
  }

  public void setDiscountAmount(Money discountAmount) {
    this.discountAmount = discountAmount;
  }

  public double getDiscountPercent() {
    return discountPercent;
  }

  public void setDiscountPercent(double discountPercent) {
    this.discountPercent = discountPercent;
  }
}
