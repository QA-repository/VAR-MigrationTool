package pages.WCIRB.project;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExcelHtmlTagRemover {

    public static void main(String[] args) {
        String inputFile = "C:\\Users\\Vardot QA\\Downloads\\[Migration] Classification Record Testing Result.xlsx"; // Path to your input Excel file
        String outputFile = "C:\\Users\\Vardot QA\\Downloads\\[Migration] Classification Record Testing Resultw.xlsx"; // Path to your output Excel file

        try {
            InputStream inputStream = new FileInputStream(inputFile);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (cell.getCellType() == CellType.STRING) {
                        String cellValue = cell.getStringCellValue();
                        String cleanedValue = removeHtmlTags(cellValue);
                        cell.setCellValue(cleanedValue);
                    }
                }
            }

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            workbook.write(outputStream);
            workbook.close();
            inputStream.close();
            outputStream.close();

            System.out.println("HTML tags removed successfully and saved to " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String removeHtmlTags(String html) {
        return Jsoup.parse(html).text();
    }

}
