package org.genedb.querying.tmpquery;

import org.genedb.querying.core.QueryClass;
import org.genedb.querying.core.QueryParam;

import org.springframework.validation.Errors;

@QueryClass(
        title="Transcripts by their type",
        shortDesc="Get a list of transcripts by type",
        longDesc=""
    )
public class GeneLocationQuery extends OrganismHqlQuery {

    //private static final Logger logger = Logger.getLogger(GeneLocationQuery.class);

    @QueryParam(
            order=1,
            title="Name of feature"
    )
//    @NotEmpty(message="{topLevelFeatureName.empty}")
    private String topLevelFeatureName;


    @QueryParam(
            order=2,
            title="Min coordinate"
    )
    private int min;


    @QueryParam(
            order=3,
            title="Max coordinate"
    )
    private int max;

    @QueryParam(order = 4, title = "Include pseudogenes")
    private boolean pseudogenes = true;

    @Override
    public String getQueryDescription() {
    	return "Search for a gene by specifing its location on a chromosome, contig or other top level feature.";
    }

    @Override
    public String getQueryName() {
        return "Location";
    }

    @Override
    protected String getHql() {
        StringBuffer sb = new StringBuffer();
        sb.append("select f.uniqueName ");
        sb.append("from Feature f inner join f.featureLocs fl ");
        sb.append("where fl.sourceFeature.uniqueName=:topLevelFeatureName ");
        sb.append("and fl.fmin >= :min ");
        sb.append("and fl.fmax <= :max @ORGANISM@ ");

        if (pseudogenes) {
            sb.append(RESTRICT_TO_TRANSCRIPTS_AND_PSEUDOGENES);
        } else {
            sb.append(RESTRICT_TO_TRANSCRIPTS_ONLY);
        }
        sb.append(" order by f.organism, f.uniqueName");
        return sb.toString();
    }


//    public Map<String, Object> prepareModelData() {
//        Map<String, String> typeMap = new HashMap<String, String>();
//        typeMap.put("mRNA", "protein-coding");
//        typeMap.put("pseudogene", "pseudogenic transcript");
//        typeMap.put("tRNA", "tRNA");
//        typeMap.put("snoRNA", "snoRNA");
//
//        Map<String, Object> ret = new HashMap<String, Object>();
//        ret.put("typeMap", typeMap);
//        return ret;
//    }

    // ------ Autogenerated code below here

    public void setTopLevelFeatureName(String topLevelFeatureName) {
        this.topLevelFeatureName = topLevelFeatureName;
    }

    public String getTopLevelFeatureName() {
        return topLevelFeatureName;
    }


    public int getMin() {
        return min;
    }


    public int getMax() {
        return max;
    }


    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }


    @Override
    protected String[] getParamNames() {
        return new String[] {"topLevelFeatureName", "min", "max"};
    }

    @Override
    protected void populateQueryWithParams(org.hibernate.Query query) {
        super.populateQueryWithParams(query);
        query.setString("topLevelFeatureName", topLevelFeatureName);
        query.setInteger("min", min);
        query.setInteger("max", max);    }


    @Override
    protected void extraValidation(Errors errors) {

        //validate dependent properties
        if (!errors.hasErrors()) {
            if (getMin() > getMax()) {
                errors.reject("start.greater.than.end");
            }
        }
    }


    public boolean isPseudogenes() {
        return pseudogenes;
    }


    public void setPseudogenes(boolean pseudogenes) {
        this.pseudogenes = pseudogenes;
    }

}
