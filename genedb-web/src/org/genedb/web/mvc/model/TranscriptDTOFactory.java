package org.genedb.web.mvc.model;

import org.genedb.db.dao.SequenceDao;
import org.genedb.db.domain.objects.DatabasePolypeptideRegion;
import org.genedb.db.domain.objects.InterProHit;
import org.genedb.db.domain.objects.PolypeptideRegionGroup;
import org.genedb.db.domain.objects.SimpleRegionGroup;
import org.genedb.web.gui.DiagramCache;
import org.genedb.web.gui.ImageMapSummary;
import org.genedb.web.gui.ProteinMapDiagram;
import org.genedb.web.gui.RenderedDiagramFactory;
import org.genedb.web.gui.RenderedProteinMap;

import org.gmod.schema.feature.AbstractGene;
import org.gmod.schema.feature.GPIAnchorCleavageSite;
import org.gmod.schema.feature.Polypeptide;
import org.gmod.schema.feature.PolypeptideDomain;
import org.gmod.schema.feature.ProductiveTranscript;
import org.gmod.schema.feature.PseudogenicTranscript;
import org.gmod.schema.feature.SignalPeptide;
import org.gmod.schema.feature.Transcript;
import org.gmod.schema.feature.TransmembraneRegion;
import org.gmod.schema.mapped.DbXRef;
import org.gmod.schema.mapped.Feature;
import org.gmod.schema.mapped.FeatureCvTerm;
import org.gmod.schema.mapped.FeatureDbXRef;
import org.gmod.schema.mapped.FeatureLoc;
import org.gmod.schema.mapped.FeatureProp;
import org.gmod.schema.mapped.FeaturePub;
import org.gmod.schema.mapped.FeatureRelationship;
import org.gmod.schema.mapped.FeatureSynonym;
import org.gmod.schema.mapped.Organism;
import org.gmod.schema.mapped.Synonym;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.Assert;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Configurable
public class TranscriptDTOFactory {

    private transient Logger logger = Logger.getLogger(TranscriptDTOFactory.class);

    @Autowired
    private transient SequenceDao sequenceDao;

    private Map<String,Object> prepareAlgorithmData(Polypeptide polypeptide) {
        Map<String,Object> aData = new HashMap<String,Object>();
        putIfNotEmpty(aData, "SignalP", prepareSignalPData(polypeptide));
        putIfNotEmpty(aData, "DGPI", prepareDGPIData(polypeptide));
        putIfNotEmpty(aData, "PlasmoAP", preparePlasmoAPData(polypeptide));
        putIfNotEmpty(aData, "TMHMM", prepareTMHMMData(polypeptide));
        return aData;
    }

    private Map<String,Object> prepareSignalPData(Polypeptide polypeptide) {
        Map<String,Object> signalPData = new HashMap<String,Object>();

        String prediction  = polypeptide.getProperty("genedb_misc", "SignalP_prediction");
        String peptideProb = polypeptide.getProperty("genedb_misc", "signal_peptide_probability");
        String anchorProb  = polypeptide.getProperty("genedb_misc", "signal_anchor_probability");

        putIfNotNull(signalPData, "prediction",  prediction);
        putIfNotNull(signalPData, "peptideProb", peptideProb);
        putIfNotNull(signalPData, "anchorProb",  anchorProb);

        Collection<SignalPeptide> signalPeptides = polypeptide.getRegions(SignalPeptide.class);
        if (!signalPeptides.isEmpty()) {
            if (signalPeptides.size() > 1) {
                logger.error(String.format("Polypeptide '%s' has %d signal peptide regions; only expected one",
                    polypeptide.getUniqueName(), signalPeptides.size()));
            }
            SignalPeptide signalPeptide = signalPeptides.iterator().next();
            FeatureLoc signalPeptideLoc = signalPeptide.getRankZeroFeatureLoc();

            signalPData.put("cleavageSite", signalPeptideLoc.getFmax());
            signalPData.put("cleavageSiteProb", signalPeptide.getProbability());
        }
        return signalPData;
    }

    private Map<String,Object> prepareDGPIData(Polypeptide polypeptide) {
        /* If the GPI_anchored property is not present, we do not add the
         * predicted cleavage site, even if there is one.
         */
        if (!polypeptide.hasProperty("genedb_misc", "GPI_anchored")) {
            return Collections.emptyMap();
        }

        Map<String,Object> dgpiData = new HashMap<String,Object>();
        dgpiData.put("anchored", true);

        Collection<GPIAnchorCleavageSite> cleavageSites = polypeptide.getRegions(GPIAnchorCleavageSite.class);
        if (!cleavageSites.isEmpty()) {
            if (cleavageSites.size() > 1) {
                logger.error(String.format("There are %d GPI anchor cleavage sites on polypeptide '%s'; only expected one",
                    cleavageSites.size(), polypeptide.getUniqueName()));
            }
            GPIAnchorCleavageSite cleavageSite = cleavageSites.iterator().next();
            FeatureLoc cleavageSiteLoc = cleavageSite.getRankZeroFeatureLoc();
            dgpiData.put("location", cleavageSiteLoc.getFmax());
            dgpiData.put("score", cleavageSite.getScore());
        }

        return dgpiData;
    }

    private Map<String,Object> preparePlasmoAPData(Polypeptide polypeptide) {
        Map<String,Object> plasmoAPData = new HashMap<String,Object>();
        String score = polypeptide.getProperty("genedb_misc", "PlasmoAP_score");
        if (score != null) {
            plasmoAPData.put("score", score);
            switch (Integer.parseInt(score)) {
            case 0:
            case 1:
            case 2:
                plasmoAPData.put("description", "Unlikely");
                break;
            case 3:
                plasmoAPData.put("description", "Unknown");
                break;
            case 4:
                plasmoAPData.put("description", "Likely");
                break;
            case 5:
                plasmoAPData.put("description", "Very likely");
                break;
            default:
                throw new RuntimeException(String.format("Polypeptide '%s' has unrecognised PlasmoAP score '%s'",
                    polypeptide.getUniqueName(), score));
            }
        }

        return plasmoAPData;
    }


    private List<String> prepareTMHMMData(Polypeptide polypeptide) {
        List<String> tmhmmData = new ArrayList<String>();

        for (TransmembraneRegion transmembraneRegion: polypeptide.getRegions(TransmembraneRegion.class)) {
            tmhmmData.add(String.format("%d-%d",
                1 + transmembraneRegion.getRankZeroFeatureLoc().getFmin(),
                transmembraneRegion.getRankZeroFeatureLoc().getFmax()));
        }

        return tmhmmData;
    }

    private <S> void putIfNotEmpty(Map<S,? super Collection<?>> map, S key, Collection<?> value) {
        if (!value.isEmpty()) {
            map.put(key, value);
        }
    }
    private <S,T> void putIfNotNull(Map<S,T> map, S key, T value) {
        if (value != null) {
            map.put(key, value);
        }
    }

    private <S> void putIfNotEmpty(Map<S,? super Map<?,?>> map, S key, Map<?,?> value) {
        if (!value.isEmpty()) {
            map.put(key, value);
        }
    }


    public TranscriptDTO make(Transcript transcript, DiagramCache diagramCache) {
        TranscriptDTO ret = new TranscriptDTO();
        AbstractGene gene = transcript.getGene();
        Polypeptide polypeptide = null;
        if (transcript instanceof ProductiveTranscript) {
            polypeptide = ((ProductiveTranscript)transcript).getProtein();
            if (transcript instanceof PseudogenicTranscript) {
                ret.setPseudo(true);
            } else {
                ret.setPseudo(false);
            }
        }

        //---------------------
        if (polypeptide == null) {
            logger.error(String.format("The transcript '%s' has no polypeptide", transcript.getUniqueName()));
            //return;
        } else {
            ret.setProteinCoding(true);
        }

        // =-----------------------------

        if (gene.getTranscripts().size()>1) {
            ret.setAnAlternateTranscript(true);
        }
        populateNames(ret, transcript, gene);
        populateParentDetails(ret, gene);
        populateMisc(ret, transcript);
        populateOrganismDetails(ret, transcript);
        populateLastModified(ret, transcript, polypeptide);

        if (polypeptide != null) {
            ret.setAlgorithmData(prepareAlgorithmData(polypeptide));
            ret.setPolypeptideProperties(polypeptide.calculateStats());
            populateFromFeatureProps(ret, polypeptide);

            ret.setProducts(populateFromFeatureCvTerms(polypeptide, "genedb_products"));
            ret.setControlledCurations(populateFromFeatureCvTerms(polypeptide, "CC_"));
            ret.setGoBiologicalProcesses(populateFromFeatureCvTerms(polypeptide, "biological_process"));
            ret.setGoMolecularFunctions(populateFromFeatureCvTerms(polypeptide, "molecular_function"));
            ret.setGoCellularComponents(populateFromFeatureCvTerms(polypeptide, "cellular_component"));
            populateFromFeatureDbXrefs(ret, polypeptide);

            populateFromFeatureRelationships(ret, polypeptide);

            populateFromFeaturePubs(ret, polypeptide);

            ret.setDomainInformation(prepareDomainInformation(polypeptide));
            //model.put("domainInformation", domainInformation);



                // Get image
            ProteinMapDiagram diagram = new ProteinMapDiagram(polypeptide, transcript, ret.getDomainInformation());
            if (!diagram.isEmpty()) {
                RenderedProteinMap renderedProteinMap = (RenderedProteinMap) RenderedDiagramFactory.getInstance().getRenderedDiagram(diagram);

                        try {
                            ret.setIms(new ImageMapSummary(
                                    renderedProteinMap.getWidth(),
                                    renderedProteinMap.getHeight(),
                                    diagramCache.fileForProteinMap(renderedProteinMap),
                                    renderedProteinMap.getRenderedFeaturesAsHTML("proteinMapMap")));
                        } catch (IOException exp) {
                            logger.error("Failed to create an imageMapSummary", exp);
                        }
            }



        }

        return ret;

    }

    // TODO What happens wrt time last accessioned
    private void populateLastModified(TranscriptDTO transcriptDTO, Transcript transcript, Polypeptide polypeptide) {
            Timestamp date = transcript.getTimeLastModified();

            if (polypeptide != null) {
                Timestamp polypeptideDate = polypeptide.getTimeLastModified();
                if (date == null && polypeptideDate == null) {
                    return;
                }
                if (polypeptideDate != null && polypeptideDate.after(date)) {
                    date = polypeptideDate;
                }
            }

            if (date != null) {
                transcriptDTO.setLastModified(date.getTime());
            }
    }

    private void populateOrganismDetails(TranscriptDTO ret, Transcript transcript) {
        ret.setOrganismCommonName(transcript.getOrganism().getCommonName());
        ret.setOrganismHtmlShortName(transcript.getOrganism().getHtmlShortName());
    }

    private List<PolypeptideRegionGroup> prepareDomainInformation(Polypeptide polypeptide) {

        Map<DbXRef, InterProHit> interProHitsByDbXRef= Maps.newHashMap();
        SimpleRegionGroup otherMatches = new SimpleRegionGroup("Other matches", "Other");

        for (PolypeptideDomain domain: polypeptide.getRegions(PolypeptideDomain.class)) {

            DatabasePolypeptideRegion thisHit = DatabasePolypeptideRegion.build(domain);

            if (thisHit == null) {
                continue;
            }

            DbXRef interProDbXRef = domain.getInterProDbXRef();
            Hibernate.initialize(interProDbXRef);
            if (interProDbXRef == null) {
                otherMatches.addRegion(thisHit);
            } else {
                if (!interProHitsByDbXRef.containsKey(interProDbXRef)) {
                    interProHitsByDbXRef.put(interProDbXRef, new InterProHit(interProDbXRef));
                }
                interProHitsByDbXRef.get(interProDbXRef).addRegion(thisHit);
            }
        }

        List<PolypeptideRegionGroup> domainInfo = new ArrayList<PolypeptideRegionGroup>(interProHitsByDbXRef.values());

        if (!otherMatches.isEmpty()) {
            domainInfo.add(otherMatches);
        }

        return domainInfo;
    }

    private void populateFromFeatureRelationships(TranscriptDTO transcriptDTO, Polypeptide polypeptide) {

        List<String> clusterIds = Lists.newArrayList();
        List<String> orthologueNames = Lists.newArrayList();
        List<FeatureRelationship> filtered =
            polypeptide.getFeatureRelationshipsForSubjectIdFilteredByCvNameAndTermName("sequence", "orthologous_to");

        for (FeatureRelationship featureRelationship : filtered) {
            Feature f = featureRelationship.getObjectFeature();
            if (f.getType().getName().equals("protein_match")) {
                clusterIds.add(f.getUniqueName());
            } else {
                if (f.getType().getName().equals("polypeptide")) {
                    orthologueNames.add(f.getUniqueName());
                }
            }
        }

        transcriptDTO.setClusterIds(clusterIds);
        transcriptDTO.setOrthologueNames(orthologueNames);

    }


    private void populateFromFeatureDbXrefs(TranscriptDTO transcriptDTO, Polypeptide polypeptide) {
        List<DbXRefDTO> dbXRefDTOs = new ArrayList<DbXRefDTO>();
        for(FeatureDbXRef fdx : polypeptide.getFeatureDbXRefs()) {
            dbXRefDTOs.add(new DbXRefDTO(fdx.getDbXRef().getDb().getName(),
                    fdx.getDbXRef().getAccession(),
                    fdx.getDbXRef().getDb().getUrlPrefix()));
        }
        if (dbXRefDTOs.size() > 0) {
            transcriptDTO.setDbXRefDTOs(dbXRefDTOs);
        }
    }


    private void populateFromFeaturePubs(TranscriptDTO transcriptDTO, Polypeptide polypeptide) {
        List<String> pubNames = new ArrayList<String>();
        for(FeaturePub fp : polypeptide.getFeaturePubs()) {
            String name = fp.getPub().getUniqueName();
            if (name.startsWith("PMID")) {
                pubNames.add(name);
            } else {
                logger.warn(String.format("Got a pub that isn't a PMID ('%s') for '%s'", name, polypeptide.getUniqueName()));
            }
        }
        if (pubNames.size() > 0) {
            transcriptDTO.setPublications(pubNames);
        }
    }

    private List<FeatureCvTermDTO> populateFromFeatureCvTerms(Polypeptide polypeptide, String cvNamePrefix) {
        Assert.notNull(polypeptide);

        Organism org = polypeptide.getOrganism();
        List<FeatureCvTermDTO> dtos = new ArrayList<FeatureCvTermDTO>();
        for (FeatureCvTerm featureCvTerm : polypeptide.getFeatureCvTermsFilteredByCvNameStartsWith(cvNamePrefix)) {
            FeatureCvTermDTO fctd = new FeatureCvTermDTO(featureCvTerm);
            fctd.setCount(sequenceDao.getFeatureCvTermCountInOrganism(featureCvTerm.getCvTerm().getName(), org));
            dtos.add(fctd);
        }
        if (dtos.size() > 0) {
            return dtos;
        }
        return null;
    }


    private void populateMisc(TranscriptDTO ret, Transcript transcript) {
        String type = transcript.getType().getName();
        if ("mRNA".equals(type)) {
            type = "Protein coding gene";
        } else {
            if ("pseudogenic_transcript".equals(type)) {
                type = "Pseudogene";
            }
        }
        ret.setTypeDescription(type);
    }


    private void populateParentDetails(TranscriptDTO ret, AbstractGene gene) {
        FeatureLoc top = gene.getRankZeroFeatureLoc();
        ret.setMin(top.getFmin());
        ret.setMax(top.getFmax());
        ret.setStrand((top.getStrand()) != null ? top.getStrand() : 0);

        Feature topLevelFeature = top.getSourceFeature();
        ret.setTopLevelFeatureType(topLevelFeature.getType().getName());
        ret.setTopLevelFeatureDisplayName(topLevelFeature.getDisplayName());
        ret.setTopLevelFeatureUniqueName(topLevelFeature.getUniqueName());
        ret.setTopLevelFeatureLength(topLevelFeature.getSeqLen());
    }



    private void populateFromFeatureProps(TranscriptDTO ret, Polypeptide polypeptide) {
        Assert.notNull(polypeptide);
        ret.setNotes(stringListFromFeaturePropList(polypeptide, "feature_property", "comment"));
        ret.setComments(stringListFromFeaturePropList(polypeptide, "genedb_misc", "curation"));
    }



    private List<String> stringListFromFeaturePropList(Polypeptide polypeptide, String cvName, String cvTermName) {
        List<String> ret = new ArrayList<String>();
        List<FeatureProp> featurePropNotes = polypeptide.getFeaturePropsFilteredByCvNameAndTermName(cvName, cvTermName);
        for (FeatureProp featureProp : featurePropNotes) {
            ret.add(featureProp.getValue());
        }
        logger.debug(String.format("Got '%d' results for filtering featureprops for '%s' in '%s'", ret.size(), cvTermName, cvName));
        if (ret.size() > 0) {
            return ret;
        }
        return Collections.emptyList();
    }



    private void populateNames(TranscriptDTO ret, Transcript transcript, AbstractGene gene) {
        String uniqueName = transcript.getUniqueName();
        ret.setUniqueName(uniqueName);
        ret.setGeneName(gene.getName());
        if (transcript.getName() != null && !transcript.getName().equals(uniqueName)) {
            ret.setProperName(transcript.getName());
        }
        Collection<FeatureSynonym> featureSynonyms = transcript.getFeatureSynonyms();
        //Get the map of lists of synonyms
        ret.setSynonymsByTypes(findFromSynonymsByType(featureSynonyms));
    }


    /**
     * Create lists of synonyms, where each list grouped by the synonym type
     * @param synonyms
     * @return
     */
    private Map<String, List<String>> findFromSynonymsByType(Collection<FeatureSynonym> synonymCollection) {
        HashMap<String, List<String>> synonymsByType = new HashMap<String, List<String>>();
        for (FeatureSynonym featSynonym : synonymCollection) {
            Synonym synonym = featSynonym.getSynonym();
            String typeName = formatSynonymTypeName(synonym.getType().getName());
            List<String> filtered = synonymsByType.get(typeName);
            if (filtered == null){
                filtered = new ArrayList<String>();
                synonymsByType.put(typeName, filtered);
            }
            filtered.add(synonym.getName());
        }

        if (synonymsByType.size() > 0 ) {
            return synonymsByType;
        }
        return null;
    }

    /**
     * Re-format the synonym type name
     * @param rawName
     * @return
     */
    private String formatSynonymTypeName(String rawName){

        char formattedName[] = rawName.toCharArray();
        for(int i=0; i<formattedName.length; ++i){

            //Replace underscores with spaces
            if (formattedName[i]=='_'){
                formattedName[i] = ' ';

            //Replace first char lowercase to a uppercase char
            }else if(i==0 && Character.isLowerCase(formattedName[i])){
                formattedName[i] = Character.toUpperCase(formattedName[i]);

            //Replace any occurrence of a lowercase char preceeded a space with a upper case char
            }else if(i>0 && formattedName[i-1]==' ' && Character.isLowerCase(formattedName[i])){
                formattedName[i] = Character.toUpperCase(formattedName[i]);
            }
        }
        return String.valueOf(formattedName).trim();
    }


}
