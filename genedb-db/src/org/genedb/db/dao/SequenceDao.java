package org.genedb.db.dao;


import org.genedb.db.helpers.NameLookup;
import org.genedb.db.helpers.Product;
import org.gmod.schema.cv.CvTerm;
import org.gmod.schema.dao.SequenceDaoI;
import org.gmod.schema.organism.Organism;
import org.gmod.schema.sequence.Feature;
import org.gmod.schema.sequence.FeatureCvTerm;
import org.gmod.schema.sequence.FeatureDbXRef;
import org.gmod.schema.sequence.FeatureLoc;
import org.gmod.schema.sequence.FeatureSynonym;
import org.gmod.schema.sequence.Synonym;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SequenceDao extends BaseDao implements SequenceDaoI {
    
 
    /* (non-Javadoc)
     * @see org.genedb.db.dao.SequenceDaoI#getFeatureById(int)
     */
    public Feature getFeatureById(int id) {
        return (Feature) getHibernateTemplate().load(Feature.class, id);
    }

    /* (non-Javadoc)
     * @see org.genedb.db.dao.SequenceDaoI#getFeatureByUniqueName(java.lang.String)
     */
    public Feature getFeatureByUniqueName(String name) {
        List features = getHibernateTemplate().findByNamedParam(
                "from Feature f where f.uniqueName=:name", "name", name);
        if (features.size() > 0) {
            return (Feature) features.get(0);
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.dao.SequenceDaoI#getFeatureByAnyCurrentName(java.lang.String)
     */
    @SuppressWarnings({ "unchecked", "cast" })
    public List<Feature> getFeaturesByAnyCurrentName(String name) {
        List<Feature> features = (List<Feature>) getHibernateTemplate().findByNamedParam(
                "select f from Feature f, FeatureSynonym fs, Synonym s where f=fs.feature and fs.synonym=s and fs.current=true and s.name=:name",
                "name", name);
        return features;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.dao.SequenceDaoI#getFeatureByRange(int, int, int, org.genedb.db.jpa.Feature, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<Feature> getFeaturesByRange(int min,int max,int strand,Feature feat,String type) {
        List<Feature> features;
        int fid = feat.getFeatureId();
        //int min = loc.getMin();
        //int max = loc.getMax();
        //String name = "mRNA";
        features = getHibernateTemplate().findByNamedParam("select f " +
                "from Feature f, FeatureLoc loc, CvTerm cvt where " +
                "f.featureId=loc.featureByFeatureId and f.cvTerm=cvt.cvTermId and cvt.name=:type and loc.strand="+strand+" and" +
                " loc.featureBySrcFeatureId="+fid+" and (" +
                " loc.fmin<=:min and loc.fmax>=:max)",
                new String[]{"type", "min", "max"},
                new Object[]{type, min, max});
        return features;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.dao.SequenceDaoI#getFeatureByAnyName(org.genedb.db.helpers.NameLookup, java.lang.String)
     */
    @SuppressWarnings({ "unchecked", "cast" })
    public List<Feature> getFeaturesByAnyName(String nl,String featureType) {

        if (!nl.startsWith("*")) {
            nl = "*" + nl;
        }
        if (!nl.endsWith("*")) {
            nl += "*";
        }

        String lookup = nl.replaceAll("\\*", "%");

        logger.info("lookup is " + lookup);
        List<Feature> features = (List<Feature>)
        //getHibernateTemplate().find
        getHibernateTemplate().findByNamedParam("select f from Feature f, FeatureSynonym fs, Synonym s, CvTerm cvt where f=fs.feature and fs.synonym=s and fs.current=true and f.cvTerm=cvt.cvTermId and cvt.name='" + featureType + "' and s.name like :lookup",
                "lookup", lookup);
        return features;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.dao.SequenceDaoI#getFeatureCvTermByFeatureAndCvTerm(org.genedb.db.jpa.Feature, org.genedb.db.hibernate3gen.CvTerm, boolean)
     */
    @SuppressWarnings("unchecked")
    public FeatureCvTerm getFeatureCvTermByFeatureAndCvTerm(Feature feature, CvTerm cvTerm, boolean not) {
        List<FeatureCvTerm> list = getHibernateTemplate().findByNamedParam("from FeatureCvTerm fct where fct.feature=:feature and fct.cvTerm=:cvTerm and fct.not=:not", 
                new String[]{"feature", "cvTerm", "not"}, 
                new Object[]{feature, cvTerm, not});

        return firstFromList(list, "feature", feature.getUniqueName(), "cvTerm", cvTerm, "not", not);
    }

    /* (non-Javadoc)
     * @see org.genedb.db.dao.SequenceDaoI#getSynonymsByNameAndCvTerm(java.lang.String, org.genedb.db.hibernate3gen.CvTerm)
     */
    @SuppressWarnings("unchecked")
    public Synonym getSynonymByNameAndCvTerm(String name, CvTerm type) {
        List<Synonym> tmp = getHibernateTemplate().findByNamedParam(
                "from Synonym s where s.name=:name and s.cvTerm=:cvterm",
                new String[] {"name", "cvterm"},
                new Object[] {name, type});

        return firstFromList(tmp, "name", name, "cvterm", type.getName());
    }


    /* (non-Javadoc)
     * @see org.genedb.db.dao.SequenceDaoI#getFeatureSynonymsByFeatureAndSynonym(org.genedb.db.jpa.Feature, org.genedb.db.hibernate3gen.Synonym)
     */
    @SuppressWarnings("unchecked")
    public List<FeatureSynonym> getFeatureSynonymsByFeatureAndSynonym(Feature feature, Synonym synonym) {
        return getHibernateTemplate().findByNamedParam(
                "from FeatureSynonym fs where fs.feature=:feature and fs.synonym=:synonym",
                new String[] {"feature", "synonym"},
                new Object[] {feature, synonym});
    }

    @SuppressWarnings("unchecked")
    public List<Feature> getFeaturesByLocatedOnFeature(Feature parent) {
        List<Feature> features;
        //int fid = parent.getFeatureId();
        features = getHibernateTemplate().findByNamedParam("select f " +
                "from Feature f, FeatureLoc loc, CvTerm cvt where " +
                "f.featureId=loc.featureByFeatureId and" +
                " loc.featureBySrcFeatureId=:parent",
                new String[]{"parent"},
                new Object[]{parent});
        return features;
    }

    @SuppressWarnings("unchecked")
    public List<FeatureDbXRef> getFeatureDbXRefsByFeatureUniquename(String uniqueName) {
        if (uniqueName == null) {
            return getHibernateTemplate().find("select from FeatureDbXRef");
        }
        return getHibernateTemplate().findByNamedParam(
                "from FeatureDbXRef fdxr where fdxr.feature.uniqueName=:uniqueName",
                new String[] {"uniqueName"},
                new Object[] {uniqueName});
    }

    @SuppressWarnings("unchecked")
    public List<FeatureSynonym> getFeatureSynonymsByFeatureUniquename(String uniqueName) {
        if (uniqueName == null) {
            return getHibernateTemplate().find("select from FeatureSynonym");
        }
        return getHibernateTemplate().findByNamedParam(
                "from FeatureSynonym fs where fs.feature.uniqueName=:uniqueName",
                new String[] {"uniqueName"},
                new Object[] {uniqueName});
    }

    @SuppressWarnings("unchecked")
    public List<List> getFeatureByGO(String go) {
    	String temp[] = go.split(":");
    	String number = temp[1];
    	List <Feature> polypeptides;
    	List <CvTerm> goName;
    	List <Feature> features = new ArrayList<Feature>();
    	polypeptides = getHibernateTemplate().findByNamedParam("select f " +
	    			"from Feature f, DbXRef d, CvTerm c, FeatureCvTerm fc where " +
	    			"d.accession=:number and d.dbXRefId=c.dbXRef and c.cvTermId=fc.cvTerm " +
	    			"and fc.feature=f.featureId",
    			new String[]{"number"},
    			new Object[]{number});
		for (Feature polypep : polypeptides) {
			logger.info(polypep.getUniqueName());
			List<Feature> genes = getHibernateTemplate().findByNamedParam("select f " +
					"from Feature f,FeatureRelationship f1,FeatureRelationship f2 where " +
					"f2.featureBySubjectId=:polypep and f2.featureByObjectId=f1.featureBySubjectId " +
					"and f1.featureByObjectId=f",
					new String[]{"polypep"},
					new Object[]{polypep});
			features.add(genes.get(0));
		}
		goName = getHibernateTemplate().findByNamedParam("select cv " +
				"from CvTerm cv where cv.dbXRef.accession=:number", new String[]{"number"}, new Object[]{number});
		
		List <Feature> flocs = new ArrayList<Feature>();
		String name = "chromosome";
		flocs = getHibernateTemplate().findByNamedParam("select f from Feature f " +
				"where f.cvTerm.name=:name",
				new String[]{"name"},
				new Object[]{name});
		List <List> data = new ArrayList<List>();
		data.add(features);
		data.add(flocs);
		data.add(goName);
    	return data;
	}

	@SuppressWarnings("unchecked")
	public List<Feature> getFeaturesByAnyNameAndOrganism(String nl, List<String> ids,String featureType) {
		
		
		if (ids == null || ids.size()==0 ) {
			logger.info("nl.getOrglist is null therefore calling featuresbyname");
			return(getFeaturesByAnyName(nl,featureType));
		}
		
		List<Feature> features = new ArrayList<Feature>();

        if (!nl.startsWith("*")) {
            nl = "*" + nl;
        }
        if (!nl.endsWith("*")) {
            nl += "*";
        }

        String lookup = nl.replaceAll("\\*", "%");
        String orglist = ids.get(0);
         
        /*
        StringBuffer ids = new StringBuffer();
        for (int i=0; i<organisms.size(); i++) {
			if (i+1 == organisms.size()) {
				ids.append(organisms.get(i).getOrganismId());
			} else {
				ids.append(organisms.get(i).getOrganismId());
				ids.append(",");
			}
		}*/
        //String id = Integer.toString(organism.getOrganismId());
        logger.info("id is " + orglist);
        logger.info("calling the right query...");
        features = getHibernateTemplate().findByNamedParam("select f from Feature f where" +
        		" f.uniqueName like :lookup and f.cvTerm.name=:featureType and f.organism.commonName in (:orglist)", 
        		new String[]{"lookup","featureType","orglist"}, new Object[]{lookup,featureType,orglist});
		return features;
	}

	public List<Product> getProducts() {
		List<Product> products = new ArrayList<Product>();
		Iterator results = getHibernateTemplate().find("select cvt.name,count(f.uniqueName) from CvTerm cvt,FeatureCvTerm fct,Feature f " +
				"where f=fct.feature and cvt=fct.cvTerm and cvt.cv=15 group by cvt.name").listIterator();
		while (results.hasNext()){
			Product p = new Product();
			Object[] row = (Object[]) results.next();
			p.setName((String)row[0]);
			p.setCount((Integer)row[1]);
			products.add(p);
		}
		return products;
	}

	public List<Feature> getFeaturesByCvTermName(String cvTermName) {
		List<Feature> features = getHibernateTemplate().findByNamedParam(
				"select f.feature from FeatureCvTerm f where f.cvTerm.name like :cvTermName", 
				"cvTermName", cvTermName);
		return features;
	}

	public List<Feature> getTopLevelFeatures(){
		String name = "chromosome%";
		List<Feature> topLevels = getHibernateTemplate().findByNamedParam("select f from Feature f " +
				"where f.cvTerm.name like :name",
				"name",
				name);
		return topLevels;
	}
}
