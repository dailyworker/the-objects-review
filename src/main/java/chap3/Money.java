package chap3;

import java.math.BigDecimal;

public class Money {
    public static final Money ZERO = Money.amountToWon(0);

    private final BigDecimal amount;

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    public static Money amountToWon(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public Money times(double percent) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(percent)));
    }

    public Money minus(Money discountAmount) {
        return new Money(this.amount.subtract(discountAmount.amount));
    }
}
