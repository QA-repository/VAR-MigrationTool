package src.test.tests;

import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


public class MigrationTestCases extends TestBases {

@Test
    public void readJsonTestCase(){

    String filePath = "path/to/your/file.json";
    try {
        processJsonFile(filePath);
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    @Test
    public void readXMLTestCase(){

        String filePath = "path/to/your/file.XML";

        try {
            processXmlFile(filePath);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }




}
