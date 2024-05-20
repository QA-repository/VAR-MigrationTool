package pages.WCIRB.project;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassCodeReaderEnhanced {

    public static void main(String[] args) {
        String baseUrl = "https://www.dev-54ta5gq-zy4q7lto4eli2.us.platformsh.site/taxonomy/term/";
        int start = 366;
        int end = 908;

        System.out.println("Starting data extraction...");

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet dataSheet = workbook.createSheet("API Data");
            Sheet classCountSheet = workbook.createSheet("Class Count");

            int rowIndex = 0;

            // Define the fields to be extracted from each class
            List<String> fieldNames = Arrays.asList("Field1", "Field2", "Field3");

            Map<String, Integer> classCounts = new HashMap<>();

            for (int i = start; i <= end; i++) {
                String url = baseUrl + i + "/edit";
                System.out.println("Calling API:" + url);
                Document doc = null;
                try {
                    doc = Jsoup.connect(url)
                            .cookie("cookie", "_hjSessionUser_1198465=eyJpZCI6ImQxODdkYjk1LWY3MTctNTUyZC1hODNhLTgwOTVmNzBjMmM0MiIsImNyZWF0ZWQiOjE3MTAyMzg2MDY5NTMsImV4aXN0aW5nIjp0cnVlfQ==; SPL3e4814da78d3af4b89f11e5e03e90075=Oa3RpLA7OvzvNkv2BkxnkrPCqea0R1qPv7BCQx8JEMw%3AQii9MFP_AA5DXwIoYN8E9nHHgMpB3RAO4ItTB5kr_tA; _hjSession_1198465=eyJpZCI6IjBjMGJmMmNiLTY1YzYtNDEzNi1hODdlLTQxZTlhZjZmNDIyMyIsImMiOjE3MTE3NjEyMzIwMTYsInMiOjAsInIiOjAsInNiIjowLCJzciI6MCwic2UiOjAsImZzIjowLCJzcCI6MX0=; SSESS3e4814da78d3af4b89f11e5e03e90075=QwsKomphl2o3uZidr%2C8V8HMzH1L2nty7UK-OynS9YfzCTNgk")
                            .timeout(50000 * 2)
                            .get();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (doc != null) {
                    Elements paragraphs = doc.select("div.paragraphs-subform.js-form-wrapper.form-wrapper");

                    int classCount = paragraphs.size();
                    classCounts.put(url, classCount);

                    if (classCount == 0) {
                        int responseCode = getResponseCode(url);
                        Row newRow = dataSheet.createRow(rowIndex++);
                        int cellIndex = 0;
                        newRow.createCell(cellIndex++).setCellValue(url); // API identifier
                        newRow.createCell(cellIndex++).setCellValue(responseCode); // Response code
                        newRow.createCell(cellIndex++).setCellValue(classCount); // Class count
                        newRow.createCell(cellIndex++).setCellValue(""); // Field 1
                        newRow.createCell(cellIndex++).setCellValue(""); // Field 2
                        newRow.createCell(cellIndex++).setCellValue(""); // Field 3

                    }

                    int responseCode = getResponseCode(url);

                    for (Element paragraph : paragraphs) {
                        // Initialize a list to hold field values
                        List<String> fieldValues = new ArrayList<>();

                        Elements fields = paragraph.select("input, select, textarea");
                        for (Element field : fields) {
                            if (field.tagName().equals("select")) {
                                fieldValues.add(field.select("option[selected]").text());
                            } else {
                                fieldValues.add(field.val());
                            }
                        }
                        // Fill null values for missing fields
                        for (int j = fieldValues.size(); j < fieldNames.size(); j++) {
                            fieldValues.add("null");

                        }

                        // Create a new row and populate it with data
                        Row newRow = dataSheet.createRow(rowIndex++);
                        int cellIndex = 0;
                        newRow.createCell(cellIndex++).setCellValue(url); // API identifier
                        newRow.createCell(cellIndex++).setCellValue(responseCode); // Response code
                        newRow.createCell(cellIndex++).setCellValue(classCount); // Class count

                        for (String value : fieldValues) {
                            newRow.createCell(cellIndex++).setCellValue(value);
                        }
                    }
                }
            }

            // Writing class counts to the separate sheet
            int countRowIndex = 0;
            for (Map.Entry<String, Integer> entry : classCounts.entrySet()) {
                Row newRow = classCountSheet.createRow(countRowIndex++);
                newRow.createCell(0).setCellValue(entry.getKey()); // URL
                newRow.createCell(1).setCellValue(entry.getValue()); // Class count
            }

            String excelFilePath = "C:\\Users\\Vardot QA\\Downloads\\VAR-MigrationTool\\api_datatest.xlsx";

            try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
                workbook.write(fileOut);
                System.out.println("Excel file generated successfully: " + excelFilePath);
            } catch (IOException e) {
                System.out.println("Error occurred while generating Excel file: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Error occurred while creating workbook: " + e.getMessage());
        }
    }

    private static int getResponseCode(String url) {
        try {
            Connection.Response response = Jsoup.connect(url)
                    .cookie("cookie", "_hjSessionUser_1198465=eyJpZCI6ImQxODdkYjk1LWY3MTctNTUyZC1hODNhLTgwOTVmNzBjMmM0MiIsImNyZWF0ZWQiOjE3MTAyMzg2MDY5NTMsImV4aXN0aW5nIjp0cnVlfQ==; SPL3e4814da78d3af4b89f11e5e03e90075=Oa3RpLA7OvzvNkv2BkxnkrPCqea0R1qPv7BCQx8JEMw%3AQii9MFP_AA5DXwIoYN8E9nHHgMpB3RAO4ItTB5kr_tA; _hjSession_1198465=eyJpZCI6IjBjMGJmMmNiLTY1YzYtNDEzNi1hODdlLTQxZTlhZjZmNDIyMyIsImMiOjE3MTE3NjEyMzIwMTYsInMiOjAsInIiOjAsInNiIjowLCJzciI6MCwic2UiOjAsImZzIjowLCJzcCI6MX0=; SSESS3e4814da78d3af4b89f11e5e03e90075=QwsKomphl2o3uZidr%2C8V8HMzH1L2nty7UK-OynS9YfzCTNgk")
                    .execute();
            return response.statusCode();
        } catch (IOException e) {
            e.printStackTrace();
            return -1; // Return -1 if unable to fetch response code
        }
    }
}
