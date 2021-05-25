package GUI;

import models.Event;
import service.MainService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Vector;
import java.util.stream.Collectors;

public class TicketPage extends JPanel{
    private final JTextField no;
    private final JTextField price;
    private final JTextField type;
    private final JButton submitButton;
    FilterJList<Event> events;
    TicketPage(){
        setLayout(new GridBagLayout());
        setBorder(new TitledBorder("Add Location"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8 ,0 ,8);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Event:"), gbc);
        gbc.gridy++;
        add(new JLabel("Number of tickets:"), gbc);
        gbc.gridy++;
        add(new JLabel("Price:"), gbc);
        gbc.gridy++;
        add(new JLabel("Type of ticket:"), gbc);
        gbc.gridx++;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        events = new FilterJList<Event>(new Vector<>()) {
            @Override
            public void getStrings() {
                this.strings = values.stream().map(Event::getName).collect(Collectors.toList());
            }
        };
        add(events, gbc);
        gbc.gridy++;
        no = new JTextField(10);
        add(no, gbc);
        gbc.gridy++;
        price = new JTextField(10);
        add(price, gbc);
        gbc.gridy++;
        type = new JTextField(10);
        add(type, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 0 ,0 ,0);
        submitButton = new JButton("Submit");
        submitButton.addActionListener((e ->
        {
            MainService.getInstance().createTickets(events.getSelectedValue(), Integer.valueOf(no.getText()), Integer.valueOf(price.getText()), type.getText());
        }
        ));
        add(submitButton, gbc);
    }

    public void setEvents(){
        events.bindData(MainService.getInstance().getUserEvents());
    }
}
