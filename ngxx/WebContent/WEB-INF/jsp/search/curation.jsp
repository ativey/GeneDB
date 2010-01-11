<%@ include file="/WEB-INF/jsp/topinclude.jspf" %>
<%@ taglib prefix="db" uri="db" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<format:header title="Curation Search" />
<format:page>
<br>

<div id="geneDetails">
    <format:genePageSection id="nameSearch" className="whiteBox">
        <form:form commandName="query" action="/Query/curation" method="GET">
            <table>
                <tr>
                    <td colspan="3">
                        <font color="red"><form:errors path="*" /></font>
                    </td>
                </tr>
                <tr>
                  <td>Curation:</td>
                  <td>
                    <form:input id="curation" path="search"/>
                  </td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td colspan="2">
                    <input type="submit" value="Submit" />
                  </td>
                  <td>&nbsp;</td>
                </tr>
            </table>

        </form:form>
    </format:genePageSection>
</div>
</format:page>
