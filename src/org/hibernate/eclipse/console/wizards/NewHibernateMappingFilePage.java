/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.hibernate.eclipse.console.wizards;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.hibernate.eclipse.console.HibernateConsoleMessages;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.hibernate.eclipse.console.utils.xpl.SelectionHelper;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (hbm.xml).
 */

public class NewHibernateMappingFilePage extends WizardPage {
	private Label containerText;

	private Label fileText;

	private ISelection selection;

	private Text classToMap;

	private WizardNewFileCreationPage fileCreation;

	private boolean beenShown;

	/**
	 * Constructor for SampleNewWizardPage.
	 * @param page
	 *
	 * @param pageName
	 */
	public NewHibernateMappingFilePage(ISelection selection, WizardNewFileCreationPage page) {
		super("wizardPage"); //$NON-NLS-1$
		this.fileCreation = page;
		setTitle(HibernateConsoleMessages.NewHibernateMappingFilePage_hibernate_xml_mapping_file);
		setDescription(HibernateConsoleMessages.NewHibernateMappingFilePage_this_wizard_creates);
		this.selection = selection;
	}

	public void setVisible(boolean visible) {
        containerText.setText(fileCreation.getContainerFullPath().toPortableString() );
        fileText.setText(fileCreation.getFileName() );
        super.setVisible(visible);
        if(visible) {
            classToMap.setFocus();
        }
        beenShown = true;
        dialogChanged();
    }

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;

		Label label = new Label(container, SWT.NULL);
		label.setText(HibernateConsoleMessages.NewHibernateMappingFilePage_container);

		containerText = new Label(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		containerText.setLayoutData(gd);

		label = new Label(container, SWT.NULL);
		label.setText(""); //$NON-NLS-1$

		label = new Label(container, SWT.NULL);
		label.setText(HibernateConsoleMessages.NewHibernateMappingFilePage_file_name);

		fileText = new Label(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fileText.setLayoutData(gd);

		label = new Label(container, SWT.NULL);
		label.setText(""); //$NON-NLS-1$

		label = new Label(container, SWT.NULL);
		label.setText(HibernateConsoleMessages.NewHibernateMappingFilePage_class_to_map);

		classToMap = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		classToMap.setLayoutData(gd);
		classToMap.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		//TODO (internal api!): ControlContentAssistHelper.createTextContentAssistant(classToMap, aCompletionProcessor);

		Button button = new Button(container, SWT.PUSH);
		button.setText(HibernateConsoleMessages.NewHibernateMappingFilePage_browse);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleClassToMapBrowse();
			}
		});

		initialize();
		dialogChanged();
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		IType initialJavaElement = SelectionHelper.getClassFromElement(SelectionHelper.getInitialJavaElement(selection));
		if(initialJavaElement!=null) {
			classToMap.setText(initialJavaElement.getFullyQualifiedName('.'));
		}
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */

	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				HibernateConsoleMessages.NewHibernateMappingFilePage_select_new_file_container);
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				containerText.setText(((Path) result[0]).toString());
			}
		}
	}

	private void handleClassToMapBrowse() {
		IType type = findClassToMap();
		if(type!=null) {
			classToMap.setText(type.getFullyQualifiedName('.'));
		}
	}

	IType findClassToMap() {
		IJavaProject root= getRootJavaProject();
		if (root == null)
			return null;

		IJavaElement[] elements= new IJavaElement[] { root };
		IJavaSearchScope scope= SearchEngine.createJavaSearchScope(elements);

		try {
			SelectionDialog dialog= JavaUI.createTypeDialog(getShell(), getWizard().getContainer(), scope, IJavaElementSearchConstants.CONSIDER_CLASSES, false, getClassToMapText());
			dialog.setTitle(HibernateConsoleMessages.NewHibernateMappingFilePage_select_class_to_map);
			dialog.setMessage(HibernateConsoleMessages.NewHibernateMappingFilePage_the_class_will_be_used_when);
			if (dialog.open() == Window.OK) {
				Object[] resultArray= dialog.getResult();
				if (resultArray != null && resultArray.length > 0)
					return (IType) resultArray[0];
			}
		} catch (JavaModelException e) {
			HibernateConsolePlugin.getDefault().log(e);
		}
		return null;
	}

	private IJavaProject getRootJavaProject() {
		IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(containerText.getText());
		if(resource!=null) {
			if(resource.getProject()!=null) {
				IJavaProject project = JavaCore.create(resource.getProject());
				return project;
			}
		}
		return null;
	}

	String getClassToMapText() {
		return classToMap.getText();
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {
		IResource container = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(getContainerName()));
		String fileName = getFileName();

		if (getContainerName().length() == 0) {
			updateStatus(HibernateConsoleMessages.NewHibernateMappingFilePage_file_container_must_be_specified);
			return;
		}
		if (container == null
				|| (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			updateStatus(HibernateConsoleMessages.NewHibernateMappingFilePage_file_container_must_exist);
			return;
		}
		if (!container.isAccessible()) {
			updateStatus(HibernateConsoleMessages.NewHibernateMappingFilePage_project_must_be_writable);
			return;
		}
		if (fileName.length() == 0) {
			updateStatus(HibernateConsoleMessages.NewHibernateMappingFilePage_file_name_must_be_specified);
			return;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus(HibernateConsoleMessages.NewHibernateMappingFilePage_file_name_must_be_valid);
			return;
		}

		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getContainerName() {
		return containerText.getText();
	}

	public String getFileName() {
		return fileText.getText();
	}
}