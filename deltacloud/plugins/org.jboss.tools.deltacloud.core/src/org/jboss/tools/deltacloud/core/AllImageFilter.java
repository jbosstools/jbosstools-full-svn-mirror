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

public class AllImageFilter implements IImageFilter {

	private IFieldMatcher matcher = new AllFieldMatcher();
	
	@Override
	public boolean isVisible(DeltaCloudImage image) {
		return true;
	}

	@Override
	public void setRules(String ruleString) {
		// ignore, never set the rules for this filter
	}
	
	@Override
	public String toString() {
		return ALL_STRING;
	}
	
	public IFieldMatcher getNameRule() {
		return matcher; 
	}
	
	public IFieldMatcher getIdRule() {
		return matcher; 
	}
	
	public IFieldMatcher getArchRule() {
		return matcher; 
	}
	
	public IFieldMatcher getDescRule() {
		return matcher;
	}

}
