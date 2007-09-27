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

public class PostfixUrlPattern implements JSFUrlPattern {
	protected String postfix = ".jsf";
	protected String fileSuffix = ".jsp";
	
	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}
	
	public void setFileSuffix(String s) {
		fileSuffix = s;
	}

	public boolean matches(String path) {
		return path.endsWith(postfix);
	}
		
	public boolean isJSFUrl(String path) {
		return (path.endsWith(postfix) || path.endsWith(fileSuffix));
	}
	
	public String getJSFPath(String url) {
		if(url == null || url.length() == 0) return url;
		return (url.endsWith(postfix)) ? url.substring(0, url.length() - postfix.length()) + fileSuffix : url;
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
