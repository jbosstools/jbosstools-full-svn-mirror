/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.shale.model.pv.handlers;

import java.util.Properties;

import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.meta.action.impl.*;
import org.jboss.tools.common.meta.action.impl.handlers.DefaultCreateHandler;
import org.jboss.tools.common.meta.action.impl.handlers.DefaultRemoveHandler;
import org.jboss.tools.common.model.*;
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;

public class EditShaleConfigurationSupport extends SpecialWizardSupport {
	static String DIALOG_CONFIG_FILES = "dialog config files";
	static String DIALOG_CONFIG_PARAM = "org.apache.shale.dialog.CONFIGURATION";

	static String CLAY_CONFIG_FILES = "clay config files";
	static String CLAY_CONFIG_PARAM = "clay-config-files";

	static String CLAY_TEMPLATE_SUFFIX = "clay template suffix";
	static String CLAY_TEMPLATE_PARAM = "clay-template-suffix";

	static String SHALE_FILTER = "shale filter";
	static String DEFAULT_SHALE_FILTER = "org.apache.shale.faces.ShaleApplicationFilter";
	static String FILTER_MAPPING = "shale filter mapping";

	static String CHAIN_CONFIG_FILES = "chain config files";
	static String CHAIN_CONFIG_PARAM = "org.apache.commons.chain.CONFIG_WEB_RESOURCE";
	
	static String CONTEXT_CONFIG_FILES = "context config files";
	static String CONTEXT_CONFIG_PARAM = "contextConfigLocation";
	
	static String CHAIN_LISTENER = "chain listener";
	static String DEFAULT_CHAIN_LISTENER = "org.apache.commons.chain.web.ChainListener";

	static String CLAY_LISTENER = "clay listener";
	static String DEFAULT_CLAY_LISTENER = "org.apache.shale.clay.config.ClayConfigureListener";

	static String SPRING_LISTENER = "spring listener";
	static String DEFAULT_SPRING_LISTENER = "org.springframework.web.context.ContextLoaderListener";

	public void reset() {
    	XModelObject webxml = WebAppHelper.getWebApp(target.getModel());
    	
    	XModelObject o = WebAppHelper.findWebAppContextParam(webxml, DIALOG_CONFIG_PARAM);
    	if(o != null) setAttributeValue(0, DIALOG_CONFIG_FILES, o.getAttributeValue("param-value"));
		
    	o = WebAppHelper.findWebAppContextParam(webxml, CLAY_CONFIG_PARAM);
    	if(o != null) setAttributeValue(0, CLAY_CONFIG_FILES, o.getAttributeValue("param-value"));
		
    	o = WebAppHelper.findWebAppContextParam(webxml, CLAY_TEMPLATE_PARAM);
    	if(o != null) setAttributeValue(0, CLAY_TEMPLATE_SUFFIX, o.getAttributeValue("param-value"));
		
    	o = WebAppHelper.findWebAppContextParam(webxml, CHAIN_CONFIG_PARAM);
    	if(o != null) setAttributeValue(0, CHAIN_CONFIG_FILES, o.getAttributeValue("param-value"));
		
    	o = WebAppHelper.findWebAppContextParam(webxml, CONTEXT_CONFIG_PARAM);
    	if(o != null) setAttributeValue(0, CONTEXT_CONFIG_FILES, o.getAttributeValue("param-value"));
    	
    	o = findListener(webxml, DEFAULT_CHAIN_LISTENER);
    	setAttributeValue(0, CHAIN_LISTENER, (o != null) ? "true" : "false");
		
    	o = findListener(webxml, DEFAULT_CLAY_LISTENER);
    	setAttributeValue(0, CLAY_LISTENER, (o != null) ? "true" : "false");
		
    	o = findListener(webxml, DEFAULT_SPRING_LISTENER);
    	setAttributeValue(0, SPRING_LISTENER, (o != null) ? "true" : "false");
    	
    	o = WebAppHelper.findFilterByClass(webxml, DEFAULT_SHALE_FILTER);
    	setAttributeValue(0, SHALE_FILTER, (o != null) ? "true" : "false");
    	if(o != null) {
    		String fn = o.getAttributeValue("filter-name");
    		o = WebAppHelper.findFilterMapping(webxml, fn);
    		if(o != null) setAttributeValue(0, FILTER_MAPPING, o.getAttributeValue("url-pattern"));
    	}
	}
	
	private XModelObject findListener(XModelObject webxml, String cls) {
		XModelObject folder = webxml.getChildByPath("Listeners");
		if(folder == null) folder = webxml;
		XModelObject[] os = folder.getChildren("WebAppListener");
		for (int i = 0; i < os.length; i++) {
			String c = os[i].getAttributeValue("listener-class");
			if(cls.equals(c)) return os[i];
		}
		return null;
	}
	
	public boolean isEnabled(XModelObject target) {
		if(!super.isEnabled(target)) return false;
    	XModelObject webxml = WebAppHelper.getWebApp(target.getModel());
		return webxml != null && webxml.isObjectEditable();
	}

	public void action(String name) throws Exception {
		if(FINISH.equals(name)) {
			execute();
			setFinished(true);
		} else if(CANCEL.equals(name)) {
			setFinished(true);
		}
	}
	
    public String[] getActionNames(int stepId) {
        return new String[]{FINISH, CANCEL, HELP};
    }
    
    protected void execute() throws Exception {
    	XModelObject webxml = WebAppHelper.getWebApp(getTarget().getModel());
    	Properties p0 = extractStepData(0);
    	changeWebAppContextParam(webxml, DIALOG_CONFIG_PARAM, p0.getProperty(DIALOG_CONFIG_FILES));
    	changeWebAppContextParam(webxml, CLAY_CONFIG_PARAM, p0.getProperty(CLAY_CONFIG_FILES));
    	changeWebAppContextParam(webxml, CLAY_TEMPLATE_PARAM, p0.getProperty(CLAY_TEMPLATE_SUFFIX));
    	changeWebAppContextParam(webxml, CHAIN_CONFIG_PARAM, p0.getProperty(CHAIN_CONFIG_FILES));
    	changeWebAppContextParam(webxml, CONTEXT_CONFIG_PARAM, p0.getProperty(CONTEXT_CONFIG_FILES));
    	updateListener(webxml, DEFAULT_CHAIN_LISTENER, p0.getProperty(CHAIN_LISTENER));
    	updateListener(webxml, DEFAULT_CLAY_LISTENER, p0.getProperty(CLAY_LISTENER));
    	updateListener(webxml, DEFAULT_SPRING_LISTENER, p0.getProperty(SPRING_LISTENER));
    	updateFilterAndFilterMapping(webxml, DEFAULT_SHALE_FILTER, p0.getProperty(SHALE_FILTER), p0.getProperty(FILTER_MAPPING));
    	
    	if(webxml.isModified()) {
    		XActionInvoker.invoke("SaveActions.Save", webxml, null);
    	}
    }
    
    void changeWebAppContextParam(XModelObject webxml, String param, String value) {
    	XModelObject o = WebAppHelper.findWebAppContextParam(webxml, value);
    	if(o == null && (value == null || value.trim().length() == 0)) return;
    	if(value == null || value.trim().length() == 0) {
    		DefaultRemoveHandler.removeFromParent(o);
    	} else {
    		WebAppHelper.setWebAppContextParam(webxml, param, value.trim());
    	}
    }
    
    void updateListener(XModelObject webxml, String cls, String value) {
    	XModelObject o = findListener(webxml, cls);
    	if(o == null && !"true".equals(value)) return;
    	if(!"true".equals(value)) {
    		DefaultRemoveHandler.removeFromParent(o);
    	} else if(o == null) {
    		o = webxml.getModel().createModelObject("WebAppListener", null);
    		o.setAttributeValue("listener-class", cls);
    		DefaultCreateHandler.addCreatedObject(webxml, o, getProperties());
    	}
    }
    
    void updateFilterAndFilterMapping(XModelObject webxml, String cls, String add, String mapping) {
    	XModelObject folder = webxml.getChildByPath("Filters");
    	if(folder == null) folder = webxml;
    	XModelObject o = WebAppHelper.findFilterByClass(webxml, cls);
    	XModelObject m = (o == null) ? null : WebAppHelper.findFilterMapping(webxml, o.getAttributeValue("filter-name"));
    	if(o == null && !"true".equals(add)) return;
    	if(!"true".equals(add)) {
    		DefaultRemoveHandler.removeFromParent(o);
    		if(m != null) DefaultRemoveHandler.removeFromParent(m);
    	} else {
    		if(o == null) {
    			o = webxml.getModel().createModelObject("WebAppFilter", null);
    			o.setAttributeValue("filter-name", "shale");
    			o.setAttributeValue("filter-class", cls);
    			DefaultCreateHandler.addCreatedObject(folder, o, getProperties());
    			m = WebAppHelper.findFilterMapping(webxml, o.getAttributeValue("filter-name"));
    		}
    		if(m != null) {
    			o.getModel().changeObjectAttribute(m, "url-pattern", mapping);
    		} else if(mapping != null && mapping.trim().length() > 0) {
    			m = webxml.getModel().createModelObject("WebAppFilterMapping", null);
    			m.setAttributeValue("filter-name", o.getAttributeValue("filter-name"));
    			m.setAttributeValue("url-pattern", mapping);
        		DefaultCreateHandler.addCreatedObject(folder, m, -1);
    		}
    	}
    }


}
