import javax.swing.*;

public class Main {
    public static void main(String[] args){ new Main().init(); }

    void init(){
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace();}
        //MainWindow mainWindow = new MainWindow("Business Records");
        Records records = new Records();

        String json = records.toJson();
        System.out.println(json);
    }
}
