/**
 * @author Erick
 * Created on 14.07.2005
 */
package org.jboss.tools.vpe.editor.toolbar;

import org.eclipse.swt.widgets.Composite;

/**
 * This interface create a toolbarmanager for swt toolbar in the VPE. 
 * To use it procede only in the following order: first call 
 * createToolBarComposite(composite) where composite is the parent for the future toolbar
 * then call addToolBar(IvpeToolbar) to add it and finally createMenuComposite(Composite) to add it to the menu bar. 
 * @author Igels
 */
public interface IVpeToolBarManager {

	/**
	 * This method create a splitter in the given composite
	 * @param parent
	 * @return
	 */
	public Composite createToolBarComposite(Composite parent);

	/**
	 * This method add the Toolbar to the splitter with the scpecified layout
	 * @param bar
	 */
	public void addToolBar(IVpeToolBar bar);

	/**
	 * This method create and add a new menuitem to the toolbars Menu. 
	 * @param parent
	 * @return
	 */
	public Composite createMenuComposite(Composite parent);
}