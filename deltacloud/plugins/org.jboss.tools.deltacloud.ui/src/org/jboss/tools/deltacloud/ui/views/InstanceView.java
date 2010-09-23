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
package org.jboss.tools.deltacloud.ui.views;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.rse.core.IRSECoreRegistry;
import org.eclipse.rse.core.IRSESystemType;
import org.eclipse.rse.core.RSECorePlugin;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.model.ISystemRegistry;
import org.eclipse.rse.core.model.SystemStartHere;
import org.eclipse.rse.core.subsystems.IConnectorService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
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
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.core.ICloudManagerListener;
import org.jboss.tools.deltacloud.core.IInstanceListListener;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.deltacloud.ui.IDeltaCloudPreferenceConstants;
import org.osgi.service.prefs.Preferences;

public class InstanceView extends ViewPart implements ICloudManagerListener, IInstanceListListener {

	private final static String CLOUD_SELECTOR_LABEL = "CloudSelector.label"; //$NON-NLS-1$
	private final static String START_LABEL = "Start.label"; //$NON-NLS-1$
	private final static String STOP_LABEL = "Stop.label"; //$NON-NLS-1$
	private final static String REBOOT_LABEL = "Reboot.label"; //$NON-NLS-1$
	private final static String DESTROY_LABEL = "Destroy.label"; //$NON-NLS-1$
	private final static String RSE_LABEL = "ShowInRSE.label"; //$NON-NLS-1$
	private final static String STARTING_INSTANCE_TITLE = "StartingInstance.title"; //$NON-NLS-1$
	private final static String STARTING_INSTANCE_MSG = "StartingInstance.msg"; //$NON-NLS-1$
	private final static String STOPPING_INSTANCE_TITLE = "StoppingInstance.title"; //$NON-NLS-1$
	private final static String STOPPING_INSTANCE_MSG = "StoppingInstance.msg"; //$NON-NLS-1$
	private final static String REBOOTING_INSTANCE_TITLE = "RebootingInstance.title"; //$NON-NLS-1$
	private final static String REBOOTING_INSTANCE_MSG = "RebootingInstance.msg"; //$NON-NLS-1$
	private final static String DESTROYING_INSTANCE_TITLE = "DestroyingInstance.title"; //$NON-NLS-1$
	private final static String DESTROYING_INSTANCE_MSG = "DestroyingInstance.msg"; //$NON-NLS-1$
	private final static String RSE_CONNECTING_MSG = "ConnectingRSE.msg"; //$NON-NLS-1$
	private static final String REFRESH = "Refresh.label"; //$NON-NLS-1$
	
	
	private TableViewer viewer;
	private Composite container;
	private Combo cloudSelector;
	private DeltaCloudInstance selectedElement;
	
	private DeltaCloud[] clouds;
	private DeltaCloud currCloud;
	
	private InstanceViewLabelAndContentProvider contentProvider;
	
	private Action refreshAction;
	private Action startAction;
	private Action stopAction;
	private Action destroyAction;
	private Action rebootAction;
	private Action rseAction;
	
	private Map<String, Action> instanceActions;
	private Map<String, Job> currentPerformingActions = new HashMap<String, Job>();
	
	private InstanceView parentView;

	public InstanceView() {
		parentView = this;
	}

	private ModifyListener cloudModifyListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			int index = cloudSelector.getSelectionIndex();
			if (currCloud != null)
				currCloud.removeInstanceListListener(parentView);
			currCloud = clouds[index];
			Preferences prefs = new InstanceScope().getNode(Activator.PLUGIN_ID);
			try {
				prefs.put(IDeltaCloudPreferenceConstants.LAST_CLOUD_INSTANCE_VIEW, currCloud.getName());
			} catch(Exception exc) {
				// do nothing
			}
			viewer.setInput(new DeltaCloudInstance[0]);
			viewer.refresh();
			Display.getCurrent().asyncExec(new Runnable() {

				@Override
				public void run() {
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
		
		if (currCloud != null) {
			currCloud.removeInstanceListListener(parentView);
			viewer.setInput(currCloud);
			currCloud.addInstanceListListener(parentView);
		}

		Point p1 = cloudSelectorLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Point p2 = cloudSelector.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int centering = (p2.y - p1.y + 1) / 2;

		FormData f = new FormData();
		f.top = new FormAttachment(0, 5 + centering);
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
		manager.add(refreshAction);
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
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(rseAction);
		if (selectedElement.getState().equals(DeltaCloudInstance.RUNNING) ||
				selectedElement.getState().equals(DeltaCloudInstance.STOPPED))
			rseAction.setEnabled(true);
		else
			rseAction.setEnabled(false);
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
		private String expectedState;
		
	 	public PerformInstanceActionThread(DeltaCloud cloud, DeltaCloudInstance instance, 
	 			String action, String title, String taskName, String expectedState) {
	 		super(title);
	 		this.cloud = cloud;
	 		this.instance = instance;
	 		this.action = action;
	 		this.taskName = taskName;
	 		this.expectedState = expectedState;
	 	}
	 	
		@Override
		public IStatus run(IProgressMonitor pm) {
			String id = instance.getId();
			try {
				pm.beginTask(taskName, IProgressMonitor.UNKNOWN);
				pm.worked(1);
				// To handle the user starting a new action when we haven't confirmed the last one yet,
				// cancel the previous job and then go on performing this action
				Job job = cloud.getActionJob(id);
				if (job != null) {
					job.cancel();
					try {
						job.join();
					} catch (InterruptedException e) {
						// do nothing, this is ok
					}
				}
				currentPerformingActions.put(id, this);
				cloud.performInstanceAction(id, action);
				while (instance != null && !(instance.getState().equals(expectedState))
						&& !(instance.getState().equals(DeltaCloudInstance.TERMINATED))) {
					instance = refreshInstance(instance);
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						break;
					}
				}
			} catch (DeltaCloudException e) {
				// do nothing..action had problem executing..perhaps illegal
			} finally {
				cloud.removeActionJob(id, this);
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
	 		String id = instance.getId();
	 		try {
	 			pm.beginTask(taskName, IProgressMonitor.UNKNOWN);
	 			pm.worked(1);
				// To handle the user starting a new action when we haven't confirmed the last one yet,
				// cancel the previous job and then go on performing this action
				Job job = cloud.getActionJob(id);
				if (job != null) {
					job.cancel();
					try {
						job.join();
					} catch (InterruptedException e) {
						// do nothing, this is ok
					}
				}
				cloud.registerActionJob(id, this);
	 			Display.getDefault().asyncExec(new Runnable() {
	 				@Override
	 				public void run() {
	 					cloud.destroyInstance(instance.getId());
	 				}
	 			});
	 		} finally {
	 			cloud.removeActionJob(id, this);
	 			pm.done();
	 		}
	 		return Status.OK_STATUS;
	 	}
	}
	
	private void makeActions() {
		refreshAction = new Action() {
			public void run() {
				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {
						if (currCloud != null) {
							currCloud.getInstances();
						}
					}

				});
				t.start();
			}
		};
		refreshAction.setText(CVMessages.getString(REFRESH));
		refreshAction.setToolTipText(CVMessages.getString(REFRESH));
		refreshAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
		
		startAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				DeltaCloudInstance instance = (DeltaCloudInstance)((IStructuredSelection)selection).getFirstElement();
				PerformInstanceActionThread t = new PerformInstanceActionThread(currCloud, instance, DeltaCloudInstance.START,
						CVMessages.getString(STARTING_INSTANCE_TITLE), 
						CVMessages.getFormattedString(STARTING_INSTANCE_MSG, new String[]{instance.getName()}),
						DeltaCloudInstance.RUNNING);
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
						CVMessages.getFormattedString(STOPPING_INSTANCE_MSG, new String[]{instance.getName()}),
						DeltaCloudInstance.STOPPED);
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
						CVMessages.getFormattedString(REBOOTING_INSTANCE_MSG, new String[]{instance.getName()}),
						DeltaCloudInstance.RUNNING);
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
		
		rseAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				DeltaCloudInstance instance = (DeltaCloudInstance)((IStructuredSelection)selection).getFirstElement();
				String hostname = instance.getHostName();
				ISystemRegistry registry = SystemStartHere.getSystemRegistry();
				RSECorePlugin rsep = RSECorePlugin.getDefault();
				IRSECoreRegistry coreRegistry = rsep.getCoreRegistry();
				IRSESystemType[] sysTypes = coreRegistry.getSystemTypes();
				IRSESystemType sshType = null;			
				for (IRSESystemType sysType : sysTypes) {
					if (sysType.getId().equals(IRSESystemType.SYSTEMTYPE_SSH_ONLY_ID))
						sshType = sysType;
				}
				String connectionName = instance.getName() + " [" + instance.getId() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
				try {
					IHost host = registry.createHost(sshType, connectionName, hostname, null);
					if (host != null) {
						host.setDefaultUserId("root"); //$NON-NLS-1$
						IConnectorService[] services = host.getConnectorServices();
						if (services.length > 0) {
							final IConnectorService service = services[0];
							Job connect = new Job(CVMessages.getFormattedString(RSE_CONNECTING_MSG, connectionName)) {
								@Override
								protected IStatus run(IProgressMonitor monitor) {
									try {
										service.connect(monitor);
										return Status.OK_STATUS;
									} catch(Exception e) {
										return Status.CANCEL_STATUS;
									}
								}
							};
							connect.setUser(true);
							connect.schedule();
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Activator.log(e);
				}
			}
		};
		rseAction.setText(CVMessages.getString(RSE_LABEL));
		rseAction.setToolTipText(CVMessages.getString(RSE_LABEL));
		
		instanceActions = new HashMap<String, Action>();
		instanceActions.put(DeltaCloudInstance.START, startAction);
		instanceActions.put(DeltaCloudInstance.STOP, stopAction);
		instanceActions.put(DeltaCloudInstance.REBOOT, rebootAction);
		instanceActions.put(DeltaCloudInstance.DESTROY, destroyAction);
	}
	
	private DeltaCloudInstance refreshInstance(DeltaCloudInstance instance) {
		DeltaCloudInstance[] instances = (DeltaCloudInstance[])contentProvider.getElements(currCloud);
		for (int i = 0; i < instances.length; ++i) {
			DeltaCloudInstance d = instances[i];
			if (d.getId().equals(instance.getId())) {
				return currCloud.refreshInstance(d.getId());
			}
		}
		return null;
	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	private void initializeCloudSelector() {
		int defaultIndex = 0;
		clouds = DeltaCloudManager.getDefault().getClouds();
		String[] cloudNames = new String[clouds.length];
		// If we have saved the last cloud used from a previous session,
		// default to using that cloud to start unless it no longer exists
		Preferences prefs = new InstanceScope().getNode(Activator.PLUGIN_ID);
		String lastCloudUsed = prefs.get(IDeltaCloudPreferenceConstants.LAST_CLOUD_INSTANCE_VIEW, "");
		for (int i = 0; i < clouds.length; ++i) {
			cloudNames[i] = clouds[i].getName();
			if (cloudNames[i].equals(lastCloudUsed))
				defaultIndex = i;
		}
		cloudSelector.setItems(cloudNames);
		if (clouds.length > 0) {
			cloudSelector.setText(cloudNames[defaultIndex]);
			currCloud = clouds[defaultIndex];
		}
	}
	
	public void changeEvent(int type) {
		String currName = null;
		int currIndex = 0;
		if (currCloud != null) {
			currName = currCloud.getName();
			currIndex = cloudSelector.getSelectionIndex();
		}
		clouds = DeltaCloudManager.getDefault().getClouds();
		String[] cloudNames = new String[clouds.length];
		int index = 0;
		for (int i = 0; i < clouds.length; ++i) {
			cloudNames[i] = clouds[i].getName();
			if (cloudNames[i].equals(currName))
				index = i;
		}
		if (type == ICloudManagerListener.RENAME_EVENT) {
			index = currIndex; // no change in cloud displayed
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

	public void listChanged(DeltaCloud cloud, DeltaCloudInstance[] list) {
		// Run following under Display thread since this can be
		// triggered by a non-display thread notifying listeners.
		final DeltaCloudInstance[] finalList = list;
		if (cloud.getName().equals(currCloud.getName())) {
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					currCloud.removeInstanceListListener(parentView);
					viewer.setInput(finalList);
					currCloud.addInstanceListListener(parentView);
					viewer.refresh();
				}
			});
		}
	}

}
