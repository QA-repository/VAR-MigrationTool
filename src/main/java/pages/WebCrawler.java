package pages;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.*;

public class WebCrawler {
    static String cookie = "_ga=GA1.1.1691316110.1712130714; _ga_MYSBMM8KRP=GS1.1.1713366039.11.0.1713366039.0.0.0; Drupal.visitor.autologout_login=1713393926; SSESSf63998ca6d4f168e8d3417b9b6ef88d1=2JNiyOBqRmY47Y4oxZy25%2C1rcmxKue61HkB-hlSPiywgGWY-";
    private static final String EXCEL_FILE_PREFIX = "internal_links_thread_";
    private static final String EXCEL_FILE_SUFFIX = ".xlsx";
    private static final int MAX_LINKS_TO_CRAWL = 700000000;
    private static final int NUM_THREADS = 6;

    public static void main(String[] args) {
        String url = "https://dev.educationcluster.net/admin/content";
        Set<String> visitedLinks = new HashSet<>();
        Set<String> skippedLinks = new HashSet<>();

        // Check for flush or logout in the URL before making API calls
        if (url.toLowerCase().contains("flush") || url.toLowerCase().contains("logout")) {
            System.out.println("Skipping URL: " + url + " because it contains flush or logout.");
            skippedLinks.add(url);
        } else {
            crawlAndStoreLinks(url, visitedLinks, skippedLinks);
        }

        // Write skipped links to Excel
        writeToExcel(skippedLinks, "skipped_links.xlsx");
    }

    private static void crawlAndStoreLinks(String url, Set<String> visitedLinks, Set<String> skippedLinks) {
        Queue<String> linksToVisit = new LinkedList<>();
        linksToVisit.add(url);
        int scannedLinksCount = 0;

        while (!linksToVisit.isEmpty() && visitedLinks.size() < MAX_LINKS_TO_CRAWL) {
            String currentUrl;
            synchronized (linksToVisit) {
                currentUrl = linksToVisit.poll();
            }
            if (!visitedLinks.contains(currentUrl)) {
                try {
                    // Check for flush or logout in the URL before making API calls
                    if (currentUrl.toLowerCase().contains("flush") || currentUrl.toLowerCase().contains("logout")) {
                        System.out.println("Skipping URL: " + currentUrl + " because it contains flush or logout.");
                        skippedLinks.add(currentUrl);
                        continue; // Skip further processing
                    }

                    Document document = crawlWebsite(currentUrl, "unicc", "5NJjoVm-RV8u9Qun4hnt");
                    if (document != null) {
                        int statusCode = document != null ? 200 : 404; // assuming 404 if document is null
                        System.out.println("Scanned link #" + (++scannedLinksCount) + ": " + currentUrl + ", Status Code: " + statusCode);
                        extractAndStoreLinks(document, currentUrl, visitedLinks, linksToVisit);
                        visitedLinks.add(currentUrl);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Write skipped links to Excel
        writeToExcel(skippedLinks, "skipped_links.xlsx");
    }

    private static void extractAndStoreLinks(Document document, String baseUrl, Set<String> visitedLinks,
                                             Queue<String> linksToVisit) {
        URI baseUri = URI.create(baseUrl);
        Elements links = document.select("a[href]");

        for (Element link : links) {
            String absoluteUrl = link.absUrl("href");
            URI absoluteUri = URI.create(absoluteUrl);

            if (baseUri.getHost() != null && baseUri.getHost().equalsIgnoreCase(absoluteUri.getHost())) {
                if (!visitedLinks.contains(absoluteUrl) && !linksToVisit.contains(absoluteUrl)) {
                    linksToVisit.add(absoluteUrl);
                }
            } else {
                // External link, store it without further crawling
                visitedLinks.add(absoluteUrl);
            }
        }
    }

    private static Document crawlWebsite(String url, String username, String password) throws IOException {
        String base64Credentials = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        return Jsoup.connect(url)
                .header("Authorization", "Basic " + base64Credentials)
                .cookie("cookie", cookie) // Replace with the actual cookie value
                .userAgent("Mozilla/5.0")
                .get();
    }

    private static void writeToExcel(Set<String> links, String excelFilePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("SkippedLinks");

            int rowNumber = 0;
            for (String link : links) {
                Row row = sheet.createRow(rowNumber++);
                Cell cell = row.createCell(0);
                cell.setCellValue(link);
            }

            try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
                workbook.write(fileOut);
                System.out.println("Skipped links successfully stored in Excel file: " + excelFilePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
