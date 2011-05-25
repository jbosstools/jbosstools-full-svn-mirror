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
package org.jboss.tools.internal.deltacloud.ui.common.databinding.validator;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.runtime.Assert;

/**
 * A converter that applies a chain of converters.
 * 
 * @author André Dietisheim
 */
public class CompositeConverter implements IConverter {

	private final IConverter[] converters;

	public CompositeConverter(final IConverter... converters) {
		Assert.isLegal(converters != null);
		Assert.isLegal(converters.length >= 1);

		this.converters = converters;
	}

	@Override
	public Object getFromType() {
		return converters[0].getFromType();
	}

	@Override
	public Object getToType() {
		return converters[converters.length - 1].getToType();
	}

	@Override
	public Object convert(Object fromValue) {
		Object toValue = fromValue;
		for (IConverter converter : converters) {
			toValue = converter.convert(toValue);
		}
		return toValue;
	}
}
