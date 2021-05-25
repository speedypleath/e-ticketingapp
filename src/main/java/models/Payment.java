package models;

import java.util.Date;
import java.util.UUID;

public final class Payment
{
    final private Long id;
    final private Client client;
    final private Date date;

    public Payment(Client client)
    {
        this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        this.client = new Client(client);
        this.date = new Date();
    }

    public Payment(Long id, Client client, Date date)
    {
        this.id = id;
        this.client = new Client(client);
        this.date = date;
    }

    public Payment(Payment payment){
        this.id = payment.getId();
        this.client = new Client((Client) payment.getClient());
        this.date = payment.getDate();
    }

    public Long getId() { return  id; }

    public User getClient() {
        return client;
    }

    public Date getDate() { return date; }
}
