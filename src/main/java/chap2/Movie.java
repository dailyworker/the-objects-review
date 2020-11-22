package chap2;

import chap2.policy.DiscountPoilcy;

import java.time.Duration;

public class Movie {
    private String title;
    private Duration runningTime;
    private Money fee;
    private DiscountPoilcy discountPoilcy;

    public Movie(String title, Duration runningTime, Money fee, DiscountPoilcy discountPoilcy) {
        this.title = title;
        this.runningTime = runningTime;
        this.fee = fee;
        this.discountPoilcy = discountPoilcy;
    }

    public Money getFee() {
        return fee;
    }

    public Money calculateMovieFee(Screening screening) {
        return fee.minus(discountPoilcy.calculateDiscountAmount(screening));
    }
}
