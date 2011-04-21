/**
 * 
 */
package org.jboss.tools.smooks.ui.popup;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;

/**
 * @author Dart
 * 
 */
public interface ISmooksAction extends IAction {
	public SmooksConfigurationFileGenerateContext getSmooksContext();

	public void setSmooksContext(SmooksConfigurationFileGenerateContext context);

	public void selectionChanged(ISelection selection);
}
