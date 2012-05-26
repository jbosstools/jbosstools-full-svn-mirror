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

import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudDriver;

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
public class Url2DriverConverter extends Converter {

	IObservableValue cloudTypeObservable = new WritableValue();

	public Url2DriverConverter() {
		super(String.class, DeltaCloudDriver.class);
	}

	@Override
	public Object convert(Object fromObject) {
		String deltaCloudUrl = (String) fromObject;
		DeltaCloudDriver cloudType = getCloudType(deltaCloudUrl);
		cloudTypeObservable.setValue(cloudType);
		return cloudType;
	}

	private DeltaCloudDriver getCloudType(String url) {
		try {
			return DeltaCloud.getServerDriver(url);
		} catch (DeltaCloudException e) {
			return DeltaCloudDriver.UNKNOWN;
		}
	}

	public IObservableValue getCloudTypeObservable() {
		return cloudTypeObservable;
	}
}
