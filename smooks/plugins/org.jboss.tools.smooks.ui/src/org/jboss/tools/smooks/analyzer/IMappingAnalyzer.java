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

import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;

/**
 * @author Dart Peng
 * 
 */
public interface IMappingAnalyzer {
	/**
	 * Parse the graphical model and generate the smooks config file model
	 * @param context
	 * @throws SmooksAnalyzerException
	 */
	public void analyzeMappingGraphModel(
			SmooksConfigurationFileGenerateContext context)
			throws SmooksAnalyzerException;
	
	/**
	 * Parse the smooks config file model what loaded by EMF , and create the relating graphical model for it.
	 * @param listType
	 * @param sourceObject
	 * @param targetObject
	 * @return
	 */
	public MappingResourceConfigList analyzeMappingSmooksModel(
			SmooksResourceListType listType,Object sourceObject,Object targetObject);
	
	/**
	 * Analyze the graphical model to find the incorrect logic model and return the analyze result.<p>
	 * The graphical editor will display the analyze result.
	 * @param context
	 * @return
	 */
	public DesignTimeAnalyzeResult[] analyzeGraphModel(SmooksConfigurationFileGenerateContext context);
	
}

