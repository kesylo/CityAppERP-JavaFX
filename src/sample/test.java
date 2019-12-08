package sample;


import com.itextpdf.text.*;
import com.itextpdf.text.List;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.text.TextAlignment;

import java.io.*;
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


        // read file

        ArrayList<String> fileLines = new ArrayList<>();
        try
        {
            //the file to be opened for reading
            FileInputStream fis=new FileInputStream("src/sample/Ressources/documents/CDD_EMPLOYES_VARIABLE.txt");

            Scanner sc=new Scanner(fis);    //file to be scanned
            //returns true if there is another line to read
            while(sc.hasNextLine())
            {
                fileLines.add(sc.nextLine());
                //System.out.println(sc.nextLine());      //returns the line that was skipped
            }
            sc.close();     //closes the scanner
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        //System.out.println(System.getProperty("user.dir"));

        /*for(String line : fileLines) {
            System.out.println(line);
        }*/

        /*System.out.println(fileLines.get(0));*/

    Document pdf = new Document();
        try {

            PdfWriter writer = PdfWriter.getInstance(pdf,
                    new FileOutputStream("test.pdf"));
            pdf.open();

            // ========================================================================== PDF START

            Paragraph p;
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA);
            headerFont.setStyle(Font.BOLD);
            headerFont.setSize(18);

            Font textBold = FontFactory.getFont(FontFactory.HELVETICA);
            textBold.setStyle(Font.BOLD);
            textBold.setSize(12);

            Font textNormal = FontFactory.getFont(FontFactory.HELVETICA);
            textNormal.setStyle(Font.NORMAL);
            textNormal.setSize(12);

            // ======================================================================================== HEADER
            p = new Paragraph(fileLines.get(0), headerFont);
            p.setAlignment(Element.ALIGN_CENTER);
            p.setSpacingAfter(25);
            pdf.add(p);

            p = new Paragraph(fileLines.get(3), textNormal);
            pdf.add(p);

            PdfPTable table = new PdfPTable(2);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(100);
            p = new Paragraph();
            table.addCell(new Phrase(fileLines.get(4), textNormal));
            table.addCell(new Phrase("TRANS TECHNICS SERVICES", textBold));
            table.addCell(new Phrase(fileLines.get(5), textNormal));
            table.addCell(new Phrase("Rue de la Fourche, 8", textBold));
            table.addCell(" ");
            table.addCell(new Phrase("1000 – BRUXELLES 1", textBold));
            table.addCell(" ");
            table.addCell(new Phrase("RPM: BE0.422.634.443", textBold));
            table.addCell(" ");
            table.addCell(new Phrase("ONSS: 0488.450-28", textBold));
            table.addCell(new Phrase(fileLines.get(9), textNormal));
            table.addCell(new Phrase("Christian Drappier", textBold));
            p.add(table);
            p.setIndentationLeft(20);
            pdf.add(p);

            p = new Paragraph(fileLines.get(11), textNormal);
            pdf.add(p);

            // add user data in this section
            table = new PdfPTable(2);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(100);
            p = new Paragraph();
            table.addCell(new Phrase(fileLines.get(12), textNormal));
            table.addCell(new Phrase("Christian Drappier", textBold));
            table.addCell(new Phrase(fileLines.get(13), textNormal));
            table.addCell(new Phrase("41444444444444444444", textBold));
            table.addCell(new Phrase(fileLines.get(14), textNormal));
            table.addCell(new Phrase("grfdhgf", textBold));
            table.addCell(new Phrase(fileLines.get(15), textNormal));
            table.addCell(new Phrase("541123151563", textBold));
            table.addCell(new Phrase(fileLines.get(16), textNormal));
            table.addCell(new Phrase("grfdhgf", textBold));
            table.addCell(new Phrase(fileLines.get(17), textNormal));
            table.addCell(new Phrase("gfdgfdgfdfdsds", textBold));
            table.addCell(new Phrase(fileLines.get(21), textNormal));
            table.addCell("");
            p.add(table);
            p.setIndentationLeft(20);
            pdf.add(p);
            pdf.add( Chunk.NEWLINE );

            p = new Paragraph(new Phrase(fileLines.get(22), textNormal));
            pdf.add(p);

            // ======================================================================================== BODY
            // We create a list:
            pdf.add( Chunk.NEWLINE );
            ZapfDingbatsList list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(25), textBold)));
            pdf.add(list);

            // add user data in this section
            p = new Paragraph();
            p.add(new Phrase(fileLines.get(27), textNormal));
            p.add(new Phrase(" testeur", textBold));
            pdf.add(p);

            // add user data in this section
            p = new Paragraph();
            p.add(new Phrase(fileLines.get(28), textNormal));
            p.add(new Phrase(" testeur", textBold));
            pdf.add(p);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(30), textNormal));
            pdf.add(p);

            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(32), textBold)));
            pdf.add(list);

            // add user data in this section
            table = new PdfPTable(4);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(100);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            p = new Paragraph();
            table.addCell(new Phrase(fileLines.get(34), textNormal));
            table.addCell(new Phrase("2019-12-12", textBold));
            table.addCell(new Phrase("au", textNormal));
            table.addCell(new Phrase("2301-25-45", textBold));
            p.add(table);
            float[] columnWidths = new float[]{70f, 13f, 3.5f, 14.5f};
            table.setWidths(columnWidths);
            pdf.add(p);

            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(37), textBold)));
            pdf.add(list);

            // add user data in this section
            table = new PdfPTable(3);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(100);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            p = new Paragraph();
            table.addCell(new Phrase(fileLines.get(39), textNormal));
            table.addCell(new Phrase("1510.20", textBold));
            table.addCell(new Phrase("€ par mois.", textNormal));
            p.add(table);
            columnWidths = new float[]{47f, 10f, 43f};
            table.setWidths(columnWidths);
            pdf.add(p);

            p = new Paragraph(new Phrase(fileLines.get(41), textNormal));
            pdf.add(p);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(43), textNormal));
            pdf.add(p);

            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(45), textBold)));
            pdf.add(list);

            // add user data in this section
            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(47) + " ", textNormal));
            p.add(new Phrase("12,02", textBold));
            p.add(new Phrase(" heures sera réalisée sur base annuelle.", textNormal));
            pdf.add(p);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(49), textNormal));
            pdf.add(p);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(51), textNormal));
            pdf.add(p);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(53), textNormal));
            pdf.add(p);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(55), textNormal));
            pdf.add(p);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(57), textNormal));
            pdf.add(p);

            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(60), textBold)));
            pdf.add(list);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(62), textNormal));
            pdf.add(p);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(64), textNormal));
            pdf.add(p);

            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(66), textBold)));
            pdf.add(list);

            p = new Paragraph(new Phrase(fileLines.get(68), textNormal));
            pdf.add(p);

            // ======================================================================================== FOOTER

            // add user data in this section
            p = new Paragraph();
            p.add(new Phrase(fileLines.get(73), textNormal));
            p.add(new Phrase("12-08-2019" + ".", textBold));
            pdf.add(p);

            p = new Paragraph(new Phrase(fileLines.get(74), textNormal));
            p.setSpacingAfter(30);
            pdf.add(p);


            table = new PdfPTable(2);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(100);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            p = new Paragraph();
            table.addCell(new Phrase(fileLines.get(77), textBold));
            table.addCell(new Phrase(fileLines.get(78), textBold));
            table.addCell(new Phrase(fileLines.get(79), textNormal));
            table.addCell(new Phrase(fileLines.get(80), textNormal));
            table.addCell(new Phrase(fileLines.get(81), textNormal));
            table.addCell(new Phrase(fileLines.get(82), textNormal));
            p.add(table);
            columnWidths = new float[]{50f, 50f};
            table.setWidths(columnWidths);
            p.setSpacingAfter(50);
            pdf.add(p);

            table = new PdfPTable(2);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(100);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            p = new Paragraph();
            table.addCell(new Phrase(fileLines.get(83), textNormal));
            table.addCell(new Phrase(fileLines.get(83), textNormal));
            p.add(table);
            columnWidths = new float[]{50f, 50f};
            table.setWidths(columnWidths);
            pdf.add(p);

            pdf.close();
            writer.close();

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
