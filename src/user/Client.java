package user;

public class Client extends User
{

    public Client(String username) {
        super(username);
    }

    public Client(Client client) {
        super(client.getUsername());
    }
}
