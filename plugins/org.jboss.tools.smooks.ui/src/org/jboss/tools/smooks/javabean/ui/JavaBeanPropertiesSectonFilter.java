/**
 * 
 */
package org.jboss.tools.smooks.javabean.ui;

import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.ui.AbstractConnectionModelSectionFilter;

/**
 * @author Dart
 *
 */
public class JavaBeanPropertiesSectonFilter extends
		AbstractConnectionModelSectionFilter {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IFilter#select(java.lang.Object)
	 */
	public boolean select(Object toTest) {
			Object source =  this.getReferenceSourceObject(toTest);
			Object target = this.getReferenceTargetObject(toTest);
			if (source != null && source instanceof JavaBeanModel) {
				return true;
			}
			if (target != null && target instanceof JavaBeanModel) {
				return true;
			}
		return false;
	}

}
