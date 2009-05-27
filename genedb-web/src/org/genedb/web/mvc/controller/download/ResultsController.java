package org.genedb.web.mvc.controller.download;

import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;

import org.genedb.querying.core.Query;
import org.genedb.querying.core.QueryException;
import org.genedb.querying.core.QueryFactory;
import org.genedb.querying.tmpquery.GeneSummary;
import org.genedb.querying.tmpquery.IdsToGeneSummaryQuery;
import org.genedb.web.mvc.controller.WebConstants;
import org.genedb.web.mvc.model.ResultsCacheFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/Results")
public class ResultsController {

    private static final Logger logger = Logger.getLogger(ResultsController.class);

    private static final String IDS_TO_GENE_SUMMARY_QUERY = "idsToGeneSummary";

    public static final int DEFAULT_LENGTH = 30;

    //@Autowired
    private ResultsCacheFactory resultsCacheFactory;

    public void setResultsCacheFactory(ResultsCacheFactory resultsCacheFactory) {
        this.resultsCacheFactory = resultsCacheFactory;
    }

    public void setQueryFactory(QueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    //@Autowired
    private QueryFactory queryFactory;

    @RequestMapping(method = RequestMethod.GET)
    public String setUpForm() {
        return "redirect:/QueryList";
    }

    @RequestMapping(method = {RequestMethod.GET} , params= "key")
    public String setUpForm(
            @RequestParam(value="key") String key,
            @RequestParam(value="taxonNodeName", required=false) String taxonNodeName,
            ServletRequest request,
            HttpSession session,
            Model model) throws QueryException {
        // TODO Do we want form submission via GET?

        if (!StringUtils.hasText(key)) {
            session.setAttribute(WebConstants.FLASH_MSG, "Unable to identify which query to use");
            return "redirect:/QueryList";
        }

        String pName = new ParamEncoder("row").encodeParameterName(TableTagParameters.PARAMETER_PAGE);
        logger.debug("pName is '"+pName+"'");
        String startString = request.getParameter((new ParamEncoder("row").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
        logger.debug("The start string is '"+startString+"'");
        int start = 0;
        if (startString != null) {
            start = (Integer.parseInt(startString) - 1) * DEFAULT_LENGTH;
        }

        int end = start + DEFAULT_LENGTH;

        if (!resultsCacheFactory.getResultsCacheMap().containsKey(key)) {
            session.setAttribute(WebConstants.FLASH_MSG, "Unable to retrieve results for this key");
            logger.error("Unable to retrieve results for key '"+key+"'");
            return "redirect:/QueryList";
        }

        ResultEntry resultEntry = resultsCacheFactory.getResultsCacheMap().get(key);
        List<GeneSummary> results = resultEntry.results;

        logger.debug("The number of results retrived from cache is '"+results.size()+"'");
        logger.debug("The end marker, before adjustment, is '"+end+"'");

        if (end >= results.size()) {
            end = results.size() - 1;
        }


        boolean justSome = true;
        List<GeneSummary> subset;
        if (start == 1 && end == results.size()-1) {
            subset = results;
            justSome = false;
            logger.debug("The \"subset\" is all of the results!");
        } else {
            subset = results.subList(start, end);
            logger.debug(String.format("The \"subset\" is from %d to %d of %d total results!", start, end, results.size()));
        }

        List<GeneSummary> possiblyExpanded = possiblyExpandResults(subset);

        if (possiblyExpanded == null) {
            possiblyExpanded = subset;
            logger.debug("The subset is already expanded");
        } else {
            // Need to update cache
            logger.debug("We've expanded the systematic ids");
            if (!justSome) {
                resultEntry.results = possiblyExpanded;
                resultsCacheFactory.getResultsCacheMap().put(key, resultEntry);
                logger.debug("And stored the set back");
            }
        }

        model.addAttribute("results", possiblyExpanded);
        model.addAttribute("resultsSize", results.size());
        model.addAttribute("key", key);
        model.addAttribute("firstResultIndex", start);
        if (taxonNodeName != null) {
            model.addAttribute("taxonNodeName", taxonNodeName);
        }

        if (resultEntry.query != null) {
            model.addAttribute("query", resultEntry.query);
            populateModelData(model, resultEntry.query);
            return "search/"+resultEntry.queryName;
        }
        return "list/results2";
    }

    private List<GeneSummary> possiblyExpandResults(List<GeneSummary> results) throws QueryException {
        boolean needToExpand = false;
        List<String> ids = Lists.newArrayListWithExpectedSize(results.size());
        for (GeneSummary geneSummary : results) {
            if ( ! geneSummary.isConfigured()) {
                needToExpand = true;
            }
            ids.add(geneSummary.getSystematicId());
        }

        if (! needToExpand) {
            return null;
        }

        List<GeneSummary> expanded = convertIdsToGeneSummaries(ids);
        return expanded;
    }

    private List<GeneSummary> convertIdsToGeneSummaries(List<String> ids) throws QueryException {
        IdsToGeneSummaryQuery idsToGeneSummary = (IdsToGeneSummaryQuery) queryFactory.retrieveQuery(IDS_TO_GENE_SUMMARY_QUERY);
        if (idsToGeneSummary == null) {
            throw new RuntimeException("Internal error - unable to find ids to gene summary query");
        }
        idsToGeneSummary.setIds(ids);
        return (List<GeneSummary>)idsToGeneSummary.getResults();
    }

    private void populateModelData(Model model, Query query) {
        Map<String, Object> modelData = query.prepareModelData();
        for (Map.Entry<String, Object> entry : modelData.entrySet()) {
            model.addAttribute(entry.getKey(), entry.getValue());
        }
    }

}
