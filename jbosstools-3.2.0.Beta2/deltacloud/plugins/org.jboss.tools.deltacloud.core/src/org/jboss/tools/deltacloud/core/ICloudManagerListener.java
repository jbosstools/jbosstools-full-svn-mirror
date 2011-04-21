/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.deltacloud.core;

public interface ICloudManagerListener {
	
	public static int ADD_EVENT = 1;
	public static int REMOVE_EVENT = 2;
	public static int RENAME_EVENT = 3;
	
	void cloudsChanged(int type);
}
