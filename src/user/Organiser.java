package user;

public class Organiser extends User
{
    public Organiser(Organiser organiser) {super(organiser);}
    public Organiser(String salt, String username, String password, String email, String name) {
        super(salt, username, password, email, name);
    }

    @Override
    public String toCSV() {
        return super.toCSV() + ",organiser\n";
    }
}