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

	public ImageFilter(DeltaCloud cloud) {
		super(new AllMatcher(), new AllMatcher(), cloud);
		setRules(new AllMatcher(), new AllMatcher());
	}

	public ImageFilter(String rules, DeltaCloud cloud) {
		super(cloud);
		setRules(rules);
	}

	public ImageFilter(String nameRule, String idRule, String archRule, String descRule, DeltaCloud cloud) {
		super(nameRule, idRule, cloud);
		setRules(archRule, descRule);
	}

	@Override
	protected Iterator<String> setRules(String rules) {
		Iterator<String> rulesIterator = super.setRules(rules);
		setRules(createRule(rulesIterator), createRule(rulesIterator));
		return rulesIterator;
	}

	private void setRules(String archRule, String descRule) {
		setRules(createRule(archRule), createRule(descRule));
	}

	private void setRules(IFieldMatcher archMatcher, IFieldMatcher descMatcher) {
		this.archRule = archMatcher;
		this.descRule = descMatcher;
	}

	@Override
	public boolean matches(DeltaCloudImage image) {
		return super.matches(image) &&
				archRule.matches(image.getArchitecture()) &&
				descRule.matches(image.getDescription());
	}

	@Override
	public String toString() {
		return super.toString() //$NON-NLS-1$
				+ archRule + EXPRESSION_DELIMITER //$NON-NLS-1$
				+ descRule; //$NON-NLS-1$
	}

	public IFieldMatcher getArchRule() {
		return archRule;
	}

	public IFieldMatcher getDescRule() {
		return descRule;
	}
}
