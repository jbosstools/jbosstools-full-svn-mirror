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
package org.jboss.tools.smooks.javabean.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.ui.search.JavaSearchScopeFactory;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.javabean.model.JavaBeanModelFactory;
import org.jboss.tools.smooks.ui.SmooksUIActivator;
import org.jboss.tools.smooks.utils.ProjectClassLoader;

/**
 * @author Dart Peng
 * 
 * @CreateTime Jul 22, 2008
 */
public class JavaBeanModelLoadComposite extends Composite implements
		SelectionListener {

	protected Text classText;
	private Button classBrowseButton;
	protected String classFullName;
	protected boolean loadAtomic;
	protected IJavaProject javaProject;
	protected IRunnableContext runnableContext;
	protected JavaBeanModel currentRootJavaBeanModel = null;
	protected JavaBeanModel returnJavaBeanModel = null;

	protected ProjectClassLoader loader = null;

	public JavaBeanModelLoadComposite(Composite parent, int style,
			IRunnableContext runnableContext, IJavaProject project,
			boolean loadJavapropertiesAtomic) throws Exception {
		super(parent, style);
		loadAtomic = loadJavapropertiesAtomic;
		this.runnableContext = runnableContext;
		if (this.runnableContext == null) {
			this.runnableContext = SmooksUIActivator.getDefault()
					.getWorkbench().getActiveWorkbenchWindow();
			// this.runnableContext = new
			// ProgressMonitorDialog(parent.getShell());
			if (this.runnableContext == null)
				throw new Exception("Can't init IRunnableContent");
		}

		if (project != null) {
			javaProject = project;
			loader = new ProjectClassLoader(javaProject);
		}

		createCompositeContent();
	}

	public JavaBeanModelLoadComposite(Composite parent, int style,
			IRunnableContext runnableContext, IJavaProject project)
			throws Exception {
		this(parent, style, runnableContext, project, true);
	}

	protected Control createCompositeContent() {
		Composite parent = this;
		parent.setLayout(new FillLayout());
		Composite com = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		com.setLayout(layout);

		Label classLabel = new Label(com, SWT.NULL);
		classLabel.setText("Class Name:");

		Composite classTextContainer = new Composite(com, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		classTextContainer.setLayoutData(gd);

		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		classTextContainer.setLayout(gl);

		{
			classText = new Text(classTextContainer, SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.grabExcessHorizontalSpace = true;
			classText.setLayoutData(gd);
			classText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent arg0) {
					classFullName = classText.getText();
				}
			});

			classBrowseButton = new Button(classTextContainer, SWT.NONE);
			classBrowseButton.addSelectionListener(this);
			classBrowseButton.setText("Browse...");
		}
		return com;
	}

	protected void recordModel() {
		// this.currentRootJavaBeanModel.setProperties(null);
		// this.fillCheckStateModel(this.currentRootJavaBeanModel);
	}

	protected void fillTheModelWithCheckStatus(JavaBeanModel javaBeanModel,
			CheckboxTreeViewer viewer) {
		ArrayList clist = new ArrayList();
		if (javaBeanModel.propertiesHasBeenLoaded()) {
			List children = javaBeanModel.getProperties();
			for (Iterator iterator = children.iterator(); iterator.hasNext();) {
				JavaBeanModel child = (JavaBeanModel) iterator.next();
				boolean checked = viewer.getChecked(child);
				if (checked) {
					clist.add(child);
					fillTheModelWithCheckStatus(child, viewer);
				}
			}
		}
		javaBeanModel.setProperties(clist);
	}

	/**
	 * 
	 * @return
	 */
	public JavaBeanModel getCheckedJavaBeanModel() {
		return this.fillCheckStateModel(currentRootJavaBeanModel);
	}

	/**
	 * 
	 * @return
	 */
	public JavaBeanModel fillCheckStateModel(JavaBeanModel rootJavaBean) {
		if (rootJavaBean == null) {
			// List list = (List) treeViewer.getInput();
			// if (list != null)
			// rootJavaBean = (JavaBeanModel) list.get(0);
			try {
				ProjectClassLoader loader = new ProjectClassLoader(javaProject);
				Class clazz = loader.loadClass(this.classText.getText());
				rootJavaBean = JavaBeanModelFactory
						.getJavaBeanModelWithLazyLoad(clazz);
			} catch (Exception e) {

			}
		}
		// if (rootJavaBean != null)
		// this.fillTheModelWithCheckStatus(rootJavaBean, treeViewer);
		return rootJavaBean;
	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
		this.widgetSelected(arg0);

	}

	public void widgetSelected(SelectionEvent arg0) {
		IJavaSearchScope scope = JavaSearchScopeFactory.getInstance()
				.createJavaProjectSearchScope(javaProject, true);
		SelectionDialog dialog;
		try {
			dialog = JavaUI.createTypeDialog(this.getShell(), runnableContext,
					scope, IJavaElementSearchConstants.CONSIDER_CLASSES, false);
			dialog.setMessage("Source Java Bean:");
			dialog.setTitle("Search java bean");

			if (dialog.open() == Window.OK) {
				Object[] results = dialog.getResult();
				if (results.length > 0) {
					Object result = results[0];
					String packageFullName = JavaModelUtil
							.getTypeContainerName((IType) result);
					if (packageFullName == null
							|| packageFullName.length() <= 0) {
						classText.setText(((IType) result).getElementName());
					} else {
						classText.setText(packageFullName + "."
								+ ((IType) result).getElementName());
					}
				}
			}
		} catch (Exception e) {
			// this.setErrorMessage("Error occurs!please see log file");
			e.printStackTrace();
		}

	}

}
