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
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/wcirblive";
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
            useStatement.execute("USE wcirblive;");
            useStatement.close();

            // Prepare and execute main query
            String sqlQuery = "SELECT n.*, " +
                    "fdb.body_value, " +
                    "field_data_field_tags.field_tags_tid, " +
                    "img.field_image_fid, " +
                    "field_data_field_e_newsletter_type.field_e_newsletter_type_tid, " +
                    "ia.field_image_alt, " +
                    "fdb.body_summary, " +
                    "field_data_field_search_weight.field_search_weight_value, " +
                    "GROUP_CONCAT(DISTINCT fdt.field_topic_tid SEPARATOR ',') AS topic_ids " +
                    "FROM node AS n " +
                    "LEFT JOIN field_data_body AS fdb ON fdb.entity_id = n.nid " +
                    "LEFT JOIN field_data_field_image AS img ON img.entity_id = n.nid " +
                    "LEFT JOIN field_data_field_image AS ia ON ia.entity_id = n.nid " +
                    "LEFT JOIN field_data_field_topic AS fdt ON fdt.entity_id = n.nid " +
                    "LEFT JOIN field_data_field_search_weight AS field_data_field_search_weight ON field_data_field_search_weight.entity_id = n.nid " +
                    "LEFT JOIN field_data_field_e_newsletter_type AS field_data_field_e_newsletter_type ON field_data_field_e_newsletter_type.entity_id = n.nid " +
                    "LEFT JOIN field_data_field_tags AS field_data_field_tags ON field_data_field_tags.entity_id = n.nid " +
                    "WHERE n.type = 'article' " +
                    "GROUP BY n.nid, fdb.body_value, img.field_image_fid, ia.field_image_alt, fdb.body_summary;";

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
                    if (cellValue != null && metaData.getColumnName(i).equalsIgnoreCase("topic_ids")) {
                        if (cellValue.contains(",")) {
                            String[] parts = cellValue.split(",");
                            StringBuilder result = new StringBuilder();
                            for (String part : parts) {
                                String partResult = switch (part.trim()) {
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
                                result.append(partResult).append(",");
                            }
                            result.deleteCharAt(result.length() - 1); // Remove trailing comma
                            cellValue = result.toString();
                        } else {
                            cellValue = switch (cellValue.trim()) {
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
                    }

                    if (metaData.getColumnName(i).equalsIgnoreCase("uid")) {
                        cellValue = switch (cellValue) {
                            case "1" -> "bill@hugemedia.com";
                            case "55" -> "josh@hugemedia.com";
                            case "100" -> "jhannan@wcirb.com";
                            case "101" -> "twidmer@wcirb.com";
                            case "102" -> "rayuyang@wcirb.com";
                            case "7561" -> "jmoline@wcirb.com";
                            case "10321" -> "apursell@wcirb.com";
                            case "11721" -> "mtrinidad@wcirb.com";
                            default -> cellValue;
                        };
                    }
                    if (metaData.getColumnName(i).equalsIgnoreCase("body_value") ||
                            metaData.getColumnName(i).equalsIgnoreCase("body_summary")) {
                        cellValue = extractPlainText(cellValue);
                    }
                    if (metaData.getColumnName(i).equalsIgnoreCase("field_abstract_value") ||
                            metaData.getColumnName(i).equalsIgnoreCase("field_abstract_value")) {
                        cellValue = extractPlainText(cellValue);
                    }
                    if (cellValue != null && cellValue.length() > 32767) {
                        cellValue = cellValue.substring(0, Math.min(cellValue.length(), 32767));
                    }
                    if (metaData.getColumnName(i).equalsIgnoreCase("field_phraseology_value") ||
                            metaData.getColumnName(i).equalsIgnoreCase("field_phraseology_value")) {
                        cellValue = extractPlainText(cellValue);
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
