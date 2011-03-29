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

/**
 * A filter for images that matches on all elements (no criteria checked)
 * 
 * @see IImageFilter
 * @see DeltaCloud#getImageFilter()
 * 
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public class AllImageFilter extends AbstractCloudElementFilter<DeltaCloudImage> implements IImageFilter {

	private IFieldMatcher matcher = new AllFieldMatcher();
	
	public AllImageFilter(DeltaCloud cloud) {
		super(cloud);
	}
	
	@Override
	public boolean matches(DeltaCloudImage image) {
		return true;
	}

	@Override
	public void setRules(String ruleString) {
		// ignore, never set the rules for this filter
	}

	@Override
	public IFieldMatcher getNameRule() {
		return matcher;
	}

	@Override
	public IFieldMatcher getIdRule() {
		return matcher;
	}

	public IFieldMatcher getArchRule() {
		return matcher; 
	}
	
	public IFieldMatcher getDescRule() {
		return matcher;
	}

	@Override
	public String toString() {
		return ALL_STRING;
	}
}
