package GUI;

import models.Location;
import service.MainService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class LocationPage extends JPanel {
    private final JTextField name;
    private final JTextField address;
    private final JTextField capacity;
    private final JButton submitButton;
    private final JButton deleteButton;
    private Location location;
    LocationPage(CardLayout layout, JPanel cards){
        setLayout(new GridBagLayout());
        setBorder(new TitledBorder("Add Location"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8 ,0 ,8);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Name:"), gbc);
        gbc.gridy++;
        add(new JLabel("Address:"), gbc);
        gbc.gridy++;
        add(new JLabel("Capacity:"), gbc);

        gbc.gridx++;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        name = new JTextField(10);
        add(name, gbc);
        gbc.gridy++;
        address = new JTextField(10);
        add(address, gbc);
        gbc.gridy++;
        capacity = new JTextField(10);
        add(capacity, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 0 ,0 ,0);
        JTextArea error = new JTextArea("Not a number");
        error.setForeground(Color.red);
        error.setVisible(false);
        error.setLineWrap(true);
        error.setWrapStyleWord(true);
        error.setOpaque(false);
        error.setEditable(false);
        submitButton = new JButton("Submit");
        submitButton.addActionListener((e ->
        {
            if(location != null) {
                if (MainService.getInstance().addLocation(name.getText(), address.getText(), capacity.getText())) {
                    error.setVisible(false);
                } else
                    error.setVisible(true);
            }
            else
                MainService.getInstance().editLocation(location, name.getText(), address.getText(), Integer.valueOf(capacity.getText()));
        }
        ));
        add(submitButton, gbc);
        gbc.gridx++;

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e ->
        {
            MainService.getInstance().deleteLocation(location);
            addArtist();
        });
        add(deleteButton, gbc);
        deleteButton.setVisible(false);
    }
    public void edit(Location value) {
        name.setText(value.getName());
        address.setText(value.getAddress());
        capacity.setText(String.valueOf(value.getCapacity()));
        location = value;
        deleteButton.setVisible(true);
    }

    public void addArtist(){
        name.setText("");
        address.setText("");
        capacity.setText("");
        location = null;
        deleteButton.setVisible(false);
    }
}
