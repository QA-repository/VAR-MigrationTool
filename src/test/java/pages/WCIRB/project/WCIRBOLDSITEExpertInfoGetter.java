package pages.WCIRB.project;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class WCIRBOLDSITEExpertInfoGetter {

    public static void main(String[] args) {
        try {
            String excelFilePath = "C:\\Users\\Vardot QA\\Desktop\\certifiedexpertolddata.xlsx";
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Data");


            String excelFileName = "C:\\Users\\Vardot QA\\Desktop\\certifiedexpertfulldata.xlsx";

            Workbook excelWorkbook = WorkbookFactory.create(new FileInputStream(new File(excelFileName)));
            Sheet excelSheet = excelWorkbook.getSheetAt(0);

            for (Row row : excelSheet) {
                String urlValue = row.getCell(0).getStringCellValue();
                Connection connection = Jsoup.connect("https://www.wcirb.com/node/" + urlValue + "/edit");
             connection.cookie("cookie","_hjSessionUser_1198465=eyJpZCI6ImQ1NTg4ZmIyLTgyNGItNTBkYi1iYWJjLWExMTU3N2VlMWNlZCIsImNyZWF0ZWQiOjE3MDkyMDMyMzMzNzIsImV4aXN0aW5nIjp0cnVlfQ==; _ga_XSFPBV07M7=GS1.1.1709212775.2.0.1709212775.60.0.0; Drupal.tableDrag.showWeight=0; context_breakpoints=none; _gid=GA1.2.628098594.1710423983; _hjSession_1198465=eyJpZCI6IjIyNTBiNmY5LWI3YTEtNGZkYS04OWUxLWQ5ZDZjYmEyODI3MyIsImMiOjE3MTA1MDkyNjUzMTgsInMiOjAsInIiOjAsInNiIjowLCJzciI6MCwic2UiOjAsImZzIjowLCJzcCI6MX0=; SSESSdcfc7fdb45e6cda82752ddad34647bcc=LXXlpv1wlXQh0FxBogrBt0MrCnR3zC9YPd_joRtPpnY; _ga=GA1.1.1977765049.1709203232; _ga_5VNYL7KKEG=GS1.1.1710509262.17.1.1710509808.29.0.0");
                Document document = connection.get();

                String publicationDate = document.select("input#edit-field-publication-date-und-0-value-datepicker-popup-0").attr("value");
                String publicationTime = document.select("input#edit-field-publication-date-und-0-value-timeEntry-popup-1").attr("value");
                String authorName = document.select("input#edit-name").attr("value");
                String authoredOn = document.select("input#edit-date").attr("value");

                boolean isPublished = document.select("input#edit-status").hasAttr("checked");
                boolean containsHelloVardot = document.select("a[href='/users/vardot']").text().contains("Hello vardot");
                System.out.println("containsHelloVardot " + containsHelloVardot);

                System.out.println("API Call for URL: " + urlValue);
                System.out.println("Extracted data:");

                System.out.println("Publication Date: " + publicationDate);
                System.out.println("Publication Time: " + publicationTime);
                System.out.println("Author Name: " + authorName);
                System.out.println("Authored On: " + authoredOn);
                System.out.println("Published: " + isPublished);

                System.out.println("------------------------------------");

                Row outputRow = sheet.createRow(sheet.getLastRowNum() + 1);


                outputRow.createCell(1).setCellValue(publicationDate);
                outputRow.createCell(2).setCellValue(publicationTime);
                outputRow.createCell(3).setCellValue(authorName);
                outputRow.createCell(4).setCellValue(authoredOn);
                outputRow.createCell(5).setCellValue(isPublished);


            }

            try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
                workbook.write(fileOut);
            }

            workbook.close();
            excelWorkbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


