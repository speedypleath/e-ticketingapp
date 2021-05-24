package GUI;

import artist.Artist;
import auth.AuthActions;
import auth.LoginLogoutChain;
import event.Event;
import location.Location;
import service.MainService;

import javax.swing.*;
import java.awt.*;
import java.util.stream.Collectors;

public class MainPage extends JPanel implements AuthActions {
    private final JPanel left;
    private final JPanel cards;
    private final CardLayout layout;
    private final SearchPage<Event> searchEvents;
    private final SearchPage<Artist> searchArtists;
    private final SearchPage<Location> searchLocations;
    MainPage()
    {
        setLayout(new BorderLayout());

        setLayout(new BorderLayout());
        layout = new CardLayout();
        cards = new JPanel();
        AddEventPage addEvent = new AddEventPage(layout, cards);
        searchEvents = new SearchPage<Event>(cards, layout, addEvent, MainService.getInstance().getEvents()) {
            @Override
            public void getStrings() {
                this.strings = values.stream().map(event -> event.getName() + "    " + event.getDate().toString()).collect(Collectors.toList());
            }

            @Override
            public void action() {
                Event value = values.get(myJList.getSelectedIndex());
                addEventPage.editEvent(value);
                layout.show(cards, "addEvent");
            }
        };

        searchArtists = new SearchPage<Artist>(cards, layout, addEvent, MainService.getInstance().getArtists()) {
            @Override
            public void getStrings() {
                this.strings = values.stream().map(artist -> artist.getPseudonym()).collect(Collectors.toList());
            }

            @Override
            public void action() {
                Artist value = values.get(myJList.getSelectedIndex());
                layout.show(cards, "searchEvents");
                searchEvents.bindData(MainService.getInstance().getEventsByArtist(value));
            }
        };

        searchLocations = new SearchPage<Location>(cards, layout, addEvent, MainService.getInstance().getLocations()) {
            @Override
            public void getStrings() {
                this.strings = values.stream().map(location -> location.getName() + "   " + location.getAddress()).collect(Collectors.toList());
            }

            @Override
            public void action() {
                Location value = values.get(myJList.getSelectedIndex());
                layout.show(cards, "searchEvents");
                searchEvents.bindData(MainService.getInstance().getEventsByLocation(value));
            }
        };
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
        cards.add(searchEvents, "searchEvents");
        cards.add(searchArtists, "searchArtists");
        cards.add(searchLocations, "searchLocations");
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

        JButton searchArtistButton = new JButton("Search artist");
        searchArtistButton.setPreferredSize(new Dimension(150, 30));
        searchArtistButton.addActionListener((actionEvent) -> {
            layout.show(cards, "searchArtists");
            searchArtists.bindData(MainService.getInstance().getArtists());
        });
        left.add(searchArtistButton, gbc);
        gbc.gridy++;

        JButton searchLocationsButton = new JButton("Search location");
        searchLocationsButton.setPreferredSize(new Dimension(150, 30));
        searchLocationsButton.addActionListener((actionEvent) -> {
            layout.show(cards, "searchLocations");
            searchLocations.bindData(MainService.getInstance().getLocations());
        });
        left.add(searchLocationsButton, gbc);
        gbc.gridy++;

        if(user.equals("client")) {
            JButton searchEventButton = new JButton("Search event");
            searchEventButton.setPreferredSize(new Dimension(150, 30));
            searchEventButton.addActionListener((actionEvent) -> {
                layout.show(cards, "searchEvents");
                searchEvents.bindData(MainService.getInstance().getEvents());
            });
            left.add(searchEventButton, gbc);
            gbc.gridy++;
        }

        if(user.equals("client") || user.equals("organiser")) {
            JButton userEventsButton = new JButton("Your events");
            userEventsButton.setPreferredSize(new Dimension(150, 30));
            userEventsButton.addActionListener((actionEvent) -> {
                layout.show(cards, "searchEvents");
                searchEvents.bindData(MainService.getInstance().getUserEvents());
            });
            left.add(userEventsButton, gbc);
            gbc.gridy++;
        }

        if(user.equals("admin")) {
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

        if (user.equals("organiser")) {
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
