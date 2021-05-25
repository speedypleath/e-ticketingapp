package models;

public final class SoldTicket extends Ticket{
    private final Client client;

    public SoldTicket(TicketType type, Client client) {
        super(type);
        this.client = new Client(client);
    }

    public SoldTicket(SoldTicket ticket) {
        super(ticket);
        this.client = new Client(ticket.client);
    }

    public User getClient() {
        return client;
    }
}
