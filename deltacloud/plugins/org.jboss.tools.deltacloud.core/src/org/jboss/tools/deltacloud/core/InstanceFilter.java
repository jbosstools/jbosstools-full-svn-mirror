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
 * A filter that may be applied on DeltaCloudInstances
 * 
 * @see DeltaCloudInstance
 * 
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public class InstanceFilter extends AbstractCloudElementFilter<DeltaCloudInstance> implements IInstanceFilter {

	private IFieldMatcher imageIdRule;
	private IFieldMatcher realmRule;
	private IFieldMatcher profileRule;
	private IFieldMatcher ownerIdRule;
	private IFieldMatcher keyNameRule;
	
	@Override
	public boolean matches(DeltaCloudInstance instance) {
		return super.matches(instance) &&
		imageIdRule.matches(instance.getImageId()) &&
		ownerIdRule.matches(instance.getOwnerId()) &&
		keyNameRule.matches(instance.getKey()) &&
		realmRule.matches(instance.getRealmId()) &&
		profileRule.matches(instance.getProfileId());
	}

	@Override
	public void setRules(String ruleString) throws PatternSyntaxException {
		Iterator<String> rulesIterator = super.setRules(ruleString, getRulesIterator(ruleString));
		this.imageIdRule = createRule(rulesIterator);
		this.ownerIdRule = createRule(rulesIterator);
		this.keyNameRule = createRule(rulesIterator);
		this.realmRule = createRule(rulesIterator);
		this.profileRule = createRule(rulesIterator);
	}
	
	@Override
	public String toString() {
		return super.toString()
		+ imageIdRule + ";" //$NON-NLS-1$
		+ ownerIdRule + ";" //$NON-NLS-1$
		+ keyNameRule + ";" //$NON-NLS-1$
		+ realmRule + ";" //$NON-NLS-1$
		+ profileRule; //$NON-NLS-1$
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
