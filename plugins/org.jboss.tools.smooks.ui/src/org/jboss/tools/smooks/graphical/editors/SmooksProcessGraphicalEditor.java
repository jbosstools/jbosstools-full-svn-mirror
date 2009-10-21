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
package org.jboss.tools.smooks.graphical.editors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.ScrolledPageBook;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.jboss.tools.smooks.configuration.SmooksConstants;
import org.jboss.tools.smooks.editor.AbstractSmooksFormEditor;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.editor.ISourceSynchronizeListener;
import org.jboss.tools.smooks.graphical.actions.AbstractProcessGraphAction;
import org.jboss.tools.smooks.graphical.actions.AddNextTaskNodeAction;
import org.jboss.tools.smooks.graphical.actions.AddPreviousTaskNodeAction;
import org.jboss.tools.smooks.graphical.actions.AddTaskNodeAction;
import org.jboss.tools.smooks.graphical.actions.DeleteTaskNodeAction;
import org.jboss.tools.smooks.model.graphics.ext.GraphFactory;
import org.jboss.tools.smooks.model.graphics.ext.GraphPackage;
import org.jboss.tools.smooks.model.graphics.ext.ISmooksGraphChangeListener;
import org.jboss.tools.smooks.model.graphics.ext.ProcessType;
import org.jboss.tools.smooks.model.graphics.ext.ProcessesType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.graphics.ext.TaskType;
import org.jboss.tools.smooks.model.smooks.DocumentRoot;

/**
 * @author Dart
 * 
 */
public class SmooksProcessGraphicalEditor extends FormPage implements ISelectionChangedListener,
		ISourceSynchronizeListener, ISmooksGraphChangeListener, IPropertyListener {

	private List<IAction> processPanelActions = new ArrayList<IAction>();

	public static final int EXECUTE_COMMAND = 0;

	public static final int REDO_COMMAND = 1;

	public static final int UNDO_COMMAND = 2;

	private Object emptyKey = new Object();

	private ISmooksModelProvider smooksModelProvider = null;

	private GraphViewer processGraphViewer;

	private ScrolledPageBook pageBook;

	private Map<String, Object> registedTaskPages = new HashMap<String, Object>();

	public SmooksProcessGraphicalEditor(FormEditor editor, String id, String title, ISmooksModelProvider provider) {
		super(editor, id, title);
		this.smooksModelProvider = provider;
	}

	public SmooksProcessGraphicalEditor(String id, String title, ISmooksModelProvider provider) {
		super(id, title);
		this.smooksModelProvider = provider;
	}

	protected void createProcessGraphicalPanel(Composite parent) {
		processGraphViewer = new GraphViewer(parent, SWT.NONE);
		processGraphViewer.setContentProvider(new ProcessGraphContentProvider());

		processGraphViewer.setLabelProvider(new LabelProvider() {

			@Override
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return super.getImage(element);
			}

			@Override
			public String getText(Object element) {
				if (element instanceof TaskType) {
					String id = ((TaskType) element).getId();
					String name = ((TaskType) element).getName();
					if (name == null) {
						name = id;
					}
					if (name == null) {
						name = "null";
					}
					return name;
				}
				return "";
			}

		});
		processGraphViewer.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
		HorizontalTreeLayoutAlgorithm layoutAlgorithm = new HorizontalTreeLayoutAlgorithm(
				LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		processGraphViewer.setLayoutAlgorithm(layoutAlgorithm, true);
		if (parent instanceof Section) {
			((Section) parent).setClient(processGraphViewer.getControl());
		}
	}

	protected void initProcessGraphicalViewer() {
		SmooksGraphicsExtType ext = this.smooksModelProvider.getSmooksGraphicsExt();
		ProcessesType processes = ext.getProcesses();
		if (processes == null) {
			processes = GraphFactory.eINSTANCE.createProcessesType();
			ext.setProcesses(processes);
		}
		ProcessType process = null;
		if (processes != null) {
			process = processes.getProcess();
		}

		if (process == null) {
			process = GraphFactory.eINSTANCE.createProcessType();
			processes.setProcess(process);
		}

		if (process != null) {
			getProcessGraphViewer().setInput(process);
		}
	}

	protected void configProcessGraphicalViewer() {
		MenuManager manager = new MenuManager();

		initProcessGraphicalPanelActions(manager);

		Menu menu = manager.createContextMenu(getProcessGraphViewer().getControl());
		getProcessGraphViewer().getControl().setMenu(menu);
		manager.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				for (Iterator<?> iterator = processPanelActions.iterator(); iterator.hasNext();) {
					IAction a = (IAction) iterator.next();
					if (a instanceof AbstractProcessGraphAction) {
						((AbstractProcessGraphAction) a).selectionChanged(new SelectionChangedEvent(processGraphViewer,
								processGraphViewer.getSelection()));
					}
				}
				manager.update();
			}
		});
	}

	protected void initProcessGraphicalPanelActions(IMenuManager manager) {

		AddTaskNodeAction addInputTaskAction = new AddTaskNodeAction(SmooksConstants.TASK_ID_INPUT, "Add Input Task",
				smooksModelProvider) {

			@Override
			public void run() {
				if (this.provider != null) {
					SmooksGraphicsExtType graph = provider.getSmooksGraphicsExt();
					ProcessType process = graph.getProcesses().getProcess();
					if (process != null && process.getTask().isEmpty()) {
						TaskType childTask = GraphFactory.eINSTANCE.createTaskType();
						childTask.setId(taskID);
						childTask.setName("Input Task");
						Command command = AddCommand.create(provider.getEditingDomain(), process,
								GraphPackage.Literals.PROCESS_TYPE__TASK, childTask);
						provider.getEditingDomain().getCommandStack().execute(command);
					}
				}
			}

			@Override
			public void update() {
				this.setEnabled(false);
				SmooksGraphicsExtType graph = smooksModelProvider.getSmooksGraphicsExt();
				ProcessType process = graph.getProcesses().getProcess();
				if (process != null && process.getTask().isEmpty()) {
					this.setEnabled(true);
				}
			}

		};
		manager.add(addInputTaskAction);
		processPanelActions.add(addInputTaskAction);

		MenuManager addNextTaskMenuManager = new MenuManager("Add Next Task");
		manager.add(addNextTaskMenuManager);

		AddNextTaskNodeAction addNextInputAction = new AddNextTaskNodeAction(SmooksConstants.TASK_ID_INPUT, "Input",
				smooksModelProvider);
		this.processPanelActions.add(addNextInputAction);
		addNextTaskMenuManager.add(addNextInputAction);

		AddNextTaskNodeAction addNextJavaMappingAction = new AddNextTaskNodeAction(
				SmooksConstants.TASK_ID_JAVA_MAPPING, "Java Mapping", smooksModelProvider);
		this.processPanelActions.add(addNextJavaMappingAction);
		addNextTaskMenuManager.add(addNextJavaMappingAction);

		MenuManager addPreTaskMenuManager = new MenuManager("Add Previous Task");
		manager.add(addPreTaskMenuManager);

		AddPreviousTaskNodeAction addPreInputAction = new AddPreviousTaskNodeAction(SmooksConstants.TASK_ID_INPUT,
				"Input", smooksModelProvider);
		this.processPanelActions.add(addPreInputAction);
		addPreTaskMenuManager.add(addPreInputAction);

		AddPreviousTaskNodeAction addPreJavaMappingAction = new AddPreviousTaskNodeAction(
				SmooksConstants.TASK_ID_JAVA_MAPPING, "Java Mapping", smooksModelProvider);
		this.processPanelActions.add(addPreJavaMappingAction);
		addPreTaskMenuManager.add(addPreJavaMappingAction);

		DeleteTaskNodeAction deleteAction = new DeleteTaskNodeAction(smooksModelProvider);
		manager.add(deleteAction);

		this.processPanelActions.add(deleteAction);

		getProcessGraphViewer().addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				Object firstElement = selection.getFirstElement();
				showTaskControl(firstElement);
			}
		});
	}

	public void registeTaskDetailsPage(IEditorPart editor, String taskID) {
		editor.addPropertyListener(this);
		this.registedTaskPages.put(taskID, editor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.editor.FormPage#createPartControl(org.eclipse.swt
	 * .widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		super.createPartControl(parent);
	}

	public GraphViewer getProcessGraphViewer() {
		return processGraphViewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui
	 * .forms.IManagedForm)
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		toolkit.decorateFormHeading(form.getForm());
		form.getBody().setLayout(new FillLayout());
		form.setText("Process");

		Composite mainComposite = toolkit.createComposite(form.getBody());

		GridLayout mgl = new GridLayout();
		mgl.marginHeight = 13;
		mgl.horizontalSpacing = 20;
		mainComposite.setLayout(mgl);

		SashForm sashForm = new SashForm(mainComposite, SWT.VERTICAL);
		sashForm.SASH_WIDTH = 1;
		GridData gd = new GridData(GridData.FILL_BOTH);
		sashForm.setLayoutData(gd);

		Section processGraphSection = toolkit.createSection(sashForm, Section.DESCRIPTION | Section.TITLE_BAR);
		processGraphSection.setText("Process Map");
		processGraphSection.setDescription("Right-Click to open the PopMenu to add or remove task node");

		Composite processGraphComposite = toolkit.createComposite(processGraphSection);

		FillLayout processGraphFillLayout = new FillLayout();
		processGraphFillLayout.marginWidth = 1;
		processGraphFillLayout.marginHeight = 1;
		processGraphComposite.setLayout(processGraphFillLayout);

		processGraphComposite.setBackground(toolkit.getColors().getBorderColor());

		processGraphSection.setClient(processGraphComposite);

		createProcessGraphicalPanel(processGraphComposite);
		configProcessGraphicalViewer();
		initProcessGraphicalViewer();

		Composite taskDetailsComposite = toolkit.createComposite(sashForm);
		FillLayout taskDetailsFillLayout = new FillLayout();
		taskDetailsFillLayout.marginWidth = 0;
		taskDetailsFillLayout.marginHeight = 5;
		taskDetailsComposite.setLayout(taskDetailsFillLayout);

		Section section = toolkit.createSection(taskDetailsComposite, Section.DESCRIPTION | Section.TITLE_BAR);
		section.setText("Task Configuration");
		section.setDescription("Configurate the selected task");
		pageBook = new ScrolledPageBook(section);
		pageBook.setBackground(toolkit.getColors().getBackground());
		section.setClient(pageBook);

		sashForm.setWeights(new int[] { 2, 8 });

		Composite emptyComposite = pageBook.createPage(emptyKey);
		emptyComposite.setLayout(new FillLayout());
		createEmptyTaskPanel(emptyComposite, toolkit);
		pageBook.showPage(emptyKey);
	}

	private void handleCommandStack(org.eclipse.emf.common.command.CommandStack commandStack) {
		commandStack.addCommandStackListener(new org.eclipse.emf.common.command.CommandStackListener() {
			public void commandStackChanged(EventObject event) {
				final Command mostRecentCommand = ((org.eclipse.emf.common.command.CommandStack) event.getSource())
						.getMostRecentCommand();
				getEditorSite().getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						if (mostRecentCommand != null) {
							if (getProcessGraphViewer() != null) {
								getProcessGraphViewer().refresh();
								getProcessGraphViewer().applyLayout();
							}
						}
					}

				});
			}
		});
	}

	public SmooksGraphicsExtType getSmooksGraphicsExtType() {
		if (smooksModelProvider != null) {
			return smooksModelProvider.getSmooksGraphicsExt();
		}
		return null;
	}

	/**
	 * @return the smooksModelProvider
	 */
	public ISmooksModelProvider getSmooksModelProvider() {
		return smooksModelProvider;
	}

	/**
	 * @param smooksModelProvider
	 *            the smooksModelProvider to set
	 */
	public void setSmooksModelProvider(ISmooksModelProvider smooksModelProvider) {
		this.smooksModelProvider = smooksModelProvider;
	}

	public EObject getSmooksResourceList() {
		if (smooksModelProvider != null) {
			EObject m = null;
			EObject smooksModel = smooksModelProvider.getSmooksModel();
			if (smooksModel instanceof org.jboss.tools.smooks10.model.smooks.DocumentRoot) {
				m = ((org.jboss.tools.smooks10.model.smooks.DocumentRoot) smooksModel).getSmooksResourceList();
			}
			if (smooksModel instanceof DocumentRoot) {
				m = ((DocumentRoot) smooksModel).getSmooksResourceList();
			}
			return m;
		}
		return null;
	}

	public EditingDomain getEditingDomain() {
		if (smooksModelProvider != null) {
			return smooksModelProvider.getEditingDomain();
		}
		return null;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
		if (smooksModelProvider != null) {
			this.handleCommandStack(smooksModelProvider.getEditingDomain().getCommandStack());
		}
		SmooksGraphicalEditorPart javaMappingPart = new SmooksGraphicalEditorPart(smooksModelProvider);
		this.registeTaskDetailsPage(javaMappingPart, SmooksConstants.TASK_ID_JAVA_MAPPING);
	}

	@Override
	public Object getAdapter(Class type) {
		if (type == ISmooksModelProvider.class) {
			return this.smooksModelProvider;
		}
		return super.getAdapter(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.FormPage#isDirty()
	 */
	@Override
	public boolean isDirty() {
		boolean dirty = false;
		Collection<Object> editors = registedTaskPages.values();
		for (Iterator<?> iterator = editors.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof IEditorPart) {
				dirty = (((IEditorPart) object).isDirty() || dirty);
			}
		}
		return dirty;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		Collection<Object> editors = registedTaskPages.values();
		for (Iterator<?> iterator = editors.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof IEditorPart) {
				((IEditorPart) object).doSave(monitor);
			}
		}
		firePropertyChange(PROP_DIRTY);
	}

	public void selectionChanged(SelectionChangedEvent event) {
	}

	protected Control createTaskPanel(Composite parent, FormToolkit toolkit, String taskID) {

		if (taskID == null)
			return null;

		if (taskID.equals(SmooksConstants.TASK_ID_JAVA_MAPPING)) {
			return null;
		}

		if (taskID.equals(SmooksConstants.TASK_ID_INPUT)) {
			GridLayout gl = new GridLayout();
			gl.numColumns = 2;
			parent.setLayout(gl);
			toolkit.createLabel(parent,
					"Click the link to switch to the \"Input\" tab to configurate the Smooks Input : ");
			Hyperlink link = toolkit.createHyperlink(parent, "Go to the Input page", SWT.NONE);
			link.addHyperlinkListener(new IHyperlinkListener() {

				public void linkExited(HyperlinkEvent e) {

				}

				public void linkEntered(HyperlinkEvent e) {

				}

				public void linkActivated(HyperlinkEvent e) {
					if (smooksModelProvider instanceof AbstractSmooksFormEditor) {
						((AbstractSmooksFormEditor) smooksModelProvider).setActivePage("reader_page");
					}
				}
			});

			return parent;
		}

		return null;
	}

	protected void showTaskControl(Object model) {
		FormToolkit toolkit = ((AbstractSmooksFormEditor) this.smooksModelProvider).getToolkit();
		if (model instanceof TaskType) {
			String id = ((TaskType) model).getId();
			if (id != null) {
				if (!pageBook.hasPage(id)) {
					Composite parent = pageBook.createPage(id);

					Object page = getRegisteTaskPage(id);
					if (page != null && page instanceof IEditorPart) {
						try {
							parent.setLayout(new FillLayout());
							createTaskPage((IEditorPart) page, parent);
							pageBook.showPage(id);
						} catch (PartInitException e) {
							pageBook.removePage(id);
							pageBook.showPage(emptyKey);
						}
					} else {
						Control control = createTaskPanel(parent, toolkit, id);
						if (control != null) {
							pageBook.showPage(id);
						} else {
							pageBook.removePage(id);
							pageBook.showPage(emptyKey);
						}
					}
				} else {
					pageBook.showPage(id);
				}
			}
		} else {
			// pageBook.showEmptyPage();
		}
	}

	protected IEditorSite createSite(IEditorPart editor) {
		return new SmooksTaskDetailsEditorSite(this.getEditor(), editor, this);
	}

	protected void createTaskPage(IEditorPart editorPart, Composite parent) throws PartInitException {
		IEditorSite site = createSite(editorPart);
		editorPart.init(site, getEditorInput());
		editorPart.createPartControl(parent);
	}

	public Object getRegisteTaskPage(String id) {
		return registedTaskPages.get(id);
	}

	protected Control createEmptyTaskPanel(Composite parent, FormToolkit toolkit) {
		parent.setLayout(new FillLayout());
		return toolkit.createLabel(parent, "Select the task node");
	}

	public void sourceChange(Object model) {
		Collection<Object> editors = registedTaskPages.values();
		for (Iterator<?> iterator = editors.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof ISmooksGraphChangeListener) {
				((ISourceSynchronizeListener) object).sourceChange(model);
			}
		}
	}

	public void graphChanged(SmooksGraphicsExtType extType) {
		Collection<Object> editors = registedTaskPages.values();
		for (Iterator<?> iterator = editors.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof ISmooksGraphChangeListener) {
				((ISmooksGraphChangeListener) object).graphChanged(extType);
			}
		}
	}

	public void graphPropertyChange(EStructuralFeature featre, Object value) {
		Collection<Object> editors = registedTaskPages.values();
		for (Iterator<?> iterator = editors.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof ISmooksGraphChangeListener) {
				((ISmooksGraphChangeListener) object).graphPropertyChange(featre, value);
			}
		}
	}

	public void inputTypeChanged(SmooksGraphicsExtType extType) {
		Collection<Object> editors = registedTaskPages.values();
		for (Iterator<?> iterator = editors.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof ISmooksGraphChangeListener) {
				((ISmooksGraphChangeListener) object).inputTypeChanged(extType);
			}
		}
	}

	public void propertyChanged(Object source, int propId) {
		this.firePropertyChange(propId);
		if (propId == PROP_DIRTY) {
			this.getManagedForm().dirtyStateChanged();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.FormPage#dispose()
	 */
	@Override
	public void dispose() {
		Collection<Object> editors = registedTaskPages.values();
		for (Iterator<?> iterator = editors.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof IEditorPart) {
				((IEditorPart) object).removePropertyListener(this);
				((IEditorPart) object).dispose();
			}
		}
		registedTaskPages.clear();
		registedTaskPages = null;
		super.dispose();
	}
}
