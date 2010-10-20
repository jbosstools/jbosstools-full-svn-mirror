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
package org.jboss.tools.internal.deltacloud.ui.wizards;

import org.eclipse.core.databinding.conversion.IConverter;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClient;

/**
 * @author Andre Dietisheim
 */
public class UrlToCloudTypeConverter implements IConverter {

	@Override
	public Object getFromType() {
		return String.class;
	}

	@Override
	public Object getToType() {
		return DeltaCloudClient.DeltaCloudType.class;
	}

	@Override
	public Object convert(Object fromObject) {
		String deltaCloudUrl = (String) fromObject;
		return DeltaCloudClient.getDeltaCloudType(deltaCloudUrl);
	}

}
