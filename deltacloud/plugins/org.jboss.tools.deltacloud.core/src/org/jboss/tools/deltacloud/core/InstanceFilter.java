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

/**
 * @author Jeff Johnston
 */
public class InstanceFilter implements IInstanceFilter {

	private IFieldMatcher nameRule;
	private IFieldMatcher idRule;
	private IFieldMatcher imageIdRule;
	private IFieldMatcher realmRule;
	private IFieldMatcher profileRule;
	private IFieldMatcher ownerIdRule;
	private IFieldMatcher keyNameRule;
	
	@Override
	public boolean isVisible(DeltaCloudInstance instance) {
		return nameRule.matches(instance.getName()) &&
		idRule.matches(instance.getId()) &&
		imageIdRule.matches(instance.getImageId()) &&
		ownerIdRule.matches(instance.getOwnerId()) &&
		keyNameRule.matches(instance.getKey()) &&
		realmRule.matches(instance.getRealmId()) &&
		profileRule.matches(instance.getProfileId());
	}

	@Override
	public void setRules(String ruleString) throws PatternSyntaxException {
		String[] tokens = ruleString.split(";");
		this.nameRule = createRule(tokens[0]);
		this.idRule = createRule(tokens[1]);
		this.imageIdRule = createRule(tokens[2]);
		this.ownerIdRule = createRule(tokens[3]);
		this.keyNameRule = createRule(tokens[4]);
		this.realmRule = createRule(tokens[5]);
		this.profileRule = createRule(tokens[6]);
	}

	private IFieldMatcher createRule(String expression) {
		if (expression.equals(ALL_MATCHER_EXPRESSION)) { //$NON-NLS-1$
			return new AllFieldMatcher();
		} else {
			return new FieldMatcher(expression);
		}
	}
	
	@Override
	public String toString() {
		return nameRule + ";" //$NON-NLS-1$ 
		+ idRule + ";"  //$NON-NLS-1$
		+ imageIdRule + ";" //$NON-NLS-1$
		+ ownerIdRule + ";" //$NON-NLS-1$
		+ keyNameRule + ";" //$NON-NLS-1$
		+ realmRule + ";" //$NON-NLS-1$
		+ profileRule; //$NON-NLS-1$
	}

	@Override
	public IFieldMatcher getNameRule() {
		return nameRule;
	}
	
	@Override
	public IFieldMatcher getIdRule() {
		return idRule;
	}

	@Override
	public IFieldMatcher getImageIdRule() {
		return imageIdRule;
	}

	@Override
	public IFieldMatcher getKeyNameRule() {
		return keyNameRule;
	}

	@Override
	public IFieldMatcher getOwnerIdRule() {
		return ownerIdRule;
	}

	@Override
	public IFieldMatcher getProfileRule() {
		return profileRule;
	}

	@Override
	public IFieldMatcher getRealmRule() {
		return realmRule;
	}
}
