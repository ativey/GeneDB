package org.genedb.top.querying.tmpquery;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.genedb.top.querying.core.QueryClass;
import org.genedb.top.querying.core.QueryParam;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@QueryClass(
        title="Transcripts by their type",
        shortDesc="Get a list of transcripts by type",
        longDesc=""
    )
public class GeneTypeQuery extends OrganismLuceneQuery {

    @QueryParam(
            order=2,
            title="Type of transcript"
    )
    private String type = "mRNA";


    @Override
    protected String getluceneIndexName() {
        return "org.gmod.schema.mapped.Feature";
    }
    
    @Override
    public String getQueryDescription() {
    	return "Search for genes of a certain type.";
    }
    
    @Override
    public String getQueryName() {
        return "Gene Type";
    }

    @Override
    protected void getQueryTermsWithoutOrganisms(List<org.apache.lucene.search.Query> queries) {
        queries.add(new TermQuery(new Term("type.name", type)));
    }

    @Override
    public Map<String, Object> prepareModelData() {
        Map<String, String> typeMap = new LinkedHashMap<String, String>();
        typeMap.put("mRNA", "protein-coding");
        typeMap.put("pseudogene", "pseudogene");
        typeMap.put("rRNA", "rRNA");
        typeMap.put("snoRNA", "snoRNA");
        typeMap.put("snRNA", "snRNA");
        typeMap.put("tRNA", "tRNA");
        typeMap.put("transcript", "miscRNA");

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("typeMap", typeMap);
        return ret;
    }

    // ------ Autogenerated code below here

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    protected String[] getParamNames() {
        return new String[] {"type"};
    }

}
