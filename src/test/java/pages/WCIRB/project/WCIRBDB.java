package pages.WCIRB.project;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tools.ant.types.selectors.SelectSelector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class WCIRBDB {
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/live";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) throws Exception {
        WCIRBDB wcirbDB = new WCIRBDB();
        wcirbDB.exportQueryResultToExcel();
    }

    public void exportQueryResultToExcel() throws Exception {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            // Change database
            Statement useStatement = connection.createStatement();
            useStatement.execute("USE live;");
            useStatement.close();

            // Prepare and execute main query
      String sqlQuery = "SELECT " +
                    "n.nid, " +
                    "n.*, " +
                    "lsb.field_sidebar_box_nid, " +
                    "fb.body_value, " +
                    "fb.body_summary, " +
                    "FSW.field_search_weight_value, " +
                    "FDT.field_topic_tid " +
                    "FROM " +
                    "node n " +
                    "LEFT JOIN " +
                    "field_data_field_sidebar_box lsb ON lsb.entity_id = n.nid " +
                    "LEFT JOIN " +
                    "field_data_body fb ON fb.entity_id = n.nid " +
                    "LEFT JOIN " +
                    "field_data_field_search_weight FSW ON FSW.entity_id = n.nid " +
                    "LEFT JOIN " +
                    "field_data_field_topic FDT ON FDT.entity_id = n.nid " +
                    "WHERE " +
                    "n.type = 'page' " +
                    "GROUP BY " +
                    "n.nid;";


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
                    if (cellValue != null&&metaData.getColumnName(i).equalsIgnoreCase("field_topic_tid")) {
                        cellValue = switch (cellValue) {
                            case "94" -> "About WCIRB";
                            case "10" -> "Classification";
                            case "75" -> "Coverage Information";
                            case "21" -> "Data Quality";
                            case "12" -> "Data Reporting";
                            case "26" -> "DR: Policy Examination";
                            case "25" -> "DR: Unit Statistical";
                            case "87" -> "DR:Aggregate Financial Data";
                            case "11" -> "Experience Rating";
                            case "110" -> "Filing";
                            case "17" -> "Insurer Experience";
                            case "18" -> "Loss Development";
                            case "19" -> "Losses and Expenses";
                            case "152" -> "Medical Transactional Data";
                            case "14" -> "Ownership";
                            case "111" -> "Policyholder Information";
                            case "16" -> "Regulations";
                            case "13" -> "Test Audit";
                            case "20" -> "WCIRB Connect";
                            default -> "null";
                        };
                }
                    if (metaData.getColumnName(i).equalsIgnoreCase("uid")) {
                        cellValue = switch (cellValue) {
                            case "1" -> "wwolfrath";
                            case "55" -> "josh";
                            case "100" -> "jhannan";
                            case "101" -> "twidmer";
                            case "102" -> "rayuyang";
                            case "7561" -> "jmoline";
                            case "10321" -> "apursell";
                            case "11721" -> "mtrinidad";
                            default -> cellValue;
                        };
                    }
                    if (metaData.getColumnName(i).equalsIgnoreCase("body_value") ||
                            metaData.getColumnName(i).equalsIgnoreCase("body_summary")) {
                        cellValue = extractPlainText(cellValue);
                    }
                    if (cellValue != null && cellValue.length() > 32767) {
                        cellValue = cellValue.substring(0, Math.min(cellValue.length(), 32767));
                    }
                    row.createCell(i - 1).setCellValue(cellValue);
                }
            }

            for (int i = 0; i < columnCount; i++) {
                sheet.autoSizeColumn(i);
            }

            String fileName = "WCIRB_Result_" + System.currentTimeMillis() + ".xlsx";
            FileOutputStream fileOut = new FileOutputStream(fileName);
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

    private String convertTimestamp(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.of("America/Los_Angeles"));
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private String extractPlainText(String htmlContent) {
        if (htmlContent != null) {
            Document doc = Jsoup.parse(htmlContent);
            return doc.text();
        }
        return null;
    }
}
