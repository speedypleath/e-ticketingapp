package GUI;

import service.MainService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class Register extends JPanel
{
    Register(CardLayout layout, JPanel cards){
        setLayout(new GridBagLayout());
        setBorder(new TitledBorder("Register"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8 ,0 ,8);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Name:"), gbc);
        gbc.gridy++;
        add(new JLabel("Email:"), gbc);
        gbc.gridy++;
        add(new JLabel("Username:"), gbc);
        gbc.gridy++;
        add(new JLabel("Password:"), gbc);

        gbc.gridx++;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        JTextField name = new JTextField(10);
        add(name, gbc);
        gbc.gridy++;
        JTextField email = new JTextField(10);
        add(email, gbc);
        gbc.gridy++;
        JTextField username = new JTextField(10);
        add(username, gbc);
        gbc.gridy++;
        JPasswordField password = new JPasswordField(10);
        add(password, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        JRadioButton organiser = new JRadioButton("Organiser");
        JRadioButton client = new JRadioButton("Client");
        client.setSelected(true);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(organiser);
        buttonGroup.add(client);
        add(client, gbc);
        gbc.gridx++;
        add(organiser, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 0 ,0 ,0);
        JTextArea error = new JTextArea();
        error.setForeground(Color.red);
        error.setVisible(false);
        error.setLineWrap(true);
        error.setWrapStyleWord(true);
        error.setOpaque(false);
        error.setEditable(false);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener((actionEvent) -> {
            StringBuilder result = MainService.getInstance().register(username.getText(),
                    String.valueOf(password.getPassword()), email.getText(), name.getText(), client.isSelected() ? "client" : "organiser");
            if(result.length() == 0){
                error.setVisible(false);
            }
            else {
                error.setVisible(true);
                error.setText(String.valueOf(result));
            }
        });
        add(registerButton, gbc);
        gbc.gridx++;
        JButton loginButton = new JButton("Login Page");
        loginButton.addActionListener(e -> {
            layout.show(cards, "login");
            cards.setPreferredSize(new Dimension(350, 225));
        });
        add(loginButton, gbc);
        gbc.insets = new Insets(20, 0 ,0 ,0);
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridy++;
        gbc.gridx = 1;
        add(error, gbc);
    }
}
