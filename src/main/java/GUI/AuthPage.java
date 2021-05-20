package GUI;

import javax.swing.*;
import java.awt.*;

public class AuthPage extends JPanel {
    AuthPage()
    {
        setLayout(new BorderLayout());
        CardLayout layout = new CardLayout();
        JPanel cards = new JPanel();
        JPanel login = new LoginPage(layout, cards);
        JPanel register = new RegisterPage(layout, cards);
        cards.setLayout(layout);
        cards.add(login, "login");
        cards.add(register, "register");
        layout.show(cards, "login");
        add(cards);
    }
}
