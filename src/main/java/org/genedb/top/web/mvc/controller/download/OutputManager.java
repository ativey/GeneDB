package org.genedb.top.web.mvc.controller.download;

import org.genedb.top.web.utils.DownloadUtils;

import org.genedb.top.chado.feature.TopLevelFeature;
import org.genedb.top.chado.feature.Transcript;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

public class OutputManager {

    private static Logger logger = LoggerFactory.getLogger(OutputManager.class);

    private OutputFormat outputFormat;
    private OutputContent outputContent;


    public OutputManager(OutputFormat outputFormat, OutputContent outputContent) {
        super();
        this.outputFormat = outputFormat;
        this.outputContent = outputContent;
    }


    public void write(TopLevelFeature feature, PrintWriter w) {
        if (!feature.isTopLevelFeature()) {
            logger.error(String.format("The feature '%s' isn't actually a top level feature", feature.getUniqueName()));
            return;
        }

        switch (outputFormat) {
        case FASTA:
            fastaOutput(feature, w);
            break;


        }
    }


    private void fastaOutput(TopLevelFeature feature, PrintWriter out) {

        switch(outputContent) {
        case ALL:
            DownloadUtils.writeFasta(out, feature.getUniqueName(), feature.getResidues());
            return;
        case TRANSCRIPT:
            List<Transcript> transcripts = null;//feature.getLocatedChildrenByClass(Transcript.class);
            for (Transcript transcript : transcripts) {
                DownloadUtils.writeFasta(out, feature.getUniqueName(), feature.getResidues());
            }
            return;
        }

    }

}
