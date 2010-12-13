package org.jboss.tools.bpmn2.view;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.MessagePage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.jboss.tools.bpmn2.process.diagram.part.Bpmn2DiagramEditor;

public class Bpmn2Details extends PageBookView implements ISelectionProvider,
        ISelectionChangedListener {

    private ISelection bootstrapSelection;

    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        getSelectionProvider().addSelectionChangedListener(listener);
    }

    protected IPage createDefaultPage(PageBook book) {
        MessagePage page = new MessagePage();
        initPage(page);
        page.createControl(book);
        page.setMessage("Details are not available");
        return page;
    }

    protected PageRec doCreatePage(IWorkbenchPart part) {     
        Object obj = part.getAdapter(IBpmn2DetailsPage.class);
        if (obj instanceof IBpmn2DetailsPage) {
        	IBpmn2DetailsPage page = (IBpmn2DetailsPage) obj;
            if (page instanceof IPageBookViewPage) {
				initPage((IPageBookViewPage) page);
			}
            page.createControl(getPageBook());
            return new PageRec(part, page);
        }
        return null;
    }

    protected void doDestroyPage(IWorkbenchPart part, PageRec rec) {
    	IBpmn2DetailsPage page = (IBpmn2DetailsPage) rec.page;
        page.dispose();
        rec.dispose();
    }

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class type) {
		IWorkbenchPart currentPart =  getCurrentContributingPart();
		if (type == IPropertySheetPage.class && currentPart != null && currentPart instanceof Bpmn2DiagramEditor) {
			return ((Bpmn2DiagramEditor)currentPart).getAdapter(type);
		}
		return super.getAdapter(type);
	}

	@Override
    protected IWorkbenchPart getBootstrapPart() {
        IWorkbenchPage page = getSite().getPage();
        if (page != null) {
        	bootstrapSelection = page.getSelection();
			return page.getActiveEditor();
		}
        return null;
    }

    @Override
    public void partActivated(IWorkbenchPart part) {
		super.partActivated(part);
        if (bootstrapSelection != null) {
            IPage page = getCurrentPage();
            if (page != null && page instanceof IBpmn2DetailsPage) {
				((IBpmn2DetailsPage)page).selectionChanged(part, bootstrapSelection);
	            bootstrapSelection = null;
			}
        }
    }
    
    public ISelection getSelection() {
        return getSelectionProvider().getSelection();
    }

    protected boolean isImportant(IWorkbenchPart part) {
        return (part instanceof IEditorPart);
    }

    public void partBroughtToTop(IWorkbenchPart part) {
        partActivated(part);
    }

    public void removeSelectionChangedListener(
            ISelectionChangedListener listener) {
        getSelectionProvider().removeSelectionChangedListener(listener);
    }

    public void selectionChanged(SelectionChangedEvent event) {
        getSelectionProvider().selectionChanged(event);
    }

    public void setSelection(ISelection selection) {
        getSelectionProvider().setSelection(selection);
    }

    protected void showPageRec(PageRec pageRec) {
        IPageSite pageSite = getPageSite(pageRec.page);
        ISelectionProvider provider = pageSite.getSelectionProvider();
        if (provider == null && (pageRec.page instanceof IBpmn2DetailsPage)) {
            pageSite.setSelectionProvider((IBpmn2DetailsPage) pageRec.page);
		}
        super.showPageRec(pageRec);
    }
}
