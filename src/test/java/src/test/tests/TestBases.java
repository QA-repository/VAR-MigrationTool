package src.test.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bonigarcia.wdm.WebDriverManager;
import net.bytebuddy.asm.Advice;
import org.apache.xmlbeans.xml.stream.Space;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.System.getProperty;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;


public class TestBases {

		static void processJsonFile(String filePath) throws IOException {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(new File(filePath));
			System.out.println("JSON content:\n" + jsonNode.toString());


	}
	static String processXmlFile(String filePath) throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.parse(new File(filePath));

			//System.out.println("XML content:\n" + document.getDocumentElement().getTextContent());
			return document.getDocumentElement().getTextContent();
		} catch (ParserConfigurationException | IOException | SAXException e) {
			throw e;
		}
	}
	static void xmlCount(String XMLdata)
	{
		// Split the string into records
		String[] records = XMLdata.split("\\n\\n");
		// Count the number of records
		int recordCount = (records.length)-3;
		System.out.println("Number of records are: " + recordCount);
	}
	static void xmlEmptyRecodes(String XMLdata)
	{
		// Split the string into records
		String[] records = XMLdata.split("\\n\\n");
		int emptyRecordCount = 0;

		for (String record : records) {
			if (record.trim().isEmpty()) {
				emptyRecordCount++;
			}
		}
		if (emptyRecordCount > 0) {
			System.out.println("Number of empty records: " + emptyRecordCount);
		} else {
			System.out.println("No records have an empty value.");
		}
	}
	static void xmlDataType(String XMLdata)
	{
		String trimExtraStrings = XMLdata.trim();
		String[] rows = trimExtraStrings.split("\n\n");

		for (String row : rows) {
			String[] elements = row.split("\n");
			for (String element : elements) {
				String dataType = getDataType(element);
				System.out.println("Data Type for '" + element + "': " + dataType);
			}
		}
	}
	public static String getDataType(String element) {
		if (element.matches("[a-zA-Z]+$")) {
			return "String";
		} else if (element.matches("[0-9]+$")) {
			return "Number)";
		} else if (element.matches("[a-zA-Z0-9\\s]+")) {
			return "Alphanumeric";
		} else if (element == null || element.isEmpty()) {
			return "Empty";
		} else {
			return "Not Identifier";
		}
	}

	public static void writeXmlToExcel(Document firstDoc, String fileName) throws Exception {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Data");

		Map<String, List<String>> dataMap = new HashMap<>();

		processNode(firstDoc.getDocumentElement(), dataMap);

		writeDataToSheet(sheet, dataMap);
		removeColumns(sheet, "results");
		removeColumns(sheet, "item");

		try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
			workbook.write(outputStream);
		}
	}

	private static void processNode(Node node, Map<String, List<String>> dataMap) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			String tagName = node.getNodeName();
			String textContent = node.getTextContent();

				if (!dataMap.containsKey(tagName)) {
					dataMap.put(tagName, new ArrayList<>());
				}
				dataMap.get(tagName).add(textContent);

				NodeList childNodes = node.getChildNodes();
				for (int i = 0; i < childNodes.getLength(); i++) {
					Node child = childNodes.item(i);
					processNode(child, dataMap);
				}
			}

	}
	private static void removeColumns(Sheet sheet, String columnName) {
		int columnIndex = getColumnIndex(sheet, columnName);
		if (columnIndex != -1) {
			for (Row row : sheet) {
				row.removeCell(row.getCell(columnIndex));
			}
			sheet.setColumnHidden(columnIndex, true); // Hide the column
		}
	}

	private static int getColumnIndex(Sheet sheet, String columnName) {
		Row headerRow = sheet.getRow(0);
		if (headerRow != null) {
			for (Cell cell : headerRow) {
				if (columnName.equals(cell.getStringCellValue())) {
					return cell.getColumnIndex();
				}
			}
		}
		return -1; // Column not found
	}

	private static void writeDataToSheet(Sheet sheet, Map<String, List<String>> dataMap) {
		int rowIndex = 0;
		Row headerRow = sheet.createRow(rowIndex++);

		int columnIndex = 0;
		for (String header : dataMap.keySet()) {
			headerRow.createCell(columnIndex++).setCellValue(header);
		}

		int maxRowCount = 0;
		for (List<String> dataList : dataMap.values()) {
			maxRowCount = Math.max(maxRowCount, dataList.size());
		}

		for (int i = 0; i < maxRowCount; i++) {
			Row row = sheet.createRow(rowIndex++);
			columnIndex = 0;
			for (List<String> dataList : dataMap.values()) {
				String value = (i < dataList.size()) ? dataList.get(i) : "";
				if (value.length() > 32767)
					row.createCell(columnIndex++).setCellValue("Long text: more than 32767 characters");
				else
					row.createCell(columnIndex++).setCellValue(value);
			}
		}
	}

}