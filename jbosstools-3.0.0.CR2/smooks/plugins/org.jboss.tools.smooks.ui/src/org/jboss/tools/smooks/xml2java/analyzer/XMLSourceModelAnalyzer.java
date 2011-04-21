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
package org.jboss.tools.smooks.xml2java.analyzer;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.model.SmooksResourceListType;

/**
 * @author Dart Peng<br>
 *         Date : Sep 23, 2008
 */
public class XMLSourceModelAnalyzer extends AbstractXMLModelAnalyzer {

	public XMLSourceModelAnalyzer() {
		super("sourceDataPath"); //$NON-NLS-1$
	}

	@Override
	public Object buildSourceInputObjects(GraphInformations graphInfo,
			SmooksResourceListType listType, IFile sourceFile , Object viewer)
			throws InvocationTargetException {
		return super.buildSourceInputObjects(graphInfo, listType, sourceFile,viewer);
	}
}
