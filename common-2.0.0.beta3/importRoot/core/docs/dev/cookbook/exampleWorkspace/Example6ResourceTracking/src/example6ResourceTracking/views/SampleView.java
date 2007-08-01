package example6ResourceTracking.views;

import org.eclipse.core.internal.events.ResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.internal.ui.viewsupport.JavaUILabelProvider;
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
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import example6ResourceTracking.Example6Model;
import example6ResourceTracking.Example6ResourceTrackingPlugin;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class SampleView extends ViewPart implements Example6Model.MyChangeListener{
	private TreeViewer viewer;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;


	class ViewContentProvider implements IStructuredContentProvider, 
										   ITreeContentProvider {

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			if( parent instanceof Example6Model ) {
				// The child to our invisible root (the model) are our two categories
				return new String[] { Example6Model.RESOURCES, Example6Model.ELEMENTS};
			}
			return new Object[0];
		}
		public Object getParent(Object child) {
			// must be implemented if we use drilldown adaptors. We are not using them here.
			return null;
		}
		public Object [] getChildren(Object parent) {
			if( parent instanceof String ) {
				// They're asking about 
				if( parent.equals( Example6Model.ELEMENTS)) {
					return Example6Model.getDefault().getModel().getElementChanges().toArray();
				}
				if( parent.equals( Example6Model.RESOURCES)) {
					return Example6Model.getDefault().getModel().getResourceChanges().toArray();
				}
			}

			
			if( parent instanceof ElementChangedEvent ) {
				return ((ElementChangedEvent)parent).getDelta().getAffectedChildren();
			}
			if( parent instanceof IJavaElementDelta ) {
				return ((IJavaElementDelta)parent).getAffectedChildren();
			}

			
			
			if( parent instanceof ResourceChangeEvent ) {
				return ((ResourceChangeEvent)parent).getDelta().getAffectedChildren();
			}
			System.out.println("3 " + parent.getClass().getName());
			return new Object[0];
		}
		public boolean hasChildren(Object parent) {
			if( parent instanceof String ) {
				if( parent.equals( Example6Model.ELEMENTS)) {
					return Example6Model.getDefault().getModel().getElementChanges().size() > 0;
				}
				if( parent.equals( Example6Model.RESOURCES)) {
					return Example6Model.getDefault().getModel().getResourceChanges().size() > 0;
				}
			}
			
			
			if( parent instanceof ElementChangedEvent ) {
				IJavaElementDelta delta = ((ElementChangedEvent)parent).getDelta();
				if( delta.getAffectedChildren().length > 0 ) return true;
			}
			if( parent instanceof IJavaElementDelta ) {
				IJavaElement element = ((IJavaElementDelta)parent).getElement();
				if( element instanceof ICompilationUnit ) {
					ICompilationUnit comp = ((ICompilationUnit)element);
				}

				return ((IJavaElementDelta)parent).getAffectedChildren().length > 0;
			}
			
			
			if( parent instanceof ResourceChangeEvent ) {
				return ((ResourceChangeEvent)parent).getDelta().getAffectedChildren().length > 0;
			}
			
			System.out.println("4 " + parent.getClass().getName());
			return false;
		}
/*
 * We will set up a dummy model to initialize tree heararchy.
 * In a real code, you will connect to a real model and
 * expose its hierarchy.
 */
		private void initialize() {
		}
	}
	class ViewLabelProvider extends LabelProvider {

		private JavaUILabelProvider delegate;
		public ViewLabelProvider() {
			delegate = new JavaUILabelProvider();
		}
		
		private String deltaKind(int kind) {
			if( kind == 1 ) return "Added";
			if( kind == 2 ) return "Removed";
			if( kind == 4 ) return "Changed";
			return "";
		}
		public String getText(Object obj) {
			if( obj instanceof IJavaElementDelta ) {
				IJavaElementDelta delta = ((IJavaElementDelta)obj);
				String elementName = ((IJavaElementDelta)obj).getElement().getElementName();
				// default
				return  elementName == "" ? 
						((IJavaElementDelta)obj).getElement().getClass().getName() : 
						elementName + " - " + deltaKind(delta.getKind());					
			}
			if( obj instanceof ElementChangedEvent) {
				ElementChangedEvent event = ((ElementChangedEvent)obj);
				return "source: " + event.getSource();
			}
			
			if( obj instanceof IResourceDelta) {
				return ((IResourceDelta)obj).getResource().getName();
			}
			return obj.toString();
		}
		public Image getImage(Object obj) {
			if( obj instanceof IJavaElementDelta ) {
				IJavaElementDelta delta = ((IJavaElementDelta)obj);
				IJavaElement element = delta.getElement();
				return delegate.getImage(element);
			}
			return null;
		}
	}

	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public SampleView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
		
		/*
		 * BEGIN example6 specific code
		 */
		Example6Model model = Example6ResourceTrackingPlugin.getDefault().getModel();
		viewer.setInput(model);
		model.addListener(this);
		
		
	}

	// Called from our model when *IT* receives a real event
	public void fireMyChangeEvent() {
		// event fired
		final Example6Model model = Example6ResourceTrackingPlugin.getDefault().getModel();

        if (!viewer.getControl().isDisposed())
        {
	        Display display = viewer.getControl().getDisplay();
	        
	        if (!display.isDisposed()) {
	            display.asyncExec(new Runnable() {
	                public void run() {
	                    //make sure the tree still exists
	                    if (viewer != null && viewer.getControl().isDisposed())
	                        return;
	            		viewer.refresh();
	                }
	            });
	        }
	        }

	}

	/* END example6 specific coding  */

	
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SampleView.this.fillContextMenu(manager);
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

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(new Separator());
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(new Separator());
	}

	private void makeActions() {
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
			"Sample View",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}