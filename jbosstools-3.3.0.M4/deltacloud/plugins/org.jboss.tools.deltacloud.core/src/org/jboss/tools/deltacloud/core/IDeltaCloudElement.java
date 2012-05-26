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

/**
 * A common interface for all elements of a cloud. Currently known implementors
 * are DeltaCloudImage and DeltaCloudInstance.
 * 
 * @see DeltaCloudImage
 * @see DeltaCloudInstace
 * 
 * @author Andre Dietisheim
 */
public interface IDeltaCloudElement {

	public DeltaCloud getDeltaCloud();

	public String getName();

	public String getId();
}
