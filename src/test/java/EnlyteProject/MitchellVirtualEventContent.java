package EnlyteProject;

public class MitchellVirtualEventContent {
    //private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/mitchellschema";
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/newenlyteschema";
    //private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/liveenlyteschema";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    public static void main(String[] args) throws Exception {
        String schemaName = "newenlyteschema";
        String fileName = "New Enlyte Virtual Event Sheet2_";
        String sqlQuery = "SELECT " +
                "nfd.nid, nfd.vid, nfd.uid, nfd.status, nfd.title, nfd.created, nfd.changed, nfd.promote, nfd.sticky, nfd.publish_on, nfd.unpublish_on, " +
                "n.uuid AS node__uuid, " +
                "pa.alias, " +
                "kv.value AS pathauto_state_value, " +
                "nfb.body_value, nfb.body_summary, nfb.body_format, " +
                "nfell.field_external_link_label_value, " +
                "nfel.field_external_link_value, " +
                "nflmc.field_link_to_main_content_uri, nflmc.field_link_to_main_content_title, " +
                "nfold_url.field_old_url_value, " +
                "nffcl.field_full_content_link_value, " +
                "nffd.field_form_description_value, nffd.field_form_description_format, " +
                "nffn.field_form_newsletter_value, " +
                "nfft.field_form_title_value, " +
                "nfdate.field_date_value, " +
                "nfevdate.field_event_date_value, " +
                "media_hero_image.uuid AS node__field_hero_image, " +
                "media_opengraph.uuid AS node__field_opengraph_image, " +
                "media_twitter.uuid AS node__field_twitter_card_image, " +
                "nflof.field_link_on_form_uri, nflof.field_link_on_form_title, " +
                "nfo.field_origin_uri, nfo.field_origin_title, " +
                "nfpl.field_pardot_link_uri AS field_pardot_link_uri, nfpl.field_pardot_link_title AS field_pardot_link_title, " +
                "nfrl.field_recording_link_uri AS field_recording_link_uri, nfrl.field_recording_link_title AS field_recording_link_title, " +
                "nfrgl.field_registration_link_uri AS field_registration_link_uri, nfrgl.field_registration_link_title AS field_registration_link_title, " +
                "nfsp.field_speaker_value, " +
                "GROUP_CONCAT(DISTINCT n_author.uuid ORDER BY nfa.delta SEPARATOR ',') AS field_author_uuids, " +
                "GROUP_CONCAT(DISTINCT ttd_markets.uuid ORDER BY nfm.delta SEPARATOR ',') AS field_markets_uuids, " +
                "GROUP_CONCAT(DISTINCT ttd_topics.uuid ORDER BY nftop.delta SEPARATOR ',') AS field_topics_uuids, " +
                "nfol.field_open_link_value, " +
                "nfc.field_colorized_value, " +
                "nfdf.field_display_form_value, " +
                "nfdioc.field_display_image_on_card_value, " +
                "nfdrt.field_display_read_time_value, " +
                "nfdrc.field_display_related_stories_value, " +
                "nfbrand.field_brand_value, " +
                "nfct.field_card_type_value, " +
                "nfsst.field_show_start_time_value, " +
                "nfs.field_seo_value, " +
                "nfeed.field_event_end_date_value, " +
                "nfeet.field_event_end_time_value, " +
                "nflc.field_layout_canvas_target_id, " +
                "nfod.field_on_demand_value, " +
                "nfr.field_resource_uri, nfr.field_resource_title, " +
                "nfsm.field_show_map_value, " +
                "nfsubsites.field_subsites_value, " +
                "nfdtol.field_display_thumbnail_on_listi_value " +
                "FROM " +
                "node_field_data AS nfd " +
                "LEFT JOIN node AS n ON n.nid = nfd.nid " +
                "LEFT JOIN path_alias AS pa ON CONCAT('/node/', nfd.nid) = pa.path " +
                "LEFT JOIN key_value AS kv ON kv.name = nfd.nid AND kv.collection = 'pathauto_state.node' " +
                "LEFT JOIN node__body AS nfb ON nfb.entity_id = nfd.nid " +
                "LEFT JOIN node__field_external_link_label AS nfell ON nfell.entity_id = nfd.nid " +
                "LEFT JOIN node__field_external_link AS nfel ON nfel.entity_id = nfd.nid " +
                "LEFT JOIN node__field_link_to_main_content AS nflmc ON nflmc.entity_id = nfd.nid " +
                "LEFT JOIN node__field_old_url AS nfold_url ON nfold_url.entity_id = nfd.nid " +
                "LEFT JOIN node__field_full_content_link AS nffcl ON nffcl.entity_id = nfd.nid " +
                "LEFT JOIN node__field_form_description AS nffd ON nffd.entity_id = nfd.nid " +
                "LEFT JOIN node__field_form_newsletter AS nffn ON nffn.entity_id = nfd.nid " +
                "LEFT JOIN node__field_form_title AS nfft ON nfft.entity_id = nfd.nid " +
                "LEFT JOIN node__field_date AS nfdate ON nfdate.entity_id = nfd.nid " +
                "LEFT JOIN node__field_event_date AS nfevdate ON nfevdate.entity_id = nfd.nid " +
                "LEFT JOIN node__field_hero_image AS nfhi ON nfhi.entity_id = nfd.nid " +
                "LEFT JOIN media__field_media_image AS mfmi_hi ON mfmi_hi.entity_id = nfhi.field_hero_image_target_id " +
                "LEFT JOIN media AS media_hero_image ON media_hero_image.mid = mfmi_hi.entity_id " +
                "LEFT JOIN node__field_opengraph_image AS nfogi ON nfogi.entity_id = nfd.nid " +
                "LEFT JOIN media__field_media_image AS mfmi_ogi ON mfmi_ogi.entity_id = nfogi.field_opengraph_image_target_id " +
                "LEFT JOIN media AS media_opengraph ON media_opengraph.mid = mfmi_ogi.entity_id " +
                "LEFT JOIN node__field_twitter_card_image AS nftci ON nftci.entity_id = nfd.nid " +
                "LEFT JOIN media__field_media_image AS mfmi_tci ON mfmi_tci.entity_id = nftci.field_twitter_card_image_target_id " +
                "LEFT JOIN media AS media_twitter ON media_twitter.mid = mfmi_tci.entity_id " +
                "LEFT JOIN node__field_link_on_form AS nflof ON nflof.entity_id = nfd.nid " +
                "LEFT JOIN node__field_origin AS nfo ON nfo.entity_id = nfd.nid " +
                "LEFT JOIN node__field_pardot_link AS nfpl ON nfpl.entity_id = nfd.nid " +
                "LEFT JOIN node__field_recording_link AS nfrl ON nfrl.entity_id = nfd.nid " +
                "LEFT JOIN node__field_registration_link AS nfrgl ON nfrgl.entity_id = nfd.nid " +
                "LEFT JOIN node__field_speaker AS nfsp ON nfsp.entity_id = nfd.nid " +
                "LEFT JOIN node__field_author AS nfa ON nfa.entity_id = nfd.nid " +
                "LEFT JOIN node AS n_author ON nfa.field_author_target_id = n_author.nid " +
                "LEFT JOIN node__field_markets AS nfm ON nfm.entity_id = nfd.nid " +
                "LEFT JOIN taxonomy_term_data AS ttd_markets ON nfm.field_markets_target_id = ttd_markets.tid " +
                "LEFT JOIN node__field_topics AS nftop ON nftop.entity_id = nfd.nid " +
                "LEFT JOIN taxonomy_term_data AS ttd_topics ON nftop.field_topics_target_id = ttd_topics.tid " +
                "LEFT JOIN node__field_open_link AS nfol ON nfol.entity_id = nfd.nid " +
                "LEFT JOIN node__field_colorized AS nfc ON nfc.entity_id = nfd.nid " +
                "LEFT JOIN node__field_display_form AS nfdf ON nfdf.entity_id = nfd.nid " +
                "LEFT JOIN node__field_display_image_on_card AS nfdioc ON nfdioc.entity_id = nfd.nid " +
                "LEFT JOIN node__field_display_read_time AS nfdrt ON nfdrt.entity_id = nfd.nid " +
                "LEFT JOIN node__field_display_related_stories AS nfdrc ON nfdrc.entity_id = nfd.nid " +
                "LEFT JOIN node__field_brand AS nfbrand ON nfbrand.entity_id = nfd.nid " +
                "LEFT JOIN node__field_card_type AS nfct ON nfct.entity_id = nfd.nid " +
                "LEFT JOIN node__field_show_start_time AS nfsst ON nfsst.entity_id = nfd.nid " +
                "LEFT JOIN node__field_seo AS nfs ON nfs.entity_id = nfd.nid " +
                "LEFT JOIN node__field_event_end_date AS nfeed ON nfeed.entity_id = nfd.nid " +
                "LEFT JOIN node__field_event_end_time AS nfeet ON nfeet.entity_id = nfd.nid " +
                "LEFT JOIN node__field_layout_canvas AS nflc ON nflc.entity_id = nfd.nid " +
                "LEFT JOIN node__field_on_demand AS nfod ON nfod.entity_id = nfd.nid " +
                "LEFT JOIN node__field_resource AS nfr ON nfr.entity_id = nfd.nid " +
                "LEFT JOIN node__field_show_map AS nfsm ON nfsm.entity_id = nfd.nid " +
                "LEFT JOIN node__field_subsites AS nfsubsites ON nfsubsites.entity_id = nfd.nid " +
                "LEFT JOIN node__field_display_thumbnail_on_listi AS nfdtol ON nfdtol.entity_id = nfd.nid " +
                "WHERE nfd.type = 'event' " +
                "GROUP BY nfd.nid " +
                ";";
//                "HAVING nfd.nid NOT IN ( " +
//                "SELECT nfd_sub.nid " +
//                "FROM node_field_data AS nfd_sub " +
//                "LEFT JOIN node__field_markets AS nfm_sub ON nfm_sub.entity_id = nfd_sub.nid " +
//                "LEFT JOIN node__field_topics AS nftop_sub ON nftop_sub.entity_id = nfd_sub.nid " +
//                "WHERE nfd_sub.type = 'event' " +
//                "AND ( " +
//                "nfm_sub.field_markets_target_id IN (2231, 2246) " +
//                "OR nftop_sub.field_topics_target_id IN (2231, 2246) " +
//                ") " +
 //               ");";



        CommonFunctionsMigrationUsingDB.exportQueryResultToExcel(JDBC_URL,USERNAME, PASSWORD, schemaName, sqlQuery, fileName);

    }
}
