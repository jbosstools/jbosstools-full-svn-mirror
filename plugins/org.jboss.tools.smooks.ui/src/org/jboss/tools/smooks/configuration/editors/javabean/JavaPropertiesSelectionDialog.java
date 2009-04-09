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
package org.jboss.tools.smooks.configuration.editors.javabean;

import java.beans.PropertyDescriptor;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;
import org.jboss.tools.smooks.configuration.editors.uitls.IFieldDialog;
import org.jboss.tools.smooks.configuration.editors.uitls.JavaPropertyUtils;

/**
 * @author Dart (dpeng@redhat.com)
 *         <p>
 *         Apr 9, 2009
 */
public class JavaPropertiesSelectionDialog implements IFieldDialog {

	private IJavaProject resource;

	private Class<?> clazz;

	public JavaPropertiesSelectionDialog(IJavaProject resource, Class<?> clazz) {
		super();
		this.resource = resource;
		this.clazz = clazz;
	}

	public Object open(Shell shell) {
		if (resource != null && clazz != null) {
			PropertySelectionDialog dialog = new PropertySelectionDialog(shell, resource, clazz);
			if (dialog.open() == Dialog.OK) {
				PropertyDescriptor pd = (PropertyDescriptor) dialog.getCurrentSelection();
				return pd.getName();
			}else{
				return null;
			}
		}
		MessageDialog.openInformation(shell, "Can't open dialog",
				"Can't open java properties selection dialog.");
		return null;
	}

	private class PropertySelectionDialog extends Dialog {

		private TableViewer viewer;

		private IJavaProject project;

		private Class<?> clazz;

		private Object currentSelection;

		
		
		public PropertySelectionDialog(IShellProvider parentShell) {
			super(parentShell);
		}

		public PropertySelectionDialog(Shell parentShell, IJavaProject project, Class<?> clazz) {
			super(parentShell);
			this.project = project;
			this.clazz = clazz;
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			Composite composite = (Composite) super.createDialogArea(parent);
			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.heightHint = 400;
			gd.widthHint = 400;
			composite.setLayoutData(gd);
			composite.setLayout(new FillLayout());
			viewer = new TableViewer(composite, SWT.BORDER);
			Table table = viewer.getTable();
			TableColumn nameColumn = new TableColumn(table, SWT.NONE);
			nameColumn.setWidth(100);
			nameColumn.setText("Name");
			TableColumn typeColumn = new TableColumn(table, SWT.NONE);
			typeColumn.setWidth(200);
			typeColumn.setText("Type");
			table.setHeaderVisible(true);
			viewer.setContentProvider(new PropertyDescriptorContentProvider());
			viewer.setLabelProvider(new PropertyDescriptorLabelProvider());
			PropertyDescriptor[] pds = JavaPropertyUtils.getPropertyDescriptor(clazz);
			viewer.setInput(pds);
			viewer.addSelectionChangedListener(new ISelectionChangedListener() {

				public void selectionChanged(SelectionChangedEvent event) {
					currentSelection = ((IStructuredSelection) event.getSelection()).getFirstElement();
				}

			});
			return composite;
		}

		public Object getCurrentSelection() {
			return currentSelection;
		}

		public void setCurrentSelection(Object currentSelection) {
			this.currentSelection = currentSelection;
		}
	}

	private class PropertyDescriptorContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement.getClass().isArray()) {
				return (Object[]) inputElement;
			}
			return new Object[] {};
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}

	}

	private class PropertyDescriptorLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			if (columnIndex == 0) {
				return SmooksConfigurationActivator.getDefault().getImageRegistry().get(
						GraphicsConstants.JAVA_PROPERTY_ICON);
			}
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof PropertyDescriptor) {
				PropertyDescriptor p = (PropertyDescriptor) element;
				switch (columnIndex) {
				case 0:
					return p.getName();
				case 1:
					Class<?> cla = p.getPropertyType();
					if(cla.isArray()){
						return cla.getComponentType().getName() + "[]";
					}
					return cla.getName();
				}
			}
			return getText(element);
		}
	}
}
