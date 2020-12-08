package chap3;

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

    public DiscountCondition(DiscountConditionType type, int sequence) {
        if(validateType(type)) {
            this.type = type;
            this.sequence = sequence;
        } else {
            throw new IllegalArgumentException("Type이 잘못 되었습니다.");
        }
    }

    public DiscountCondition(DiscountConditionType type, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        if(!validateType(type)) {
            this.type = type;
            this.dayOfWeek = dayOfWeek;
            this.startTime = startTime;
            this.endTime = endTime;
        } else {
            throw new IllegalArgumentException("Type이 잘못 되었습니다.");
        }
    }

    private boolean validateType(DiscountConditionType type) {
        return type == DiscountConditionType.SEQUENCE;
    }

    public boolean isDiscountable(DayOfWeek dayOfWeek, LocalTime time) {
        if(type != DiscountConditionType.PERIOD){
            throw new IllegalArgumentException("PERIOD 타입의 할인 정책이 아닙니다.");
        }

        return this.dayOfWeek.equals(dayOfWeek) &&
                this.startTime.compareTo(time) <= 0 &&
                this.endTime.compareTo(time)  >= 0;

    }

    public boolean isDiscountable(int sequence) {
        if(type != DiscountConditionType.SEQUENCE) {
            throw new IllegalArgumentException("SEQUENCE 타입의 할인 정책이 아닙니다.");
        }

        return this.sequence == sequence;
    }

}
