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

import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
import org.jboss.tools.smooks.model.AbstractResourceConfig;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.ResourceType;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.xml.model.TagList;
import org.jboss.tools.smooks.xml.model.TagObject;
import org.jboss.tools.smooks.xml.model.TagPropertyObject;
import org.jboss.tools.smooks.xml.model.XMLObjectAnalyzer;

/**
 * @author Dart Peng<br>
 *         Date : Sep 23, 2008
 */
public class AbstractXMLModelAnalyzer implements ISourceModelAnalyzer,
		ITargetModelAnalyzer {

	public static final String FILE_PRIX = "File:/"; //$NON-NLS-1$

	public static final String WORKSPACE_PRIX = "Workspace:/"; //$NON-NLS-1$

	public static final String RESOURCE = "Resource:/";

	public static final String XSL_NAMESPACE = " xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" ";

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
					throw new InvocationTargetException(
							new Exception(
									Messages
											.getString("AbstractXMLModelAnalyzer.FileDosentExistErrorMessage1") + path + Messages.getString("AbstractXMLModelAnalyzer.FileDosentExistErrorMessage2"))); //$NON-NLS-1$ //$NON-NLS-2$
				}
			} else {
				throw new InvocationTargetException(
						new Exception(
								Messages
										.getString("AbstractXMLModelAnalyzer.IllegalPathErrorMessage1") + path + ".")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return path;
	}

	public Object buildSourceInputObjects(GraphInformations graphInfo,
			SmooksResourceListType listType, IFile sourceFile, Object viewer)
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
		TagList document = new TagList();
		if (path != null) {
			path = parseFilePath(path);
			try {
				InputStream stream = null;
				if (!new File(path).exists()) {
					// maybe it's resource path:
					stream = getClass().getResourceAsStream(path);
				} else {
					stream = new FileInputStream(path);
				}
				XMLObjectAnalyzer objectBuilder = new XMLObjectAnalyzer();
				document = objectBuilder.analyze(stream, null);
				if (viewer != null && viewer instanceof PropertyChangeListener) {
					document
							.addNodePropetyChangeListener((PropertyChangeListener) viewer);
				}
				return document;
			} catch (FileNotFoundException e) {
				throw new InvocationTargetException(
						new Exception(
								"Can't find the file from .graph file , please make sure the source/target data xml exists"));
			} catch (DocumentException e) {
				throw new InvocationTargetException(e);
			}
		} else {
			String sid = graphInfo.getMappingType().getSourceTypeID();
			String tid = graphInfo.getMappingType().getTargetTypeID();
			if (sid.equals("org.jboss.tools.smooks.xml.viewerInitor.xml")
					&& tid
							.equals("org.jboss.tools.smooks.xml.viewerInitor.xml")) {
				List list = listType.getAbstractResourceConfig();
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					AbstractResourceConfig re = (AbstractResourceConfig) iterator
							.next();
					if (re instanceof ResourceConfigType) {
						ResourceConfigType config = (ResourceConfigType) re;
						ResourceType resource = config.getResource();
						if (resource != null) {
							if ("xsl".equals(resource.getType())) {
								String cdata = resource.getCDATAValue();
								if (cdata != null) {
									cdata = processXSLFragmentString(cdata);
									XMLObjectAnalyzer fragmentBuilder = new XMLObjectAnalyzer();
									try {
										TagObject tag = fragmentBuilder
												.analyzeFregment(
														new ByteArrayInputStream(
																cdata
																		.getBytes()),
														new String[] {
																"value-of",
																"null_xsl" });
										if (tag != null) {
											if (viewer instanceof PropertyChangeListener) {
												document
														.addNodePropetyChangeListener((PropertyChangeListener) viewer);
												hookNodes(
														tag,
														(PropertyChangeListener) viewer);
												document.addRootTag(tag);
											}
										}

									} catch (DocumentException e1) {
										e1.printStackTrace();
									}
								}
							}
						}
					}
				}
			} else {
				throw new InvocationTargetException(
						new Exception(
								"Can't load xml file from .graph file , please make sure that the .graph file records any source/target xml file path"));
			}
		}
		return document;
	}

	private String processXSLFragmentString(String cdata) {
		// cdata = cdata.replaceAll(":", "-");
		int start_index = cdata.indexOf("<");
		int end_index = cdata.indexOf("/>");
		if (end_index == -1) {
			end_index = cdata.indexOf(">");
		}
		if (start_index == -1 || end_index == -1)
			return cdata;
		String contents = cdata.substring(start_index, end_index);
		if (contents.indexOf("\"http://www.w3.org/1999/XSL/Transform\"") != -1) {
			return cdata;
		}

		String second_frg = cdata.substring(end_index, cdata.length());

		cdata = contents + XSL_NAMESPACE + second_frg;
		return cdata;
	}

	public static void hookNodes(TagObject tag, PropertyChangeListener viewer) {
		tag.setCanEdit(true);
		tag.addNodePropetyChangeListener(viewer);
		List<TagPropertyObject> properties = tag.getProperties();
		for (Iterator iterator = properties.iterator(); iterator.hasNext();) {
			TagPropertyObject tagPropertyObject = (TagPropertyObject) iterator
					.next();
			tagPropertyObject.setCanEdit(true);
			tagPropertyObject.addNodePropetyChangeListener(viewer);
		}
		List list = tag.getXMLNodeChildren();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof TagObject) {
				hookNodes((TagObject) object, viewer);
			}
		}
	}

	public Object buildTargetInputObjects(GraphInformations graphInfo,
			SmooksResourceListType listType, IFile sourceFile, Object viewer)
			throws InvocationTargetException {
		return buildSourceInputObjects(graphInfo, listType, sourceFile, viewer);
	}
}
