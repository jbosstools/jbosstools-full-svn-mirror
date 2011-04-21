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
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.ui.search.JavaSearchScopeFactory;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
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

	private List<JavaBeanModel> javabeanList = new ArrayList<JavaBeanModel>();
	protected Text classText;
	private Button classBrowseButton;
	protected String classFullName;
	protected boolean loadAtomic;
	protected IJavaProject javaProject;
	protected IRunnableContext runnableContext;
	protected JavaBeanModel currentRootJavaBeanModel = null;
	protected JavaBeanModel returnJavaBeanModel = null;

	private List<IJavaBeanSelectionListener> selectionListenerList = new ArrayList<IJavaBeanSelectionListener>();

	protected ProjectClassLoader loader = null;
	private TableViewer listViewer;

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
				throw new Exception(
						Messages
								.getString("JavaBeanModelLoadComposite.InitRunnableContextException")); //$NON-NLS-1$
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

	public List<JavaBeanModel> getJavabeanList() {
		return javabeanList;
	}

	public void addJavaBeanSelectionListener(IJavaBeanSelectionListener listener) {
		this.selectionListenerList.add(listener);
	}

	public void removeJavaBeanSelectionListener(
			IJavaBeanSelectionListener listener) {
		this.selectionListenerList.remove(listener);
	}

	public void setJavabeanList(List<JavaBeanModel> javabeanList) {
		this.javabeanList = javabeanList;
	}

	protected Control createCompositeContent() {
		Composite parent = this;
		parent.setLayout(new FillLayout());
		Composite com = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		com.setLayout(layout);

		Label classLabel = new Label(com, SWT.NULL);
		classLabel.setText(Messages
				.getString("JavaBeanModelLoadComposite.ClassNameText")); //$NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		classLabel.setLayoutData(gd);
		// Composite classTextContainer = new Composite(com, SWT.NONE);
		// GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		// gd.grabExcessHorizontalSpace = true;
		// classTextContainer.setLayoutData(gd);
		//
		// GridLayout gl = new GridLayout();
		// gl.numColumns = 2;
		// classTextContainer.setLayout(gl);
		//
		// {
		// classText = new Text(classTextContainer, SWT.BORDER);
		// gd = new GridData(GridData.FILL_HORIZONTAL);
		// gd.grabExcessHorizontalSpace = true;
		// classText.setLayoutData(gd);
		// classText.addModifyListener(new ModifyListener() {
		// public void modifyText(ModifyEvent arg0) {
		// classFullName = classText.getText();
		// }
		// });
		//
		// classBrowseButton = new Button(classTextContainer, SWT.NONE);
		// classBrowseButton.addSelectionListener(this);
		//			classBrowseButton.setText(Messages.getString("JavaBeanModelLoadComposite.Browse")); //$NON-NLS-1$
		// }

		Composite listViewerComposite = new Composite(com, SWT.NONE);
		GridLayout listLayout = new GridLayout();
		listLayout.numColumns = 2;
		listViewerComposite.setLayout(listLayout);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		listViewerComposite.setLayoutData(gd);

		listViewer = new TableViewer(listViewerComposite, SWT.BORDER);
		gd = new GridData(GridData.FILL_BOTH);
		listViewer.getControl().setLayoutData(gd);
		listViewer.setContentProvider(new IStructuredContentProvider() {

			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof List) {
					return ((List) inputElement).toArray();
				}
				return new Object[] {};
			}

			public void dispose() {

			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {

			}

		});
		listViewer.setLabelProvider(new LabelProvider() {

			@Override
			public Image getImage(Object element) {
				if (element instanceof JavaBeanModel) {
					return SmooksUIActivator.getDefault().getImageRegistry()
							.get(JavaImageConstants.IMAGE_JAVA_OBJECT);
				}
				return super.getImage(element);
			}

			@Override
			public String getText(Object element) {
				if (element instanceof JavaBeanModel) {
					return ((JavaBeanModel) element).getBeanClassString();
				}
				return super.getText(element);
			}

		});
		listViewer.setInput(javabeanList);

		Composite buttonArea = new Composite(listViewerComposite, SWT.NONE);
		gd = new GridData(GridData.FILL_VERTICAL);
		buttonArea.setLayoutData(gd);

		GridLayout buttonAreaLayout = new GridLayout();
		buttonArea.setLayout(buttonAreaLayout);

		Button addButton = new Button(buttonArea, SWT.NONE);
		addButton.setText("Add");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		addButton.setLayoutData(gd);
		addButton.addSelectionListener(this);

		Button removeButton = new Button(buttonArea, SWT.NONE);
		removeButton.setText("Remove");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		removeButton.setLayoutData(gd);
		removeButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) listViewer
						.getSelection();
				if (selection.isEmpty())
					return;
				javabeanList.removeAll(selection.toList());
				listViewer.refresh();
			}

		});
		return com;
	}

	public ProjectClassLoader getProjectClassLoader() {
		if (loader == null) {
			try {
				loader = new ProjectClassLoader(javaProject);
			} catch (Exception e) {
			}
		}
		return loader;
	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
		this.widgetSelected(arg0);

	}

	public void widgetSelected(SelectionEvent arg0) {
		IJavaSearchScope scope = JavaSearchScopeFactory.getInstance()
				.createJavaProjectSearchScope(javaProject, true);
		SelectionDialog dialog;
		Exception exception = null;
		try {
			dialog = JavaUI.createTypeDialog(this.getShell(), runnableContext,
					scope, IJavaElementSearchConstants.CONSIDER_CLASSES, false);
			dialog.setMessage(Messages
					.getString("JavaBeanModelLoadComposite.SourceJavaBean")); //$NON-NLS-1$
			dialog.setTitle(Messages
					.getString("JavaBeanModelLoadComposite.SearchJavaType")); //$NON-NLS-1$

			if (dialog.open() == Window.OK) {
				Object[] results = dialog.getResult();
				if (results.length > 0) {
					Object result = results[0];
					String packageFullName = JavaModelUtil
							.getTypeContainerName((IType) result);
					String className = null;
					if (packageFullName == null
							|| packageFullName.length() <= 0) {
						className = ((IType) result).getElementName();
					} else {
						className = packageFullName + "." //$NON-NLS-1$
								+ ((IType) result).getElementName();
					}
					if (className != null) {
						ClassLoader l = this.getProjectClassLoader();
						if (l != null) {
							Class clazz = l.loadClass(className);
							if (clazz != null) {
								JavaBeanModel model = JavaBeanModelFactory
										.getJavaBeanModelWithLazyLoad(clazz);
								javabeanList.add(model);
								listViewer.refresh();
							}
						}

					}
				}
			}
		} catch (Exception e){
			exception = e;
		}
		if (exception != null) {
			for (Iterator<IJavaBeanSelectionListener> iterator = this.selectionListenerList
					.iterator(); iterator.hasNext();) {
				IJavaBeanSelectionListener l = (IJavaBeanSelectionListener) iterator
						.next();
				l.exceptionOccur(exception);
			}
		}

	}

}
