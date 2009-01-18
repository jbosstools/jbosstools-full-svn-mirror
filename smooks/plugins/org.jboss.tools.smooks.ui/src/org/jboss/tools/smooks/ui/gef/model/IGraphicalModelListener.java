/**
 * 
 */
package org.jboss.tools.smooks.ui.gef.model;

import java.beans.PropertyChangeEvent;

import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;

/**
 * @author Dart
 * 
 */
public interface IGraphicalModelListener {
	
	public void modelAdded(Object graphicalModel,
			SmooksConfigurationFileGenerateContext context);

	public void modelRemoved(Object graphicalModel,
			SmooksConfigurationFileGenerateContext context);
	
	public void modelChanged(Object graphicalModel,
			SmooksConfigurationFileGenerateContext context , PropertyChangeEvent event);
}
