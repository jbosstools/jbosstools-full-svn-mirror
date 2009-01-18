/**
 * 
 */
package org.jboss.tools.smooks.test.java2java;

import java.util.List;

import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.javabean.model.JavaBeanModelFactory;

/**
 * @author Dart
 *
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JavaBeanModel model = JavaBeanModelFactory.getJavaBeanModelWithLazyLoad(InterfaceBean.class);
		System.out.println(model);
		List properties = model.getProperties();
		System.out.println(properties);
	}

}
