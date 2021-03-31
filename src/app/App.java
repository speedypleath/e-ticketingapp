package app;

import event.ActualEvent;
import location.Location;
import user.*;

public class App
{
    User user;
    void createEvent(Organiser organiser)
    {

    }

    public static void main(String[] args) {
        Organiser organiser = new Organiser("speed");
        Location location = new Location();
        ActualEvent event = new ActualEvent.Builder()
                .name("test")
                .organiser(organiser)
                .location(location)
                .description("lalala")
                .build();
        System.out.println(event.toString());
    }
}
