package GUI;

import exceptions.NoOrganiserException;
import exceptions.NoTypeException;
import models.Event;
import models.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import service.MainService;
import utility.DateLabelFormatter;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.stream.Collectors;

public class EventPage extends JPanel {
    JTextField name = new JTextField(10);
    JTextArea description = new JTextArea();
    FilterJList<Artist> artists = new FilterJList<>(MainService.getInstance().getArtists()) {
        @Override
        public void getStrings() {
            this.strings = values.stream().map(Artist::getPseudonym).collect(Collectors.toList());
        }
    };
    FilterJList<Location> locations = new FilterJList<>(MainService.getInstance().getLocations()) {
        @Override
        public void getStrings() {
            this.strings = values.stream().map(location -> location.getName() + "  " + location.getAddress()).collect(Collectors.toList());
        }
    };
    FilterJList<TicketType> tickets = new FilterJList<TicketType>(new ArrayList<>()) {
        @Override
        public void getStrings() {
            this.strings = values.stream().map(type -> type.getType() + "  " + type.getPrice()).collect(Collectors.toList());
        }
    };
    JTextField url = new JTextField();
    JTextField actualDate = new JTextField();
    JTextField actualArtists = new JTextField();
    JDatePanelImpl datePanel;
    JDatePickerImpl datePicker;
    JRadioButton online = new JRadioButton("Online");
    JRadioButton live = new JRadioButton("Live");
    JButton deleteButton;
    JButton submitButton;
    JButton addToCart;
    JButton removeFromCart;
    JLabel inviteLinkText;
    JLabel locationText;
    JLabel ticketsText = new JLabel("Tickets");
    JLabel typeText = new JLabel("Type:");
    Event event;

    EventPage() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 0, 8);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Name:"), gbc);
        gbc.gridy++;
        add(new JLabel("Description:"), gbc);
        gbc.gridy++;
        add(new JLabel("Date:"), gbc);
        gbc.gridy++;
        add(new JLabel("Artists:"), gbc);
        gbc.gridy++;

        add(typeText, gbc);
        gbc.gridy++;
        locationText = new JLabel("Location:");
        add(locationText, gbc);
        inviteLinkText = new JLabel("Invite link:");
        add(inviteLinkText, gbc);
        inviteLinkText.setVisible(false);
        gbc.gridy++;
        add(ticketsText, gbc);

        gbc.gridx++;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        add(name, gbc);
        gbc.gridy++;

        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setMinimumSize(new Dimension(80, 80));
        add(description, gbc);

        gbc.gridy++;
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        add(datePicker, gbc);
        actualDate.setEditable(false);
        actualDate.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        add(actualDate, gbc);
        gbc.gridy++;
        actualArtists.setEditable(false);
        actualArtists.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        add(actualArtists, gbc);
        add(artists, gbc);
        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;

        live.setSelected(true);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(online);
        buttonGroup.add(live);
        add(live, gbc);
        gbc.gridx++;
        add(online, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        add(locations, gbc);
        add(url, gbc);
        gbc.gridy++;
        add(tickets, gbc);
        url.setVisible(false);

        live.addActionListener((e -> {
            url.setVisible(false);
            locations.setVisible(true);
            inviteLinkText.setVisible(false);
            locationText.setVisible(true);
        }));

        online.addActionListener(e -> {
            url.setVisible(true);
            locations.setVisible(false);
            inviteLinkText.setVisible(true);
            locationText.setVisible(false);
        });

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 0, 0, 0);
        submitButton = new JButton("Submit");
        submitButton.addActionListener((actionEvent) -> {
            Location location = locations.getSelectedValue();
            try {
                if (event == null)
                    MainService.getInstance().addEvent(name.getText(),
                            description.getText(), (Date) datePicker.getModel().getValue(), artists.getSelectedValues(),
                            live.isSelected() ? "live" : "online",
                            live.isSelected() ? location : null, live.isSelected() ? null : inviteLinkText.getText(), null);
                else {
                    MainService.getInstance().editEvent(event, name.getText(),
                            description.getText(), (Date) datePicker.getModel().getValue(), artists.getSelectedValues(),
                            live.isSelected() ? "live" : "online",
                            live.isSelected() ? location : null, live.isSelected() ? null : inviteLinkText.getText());
                }
            } catch (NoOrganiserException | NoTypeException e) {
                e.printStackTrace();
            }
        });
        add(submitButton, gbc);
        addToCart = new JButton("Add to cart");
        addToCart.addActionListener((actionEvent) -> {
            MainService.getInstance().addToCart(tickets.getSelectedValue());
        });
        add(addToCart, gbc);
        removeFromCart = new JButton("Remove from cart");
        add(removeFromCart, gbc);
        gbc.gridx++;
        deleteButton = new JButton("Delete Event");
        deleteButton.addActionListener(e -> {
            MainService.getInstance().deleteEvent(event);
            addEvent();
        });
        deleteButton.setVisible(false);
        add(deleteButton, gbc);
    }

    void editEvent(Event event) {
        setForm();
        setBorder(new TitledBorder("Edit Event"));
        name.setText(event.getName());
        description.setText(event.getDescription());
        if (event instanceof ActualEvent) {
            live.setSelected(true);
            url.setVisible(false);
            url.setText("");
            locations.setVisible(true);
        }
        else {
            online.setSelected(true);
            url.setVisible(true);
            url.setText(((VirtualEvent) event).getInviteLink());
            locations.setVisible(false);
        }
        deleteButton.setVisible(true);
        this.event = event;
        submitButton.setVisible(true);
        locations.setVisible(true);
        name.setEditable(true);
        description.setEditable(true);
        url.setEditable(true);
    }

    void addEvent() {
        setForm();
        setBorder(new TitledBorder("Add Event"));
        name.setText("");
        description.setText("");
        url.setText("");
        event = null;
        deleteButton.setVisible(false);
        live.setSelected(false);
        online.setSelected(false);
        submitButton.setVisible(true);
        locations.setVisible(true);
        name.setEditable(true);
        live.setSelected(true);
        url.setVisible(false);
        description.setEditable(true);
        url.setEditable(true);
    }

    private void setForm(){
        ticketsText.setVisible(false);
        tickets.setVisible(false);
        name.setEditable(true);
        name.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
        description.setEditable(true);
        description.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextArea.border"));
        description.setOpaque(true);
        url.setEditable(true);
        url.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
        deleteButton.setVisible(true);
        submitButton.setVisible(true);
        locations.setVisible(true);
        url.setVisible(true);
        live.setVisible(true);
        online.setVisible(true);
        typeText.setVisible(true);
        actualDate.setVisible(false);
        datePicker.setVisible(true);
        artists.setVisible(true);
        actualArtists.setVisible(false);
        addToCart.setVisible(false);
        removeFromCart.setVisible(false);
    }

    void viewEvent(Event event) {
        editEvent(event);
        setBorder(new TitledBorder("View Event"));
        name.setEditable(false);
        name.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        description.setEditable(false);
        description.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        description.setOpaque(false);
        ticketsText.setVisible(true);
        url.setEditable(false);
        url.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        deleteButton.setVisible(false);
        submitButton.setVisible(false);
        locations.setVisible(false);
        url.setVisible(true);
        live.setVisible(false);
        online.setVisible(false);
        typeText.setVisible(false);
        actualDate.setVisible(true);
        actualDate.setText(event.getDate().toString());
        datePicker.setVisible(false);
        artists.setVisible(false);
        actualArtists.setVisible(true);
        String text = event.getArtists().stream().map(Artist::getName).reduce("", (sub, e) -> sub + e + ", ");
        actualArtists.setText(text.length()>2 ? text.substring(0, text.length() - 2) : "");
        if (event instanceof VirtualEvent) {
            url.setText(((VirtualEvent) event).getInviteLink());
            locationText.setVisible(false);
            inviteLinkText.setVisible(true);
        }
        else if (event instanceof ActualEvent) {
            url.setText(((ActualEvent) event).getLocation().getName());
            locationText.setVisible(true);
            inviteLinkText.setVisible(false);
        }
        tickets.setVisible(true);
        tickets.bindData(MainService.getInstance().getAvailableTickets(event));
        addToCart.setVisible(true);
    }
}
