package org.jboss.tools.vpe.spring.test.springtest.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.tools.vpe.spring.test.springtest.FormBean;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * 
 * @author Denis Vinnichek (dvinnichek)
 */
public class ErrorsController extends SimpleFormController  {

    protected final Log logger = LogFactory.getLog(getClass());
    public ErrorsController()	{
		setCommandClass(FormBean.class);
		setCommandName("formBean");
	}
}
