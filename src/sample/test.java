package sample;


import java.text.SimpleDateFormat;
import java.util.Calendar;

public class test {
    public static void main(String[] args) {

        /*Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        System.out.println(sdf.format(cal.getTime()));*/

        /*String input = "Android gave new life to Java";
        if( input.contains("android")){
            System.out.println("fouund");
        }*/

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
