package org.genedb.top.web.mvc.controller.download;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.genedb.top.web.mvc.controller.WebConstants;

public class WebUtils {

	private static Logger logger = LoggerFactory.getLogger(WebUtils.class);

	public static void setFlashMessage(String message, HttpSession session) {
		logger.error("Setting flash message '"+message+"' in session '"+session.getId()+"'");
		session.setAttribute(WebConstants.FLASH_MSG, message);
	}

	public static void removeFlashMessage(HttpSession session) {
		session.removeAttribute(WebConstants.FLASH_MSG);
	}

}
