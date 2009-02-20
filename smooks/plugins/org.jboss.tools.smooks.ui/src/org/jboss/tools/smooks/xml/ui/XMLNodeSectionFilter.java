/**
 * 
 */
package org.jboss.tools.smooks.xml.ui;

import org.eclipse.jface.viewers.IFilter;
import org.jboss.tools.smooks.xml.model.AbstractXMLObject;

/**
 * @author Dart
 * 
 */
public class XMLNodeSectionFilter implements IFilter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IFilter#select(java.lang.Object)
	 */
	public boolean select(Object toTest) {
		if (toTest instanceof AbstractXMLObject) {
			return ((AbstractXMLObject) toTest).isCanEdit();
		}
		return false;
	}

}
