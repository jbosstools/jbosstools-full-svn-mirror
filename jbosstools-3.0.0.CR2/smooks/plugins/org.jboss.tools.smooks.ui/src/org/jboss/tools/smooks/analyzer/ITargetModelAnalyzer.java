/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.analyzer;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.model.SmooksResourceListType;

/**
 * @author Dart Peng<br>
 *         Date : Sep 5, 2008
 */
public interface ITargetModelAnalyzer {
	public Object buildTargetInputObjects(GraphInformations graphInfo,
			SmooksResourceListType listType, IFile sourceFile , Object viewer )
			throws InvocationTargetException;
}
