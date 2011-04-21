package org.jboss.tools.vpe.spring.test.springtest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * 
 * @author Yahor Radtsevich (yradtsevich)
 */
public class FormController implements Controller {

    protected final Log logger = LogFactory.getLog(getClass());

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String viewName = request.getServletPath().replace(".htm", "");
        FormBean formBean = new FormBean();
        return new ModelAndView(viewName, "formBean", formBean);
    }
}
