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
package org.jboss.tools.deltacloud.core.client.unmarshal;

import java.io.InputStream;
import java.text.MessageFormat;

import org.jboss.tools.deltacloud.client.DeltaCloudException;

/**
 * @author André Dietisheim
 *
 * @param <RESOURCE>
 */
public abstract class AbstractDeltaCloudObjectUnmarshaller<RESOURCE> implements IDeltaCloudObjectUnmarshaller<RESOURCE> {

	private Class<RESOURCE> type;

	public AbstractDeltaCloudObjectUnmarshaller(Class<RESOURCE> type) {
		this.type = type;
	}

	public RESOURCE create(InputStream inputStream) throws DeltaCloudException {

		try {
			return doCreate(inputStream);
		} catch (Exception e) {
			// TODO: internationalize strings
			throw new DeltaCloudException(
					MessageFormat.format("Could not unmarshall resource of type \"{0}\"", type), e);
		}
	}

	protected abstract RESOURCE doCreate(InputStream inputStream) throws Exception;

}
