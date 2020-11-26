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
        System.out.println("adopted amount discount policy movie fee is : " + actual.toString());
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
        System.out.println("adopted percent discount policy movie fee is : " + actual.toString());
        assertTrue(actual.isGreaterThanOrEqaul(expected));
    }

    @Test
    public void is_none_discount_policy() {
        Movie starWars = new Movie("스타워즈", Duration.ofMinutes(210), Money.amountToWon(10000), new NoneDiscountPolicy());
        Screening screening = new Screening(starWars, 1,
                LocalDateTime.of(LocalDate.of(2020, 11, 24),
                LocalTime.of(14, 25)));
        Money actual = starWars.calculateMovieFee(screening);
        Money expected = new Money(BigDecimal.valueOf(10001));
        System.out.println("none discount policy movie fee is : " + actual.toString());
        assertTrue(actual.isLessThan(expected));
    }

    @Test
    public void is_change_discount_policy_in_movie() {
        Movie avatar = new Movie("아바타",
                Duration.ofMinutes(162),
                Money.amountToWon(10000),
                new AmountDiscountPolicy(Money.amountToWon(800),
                        new SequenceCondition(1),
                        new SequenceCondition(10),
                        new PeriodCondition(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 59)),
                        new PeriodCondition(DayOfWeek.THURSDAY, LocalTime.of(10, 0), LocalTime.of(20, 59))
                )
        );

        avatar.changeDiscountPolicy(
                new PercentDiscountPolicy(0.1,
                        new PeriodCondition(DayOfWeek.TUESDAY, LocalTime.of(14, 0), LocalTime.of(16, 59)),
                        new SequenceCondition(2),
                        new PeriodCondition(DayOfWeek.THURSDAY, LocalTime.of(10, 0), LocalTime.of(13, 59))
                )
        );

        Screening screening = new Screening(avatar, 2, LocalDateTime.of(LocalDate.of(2020, 11, 26), LocalTime.of(14, 23)));
        Money actual = avatar.calculateMovieFee(screening);
        Money expected = new Money(BigDecimal.valueOf(9000));
        System.out.println("adopted percent discount policy movie fee is : " + actual.toString());
        assertTrue(actual.isGreaterThanOrEqaul(expected));
    }

}