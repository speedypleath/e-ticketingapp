package GUI;

import utility.CosineSimilarity;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public abstract class SearchPage <T> extends JPanel{

    DefaultListModel defaultListModel;
    List<T> values;
    List strings;
    JPanel cards;
    CardLayout layout;
    AddEventPage addEventPage;

    public abstract void getStrings();

    public SearchPage(JPanel cards, CardLayout layout, AddEventPage addEvent, Vector<T> values) {
        this.cards = cards;
        this.layout = layout;
        addEventPage = addEvent;
        initComponents();
        this.bindData(values);
    }
    public void bindData(List<T> values){
        this.values = values;
        getStrings();
        DefaultListModel defaultListModel = new DefaultListModel();
        strings.forEach((string) -> defaultListModel.addElement(string));
        myJList.setModel(defaultListModel);
        myJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
        myJList.setModel(defaultListModel);
    }

    public abstract void action();


    private void initComponents() {

        JScrollPane jScrollPane1 = new JScrollPane();
        myJList = new JList<>();
        searchTxt = new JTextField();
        JLabel searchLabel = new JLabel();
        JButton cartButton = new JButton("Buy");

        myJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        myJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                action();
            }
        });
        jScrollPane1.setViewportView(myJList);

        searchTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchTxtKeyReleased();
            }
        });

        searchLabel.setText("Search");



        GroupLayout jPanel1Layout = new GroupLayout(this);
        this.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(69, 69, 69)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(searchLabel)
                                                .addGap(18, 18, 18)
                                                .addComponent(searchTxt, GroupLayout.PREFERRED_SIZE, 500, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 500, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cartButton, GroupLayout.PREFERRED_SIZE, 495, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(90, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(searchTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(searchLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
                                .addComponent(cartButton, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                                .addContainerGap())
        );

    }

    private void searchTxtKeyReleased() {
        searchFilter(searchTxt.getText());
    }

    JList<String> myJList;
    private JTextField searchTxt;
}
