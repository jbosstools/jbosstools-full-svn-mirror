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

import java.util.regex.PatternSyntaxException;

public class ImageFilter implements IImageFilter {

	private IFieldMatcher nameRule;
	private IFieldMatcher idRule;
	private IFieldMatcher archRule;
	private IFieldMatcher descRule;
	
	@Override
	public boolean isVisible(DeltaCloudImage image) {
		return nameRule.matches(image.getName()) &&
		idRule.matches(image.getId()) &&
		archRule.matches(image.getArchitecture()) &&
		descRule.matches(image.getDescription());
	}

	@Override
	public void setRules(String ruleString) throws PatternSyntaxException {
		// TODO: replace filter passing (;-delimited string) by list
		String[] tokens = ruleString.split(";");
		this.nameRule = createRule(tokens[0]);
		this.idRule = createRule(tokens[1]);
		this.archRule = createRule(tokens[2]);
		this.descRule = createRule(tokens[3]);
	}

	private IFieldMatcher createRule(String token) {
		if (token.equals(ALL_MATCHER_EXPRESSION)) { //$NON-NLS-1$
			return new AllFieldMatcher();
		} else {
			return new FieldMatcher(token);
		}
	}
	
	@Override
	public String toString() {
		return nameRule + ";" //$NON-NLS-1$ 
		+ idRule + ";"  //$NON-NLS-1$
		+ archRule + ";"  //$NON-NLS-1$
		+ descRule; //$NON-NLS-1$
	}

	public IFieldMatcher getNameRule() {
		return nameRule;
	}
	
	public IFieldMatcher getIdRule() {
		return idRule;
	}
	
	public IFieldMatcher getArchRule() {
		return archRule;
	}
	
	public IFieldMatcher getDescRule() {
		return descRule;
	}
}
