package pages.WCIRB.project;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DynamicFieldExtractor {

    public static void main(String[] args) {
        try {
            Map<String, String> fieldTitles = getFieldTitles();

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Data");
            int rowIndex = 0;

            createHeaderRow(sheet, rowIndex, fieldTitles);

            for (int id = 2110; id <= 2818; id++) {
                String url = "https://www.dev-54ta5gq-zy4q7lto4eli2.us.platformsh.site/node/" + id + "/edit?destination=/admin/content%3Fstatus%3DAll%26type%3Dclassification_record%26title%3D%26created_from%255Bdate%255D%3D%26created_to%255Bdate%255D%3D%26uid%3D%26items_per_page%3D50";
                System.out.println("Strting with"+ url);
                Document doc = Jsoup.connect(url)
                        .cookie("Cookie", "_hjSessionUser_1198465=eyJpZCI6IjlmMjJhMmRlLTI1ODctNWM3YS1hZDA0LWIzN2EzMjAzN2UyZiIsImNyZWF0ZWQiOjE3MTIxMDU5NjI4MjcsImV4aXN0aW5nIjp0cnVlfQ==; _hjSession_1198465=eyJpZCI6IjRjNWIwMTM3LTllNjktNGVjOC1iMTk1LTg2ZjQ4MmRlMzQyYiIsImMiOjE3MTIyMzM1OTM5NjQsInMiOjAsInIiOjAsInNiIjowLCJzciI6MCwic2UiOjAsImZzIjowLCJzcCI6MX0=; SSESS3e4814da78d3af4b89f11e5e03e90075=otonO1oWU54GDD8n3OsrV9WQFj%2C7lI4f49zBsCYi9373w24F")
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

                Cell keywordsCell = valueRow.createCell(columnIndex++);
                keywordsCell.setCellValue(parseKeywords(doc));

                Cell authoredByCell = valueRow.createCell(columnIndex++);
                authoredByCell.setCellValue(authoredBy(doc));

                Cell authoredOnCell = valueRow.createCell(columnIndex++);
                authoredOnCell.setCellValue(authoredOn(doc));

                Cell moderationStateCell = valueRow.createCell(columnIndex);
                moderationStateCell.setCellValue(moderationState(doc));

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
        fieldTitles.put("field_date[0][value][date]", "Effective date");
        fieldTitles.put("field_elimination_date[0][value][date]", "Elimination date");
        fieldTitles.put("field_classification_code", "ClassCode");
        fieldTitles.put("field_code_suffix", "Suffix");
        fieldTitles.put("field_phraseology[0][value]", "Phraseology");
        fieldTitles.put("body[0][value]", "Footnote");
        fieldTitles.put("field_combine_code[0][value]", "Combine Code");
        fieldTitles.put("field_short_listing[0][value]", "Short listing");
        fieldTitles.put("field_industry[]", "field_industry");
        fieldTitles.put("field_topics[]", "field_topics");
        fieldTitles.put("edit-field-rules-wrapper", "edit-field-rules-wrapper");
        fieldTitles.put("field_revision_history[0][subform][field_revision_date][0][value][date]", "Revision date");
        fieldTitles.put("field_revision_history[0][subform][field_revision_description][0][value]", "Revision Description");
        return fieldTitles;
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

        Cell keywordsHeader = headerRow.createCell(columnIndex++);
        keywordsHeader.setCellValue("Keywords");

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

    private static String parseRoles(Document doc) {
        Elements rolesInputElements = doc.select("table#field-rules-values input.form-autocomplete");
        StringBuilder roles = new StringBuilder();

        for (Element inputElement : rolesInputElements) {
            roles.append(inputElement.attr("value")).append(", ");
        }

        return roles.length() > 0 ? roles.substring(0, roles.length() - 2) : "No roles found";
    }

    private static String parseKeywords(Document doc) {
        Elements keywordsInputElements = doc.select("table#field-keywords-values input.form-autocomplete");
        StringBuilder keywords = new StringBuilder();
        for (Element inputElement : keywordsInputElements) {
            keywords.append(inputElement.attr("value")).append(", ");
        }
        return keywords.length() > 0 ? keywords.substring(0, keywords.length() - 2) : "No keywords found";
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
        try (FileOutputStream fileOut = new FileOutputStream("output.xlsx")) {
            workbook.write(fileOut);
        }
    }
}
