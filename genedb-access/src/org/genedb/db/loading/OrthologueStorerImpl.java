/*
 * Copyright (c) 2006 Genome Research Limited.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published
 * by  the Free Software Foundation; either version 2 of the License or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this program; see the file COPYING.LIB.  If not, write to
 * the Free Software Foundation Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307 USA
 */

package org.genedb.db.loading;

import org.genedb.db.dao.CvDao;
import org.genedb.db.dao.OrganismDao;
import org.genedb.db.dao.PubDao;
import org.genedb.db.dao.SequenceDao;

import org.gmod.schema.analysis.Analysis;
import org.gmod.schema.analysis.AnalysisFeature;
import org.gmod.schema.cv.CvTerm;
import org.gmod.schema.general.Db;
import org.gmod.schema.general.DbXRef;
import org.gmod.schema.organism.Organism;
import org.gmod.schema.sequence.Feature;
import org.gmod.schema.sequence.FeatureDbXRef;
import org.gmod.schema.sequence.FeatureLoc;
import org.gmod.schema.sequence.FeatureProp;
import org.gmod.schema.sequence.FeatureRelationship;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.biojava.bio.Annotation;
import org.biojava.bio.gui.sequence.PairwiseOverlayRenderer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;



/**
 * This class is the loads up orthologue data into GeneDB.
 *
 * Usage: OrthologueStorer orthologue_file [orthologue_file ...]
 *
 *
 * @author Adrian Tivey (art)
 */
public class OrthologueStorerImpl implements OrthologueStorer {

    private static String usage="OrthologueStorer orthologue_file";

    protected static final Log logger = LogFactory.getLog(OrthologueStorerImpl.class);

	private static CvTerm PARALOGOUS_RELATIONSHIP;

	private static CvTerm ORTHOLOGOUS_RELATIONSHIP;
	
	private List<String> clusterNames = new ArrayList<String>();

//    private FeatureHandler featureHandler;
//
//    private RunnerConfig runnerConfig;
//
//    private RunnerConfigParser runnerConfigParser;
//
//    private Set<String> noInstance = new HashSet<String>();
//
    private FeatureUtils featureUtils;
//
//    private Organism organism;
//
//    private ApplicationContext applicationContext;
//
    private SequenceDao sequenceDao;

    private OrganismDao organismDao;

    private CvDao cvDao;
//    
    private PubDao pubDao;
//
//    private GeneralDao generalDao;
//    
    private HibernateTransactionManager hibernateTransactionManager;
    
    private SessionFactory sessionFactory;
    
    private Organism DUMMY_ORG;
//
//    Map<String,String> cdsQualifiers = new HashMap<String,String>();
//    
//	private Set<String> handeledQualifiers = new HashSet<String>();
//    
//    private OrthologueStorage orthologueStorage = new OrthologueStorage();
    
    
    public void setHibernateTransactionManager(
			HibernateTransactionManager hibernateTransactionManager) {
		this.hibernateTransactionManager = hibernateTransactionManager;
	}




    public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}




	/**
     * Main entry point. It uses a BeanPostProcessor to apply a set of overrides
     * based on a Properties file, based on the organism. This is passed in on
     * the command-line.
     *
     * @param args organism_common_name, [conf file path]
     * @throws XMLStreamException 
     * @throws FileNotFoundException 
     */
    public static void main (String[] args) throws FileNotFoundException, XMLStreamException {

        String[] filePaths = args;

        if (filePaths.length == 0) {
        	System.err.println("No input files specified");
        	System.exit(-1);
        }
        
        // Override properties in Spring config file (using a
        // BeanFactoryPostProcessor) based on command-line args
        Properties overrideProps = new Properties();
        overrideProps.setProperty("dataSource.username", "chado");
        //overrideProps.setProperty("runner.organismCommonName", organismCommonName);
        //overrideProps.setProperty("runnerConfigParser.organismCommonName", organismCommonName);

//        if (configFilePath != null) {
//            overrideProps.setProperty("runnerConfigParser.configFilePath", configFilePath);
//        }


        PropertyOverrideHolder.setProperties("dataSourceMunging", overrideProps);


        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                new String[] {"NewRunner.xml"});

        OrthologueStorerImpl ostore = (OrthologueStorerImpl) ctx.getBean("ostore", OrthologueStorerImpl.class);
        ostore.afterPropertiesSet();
        long start = new Date().getTime();
        for (int i = 0; i < filePaths.length; i++) {
			File input = new File(filePaths[i]);
	        ostore.afterPropertiesSet();
			ostore.process(input);
		}
        ostore.writeToDb();
//      long duration = (new Date().getTime()-start)/1000;
//      logger.info("Processing completed: "+duration / 60 +" min "+duration  % 60+ " sec.");
    }

    public void writeToDb() {
    	
    	System.err.println("orthologues='"+orthologues.size()+"' paralogues='"+paralogues.size()+"' cluster keys='"+clusters.keySet().size()+"'");
    	System.err.println("cvDao is '"+this.cvDao+"'");
    	
    	CvTerm ORTHOLOGOUS_TO = cvDao.getCvTermByNameAndCvName("orthologous_to", "sequence");
    	CvTerm PARALOGOUS_TO = cvDao.getCvTermByNameAndCvName("paralogous_to", "sequence");
    	
		for (GenePair pair : orthologues) {
			//storePairs(pair, ORTHOLOGOUS_TO);
		}
		for (GenePair pair : paralogues) {
			//storePairs(pair, PARALOGOUS_TO);
		}
		for (Map.Entry<String, List<String>> cluster : clusters.entrySet()) {
			storeCluster(cluster, ORTHOLOGOUS_TO);
			//System.err.println(clusterName);
		}
		
		finishClusterHandling();
		
		
//		TransactionTemplate tt = hibernateTransactionManager.
//      tt.execute(
//		  new TransactionCallbackWithoutResult() {
//			  @Override
//			  public void doInTransactionWithoutResult(TransactionStatus status) {
//				  finishClusterHandling();
//			  }
//		  });
		
	}


	private void storePairs(GenePair pair, CvTerm relationship) {
    	if (pair.getFirst().equals(pair.getSecond())) {
    		System.err.println("Skipping storing '"+pair.getFirst()+"' as an ortho/paralogue of itself");
    		return;
    	}
    	Feature gene1 = sequenceDao.getFeatureByUniqueName(pair.getFirst()+":pep", "polypeptide");
    	if (gene1 == null) {
    		System.err.println("Failing lookup for '"+pair.getFirst()+"'");
    		return;
    	}
    	Feature gene2 = sequenceDao.getFeatureByUniqueName(pair.getSecond()+":pep", "polypeptide");
    	if (gene2 == null) {
    		System.err.println("Failing lookup for '"+pair.getSecond()+"'");
    		return;
    	}
    	FeatureRelationship fr = new FeatureRelationship(gene1, gene2, relationship, 0);
    	sequenceDao.persist(fr);
    	
    	// Need to store camouflage - a parent feature so it looks like a cluster
    	String uniqueName = "ORTHO_PARA_" +pair.getFirst() + "_" + pair.getSecond();

    	Feature matchFeature = featureUtils.createFeature("protein_match", uniqueName, DUMMY_ORG);
    	sequenceDao.persist(matchFeature);
    	fr = new FeatureRelationship(gene1, matchFeature, relationship, 0);
    	sequenceDao.persist(fr);
    	fr = new FeatureRelationship(gene2, matchFeature, relationship, 0);
    	sequenceDao.persist(fr);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void finishClusterHandling() {
		Session session = sessionFactory.openSession();
		//Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		//hibernateTransactionManager.getTransaction(new SimpleTransactionStatus());
		//Transaction transaction = session.beginTransaction();
    	//System.err.println(session);
    	//StatelessSession session = sessionFactory.openStatelessSession();
    	//Transaction transaction = session.beginTransaction();
		for (String clusterName : clusterNames) {
			System.err.println(clusterName);
	    	Feature matchFeature = sequenceDao.getFeatureByUniqueName(clusterName, "protein_match");
			Collection<FeatureRelationship> frs = matchFeature.getFeatureRelationshipsForSubjectId();
			System.err.println("There are '"+frs.size()+"' members for cluster '"+clusterName+"'");
		}
		//transaction.commit();
		session.close();
	}
	
    private void storeCluster(Map.Entry<String, List<String>> entry, CvTerm relationship) {
    	String clusterName = entry.getKey();
    	String uniqueName = "CLUSTER_" +clusterName;

    	
    	Feature matchFeature = sequenceDao.getFeatureByUniqueName(uniqueName, "protein_match");
    	if (matchFeature == null) {
    		matchFeature = featureUtils.createFeature("protein_match", uniqueName, DUMMY_ORG);
        	sequenceDao.persist(matchFeature);
        	clusterNames.add(uniqueName);
    	}
    	clusterNames.add(uniqueName);
    	List<String> genes = entry.getValue();
    	for (String geneName : genes) {
        	Feature gene = sequenceDao.getFeatureByUniqueName(geneName+":pep", "polypeptide");
        	if (gene == null) {
        		System.err.println("Failing lookup for '"+geneName+"'");
        		return;
        	}
        	FeatureRelationship fr = new FeatureRelationship(gene, matchFeature, relationship, 0);
        	//sequenceDao.persist(fr);
		}
    	
    }

//    private void createSimilarity(Feature polypeptide, Feature transcript, Annotation an) {
//
//        String cv = "genedb_misc";
//        List<SimilarityInstance> similarities = this.siParser.getAllSimilarityInstance(an);
//        int count = 0;
//        if (similarities.size() > 0)  {
//            for (SimilarityInstance si : similarities) {
//
//                count++;
//
//                Feature queryFeature = null;
//                String cvTerm = null;
//
//                    queryFeature = polypeptide;
//                    cvTerm = "protein_match";
//                /* look for analysis and create new if one does not already exists */
//
//                Analysis analysis = null;
//                analysis = generalDao.getAnalysisByProgram(si.getAlgorithm());
//                if (analysis == null){
//                    analysis = new Analysis();
//                    analysis.setAlgorithm(si.getAlgorithm());
//                    analysis.setProgram(si.getAlgorithm());
//                    analysis.setProgramVersion("1.0");
//                    analysis.setSourceName(si.getAlgorithm());
//                    Date epoch = new Date(0);
//                    analysis.setTimeExecuted(epoch);
//                    generalDao.persist(analysis);
//                }
//
//                /* create match feature 
//                 * create new dbxref for match feature if one does not already exsists 
//                 */ 
//                Feature matchFeature = null;
//                String uniqueName = null;
//
//                uniqueName = "MATCH_" + queryFeature.getUniqueName() + "_" + count;
//
//                matchFeature = this.featureUtils.createFeature(cvTerm, uniqueName, organism);
//                this.sequenceDao.persist(matchFeature);
//
//                CvTerm uId = this.cvDao.getCvTermByNameAndCvName("ungapped id",cv );
//                FeatureProp ungappedId = new FeatureProp(matchFeature,uId,si.getUngappedId(),0);
//                this.sequenceDao.persist(ungappedId);
//
//                CvTerm olap = this.cvDao.getCvTermByNameAndCvName("overlap", cv);
//                FeatureProp overlap = new FeatureProp(matchFeature,olap,si.getOverlap(),0);
//                this.sequenceDao.persist(overlap);
//
//                /* create analysisfeature 
//                 * 
//                 */
//                Double score = null;
//                if (si.getScore() != null) {
//                    score = Double.parseDouble(si.getScore());
//                } 
//
//                Double evalue = null;
//                if (si.getEvalue() != null) {
//                    evalue = Double.parseDouble(si.getEvalue());
//                } 
//
//                Double id = null;
//                if (si.getId() != null) {
//                    id = Double.parseDouble(si.getId());
//                } 
//
//                AnalysisFeature analysisFeature = new AnalysisFeature(analysis,matchFeature,0.0,score,evalue,id);
//                this.generalDao.persist(analysisFeature);
//
//                /* create subject feature if one does not already exists. If two database are 
//                 * referenced; seperate the primary and the secondary. Create feature.dbxref 
//                 * for primary and featuredbxref for secondary. Also add organism, product, gene, 
//                 * overlap and ungappedid as featureprop to this feature. Create featureloc from 
//                 * subject XX-XXX aa and link it to matchFeature. set the rank of src_feature_id 
//                 * of featureloc to 0. 
//                 */
//                Feature subjectFeature = null;
//                
//                String sections[] = parseDbString(si.getPriDatabase());
//                String values[] = parseDbString(si.getSecDatabase());
//                if (sections[0].equals("SWALL") && sections[1].contains("_")) {
//                    subjectFeature = this.sequenceDao.getFeatureByUniqueName("UniProt:"+values[1],"region");
//                } else if(sections[0].equals("SWALL")){
//                    subjectFeature = this.sequenceDao.getFeatureByUniqueName("UniProt:"+sections[1],"region");
//                } else {
//                    subjectFeature = this.sequenceDao.getFeatureByUniqueName(si.getPriDatabase(),"region");
//                }
//                if (subjectFeature == null) {
//                    subjectFeature = this.sequenceDao.getFeatureByUniqueName(si.getSecDatabase(),"region");
//                }
//
//                if (subjectFeature == null) {
//
//                    /* hmm...looks like encountered this for the first time so create
//                     * subject feature
//                     */
//
//                    DbXRef dbXRef = null;
//
//
//
//                    String priDatabase = sections[0];
//                    String priId = sections[1];
//
//                    String secDatabase = values[0];
//                    String secId = values[1];
//
//                    String accession = null;
//                    uniqueName = null;
//
//                    Db db = null;
//                    if (priDatabase.equals("SWALL")){
//                        db = this.generalDao.getDbByName("UniProt");
//                    } else {
//                        db = this.generalDao.getDbByName(priDatabase);
//                    }
//
//                    if (priDatabase.equals(secDatabase)) {
//                        if (priId.contains("_")) {
//                            accession = secId;
//                        } else {
//                            accession = priId;
//                        }
//                        if (priDatabase.equals("SWALL")) {
//                            priDatabase = "UniProt";
//                        }
//                        uniqueName = priDatabase + ":" + accession;
//                        subjectFeature = this.featureUtils.createFeature("region", uniqueName, organism);
//
//                        dbXRef = this.generalDao.getDbXRefByDbAndAcc(db, accession);
//                        if (dbXRef == null) {
//                            dbXRef = new DbXRef(db,accession);
//                            this.generalDao.persist(dbXRef);
//                        }
//                        subjectFeature.setDbXRef(dbXRef);
//                        subjectFeature.setSeqLen(Integer.parseInt(si.getLength()));
//                        this.sequenceDao.persist(subjectFeature);
//                    }  else {
//                        if (priDatabase.equals("SWALL")) {
//                            priDatabase = "UniProt";
//                        }
//                        subjectFeature = this.featureUtils.createFeature("region", priDatabase + ":" + sections[1], organism);
//
//                        dbXRef = this.generalDao.getDbXRefByDbAndAcc(db, priId);
//                        if (dbXRef == null) {
//                            dbXRef = new DbXRef(db,priId);
//                            this.generalDao.persist(dbXRef);
//                        }
//                        subjectFeature.setDbXRef(dbXRef);
//                        subjectFeature.setSeqLen(Integer.parseInt(si.getLength()));
//                        this.sequenceDao.persist(subjectFeature);
//
//                        DbXRef secDbXRef = null;
//                        Db secDb = this.generalDao.getDbByName(secDatabase);
//                        secDbXRef = this.generalDao.getDbXRefByDbAndAcc(secDb, secId);
//                        if (secDbXRef == null) {
//                            secDbXRef = new DbXRef(secDb,secId);
//                            this.generalDao.persist(secDbXRef);
//                        }
//                        FeatureDbXRef featureDbXRef = new FeatureDbXRef(secDbXRef,subjectFeature,true);
//                        this.sequenceDao.persist(featureDbXRef);
//                    }
//
//                    /* once the dbxrefs are set create featureprop for gene, organism and product
//                     * 
//                     */
//                    CvTerm org = this.cvDao.getCvTermByNameAndCvName("organism", cv);
//                    FeatureProp propOrganism = new FeatureProp(subjectFeature,org,si.getOrganism(),0);
//                    this.sequenceDao.persist(propOrganism);
//
//                    CvTerm pro = this.cvDao.getCvTermByNameAndCvName("product", cv);
//                    FeatureProp propProduct = new FeatureProp(subjectFeature,pro,si.getProduct(),1);
//                    this.sequenceDao.persist(propProduct);
//
//                    CvTerm gene = this.cvDao.getCvTermByNameAndCvName("gene", cv);
//                    FeatureProp propGene = new FeatureProp(subjectFeature,gene,si.getGene(),2);
//                    this.sequenceDao.persist(propGene);
//
//                }
//
//                /* create featureloc and attach 'em to matchFeature
//                 * 
//                 */
//                short strand = 1;
//                String sCoords[] = si.getSubject().split("-");
//                FeatureLoc subjectFLoc = this.featureUtils.createLocation(subjectFeature, matchFeature,Integer.parseInt(sCoords[0]) ,Integer.parseInt(sCoords[1]), strand);
//                subjectFLoc.setRank(0);
//                this.sequenceDao.persist(subjectFLoc);
//
//                String qCoords[] = si.getQuery().split("-");
//                FeatureLoc queryFLoc = this.featureUtils.createLocation(queryFeature, matchFeature,Integer.parseInt(qCoords[0]) ,Integer.parseInt(qCoords[1]), strand);
//                queryFLoc.setRank(1);
//                this.sequenceDao.persist(queryFLoc);
//
//            }
//        }
//    }
    

	public void afterPropertiesSet() {
		System.err.println("In aps cvDao='"+cvDao+"'");
        featureUtils = new FeatureUtils();
        featureUtils.setCvDao(cvDao);
        featureUtils.setSequenceDao(sequenceDao);
        featureUtils.setPubDao(pubDao);
        featureUtils.afterPropertiesSet();
		System.err.println("In aps cvDao='"+cvDao+"', class is '"+cvDao.getClass()+"'");
        DUMMY_ORG = organismDao.getOrganismByCommonName("dummy");
    }


	public void process(final File input) throws FileNotFoundException, XMLStreamException {

		System.err.println("Processing '"+input.getName()+"'");
    	System.err.println("cvDao in process is '"+this.cvDao+"'");
    	// Read in data from file
       	InputStream in = null;
		try {
			in = new FileInputStream(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		

		
	}
    
    private boolean checkOrgs(File input) {
    	return true; // FIXME Should go through orgs to check all loaded
    }
    
    private Set<GenePair> orthologues = new HashSet<GenePair>();
    private Set<GenePair> paralogues = new HashSet<GenePair>();
    private Map<String,List<String>> clusters = new HashMap<String,List<String>>();

    public void setOrganismDao(OrganismDao organismDao) {
        this.organismDao = organismDao;
    }

    public void setSequenceDao(SequenceDao sequenceDao) {
        this.sequenceDao = sequenceDao;
    }

    public void setCvDao(CvDao cvDao) {
    	System.err.println("Changing cvDao to '"+cvDao+"'");
        this.cvDao = cvDao;
    }
//
//    public void setGeneralDao(GeneralDao generalDao) {
//        this.generalDao = generalDao;
//    }
//
	public void setPubDao(PubDao pubDao) {
		this.pubDao = pubDao;
	}

//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.applicationContext = applicationContext;
//    }
    
}
