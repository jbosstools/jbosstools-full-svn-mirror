package org.jboss.tools.deltacloud.ui.views;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.core.ICloudManagerListener;
import org.jboss.tools.deltacloud.core.IInstanceListListener;

public class InstanceView extends ViewPart implements ICloudManagerListener, IInstanceListListener {

	private final static String CLOUD_SELECTOR_LABEL = "CloudSelector.label"; //$NON-NLS-1$
	private final static String START_LABEL = "Start.label"; //$NON-NLS-1$
	private final static String STOP_LABEL = "Stop.label"; //$NON-NLS-1$
	private final static String REBOOT_LABEL = "Reboot.label"; //$NON-NLS-1$
	private final static String DESTROY_LABEL = "Destroy.label"; //$NON-NLS-1$
	private final static String STARTING_INSTANCE_TITLE = "StartingInstance.title"; //$NON-NLS-1$
	private final static String STARTING_INSTANCE_MSG = "StartingInstance.msg"; //$NON-NLS-1$
	private final static String STOPPING_INSTANCE_TITLE = "StoppingInstance.title"; //$NON-NLS-1$
	private final static String STOPPING_INSTANCE_MSG = "StoppingInstance.msg"; //$NON-NLS-1$
	private final static String REBOOTING_INSTANCE_TITLE = "RebootingInstance.title"; //$NON-NLS-1$
	private final static String REBOOTING_INSTANCE_MSG = "RebootingInstance.msg"; //$NON-NLS-1$
	private final static String DESTROYING_INSTANCE_TITLE = "DestroyingInstance.title"; //$NON-NLS-1$
	private final static String DESTROYING_INSTANCE_MSG = "DestroyingInstance.msg"; //$NON-NLS-1$
	
	
	private TableViewer viewer;
	private Composite container;
	private Combo cloudSelector;
	private DeltaCloudInstance selectedElement;
	
	private DeltaCloud[] clouds;
	private DeltaCloud currCloud;
	
	private InstanceViewLabelAndContentProvider contentProvider;
	
	private Action doubleClickAction;
	private Action startAction;
	private Action stopAction;
	private Action destroyAction;
	private Action rebootAction;
	
	private Map<String, Action> instanceActions;
	
	private InstanceView parentView;

	public InstanceView() {
		parentView = this;
	}

	private ModifyListener cloudModifyListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			int index = cloudSelector.getSelectionIndex();
			currCloud = clouds[index];
			Display.getCurrent().asyncExec(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					currCloud.removeInstanceListListener(parentView);
					viewer.setInput(currCloud);
					currCloud.addInstanceListListener(parentView);
					viewer.refresh();
					
				}
				
			});
		}
		
	};
	
	private class ColumnListener extends SelectionAdapter {
		
		private int column;
		private TableViewer viewer;
		
		public ColumnListener(int column, TableViewer viewer) {
			this.column = column;
			this.viewer = viewer;
		}
		@Override
		public void widgetSelected(SelectionEvent e) {
			InstanceComparator comparator = (InstanceComparator)viewer.getComparator();
			Table t = viewer.getTable();
			if (comparator.getColumn() == column) {
				comparator.reverseDirection();
			}
			comparator.setColumn(column);
			TableColumn tc = (TableColumn)e.getSource();
			t.setSortColumn(tc);
			t.setSortDirection(SWT.NONE);
			viewer.refresh();
		}
	
	};
	
	@Override
	public void dispose() {
		for (DeltaCloud cloud : clouds) {
			cloud.removeInstanceListListener(this);
		}
		DeltaCloudManager.getDefault().removeCloudManagerListener(this);
		super.dispose();
	}
	
	@Override
	public void createPartControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		FormLayout layout = new FormLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		container.setLayout(layout);
		
		Label cloudSelectorLabel = new Label(container, SWT.NULL);
		cloudSelectorLabel.setText(CVMessages.getString(CLOUD_SELECTOR_LABEL));
		
		cloudSelector = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
		initializeCloudSelector();
		cloudSelector.addModifyListener(cloudModifyListener);
		// Following is a kludge so that on Linux the Combo is read-only but
		// has a white background.
		cloudSelector.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				e.doit = false;
			}
		});
		
		Composite tableArea = new Composite(container, SWT.NULL);
		TableColumnLayout tableLayout = new TableColumnLayout();
		tableArea.setLayout(tableLayout);
		
		viewer = new TableViewer(tableArea, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		contentProvider = new InstanceViewLabelAndContentProvider();
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(contentProvider);
		InstanceComparator comparator = new InstanceComparator(0);
		viewer.setComparator(comparator);
		
		for (int i = 0; i < InstanceViewLabelAndContentProvider.Column.getSize(); ++i) {
			InstanceViewLabelAndContentProvider.Column c = 
				InstanceViewLabelAndContentProvider.Column.getColumn(i);
			TableColumn tc = new TableColumn(table, SWT.NONE);
			if (i == 0)
				table.setSortColumn(tc);
			tc.setText(CVMessages.getString(c.name()));
			tableLayout.setColumnData(tc, new ColumnWeightData(c.getWeight(), true));
			tc.addSelectionListener(new ColumnListener(i, viewer));
		}
		table.setSortDirection(SWT.NONE);
		
		if (clouds.length > 0) {
			currCloud = clouds[0];
			currCloud.removeInstanceListListener(parentView);
			viewer.setInput(clouds[0]);
			currCloud.addInstanceListListener(parentView);
		}

		FormData f = new FormData();
		f.top = new FormAttachment(0, 8);
		f.left = new FormAttachment(0, 30);
		cloudSelectorLabel.setLayoutData(f);
		
		f = new FormData();
		f.top = new FormAttachment(0, 5);
		f.left = new FormAttachment(cloudSelectorLabel, 5);
		cloudSelector.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(cloudSelector, 8);
		f.left = new FormAttachment(0, 0);
		f.right = new FormAttachment(100, 0);
		f.bottom = new FormAttachment(100, 0);
		tableArea.setLayoutData(f);
		
		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "org.jboss.tools.deltacloud.ui.viewer");
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		hookSelection();
		contributeToActionBars();
		
		DeltaCloudManager.getDefault().addCloudManagerListener(this);
	}
	
	private void hookSelection() {
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				handleSelection();
			}
		});
	}
	
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				InstanceView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void handleSelection() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		selectedElement = (DeltaCloudInstance)selection.getFirstElement();
	}
	
	private void fillLocalPullDown(IMenuManager manager) {
		//TODO
	}

	private void fillContextMenu(IMenuManager manager) {
		List<String> actions = selectedElement.getActions();
		manager.add(instanceActions.get(DeltaCloudInstance.START));
		instanceActions.get(DeltaCloudInstance.START).setEnabled(false);
		manager.add(instanceActions.get(DeltaCloudInstance.STOP));
		instanceActions.get(DeltaCloudInstance.STOP).setEnabled(false);
		manager.add(instanceActions.get(DeltaCloudInstance.REBOOT));
		instanceActions.get(DeltaCloudInstance.REBOOT).setEnabled(false);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(instanceActions.get(DeltaCloudInstance.DESTROY));
		instanceActions.get(DeltaCloudInstance.DESTROY).setEnabled(false);
		for (String action : actions) {
			instanceActions.get(action).setEnabled(true);
		}
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		//TODO
	}

	private class PerformInstanceActionThread extends Job {
		private DeltaCloud cloud;
		private DeltaCloudInstance instance;
		private String action;
		private String taskName;
		
	 	public PerformInstanceActionThread(DeltaCloud cloud, DeltaCloudInstance instance, 
	 			String action, String title, String taskName) {
	 		super(title);
	 		this.cloud = cloud;
	 		this.instance = instance;
	 		this.action = action;
	 		this.taskName = taskName;
	 	}
	 	
		@Override
		public IStatus run(IProgressMonitor pm) {
			try {
				pm.beginTask(taskName, IProgressMonitor.UNKNOWN);
				pm.worked(1);
				cloud.performInstanceAction(instance.getId(), action);
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						refreshInstance(instance);				
					}
				});
			} finally {
				pm.done();
			}
			return Status.OK_STATUS;
		}
	}
	
	private class PerformDestroyInstanceActionThread extends Job {
		private DeltaCloud cloud;
		private DeltaCloudInstance instance;
		private String taskName;
		
	 	public PerformDestroyInstanceActionThread(DeltaCloud cloud, DeltaCloudInstance instance,
	 			String title, String taskName) {
	 		super(title);
	 		this.cloud = cloud;
	 		this.instance = instance;
	 		this.taskName = taskName;
	 	}
	 	
	 	@Override
	 	public IStatus run(IProgressMonitor pm) {
	 		try {
	 			pm.beginTask(taskName, IProgressMonitor.UNKNOWN);
	 			pm.worked(1);
	 			Display.getDefault().asyncExec(new Runnable() {
	 				@Override
	 				public void run() {
	 					cloud.destroyInstance(instance.getId());
	 				}
	 			});
	 		} finally {
	 			pm.done();
	 		}
	 		return Status.OK_STATUS;
	 	}
	}
	
	private void makeActions() {
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showMessage("Double-click detected on "+obj.toString());
			}
		};
		startAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				DeltaCloudInstance instance = (DeltaCloudInstance)((IStructuredSelection)selection).getFirstElement();
				PerformInstanceActionThread t = new PerformInstanceActionThread(currCloud, instance, DeltaCloudInstance.START,
						CVMessages.getString(STARTING_INSTANCE_TITLE), 
						CVMessages.getFormattedString(STARTING_INSTANCE_MSG, new String[]{instance.getName()}));
				t.setUser(true);
				t.schedule();
			}
		};
		startAction.setText(CVMessages.getString(START_LABEL));
		startAction.setToolTipText(CVMessages.getString(START_LABEL));
		
		stopAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				DeltaCloudInstance instance = (DeltaCloudInstance)((IStructuredSelection)selection).getFirstElement();
				PerformInstanceActionThread t = new PerformInstanceActionThread(currCloud, instance, DeltaCloudInstance.STOP,
						CVMessages.getString(STOPPING_INSTANCE_TITLE), 
						CVMessages.getFormattedString(STOPPING_INSTANCE_MSG, new String[]{instance.getName()}));
				t.setUser(true);
				t.schedule();
			}
		};
		stopAction.setText(CVMessages.getString(STOP_LABEL));
		stopAction.setToolTipText(CVMessages.getString(STOP_LABEL));
		
		rebootAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				DeltaCloudInstance instance = (DeltaCloudInstance)((IStructuredSelection)selection).getFirstElement();
				PerformInstanceActionThread t = new PerformInstanceActionThread(currCloud, instance, DeltaCloudInstance.REBOOT,
						CVMessages.getString(REBOOTING_INSTANCE_TITLE), 
						CVMessages.getFormattedString(REBOOTING_INSTANCE_MSG, new String[]{instance.getName()}));
				t.setUser(true);
				t.schedule();
			}
		};
		rebootAction.setText(CVMessages.getString(REBOOT_LABEL));
		rebootAction.setToolTipText(CVMessages.getString(REBOOT_LABEL));
		
		destroyAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				DeltaCloudInstance instance = (DeltaCloudInstance)((IStructuredSelection)selection).getFirstElement();
				PerformDestroyInstanceActionThread t = new PerformDestroyInstanceActionThread(currCloud, instance,
						CVMessages.getString(DESTROYING_INSTANCE_TITLE), 
						CVMessages.getFormattedString(DESTROYING_INSTANCE_MSG, new String[]{instance.getName()}));
				t.setUser(true);
				t.schedule();
			}
		};
		destroyAction.setText(CVMessages.getString(DESTROY_LABEL));
		destroyAction.setToolTipText(CVMessages.getString(DESTROY_LABEL));
		
		instanceActions = new HashMap<String, Action>();
		instanceActions.put(DeltaCloudInstance.START, startAction);
		instanceActions.put(DeltaCloudInstance.STOP, stopAction);
		instanceActions.put(DeltaCloudInstance.REBOOT, rebootAction);
		instanceActions.put(DeltaCloudInstance.DESTROY, destroyAction);
	}
	
	private void refreshInstance(DeltaCloudInstance instance) {
		DeltaCloudInstance[] instances = (DeltaCloudInstance[])contentProvider.getElements(currCloud);
		for (int i = 0; i < instances.length; ++i) {
			DeltaCloudInstance d = instances[i];
			if (d == instance) {
				currCloud.refreshInstance(d.getId());
				break;
			}
		}
	}
	
	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			CVMessages.getString("CloudViewName"), //$NON-NLS-1$
			message);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	private void initializeCloudSelector() {
		clouds = DeltaCloudManager.getDefault().getClouds();
		String[] cloudNames = new String[clouds.length];
		for (int i = 0; i < clouds.length; ++i) {
			cloudNames[i] = clouds[i].getName();
		}
		cloudSelector.setItems(cloudNames);
		if (clouds.length > 0) {
			cloudSelector.setText(cloudNames[0]);
			currCloud = clouds[0];
		}
	}
	
	public void changeEvent(int type) {
		String currName = null;
		clouds = DeltaCloudManager.getDefault().getClouds();
		if (currCloud != null) {
			currName = currCloud.getName();
		}
		String[] cloudNames = new String[clouds.length];
		int index = 0;
		for (int i = 0; i < clouds.length; ++i) {
			cloudNames[i] = clouds[i].getName();
			if (cloudNames[i].equals(currName))
				index = i;
		}
		cloudSelector.removeModifyListener(cloudModifyListener);
		cloudSelector.setItems(cloudNames);
		if (cloudNames.length > 0) {
			cloudSelector.setText(cloudNames[index]);
			currCloud = clouds[index];
			viewer.setInput(currCloud);
		} else {
			currCloud = null;
			cloudSelector.setText("");
			viewer.setInput(new DeltaCloudInstance[0]);
		}
		cloudSelector.addModifyListener(cloudModifyListener);
	}

	public void listChanged(DeltaCloudInstance[] list) {
		currCloud.removeInstanceListListener(parentView);
		viewer.setInput(list);
		currCloud.addInstanceListListener(parentView);
		viewer.refresh();
	}

}
