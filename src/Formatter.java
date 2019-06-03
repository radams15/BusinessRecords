import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

public class Formatter {
    static String money(double cost){
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(cost);
    }

    static String time(double time){
        int hours = (int) time;
        int minutes = (int) (time * 60) % 60;
        return hours+":"+minutes;
    }

    static double moneyToDouble(String money){
        final NumberFormat format = NumberFormat.getNumberInstance();
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setParseBigDecimal(true);
        }
        try {
            return format.parse(money.replaceAll("[^\\d.,]", "")).doubleValue();
        }catch(ParseException e){
            return 0;
        }
    }
}
