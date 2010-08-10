package org.jboss.tools.deltacloud.ui.views;

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
import org.eclipse.swt.events.TreeAdapter;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
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

public class InstanceView extends ViewPart implements ICloudManagerListener {

	private final static String CLOUD_SELECTOR_LABEL = "CloudSelector.label"; //$NON-NLS-1$
	private TableViewer viewer;
	private Composite container;
	private Combo cloudSelector;
	private DeltaCloudInstance selectedElement;
	
	private DeltaCloud[] clouds;
	private DeltaCloud currCloud;
	
	private Action doubleClickAction;

	public InstanceView() {
		// TODO Auto-generated constructor stub
	}

	private ModifyListener cloudModifyListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			int index = cloudSelector.getSelectionIndex();
			currCloud = clouds[index];
			viewer.setInput(currCloud);
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
			int direction = SWT.UP;
			if (comparator.getColumn() == column) {
				comparator.reverseDirection();
				if (t.getSortDirection() == SWT.UP)
					direction = SWT.DOWN;
			}
			comparator.setColumn(column);
			TableColumn tc = (TableColumn)e.getSource();
			t.setSortColumn(tc);
			t.setSortDirection(direction);
			viewer.refresh();
		}
	
	};
	
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
		viewer.setContentProvider(new InstanceViewLabelAndContentProvider());
		viewer.setLabelProvider(new InstanceViewLabelAndContentProvider());
		InstanceComparator comparator = new InstanceComparator(0);
		viewer.setComparator(comparator);
		
		for (int i = 0; i < InstanceViewLabelAndContentProvider.Column.getSize(); ++i) {
			InstanceViewLabelAndContentProvider.Column c = 
				InstanceViewLabelAndContentProvider.Column.getColumn(i);
			TableColumn tc = new TableColumn(table, SWT.NONE);
			if (i == 0)
				table.setSortColumn(tc);
			tc.setText(CVMessages.getString(c.name()));
			tableLayout.setColumnData(tc, new ColumnWeightData(c.getWeight()));
			tc.addSelectionListener(new ColumnListener(i, viewer));
		}
		table.setSortDirection(SWT.UP);
		
		viewer.setInput(clouds[0]);

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
		//TODO
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		//TODO
	}

	private void makeActions() {
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showMessage("Double-click detected on "+obj.toString());
			}
		};
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
		cloudSelector.setText(cloudNames[0]);
		currCloud = clouds[0];
	}
	
	@Override
	public void changeEvent(int type) {
		String currName = currCloud.getName();
		clouds = DeltaCloudManager.getDefault().getClouds();
		String[] cloudNames = new String[clouds.length];
		int index = 0;
		for (int i = 0; i < clouds.length; ++i) {
			cloudNames[i] = clouds[i].getName();
			if (currName.equals(cloudNames[i]))
				index = i;
		}
		cloudSelector.removeModifyListener(cloudModifyListener);
		cloudSelector.setItems(cloudNames);
		cloudSelector.setText(cloudNames[index]);
		cloudSelector.addModifyListener(cloudModifyListener);
	}

}
