package pages;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class URLCrawler {
    static long startTime;
    static long endTime;
    static String urlType = "internal";
    private static int scannedLinks = 0;
    private static int apiResponseCount = 0;

    public static void main(String[] args) {
        try {
            String startingUrl = "https://unhcr-staging.unhcr.info/admin/content";
String cookie="_fbp=fb.1.1697985671735.265970786; _rup_ga=GA1.1.1286796730.1698049924; _ga=GA1.3.1286796730.1698049924; geo_redirect_closed=1; __qca=P0-853754207-1701264865442; _ga_RDNCXLXWYH=GS1.1.1701860813.4.0.1701860822.51.0.0; __utmz=269327925.1704209347.16.2.utmcsr=acnur-staging.unhcr.info|utmccn=(referral)|utmcmd=referral|utmcct=/; _gcl_au=1.1.1176448907.1705827794; __adroll_fpc=0c01230e3972c59a1b221519986c9edd-1706166888750; __ar_v4=%7CCJG6Y263JBCNXM4UR2QNCB%3A20240124%3A1%7CZKLE4AYT4VCH3ATNBGG47S%3A20240124%3A1%7CBOOEUQWCUJAS5HFQANRCES%3A20240124%3A1; _uetvid=b9d795e0419a11ee974f859fad821d29; _ga_KD4J9DLPE3=GS1.1.1706426408.103.1.1706426413.0.0.0; _ga_FXYR2Y8W7G=GS1.1.1706426413.2.0.1706426413.0.0.0; _ga_F9VC73DPR5=GS1.1.1706599992.3.0.1706599992.0.0.0; _gid=GA1.3.1070223470.1706533334; _ga=GA1.1.1286796730.1698049924; cookie_consent=true; __utmc=269327925; SSESS771e07aadf9c319dced03c719df72ae8=gfX0oyPP3F7h2eaqVLoBt54HVs6Q4ZUSoaM0gHBd6qz90UvR; SSESS283c1aed81f4a37da21d0fca6b2b6661=3Ev0uZ3Iy-G%2Cnj9SO7K7aKKAePo62bCllq638jSLmXUwfPpD; SSESSfacf9b8b25d82259a587f5f12d822c95=oiYqQAfHaMeDLtNtRdUnCuGsXe-mpR8uA8B2dKu1BniRHx8h; __utma=269327925.1286796730.1698049924.1706870700.1706877489.27; _rup_ga_EVDQTJ4LMY=GS1.1.1706902360.270.0.1706902360.0.0.0; _ga_TNZM2XR4ZN=GS1.1.1706902360.96.0.1706902360.0.0.0;";
            Set<String> processedURLs = new HashSet<>();
            Workbook workbook = new XSSFWorkbook();
            Sheet resultsSheet = workbook.createSheet("Testing Result");

            System.out.println("Crawling and verifying URLs...");

            crawlAndVerifyURL(startingUrl, cookie, resultsSheet, processedURLs);

            System.out.println("URL crawling and verification completed.");

            try (FileOutputStream fileOut = new FileOutputStream("C:\\Users\\Vardot QA\\Downloads\\test 2.xlsx")) {
                workbook.write(fileOut);
                System.out.println("Workbook written to file successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                logError("Error writing workbook to file: " + e.getMessage());
                System.exit(-1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logError("Unhandled exception: " + e.getMessage());
            System.exit(-1);
        }
    }

    private static void crawlAndVerifyURL(String startingUrl, String currentCookie, Sheet resultsSheet, Set<String> processedURLs) throws IOException {
        Queue<String> queue = new LinkedList<>();
        Set<String> visitedURLs = new HashSet<>();

        queue.add(startingUrl);

        Row headerRow = resultsSheet.createRow(0);
        createCell(headerRow, 0, "URL");
        createCell(headerRow, 1, "URL Type");
        createCell(headerRow, 2, "Test Status");
        createCell(headerRow, 3, "H1 tags number");
        createCell(headerRow, 4, "Response Time");
        createCell(headerRow, 5, "PDF Size");
        createCell(headerRow, 6, "Image Size");
        createCell(headerRow, 7, "Response Code");
        createCell(headerRow, 8, "Response Text");

        while (!queue.isEmpty()) {
            String currentUrl = queue.poll();

            System.out.println("Processing URL: " + currentUrl);

            if (processedURLs.contains(currentUrl)) {
                continue;
            }

            if (!isWellFormattedHttpUrl(currentUrl)) {
                continue;
            }

            if (currentUrl.contains("logout") || currentUrl.contains("/admin/flush")) {
                continue;
            }

            boolean isSameDomain = isSameDomain(startingUrl, currentUrl);

            scannedLinks++;

            String[] apiResponse = apiResponse(currentUrl, currentCookie);
            String result = getResult(currentUrl, apiResponse, resultsSheet);

            Row row = resultsSheet.createRow(resultsSheet.getLastRowNum() + 1);
            Elements h1Tags = Jsoup.connect(currentUrl).header("Cookie", currentCookie).get().select("h1");
            int number_Of_H1Tags = h1Tags.size();
            String h1TagResult = String.valueOf(number_Of_H1Tags);
            createCell(row, 0, currentUrl);
            createCell(row, 1, urlType);
            createCell(row, 2, result);
            createCell(row, 3, h1TagResult);
            createCell(row, 4, String.valueOf(endTime - startTime));

            processedURLs.add(currentUrl);

            if (isSameDomain || !result.equals("Passed")) {
                Elements links = Jsoup.connect(currentUrl).header("Cookie", currentCookie).get().select("a[href]");

                for (Element link : links) {
                    String url = link.absUrl("href");

                    if (!isWellFormattedHttpUrl(url)) {
                        continue;
                    }

                    if (!url.isEmpty() && visitedURLs.add(url)) {
                        if (!url.contains("logout") && !url.contains("/admin/flush")) {
                            queue.add(url);
                            urlType = "internal";
                        } else {
                            String[] externalApiResponse = apiResponse(url, currentCookie);
                            String externalResult = getResult(url, externalApiResponse, resultsSheet);

                            if (externalResult.equals("Passed")) {
                                verifyPDFAndImageSizes(url, row, currentCookie);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void verifyPDFAndImageSizes(String urlString, Row row, String currentCookie) {
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Cookie", currentCookie);

            if (connection instanceof HttpURLConnection) {
                HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
                httpURLConnection.setRequestMethod("GET");

                try (InputStream inputStream = httpURLConnection.getInputStream()) {
                    String contentType = httpURLConnection.getContentType();

                    if (contentType != null && contentType.toLowerCase().contains("application/pdf")) {
                        verifyPDFResponse(urlString, httpURLConnection, row);
                    } else if (contentType != null && contentType.toLowerCase().startsWith("image/")) {
                        verifyImageResponse(urlString, httpURLConnection, row);
                    }
                } finally {
                    endTime = System.currentTimeMillis();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void verifyPDFResponse(String urlString, HttpURLConnection httpURLConnection, Row row) {
        try (InputStream inputStream = httpURLConnection.getInputStream()) {
            int pdfResponseCode = httpURLConnection.getResponseCode();
            long pdfSize = httpURLConnection.getContentLengthLong();

            createCell(row, 5, String.valueOf(pdfSize));

            if (pdfResponseCode == 200 && pdfSize <= 800 * 1024) {
                System.out.println("PDF Verification Passed for URL: " + urlString);
            } else {
                System.out.println("PDF Verification Failed for URL: " + urlString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void verifyImageResponse(String urlString, HttpURLConnection httpURLConnection, Row row) {
        try (InputStream inputStream = httpURLConnection.getInputStream()) {
            int imageResponseCode = httpURLConnection.getResponseCode();
            long imageSize = httpURLConnection.getContentLengthLong();

            createCell(row, 6, String.valueOf(imageSize));

            if (imageResponseCode == 200 && imageSize <= 800 * 1024) {
                System.out.println("Image Verification Passed for URL: " + urlString);
            } else {
                System.out.println("Image Verification Failed for URL: " + urlString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isWellFormattedHttpUrl(String url) {
        try {
            URL u = new URL(url);
            return "http".equalsIgnoreCase(u.getProtocol()) || "https".equalsIgnoreCase(u.getProtocol());
        } catch (MalformedURLException e) {
            return false;
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

    private static void logError(String message) {
        System.err.println("ERROR: " + message);
    }

    private static String[] apiResponse(String urlString, String currentCookie) {
        // Skip certain URLs
        if (shouldSkipUrl(urlString)) {
            return new String[]{"200", "Skipped"};
        }

        try {
            HttpURLConnection httpURLConnection = createHttpURLConnection(urlString, currentCookie);

            String responseText = readHttpResponse(httpURLConnection);

            handleApiResponse(responseText, currentCookie);

            return responseText.split(";", 2);
        } catch (IOException e) {
            return handleIOException(e);
        }
    }

    private static boolean shouldSkipUrl(String urlString) {
        return urlString.contains("/logout") || urlString.contains("/admin/flush");
    }

    private static HttpURLConnection createHttpURLConnection(String urlString, String currentCookie) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", currentCookie);

        return connection;
    }

    private static String readHttpResponse(HttpURLConnection httpURLConnection) throws IOException {
        try (InputStream inputStream = httpURLConnection.getInputStream()) {
            return httpURLConnection.getResponseCode() + ";" + readInputStream(inputStream);
        }
    }

    private static void handleApiResponse(String responseText, String currentCookie) {
        if ("403".equals(responseText.split(";")[0])) {
            handleConsecutive403Responses(currentCookie);
        } else {
            resetConsecutive403Counter();
            System.out.println("Response code: " + responseText.split(";")[0]);
        }
    }

    private static void handleConsecutive403Responses(String currentCookie) {
        apiResponseCount++;
        System.out.println("Current API response count: " + apiResponseCount);

        if (apiResponseCount <= 0) {
            System.out.println("Five consecutive 403 responses encountered. Updating cookie...");
            updateCookieAndResetCounter(currentCookie);
        }
    }

    private static void updateCookieAndResetCounter(String currentCookie) {
        System.out.println("Five consecutive 403 responses encountered. Please enter a new cookie:");
        Scanner scanner = new Scanner(System.in);
        String newCookie = scanner.nextLine().trim();

        currentCookie = newCookie;
        apiResponseCount = 0;

        System.out.println("Cookie updated. Current API response count: " + apiResponseCount);
    }

    private static void resetConsecutive403Counter() {
        apiResponseCount = 0;
    }

    private static String[] handleIOException(IOException e) {
        String[] resultArray;
        if (e instanceof FileNotFoundException || e instanceof UnknownServiceException) {
            resultArray = new String[]{"200", "Skipped - Image/PDF"};
        } else {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String stackTrace = sw.toString();
            resultArray = new String[]{"500", stackTrace};
        }
        return resultArray;
    }

    private static String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        return textBuilder.toString();
    }

    private static String getResult(String currentUrl, String[] apiResponse, Sheet resultsSheet) {
        String responseCode = apiResponse[0];
        String responseText = apiResponse[1];

        if (responseCode.equals("200")) {
            return "Passed";
        } else {
            Row row = resultsSheet.createRow(resultsSheet.getLastRowNum() + 1);
            createCell(row, 0, currentUrl);
            createCell(row, 1, urlType);
            createCell(row, 2, "Failed");
            createCell(row, 3, "");
            createCell(row, 4, "");
            createCell(row, 5, "");
            createCell(row, 6, "");
            createCell(row, 7, responseCode);
            createCell(row, 8, responseText);
            return "Failed";
        }
    }
}
