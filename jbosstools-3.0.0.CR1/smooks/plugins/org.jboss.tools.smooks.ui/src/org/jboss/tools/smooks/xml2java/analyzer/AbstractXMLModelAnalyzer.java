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
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
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
public class AbstractXMLModelAnalyzer implements ISourceModelAnalyzer,
		ITargetModelAnalyzer {

	public static final String FILE_PRIX = "File:/"; //$NON-NLS-1$

	public static final String WORKSPACE_PRIX = "Workspace:/"; //$NON-NLS-1$

	private String parmaKey = ""; //$NON-NLS-1$

	public AbstractXMLModelAnalyzer(String paramKey) {
		this.parmaKey = paramKey;
	}

	public static String parseFilePath(String path)
			throws InvocationTargetException {
		int index = path.indexOf(FILE_PRIX);
		if (index != -1) {
			path = path.substring(index + FILE_PRIX.length(), path.length());
		} else {
			index = path.indexOf(WORKSPACE_PRIX);
			if (index != -1) {
				path = path.substring(index + WORKSPACE_PRIX.length(), path
						.length());
				Path wpath = new Path(path);
				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(
						wpath);
				if (file.exists()) {
					path = file.getLocation().toOSString();
				} else {
					throw new InvocationTargetException(new Exception(
							Messages.getString("AbstractXMLModelAnalyzer.FileDosentExistErrorMessage1") + path + Messages.getString("AbstractXMLModelAnalyzer.FileDosentExistErrorMessage2"))); //$NON-NLS-1$ //$NON-NLS-2$
				}
			} else {
				throw new InvocationTargetException(new Exception(
						Messages.getString("AbstractXMLModelAnalyzer.IllegalPathErrorMessage1") + path + ".")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return path;
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
			 return null;
//			throw new InvocationTargetException(new Exception(
//					"xml file can't be found in the .graph file."));
		}
		path = parseFilePath(path);
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
		return buildSourceInputObjects(graphInfo, listType, sourceFile);
	}
}
