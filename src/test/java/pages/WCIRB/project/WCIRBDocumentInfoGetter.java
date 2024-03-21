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

public class WCIRBDocumentInfoGetter {

        public static void main(String[] args) {
            try {
                String excelFilePath = "C:\\Users\\Vardot QA\\Desktop\\document idresult.xlsx";
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Data");


                String excelFileName = "C:\\Users\\Vardot QA\\Desktop\\document id.xlsx";

                Workbook excelWorkbook = WorkbookFactory.create(new FileInputStream(new File(excelFileName)));
                Sheet excelSheet = excelWorkbook.getSheetAt(0);

                for (Row row : excelSheet) {
                    String urlValue = row.getCell(0).getStringCellValue();
                    Connection connection = Jsoup.connect("https://www.wcirb.com/node/" + urlValue + "/edit");
                    connection.cookie("cookie", "_hjSessionUser_1198465=eyJpZCI6ImQ1NTg4ZmIyLTgyNGItNTBkYi1iYWJjLWExMTU3N2VlMWNlZCIsImNyZWF0ZWQiOjE3MDkyMDMyMzMzNzIsImV4aXN0aW5nIjp0cnVlfQ==; _ga_XSFPBV07M7=GS1.1.1709212775.2.0.1709212775.60.0.0; Drupal.tableDrag.showWeight=0; context_breakpoints=none; _gid=GA1.2.73958334.1710239675; _hjSession_1198465=eyJpZCI6IjNmZWI1YmY0LTVjMTQtNDE4ZS05MTM3LWM2N2EwMjc2MDkyMiIsImMiOjE3MTAyMzk2NzU0ODUsInMiOjAsInIiOjAsInNiIjowLCJzciI6MCwic2UiOjAsImZzIjowLCJzcCI6MH0=; SSESSdcfc7fdb45e6cda82752ddad34647bcc=AiGfJUJQ-811eI_JLm4JgKt7rNAQXMpHV4bUytyENMU; _ga=GA1.1.1977765049.1709203232; _ga_5VNYL7KKEG=GS1.1.1710239674.13.1.1710243144.60.0.0");
                    Document document = connection.get();
                    String oldSiteTitle = document.select("div.form-item-title input#edit-title").attr("value");
                    String oldSiteBody = document.select("textarea#edit-field-abstract-und-0-value").text();
                    String oldSiteMediaFileName = document.select("a[href$=.pdf]").text();
                    String oldSiteDocumentType = document.select("select#edit-field-document-type-und option[selected]").text();

                    String committeeDocumentType = document.select("select#edit-field-committee-document-type-und option[selected]").text();
                    String meetingDate = document.select("input#edit-field-meeting-date-und-0-value-datepicker-popup-0").attr("value");
                    String publicationDate = document.select("input#edit-field-publication-date-und-0-value-datepicker-popup-0").attr("value");
                    String publicationTime = document.select("input#edit-field-publication-date-und-0-value-timeEntry-popup-1").attr("value");
                    String oldSiteTopic = document.select("select#edit-field-topic-und option[selected]").text();
                    String oldSiteAudience = document.select("select#edit-field-audience-und option[selected]").text();
                    String oldSiteKeyword = document.select("input#edit-field-keywords-tags-und").val();
                    String oldSiteSearchWeight = document.select("select#edit-field-search-weight-und option[selected]").text();
                    String authorName = document.select("input#edit-name").attr("value");
                    String authoredOn = document.select("input#edit-date").attr("value");
                    String formNumber = document.select("input#edit-field-form-id-und-0-value").attr("value");

                    boolean isPublished = document.select("input#edit-status").hasAttr("checked");
                    boolean containsHelloVardot = document.select("a[href='/users/vardot']").text().contains("Hello vardot");
                    System.out.println("containsHelloVardot " + containsHelloVardot);

                    System.out.println("API Call for URL: " + urlValue);
                    System.out.println("Extracted data:");
                    System.out.println("Title: " + oldSiteTitle);
                    System.out.println("Body: " + oldSiteBody);
                    System.out.println("Media File Name: " + oldSiteMediaFileName);
                    System.out.println("Document Type: " + oldSiteDocumentType);
                    System.out.println("Committee Document Type: " + committeeDocumentType);
                    System.out.println("Meeting Date: " + meetingDate);
                    System.out.println("Publication Date: " + publicationDate);
                    System.out.println("Publication Time: " + publicationTime);
                    System.out.println("Topic: " + oldSiteTopic);
                    System.out.println("Audience: " + oldSiteAudience);
                    System.out.println("Keyword: " + oldSiteKeyword);
                    System.out.println("Search Weight: " + oldSiteSearchWeight);
                    System.out.println("Author Name: " + authorName);
                    System.out.println("Authored On: " + authoredOn);
                    System.out.println("Published: " + isPublished);
                    System.out.println("formNumber: " + formNumber);

                    System.out.println("------------------------------------");

                    Row outputRow = sheet.createRow(sheet.getLastRowNum() + 1);

                    outputRow.createCell(0).setCellValue(oldSiteTitle);
                    outputRow.createCell(1).setCellValue(oldSiteBody);
                    outputRow.createCell(2).setCellValue(oldSiteMediaFileName);
                    outputRow.createCell(3).setCellValue(oldSiteDocumentType);
                    outputRow.createCell(4).setCellValue(committeeDocumentType);
                    outputRow.createCell(5).setCellValue(meetingDate);
                    outputRow.createCell(6).setCellValue(publicationDate);
                    outputRow.createCell(7).setCellValue(publicationTime);
                    outputRow.createCell(8).setCellValue(oldSiteTopic);
                    outputRow.createCell(9).setCellValue(oldSiteAudience);
                    outputRow.createCell(10).setCellValue(oldSiteKeyword);
                    outputRow.createCell(11).setCellValue(oldSiteSearchWeight);
                    outputRow.createCell(12).setCellValue(authorName);
                    outputRow.createCell(13).setCellValue(authoredOn);
                    outputRow.createCell(14).setCellValue(isPublished);
                    outputRow.createCell(15).setCellValue(formNumber);


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


