package pages.WCIRB.project;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class WCIRBDocumentChecker {



    public static void main(String[] args) {
        try {
            FileInputStream file = new FileInputStream("C:\\Users\\Vardot QA\\Desktop\\document names on dev site.xlsx");
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell cell = row.getCell(0);

                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String originalValue = cell.getStringCellValue();

                    String sanitizedValue = originalValue.replaceAll("[^a-zA-Z0-9]+", "-");

                    sanitizedValue = sanitizedValue.replaceAll("^-|-$", "");

                    String[] excludedWords = {"of", "and", "the"};

                    for (String word : excludedWords) {
                        sanitizedValue = sanitizedValue.replaceAll("\\b" + word + "\\b", "");
                    }

                    sanitizedValue = sanitizedValue.replaceAll("--+", "-");

                    String url = "https://www.dev-54ta5gq-zy4q7lto4eli2.us.platformsh.site/document/" + sanitizedValue;

                    int responseCode = getResponseCode(url);

                    System.out.println("URL: " + url + " - Response Code: " + responseCode);

                    Cell responseCodeCell = row.createCell(1, CellType.NUMERIC);
                    responseCodeCell.setCellValue(responseCode);
                }
            }

            FileOutputStream fileOut = new FileOutputStream("C:\\Users\\Vardot QA\\Desktop\\media links on devsite WCIRB_updated.xls");
            workbook.write(fileOut);
            fileOut.close();

            workbook.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getResponseCode(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();


        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        connection.disconnect();

        return responseCode;
    }
}
