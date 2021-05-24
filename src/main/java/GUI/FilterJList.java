package GUI;


import utility.CosineSimilarity;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class FilterJList<T> extends JPanel {
    DefaultListModel defaultListModel = new DefaultListModel();
    Vector<T> values;
    JList artistList = new JList();
    List<String> strings;
    public abstract void getStrings();

    FilterJList(Vector<T> values)
    {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setMinimumSize(new Dimension(100,100));
        this.values = values;
        JTextField search = new JTextField(10);
        add(search);
        getStrings();
        this.strings.stream().forEach((element) -> defaultListModel.addElement(element));
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

        strings.sort(comparator);
        for(int i=0;i<Math.min(5, strings.size());i++) {
            filteredItems.addElement(strings.get(i));
            System.out.println(strings.get(i));
        }
        defaultListModel = filteredItems;
        artistList.setModel(defaultListModel);
    }

    public List<T> getSelectedValues()
    {
        List<T> selectedValues = new ArrayList<>();
        for(var i : artistList.getSelectedIndices())
            selectedValues.add(this.values.get(i));
        return selectedValues;
    }

    public T getSelectedValue() {
        return values.get(artistList.getSelectedIndex());
    }

}