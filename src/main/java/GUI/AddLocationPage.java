package GUI;

import service.MainService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class AddLocationPage extends JPanel {
    AddLocationPage(CardLayout layout, JPanel cards){
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
        JTextField name = new JTextField(10);
        add(name, gbc);
        gbc.gridy++;
        JTextField address = new JTextField(10);
        add(address, gbc);
        gbc.gridy++;
        JTextField capacity = new JTextField(10);
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
        JButton loginButton = new JButton("Submit");
        loginButton.addActionListener((e ->
        {
            if (MainService.getInstance().addLocation(name.getText(), address.getText(), capacity.getText()))
            {
                error.setVisible(false);
            }
            else
                error.setVisible(true);

        }
        ));
        add(loginButton, gbc);
        gbc.gridx++;
        JButton registerButton = new JButton("Cancel");
        registerButton.addActionListener(e ->
        {
            layout.show(cards, "register");
            cards.setPreferredSize(new Dimension(600, 350));
        });
        add(registerButton, gbc);
        gbc.gridy++;
        gbc.gridx = 1;
        add(error, gbc);
    }
}
