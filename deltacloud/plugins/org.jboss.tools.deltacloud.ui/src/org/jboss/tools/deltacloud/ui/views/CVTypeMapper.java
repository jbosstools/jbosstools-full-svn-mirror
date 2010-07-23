package org.jboss.tools.deltacloud.ui.views;

import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.ui.views.properties.tabbed.AbstractTypeMapper;

public class CVTypeMapper extends AbstractTypeMapper {

	@SuppressWarnings("unchecked")
	@Override
	public Class mapType(Object object) {
        if (object instanceof TreeNode) {
            return ((TreeNode) object).getValue().getClass();
        }
        return super.mapType(object);
    }

}
