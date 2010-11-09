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

public interface IImageFilter {

	public final static String ALL_STRING = "*;*;*;*"; //$NON-NLS-1$
	
	public boolean isVisible(DeltaCloudImage image);
	public void setRules(String ruleString);
	public IFieldMatcher getNameRule();
	public IFieldMatcher getIdRule();
	public IFieldMatcher getArchRule();
	public IFieldMatcher getDescRule();
}
