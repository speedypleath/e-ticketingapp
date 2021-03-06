package models;

import java.util.Set;

public final class Client extends User
{
    private Set<Ticket> tickets;

    public Client(String salt, String username, String password, String email, String name) {
        super(salt, username, password, email, name);
    }

    public Client(Client client) { super(client);}

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void addTicket(Ticket ticket) {
        this.tickets.add(ticket);
    }

    public void removeTicket(Ticket ticket){
        this.tickets.remove(ticket);
    }

    @Override
    public String toCSV() {
        return super.toCSV() + ",client\n";
    }
}