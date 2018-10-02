package org.genedb.top.web.mvc.model.load;

import org.slf4j.Logger;

import java.util.Date;


public class TimerHelper {

    
    public static void printTimeLapse(Logger logger, Date startTime, String queryName){
        Date timeAfter = new Date();
        long timeTaken =  timeAfter.getTime() - startTime.getTime();
        if(timeTaken < 1000){
            logger.info(queryName + " execution time = "+ timeTaken + " millisecs");
            
        }else if(timeTaken > 60000){
            logger.info(queryName + " execution time = "+ timeTaken/60000 + "." + (timeTaken%60000)/1000 +" mins");
            
        }else{
            logger.info(queryName + " execution time = "+ timeTaken/1000 + "." + timeTaken%1000 +" secs");
        }
    }
}
