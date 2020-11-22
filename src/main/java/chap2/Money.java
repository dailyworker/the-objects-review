package chap2;

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

    public static Money amountToWon(double amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public Money plus(Money money) {
        return new Money(this.amount.add(money.amount));
    }

    public Money minus(Money money) {
        return new Money(this.amount.subtract(money.amount));
    }

    public Money times(double percent) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(percent)));
    }

    public boolean isLessThan(Money other) {
        return amount.compareTo(other.amount) < 0;
    }

    public boolean isGreaterThanOrEqaul(Money other) {
        return amount.compareTo(other.amount) >= 0;
    }


    @Override
    public String toString() {
        return "Money{" +
                "amount=" + amount +
                '}';
    }
}
