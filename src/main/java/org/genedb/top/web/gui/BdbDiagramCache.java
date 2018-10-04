//package org.genedb.web.gui;
//
//import org.genedb.web.mvc.model.BerkeleyMapFactory;
//
//import org.slf4j.Logger;import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Required;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//
//public class BdbDiagramCache implements DiagramCache {
//
//    private static final Logger logger = LoggerFactory.getLogger(BdbDiagramCache.class);
//
//    private BerkeleyMapFactory bmf;
//
//    @Override
//    public String fileForContextMap(RenderedContextMap renderedContextMap)
//            throws IOException, ImageCreationException {
//
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        renderedContextMap.writeTo(out);
//        out.close();
//
//        bmf.getImageMap().put(renderedContextMap.getKey(), out.toByteArray());
//        return renderedContextMap.getKey();
//    }
//
//    @Override
//    public String fileForProteinMap(RenderedProteinMap renderedProteinMap)
//            throws IOException, ImageCreationException {
//        // TODO Auto-generated method stub
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        renderedProteinMap.writeTo(out);
//        out.close();
//
//        bmf.getImageMap().put(renderedProteinMap.getKey(), out.toByteArray());
//        return renderedProteinMap.getKey();
//    }
//
////    @Override
////    public String getBaseUri() {
////        // TODO Auto-generated method stub
////        return null;
////    }
//
//    @Required
//    public void setBerkeleyMapFactory(BerkeleyMapFactory bmf) {
//        this.bmf = bmf;
//    }
//
//}
