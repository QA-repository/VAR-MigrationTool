package src.test.tests;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class FetchDataFromContent extends DataFieldsTypes {

    // Properties file paths
    String checkboxPropertiesPath = "configs/DDS-For-CheckBox.properties";
    String ckEditorTextFieldsPropertiesPath = "configs/DDS-For-CKEditorTextFields.properties";
    String dropDownOptionListPropertiesPath = "configs/DDS-For-DropDownOptionList.properties";
    String mediaEntityPropertiesPath = "configs/DDS-For-MediaEntity.properties";
    String multipleItemPropertiesPath = "configs/DDS-For-MultipleItem.properties";
    String radioButtonPropertiesPath = "configs/DDS-For-RadioButtons.properties";
    String staticFieldLinkPropertiesPath = "configs/DDS-For-StaticFieldLink.properties";
    String textFieldsDescriptionPropertiesPath = "configs/DDS-For-TextFieldsDescription.properties";
    String textFieldsInputValuesPropertiesPath = "configs/DDS-For-TextFieldsInputValues.properties";

    // Read properties files
    Map<String, String> checkboxProperties = PropertiesReader.readProperties(checkboxPropertiesPath);
    Map<String, String> ckEditorTextFieldsProperties = PropertiesReader.readProperties(ckEditorTextFieldsPropertiesPath);
    Map<String, String> dropDownOptionListProperties = PropertiesReader.readProperties(dropDownOptionListPropertiesPath);
    Map<String, String> mediaEntityProperties = PropertiesReader.readProperties(mediaEntityPropertiesPath);
    Map<String, String> multipleItemProperties = PropertiesReader.readProperties(multipleItemPropertiesPath);
    Map<String, String> radioButtonProperties = PropertiesReader.readProperties(radioButtonPropertiesPath);
    Map<String, String> staticFieldLinkProperties = PropertiesReader.readProperties(staticFieldLinkPropertiesPath);
    Map<String, String> textFieldsDescriptionProperties = PropertiesReader.readProperties(textFieldsDescriptionPropertiesPath);
    Map<String, String> textFieldsInputValuesProperties = PropertiesReader.readProperties(textFieldsInputValuesPropertiesPath);


    String cookies = "_rup_ga=GA1.1.7222690.1693401802; _ga_TNZM2XR4ZN=GS1.1.1693401801.1.1.1693401836.0.0.0; RW_cookie_consent=true; _gid=GA1.2.1350146555.1716742803; SSESS80b96fbdf177244a118611b368ce9226=gAD8ySQCbT7-GKM0qVijKOCcxLImnQS3EFVlk9AAUzCA7%2CcI; SPL80b96fbdf177244a118611b368ce9226=UcigzAXdvq6b8hj4apgIf56FQR_S6a2AtmPoBcSHy9g%3AiVolbdrLrmO73zzHNQCwZr3LGwBk-4qNPwYcVd1Xf3c; _gat_UA-31149044-1=1; _gat_UA-1473340-18=1; _ga=GA1.2.7222690.1693401802; _rup_ga_EVDQTJ4LMY=GS1.1.1716800579.18.1.1716800722.47.0.0; _ga_F9VC73DPR5=GS1.1.1716800579.17.1.1716800722.47.0.0";
    String username = "admin";
    String password = "Refw0rld!";

    @Test
    public void fetchDataFromContent() {

        String file = "Resources/Edit_URLs_Content_Page.xlsx";
        this.cookies = cookies;
        List<List<String>> excelLinks = readLinkToStore(file);
        System.out.println(excelLinks);
        this.username = username;
        this.password = password;
        List<String> savedResponseBodies = makeGetRequests(excelLinks, username, password, cookies);
        if (!savedResponseBodies.isEmpty()) {
            writeTabNamesToExcel(savedResponseBodies.get(0), "Resources/Tabs_Content_Page.xlsx");
        }
    }


    @Test
    public void extractDataToExcel(){



        String file = "Resources/Edit_URLs_Content_Page.xlsx";
        this.cookies = cookies;
        this.username = username;
        this.password = password;
        String tabsFile= "Resources/Tabs_Content_Page.xlsx";
        List<List<String>> excelLinks = readLinkToStore(file);
        List<String> savedResponseBodies = makeGetRequests(excelLinks, username, password, cookies);
        //System.out.println(savedResponseBodies);



        // Open the existing Excel file
        try (FileInputStream inputStream = new FileInputStream(tabsFile);
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            int rowIndex = 1; // Start from the second row

            for (int i = 0; i < savedResponseBodies.size(); i++) {
                String responseBody = savedResponseBodies.get(i);

                if (responseBody != null) {
                    Document document = Jsoup.parse(responseBody);

                    for (Sheet sheet : workbook) {
                        Row row = sheet.getRow(rowIndex);
                        if (row == null) {
                            row = sheet.createRow(rowIndex);
                        }

                        // Write the link to the first column
                        Cell linkCell = row.createCell(0);
                        linkCell.setCellValue(excelLinks.get(i).get(0));

                        // Call your methods to fill other data in the row
                        // Here, I'm assuming you have methods to extract data from the document
                        // For example, if you have a method called `extractDataForSheet`:
                        List<String> data = extractDataForSheet(sheet.getSheetName(), document);
                        for (int j = 0; j < data.size(); j++) {
                            Cell cell = row.createCell(j + 1);
                            cell.setCellValue(data.get(j));
                        }
                    }

                    rowIndex++;
                }
            }

            // Save the updated Excel file
            try (FileOutputStream outputStream = new FileOutputStream(tabsFile)) {
                workbook.write(outputStream);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private List<String> extractDataForSheet(String sheetName, Document document) {
        // This method should return a list of strings representing the data to be written in each column
        // for the given sheetName. Implement your data extraction logic here.
        List<String> data = new ArrayList<>();

        // Example extraction logic based on sheetName
        switch (sheetName) {
            case "Basic info":
                // Extract basic info data from the document
                String DDSTitle = textFieldsInputValuesProperties.get("BasicInfo_Title");
                String DDSBody = ckEditorTextFieldsProperties.get("BasicInfo_Body");
                data.add(textField(DDSTitle, document));
                data.add(textFieldBody(DDSBody, document));
                break;
            case "Media header":
                // Extract media header data from the document
                String DDSMediaHeaderStyle = radioButtonProperties.get("MediaHeader_MediaHeaderStyle");
                String DDSBgColor = dropDownOptionListProperties.get("MediaHeader_BackGroundColor");
                String DDSPreTitle = textFieldsInputValuesProperties.get("MediaHeader_PreTitle");
                String DDSPageDesc = textFieldsDescriptionProperties.get("MediaHeader_PageDescription");
                String DDSTextAlign = dropDownOptionListProperties.get("MediaHeader_TextAlign");
                String DDSMediaEntity = mediaEntityProperties.get("MediaHeader_Media");
                String DDSVideoButtonText = textFieldsInputValuesProperties.get("MediaHeader_VideoButtonText");
                String DDSHideVideo = checkboxProperties.get("MediaHeader_HideVideo");
                String DDSURL00 = staticFieldLinkProperties.get("MediaHeader_Url00");
                String DDSLinkText00 = staticFieldLinkProperties.get("MediaHeader_Url00");
                String DDSSelectStyle00 = dropDownOptionListProperties.get("MediaHeader_SelectAStyle_00");
                String DDSURL01 = staticFieldLinkProperties.get("MediaHeader_Url01");
                String DDSLinkText01 = staticFieldLinkProperties.get("MediaHeader_Url01");
                String DDSSelectStyle01 = dropDownOptionListProperties.get("MediaHeader_SelectAStyle_01");
                data.add(radioButton(DDSMediaHeaderStyle, document));
                data.add(dropDown(DDSBgColor, document));
                data.add(textField(DDSPreTitle, document));
                data.add(textFieldDescription(DDSPageDesc, document));
                data.add(dropDown(DDSTextAlign, document));
                data.add(mediaEntity(DDSMediaEntity, document));
                data.add(textField(DDSVideoButtonText, document));
                data.add(checkBox(DDSHideVideo, document));
                data.add(staticFieldLink(DDSURL00, document));
                data.add(staticFieldLink(DDSLinkText00, document));
                data.add(dropDown(DDSSelectStyle00, document));
                data.add(staticFieldLink(DDSURL01, document));
                data.add(staticFieldLink(DDSLinkText01, document));
                data.add(dropDown(DDSSelectStyle01, document));

                break;
            case "Related info":
                // Extract related info data from the document

                String DDSTopic = multipleItemProperties.get("RelatedInfo_Topic");
                String DDSTags = multipleItemProperties.get("RelatedInfo_Tags");
                String DDSRegion = multipleItemProperties.get("RelatedInfo_Region");
                String DDSCountries = multipleItemProperties.get("RelatedInfo_Countries");
                data.add(multipleItem(DDSTopic, document));
                data.add(multipleItem(DDSTags, document));
                data.add(multipleItem(DDSRegion, document));
                data.add(multipleItem(DDSCountries, document));
                System.out.println(data + "  26454212  ");

                break;
            default:
                // Handle other sheet names if necessary
                break;
        }

        return data;
    }







    private static List<String> makeGetRequests(List<List<String>> excelLinks, String username, String password, String cookies) {
        List<String> responseBodyList = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(10); // Adjust the pool size based on your needs

        List<Future<String>> futures = new ArrayList<>();
        for (List<String> row : excelLinks) {
            String url = row.get(0);
            Callable<String> task = () -> fetchUrlContent(url, username, password, cookies);
            futures.add(executor.submit(task));
        }

        for (Future<String> future : futures) {
            try {
                String responseBody = future.get();
                if (responseBody != null) {
                    responseBodyList.add(responseBody);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        return responseBodyList;
    }

    private static String fetchUrlContent(String url, String username, String password, String cookies) {
        int maxRetries = 4;
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                String base64Credentials = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
                Connection.Response response = Jsoup.connect(url)
                        .header("Authorization", "Basic " + base64Credentials)
                        .header("Cookie", cookies)
                        .method(Connection.Method.GET)
                        .timeout(30000) // Set a timeout of 10 seconds
                        .execute();

                int statusCode = response.statusCode();
                System.out.println("Sending GET request to URL: " + url);
                System.out.println("Response Code: " + statusCode);

                if (statusCode == 200) {
                    Document document = response.parse();
                    return document.outerHtml(); // or use document.toString() for raw HTML
                } else {
                    System.out.println("Failed to fetch data from URL: " + url + ". Status code: " + statusCode);
                }
            } catch (IOException e) {
                attempt++;
                System.out.println("Attempt " + attempt + " failed for URL: " + url + " with error: " + e.getMessage());
                if (attempt >= maxRetries) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static List<List<String>> readLinkToStore(String file) {
        List<List<String>> data = new ArrayList<>();

        try (FileInputStream inputStream = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0); // Assuming you are reading the first sheet

            // Iterate through each row in the sheet
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    List<String> rowData = new ArrayList<>();
                    // Iterate through each cell in the row
                    for (Cell cell : row) {
                        rowData.add(cell.getStringCellValue());
                    }
                    data.add(rowData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private static List<String> getTabNames(String htmlContent) {
        List<String> tabNames = new ArrayList<>();

        try {
            Document document = Jsoup.parse(htmlContent);
            Elements tabs = document.select("div[data-horizontal-tabs-panes] details");

            for (Element tab : tabs) {
                String tabName = tab.select("summary.claro-details__summary").text();
                tabNames.add(tabName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tabNames;
    }

    private static void writeTabNamesToExcel(String htmlContent, String outputFile) {
        List<String> tabNames = getTabNames(htmlContent);
        Workbook workbook = new XSSFWorkbook();
        FileOutputStream fileOut = null;


        try {
            for (String tabName : tabNames) {
                workbook.createSheet(tabName);
            }

            fileOut = new FileOutputStream(outputFile);
            workbook.write(fileOut);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOut != null) {
                    fileOut.close();
                }
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
