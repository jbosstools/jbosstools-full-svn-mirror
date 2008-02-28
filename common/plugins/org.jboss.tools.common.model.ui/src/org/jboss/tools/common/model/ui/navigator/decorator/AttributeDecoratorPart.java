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
package org.jboss.tools.common.model.ui.navigator.decorator;

import org.jboss.tools.common.model.XModelObject;

/**
 * @author Viacheslav Kabanovich
 */
public class AttributeDecoratorPart implements IDecoratorPart {
	Variable variable;
	
	public AttributeDecoratorPart(Variable variable) {
		this.variable = variable;
	}

	public String getLabelPart(XModelObject object) {
		String v = object.getAttributeValue(variable.getName());
		return v == null ? "{" + variable.getName() + "}" : v; 
	}

}
