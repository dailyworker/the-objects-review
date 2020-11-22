package chap2;

import chap2.policy.*;
import chap2.policy.condition.PeriodCondition;
import chap2.policy.condition.SequenceCondition;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.*;

import static org.junit.Assert.*;

public class MovieTest {

    @Test
    public void is_adopt_amount_discount_policy() {
        Movie avatar = new Movie("아바타",
                            Duration.ofMinutes(162),
                            Money.amountToWon(10000),
                            new AmountDiscountPolicy(Money.amountToWon(800),
                                    new SequenceCondition(1),
                                    new SequenceCondition(10),
                                    new PeriodCondition(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 59)),
                                    new PeriodCondition(DayOfWeek.THURSDAY, LocalTime.of(10, 0), LocalTime.of(20, 59))
                            ));

        Screening screening = new Screening(avatar, 1, LocalDateTime.of(LocalDate.of(2020, 11, 23), LocalTime.of(10, 23)));
        Money actual = avatar.calculateMovieFee(screening);
        Money expected = new Money(BigDecimal.valueOf(9200));
        System.out.println("adopted amount discount policy movie fee is : " + expected.toString());
        assertTrue(actual.isGreaterThanOrEqaul(expected));
    }

    @Test
    public void is_adopt_percent_discount_policy() {
        Movie terminator = new Movie("터미네이터 3",
                                        Duration.ofMinutes(109),
                                        Money.amountToWon(10000),
                                        new PercentDiscountPolicy(0.1,
                                            new PeriodCondition(DayOfWeek.TUESDAY, LocalTime.of(14, 0), LocalTime.of(16, 59)),
                                            new SequenceCondition(2),
                                            new PeriodCondition(DayOfWeek.THURSDAY, LocalTime.of(10, 0), LocalTime.of(13, 59))
                                        ));

        Screening screening = new Screening(terminator, 2, LocalDateTime.of(LocalDate.of(2020, 11, 26), LocalTime.of(14, 23)));
        Money actual = terminator.calculateMovieFee(screening);
        Money expected = new Money(BigDecimal.valueOf(9000));
        System.out.println("adopted percent discount policy movie fee is : " + expected.toString());
        assertTrue(actual.isGreaterThanOrEqaul(expected));
    }
}