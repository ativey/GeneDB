package org.genedb.top.db.domain.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SemanticLog {

    private static final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void log(String msg, Object... args) {

        logger.info(String.format(msg, args));

    }

}
