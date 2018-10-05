package org.genedb.top.web.mvc.model;

import org.apache.lucene.search.TopDocs;
import org.genedb.top.db.audit.ChangeSet;

import org.genedb.top.chado.feature.AbstractGene;
import org.genedb.top.chado.feature.Gap;
import org.genedb.top.chado.feature.Gene;
import org.genedb.top.chado.feature.MRNA;
import org.genedb.top.chado.feature.NcRNA;
import org.genedb.top.chado.feature.Polypeptide;
import org.genedb.top.chado.feature.Pseudogene;
import org.genedb.top.chado.feature.PseudogenicTranscript;
import org.genedb.top.chado.feature.RRNA;
import org.genedb.top.chado.feature.SnRNA;
import org.genedb.top.chado.feature.TRNA;
import org.genedb.top.chado.feature.Transcript;
import org.genedb.top.chado.mapped.Feature;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.reader.ReaderProvider;
import org.hibernate.search.store.DirectoryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

@Repository
@Transactional
public class IndexSynchroniser implements IndexUpdater{
    @Override
    public int updateTranscriptCache(ChangeSet changeSet) throws Exception {
        // TODO Auto-generated method stub
        return 0;
    }

    private static final Logger logger = LoggerFactory.getLogger(IndexSynchroniser.class);

    private static final int BATCH_SIZE = 10;//must match BATCH_SIZE in config

    private SessionFactory sessionFactory;

    @Transactional
    public boolean updateAllCaches(ChangeSet changeSet) {
        logger.debug("Starting updateAllCaches");
        Session session = SessionFactoryUtils.getSession(sessionFactory, false);
        FullTextSession fullTextSession = Search.getFullTextSession(session);

        //Delete deleted features
        Set failedDeletes = deleteFeatures(fullTextSession, changeSet);

        //prevent unncecesary flush
        fullTextSession.setHibernateFlushMode(FlushMode.MANUAL);

        //disable 2nd-level cache ops
        fullTextSession.setCacheMode(CacheMode.IGNORE);

        //Index altered features
        indexFeatures(fullTextSession, changeSet);

        return true;
    }

    /**
     * Index features in batches
     * @param session
     * @param changeSet
     */
    private void indexFeatures(FullTextSession session, ChangeSet changeSet){
        logger.debug("Starting indexFeatures");

        Set<Integer> alteredIds = Sets.newHashSet();
        alteredIds.addAll(changeSet.newFeatureIds(AbstractGene.class));
        alteredIds.addAll(changeSet.changedFeatureIds(AbstractGene.class));
        alteredIds.addAll(changeSet.newFeatureIds(Transcript.class));
        alteredIds.addAll(changeSet.changedFeatureIds(Transcript.class));
        alteredIds.addAll(changeSet.newFeatureIds(Polypeptide.class));
        alteredIds.addAll(changeSet.changedFeatureIds(Polypeptide.class));
        alteredIds.addAll(changeSet.newFeatureIds(Gap.class));
        alteredIds.addAll(changeSet.changedFeatureIds(Gap.class));

        Set<Integer> failedIds = Sets.newHashSet();
        Set<Integer> batchIds = Sets.newHashSet();
        Set<Integer> unflushedIds = Sets.newHashSet();

        int index = 0;
        //First attempt to index
        for (Integer featureId : alteredIds) {
            index++;
            try{
                logger.debug("featureID " + featureId + " being loaded");
                batchIds.add(featureId);
                Feature feature = (Feature)session.get(Feature.class, featureId);
                session.index(feature);
                logger.debug("--featureID: " + featureId + " indexed...");
            }catch(Exception e){
                logger.error(String.format("Error found in first attempt with feature ID: %s", featureId), e);
                index = 0;
                failedIds.add(featureId);
                unflushedIds.addAll(batchIds);
                session.clear();
            }

            if (index % BATCH_SIZE == 0){
                batchIds.clear();
                session.clear();
            }
        }

        //Second attempt to index non-defective features
        index=0;
        for(Integer featureId : unflushedIds){
            if(!failedIds.contains(featureId)){
                index++;
                Feature feature = (Feature)session.get(Feature.class, featureId);
                logger.debug(String.format("About to try and index unflushed id %s", featureId));
                session.index(feature);
                logger.debug(String.format("Indexed unflushed id %s", featureId));
            }

            if (index % BATCH_SIZE == 0){
                batchIds.clear();  // TODO needed ?
                session.clear();
            }
        }

        logger.debug("Exiting indexFeatures");
    }

    /**
     * Delete Features
     * @param session
     * @param changeSet
     * @return
     */
    private Set<Integer> deleteFeatures(FullTextSession session, ChangeSet changeSet) {
        logger.debug("Starting deleteFeatures");
        Set<Integer> failedDeletes = new HashSet<Integer>();

        Set<Integer> deletedIds = Sets.newHashSet();
        deletedIds.addAll(changeSet.deletedFeatureIds(Gene.class));
        deletedIds.addAll(changeSet.deletedFeatureIds(Transcript.class));
        deletedIds.addAll(changeSet.deletedFeatureIds(Polypeptide.class));
        deletedIds.addAll(changeSet.deletedFeatureIds(Gap.class));

        IndexSearcher indexSearcher = createIndexSearcher(session);
        for (Integer featureId : deletedIds) {
            try {
                deleteFeature(session, indexSearcher, featureId);
            } catch (Exception exp) {
                logger.error(String.format("Failed to delete %s", featureId), exp);
                failedDeletes.add(featureId);
            }
        }
        if (failedDeletes.size()>0) {
            logger.debug(String.format("Ended deleteFeatures with %s features undeleted due to failures", failedDeletes.size()));
        } else {
            logger.debug("Ended deleteFeatures with no errors");
        }
        return failedDeletes;
    }

    /**
     * Delete a feature
     * @param session
     * @param indexSearcher
     * @param featureId
     * @throws Exception
     */
    private void deleteFeature(FullTextSession session, IndexSearcher indexSearcher, Integer featureId)throws Exception{
        logger.debug(String.format("Starting deleteFeature(session, indexSearcher, %s)", featureId));
        Class<? extends Feature> entityType = findFeatureSubclass(indexSearcher, featureId);
        session.purge(entityType, featureId);
        logger.debug(String.format("Deleted ID %s of entity type %s", featureId, entityType.getName()));
    }

    /**
     * Create the IndexSearcher
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")

//


    private IndexSearcher createIndexSearcher(FullTextSession session){
        logger.debug("Starting createIndexSearcher");
        SearchFactory searchFactory = session.getSearchFactory();
        ReaderProvider readerProvider = session.getSearchFactory().getReaderProvider();
        DirectoryProvider[] directoryProviders = searchFactory.getDirectoryProviders(Feature.class);
        IndexReader indexReader = readerProvider.openReader(directoryProviders[0]);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        logger.debug("Ending createIndexSearcher");
        return indexSearcher;
    }
    IndexReader reader = searchFactory.getIndexReaderAccessor().open(Order.class);
//try {
//        //perform read-only operations on the reader
//    }
//finally {
//        searchFactory.getIndexReaderAccessor().close(reader);
//    }
//



    /**
     *
     * @param indexSearcher
     * @param featureId
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private Class<? extends Feature> findFeatureSubclass(IndexSearcher indexSearcher, Integer featureId)throws Exception{
        TermQuery query = new TermQuery(new Term("featureId", Integer.toString(featureId)));
        Hits hits = indexSearcher.search(query);
        Document doc = hits.doc(0);
        Field field = doc.getField("_hibernate_class");
        String className = field.stringValue();
        logger.debug(String.format("Class name: %s for ID %s", className, featureId));
        return (Class<? extends Feature>) Class.forName(className);
    }

    /*
     * This is useful for JUnit testing
     */
    @Transactional
    public void purgeAll(){
        logger.debug("Starting purgeAll");
        Session session = SessionFactoryUtils.getSession(sessionFactory, false);
        FullTextSession fullTextSession = Search.createFullTextSession(session);
        fullTextSession.purgeAll(Gap.class);
        fullTextSession.purgeAll(Gene.class);
        fullTextSession.purgeAll(Transcript.class);
        fullTextSession.purgeAll(MRNA.class);
        fullTextSession.purgeAll(NcRNA.class);
        fullTextSession.purgeAll(TRNA.class);
        fullTextSession.purgeAll(RRNA.class);
        fullTextSession.purgeAll(SnRNA.class);
        fullTextSession.purgeAll(Polypeptide.class);
        fullTextSession.purgeAll(Pseudogene.class);
        fullTextSession.purgeAll(PseudogenicTranscript.class);
        fullTextSession.getSearchFactory().optimize();
        logger.debug("Ended purgeAll");
    }


    /*
     * This is useful for JUnit testing
     */
    @Transactional
    public void indexSingle(Integer featureId){
        logger.debug("Starting indexSingle");
        Session session = SessionFactoryUtils.getSession(sessionFactory, false);
        FullTextSession fullTextSession = Search.createFullTextSession(session);
        Feature feature = (Feature)fullTextSession.get(Feature.class, featureId);
        fullTextSession.index(feature);
        logger.debug("Ended indexSingle");
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
