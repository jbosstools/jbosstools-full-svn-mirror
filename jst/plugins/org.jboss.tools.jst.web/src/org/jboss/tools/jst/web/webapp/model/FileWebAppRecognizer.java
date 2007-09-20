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
package org.jboss.tools.jst.web.webapp.model;

import java.io.IOException;

import org.jboss.tools.common.model.loaders.*;
import org.jboss.tools.common.xml.XMLEntityResolver;
import org.jboss.tools.jst.web.WebModelPlugin;

public class FileWebAppRecognizer implements EntityRecognizer {
    static {
        try {
            XMLEntityResolver.registerPublicEntity(WebAppConstants.DOC_PUBLICID, FileWebAppRecognizer.class, "/meta/web-app_2_3.dtd");
            XMLEntityResolver.registerPublicEntity(WebAppConstants.DOC_PUBLICID_2_3, FileWebAppRecognizer.class, "/meta/web-app_2_3.dtd");
        } catch (IOException e) {
        	WebModelPlugin.getPluginLog().logError(e);
        }
    }

    public String getEntityName(String ext, String body) {
    	if((body == null || !"xml".equals(ext))) return null;
        int i = body.indexOf("<!DOCTYPE");
        if(i >= 0) {
        	int j = body.indexOf(">", i);
        	if(j < 0) return null;
        	String dt = body.substring(i, j);
        	if(dt.indexOf("web-app") < 0) return null;
        	if(dt.indexOf(WebAppConstants.DOC_PUBLICID) > 0) return "FileWebApp";
        	if(dt.indexOf(WebAppConstants.DOC_PUBLICID_2_3) > 0) return "FileWebApp";
        	if(dt.indexOf("SYSTEM") > 0 && dt.indexOf("web-app_2_3.dtd") > 0) return "FileWebApp";
        }

    	return (body.indexOf("<web-app") >= 0 
		        && body.indexOf("version=\"2.4\"") > 0
		        && body.indexOf("xmlns=\"http://java.sun.com/xml/ns/j2ee\"") > 0) ? "FileWebApp24" :
		       (body.indexOf("<web-app") >= 0 
				        && body.indexOf("version=\"2.5\"") > 0
				        && body.indexOf("xmlns=\"http://java.sun.com/xml/ns/javaee\"") > 0) ? "FileWebApp25" :
               null;
    }

}

