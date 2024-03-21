package pages.WCIRB.project;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WCIRB_ContentGetter {

    public static void main(String[] args) {
        String filePath = "C:\\Users\\Vardot QA\\Downloads\\Content _ WCIRB California.html";
        String targetHtmlSelector = "td.views-field.views-field-title";

        try {
            String htmlContent = readHtmlFromFile(filePath);
            Elements elements = extractElements(htmlContent, targetHtmlSelector);

            for (Element element : elements) {
                String contentName = element.select("a").text();
                String hrefValue = element.select("a").attr("href");

                printProgress("Content Name: " + contentName);
                printProgress("Href Value: " + hrefValue);
            }

            writeToExcel(elements);

            printProgress("Data exported to Excel successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            printProgress("Error occurred during HTML file reading or extraction.");
        }
    }

    private static String readHtmlFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        return Jsoup.parse(file, "UTF-8").html();
    }

    private static Elements extractElements(String html, String targetHtmlSelector) {
        Document doc = Jsoup.parse(html);
        return doc.select(targetHtmlSelector);
    }

    private static void writeToExcel(Elements elements) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("ContentData");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Content Name");
            headerRow.createCell(1).setCellValue("Href Value");

            int rowNum = 1;
            for (Element element : elements) {
                String contentName = element.select("a").text();
                String hrefValue = element.select("a").attr("href");

                Row dataRow = sheet.createRow(rowNum++);
                dataRow.createCell(0).setCellValue(contentName);
                dataRow.createCell(1).setCellValue(hrefValue);
            }

            try (FileOutputStream fileOut = new FileOutputStream("ContentData.xlsx")) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
            printProgress("Error occurred while writing to Excel.");
        }
    }

    private static void printProgress(String message) {
        System.out.println(message);
    }
}
