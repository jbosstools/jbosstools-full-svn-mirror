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
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.runtime.Assert;

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
		this.cloud = cloud;
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

	public abstract void setRules(String rulesString) throws PatternSyntaxException;

	protected Iterator<String> setRules(String ruleString, Iterator<String> rulesIterator)
			throws PatternSyntaxException {
		this.nameRule = createRule(rulesIterator);
		this.idRule = createRule(rulesIterator);
		return rulesIterator;
	}

	protected Iterator<String> getRulesIterator(String ruleString) {
		return Arrays.asList(ruleString.split(";")).iterator();
	}

	protected IFieldMatcher createRule(Iterator<String> rulesIterator) {
		Assert.isLegal(rulesIterator.hasNext());
		String expression = rulesIterator.next();
		if (expression.equals(ALL_MATCHER_EXPRESSION)) { //$NON-NLS-1$
			return new AllFieldMatcher();
		} else {
			return new FieldMatcher(expression);
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
