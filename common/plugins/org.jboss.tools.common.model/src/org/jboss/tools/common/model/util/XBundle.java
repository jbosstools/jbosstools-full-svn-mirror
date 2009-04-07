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
package org.jboss.tools.common.model.util;

import java.text.MessageFormat;

import org.jboss.tools.common.meta.key.WizardKeys;
import org.jboss.tools.common.model.plugin.ModelPlugin;

public class XBundle {
    private static XBundle bundle = new XBundle();
	
    private XBundle() {}

    public static XBundle getInstance() {
        return bundle;
    }

    private String findTemplate(String resourceid, String templateid) {
        return WizardKeys.getString(templateid);
    }

    public String getMessage(String resourceid, String templateid) {
        return getMessage(resourceid, templateid, null);
    }

    public String getMessage(String resourceid, String templateid, Object[] args) {
        String t = findTemplate(resourceid, templateid);
        if(args == null) return t;
        try {
            return MessageFormat.format(t, args);
        } catch (IllegalArgumentException e) {
        	ModelPlugin.getPluginLog().logError(e);
            return MessageFormat.format(Messages.getString("ERR_GET_MESSAGE"),new Object[]{resourceid,templateid,t}); //$NON-NLS-1$
	    }
    }

}

