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
package org.jboss.tools.jst.web.tld.model;

import java.util.Properties;

import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.model.impl.*;

//See WebAppErrorPageImpl

public class TLDVariableImpl extends RegularObjectImpl implements TLDConstants {
	private static final long serialVersionUID = 1L;
	
	public String getPresentationString() {
        String ng = getAttributeValue(NAME_GIVEN);
        String na = getAttributeValue(NAME_FROM_ATTRIBUTE);
        return "" + ((ng != null && ng.length() > 0) ? ng : na);
	}

	public String name() {
        String ng = getAttributeValue(NAME_GIVEN);
        String na = getAttributeValue(NAME_FROM_ATTRIBUTE);
        return "variable:" + ((ng != null && ng.length() > 0) ? ng : na);
    }

	protected void onAttributeValueEdit(String name, String oldValue, String newValue) {
		if(NAME_GIVEN.equals(name) && newValue != null && newValue.length() > 0) {
			if(getAttributeValue(NAME_FROM_ATTRIBUTE).length() > 0) {
				Properties p = new Properties();
				p.setProperty("focusAttribute", NAME_GIVEN);
				XActionInvoker.invoke("EditActions.Edit", this, p);
				if(!"true".equals(p.getProperty("done"))) {
					if(oldValue == null) oldValue = "";
					setAttributeValue(NAME_GIVEN, oldValue);
				}
			}
		} else if(NAME_FROM_ATTRIBUTE.equals(name) && newValue != null && newValue.length() > 0) {
			if(getAttributeValue(NAME_GIVEN).length() > 0) {
				Properties p = new Properties();
				p.setProperty("focusAttribute", NAME_FROM_ATTRIBUTE);
				XActionInvoker.invoke("EditActions.Edit", this, p);
				if(!"true".equals(p.getProperty("done"))) {
					if(oldValue == null) oldValue = "";
					setAttributeValue(NAME_FROM_ATTRIBUTE, oldValue);
				}
			}
		}
	}

}

