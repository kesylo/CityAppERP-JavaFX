package sample;


import com.itextpdf.text.*;
import com.itextpdf.text.List;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfWriter;
import sample.Controller.Global.Global;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

        /*String date = "2019-12-03";
        String arr[] = date.split("-");
        for (String a :
                arr) {
            System.out.println(a);
        }*/

        // Creates a Year object
      /*  Year firstYear = Year.of(Integer.parseInt(arr[0]));

        // Create a DateTimeFormatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy");

        // Print the current year after formatting
        System.out.println(firstYear.format(formatter));*/

        //System.out.println(StringUtils.capitalize("jujkdi jhfu jnhf"));

        /*String test  = "kemingukdddj.col";
        if (test.matches("^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$")){
            System.out.println("ok");
        }else {
            System.out.println("no");
        }*/

       /* String test  = "15.15.12-856.25";
        if (test.matches("([0-9]{2})\\.([0-9]{2})\\.([0-9]{2})\\-([0-9]{3})\\.([0-9]{2})")){
            System.out.println("ok");
        }else {
            System.out.println("no");
        }*/


        /*LocalDate birthday = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedString = birthday.format(formatter);
        System.out.println(formattedString);*/

       /* int num = 4400009;
        if (num % 97 == 0){
            System.out.println("even");
        }else {
            System.out.println("not even");
        }*/

        //System.out.println(Global.stringToLocalDate("2019-02-16"));


        //System.out.println(Global.convertBirthdayToRegisterNber(LocalDate.now()));

        /*if (Global.isWindows()){

            String contractsFolder = "Contracts";
            String contractFileName = "Contrat-Employe-";
            String collaboratorName = "dcs";

            String myDocumentsPath = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
            File path = new File(myDocumentsPath + "\\" + contractsFolder);
            path.mkdirs();

            System.out.println(path.toString());


            Document pdf = new Document();

            try {

                PdfWriter writer = PdfWriter.getInstance(pdf,
                        new FileOutputStream(myDocumentsPath
                                +"\\"
                                + contractsFolder
                                +"\\"
                                + contractFileName
                                + collaboratorName
                                + ".pdf"));
                pdf.open();
                pdf.add(new Paragraph("gfd"));
                pdf.close();
                writer.close();

            } catch (DocumentException | FileNotFoundException e) {
                e.printStackTrace();
            }




        }*/

        Document pdf = new Document();
        try {

            PdfWriter writer = PdfWriter.getInstance(pdf,
                    new FileOutputStream("test.pdf"));
            pdf.open();

            // ========================================================================== PDF START
            Font font = new Font();
            font.setStyle(Font.BOLD);
            font.setSize(18);

            // Heading
            Paragraph heading = new Paragraph("CONTRAT DE TRAVAIL A DUREE DETERMINEE POUR EMPLOYES", font);
            heading.setAlignment(Element.ALIGN_CENTER);
            heading.setSpacingAfter(15);
            pdf.add(heading);
            pdf.add(new Paragraph("ENTRE les parties soussignées:"));
            // list
            List unorderedList = new List(List.UNORDERED);
            unorderedList.add(new ListItem("d'une part "));
            pdf.add(unorderedList);


            pdf.add(unorderedList);

            pdf.close();
            writer.close();

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
