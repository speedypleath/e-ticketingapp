package GUI;

import service.MainService;
import utility.CosineSimilarity;

import javax.swing.*;
import java.util.Comparator;
import java.util.Locale;
import java.util.Vector;

public class SearchEvents extends JPanel {

    DefaultListModel defaultListModel = new DefaultListModel();
    Vector values = MainService.getInstance().getEvents();
    /*
    Constructor
    */
    public SearchEvents() {
        initComponents();
        this.bindData();
    }
    private void bindData(){
        MainService.getInstance().getEvents().forEach((event) -> defaultListModel.addElement(event));
        myJList.setModel(defaultListModel);
        myJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    /*
    Search/Filter data
    */
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
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jScrollPane1 = new JScrollPane();
        myJList = new JList<>();
        searchTxt = new JTextField();
        searchLabel = new JLabel();
        JButton cartButton = new JButton("Buy");

        myJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        myJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {

            }
        });
        jScrollPane1.setViewportView(myJList);

        searchTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchTxtKeyReleased(evt);
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

    }// </editor-fold>

    private void searchTxtKeyReleased(java.awt.event.KeyEvent evt) {
        searchFilter(searchTxt.getText());
    }

    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JList<String> myJList;
    private JLabel searchLabel;
    private JTextField searchTxt;
}