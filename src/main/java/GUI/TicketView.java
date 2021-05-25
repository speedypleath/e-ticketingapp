package GUI;

import models.Ticket;
import service.MainService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class TicketView extends JPanel
{
    private final JTextField event;
    private final JTextField price;
    private final JTextField type;
    private final JButton remove;
    private Ticket ticket;
    TicketView(){
        setLayout(new GridBagLayout());
        setBorder(new TitledBorder("Add Location"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8 ,0 ,8);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Event:"), gbc);
        gbc.gridy++;
        add(new JLabel("Price:"), gbc);
        gbc.gridy++;
        add(new JLabel("Type:"), gbc);
        gbc.gridx++;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        event = new JTextField(10);
        event.setEditable(false);
        event.setBorder(BorderFactory.createEmptyBorder());
        add(event, gbc);
        gbc.gridy++;
        price = new JTextField(10);
        price.setEditable(false);
        price.setBorder(BorderFactory.createEmptyBorder());
        add(price, gbc);
        gbc.gridy++;
        type = new JTextField(10);
        type.setEditable(false);
        type.setBorder(BorderFactory.createEmptyBorder());
        add(type, gbc);
        remove = new JButton("Remove from cart");
        remove.addActionListener(e -> {
            MainService.getInstance().removeFromCart(ticket);
        });
        gbc.gridy++;
        add(remove, gbc);
    }

    public void setTicket(Ticket ticket)
    {
        this.ticket = ticket;
        event.setText(MainService.getInstance().getEventById(ticket.getType().getEvent().getId()).getName());
        price.setText(ticket.getType().getPrice().toString());
        type.setText(ticket.getType().getType());
    }
}
