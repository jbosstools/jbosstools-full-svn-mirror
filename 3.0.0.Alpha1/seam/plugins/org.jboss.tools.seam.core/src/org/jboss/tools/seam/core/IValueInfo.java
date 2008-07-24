 /*******************************************************************************
  * Copyright (c) 2007 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.seam.core;

import java.util.Properties;

import org.w3c.dom.Element;

/**
 * @author Viacheslav Kabanovich
 */
public interface IValueInfo extends ISeamTextSourceReference {
	
	/**
	 * Returns string value
	 * @return
	 */
	public String getValue();
	
	public Element toXML(Element parent, Properties context);
	
	public void loadXML(Element element, Properties context);
}