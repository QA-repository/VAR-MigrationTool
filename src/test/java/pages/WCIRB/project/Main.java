package pages.WCIRB.project;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws IOException {
        // Initialize Excel workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Links");
        int rowNumber = 0;

        // Iterate through API calls
        for (int i = 0; i < 8; i++) {
            String apiUrl = "https://www.dev-54ta5gq-zy4q7lto4eli2.us.platformsh.site/admin/content?status=All&type=classification_record&title=&created_from%5Bdate%5D=&created_to%5Bdate%5D=&uid=&items_per_page=100&order=changed&sort=desc&page=" + i;

            // Make HTTP GET request
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestProperty("cookie","_hjSessionUser_1198465=eyJpZCI6IjlmMjJhMmRlLTI1ODctNWM3YS1hZDA0LWIzN2EzMjAzN2UyZiIsImNyZWF0ZWQiOjE3MTIxMDU5NjI4MjcsImV4aXN0aW5nIjp0cnVlfQ==; _hjSession_1198465=eyJpZCI6IjI5OWIwYmFhLWUyMTktNGNhNC04OWQxLTFkMGY4MTVhY2YzNyIsImMiOjE3MTIxODA5NDM3NzcsInMiOjAsInIiOjAsInNiIjowLCJzciI6MCwic2UiOjAsImZzIjowLCJzcCI6MH0=; SSESS3e4814da78d3af4b89f11e5e03e90075=ft5032vMSPi7XIw1YRgp2ZRkebNQtgCkfro8YX20XfYhSHa2");
            connection.setRequestMethod("GET");

            // Parse HTML response
            Document doc = Jsoup.parse(connection.getInputStream(), null, "");
            Elements links = doc.select("ul.dropbutton a[href]");

            // Iterate through extracted links
            for (Element link : links) {
                String text = link.text().trim();
                if (text.equals("Edit")) {
                    String href = link.attr("href");
                    System.out.println(href); // Print to console

                    // Write to Excel sheet
                    Row row = sheet.createRow(rowNumber++);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(href);
                }
            }
        }

        // Save Excel workbook
        try (FileOutputStream outputStream = new FileOutputStream("links.xlsx")) {
            workbook.write(outputStream);
        }

        // Close workbook
        workbook.close();
    }
}

