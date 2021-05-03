package transaction;

import event.ActualEvent;
import event.Event;
import event.VirtualEvent;
import user.Client;
import user.User;

public class Ticket
{
    private Event event;
    private Client client;
    private int price;
    private String additionalInfo;

    public Ticket(Ticket ticket)
    {
        this.price = ticket.price;
        if(ticket.event instanceof VirtualEvent)
            this.event = new VirtualEvent((VirtualEvent) ticket.event);
        else if(ticket.event instanceof ActualEvent)
            this.event = new ActualEvent((ActualEvent) ticket.event);
        this.client = new Client(ticket.client);
        this.additionalInfo = ticket.additionalInfo;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
