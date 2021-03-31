package transaction;

import transaction.Ticket;
import user.Client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class Payment
{
    // oare a meritat efortul pe care l-am depus ca sa fac clasa asta imutabila?
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
}
