/**
 * 
 */
package org.jboss.tools.smooks.javabean.ui;

import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.ui.AbstractConnectionModelSectionFilter;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.xml.model.AbstractXMLObject;
import org.jboss.tools.smooks.xml.ui.XMLPropertiesSection;
import org.jboss.tools.smooks.xml2xml.XML2XMLGraphicalModelListener;

/**
 * @author Dart
 * 
 */
public class BeanPopulatorConnectionFilter extends
		AbstractConnectionModelSectionFilter {

	public boolean select(Object toTest) {
		Object target = this.getReferenceTargetObject(toTest);
		Object source = getReferenceSourceObject(toTest);
		if (target != null && target instanceof JavaBeanModel) {
			LineConnectionModel connection = getConnectionModel(toTest);
			if (BeanPopulatorMappingAnalyzer.REFERENCE_BINDING
					.equals(connection
							.getProperty(BeanPopulatorMappingAnalyzer.PRO_BINDING_TYPE))) {
				return false;
			}
			return true;
		}
		if (target != null && target instanceof AbstractXMLObject) {
			if (source != null && source instanceof AbstractXMLObject) {
				LineConnectionModel connection = getConnectionModel(toTest);
				if (XMLPropertiesSection.MAPPING.equals(connection
						.getProperty(XMLPropertiesSection.MAPPING_TYPE))) {
					return true;
				}
			}
		}

		return false;
	}

}
