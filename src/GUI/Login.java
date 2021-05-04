package GUI;

import service.MainService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class Login extends JPanel
{
    Login(CardLayout layout, JPanel cards){
        setLayout(new GridBagLayout());
        setBorder(new TitledBorder("Login"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8 ,0 ,8);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);
        gbc.gridy++;
        add(new JLabel("Password:"), gbc);

        gbc.gridx++;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        JTextField username = new JTextField(10);
        add(username, gbc);
        gbc.gridy++;
        JPasswordField password = new JPasswordField(10);
        add(password, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 0 ,0 ,0);
        JTextArea error = new JTextArea("Invalid Login");
        error.setForeground(Color.red);
        error.setVisible(false);
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener((e ->
        {
            if (MainService.getInstance().login(username.getText(), String.valueOf(password.getPassword())))
            {
                System.out.println("da");
                error.setVisible(false);
            }
            else {
                error.setVisible(true);
                error.setText("Invalid Login");
                System.out.println("nu");
            }
        }
        ));
        add(loginButton, gbc);
        gbc.gridx++;
        JButton registerButton = new JButton("Register Page");
        registerButton.addActionListener(e -> layout.show(cards, "register"));
        add(registerButton, gbc);
        gbc.gridy++;
        gbc.gridx = 1;
        add(error, gbc);
    }

}