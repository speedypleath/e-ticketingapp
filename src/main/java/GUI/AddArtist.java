package GUI;

import service.MainService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class AddArtist extends JPanel {
    AddArtist(CardLayout layout, JPanel cards){
        setLayout(new GridBagLayout());
        setBorder(new TitledBorder("Add Location"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8 ,0 ,8);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Name:"), gbc);
        gbc.gridy++;
        add(new JLabel("Pseudonym:"), gbc);

        gbc.gridx++;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        JTextField name = new JTextField(10);
        add(name, gbc);
        gbc.gridy++;
        JTextField pseudonym = new JTextField(10);
        add(pseudonym, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 0 ,0 ,0);
        JButton loginButton = new JButton("Submit");
        loginButton.addActionListener((e -> MainService.getInstance().addArtist(name.getText(), pseudonym.getText())));
        add(loginButton, gbc);
        gbc.gridx++;
        JButton registerButton = new JButton("Cancel");
        registerButton.addActionListener(e ->
        {
            layout.show(cards, "register");
            cards.setPreferredSize(new Dimension(600, 350));
        });
        add(registerButton, gbc);
    }
}
