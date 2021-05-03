package GUI;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        add(new JTextField(10), gbc);
        gbc.gridy++;
        add(new JPasswordField(10), gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 0 ,0 ,0);
        add(new JButton("Login"), gbc);
        gbc.gridx++;
        JButton registerButton = new JButton("Register Page");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                layout.show(cards, "register");
            }
        });
        add(registerButton, gbc);
    }

}