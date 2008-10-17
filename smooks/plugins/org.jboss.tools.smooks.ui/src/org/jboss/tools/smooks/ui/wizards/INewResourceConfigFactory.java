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
package org.jboss.tools.smooks.ui.wizards;

import org.jboss.tools.smooks.model.ResourceConfigType;

/**
 * @author Dart Peng<br>
 * Date : Sep 18, 2008
 */
public interface INewResourceConfigFactory {
	public ResourceConfigType createNewResourceConfig(NewResourceConfigKey key);
	
	public NewResourceConfigKey[] getAllIDs();
}
