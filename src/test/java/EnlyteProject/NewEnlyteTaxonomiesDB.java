package EnlyteProject;

public class NewEnlyteTaxonomiesDB {

    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/newenlyteschema";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    public static void main(String[] args) throws Exception {

        String schemaName = "newenlyteschema";
//        String fileName = "New Enlyte Markets Taxonomy Sheet_";
//        String sqlQuery = "SELECT t.tid, t.uuid, " +
//                "ttfd.name, ttfd.weight, " +
//                "ttp.parent_target_id, " +
//                "pa.alias " +
//                "FROM taxonomy_term_data AS t " +
//                "INNER JOIN taxonomy_term_field_data AS ttfd ON ttfd.tid = t.tid " +
//                "INNER JOIN taxonomy_term__parent AS ttp ON ttp.entity_id = t.tid " +
//                "LEFT JOIN path_alias AS pa ON CONCAT('/taxonomy/term/', t.tid) = pa.path " +
//                "WHERE t.vid = 'Markets';";
//
//        CommonFunctionsMigrationUsingDB.exportQueryResultToExcel(JDBC_URL,USERNAME, PASSWORD, schemaName, sqlQuery, fileName);

        String fileName2 = "New Enlyte Tags Taxonomy Sheet_";
        String sqlQuery2 = "SELECT t.tid, t.uuid, " +
                "ttfd.name, ttfd.weight, " +
                "ttp.parent_target_id, " +
                "pa.alias " +
                "FROM taxonomy_term_data AS t " +
                "INNER JOIN taxonomy_term_field_data AS ttfd ON ttfd.tid = t.tid " +
                "INNER JOIN taxonomy_term__parent AS ttp ON ttp.entity_id = t.tid " +
                "LEFT JOIN path_alias AS pa ON CONCAT('/taxonomy/term/', t.tid) = pa.path " +
                "WHERE t.vid = 'Tags';";

        CommonFunctionsMigrationUsingDB.exportQueryResultToExcel(JDBC_URL,USERNAME, PASSWORD, schemaName, sqlQuery2, fileName2);
//
//        String fileName3 = "New Enlyte Topics Taxonomy Sheet_";
//        String sqlQuery3 = "SELECT t.tid, t.uuid, " +
//                "ttfd.name, ttfd.weight, " +
//                "ttp.parent_target_id, " +
//                "pa.alias " +
//                "FROM taxonomy_term_data AS t " +
//                "INNER JOIN taxonomy_term_field_data AS ttfd ON ttfd.tid = t.tid " +
//                "INNER JOIN taxonomy_term__parent AS ttp ON ttp.entity_id = t.tid " +
//                "LEFT JOIN path_alias AS pa ON CONCAT('/taxonomy/term/', t.tid) = pa.path " +
//                "WHERE t.vid = 'Topics';";
//
//        CommonFunctionsMigrationUsingDB.exportQueryResultToExcel(JDBC_URL,USERNAME, PASSWORD, schemaName, sqlQuery3, fileName3);

    }
}
