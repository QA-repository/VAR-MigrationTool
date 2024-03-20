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



        String filePath = projectPath + "/Resources/JsonSample.json";
         try {
              processJsonFile(filePath);
        } catch (IOException e) {
             e.printStackTrace();
        }
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
        //String XMLdata = readXMLTestCase();
       // TestBases.xmlCount(XMLdata);
       // TestBases.xmlEmptyRecodes(XMLdata);
        //TestBases.xmlDataType(XMLdata);
        //duplication itemsz
        // collect items pre-post execution
        //String filePath = projectPath + "/Resources/countries.XML";
        String filePath = projectPath + "/Resources/externallinks.XML";
        //TestBases.writeDataToExcel(filePath);
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
