package chap3;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class ReservationAgencyTest {

  @Test
  public void is_created_reservation() {
      Customer customer = new Customer("seansin", "dailyworker");
      List<DiscountCondition> discountConditions = new ArrayList<>();

      discountConditions.add(new DiscountCondition(DiscountConditionType.SEQUENCE, 1));
      discountConditions.add(new DiscountCondition(DiscountConditionType.SEQUENCE, 2));

      Movie terminator = new Movie("터미네이터 3",
        Duration.ofMinutes(109),
        new Money(BigDecimal.valueOf(10000)),
          discountConditions,
          MovieType.AMOUNT_DISCOUNT,
          Money.amountToWon(100)
      ,0);

      Screening screening = new Screening(terminator, 2, LocalDateTime.now());
      Reservation reserved = ReservationAgency.reserve(screening, customer, 2);
      Money money = new Money(BigDecimal.valueOf(19800.0));

      System.out.println(reserved.toString());

      assertEquals(reserved.getCustomer().getName(), "seansin");
      assertEquals(reserved.getScreening(), screening);
      assertEquals(reserved.getAudienceCount(), 2);
      assertEquals(reserved.getFee(), money);
  }
}