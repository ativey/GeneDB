package org.genedb.top.web.mvc.controller;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.hazelcast.core.Hazelcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HazelCastServletContextListener implements ServletContextListener {
	
	private static final Logger logger = LoggerFactory.getLogger(HazelCastServletContextListener.class);
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("Starting up context listener.");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("Shutting down hazelcasts!");
		Hazelcast.shutdownAll();
	}

}
