package GUI;

import service.MainService;

import javax.swing.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class test {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("Sad");
        frame.setBounds(300, 90, 900, 600);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        FilterJList filterJList = new FilterJList(MainService.getInstance().getArtists());
        filterJList.setVisible(true);
        panel.add(filterJList);
        panel.setVisible(true);
        frame.add(panel);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
