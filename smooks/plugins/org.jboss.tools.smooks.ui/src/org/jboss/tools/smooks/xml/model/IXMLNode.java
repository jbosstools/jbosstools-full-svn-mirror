/**
 * 
 */
package org.jboss.tools.smooks.xml.model;

import java.beans.PropertyChangeListener;

/**
 * @author Dart
 *
 */
public interface IXMLNode {

	public void addNodePropetyChangeListener(PropertyChangeListener listener);
	
	public void removeNodePropetyChangeListener(PropertyChangeListener listener);
}
