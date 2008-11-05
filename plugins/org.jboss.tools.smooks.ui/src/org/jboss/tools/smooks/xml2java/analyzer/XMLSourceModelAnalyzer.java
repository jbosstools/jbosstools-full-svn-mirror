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
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.model.AbstractResourceConfig;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;

/**
 * @author Dart Peng<br>
 *         Date : Sep 23, 2008
 */
public class XMLSourceModelAnalyzer extends AbstractXMLModelAnalyzer {

	public static final String[] SELECTORE_SPLITER = new String[] { ":", "\\", "/" };

	public XMLSourceModelAnalyzer() {
		super("sourceDataPath");
	}

	@Override
	public Object buildSourceInputObjects(GraphInformations graphInfo,
			SmooksResourceListType listType, IFile sourceFile)
			throws InvocationTargetException {
		List<AbstractResourceConfig> lists = listType
				.getAbstractResourceConfig();
		for (Iterator<AbstractResourceConfig> iterator = lists.iterator(); iterator
				.hasNext();) {
			ResourceConfigType resourceConfig = (ResourceConfigType) iterator
					.next();
			String selector = resourceConfig.getSelector();
			checkBindingSelector(selector);
			List<Object> list = SmooksModelUtils
					.getBindingListFromResourceConfigType(resourceConfig);
			if(list == null) continue;
			for (Iterator<Object> iterator2 = list.iterator(); iterator2
					.hasNext();) {
				AnyType binding = (AnyType) iterator2.next();
				String bindingMessage = SmooksModelUtils
						.getAttributeValueFromAnyType(binding,
								SmooksModelUtils.ATTRIBUTE_SELECTOR);
				checkBindingSelector(bindingMessage);
			}
		}
		return super.buildSourceInputObjects(graphInfo, listType, sourceFile);
	}

	protected void checkBindingSelector(String message)
			throws InvocationTargetException {
		if(message == null) return;
		for (int i = 0; i < SELECTORE_SPLITER.length; i++) {
			String splitString = SELECTORE_SPLITER[i];
			if (message.indexOf(splitString) != -1) {
				throw new InvocationTargetException(new Exception(
						"[XML] The Selector string dosen't support \""
								+ splitString + "\" character"));
			}
		}
	}

}
