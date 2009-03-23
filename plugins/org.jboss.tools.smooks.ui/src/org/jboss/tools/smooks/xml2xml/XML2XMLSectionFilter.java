/**
 * 
 */
package org.jboss.tools.smooks.xml2xml;

import org.jboss.tools.smooks.ui.AbstractConnectionModelSectionFilter;
import org.jboss.tools.smooks.xml.model.AbstractXMLObject;

/**
 * @author DartPeng
 * 
 */
public class XML2XMLSectionFilter extends AbstractConnectionModelSectionFilter {

	public boolean select(Object toTest) {
		Object source = getReferenceSourceObject(toTest);
		Object target = getReferenceTargetObject(toTest);
		if (source instanceof AbstractXMLObject
				&& target instanceof AbstractXMLObject)
			return true;
		return false;
	}

}
