//package org.genedb.web.mvc.controller;
//
//import org.genedb.top.db.dao.SequenceDao;
//import org.genedb.top.web.mvc.model.BerkeleyMapFactory;
//
//import org.genedb.top.chado.feature.TopLevelFeature;
//
//import org.slf4j.Logger;import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import java.io.IOException;
//import java.io.Writer;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Controller
//@RequestMapping("/ContextMap")
//public class ContextMapController {
//    /*
//     * More than 30,000 pixels per tile, and almost all browsers
//     * fail. More than 5,000 or so, and Linux Firefox crashes.
//     */
//    //private static final int TILE_WIDTH = 5000; // in pixels
//
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    private BerkeleyMapFactory bmf;
//
//    @Autowired
//    private SequenceDao sequenceDao;
//
//    @RequestMapping(method=RequestMethod.GET)
//    public void writeMapInfo(HttpServletRequest request, Writer out,
//            HttpServletResponse response, String chromosome) throws IOException {
//
//
//        TopLevelFeature tlf = sequenceDao.getFeatureByUniqueName(chromosome, TopLevelFeature.class);
//        if (tlf == null) {
//
//        }
//        if ( ! tlf.isTopLevelFeature()) {
//            // TODO
//        }
//
//
//        String text = bmf.getContextMapMap().get(tlf.getFeatureId());
//        if (text != null) {
//            logger.trace("Cache hit for context map '"+chromosome+"' of '"+text+"'");
//        } else {
//            logger.debug(String.format("The context maps for '%s' aren't cached and need to be generated", chromosome));
//        }
//
//        out.write(text);
//    }
//
//    public void setBerkeleyMapFactory(BerkeleyMapFactory bmf) {
//        this.bmf = bmf;
//    }
//
//}
