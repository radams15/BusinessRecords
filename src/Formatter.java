import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formatter {

    static String dateFormat = "dd/MM/yyyy";
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);

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

    static String dateToText(Date date){
        return dateFormatter.format(date);
    }

    static Date textToDate(String text){
        try {
            return dateFormatter.parse(text);
        }catch(ParseException e){
            return new Date();
        }
    }
}
