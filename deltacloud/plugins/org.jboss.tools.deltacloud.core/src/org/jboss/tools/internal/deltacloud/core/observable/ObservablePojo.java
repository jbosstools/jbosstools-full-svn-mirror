/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.internal.deltacloud.core.observable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A POJO base class that may notify observers on behalf of
 * {@link PropertyChangeSupport}
 */
public abstract class ObservablePojo {

	private PropertyChangeSupport propertyChangeSupport;

	public ObservablePojo() {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
	}

	public void firePropertyChange(String propertyName, 
			Object oldValue, Object newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(
			PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	protected PropertyChangeSupport getPropertyChangeSupport() {
		return propertyChangeSupport;
	}
}
