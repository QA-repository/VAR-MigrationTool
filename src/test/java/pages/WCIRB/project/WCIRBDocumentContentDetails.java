package pages.WCIRB.project;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;

public class WCIRBDocumentContentDetails {

    public static void main(String[] args) {
        String baseUrl = "https://www.dev-54ta5gq-zy4q7lto4eli2.us.platformsh.site/node/";
        int startNumber = 924;
        int range = 955;

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet extractedDataSheet = workbook.createSheet("Extracted Data");
            Row headerRow = extractedDataSheet.createRow(0);
            headerRow.createCell(0).setCellValue("Title");
            headerRow.createCell(1).setCellValue("Body");
            headerRow.createCell(2).setCellValue("Media");
            headerRow.createCell(3).setCellValue("Document Type");
            headerRow.createCell(4).setCellValue("Committee Document Type");
            headerRow.createCell(5).setCellValue("Meeting Date");
            headerRow.createCell(6).setCellValue("Publishing");
            headerRow.createCell(7).setCellValue("Topic");
            headerRow.createCell(8).setCellValue("Audience");
            headerRow.createCell(9).setCellValue("Site Keyword");
            headerRow.createCell(10).setCellValue("Authored By");
            headerRow.createCell(11).setCellValue("Authored On");

            int rowIndex = 1;

            for (int i = startNumber; i < startNumber + range; i++) {
                String apiUrl = baseUrl + i + "/edit";

                Connection connection = Jsoup.connect(apiUrl);
                connection.cookie("_hjSessionUser_1198465", "eyJpZCI6ImQxODdkYjk1LWY3MTctNTUyZC1hODNhLTgwOTVmNzBjMmM0MiIsImNyZWF0ZWQiOjE3MTAyMzg2MDY5NTMsImV4aXN0aW5nIjp0cnVlfQ==; SPL3e4814da78d3af4b89f11e5e03e90075=Oa3RpLA7OvzvNkv2BkxnkrPCqea0R1qPv7BCQx8JEMw%3AQii9MFP_AA5DXwIoYN8E9nHHgMpB3RAO4ItTB5kr_tA; SSESS3e4814da78d3af4b89f11e5e03e90075=ATZB8ifrIyPeuy16f8fxm48aQOomWwfBpTueNfwTAhPCbG1x");
                connection.timeout(50000 * 2);

                Document doc = connection.get();

                String title = parsetitle(doc, "Title");

                String body = parsebody(doc);
                String media = parsemedia(doc);
                String Documenttype = parseDocumenttype(doc);
                String CommitteeDocumentType = parseCommitteeDocumentType(doc);
                String meetingDate = parsemeetingdate(doc, "Date");
                String Publishing = parselastPublishing(doc);
                String Topic = parsCategory(doc);
                String audience = parsetaudience(doc);
                String SiteKeyword = keywordsInputElements(doc);
                String authoredBy = authoredby(doc, "authorName");

                String authoredOn = authoron(doc, "Authored On");
String moderationState=moderationState(doc,"");
                Row newRow = extractedDataSheet.createRow(rowIndex++);
                newRow.createCell(0).setCellValue(title);
                newRow.createCell(1).setCellValue(body);
                newRow.createCell(2).setCellValue(media);
                newRow.createCell(3).setCellValue(Documenttype);
                newRow.createCell(4).setCellValue(CommitteeDocumentType);
                newRow.createCell(5).setCellValue(meetingDate);
                newRow.createCell(6).setCellValue(Publishing);
                newRow.createCell(7).setCellValue(Topic);
                newRow.createCell(8).setCellValue(audience);
                newRow.createCell(9).setCellValue(SiteKeyword);
                newRow.createCell(10).setCellValue(authoredBy);
                newRow.createCell(11).setCellValue(authoredOn);
                newRow.createCell(12).setCellValue(moderationState);

                System.out.println("Title: " + title);

                System.out.println("Body: " + body);
                System.out.println("Media: " + media);
                System.out.println("Document Type: " + Documenttype);
                System.out.println("Committee Document Type: " + CommitteeDocumentType);
                System.out.println("Meeting Date: " + meetingDate);
                System.out.println("Publishing: " + Publishing);
                System.out.println("Topic: " + Topic);
                System.out.println("Audience: " + audience);
                System.out.println("Site Keyword: " + SiteKeyword);
                System.out.println("Authored By: " + authoredBy);
                System.out.println("Authored On: " + authoredOn);
                System.out.println("moderationState " + moderationState);


                System.out.println("Record for ID " + i + " extracted successfully.");
            }

            try (FileOutputStream outputStream = new FileOutputStream("aapis.xlsx")) {
                workbook.write(outputStream);
            }
            System.out.println("Data exported successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String authoron(Document doc, String fieldName) {
        String authoredOnDate = doc.select("input#edit-created-0-value-date").attr("value");
        String authoredOnTime = doc.select("input#edit-created-0-value-time").attr("value");
        String authoredOn = authoredOnDate + " - " + authoredOnTime;
        return  authoredOn;
    }
    private static String authoredby(Document doc, String fieldName) {

        return  doc.select("input#edit-uid-0-target-id").attr("value");
    }
    private static String moderationState(Document doc, String fieldName) {

        return doc.select("select[data-drupal-selector=edit-moderation-state-0-state] option[selected]").text();
    }
    private static String parsCategory(Document doc) {
        // Assuming you have already obtained a Document object named doc
        Elements topicsInputElements = doc.select("table#field-topics-values div.claro-autocomplete input.form-autocomplete");
        StringBuilder topics = new StringBuilder();
        for (Element inputElement : topicsInputElements) {
            topics.append(inputElement.attr("value")).append(", ");
        }

        return topics.length() > 0 ? topics.substring(0, topics.length() - 2) : "No audience found";

    }
    private static String parsetitle(Document doc, String fieldName) {
        Element inputElement = doc.selectFirst("input[data-drupal-selector=edit-title-0-value]");
        return inputElement.attr("value");
    }
    private static String parsemeetingdate(Document doc, String fieldName) {
        Element inputElement = doc.selectFirst("input[data-drupal-selector=edit-field-date-0-value-date]");
        return  inputElement.attr("value");

    }
    private static String parsebody(Document doc) {
        Element textareaElement = doc.selectFirst("textarea.js-text-full");
        if (textareaElement != null) {
            String encodedHtml = textareaElement.attr("data-editor-value-original");
            Document htmlDoc = Jsoup.parse(encodedHtml);
            String body = htmlDoc.text();
            return body.isEmpty() ? "No body found" : body;
        }
        return "No body found";
    }

    private static String parsemedia(Document doc) {
        Elements elements = doc.select("div.media-library-item__attributes > a");
        StringBuilder media = new StringBuilder();
        for (Element element : elements) {
            media.append(element.text()).append(", ");
        }
        return media.length() > 0 ? media.substring(0, media.length() - 2) : "No media found";
    }

    private static String parseDocumenttype(Document doc) {
        Element selectElement = doc.selectFirst("select#edit-field-document-type");
        return selectElement != null ? selectElement.select("option[selected]").text() : "Document type not found";
    }

    private static String parseCommitteeDocumentType(Document doc) {
        Element selectElement = doc.selectFirst("select#edit-field-committee-document-type");
        return selectElement != null ? selectElement.select("option[selected]").text() : "Committee Document type not found";
    }

    private static String parselastPublishing(Document doc) {
        Element element = doc.selectFirst("div#edit-meta-changed");
        return element != null ? element.text().trim() : "Publishing not found";
    }

    private static String parsetopics(Document doc) {
        Elements selectElements = doc.select("table#field-topics-values select");
        StringBuilder topics = new StringBuilder();
        for (Element selectElement : selectElements) {
            topics.append(selectElement.select("option[selected]").text()).append(", ");
        }
        return topics.length() > 0 ? topics.substring(0, topics.length() - 2) : "No topics found";
    }

    private static String parsetaudience(Document doc) {
        Elements audienceInputElements = doc.select("table#field-audience-values div.claro-autocomplete input.form-autocomplete");
        StringBuilder audience = new StringBuilder();
        for (Element inputElement : audienceInputElements) {
            audience.append(inputElement.attr("value")).append(", ");
        }

        return audience.length() > 0 ? audience.substring(0, audience.length() - 2) : "No audience found";
    }


    private static String keywordsInputElements(Document doc) {
        Elements keywordsInputElements = doc.select("table#field-keywords-values div.claro-autocomplete input.form-autocomplete");
        StringBuilder keywords = new StringBuilder();
        for (Element inputElement : keywordsInputElements) {
            keywords.append(inputElement.attr("value")).append(", ");
        }
        return keywords.length() > 0 ? keywords.substring(0, keywords.length() - 2) : "No keywords found";
    }
}
