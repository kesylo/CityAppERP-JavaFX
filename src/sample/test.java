package sample;


import sample.Controller.Global;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class test {
    public static void main(String[] args) {

        /*Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        System.out.println(sdf.format(cal.getTime()));*/

        /*String input = "Android gave new life to Java";
        if( input.contains("android")){
            System.out.println("fouund");
        }*/

        int a = 4441;
        String b = String.format("%04d", a);
        System.out.println(b);


        int year = Calendar.getInstance().get(Calendar.YEAR);
        //double n = 0.05264;
        //System.out.println(Global.getSystemDate());

        final String stringDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        LocalDate m = LocalDate.parse(stringDate);

        //System.out.println(m);

        String test  = "91-1124-31203";
        if (test.matches("\\d{2}+[-+]\\d{4}+[-+]\\d{5}")){
            System.out.println("ok");
        }else {
            System.out.println("no");
        }

        int num = 4400009;
        if (num % 97 == 0){
            System.out.println("even");
        }else {
            System.out.println("not even");
        }
    }
}
