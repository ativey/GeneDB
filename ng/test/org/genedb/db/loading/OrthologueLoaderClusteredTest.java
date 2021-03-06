package org.genedb.db.loading;

import org.genedb.top.chado.feature.Chromosome;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Test the loading of orthologue data in unclustered mode.
 * Implicit cluster mode is engaged when the input file does not
 * contain explicit clusters and no algorithm is specified (indicating
 * that the orthologues are manually curated). Identity percentage
 * values are not stored in the database in this mode.
 * <p>
 * This is not a unit test, in that it relies on the EMBL loader
 * to load the genes before we load their orthologue data.
 *
 * @author rh11
 *
 */
public class OrthologueLoaderClusteredTest {

    private static final Logger logger = TestLoggerFactory.getLogger(OrthologueLoaderClusteredTest.class);

    private static ApplicationContext applicationContext;
    private static OrthologueTester tester;

    private static final String program = "OrthoMCL";
    private static final String programVersion = "1.4";
    private static final String algorithm = "(default parameters)";

    private static final String DATASET_NAME = "test";

    @BeforeClass
    public static void setup() throws IOException, ParsingException {
        applicationContext = new ClassPathXmlApplicationContext(new String[] {"Load.xml", "Test.xml"});

        loadEmblFile("test/data/MRSA252_clusters.embl", "Saureus_MRSA252");
        loadEmblFile("test/data/MSSA476_clusters.embl", "Saureus_MSSA476");
        loadEmblFile("test/data/EMRSA15_clusters.embl", "Saureus_EMRSA15");

        loadOrthologues("test/data/Saureus_clusters.ortho", false);

        tester = applicationContext.getBean("orthologueTester", OrthologueTester.class);
    }

    private static void loadOrthologues(String filename, boolean geneNames)
        throws IOException, ParsingException {

        OrthologuesLoader loader = applicationContext.getBean("orthologuesLoader", OrthologuesLoader.class);

        loader.setAnalysisProperties(program, programVersion, algorithm);
        loader.setDatasetName(DATASET_NAME);
        File file = new File(filename);
        Reader reader = new FileReader(file);
        try {
            OrthologueFile orthologueFile = new OrthologueFile(file, reader);

            loader.setGeneNames(geneNames);
            loader.load(orthologueFile);
        } finally {
            reader.close();
        }
    }

    @AfterClass
    public static void cleanUp() {
        if (tester == null) {
            // This can happen if there's an error in setup:
            // JUnit still calls us even if setup threw an exception.
            logger.error("Tester is null in cleanUp");
        } else {
            tester.cleanUp();
        }
    }

    private static void loadEmblFile(String filename, String organismCommonName) throws IOException, ParsingException {
        logger.trace(String.format("Loading '%s' into organism '%s'", filename, organismCommonName));

        EmblLoader emblLoader = applicationContext.getBean("emblLoader", EmblLoader.class);
        emblLoader.setOrganismCommonName(organismCommonName);
        emblLoader.setSloppyControlledCuration(true);
        emblLoader.setTopLevelFeatureClass(Chromosome.class);

        File file = new File(filename);
        Reader reader = new FileReader(file);
        try {
            emblLoader.load(new EmblFile(file, reader));
        } finally {
            reader.close();
        }
    }

    private void cluster(String clusterName, String... polypeptideUniqueNames) {
        tester.orthologueGroup(DATASET_NAME, clusterName, program, programVersion, algorithm, null, polypeptideUniqueNames);
    }

    @Transactional @Test
    public void testClusters() {
        cluster("ORTHOMCL32", "SAEMRSA1519860.1:pep", "SAR0071.1:pep", "SAR2164.1:pep", "SAS1981.1:pep");
        cluster("ORTHOMCL139", "SAEMRSA1501410.1:pep", "SAR0177.1:pep", "SAS0151.1:pep");
        cluster("ORTHOMCL290", "SAEMRSA1503120.1:pep", "SAR0354.1:pep", "SAS0333.1:pep");
        cluster("ORTHOMCL552", "SAEMRSA1505950.1:pep", "SAR0680.1:pep", "SAS0634.1:pep");
        cluster("ORTHOMCL628", "SAEMRSA1506710.1:pep", "SAR0799.1:pep", "SAS0710.1:pep");
        cluster("ORTHOMCL990", "SAEMRSA1510490.1:pep", "SAR1192.1:pep", "SAS1150.1:pep");
        cluster("ORTHOMCL1038", "SAEMRSA1510970.1:pep", "SAR1240.1:pep", "SAS1198.1:pep");
        cluster("ORTHOMCL1226", "SAEMRSA1513180.1:pep", "SAR1468.1:pep", "SAS1400.1:pep");
        cluster("ORTHOMCL1274", "SAEMRSA1513730.1:pep", "SAR1518.1:pep", "SAS0933.1:pep");
        cluster("ORTHOMCL1719", "SAEMRSA1519010.1:pep", "SAR2076.1:pep", "SAS0913.1:pep");
        cluster("ORTHOMCL1726", "SAEMRSA1519080.1:pep", "SAR2085.1:pep", "SAS1906.1:pep");
        cluster("ORTHOMCL1744", "SAEMRSA1519390.1:pep", "SAR2118.1:pep", "SAS1937.1:pep");
        cluster("ORTHOMCL2004", "SAEMRSA1522100.1:pep", "SAR2396.1:pep", "SAS2204.1:pep");
    }
}
