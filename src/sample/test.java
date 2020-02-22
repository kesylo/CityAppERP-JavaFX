package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.PropertyTemplate;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sample.Global.Global;
import sample.Model.Planning;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.*;

public class test {
    public static void main(String[] args) {

        //region TEST
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


        //int year = Calendar.getInstance().get(Calendar.YEAR);
        //double n = 0.05264;
        //System.out.println(CashRegisterGlobal.getSystemDateYMD());

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
        //endregion

        // read file
/*

        //region PDF

        String myDocumentsPath = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();

        //region Read txt file
        ArrayList<String> fileLines = new ArrayList<>();
        File file = new File(myDocumentsPath + "/ERPDocs/CDD_ETUDIANTS_EMPLOYES_VARIABLE.txt");

        try {
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                fileLines.add(line);
            }
        }catch (Exception e){
            e.fillInStackTrace();
        }

        //endregion

        //region PDF Generation
        // variables
        // Change Here
        //User selectedUser = comboUser.getSelectionModel().getSelectedItem();
        User selectedUser = new User();
        String collaboratorName = selectedUser.getFirstName() + " " + selectedUser.getLastName() ;
        String contractsFolder = "Contracts";
        String contractFileName = "Contrat-Etudiant-Employe-";
        // pdf generation
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
            table.addCell(new Phrase(collaboratorName, textBold));
            table.addCell(new Phrase(fileLines.get(13), textNormal));
            table.addCell(new Phrase(selectedUser.getNationalRegistreryNumber(), textBold));
            table.addCell(new Phrase(fileLines.get(14), textNormal));
            table.addCell(new Phrase(selectedUser.getAddress() + " "
                    + selectedUser.getHouseNumber() + " "
                    + selectedUser.getPostalCode() + " "
                    + selectedUser.getCity(),
                    textBold));
            table.addCell(new Phrase(fileLines.get(15), textNormal));
            table.addCell(new Phrase(selectedUser.getPhoneNumber(), textBold));
            table.addCell(new Phrase(fileLines.get(16), textNormal));
            table.addCell(new Phrase(selectedUser.getEmail(), textBold));
            table.addCell(new Phrase(fileLines.get(17), textNormal));
            table.addCell(new Phrase(selectedUser.getIban(), textBold));
            table.addCell(new Phrase(fileLines.get(21), textNormal));
            table.addCell("");
            p.add(table);
            p.setIndentationLeft(20);
            pdf.add(p);
            pdf.add( Chunk.NEWLINE );

            p = new Paragraph(new Phrase(fileLines.get(22), textNormal));
            pdf.add(p);

            // ======================================================================================== BODY
            // Article 1
            pdf.add( Chunk.NEWLINE );
            ZapfDingbatsList list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(25), textBold)));
            pdf.add(list);

            // Change Here
            //String categoryValue = comboCategory.getSelectionModel().getSelectedItem();
            String categoryValue = "3112 - 213";
            String cat[] = categoryValue.split(" - ");

            p = new Paragraph();
            p.add(new Phrase(fileLines.get(27), textNormal));
            p.add(new Phrase(" " + cat[1], textBold));
            pdf.add(p);

            // add user data in this section
            p = new Paragraph();
            p.add(new Phrase(fileLines.get(28), textNormal));
            p.add(new Phrase(" " + cat[0], textBold));
            pdf.add(p);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(30), textNormal));
            pdf.add(p);

            // Article 2
            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(44), textBold)));
            pdf.add(list);

            // Change Here
            table = new PdfPTable(3);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(100);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            p = new Paragraph();
            table.addCell(new Phrase(fileLines.get(46), textNormal));
            //table.addCell(new Phrase(datePickerStartContract.getValue().toString(), textBold));
            table.addCell(new Phrase("", textBold));
            table.addCell(new Phrase(fileLines.get(47), textNormal));
            p.add(table);
            float[] columnWidths = new float[]{70f, 13f, 14.5f};
            table.setWidths(columnWidths);
            pdf.add(p);

            // Article 3
            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(61), textBold)));
            pdf.add(list);

            // add user data in this section
            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add((new Phrase(fileLines.get(63), textNormal)));
            p.add((new Phrase(fileLines.get(64), textNormal)));
            p.add((new Phrase(fileLines.get(65), textNormal)));
            p.add((new Phrase(fileLines.get(66), textNormal)));
            pdf.add(p);

            // Article 4
            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(80), textBold)));
            pdf.add(list);

            // Change Here
            table = new PdfPTable(4);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(100);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            p = new Paragraph();
            table.addCell(new Phrase(fileLines.get(82), textNormal));
            //table.addCell(new Phrase(user salary, textBold));
            table.addCell(new Phrase(fileLines.get(83), textNormal));
            p.add(table);
            columnWidths = new float[]{70f, 13f, 3.5f, 14.5f};
            table.setWidths(columnWidths);
            pdf.add(p);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add((new Phrase(fileLines.get(85), textNormal)));
            p.add((new Phrase(fileLines.get(86), textNormal)));
            p.add((new Phrase(fileLines.get(87), textNormal)));
            pdf.add(p);

            // Article 5
            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(101), textBold)));
            pdf.add(list);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add(new Phrase(fileLines.get(103), textNormal));
            p.add(new Phrase(fileLines.get(105), textNormal));
            pdf.add(p);

            // Change Here
            table = new PdfPTable(3);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(100);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            p = new Paragraph();
            table.addCell(new Phrase(fileLines.get(106), textNormal));
            //table.addCell(new Phrase(work hour from txt, textBold));
            table.addCell(new Phrase("", textBold));
            table.addCell(new Phrase(fileLines.get(107), textNormal));
            p.add(table);
            columnWidths = new float[]{70f, 13f, 14.5f};
            table.setWidths(columnWidths);
            pdf.add(p);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add((new Phrase(fileLines.get(108), textNormal)));
            p.add((new Phrase(fileLines.get(109), textNormal)));
            p.add((new Phrase(fileLines.get(110), textNormal)));
            p.add((new Phrase(fileLines.get(111), textNormal)));
            p.add((new Phrase(fileLines.get(112), textNormal)));
            p.add((new Phrase(fileLines.get(113), textNormal)));
            p.add((new Phrase(fileLines.get(114), textNormal)));
            pdf.add(p);

            // Article 6
            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(128), textBold)));
            pdf.add(list);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add((new Phrase(fileLines.get(130), textNormal)));
            p.add((new Phrase(fileLines.get(131), textNormal)));
            pdf.add(p);

            // Article 7
            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(145), textBold)));
            pdf.add(list);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add((new Phrase(fileLines.get(147), textNormal)));
            p.add((new Phrase(fileLines.get(148), textNormal)));
            pdf.add(p);

            // Article 8
            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(162), textBold)));
            pdf.add(list);

            p = new Paragraph();
            p.add((new Phrase(fileLines.get(164), textNormal)));
            pdf.add(p);

            // Article 9
            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(178), textBold)));
            pdf.add(list);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add((new Phrase(fileLines.get(180), textNormal)));
            p.add((new Phrase(fileLines.get(181), textNormal)));
            pdf.add(p);

            // Article 10
            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(195), textBold)));
            pdf.add(list);

            p = new Paragraph();
            p.add((new Phrase(fileLines.get(197), textNormal)));
            pdf.add(p);

            // Article 11
            pdf.add( Chunk.NEWLINE );
            list = new ZapfDingbatsList(70, 15);
            list.add(new ListItem(new Phrase(fileLines.get(211), textBold)));
            pdf.add(list);

            p = new Paragraph();
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.add((new Phrase(fileLines.get(213), textNormal)));
            p.add((new Phrase(fileLines.get(214), textNormal)));
            pdf.add(p);

            // ======================================================================================== FOOTER

            pdf.add( Chunk.NEWLINE );
            pdf.add( Chunk.NEWLINE );
            // add user data in this section
            p = new Paragraph();
            p.add(new Phrase(fileLines.get(244), textNormal));
            p.add(new Phrase(Global.getSystemDateYMD() + ".", textBold));
            pdf.add(p);

            p = new Paragraph(new Phrase(fileLines.get(245), textNormal));
            p.setSpacingAfter(30);
            pdf.add(p);


            table = new PdfPTable(2);
            table.getDefaultCell().setBorder(0);
            table.setWidthPercentage(100);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            p = new Paragraph();
            table.addCell(new Phrase(fileLines.get(248), textBold));
            table.addCell(new Phrase(fileLines.get(249), textBold));
            table.addCell(new Phrase(fileLines.get(250), textNormal));
            table.addCell(new Phrase(fileLines.get(251), textNormal));
            table.addCell(new Phrase(fileLines.get(252), textNormal));
            table.addCell(new Phrase(fileLines.get(253), textNormal));
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
            table.addCell(new Phrase(fileLines.get(254), textNormal));
            table.addCell(new Phrase(fileLines.get(254), textNormal));
            p.add(table);
            columnWidths = new float[]{50f, 50f};
            table.setWidths(columnWidths);
            pdf.add(p);

            pdf.close();
            writer.close();

        } catch (DocumentException e) {
            Global.showErrorMessage("Erreur lors de la création", "Impossible de créer le PDF");
            //return false;
        } catch (FileNotFoundException e) {
            Global.showErrorMessage("Erreur lors de la génération", "Veuillez fermer le fichier pdf avant de générer.");
            //return false;
        }
        //endregion
*/
        /*LocalDate startDate = LocalDate.of(2015,1,1);
        LocalDate endDate = LocalDate.of(2030,1,1);
        List<LocalDate> dates = Global.getYearsBetween(startDate, endDate);

        for (LocalDate date : dates) {
            int intDate = date.getYear();
            System.out.println(intDate);
        }*/



        /*LocalTime l1 = LocalTime.parse("02:53");
        LocalTime l2 = LocalTime.parse("17:54");
        System.out.println();

        long min = MINUTES.between(l1, l2);
        int h = 0;

        long hours = min / 60; //since both are ints, you get an int
        long minutes = min % 60;

        System.out.printf("%d:%02d", hours, minutes);

        System.out.println();

        while (min >=60){
            min -=60;
            h++;
        }

        System.out.println(h + " " + min);*/


/*
        InputStream inputStream = Main.class.getResourceAsStream("/documents/CDD_EMPLOYES_VARIABLE.txt");
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        try {
            String line;
            while ((line = reader.readLine()) !=null){
                System.out.println(line);
            }
            reader.close();
        } catch (Exception e){
            e.fillInStackTrace();
        }*/






/*
        ObservableList<Planning> planningList = FXCollections.observableArrayList();
        Planning p = new Planning("12-01-2019","12:01", "14:12");
        planningList.add(p);
        p = new Planning("21-03-2019","02:01", "15:32");
        planningList.add(p);

        try {
            // variables
            Double totalPrestaion = 0.0;

            // create excel sheet
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("Timesheet de LOIC" );
            sheet.setFitToPage(true);
            sheet.setHorizontallyCenter(true);


            // row yellow
            XSSFRow row1 = sheet.createRow(0);
            createCell(row1, wb, "Nom :", 0, 12,IndexedColors.YELLOW.getIndex());
            drawBorders(0,0,0,0, BorderExtent.ALL, sheet);
            createCell(row1, wb, "LOIC", 1, 12,IndexedColors.YELLOW.getIndex());
            drawBorders(0,0,1,5, BorderExtent.OUTSIDE, sheet);
            createCell(row1, wb, "12.0", 5, 12,IndexedColors.YELLOW.getIndex());
            drawBorders(0,0,5,5, BorderExtent.ALL, sheet);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 4));

            // row green
            XSSFRow row2 = sheet.createRow(1);
            row2.setHeightInPoints(45);
            List<String> values = new ArrayList<>();
            values.add("Date");
            values.add("Heure de début");
            values.add("Heure de fin");
            values.add("Pause");
            values.add("Heures prestées");
            values.add("Payts");
            values.add("Heures normales");
            values.add("Heures supplémentaires");
            values.add("A Payer");
            values.add("PlanningReport");
            values.add("Solde");

            for (int i = 0; i < values.size(); i ++){
                createCell(row2, wb, values.get(i), i, 12,IndexedColors.SEA_GREEN.getIndex());
            }
            drawBorders(1,1,0,10, BorderExtent.ALL, sheet);



        */
/*    // dates
            List<String> dates = new ArrayList<>();
            dates.add("01-12-19");
            dates.add("07-12-19");
            dates.add("13-12-19");
            int indexStart = 3;
            *//*
*/
/*for (String date : dates) {
                XSSFRow rowDates = sheet.createRow(indexStart);
                createCell(rowDates, wb, date, 0, 12, IndexedColors.GREY_25_PERCENT.getIndex());
                indexStart += 1;
            }*//*


            // data
            int indexStart = 3;
            int startDataRow = 3;
            for (Planning aPlanningList : planningList) {
                XSSFRow rowDates = sheet.createRow(indexStart);

                // dates
                createCell(rowDates, wb, aPlanningList.getPrestationDate(), 0, 12, IndexedColors.GREY_25_PERCENT.getIndex());

                // start Time
                String oldString = aPlanningList.getStartTime();
                String newString = oldString.replace(":", ".");
                Double value1 = Double.parseDouble(newString);
                createCell(rowDates, wb, newString, 1, 12, IndexedColors.WHITE.getIndex());

                // end Time
                String oldString2 = aPlanningList.getEndTime();
                String newString2 = oldString2.replace(":", ".");
                Double value2 = Double.parseDouble(newString2);
                createCell(rowDates, wb, newString2, 2, 12, IndexedColors.WHITE.getIndex());

                // work time
                Double workTime = value2 - value1;
                createCell(rowDates, wb, workTime.toString(), 4, 12, IndexedColors.WHITE.getIndex());

                // compute total time
                if (workTime > 0) {
                    totalPrestaion += workTime;
                }
                indexStart += 1;
            }
            drawBorders(startDataRow,indexStart,0,4, BorderExtent.ALL, sheet);




            // row purple and green
            XSSFRow row3 = sheet.createRow(2);
            createCell(row3, wb, "", 1, 12,IndexedColors.GREY_25_PERCENT.getIndex());
            drawBorders(2,2,0,0, BorderExtent.ALL, sheet);

            String value = "Heures au format décimal";
            createCell(row3, wb, value, 1, 12,IndexedColors.ROSE.getIndex());
            drawBorders(2,2,1,3, BorderExtent.OUTSIDE, sheet);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 3));

            createCell(row3, wb, totalPrestaion.toString(), 4, 12,IndexedColors.SEA_GREEN.getIndex());
            // payments from DB
            createCell(row3, wb, "00.00", 5, 12,IndexedColors.SEA_GREEN.getIndex());
            // declared Hours from input
            createCell(row3, wb, "86.00", 6, 12,IndexedColors.SKY_BLUE.getIndex());
            // extra hour (computed: worked Hours - declared hours)
            createCell(row3, wb, "20.75", 7, 12,IndexedColors.SEA_GREEN.getIndex());
            // to pay (computed: salary * extra hour)
            createCell(row3, wb, "246.00", 8, 12,IndexedColors.SEA_GREEN.getIndex());
            // report from DB
            createCell(row3, wb, "6.00", 9, 12,IndexedColors.SKY_BLUE.getIndex());
            // balance computed
            createCell(row3, wb, "252.00", 10, 12,IndexedColors.SEA_GREEN.getIndex());

            drawBorders(2,2,3,10, BorderExtent.ALL, sheet);

            // auto size columns
            for (int i = 0; i < 10; i++){
                sheet.autoSizeColumn(i);
            }

            // write to file
            FileOutputStream fileOut = new FileOutputStream("test.xlsx");
            wb.write(fileOut);
            fileOut.close();

        } catch (IOException e) {
            e.printStackTrace();
        }





    }

    private static void createCell(XSSFRow row, XSSFWorkbook wb, String value, int columnIndex, int fontSize, short color){

        Cell cell = row.createCell(columnIndex);

        if (Global.isStringNumeric(value)){
            double v = Double.parseDouble(value);
            cell.setCellValue(v);
        }else {
            cell.setCellValue(value);
        }


        // create it's style
        CellStyle style = wb.createCellStyle();

        // create font
        Font headerFont = wb.createFont();
        headerFont.setFontHeightInPoints((short) fontSize);
        style.setFont(headerFont);

        // add color
        style.setFillForegroundColor(color);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // align
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);

        if (Global.isStringNumeric(value)){
            style.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("00.00"));
        }

        style.setWrapText(true);

        // apply created style
        cell.setCellStyle(style);
    }



    private static void drawBorders(int firstRow, int lastRow, int firstCol, int lastCol, BorderExtent type, XSSFSheet sheet){
        PropertyTemplate pt = new PropertyTemplate();
        pt.drawBorders(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol),
                BorderStyle.THIN, type);
        pt.applyBorders(sheet);
    }
*/
        /*Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.MONTH, -1);

        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH) + 1;
        int year  = c.get(Calendar.YEAR);

        String m = String.format("%02d", month);
        String d = String.format("%02d", day);

        String lastMonthDate = d + "-" + m + "-" + year;

        System.out.println(lastMonthDate);*/



        //Global.stringToLocalDate("2020-02-22");

        LocalDate monday = Global.stringToLocalDate("2019-11-18");
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }

        System.out.println("Monday of the Week: " + monday.toString());
        System.out.println(monday.getMonth().getValue());
        System.out.println(monday.getDayOfMonth());
        System.out.println(monday.getYear());





}}
