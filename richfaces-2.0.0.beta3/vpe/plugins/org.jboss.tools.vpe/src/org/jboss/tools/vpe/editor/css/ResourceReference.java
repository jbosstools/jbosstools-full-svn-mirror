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
package org.jboss.tools.vpe.editor.css;

public class ResourceReference {
	public static int FILE_SCOPE = 0;
	public static int FOLDER_SCOPE = 1;
	public static int PROJECT_SCOPE = 2;
	
	public static String[] SCOPE_NAMES = new String[]{"Page", "Folder", "Project"};

	private String location;
	private int scope;
	private int depth = 0;
	private String properties = "";
	
	public ResourceReference(String location, int scope) {
		this.location = location;
		this.scope = scope;
		int q = location.indexOf('%');
		if(q >= 0) {
			properties = location.substring(q + 1);
			this.location = location.substring(0, q);
		}
	}
	
	public String getLocation() {
		return location;
	}
	
	public int getScope() {
		return scope;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public void setProperties(String properties) {
		this.properties = properties;
	}
	
	public String getProperties() {
		return properties;
	}
	
	public void setScope(int scope) {
		this.scope = scope;
	}
	
	public String getScopeName() {
		return SCOPE_NAMES[scope];
	}
	
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public String getLocationAndProperties() {
		String v = location;
		if(properties.length() > 0) {
			v += "%" + properties;
		}
		return v;
	}

}
