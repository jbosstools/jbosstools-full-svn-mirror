package org.jboss.tools.bpmn2.process.diagram.sheet;

import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.provider.Bpmn2ItemProviderAdapterFactory;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class Bpmn2PropertySectionBis extends AbstractPropertySection {
	
	private EObject selectedObject = null;
	private TreeViewer treeViewer;
	private AdapterFactory adapterFactory = new Bpmn2ItemProviderAdapterFactory();
	
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		final Composite composite = getWidgetFactory().createFlatFormComposite(parent);
		createMasterArea(composite);
		createDetailsArea(composite);
	}
	
	private void createMasterArea(Composite composite) {
		Tree eventTree = getWidgetFactory().createTree(
				composite, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		eventTree.setLayoutData(createEventTreeLayoutData());
		treeViewer = new TreeViewer(eventTree);
		treeViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
		treeViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
		treeViewer.setFilters(new ViewerFilter[] { new RootFilter(), new FlowElementFilter() });
	}
	
	private FormData createEventTreeLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(20, 0);
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		return data;
	}
	
	private void createDetailsArea(Composite composite) {
		
	}

	public boolean shouldUseExtraSpace() {
		return true;
	}
	
 	public void setInput(IWorkbenchPart part, ISelection selection) {
        super.setInput(part, selection);
        selectedObject = unwrapSelection(selection);
        treeViewer.setInput(selectedObject.eContainer());
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
