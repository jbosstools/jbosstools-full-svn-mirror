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
package org.jboss.tools.deltacloud.core.client;

/**
 * @author Andre Dietisheim
 */
public interface InternalDeltaCloudClient extends DeltaCloudClient {

	/**
	 * Performs the given action on the given instance *.
	 * 
	 * @param action
	 *            the action to perform
	 * @param instance
	 *            the instance to perform on
	 * @return true, if the action was performed successfully
	 * @throws DeltaCloudClientException
	 *             indicates that an error occured while performing the action
	 * @see InstanceAction
	 * @see Instance
	 */
	public boolean performInstanceAction(InstanceAction action) throws DeltaCloudClientException;

}
