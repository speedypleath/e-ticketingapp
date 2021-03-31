package transaction;

import event.ActualEvent;
import event.Event;
import event.VirtualEvent;
import user.Client;
import user.Organiser;

public class Ticket
{
    private Event event;
    private Client client;
    private int price;
    private String additionalInfo;

    public Ticket(Ticket ticket) {
    }
    /*
    Ticket(Client client, Event event)
    {
        this.client = client;
        this.event = event.
    }

    public Ticket(Ticket ticket)
    {
        ActualEvent test = new ActualEvent((ActualEvent) event);
        test.
        this.price = ticket.price;
        if(ticket.event instanceof VirtualEvent)
            this.event = new VirtualEvent((VirtualEvent) ticket.event);
        else if(ticket.event instanceof ActualEvent)
            this.event = new ActualEvent((ActualEvent) ticket.event);
        this.client = new Client(ticket.client);
        this.additionalInfo = ticket.additionalInfo;
    }

    public Event getEvent() {
        if(event instanceof VirtualEvent)
            return new VirtualEvent((VirtualEvent) event);
        else if(event instanceof ActualEvent)
            return new ActualEvent((ActualEvent) event);
        return null;
    }

    public Client getClient() {
        return new Client(client);
    }

    public int getPrice() {
        return price;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }*/
}
