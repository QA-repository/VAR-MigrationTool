package EnlyteProject;

public class NewEnlyteLiveAllMediaDB {
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/newenlyteschema";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) throws Exception {

        String schemaName = "newenlyteschema";
        String fileName = "New Enlyte Live Media Sheet_";
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
