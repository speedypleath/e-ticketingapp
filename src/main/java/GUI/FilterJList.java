package GUI;


import utility.CosineSimilarity;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class FilterJList extends JPanel {
    DefaultListModel defaultListModel = new DefaultListModel();
    Vector<String> values;
    JList artistList = new JList();

    FilterJList(Vector<String> values)
    {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setMinimumSize(new Dimension(100,100));
        this.values = values;
        JTextField search = new JTextField(10);
        add(search);
        this.values.stream().forEach((element) -> defaultListModel.addElement(element));
        artistList.setModel(defaultListModel);
        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane);
        scrollPane.setViewportView(artistList);

        search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchFilter(search.getText());
            }
        });
    }

    private void searchFilter(String searchTerm) {
        if (searchTerm.length() == 0)
            return;
        DefaultListModel filteredItems = new DefaultListModel();
        Comparator<String> comparator = Comparator.comparingDouble(
                p -> CosineSimilarity.cosineSimilarity(p.toLowerCase(Locale.ROOT), searchTerm.toLowerCase(Locale.ROOT)));

        values.sort(comparator);
        for(int i=0;i<Math.min(5, values.size());i++) {
            filteredItems.addElement(values.get(i));
            System.out.println(values.get(i));
        }
        defaultListModel = filteredItems;
        artistList.setModel(defaultListModel);
    }

    public List<String> getSelectedValues()
    {
        return artistList.getSelectedValuesList();
    }

    public String getSelectedValue()
    {
        return String.valueOf(artistList.getSelectedValuesList().get(0));
    }
}