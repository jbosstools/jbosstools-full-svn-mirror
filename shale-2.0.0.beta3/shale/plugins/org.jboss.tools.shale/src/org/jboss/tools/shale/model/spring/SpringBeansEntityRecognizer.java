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
package org.jboss.tools.shale.model.spring;

import org.jboss.tools.common.model.loaders.EntityRecognizer;
import org.jboss.tools.common.xml.XMLEntityResolver;
import org.jboss.tools.shale.ShaleModelPlugin;

public class SpringBeansEntityRecognizer implements SpringBeansConstants, EntityRecognizer {

    static {
        try {
            Class<?> c = SpringBeansEntityRecognizer.class;
            XMLEntityResolver.registerPublicEntity(DOC_PUBLICID, c, "/meta/spring-beans.dtd");
        } catch (Exception e) {
        	ShaleModelPlugin.getPluginLog().logError(e);
        }
    }

    public String getEntityName(String ext, String body) {
        if(body == null) return null;
        if(body.indexOf(DOC_PUBLICID) > 0) return ENT_FILE_SPRING_BEANS;
        return null;
    }
    
}
