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
package org.jboss.tools.deltacloud.core;

/**
 * @author André Dietisheim
 */
public class InstanceAliasMapping implements IInstanceAliasMapping {

	private String alias;
	private String id;

	public InstanceAliasMapping(String id, String alias) {
		this.alias = alias;
		this.id = id;
	}

	@Override
	public boolean matches(DeltaCloudInstance instance) {
		return this.id.equals(instance.getId());
	}

	@Override
	public String getAlias() {
		return alias;
	}
}
