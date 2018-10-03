package org.genedb.top.web.mvc.model;

import org.genedb.top.chado.mapped.DbXRef;

import java.io.Serializable;


//@XStreamAlias("dbxref")
public class DbXRefDTO implements Serializable {
    private String dbName;
    private String accession;
    private String urlPrefix;

    public DbXRefDTO(String dbName, String accession, String urlPrefix) {
        this.dbName = dbName;
        this.accession = accession;
        this.urlPrefix = urlPrefix;
    }

    public DbXRefDTO(DbXRef dbXRef) {
        this(dbXRef.getDb().getName(), dbXRef.getAccession(), dbXRef.getDb().getUrlPrefix());
    }

    public String getDbName() {
        return dbName;
    }

    public String getAccession() {
        return accession;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

}
