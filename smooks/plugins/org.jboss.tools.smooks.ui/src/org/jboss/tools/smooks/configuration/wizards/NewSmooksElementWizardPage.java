/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.wizards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.smooks.configuration.actions.AddSmooksResourceAction;
import org.jboss.tools.smooks.configuration.editors.actions.Calc11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.Database11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.Datasources11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.FragmentRouting11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.FragmentRouting12ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.ISmooksActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.JavaBean11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.JavaBean12ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.PersistenceActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.Reader11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.Reader12ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.Scripting11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.Templating11ActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.Validation10ActionGrouper;

/**
 * @author Dart
 * 
 */
public class NewSmooksElementWizardPage extends org.eclipse.jface.wizard.WizardPage {

	private Collection<IAction> childDescriptor = null;

	private IAction selectedAction;

	private TreeViewer actionViewer = null;

	private ViewerFilter[] filters;

	public NewSmooksElementWizardPage(String pageName, String title, ImageDescriptor titleImage,
			Collection<IAction> childDescriptor, ViewerFilter[] filters, String text, String description) {
		super(pageName, title, titleImage);
		this.childDescriptor = childDescriptor;
		this.setTitle("Add Child");
		this.setDescription("Add Smooks Elements");
		if (text != null) {
			this.setTitle(text);
		}
		if (description != null) {
			this.setDescription(description);
		}
		this.filters = filters;
	}

	public NewSmooksElementWizardPage(String pageName, Collection<IAction> childDescriptor, ViewerFilter[] filters,
			String text, String description) {
		super(pageName);
		this.childDescriptor = childDescriptor;
		this.setTitle("Add Child");
		this.setDescription("Add Smooks Elements");
		if (text != null) {
			this.setTitle(text);
		}
		if (description != null) {
			this.setDescription(description);
		}
		this.filters = filters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createControl(Composite parent) {
		selectedAction = null;
		actionViewer = new TreeViewer(parent, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_BOTH);
		actionViewer.getControl().setLayoutData(gd);

		actionViewer.setContentProvider(new SmooksElementActionsContentProvider());
		actionViewer.setLabelProvider(new SmooksElementActionsLabelProvider());
		actionViewer.setInput(getActions());
		actionViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				selectedAction = (IAction) ((IStructuredSelection) event.getSelection()).getFirstElement();
				updatePageStatus();
			}
		});

		actionViewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				// getWizard().get
			}
		});
		this.setControl(parent);
	}

	public IAction getSelectedAction() {
		return selectedAction;
	}

	protected void updatePageStatus() {
		this.setPageComplete(true);
		if (selectedAction instanceof GroupAction) {
			this.setPageComplete(false);
		}
	}

	private void filterAction() {
		if (this.filters != null) {
			for (Iterator<?> iterator = childDescriptor.iterator(); iterator.hasNext();) {
				AddSmooksResourceAction action = (AddSmooksResourceAction) iterator.next();
				if (!action.isEnabled()) {
					continue;
				}
				for (int i = 0; i < filters.length; i++) {
					ViewerFilter vf = filters[i];
					Object descriptor = action.getDescriptor();
					if (descriptor instanceof CommandParameter) {
						CommandParameter parameter = (CommandParameter) descriptor;
						if (parameter.getValue() != null) {
							Object value = AdapterFactoryEditingDomain.unwrap(parameter.getValue());
							boolean enable = vf.select(null, null, value);
							action.setEnabled(enable);
							if (!enable) {
								break;
							}
						}
					}
				}
			}

		}
	}

	private Collection<IAction> getActions() {
		filterAction();
		List<IAction> actions = new ArrayList<IAction>();
		List<ISmooksActionGrouper> grouperList = getSmooksActionGrouper();
		for (Iterator<?> iterator = grouperList.iterator(); iterator.hasNext();) {
			ISmooksActionGrouper iSmooksActionGrouper = (ISmooksActionGrouper) iterator.next();
			GroupAction ga = new GroupAction();
			ga.setText(iSmooksActionGrouper.getGroupName());
			fillGroupAction(ga, iSmooksActionGrouper);
			if (!ga.getChildrenAction().isEmpty())
				actions.add(ga);
		}
		List<IAction> tempActions = new ArrayList<IAction>(actions);
		for (Iterator<?> iterator2 = this.childDescriptor.iterator(); iterator2.hasNext();) {
			IAction c = (IAction) iterator2.next();
			boolean exsit = false;
			for (Iterator<?> iterator = tempActions.iterator(); iterator.hasNext();) {
				IAction iAction = (IAction) iterator.next();
				if (iAction instanceof GroupAction) {
					List<IAction> childrenAction = ((GroupAction) iAction).getChildrenAction();
					if (childrenAction.indexOf(c) != -1) {
						exsit = true;
						break;
					}
				}
			}
			if (!exsit && c.isEnabled()) {
				actions.add(c);
			}
		}

		return actions;
	}

	private void fillGroupAction(GroupAction ga, ISmooksActionGrouper grouper) {
		if (childDescriptor != null) {
			for (Iterator<?> iterator = childDescriptor.iterator(); iterator.hasNext();) {
				AddSmooksResourceAction action = (AddSmooksResourceAction) iterator.next();
				Object descriptor = action.getDescriptor();
				if (grouper.belongsToGroup(descriptor) && action.isEnabled()) {
					ga.addAction(action);
				}
			}
		}
	}

	private List<ISmooksActionGrouper> getSmooksActionGrouper() {
		List<ISmooksActionGrouper> grouperList = new ArrayList<ISmooksActionGrouper>();

		grouperList.add(new JavaBean11ActionGrouper());
		grouperList.add(new Reader11ActionGrouper());
		grouperList.add(new Calc11ActionGrouper());
		grouperList.add(new Database11ActionGrouper());
		grouperList.add(new Datasources11ActionGrouper());
		grouperList.add(new FragmentRouting11ActionGrouper());
		grouperList.add(new Scripting11ActionGrouper());
		grouperList.add(new Templating11ActionGrouper());
		// grouperList.add(new SeparatorActionGrouper("V1.1-V1.2"));
		grouperList.add(new JavaBean12ActionGrouper());
		grouperList.add(new Reader12ActionGrouper());
		grouperList.add(new FragmentRouting12ActionGrouper());
		grouperList.add(new PersistenceActionGrouper());
		grouperList.add(new Validation10ActionGrouper());
		// grouperList.add(new SeparatorActionGrouper("No Group actions"));
		return grouperList;
	}

	private class SmooksElementActionsContentProvider implements ITreeContentProvider {

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof Collection<?>) {
				return ((Collection<?>) parentElement).toArray();
			}
			if (parentElement instanceof GroupAction) {
				return ((GroupAction) parentElement).getChildrenAction().toArray();
			}
			return new Object[] {};
		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object element) {
			if (element instanceof Collection<?>)
				return !((Collection<?>) element).isEmpty();
			if (element instanceof GroupAction) {
				return !((GroupAction) element).getChildrenAction().isEmpty();
			}
			return false;
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof Collection<?>) {
				return ((Collection<?>) inputElement).toArray();
			}
			return new Object[] {};
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}
	}

	private class GroupAction extends Action {

		private List<IAction> childrenAction = null;

		@Override
		public void run() {
		}

		public List<IAction> getChildrenAction() {
			if (childrenAction == null) {
				childrenAction = new ArrayList<IAction>();
			}
			return childrenAction;
		}

		public void addAction(IAction action) {
			this.getChildrenAction().add(action);
		}

		// public void removeAction(IAction action){
		// this.getChildrenAction().remove(action);
		// }

	}

	private class SmooksElementActionsLabelProvider extends LabelProvider {

		private Map<Object, Object> imageMap = new HashMap<Object, Object>();

		@Override
		public void dispose() {
			if (imageMap != null) {
				Collection<?> values = imageMap.values();
				for (Iterator<?> iterator = values.iterator(); iterator.hasNext();) {
					Object object = (Object) iterator.next();
					if (object != null && object instanceof Image) {
						((Image) object).dispose();
					}
				}
			}
			super.dispose();
		}

		@Override
		public Image getImage(Object element) {
			if (element instanceof IAction) {
				String key = ((IAction) element).getText();
				Image img = (Image) imageMap.get(key);
				if (img == null) {
					ImageDescriptor id = ((IAction) element).getImageDescriptor();
					if (id == null)
						return null;
					img = id.createImage();
					((IAction) element).getDisabledImageDescriptor();
					imageMap.put(key, img);
				} else {

				}
				return img;
			}
			return super.getImage(element);
		}

		@Override
		public String getText(Object element) {
			if (element instanceof IAction) {
				return ((IAction) element).getText();
			}
			return super.getText(element);
		}

	}
}
