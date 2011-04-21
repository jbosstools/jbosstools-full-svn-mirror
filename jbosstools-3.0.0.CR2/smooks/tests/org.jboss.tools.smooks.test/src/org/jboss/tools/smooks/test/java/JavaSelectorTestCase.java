/**
 * 
 */
package org.jboss.tools.smooks.test.java;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.javabean.model.JavaBeanModelFactory;
import org.jboss.tools.smooks.ui.IXMLStructuredObject;
import org.jboss.tools.smooks.utils.UIUtils;

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

		IXMLStructuredObject aModel = UIUtils.localXMLNodeWithPath("a", model);
		Assert.assertNotNull(aModel);
		Assert.assertEquals(((JavaBeanModel) aModel).getBeanClass(), A.class);

		IXMLStructuredObject nullModel = UIUtils.localXMLNodeWithPath("a" + sperator
				+ "null", model, null, false);
		Assert.assertNull(nullModel);

		// avoid died loop
		nullModel =   UIUtils.localXMLNodeWithPath("null", model, null, false);
		Assert.assertNull(nullModel);
	}
}
