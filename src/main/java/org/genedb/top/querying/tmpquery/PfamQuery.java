package org.genedb.top.querying.tmpquery;

import org.genedb.top.querying.core.QueryClass;
import org.genedb.top.querying.core.QueryParam;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.BooleanClause.Occur;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@QueryClass(
        title="Coding and pseudogenes by protein length",
        shortDesc="Get a list of transcripts ",
        longDesc=""
    )
public class PfamQuery extends OrganismLuceneQuery {

    private static Logger logger = LoggerFactory.getLogger(PfamQuery.class);
    
    @QueryParam(
            order=1,
            title="The search string"
    )
    private String search = "";


    @Override
    protected String getluceneIndexName() {
        return "org.gmod.schema.mapped.Feature";
    }
    
    @Override
    public String getQueryDescription() {
    	return "Search for pfam domains.";
    }
    
    @Override
    public String getQueryName() {
        return "Pfam";
    }
    
    // we're only interested in numbers here
    Pattern pattern = Pattern.compile("[^0-9]");
    
    @Override
    protected void getQueryTermsWithoutOrganisms(List<org.apache.lucene.search.Query> queries) {
        //String tokens[] = search.trim().split("\\s");
        
        BooleanQuery bq = new BooleanQuery();
        
        String tokens[] = search.trim().split("\\s");
        
        for (String token : tokens) {
            
            // by default, we searching exact matches to the description or the accession
            String searchQueryString = token.toLowerCase().trim();
            
            // if there is a ":" then, there could be a number of prefixes, let's only get the numerical part
            if (searchQueryString.contains(":")) {
                
                // let's get rid of anything that's NaN
                Matcher regexMatcher = pattern.matcher(searchQueryString);
                searchQueryString = regexMatcher.replaceAll("");
                
                //logger.debug(String.format("searchQueryString: '%s'" , searchQueryString));
            }
            
            TermQuery q = new TermQuery (new Term("pfam", searchQueryString));
            
            bq.add(q, Occur.MUST);
            
        }
        
        
        
        
        
        
        
        
//        BooleanQuery bq = new BooleanQuery();
//
//        if (tokens.length > 1) {
//            PhraseQuery pq = new PhraseQuery();
//            for (String token : tokens) {
//                pq.add(new Term("pfam", token));
//            }
//            bq.add(pq, Occur.SHOULD);
//
//        } else {
//            if (search.indexOf('*') == -1) {
//                bq.add(new TermQuery(new Term("pfam",search.toLowerCase())), Occur.SHOULD);
//            } else {
//                bq.add(new WildcardQuery(new Term("pfam", search.toLowerCase())), Occur.SHOULD);
//            }
//        }

        queries.add(bq);

    }

    // ------ Autogenerated code below here

    public void setSearch(String search) {
        this.search = search;
    }

    public String getSearch() {
        return search;
    }

    @Override
    protected String[] getParamNames() {
        return new String[] {"search", "product", "allNames", "pseudogenes"};
    }

}
