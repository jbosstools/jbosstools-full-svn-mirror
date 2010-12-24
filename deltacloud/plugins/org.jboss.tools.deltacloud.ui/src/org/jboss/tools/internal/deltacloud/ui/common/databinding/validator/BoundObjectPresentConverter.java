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

import org.eclipse.core.databinding.conversion.Converter;

/**
 * @author André Dietisheim
 */
public class BoundObjectPresentConverter extends Converter {

	public BoundObjectPresentConverter() {
		super(Object.class, Boolean.class);
	}

	@Override
	public Object convert(Object fromObject) {
		return fromObject != null;
	}
}
