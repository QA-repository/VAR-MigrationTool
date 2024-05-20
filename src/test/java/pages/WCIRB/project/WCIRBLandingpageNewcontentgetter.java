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

public class WCIRBLandingpageNewcontentgetter {
    public static void main(String[] args) {
        String baseUrl = "https://www.dev-54ta5gq-zy4q7lto4eli2.us.platformsh.site/node/";
        int startNode = 1898;
        int endNode = 2086;

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(11).setCellValue("Title");
            headerRow.createCell(12).setCellValue("Body");
            headerRow.createCell(13).setCellValue("Layout");
            headerRow.createCell(14).setCellValue("Topic");
            headerRow.createCell(14).setCellValue("publicationDateTime");
            headerRow.createCell(14).setCellValue("authorName");
            headerRow.createCell(14).setCellValue("authoredOn");
            headerRow.createCell(14).setCellValue("moderationState");
            headerRow.createCell(14).setCellValue("pTagContent");



            int rowNum = 1;

            for (int i = startNode; i <=endNode; i++) {
                String url = baseUrl + i + "/edit";

                Connection connection = Jsoup.connect(url);
connection.cookie("cookie","_hjSessionUser_1198465=eyJpZCI6ImQxODdkYjk1LWY3MTctNTUyZC1hODNhLTgwOTVmNzBjMmM0MiIsImNyZWF0ZWQiOjE3MTAyMzg2MDY5NTMsImV4aXN0aW5nIjp0cnVlfQ==; SPL3e4814da78d3af4b89f11e5e03e90075=Oa3RpLA7OvzvNkv2BkxnkrPCqea0R1qPv7BCQx8JEMw%3AQii9MFP_AA5DXwIoYN8E9nHHgMpB3RAO4ItTB5kr_tA; SSESS3e4814da78d3af4b89f11e5e03e90075=ATZB8ifrIyPeuy16f8fxm48aQOomWwfBpTueNfwTAhPCbG1x; _hjSession_1198465=eyJpZCI6IjYxMjk3MDJiLTk4MmQtNDVlMi1hNWI4LTBjMDM1MDMwZDlmMyIsImMiOjE3MTEzMTM0MzY5MzQsInMiOjEsInIiOjEsInNiIjowLCJzciI6MCwic2UiOjAsImZzIjowLCJzcCI6MX0=");
connection.timeout(50000*2);
                Document doc = connection.get();
                String title = doc.select("input[data-drupal-selector=edit-title-0-value]").attr("value");


                String topic = doc.select("select[data-drupal-selector=edit-field-topics] option[selected]").text();


                String moderationState = doc.select("select[data-drupal-selector=edit-moderation-state-0-state] option[selected]").text();


                Element textareaElement = doc.selectFirst("textarea.js-text-full");

                String encodedHtml = textareaElement.attr("data-editor-value-original");

                Document htmlDoc = Jsoup.parse(encodedHtml);

                String body = htmlDoc.text();

System.out.println(body);



                Element textarea = doc.select("textarea").first();

                String content = textarea.text();

                Document contentDoc = Jsoup.parse(content);

                String pTagContent = contentDoc.select("p").text();









                Element selectElement = doc.getElementById("edit-layout-selection");
                Element selectedOption = selectElement.selectFirst("option[selected]");
                String layout;
                if (selectedOption != null) {
                    layout = selectedOption.text();
                } else {
                   layout = "null";
                }

                String publicationDateTime = doc.select("div#edit-meta-changed").text().trim();

                String authorName = doc.select("input#edit-uid-0-target-id").attr("value");

                String authoredOnDate = doc.select("input#edit-created-0-value-date").attr("value");
                String authoredOnTime = doc.select("input#edit-created-0-value-time").attr("value");
                String authoredOn = authoredOnDate + " - " + authoredOnTime;

                System.out.println("Start Checking " + url);
                System.out.println("Title: " + title);
                System.out.println("Body: " + body);
                System.out.println("Layout: " + layout);
                System.out.println("Topic: " + topic);
                System.out.println("Publication Date: " + publicationDateTime);
                System.out.println("Author Name: " + authorName);
                System.out.println("Authored On: " + authoredOn);
                System.out.println("Moderation State: " + moderationState);
                System.out.println("Summarytext: " + pTagContent);

                Row row = sheet.createRow(rowNum++);
                row.createCell(11).setCellValue(title);
                row.createCell(12).setCellValue(body.substring(0, Math.min(body.length(), 32767)));
                row.createCell(13).setCellValue(layout);
                row.createCell(14).setCellValue(topic);
                row.createCell(15).setCellValue(publicationDateTime);
                row.createCell(16).setCellValue(authorName);
                row.createCell(17).setCellValue(authoredOn);
                row.createCell(18).setCellValue(moderationState
                );
                row.createCell(19).setCellValue(pTagContent
                );


            }

            try (FileOutputStream outputStream = new FileOutputStream("LandingpageoutputWCIRB.xlsx")) {
                workbook.write(outputStream);
            }

            System.out.println("Data exported successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

