package GUI;

import event.Event;
import service.MainService;
import utility.CosineSimilarity;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.Locale;
import java.util.Vector;

public class SearchEventsPage extends JPanel {

    DefaultListModel defaultListModel;
    Vector values = MainService.getInstance().getEvents();
    JPanel cards;
    CardLayout layout;
    AddEventPage addEventPage;

    public SearchEventsPage(JPanel cards, CardLayout layout, AddEventPage addEvent) {
        this.cards = cards;
        this.layout = layout;
        addEventPage = addEvent;
        initComponents();
        this.bindData(MainService.getInstance().getEvents());
    }
    public void bindData(Vector<String> events){
        DefaultListModel defaultListModel = new DefaultListModel();
        events.forEach((event) -> defaultListModel.addElement(event));
        myJList.setModel(defaultListModel);
        myJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
        myJList.setModel(defaultListModel);
    }

    private void initComponents() {

        JScrollPane jScrollPane1 = new JScrollPane();
        myJList = new JList<>();
        searchTxt = new JTextField();
        JLabel searchLabel = new JLabel();
        JButton cartButton = new JButton("Buy");

        myJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        myJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                String text = myJList.getSelectedValue();
                Event event = MainService.getInstance().getEventByName(text);
                System.out.println(event);
                addEventPage.editEvent(event);
                layout.show(cards, "addEvent");
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

    private JList<String> myJList;
    private JTextField searchTxt;
}