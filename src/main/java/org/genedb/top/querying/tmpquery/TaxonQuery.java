package org.genedb.top.querying.tmpquery;

import org.genedb.top.db.taxon.TaxonNode;
import org.genedb.top.db.taxon.TaxonNodeList;

public interface TaxonQuery {

    public abstract TaxonNodeList getTaxons();

    public abstract void setTaxons(TaxonNodeList taxons);

}