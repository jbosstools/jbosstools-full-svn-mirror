/*******************************************************************************
 * Copyright (c) 2007-2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.template.custom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.util.VpeStyleUtil;
import org.jboss.tools.vpe.editor.util.XmlUtil;
import org.w3c.dom.Node;

/**
 * Class which contains information about custom taglibs Singleton
 * 
 * @author mareshkau
 * 
 */
public class CustomTLDReference {

	private static final String FACELETS_LIBS_PARAMETER = "facelets.LIBRARIES"; //$NON-NLS-1$

	private static final String PARAM_VALUE = "param-value"; //$NON-NLS-1$

	/**
	 * Returns absolute path to custom template file, if such exist or null otherwise
	 * 
	 * @param sourceNode
	 * @return full path to custom template if exist or null if not exist
	 */
	public static IPath getCustomElementPath(Node sourceNode,
			VpePageContext pageContext) {
		List<TaglibData> taglibs = XmlUtil.getTaglibsForNode(sourceNode,
				pageContext);
		TaglibData sourceNodeTaglib = XmlUtil.getTaglibForPrefix(sourceNode
				.getPrefix(), taglibs);
		String uri = sourceNodeTaglib.getUri();
		CustomTLDData customTLDData = getCustomTLDDataMap(pageContext).get(uri);
		if (customTLDData == null) {
			return null;
		}
		IPath pathToSourceFile = customTLDData.getTldFilePath();
		pathToSourceFile = pathToSourceFile.removeLastSegments(1);
		String sourceParamValue = CustomTLDParser.getSourceValuetInTag(
				customTLDData.getTldFilePath(), sourceNode.getLocalName());
		if (sourceParamValue == null) {
			return null;
		}
		pathToSourceFile = pathToSourceFile.append(sourceParamValue);
		return pathToSourceFile;
	}
	/**
	 * 
	 * @param pageContext
	 * @param uri node namespace uri
	 * @return true if such template defined in facelets lib or falce if not defined
	 */
	public static boolean isExistInCustomTlds(VpePageContext pageContext,
			String uri) {
		return getCustomTLDDataMap(pageContext).containsKey(uri);
	}

	/**
	 * Looks for taglibs library
	 * @param pageContext
	 * @param sourceNode node for which we look for taglib library
	 * @return path to taglibs, if such exists or null otherwise
	 */
	public static IPath getCustomTLDPath(VpePageContext pageContext, Node sourceNode) {
		List<TaglibData> taglibs = XmlUtil.getTaglibsForNode(sourceNode,
				pageContext);
		TaglibData sourceNodeTaglib = XmlUtil.getTaglibForPrefix(sourceNode
				.getPrefix(), taglibs);
		String uri = sourceNodeTaglib.getUri();
		CustomTLDData customTLDData = getCustomTLDDataMap(pageContext).get(uri);
		if(customTLDData!=null) {
			return customTLDData.getTldFilePath();
		} 
			return null;	
	}
	/**
	 * Looks for custom taglib library map
	 * @return the customTLDDataMap
	 */
	private static Map<String, CustomTLDData> getCustomTLDDataMap(
			VpePageContext pageContext) {

		Map<String, CustomTLDData> customTLDMap = new HashMap<String, CustomTLDData>();

		IEditorInput iEditorInput = pageContext.getEditPart().getEditorInput();

		if (iEditorInput instanceof IFileEditorInput) {

			IFileEditorInput iFileEditorInput = (IFileEditorInput) iEditorInput;

			IFile iFile = iFileEditorInput.getFile();

			IProject project = iFile.getProject();
			IModelNature nature = EclipseResourceUtil.getModelNature(project);
			if (nature != null) {
				XModel model = nature.getModel();
				XModelObject webXML = WebAppHelper.getWebApp(model);
				XModelObject param = WebAppHelper.findWebAppContextParam(
						webXML, CustomTLDReference.FACELETS_LIBS_PARAMETER); 
				if (param != null) {
					String value = param.getAttributeValue(PARAM_VALUE);
					if (value != null) {
						String[] libs = value.split(";");//$NON-NLS-1$
						for (String faceletLib : libs) {
							IPath rootPath = VpeStyleUtil
									.getRootPath(iEditorInput);
							IPath projectPath = ((IFileEditorInput)iEditorInput).getFile().getProject().getLocation();
							
							IPath pathToCustonTld = new Path(faceletLib);
							
							pathToCustonTld = rootPath.append(pathToCustonTld);
							pathToCustonTld = pathToCustonTld.makeRelativeTo(projectPath);
							pathToCustonTld = ((IFileEditorInput)iEditorInput).getFile().getProject().getFile(pathToCustonTld).getFullPath();
							CustomTLDData customTLDData = new CustomTLDData(
									pathToCustonTld, CustomTLDParser
											.getNameSpace(pathToCustonTld));
							if (customTLDData.getNamespace() != null) {
								customTLDMap.put(customTLDData.getNamespace(),
										customTLDData);
							}
						}
					}
				}
			}
		}
		return customTLDMap;
	}
}
