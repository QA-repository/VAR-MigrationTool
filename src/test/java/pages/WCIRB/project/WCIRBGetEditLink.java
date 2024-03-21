package pages.WCIRB.project;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class WCIRBGetEditLink {

    public static void main(String[] args) {
        try {
            FileInputStream excelFile = new FileInputStream("C:\\Users\\Vardot QA\\Desktop\\WCIRBDOCUMENToutput.xlsx");
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                Cell urlCell = row.getCell(0);

                if (urlCell != null) {
                    String url = urlCell.getStringCellValue();

                    String extractedValue = extractValueFromApi(url);

                    System.out.println("Row " + (row.getRowNum() + 1) + " " + extractedValue);

                    Cell resultCell = row.createCell(1);
                    resultCell.setCellValue(extractedValue);
                } else {
                    System.out.println("Row " + (row.getRowNum() + 1) + " URL not found");
                }
            }

            FileOutputStream outputStream = new FileOutputStream("C:\\Users\\Vardot QA\\Desktop\\WCIRBDOCUMENTeditlink.xlsx");
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String extractValueFromApi(String url) throws IOException {
        Connection connection = Jsoup.connect(url);
        connection.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        connection.timeout(15000);
        connection.cookie("cookie","SPL3e4814da78d3af4b89f11e5e03e90075=ZKzqxwK9ZDTgvNktB2jYeT539ad2zdCLEFR8OKKTV3o%3A_-uOYO8cJ3U36hrAamZMifQi2JHUh9krKwu3RzNI-1s; SSESS3e4814da78d3af4b89f11e5e03e90075=f7FPBp492L0aqy2bsa8nRYJV-Ki5-VCCLPdHa29dlEJ5rZjQ; _hjSessionUser_1198465=eyJpZCI6ImQxODdkYjk1LWY3MTctNTUyZC1hODNhLTgwOTVmNzBjMmM0MiIsImNyZWF0ZWQiOjE3MTAyMzg2MDY5NTMsImV4aXN0aW5nIjp0cnVlfQ==; _hjSession_1198465=eyJpZCI6IjI5ZTA0ZGRmLTc3YzMtNDQ4MC04ZGRiLTM5OTdkYjA4NmI0MiIsImMiOjE3MTAyODEzNDc2NjUsInMiOjAsInIiOjAsInNiIjowLCJzciI6MCwic2UiOjAsImZzIjowLCJzcCI6MH0=");

        Document doc = connection.get();        Elements breadcrumbLinks = doc.select(".gin-breadcrumb__link");

        for (Element link : breadcrumbLinks) {
            if (link.attr("href").contains("/node/")) {
                System.out.println( link.attr("href"));

                return "https://www.dev-54ta5gq-zy4q7lto4eli2.us.platformsh.site" + link.attr("href");
            }
        }

        return "Not found";
    }
}
