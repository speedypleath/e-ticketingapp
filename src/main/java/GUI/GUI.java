package GUI;

import auth.AuthActions;
import service.MainService;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame implements AuthActions
{
    private JPanel authPage;
    private JPanel cards;
    private MainPage mainPage;
    CardLayout layout;
    AuthActions actions;
    private static GUI instance;
    private GUI(){
        setLayout(new GridBagLayout());
        setTitle("Sad");
        setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                MainService.getInstance().saveData()));
        setResizable(false);
        setVisible(true);
        layout = new CardLayout();
        authPage = new AuthPage();
        mainPage = new MainPage();
        cards = new JPanel();
        cards.setPreferredSize(new Dimension(800,500));
        cards.setLayout(layout);
        cards.add(authPage, "auth");
        cards.add(mainPage, "main");
        layout.show(cards, "auth");
        add(cards);
        setVisible(true);
    }

    public static GUI getInstance()
    {
        if(instance == null) {
            instance = new GUI();
        }
        return instance;
    }

    @Override
    public boolean login(String username, String password) {
        layout.show(cards, "main");
        mainPage.login(username, password);
        return true;
    }

    @Override
    public void setNextAction(AuthActions nextAction) {

    }

    @Override
    public void logout() {
        cards.setPreferredSize(new Dimension(800,500));
        layout.show(cards, "auth");
    }
}
