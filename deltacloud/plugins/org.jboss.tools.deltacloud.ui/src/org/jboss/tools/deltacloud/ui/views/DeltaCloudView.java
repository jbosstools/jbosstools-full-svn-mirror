package org.jboss.tools.deltacloud.ui.views;


import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.core.ICloudManagerListener;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;
import org.jboss.tools.internal.deltacloud.ui.wizards.NewInstance;


public class DeltaCloudView extends ViewPart implements ICloudManagerListener {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.jboss.tools.deltacloud.ui.views.DeltaCloudView";
	
	private static final String REMOVE_CLOUD = "RemoveCloud.label"; //$NON-NLS-1$
	private static final String REFRESH = "Refresh.label"; //$NON-NLS-1$
	private static final String CREATE_INSTANCE = "CreateInstance.label"; //$NON-NLS-1$
	
	public static final String COLLAPSE_ALL = "CollapseAll.label"; //$NON-NLS-1$

	private TreeViewer viewer;
	private Action action1;
	private Action action2;
	private Action removeCloud;
	private Action refreshAction;
	private Action collapseall;
	private Action doubleClickAction;
	private Action createInstance;
	
	private CloudViewElement selectedElement;

	/**
	 * The constructor.
	 */
	public DeltaCloudView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		viewer.setContentProvider(new CloudViewContentProvider());
		viewer.setLabelProvider(new CloudViewLabelProvider());
		viewer.setInput(getViewSite());
		viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		getSite().setSelectionProvider(viewer); // for tabbed properties

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
				DeltaCloudView.this.fillContextMenu(manager);
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
		selectedElement = (CloudViewElement)selection.getFirstElement();
	}
	
	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(removeCloud);
		manager.add(refreshAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		if (selectedElement instanceof CVImageElement) {
			manager.add(createInstance);
		} else {
			manager.add(action1);
			manager.add(action2);
		}
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(collapseall);
	}

	private void makeActions() {
		removeCloud = new Action() {
			public void run() {
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				CloudViewElement element = (CloudViewElement)selection.getFirstElement();
				while (element != null && !(element instanceof CVCloudElement)) {
					element = (CloudViewElement)element.getParent();
				}
				if (element != null) {
					CVCloudElement cve = (CVCloudElement)element;
					DeltaCloudManager.getDefault().removeCloud((DeltaCloud)element.getElement());
					CloudViewContentProvider p = (CloudViewContentProvider)viewer.getContentProvider();
					Object[] elements = p.getElements(getViewSite());
					int index = -1;
					for (int i = 0; i < elements.length; ++i) {
						if (elements[i] == cve)
							index = i;
					}
					if (index >= 0)
						((TreeViewer)cve.getViewer()).remove(getViewSite(), index);
				}
			}
		};
		removeCloud.setText(CVMessages.getString(REMOVE_CLOUD));
		removeCloud.setToolTipText(CVMessages.getString(REMOVE_CLOUD));
		removeCloud.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_ELCL_REMOVE));

		createInstance = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Shell shell = viewer.getControl().getShell();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				if (obj instanceof CVImageElement) {
					CVImageElement imageElement = (CVImageElement)obj;
					DeltaCloudImage image = (DeltaCloudImage)imageElement.getElement();
					CVCategoryElement images = (CVCategoryElement)imageElement.getParent();
					CVCloudElement cloudElement = (CVCloudElement)images.getParent();
					DeltaCloud cloud = (DeltaCloud)cloudElement.getElement();
					IWizard wizard = new NewInstance(cloud, image);
					WizardDialog dialog = new WizardDialog(shell, wizard);
					dialog.create();
					dialog.open();
				}
			}
		};		
		createInstance.setText(CVMessages.getString(CREATE_INSTANCE));
		createInstance.setToolTipText(CVMessages.getString(CREATE_INSTANCE));
		createInstance.setImageDescriptor(SWTImagesFactory.DESC_INSTANCE);
		
		refreshAction = new Action() {
			public void run() {
				viewer.setInput(getViewSite());
			}
		};
		refreshAction.setText(CVMessages.getString(REFRESH));
		refreshAction.setToolTipText(CVMessages.getString(REFRESH));
		refreshAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
	
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showMessage("Double-click detected on "+obj.toString());
			}
		};
		collapseall = new Action() {
			public void run() {
				viewer.collapseAll();
			}
		};
		collapseall.setText(CVMessages.getString(COLLAPSE_ALL));
		collapseall.setToolTipText(CVMessages.getString(COLLAPSE_ALL));
		collapseall.setImageDescriptor(SWTImagesFactory.DESC_COLLAPSE_ALL);
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

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void changeEvent(int type) {
		viewer.setInput(getViewSite());
	}
}