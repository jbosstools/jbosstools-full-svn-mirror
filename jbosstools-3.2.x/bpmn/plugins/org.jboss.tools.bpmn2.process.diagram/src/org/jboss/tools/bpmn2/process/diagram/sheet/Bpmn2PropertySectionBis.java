package org.jboss.tools.bpmn2.process.diagram.sheet;

import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.provider.Bpmn2ItemProviderAdapterFactory;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.emf.ui.properties.sections.UndoableModelPropertySheetEntry;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class Bpmn2PropertySectionBis extends AbstractPropertySection {
	
	private EObject selectedObject = null;
	private TreeViewer treeViewer;
	private PropertySheetPage propertySheetPage;
	private AdapterFactory adapterFactory = new Bpmn2ItemProviderAdapterFactory();
	private Composite master, details;
	
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		final Composite composite = getWidgetFactory().createFlatFormComposite(parent);
		createMasterArea(composite);
		createDetailsArea(composite);
	}
	
	private void createMasterArea(Composite composite) {
		master = getWidgetFactory().createComposite(composite);
		master.setLayoutData(createMasterAreaLayoutData());
		master.setLayout(new FormLayout());
		Tree eventTree = getWidgetFactory().createTree(
				master, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		eventTree.setLayoutData(createFillLayoutData());
		treeViewer = new TreeViewer(eventTree);
		treeViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
		treeViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
		treeViewer.setFilters(new ViewerFilter[] { new RootFilter(), new FlowElementFilter() });
	}
	
	private FormData createMasterAreaLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(0, 150);
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
		details = getWidgetFactory().createComposite(composite, SWT.BORDER);
		details.setLayoutData(createDetailsAreaLayoutData());
		details.setLayout(new FormLayout());
        propertySheetPage = new PropertySheetPage();
        UndoableModelPropertySheetEntry root = new UndoableModelPropertySheetEntry(
            OperationHistoryFactory.getOperationHistory());       
        root.setPropertySourceProvider(new AdapterFactoryContentProvider(adapterFactory));
        propertySheetPage.setRootEntry(root);
        propertySheetPage.createControl(details);
        propertySheetPage.getControl().setLayoutData(createFillLayoutData());
	}

	private FormData createDetailsAreaLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(master, 0);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		return data;
	}
	
	public boolean shouldUseExtraSpace() {
		return true;
	}
	
 	public void setInput(IWorkbenchPart part, ISelection selection) {
        super.setInput(part, selection);
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
