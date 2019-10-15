package sample;


import com.sun.xml.internal.ws.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

        /*double i = -81.78999999999999;
        double r= Math.round(i * 100D) / 100D;
        System.out.println(r);*/


        int year = Calendar.getInstance().get(Calendar.YEAR);
        //double n = 0.05264;
        //System.out.println(CashRegisterGlobal.getSystemDate());

       /* final String stringDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        LocalDate m = LocalDate.parse(stringDate);*/

        //System.out.println(m);

        String date = "2019-12-03";
        String arr[] = date.split("-");
        for (String a :
                arr) {
            System.out.println(a);
        }
        System.out.println(StringUtils.capitalize("jujkdi jhfu jnhf"));

        String test  = "kemingukdd@dj.col";
        if (test.matches("^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$")){
            System.out.println("ok");
        }else {
            System.out.println("no");
        }


        LocalDate birthday = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedString = birthday.format(formatter);
        System.out.println(formattedString);

       /* int num = 4400009;
        if (num % 97 == 0){
            System.out.println("even");
        }else {
            System.out.println("not even");
        }*/
    }
}
