/*******************************************************************************
 * Copyright (c) 2007-2011 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jsf.web.validation.jsf2.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.common.model.XModelObjectConstants;
import org.jboss.tools.jsf.JSFModelPlugin;
import org.jboss.tools.jsf.jsf2.util.JSF2ResourceUtil;
import org.jboss.tools.jst.jsp.editor.IVisualContext;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPTextEditor;
import org.jboss.tools.jst.jsp.jspeditor.SourceEditorPageContext;
import org.jboss.tools.jst.jsp.jspeditor.dnd.JSPPaletteInsertHelper;
import org.jboss.tools.jst.jsp.jspeditor.dnd.PaletteTaglibInserter;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.jst.web.tld.URIConstants;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class CreateJSF2CompositeAction extends Action {

	private final String IMPLEMENTATION = "composite:implementation";  //$NON-NLS-1$
	
	private JSPMultiPageEditor editor;

	public CreateJSF2CompositeAction() {
		super();
	}

	@Override
	public void run() {
		editor = (JSPMultiPageEditor) PlatformUI.getWorkbench()
		.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		/*
		 * Get the selected text
		 */
		ISelection selection = editor.getSelectionProvider().getSelection();
		if (selection instanceof TextSelection) {
			TextSelection textSelection = (TextSelection) selection;
			String text = textSelection.getText();
			final IProject project = editor.getProject();
			/*
			 * Get composite template creator
			 */
			InputDialog dlg = new InputDialog(Display.getCurrent().getActiveShell(),
					"Creating composite component", //$NON-NLS-1$
					"Enter component's namespace and name:", "namespace:name", //$NON-NLS-1$ //$NON-NLS-2$
					new IInputValidator() {
						public String isValid(String newText) {
							String trim = newText.trim();
							String result = null;
							String[] split = trim.split(":", 2); //$NON-NLS-1$
							Pattern p = Pattern.compile("([a-zA-Z]+\\d*)+"); //$NON-NLS-1$ 
							/*
							 * Check the correct format.
							 * Matcher will accept only word characters with optional numbers.
							 */
							if ((split.length != 2) || trim.startsWith(":") || trim.endsWith(":") //$NON-NLS-1$ //$NON-NLS-2$
									|| (split[0].length() == 0) || (split[1].length() == 0)) {
								result = "Component's name should fit in the pattern \"namespace:name\""; //$NON-NLS-1$
							} else if(!p.matcher(split[0]).matches()) {
								result = "Namespace '"+split[0]+"' has wrong spelling, please correct"; //$NON-NLS-1$ //$NON-NLS-2$
							} else if(!p.matcher(split[1]).matches()) {
								result = "Name '"+split[1]+"' has wrong spelling, please correct"; //$NON-NLS-1$ //$NON-NLS-2$
							} else {
								String nameSpaceURI = JSF2ResourceUtil.JSF2_URI_PREFIX + "/" + split[0];  //$NON-NLS-1$
								Object fld = JSF2ResourceUtil.findResourcesFolderContainerByNameSpace(project, nameSpaceURI);
								if (fld instanceof IFolder) {
									IResource res = ((IFolder) fld).findMember(split[1]+ ".xhtml"); //$NON-NLS-1$
									if ((res instanceof IFile) && ((IFile)res).exists() ) {
										result = "Component with the same name already exists"; //$NON-NLS-1$
									}
								}
							}
							return result;
						}
					});
			if (dlg.open() == Window.OK) {
				/*
				 * Create all required files
				 */
				String componentName = dlg.getValue();
				String[] split = componentName.split(":", 2); //$NON-NLS-1$
				String path = ""; //$NON-NLS-1$
				path = componentName.replaceAll(":", "/") + ".xhtml"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				if (project != null) {
					try {
						IFile createdFile = JSF2ResourceUtil
						.createCompositeComponentFile(project, new Path(path));
						/*
						 * Add selected text to the template
						 */
						IModelManager manager = StructuredModelManager.getModelManager();
						if (manager != null) {
							IStructuredModel model = null;
							try {
								model = manager.getModelForEdit(createdFile);
								if (model instanceof IDOMModel) {
									IDOMModel domModel = (IDOMModel) model;
									IDOMDocument document = domModel.getDocument();
									NodeList list = document.getElementsByTagName(IMPLEMENTATION);
									if (list.getLength() == 1) {
										String content = document.getStructuredDocument().getText();
										int index = content.indexOf("<"+IMPLEMENTATION+">") + ("<"+IMPLEMENTATION+">").length(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
										content = content.subSequence(0, index) + "\n" + text + content.subSequence(index, content.length()); //$NON-NLS-1$
										domModel.reload(new ByteArrayInputStream(content.getBytes()));
										model.save();
										/*
										 * Register JSF 2 composite on the current page
										 */
										IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
										int offset = textSelection.getOffset();
										int length = textSelection.getLength();
										String replacement = "<" + componentName + "> </" + componentName + ">"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
										doc.replace(offset, length, replacement);
										/*
										 * Register the required taglib
										 */
										StructuredTextEditor ed = ((JSPMultiPageEditor) editor).getSourceEditor();
										if (ed instanceof JSPTextEditor) {
											String libraryUri = JSF2ResourceUtil.JSF2_URI_PREFIX + "/" + split[0]; //$NON-NLS-1$
											PaletteTaglibInserter PaletteTaglibInserter = new PaletteTaglibInserter();
											Properties p = new Properties();
											p.put("selectionProvider", editor.getSelectionProvider()); //$NON-NLS-1$
											p.setProperty(URIConstants.LIBRARY_URI, libraryUri);
											p.setProperty(URIConstants.LIBRARY_VERSION, ""); //$NON-NLS-1$
											p.setProperty(URIConstants.DEFAULT_PREFIX, split[0]);
											p.setProperty(JSPPaletteInsertHelper.PROPOPERTY_ADD_TAGLIB, "true"); //$NON-NLS-1$
											p.setProperty(XModelObjectConstants.REFORMAT, "yes"); //$NON-NLS-1$
											p.setProperty(XModelObjectConstants.START_TEXT, 
													"<%@ taglib uri=\""+libraryUri+"\" prefix=\"" +split[0]+ "\" %>\\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
											PaletteTaglibInserter.inserTaglib(ed.getTextViewer().getDocument(), p);
										}
										/*
										 * Add required taglibs to the composite file
										 */
										IVisualContext context = editor.getJspEditor().getPageContext();
										List<TaglibData> taglibs = null;
										if (context instanceof SourceEditorPageContext) {
											SourceEditorPageContext sourcePageContext = (SourceEditorPageContext) context;
											taglibs = sourcePageContext.getTagLibs();
										}										
									}
								}
							} catch (CoreException e) {
								JSFModelPlugin.getPluginLog().logError(e);
							} catch (IOException e) {
								JSFModelPlugin.getPluginLog().logError(e);
							} catch (BadLocationException e) {
								JSFModelPlugin.getPluginLog().logError(e);
							} finally {
								if (model != null) {
									model.releaseFromEdit();
								}
							}
						}
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}	
}