/**
 * 
 */
package org.jboss.tools.smooks.test.java;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.jboss.tools.smooks.javabean.analyzer.JavaBeanAnalyzer;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.javabean.model.JavaBeanModelFactory;

/**
 * @author Dart
 * 
 */
public class JavaSelectorTestCase extends TestCase {
	public void testLocalJavaBean() {
		localJavabean(" ");
	}

	public void localJavabean(String sperator) {
		JavaBeanModel model = JavaBeanModelFactory
				.getJavaBeanModelWithLazyLoad(A.class);

		JavaBeanModel aModel = JavaBeanAnalyzer.localJavaBeanModelWithSelector(
				"a", model);
		Assert.assertNotNull(aModel);
		Assert.assertEquals(aModel.getBeanClass(), A.class);

		JavaBeanModel nullModel = JavaBeanAnalyzer
				.localJavaBeanModelWithSelectorWithoutException("a" + sperator
						+ "null", model);
		Assert.assertNull(nullModel);

		// avoid died loop
		nullModel = JavaBeanAnalyzer
				.localJavaBeanModelWithSelectorWithoutException("null", model);
		Assert.assertNull(nullModel);
	}
}
