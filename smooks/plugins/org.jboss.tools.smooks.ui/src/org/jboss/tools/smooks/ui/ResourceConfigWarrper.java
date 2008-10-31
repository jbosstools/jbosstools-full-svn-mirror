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
package org.jboss.tools.smooks.ui;

import org.jboss.tools.smooks.model.ResourceConfigType;

/**
 * @author Dart Peng<br>
 * Date : Sep 16, 2008
 */
public class ResourceConfigWarrper {
	private ResourceConfigType resourceConfig;

	public ResourceConfigWarrper(){
		
	}
	
	public ResourceConfigWarrper(ResourceConfigType type){
		setResourceConfig(type);
	}
	
	public ResourceConfigType getResourceConfig() {
		return resourceConfig;
	}

	public void setResourceConfig(ResourceConfigType resourceConfig) {
		this.resourceConfig = resourceConfig;
	}
}
