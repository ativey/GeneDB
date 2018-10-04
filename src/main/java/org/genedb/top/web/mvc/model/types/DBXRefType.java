package org.genedb.top.web.mvc.model.types;

import org.genedb.top.web.mvc.model.DbXRefDTO;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;

public class DBXRefType {

    private static final Logger logger = LoggerFactory.getLogger(DBXRefType.class);

    private String accession;
    private String dbName;
    private String urlPrefix;

    public DBXRefType(String accession, String dbName, String urlPrefix){
        this.accession = accession;
        this.dbName = dbName;
        this.urlPrefix = urlPrefix;
    }

    public DBXRefType(DbXRefDTO dto) {
        accession = dto.getAccession();
        dbName = dto.getDbName();
        urlPrefix = dto.getUrlPrefix();
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("(");

        sb.append("'");
        sb.append(accession != null ? escape(accession) : "");
        sb.append("',");

        sb.append("'");
        sb.append(dbName != null ? escape(dbName) : "");
        sb.append("',");

        sb.append("'");
        sb.append(urlPrefix != null ? escape(urlPrefix) : "");
        sb.append("'");

        sb.append(")");
        return sb.toString();
    }

    private String escape(String in) {
        logger.error(String.format("About to try and substitute '%s'", in));
        return in.replaceAll("\\(", "_").replaceAll("\\)", "_");
    }
}
