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
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public interface IInstanceFilter extends ICloudElementFilter<DeltaCloudInstance> {
	
	public static final String ALL_STRING =
		ALL_MATCHER_EXPRESSION + EXPRESSION_DELIMITER // name
				+ ALL_MATCHER_EXPRESSION + EXPRESSION_DELIMITER // id
				+ ALL_MATCHER_EXPRESSION + EXPRESSION_DELIMITER // alias
				+ ALL_MATCHER_EXPRESSION + EXPRESSION_DELIMITER // imageId
				+ ALL_MATCHER_EXPRESSION + EXPRESSION_DELIMITER // ownerId
				+ ALL_MATCHER_EXPRESSION + EXPRESSION_DELIMITER // keynameId
				+ ALL_MATCHER_EXPRESSION + EXPRESSION_DELIMITER // realm
				+ ALL_MATCHER_EXPRESSION;	// profile

	public IFieldMatcher getAliasRule();
	public IFieldMatcher getImageIdRule();
	public IFieldMatcher getOwnerIdRule();
	public IFieldMatcher getKeyNameRule();
	public IFieldMatcher getRealmRule();
	public IFieldMatcher getProfileRule();
}
