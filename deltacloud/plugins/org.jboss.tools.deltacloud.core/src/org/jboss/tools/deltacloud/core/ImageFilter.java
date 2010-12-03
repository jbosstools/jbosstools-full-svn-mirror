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

import java.util.Iterator;
import java.util.regex.PatternSyntaxException;

/**
 * A filter that may be applied on DeltaCloudImages
 * 
 * @see DeltaCloudImage
 * 
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public class ImageFilter extends AbstractCloudElementFilter<DeltaCloudImage> implements IImageFilter {

	private IFieldMatcher archRule;
	private IFieldMatcher descRule;
	
	@Override
	public boolean matches(DeltaCloudImage image) {
		return super.matches(image) &&
		archRule.matches(image.getArchitecture()) &&
		descRule.matches(image.getDescription());
	}

	@Override
	public void setRules(String ruleString) throws PatternSyntaxException {
		// TODO: replace filter passing (;-delimited string) by list
		Iterator<String> rulesIterator = super.setRules(ruleString, getRulesIterator(ruleString));		
		this.archRule = createRule(rulesIterator);
		this.descRule = createRule(rulesIterator);
	}
	
	@Override
	public String toString() {
		return super.toString()  //$NON-NLS-1$
		+ archRule + ";"  //$NON-NLS-1$
		+ descRule; //$NON-NLS-1$
	}
	
	public IFieldMatcher getArchRule() {
		return archRule;
	}
	
	public IFieldMatcher getDescRule() {
		return descRule;
	}
}
