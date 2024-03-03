package src.test.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.xssf.usermodel.*;
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

			// Write the workbook to a file
			FileOutputStream out = new FileOutputStream(new File("Count_Entries.xlsx"));
			workbook.write(out);
			out.close();
			workbook.close(); // Close the workbook after writing

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void countEntries(JsonNode jsonNode, Map<String, Integer> countMap, String parentKey) {
		if (jsonNode.isArray()) {
			countArray(jsonNode, countMap, parentKey); // Pass parentKey to countArray
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




	static void processXmlFile(String filePath) throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.parse(new File(filePath));

			System.out.println("XML content:\n" + document.getDocumentElement().getTextContent());

		} catch (ParserConfigurationException | IOException | SAXException e) {
			throw e;
		}
	}
}