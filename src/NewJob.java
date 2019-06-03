import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.ArrayList;

public class NewJob extends JDialog {
    private JPanel mainPanel;
    JTextPane descriptionBox;
    private JButton okButton;
    private JButton cancelButton;
    JComboBox customerBox;
    JTextField dateField;
    JFormattedTextField timeField;
    JFormattedTextField costField;

    private boolean isUpdating = false;

    boolean complete = false;

    public NewJob(Customer[] customers) {
        this(customers, null, null, 0, 0, null);
    }

    public NewJob(Customer[] customers, Customer customer) {
        this(customers, customer, null, 0, 0, null);
    }

    public NewJob(Customer[] customers, Customer customer, String date, double time, double cost, String description) {
        isUpdating = true;
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

        ComboBoxModel<Customer> customerModel = new DefaultComboBoxModel<Customer>(customers);

        customerBox.setModel(customerModel);

        customerModel.setSelectedItem(customer);
        dateField.setText(date);
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
}
