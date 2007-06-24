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
package org.jboss.tools.shale.model.dialog;

import org.eclipse.core.runtime.FileLocator;
import org.jboss.tools.common.model.loaders.EntityRecognizer;
import org.jboss.tools.common.model.util.XMLEntityResolver;
import org.jboss.tools.shale.ShaleModelPlugin;

public class ShaleDialogEntityRecognizer implements ShaleDialogConstants, EntityRecognizer {

    static {
        try {
            Class c = ShaleDialogEntityRecognizer.class;
            XMLEntityResolver.registerPublicEntity(DOC_PUBLICID, FileLocator.resolve(c.getResource("/meta/shale-dialog-config_1_0.dtd")).toString());
        } catch (Exception e) {
        	ShaleModelPlugin.log(e);
        }
    }

    public String getEntityName(String ext, String body) {
        if(body == null) return null;
        if(body.indexOf(DOC_PUBLICID) > 0) return ENT_SHALE_DIALOG;
        return null;
    }
    
}
