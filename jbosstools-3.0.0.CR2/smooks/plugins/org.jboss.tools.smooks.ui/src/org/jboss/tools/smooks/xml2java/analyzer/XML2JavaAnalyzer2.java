/**
 * 
 */
package org.jboss.tools.smooks.xml2java.analyzer;

import org.jboss.tools.smooks.javabean.model.JavaBeanList;
import org.jboss.tools.smooks.javabean.ui.BeanPopulatorMappingAnalyzer;
import org.jboss.tools.smooks.xml.model.TagList;

/**
 * @author Dart
 *
 */
public class XML2JavaAnalyzer2 extends BeanPopulatorMappingAnalyzer {

	@Override
	protected boolean checkSourceAndTarget(Object sourceObject,
			Object targetObject) {
		if(sourceObject instanceof TagList && targetObject instanceof JavaBeanList){
			return true;
		}
		return false;
	}

}
