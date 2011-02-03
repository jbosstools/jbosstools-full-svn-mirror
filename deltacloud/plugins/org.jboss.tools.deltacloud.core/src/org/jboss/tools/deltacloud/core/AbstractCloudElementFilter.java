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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public abstract class AbstractCloudElementFilter<CLOUDELEMENT extends IDeltaCloudElement> implements
		ICloudElementFilter<CLOUDELEMENT> {

	private DeltaCloud cloud;
	private IFieldMatcher nameRule;
	private IFieldMatcher idRule;


	public AbstractCloudElementFilter(DeltaCloud cloud) {
		setRules(new AllMatcher(), new AllMatcher());
		this.cloud = cloud;
	}

	public AbstractCloudElementFilter(String nameRule, String idRule, DeltaCloud cloud) {
		setRules(nameRule, idRule);
		this.cloud = cloud;
	}

	public AbstractCloudElementFilter(IFieldMatcher nameRule, IFieldMatcher idRule, DeltaCloud cloud) {
		setRules(nameRule, idRule);
		this.cloud = cloud;
	}

	protected Iterator<String> setRules(String rules) {
		Iterator<String> rulesIterator = Arrays.asList(rules.split(EXPRESSION_DELIMITER)).iterator();
		setRules(createRule(rulesIterator), createRule(rulesIterator));
		return rulesIterator;
	}

	private void setRules(String nameRule, String idRule) {
		setRules(createRule(nameRule), createRule(idRule));
	}

	private void setRules(IFieldMatcher nameMatcher, IFieldMatcher idMatcher) {
		this.nameRule = nameMatcher;
		this.idRule = idMatcher;
	}

	public Collection<CLOUDELEMENT> filter(CLOUDELEMENT[] cloudElements) throws DeltaCloudException {
		List<CLOUDELEMENT> filteredElements = new ArrayList<CLOUDELEMENT>();
		for (CLOUDELEMENT cloudElement : cloudElements) {
			if (matches(cloudElement)) {
				filteredElements.add(cloudElement);
			}
		}
		return filteredElements;
	}

	protected boolean matches(CLOUDELEMENT cloudElement) {
		return nameRule.matches(cloudElement.getName())
				&& idRule.matches(cloudElement.getId());
	}

	protected IFieldMatcher createRule(Iterator<String> rulesIterator) {
		if (!rulesIterator.hasNext()) {
			return new AllMatcher();
		}
		return createRule(rulesIterator.next());
	}

	protected IFieldMatcher createRule(String rule) {
		if (rule == null || rule.equals(ALL_MATCHER_EXPRESSION)) {
			return new AllMatcher();
		} else {
			return new StringMatcher(rule);
		}
	}

	protected DeltaCloud getCloud() {
		return cloud;
	}
	
	@Override
	public String toString() {
		return nameRule + ";" //$NON-NLS-1$ 
				+ idRule + ";"; //$NON-NLS-1$
	}

	@Override
	public IFieldMatcher getNameRule() {
		return nameRule;
	}

	@Override
	public IFieldMatcher getIdRule() {
		return idRule;
	}
}
