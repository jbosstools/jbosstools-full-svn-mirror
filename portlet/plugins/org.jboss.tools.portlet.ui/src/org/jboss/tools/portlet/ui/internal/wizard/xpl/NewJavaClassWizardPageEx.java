package org.jboss.tools.portlet.ui.internal.wizard.xpl;

/*******************************************************************************
 * Copyright (c) 2003, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * David Schneider, david.schneider@unisys.com - [142500] WTP properties pages fonts don't follow Eclipse preferences
 *******************************************************************************/

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.internal.ui.dialogs.FilteredTypesSelectionDialog;
import org.eclipse.jdt.internal.ui.viewsupport.IViewPartInputProvider;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.jst.j2ee.internal.common.operations.INewJavaClassDataModelProperties;
import org.eclipse.jst.j2ee.internal.dialogs.TypeSearchEngine;
import org.eclipse.jst.j2ee.internal.plugin.J2EEUIMessages;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.wst.common.componentcore.internal.operation.IArtifactEditOperationDataModelProperties;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.internal.datamodel.ui.DataModelWizardPage;
import org.eclipse.wst.common.frameworks.internal.plugin.WTPCommonPlugin;
import org.eclipse.wst.common.project.facet.core.FacetedProjectFramework;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.ui.PortletUIActivator;

/**
 *
 */
public class NewJavaClassWizardPageEx extends DataModelWizardPage {

	private Text folderText;
	private Button folderButton;
	protected Text packageText;
	protected Button packageButton;
	protected Label packageLabel;
	protected Text classText;
	protected Label classLabel;
	protected Text superText;
	protected Button superButton;
	protected Label superLabel;
	protected Label projectNameLabel;
	private Combo projectNameCombo;	
	protected String projectType;
	private String projectName;

	/**
	 * @param model
	 * @param pageName
	 */
	public NewJavaClassWizardPageEx(IDataModel model, String pageName, String pageDesc, String pageTitle,
			String moduleType) {
		super(model, pageName);
		setDescription(pageDesc);
		this.setTitle(pageTitle);
		setPageComplete(false);
		this.projectType = moduleType;
		this.projectName = null;
	}

	/**
	 * 
	 */
	protected String[] getValidationPropertyNames() {
		return new String[]{IArtifactEditOperationDataModelProperties.PROJECT_NAME, 
				IArtifactEditOperationDataModelProperties.COMPONENT_NAME, 
				INewJavaClassDataModelProperties.SOURCE_FOLDER, 
				INewJavaClassDataModelProperties.JAVA_PACKAGE, 
				INewJavaClassDataModelProperties.CLASS_NAME, 
				INewJavaClassDataModelProperties.SUPERCLASS};
	}

	/**
	 * 
	 */
	protected Composite createTopLevelComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		composite.setLayout(layout);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.widthHint = 300;
		composite.setLayoutData(data);

		addProjectNameGroup(composite);
		addFolderGroup(composite);
		addSeperator(composite, 3);
		addPackageGroup(composite);
		addClassnameGroup(composite);
		addSuperclassGroup(composite);

		classText.setFocus();
		PlatformUI.getWorkbench().getHelpSystem().setHelp(composite, getInfopopID());
	    Dialog.applyDialogFont(parent);
		return composite;
	}

	/**
	 * Add project group
	 */
	private void addProjectNameGroup(Composite parent) {
		// set up project name label
		projectNameLabel = new Label(parent, SWT.NONE);
		projectNameLabel.setText(J2EEUIMessages.getResourceString(J2EEUIMessages.MODULES_DEPENDENCY_PAGE_TABLE_PROJECT));
		GridData data = new GridData();
		projectNameLabel.setLayoutData(data);
		// set up project name entry field
		projectNameCombo = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 300;
		data.horizontalSpan = 1;
		projectNameCombo.setLayoutData(data);
		projectNameCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				// update source folder
				if (folderText != null) {					
					String sourceFolder = getDefaultJavaSourceFolder(ProjectUtilities.getProject(projectNameCombo.getText())).getFullPath().toOSString();					
					if (sourceFolder != null)
						folderText.setText(sourceFolder);
				}
			}
		});
		synchHelper.synchCombo(projectNameCombo, IArtifactEditOperationDataModelProperties.PROJECT_NAME, null);
		initializeProjectList();
		new Label(parent, SWT.NONE);
	}
	
	/**
	 * 
	 **/
	private IFolder getDefaultJavaSourceFolder(IProject project) {
		
		if (project == null)
			return null;
		IPackageFragmentRoot[] sources = J2EEProjectUtilities.getSourceContainers(project);
		// Try and return the first source folder
		if (sources.length > 0) {
			try {
				return (IFolder) sources[0].getCorrespondingResource();
			} catch (Exception e) {
				return null;
			}
		}
		IJavaProject javaProject = JavaCore.create(project);
		if (javaProject == null) {
			return null;
		}
		try {
			IClasspathEntry[] entries = javaProject.getResolvedClasspath(false);
			for (int i = 0; i < entries.length; i++) {
				IClasspathEntry entry = entries[i];
				if (entry.getContentKind() == IPackageFragmentRoot.K_SOURCE) {
					IPath path = entry.getPath();
					IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
					if (resource instanceof IFolder) {
						return (IFolder) resource;
					}
				}
			}
		} catch (JavaModelException e) {
			// ignore
		}
		return null;
	}
	
	/**
	 * This method is used by the project list initializer. The method checks 
	 * if the specified project is valid to include it in the project list.
	 * 
	 * <p>Subclasses of this wizard page should override this method to 
	 * adjust filtering of the projects to their needs. </p>
	 * 
	 * @param project reference to the project to be checked
	 * 
	 * @return <code>true</code> if the project is valid to be included in 
	 * 		   the project list, <code>false</code> - otherwise. 
	 */
	protected boolean isProjectValid(IProject project) {
		boolean result;
		try {
			return FacetedProjectFramework.hasProjectFacet(project, IPortletConstants.JSFPORTLET_FACET_ID);
		} catch (CoreException ce) {
			result = false;
		}
		return result;
	}
	 
	/**
	 * 
	 */
	private void initializeProjectList() {
		IProject[] workspaceProjects = ProjectUtilities.getAllProjects();
		List items = new ArrayList();
		for (int i = 0; i < workspaceProjects.length; i++) {
			IProject project = workspaceProjects[i];
			if (isProjectValid(project))
				items.add(project.getName());
		}
		if (items.isEmpty()) return;
		String[] names = new String[items.size()];
		for (int i = 0; i < items.size(); i++) {
			names[i] = (String) items.get(i);
		}
		projectNameCombo.setItems(names);
		IProject selectedProject = null;
		try {
			if (model !=null) {
				String projectNameFromModel = model.getStringProperty(IArtifactEditOperationDataModelProperties.COMPONENT_NAME);
				if (projectNameFromModel!=null && projectNameFromModel.length()>0)
					selectedProject = ProjectUtilities.getProject(projectNameFromModel);
			}
		} catch (Exception e) {};
		try {
			if (selectedProject == null)
				selectedProject = getSelectedProject();
			if (selectedProject != null && selectedProject.isAccessible()
					&& selectedProject.hasNature(IModuleConstants.MODULE_NATURE_ID)) {
				projectNameCombo.setText(selectedProject.getName());
				model.setProperty(IArtifactEditOperationDataModelProperties.PROJECT_NAME, selectedProject.getName());
			}
		} catch (CoreException ce) {
			// Ignore
		}
		if (projectName == null && names.length > 0)
			projectName = names[0];

		if ((projectNameCombo.getText() == null || projectNameCombo.getText().length() == 0) && projectName != null) {
			projectNameCombo.setText(projectName);
			model.setProperty(IArtifactEditOperationDataModelProperties.PROJECT_NAME, projectName);
		}

	}

	/**
	 * Add folder group to composite
	 */
	private void addFolderGroup(Composite composite) {
		// folder
		Label folderLabel = new Label(composite, SWT.LEFT);
		folderLabel.setText(J2EEUIMessages.FOLDER_LABEL);
		folderLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		folderText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		folderText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		synchHelper.synchText(folderText, INewJavaClassDataModelProperties.SOURCE_FOLDER, null);
		
		IPackageFragmentRoot root = getSelectedPackageFragmentRoot();
		String projectName = model.getStringProperty(IArtifactEditOperationDataModelProperties.PROJECT_NAME);
		if (projectName != null && projectName.length() > 0) {
			IProject targetProject = ProjectUtilities.getProject(projectName);
			if (root == null || !root.getJavaProject().getProject().equals(targetProject)) {
				IFolder folder = getDefaultJavaSourceFolder(targetProject);
				if (folder != null)
					folderText.setText(folder.getFullPath().toOSString());
			} else {
				folderText.setText(root.getPath().toString());
			}
		}
		
		folderButton = new Button(composite, SWT.PUSH);
		folderButton.setText(J2EEUIMessages.BROWSE_BUTTON_LABEL);
		folderButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		folderButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				handleFolderButtonPressed();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// Do nothing
			}
		});
	}

	/**
	 * Add package group to composite
	 */
	private void addPackageGroup(Composite composite) {
		// package
		packageLabel = new Label(composite, SWT.LEFT);
		packageLabel.setText(J2EEUIMessages.JAVA_PACKAGE_LABEL);
		packageLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		packageText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		packageText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		synchHelper.synchText(packageText, INewJavaClassDataModelProperties.JAVA_PACKAGE, null);
		
		IPackageFragment packageFragment = getSelectedPackageFragment();
		String targetProject = model.getStringProperty(IArtifactEditOperationDataModelProperties.PROJECT_NAME); 
		if (packageFragment != null && packageFragment.exists() && 
				packageFragment.getJavaProject().getElementName().equals(targetProject)) {
			IPackageFragmentRoot root = getPackageFragmentRoot(packageFragment);
			if (root != null)
				folderText.setText(root.getPath().toString());
			model.setProperty(INewJavaClassDataModelProperties.JAVA_PACKAGE, packageFragment.getElementName());
		}

		packageButton = new Button(composite, SWT.PUSH);
		packageButton.setText(J2EEUIMessages.BROWSE_BUTTON_LABEL);
		packageButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		packageButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				handlePackageButtonPressed();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// Do nothing
			}
		});
	}
	
	protected IPackageFragmentRoot getPackageFragmentRoot(IPackageFragment packageFragment) {
		if (packageFragment == null)
			return null;
		else if (packageFragment.getParent() instanceof IPackageFragment)
			return getPackageFragmentRoot((IPackageFragment) packageFragment.getParent());
		else if (packageFragment.getParent() instanceof IPackageFragmentRoot)
			return (IPackageFragmentRoot) packageFragment.getParent();
		else
			return null;
	}

	/**
	 * Add classname group to composite
	 */
	private void addClassnameGroup(Composite composite) {
		// class name
		classLabel = new Label(composite, SWT.LEFT);
		classLabel.setText(J2EEUIMessages.CLASS_NAME_LABEL);
		classLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		classText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		classText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		synchHelper.synchText(classText, INewJavaClassDataModelProperties.CLASS_NAME, null);

		new Label(composite, SWT.LEFT);
	}

	/**
	 * Add seperator to composite
	 */
	protected void addSeperator(Composite composite, int horSpan) {
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.widthHint = 300;
		// Separator label
		Label seperator = new Label(composite, SWT.HORIZONTAL | SWT.SEPARATOR);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		data.horizontalSpan = horSpan;
		seperator.setLayoutData(data);
	}

	/**
	 * Add superclass group to the composite
	 */
	private void addSuperclassGroup(Composite composite) {
		// superclass
		superLabel = new Label(composite, SWT.LEFT);
		superLabel.setText(J2EEUIMessages.SUPERCLASS_LABEL);
		superLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		superText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		superText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		synchHelper.synchText(superText, INewJavaClassDataModelProperties.SUPERCLASS, null);

		superButton = new Button(composite, SWT.PUSH);
		superButton.setText(J2EEUIMessages.BROWSE_BUTTON_LABEL);
		superButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		superButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				handleSuperButtonPressed();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// Do nothing
			}
		});
	}

	/**
	 * Browse for a new Destination Folder
	 */
	protected void handleFolderButtonPressed() {
		ISelectionStatusValidator validator = getContainerDialogSelectionValidator();
		ViewerFilter filter = getContainerDialogViewerFilter();
		ITreeContentProvider contentProvider = new WorkbenchContentProvider();
		ILabelProvider labelProvider = new DecoratingLabelProvider(new WorkbenchLabelProvider(), PlatformUI.getWorkbench()
				.getDecoratorManager().getLabelDecorator());
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(getShell(), labelProvider, contentProvider);
		dialog.setValidator(validator);
		dialog.setTitle(J2EEUIMessages.CONTAINER_SELECTION_DIALOG_TITLE);
		dialog.setMessage(J2EEUIMessages.CONTAINER_SELECTION_DIALOG_DESC);
		dialog.addFilter(filter);
		String projectName = model.getStringProperty(IArtifactEditOperationDataModelProperties.PROJECT_NAME);
		if (projectName==null || projectName.length()==0)
			return;
		IProject project = ProjectUtilities.getProject(projectName);
		dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());

		if (project != null)
			dialog.setInitialSelection(project);
		if (dialog.open() == Window.OK) {
			Object element = dialog.getFirstResult();
			try {
				if (element instanceof IContainer) {
					IContainer container = (IContainer) element;
					folderText.setText(container.getFullPath().toString());
					// dealWithSelectedContainerResource(container);
				}
			} catch (Exception ex) {
				// Do nothing
			}

		}
	}

	protected void handlePackageButtonPressed() {
		IPackageFragmentRoot packRoot = (IPackageFragmentRoot) model.getProperty(INewJavaClassDataModelProperties.JAVA_PACKAGE_FRAGMENT_ROOT);
		if (packRoot == null)
			return;
		IJavaElement[] packages = null;
		try {
			packages = packRoot.getChildren();
		} catch (JavaModelException e) {
			// Do nothing
		}
		if (packages == null)
			packages = new IJavaElement[0];

		ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), new JavaElementLabelProvider(
				JavaElementLabelProvider.SHOW_DEFAULT));
		dialog.setTitle(J2EEUIMessages.PACKAGE_SELECTION_DIALOG_TITLE);
		dialog.setMessage(J2EEUIMessages.PACKAGE_SELECTION_DIALOG_DESC);
		dialog.setEmptyListMessage(J2EEUIMessages.PACKAGE_SELECTION_DIALOG_MSG_NONE);
		dialog.setElements(packages);
		if (dialog.open() == Window.OK) {
			IPackageFragment fragment = (IPackageFragment) dialog.getFirstResult();
			if (fragment != null) {
				packageText.setText(fragment.getElementName());
			} else {
				packageText.setText(J2EEUIMessages.EMPTY_STRING);
			}
		}
	}

	protected void handleSuperButtonPressed() {
		getControl().setCursor(new Cursor(getShell().getDisplay(), SWT.CURSOR_WAIT));
		IPackageFragmentRoot packRoot = (IPackageFragmentRoot) model.getProperty(INewJavaClassDataModelProperties.JAVA_PACKAGE_FRAGMENT_ROOT);
		if (packRoot == null)
			return;

		// this eliminates the non-exported classpath entries
		final IJavaSearchScope scope = TypeSearchEngine.createJavaSearchScopeForAProject(packRoot.getJavaProject(), true, true);

		// This includes all entries on the classpath. This behavior is
		// identical
		// to the Super Class Browse Button on the Create new Java Class Wizard
		// final IJavaSearchScope scope = SearchEngine.createJavaSearchScope(new
		// IJavaElement[] {root.getJavaProject()} );
		FilteredTypesSelectionDialog dialog = new FilteredTypesSelectionDialog(getShell(),false, getWizard().getContainer(), scope, IJavaSearchConstants.CLASS);
		dialog.setTitle(J2EEUIMessages.SUPERCLASS_SELECTION_DIALOG_TITLE);
		dialog.setMessage(J2EEUIMessages.SUPERCLASS_SELECTION_DIALOG_DESC);

		if (dialog.open() == Window.OK) {
			IType type = (IType) dialog.getFirstResult();
			String superclassFullPath = J2EEUIMessages.EMPTY_STRING;
			if (type != null) {
				superclassFullPath = type.getFullyQualifiedName();
			}
			superText.setText(superclassFullPath);
			getControl().setCursor(null);
			return;
		}
		getControl().setCursor(null);
	}

	/**
	 * Returns a new instance of the Selection validator for the Container
	 * Selection Dialog This method can be extended by subclasses, as it does
	 * some basic validation.
	 */
	protected ISelectionStatusValidator getContainerDialogSelectionValidator() {
		return new ISelectionStatusValidator() {
			public IStatus validate(Object[] selection) {
				if (selection != null && selection[0] != null && !(selection[0] instanceof IProject))
					return WTPCommonPlugin.OK_STATUS;
				return WTPCommonPlugin.createErrorStatus(J2EEUIMessages.CONTAINER_SELECTION_DIALOG_VALIDATOR_MESG);
			}
		};
	}

	/**
	 * Returns a new instance of the Selection Listner for the Container
	 * Selection Dialog
	 */
	protected ViewerFilter getContainerDialogViewerFilter() {
		return new ViewerFilter() {
			public boolean select(Viewer viewer, Object parent, Object element) {
				if (element instanceof IProject) {
					IProject project = (IProject) element;
					return project.getName().equals(model.getProperty(IArtifactEditOperationDataModelProperties.PROJECT_NAME));
				} else if (element instanceof IFolder) {
					IFolder folder = (IFolder) element;
					// only show source folders
					IProject project = ProjectUtilities.getProject(model.getStringProperty(IArtifactEditOperationDataModelProperties.PROJECT_NAME));
					IPackageFragmentRoot[] sourceFolders = J2EEProjectUtilities.getSourceContainers(project);
					for (int i = 0; i < sourceFolders.length; i++) {
						if (sourceFolders[i].getResource()!= null && sourceFolders[i].getResource().equals(folder))
							return true;
					}
				}
				return false;
			}
		};
	}

	

	/**
	 * @return
	 */
	private IProject getSelectedProject() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null)
			return null;
		ISelection selection = window.getSelectionService().getSelection();
		if (selection == null)
			return null;
		if (!(selection instanceof IStructuredSelection)) 
			return null;
		IStructuredSelection stucturedSelection = (IStructuredSelection) selection;
		if (stucturedSelection.getFirstElement() instanceof EObject)
			return ProjectUtilities.getProject(stucturedSelection.getFirstElement());
		IJavaElement element = getInitialJavaElement(selection);
		if (element != null && element.getJavaProject() != null)
			return element.getJavaProject().getProject();
		return getExtendedSelectedProject(stucturedSelection.getFirstElement());
	}
	
	protected IProject getExtendedSelectedProject(Object selection) {
		return null;
	}

	/**
	 * @return
	 */
	private IPackageFragment getSelectedPackageFragment() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null)
			return null;
		ISelection selection = window.getSelectionService().getSelection();
		if (selection == null)
			return null;
		IJavaElement element = getInitialJavaElement(selection);
		if (element != null) {
			if (element.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
				return (IPackageFragment) element;
			} else if (element.getElementType() == IJavaElement.COMPILATION_UNIT) { 
				IJavaElement parent = ((ICompilationUnit) element).getParent();
				if (parent.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
					return (IPackageFragment) parent;
				}
			} else if (element.getElementType() == IJavaElement.TYPE) {
				return ((IType) element).getPackageFragment();
			}
		}
		return null;
	}
	
	private IPackageFragmentRoot getSelectedPackageFragmentRoot() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null)
			return null;
		ISelection selection = window.getSelectionService().getSelection();
		if (selection == null)
			return null;
		// StructuredSelection stucturedSelection = (StructuredSelection)
		// selection;
		IJavaElement element = getInitialJavaElement(selection);
		if (element != null) {
			if (element.getElementType() == IJavaElement.PACKAGE_FRAGMENT_ROOT)
				return (IPackageFragmentRoot) element;
		}
		return null;
	}

	/**
	 * Utility method to inspect a selection to find a Java element.
	 * 
	 * @param selection
	 *            the selection to be inspected
	 * @return a Java element to be used as the initial selection, or
	 *         <code>null</code>, if no Java element exists in the given
	 *         selection
	 */
	protected IJavaElement getInitialJavaElement(ISelection selection) {
		IJavaElement jelem = null;
		if (selection != null && !selection.isEmpty() && selection instanceof IStructuredSelection) {
			Object selectedElement = ((IStructuredSelection) selection).getFirstElement();
			if (selectedElement instanceof IAdaptable) {
				IAdaptable adaptable = (IAdaptable) selectedElement;

				jelem = (IJavaElement) adaptable.getAdapter(IJavaElement.class);
				if (jelem == null) {
					IResource resource = (IResource) adaptable.getAdapter(IResource.class);
					if (resource != null && resource.getType() != IResource.ROOT) {
						while (jelem == null && resource.getType() != IResource.PROJECT) {
							resource = resource.getParent();
							jelem = (IJavaElement) resource.getAdapter(IJavaElement.class);
						}
						if (jelem == null) {
							jelem = JavaCore.create(resource); // java project
						}
					}
				}
			}
		}
		if (jelem == null) {
			IWorkbenchWindow window= PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			if (window == null)
				return null;
			IWorkbenchPart part = window.getActivePage().getActivePart();
			if (part instanceof ContentOutline) {
				part = window.getActivePage().getActiveEditor();
			}

			if (part instanceof IViewPartInputProvider) {
				Object elem = ((IViewPartInputProvider) part).getViewPartInput();
				if (elem instanceof IJavaElement) {
					jelem = (IJavaElement) elem;
				}
			}
		}

		if (jelem == null || jelem.getElementType() == IJavaElement.JAVA_MODEL) {
			try {
				IJavaProject[] projects = JavaCore.create(getWorkspaceRoot()).getJavaProjects();
				if (projects.length == 1) {
					jelem = projects[0];
				}
			} catch (JavaModelException e) {
				PortletUIActivator.log(e);
			}
		}
		return jelem;
	}

	protected IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}
