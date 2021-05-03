package transaction;

import user.Client;
import user.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class Payment
{
    final private Invoice invoice;
    final private Boolean refundable;
    final private List<Ticket> tickets;
    final private Client client;

    public Payment(List<Ticket> tickets, Client client, Boolean refundable)
    {
        this.invoice = new Invoice(new Date());
        this.refundable = refundable;
        this.tickets = new ArrayList<>();
        for(var ticket : tickets)
            this.tickets.add(new Ticket(ticket));
        this.client = new Client(client);
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public Boolean getRefundable() {
        return refundable;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public User getClient() {
        return client;
    }
}
