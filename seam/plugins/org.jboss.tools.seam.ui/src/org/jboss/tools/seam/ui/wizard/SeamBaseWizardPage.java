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
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.seam.core.SeamCorePlugin;
import org.jboss.tools.seam.internal.core.project.facet.ISeamFacetDataModelProperties;
import org.jboss.tools.seam.ui.SeamUIMessages;
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

	protected final IStructuredSelection initialSelection;

	/**
	 * 
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public SeamBaseWizardPage(String pageName, String title,
			ImageDescriptor titleImage, IStructuredSelection initialSelection) {
		super(pageName, title, titleImage);
		this.initialSelection = initialSelection;
		createEditors();
	}

	/**
	 * @param pageName
	 */
	protected SeamBaseWizardPage(String pageName, IStructuredSelection initSelection) {
		super(pageName);
		this.initialSelection = initSelection;
		createEditors();
	}

	protected void createEditors() {
		addEditors(SeamWizardFactory.createBaseFormFieldEditors(SeamWizardUtils.getRootSeamProjectName(initialSelection)));
		String selectedProject = SeamWizardUtils.getRootSeamProjectName(initialSelection);
		String packageName = getDefaultPackageName(selectedProject);
		addEditor(SeamWizardFactory.createSeamJavaPackageSelectionFieldEditor(packageName));
		setSeamProjectNameData(selectedProject);
	}

	Map<String,IFieldEditor> editorRegistry = new HashMap<String,IFieldEditor>();

	List<IFieldEditor> editorOrder = new ArrayList<IFieldEditor>();

	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		setControl(new GridLayoutComposite(parent));

		if (!"".equals(editorRegistry.get(IParameter.SEAM_PROJECT_NAME).getValue())){ //$NON-NLS-1$
			Map errors = ValidatorFactory.SEAM_PROJECT_NAME_VALIDATOR.validate(
					getEditor(IParameter.SEAM_PROJECT_NAME).getValue(), null);

			if(errors.size()>0) {
				setErrorMessage(errors.get(IValidator.DEFAULT_ERROR).toString());
				getEditor(IParameter.SEAM_BEAN_NAME).setEnabled(false);
			} else if(isWar()) {
				getEditor(IParameter.SEAM_BEAN_NAME).setEnabled(false);	
				LabelFieldEditor label = (LabelFieldEditor)((CompositeEditor)getEditor(IParameter.SEAM_LOCAL_INTERFACE_NAME)).getEditors().get(0);
				label.getLabelControl().setText(SeamUIMessages.SEAM_BASE_WIZARD_PAGE_POJO_CLASS_NAME);
			} else {
				getEditor(IParameter.SEAM_BEAN_NAME).setEnabled(true);
			}
		} else {
			getEditor(IParameter.SEAM_BEAN_NAME).setEnabled(false);
			if(getEditor(IParameter.SEAM_PACKAGE_NAME)!=null) {
				getEditor(IParameter.SEAM_PACKAGE_NAME).setEnabled(false);
			}
		}
		String selectdProject = getEditor(IParameter.SEAM_PROJECT_NAME).getValueAsString();

		if(selectdProject!=null && !"".equals(selectdProject) && isValidProjectSelected()) {
			isValidRuntimeConfigured(getSelectedProject());
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

	public void setDefaultValue(String name, Object value) {
		IFieldEditor editor = getEditor(name);
		editor.removePropertyChangeListener(this);
		editor.setValue(value);
		editor.addPropertyChangeListener(this);
	}

	/**
	 * @author eskimo
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
		doFillDefaults(event);
		doValidate(event);
	}

	/**
	 * 
	 */
	protected void doValidate(PropertyChangeEvent event) {
		if(!isValidProjectSelected()) return;

		IProject project = getSelectedProject();
		boolean isWar = isWar();
		getEditor(IParameter.SEAM_BEAN_NAME).setEnabled(!isWar);
		IFieldEditor packageEditor = getEditor(IParameter.SEAM_PACKAGE_NAME);
		if(packageEditor!=null) {
			packageEditor.setEnabled(true);
		}

		if(!isValidRuntimeConfigured(project)) return;

		LabelFieldEditor label = (LabelFieldEditor)((CompositeEditor)getEditor(IParameter.SEAM_LOCAL_INTERFACE_NAME)).getEditors().get(0);
		label.getLabelControl().setText(isWar?SeamUIMessages.SEAM_BASE_WIZARD_PAGE_POJO_CLASS_NAME: SeamUIMessages.SEAM_BASE_WIZARD_PAGE_LOCAL_CLASS_NAME);

		Map errors = ValidatorFactory.SEAM_COMPONENT_NAME_VALIDATOR.validate(
				editorRegistry.get(IParameter.SEAM_COMPONENT_NAME).getValue(), null);

		if(errors.size()>0) {
			setErrorMessage(NLS.bind(errors.get(IValidator.DEFAULT_ERROR).toString(),SeamUIMessages.SEAM_BASE_WIZARD_PAGE_SEAM_COMPONENTS));
			setPageComplete(false);
			return;
		}

		errors = ValidatorFactory.SEAM_COMPONENT_NAME_VALIDATOR.validate(
				editorRegistry.get(IParameter.SEAM_LOCAL_INTERFACE_NAME).getValue(), null);

		if(errors.size()>0) {
			setErrorMessage(NLS.bind(errors.get(IValidator.DEFAULT_ERROR).toString(),SeamUIMessages.SEAM_BASE_WIZARD_PAGE_LOCAL_INTERFACE));
			setPageComplete(false);
			return;
		}

		if(!isWar) {
			errors = ValidatorFactory.SEAM_COMPONENT_NAME_VALIDATOR.validate(
					editorRegistry.get(IParameter.SEAM_BEAN_NAME).getValue(), null);

			if(errors.size()>0) {
				setErrorMessage(NLS.bind(errors.get(IValidator.DEFAULT_ERROR).toString(),"Bean")); //$NON-NLS-1$
				setPageComplete(false);
				return;
			}
		}

		IFieldEditor editor = editorRegistry.get(IParameter.SEAM_PACKAGE_NAME);
		if(editor!=null) {
			errors = ValidatorFactory.PACKAGE_NAME_VALIDATOR.validate(editor.getValue(), null);
			if(errors.size()>0) {
				setErrorMessage(errors.get(IValidator.DEFAULT_ERROR).toString()); //$NON-NLS-1$
				setPageComplete(false);
				return;
			}
		}

		errors = ValidatorFactory.SEAM_METHOD_NAME_VALIDATOR.validate(
				editorRegistry.get(IParameter.SEAM_METHOD_NAME).getValue(), new Object[]{"Method",project}); //$NON-NLS-1$

		if(errors.size()>0) {
			setErrorMessage(errors.get(IValidator.DEFAULT_ERROR).toString());
			setPageComplete(false);
			return;
		}

		errors = ValidatorFactory.FILE_NAME_VALIDATOR.validate(
				editorRegistry.get(IParameter.SEAM_PAGE_NAME).getValue(), new Object[]{"Page",project}); //$NON-NLS-1$

		if(errors.size()>0) {
			setErrorMessage(errors.get(IValidator.DEFAULT_ERROR).toString());
			setPageComplete(false);
			return;
		}

		errors = ValidatorFactory.SEAM_JAVA_INTEFACE_NAME_CONVENTION_VALIDATOR.validate(
				editorRegistry.get(IParameter.SEAM_LOCAL_INTERFACE_NAME).getValue(), new Object[]{SeamUIMessages.SEAM_BASE_WIZARD_PAGE_LOCAL_INTERFACE,project});

		if(errors.size()>0) {
			setErrorMessage(null);
			setMessage(errors.get(IValidator.DEFAULT_ERROR).toString(),IMessageProvider.WARNING);
			setPageComplete(true);
			return;
		}

		setErrorMessage(null);
		setMessage(getDefaultMessageText());
		setPageComplete(true);
	}

	/**
	 * @param project
	 */
	protected boolean isValidRuntimeConfigured(IProject project) {
		Map errors;
		String seamRt = SeamCorePlugin.getSeamPreferences(project).get(ISeamFacetDataModelProperties.SEAM_RUNTIME_NAME,""); //$NON-NLS-1$
		errors = ValidatorFactory.SEAM_RUNTIME_VALIDATOR.validate(seamRt, null);
		if(errors.size()>0) {
			setErrorMessage(errors.get(IValidator.DEFAULT_ERROR).toString());
			setPageComplete(false);
			return false;
		}
		return true;
	}

	protected boolean isValidProjectSelected() {
		Map errors = ValidatorFactory.SEAM_PROJECT_NAME_VALIDATOR.validate(
				editorRegistry.get(IParameter.SEAM_PROJECT_NAME).getValue(), null);

		if(errors.size()>0) {
			setErrorMessage(errors.get(IValidator.DEFAULT_ERROR).toString());
			setPageComplete(false);
			IFieldEditor beanEditor = getEditor(IParameter.SEAM_BEAN_NAME);
			if(beanEditor!=null) {
				beanEditor.setEnabled(false);
			}
			IFieldEditor packageEditor = getEditor(IParameter.SEAM_PACKAGE_NAME);
			if(packageEditor!=null) {
				packageEditor.setEnabled(false);
			}
			return false;
		} else {
			
		}
		return true;
	}

	/**
	 * 
	 */
	protected void doFillDefaults(PropertyChangeEvent event) {
		if(event.getPropertyName().equals(IParameter.SEAM_COMPONENT_NAME) || event.getPropertyName().equals(IParameter.SEAM_PROJECT_NAME)) {
			String value = getEditor(IParameter.SEAM_COMPONENT_NAME).getValueAsString();
			if(value==null||"".equals(value)) { //$NON-NLS-1$
				setDefaultValue(IParameter.SEAM_COMPONENT_NAME, ""); //$NON-NLS-1$
				setDefaultValue(IParameter.SEAM_LOCAL_INTERFACE_NAME, ""); //$NON-NLS-1$
				setDefaultValue(IParameter.SEAM_BEAN_NAME, ""); //$NON-NLS-1$
				setDefaultValue(IParameter.SEAM_METHOD_NAME, ""); //$NON-NLS-1$
				setDefaultValue(IParameter.SEAM_PAGE_NAME, ""); //$NON-NLS-1$
			} else {
				String valueU = value.substring(0,1).toUpperCase() + value.substring(1);
				setDefaultValue(IParameter.SEAM_LOCAL_INTERFACE_NAME, valueU);
				setDefaultValue(IParameter.SEAM_BEAN_NAME, valueU+"Bean"); //$NON-NLS-1$
				String valueL = value.substring(0,1).toLowerCase() + value.substring(1);
				setDefaultValue(IParameter.SEAM_METHOD_NAME, valueL);
				setDefaultValue(IParameter.SEAM_PAGE_NAME, valueL);
			}
		}
		if(event.getPropertyName().equals(IParameter.SEAM_PROJECT_NAME)&& getEditor(IParameter.SEAM_PACKAGE_NAME)!=null) {
			String selectedProject = event.getNewValue().toString();
			setSeamProjectNameData(selectedProject);
			setDefaultValue(IParameter.SEAM_PACKAGE_NAME, getDefaultPackageName(selectedProject));
		}
	}

	protected String getDefaultPackageName(String selectedProject) {
		String packageName = "";
		if(selectedProject!=null && selectedProject.length()>0) {
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(selectedProject);
			if(project!=null) {
				IEclipsePreferences seamFacetPrefs = SeamCorePlugin.getSeamPreferences(project);
				packageName = getDefaultPackageName(seamFacetPrefs);
			}
		}

		return packageName;
	}

	protected String getDefaultPackageName(IEclipsePreferences seamFacetPrefs) {
		return seamFacetPrefs.get(ISeamFacetDataModelProperties.SESSION_BEAN_PACKAGE_NAME, "");
	}

	protected void setSeamProjectNameData(String projectName) {
		IFieldEditor editor = getEditor(IParameter.SEAM_PACKAGE_NAME);
		if(editor!=null) {
			editor.setData(IParameter.SEAM_PROJECT_NAME, projectName);
		}
	}

	/**
	 * @return
	 */
	public IProject getSelectedProject() {
		IResource project = ResourcesPlugin.getWorkspace().getRoot().findMember(
				editorRegistry.get(IParameter.SEAM_PROJECT_NAME).getValueAsString());
		return (IProject)project;
	}

	public boolean isWar() {
		if(getSelectedProject()==null ||
		SeamCorePlugin.getSeamPreferences(getSelectedProject().getProject())==null) return true;
		return "war".equals(SeamCorePlugin.getSeamPreferences(getSelectedProject().getProject()).get(ISeamFacetDataModelProperties.JBOSS_AS_DEPLOY_AS,"war")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public abstract String getDefaultMessageText();
}