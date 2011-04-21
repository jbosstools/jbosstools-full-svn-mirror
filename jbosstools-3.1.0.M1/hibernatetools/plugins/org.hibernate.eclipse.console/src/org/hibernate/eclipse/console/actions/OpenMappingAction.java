/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.hibernate.eclipse.console.actions;

import java.io.FileNotFoundException;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.eclipse.ui.texteditor.ITextEditor;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.eclipse.console.HibernateConsoleMessages;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.hibernate.eclipse.console.utils.OpenMappingUtils;
import org.hibernate.eclipse.console.utils.ProjectUtils;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.RootClass;

/**
 * Open Mapping File action
 * 
 * @author Dmitry Geraskov
 * @author Vitali Yemialyanchyk
 */
public class OpenMappingAction extends SelectionListenerAction {

	private final String imageFilePath =  "icons/images/mapping.gif"; //$NON-NLS-1$

	public OpenMappingAction() {
		super(HibernateConsoleMessages.OpenMappingAction_open_mapping_file);
		setToolTipText(HibernateConsoleMessages.OpenMappingAction_open_mapping_file);
		setEnabled(true);
		setImageDescriptor(HibernateConsolePlugin.getImageDescriptor(imageFilePath ));
	}

	public void run() {
		IStructuredSelection sel = getStructuredSelection();
		if (!(sel instanceof TreeSelection)) {
			return;
		}
		TreePath[] paths = ((TreeSelection)sel).getPaths();
		for (int i = 0; i < paths.length; i++) {
			TreePath path = paths[i];
			ConsoleConfiguration consoleConfig = (ConsoleConfiguration)(path.getSegment(0));
			try {
				run(consoleConfig, path);
			} catch (JavaModelException e) {
				HibernateConsolePlugin.getDefault().logErrorMessage(HibernateConsoleMessages.OpenMappingAction_cannot_find_mapping_file, e);
			} catch (PartInitException e) {
				HibernateConsolePlugin.getDefault().logErrorMessage(HibernateConsoleMessages.OpenMappingAction_cannot_open_mapping_file, e);
			} catch (FileNotFoundException e) {
				HibernateConsolePlugin.getDefault().logErrorMessage(HibernateConsoleMessages.OpenMappingAction_cannot_find_mapping_file, e);
			}
		}
	}

	/**
	 * @param path
	 * @param consoleConfig
	 * @return
	 * @throws PartInitException
	 * @throws JavaModelException
	 * @throws FileNotFoundException
	 */
	public static IEditorPart run(ConsoleConfiguration consoleConfig, TreePath path) 
			throws PartInitException, JavaModelException, FileNotFoundException {
		boolean isPropertySel = (path.getLastSegment().getClass() == Property.class);
		if (isPropertySel) {
			Property propertySel = (Property)path.getLastSegment();
			PersistentClass persClass = propertySel.getPersistentClass();
			if (persClass == null
					|| (RootClass.class.isAssignableFrom(persClass.getClass())
					&& persClass.getClass() != RootClass.class)) {
				Property parentProp = (Property)path.getParentPath().getLastSegment();
				return run(consoleConfig, propertySel, parentProp);
			}
		}
		return run(consoleConfig, path.getLastSegment());
	}

	/**
	 * @param consoleConfig
	 * @param selection
	 * @throws JavaModelException
	 * @throws PartInitException
	 * @throws PresistanceClassNotFoundException
	 * @throws FileNotFoundException
	 */
	public static IEditorPart run(ConsoleConfiguration consoleConfig, Object selection) throws PartInitException, JavaModelException, FileNotFoundException {
		IEditorPart editorPart = null;
		IFile file = null;
		if (selection instanceof Property) {
			Property p = (Property)selection;
			if (p.getPersistentClass() != null) {
				//use PersistentClass to open editor
				file = OpenMappingUtils.searchFileToOpen(consoleConfig, p.getPersistentClass());
				//editorPart = openMapping(p.getPersistentClass(), consoleConfig);
			}
		}
		else {
			file = OpenMappingUtils.searchFileToOpen(consoleConfig, selection);
			//editorPart = openMapping(selection, consoleConfig);
		}
		if (file != null) {
			editorPart = OpenMappingUtils.openFileInEditor(file);
			updateEditorSelection(editorPart, selection);
		}
		if (editorPart == null) {
			//try to find hibernate-annotations
			PersistentClass rootClass = null;
			if (selection instanceof PersistentClass) {
				rootClass = (PersistentClass)selection;
		    }
			else if (selection instanceof Property) {
	    		Property p = (Property)selection;
	    		if (p.getPersistentClass() != null) {
	    			rootClass = p.getPersistentClass();
	    		}
		    }
			if (rootClass != null){
				if (OpenMappingUtils.hasConfigXMLMappingClassAnnotation(consoleConfig, rootClass)) {
					String fullyQualifiedName = rootClass.getClassName();
					editorPart = OpenSourceAction.run(consoleConfig, selection, fullyQualifiedName);
				}
			}
			else {
				String out = NLS.bind(HibernateConsoleMessages.OpenMappingAction_mapping_for_not_found, selection);
				throw new FileNotFoundException(out);
			}
		}
		return editorPart;
	}

	/**
	 * @param consoleConfig
	 * @param compositeProperty
	 * @param parentProperty
	 * @throws JavaModelException
	 * @throws PartInitException
	 * @throws FileNotFoundException
	 * @throws BadLocationException
	 */
	public static IEditorPart run(ConsoleConfiguration consoleConfig, Property compositeProperty, Property parentProperty) 
			throws PartInitException, JavaModelException, FileNotFoundException {
		PersistentClass rootClass = parentProperty.getPersistentClass();
		IFile file = OpenMappingUtils.searchFileToOpen(consoleConfig, rootClass);
		IEditorPart editorPart = null;
		if (file != null){
			editorPart = OpenMappingUtils.openFileInEditor(file);
			updateEditorSelection(editorPart, compositeProperty, parentProperty);
		}
   		if (editorPart == null && parentProperty.isComposite()) {
			if (OpenMappingUtils.hasConfigXMLMappingClassAnnotation(consoleConfig, rootClass)) {
				String fullyQualifiedName =((Component)parentProperty.getValue()).getComponentClassName();
				editorPart = OpenSourceAction.run(consoleConfig, compositeProperty, fullyQualifiedName);
			}
	    }
   		if (editorPart == null) {
   			String out = NLS.bind(HibernateConsoleMessages.OpenMappingAction_mapping_file_for_property_not_found, compositeProperty.getNodeName());
   			throw new FileNotFoundException(out);
   		}
   		return editorPart;
	}

	/**
	 * @param editorPart
	 * @param selection
	 */
	public static boolean updateEditorSelection(IEditorPart editorPart, Object selection) {
		ITextEditor[] textEditors = OpenMappingUtils.getTextEditors(editorPart);
		if (textEditors.length == 0) {
			return false;
		}
		textEditors[0].selectAndReveal(0, 0);
		FindReplaceDocumentAdapter findAdapter = null;
		ITextEditor textEditor = null;
		for (int i = 0; i < textEditors.length && findAdapter == null; i++) {
			textEditor = textEditors[i];
			findAdapter = OpenMappingUtils.createFindDocAdapter(textEditor);
		}
		if (findAdapter == null) {
			return false;
		}
		IJavaProject proj = ProjectUtils.findJavaProject(editorPart);
		IRegion selectRegion = OpenMappingUtils.findSelectRegion(proj, findAdapter, selection);
		if (selectRegion != null) {
			textEditor.selectAndReveal(selectRegion.getOffset(), selectRegion.getLength());
			return true;
		}
		return false;
	}

	/**
	 * @param editorPart
	 * @param compositeProperty
	 * @param parentProperty
	 */
	public static boolean updateEditorSelection(IEditorPart editorPart, Property compositeProperty, Property parentProperty) {
		ITextEditor[] textEditors = OpenMappingUtils.getTextEditors(editorPart);
		if (textEditors.length == 0) {
			return false;
		}
		textEditors[0].selectAndReveal(0, 0);
		FindReplaceDocumentAdapter findAdapter = null;
		ITextEditor textEditor = null;
		for (int i = 0; i < textEditors.length && findAdapter == null; i++) {
			textEditor = textEditors[i];
			findAdapter = OpenMappingUtils.createFindDocAdapter(textEditor);
		}
		if (findAdapter == null) {
			return false;
		}
		IJavaProject proj = ProjectUtils.findJavaProject(editorPart);
		IRegion parentRegion = OpenMappingUtils.findSelectRegion(proj, findAdapter, parentProperty);
		if (parentRegion == null) {
			return false;
		}
		int startOffset = parentRegion.getOffset() + parentRegion.getLength();
		IRegion propRegion = null;
		try {
			final String hbmPropertyPattern = OpenMappingUtils.generateHbmPropertyPattern(compositeProperty);
			propRegion = findAdapter.find(startOffset, hbmPropertyPattern, true, true, false, true);
			PersistentClass rootClass = parentProperty.getPersistentClass();
			if (propRegion == null && parentProperty.isComposite()
					&& rootClass.getIdentifierProperty() == parentProperty) {
				// try to use key-property
				String pattern = hbmPropertyPattern.replaceFirst("<property", "<key-property"); //$NON-NLS-1$ //$NON-NLS-2$
				propRegion = findAdapter.find(startOffset, pattern, true, true, false, true);
				if (propRegion == null) {
					// try to use key-many-to-one
					pattern = hbmPropertyPattern.replaceFirst("<many-to-one", "<key-many-to-one"); //$NON-NLS-1$ //$NON-NLS-2$
					propRegion = findAdapter.find(startOffset, pattern, true, true, false, true);
				}
			}
		} catch (BadLocationException e) {
			HibernateConsolePlugin.getDefault().logErrorMessage(HibernateConsoleMessages.OpenMappingAction_selection_not_found, e);
		}
		if (propRegion == null) {
			return false;
		}
		int length = compositeProperty.getNodeName().length();
		int offset = propRegion.getOffset() + propRegion.getLength() - length - 1;
		propRegion = new Region(offset, length);
		textEditor.selectAndReveal(propRegion.getOffset(), propRegion.getLength());
		return true;
	}
}
