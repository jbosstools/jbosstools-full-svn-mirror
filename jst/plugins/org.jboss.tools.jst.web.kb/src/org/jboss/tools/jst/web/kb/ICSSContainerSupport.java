/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb;

import java.util.List;

import org.w3c.dom.css.CSSStyleSheet;

/**
 * The interface defines the methods to collect
 * CSS Stylesheets used within the page.
 * 
 * @author Victor Rubezhny
 *
 */
public interface ICSSContainerSupport {

	/**
	 * Adds the CSS StyleSheet object found within the page
	 * 
	 * @param includedContext
	 */
	void addCSSStyleSheet(CSSStyleSheet cssStyleSheet);
	
	/**
	 * Returns the list of all the collected CSS StyleSheet objects
	 * 
	 * @return
	 */
	List<CSSStyleSheet> getCSSStyleSheets();
}
