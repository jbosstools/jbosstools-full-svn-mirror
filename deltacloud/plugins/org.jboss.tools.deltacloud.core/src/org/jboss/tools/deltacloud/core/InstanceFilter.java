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

/**
 * A filter that may be applied on DeltaCloudInstances
 * 
 * @see DeltaCloudInstance
 * 
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public class InstanceFilter extends AbstractCloudElementFilter<DeltaCloudInstance> implements IInstanceFilter {

	private IFieldMatcher aliasRule;
	private IFieldMatcher imageIdRule;
	private IFieldMatcher ownerIdRule;
	private IFieldMatcher keyNameRule;
	private IFieldMatcher realmRule;
	private IFieldMatcher profileRule;

	public InstanceFilter(DeltaCloud cloud) {
		this(new AllMatcher(), new AllMatcher(), new AllMatcher(), new AllMatcher(), new AllMatcher(),
				new AllMatcher(), new AllMatcher(), new AllMatcher(), cloud);
	}

	public InstanceFilter(IFieldMatcher nameMatcher, IFieldMatcher idMatcher, IFieldMatcher aliasMatcher,
			IFieldMatcher imageIdMatcher, IFieldMatcher ownerIdMatcher, IFieldMatcher keyMatcher,
			IFieldMatcher realmMatcher, IFieldMatcher profileMatcher, DeltaCloud cloud) {
		super(nameMatcher, idMatcher, cloud);
		setRules(aliasMatcher, imageIdMatcher, ownerIdMatcher, keyMatcher, realmMatcher, profileMatcher);
	}

	public InstanceFilter(String rulesString, DeltaCloud cloud) {
		super(cloud);
		setRules(rulesString);
	}

	public InstanceFilter(String nameRule, String idRule, String aliasRule, String imageIdRule,
			String ownerIdRule, String keyNameRule, String realmRule, String profileRule, DeltaCloud cloud) {
		super(nameRule, idRule, cloud);
		setRules(aliasRule, imageIdRule, ownerIdRule, keyNameRule, realmRule, profileRule);
	}

	@Override
	protected Iterator<String> setRules(String rules) {
		Iterator<String> rulesIterator = super.setRules(rules);
		setRules(createRule(rulesIterator), createRule(rulesIterator), createRule(rulesIterator),
				createRule(rulesIterator), createRule(rulesIterator), createRule(rulesIterator));
		return rulesIterator;
	}

	private void setRules(String aliasRule, String imageIdRule, String ownerIdRule, String keyNameRule,
			String realmRule, String profileRule) {
		setRules(createRule(aliasRule), createRule(imageIdRule), createRule(ownerIdRule), createRule(keyNameRule),
				createRule(realmRule), createRule(profileRule));
	}

	private void setRules(IFieldMatcher aliasMatcher, IFieldMatcher imageIdMatcher, IFieldMatcher ownerIdMatcher,
			IFieldMatcher keyNameMatcher, IFieldMatcher realmMatcher, IFieldMatcher profileMatcher) {
		this.aliasRule = aliasMatcher;
		this.imageIdRule = imageIdMatcher;
		this.keyNameRule = keyNameMatcher;
		this.ownerIdRule = ownerIdMatcher;
		this.realmRule = realmMatcher;
		this.profileRule = profileMatcher;
	}

	@Override
	public boolean matches(DeltaCloudInstance instance) {
		return super.matches(instance) &&
				aliasRule.matches(instance.getAlias()) &&
				imageIdRule.matches(instance.getImageId()) &&
				ownerIdRule.matches(instance.getOwnerId()) &&
				keyNameRule.matches(instance.getKeyId()) &&
				realmRule.matches(instance.getRealmId()) &&
				profileRule.matches(instance.getProfileId());
	}

	@Override
	public String toString() {
		return super.toString()
				+ aliasRule + EXPRESSION_DELIMITER
				+ imageIdRule + EXPRESSION_DELIMITER
				+ ownerIdRule + EXPRESSION_DELIMITER
				+ keyNameRule + EXPRESSION_DELIMITER
				+ realmRule + EXPRESSION_DELIMITER
				+ profileRule; //$NON-NLS-1$
	}

	@Override
	public IFieldMatcher getImageIdRule() {
		return imageIdRule;
	}

	@Override
	public IFieldMatcher getAliasRule() {
		return aliasRule;
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
