package src.test.tests;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashSet;
import java.util.List;


public class CollectURLsFromDrupal {

    public static void main(String[] args) {

        //change the below values based in your website
        String siteDomain = "https://www.coventrywcs.com";
        String url = "https://www.coventrywcs.com/admin/content?title=&body_value=&type=author&status=All&langcode=All&page=";
        String cookies = "_hjSessionUser_3508216=eyJpZCI6IjU0NjQ2OTE1LTdiMWQtNWIwMC1iMDRmLWU4MjcwODY3YWE5ZSIsImNyZWF0ZWQiOjE3MTM0NDY1MjMwNjQsImV4aXN0aW5nIjp0cnVlfQ==; _gcl_au=1.1.322664639.1713446523; _hjSessionUser_3508218=eyJpZCI6IjNmYTM2YWJmLWE1YTktNTYwZC04NWQ0LTcwYTQ1YjgzNDUwOSIsImNyZWF0ZWQiOjE3MTM0NDY1MjM5NzUsImV4aXN0aW5nIjp0cnVlfQ==; __adroll_fpc=522bf89dcaf9b74dd5289da830ab0ba5-1713446524255; visitor_id922373=1491748737; visitor_id922373-hash=bfce278a6cfd3a63af4d6ac01379afffab551b1b313b2d0c05c3278df266a675366ff12b4f07d2b0497ae2a027bed41d6ef5ebc8; fpestid=7CjhEsB15mw6_9T2jUvdzOvXfILE-0A291ujlhDAJxfvaRuW3i6VuZIqRj8Rk-aaaV8bKg; _cc_id=47196df3854527e12c98a1da59dfe555; panoramaId_expiry=1714051331603; panoramaId=b27da811d2e71f5a35da2dd6517116d5393867c9a5f11848ad84d48e27b60e3b; panoramaIdType=panoIndiv; SSESSe398ecc9b5b9c48dbab05216cd7ac9bc=MnXoX-FN3QGk4Svc5SbWpBBkkRkzwJRdajyIw32exkctPBSh; OptanonConsent=isGpcEnabled=0&datestamp=Wed+Apr+24+2024+09%3A37%3A19+GMT%2B0300+(GMT%2B03%3A00)&version=202308.2.0&browserGpcFlag=0&isIABGlobal=false&hosts=&consentId=94825c48-df78-43f6-89d5-122d91318aa5&interactionCount=1&landingPath=NotLandingPage&groups=C0001%3A1%2CSSPD_BG%3A1%2CTRGT4%3A1%2CPERF2%3A1&AwaitingReconsent=false; _hjSession_3508216=eyJpZCI6IjQ1N2M4NjYzLWJlYzYtNDA2My05MjgwLWIwZDIyZjc4NzcxYyIsImMiOjE3MTM5NDA2Mzk2NDUsInMiOjEsInIiOjEsInNiIjowLCJzciI6MCwic2UiOjAsImZzIjowLCJzcCI6MH0=; _ga_Q62NYMQGXR=GS1.1.1713940640.3.0.1713940640.0.0.0; _gid=GA1.2.2087032461.1713940640; _ga=GA1.1.2052530563.1713446523; _ga_0S3M7QC1ST=GS1.2.1713940640.3.0.1713940640.60.0.0; __ar_v4=PRVGBFII2NAHXPTZH5AVPP%3A20240418%3A5%7CXW6WBPI7BNDHTFBHQM3AXP%3A20240418%3A5%7CE7NKPPQ75VGEZEAO26T3C2%3A20240418%3A5; _ga_2L32WWDNTK=GS1.1.1713940640.3.0.1713940643.0.0.0";
        String excelFilePathFinalResult = "C:\\Users\\Delana\\Downloads\\Edit_URLs_Content_Page.xlsx";
        //change this number based on the number of the content pages in your website
        int numberOfContentPages = 2;

        List<String> contentPageLinks = new ArrayList<>();
        for (int i = 0; i < numberOfContentPages; i++)
            contentPageLinks.addAll(extractAllEditLinksFromDrupal(url+i,cookies,"",""));

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("ExtractedContentPageEditLinks");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("URLs");

            for (int i = 0; i < contentPageLinks.size(); i++) {
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(siteDomain + contentPageLinks.get(i));
            }

            try (FileOutputStream outputStream = new FileOutputStream(excelFilePathFinalResult)) {
                workbook.write(outputStream);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    private static LinkedHashSet<String> extractAllEditLinksFromDrupal(String url, String cookies, String username, String password) {

        Document document = Jsoup.parse("");
        try {
            System.out.println("URL= "+url);
            if (username != "" && password != "") {
                String base64Credentials = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
                 document = Jsoup.connect(url)
                        .header("Authorization", "Basic " + base64Credentials)
                        .header("Cookie", cookies)
                        .get();
            } else {
                Connection connection = Jsoup.connect(url);
                connection.cookie("cookie", cookies);
                document = connection.get();
            }

            Elements rows = document.select("tr");
            LinkedHashSet<String> collectLinks = new LinkedHashSet<>();

            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements editLinks= row.select("li.edit.dropbutton__item a");

                if (editLinks != null || !editLinks.isEmpty())
                    collectLinks.add(String.valueOf(editLinks.attr("href").substring(0, editLinks.attr("href").indexOf("?destination"))));
                else
                    collectLinks.add("");
            }

            if (collectLinks != null) {
                return collectLinks;
            } else {
                System.out.println("NO Drupal data found");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}