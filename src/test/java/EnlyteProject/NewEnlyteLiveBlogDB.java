package EnlyteProject;

public class NewEnlyteLiveBlogDB {
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/newenlyteschema";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    public static void main(String[] args) throws Exception {
        String schemaName = "newenlyteschema";
        String fileName = "New Enlyte All Blogs Sheet_";
        String sqlQuery = "SELECT " +
                "nfd.nid, " +
                "nfd.vid, " +
                "nfd.uid, " +
                "nfd.status, " +
                "nfd.title, " +
                "nfd.created, " +
                "nfd.changed, " +
                "nfd.promote, " +
                "nfd.sticky, " +
                "nfd.publish_on, " +
                "nfd.unpublish_on, " +
                "n.uuid AS node__uuid, " +
                "pa.alias, " +
                "nfb.body_value, " +
                "nfb.body_summary, " +
                "nfb.body_format, " +
                "nfbp.field_brand_path_value, " +
                "nfdl.field_document_link_value, " +
                "nfell.field_external_link_label_value, " +
                "nffd.field_form_description_value, " +
                "nffd.field_form_description_format, " +
                "nffn.field_form_newsletter_value, " +
                "nfft.field_form_title_value, " +
                "nffcl.field_full_content_link_value, " +
                "nfol.field_old_url_value, " +
                "media_hero.uuid AS node__field_hero, " +
                "media_hero_image.uuid AS node__field_hero_image, " +
                "media_hero_mobile.uuid AS node__field_hero_mobile, " +
                "media_opengraph.uuid AS node__field_opengraph_image, " +
                "media_twitter.uuid AS node__field_twitter_card_image, " +
                "media_video.uuid AS field_video_file_uuid, " +
                "media_oembed.uuid AS field_video_oembed_uuid, " +
                "nfac.field_additional_content_uri, " +
                "nfac.field_additional_content_title, " +
                "nfr.field_resource_uri, " +
                "nfr.field_resource_title, " +
                "nflof.field_link_on_form_uri, " +
                "nflof.field_link_on_form_title, " +
                "nflmc.field_link_to_main_content_uri, " +
                "nflmc.field_link_to_main_content_title, " +
                "nfo.field_origin_uri, " +
                "nfo.field_origin_title, " +
                "nfpu.field_pdf_url_uri, " +
                "nfpu.field_pdf_url_title, " +
                "GROUP_CONCAT(n_author.uuid SEPARATOR ',') AS field_author_uuids, " +
                "GROUP_CONCAT(ttd_markets.uuid SEPARATOR ',') AS field_markets_uuids, " +
                "GROUP_CONCAT(ttd_topics.uuid SEPARATOR ',') AS field_topics_uuids, " +
                "nff.field_feature_value, " +
                "nfc.field_colorized_value, " +
                "nfdf.field_display_form_value, " +
                "nfdioc.field_display_image_on_card_value, " +
                "nfdrt.field_display_read_time_value, " +
                "nfdrc.field_display_related_stories_value, " +
                "nfdtol.field_display_thumbnail_on_listi_value, " +
                "nfel.field_external_link_value, " +
                "nfbrand.field_brand_value, " +
                "nfct.field_card_type_value, " +
                "fmdoc.uuid AS field_document_uuid, " +
                "nfs.field_seo_value " +
                "FROM " +
                "node_field_data nfd " +
                "LEFT JOIN node n ON n.nid = nfd.nid " +
                "LEFT JOIN path_alias pa ON CONCAT('/node/', nfd.nid) = pa.path " +
                "LEFT JOIN node__body nfb ON nfb.entity_id = nfd.nid " +
                "LEFT JOIN node__field_brand_path nfbp ON nfbp.entity_id = nfd.nid " +
                "LEFT JOIN node__field_document_link nfdl ON nfdl.entity_id = nfd.nid " +
                "LEFT JOIN node__field_external_link_label nfell ON nfell.entity_id = nfd.nid " +
                "LEFT JOIN node__field_form_description nffd ON nffd.entity_id = nfd.nid " +
                "LEFT JOIN node__field_form_newsletter nffn ON nffn.entity_id = nfd.nid " +
                "LEFT JOIN node__field_form_title nfft ON nfft.entity_id = nfd.nid " +
                "LEFT JOIN node__field_full_content_link nffcl ON nffcl.entity_id = nfd.nid " +
                "LEFT JOIN node__field_old_url nfol ON nfol.entity_id = nfd.nid " +
                "LEFT JOIN node__field_hero nfh ON nfh.entity_id = nfd.nid " +
                "LEFT JOIN media__field_media_image mfmi_h ON mfmi_h.entity_id = nfh.field_hero_target_id " +
                "LEFT JOIN media media_hero ON media_hero.mid = mfmi_h.entity_id " +
                "LEFT JOIN node__field_hero_image nfhi ON nfhi.entity_id = nfd.nid " +
                "LEFT JOIN media__field_media_image mfmi_hi ON mfmi_hi.entity_id = nfhi.field_hero_image_target_id " +
                "LEFT JOIN media media_hero_image ON media_hero_image.mid = mfmi_hi.entity_id " +
                "LEFT JOIN node__field_hero_mobile nfhm ON nfhm.entity_id = nfd.nid " +
                "LEFT JOIN media__field_media_image mfmi_hm ON mfmi_hm.entity_id = nfhm.field_hero_mobile_target_id " +
                "LEFT JOIN media media_hero_mobile ON media_hero_mobile.mid = mfmi_hm.entity_id " +
                "LEFT JOIN node__field_opengraph_image nfogi ON nfogi.entity_id = nfd.nid " +
                "LEFT JOIN media__field_media_image mfmi_ogi ON mfmi_ogi.entity_id = nfogi.field_opengraph_image_target_id " +
                "LEFT JOIN media media_opengraph ON media_opengraph.mid = mfmi_ogi.entity_id " +
                "LEFT JOIN node__field_twitter_card_image nftci ON nftci.entity_id = nfd.nid " +
                "LEFT JOIN media__field_media_image mfmi_tci ON mfmi_tci.entity_id = nftci.field_twitter_card_image_target_id " +
                "LEFT JOIN media media_twitter ON media_twitter.mid = mfmi_tci.entity_id " +
                "LEFT JOIN node__field_video nfv ON nfv.entity_id = nfd.nid " +
                "LEFT JOIN media__field_media_video_file mfmv ON mfmv.entity_id = nfv.field_video_target_id " +
                "LEFT JOIN media media_video ON media_video.mid = mfmv.entity_id " +
                "LEFT JOIN media__field_media_oembed_video mfov ON mfov.entity_id = nfv.field_video_target_id " +
                "LEFT JOIN media media_oembed ON media_oembed.mid = mfov.entity_id " +
                "LEFT JOIN node__field_additional_content nfac ON nfac.entity_id = nfd.nid " +
                "LEFT JOIN node__field_resource nfr ON nfr.entity_id = nfd.nid " +
                "LEFT JOIN node__field_link_on_form nflof ON nflof.entity_id = nfd.nid " +
                "LEFT JOIN node__field_link_to_main_content nflmc ON nflmc.entity_id = nfd.nid " +
                "LEFT JOIN node__field_origin nfo ON nfo.entity_id = nfd.nid " +
                "LEFT JOIN node__field_pdf_url nfpu ON nfpu.entity_id = nfd.nid " +
                "LEFT JOIN node__field_author nfa ON nfa.entity_id = nfd.nid " +
                "LEFT JOIN node n_author ON nfa.field_author_target_id = n_author.nid " +
                "LEFT JOIN node__field_markets nfm ON nfm.entity_id = nfd.nid " +
                "LEFT JOIN taxonomy_term_data ttd_markets ON nfm.field_markets_target_id = ttd_markets.tid " +
                "LEFT JOIN node__field_topics nftop ON nftop.entity_id = nfd.nid " +
                "LEFT JOIN taxonomy_term_data ttd_topics ON nftop.field_topics_target_id = ttd_topics.tid " +
                "LEFT JOIN node__field_feature nff ON nff.entity_id = nfd.nid " +
                "LEFT JOIN node__field_colorized nfc ON nfc.entity_id = nfd.nid " +
                "LEFT JOIN node__field_display_form nfdf ON nfdf.entity_id = nfd.nid " +
                "LEFT JOIN node__field_display_image_on_card nfdioc ON nfdioc.entity_id = nfd.nid " +
                "LEFT JOIN node__field_display_read_time nfdrt ON nfdrt.entity_id = nfd.nid " +
                "LEFT JOIN node__field_display_related_stories nfdrc ON nfdrc.entity_id = nfd.nid " +
                "LEFT JOIN node__field_display_thumbnail_on_listi nfdtol ON nfdtol.entity_id = nfd.nid " +
                "LEFT JOIN node__field_external_link nfel ON nfel.entity_id = nfd.nid " +
                "LEFT JOIN node__field_brand nfbrand ON nfbrand.entity_id = nfd.nid " +
                "LEFT JOIN node__field_card_type nfct ON nfct.entity_id = nfd.nid " +
                "LEFT JOIN node__field_document nfdc ON nfdc.entity_id = nfd.nid " +
                "LEFT JOIN file_managed fmdoc ON fmdoc.fid = nfdc.field_document_target_id " +
                "LEFT JOIN node__field_seo nfs ON nfs.entity_id = nfd.nid " +
                "LEFT JOIN taxonomy_index ti ON ti.nid = nfd.nid " +
                "WHERE " +
                "nfd.type = 'blog' " +
                "GROUP BY " +
                "nfd.nid;";
        CommonFunctionsMigrationUsingDB.exportQueryResultToExcel(JDBC_URL,USERNAME, PASSWORD, schemaName, sqlQuery, fileName);
    }
}
