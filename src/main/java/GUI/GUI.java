package GUI;

import service.MainService;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame
{
    private JPanel register;
    private JPanel login;
    private JPanel cards;
    private JPanel addEvent;
    private JPanel addLocation;
    private JPanel addArtist;
    private JPanel searchEvents;
    CardLayout layout;

    public GUI()
    {
        setLayout(new GridBagLayout());
        setTitle("Sad");
        setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                MainService.getInstance().saveData()));
        setResizable(false);
        setVisible(true);
        layout = new CardLayout();
        cards = new JPanel();
        login = new Login(layout, cards);
        register = new Register(layout, cards);
        addEvent = new AddEvent(layout, cards);
        searchEvents = new SearchEvents();
        addLocation = new AddLocation(layout, cards);
        addArtist = new AddArtist(layout, cards);

        cards.setPreferredSize(new Dimension(700,500));
        cards.setLayout(layout);
        cards.add(login, "login");
        cards.add(register, "register");
        cards.add(addEvent, "addEvent");
        cards.add(addLocation, "addLocation");
        cards.add(addArtist, "addArtist");
        cards.add(searchEvents, "searchEvents");
        layout.show(cards, "searchEvents");
        add(cards);
        setVisible(true);
    }
}
