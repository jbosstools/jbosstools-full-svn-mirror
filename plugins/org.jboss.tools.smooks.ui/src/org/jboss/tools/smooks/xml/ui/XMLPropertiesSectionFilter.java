/**
 * 
 */
package org.jboss.tools.smooks.xml.ui;

import org.jboss.tools.smooks.ui.AbstractConnectionModelSectionFilter;
import org.jboss.tools.smooks.xml.model.AbstractXMLObject;

/**
 * @author Dart
 * 
 */
public class XMLPropertiesSectionFilter extends
		AbstractConnectionModelSectionFilter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IFilter#select(java.lang.Object)
	 */
	public boolean select(Object toTest) {
		Object source = getReferenceSourceObject(toTest);
		Object target = getReferenceSourceObject(toTest);
		if(target != null && target instanceof AbstractXMLObject){
			// don't allow show it when editing x2x
			return false;
		}
		if (source != null && source instanceof AbstractXMLObject) {
			return true;
		}
		return false;
	}

}
