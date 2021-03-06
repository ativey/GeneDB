package org.genedb.top.web.mvc.controller.download;

//import org.displaytag.tags.TableTagParameters;
//import org.displaytag.util.ParamEncoder;

//import org.genedb.top.querying.tmpquery.SuggestQuery;
//import org.genedb.top.web.mvc.model.ResultsCacheFactory;

//import org.springframework.beans.factory.annotation.Autowired;


//
//@Controller
//@RequestMapping("/Results")
public class ResultsController {

//    private static final Logger logger = LoggerFactory.getLogger(ResultsController.class);
//
//    //private static final String IDS_TO_GENE_SUMMARY_QUERY = "idsToGeneSummary";
//
//    public static final int DEFAULT_LENGTH = 30;
//
//    public static final int ID_TO_GENE_SUMMARY_EXPANSION_BATCH = 10;
//    
//    private HistoryManagerFactory hmFactory;
//    
//    public void setHmFactory(HistoryManagerFactory hmFactory) {
//        this.hmFactory = hmFactory;
//    }
//    
//    //@Autowired
//    //private ResultsCacheFactory resultsCacheFactory;
//
////    public void setResultsCacheFactory(ResultsCacheFactory resultsCacheFactory) {
////        this.resultsCacheFactory = resultsCacheFactory;
////    }
//
//    @SuppressWarnings("unchecked")
//	public void setQueryFactory(QueryFactory queryFactory) {
//        this.queryFactory = queryFactory;
//    }
//
//    //@Autowired
//    @SuppressWarnings("unchecked")
//	private QueryFactory queryFactory;
//
//    @RequestMapping(method = RequestMethod.GET)
//    public String setUpForm() {
//        return "redirect:/QueryList";
//    }
//
//    @RequestMapping(method = {RequestMethod.GET} , value="/{key}")
//    public String setUpForm(
//            @PathVariable(value="key") String key,
//            @RequestParam(value="taxonNodeName", required=false) String taxonNodeName,
//            HttpServletRequest request,
//            HttpSession session,
//            Model model) throws QueryException {
//        // TODO Do we want form submission via GET?
//
//        if (!StringUtils.hasText(key)) {
//        	WebUtils.setFlashMessage("Unable to identify which query to use", session);
//            return "redirect:/QueryList";
//        }
//
//        String pName = new ParamEncoder("row").encodeParameterName(TableTagParameters.PARAMETER_PAGE);
//        logger.debug("pName is '"+pName+"'");
//        String startString = request.getParameter((new ParamEncoder("row").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
//        logger.debug("The start string is '"+startString+"'");
//
//        // Use 1-based index for start and end
//        int start = 1;
//        if (startString != null) {
//            start = (Integer.parseInt(startString) - 1) * DEFAULT_LENGTH + 1;
//        }
//
//        int end = start + DEFAULT_LENGTH;
//        
//        logger.debug(key);
//        //logger.debug(resultsCacheFactory);
//        //logger.debug(resultsCacheFactory.getResultsCacheMap());
//        
////        if (!resultsCacheFactory.getResultsCacheMap().containsKey(key)) {
////        	WebUtils.setFlashMessage("Unable to retrieve results for this key", session);
////            logger.error("Unable to retrieve results for key '"+key+"'");
////            return "redirect:/QueryList";
////        }
//        
//        
//        
//        //ResultEntry resultEntry = resultsCacheFactory.getResultsCacheMap().get(key);
//        HistoryManager hm = hmFactory.getHistoryManager(session);
//        HistoryItem item = hm.getHistoryItemByName("query"+key);
//        
//        logger.info("item:" + item);
//        
//        Query query = item.getQuery();
//        
//        logger.info("Found results for query (Query: '"+query.getQueryName()+" 'Session: '"+session.getId()+"' key: '"+key+"')-");
//        
//        //List<GeneSummary> results = query.getResults();
//        
//        String queryName = queryFactory.getRealName(query);
//        
//        Integer resultSize = item.getNumberItems();
//
//        
//        logger.debug("The number of results retrieved is '"+resultSize+"'");
//        logger.debug("The end marker, before adjustment, is '"+end+"'");
//
//        if (end > resultSize + 1) {
//            end = resultSize + 1;
//        }
//
//        // List<GeneSummary> possiblyExpanded = possiblyExpandResults(results);
//
////        if (possiblyExpanded == null) {
////            possiblyExpanded = results;
////            logger.debug("The subset is already expanded");
////        } else {
////            // Need to update cache
////            logger.debug("We've expanded the systematic ids");
////            resultEntry.results = possiblyExpanded;
////            resultsCacheFactory.getResultsCacheMap().put(key, resultEntry);
////            logger.debug("And stored the set back");
////        }
//
//        model.addAttribute("results", query.getResults());
//        model.addAttribute("resultsSize", resultSize);
//        model.addAttribute("key", key);
//        model.addAttribute("firstResultIndex", start);
//        
//        //model.addAttribute("isMaxResultsReached", Boolean.valueOf(resultEntry.query.isMaxResultsReached()));
//        
//        if (taxonNodeName != null) {
//            model.addAttribute("taxonNodeName", taxonNodeName);
//        }
////        if (resultEntry.taxonGroup != null) {
////            model.addAttribute("taxonGroup", resultEntry.taxonGroup);
////        }
//
//        if (query != null) {
//            
//            populateModelData(model, query);
//            
//            if (queryName.equals("quickSearch")) {
//            	
//            	QuickSearchQuery quickSearchQuery = (QuickSearchQuery) query;
//            	model.addAttribute("query", quickSearchQuery);
//            	logger.info("taxonGroup : " + quickSearchQuery.getQuickSearchQueryResults().getTaxonGroup());
//            	model.addAttribute("taxonGroup", quickSearchQuery.getQuickSearchQueryResults().getTaxonGroup());
//            	
//            	if (resultSize == 0) {
//	            	SuggestQuery squery = (SuggestQuery) queryFactory.retrieveQuery("suggest", NumericQueryVisibility.PRIVATE);
//	            	squery.setSearchText(quickSearchQuery.getSearchText());
//	            	squery.setTaxons(quickSearchQuery.getTaxons());
//	            	squery.setMax(30);
//	
//					List sResults = squery.getResults();
//					model.addAttribute("suggestions", sResults);
//            	}
//            	
//            } else {
//            	model.addAttribute("query", query);
//            }
////            	// quick search query has a special path...
////            	model.addAttribute("actionName" , request.getContextPath() + "/QuickSearchQuery");
////            } else {
////            	
////            }
////            
//            model.addAttribute("actionName" , request.getContextPath() + "/Query/" + queryName);
//            
//            return "search/"+ queryName;
//        }
//        
//        return "list/results2";
//    }
//
//    /**
//     * Expand the current resultset, i.e. to initialise more fields in the list of GeneSummary instances
//     * @param results The un-expanded resultset
//     * @return expanded The expanded resultset
//     * @throws QueryException
//     */
////    private List<GeneSummary> possiblyExpandResults(List<GeneSummary> results) throws QueryException {
////        List<String> subset = new ArrayList<String>();
////        List<GeneSummary> expanded = new ArrayList<GeneSummary>();
////
////        for(int i=0; i<results.size(); ++i){
////
////            subset.add(results.get(i).getSystematicId());
////            if (i % ID_TO_GENE_SUMMARY_EXPANSION_BATCH == 0
////                    || i+1 == results.size()){
////                //expand current batch
////                List<GeneSummary> converts = convertIdsToGeneSummaries(subset);
////                expanded.addAll(converts);
////                subset.clear();
////            }
////        }
////
////        //Sort overall result
////        Collections.sort(expanded);
////        return expanded;
////    }
//
////    @SuppressWarnings("unchecked")
////	private List<GeneSummary> convertIdsToGeneSummaries(List<String> ids) throws QueryException {
////        IdsToGeneSummaryQuery idsToGeneSummary =
////            (IdsToGeneSummaryQuery) queryFactory.retrieveQuery(IDS_TO_GENE_SUMMARY_QUERY, NumericQueryVisibility.PRIVATE);
////
////        if (idsToGeneSummary == null) {
////            throw new RuntimeException("Internal error - unable to find ids to gene summary query");
////        }
////        idsToGeneSummary.setIds(ids);
////        return (List<GeneSummary>)idsToGeneSummary.getResults();
////    }
//
//    private void populateModelData(Model model, Query query) {
//        Map<String, Object> modelData = query.prepareModelData();
//        for (Map.Entry<String, Object> entry : modelData.entrySet()) {
//        	logger.info("Populating " + entry.getKey());
//            model.addAttribute(entry.getKey(), entry.getValue());
//        }
//    }

}
