package src.test.tests;

import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


public class MigrationTestCases extends TestBases {
    String projectPath = System.getProperty("user.dir");

@Test
    public void readJsonTestCase(){

    String filePath = projectPath + "/Resources/jsonformatter.json";
    try {
        processJsonFile(filePath);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    @Test
    public void countEntriesJson(){

        String filePath = projectPath + "/Resources/jsonformatter.json";
        mapJson(filePath);
    }

    @Test
    public void readJsonToStoreExcel(){

        String filePath = projectPath + "/Resources/xmltojson.json";
        insertJsonToExcel(filePath);

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
