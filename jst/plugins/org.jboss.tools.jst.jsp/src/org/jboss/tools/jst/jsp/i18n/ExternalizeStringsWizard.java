/*******************************************************************************
 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.i18n;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.document.AttrImpl;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.jboss.tools.common.EclipseUtil;
import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.meta.action.impl.handlers.DefaultCreateHandler;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.ui.ModelUIImages;
import org.jboss.tools.common.model.ui.editors.dnd.DropURI;
import org.jboss.tools.common.model.ui.views.palette.PaletteInsertHelper;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.bundle.BundleMap;
import org.jboss.tools.jst.jsp.editor.IVisualContext;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPTextEditor;
import org.jboss.tools.jst.jsp.jspeditor.SourceEditorPageContext;
import org.jboss.tools.jst.jsp.jspeditor.dnd.JSPPaletteInsertHelper;
import org.jboss.tools.jst.jsp.jspeditor.dnd.PaletteTaglibInserter;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.jsp.util.Constants;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.jst.web.tld.URIConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ExternalizeStringsWizard extends Wizard {
	
	public static final String EXTERNALIZE_STRINGS_DIALOG_NEW_FILE_PAGE = 
		"EXTERNALIZE_STRINGS_DIALOG_NEW_FILE_PAGE"; //$NON-NLS-1$
	
	ITextEditor editor = null;
	BundleMap bm = null;
	ExternalizeStringsWizardPage page1 = null;
	WizardNewFileCreationPage page2 = null;
	ExternalizeStringsWizardRegisterBundlePage page3 = null;
	
	public ExternalizeStringsWizard(ITextEditor editor, BundleMap bm) {
		super();
		setHelpAvailable(false);
		setWindowTitle(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE);
		this.editor = editor; 
		this.bm = bm;
	}
	
	@Override
	public void addPages() {
		super.addPages();
		page1 = new ExternalizeStringsWizardPage(
				ExternalizeStringsWizardPage.PAGE_NAME, editor, bm);
		page2 = new WizardNewFileCreationPage(EXTERNALIZE_STRINGS_DIALOG_NEW_FILE_PAGE,
				(IStructuredSelection) editor.getSelectionProvider().getSelection()) {
			protected InputStream getInitialContents() {
				return new ByteArrayInputStream(page1.getKeyValuePair().getBytes());
			}

			@Override
			public boolean canFlipToNextPage() {
				return isPageComplete();
			}

			@Override
			public IWizardPage getNextPage() {
				/*
				 * Update the status for the next page
				 */
				page3.updateStatus();
				return super.getNextPage();
			}
		};
		page3 = new ExternalizeStringsWizardRegisterBundlePage(
				ExternalizeStringsWizardRegisterBundlePage.PAGE_NAME);
		page2.setTitle(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE);
		page2.setDescription(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_DESCRIPTION);
		page2.setImageDescriptor(ModelUIImages.getImageDescriptor(ModelUIImages.WIZARD_DEFAULT));
		/*
		 * https://jira.jboss.org/browse/JBIDE-7247
		 * Set initial values for the new properties file
		 */
		IPath containerFullPath = null;
		if (editor.getEditorInput() instanceof IFileEditorInput) {
			IProject project = ((IFileEditorInput)editor.getEditorInput()).getFile().getProject();
			IResource[] src = EclipseUtil.getJavaSourceRoots(project);
			if (src.length > 0) {
				containerFullPath = src[0].getFullPath();
			}
		} else if (editor.getEditorInput() instanceof IStorageEditorInput) {
			try {
				containerFullPath = ((IStorageEditorInput) editor.getEditorInput()).getStorage().getFullPath();
			} catch (CoreException e) {
				JspEditorPlugin.getDefault().logError(e);
			}
		}
		if (null != containerFullPath) {
			page2.setContainerFullPath(containerFullPath);
		}
		String fileName = editor.getEditorInput().getName();
		int pos = fileName.lastIndexOf(Constants.DOT);
		if (pos != -1) {
			fileName = fileName.substring(0, pos) + Constants.PROPERTIES_EXTENTION;
		}
		/*
		 * Set the file name
		 */
		page2.setFileName(fileName);
		/*
		 * Set the supported extension
		 */
		page2.setFileExtension("properties"); //$NON-NLS-1$
		/*
		 * The new file should not exist
		 */
		page2.setAllowExistingResources(false);
		/*
		 * Add all the pages to the wizard
		 */
		addPage(page1);
		addPage(page2);
		addPage(page3);
	}

	@Override
	public boolean canFinish() {
		return (!page1.isNewFile() && page1.isPageComplete())
					|| (page1.isNewFile() && page2.isPageComplete() && page3.isPageComplete()
						&& (getContainer().getCurrentPage() == page3));
	}

	@Override
	public boolean performFinish() {
		String var = page1.getBundlePrefix();
		String key = page1.getKey();
		if (!page1.isDuplicatedKeyAndValue()) {
			IFile bundleFile = null;
			if (page1.isNewFile()) {
				bundleFile = page2.createNewFile();
			} else {
				bundleFile = page1.getBundleFile();
			}
			/*
			 * Exit when the file is null
			 */
			if (bundleFile == null) {
				return false;
			}
			/*
			 * Add "key=value" to the bundle file that is already exists. 
			 * When the file is new key and value will be written to the file content
			 * via getInitialContent() method of the page2 during the file creation. 
			 */
			if (bundleFile.exists() && !page1.isNewFile()) {
				/*
				 * https://jira.jboss.org/browse/JBIDE-7218
				 * Add only one line before adding the value. 
				 */
				String writeToFile = "\n" + page1.getKeyValuePair(); //$NON-NLS-1$
				InputStream is = new ByteArrayInputStream(writeToFile.getBytes());
				try {
					bundleFile.appendContents(is, false, true, null);
					is.close();
					is = null;
				} catch (CoreException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			/*
			 * Register new resource bundle when new file is created. 
			 */
			if (page1.isNewFile() && !page3.isUserDefined()) {
				var = page3.getBundleName();
				if (editor.getEditorInput() instanceof IFileEditorInput) {
					IProject project = ((IFileEditorInput)editor.getEditorInput()).getFile().getProject();
					String userDefinedPath = page2.getContainerFullPath().toString();
					/*
					 * Get the source folders for the project
					 */
					IResource[] src = EclipseUtil.getJavaSourceRoots(project);
					/*
					 * When there are multiple source folders --
					 * match user defined folder to them.
					 */
					String srcPath = userDefinedPath;
					String bundlePath = Constants.EMPTY;
					if (src.length > 1) {
						for (IResource res : src) {
							srcPath =  res.getFullPath().toString();
							if (userDefinedPath.indexOf(srcPath) > -1) {
								break;
							}
						}
					} else if (src.length == 1) {
						srcPath = src[0].getFullPath().toString();
					}
					/*
					 * After the source folder has been found --
					 * generate the bundle path.
					 */
					if (userDefinedPath.indexOf(srcPath) > -1) {
						/*
						 * User has selected the folder in the projects's source path.
						 * Replace the beginning
						 */
						bundlePath = userDefinedPath.replaceFirst(srcPath, "").replaceAll("/", "\\."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					} else {
						/*
						 * User defined path is different from the source folder.
						 * Thus base-name would be incorrect.
						 * Simply replace slashes with dots
						 */
						bundlePath = userDefinedPath.replaceAll("/", "\\."); //$NON-NLS-1$ //$NON-NLS-2$
					}
					if (bundlePath.startsWith(".")) { //$NON-NLS-1$
						bundlePath = bundlePath.substring(1);
					}
					String fileName = page2.getFileName();
					int pos = fileName.indexOf(Constants.PROPERTIES_EXTENTION); 
					if (pos > -1) {
						fileName = fileName.substring(0, pos);
					}
					if (bundlePath.length() != 0) {
						bundlePath = bundlePath + Constants.DOT;
					}
					bundlePath = bundlePath + fileName;
					/*
					 * And then decide where to register the resource bundle 
					 */
					if (page3.isInFacesConfig()) {
						/*
						 * Register new bundle in the faces-config.xml 
						 * We should write only correct base-name.
						 * If it is not -- then just skip it. But such a situation
						 * should never happen. 
						 */
						XModel model = EclipseResourceUtil.getModelNature(project).getModel();
						XModelObject facesConfig = ExternalizeStringsUtils.findFacesConfig(model);
						XModelObject application = facesConfig.getChildByPath("application"); //$NON-NLS-1$
						XModelObject resourceBundle = facesConfig.getModel().createModelObject("JSFResourceBundle", null); //$NON-NLS-1$
						resourceBundle.setAttributeValue("base-name", bundlePath); //$NON-NLS-1$
						resourceBundle.setAttributeValue("var", var); //$NON-NLS-1$
						try {
							DefaultCreateHandler.addCreatedObject(application, resourceBundle, 0);
						} catch (XModelException e) {
							JspEditorPlugin.getDefault().logError(
									"Could not add <resource-bundle> to the faces-config.xml", e); //$NON-NLS-1$
						}
						/*
						 * When the faces-config.xml is opened in the editor the following
						 * action should be called to ensure that changes have been applied. 
						 */
						XActionInvoker.invoke("SaveActions.Save", facesConfig, new Properties()); //$NON-NLS-1$
					} else if (page3.isViaLoadBundle()) {
						/*
						 * Add <f:loadBundle> tag to the current page.
						 * Insert the tag before currently selected tag.
						 */
						ISelection sel = editor.getSelectionProvider().getSelection();
						if (ExternalizeStringsUtils.isSelectionCorrect(sel)) {
							IStructuredSelection structuredSelection = (IStructuredSelection) sel;
							Object selectedElement = structuredSelection.getFirstElement();
							if (selectedElement instanceof Node) {
								Node node = (Node) selectedElement;
								List<TaglibData> taglibs = null;
								String jsfCoreTaglibPrefix = "f"; //$NON-NLS-1$
								boolean isJsfCoreTaglibRegistered = false;
								if (editor instanceof JSPMultiPageEditor) {
									StructuredTextEditor ed = ((JSPMultiPageEditor) editor).getSourceEditor();
									if (ed instanceof JSPTextEditor) {
										IVisualContext context =  ((JSPTextEditor) ed).getPageContext();
										if (context instanceof SourceEditorPageContext) {
											SourceEditorPageContext sourcePageContext = (SourceEditorPageContext) context;
											taglibs = sourcePageContext.getTagLibs();
											if (null == taglibs) {
												JspEditorPlugin.getDefault().logError(
														JstUIMessages.CANNOT_LOAD_TAGLIBS_FROM_PAGE_CONTEXT);
											} else {
												for (TaglibData tl : taglibs) {
													if (DropURI.JSF_CORE_URI.equalsIgnoreCase(tl.getUri())) {
														isJsfCoreTaglibRegistered = true;
														jsfCoreTaglibPrefix = tl.getPrefix();
														break;
													}
												}
												if (!isJsfCoreTaglibRegistered) {
													/*
													 * Register the required taglib
													 */
													PaletteTaglibInserter PaletteTaglibInserter = new PaletteTaglibInserter();
													Properties p = new Properties();
													p.put("selectionProvider", editor.getSelectionProvider()); //$NON-NLS-1$
													p.setProperty(URIConstants.LIBRARY_URI, DropURI.JSF_CORE_URI);
													p.setProperty(URIConstants.LIBRARY_VERSION, ""); //$NON-NLS-1$
													p.setProperty(URIConstants.DEFAULT_PREFIX, jsfCoreTaglibPrefix);
													p.setProperty(JSPPaletteInsertHelper.PROPOPERTY_ADD_TAGLIB, "true"); //$NON-NLS-1$
													p.setProperty(JSPPaletteInsertHelper.PROPOPERTY_REFORMAT_BODY, "yes"); //$NON-NLS-1$
													p.setProperty(PaletteInsertHelper.PROPOPERTY_START_TEXT, 
															"<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\\n"); //$NON-NLS-1$
													PaletteTaglibInserter.inserTaglib(ed.getTextViewer().getDocument(), p);
												}
											}
										}
									}
								}
								Element loadBundle = node.getOwnerDocument().createElement(
										jsfCoreTaglibPrefix + Constants.COLON + "loadBundle"); //$NON-NLS-1$
								loadBundle.setAttribute("var", var); //$NON-NLS-1$
								loadBundle.setAttribute("basename", bundlePath); //$NON-NLS-1$
								Node elementToInsertBefore = null;
								Node refChild = null;
								if (node.getParentNode() != null) {
									refChild = node;
									elementToInsertBefore = node.getParentNode();
								} else if (node instanceof AttrImpl) {
									AttrImpl attr = (AttrImpl) node;
									if ((attr.getOwnerElement().getParentNode() != null)){
										refChild = attr.getOwnerElement();
										elementToInsertBefore = attr.getOwnerElement().getParentNode();
									}
								}
								if ((elementToInsertBefore != null) && (refChild != null)){
									elementToInsertBefore.insertBefore(loadBundle, refChild);
								} else {
									JspEditorPlugin.getDefault().logWarning(
											JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_CANNOT_ADD_LOAD_BUNDLE_TAG);
								}
							}
						}
					}
				}
			}
		}
		/*
		 * Replace text in the editor
		 */
		String replacement = "#{" + var + Constants.DOT + key + "}"; //$NON-NLS-1$ //$NON-NLS-2$
		page1.replaceText(replacement);
		return true;
	}
}
