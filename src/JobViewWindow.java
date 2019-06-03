import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;

public class JobViewWindow extends JDialog {
    private JPanel mainPanel;
    private JButton okButton;
    private JTable mainTable;
    private JButton addJobButton;
    private JButton editButton;
    private Records records;

    private int id;
    DefaultTableModel tableModel;

    JobViewWindow(Records records, int id) {
        this.records = records;
        this.id = id;
        setContentPane(mainPanel);
        setModal(true);
        getRootPane().setDefaultButton(okButton);

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        addJobButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addNewJobButtonPressed();
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = mainTable.getSelectedRow();
                int id = (int)mainTable.getValueAt(row, 0);
                editJob(id);
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onDestroy();
            }
        });

        mainPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onDestroy();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        mainTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {

            }
        });

        mainPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = mainTable.getSelectedRow();
                int jobId = (int)mainTable.getValueAt(row, 0);

                records.deleteJob(records.getJob(jobId));
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        tableModel = new DefaultTableModel();

        mainTable.setModel(tableModel);

        tableModel.addColumn("Job ID");
        tableModel.addColumn("Description");
        tableModel.addColumn("Cost");
        tableModel.addColumn("Time Taken");
        tableModel.addColumn("Date");

        mainTable.setDefaultEditor(Object.class, null); // Stops Cell Editing
        update();
    }

    private void clearTable(){
        if (tableModel.getRowCount() > 0) {
            for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
                tableModel.removeRow(i);
            }
        }
    }

    private void update(){
        clearTable();
        for(Job job : records.getJobs(id)){
            tableModel.addRow(new Object[]{job.id, job.description, Formatter.money(job.cost), Formatter.time(job.time), job.date});
        }
    }

    void addNewJobButtonPressed(){
        ArrayList<Customer> customersArray = records.getCustomers();
        Customer[] customers = new Customer[customersArray.size()];

        for(int i=0 ; i < customers.length ; i++){
            customers[i] = customersArray.get(i);
        }

        NewJob window = new NewJob(customers, records.getCustomer(id));
        window.pack();
        window.setVisible(true);

        if(window.complete) {
            Customer customer = (Customer) window.customerBox.getSelectedItem();
            double cost = Formatter.moneyToDouble(window.costField.getText());
            double time = Double.valueOf(window.timeField.getText());

            Job newJob = new Job(customer,
                    window.descriptionBox.getText(),
                    cost,
                    time,
                    window.dateField.getText()
            );
            records.addJob(newJob);
            update();
        }
    }


    private void editJob(int id){
        Job job = records.getJob(id);
        Customer customer = job.customer;
        ArrayList<Customer> customersArray = records.getCustomers();

        Customer[] customers = customersArray.toArray(new Customer[customersArray.size()]);

        NewJob window = new NewJob(customers, customer, job.date, job.time, job.cost, job.description);
        window.pack();
        window.setVisible(true);

        Customer newCustomer = (Customer) window.customerBox.getSelectedItem();
        double cost = Formatter.moneyToDouble(window.costField.getText());
        double time = Double.valueOf(window.timeField.getText());

        Job newJob = new Job(
                newCustomer,
                window.descriptionBox.getText(),
                cost,
                time,
                window.dateField.getText()
        );

        if(window.complete){ // ok pressed
            records.deleteJob(job);
            records.addJob(newJob);
        }

        update();
    }

    private void onOK() {
        onDestroy();
    }

    private void onDestroy() {
        dispose();
    }
}
