package src.test.tests;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.apache.poi.ss.util.CellUtil.createCell;

public class URLCrawler {

    private static int scannedLinks = 0;

    public static void main(String[] args) {
        String startingUrl = "https://www.rcw-358-pfyln2a-guf2qdgx2ziro.us-4.platformsh.site/en/content"; // Replace with your starting URL

String cookie="_cc_id=e3dc8d86166aaaba5a05d8ffb9eb8db9; panoramaId_expiry=1714993436784; panoramaId=d0e8c56501c85382dd8abce0061b185ca02c14b733f10e571a17a384f71e5e06; panoramaIdType=panoDevice; language_cookie=en; SSESS35e7236b95326bae95b88618edb7dc03=61tQklY0w3dFBnw00-1QV-SlNGjO0705448-1n5MglSCSF80";
        Set<String> processedURLs = new HashSet<>();
        Workbook workbook = new XSSFWorkbook();
        Sheet resultsSheet = workbook.createSheet("unhcr result1");

        crawlAndVerifyURLs(startingUrl, cookie, resultsSheet);

        try (FileOutputStream fileOut = new FileOutputStream("C:\\Users\\Vardot QA\\Downloads\\test 2.xlsx")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void crawlAndVerifyURLs(String startingUrl, String cookie, Sheet resultsSheet) {
        Queue<String> queue = new LinkedList<>();
        Set<String> processedURLs = new HashSet<>();

        queue.add(startingUrl);

        while (!queue.isEmpty()) {
            String currentUrl = queue.poll();

            try {
                Document document = Jsoup.connect(currentUrl).header("Cookie", cookie).get();
                Elements links = document.select("a[href]");

                int remainingLinks = links.size() - scannedLinks;
                System.out.println("Scanned links: " + scannedLinks + ", Remaining links: " + Math.max(0, remainingLinks));

                for (Element link : links) {
                    String url = link.absUrl("href");

                    if (processedURLs.contains(url) || !isSameDomain(startingUrl, url) || url.matches(".*[a-zA-Z]+\\?.*")) {
                        System.out.println("URL: " + url + " already processed, different domain, or contains excludePattern. Skipping...");
                        continue;
                    }

                    scannedLinks++;

                    String[] apiResponse = apiResponse(url, cookie);
                    String responseCode = apiResponse[0];
                    String responseText = apiResponse[1];

                    String result = getResult(url, responseCode, responseText);

                    processedURLs.add(url);

                    // Write to Excel
                    Row row = resultsSheet.createRow(resultsSheet.getLastRowNum() + 1);
                    createCell(row, 0, url);
                    createCell(row, 1, result);

                    // Add the new URL to the queue
                    queue.add(url);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isSameDomain(String startingUrl, String url) {
        try {
            URI startingUri = new URI(startingUrl);
            URI uri = new URI(url);
            return Objects.equals(startingUri.getHost(), uri.getHost());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void createCell(Row row, int index, String value) {
        Cell cell = row.createCell(index);
        int maxLength = 2000;
        if (value.length() > maxLength) {
            value = value.substring(0, maxLength);
        }
        cell.setCellValue(value);
    }

    public static String[] apiResponse(String urlString, String Cookie) {
        StringBuilder responseText = new StringBuilder();
        String[] apiResponse = new String[2];
        String base64Credentials = Base64.getEncoder().encodeToString(("unicc" + ":" + "5NJjoVm-RV8u9Qun4hnt").getBytes());

        if (urlString.contains("/logout")) {
            apiResponse[0] = "200";
            apiResponse[1] = "Skipped";
        } else {
            try {

                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Cookie", Cookie);
                connection.setRequestProperty("Authorization", "Basic " + base64Credentials);

                connection.setRequestMethod("GET");

                try (InputStream inputStream = connection.getInputStream()) {
                    // Check if the content type is an image or PDF
                    String contentType = connection.getContentType();
                    if (contentType != null && (contentType.startsWith("image/") || contentType.endsWith("/pdf"))) {
                        apiResponse[0] = String.valueOf(connection.getResponseCode());
                        apiResponse[1] = "Skipped - Image/PDF";
                        return apiResponse;
                    }

                    // Read the response text for non-image/PDF content
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            responseText.append(line);
                        }
                    }
                }

                apiResponse[0] = String.valueOf(connection.getResponseCode());
                apiResponse[1] = responseText.toString();
            } catch (IOException e) {
                // Handle specific exceptions for image/PDF content
                if (e instanceof FileNotFoundException || e instanceof UnknownServiceException) {
                    apiResponse[0] = "200";
                    apiResponse[1] = "Skipped - Image/PDF";
                } else {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    String stackTrace = sw.toString();
                    apiResponse[0] = stackTrace;
                }
            }
        }

        return apiResponse;
    }


    private static String getResult(String url, String responseCode, String responseText) {
        if (!Objects.equals(responseCode, "200")) {
            System.out.println("URL: " + url + ", Verification Failed. Response Code: " + responseCode);
            return "Verification Failed. Response Code:" + responseCode;
        } else if (responseText.contains("Fatal error")) {
            System.out.println("URL: " + url + ", Verification Failed. Response Text: Fatal error");
            return "Verification Failed. Response Text: Fatal error";
        } else if (responseText.contains("Page not found")) {
            System.out.println("URL: " + url + ", Verification Failed. Response Text: Page not found");
            return " Verification Failed. Response Text: Page not found";
        } else if (responseText.contains("error message")) {
            System.out.println("URL: " + url + ", Verification Failed. Response Text: Page not found");
            return " Verification Failed. Response Text: error message";
        } else if (responseText.contains("The website encountered an unexpected error")) {
            System.out.println("URL: " + url + ", Verification Failed. Response Text: The website encountered an unexpected error");
            return "failed";
        } else {
            System.out.println("URL: " + url + ", Verification Passed");
            return "Passed";
        }
    }
}