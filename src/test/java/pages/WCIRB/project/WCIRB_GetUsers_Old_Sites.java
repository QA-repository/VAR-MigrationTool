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
import java.util.ArrayList;
import java.util.List;

public class WCIRB_GetUsers_Old_Sites {

    public static void main(String[] args) {
        String htmlFilePath = "C:\\Users\\Vardot QA\\Downloads\\People _ WCIRB California.html";
        String excelFilePath = "C:\\Users\\Vardot QA\\Downloads\\user_data.xlsx";

        List<UserData> userDataList = extractDataFromHTML(htmlFilePath);
        exportToExcel(userDataList, excelFilePath);

        System.out.println("Data exported to " + excelFilePath);
    }
    private static List<UserData> extractDataFromHTML(String htmlFilePath) {
        List<UserData> userDataList = new ArrayList<>();

        try {
            File input = new File(htmlFilePath);
            Document document = Jsoup.parse(input, "UTF-8");

            Elements tdElements = document.select("td.views-field.views-field-name");
            for (Element td : tdElements) {
                String username = td.select("a.username").text().trim();

                // Get the next sibling of <br>, if it exists
                Element br = td.select("br").first();
                String email = (br != null && br.nextSibling() != null) ? br.nextSibling().toString().trim() : "";

                userDataList.add(new UserData(username, email));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userDataList;
    }



    private static void exportToExcel(List<UserData> userDataList, String excelFilePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("User Data");

            // Header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Username");
            headerRow.createCell(1).setCellValue("Email");

            // Data rows
            int rowNum = 1;
            for (UserData userData : userDataList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(userData.getUsername());
                row.createCell(1).setCellValue(userData.getEmail());
            }

            // Write the output to a file
            try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class UserData {
        private final String username;
        private final String email;

        public UserData(String username, String email) {
            this.username = username;
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }
    }
}
