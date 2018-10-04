//package org.genedb.web.mvc.model;
//
//import java.io.File;
//import java.net.URL;
//
//import org.junit.Before;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public abstract class TestAbstractUpdater {
//    
//    @Autowired
//    BerkeleyMapFactory berkeleyMapFactory;
//    
//    @Before
//    public void setupBerkeleyMapRootFactory(){
//        String rootDir = berkeleyMapFactory.getRootDirectory();
//        File dir = new File(rootDir);
//        if (!dir.exists()){
//            dir.mkdirs();
//        }
//    }
//}
