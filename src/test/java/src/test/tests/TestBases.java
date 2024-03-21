package src.test.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.poi.xssf.usermodel.*;

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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.LinkedHashMap;
import java.io.FileOutputStream;
import java.util.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.*;
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


	//Count Json Values

	static void mapJson(String filePath) {
		XSSFSheet mainSheet = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(new File(filePath));

			Map<String, Integer> countMap = new HashMap<>();
			countEntries(jsonNode, countMap, "");

			XSSFWorkbook workbook = new XSSFWorkbook();
			mainSheet = workbook.createSheet("Entries Duplication");

			// Populate CountEntries map
			Map<String, Object[]> CountEntries = new TreeMap<>();
			CountEntries.put("1", new Object[]{"Key", "Number of Repetition"});
			int rowNum = 2; // Start from the second row for data

			for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
				CountEntries.put(String.valueOf(rowNum++), new Object[]{entry.getKey(), entry.getValue()});
			}

			// Write CountEntries to main sheet
			int rowId = 0;
			for (Map.Entry<String, Object[]> entry : CountEntries.entrySet()) {
				XSSFRow row = mainSheet.createRow(rowId++);
				Object[] rowData = entry.getValue();
				int cellId = 0;
				for (Object obj : rowData) {
					Cell cell = row.createCell(cellId++);
					if (obj instanceof Integer) {
						cell.setCellValue((Integer) obj);
					} else if (obj instanceof String) {
						cell.setCellValue((String) obj);
					} else {
						cell.setCellValue(obj.toString());
					}
				}
			}

			FileOutputStream out = new FileOutputStream(new File("Count_Entries.xlsx"));
			workbook.write(out);
			out.close();
			workbook.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void countEntries(JsonNode jsonNode, Map<String, Integer> countMap, String parentKey) {
		if (jsonNode.isArray()) {
			countArray(jsonNode, countMap, parentKey);
		} else if (jsonNode.isObject()) {
			Iterator<Map.Entry<String, JsonNode>> fieldsIterator = jsonNode.fields();
			while (fieldsIterator.hasNext()) {
				Map.Entry<String, JsonNode> fieldEntry = fieldsIterator.next();
				String fieldName = fieldEntry.getKey();
				JsonNode fieldValue = fieldEntry.getValue();
				if (fieldValue.isObject() || fieldValue.isArray()) {
					countEntries(fieldValue, countMap, parentKey + "." + fieldName); // Recursively count entries if the field is an object or array
				} else {
					countValue(parentKey + "." + fieldName, fieldValue, countMap); // Count the value if the field is not an object or array
				}
			}
		}
	}

	static void countArray(JsonNode arrayNode, Map<String, Integer> countMap, String parentKey) {
		String arrayName = parentKey.isEmpty() ? "Entries" : parentKey + ".Entries"; // Assign a default name for the array
		Iterator<JsonNode> elementsIterator = arrayNode.elements();
		while (elementsIterator.hasNext()) {
			JsonNode element = elementsIterator.next();
			countEntries(element, countMap, parentKey); // Pass parentKey to countEntries
		}
		countMap.put(arrayName, arrayNode.size()); // Store the count for the entire array
	}
	static void countValue(String fullFieldName, JsonNode fieldValue, Map<String, Integer> countMap) {
		// Handle non-null values as before
		String fieldValueStr = fieldValue.toString();
		countMap.put(fullFieldName + ": " + fieldValueStr, countMap.getOrDefault(fullFieldName + ": " + fieldValueStr, 0) + 1);
	}




	//Read Json To Write into Excel

	static void insertJsonToExcel(String filePath){
		try {
			// Create an ObjectMapper instance
			ObjectMapper objectMapper = new ObjectMapper();

			// Read JSON data from file into a JsonNode
			JsonNode jsonNode = objectMapper.readTree(new File(filePath));

			// Extract keys and their types dynamically
			LinkedHashMap<String, String> keysAndTypes = new LinkedHashMap<>();
			extractKeysAndTypes(jsonNode, keysAndTypes, "");

			// Create Excel workbook and sheet
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("Data");

			// Create header row
			Row headerRow = sheet.createRow(0);
			int columnCount = 0;
			for (String key : keysAndTypes.keySet()) {
				Cell cell = headerRow.createCell(columnCount++);
				cell.setCellValue(key);
			}

			// Fill rows with object values
			int rowCount = 1;
			JsonNode itemArray = jsonNode.get("item");
			if (itemArray.isArray()) {
				for (JsonNode objNode : itemArray) {
					Row row = sheet.createRow(rowCount++);
					int cellCount = 0;
					for (String key : keysAndTypes.keySet()) {
						String[] keys = key.split("\\.");
						JsonNode currentNode = objNode;
						for (String k : keys) {
							if (currentNode != null && currentNode.has(k)) {
								currentNode = currentNode.get(k);
							} else {
								// Handle the case when the key does not exist
								System.out.println(currentNode + "This Value is NULL" + ", K Value = " + k);


							}
						}
						if (currentNode != null) {
							System.out.println("Key: " + key + ", Value: " + currentNode);
							Cell cell = row.createCell(cellCount++);
							setCellValue(cell, currentNode);
						} else {
							// If the key doesn't exist, set an empty value for the cell
							Cell cell = row.createCell(cellCount++);
							cell.setCellValue("");
						}
					}
				}
			}

			// Write the workbook to file
			FileOutputStream fileOut = new FileOutputStream("output.xlsx");
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();
			System.out.println("Excel file generated successfully.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Method to extract keys and their types recursively from a JSON node
	private static void extractKeysAndTypes(JsonNode jsonNode, Map<String, String> keysAndTypes, String parent) {
		// Iterate through each field in the JSON node
		Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
		while (fields.hasNext()) {
			Map.Entry<String, JsonNode> fieldEntry = fields.next();
			String fieldName = fieldEntry.getKey();
			JsonNode fieldNode = fieldEntry.getValue();

			// Create the key in the format "parent.child" if parent is not empty
			String key = parent.isEmpty() ? fieldName : parent + "." + fieldName;

			// Add the current key and its type
			keysAndTypes.put(key, getType(fieldNode));

			// If the fieldNode is an object, recursively extract its keys and types
			if (fieldNode.isObject()) {
				extractKeysAndTypes(fieldNode, keysAndTypes, key);
			}

			// If the fieldNode is an array, get the keys and types of its elements
			if (fieldNode.isArray() && fieldNode.size() > 0) {
				JsonNode arrayElement = fieldNode.get(0); // Get the first element
				if (arrayElement.isObject()) {
					extractKeysAndTypes(arrayElement, keysAndTypes, key);
				}
			}
		}
	}

	// Method to get the type of a JSON node
	private static String getType(JsonNode jsonNode) {
		if (jsonNode.isArray()) {
			return "Array";
		} else if (jsonNode.isObject()) {
			return "Object";
		} else if (jsonNode.isNumber()) {
			return "Number";
		} else if (jsonNode.isBoolean()) {
			return "Boolean";
		} else {
			return "String";
		}
	}

	// Method to set cell value based on the type of JSON node
	private static void setCellValue(Cell cell, JsonNode jsonNode) {
		if (jsonNode.isTextual()) {
			cell.setCellValue(jsonNode.asText());
		} else if (jsonNode.isNumber()) {
			cell.setCellValue(jsonNode.asDouble());
		} else if (jsonNode.isBoolean()) {
			cell.setCellValue(jsonNode.asBoolean());
		} else {
			cell.setCellValue(jsonNode.toString());
		}
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