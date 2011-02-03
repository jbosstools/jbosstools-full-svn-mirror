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

import java.util.Collection;

/**
 * A filter interface that may be applied on IDeltaCloudElement. Known
 * implementors are IImageFilter and IInstanceFilter
 * 
 * @see IDeltaCloudElement
 * @see IImageFilter
 * @see IInstanceFilter
 * 
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public interface ICloudElementFilter<CLOUDELEMENT extends IDeltaCloudElement> {

	public static final String EXPRESSION_DELIMITER = ";"; // $NON-NLS-1$

	public static final String ALL_MATCHER_EXPRESSION = "*"; //$NON-NLS-1$

	public Collection<CLOUDELEMENT> filter(CLOUDELEMENT[] cloudElements) throws DeltaCloudException;

	public IFieldMatcher getNameRule();

	public IFieldMatcher getIdRule();
}
