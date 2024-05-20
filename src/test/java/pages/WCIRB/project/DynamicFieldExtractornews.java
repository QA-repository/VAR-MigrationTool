package pages.WCIRB.project;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DynamicFieldExtractornews {

    public static void main(String[] args) {
        try {
            Map<String, String> fieldTitles = getFieldTitles();

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Data");
            int rowIndex = 0;

            createHeaderRow(sheet, rowIndex, fieldTitles);

            for (int id = 3020; id <= 3020; id++) {
                String url = "https://www.dev-54ta5gq-zy4q7lto4eli2.us.platformsh.site/node/" + id + "/edit?destination=/admin/content%3Fstatus%3DAll%26type%3Dclassification_record%26title%3D%26created_from%255Bdate%255D%3D%26created_to%255Bdate%255D%3D%26uid%3D%26items_per_page%3D50";
                System.out.println("Starting  with "+""+ url);
                Document doc = Jsoup.connect(url)
                        .cookie("cookie","_hjSessionUser_1198465=eyJpZCI6IjlmMjJhMmRlLTI1ODctNWM3YS1hZDA0LWIzN2EzMjAzN2UyZiIsImNyZWF0ZWQiOjE3MTIxMDU5NjI4MjcsImV4aXN0aW5nIjp0cnVlfQ==; SSESS3e4814da78d3af4b89f11e5e03e90075=%2CueR1nCVw7oxZy5mYW0B4XB2s2ArRYkV9Ls9WbVv%2Clc11O%2Cy")
                        .timeout(90000)
                        .get();

                Row valueRow = sheet.createRow(++rowIndex);
                int columnIndex = 0;
                for (String field : fieldTitles.keySet()) {
                    String value = extractValueFromField(doc, field);
                    Cell valueCell = valueRow.createCell(columnIndex++);
                    valueCell.setCellValue(value);
                }

                Cell authorRolesCell = valueRow.createCell(columnIndex++);
                authorRolesCell.setCellValue(parseRoles(doc));




                Cell authoredByCell = valueRow.createCell(columnIndex++);
                authoredByCell.setCellValue(authoredBy(doc));

                Cell authoredOnCell = valueRow.createCell(columnIndex++);
                authoredOnCell.setCellValue(authoredOn(doc));

                Cell moderationStateCell = valueRow.createCell(columnIndex++);
                moderationStateCell.setCellValue(moderationState(doc));
                Cell Lastsaved = valueRow.createCell(columnIndex++);
                Lastsaved.setCellValue(parsemedia(doc));
                Cell parselastPublishing = valueRow.createCell(columnIndex++);
                parselastPublishing.setCellValue(parselastPublishing(doc));



                adjustColumnWidth(sheet, columnIndex);
            }

            writeWorkbookToFile(workbook);

            System.out.println("Excel report generated successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static Map<String, String> getFieldTitles() {
        Map<String, String> fieldTitles = new HashMap<>();
        fieldTitles.put("title[0][value]", "Title");
        fieldTitles.put("body[0][value]", "body");
        fieldTitles.put("field_topics[]", "field_topics");




        return fieldTitles;
    }
    private static String parselastPublishing(Document doc) {
        Element element = doc.selectFirst("div#edit-meta-changed");
        return element != null ? element.text().trim() : "Publishing not found";
    }
    private static void createHeaderRow(Sheet sheet, int rowIndex, Map<String, String> fieldTitles) {
        Row headerRow = sheet.createRow(rowIndex);
        int columnIndex = 0;
        for (String title : fieldTitles.values()) {
            Cell headerCell = headerRow.createCell(columnIndex++);
            headerCell.setCellValue(title);
        }
        // Additional headers for extra data
        Cell authorRolesHeader = headerRow.createCell(columnIndex++);
        authorRolesHeader.setCellValue("Author Roles");


        Cell authoredByHeader = headerRow.createCell(columnIndex++);
        authoredByHeader.setCellValue("Authored By");

        Cell authoredOnHeader = headerRow.createCell(columnIndex++);
        authoredOnHeader.setCellValue("Authored On");

        Cell moderationStateHeader = headerRow.createCell(columnIndex);
        moderationStateHeader.setCellValue("Moderation State");
    }

    private static String extractValueFromField(Document doc, String field) {
        Element element = doc.selectFirst("[name=" + field + "]");
        String value = "";

        if (element != null) {
            if (element.tagName().equals("input") && (element.attr("type").equals("text") || element.attr("type").equals("date"))) {
                value = element.val();
            } else if (element.tagName().equals("textarea")) {
                value = element.text();
            } else if (element.tagName().equals("select")) {
                Elements selectedOptions = element.select("option[selected]");
                StringBuilder selectedValues = new StringBuilder();
                for (Element option : selectedOptions) {
                    selectedValues.append(option.text()).append(", ");
                }
                value = selectedValues.toString().trim();
            }
        }
        // Remove HTML tags
        value = value.replaceAll("\\<.*?\\>", "");
        // Convert HTML entities to their respective characters
        value = StringEscapeUtils.unescapeHtml4(value);
        return value;
    }
    private static String extractValueFromFieldbyid(Document doc, String field) {
        Element element = doc.selectFirst("[id=" + field + "]");
        String value = "";

        if (element != null) {
            switch (element.tagName()) {
                case "input":
                    if (element.attr("type").equals("text") || element.attr("type").equals("date")) {
                        value = element.val();
                    }
                    break;
                case "textarea":
                    value = element.text();
                    break;
                case "select":
                    Elements selectedOptions = element.select("option[selected]");
                    StringBuilder selectedValues = new StringBuilder();
                    for (Element option : selectedOptions) {
                        selectedValues.append(option.text()).append(", ");
                    }
                    value = selectedValues.toString().trim();
                    break;
                default:
                    // Handle other cases if necessary
            }
        }

        // Remove HTML tags
        value = value.replaceAll("\\<.*?\\>", "");
        // Convert HTML entities to their respective characters
        value = StringEscapeUtils.unescapeHtml4(value);

        return value;
    }

    private static String parseRoles(Document doc) {
        Elements rolesInputElements = doc.select("table#field-rules-values input.form-autocomplete");
        StringBuilder roles = new StringBuilder();

        for (Element inputElement : rolesInputElements) {
            roles.append(inputElement.attr("value")).append(", ");
        }

        return roles.length() > 0 ? roles.substring(0, roles.length() - 2) : "No roles found";
    }



    private static String authoredBy(Document doc) {
        return doc.select("input#edit-uid-0-target-id").attr("value");
    }

    private static String authoredOn(Document doc) {
        String authoredOnDate = doc.select("input#edit-created-0-value-date").attr("value");
        String authoredOnTime = doc.select("input#edit-created-0-value-time").attr("value");
        return authoredOnDate + " - " + authoredOnTime;
    }

    private static String moderationState(Document doc) {
        return doc.select("select[data-drupal-selector=edit-moderation-state-0-state] option[selected]").text();
    }

    private static void adjustColumnWidth(Sheet sheet, int columnIndex) {
        for (int i = 0; i < columnIndex; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private static void writeWorkbookToFile(Workbook workbook) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream("outputE-news.xlsx")) {
            workbook.write(fileOut);
        }
    }
    private static String parsemedia(Document doc) {
        Elements elements = doc.select("div.media-library-item__attributes > a");
        StringBuilder media = new StringBuilder();
        for (Element element : elements) {
            media.append(element.text()).append(", ");
        }
        return media.length() > 0 ? media.substring(0, media.length() - 2) : "No media found";
    }
}
