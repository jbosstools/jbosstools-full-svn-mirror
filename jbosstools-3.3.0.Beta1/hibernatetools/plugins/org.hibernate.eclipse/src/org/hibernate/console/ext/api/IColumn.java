/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.hibernate.console.ext.api;

/**
 * @author Dmitry Geraskov {geraskov@gmail.com}
 *
 */
public interface IColumn {

	public String getName();

	public Integer getSqlTypeCode();

}
