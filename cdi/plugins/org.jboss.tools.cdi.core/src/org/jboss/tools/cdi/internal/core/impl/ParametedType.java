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
package org.jboss.tools.cdi.internal.core.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IType;
import org.jboss.tools.cdi.core.IParametedType;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class ParametedType implements IParametedType {
	protected IType type;
	protected String signature;
	protected List<ParametedType> parameterTypes = new ArrayList<ParametedType>();

	public ParametedType() {}

	public IType getType() {
		return type;
	}

	public String getSignature() {
		return signature;
	}

	public void setType(IType type) {
		this.type = type;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public void addParameter(ParametedType p) {
		parameterTypes.add(p);
	}

	public boolean equals(Object object) {
		if(!(object instanceof ParametedType)) return false;
		ParametedType other = (ParametedType)object;
		if(signature != null && signature.equals(other.signature)) {
			return true;
		}
		//TODO
		return false;
	}

}
