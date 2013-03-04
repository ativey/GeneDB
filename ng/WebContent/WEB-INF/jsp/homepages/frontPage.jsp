<%@ include file="/WEB-INF/jsp/topinclude.jspf" %>
<%@ taglib prefix="db" uri="db" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="misc" uri="misc" %>
<format:header title="Homepage" />
<format:page>
<br>

<misc:url value="/Homepage/" var="base2" />
<div id="col-1-1">
<h1>Datasets</h1>
<div class="main-light-grey-top"></div>
<div class="main-light-grey">
<table cellpadding="0" cellspacing="0" width="100%" class="dataset-table">
<tr>
<td align="center" valign="top" width="33%">
<h3>Apicomplexan Protozoa</h3>
<img src="<misc:url value="/includes/image/dataset-apicomplexan-protozoa.jpg"/>" height="163" width="136" alt="Apicomplexan Protozoa" />
<db:homepageselect top="Apicomplexa" baseUrl="${base2}" />
</td>
<td align="center" valign="top" width="33%">

<h3>Kinetoplastid Protozoa</h3>
<img src="<misc:url value="/"/>includes/image/dataset-kinetoplastid-protozoa.jpg" height="163" width="136" alt="Kinetoplastid Protozoa" />
<db:homepageselect top="Kinetoplastida" baseUrl="${base2}" leafOnly="true" />
</td>
<td align="center" valign="top" width="33%">
<h3>Parasitic Helminths</h3>
<img src="<misc:url value="/"/>includes/image/dataset-parasitic-helminths.jpg" height="163" width="136" alt="Parasitic Helminths" />
<db:homepageselect top="Helminths" baseUrl="${base2}" leafOnly="true" />
</td>

</tr>

<tr>
<td align="center" valign="top" width="33%">
<h3>Bacteria</h3>
<img src="<misc:url value="/"/>includes/image/dataset-bacteria.jpg" height="163" width="136" alt="Bacteria" />
<db:homepageselect top="Bacteria" baseUrl="${base2}" leafOnly="true"/>
</td>
<td align="center" valign="top" width="33%">
<h3>Parasite Vectors</h3>
<img src="<misc:url value="/includes/image/dataset-parasite-vectors.jpg"/>" height="163" width="136" alt="Parasite Vectors" />
<select name="organism" onChange="if (this.selectedIndex != 0) {document.location.href=this.value }"><option value="none">Select an organism</option><option value="${baseUrl}Page/parasiteVectors">Parasite Vectors</option></select>
</td>
<td align="center" valign="top" width="33%">
<h3>Viruses</h3>
<img src="<misc:url value="/includes/image/dataset-viruses.jpg"/>" height="163" width="136" alt="Viruses" />
<select name="organism" onChange="if (this.selectedIndex != 0) {document.location.href=this.value }"><option value="none">Select an organism</option><option value="${baseUrl}Page/virus">Viruses</option></select>

</td>
</tr>
</table>
</div>
<div class="main-light-grey-bot"></div>

</div><!-- end main content column -left -->

<div id="col-1-2">
<h1>March 4th 2013: BLAST problems</h1>
<div class="light-grey-top"></div>
<div class="light-grey">
<p>&raquo;We are currently having problems with the GeneDB BLAST server, which we are trying to fix. We do not, as yet, have an estimated time for BLAST services being restored to GeneDB.<br>
</div>
<h1>Publications</h1>
<div class="baby-blue-top"></div>
<div class="baby-blue">
<p>
&raquo; A <a href="http://nar.oxfordjournals.org/content/early/2011/11/23/nar.gkr1032.abstract">GeneDB publication</a> is available to access online! <br>
</div>
<h1>News</h1>
<div class="light-grey-top"></div>
<div class="light-grey">
<p class="block-para">Data <br />
&raquo;The Parasite Genomics team are happy to announce that the <i>Taenia solium</i> genome, assembled and annotated by the <i>Taenia solium</i> sequencing consortium, is now publicly available. The genome can be accessed from <a href="http://www.taeniasolium.unam.mx/taenia">their homepage</a> and downloaded from an <a href="ftp://bioinformatica.biomedicas.unam.mx/TsM1_13.12.11/">FTP-site</a>.<br>
&raquo; As the locus tag prefix EmW is already in use in the <a href="http://www.insdc.org/">INSDC</a> databases, the prefix used for <a href="http://www.genedb.org/Homepage/Emultilocularis"><i>E. multilocularis</i></a> systematic IDs has been changed to EmuJ. The numbers after the prefix remain the same (e.g. EmW_001166400 has become EmuJ_001166400).<br>
&raquo; Version 3 of  <a href="http://www.genedb.org/Homepage/PfalciparumIT" ><i>P. falciparum</i> IT</a> is now available on GeneDB!<br>
&raquo; <b>2012-06-27</b>: Automatically generated gene product descriptions of <a href="http://www.genedb.org/Homepage/Smansoni"><i>Schistosoma mansoni</i></a> have been updated. Manually curated genes have not been altered. If you have any concerns please <a href="mailto:genedb-help@sanger.ac.uk">contact us</a>.<br>
</p>
</div>
<div class="light-grey-bot"></div>

<h1>Web services</h1>
<div class="baby-blue-top"></div>
<div class="baby-blue">
<p>
are now available for GeneDB : <br />
&raquo; <a href="http://www.genedb.org/services">Web services</a>
</p>
</div>
<div class="baby-blue-bot"></div>

<h1>Sequence searches</h1>
<div class="light-grey-top"></div>
<div class="light-grey">
<p class="block-para">Blast <br />

&raquo; Single organism <span class="dataset-table"><db:homepageselect title="Select an organism" top="Root" baseUrl="/blast/submitblast/GeneDB_" leafOnly="true" alwaysLink="true"/></span> <br />

&raquo; <a href="<misc:url value="/blast/submitblast/GeneDB_proteins/omni" />">Multi-organism (proteins)</a><br />
&raquo; <a href="<misc:url value="/blast/submitblast/GeneDB_transcripts/omni" />">Multi-organism (transcripts and contigs/chromosomes)</a><br />

 </p>


</div>
<div class="light-grey-bot"></div>


<h1>New mailing list</h1>
<div class="baby-blue-top"></div>
<div class="baby-blue">

<p>There's a mailing list for announcing new releases of the GeneDB
website, service interruptions etc. To subscribe please see <br>
<a href="http://lists.sanger.ac.uk/mailman/listinfo/genedb-info">http://lists.sanger.ac.uk/mailman/listinfo/genedb-info</a></p>
</div>
<div class="baby-blue-bot"></div>

<h1>Go to our</h1>
<div class="light-grey-top"></div>
<div class="light-grey">
&raquo; <a href="<misc:url value="/Query" />">Query page</a><br />
&raquo; <a href="<misc:url value="/web-artemis/"/>">Web Artemis</a><br />
&raquo; <a href="<misc:url value="/cgi-bin/amigo/go.cgi"/>">AmiGO</a><br />
&raquo; <a href="<misc:url value="/Page/jbrowse"/>">JBrowse</a><br />
</div>
<div class="light-grey-bot"></div>

<h1>Go to</h1>
<div class="light-grey-top"></div>
<div class="light-grey">
&raquo; <a href="http://old.genedb.org/">Previous GeneDB version</a><br />
&raquo; <a href="http://www.pombase.org/">Pombase</a><br />
</div>
<div class="light-grey-bot"></div>

<h1>Information</h1>
<div class="baby-blue-top"></div>
<div class="baby-blue">

<p>Data <br />
&raquo; <a href="<misc:url value="/Page/releases"/>">Data Release Policy</a><br />
</p>
<br />
<p>PSU Sequencing Projects <br />
&raquo; <a href="http://www.sanger.ac.uk/Projects/Microbes/">Prokaryotes</a><br />
&raquo; <a href="http://www.sanger.ac.uk/Projects/Protozoa/">Eukaryotes (Protozoa)</a><br />
</p>
<br />
<p>
Software<br />
&raquo; <a href="http://www.sanger.ac.uk/Software/ACT/">ACT</a><br />
&raquo; <a href="http://www.sanger.ac.uk/Software/Artemis/">Artemis</a><br />

</p>
<br />
<p>
Contributor<br />
&raquo; <a href="<misc:url value="/Page/acknowledgements" />">Acknowledgements</a><br />
</p>


</div>
<div class="baby-blue-bot"></div>
</div><!-- end sidebar content columb -right -->

</format:page>
