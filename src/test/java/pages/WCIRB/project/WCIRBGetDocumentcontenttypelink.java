package pages.WCIRB.project;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WCIRBGetDocumentcontenttypelink {

    private static final int THREAD_COUNT = 10;

    public static void main(String[] args) {
        try {
            new WCIRBGetDocumentcontenttypelink().run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void run() throws IOException {
        System.out.println("Starting the program...");

        Workbook workbook = new XSSFWorkbook("C:\\Users\\Vardot QA\\Desktop\\document node id newsite.xlsx");

        Sheet sheet = workbook.getSheetAt(0);

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        System.out.println("Processing rows...");

        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            executorService.submit(() -> processRow(row));

            System.out.println("Processing row: " + row.getRowNum() + " out of " + sheet.getLastRowNum());
        }

        executorService.shutdown();

        while (!executorService.isTerminated()) {
        }

        System.out.println("Saving the updated workbook...");

        // Save the updated workbook
        try (FileOutputStream outputStream = new FileOutputStream("WCIRBDOCUMENToutput.xlsx")) {
            workbook.write(outputStream);
        }

        workbook.close();

        System.out.println("Program completed.");
    }

    private void processRow(Row row) {
        try {
            Cell cell = row.getCell(0);

            String placeholder = cell.getStringCellValue();

            String encodedPlaceholder =  URLEncoder.encode(placeholder, StandardCharsets.UTF_8.toString());


            String apiUrl = "https://www.dev-54ta5gq-zy4q7lto4eli2.us.platformsh.site/admin/content?status=All&type=document&title="
                    + encodedPlaceholder
                    + "&created_from%5Bdate%5D=&created_to%5Bdate%5D=&uid=&items_per_page=100";

            String response = sendGetRequest(apiUrl);

            String extractedValue = extractValueFromResponse(response);

            Cell resultCell = row.createCell(1);
            resultCell.setCellValue(extractedValue);

            System.out.println("Row processed: " + row.getRowNum());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String sendGetRequest(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestProperty("cookie","SPL3e4814da78d3af4b89f11e5e03e90075=ZKzqxwK9ZDTgvNktB2jYeT539ad2zdCLEFR8OKKTV3o%3A_-uOYO8cJ3U36hrAamZMifQi2JHUh9krKwu3RzNI-1s; SSESS3e4814da78d3af4b89f11e5e03e90075=f7FPBp492L0aqy2bsa8nRYJV-Ki5-VCCLPdHa29dlEJ5rZjQ; _hjSessionUser_1198465=eyJpZCI6ImQxODdkYjk1LWY3MTctNTUyZC1hODNhLTgwOTVmNzBjMmM0MiIsImNyZWF0ZWQiOjE3MTAyMzg2MDY5NTMsImV4aXN0aW5nIjp0cnVlfQ==; _hjSession_1198465=eyJpZCI6IjI5ZTA0ZGRmLTc3YzMtNDQ4MC04ZGRiLTM5OTdkYjA4NmI0MiIsImMiOjE3MTAyODEzNDc2NjUsInMiOjAsInIiOjAsInNiIjowLCJzciI6MCwic2UiOjAsImZzIjowLCJzcCI6MH0=");
        InputStream inputStream = connection.getInputStream();
        StringBuilder response = new StringBuilder();

        int bytesRead;
        byte[] buffer = new byte[1024];
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            response.append(new String(buffer, 0, bytesRead));
        }
        System.out.println(apiUrl + connection.getResponseCode());

        inputStream.close();
        connection.disconnect();

        return response.toString();
    }

    private String extractValueFromResponse(String response) {
        Document document = Jsoup.parse(response);

        Elements tdElements = document.select("td.views-field-title a[href]");

        if (!tdElements.isEmpty()) {
            return tdElements.first().attr("href");
        } else {
            return "Value not found in response";
        }
    }
}
