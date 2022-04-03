package viewer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SQLiteViewer extends JFrame {
    String sqlDBurl;        // address of the database we want to open
    DBConnector connector;  //connector object representing the connection to the database

    public SQLiteViewer() {
        //frame description and composition
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("SQLite Viewer");
        setSize(700, 900);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        setResizable(true);

        //textField description and composition, for entering database url
        JTextField textField = new JTextField();
        textField.setName("FileNameTextField");
        textField.setBounds(10,20,580,30);
        add(textField);

        //textArea description and composition, for displaying and entering sqlite statements
        JTextArea textArea = new JTextArea();
        textArea.setName("QueryTextArea");
        textArea.setBounds(10,100,550,100);
        textArea.setEnabled(false);
        add(textArea);

        /*table description and composition for displaying data from the database,
         according to the executed sqlite statement*/
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        table.setName("Table");
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBounds(10,250,660,300);
        tableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(tableScrollPane);

        //combo box description and composition, for selecting available tables a desired database
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        JComboBox<String> comboBox = new JComboBox<>(model);
        comboBox.setName("TablesComboBox");
        comboBox.setBounds(10, 60, 660,30);
        comboBox.addActionListener(e -> {
            try{
                textArea.setText("SELECT * FROM " + comboBox.getSelectedItem().toString().trim()+";");
            }catch (NullPointerException ex){
                ex.printStackTrace();
            }
        });
        add(comboBox);

        //button to execute desired statement in relation to selected table from the cmbobox
        JButton execute = new JButton("Execute");
        execute.setName("ExecuteQueryButton");
        execute.setBounds(570, 100, 100,30);
        execute.setEnabled(false);
        execute.addActionListener(e->{
            tableModel.setRowCount(0);
            //handle invalid statements with a popup window to warn the user
            try {
                if(connector.getQuery(textArea.getText()).isEmpty()){
                    throw new Exception();
                }
            }catch(Exception ex){
                JOptionPane.showMessageDialog(new Frame(), "Invalid Command");
            }

            //read and add data to the table model
            tableModel.setColumnIdentifiers(connector.gatTableData().get(0));
            for (int i = 1; i < connector.gatTableData().size(); i++) {
                tableModel.addRow(connector.gatTableData().get(i));
            }
        });

        //button to open the entered in text field database
        JButton open = new JButton("Open");
        open.setName("OpenFileButton");
        open.setBounds(600, 20, 70,30);
        add(open);
        open.addActionListener(e ->{
            comboBox.removeAllItems();
            sqlDBurl  = textField.getText();
            connector = new DBConnector(sqlDBurl);
            for (int i = 1; i < connector.gatTableData().size(); i++) {
                comboBox.addItem(connector.gatTableData().get(i)[0]);
            }
            /*check if entered database exists and make a popup window appear in case it doesn't.
            Set availability of execute button and textArea based on the success of open button execution*/
            if (comboBox.getItemCount()==0){
                execute.setEnabled(false);
                textArea.setEnabled(false);
                tableModel.setRowCount(0);
               JOptionPane.showMessageDialog(new Frame(), "File doesn't exist!");
            }else{
                textArea.setEnabled(true);
                execute.setEnabled(true);
            }

            System.out.println(connector.gatTableData().get(0).toString());
        });

        add(execute);
        setVisible(true);
    }
}
