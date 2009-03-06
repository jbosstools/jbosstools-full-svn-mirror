/**
 * 
 */
package org.jboss.tools.smooks.xml.model;

import java.beans.PropertyChangeListener;

/**
 * @author Dart
 *
 */
public interface ITransformTreeNode {

	public void addNodePropetyChangeListener(PropertyChangeListener listener);
	
	public void removeNodePropetyChangeListener(PropertyChangeListener listener);
	
	public void cleanAllNodePropertyChangeListeners();
}
