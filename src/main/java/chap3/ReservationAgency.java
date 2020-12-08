package chap3;

public class ReservationAgency {
   public static Reservation reserve(Screening screening, Customer customer, int audienceCount) {
       Money fee = screening.calculateFee(audienceCount);
       return new Reservation(customer, screening, fee, audienceCount);
   }
}
