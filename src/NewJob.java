import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

public class NewJob extends JDialog {
    private JPanel mainPanel;
    private GridLayout mainLayout;
    JTextPane descriptionBox;
    private JButton okButton;
    private JButton cancelButton;
    JComboBox customerBox;
    JFormattedTextField timeField;
    JFormattedTextField costField;
    JDateChooser datePicker;

    boolean complete = false;

    public NewJob(Customer[] customers, Customer customer) {
        this(customers, customer, null, 0, 0, null);
    }

    public NewJob(Customer[] customers, Customer customer, String date, double time, double cost, String description) {
        createUIComponents();
        setContentPane(mainPanel);
        setModal(true);
        getRootPane().setDefaultButton(okButton);

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        mainPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        ComboBoxModel<Customer> customerModel = new DefaultComboBoxModel<>(customers);

        customerBox.setModel(customerModel);

        customerModel.setSelectedItem(customer);

        Date d = Formatter.textToDate(date);
        datePicker.setDate(d);
        timeField.setText(String.valueOf(time));
        costField.setText(String.valueOf(cost));
        descriptionBox.setText(description);
    }

    private void onOK() {
        complete = true;
        onDispose();
    }

    private void onCancel() {
        onDispose();
    }

    private void onDispose(){
        dispose();
    }

    private void createUIComponents() {
        mainPanel = new JPanel();
        mainLayout = new GridLayout(0,2);
        mainPanel.setLayout(mainLayout);

        descriptionBox = new JTextPane();
        okButton = new JButton("Ok");
        cancelButton = new JButton("Cancel");
        customerBox = new JComboBox();
        costField = new JFormattedTextField();
        timeField = new JFormattedTextField();
        datePicker = new JDateChooser();

        add(mainPanel);

        mainPanel.add(new JLabel("Customer"));
        mainPanel.add(customerBox);
        mainPanel.add(new JLabel("Description"));
        mainPanel.add(descriptionBox);
        mainPanel.add(new JLabel("Time Taken"));
        mainPanel.add(timeField);
        mainPanel.add(new JLabel("Cost"));
        mainPanel.add(costField);
        mainPanel.add(new JLabel("Date Done"));
        mainPanel.add(datePicker);

        JPanel buttonPanel = new JPanel(new GridLayout(0,2));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel);

        datePicker.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        datePicker.setDateFormatString(Formatter.dateFormat);
    }
}
