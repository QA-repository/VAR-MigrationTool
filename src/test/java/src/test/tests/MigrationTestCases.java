package src.test.tests;

import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.script.ScriptException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.lang.NoSuchMethodException;
import java.util.Scanner;


public class MigrationTestCases extends TestBases {
    String projectPath = System.getProperty("user.dir");

@Test
    public void readJsonTestCase(){


    String filePath = projectPath + "/Resources/xmltojson.json";
    try {
        processJsonFile(filePath);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    @Test
    public void countEntriesJson(){

        String filePath = projectPath + "/Resources/xmltojson.json";
        mapJson(filePath);
    }

    @Test
    public void readJsonToStoreExcel(){

        String filePath = projectPath + "/Resources/xmltojson.json";
        insertJsonToExcel(filePath);

    }

    @Test
    public String readXMLTestCase(){
        String filePath = projectPath + "/Resources/countries.XML";
        String XMLdata = "";

        try {
           XMLdata =  processXmlFile(filePath);

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return XMLdata;
    }
    @Test
    public void countXMLdata(){

        String filePath = projectPath + "/Resources/externallinks.XML";
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document firstDoc = builder.parse(new File(filePath));
            TestBases.writeXmlToExcel(firstDoc, "output4.xlsx");

            System.out.println("Data has been successfully written to Excel file.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
