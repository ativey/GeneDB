//package org.genedb.web.mvc.model;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//import org.genedb.top.db.domain.test.MockBasicGeneService;
//import org.genedb.top.chado.feature.AbstractGene;
//import org.genedb.top.chado.feature.Chromosome;
//import org.genedb.top.chado.feature.Gene;
//import org.genedb.top.chado.feature.Transcript;
//import org.genedb.top.chado.mapped.CvTerm;
//import org.genedb.top.chado.mapped.Feature;
//import org.genedb.top.chado.mapped.FeatureCvTerm;
//import org.genedb.top.chado.mapped.FeatureLoc;
//import org.genedb.top.chado.mapped.FeatureRelationship;
//import org.genedb.top.chado.mapped.Organism;
//import org.genedb.top.chado.mapped.MockChromosome;
//import org.genedb.top.chado.mapped.MockTranscript;
//
//import com.sleepycat.collections.StoredMap;
//
//public class CacheSynchTestDelegate extends CacheSynchroniser {
//    
//    @Override
//    protected List<Feature> findTopLevelFeatures(Collection<Integer> featureIds, Class<? extends Feature> clazz){
//        List<Feature> features = new ArrayList<Feature>();
//        for(Integer featureId : featureIds){
//            Feature feature = createTopLevelFeature(featureId);
//            feature.setSeqLen(6000);
//            features.add(feature);
//        }
//        return features;
//    }
//    
//    @Override
//    protected List<Transcript> findTranscripts(Collection<Integer> featureIds, Class<? extends Feature> clazz){
//        List<Transcript> transcripts = new ArrayList<Transcript>();
//        for(Integer featureId : featureIds){
//            String uniqueName = String.valueOf(featureId);
//            Transcript transcript = new MockTranscript(
//                    createOrganism(), uniqueName, true, false, null);
//            transcript.setSeqLen(6000);
//            
//            AbstractGene gene = createAbstractGene(featureId);
//            transcript.addFeatureRelationshipsForSubjectId(
//                    new FeatureRelationship(null, gene, null, 0));
//            transcript.setGene(gene);
//            transcript.addCvTerm(createCvTerm());
//            transcripts.add(transcript);
//        }
//        return transcripts;
//    }
//    
//    private AbstractGene createAbstractGene(Integer featureId){
//        String uniqueName = String.valueOf(featureId);
//        Gene gene = new Gene(createOrganism(), uniqueName, true, false, null);
//        FeatureLoc featureLoc = new FeatureLoc(
//                createTopLevelFeature(featureId), null, 4, true, 7, true, null, null, 0, 0); 
//        ((Feature)gene).addFeatureLoc(featureLoc);
//       return gene;
//    }
//    
//
//    @Override
//    protected void init(){
//        dtoMap = bmf.getDtoMap(); // TODO More nicely
//        contextMapMap = bmf.getContextMapMap();
//        contextImageMap = bmf.getImageMap();
//        basicGeneService = new MockBasicGeneService("cat", "chr1", 1);
//    }
//    
//    private Organism createOrganism(){
//        Organism organism = new Organism("_genus", "_species", "_commonName", "_abbreviation", "_comment");
//        return organism;
//    }
//    
//    private Chromosome createTopLevelFeature(Integer featureId){
//        String uniqueName = String.valueOf(featureId);
//        Chromosome chr = new MockChromosome(createOrganism(), uniqueName, true, false, null);
//        ((Feature)chr).addCvTerm(createCvTerm());
//        ((Feature)chr).addFeatureCvTerm(createFeatureCvTerm(chr));
//        return chr;
//    }
//    
//    private CvTerm createCvTerm(){
//        CvTerm cvterm = new CvTerm(null, null, "_cvterm", "gergeww");
//        return cvterm;
//    }
//    
//    private FeatureCvTerm createFeatureCvTerm(Feature feature){
//        FeatureCvTerm fct = new FeatureCvTerm(createCvTerm(),feature, null, false, 10);
//        return fct;
//    }
//    
//    public StoredMap<Integer, String> getContextMapMap(){
//        return contextMapMap;
//    }
//    
//    public StoredMap<String, byte[]> getContextImageMap(){
//        return contextImageMap;
//    }
//    
//    public StoredMap<Integer, TranscriptDTO> getDtoMap(){
//        return dtoMap;
//    }
//    
//    public StringBuffer getChangeSetInfo(){
//        return changeSetInfo;
//    }
//}
