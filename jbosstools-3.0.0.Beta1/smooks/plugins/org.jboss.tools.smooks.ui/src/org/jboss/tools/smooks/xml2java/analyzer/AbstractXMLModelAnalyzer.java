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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.DocumentException;
import org.eclipse.core.resources.IFile;
import org.jboss.tools.smooks.analyzer.ISourceModelAnalyzer;
import org.jboss.tools.smooks.analyzer.ITargetModelAnalyzer;
import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.graphical.Param;
import org.jboss.tools.smooks.graphical.Params;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.xml.model.DocumentObject;
import org.jboss.tools.smooks.xml.model.XMLObjectAnalyzer;

/**
 * @author Dart Peng<br>
 *         Date : Sep 23, 2008
 */
public class AbstractXMLModelAnalyzer implements ISourceModelAnalyzer , ITargetModelAnalyzer {

	private String parmaKey = "";
	
	public AbstractXMLModelAnalyzer(String paramKey){
		this.parmaKey = paramKey;
	}

	public Object buildSourceInputObjects(GraphInformations graphInfo,
			SmooksResourceListType listType, IFile sourceFile)
			throws InvocationTargetException {
		Params params = graphInfo.getParams();
		String path = null;
		if (params != null) {
			List<Param> paramList = graphInfo.getParams().getParam();
			for (Iterator<Param> iterator = paramList.iterator(); iterator
					.hasNext();) {
				Param param = (Param) iterator.next();
				if (param.getName().equals(parmaKey)) {
					path = param.getValue();
					break;
				}
			}
		}
		if (path == null) {
			// TODO tell user the filepath can't find or not ?
			return null;
//			throw new InvocationTargetException(new Exception(
//					"xml file path can't find in the graphInfo file"));
		}
		XMLObjectAnalyzer objectBuilder = new XMLObjectAnalyzer();
		try {
			FileInputStream stream = new FileInputStream(path);
			DocumentObject document = objectBuilder.analyze(stream);
			return document;
		} catch (FileNotFoundException e) {
			throw new InvocationTargetException(e);
		} catch (DocumentException e) {
			throw new InvocationTargetException(e);
		}
	}

	public Object buildTargetInputObjects(GraphInformations graphInfo,
			SmooksResourceListType listType, IFile sourceFile)
			throws InvocationTargetException {
		return buildSourceInputObjects(graphInfo,listType,sourceFile);
	}
}
