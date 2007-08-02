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

import org.jboss.tools.common.model.impl.*;

public class ShaleDialogStateObjectImpl extends CustomizedObjectImpl {
	private static final long serialVersionUID = 1L;

	public ShaleDialogStateObjectImpl() {
		
	}
	
	public String getAttributeValue(String name) {
		if("presentation".equals(name)) {
			return getPresentationString();
		} else {
			return super.getAttributeValue(name);
		}
	}
	
	public String setAttributeValue(String name, String value) {
		if("presentation".equals(name)) {
			return value;
		} else {
			return super.setAttributeValue(name, value);
		}		
	}

}
