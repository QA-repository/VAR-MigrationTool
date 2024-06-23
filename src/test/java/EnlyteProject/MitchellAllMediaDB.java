package EnlyteProject;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class MitchellAllMediaDB {
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/mitchellschema";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) throws Exception {

        String schemaName = "mitchellschema";
        String fileName = "Mitchell Media Sheet_";
        String sqlQuery = "SELECT m.thumbnail__target_id, m.mid, m.thumbnail__alt, m.status, m.created, m.changed, " +
                "fm.uuid, fm.filename, fm.filemime, fm.uri, fm.status AS fm_status, " +
                "med.uuid AS med_uuid " +
                "FROM media_field_data AS m " +
                "LEFT JOIN media__field_media_audio_file AS fmaf ON fmaf.entity_id = m.mid " +
                "LEFT JOIN file_managed AS fm ON fmaf.field_media_audio_file_target_id = fm.fid " +
                "LEFT JOIN media AS med ON med.mid = m.mid " +
                "WHERE m.bundle = 'audio' " +
                "UNION " +
                "SELECT m.thumbnail__target_id, m.mid, m.thumbnail__alt, m.status, m.created, m.changed, " +
                "fm.uuid, fm.filename, fm.filemime, fm.uri, fm.status AS fm_status, " +
                "med.uuid AS med_uuid " +
                "FROM media_field_data AS m " +
                "LEFT JOIN file_managed AS fm ON m.thumbnail__target_id = fm.fid " +
                "LEFT JOIN media AS med ON med.mid = m.mid " +
                "WHERE m.bundle = 'image' " +
                "UNION " +
                "SELECT m.thumbnail__target_id, m.mid, m.thumbnail__alt, m.status, m.created, m.changed, " +
                "fm.uuid, fm.filename, fm.filemime, fm.uri, fm.status AS fm_status, " +
                "med.uuid AS med_uuid " +
                "FROM media_field_data AS m " +
                "LEFT JOIN media__field_media_document AS fmd ON fmd.entity_id = m.mid " +
                "LEFT JOIN file_managed AS fm ON fmd.field_media_document_target_id = fm.fid " +
                "LEFT JOIN media AS med ON med.mid = m.mid " +
                "WHERE m.bundle = 'document' " +
                "UNION " +
                "SELECT m.thumbnail__target_id, m.mid, m.thumbnail__alt, m.status, m.created, m.changed, " +
                "fm.uuid, fm.filename, fm.filemime, fm.uri, fm.status AS fm_status, " +
                "med.uuid AS med_uuid " +
                "FROM media_field_data AS m " +
                "LEFT JOIN media__field_media_video_file AS fmvf ON fmvf.entity_id = m.mid " +
                "LEFT JOIN file_managed AS fm ON fmvf.field_media_video_file_target_id = fm.fid " +
                "LEFT JOIN media AS med ON med.mid = m.mid " +
                "WHERE m.bundle = 'video' " +
                "UNION " +
                "SELECT m.thumbnail__target_id, m.mid, m.thumbnail__alt, m.status, m.created, m.changed, " +
                "fmd.field_media_oembed_video_value, " +
                "med.uuid AS med_uuid " +
                "FROM media_field_data AS m " +
                "LEFT JOIN media__field_media_oembed_video AS fmd ON fmd.entity_id = m.mid " +
                "LEFT JOIN media AS med ON med.mid = m.mid " +
                "WHERE m.bundle = 'remote_video';";
        CommonFunctionsMigrationUsingDB.exportQueryResultToExcel(JDBC_URL,USERNAME, PASSWORD, schemaName, sqlQuery, fileName);
    }
}
