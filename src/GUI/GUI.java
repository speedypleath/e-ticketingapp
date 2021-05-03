package GUI;

import service.MainService;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame
{
    private JPanel register;
    private JPanel login;
    private JPanel cards;
    CardLayout layout;
    MainService service;

    public GUI()
    {
        setLayout(new GridBagLayout());
        setTitle("Sad");
        setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                MainService.getInstance().saveData()));
        setResizable(false);
        setVisible(true);
        layout = new CardLayout();
        cards = new JPanel();
        login = new Login(layout, cards);
        register = new Register(layout, cards);
        login.setPreferredSize(new Dimension(400,250));
        register.setPreferredSize(new Dimension(400, 250));
        cards.setLayout(layout);
        cards.add(login, "login");
        cards.add(register, "register");
        layout.show(cards, "login");
        add(cards);
        setVisible(true);
    }
}
