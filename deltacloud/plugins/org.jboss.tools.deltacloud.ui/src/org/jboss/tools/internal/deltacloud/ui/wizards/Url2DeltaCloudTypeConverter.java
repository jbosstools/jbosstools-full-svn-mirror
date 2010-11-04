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

import java.net.MalformedURLException;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClientImpl;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClientImpl.DeltaCloudServerType;

/**
 * A class that converts an url (string) to a DeltaCloudType (enum). The state
 * of the converter may be tracked by tracking its observable value cloudType
 * observable.
 * 
 * @author Andre Dietisheim
 * 
 * @see IConverter
 * @see IObservableValue
 * @see #getCloudTypeObservable()
 */
public class Url2DeltaCloudTypeConverter implements IConverter {

	IObservableValue cloudTypeObservable = new WritableValue();

	@Override
	public Object getFromType() {
		return String.class;
	}

	@Override
	public Object getToType() {
		return DeltaCloudClientImpl.DeltaCloudServerType.class;
	}

	@Override
	public Object convert(Object fromObject) {
		String deltaCloudUrl = (String) fromObject;
		DeltaCloudServerType cloudType = getCloudType(deltaCloudUrl);
		cloudTypeObservable.setValue(cloudType);
		return cloudType;
	}

	private DeltaCloudServerType getCloudType(String deltaCloudUrl) {
		try {
			return new DeltaCloudClientImpl(deltaCloudUrl, "", "").getServerType();
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public IObservableValue getCloudTypeObservable() {
		return cloudTypeObservable;
	}
}
