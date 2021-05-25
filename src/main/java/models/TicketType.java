package models;

import java.util.Objects;
import java.util.UUID;

public final class TicketType
{
    private final Long id;
    private final Integer price;
    private final String type;
    private final Event event;

    public TicketType(TicketType ticketType) {
        this.id = ticketType.getId();
        this.price = ticketType.getPrice();
        this.type = ticketType.getType();
        if(ticketType.getEvent() instanceof VirtualEvent)
            this.event = new VirtualEvent((VirtualEvent) ticketType.getEvent());
        else
            this.event = new ActualEvent((ActualEvent) ticketType.getEvent());
    }


    public TicketType(Long id, Event event, String type, Integer price) {
        this.id = id;
        this.price = price;
        this.type = type;
        if(event instanceof VirtualEvent)
            this.event = new VirtualEvent((VirtualEvent) event);
        else
            this.event = new ActualEvent((ActualEvent) event);
    }

    public TicketType(Integer price, String type, Event event) {
        this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        this.price = price;
        this.type = type;
        if(event instanceof VirtualEvent)
            this.event = new VirtualEvent((VirtualEvent) event);
        else
            this.event = new ActualEvent((ActualEvent) event);
    }

    public Integer getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public Event getEvent() {
        return event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketType that = (TicketType) o;
        return Objects.equals(price, that.price) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, type);
    }

    public Long getId() {
        return id;
    }
}
