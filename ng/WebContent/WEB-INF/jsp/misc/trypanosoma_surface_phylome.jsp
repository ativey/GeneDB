<%@ include file="/WEB-INF/jsp/topinclude.jspf" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<format:header title="The African trypanosome surface phylome" />
<format:page>

<div id="col-2-1">

<h1>
    The African Trypanosome Cell Surface Phylome
</h1>

<div id="surface_phylome"></div>

<format:genePageSection>

<div style="width:750px">

The taxonomic distribution of gene families with putative cell-surface roles, displayed in a Venn diagram. Each circle represents a family. The 
label in each circle refers to the description key, while size reflects the number of genes it contains; for large families the absolute number 
is shown in parentheses. For families present in multiple species, a pie chart is shown indicating relative gene numbers. The three tabs attending 
each species domain show the number of single-copy genes, pairs and triplets also predicted to have cell surface roles and to be species-specific 
(e.g. 101 singletons in <i>T. brucei</i>).

<h2>Instructions for use</h2>
<P>
 Mouse over a circle to access the data and phylogenetic analysis for a given family. Bayesian and Maximum likelihood phylogenies are available in 
 NEWICK format, as well as the multiple sequence alignments used in their estimation, in PHYLIP and NEXUS formats. For certain families a supplementary 
 figure is also available describing the phylogeny in its comparative genomic context.</P>
 <P>Additional files including VSG superfamily trees and a table describing the surface phylome are 
 available from the menu to the bottom left of the Venn diagram.</P>
<h2>Method</h2>
<P>The surface phylome was created by extracting all genes with a predicted glycophosphatidylinositol (GPI) anchor, signal peptide or transmembrane helix 
from genomes of <em>T. brucei</em> 927, <em>T. congolense IL3000</em> and <em>T. vivax</em> Y486, sequenced by the Pathogen Genomics group at the Wellcome Trust Sanger Institute. 
Each gene was compared against all genes from the three species using wuBLAST and where >3 homologs were found in at least one species, these constituted 
a family. These family members were then subtracted from the total list of surface-expressed genes before proceeding to the next search. Gene families 
with known or predicted associations to organellar membranes were subsequently removed. Bayesian (MrBayes v3.1.2.) and Maximum Likelihood (PHYML v3.0) 
phylogenies were estimated from manual alignments of all homologous protein sequences.</P>
<P>
Homologous sequences from the non-African trypanosome species <em>Trypanosoma cruzi</em> CL-Brener were used  as outgroups when possible. In some cases there were 
many <em>T. cruzi</em> homologs; not all of these are necessarily included in the taxon sets. However, where a sample of <em>T. cruzi</em> sequences has been taken, this 
is representative of total diversity in <em>T. cruzi</em>.
</P>

<h2>Comments or questions? </h2> 
Email Dr Andrew Jackson at the Wellcome Trust Sanger Institute (<a href="mailto:aj4@sanger.ac.uk">aj4@sanger.ac.uk</a>).

<h2>Credit</h2>
<P>Jackson, et al. (2011). Divergent evolution of antigenic variation in African trypanosomes. Manuscript submitted.</P>
 
 </div>

</format:genePageSection>

<script>

$(function(){
	
    //if (console) {
     //   console.log($.browser);
    //}
    
	if ($.browser.safari) {
		$('#surface_phylome').html("<iframe src='/cgi-bin/surface_phylome/surface_phylome.pl' frameborder=0 height='920' width='920'></iframe> ");
	} else  {
		$('#surface_phylome').load('/cgi-bin/surface_phylome/surface_phylome.pl');
	}
	
	
});



</script>

</div>
</format:page>
