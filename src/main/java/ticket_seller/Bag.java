package ticket_seller;

public class Bag {
    private Long amount;
    private Invitation invitation;
    private Ticket ticket;

    /***
     * 이벤트에 당첨된 관람객의 가방 안에는 현금, 초대장이 들어있음.
     * 이벤트에 당첨되지 않은 관람객의 가방 안에는 초대장이 없음.
     * @param amount
     */
    public Bag(Long amount) {
        this(null, amount);
    }

    public Bag(Invitation invitation, Long amount) {
        this.amount = amount;
        this.invitation = invitation;
    }

    public boolean hasInvitation() {
        return invitation != null;
    }

    public boolean hasTicket() {
        return ticket != null;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public void minusAmount(Long amount) {
        this.amount -= amount;
    }

    public void plusAmount(Long amount) {
        this.amount += amount;
    }

}
