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
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.hibernate.eclipse.console.utils.ProjectUtils;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

/**
 * @author Dmitry Geraskov
 */

public class OpenSourceAction extends SelectionListenerAction {

	public OpenSourceAction() { 
		super("Open Source File");
		setToolTipText("Open Source File");
		setEnabled( true );
	}
	
	public void run() {
		IStructuredSelection sel = getStructuredSelection();
		if (sel instanceof TreeSelection){
			TreePath path = ((TreeSelection)sel).getPaths()[0];
			Object lastSegment = path.getLastSegment();
	    	PersistentClass persClass = getPersistentClass(lastSegment);
			ConsoleConfiguration consoleConfiguration = (ConsoleConfiguration)(path.getSegment(0));
			IJavaProject proj = ProjectUtils.findJavaProject(consoleConfiguration);
			
			String fullyQualifiedName = null;
			if (lastSegment instanceof Property){
				Object prevSegment = path.getParentPath().getLastSegment();
				if (prevSegment instanceof Property
						&& ((Property)prevSegment).isComposite()){
					fullyQualifiedName =((Component)((Property) prevSegment).getValue()).getComponentClassName();
				}
			}
			if (fullyQualifiedName == null && persClass != null){
				fullyQualifiedName = persClass.getClassName();
			}

			try {
				run(lastSegment, proj, fullyQualifiedName);
			} catch (JavaModelException e) {
				HibernateConsolePlugin.getDefault().logErrorMessage("Can't find source file.", e);
			} catch (PartInitException e) {
				HibernateConsolePlugin.getDefault().logErrorMessage("Can't open source file.", e);
			} catch (FileNotFoundException e) {
				HibernateConsolePlugin.getDefault().logErrorMessage("Can't find source file.", e);
			}
		}
	}

	/**
	 * @param selection
	 * @param proj
	 * @param fullyQualifiedName
	 * @throws JavaModelException 
	 * @throws PartInitException 
	 * @throws FileNotFoundException 
	 */
	public IEditorPart run(Object selection, IJavaProject proj,
			String fullyQualifiedName) throws JavaModelException, PartInitException, FileNotFoundException {
		if (fullyQualifiedName == null) return null;
		String remainder = null;
		IType type = null;
		if (fullyQualifiedName.indexOf("$") > 0) {
			remainder = fullyQualifiedName.substring(fullyQualifiedName.indexOf("$") + 1);
			fullyQualifiedName = fullyQualifiedName.substring(0, fullyQualifiedName.indexOf("$"));
			type = proj.findType(fullyQualifiedName);
			while ( remainder.indexOf("$") > 0 ){
				String subtype = remainder.substring(0, fullyQualifiedName.indexOf("$"));
				type = type.getType(subtype);
				remainder = remainder.substring(fullyQualifiedName.indexOf("$") + 1);
			}
			type = type.getType(remainder);
		} else {
			type = proj.findType(fullyQualifiedName);
		}
		IResource resource = null;			
		if (type != null) resource = type.getResource();	
		
		IEditorPart editorPart = null;
		if (resource instanceof IFile){
			editorPart = OpenFileActionUtils.openEditor(HibernateConsolePlugin.getDefault().getActiveWorkbenchWindow().getActivePage(), (IFile) resource);
			if (editorPart instanceof JavaEditor) {
				IJavaElement jElement = null;
				if (selection instanceof Property){
					jElement = type.getField(((Property)selection).getName());
				} else {
					jElement = type;
				}        		
				JavaEditor jEditor = (JavaEditor) editorPart;
				selectionToEditor(jElement, jEditor);				
			}        	
		}               
		
		if (editorPart == null) {
			throw new FileNotFoundException("Source file for class '" + fullyQualifiedName + "' not found.");
		}
		return editorPart;
		
	}
	
	private PersistentClass getPersistentClass(Object selection){
    	if (selection instanceof Property){
    		return ((Property)selection).getPersistentClass();
		} else if (selection instanceof PersistentClass){
			return (PersistentClass)selection;
		} else {
			return null;
		}
	}

	private void selectionToEditor(IJavaElement jElement, JavaEditor jEditor) {
		if (jEditor != null) {
			jEditor.setSelection(jElement);
		}
	}


}
