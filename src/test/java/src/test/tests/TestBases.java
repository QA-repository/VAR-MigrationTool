package src.test.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import static java.lang.System.getProperty;


public class TestBases {

		static void processJsonFile(String filePath) throws IOException {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(new File(filePath));
			System.out.println("JSON content:\n" + jsonNode.toString());


	}

	static void mapJson(String filePath) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(new File(filePath));
			//System.out.println("JSON content:");
			//System.out.println(jsonNode);

			Map<String, Integer> countMap = new HashMap<>();
			countEntries(jsonNode, countMap);

			// Print the count for each value
			for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
				System.out.println(entry.getKey() + ": " + entry.getValue());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void countEntries(JsonNode jsonNode, Map<String, Integer> countMap) {
		if (jsonNode.isArray()) {
			countArray(jsonNode, countMap); // Count occurrences for the entire JSON file and treat each array as a single entity
		} else if (jsonNode.isObject()) {
			Iterator<Map.Entry<String, JsonNode>> fieldsIterator = jsonNode.fields();
			while (fieldsIterator.hasNext()) {
				Map.Entry<String, JsonNode> fieldEntry = fieldsIterator.next();
				String fieldName = fieldEntry.getKey();
				JsonNode fieldValue = fieldEntry.getValue();
				if (fieldValue.isObject() || fieldValue.isArray()) {
					countEntries(fieldValue, countMap); // Recursively count entries if the field is an object or array
				} else {
					countValue(fieldName, fieldValue, countMap); // Count the value if the field is not an object or array
				}
			}
		}
	}

	static void countArray(JsonNode arrayNode, Map<String, Integer> countMap) {
		String arrayName = "Entries"; // Assign a default name for the array
		Iterator<JsonNode> elementsIterator = arrayNode.elements();
		while (elementsIterator.hasNext()) {
			JsonNode element = elementsIterator.next();
			countEntries(element, countMap); // Count occurrences for each element in the array
		}
		countMap.put(arrayName, arrayNode.size()); // Store the count for the entire array
	}

	static void countValue(String fieldName, JsonNode fieldValue, Map<String, Integer> countMap) {
		String fieldValueStr = fieldValue.toString(); // Get string representation of the field value
		countMap.put(fieldName + ": " + fieldValueStr, countMap.getOrDefault(fieldName + ": " + fieldValueStr, 0) + 1);
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