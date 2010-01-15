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
package org.jboss.tools.vpe.editor.template;

public class VpeTemplateData {
	private boolean changed = false;

	private String name;
	private boolean caseSensitive = false;
	private boolean children = false;
	private boolean modify = false;

	public VpeTemplateData () {
	}
	public VpeTemplateData (String name) {
		this.name = name;
	}
	public VpeTemplateData (
			String name,
			boolean caseSensitive, 
			boolean children, 
			boolean modify
		) {
		this.name = name;
		this.caseSensitive = caseSensitive;
		this.children = children;
		this.modify = modify;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}
	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public boolean isChildren() {
		return children;
	}
	public void setChildren(boolean children) {
		this.children = children;
	}

	public boolean isModify() {
		return modify;
	}
	public void setModify(boolean modify) {
		this.modify = modify;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public boolean isChanged() {
		return changed;
	}
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public String getPrefix() {
		if ((name != null) && (name.indexOf(":") > 0)) { //$NON-NLS-1$
			return name.substring(0, name.indexOf(":")); //$NON-NLS-1$
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "VpeTemplateData [caseSensitive=" + caseSensitive + ", changed=" //$NON-NLS-1$ //$NON-NLS-2$
				+ changed + ", children=" + children + ", modify=" + modify //$NON-NLS-1$ //$NON-NLS-2$
				+ ", name=" + name + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		VpeTemplateData other = (VpeTemplateData) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
	
}
