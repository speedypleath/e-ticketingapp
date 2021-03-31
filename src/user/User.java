package user;

import event.Event;

import java.util.List;

public class User
{
    private String username;

    User(){}
    User(String username){ this.username = username; }
    public String getUsername() { return username; }
}
