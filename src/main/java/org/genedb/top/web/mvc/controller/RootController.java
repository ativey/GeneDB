package org.genedb.top.web.mvc.controller;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 *
 * @author Adrian Tivey
 */
@Controller
@RequestMapping("/")
public class RootController {

    Logger logger = LoggerFactory.getLogger(RootController.class);


    @RequestMapping(method=RequestMethod.GET, value="/")
    public ModelAndView goToHomePage() {

        return new ModelAndView("redirect:/Homepage");
    }


}
