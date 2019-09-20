package sample;


import java.text.SimpleDateFormat;
import java.util.Calendar;

public class test {
    public static void main(String[] args) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        System.out.println(sdf.format(cal.getTime()));
    }
}
