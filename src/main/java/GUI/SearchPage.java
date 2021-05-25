package GUI;

import utility.CosineSimilarity;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public abstract class SearchPage <T> extends JPanel{

    DefaultListModel defaultListModel;
    List<T> values;
    List strings;
    private JPanel cards;
    private CardLayout layout;
    EventPage eventPage;
    private String currentAction = "first";

    public abstract void getStrings();

    public SearchPage(JPanel cards, CardLayout layout, EventPage addEvent, List<T> values) {
        this.cards = cards;
        this.layout = layout;
        eventPage = addEvent;
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
        repaint();
        revalidate();
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

    public abstract void action1();

    public abstract void action2();

    public void setCurrentAction(String action){
        currentAction = action;
    };

    private void initComponents() {

        JScrollPane jScrollPane1 = new JScrollPane();
        myJList = new JList<>();
        searchTxt = new JTextField();
        JLabel searchLabel = new JLabel();

        myJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        myJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(currentAction == "first")
                    action1();
                else
                    action2();
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
                                )));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(searchTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(searchLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
        ));

    }

    private void searchTxtKeyReleased() {
        searchFilter(searchTxt.getText());
    }

    JList<String> myJList;
    private JTextField searchTxt;
}
