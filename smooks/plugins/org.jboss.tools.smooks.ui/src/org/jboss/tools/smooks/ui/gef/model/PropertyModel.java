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
package org.jboss.tools.smooks.ui.gef.model;

/**
 * @author Dart Peng<br>
 *         Date : Sep 4, 2008
 */
public class PropertyModel extends AbstractStructuredDataModel {
	private String name;
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		String oldName = this.name;
		this.name = name;
		firePropertyChange(
				AbstractStructuredDataConnectionModel.CONNECTION_PROPERTY_CHANGE,
				oldName, this.name);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		String oldValue = this.value;
		this.value = value;
		firePropertyChange(
				AbstractStructuredDataConnectionModel.CONNECTION_PROPERTY_CHANGE,
				oldValue, this.value);
	}
}
