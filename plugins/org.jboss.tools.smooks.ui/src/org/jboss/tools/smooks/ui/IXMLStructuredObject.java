/**
 * 
 */
package org.jboss.tools.smooks.ui;

import java.util.List;

/**
 * @author Dart
 *
 */
public interface IXMLStructuredObject {
	
	public String getNodeName();
	
	public List<IXMLStructuredObject> getChildren();
	
	public IXMLStructuredObject getParent();
	
	public Object getID();
	
	public boolean isAttribute();
}
