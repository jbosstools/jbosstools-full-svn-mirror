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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.XModelObjectConstants;
import org.jboss.tools.common.model.filesystems.impl.RecognizedFileImpl;
import org.jboss.tools.common.model.filesystems.impl.SimpleFileImpl;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.project.WebProject;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.util.FileUtil;
import org.jboss.tools.vpe.editor.util.XmlUtil;
import org.w3c.dom.Node;

/**
 * Class which contains information about custom taglibs Singleton
 * 
 * @author mareshkau
 * 
 */
public class CustomTLDReference {

	private static final String JSF2_CUSTOM_COMPONENT_URI = "http://java.sun.com/jsf/composite/"; //$NON-NLS-1$

	/**
	 * Returns absolute path to custom template file, if such exist or null
	 * otherwise
	 * 
	 * @param sourceNode
	 * @return full path to custom template if exist or null if not exist
	 */
	public static IStorage getCustomElementStorage(Node sourceNode,
			VpePageContext pageContext) {
		List<TaglibData> taglibs = XmlUtil.getTaglibsForNode(sourceNode,
				pageContext);
		TaglibData sourceNodeTaglib = XmlUtil.getTaglibForPrefix(sourceNode
				.getPrefix(), taglibs);
		String uri = sourceNodeTaglib.getUri();
		XModelObject xmodel = getCustomTaglibObject(pageContext, uri);

		XModelObject o = xmodel.getChildByPath(sourceNode.getLocalName()
				+ XModelObjectConstants.SEPARATOR + "declaration"); //$NON-NLS-1$
		String sourceAttributeValue = null;
		if (o != null) {
			sourceAttributeValue = o.getAttributeValue("source"); //$NON-NLS-1$
		}

		if (sourceAttributeValue == null) {
			return null;
		}
		if (xmodel instanceof SimpleFileImpl) {
			IFile iFile = ((SimpleFileImpl) xmodel).getFile();
			if (iFile == null) {
				// possibly it's a jar file

				XModelObject sourceFile = xmodel.getParent().getParent()
						.getChildByPath(sourceAttributeValue.substring(1));
				if (sourceFile instanceof RecognizedFileImpl) {
					String content = ((RecognizedFileImpl) sourceFile)
							.getAsText();
					String name = ((RecognizedFileImpl) sourceFile)
							.getPresentationString();
					IStorage customStorage = new VpeCustomStringStorage(
							content, name);
					return customStorage;
				}
				return null;
			}
			IPath pathToSourceFile = iFile.getFullPath();
			// pathToSourceFile now smth like this
			// /customFaceletsTestProject/WebContent/tags/facelets.taglib.xml
			// so we remove facelet taglib name now
			pathToSourceFile = pathToSourceFile.removeLastSegments(1);
			pathToSourceFile = pathToSourceFile.append(sourceAttributeValue);
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(
					pathToSourceFile);
			return file;
		}
		return null;
	}

	/**
	 * Checks is resource exist in custom jsf 2 tags
	 * 
	 * @author mareshkau
	 * @param pageContext
	 * @param sourceNodeUri
	 * @param sourceNodeName
	 * @return
	 */

	public static boolean isExistInJsf2CustomComponenets(
			VpePageContext pageContext, String sourceNodeUri,
			String sourceNodeName) {
		boolean result = false;
		// check uri if it jsf 2.0 URI
		sourceNodeUri = sourceNodeUri.trim();
		if (sourceNodeUri != null
				&& sourceNodeUri.startsWith(JSF2_CUSTOM_COMPONENT_URI)) {
			// calculating resource path string
			String jsfComponentResourcePath = sourceNodeUri.substring(
					JSF2_CUSTOM_COMPONENT_URI.length(), sourceNodeUri.length());
			String fileName = pageContext.getEditPart().getEditorInput()
					.getName();
			String jsfComponentExtension = ".xhtml"; //$NON-NLS-1$
			if (fileName != null && fileName.lastIndexOf('.') != -1) {
				String fileExtension = fileName.substring(fileName
						.lastIndexOf('.'), fileName.length());
				if (fileExtension != null && fileExtension.length() > 0) {
					jsfComponentExtension = fileExtension;
				}
				jsfComponentResourcePath = jsfComponentResourcePath + '/'
						+ sourceNodeName + jsfComponentExtension;
				result = FileUtil.isExistsInJSF2Resources(pageContext,
						jsfComponentResourcePath);
			}

		}
		return result;
	}

	public static IStorage getJsf2CustomComponentStorage(VpePageContext pageContext, String sourceNodeUri, String sourceNodeName){
		IStorage result=null;
		//check uri if it jsf 2.0 URI
		sourceNodeUri = sourceNodeUri.trim();
		if(sourceNodeUri!=null && sourceNodeUri.startsWith(JSF2_CUSTOM_COMPONENT_URI)) {
			//calculating resource path string
			String jsfComponentResourcePath = sourceNodeUri.substring(JSF2_CUSTOM_COMPONENT_URI.length(),sourceNodeUri.length());
			String fileName = pageContext.getEditPart().getEditorInput().getName();
			String jsfComponentExtension = ".xhtml"; //$NON-NLS-1$
			if(fileName!=null && fileName.lastIndexOf('.')!=-1){
				String fileExtension = fileName.substring(fileName.lastIndexOf('.'),fileName.length());
				if(fileExtension!=null && fileExtension.length()>0) {
					jsfComponentExtension = fileExtension;
				}
				jsfComponentResourcePath=jsfComponentResourcePath+'/'+sourceNodeName+jsfComponentExtension;
				String fullResourcePath = FileUtil.getJSF2ResourcePath(pageContext, jsfComponentResourcePath);
				result = FileUtil.getFile(pageContext.getEditPart().getEditorInput(), fullResourcePath); 
				if(result!=null) {
					return result;
				}
				File fileToOpen = new File(fullResourcePath);
				if (fileToOpen.exists() && fileToOpen.isFile()) {
					StringBuffer fileContent = new StringBuffer();
					 try {
						 BufferedReader input =  new BufferedReader(new FileReader(fileToOpen));
						 try {
						        String line = null; //not declared within while loop
						        /*
						        * readLine is a bit quirky :
						        * it returns the content of a line MINUS the newline.
						        * it returns null only for the END of the stream.
						        * it returns an empty String if two newlines appear in a row.
						        */
						        while (( line = input.readLine()) != null){
						        	fileContent.append(line);
						 
						        }

						 } catch (IOException e) {
							 VpePlugin.reportProblem(e);
						}finally {
					         input.close();
					       }

					} catch (FileNotFoundException e) {
						VpePlugin.reportProblem(e);
					}catch (IOException e) {
						 VpePlugin.reportProblem(e);
					}
					result = new VpeCustomStringStorage(fileContent.toString(), fileToOpen.getName());
			}
		}
		}
		return result;
	}

	/**
	 * 
	 * @param pageContext
	 * @param uri
	 *            node namespace uri
	 * @return true if such template defined in facelets lib or falce if not
	 *         defined
	 */
	public static boolean isExistInCustomTlds(VpePageContext pageContext,
			String uri) {
		return getCustomTaglibObject(pageContext, uri) != null ? true : false;
	}

	/**
	 * Looks for custom taglib library map
	 * 
	 * @return the customTLDDataMap
	 */
	private static XModelObject getCustomTaglibObject(
			VpePageContext pageContext, String uri) {
		IEditorInput editorInput = pageContext.getEditPart().getEditorInput();

		if (editorInput instanceof IFileEditorInput) {
			XModel xm = null;
			IProject project = ((IFileEditorInput) editorInput).getFile()
					.getProject();
			IModelNature mn = EclipseResourceUtil.getModelNature(project);
			if (mn != null) {
				xm = mn.getModel();
			}
			if (xm != null) {
				return WebProject.getInstance(xm).getTaglibMapping()
						.getTaglibObject(uri);
			}
		}
		return null;
	}

}
