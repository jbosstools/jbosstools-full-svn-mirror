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
package org.jboss.tools.jsf.web.pattern;

import java.util.ArrayList;
import java.util.List;

public class PostfixUrlPattern implements JSFUrlPattern {
	protected String postfix = ".jsf";
	/*
	 * Fixes https://jira.jboss.org/jira/browse/JBIDE-5577
	 * Fixes https://jira.jboss.org/jira/browse/JBIDE-5635
	 * JSF files could be mapped to xhtml or jspx files also.
	 * All of this extensions should be checked.
	 */
	protected String[] fileExtensions = PatternLoader.DEFAULT_SUFFIXES;

	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}
	
	public void setFileSuffixes(String[] fileExtentions) {
		this.fileExtensions = fileExtentions;
	}
	
	public boolean matches(String path) {
		return path.endsWith(postfix);
	}
		
	public boolean isJSFUrl(String path) {
		if(path.endsWith(postfix)) {
			return true;
		}
		for (String fileSuffix: fileExtensions) {
			if(path.endsWith(fileSuffix)) {
				return true;
			}
		}
		return false;
	}
	
	public String getJSFPath(String url) {
		if(url == null || url.length() == 0) return url;
		List<String> ps = getJSFPaths(url);
		return (ps.isEmpty()) ? url : ps.get(0);
	}
	
	public List<String> getJSFPaths(String url) {
		List<String> jsfPathsList = new ArrayList<String>();
		if((url != null) && (url.length() > 0)) {
			if (url.endsWith(postfix)) {
				for (String extension : fileExtensions) {
					jsfPathsList.add(url.substring(0, url.length() - postfix.length()) + extension);
				}
			} else {
				jsfPathsList.add(url);
			}
		}
		return jsfPathsList;
	}
	
	public String getJSFUrl(String path) {
		if(path == null || path.length() == 0) return path;
		if(!path.endsWith(postfix)) {
			int dot = path.lastIndexOf('.');
			if(dot >= 0) path = path.substring(0, dot);
			path += postfix;
		} 
		if(!path.startsWith("/")) path = "/" + path;
		return path;
	}
	
	public String toString() {
		return "PostfixUrlPattern:" + postfix;
	}

}
