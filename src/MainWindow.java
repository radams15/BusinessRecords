import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainWindow {
    private JTable mainTable;
    private JButton updateButton;
    private JButton newCustomerButton;
    private JPanel mainPanel;
    private JTextArea displayArea;
    private JButton editButton;
    private JFrame frame;
    private DefaultTableModel tableModel;

    private Records records;


    MainWindow(String title){
        frame = new JFrame(title);
        frame.setContentPane(mainPanel);
        frame.pack();
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                onDestroy();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                update();
            }
        });

        newCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                addNewCustomerButtonPressed();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int row = mainTable.getSelectedRow();
                int id = (int)mainTable.getValueAt(row, 0);
                editCustomer(records.getCustomer(id));
            }
        });

        mainTable.addMouseListener(new MouseAdapter() { // double click event
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int id = (int)table.getValueAt(row, 0);
                    customerDoubleClicked(id);
                }
            }
        });

        records = new Records();

        tableModel = new DefaultTableModel();

        mainTable.setModel(tableModel);
        mainTable.setAutoCreateColumnsFromModel( true );

        tableModel.addColumn("Customer ID");
        tableModel.addColumn("Forename");
        tableModel.addColumn("Surname");
        tableModel.addColumn("Address");
        tableModel.addColumn("Jobs Done");
        tableModel.addColumn("Money Made");

        mainTable.setDefaultEditor(Object.class, null);

        update();
    }

    void onDestroy(){
        records.close();
        frame.dispose();
    }

    void clearTableModel(){
        if (tableModel.getRowCount() > 0) {
            for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
                tableModel.removeRow(i);
            }
        }
    }

    void clearDisplayArea(){
        displayArea.setText(null);
    }

    void update(){
        clearTableModel();
        clearDisplayArea();
        ArrayList<Customer> customers = records.getCustomers();

        for(Customer customer : customers){

            int id = customer.id;

            tableModel.addRow(new Object[]{customer.id,
                    customer.forename,
                    customer.surname,
                    customer.address,
                    records.getJobAmount(id),
                    Formatter.money(records.getMoneyMade(id))
            });
        }
        displayArea.append("Money Made: "+Formatter.money(records.getMoneyMade()));
    }

    void addNewCustomerButtonPressed(){
        NewCustomer window = new NewCustomer();
        window.pack();
        window.setVisible(true);

        if(window.complete) {
            Customer newCustomer = new Customer(window.forenameField.getText(), window.surnameField.getText(), window.addressField.getText());
            records.addCustomer(newCustomer);
            update();
        }
    }

    void customerDoubleClicked(int id){
        ArrayList<Job> jobsArray = records.getJobs(id);
        JobViewWindow window = new JobViewWindow(records, id);
        window.pack();
        window.setVisible(true);
        update();
    }

    void editCustomer(Customer customer){
        ArrayList<Customer> customersArray = records.getCustomers();
        Customer[] customers = new Customer[customersArray.size()];

        for(int i=0 ; i < customers.length ; i++){
            customers[i] = customersArray.get(i);
        }

        NewCustomer window = new NewCustomer(customer.forename, customer.surname, customer.address);
        window.pack();
        window.setVisible(true);

        if(window.complete) {
            Customer newCustomer = new Customer(
                    window.forenameField.getText(),
                    window.surnameField.getText(),
                    window.addressField.getText()
            );
            records.deleteCustomer(customer);
            records.addCustomer(newCustomer);
            update();
        }
    }
}
