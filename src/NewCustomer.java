import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NewCustomer extends JDialog {
    private JPanel mainPanel;
    private JButton okButton;
    private JButton cancelButton;
    JTextField forenameField;
    JTextField surnameField;
    JTextField addressField;
    boolean complete = false;

    public NewCustomer() {
        this(null, null, null);
    }

    public NewCustomer(String forename, String surname, String address){
        setContentPane(mainPanel);
        setModal(true);
        getRootPane().setDefaultButton(okButton);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                onDestroy();
            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                onOk();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                onCancel();
            }
        });

        forenameField.setText(forename);
        surnameField.setText(surname);
        addressField.setText(address);
    }

    void onOk(){
        complete = true;
        onDestroy();
    }

    void onCancel(){
        onDestroy();
    }

    void onDestroy(){
        dispose();
    }
}
