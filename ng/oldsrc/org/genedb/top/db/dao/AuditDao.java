package org.genedb.top.db.dao;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import java.sql.Date;

import javax.sql.DataSource;

public class AuditDao {

    private static final Logger logger = LoggerFactory.getLogger(AuditDao.class);

    private SimpleJdbcTemplate simpleJdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
    }


    public Date getLastChangeForExistingFeature(int featureId) {

        Date updated = simpleJdbcTemplate.queryForObject(
                "select timelastmodified from audit.feature_updated where feature_id=:featureId",
                Date.class,
                featureId);

        Date added = simpleJdbcTemplate.queryForObject(
                "select timelastmodified from audit.feature_added where feature_id=:featureId",
                Date.class,
                featureId);

        if (added != null) {
            if (updated == null) {
                return added;
            }
            return (added.after(updated)) ? added: updated;
        }

        return updated;
    }

}
