/**
 * 
 */
package org.jboss.tools.smooks.javabean.model;

import org.jboss.tools.smooks.javabean.ui.BeanPopulatorMappingAnalyzer;

/**
 * @author Dart
 *
 */
public class Java2JavaAnalyzer extends BeanPopulatorMappingAnalyzer {

	@Override
	protected boolean checkSourceAndTarget(Object sourceObject,
			Object targetObject) {
		if(sourceObject instanceof JavaBeanList &&
				targetObject instanceof JavaBeanList) 
			return true;
		return false;
	}
	
}
