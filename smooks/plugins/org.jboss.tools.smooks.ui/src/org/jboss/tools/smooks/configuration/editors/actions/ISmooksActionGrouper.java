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

import org.eclipse.jface.action.MenuManager;

/**
 * @author Dart
 * 
 */
public interface ISmooksActionGrouper {
	
	public boolean isSeparator();

	public String getGroupName();

	public void orderActions(MenuManager calcMenu);

	public boolean belongsToGroup(Object descriptor);
}
