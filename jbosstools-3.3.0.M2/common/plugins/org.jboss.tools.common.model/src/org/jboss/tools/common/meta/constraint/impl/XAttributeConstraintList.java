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
package org.jboss.tools.common.meta.constraint.impl;

import org.jboss.tools.common.model.plugin.ModelMessages;

public class XAttributeConstraintList extends XAttributeConstraintAList {

    public XAttributeConstraintList() {}

    public boolean accepts(String value) {
        return values.contains(value);
    }

    public String getError(String value) {
    	if(accepts(value)) {
    		return null;
    	}
    	String[] vs = getValues();
    	if(vs.length < 6) {
    		String message = " should be one of ";
    		int j = 0;
    		for (int i = 0; i < vs.length; i++) {
    			if(vs[i].length() == 0 || vs[i].startsWith("Default(")) continue; //$NON-NLS-1$
    			if(j > 0) message += ", "; //$NON-NLS-1$
    			message += getValues()[i];
    			j++;
    		}
    		return message;
    	}
        return ModelMessages.CONSTRAINT_IS_NOT_IN_LIST;
    }

}
