package GUI;

import auth.AuthActions;
import auth.LoginLogoutChain;
import models.Artist;
import models.Event;
import models.Location;
import models.Ticket;
import service.MainService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MainPage extends JPanel implements AuthActions {
    private final JPanel left;
    private final JPanel cards;
    private final CardLayout layout;
    private final SearchPage<Event> searchEvents;
    private final SearchPage<Artist> searchArtists;
    private final SearchPage<Location> searchLocations;
    private final SearchPage<Ticket> shoppingCart;
    private final EventPage eventPage;
    private final ArtistPage addArtist;
    private final LocationPage addLocation;
    private final TicketView ticketView;
    private final JButton buy;
    private TicketPage ticketPage;
    MainPage()
    {
        setLayout(new BorderLayout());

        setLayout(new BorderLayout());
        layout = new CardLayout();
        cards = new JPanel();
        eventPage = new EventPage();
        addLocation = new LocationPage(layout, cards);
        addArtist = new ArtistPage(layout, cards);
        ticketPage = new TicketPage();
        ticketView = new TicketView();
        JPanel empty = new JPanel();
        empty.setLayout(new BorderLayout());
        JLabel welcome = new JLabel("<html><div style='text-align: center;'>Welcome</div></html>");
        welcome.setHorizontalAlignment(SwingConstants.CENTER);
        welcome.setVerticalAlignment(SwingConstants.CENTER);
        empty.add(welcome, BorderLayout.CENTER);
        searchEvents = new SearchPage<Event>(cards, layout, eventPage, MainService.getInstance().getEvents()) {
            @Override
            public void getStrings() {
                this.strings = values.stream().map(event -> event.getName() + "    " + event.getDate().toString()).collect(Collectors.toList());
            }

            @Override
            public void action1() {
                Event value = values.get(myJList.getSelectedIndex());
                eventPage.viewEvent(value);
                layout.show(cards, "eventPage");
            }

            @Override
            public void action2() {
                Event value = values.get(myJList.getSelectedIndex());
                eventPage.editEvent(value);
                layout.show(cards, "eventPage");
            }
        };

        searchArtists = new SearchPage<Artist>(cards, layout, eventPage, MainService.getInstance().getArtists()) {
            @Override
            public void getStrings() {
                this.strings = values.stream().map(Artist::getPseudonym).collect(Collectors.toList());
            }

            @Override
            public void action1() {
                Artist value = values.get(myJList.getSelectedIndex());
                layout.show(cards, "searchEvents");
                searchEvents.bindData(MainService.getInstance().getEventsByArtist(value));
            }

            @Override
            public void action2() {
                Artist value = values.get(myJList.getSelectedIndex());
                addArtist.edit(value);
                layout.show(cards, "addArtist");
            }
        };

        searchLocations = new SearchPage<Location>(cards, layout, eventPage, MainService.getInstance().getLocations()) {
            @Override
            public void getStrings() {
                this.strings = values.stream().map(location -> location.getName() + "   " + location.getAddress()).collect(Collectors.toList());
            }

            @Override
            public void action1() {
                Location value = values.get(myJList.getSelectedIndex());
                layout.show(cards, "searchEvents");
                searchEvents.bindData(MainService.getInstance().getEventsByLocation(value));
            }

            @Override
            public void action2() {
                Location value = values.get(myJList.getSelectedIndex());
                addLocation.edit(value);
                layout.show(cards, "addLocation");
            }
        };

        shoppingCart = new SearchPage<Ticket>(cards, layout, eventPage, new ArrayList<>()) {
            @Override
            public void getStrings() {
                this.strings = values.stream().map(ticket ->
                        MainService.getInstance().getEventById(ticket.getType().getEvent().getId()).getName()
                                + "   "  + ticket.getType().getType()).collect(Collectors.toList());
            }

            @Override
            public void action1() {
                Ticket value = values.get(myJList.getSelectedIndex());
                layout.show(cards, "ticketView");
                ticketView.setTicket(value);
            }

            @Override
            public void action2() {
            }
        };

        JPanel shoppingCartWrap = new JPanel();
        shoppingCartWrap.setLayout(new BoxLayout(shoppingCartWrap, BoxLayout.PAGE_AXIS));
        JPanel panel1 = new JPanel(new BorderLayout());
        shoppingCart.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel1.add(shoppingCart);
        shoppingCartWrap.add(panel1);
        JPanel panel2 = new JPanel(new BorderLayout());
        buy = new JButton("Buy");
        buy.addActionListener(e -> MainService.getInstance().pay());
        buy.setAlignmentX(0.5F);
        panel2.add(buy);
        shoppingCartWrap.add(panel2);

        cards.setLayout(layout);
        cards.setPreferredSize(new Dimension(800,225));
        cards.add(empty, "empty");
        cards.add(eventPage, "eventPage");
        cards.add(addLocation, "addLocation");
        cards.add(addArtist, "addArtist");
        cards.add(searchEvents, "searchEvents");
        cards.add(searchArtists, "searchArtists");
        cards.add(searchLocations, "searchLocations");
        cards.add(ticketPage, "ticketPage");
        cards.add(ticketView, "ticketView");
        cards.add(shoppingCartWrap, "shoppingCart");
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
            searchEvents.setCurrentAction("first");
            left.add(searchEventButton, gbc);
            gbc.gridy++;

            JButton shoppingCartButton = new JButton("Shopping cart");
            shoppingCartButton.setPreferredSize(new Dimension(150, 30));
            shoppingCartButton.addActionListener((actionEvent) -> {
                layout.show(cards, "shoppingCart");
                shoppingCart.bindData(MainService.getInstance().getShoppingCart());
            });
            searchEvents.setCurrentAction("first");
            left.add(shoppingCartButton, gbc);
            gbc.gridy++;
        }

        if(user.equals("client") || user.equals("organiser")) {
            searchLocations.setCurrentAction("first");
            searchArtists.setCurrentAction("first");
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
            searchLocations.setCurrentAction("second");
            searchArtists.setCurrentAction("second");
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
            ticketPage.setEvents();
            JButton eventPageButton = new JButton("Add event");
            eventPageButton.setPreferredSize(new Dimension(150, 30));
            searchEvents.setCurrentAction("second");
            eventPageButton.addActionListener((actionEvent) -> {
                layout.show(cards, "eventPage");
                eventPage.addEvent();
            });
            left.add(eventPageButton, gbc);
            gbc.gridy++;
            JButton addTicketsButton = new JButton("Add tickets");
            addTicketsButton.setPreferredSize(new Dimension(150, 30));
            addTicketsButton.addActionListener((actionEvent) -> {
                layout.show(cards, "ticketPage");
            });
            left.add(addTicketsButton, gbc);
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
