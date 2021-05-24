package GUI;

import artist.Artist;
import service.MainService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class AddArtistPage extends JPanel {
    private final JTextField name;
    private final JTextField pseudonym;
    private final JButton submitButton;
    private final JButton deleteButton;
    private Artist artist;
    AddArtistPage(CardLayout layout, JPanel cards){
        setLayout(new GridBagLayout());
        setBorder(new TitledBorder("Add Artist"));
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
        name = new JTextField(10);
        add(name, gbc);
        gbc.gridy++;
        pseudonym = new JTextField(10);
        add(pseudonym, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 0 ,0 ,0);
        submitButton = new JButton("Submit");
        submitButton.addActionListener((e -> {
            if(artist == null)
                MainService.getInstance().addArtist(name.getText(), pseudonym.getText());
            else
                MainService.getInstance().editArtist(artist, name.getText(), pseudonym.getText());
        }));
        add(submitButton, gbc);
        gbc.gridx++;
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e ->
        {
            MainService.getInstance().deleteArtist(artist);
            addArtist();
        });
        add(deleteButton, gbc);
        deleteButton.setVisible(false);
    }

    public void edit(Artist value) {
        name.setText(value.getName());
        pseudonym.setText(value.getPseudonym());
        artist = value;
        deleteButton.setVisible(true);
    }

    public void addArtist(){
        name.setText("");
        pseudonym.setText("");
        artist = null;
        deleteButton.setVisible(false);
    }
}
