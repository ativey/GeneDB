package org.genedb.top.web.mvc.model.load;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TopLevelFeatureMapper extends FeatureMapper {
    Logger logger = LoggerFactory.getLogger(TopLevelFeatureMapper.class);

    public static final String SQL =
        " select uniquename, f.name as fname, seqlen, cvt.name as cvtname" +
        " from feature f, cvterm cvt " +
        " where f.feature_id = ?" +
        " and f.type_id = cvt.cvterm_id";

    @Override
    public FeatureMapper mapRow(ResultSet rs, int rowNum) throws SQLException {
        //Get transcript details
        FeatureMapper mapper = new FeatureMapper();
        mapper.setUniqueName(rs.getString("uniquename"));
        mapper.setName(rs.getString("fname"));
        mapper.setSeqLen(rs.getInt("seqlen"));
        mapper.setCvtName(rs.getString("cvtname"));
        return mapper;
    }
}
