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
package org.jboss.tools.smooks.configuration.editors.actions;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.jboss.tools.smooks.model.persistence12.Deleter;
import org.jboss.tools.smooks.model.persistence12.Flusher;
import org.jboss.tools.smooks.model.persistence12.Inserter;
import org.jboss.tools.smooks.model.persistence12.Locator;
import org.jboss.tools.smooks.model.persistence12.Updater;

/**
 * @author Dart
 *
 */
public class PersistenceActionGrouper extends AbstractSmooksActionGrouper {

	public static final String NAME = Messages.PersistenceActionGrouper_GroupName;
	
	

	@Override
	protected boolean canAdd(Object value) {
		if (AdapterFactoryEditingDomain.unwrap(value) instanceof Inserter) {
			return true;
		}
		if (AdapterFactoryEditingDomain.unwrap(value) instanceof Updater) {
			return true;
		}
		if (AdapterFactoryEditingDomain.unwrap(value) instanceof Deleter) {
			return true;
		}
		if (AdapterFactoryEditingDomain.unwrap(value) instanceof Locator) {
			return true;
		}
		if (AdapterFactoryEditingDomain.unwrap(value) instanceof Flusher) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.configuration.editors.actions.ISmooksActionGrouper#getGroupName()
	 */
	public String getGroupName() {
		return NAME;
	}

}
