package pages.WCIRB.project;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class WCIRBMediaChecker {

    private static final String STATIC_COOKIE = "x";

    private static final String BASIC_AUTH_USERNAME = "xx";
    private static final String BASIC_AUTH_PASSWORD = "xxx";

    public static void main(String[] args) {
        try {
            FileInputStream file = new FileInputStream("C:\\Users\\Vardot QA\\Desktop\\live WCIRB media.xlsx");
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell cell = row.getCell(0);

                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String url = cell.getStringCellValue();
                    int responseCode = getResponseCode(url);

                    System.out.println("URL: " + url + " - Response Code: " + responseCode);

                    Cell responseCodeCell = row.createCell(1, CellType.NUMERIC);
                    responseCodeCell.setCellValue(responseCode);
                }
            }

            FileOutputStream fileOut = new FileOutputStream("C:\\Users\\Vardot QA\\Desktop\\media links on Live WCIRB_updated.xls");
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

        connection.setRequestProperty("Cookie", STATIC_COOKIE);

        String authString = BASIC_AUTH_USERNAME + ":" + BASIC_AUTH_PASSWORD;
        String encodedAuthString = java.util.Base64.getEncoder().encodeToString(authString.getBytes());
        connection.setRequestProperty("Authorization", "Basic " + encodedAuthString);

        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        connection.disconnect();

        return responseCode;
    }
}
