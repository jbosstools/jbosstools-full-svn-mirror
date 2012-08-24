/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.cdi.gen.model;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class GenField extends GenMember {
	GenType type;
	String initValue;
	
	public GenField() {
		setVisibility(GenVisibility.PROTECTED);
	}

	public void setType(GenType type) {
		this.type = type;
	}

	public GenType getType() {
		return type;
	}

	public void setInitValue(String s) {
		initValue = s;
	}

	public void flush(BodyWriter sb) {
		flushAnnotations(sb);
		flushVisibility(sb);
		sb.append(type.getTypeName()).append(" ").append(getName());
		if(initValue != null) {
			sb.append(" = ").append(initValue);
		}
		sb.append(";").newLine();
	}
}
