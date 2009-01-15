/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.xml.model;

import java.util.Collections;
import java.util.List;

/**
 * @author Dart Peng
 * @Date Jul 25, 2008
 */
public class TagPropertyObject extends AbstractXMLObject {
	
	protected String type = "string";
	
	protected String value = null;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	
	
	@Override
	public boolean isAttribute() {
		return true;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public Object getID() {
		return "@" + super.getID();
	}

	@Override
	public List<AbstractXMLObject> getXMLNodeChildren() {
		return Collections.EMPTY_LIST;
	}
	@Override
	public void setChildren(List<AbstractXMLObject> children) {
	}
}
