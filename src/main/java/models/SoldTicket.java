package models;

public final class SoldTicket extends Ticket{
    private final Payment payment;

    public SoldTicket(TicketType type, Payment payment) {
        super(type);
        this.payment = new Payment(payment);
    }

    public SoldTicket(SoldTicket ticket) {
        super(ticket);
        this.payment = new Payment(ticket.payment);
    }

    public Payment getPayment() {
        return payment;
    }
}
