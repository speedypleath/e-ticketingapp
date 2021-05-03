package user;

public class Administrator extends User
{
    Administrator(String salt, String username, String password, String email, String name) {
        super(salt, username, password, email, name);
    }
}
