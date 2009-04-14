/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors.edireader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.PropertyUICreator;
import org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.model.edi.EdiPackage;

/**
 * @author Dart Peng (dpeng@redhat.com) Date Apr 10, 2009
 */
public class EDIReaderUICreator extends PropertyUICreator {

	public EDIReaderUICreator() {
	}

	private void openFile(IItemPropertyDescriptor propertyDescriptor, Object model) {
		Object path = SmooksUIUtils.getEditValue(propertyDescriptor, model);
		String p = null;
		if (path != null && path instanceof String) {
			p = ((String) path).trim();
		}
		try {
			IResource resource = SmooksUIUtils.getResource((EObject) model);
			IFile file1 = null;
			if (resource != null) {
				IProject project = resource.getProject();
				IJavaProject javaProject = JavaCore.create(project);
				if (javaProject != null) {
					IClasspathEntry[] classPathEntrys = javaProject.getRawClasspath();
					for (int i = 0; i < classPathEntrys.length; i++) {
						IClasspathEntry entry = classPathEntrys[i];
						if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
							IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(entry.getPath());
							if (folder != null && folder.exists()) {
								IFile file = folder.getFile(new Path(p));
								if (file != null && file.exists()) {
									file1 = file;
									break;
								}
							}
						}
					}
				}
				if (file1 != null) {
					IWorkbenchWindow window = SmooksConfigurationActivator.getDefault().getWorkbench().getActiveWorkbenchWindow();
					window.getActivePage().openEditor(new FileEditorInput(file1), SmooksMultiFormEditor.EDITOR_ID);
				} else {
					String message = "Path is null";
					if (p != null && p.length() != 0) {
						message = "Can't find file : " + p;
					}
					MessageDialog.openInformation(SmooksConfigurationActivator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
							"Can't open editor", message);
				}
			}
		} catch (Exception e) {

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.IPropertyUICreator#
	 * createPropertyUI(org.eclipse.ui.forms.widgets.FormToolkit,
	 * org.eclipse.swt.widgets.Composite,
	 * org.eclipse.emf.edit.provider.IItemPropertyDescriptor, java.lang.Object,
	 * org.eclipse.emf.ecore.EAttribute)
	 */
	public Composite createPropertyUI(FormToolkit toolkit, Composite parent, IItemPropertyDescriptor propertyDescriptor, Object model,
			EAttribute feature, SmooksMultiFormEditor formEditor) {
		if (feature == EdiPackage.eINSTANCE.getEDIReader_Encoding()) {
		}
		if (feature == EdiPackage.eINSTANCE.getEDIReader_MappingModel()) {
			// IResource resource =SmooksUIUtils.getResource((EObject)model);
			// if(resource != null){
			// final IProject project = resource.getProject();
			// ViewerFilter viewerFilter = new ViewerFilter(){
			// @Override
			// public boolean select(Viewer viewer, Object parentElement, Object
			// element) {
			// IResource re = null;
			// if(element instanceof IResource){
			// re = (IResource)element;
			// }
			// if(element instanceof IAdaptable){
			// re = (IResource)
			// ((IAdaptable)element).getAdapter(IResource.class);
			// }
			// if(re != null){
			// if(re.getProject() == project){
			// return true;
			// }
			// }
			// return false;
			// }
			// };
			// List<ViewerFilter> list = new ArrayList<ViewerFilter>();
			// list.add(viewerFilter);
			// setDialogViewerFilters(list);
			// }
			final Object fm = model;
			final IItemPropertyDescriptor fpd = propertyDescriptor;
			IHyperlinkListener listener = new IHyperlinkListener() {

				public void linkActivated(HyperlinkEvent e) {
					openFile(fpd, fm);
				}

				public void linkEntered(HyperlinkEvent e) {

				}

				public void linkExited(HyperlinkEvent e) {

				}

			};
			SmooksUIUtils.createLinkTextValueFieldEditor("Mapping Model", (AdapterFactoryEditingDomain) formEditor.getEditingDomain(), propertyDescriptor,
					toolkit, parent, model, false, 0, true, listener);
			return parent;
		}
		return super.createPropertyUI(toolkit, parent, propertyDescriptor, model, feature, formEditor);
	}

	@Override
	public boolean isFileSelectionFeature(EAttribute attribute) {
//		if (attribute == EdiPackage.eINSTANCE.getEDIReader_MappingModel()) {
//			return true;
//		}
		return super.isFileSelectionFeature(attribute);
	}

}