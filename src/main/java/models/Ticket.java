package models;

import java.util.UUID;

public class Ticket
{
    private final Long id;
    private final TicketType type;

    public Ticket(TicketType ticketType){
        this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        this.type = new TicketType(ticketType);
    }

    public Ticket(Ticket ticket)
    {
        this.id = ticket.getId();
        this.type = new TicketType(ticket.getType().getPrice(), ticket.getType().getType(), ticket.getType().getEvent());
    }

    public Long getId() {
        return id;
    }

    public TicketType getType() {
        return type;
    }
}
