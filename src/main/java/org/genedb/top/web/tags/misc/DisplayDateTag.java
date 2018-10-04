package org.genedb.top.web.tags.misc;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class DisplayDateTag extends SimpleTagSupport {

    private static final Logger logger = LoggerFactory.getLogger(DisplayDateTag.class);

    private long time;

    private String message;


    @Override
    public void doTag() throws JspException, IOException {

        if (time <= 0) {
            return;
        }

        Date date = new Date(time);
        PageContext pc = (PageContext) getJspContext();
        JspWriter out = pc.getOut();
        String displayDate = DateFormat.getDateInstance(DateFormat.LONG).format(date);
        logger.error(String.format("Displaying '%d' as '%s'", time, displayDate));
        out.println(message + " " + displayDate);
    }

    public void setTime(long time) {
        this.time = time;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

}
