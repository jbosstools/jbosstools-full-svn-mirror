/**
 * 
 */
package org.jboss.tools.smooks.javabean.ui;

import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.ui.AbstractConnectionModelSectionFilter;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;

/**
 * @author Dart
 * 
 */
public class JavaBeanPropertiesSectonFilter extends
		AbstractConnectionModelSectionFilter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IFilter#select(java.lang.Object)
	 */
	public boolean select(Object toTest) {
		Object target = this.getReferenceTargetObject(toTest);
		if (target != null && target instanceof JavaBeanModel) {
			LineConnectionModel connection = getConnectionModel(toTest);
			if (BeanPopulatorMappingAnalyzer.REFERENCE_BINDING
					.equals(connection
							.getProperty(BeanPopulatorMappingAnalyzer.PRO_BINDING_TYPE))) {
				return false;
			}
			return true;
		}
		return false;
	}

}
