package org.jboss.tools.bpmn2.process.diagram.sheet;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.bpmn2.Artifact;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.provider.Bpmn2ItemProviderAdapterFactory;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.ui.action.CreateChildAction;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.emf.ui.properties.sections.UndoableModelPropertySheetEntry;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.jboss.tools.bpmn2.process.diagram.part.Bpmn2DiagramEditor;

public class Bpmn2PropertySheetPage implements IPropertySheetPage {
	
	private EObject selectedObject = null;
	private Bpmn2DiagramEditor editor;
	private Composite mainControl, masterArea, detailsArea;
	private TreeViewer treeViewer;
	private PropertySheetPage propertySheetPage;
	private AdapterFactory adapterFactory = new Bpmn2ItemProviderAdapterFactory();
	private Menu popupMenu, addChildSubmenu, addSiblingSubmenu;
	
	public Bpmn2PropertySheetPage(Bpmn2DiagramEditor editor) {
		this.editor = editor;
	}

	@Override
	public void createControl(Composite parent) {
		mainControl = createMainControl(parent);
		createMasterArea(mainControl);
		createDetailsArea(mainControl);
	}

    public Composite createMainControl(Composite parent) {
        Composite composite = editor.getFormToolkit().createComposite(parent);
        FormLayout layout = new FormLayout();
        layout.marginWidth = ITabbedPropertyConstants.HSPACE + 2;
        layout.marginHeight = ITabbedPropertyConstants.VSPACE;
        layout.spacing = ITabbedPropertyConstants.VMARGIN + 1;
        composite.setLayout(layout);
        return composite;
    }
 
	private void createMasterArea(Composite composite) {
		masterArea = editor.getFormToolkit().createComposite(composite);
		masterArea.setLayoutData(createMasterAreaLayoutData());
		masterArea.setLayout(new FormLayout());
		Tree eventTree = editor.getFormToolkit().createTree(
				masterArea, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		eventTree.setLayoutData(createFillLayoutData());
		popupMenu = createPopupMenu(eventTree);
		eventTree.setMenu(popupMenu);
		treeViewer = new TreeViewer(eventTree);
		treeViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
		treeViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
		treeViewer.setFilters(new ViewerFilter[] { new RootFilter(), new FlowElementFilter() });
	}
	
	private FormData createMasterAreaLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(0, 200);
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		return data;
	}
	
	private FormData createFillLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		return data;
	}
	
	private void createDetailsArea(Composite composite) {
		detailsArea = editor.getFormToolkit().createComposite(composite, SWT.BORDER);
		detailsArea.setLayoutData(createDetailsAreaLayoutData());
		detailsArea.setLayout(new FormLayout());
        propertySheetPage = new PropertySheetPage();
        UndoableModelPropertySheetEntry root = new UndoableModelPropertySheetEntry(
            OperationHistoryFactory.getOperationHistory());       
        root.setPropertySourceProvider(new AdapterFactoryContentProvider(adapterFactory));
        propertySheetPage.setRootEntry(root);
        propertySheetPage.createControl(detailsArea);
        propertySheetPage.getControl().setLayoutData(createFillLayoutData());
	}

	private FormData createDetailsAreaLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(masterArea, 0);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		return data;
	}
	
	private Menu createPopupMenu(Composite composite) {
		Menu popupMenu = new Menu(composite);
		composite.setMenu(popupMenu);
		popupMenu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
				depopulatePopupMenu();
				populatePopupMenu();
			}			
		});
		return popupMenu;
	}
	
    private Object getTreeViewerSelectionUnwrapped() {
    	ISelection selection = treeViewer.getSelection();
    	if (selection instanceof IStructuredSelection) {
    		return ((IStructuredSelection)selection).getFirstElement();
    	}
    	return null;
    }

	private void createAddChildSubmenu() {	
		MenuItem addChildMenuItem = new MenuItem(popupMenu, SWT.CASCADE);
		addChildMenuItem.setText("&New Child");
		addChildSubmenu = new Menu(addChildMenuItem);
		addChildMenuItem.setMenu(addChildSubmenu);
		Object obj = getTreeViewerSelectionUnwrapped();
		Collection<?> descriptors = editor.getEditingDomain().getNewChildDescriptors(obj, null);
		ArrayList<IAction> actions = new ArrayList<IAction>();
		for (Object descriptor : descriptors) {
			if (descriptor instanceof CommandParameter) {
				CommandParameter commandParameter = (CommandParameter)descriptor;
				Object value = commandParameter.getValue();
				if (value instanceof FlowElement || value instanceof Artifact) continue;
				actions.add(new CreateChildAction(editor.getEditingDomain(), treeViewer.getSelection(), descriptor));
			}			
		}
		addChildMenuItem.setEnabled(!actions.isEmpty());
		for (final IAction action : actions) {
			MenuItem menuItem = new MenuItem(addChildSubmenu, SWT.PUSH);
			menuItem.setText(action.getText());
			menuItem.setImage(action.getImageDescriptor().createImage());
			menuItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					action.run();
				}
			});
		}
	}
	
	private void createAddSiblingSubmenu() {	
		MenuItem addSiblingMenuItem = new MenuItem(popupMenu, SWT.CASCADE);
		addSiblingMenuItem.setText("N&ew Sibling");
		addSiblingSubmenu = new Menu(addSiblingMenuItem);
		addSiblingMenuItem.setMenu(addSiblingSubmenu);	
		if (!canEnableAddSibling()) {
			addSiblingMenuItem.setEnabled(false);
		}
	}
	
	private boolean canEnableAddSibling() {
		ISelection selection = treeViewer.getSelection();
		if (selection instanceof IStructuredSelection) {
			return ((IStructuredSelection)selection).getFirstElement() == treeViewer.getInput();
		}
		return true;
	}
	
	private void depopulatePopupMenu() {
		disposePopupMenuItems(popupMenu);
	}
	
	private void disposePopupMenuItems(Menu menu) {
		for (MenuItem menuItem : menu.getItems()) {
			Menu nestedMenu = menuItem.getMenu();
			if (nestedMenu != null) {
				disposePopupMenuItems(nestedMenu);
			}
			menuItem.dispose();
		}
	}
	
	private void populatePopupMenu() {
		createAddChildSubmenu();
		createAddSiblingSubmenu();
	}
	
    @Override
	public void dispose() {
		popupMenu.dispose();
		detailsArea.dispose();
		masterArea.dispose();
		mainControl.dispose();
	}

	@Override
	public Control getControl() {
		return mainControl;
	}

	@Override
	public void setActionBars(IActionBars actionBars) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        selectedObject = unwrapSelection(selection);
        treeViewer.setInput(selectedObject.eContainer());
        StructuredSelection sel = new StructuredSelection(selectedObject);
        treeViewer.setSelection(sel);
        propertySheetPage.selectionChanged(part, sel);
	}

 	private EObject unwrapSelection(ISelection selection) {
 		if (!(selection instanceof StructuredSelection)) {
 			return null;
 		}
 		Object object = ((StructuredSelection)selection).getFirstElement();
 		if (object == null || !(object instanceof EditPart)) {
 			return null;
 		}
 		object = ((EditPart)object).getModel();
 		if (object == null || !(object instanceof View)) {
 			return null;
 		}
 		return ((View)object).getElement();
 	}
 	
 	class RootFilter extends ViewerFilter {
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (parentElement == selectedObject.eContainer() && element != selectedObject) {
				return false;
			}
			return true;
		} 		
 	}
 	
 	class FlowElementFilter extends ViewerFilter {
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (parentElement != selectedObject.eContainer() && element instanceof FlowElement) {
				return false;
			}
			return true;
		} 		
 	}
 	
}
