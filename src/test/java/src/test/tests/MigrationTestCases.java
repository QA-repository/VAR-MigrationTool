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
    @Test
    public void callMitchellDB() throws Exception {
        final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/mitchellschema";
        final String USERNAME = "root";
        final String PASSWORD = "";
        String schemaName = "mitchellschema";
        String fileName = "Mitchell Media Sheet_";
        String sqlQuery = "SELECT m.thumbnail__target_id, m.mid, m.thumbnail__alt, " +
                "fm.uuid, fm.filename, fm.filemime, fm.uri, fm.status " +
                "FROM media_field_data AS m " +
                "LEFT JOIN media__field_media_audio_file AS fmaf ON fmaf.entity_id = m.mid " +
                "LEFT JOIN file_managed AS fm ON fmaf.field_media_audio_file_target_id = fm.fid " +
                "WHERE m.bundle = 'audio' " +
                "UNION " +
                "SELECT m.thumbnail__target_id, m.mid, m.thumbnail__alt, " +
                "fm.uuid, fm.filename, fm.filemime, fm.uri, fm.status " +
                "FROM media_field_data AS m " +
                "LEFT JOIN file_managed AS fm ON m.thumbnail__target_id = fm.fid " +
                "WHERE m.bundle = 'image' " +
                "UNION " +
                "SELECT m.thumbnail__target_id, m.mid, m.thumbnail__alt, " +
                "fm.uuid, fm.filename, fm.filemime, fm.uri, fm.status " +
                "FROM media_field_data AS m " +
                "LEFT JOIN media__field_media_document AS fmd ON fmd.entity_id = m.mid " +
                "LEFT JOIN file_managed AS fm ON fmd.field_media_document_target_id = fm.fid " +
                "WHERE m.bundle = 'document' " +
                "UNION " +
                "SELECT m.thumbnail__target_id, m.mid, m.thumbnail__alt, " +
                "fm.uuid, fm.filename, fm.filemime, fm.uri, fm.status " +
                "FROM media_field_data AS m " +
                "LEFT JOIN media__field_media_video_file AS fmvf ON fmvf.entity_id = m.mid " +
                "LEFT JOIN file_managed AS fm ON fmvf.field_media_video_file_target_id = fm.fid " +
                "WHERE m.bundle = 'video' " +
                "UNION " +
                "SELECT m.thumbnail__target_id, m.mid, m.thumbnail__alt, " +
                "fmd.field_media_oembed_video_value AS uuid, " +
                "NULL AS filename, NULL AS filemime, NULL AS uri, NULL AS status " +
                "FROM media_field_data AS m " +
                "LEFT JOIN media__field_media_oembed_video AS fmd ON fmd.entity_id = m.mid " +
                "WHERE m.bundle = 'remote_video';";
        CommonFunctionsMigrationUsingDB.exportQueryResultToExcel(JDBC_URL,USERNAME, PASSWORD, schemaName, sqlQuery, fileName);

    }
}
