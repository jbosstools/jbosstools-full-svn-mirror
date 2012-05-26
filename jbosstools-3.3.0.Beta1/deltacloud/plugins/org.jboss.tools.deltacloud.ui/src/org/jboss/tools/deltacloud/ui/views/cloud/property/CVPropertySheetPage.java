/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.deltacloud.ui.views.cloud.property;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.ui.views.cloud.CloudItem;
import org.jboss.tools.internal.deltacloud.ui.utils.WorkbenchUtils;

/**
 * A custom property sheet page for deltacloud element properties.
 * 
 * @Jeff Johnston
 * @author André Dietisheim
 */
public class CVPropertySheetPage extends Page implements IPropertySheetPage {

	public static final String ID = "org.jboss.tools.deltacloud.ui.views.CVPropertySheetPage";

	protected TreeViewer viewer;
	protected Composite control;

	private DeltaCloud deltaCloud;
	private PropertyChangeListener cloudPropertyListener = new PropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					viewer.refresh();
				}
			});
		}
	};

	public CVPropertySheetPage() {
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		CloudItem cloudItem = WorkbenchUtils.getFirstAdaptedElement(selection, CloudItem.class);
		if (cloudItem != null) {
			removePropertyChangeListener(this.deltaCloud);
			DeltaCloud deltaCloud = cloudItem.getModel();
			if (deltaCloud != null) {
				addPropertyChangeListener(deltaCloud);
				this.deltaCloud = deltaCloud;
			}
		}

		setInput(selection);
	}

	private void setInput(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			Object o = ((IStructuredSelection) selection).getFirstElement();
			if (o != null) {
				viewer.setInput(o);
			}
		}
	}

	private void addPropertyChangeListener(DeltaCloud deltaCloud) {
		if (deltaCloud != null) {
			deltaCloud.addPropertyChangeListener(cloudPropertyListener);
		}
	}

	private void removePropertyChangeListener(DeltaCloud deltaCloud) {
		if (deltaCloud != null) {
			deltaCloud.removePropertyChangeListener(cloudPropertyListener);
		}
	}

	@Override
	public void dispose() {
		removePropertyChangeListener(deltaCloud);
	}

	@Override
	public void createControl(Composite parent) {
		control = new Composite(parent, SWT.BORDER);
		control.setLayout(new FillLayout());
		this.viewer = createTreeViewer(control);
		getSite().setSelectionProvider(viewer);
		createContextMenu(viewer.getControl());
	}

	private TreeViewer createTreeViewer(Composite parent) {
		final Tree tree = new Tree(parent, SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.NONE);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		tree.setFont(parent.getFont());
		
		TreeColumn column = new TreeColumn(tree, SWT.SINGLE);
		column.setText("Property");
		TreeColumn column2 = new TreeColumn(tree, SWT.SINGLE);
		column2.setText("Value");

		tree.addControlListener(setTreeColumInitialSize(tree));

		TreeViewer viewer = new TreeViewer(tree);
		PropertySourceContentProvider provider = new PropertySourceContentProvider();
		viewer.setContentProvider(provider);
		viewer.setLabelProvider(provider);
		return viewer;
	}

	private ControlAdapter setTreeColumInitialSize(final Tree tree) {
		return new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle area = tree.getClientArea();
				TreeColumn[] columns = tree.getColumns();
				if (area.width > 0) {
					columns[0].setWidth(area.width * 40 / 100);
					columns[1].setWidth(area.width - columns[0].getWidth() - 4);
					tree.removeControlListener(this);
				}
			}
		};
	}

	private void createContextMenu(Control control) {
		IMenuManager menuManager = WorkbenchUtils.createContextMenu(control);
		WorkbenchUtils.registerContributionManager(WorkbenchUtils.getContextMenuId(ID), menuManager, control);
	}

	@Override
	public Control getControl() {
		return control;
	}

	@Override
	public void setActionBars(IActionBars actionBars) {
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public static class PropertySourceContentProvider extends LabelProvider
			implements ITableLabelProvider, ITreeContentProvider {

		protected IPropertySource input;

		public static class CVPropertySheetPageEntry {
			private IPropertyDescriptor descriptor;
			private IPropertySource source;

			private CVPropertySheetPageEntry(IPropertyDescriptor descriptor, IPropertySource source) {
				this.descriptor = descriptor;
				this.source = source;
			}

			public String getLabel() {
				return descriptor.getDisplayName();
			}

			public Object getValue() {
				return source.getPropertyValue(descriptor.getId());
			}

			public String getStringValue() {
				return String.valueOf(getValue());
			}
		}

		public PropertySourceContentProvider() {
		}

		public Object[] getElements(Object inputElement) {
			IPropertySource propertySource = WorkbenchUtils.adapt(inputElement, IPropertySource.class);
			if (propertySource == null) {
				return new Object[]{};
			}
			this.input = propertySource;
			List<CVPropertySheetPageEntry> elements = new ArrayList<CVPropertySheetPageEntry>();
			for (IPropertyDescriptor descriptor : input.getPropertyDescriptors()) {
				elements.add(new CVPropertySheetPageEntry(descriptor, input));
			}
			return elements.toArray();
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		public Object[] getChildren(Object parentElement) {
			return new Object[] {};
		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object element) {
			return false;
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (columnIndex == 0) {
				return ((CVPropertySheetPageEntry) element).getLabel();
			}
			else if (columnIndex == 1 && element instanceof CVPropertySheetPageEntry) {
				return String.valueOf(((CVPropertySheetPageEntry) element).getValue());
			}
			return null;
		}
	}

	public void refresh() {
		viewer.refresh();
	}
}
