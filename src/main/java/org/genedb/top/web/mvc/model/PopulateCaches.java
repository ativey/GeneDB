//package org.genedb.web.mvc.model;
//
//
//import org.genedb.top.db.domain.luceneImpls.BasicGeneServiceImpl;
//import org.genedb.top.db.domain.services.BasicGeneService;
//import org.genedb.top.querying.core.LuceneIndex;
//import org.genedb.top.querying.core.LuceneIndexFactory;
//import org.genedb.top.web.gui.DiagramCache;
//import org.genedb.top.web.gui.RenderedDiagramFactory;
//import org.genedb.top.web.mvc.controller.ModelBuilder;
//
//import org.genedb.top.chado.feature.AbstractGene;
//import org.genedb.top.chado.feature.NcRNA;
//import org.genedb.top.chado.feature.Transcript;
//import org.genedb.top.chado.mapped.Feature;
//
//import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
//import org.hibernate.Query;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//import org.springframework.orm.hibernate5.SessionFactoryUtils;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import uk.co.flamingpenguin.jewel.cli.ArgumentValidationException;
//import uk.co.flamingpenguin.jewel.cli.Cli;
//import uk.co.flamingpenguin.jewel.cli.CliFactory;
//import uk.co.flamingpenguin.jewel.cli.Option;
//
//import java.io.File;
//import java.util.Iterator;
//import java.util.List;
//
//import com.sleepycat.collections.StoredMap;
//import com.sleepycat.je.DatabaseException;
//import com.sleepycat.je.EnvironmentLockedException;
//
//
//@Repository
//@Transactional
//public class PopulateCaches {
//    private static final Logger logger = LoggerFactory.getLogger(PopulateCaches.class);
//
//    private BerkeleyMapFactory bmf;
//
//    private StoredMap<Integer, TranscriptDTO> dtoMap;
//    private StoredMap<Integer, String> contextMapMap;
//
//    private SessionFactory sessionFactory;
//    private ModelBuilder modelBuilder;
//    private DiagramCache diagramCache;
//    private LuceneIndexFactory luceneIndexFactory;
//    private BasicGeneService basicGeneService;
//    private RenderedDiagramFactory renderedDiagramFactory;
//
//    private PopulateCachesArgs config;
//
//    public void setSessionFactory(SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }
//
//    /**
//     * @param args
//     * @throws DatabaseException
//     * @throws EnvironmentLockedException
//     */
//    public static void main(String[] args) {
//    	
//    	PropertyConfigurator.configure("resources/classpath/log4j.index.properties");
//    	
//        Cli<PopulateCachesArgs> cli = CliFactory.createCli(PopulateCachesArgs.class);
//        PopulateCachesArgs pca = null;
//        try {
//            pca = cli.parseArguments(args);
//        }
//        catch(ArgumentValidationException exp) {
//            System.err.println("Unable to run:");
//            System.err.println(cli.getHelpMessage());
//            System.exit(64);
//        }
//
//        ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext(
//            new String[] {"classpath:applicationContext.xml", "classpath:populateCaches.xml"});
//        PopulateCaches pc = ctx.getBean("populateCaches", PopulateCaches.class);
//        pc.setConfig(pca);
//        
//        logger.info("fully populating");
//        pc.fullCachePopulate();
//        logger.info("fully populated");
//    }
//
//    @Transactional
//    public void fullCachePopulate() {
//        dtoMap = bmf.getDtoMap(); // TODO More nicely
//        contextMapMap = bmf.getContextMapMap();
//
//        LuceneIndex luceneIndex = luceneIndexFactory.getIndex("org.gmod.schema.mapped.Feature");
//        basicGeneService = new BasicGeneServiceImpl(luceneIndex);
//
//        logger.info(config.isGeneUniqueName());
//        //logger.info(config.getGeneUniqueName());
//        
//        if (config.isGeneUniqueName()) {
//            String geneUniqueName = config.getGeneUniqueName();
//            if (geneUniqueName != null) {
//                populateCacheForGene(geneUniqueName);
//                return;
//            }
//            throw new RuntimeException("Found a --gene argument, but didn't understand it");
//        }
//        logger.info("topLevel?");
//        populateCacheForTopLevelFeatures();
//    }
//
//    /**
//     * @param session
//     */
//    @Transactional
//    private void populateCacheForTopLevelFeatures() {
//        Session session = SessionFactoryUtils.getSession(sessionFactory, true);
//        long start = System.currentTimeMillis();
//
//        Iterator<Feature> topLevelFeatures = getTopLevelFeatures();
//        
//        int count = 0;
//        while (topLevelFeatures.hasNext()) {
//            Feature feature = topLevelFeatures.next();
//            
//            logger.info(feature.getUniqueName());
//
//            if (config.isDebugCount() && count >= config.getDebugCount()) {
//                break;
//            }
//            
//            logger.info(feature.getSeqLen());
//            
//            if (!config.isNoContextMap()) {
//            	int len = feature.getSeqLen();
//            	logger.trace("About to create context map");
//            	if (len > CacheDBHelper.MIN_CONTEXT_LENGTH_BASES) {
//            		CacheDBHelper.populateContextMapCache(
//                            feature, basicGeneService, renderedDiagramFactory, diagramCache, contextMapMap);
//            	} else {
//            		logger.warn(String.format("Attempting tile width of %d for top level feature %s.", len, feature.getUniqueName()));
//            		CacheDBHelper.populateContextMapCache(
//            				feature, basicGeneService, renderedDiagramFactory, diagramCache, contextMapMap, len);
//            	}
//            	logger.trace("Created context map ");
//            }
//            
//            @SuppressWarnings("unchecked")
//            List<Feature> features = session.createQuery(
//                "select fl.feature from FeatureLoc fl" +
//                " where fl.sourceFeature = :feature")
//            .setParameter("feature", feature).list();
//
//            for (Feature f : features) {
//                if (f instanceof AbstractGene) {
//                    populateDtoCache((AbstractGene) f);
//                }
//            }
//
//            session.clear();
//            count++;
//            logger.info(String.format("Count %d of %s : Total run time %.02fs",
//                count, "unknown", (double)(System.currentTimeMillis() - start)/1000));
//        }
//    }
//
//    private void populateCacheForGene(String geneUniqueName) {
//        //Get the Gene
//        AbstractGene gene = CacheDBHelper.findGene(geneUniqueName, sessionFactory);
//
//        if (gene == null) {
//            logger.error("Could not find gene with uniqueName '"
//                + geneUniqueName + "'");
//        } else {
//            populateDtoCache(gene);
//        }
//    }
//
//    @Transactional
//    private Iterator<Feature> getTopLevelFeatures() {
//        Session session = SessionFactoryUtils.getSession(sessionFactory, true);
//        Query q;
//
//        if (config.isOrganisms()) {
//            q = session.createQuery(
//                "select fp.feature" +
//                " from FeatureProp fp" +
//                " where fp.cvTerm.name = 'top_level_seq'" +
//                " and fp.cvTerm.cv.name = 'genedb_misc'" +
//                " and fp.feature.organism.commonName in (:orgNames)")
//            .setString("orgNames", config.getOrganisms().replaceAll(":", ", "));
//        } else {
//            q = session.createQuery(
//                "select fp.feature" +
//                " from FeatureProp fp" +
//                " where fp.cvTerm.name='top_level_seq'" +
//                " and fp.cvTerm.cv.name = 'genedb_misc'");
//        }
//        
//        logger.info(config.getOrganisms());
//        logger.info(q.getQueryString());
//        
//        
//        @SuppressWarnings("unchecked")
//        Iterator<Feature> iterator = q.iterate();
//        return iterator;
//    }
//
//    //DtoDb dtoDb;
//
//    private void populateDtoCache(AbstractGene gene) {
//        for (Transcript transcript : gene.getTranscripts()) {
//        	
//        	if (transcript.isObsolete()) {
//        		continue;
//        	}
//        	
//            TranscriptDTO dto = modelBuilder.prepareTranscript(transcript);
//            dtoMap.put(transcript.getFeatureId(), dto);
//            //dtoDb.persistDTO(dto, transcript.getFeatureId());
//        }
//    }
//    
//    public void setModelBuilder(ModelBuilder modelBuilder) {
//        this.modelBuilder = modelBuilder;
//    }
//
//    public void setDiagramCache(DiagramCache diagramCache) {
//        this.diagramCache = diagramCache;
//    }
//
//    public void setLuceneIndexFactory(LuceneIndexFactory luceneIndexFactory) {
//        this.luceneIndexFactory = luceneIndexFactory;
//    }
//
//    public void setConfig(PopulateCachesArgs pca) {
//        this.config = pca;
//        if (pca.isGlobalFileRoot()) {
//            logger.error("Setting global file root to '"+pca.getGlobalFileRoot()+"'");
//            bmf.setRootDirectory(pca.getGlobalFileRoot());
//        } else {
//            logger.error("Not setting global file root");
//        }
//    }
//
//    public void setDtoMap(StoredMap<Integer, TranscriptDTO> dtoMap) {
//        this.dtoMap = dtoMap;
//    }
//
//    public void setContextMapMap(StoredMap<Integer, String> contextMapMap) {
//        this.contextMapMap = contextMapMap;
//    }
//
//
//    interface PopulateCachesArgs {
//        @Option(shortName="o", description="Only populate cache for this organism")
//        String getOrganisms();
//
//        boolean isOrganisms();
//
//        @Option
//        String getGlobalFileRoot();
//
//        boolean isGlobalFileRoot();
//
//        @Option
//        int getDebugCount();
//
//        boolean isDebugCount();
//
//        @Option(longName="ncm", description="Don't generate context maps")
//        boolean isNoContextMap();
//
//        @Option(longName="gene")
//        String getGeneUniqueName();
//
//        boolean isGeneUniqueName();
//    }
//
//
//    public void setBerkeleyMapFactory(BerkeleyMapFactory bmf) {
//        this.bmf = bmf;
//    }
//
//    public void setRenderedDiagramFactory(RenderedDiagramFactory renderedDiagramFactory) {
//        this.renderedDiagramFactory = renderedDiagramFactory;
//    }
//}
