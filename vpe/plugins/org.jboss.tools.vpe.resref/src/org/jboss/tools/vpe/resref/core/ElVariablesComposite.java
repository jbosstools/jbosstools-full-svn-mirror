package org.jboss.tools.vpe.resref.core;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.common.el.core.ELReferenceList;
import org.jboss.tools.common.resref.core.ResourceReference;
import org.jboss.tools.common.resref.core.ResourceReferenceList;
import org.jboss.tools.common.resref.ui.ResourceReferencesTableProvider;

/**
 * The Class ElVariablesComposite.
 */
public class ElVariablesComposite extends AbstractResourceReferencesComposite {

    /**
     * Creates the table provider.
     * 
     * @param dataList the data list
     * g
     * @return the resource references table provider
     */
    @Override
    protected ResourceReferencesTableProvider createTableProvider(List dataList) {
        return ResourceReferencesTableProvider.getELTableProvider(dataList);
    };

    /**
     * Gets the reference list.
     * 
     * @return the reference list
     */
    @Override
    protected ResourceReferenceList getReferenceList() {
        return ELReferenceList.getInstance();
    }

    protected ReferenceWizardDialog getDialog(ResourceReference resref) {
        return new ELReferenceWizardDialog(
				PlatformUI.getWorkbench().getDisplay().getActiveShell(), fileLocation, resref, getReferenceArray());
    }


	@Override
	protected void add(int index) {
		ResourceReference resref = getDefaultResourceReference();
		int returnCode = -1;
		ReferenceWizardDialog  d = getDialog(resref);
		if (null != d) {
			returnCode = d.open();
		}
		if (Dialog.OK == returnCode) {
			dataList.add(resref);
			update();
			table.setSelection(dataList.size() - 1);
		}
		
	}


	@Override
	protected void edit(int index) {
		if(index < 0) {
			return;
		}
		ResourceReference resref = getReferenceArray()[index];
		int returnCode = -1;
		ReferenceWizardDialog  d = getDialog(resref);
		if (null != d) {
			returnCode = d.open();
		}
		if (Dialog.OK == returnCode) {
			update();
		}
	}
}
