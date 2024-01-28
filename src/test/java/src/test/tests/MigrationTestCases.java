package src.test.tests;

import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


public class MigrationTestCases extends TestBases {
    String projectPath = System.getProperty("user.dir");

@Test
    public void readJsonTestCase(){

    String filePath = projectPath + "/Resources/JsonSample.json";
    try {
        processJsonFile(filePath);
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    @Test
    public void readXMLTestCase(){
        String filePath = projectPath + "/Resources/XMLSample.XML";


        try {
            processXmlFile(filePath);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }




}
