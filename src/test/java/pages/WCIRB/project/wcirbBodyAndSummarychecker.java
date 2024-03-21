package pages.WCIRB.project;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class wcirbBodyAndSummarychecker {
    public static void main(String[] args) {
        try {
            FileInputStream file = new FileInputStream("C:\\Users\\Vardot QA\\Documents\\showed body.xlsx");
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

            Row headerRow = sheet.getRow(0);
            Cell summaryResultHeader = headerRow.createCell(3);
            summaryResultHeader.setCellValue("Summary Match");
            Cell titleResultHeader = headerRow.createCell(5);
            titleResultHeader.setCellValue("Title Match");

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Cell apiColumn = row.getCell(2);
                Cell columnBValue = row.getCell(1);
                Cell columnAValue = row.getCell(0);
                String columnBExpectedValue;

                String apiURL = apiColumn.getStringCellValue();
                if (columnBValue != null && columnBValue.getCellType() == CellType.STRING) {
                    columnBExpectedValue = columnBValue.getStringCellValue();
                }
                else {
                    columnBExpectedValue="";

                }
                String columnAExpectedValue;
                if (columnAValue != null && columnAValue.getCellType() == CellType.STRING) {
                    columnAExpectedValue = columnAValue.getStringCellValue();
                }
                else {
                    columnAExpectedValue="";

                }
try {
    String html = callAPI(apiURL);
    String summary = extractSummary(html);
    String title = extractTitle(html);
    System.out.println(apiURL);
    Cell summaryResultCell = row.createCell(3);
    summaryResultCell.setCellValue(summary.contains(columnBExpectedValue) ? "Match" : "No Match");
    System.out.println(  summary.contains(columnBExpectedValue) ? "Match" : "No Match");
    Cell titleResultCell = row.createCell(5);
    titleResultCell.setCellValue(title.contains(columnAExpectedValue) ? "Match" : "No Match");
    System.out.println(  title.contains(columnBExpectedValue) ? "Match" : "No Match");
}catch (Exception E){
    Cell titleResultCell = row.createCell(5);

    titleResultCell.setCellValue(E.toString());

}


            }

            FileOutputStream outFile = new FileOutputStream("output_file.xlsx");
            workbook.write(outFile);

            outFile.close();
            workbook.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String callAPI(String url) throws IOException {

        return Jsoup.connect(url).get().html();
    }

    private static String extractSummary(String html) {

        Document doc = Jsoup.parse(html);
        Element summaryElement = doc.selectFirst("div.vmh-page-description");
        return summaryElement != null ? summaryElement.text() : "";
    }

    private static String extractTitle(String html) {

        Document doc = Jsoup.parse(html);
        Element BOdyelement = doc.selectFirst("div[data-component-id=wcirb:field]");
        return BOdyelement != null ? BOdyelement.text() : "";
    }
}
