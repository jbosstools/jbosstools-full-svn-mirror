package org.jboss.tools.bpmn2.view;

import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPageBookViewPage;

public class Bpmn2Definitions extends Bpmn2Details {

    protected PageRec doCreatePage(IWorkbenchPart part) {     
        Object obj = part.getAdapter(IBpmn2DefinitionsPage.class);
        if (obj instanceof IBpmn2DefinitionsPage) {
        	IBpmn2DefinitionsPage page = (IBpmn2DefinitionsPage) obj;
            if (page instanceof IPageBookViewPage) {
				initPage((IPageBookViewPage) page);
			}
            page.createControl(getPageBook());
            return new PageRec(part, page);
        }
        return null;
    }

}
