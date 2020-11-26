package chap1;

public class TicketSeller {
    private TicketOffice ticketOffice;

    public TicketSeller(TicketOffice ticketOffice) {
        this.ticketOffice = ticketOffice;
    }

    public void SellTo(Audience audience) {
        ticketOffice.sellTicketTo(audience);
    }
}
