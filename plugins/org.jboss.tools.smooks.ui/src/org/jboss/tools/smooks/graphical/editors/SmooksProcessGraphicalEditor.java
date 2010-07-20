/**
 * 
 */
package org.jboss.tools.smooks.graphical.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.IMessage;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.CGraphNode;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.graphical.actions.AbstractProcessGraphAction;
import org.jboss.tools.smooks.graphical.actions.AddNextTaskNodeAction;
import org.jboss.tools.smooks.graphical.actions.DeleteTaskNodeAction;
import org.jboss.tools.smooks.graphical.editors.Messages;
import org.jboss.tools.smooks.graphical.editors.ProcessGraphContentProvider;
import org.jboss.tools.smooks.graphical.editors.SmooksMessage;
import org.jboss.tools.smooks.graphical.editors.TaskTypeManager;
import org.jboss.tools.smooks.graphical.editors.TaskTypeManager.TaskTypeDescriptor;
import org.jboss.tools.smooks.graphical.editors.process.IProcessProvider;
import org.jboss.tools.smooks.graphical.editors.process.ProcessFactory;
import org.jboss.tools.smooks.graphical.editors.process.ProcessGraphicalViewerLabelProvider;
import org.jboss.tools.smooks.graphical.editors.process.ProcessTaskAnalyzer;
import org.jboss.tools.smooks.graphical.editors.process.ProcessType;
import org.jboss.tools.smooks.graphical.editors.process.TaskNodeFigure;

/**
 * @author Dart
 *
 */
public class SmooksProcessGraphicalEditor extends FormPage implements IProcessProvider , PropertyChangeListener{
	
	private boolean processMapActived = false;

	private int currentMessageType = IMessageProvider.NONE;

	private String currentMessage = null;

	private boolean processChanged = false;

	private boolean lockProcessChangeEvent = false;

	private List<IAction> processPanelActions = new ArrayList<IAction>();

	public static final int EXECUTE_COMMAND = 0;

	public static final int REDO_COMMAND = 1;

	public static final int UNDO_COMMAND = 2;

	private Object emptyKey = new Object();

	private ISmooksModelProvider smooksModelProvider = null;

	private GraphViewer processGraphViewer;

//	private ScrolledPageBook pageBook;

	private Map<String, Object> registedTaskPages = new HashMap<String, Object>();

	private MenuManager manager;

	protected boolean needupdatewhenshow = true;

	private ProcessType process;

	private Map<Object, String> smooksModelIdMap = new HashMap<Object, String>();
	
	private Map<String,Object> detailsControlMap = new HashMap<String, Object>();

	private Composite detailsContentsComposite;
	

	public SmooksProcessGraphicalEditor(FormEditor editor, String id, String title, ISmooksModelProvider provider) {
		super(editor, id, title);
		this.smooksModelProvider = provider;
	}

	public SmooksProcessGraphicalEditor(String id, String title, ISmooksModelProvider provider) {
		super(id, title);
		this.smooksModelProvider = provider;
		this.getManagedForm();
	}

	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		toolkit.decorateFormHeading(form.getForm());
		form.getBody().setLayout(new FillLayout());
		form.setText(Messages.SmooksProcessGraphicalEditor_FormText);

		Composite mainComposite = toolkit.createComposite(form.getBody());

		GridLayout mgl = new GridLayout();
		mgl.marginHeight = 13;
		mgl.horizontalSpacing = 20;
		mainComposite.setLayout(mgl);

		SashForm sashForm = new SashForm(mainComposite, SWT.VERTICAL);
		sashForm.SASH_WIDTH = 1;
		GridData gd = new GridData(GridData.FILL_BOTH);
		sashForm.setLayoutData(gd);

		createProcessGraphicalSection(toolkit, sashForm);

		createTaskDetailsSection(toolkit, sashForm);

		sashForm.setWeights(new int[] { 2, 8 });

//		validateEnd(null);
	}
	
	protected void createTaskDetailsSection(FormToolkit toolkit, Composite parent) {
		Composite taskDetailsComposite = toolkit.createComposite(parent);
		FillLayout taskDetailsFillLayout = new FillLayout();
		taskDetailsFillLayout.marginWidth = 0;
		taskDetailsFillLayout.marginHeight = 5;
		taskDetailsComposite.setLayout(taskDetailsFillLayout);

		Section section = toolkit.createSection(taskDetailsComposite, Section.TITLE_BAR);
		section.setText(Messages.SmooksProcessGraphicalEditor_TaskConfigurationSectionTitle);
		detailsContentsComposite = toolkit.createComposite(section);
		section.setClient(detailsContentsComposite);
//		pageBook = new ScrolledPageBook(section);
		section.setLayout(new FillLayout());
//		pageBook.setBackground(toolkit.getColors().getBackground());
//		section.setClient(pageBook);

//		Composite emptyComposite = pageBook.createPage(emptyKey);
//		emptyComposite.setLayout(new FillLayout());
//		createEmptyTaskPanel(emptyComposite, toolkit);
//		pageBook.showPage(emptyKey);
	}

	
	protected void createProcessGraphicalSection(FormToolkit toolkit, Composite parent) {
		Section processGraphSection = toolkit.createSection(parent, Section.TITLE_BAR);
		processGraphSection.setText(Messages.SmooksProcessGraphicalEditor_TasksMapSectionTitle);

		Composite processGraphComposite = toolkit.createComposite(processGraphSection);

		GridLayout processGraphGridLayoutLayout = new GridLayout();
		// processGraphFillLayout.marginWidth = 1;
		// processGraphFillLayout.marginHeight = 1;
		processGraphComposite.setLayout(processGraphGridLayoutLayout);

		processGraphSection.setClient(processGraphComposite);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);

		// Composite toolBarComposite =
		// toolkit.createComposite(processGraphComposite);
		// FillLayout l = new FillLayout();
		// l.marginHeight = 1;
		// l.marginWidth = 1;
		// toolBarComposite.setLayout(l);

		// toolBarComposite.setLayoutData(gd);
		//
		// toolBarComposite.setBackground(toolkit.getColors().getBorderColor());
		//
		// createProcessToolBar(toolBarComposite, toolkit);

		Composite graphMainComposite = toolkit.createComposite(processGraphComposite);
		FillLayout l1 = new FillLayout();
		l1.marginHeight = 1;
		l1.marginWidth = 1;
		graphMainComposite.setLayout(l1);
		gd = new GridData(GridData.FILL_BOTH);
		graphMainComposite.setLayoutData(gd);
		graphMainComposite.setBackground(toolkit.getColors().getBorderColor());

		createProcessGraphicalPanel(graphMainComposite);

		hookProcessGraphicalViewer();
		configProcessGraphicalViewer();
		initProcessGraphicalViewer();
	}
	
	protected void initProcessGraphicalViewer() {
		boolean disableProcessViewer = false;
		if (process == null) {
			process = ProcessFactory.eINSTANCE.createProcessType();
			process.addPropertyChangeListener(this);
		}
		ProcessTaskAnalyzer analyzer = new ProcessTaskAnalyzer();
		lockProcessChangeEvent = true;
		analyzer.analyzeTaskNode(process, smooksModelProvider.getSmooksModel());
		lockProcessChangeEvent = false;
		if (getProcessGraphViewer() != null) {
			getProcessGraphViewer().setInput(process);
		}
		if (disableProcessViewer) {
			getProcessGraphViewer().getControl().setBackground(
					getManagedForm().getToolkit().getColors().getBorderColor());
			getProcessGraphViewer().setInput(null);
			getProcessGraphViewer().getControl().setEnabled(false);
		}
	}

	public GraphViewer getProcessGraphViewer() {
		return processGraphViewer;
	}

	protected void initProcessGraphicalPanelActions(IMenuManager manager) {

		// AddTaskNodeAction addInputTaskAction = new
		// AddInputTaskAction(smooksModelProvider);
		// manager.add(addInputTaskAction);
		// processPanelActions.add(addInputTaskAction);

		MenuManager addNextTaskMenuManager = new MenuManager(Messages.SmooksProcessGraphicalEditor_AddTaskActionText);
		manager.add(addNextTaskMenuManager);

		generateNextTaskActions(addNextTaskMenuManager);

		// MenuManager addPreTaskMenuManager = new
		// MenuManager("Add Previous Task");
		// manager.add(addPreTaskMenuManager);
		//
		// fillPreTaskMenu(addPreTaskMenuManager);

		DeleteTaskNodeAction deleteAction = new DeleteTaskNodeAction(this, smooksModelProvider, this);
		manager.add(deleteAction);

		this.processPanelActions.add(deleteAction);

	}
	
	private void generateNextTaskActions(MenuManager addNextTaskMenuManager) {
		List<TaskTypeDescriptor> list = TaskTypeManager.getAllTaskList();
		for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
			TaskTypeDescriptor taskTypeDescriptor = (TaskTypeDescriptor) iterator.next();
			AddNextTaskNodeAction addNextInputAction = new AddNextTaskNodeAction(taskTypeDescriptor.getId(),
					taskTypeDescriptor.getLabel(), smooksModelProvider, this);
			this.processPanelActions.add(addNextInputAction);
			addNextTaskMenuManager.add(addNextInputAction);
		}
	}

	
	protected void configProcessGraphicalViewer() {
		manager = new MenuManager();

		initProcessGraphicalPanelActions(manager);

		Menu menu = manager.createContextMenu(getProcessGraphViewer().getControl());
		getProcessGraphViewer().getControl().setMenu(menu);
		manager.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				try {
					manager.removeAll();
					if (needupdatewhenshow) {
						updateProcessActions(processGraphViewer.getSelection());
						fillProcessMenu(manager);
					} else {
						for (Iterator<?> iterator = processPanelActions.iterator(); iterator.hasNext();) {
							IAction action = (IAction) iterator.next();
							if (action.isEnabled() && !(action instanceof DeleteTaskNodeAction)) {
								manager.add(action);
							}
						}
					}
					needupdatewhenshow = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				needupdatewhenshow = true;
			}
		});
	}
	
	public void updateProcessActions(ISelection selection) {
		try{
		for (Iterator<?> iterator = processPanelActions.iterator(); iterator.hasNext();) {
			IAction a = (IAction) iterator.next();
			if (a instanceof AbstractProcessGraphAction) {
				((AbstractProcessGraphAction) a).selectionChanged(new SelectionChangedEvent(processGraphViewer,
						selection));
			}
		}
		manager.update();
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	public void fillProcessMenu(IMenuManager manager) {
		MenuManager addNextTaskMenuManager = new MenuManager(Messages.SmooksProcessGraphicalEditor_AddTaskActionText);
		manager.add(addNextTaskMenuManager);

		for (Iterator<?> iterator = processPanelActions.iterator(); iterator.hasNext();) {
			IAction action = (IAction) iterator.next();
			if (action instanceof DeleteTaskNodeAction) {
				manager.add(action);
			} else {
				addNextTaskMenuManager.add(action);
			}
		}
	}


	
	protected void hookProcessGraphicalViewer() {
		getProcessGraphViewer().addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				Object firstElement = selection.getFirstElement();
				if (firstElement == null) {
					unhighlightGraphNodes();
				}
				Graph graph = getProcessGraphViewer().getGraphControl();
				List<?> nodes = graph.getNodes();
				GraphItem item = null;
				for (Iterator<?> iterator = nodes.iterator(); iterator.hasNext();) {
					GraphItem graphItem = (GraphItem) iterator.next();
					if (graphItem.getData() == firstElement) {
						item = graphItem;
						break;
					}
				}
				if (item != null) {
					unhighlightGraphNodes();
					highlightGraphNode(item);
				}
//				showTaskControl(firstElement);
//				SmooksProcessGraphicalEditor.this.selectionChanged(event);
				updateGlobalActions();
			}
		});
		// when focus change , update the actions in the Eclipse menu via
		// EditorContributor
		getProcessGraphViewer().getControl().addFocusListener(new FocusListener() {

			public void focusLost(FocusEvent e) {
				processMapActived = false;
				updateGlobalActions();
			}

			public void focusGained(FocusEvent e) {
				processMapActived = true;
				updateGlobalActions();
			}
		});
	}
	
	private void updateGlobalActions() {
		IEditorActionBarContributor contributor = getEditorSite().getActionBarContributor();
		if (contributor != null && contributor instanceof MultiPageEditorActionBarContributor) {
			// clean all actions
			((MultiPageEditorActionBarContributor) contributor).setActivePage(null);
			// re-active the page and add all actions
			((MultiPageEditorActionBarContributor) contributor).setActivePage(SmooksProcessGraphicalEditor.this);
		}
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if (lockProcessChangeEvent)
			return;
		String name = evt.getPropertyName();
		Object newtask = evt.getNewValue();
		Object oldtask = evt.getOldValue();
		if (ProcessType.PRO_ADD_CHILD.equals(name) || ProcessType.PRO_REMOVE_CHILD.equals(name)) {
			if (getProcessGraphViewer() != null) {
				getProcessGraphViewer().refresh();
				getProcessGraphViewer().applyLayout();
			}
			processChanged = true;
			getManagedForm().dirtyStateChanged();
		}

//		if (ProcessType.PRO_ADD_CHILD.equals(name)) {
//			this.showTaskControl(newtask);
//		}
//		if (ProcessType.PRO_REMOVE_CHILD.equals(name)) {
//			this.showTaskControl(null);
//			disposeTaskDetails(oldtask);
//		}
//		validateEnd(null);
	}
	
	protected void unhighlightGraphNodes() {

		Graph graph = this.getProcessGraphViewer().getGraphControl();
		List<?> elements = graph.getNodes();
		for (Iterator<?> iterator = elements.iterator(); iterator.hasNext();) {
			GraphItem graphItem = (GraphItem) iterator.next();
			unhighlightGraphNode(graphItem);
		}
	}

	protected void unhighlightGraphNode(GraphItem item) {
		if (item instanceof CGraphNode) {
			IFigure figure = ((CGraphNode) item).getFigure();
			if (figure instanceof TaskNodeFigure) {
				((TaskNodeFigure) figure).unhighlightLabel();
			}
		}
	}

	protected void highlightGraphNode(GraphItem item) {
		if (item instanceof CGraphNode) {
			IFigure figure = ((CGraphNode) item).getFigure();
			if (figure instanceof TaskNodeFigure) {
				((TaskNodeFigure) figure).highlightLabel(org.eclipse.draw2d.ColorConstants.darkBlue);
			}
		}
	}
	
	protected void createProcessGraphicalPanel(Composite parent) {
		processGraphViewer = new GraphViewer(parent, SWT.NONE);
		processGraphViewer.getControl().dispose();
		processGraphViewer.setControl(new Graph(parent, SWT.NONE) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.zest.core.widgets.Graph#dispose()
			 */
			@Override
			public void dispose() {
				try {
					super.dispose();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}

		});
		// GridData gd = new GridData(GridData.FILL_BOTH);
		// processGraphViewer.getControl().setLayoutData(gd);
		// processGraphViewer.setNodeStyle(ZestStyles.NODES_FISHEYE);
		processGraphViewer.setContentProvider(new ProcessGraphContentProvider());
		processGraphViewer.setLabelProvider(new ProcessGraphicalViewerLabelProvider(this));
		processGraphViewer.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
		HorizontalTreeLayoutAlgorithm layoutAlgorithm = new HorizontalTreeLayoutAlgorithm(
				LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		processGraphViewer.setLayoutAlgorithm(layoutAlgorithm, true);
		if (parent instanceof Section) {
			((Section) parent).setClient(processGraphViewer.getControl());
		}
	}

	
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		updateHeaderFormMessage();
	}
	
	protected void updateHeaderFormMessage() {
		if (this.getManagedForm() != null) {
			getManagedForm().getMessageManager().removeAllMessages();
			getManagedForm().getMessageManager().update();
			getProcessGraphViewer().getControl().setEnabled(true);
			getProcessGraphViewer().getControl().setBackground(
					getManagedForm().getToolkit().getColors().getBackground());
		}
		if (currentMessageType != IMessageProvider.NONE && currentMessage != null) {
			if (this.getProcessGraphViewer() != null) {
				getProcessGraphViewer().getControl().setBackground(GraphicsConstants.BORDER_CORLOR);
				getProcessGraphViewer().getControl().setEnabled(false);
				getProcessGraphViewer().setInput(new Object());
//				showTaskControl(null);
			}
			if (this.getManagedForm() != null) {

				String[] messages = currentMessage.split("\n"); //$NON-NLS-1$
				List<IMessage> messageList = new ArrayList<IMessage>();
				for (int i = 0; i < messages.length; i++) {
					String message = messages[i];
					if (message != null)
						message.trim();
					if (message.length() == 0) {
						continue;
					}
					messageList.add(new SmooksMessage(currentMessageType, message));
				}
				String mainMessage = null;
				if (messageList.isEmpty()) {
					mainMessage = currentMessage;
				} else {
					mainMessage = messageList.get(0).getMessage();
				}

				this.getManagedForm().getForm().getForm().setMessage(mainMessage, currentMessageType,
						messageList.toArray(new IMessage[] {}));

			}
		}
	}
	
	/**
	 * @return the needupdatewhenshow
	 */
	public boolean isNeedupdatewhenshow() {
		return needupdatewhenshow;
	}

	/**
	 * @param needupdatewhenshow
	 *            the needupdatewhenshow to set
	 */
	public void setNeedupdatewhenshow(boolean needupdatewhenshow) {
		this.needupdatewhenshow = needupdatewhenshow;
	}

	public ProcessType getProcess() {
		return this.process;
	}


	
}
