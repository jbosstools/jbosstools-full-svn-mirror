/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.template;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeIncludeInfo;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.custom.CustomTLDReference;
import org.mozilla.interfaces.nsIDOMDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author mareshkau
 * 
 */
public class VpeCustomTemplate extends VpeIncludeTemplate {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.vpe.editor.template.VpeIncludeTemplate#create(org.jboss
	 * .tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node,
	 * org.mozilla.interfaces.nsIDOMDocument)
	 */
	@Override
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {

		IPath pathToFile = CustomTLDReference
				.getCustomElementPath(sourceNode, pageContext);

		if (pathToFile != null) {

				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(
						pathToFile);
				if (file != null && file.exists()) {
					if (!pageContext.getVisualBuilder().isFileInIncludeStack(
							file)) {
						Document document = pageContext.getVisualBuilder()
								.getIncludeDocuments().get(file);
						if (document == null) {
							document = VpeCreatorUtil.getDocumentForRead(file);
							if (document != null)
								pageContext.getVisualBuilder()
										.getIncludeDocuments().put(file,
												document);
						}
						if (document != null) {
							VpeCreationData creationData = createInclude(
									document, visualDocument);
							creationData.setData(file);
							pageContext.getVisualBuilder().pushIncludeStack(
									new VpeIncludeInfo((Element) sourceNode,
											file, document));
							return creationData;
						}
					}
				}
		}
		VpeCreationData creationData = createStub(sourceNode.getNodeName(), visualDocument);
		creationData.setData(null);
		return creationData;
	}
	@Override
	public void openIncludeEditor(VpePageContext pageContext, Element sourceElement, Object data) {
		
		IFile file = getFileForOpenOn(pageContext, sourceElement);
		
		if(file!=null && file.exists()) {
			IWorkbenchPage workbenchPage = VpePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				IDE.openEditor(workbenchPage, file,true);
			} catch (PartInitException e) {
						VpePlugin.reportProblem(e);
			}
		}
	}
	/**
	 * Looks for file to open on each editor, for open on click
	 * @param pageContext
	 * @param sourceElement
	 * @return file, if file has been founded or null otherwise
	 */
	private static IFile getFileForOpenOn(VpePageContext pageContext, Element sourceElement) {
		IPath pathToFile = CustomTLDReference
		.getCustomElementPath(sourceElement, pageContext);
		
		IFile file =null;
		
		if(pathToFile!=null) {
			file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(
				pathToFile);
		}
		//if we cann't find source file, then just open tld definition file
		if(file==null || !file.exists()) {
			pathToFile = CustomTLDReference.getCustomTLDPath(pageContext, sourceElement);
			file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(
					pathToFile);
		}
		return file;
	}
}
