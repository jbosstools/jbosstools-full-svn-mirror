/**
 * 
 */
package org.jboss.tools.smooks.ui;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.IStartup;

/**
 * @author Dart
 *
 */
public class SmooksListenerRegister implements IStartup {

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 */
	public void earlyStartup() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new SmooksResourceChangeListener());
	}

}
