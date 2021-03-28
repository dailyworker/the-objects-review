package chap4;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class DiscountCondition {
  private DiscountConditionType type;

  private int sequence;

  private DayOfWeek dayOfWeek;
  private LocalTime startTime;
  private LocalTime endTime;

  public DiscountConditionType getType() {
    return type;
  }

  public boolean isCountable(DayOfWeek dayOfWeek, LocalTime time) {
    if(type != DiscountConditionType.PERIOD) {
      throw new IllegalArgumentException("올바르지 않은 값입니다.");
    }
    return this.dayOfWeek.equals(dayOfWeek) &&
        this.startTime.compareTo(time) <= 0 &&
        this.endTime.compareTo(time) >= 0;
  }

  public boolean isCountable(int sequence) {
    if(type != DiscountConditionType.SEQUENCE) {
      throw new IllegalArgumentException("올바르지 않은 값입니다.");
    }
    return this.sequence == sequence;
  }

  public void setType(DiscountConditionType type) {
    this.type = type;
  }

  public int getSequence() {
    return sequence;
  }

  public void setSequence(int sequence) {
    this.sequence = sequence;
  }

  public DayOfWeek getDayOfWeek() {
    return dayOfWeek;
  }

  public void setDayOfWeek(DayOfWeek dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalTime startTime) {
    this.startTime = startTime;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalTime endTime) {
    this.endTime = endTime;
  }
}
