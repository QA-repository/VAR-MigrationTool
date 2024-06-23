package EnlyteProject;

public class NewMediaUpdated {
    //private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/mitchellschema";
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/newenlyteschema";
    //private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/liveenlyteschema";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    public static void main(String[] args) throws Exception {
        String schemaName = "newenlyteschema";
        String fileName = "New Enlyte Images Sheet5_";
        String sqlQuery = "SELECT " +
                "m.thumbnail__target_id, " +
                "m.mid, " +
                "m.thumbnail__alt, " +
                "m.status, " +
                "m.created, " +
                "m.changed, " +
                "m.name, " +
                "fm.uuid AS media_image_file_uuid, " +
                "fm.filename, " +
                "fm.filemime, " +
                "fm.uri, " +
                "fm.status, " +
                "med.uuid AS uuid " +
                "FROM " +
                "media_field_data AS m " +
                "LEFT JOIN " +
                "file_managed AS fm ON m.thumbnail__target_id = fm.fid " +
                "LEFT JOIN " +
                "media AS med ON med.mid = m.mid " +
                "WHERE " +
                "m.bundle = 'image' " +
                "LIMIT 100000;";


        CommonFunctionsMigrationUsingDB.exportQueryResultToExcel(JDBC_URL,USERNAME, PASSWORD, schemaName, sqlQuery, fileName);


    }
}
