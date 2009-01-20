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
	private Object value;
	
	public PropertyModel(String name,Object value){
		this.name = name;
		this.value = value;
	}
	
	public PropertyModel(){
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		String oldName = this.name;
		this.name = name;
		firePropertyChange(
				AbstractStructuredDataConnectionModel.CONNECTION_PROPERTY_REMOVE,
				oldName, this.name);
	}

	public Object getValue() {
		return value;
	}

	public boolean isSameProperty(PropertyModel model) {
		if (this.name == null)
			return false;
		return this.name.equals(model.getName());
	}

	@Override
	public boolean equals(Object obj) {
		if (this.name == null)
			return false;
		if (obj instanceof PropertyModel) {
			String name = ((PropertyModel) obj).getName();
			if (!this.name.equals(name)) {
				return false;
			}
			Object value = ((PropertyModel) obj).getValue();
			if (value == null) {
				if (this.value == null)
					return true;
				return false;
			}

			if (this.value.equals(value)) {
				return true;
			}
		}
		return false;
	}

	public void setValue(Object value) {
		Object oldValue = this.value;
		this.value = value;
		firePropertyChange(
				AbstractStructuredDataConnectionModel.CONNECTION_PROPERTY_REMOVE,
				oldValue, this.value);
	}
}
