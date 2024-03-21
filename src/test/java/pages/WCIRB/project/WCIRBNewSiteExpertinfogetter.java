package pages.WCIRB.project;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class WCIRBNewSiteExpertinfogetter {

    public static void main(String[] args) {
        try {
            FileInputStream excelFile = new FileInputStream(new File("C:\\Users\\Vardot QA\\Desktop\\certifiedexpertfulldata.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(0);

            Sheet extractedDataSheet = workbook.createSheet("Extracted Data");
            int rowIndex = 0;

            int startNumber = 186;
            int range = 737;

            for (int i = startNumber; i < startNumber + range; i++) {
                String apiUrl = "https://www.dev-54ta5gq-zy4q7lto4eli2.us.platformsh.site/node/" + i + "/edit"; // Assuming API URLs are in the first column

                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("cookie","_hjSessionUser_1198465=eyJpZCI6ImQxODdkYjk1LWY3MTctNTUyZC1hODNhLTgwOTVmNzBjMmM0MiIsImNyZWF0ZWQiOjE3MTAyMzg2MDY5NTMsImV4aXN0aW5nIjp0cnVlfQ==; SSESS3e4814da78d3af4b89f11e5e03e90075=fcaYlO8zHIKygLaBFk8i4AxdNmhoH7h8e4wtB8XNX2hjPBm8; SPL3e4814da78d3af4b89f11e5e03e90075=Oa3RpLA7OvzvNkv2BkxnkrPCqea0R1qPv7BCQx8JEMw%3AQii9MFP_AA5DXwIoYN8E9nHHgMpB3RAO4ItTB5kr_tA");

                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    StringBuilder response = new StringBuilder();
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                    }

                    String htmlResponse = response.toString();
                    String title = parseField(htmlResponse, "Title");
                    System.out.println("Title: " + title);
                    String firstName = parseField(htmlResponse, "First name");
                    System.out.println("First Name: " + firstName);
                    String lastName = parseField(htmlResponse, "Last name");
                    System.out.println("Last Name: " + lastName);
                    String authoredOn = parseAuthoredOn(htmlResponse);
                    System.out.println("Authored On: " + authoredOn);
                    String authoredBy = parseAuthoredBy(htmlResponse);
                    System.out.println("Authored By: " + authoredBy);

                    String Publishing=parsePublishing(htmlResponse);
                    System.out.println("Authored By: " + Publishing);

                    String certificationNumber = parseField(htmlResponse, "Certification number");
                    System.out.println("Certification Number: " + certificationNumber);

                    Row newRow = extractedDataSheet.createRow(rowIndex++);
                    newRow.createCell(0).setCellValue(title);
                    newRow.createCell(1).setCellValue(firstName);
                    newRow.createCell(2).setCellValue(lastName);
                    newRow.createCell(3).setCellValue(authoredOn);
                    newRow.createCell(4).setCellValue(authoredBy);
                    newRow.createCell(5).setCellValue(certificationNumber);
                    newRow.createCell(6).setCellValue(Publishing);

                    System.out.println("Record for ID " + i + " extracted successfully.");
                } else {
                    System.out.println("Failed to fetch data from API: " + apiUrl);
                }

                connection.disconnect();
            }

            FileOutputStream outputStream = new FileOutputStream("aapis.xlsx");
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String parseField(String htmlResponse, String fieldName) {
        Document doc = Jsoup.parse(htmlResponse);
        Elements elements = doc.select("input[data-drupal-selector^=edit-field-" + fieldName.toLowerCase().replace(" ", "-") + "], input[data-drupal-selector^=edit-" + fieldName.toLowerCase().replace(" ", "-") + "]");

        for (Element element : elements) {
            String value = element.attr("value");
            if (!value.isEmpty()) {
                return value;
            }
        }
        return "Field not found";
    }

    private static String parseAuthoredOn(String htmlResponse) {
        Document doc = Jsoup.parse(htmlResponse);
        Element element = doc.selectFirst("input[data-drupal-selector^=edit-created-0-value-date]");
        String date = element.attr("value");
        Element timeElement = doc.selectFirst("input[data-drupal-selector^=edit-created-0-value-time]");
        String time = timeElement.attr("value");
        return date + " " + time;
    }

    private static String parseAuthoredBy(String htmlResponse) {
        Document doc = Jsoup.parse(htmlResponse);
        Element element = doc.selectFirst("input[data-drupal-selector^=edit-uid-0-target-id]");
        return element.attr("value");
    }
    private static String parsePublishing(String htmlResponse) {
        Document doc = Jsoup.parse(htmlResponse);
        Element element = doc.selectFirst(".container-inline.js-form-item.form-item.js-form-type-item.form-type--item.js-form-item-moderation-state-0-current.form-item--moderation-state-0-current");
        return element.text().trim();
    }
}
