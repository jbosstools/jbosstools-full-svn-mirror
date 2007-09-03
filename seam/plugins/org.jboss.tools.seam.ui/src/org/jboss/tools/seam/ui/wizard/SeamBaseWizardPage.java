/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.seam.ui.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.seam.core.ISeamProject;
import org.jboss.tools.seam.core.SeamCorePlugin;
import org.jboss.tools.seam.internal.core.project.facet.ISeamFacetDataModelProperties;
import org.jboss.tools.seam.ui.internal.project.facet.IValidator;
import org.jboss.tools.seam.ui.internal.project.facet.ValidatorFactory;
import org.jboss.tools.seam.ui.widget.editor.CompositeEditor;
import org.jboss.tools.seam.ui.widget.editor.IFieldEditor;
import org.jboss.tools.seam.ui.widget.editor.LabelFieldEditor;

/**
 * @author eskimo
 *
 */
public abstract class SeamBaseWizardPage extends WizardPage implements IAdaptable, PropertyChangeListener {

	/**
	 * 
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public SeamBaseWizardPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		createEditors();
	}

	/**
	 * @param pageName
	 */
	protected SeamBaseWizardPage(String pageName) {
		super(pageName);
		createEditors();
	}

	protected abstract void createEditors();
	
	Map<String,IFieldEditor> editorRegistry = new HashMap<String,IFieldEditor>();
	
	List<IFieldEditor> editorOrder = new ArrayList<IFieldEditor>();
	
	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		setControl(new GridLayoutComposite(parent));

		if (!"".equals(editorRegistry.get(IParameter.SEAM_PROJECT_NAME).getValue())){
			Map errors = ValidatorFactory.SEAM_PROJECT_NAME_VALIDATOR.validate(
					getEditor(IParameter.SEAM_PROJECT_NAME).getValue(), null);
			
			if(errors.size()>0) {
				setErrorMessage(errors.get(IValidator.DEFAULT_ERROR).toString());
				getEditor(IParameter.SEAM_BEAN_NAME).setEnabled(false);
			} else if(isWar()) {
				getEditor(IParameter.SEAM_BEAN_NAME).setEnabled(false);	
				LabelFieldEditor label = (LabelFieldEditor)((CompositeEditor)getEditor(IParameter.SEAM_LOCAL_INTERFACE_NAME)).getEditors().get(0);
				label.getLabelControl().setText("POJO class name:");
			} else {
				getEditor(IParameter.SEAM_BEAN_NAME).setEnabled(true);
			}
		} else {
			getEditor(IParameter.SEAM_BEAN_NAME).setEnabled(false);	
		}
		setPageComplete(false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if(adapter == Map.class)
			return editorRegistry;
		return null;
	}
	
	/**
	 * 
	 * @param id
	 * @param editor
	 */
	public void addEditor(IFieldEditor editor) {
		editorRegistry.put(editor.getName(), editor);
		editorOrder.add(editor);
		editor.addPropertyChangeListener(this);
	}
	
	/**
	 * 
	 * @param id
	 * @param editor
	 */
	public void addEditors(IFieldEditor[] editors) {
		for (IFieldEditor fieldEditor : editors) {
			addEditor(fieldEditor);
		}
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public IFieldEditor getEditor(String name) {
		return editorRegistry.get(name);
	}
	
	/**
	 * 
	 * @author eskimo
	 *
	 */
	public class GridLayoutComposite extends Composite {

		public GridLayoutComposite(Composite parent, int style) {
			super(parent, style);
			int columnNumber = 1;
			for (IFieldEditor fieldEditor : editorOrder) {
				if(fieldEditor.getNumberOfControls()>columnNumber)
					columnNumber=fieldEditor.getNumberOfControls();
			}
			GridLayout gl = new GridLayout(columnNumber,false);
			gl.verticalSpacing = 5;
			gl.marginTop = 3;
			gl.marginLeft = 3;
			gl.marginRight = 3;
			setLayout(gl);
			for (IFieldEditor fieldEditor2 : editorOrder) {
				fieldEditor2.doFillIntoGrid(this);
			}
		}
		
		public GridLayoutComposite(Composite parent) {
			this(parent, SWT.NONE);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		// TODO - finish validation

		Map errors = ValidatorFactory.SEAM_PROJECT_NAME_VALIDATOR.validate(
				editorRegistry.get(IParameter.SEAM_PROJECT_NAME).getValue(), null);
		
		if(errors.size()>0) {
			setErrorMessage(errors.get(IValidator.DEFAULT_ERROR).toString());
			setPageComplete(false);
			getEditor(IParameter.SEAM_BEAN_NAME).setEnabled(false);
			return;
		}
		
		IResource project = getSelectedProject();

		String type = SeamCorePlugin.getSeamFacetPreferences(project.getProject()).get(ISeamFacetDataModelProperties.JBOSS_AS_DEPLOY_AS,"war");
	
		getEditor(IParameter.SEAM_BEAN_NAME).setEnabled(!isWar());
	
		LabelFieldEditor label = (LabelFieldEditor)((CompositeEditor)getEditor(IParameter.SEAM_LOCAL_INTERFACE_NAME)).getEditors().get(0);
		label.getLabelControl().setText(isWar()?"POJO class name:": "Local interface:");
		
		errors = ValidatorFactory.SEAM_COMPONENT_NAME_VALIDATOR.validate(
				editorRegistry.get(IParameter.SEAM_COMPONENT_NAME).getValue(), null);
		
		if(errors.size()>0) {
			setErrorMessage(NLS.bind(errors.get(IValidator.DEFAULT_ERROR).toString(),"Seam component"));
			setPageComplete(false);
			return;
		}
		
		errors = ValidatorFactory.SEAM_COMPONENT_NAME_VALIDATOR.validate(
				editorRegistry.get(IParameter.SEAM_LOCAL_INTERFACE_NAME).getValue(), null);
		
		if(errors.size()>0) {
			setErrorMessage(NLS.bind(errors.get(IValidator.DEFAULT_ERROR).toString(),"Local interface"));
			setPageComplete(false);
			return;
		}

		if(!isWar()) {
			errors = ValidatorFactory.SEAM_COMPONENT_NAME_VALIDATOR.validate(
					editorRegistry.get(IParameter.SEAM_BEAN_NAME).getValue(), null);
			
			if(errors.size()>0) {
				setErrorMessage(NLS.bind(errors.get(IValidator.DEFAULT_ERROR).toString(),"Bean"));
				setPageComplete(false);
				return;
			}
		} else {
			
		}
		
		errors = ValidatorFactory.SEAM_METHOD_NAME_VALIDATOR.validate(
				editorRegistry.get(IParameter.SEAM_METHOD_NAME).getValue(), new Object[]{"Method",project});
		
		if(errors.size()>0) {
			setErrorMessage(errors.get(IValidator.DEFAULT_ERROR).toString());
			setPageComplete(false);
			return;
		}
		
		errors = ValidatorFactory.FILE_NAME_VALIDATOR.validate(
				editorRegistry.get(IParameter.SEAM_PAGE_NAME).getValue(), (Object)new Object[]{"Page",project});
		
		if(errors.size()>0) {
			setErrorMessage(errors.get(IValidator.DEFAULT_ERROR).toString());
			setPageComplete(false);
			return;
		}
		
		errors = ValidatorFactory.SEAM_JAVA_INTEFACE_NAME_CONVENTION_VALIDATOR.validate(
				editorRegistry.get(IParameter.SEAM_LOCAL_INTERFACE_NAME).getValue(), new Object[]{"Local interface",project});
		
		if(errors.size()>0) {
			setErrorMessage(null);
			setMessage(errors.get(IValidator.DEFAULT_ERROR).toString(),IMessageProvider.WARNING);
			setPageComplete(true);
			return;
		}

		setErrorMessage(null);
		setMessage(null);
		setPageComplete(true);
	}

	/**
	 * @return
	 */
	public IResource getSelectedProject() {
		IResource project = ResourcesPlugin.getWorkspace().getRoot().findMember(
				editorRegistry.get(IParameter.SEAM_PROJECT_NAME).getValueAsString());
		return project;
	}
	
	public boolean isWar() {
		if(getSelectedProject()==null ||
		SeamCorePlugin.getSeamFacetPreferences(getSelectedProject().getProject())==null) return true;
		return "war".equals(SeamCorePlugin.getSeamFacetPreferences(getSelectedProject().getProject()).get(ISeamFacetDataModelProperties.JBOSS_AS_DEPLOY_AS,"war"));
	}
}
