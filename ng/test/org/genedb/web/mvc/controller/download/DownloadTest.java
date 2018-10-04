package org.genedb.web.mvc.controller.download;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.genedb.top.querying.core.QueryException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:testContext-query.xml", "classpath:Download.xml"})
public class DownloadTest {
	
	@Autowired
	private DownloadProcessUtil util;
	
	OutputFormat outputFormat = OutputFormat.FASTA;
	String[] custFields = new String[] {};
	OutputDestination outputDestination = OutputDestination.TO_FILE;
	boolean includeHeader = true;
	String fieldSeparator = "|";
	String blankField = " -- ";
	String fieldInternalSeparator = ",";
	int prime3 = 0;
	int prime5 = 0;
	String email = "gv1@sanger.ac.uk";
	List<String> uniqueNames = Arrays.asList(new String[] {"MAL8P1.300"});
	String historyItemName = "testing";
	String description = " testing 123 ";
	
	@Test
	public void test1() throws QueryException, IOException {
		SequenceType sequenceType = SequenceType.UNSPLICED_DNA;
		String sequence = "ATGAAGATTAATATATTGAAGAAAGGCAAAAAATTTTATATTACAAACAATCATTTTAATTATGATATTAAACGAAATTTTACAATATTTCAAAACTCATTTATAAAAACAAACGATATAGTTTATAGAAAAAACATTGATATTGTTTGTGCAAAAGATTTATTCTTTTATACAATTCTAAATGTAGATAGATATAAGTATTTTCTACCATATGTAACGGTAAGCATAAATATATTCATATTTTTAAAACGTATGCGGATGTTGTTTATGTGTGTACATATATTTGTTCATATAAATTGTTACATTTGTTCATATAAATTGTTACATTTGTTCATGTAAATTGTTATATTTGTTCATATAAATTGTTACATTTGTTCATGTAAATTGTTATATTTGTTCATGTAAATTGTTACATTTGTTCATGTAAATTGTTATATTTGTTCATGTAAATTGTTACATTTGTTCATGTAAATTGTTACATTTATTCATGTAAATTGTTACATTTGTTCATGTAAATTGTTACATTTATTCATGTAAATTGTTACATTTGTTCATGTAAATTGTTATATTTGTTCATGTAAAATTTTATATTTGTTCATATATTTTTGCACATTTATTTTTTAAAAATTATGAGCAATTGTATTGTATATTTGTAGGATAGCAAGATAACAGAAAAAAACAAAGAATATTTTAAAGCCAATTTACAAATTGAGAATATTTTCTTTAAAGAAAAATATGACTCTTTAATTCAATTCATTTACCCAACAACAATTACGGTAATAAGACTTTAAAGAGGAAATAAATGAATGAGAAGAAATATAAGTATATTTCGTATATAAATGTGTAAATATATACATACATATATATATATATATATATATATATATATATATATATAAATACTTAGACATTGTTGCATTTTTTTATTTTATTTTATTTTATTTTATTTTATTTAGGTATCTAGCGAAGATACAAATATTTTTCATCACTTGGTATGTAATAATAATAAAGAGACAACTAATTAAAAATATAAACAGAAAAATTAATATATATACTTATATATATTTTAAATAAGGCACATATCGTGTGTCCATTATAATATATATATATATATATATATATTGTTCTTTTTATAGATAACCGAGTGGATAATTAAAGAAAAAAAGAATTGCATAAACATTGATTTTTATATAAATTTTAGGGTAACAAAAATGTTGTATATATATTATATATGTATATATGTGTGTGTGTAAATATACTATATTTTATGTATCTGTGAATTTCTCAAAATTTTAATAAGTTTACATTATTCATAAAGTATTCACTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTCTGCTTAGTTGAAAAATAAAATATATCAAAATTTTATGAATTTATACATAAAAGAGTTAGGGAAAAAAATTTTATATTCCTTTATAAATGAATCAAAATCAAACAGTTATAAAAACACGGACGTTTTACACTTGATAAAATAG";
		StringWriter writer = new StringWriter();
		runProcess(writer, sequenceType);
		checkSequence(writer, sequence);
		
	}
	
	@Test
	public void test2() throws QueryException, IOException {
		SequenceType sequenceType = SequenceType.PROTEIN;
		String sequence = "MKINILKKGKKFYITNNHFNYDIKRNFTIFQNSFIKTNDIVYRKNIDIVCAKDLFFYTILNVDRYKYFLPYVTDSKITEKNKEYFKANLQIENIFFKEKYDSLIQFIYPTTITVSSEDTNIFHHLITEWIIKEKKNCINIDFYINFRLKNKIYQNFMNLYIKELGKKILYSFINESKSNSYKNTDVLHLIK";
		StringWriter writer = new StringWriter();
		runProcess(writer, sequenceType);
		checkSequence(writer, sequence);
	}
	
	@Test
	public void test3() throws QueryException, IOException {
		SequenceType sequenceType = SequenceType.SPLICED_DNA;
		String sequence = "ATGAAGATTAATATATTGAAGAAAGGCAAAAAATTTTATATTACAAACAATCATTTTAATTATGATATTAAACGAAATTTTACAATATTTCAAAACTCATTTATAAAAACAAACGATATAGTTTATAGAAAAAACATTGATATTGTTTGTGCAAAAGATTTATTCTTTTATACAATTCTAAATGTAGATAGATATAAGTATTTTCTACCATATGTAACGGATAGCAAGATAACAGAAAAAAACAAAGAATATTTTAAAGCCAATTTACAAATTGAGAATATTTTCTTTAAAGAAAAATATGACTCTTTAATTCAATTCATTTACCCAACAACAATTACGGTATCTAGCGAAGATACAAATATTTTTCATCACTTGATAACCGAGTGGATAATTAAAGAAAAAAAGAATTGCATAAACATTGATTTTTATATAAATTTTAGGTTGAAAAATAAAATATATCAAAATTTTATGAATTTATACATAAAAGAGTTAGGGAAAAAAATTTTATATTCCTTTATAAATGAATCAAAATCAAACAGTTATAAAAACACGGACGTTTTACACTTGATAAAATAG";
		StringWriter writer = new StringWriter();
		runProcess(writer, sequenceType);
		checkSequence(writer, sequence);
	}
	
	@Test
	public void test4() throws QueryException, IOException {
		SequenceType sequenceType = SequenceType.INTERGENIC_3;
		prime3 = 100;
		String sequence = "AATTTGTTTTCGTAACCTCTCCTCTTTTTATTTTTATTTTTATATTTATTTTTATTTTCATTTTTATTTATTTTTTTGTTTTGTCCTGATATGCCATATA";
		StringWriter writer = new StringWriter();
		runProcess(writer, sequenceType);
		checkSequence(writer, sequence);
	}
	
	@Test
	public void test5() throws QueryException, IOException {
		SequenceType sequenceType = SequenceType.INTERGENIC_5;
		prime5 = 100;
		String sequence = "TTTGAACAAGTCATATTCCAAAAAAAAAGAAAGTTATACATGTATTATTTTATTTTGGAAATGAAAAAAAATATATATATATATATATATATATTATATT";
		StringWriter writer = new StringWriter();
		runProcess(writer, sequenceType);
		checkSequence(writer, sequence);
	}
	
	@Test
	public void test6() throws QueryException, IOException {
		SequenceType sequenceType = SequenceType.INTERGENIC_3and5;
		prime5 = 100;
		prime3 = 100;
		String sequence = "AATTTGTTTTCGTAACCTCTCCTCTTTTTATTTTTATTTTTATATTTATTTTTATTTTCATTTTTATTTATTTTTTTGTTTTGTCCTGATATGCCATATA";
		sequence += "ATGAAGATTAATATATTGAAGAAAGGCAAAAAATTTTATATTACAAACAATCATTTTAATTATGATATTAAACGAAATTTTACAATATTTCAAAACTCATTTATAAAAACAAACGATATAGTTTATAGAAAAAACATTGATATTGTTTGTGCAAAAGATTTATTCTTTTATACAATTCTAAATGTAGATAGATATAAGTATTTTCTACCATATGTAACGGTAAGCATAAATATATTCATATTTTTAAAACGTATGCGGATGTTGTTTATGTGTGTACATATATTTGTTCATATAAATTGTTACATTTGTTCATATAAATTGTTACATTTGTTCATGTAAATTGTTATATTTGTTCATATAAATTGTTACATTTGTTCATGTAAATTGTTATATTTGTTCATGTAAATTGTTACATTTGTTCATGTAAATTGTTATATTTGTTCATGTAAATTGTTACATTTGTTCATGTAAATTGTTACATTTATTCATGTAAATTGTTACATTTGTTCATGTAAATTGTTACATTTATTCATGTAAATTGTTACATTTGTTCATGTAAATTGTTATATTTGTTCATGTAAAATTTTATATTTGTTCATATATTTTTGCACATTTATTTTTTAAAAATTATGAGCAATTGTATTGTATATTTGTAGGATAGCAAGATAACAGAAAAAAACAAAGAATATTTTAAAGCCAATTTACAAATTGAGAATATTTTCTTTAAAGAAAAATATGACTCTTTAATTCAATTCATTTACCCAACAACAATTACGGTAATAAGACTTTAAAGAGGAAATAAATGAATGAGAAGAAATATAAGTATATTTCGTATATAAATGTGTAAATATATACATACATATATATATATATATATATATATATATATATATATATAAATACTTAGACATTGTTGCATTTTTTTATTTTATTTTATTTTATTTTATTTTATTTAGGTATCTAGCGAAGATACAAATATTTTTCATCACTTGGTATGTAATAATAATAAAGAGACAACTAATTAAAAATATAAACAGAAAAATTAATATATATACTTATATATATTTTAAATAAGGCACATATCGTGTGTCCATTATAATATATATATATATATATATATATTGTTCTTTTTATAGATAACCGAGTGGATAATTAAAGAAAAAAAGAATTGCATAAACATTGATTTTTATATAAATTTTAGGGTAACAAAAATGTTGTATATATATTATATATGTATATATGTGTGTGTGTAAATATACTATATTTTATGTATCTGTGAATTTCTCAAAATTTTAATAAGTTTACATTATTCATAAAGTATTCACTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTCTGCTTAGTTGAAAAATAAAATATATCAAAATTTTATGAATTTATACATAAAAGAGTTAGGGAAAAAAATTTTATATTCCTTTATAAATGAATCAAAATCAAACAGTTATAAAAACACGGACGTTTTACACTTGATAAAATAG";
		sequence += "TTTGAACAAGTCATATTCCAAAAAAAAAGAAAGTTATACATGTATTATTTTATTTTGGAAATGAAAAAAAATATATATATATATATATATATATTATATT";
		StringWriter writer = new StringWriter();
		runProcess(writer, sequenceType);
		checkSequence(writer, sequence);
	}
	
	private void runProcess(StringWriter writer, SequenceType sequenceType) throws QueryException, IOException {
		
		DownloadProcess process = new DownloadProcess(
				outputFormat, 
				custFields, 
				outputDestination, 
				sequenceType, 
				includeHeader, 
				fieldSeparator, 
				blankField, 
				fieldInternalSeparator, 
				prime3, 
				prime5, 
				email, 
				uniqueNames, 
				historyItemName, 
				description, 
				util, 
				"http://someurl.com");
		
		
		process.generateFASTA(writer);
		
	}
	
	private void checkSequence(StringWriter writer, String sequence) throws IOException {

		BufferedReader reader = new BufferedReader(new StringReader(writer.toString()));
		String s;
		StringBuffer sb = new StringBuffer();
		while((s = reader.readLine()) != null) {
			
			// only read sequence lines
			if (! s.startsWith(">")) {
				
				// remove * chars
				if (s.contains("*")) {
					s = s.replace("*", "");
				}
				
				sb.append(s.trim().toUpperCase());
			}
			
		}
		
		reader.close();
		
		// System.out.println("Expected");
		// System.out.println(sequence);
		
		// System.out.println("Result");
		// System.out.println(sb.toString());
		
		Assert.assertEquals(sb.toString().length(), sequence.length());
		Assert.assertEquals(sb.toString(), sequence);
	}
	
}
