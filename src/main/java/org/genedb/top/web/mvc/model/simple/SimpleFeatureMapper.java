package org.genedb.top.web.mvc.model.simple;

import org.genedb.top.web.mvc.model.load.FeatureMapper;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SimpleFeatureMapper {
    Logger logger = LoggerFactory.getLogger(FeatureMapper.class);

    public void mapRow(SimpleFeature feature, ResultSet rs) throws SQLException {
        feature.setFeatureId(rs.getInt("feature_id"));
        feature.setUniqueName(rs.getString("uniquename"));
    }

}
