/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.esb.core.model;


import org.jboss.tools.common.model.loaders.EntityRecognizer;
import org.jboss.tools.common.model.loaders.EntityRecognizerContext;

/**
 * @author Viacheslav Kabanovich
 */
public class ESBEntityRecognizer implements EntityRecognizer, ESBConstants {

    public ESBEntityRecognizer() {}

    public String getEntityName(EntityRecognizerContext context) {
    	return getEntityName(context.getExtension(), context.getBody());
    }

    String getEntityName(String ext, String body) {
        if(body == null) return null;
    	if(!isSchema(body)) {
    		return null;
    	}
    	
//    	int i = body.indexOf("xsi:schemaLocation"); //$NON-NLS-1$
    	int i = body.indexOf("xmlns="); //$NON-NLS-1$
    	if(i < 0) return null;
    	int j = body.indexOf("\"", i); //$NON-NLS-1$
    	if(j < 0) return null;
    	int k = body.indexOf("\"", j + 1); //$NON-NLS-1$
    	if(k < 0) return null;
    	String schema = body.substring(j + 1, k);
    	
    	int i101 = schema.indexOf("1.0.1"); //$NON-NLS-1$
    	if(i101 >= 0) {
    		return ENT_ESB_FILE_101;
    	}
    	int i110 = schema.indexOf("1.1.0"); //$NON-NLS-1$
    	if(i110 >= 0) {
    		return ENT_ESB_FILE_110;
    	}
    	int i120 = schema.indexOf("1.2.0"); //$NON-NLS-1$
    	if(i120 >= 0) {
    		return ENT_ESB_FILE_120;
    	}
    	int i130 = schema.indexOf("1.3.0"); //$NON-NLS-1$
    	if(i130 >= 0) {
    		return ENT_ESB_FILE_130;
    	}
        return null;
    }
    
    private boolean isSchema(String body) {
    	int i = body.indexOf("<jbossesb"); //$NON-NLS-1$
    	if(i < 0) return false;
    	int j = body.indexOf(">", i); //$NON-NLS-1$
    	if(j < 0) return false;
    	String s = body.substring(i, j);
    	String q = "\"";
    	return s.indexOf(q + SCHEMA_101 + q) > 0
    		|| s.indexOf(q + SCHEMA_110 + q) > 0
    		|| s.indexOf(q + SCHEMA_120 + q) > 0
    		|| s.indexOf(q + SCHEMA_130 + q) > 0
    		|| s.indexOf(q + NEW_SCHEMA_101 + q) > 0
    		|| s.indexOf(q + NEW_SCHEMA_110 + q) > 0
    		|| s.indexOf(q + NEW_SCHEMA_120 + q) > 0
    		|| s.indexOf(q + NEW_SCHEMA_130 + q) > 0;
    }
    
}
