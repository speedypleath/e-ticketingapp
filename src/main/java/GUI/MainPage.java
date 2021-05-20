package GUI;

import auth.AuthActions;
import auth.LoginLogoutChain;
import service.MainService;

import javax.swing.*;
import java.awt.*;

public class MainPage extends JPanel implements AuthActions {
    private JPanel left;
    private JPanel cards;
    private CardLayout layout;
    private SearchEventsPage searchEvents;
    MainPage()
    {
        setLayout(new BorderLayout());

        setLayout(new BorderLayout());
        layout = new CardLayout();
        cards = new JPanel();
        JPanel addEvent = new AddEventPage(layout, cards);
        searchEvents = new SearchEventsPage();
        JPanel addLocation = new AddLocationPage(layout, cards);
        JPanel addArtist = new AddArtistPage(layout, cards);
        JPanel empty = new JPanel();
        empty.setLayout(new BorderLayout());
        JLabel welcome = new JLabel("<html><div style='text-align: center;'>Welcome</div></html>");
        welcome.setHorizontalAlignment(SwingConstants.CENTER);
        welcome.setVerticalAlignment(SwingConstants.CENTER);
        empty.add(welcome, BorderLayout.CENTER);
        cards.setLayout(layout);
        cards.setPreferredSize(new Dimension(800,225));
        cards.add(empty, "empty");
        cards.add(addEvent, "addEvent");
        cards.add(addLocation, "addLocation");
        cards.add(addArtist, "addArtist");
        cards.add(searchEvents, "search");
        layout.show(cards, "empty");
        add(cards, BorderLayout.CENTER);
        left = new JPanel();
    }

    public void buildButtons()
    {
        left.setLayout(new GridBagLayout());
        add(left, BorderLayout.LINE_START);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8 ,0 ,8);
        gbc.gridx = 0;
        gbc.gridy = 0;

        String user = MainService.getInstance().getUserRole();
        System.out.println(user);

        if(user == "client") {
            JButton searchEventButton = new JButton("Search event");
            searchEventButton.setPreferredSize(new Dimension(150, 30));
            searchEventButton.addActionListener((actionEvent) -> {
                layout.show(cards, "search");
                searchEvents.bindData(MainService.getInstance().getEvents());
            });
            left.add(searchEventButton, gbc);
            gbc.gridy++;
        }

        if(user == "client" || user == "organiser") {
            JButton userEventsButton = new JButton("Your events");
            userEventsButton.setPreferredSize(new Dimension(150, 30));
            userEventsButton.addActionListener((actionEvent) -> {
                layout.show(cards, "search");
                searchEvents.bindData(MainService.getInstance().getUserEvents());
            });
            left.add(userEventsButton, gbc);
            gbc.gridy++;
        }

        if(user == "admin") {
            JButton addLocationButton = new JButton("Add location");
            addLocationButton.setPreferredSize(new Dimension(150, 30));
            addLocationButton.addActionListener((actionEvent) -> layout.show(cards, "addLocation"));
            left.add(addLocationButton, gbc);
            gbc.gridy++;

            JButton addArtistButton = new JButton("Add artist");
            addArtistButton.setPreferredSize(new Dimension(150, 30));
            addArtistButton.addActionListener((actionEvent) -> layout.show(cards, "addArtist"));
            left.add(addArtistButton, gbc);
            gbc.gridy++;
        }

        if (user == "organiser") {
            JButton addEventButton = new JButton("Add event");
            addEventButton.setPreferredSize(new Dimension(150, 30));
            addEventButton.addActionListener((actionEvent) -> layout.show(cards, "addEvent"));
            left.add(addEventButton, gbc);
            gbc.gridy++;
        }

        JButton logOutButton = new JButton("Log out");
        logOutButton.setPreferredSize(new Dimension(150, 30));
        logOutButton.addActionListener((actionEvent) -> logout());
        left.add(logOutButton, gbc);
    }

    @Override
    public boolean login(String username, String password) {
        left.removeAll();
        buildButtons();
        left.revalidate();
        return true;
    }

    @Override
    public void setNextAction(AuthActions nextAction) {

    }

    @Override
    public void logout()
    {
        LoginLogoutChain chain = new LoginLogoutChain();
        layout.show(cards, "empty");
        chain.logout();
    }
}
