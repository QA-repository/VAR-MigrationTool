package pages.WCIRB.project;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WCIRBDocumentcompare {

    private static String apiUrl = "//appi";

    private static String excelFilePath = "//";

    public static void main(String[] args) {
        try {
            String[] apiUrls = readApiUrlsFromExcel();
            for (String apiUrl : apiUrls) {
                String html = getHtmlFromApi(apiUrl);

                String title = parseTitle(html);
                String bodyText = parseBodyText(html);
                String attachmentName = parseAttachmentName(html);
                String topic = parseTopic(html);
                String audience = parseAudience(html);
                Map<String, String> keywords = parseKeywords(html);
                String authorDate = parseAuthorDate(html);
                String moderationState = parseModerationState(html);


                System.out.println("Title: " + title);
                System.out.println("Body Text: " + bodyText);
                System.out.println("Attachment Name: " + attachmentName);
                System.out.println("Topic: " + topic);
                System.out.println("Audience: " + audience);
                System.out.println("Keywords: " + keywords);
                System.out.println("Author Date: " + authorDate);
                System.out.println("Moderation State: " + moderationState);
            //    System.out.println("Comparison Result with Column A: " + comparisonResultA);
              //  System.out.println("Comparison Result with Column B: " + comparisonResultB);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String readExcelCellValue(String filePath, String sheetName, int rowNum, String columnName) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Row row = sheet.getRow(rowNum - 1);
            int columnIndex = getColumnIndex(sheet, columnName);
            Cell cell = row.getCell(columnIndex);

            return (cell != null) ? cell.toString() : "";
        }
    }
    private static int getColumnIndex(Sheet sheet, String columnName) {
        Row headerRow = sheet.getRow(0);
        int lastCellNum = headerRow.getLastCellNum();

        for (int i = 0; i < lastCellNum; i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null && columnName.equals(cell.toString())) {
                return i;
            }
        }
        return -1;
    }

    private static String[] readApiUrlsFromExcel() throws IOException {
        FileInputStream excelFile = new FileInputStream(excelFilePath);
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet sheet = workbook.getSheetAt(0);

        int numRows = sheet.getPhysicalNumberOfRows();
        String[] apiUrls = new String[numRows];
        Iterator<Row> iterator = sheet.iterator();

        int i = 0;
        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            Cell cell = currentRow.getCell(16);
            apiUrls[i] = cell.getStringCellValue();
            i++;
        }

        workbook.close();
        return apiUrls;
    }

    private static String getHtmlFromApi(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            return content.toString();
        } else {
            throw new IOException("Failed to fetch HTML from API. Response Code: " + responseCode);
        }
    }


    private static String parseTitle(String html) {
        Document doc = Jsoup.parse(html);
        Element titleElement = doc.select("title").first();
        return (titleElement != null) ? titleElement.text() : "";
    }

    private static String parseBodyText(String html) {
        Document doc = Jsoup.parse(html);
        Element bodyElement = doc.select("body").first();
        return (bodyElement != null) ? bodyElement.text() : "";
    }

    private static String parseAttachmentName(String html) {
        Document doc = Jsoup.parse(html);
        Element attachmentElement = doc.select("input[data-drupal-selector^=edit-field-attachments-]").first();
        return (attachmentElement != null) ? attachmentElement.val() : "";
    }

    private static String parseTopic(String html) {
        Document doc = Jsoup.parse(html);
        Elements rows = doc.select("tbody tr");
        for (Element row : rows) {
            Element autocompleteCell = row.select("td:eq(1)").first();
            String topicValue = autocompleteCell.select("input").attr("value");
            return topicValue.split("\\(")[0].trim();
        }
        return "";
    }

    private static String parseAudience(String html) {
        Document doc = Jsoup.parse(html);
        Element audienceElement = doc.select("input[data-drupal-selector^=edit-field-audience-]").first();
        return (audienceElement != null) ? audienceElement.val() : "";
    }

    private static Map<String, String> parseKeywords(String html) {
        Document doc = Jsoup.parse(html);
        Elements keywordElements = doc.select("input[data-drupal-selector^=edit-field-keywords-]");
        Map<String, String> keywords = new HashMap<>();
        int i = 1;
        for (Element keywordElement : keywordElements) {
            keywords.put("Keyword" + i, keywordElement.val());
            i++;
        }
        return keywords;
    }

    private static String parseAuthorDate(String html) {
        Document doc = Jsoup.parse(html);
        Element dateElement = doc.select("input[data-drupal-selector^=edit-created-0-value-date]").first();
        Element timeElement = doc.select("input[data-drupal-selector^=edit-created-0-value-time]").first();

        String date = (dateElement != null) ? dateElement.val() : "";
        String time = (timeElement != null) ? timeElement.val() : "";

        return date + " " + time;
    }

    private static String parseModerationState(String html) {
        Document doc = Jsoup.parse(html);
        Element stateElement = doc.select("select[data-drupal-selector^=edit-moderation-state-] option[selected]").first();
        return (stateElement != null) ? stateElement.text() : "";
    }

    private static String compareWithAll(String value1, String column1, String value2, String column2) {
        if (value1.equals(value2)) {
            System.out.println(column1 + " is equal to " + column2);
            return column1 + " is equal to " + column2;
        } else {
            System.out.println(column1 + " is not equal to " + column2);

            return column1 + " is NOT equal to " + column2;
        }
    }


}
