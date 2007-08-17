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

package org.jboss.tools.seam.internal.core;

import java.util.List;

import org.jboss.tools.seam.core.ISeamXmlComponentDeclaration;
import org.jboss.tools.seam.core.IValueInfo;
import org.jboss.tools.seam.core.ScopeType;
import org.jboss.tools.seam.core.event.Change;

public class SeamXmlComponentDeclaration extends SeamPropertiesDeclaration
		implements ISeamXmlComponentDeclaration {
	
	String autoCreate = null;
	String className = null;
	String installed = null;
	String jndiName = null;
	String precedence = null;
	String scope = null;

	public String getAutoCreateAsString() {
		return autoCreate;
	}

	public String getClassName() {
		return className;
	}

	public boolean getInstalledAsString() {
		return "true".equals(installed);
	}

	public String getJndiName() {
		return jndiName;
	}

	public String getPrecedence() {
		return precedence;
	}

	public ScopeType getScope() {
		if(scope == null || scope.length() == 0) return ScopeType.UNSPECIFIED;
		try {
			return ScopeType.valueOf(scope.toUpperCase());
		} catch (IllegalArgumentException e) {
			return ScopeType.UNSPECIFIED;
		}
	}

	public String getScopeAsString() {
		return scope;
	}

	public boolean isAutoCreate() {
		return "true".equals(autoCreate);
	}

	public boolean isInstalled() {
		return "true".equals(installed);
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public void setClassName(IValueInfo value) {
		attributes.put(CLASS, value);
		setClassName(value == null ? null : value.getValue());
	}

	public void setAutoCreate(String autoCreate) {
		this.autoCreate = autoCreate;
	}

	public void setAutoCreate(IValueInfo value) {
		attributes.put(AUTO_CREATE, value);
		setAutoCreate(value == null ? null : value.getValue());
	}

	public void setInstalled(String installed) {
		this.installed = installed;
	}

	public void setInstalled(IValueInfo value) {
		attributes.put(INSTALLED, value);
		setInstalled(value == null ? null : value.getValue());
	}

	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

	public void setJndiName(IValueInfo value) {
		attributes.put(JNDI_NAME, value);
		setJndiName(value == null ? null : value.getValue());
	}

	public void setPrecedence(String precedence) {
		this.precedence = precedence;
	}

	public void setPrecedence(IValueInfo value) {
		attributes.put(PRECEDENCE, value);
		setPrecedence(value == null ? null : value.getValue());
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public void setScope(IValueInfo value) {
		attributes.put(SCOPE, value);
		setScope(value == null ? null : value.getValue());
	}

	/**
	 * Merges loaded data into currently used declaration.
	 * If changes were done returns a list of changes. 
	 * @param d
	 * @return list of changes
	 */
	public List<Change> merge(SeamObject s) {
		List<Change> changes = super.merge(s);
		SeamXmlComponentDeclaration xd = (SeamXmlComponentDeclaration)s;
		
		if(!stringsEqual(className, xd.className)) {
			changes = Change.addChange(changes, new Change(this, CLASS, className, xd.className));
			className = xd.className;
		}
		if(!stringsEqual(autoCreate, xd.autoCreate)) {
			changes = Change.addChange(changes, new Change(this, AUTO_CREATE, autoCreate, xd.autoCreate));
			autoCreate = xd.autoCreate;
		}
		if(!stringsEqual(installed, xd.installed)) {
			changes = Change.addChange(changes, new Change(this, INSTALLED, installed, xd.installed));
			installed = xd.installed;
		}
		if(!stringsEqual(jndiName, xd.jndiName)) {
			changes = Change.addChange(changes, new Change(this, JNDI_NAME, jndiName, xd.jndiName));
			jndiName = xd.jndiName;
		}
		if(!stringsEqual(precedence, xd.precedence)) {
			changes = Change.addChange(changes, new Change(this, PRECEDENCE, precedence, xd.precedence));
			precedence = xd.precedence;
		}
		if(!stringsEqual(scope, xd.scope)) {
			changes = Change.addChange(changes, new Change(this, SCOPE, scope, xd.scope));
			scope = xd.scope;
		}

		return changes;
	}

}
