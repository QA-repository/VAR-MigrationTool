package EnlyteProject;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;

import java.io.FileOutputStream;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CommonFunctionsMigrationUsingDB {
    public static void exportQueryResultToExcel(String JDBC_URL, String USERNAME, String PASSWORD, String Schema, String sqlQuery, String fileName) throws Exception {

        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            // Change database
            Statement useStatement = connection.createStatement();
            useStatement.execute("USE "+Schema+";");
            useStatement.close();


            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = statement.executeQuery();

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Query_Result");

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            Row headerRow = sheet.createRow(0);
            for (int i = 1; i <= columnCount; i++) {
                headerRow.createCell(i - 1).setCellValue(metaData.getColumnName(i));
            }

            int rowNum = 1;
            while (resultSet.next()) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 1; i <= columnCount; i++) {
                    String cellValue = resultSet.getString(i);
                    if (metaData.getColumnName(i).equalsIgnoreCase("created")) {
                        long timestamp = Long.parseLong(cellValue);
                        cellValue = convertTimestamp(timestamp);
                    }
                    if (metaData.getColumnName(i).equalsIgnoreCase("changed")) {
                        long newtimestamp = Long.parseLong(cellValue);
                        cellValue = convertTimestamp(newtimestamp);
                    }

                    row.createCell(i - 1).setCellValue(cellValue);
                }
            }

            for (int i = 0; i < columnCount; i++) {
                sheet.autoSizeColumn(i);
            }

            String excelFileName = fileName + System.currentTimeMillis() + ".xlsx";
            FileOutputStream fileOut = new FileOutputStream(excelFileName);
            workbook.write(fileOut);
            fileOut.close();

            resultSet.close();
            statement.close();
            connection.close();

            System.out.println("Excel file exported successfully: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String convertTimestamp(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.of("America/Los_Angeles"));
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


}
