package org.jboss.tools.vpe.resref.core;

import java.util.List;

import org.jboss.tools.common.el.core.ELReferenceList;
import org.jboss.tools.common.meta.XAttribute;
import org.jboss.tools.common.meta.XModelEntity;
import org.jboss.tools.common.meta.constraint.impl.XAttributeConstraintFileFilter;
import org.jboss.tools.common.meta.impl.XModelMetaDataImpl;
import org.jboss.tools.common.resref.core.ResourceReference;
import org.jboss.tools.common.resref.core.ResourceReferenceList;
import org.jboss.tools.common.resref.ui.AbstractResourceReferencesComposite;
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
     * Gets the entity.
     * 
     * @return the entity
     */
    @Override
    protected String getEntity() {
        return (file != null) ? "VPEElReference" : "VPEElReferenceExt";
    }

    /**c
     * Gets the reference list.
     * 
     * @return the reference list
     */
    @Override
    protected ResourceReferenceList getReferenceList() {
        return ELReferenceList.getInstance();
    }

    /**
     * @see AbstractResourceReferencesComposite#createGroupLabel()
     */
    @Override
    protected String createGroupLabel() {
        return Messages.SUBSTITUTED_EL_EXPRESSIONS;
    }


	@Override
	protected void add(int index) {
		ResourceReference css = getDefaultResourceReference();

		initFilterInFileChooser();
		boolean ok = VpeAddReferenceSupport.add(file, css, getReferenceArray(),
				getEntity());
		if (!ok)
			return;
		dataList.add(css);
		update();
		table.setSelection(dataList.size() - 1);
		
	}


	@Override
	protected void edit(int index) {
		if (index < 0) {
			return;
		}
		ResourceReference css = getReferenceArray()[index];
		initFilterInFileChooser();
		boolean ok = VpeAddReferenceSupport.edit(file, css,
				getReferenceArray(), getEntity());
		if (ok) {
			update();
		}
	}

}
