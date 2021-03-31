package user;

import event.Event;

public class UserServices
{
    private User user;
    private static UserServices instance;

    private UserServices(){}

    public static UserServices getInstance()
    {
        if(instance == null)
            instance = new UserServices();
        return instance;
    }

    void login(User user)
    {
        this.user = user;
    }

    void logout()
    {
        this.user = null;
    }

    void createEvent()
    {

    }

    void deleteEvent(Event event)
    {

    }

    void updateEvent(Event event)
    {

    }
}
