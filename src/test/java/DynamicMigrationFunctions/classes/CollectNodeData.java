package DynamicMigrationFunctions.classes;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;

public class CollectNodeData {
    public static void main(String[] args) {
        try {
            String excelFilePathFinalResult = "C:\\Users\\Vardot QA\\Desktop\\document idresult.xlsx";
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Mapping Data");
            String excelEditLinks = "C:\\Users\\Vardot QA\\Desktop\\document id.xlsx";

            Workbook excelWorkbook = WorkbookFactory.create(new FileInputStream(new File(excelEditLinks)));
            Sheet excelSheet = excelWorkbook.getSheetAt(0);

            for (Row row : excelSheet) {

            }
            try (FileOutputStream fileOut = new FileOutputStream(excelEditLinks)) {
                workbook.write(fileOut);
            }
            workbook.close();
            excelWorkbook.close();

        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
