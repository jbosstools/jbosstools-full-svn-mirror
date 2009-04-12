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
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.javabean.JavaBeanModel;
import org.jboss.tools.smooks.configuration.editors.javabean.JavaBeanModelFactory;
import org.jboss.tools.smooks.configuration.editors.uitls.ProjectClassLoader;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.editors.xml.AbstractXMLObject;
import org.jboss.tools.smooks.configuration.editors.xml.TagList;
import org.jboss.tools.smooks.configuration.editors.xml.XMLObjectAnalyzer;
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
		gd.heightHint = 500;
		gd.widthHint = 450;
		composite.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		layout.makeColumnsEqualWidth = false;
		composite.setLayout(layout);
		
		Label label = new Label(composite,SWT.NONE);
		label.setText("Sperator Char : ");
		CCombo speratorCombo = new CCombo(composite,SWT.BORDER);
		speratorCombo.add(" ");
		speratorCombo.add("/");
		speratorCombo.setEditable(false);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		speratorCombo.setLayoutData(gd);
		
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
		
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		viewer.getTree().setLayoutData(gd);
		
		Label l = new Label(composite,SWT.NONE);
		l.setText("Selector generate policy : ");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		l.setLayoutData(gd);
		
		Composite com = new Composite(composite,SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		com.setLayoutData(gd);
		
		GridLayout gl = new GridLayout();
		gl.numColumns = 3;
		com.setLayout(gl);
		Button fullPathButton  = new Button(com,SWT.RADIO);
		fullPathButton.setText("Full Path");
		Button onlyNameButton  = new Button(com,SWT.RADIO);
		onlyNameButton.setText("Only Name");
		Button containtParentButton  = new Button(com,SWT.RADIO);
		containtParentButton.setText("Containt Parent Name");
		
		getShell().setText("Selector generate dialog");
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
							try {
								path = SmooksUIUtils.parseFilePath(path);
								AbstractXMLObject model = new XMLObjectAnalyzer().analyze(path, null);
								if (model != null) {
									if (model instanceof TagList) {
										List<IXMLStructuredObject> children = ((TagList) model).getChildren();
										for (Iterator<?> iterator2 = children.iterator(); iterator2.hasNext();) {
											IXMLStructuredObject structuredObject = (IXMLStructuredObject) iterator2
													.next();
											list.add(structuredObject);
										}
									} else {
										list.add(model);
									}
								}
							} finally {

							}
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
