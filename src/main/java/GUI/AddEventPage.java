package GUI;

import artist.Artist;
import event.ActualEvent;
import event.Event;
import location.Location;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import service.MainService;
import utility.DateLabelFormatter;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Date;
import java.util.Properties;
import java.util.stream.Collectors;

public class AddEventPage extends JPanel
{
    JTextField name = new JTextField(10);
    JTextArea description = new JTextArea();
    FilterJList artists = new FilterJList<Artist>(MainService.getInstance().getArtists()) {
        @Override
        public void getStrings() {
            this.strings = values.stream().map(artist -> artist.getPseudonym()).collect(Collectors.toList());
        }
    };
    FilterJList locations = new FilterJList<Location>(MainService.getInstance().getLocations()) {
        @Override
        public void getStrings() {
            this.strings = values.stream().map(location -> location.getName() + "  " + location.getAddress()).collect(Collectors.toList());
        }
    };
    JTextField url = new JTextField();
    JRadioButton online = new JRadioButton("Online");
    JRadioButton live = new JRadioButton("Live");
    JButton deleteButton;
    Event event;


    AddEventPage(CardLayout layout, JPanel cards){
        setLayout(new GridBagLayout());
        setBorder(new TitledBorder("Add Event"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8 ,0 ,8);
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
        add(new JLabel("Type:"), gbc);
        gbc.gridy++;
        JLabel locationText = new JLabel("Location:");
        add(locationText, gbc);
        JLabel inviteLinkText = new JLabel("Invite link:");
        add(inviteLinkText, gbc);
        inviteLinkText.setVisible(false);

        gbc.gridx++;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        add(name, gbc);
        gbc.gridy++;

        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setMinimumSize(new Dimension(80,80));
        add(description, gbc);

        gbc.gridy++;
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        add(datePicker, gbc);
        gbc.gridy++;
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
        gbc.insets = new Insets(20, 0 ,0 ,0);
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener((actionEvent) -> {
            Location location = (Location) locations.getSelectedValue();
            if(event == null)
                MainService.getInstance().addEvent(name.getText(),
                        description.getText(), (Date) datePicker.getModel().getValue(), artists.getSelectedValues(),
                        live.isSelected() ? "live" : "online",
                        live.isSelected() ? location : null, live.isSelected() ? null: inviteLinkText.getText(), null);
            else{
                MainService.getInstance().editEvent(event, name.getText(),
                        description.getText(), (Date) datePicker.getModel().getValue(), artists.getSelectedValues(),
                        live.isSelected() ? "live" : "online",
                        live.isSelected() ? location : null, live.isSelected() ? null: inviteLinkText.getText());
            }
        });
        add(submitButton, gbc);
        gbc.gridx++;
        deleteButton = new JButton("Delete Event");
        deleteButton.addActionListener(e -> {
            System.out.println(event);
            MainService.getInstance().deleteEvent(event);
            addEvent();
        });
        deleteButton.setVisible(false);
        add(deleteButton, gbc);
    }

    void editEvent(Event event) {
        name.setText(event.getName());
        description.setText(event.getDescription());
        if (event instanceof ActualEvent)
            live.setSelected(true);
        else
            online.setSelected(true);
        deleteButton.setVisible(true);
        this.event = event;
    }

    void addEvent() {
        name.setText("");
        description.setText("");
        event = null;
        deleteButton.setVisible(false);
        live.setSelected(false);
        online.setSelected(false);
        deleteButton.setVisible(false);
    }
}
