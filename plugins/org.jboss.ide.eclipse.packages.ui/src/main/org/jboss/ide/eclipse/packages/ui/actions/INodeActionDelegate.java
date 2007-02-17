package org.jboss.ide.eclipse.packages.ui.actions;

import org.eclipse.ui.IViewActionDelegate;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;

/**
 * All extensions of org.jboss.ide.eclipse.packages.ui.nodePopupMenus should implement this interface
 * (also see AbstractNodeActionDelegate)
 * @author Marshall
 *
 */
public interface INodeActionDelegate extends IViewActionDelegate {

	/**
	 * @param node
	 * @return Whether or not this action delegate will be enabled (viewable) for a specific package node.
	 */
	public boolean isEnabledFor (IPackageNode node);
}
