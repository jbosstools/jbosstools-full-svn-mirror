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
package org.jboss.tools.smooks.configuration.editors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.javabean.JavaBeanModel;
import org.jboss.tools.smooks.configuration.editors.javabean.JavaBeanModelFactory;
import org.jboss.tools.smooks.configuration.editors.uitls.ProjectClassLoader;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.model.graphics.ext.InputType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;

/**
 * @author Dart (dpeng@redhat.com)
 *         <p>
 *         Apr 12, 2009
 */
public class InputStructuredDataSelectionDialog extends Dialog {

	private SmooksGraphicsExtType graphicsExt;
	private TreeViewer viewer;
	private Object currentSelection;

	public InputStructuredDataSelectionDialog(IShellProvider parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	public InputStructuredDataSelectionDialog(Shell parentShell, SmooksGraphicsExtType graphicsExt) {
		super(parentShell);
		this.graphicsExt = graphicsExt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 400;
		gd.widthHint = 400;
		composite.setLayoutData(gd);
		composite.setLayout(new FillLayout());
		viewer = new TreeViewer(composite, SWT.BORDER);
		viewer.setContentProvider(new CompoundStructuredDataContentProvider());
		viewer.setLabelProvider(new CompoundStructuredDataLabelProvider());
		viewer.setInput(generateInputData());
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				okPressed();
			}
		});
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				currentSelection = ((IStructuredSelection) event.getSelection()).getFirstElement();
			}
		});
		getShell().setText("Input Datas");
		return composite;
	}

	protected List<?> generateInputData() {
		List<Object> list = new ArrayList<Object>();
		if (this.graphicsExt != null) {
			IJavaProject project = SmooksUIUtils.getJavaProject(graphicsExt);
			try {
				ProjectClassLoader classLoader = new ProjectClassLoader(project);
				List<InputType> inputLists = graphicsExt.getInput();
				for (Iterator<?> iterator = inputLists.iterator(); iterator.hasNext();) {
					InputType inputType = (InputType) iterator.next();
					String type = inputType.getType();
					String path = SmooksModelUtils.getInputPath(inputType);
					if (type != null && path != null) {
						path = path.trim();
						if (SmooksModelUtils.INPUT_TYPE_JAVA.equals(type)) {
							try {
								JavaBeanModel model = JavaBeanModelFactory
										.getJavaBeanModelWithLazyLoad(classLoader.loadClass(path));
								if (model != null) {
									list.add(model);
								}
							} finally {

							}
						}
						if (SmooksModelUtils.INPUT_TYPE_XML.equals(type)) {

						}
					}
				}
			} catch (Exception e) {
				SmooksConfigurationActivator.getDefault().log(e);
			}
		}
		return list;
	}

	/**
	 * @return the currentSelection
	 */
	public Object getCurrentSelection() {
		return currentSelection;
	}
}
