package org.genedb.top.db.domain.hibernateImpls;

import java.util.ArrayList;
import java.util.List;

import org.genedb.top.db.domain.objects.Gene;
import org.genedb.top.db.domain.objects.Transcript;
import org.genedb.top.db.domain.services.GeneService;

import org.genedb.top.chado.mapped.AnalysisFeature;
import org.genedb.top.chado.mapped.Feature;
import org.genedb.top.chado.mapped.FeatureRelationship;
import org.genedb.top.chado.mapped.FeatureSynonym;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class GeneServiceImpl extends BasicGeneServiceImpl implements GeneService {
    @Transactional
    @Override
    protected Gene geneFromFeature(Feature feat) {
        Gene ret = new Gene(super.geneFromFeature(feat));

        for (FeatureSynonym fs : feat.getFeatureSynonyms()) {
            String type = fs.getSynonym().getType().getName();
            if (type.equals("reserved_name")) {
                ret.setReservedName(fs.getSynonym().getName());
            }
        }

        Transcript transcript = ret.getTranscripts().get(0);
        Feature protein = transcript.getProtein();

        List<String> clusters = new ArrayList<String>();
        List<String> orthologues = new ArrayList<String>();
        List<String> paralogues = new ArrayList<String>();
        for (AnalysisFeature af : protein.getAnalysisFeatures()) {
            paralogues.add(af.getAnalysis().getName());
        }
        for (FeatureRelationship fr : protein.getFeatureRelationshipsForObjectId()) {
            Feature otherFeat = fr.getSubjectFeature();
            processOrthoParaClusters(fr, otherFeat, clusters, orthologues, paralogues);
        }
        for (FeatureRelationship fr : protein.getFeatureRelationshipsForSubjectId()) {
            Feature otherFeat = fr.getObjectFeature();
            processOrthoParaClusters(fr, otherFeat, clusters, orthologues, paralogues);
        }
        ret.setOrthologues(orthologues);
        ret.setParalogues(paralogues);
        ret.setClusters(clusters);

        ret.setOrganism(feat.getOrganism().getFullName());

        return ret;
    }

    private void processOrthoParaClusters(FeatureRelationship fr, Feature otherFeat,
            List<String> clusters, List<String> orthologues, List<String> paralogues) {
        String type = fr.getType().getName();
        if (type.equals("orthologous_to")) {
            if (otherFeat.getOrganism().getFullName().equals("dummy")) {
                clusters.add(otherFeat.getUniqueName());
            } else {
                orthologues.add(otherFeat.getUniqueName());
            }
        }
        if (type.equals("paralogous_to")) {
            paralogues.add(otherFeat.getUniqueName());
        }
    }

    @Override
    public Gene findGeneByUniqueName(String name) {
        return geneFromFeature(findGeneFeatureByUniqueName(name));
    }
}
