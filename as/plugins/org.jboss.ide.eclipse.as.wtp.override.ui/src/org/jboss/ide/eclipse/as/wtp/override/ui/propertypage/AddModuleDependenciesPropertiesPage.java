/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *	   David Schneider, david.schneider@unisys.com - [142500] WTP properties pages fonts don't follow Eclipse preferences
 *     Stefan Dimov, stefan.dimov@sap.com - bugs 207826, 222651
 *******************************************************************************/
package org.jboss.ide.eclipse.as.wtp.override.ui.propertypage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.wizards.BuildPathDialogAccess;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jst.j2ee.componentcore.J2EEModuleVirtualArchiveComponent;
import org.eclipse.jst.j2ee.internal.ManifestUIResourceHandler;
import org.eclipse.jst.j2ee.internal.plugin.IJ2EEModuleConstants;
import org.eclipse.jst.j2ee.internal.plugin.J2EEUIMessages;
import org.eclipse.jst.j2ee.internal.plugin.J2EEUIPlugin;
import org.eclipse.jst.j2ee.project.JavaEEProjectUtilities;
import org.eclipse.jst.j2ee.project.facet.IJavaProjectMigrationDataModelProperties;
import org.eclipse.jst.j2ee.project.facet.JavaProjectMigrationDataModelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.ModuleCoreNature;
import org.eclipse.wst.common.componentcore.datamodel.properties.ICreateReferenceComponentsDataModelProperties;
import org.eclipse.wst.common.componentcore.internal.operation.CreateReferenceComponentsDataModelProvider;
import org.eclipse.wst.common.componentcore.internal.operation.RemoveReferenceComponentsDataModelProvider;
import org.eclipse.wst.common.componentcore.internal.resources.VirtualArchiveComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelProvider;
import org.jboss.ide.eclipse.as.wtp.override.ui.Messages;

public class AddModuleDependenciesPropertiesPage implements Listener,
		IModuleDependenciesControl {

	private static final String DEPLOY_PATH_PROPERTY = new Integer(0).toString();
	private static final String SOURCE_PROPERTY = new Integer(1).toString();
	
	
	protected final String PATH_SEPARATOR = ComponentDependencyContentProvider.PATH_SEPARATOR;
	private boolean hasInitialized = false;
	protected final IProject project;
	protected final J2EEDependenciesPage propPage;
	protected IVirtualComponent rootComponent = null;
	protected Text componentNameText;
	protected Label availableModules;
	protected TableViewer availableComponentsViewer;
	protected Button projectButton, removeButton;
	protected Button projectJarButton;
	protected Button externalJarButton;
	protected Button addVariableButton;
	protected Composite buttonColumn;
	protected static final IStatus OK_STATUS = IDataModelProvider.OK_STATUS;
	protected Listener tableListener;
	protected Listener labelListener;

	protected HashMap<IVirtualComponent, String> oldComponentToRuntimePath = new HashMap<IVirtualComponent, String>();

	// This should keep a list of all elements currently in the list (not removed)
	protected HashMap<Object, String> objectToRuntimePath = new HashMap<Object, String>();

	/**
	 * Constructor for AddModulestoEARPropertiesControl.
	 */
	public AddModuleDependenciesPropertiesPage(final IProject project,
			final J2EEDependenciesPage page) {
		this.project = project;
		this.propPage = page;
		rootComponent = ComponentCore.createComponent(project);
	}

	/*
	 * UI Creation Methods
	 */

	public Composite createContents(final Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		J2EEDependenciesPage.createDescriptionComposite(composite,
				ManifestUIResourceHandler.EAR_Modules_Desc);
		createListGroup(composite);
		refresh();
		Dialog.applyDialogFont(parent);
		return composite;
	}

	protected void createListGroup(Composite parent) {
		Composite listGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		listGroup.setLayout(layout);
		GridData gData = new GridData(GridData.FILL_BOTH);
		gData.horizontalIndent = 5;
		listGroup.setLayoutData(gData);

		availableModules = new Label(listGroup, SWT.NONE);
		gData = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_FILL);
		availableModules.setText(J2EEUIMessages
				.getResourceString("AVAILABLE_J2EE_COMPONENTS")); //$NON-NLS-1$ = "Available dependent JARs:"
		availableModules.setLayoutData(gData);
		createTableComposite(listGroup);
	}

	protected void createTableComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridData gData = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(gData);
		fillComposite(composite);
	}

	public void fillComposite(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		parent.setLayout(layout);
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));
		createTable(parent);
		createButtonColumn(parent);
	}

	protected void createButtonColumn(Composite parent) {
		buttonColumn = createButtonColumnComposite(parent);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		buttonColumn.setLayoutData(data);
		createPushButtons();
	}

	protected void createPushButtons() {
		projectButton = createPushButton(Messages.AddModuleDependenciesPropertiesPage_AddProjectButton);
		projectJarButton = createPushButton(J2EEUIMessages
				.getResourceString(J2EEUIMessages.PROJECT_JAR));
		externalJarButton = createPushButton(J2EEUIMessages
				.getResourceString(J2EEUIMessages.EXTERNAL_JAR));
		addVariableButton = createPushButton(J2EEUIMessages
				.getResourceString(J2EEUIMessages.ADDVARIABLE));
		removeButton = createPushButton(Messages.AddModuleDependenciesPropertiesPage_RemoveSelectedButton);
	}

	protected Button createPushButton(String label) {
		Button aButton = new Button(buttonColumn, SWT.PUSH);
		aButton.setText(label);
		aButton.addListener(SWT.Selection, this);
		aButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return aButton;
	}

	public Composite createButtonColumnComposite(Composite parent) {
		Composite aButtonColumn = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		aButtonColumn.setLayout(layout);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING);
		aButtonColumn.setLayoutData(data);
		return aButtonColumn;
	}

	public Group createGroup(Composite parent) {
		return new Group(parent, SWT.NULL);
	}

	protected void createTable(Composite parent) {
		availableComponentsViewer = createAvailableComponentsViewer(parent);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.FILL_VERTICAL);
		availableComponentsViewer.getTable().setLayoutData(gd);

		if (rootComponent != null) {
			ComponentDependencyContentProvider provider = createProvider();
			provider.setRuntimePaths(objectToRuntimePath);
			availableComponentsViewer.setContentProvider(provider);
			availableComponentsViewer.setLabelProvider(provider);
			addTableListeners();
		}
	}

	/**
	 * Subclasses should over-ride this and extend the class
	 */
	protected ComponentDependencyContentProvider createProvider() {
		return new ComponentDependencyContentProvider();
	}

	/*
	 * Listeners of various events
	 */

	protected void addTableListeners() {
		addHoverHelpListeners();
		addDoubleClickListener();
		addSelectionListener();
	}

	protected void addHoverHelpListeners() {
		final Table table = availableComponentsViewer.getTable();
		createLabelListener(table);
		createTableListener(table);
		table.addListener(SWT.Dispose, tableListener);
		table.addListener(SWT.KeyDown, tableListener);
		table.addListener(SWT.MouseMove, tableListener);
		table.addListener(SWT.MouseHover, tableListener);
	}

	protected void createLabelListener(final Table table) {
		labelListener = new Listener() {
			public void handleEvent(Event event) {
				Label label = (Label) event.widget;
				Shell shell = label.getShell();
				switch (event.type) {
				case SWT.MouseDown:
					Event e = new Event();
					e.item = (TableItem) label.getData("_TABLEITEM"); //$NON-NLS-1$
					table.setSelection(new TableItem[] { (TableItem) e.item });
					table.notifyListeners(SWT.Selection, e);
					shell.dispose();
					table.setFocus();
					break;
				case SWT.MouseExit:
					shell.dispose();
					break;
				}
			}
		};
	}

	protected void createTableListener(final Table table) {
		tableListener = new Listener() {
			Shell tip = null;
			Label label = null;

			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.Dispose:
				case SWT.KeyDown:
				case SWT.MouseMove: {
					if (tip == null)
						break;
					tip.dispose();
					tip = null;
					label = null;
					break;
				}
				case SWT.MouseHover: {
					TableItem item = table.getItem(new Point(event.x, event.y));
					if (item != null && item.getData() != null && !canEdit(item.getData())) {
						if (tip != null && !tip.isDisposed())
							tip.dispose();
						tip = new Shell(PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow().getShell(),
								SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
						tip.setBackground(Display.getDefault().getSystemColor(
								SWT.COLOR_INFO_BACKGROUND));
						FillLayout layout = new FillLayout();
						layout.marginWidth = 2;
						tip.setLayout(layout);
						label = new Label(tip, SWT.WRAP);
						label.setForeground(Display.getDefault()
								.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
						label.setBackground(Display.getDefault()
								.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
						label.setData("_TABLEITEM", item); //$NON-NLS-1$
						label.setText(J2EEUIMessages
										.getResourceString(J2EEUIMessages.HOVER_HELP_FOR_DISABLED_LIBS));
						label.addListener(SWT.MouseExit, labelListener);
						label.addListener(SWT.MouseDown, labelListener);
						Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
						Rectangle rect = item.getBounds(0);
						Point pt = table.toDisplay(rect.x, rect.y);
						tip.setBounds(pt.x, pt.y - size.y, size.x, size.y);
						tip.setVisible(true);
					}
				}
				}
			}
		};
	}

	protected boolean canEdit(Object data) {
		if( data == null ) return false;
		if( !(data instanceof VirtualArchiveComponent)) return true;
		
		VirtualArchiveComponent d2 = (VirtualArchiveComponent)data;
		boolean sameProject = d2.getWorkspaceRelativePath() != null
			&& d2.getWorkspaceRelativePath().segment(0)
				.equals(rootComponent.getProject().getName());
		return !(sameProject && isPhysicallyAdded(d2));
	}
	
	protected void addDoubleClickListener() {
		availableComponentsViewer.setColumnProperties(new String[] { 
				DEPLOY_PATH_PROPERTY, SOURCE_PROPERTY });
		
		CellEditor[] editors = new CellEditor[] { 
				new TextCellEditor(availableComponentsViewer.getTable()),
				new TextCellEditor()};
		availableComponentsViewer.setCellEditors(editors);
		availableComponentsViewer
				.setCellModifier(new RuntimePathCellModifier());
	}

	protected void addSelectionListener() {
		availableComponentsViewer.addSelectionChangedListener(
				new ISelectionChangedListener(){
					public void selectionChanged(SelectionChangedEvent event) {
						viewerSelectionChanged();
					}
				});
	}
	
	protected void viewerSelectionChanged() {
		removeButton.setEnabled(getSelectedObject() != null && canEdit(getSelectedObject()));
	}
	
	protected Object getSelectedObject() {
		IStructuredSelection sel = (IStructuredSelection)availableComponentsViewer.getSelection();
		return sel.getFirstElement();
	}
	
	private class RuntimePathCellModifier implements ICellModifier {

		public boolean canModify(Object element, String property) {
			if( property.equals(DEPLOY_PATH_PROPERTY)) {
				if (element instanceof VirtualArchiveComponent) {
					try {
						return canEdit(element);
					} catch (IllegalArgumentException iae) {
					}
				}
				return true;
			}
			return false;
		}

		public Object getValue(Object element, String property) {
			return objectToRuntimePath.get(element) == null ? new Path("/") //$NON-NLS-1$
					.toString() : objectToRuntimePath.get(element);
		}

		public void modify(Object element, String property, Object value) {
			if (property.equals(DEPLOY_PATH_PROPERTY)) {
				TableItem item = (TableItem) element;
				objectToRuntimePath.put(item.getData(), (String) value);
				refresh();
			}
		}

	}

	public void handleEvent(Event event) {
		if (event.widget == projectButton)
			handleSelectProjectButton();
		else if (event.widget == projectJarButton)
			handleSelectProjectJarButton();
		else if (event.widget == externalJarButton)
			handleSelectExternalJarButton();
		else if (event.widget == addVariableButton)
			handleSelectVariableButton();
		else if( event.widget == removeButton ) 
			handleRemoveSelectedButton();
	}

	private void handleSelectProjectButton() {
		SelectProjectDialog d = new SelectProjectDialog(new Shell(),
				getProjectLabelProvider(), getProjectContentProvider());
		if (d.open() == Window.OK) {
			IProject selected = (IProject) d.getFirstResult();
			Object selected2 = ModuleCoreNature.isFlexibleProject(selected) ? 
					ComponentCore.createComponent(selected) : selected;
			IPath path = new Path("/"); //$NON-NLS-1$
			path = path.append(selected2 instanceof IVirtualComponent ? 
					getVirtualComponentNameWithExtension((IVirtualComponent)selected2) : selected.getName() + ".jar"); //$NON-NLS-1$
			objectToRuntimePath.put(selected2, path.toString());
			refresh();
			TableItem[] items = availableComponentsViewer.getTable().getItems();
			for (int i = 0; i < items.length; i++)
				if (items[i].getData().equals(selected2))
					items[i].setChecked(true);
		}
	}

	protected class SelectProjectDialog extends ElementTreeSelectionDialog {
		public SelectProjectDialog(Shell parent, ILabelProvider labelProvider,
				ITreeContentProvider contentProvider) {
			super(parent, labelProvider, contentProvider);
			setAllowMultiple(false);
			setTitle(Messages.AddModuleDependenciesPropertiesPage_SelectAProjectTitle);
			setInput(ResourcesPlugin.getWorkspace());
		}
	}

	protected ILabelProvider getProjectLabelProvider() {
		return new LabelProvider() {
			public Image getImage(Object element) {
				if (element instanceof IProject)
					return PlatformUI.getWorkbench().getSharedImages()
							.getImage(ISharedImages.IMG_OBJ_PROJECT);
				return null;
			}

			public String getText(Object element) {
				if (element instanceof IProject)
					return ((IProject) element).getName();
				return element == null ? "" : element.toString();//$NON-NLS-1$
			}
		};
	}

	protected ITreeContentProvider getProjectContentProvider() {
		return new ITreeContentProvider() {
			public Object[] getElements(Object inputElement) {
				return ResourcesPlugin.getWorkspace().getRoot().getProjects();
			}

			public Object[] getChildren(Object parentElement) {
				return null;
			}

			public Object getParent(Object element) {
				return null;
			}

			public boolean hasChildren(Object element) {
				return false;
			}

			public void dispose() {
			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}

		};
	}

	protected IProject[] filterAddProjectList(IProject[] list) {
		return list;
	}

	/**
	 * [Bug 238264] Add an archive as a potential new reference for
	 * this.earComponent NOTE1: the given archive will not be added as a
	 * potential reference if there is already a reference to it NOTE2: the
	 * given archive will only be added as an actual reference when
	 * this.performOk is invoked
	 * 
	 * @param archive
	 *            the archive to add as a potential new reference in
	 *            this.earComponent
	 * 
	 */
	private void addPotentialNewReference(IVirtualComponent archive) {
		// check to see if a reference to the given archive already exists
		IVirtualReference[] existingRefs = rootComponent.getReferences();
		IVirtualComponent referencedComponent;
		boolean refAlreadyExists = false;
		for (int i = 0; i < existingRefs.length && !refAlreadyExists; i++) {
			referencedComponent = existingRefs[i].getReferencedComponent();
			refAlreadyExists = referencedComponent.equals(archive);
		}

		// verify it's not already in the list... ugh
		TableItem[] allItems = availableComponentsViewer.getTable().getItems();
		for (int i = 0; i < allItems.length; i++) {
			if (allItems[i].getData().equals(archive)) {
				allItems[i].setChecked(true);
				refAlreadyExists = true;
			}
		}

		// only add the archive as a potentialy new reference 
		// if it does not already exist
		if (!refAlreadyExists) {
			String name = new Path(archive.getName()).lastSegment();
			if( archive instanceof VirtualArchiveComponent && 
					((VirtualArchiveComponent)archive).getArchiveType()
						.equals(VirtualArchiveComponent.VARARCHIVETYPE)) {
				File f = ((VirtualArchiveComponent)archive).getUnderlyingDiskFile();
				if( f != null )
					name = new Path(f.getAbsolutePath()).lastSegment();
			}
			this.objectToRuntimePath.put(archive, new Path("/").append(name).toString()); //$NON-NLS-1$
			availableComponentsViewer.add(archive);
		} else {
			// TODO should inform user that they selected an already referenced
			// archive?
		}
	}

	private void handleSelectExternalJarButton() {
		IPath[] selected = BuildPathDialogAccess
				.chooseExternalJAREntries(propPage.getShell());

		if (selected != null) {
			for (int i = 0; i < selected.length; i++) {

				String type = VirtualArchiveComponent.LIBARCHIVETYPE
						+ IPath.SEPARATOR;
				IVirtualComponent archive = ComponentCore
						.createArchiveComponent(rootComponent.getProject(),
								type + selected[i].toString());

				this.addPotentialNewReference(archive);
			}
			refresh();
		}

	}

	private void handleSelectVariableButton() {
		IPath existingPath[] = new Path[0];
		IPath[] paths = BuildPathDialogAccess.chooseVariableEntries(propPage
				.getShell(), existingPath);

		if (paths != null) {
			refresh();
			for (int i = 0; i < paths.length; i++) {
				IPath resolvedPath = JavaCore.getResolvedVariablePath(paths[i]);

				java.io.File file = new java.io.File(resolvedPath.toOSString());
				if (file.isFile() && file.exists()) {
					String type = VirtualArchiveComponent.VARARCHIVETYPE
							+ IPath.SEPARATOR;

					IVirtualComponent archive = ComponentCore
							.createArchiveComponent(rootComponent.getProject(),
									type + paths[i].toString());

					this.addPotentialNewReference(archive);
				} else {
					// display error
				}
			}
			refresh();
		}
	}

	protected void handleRemoveSelectedButton() {
		ISelection sel = availableComponentsViewer.getSelection();
		if( sel instanceof IStructuredSelection ) {
			Object o = ((IStructuredSelection)sel).getFirstElement();
			objectToRuntimePath.remove(o);
			refresh();
		}
	}

	public TableViewer createAvailableComponentsViewer(Composite parent) {
		int flags = SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI;

		Table table = new Table(parent, flags);
		availableComponentsViewer = new TableViewer(table);

		// set up table layout
		TableLayout tableLayout = new org.eclipse.jface.viewers.TableLayout();
		tableLayout.addColumnData(new ColumnWeightData(400, true));
		tableLayout.addColumnData(new ColumnWeightData(500, true));
		table.setLayout(tableLayout);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		availableComponentsViewer.setSorter(null);

		TableColumn bndColumn = new TableColumn(table, SWT.NONE, 0);
		bndColumn.setText(Messages.AddModuleDependenciesPropertiesPage_DeployPathColumn);
		bndColumn.setResizable(true);

		TableColumn projectColumn = new TableColumn(table, SWT.NONE, 1);
		projectColumn.setText(Messages.AddModuleDependenciesPropertiesPage_SourceColumn);
		projectColumn.setResizable(true);

		tableLayout.layout(table, true);
		return availableComponentsViewer;

	}

	protected boolean isPhysicallyAdded(VirtualArchiveComponent component) {
		IPath p = null;
		try {
			p = component.getProjectRelativePath();
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	private void handleSelectProjectJarButton() {
		IPath[] selected = BuildPathDialogAccess.chooseJAREntries(propPage
				.getShell(), project.getLocation(), new IPath[0]);

		if (selected != null) {
			for (int i = 0; i < selected.length; i++) {
				// IPath fullPath = project.getFile(selected[i]).getFullPath();
				String type = VirtualArchiveComponent.LIBARCHIVETYPE
						+ IPath.SEPARATOR;
				IVirtualComponent archive = ComponentCore
						.createArchiveComponent(rootComponent.getProject(),
								type + selected[i].makeRelative().toString());

				this.addPotentialNewReference(archive);
			}
			refresh();
		}

	}

	/**
	 * This should only be called on changes, such as adding a project
	 * reference, adding a lib reference etc.
	 * 
	 * It will reset the input, manually re-add missing elements, and do other
	 * tasks
	 */
	public void refresh() {
		resetTableUI();
		if (!hasInitialized) {
			initialize();
			resetTableUI();
		}

	}

	protected void resetTableUI() {
		IWorkspaceRoot input = ResourcesPlugin.getWorkspace().getRoot();
		availableComponentsViewer.setInput(input);
		GridData data = new GridData(GridData.FILL_BOTH);
		int numlines = Math.min(10, availableComponentsViewer.getTable()
				.getItemCount());
		data.heightHint = availableComponentsViewer.getTable().getItemHeight()
				* numlines;
		availableComponentsViewer.getTable().setLayoutData(data);
		GridData btndata = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING);
		buttonColumn.setLayoutData(btndata);
	}

	protected void initialize() {
		IVirtualReference[] refs = rootComponent.getReferences();
		IVirtualComponent comp;
		for( int i = 0; i < refs.length; i++ ) { 
			comp = refs[i].getReferencedComponent();
			String archiveName = refs[i].getArchiveName();
			String val = refs[i].getRuntimePath().append(refs[i].getArchiveName()).toString();
			objectToRuntimePath.put(comp, val);
			oldComponentToRuntimePath.put((IVirtualComponent) comp, val);
		}
		hasInitialized = true;
	}

	/*
	 * Clean-up methods are below. These include performCancel, performDefaults,
	 * performOK, and any other methods that are called *only* by this one.
	 */
	public void setVisible(boolean visible) {
	}

	public void performDefaults() {
	}

	public boolean performCancel() {
		return true;
	}

	public void dispose() {
		Table table = null;
		if (availableComponentsViewer != null) {
			table = availableComponentsViewer.getTable();
			if (table == null)
				return;
		}
		table.removeListener(SWT.Dispose, tableListener);
		table.removeListener(SWT.KeyDown, tableListener);
		table.removeListener(SWT.MouseMove, tableListener);
		table.removeListener(SWT.MouseHover, tableListener);
	}

	
	
	/*
	 * This is where the OK work goes. Lots of it. Watch your head.
	 * xiao xin
	 */
	protected boolean preHandleChanges(IProgressMonitor monitor) {
		return true;
	}

	protected boolean postHandleChanges(IProgressMonitor monitor) {
		return true;
	}

	public boolean performOk() {
		// grab what's checked
		ArrayList<Object> checked = new ArrayList<Object>();
		TableItem[] items = availableComponentsViewer.getTable().getItems();
		for (int i = 0; i < items.length; i++)
			checked.add(items[i].getData());

		// Fill our delta lists
		ArrayList<Object> added = new ArrayList<Object>();
		ArrayList<IVirtualComponent> removed = new ArrayList<IVirtualComponent>();
		ArrayList<IVirtualComponent> changed = new ArrayList<IVirtualComponent>();

		Iterator j = oldComponentToRuntimePath.keySet().iterator();
		Object key, val;
		while (j.hasNext()) {
			key = j.next();
			val = oldComponentToRuntimePath.get(key);
			if( !objectToRuntimePath.containsKey(key))
				removed.add((IVirtualComponent)key);
			else if (!val.equals(objectToRuntimePath.get(key)))
				changed.add((IVirtualComponent)key);
		}

		j = objectToRuntimePath.keySet().iterator();
		while (j.hasNext()) {
			key = j.next();
			if (!oldComponentToRuntimePath.containsKey(key))
				added.add(key);
		}

		NullProgressMonitor monitor = new NullProgressMonitor();
		boolean subResult = preHandleChanges(monitor);
		if( !subResult )
			return false;
		
		handleDeltas(removed, changed, added);
		subResult &= postHandleChanges(monitor);
		
		// Now update the variables
		oldComponentToRuntimePath.clear();
		ArrayList keys = new ArrayList();
		keys.addAll(objectToRuntimePath.keySet());
		Iterator i = keys.iterator();
		while(i.hasNext()) {
			Object component = i.next();
			IVirtualComponent vc = component instanceof IVirtualComponent ? (IVirtualComponent)component : null;
			String path = objectToRuntimePath.get(component);
			if( component instanceof IProject ) {
				objectToRuntimePath.remove(component);
				vc = ComponentCore.createComponent((IProject)component);
				objectToRuntimePath.put(vc, path);
			}
			oldComponentToRuntimePath.put(vc, path);
		}
		return subResult;
	}
	
	// Subclass can override if it has a good way to handle changed elements
	protected void handleDeltas(ArrayList<IVirtualComponent> removed, 
			ArrayList<IVirtualComponent> changed, ArrayList<Object> added) {
		ArrayList<IVirtualComponent> removed2 = new ArrayList<IVirtualComponent>();
		ArrayList<Object> added2 = new ArrayList<Object>();
		removed2.addAll(removed);
		removed2.addAll(changed);
		added2.addAll(added);
		added2.addAll(changed);

		// meld the changed into the added / removed
		handleRemoved(removed2);
		handleAdded(added2);
	}	
	protected void handleRemoved(ArrayList<IVirtualComponent> removed) {
		// If it's removed it should *only* be a virtual component already
		Iterator<IVirtualComponent> i = removed.iterator();
		IVirtualComponent component;
		while(i.hasNext()) {
			try {
				component = i.next();
				IDataModelOperation operation = getRemoveComponentOperation(component);
				operation.execute(null, null);
			} catch( ExecutionException e) {
				J2EEUIPlugin.logError(e);
			}
		}
	}
	
	protected IDataModelOperation getRemoveComponentOperation(IVirtualComponent component) {
		String path, archiveName;
		path = new Path(oldComponentToRuntimePath.get(component)).removeLastSegments(1).toString();
		archiveName = new Path(oldComponentToRuntimePath.get(component)).lastSegment(); 

		IDataModelProvider provider = getRemoveReferenceDataModelProvider(component);
		IDataModel model = DataModelFactory.createDataModel(provider);
		model.setProperty(ICreateReferenceComponentsDataModelProperties.SOURCE_COMPONENT, rootComponent);
		List modHandlesList = (List) model.getProperty(ICreateReferenceComponentsDataModelProperties.TARGET_COMPONENT_LIST);
		modHandlesList.add(component);
		model.setProperty(ICreateReferenceComponentsDataModelProperties.TARGET_COMPONENT_LIST, modHandlesList);
        model.setProperty(ICreateReferenceComponentsDataModelProperties.TARGET_COMPONENTS_DEPLOY_PATH, path);
		Map uriMap = new HashMap();
		uriMap.put(component, archiveName);
		model.setProperty(ICreateReferenceComponentsDataModelProperties.TARGET_COMPONENTS_TO_URI_MAP, uriMap);
		return model.getDefaultOperation();
	}
	
	protected IDataModelProvider getRemoveReferenceDataModelProvider(IVirtualComponent component) {
		return new RemoveReferenceComponentsDataModelProvider();
	}

	
	protected void handleChanged(ArrayList<IVirtualComponent> changed) {
		Iterator<IVirtualComponent> i = changed.iterator(); 
		IVirtualComponent component;
		IVirtualReference ref;
		IPath p;
		while(i.hasNext()) {
			component = i.next();
			ref = rootComponent.getReference(component.getName());
			p = new Path(objectToRuntimePath.get(component));
			ref.setRuntimePath(p);
		}
	}
	
	protected void handleAdded(ArrayList<Object> added) {
		final ArrayList<IProject> projects = new ArrayList<IProject>();
		final ArrayList<IVirtualComponent> components = new ArrayList<IVirtualComponent>();
		Iterator i = added.iterator();
		Object o;
		while(i.hasNext()) {
			o = i.next();
			if( o instanceof IProject && !ModuleCoreNature.isFlexibleProject((IProject)o)) 
				projects.add((IProject)o);
			else if( o instanceof IProject )
				components.add(ComponentCore.createComponent(((IProject)o)));
			else if( o instanceof IVirtualComponent) 
				components.add((IVirtualComponent)o);
		}
		
		IWorkspaceRunnable runnable = new IWorkspaceRunnable(){
			public void run(IProgressMonitor monitor) throws CoreException{
				addProjects(projects);
				addComponents(components);
			}
		};
		try {
			J2EEUIPlugin.getWorkspace().run(runnable, new NullProgressMonitor());
		} catch( CoreException e ) {
			J2EEUIPlugin.logError(e);
		}
	}

	protected void addProjects(ArrayList<IProject> projects) throws CoreException {
		// These projects need to be converted to  utility projects first
		Iterator<IProject> i = projects.iterator();
		IProject proj;
		Set moduleProjects = new HashSet();
		String path, archiveName;
		while(i.hasNext()) {
			proj = i.next();
			path = new Path(objectToRuntimePath.get(proj)).removeLastSegments(1).toString();
			archiveName = new Path(objectToRuntimePath.get(proj)).lastSegment(); 
			try {
				moduleProjects.add(proj);
				IDataModel migrationdm = DataModelFactory.createDataModel(new JavaProjectMigrationDataModelProvider());
				migrationdm.setProperty(IJavaProjectMigrationDataModelProperties.PROJECT_NAME, proj.getName());
				migrationdm.getDefaultOperation().execute(new NullProgressMonitor(), null);


				IDataModel refdm = DataModelFactory.createDataModel(new CreateReferenceComponentsDataModelProvider());
				List targetCompList = (List) refdm.getProperty(ICreateReferenceComponentsDataModelProperties.TARGET_COMPONENT_LIST);

				IVirtualComponent targetcomponent = ComponentCore.createComponent(proj);
				targetCompList.add(targetcomponent);

				refdm.setProperty(ICreateReferenceComponentsDataModelProperties.SOURCE_COMPONENT, rootComponent);
				refdm.setProperty(ICreateReferenceComponentsDataModelProperties.TARGET_COMPONENT_LIST, targetCompList);
				refdm.setProperty(ICreateReferenceComponentsDataModelProperties.TARGET_COMPONENTS_DEPLOY_PATH, path);
				
				Map uriMap = new HashMap();
				uriMap.put(targetcomponent, archiveName);
				refdm.setProperty(ICreateReferenceComponentsDataModelProperties.TARGET_COMPONENTS_TO_URI_MAP, uriMap);

				refdm.getDefaultOperation().execute(new NullProgressMonitor(), null);
			} catch (ExecutionException e) {
				J2EEUIPlugin.logError(e);
			}
		}
		postAddProjects(moduleProjects);
	}
	
	protected void postAddProjects(Set moduleProjects) throws CoreException {
	}
	
	protected void addComponents(ArrayList<IVirtualComponent> components) throws CoreException {
		Iterator<IVirtualComponent> i = components.iterator();
		while(i.hasNext()) {
			addOneComponent(i.next());
		}
	}
	
	protected IDataModelProvider getAddReferenceDataModelProvider(IVirtualComponent component) {
		return new CreateReferenceComponentsDataModelProvider();
	}
	
	protected void addOneComponent(IVirtualComponent component) throws CoreException {
		String path, archiveName;
		path = new Path(objectToRuntimePath.get(component)).removeLastSegments(1).toString();
		archiveName = new Path(objectToRuntimePath.get(component)).lastSegment();

		IDataModelProvider provider = getAddReferenceDataModelProvider(component);
		IDataModel dm = DataModelFactory.createDataModel(provider);
		
		dm.setProperty(ICreateReferenceComponentsDataModelProperties.SOURCE_COMPONENT, rootComponent);
		dm.setProperty(ICreateReferenceComponentsDataModelProperties.TARGET_COMPONENT_LIST, Arrays.asList(component));
		
		//[Bug 238264] the uri map needs to be manually set correctly
		Map uriMap = new HashMap();
		uriMap.put(component, archiveName);
		dm.setProperty(ICreateReferenceComponentsDataModelProperties.TARGET_COMPONENTS_TO_URI_MAP, uriMap);
        dm.setProperty(ICreateReferenceComponentsDataModelProperties.TARGET_COMPONENTS_DEPLOY_PATH, path);

		IStatus stat = dm.validateProperty(ICreateReferenceComponentsDataModelProperties.TARGET_COMPONENT_LIST);
		if (stat != OK_STATUS)
			throw new CoreException(stat);
		try {
			dm.getDefaultOperation().execute(new NullProgressMonitor(), null);
		} catch (ExecutionException e) {
			J2EEUIPlugin.logError(e);
		}	
	}
	/**
	 * Method returns the name of the given IVirtualComponent being sure the correct extension
	 * is on the end of the name, this is important for internal projects. Added for [Bug 241509]
	 * 
	 * @param virtComp the IVirtualComponent to get the name of with the correct extension
	 * @return the name of the given IVirtualComponent with the correct extension
	 */
	protected String getVirtualComponentNameWithExtension(IVirtualComponent virtComp) {
		String virtCompURIMapName = this.getURIMappingName(virtComp);
		
		boolean linkedToEAR = true;
		try {
			if(virtComp.isBinary()){
				linkedToEAR = ((J2EEModuleVirtualArchiveComponent)virtComp).isLinkedToEAR();
				((J2EEModuleVirtualArchiveComponent)virtComp).setLinkedToEAR(false);
			}
			if(JavaEEProjectUtilities.isDynamicWebComponent(virtComp)) {
				if(!virtCompURIMapName.endsWith(IJ2EEModuleConstants.WAR_EXT)) {
					//web module URIs need to end in WAR
					virtCompURIMapName += IJ2EEModuleConstants.WAR_EXT;
				}
			} else if(JavaEEProjectUtilities.isJCAComponent(virtComp)) {
				if(!virtCompURIMapName.endsWith(IJ2EEModuleConstants.RAR_EXT)) {
					//connector module URIs need to end in RAR
					virtCompURIMapName += IJ2EEModuleConstants.RAR_EXT;
				}
			} else if(!virtCompURIMapName.endsWith(IJ2EEModuleConstants.JAR_EXT)) {
				//all other modules (EJB, AppClient, Utility) need to end in JAR
				virtCompURIMapName += IJ2EEModuleConstants.JAR_EXT;
			}
		} finally {
			if(virtComp.isBinary()){
				((J2EEModuleVirtualArchiveComponent)virtComp).setLinkedToEAR(linkedToEAR);
			}
		}
		return virtCompURIMapName;
	}
	
	/**
	 * [Bug 238264]
	 * determines a unique URI mapping name for a given component
	 * this is in case two components have the same name.
	 * 
	 * @return returns a valid (none duplicate) uri mapping name for the given component\
	 */
	private String getURIMappingName(IVirtualComponent archive) {
		
		//get the default uri map name for the given archive
		IPath componentPath = Path.fromOSString(archive.getName());
		String uriMapName = componentPath.lastSegment().replace(' ', '_');
		
		
		//check to be sure this uri mapping is not already in use by another reference
		boolean dupeArchiveName;
		String refedCompName;
		int lastDotIndex;
		String increment;
		IVirtualReference [] existingRefs = rootComponent.getReferences();
		for(int i=0;i<existingRefs.length;i++){
			refedCompName = existingRefs[i].getReferencedComponent().getName();
			
			//if uri mapping names of the refed component and the given archive are the same
			//  find a new uri map name for the given archive
			if(existingRefs[i].getArchiveName().equals(uriMapName)){
				dupeArchiveName = true;
				//find a new uriMapName for the given component
				for(int j=1; dupeArchiveName; j++){
					lastDotIndex = uriMapName.lastIndexOf('.');
					increment = "_"+j; //$NON-NLS-1$
					
					//create the new potential name
					if(lastDotIndex != -1){
						uriMapName = uriMapName.substring(0, lastDotIndex) + increment + uriMapName.substring(lastDotIndex);
					} else {
						uriMapName = uriMapName.substring(0)+increment;
					}
					
					//determine if the new potential name is valid
					for(int k=0; k<existingRefs.length; k++) {
						dupeArchiveName = existingRefs[k].getArchiveName().equals(uriMapName);
						if(dupeArchiveName) {
							break;
						}
					}
				}
			}
		}
		
		return uriMapName;
	}
}
