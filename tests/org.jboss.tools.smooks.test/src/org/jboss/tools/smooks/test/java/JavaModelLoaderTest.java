/**
 * 
 */
package org.jboss.tools.smooks.test.java;

import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.javabean.model.JavaBeanModelFactory;
import org.jboss.tools.smooks.test.java2java.Header;
import org.jboss.tools.smooks.test.java2java.Order;
import org.jboss.tools.smooks.test.java2java.OrderItem;

/**
 * @author Dart
 *
 */
public class JavaModelLoaderTest extends TestCase {
	public void testJavaBeanModelFactory(){
		JavaBeanModel model = JavaBeanModelFactory.getJavaBeanModel(Order.class);
		List properties = model.getProperties();
		
		// there are 14 properties , 9 properties come from the parent class.
		Assert.assertEquals(14, properties.size());
		Assert.assertTrue(model.isRootClassModel());
		for (Iterator iterator = properties.iterator(); iterator.hasNext();) {
			JavaBeanModel property = (JavaBeanModel) iterator.next();
			// check header property
			if(property.getName().equals("header")){
				Assert.assertTrue(property.getBeanClass() == Header.class);
			}
			// check the collection property
			if(property.getName().equals("orderItems")){
				Assert.assertTrue(property.hasGenericType());
				Assert.assertTrue(property.isList());
				Assert.assertTrue(property.getGenericType() == OrderItem.class);
				// have only one model
				Assert.assertEquals(1, property.getProperties().size());
			}
			// check the array property
			if(property.getName().equals("headerArray")){
				Assert.assertTrue(property.hasGenericType());
				Assert.assertTrue(property.isArray());
				Assert.assertTrue(property.getGenericType() == Header.class);
				// have only one model
				Assert.assertEquals(1, property.getProperties().size());
			}
			if(property.getName().equals("orderCounts")){
				Assert.assertTrue(property.hasGenericType());
				Assert.assertTrue(property.isArray());
				Assert.assertTrue(property.getGenericType() == int.class);
				// have only one model
				Assert.assertEquals(1, property.getProperties().size());
			}
			if(property.getName().equals("ages")){
				Assert.assertTrue(property.hasGenericType());
				Assert.assertTrue(property.isArray());
				Assert.assertTrue(property.getGenericType() == int.class);
				// have only one model
				Assert.assertEquals(1, property.getProperties().size());
			}
			// check the parent's primitive properties
			if(property.getName().equals("stringProperty")){
				Assert.assertTrue(property.isPrimitive());
				Assert.assertTrue(property.getBeanClass() == String.class);
				// have no children model
				Assert.assertEquals(0, property.getProperties().size());
			}
			if(property.getName().equals("longProperty")){
				Assert.assertTrue(property.isPrimitive());
				Assert.assertTrue(property.getBeanClass() == Long.class);
				Assert.assertEquals(0, property.getProperties().size());
			}
			if(property.getName().equals("doubleProperty")){
				Assert.assertTrue(property.isPrimitive());
				Assert.assertTrue(property.getBeanClass() == Double.class);
				Assert.assertEquals(0, property.getProperties().size());
			}
			if(property.getName().equals("dateProperty")){
				Assert.assertTrue(property.isPrimitive());
				Assert.assertTrue(property.getBeanClass() == Date.class);
				Assert.assertEquals(0, property.getProperties().size());
			}
			if(property.getName().equals("BooleanProperty")){
				Assert.assertTrue(property.isPrimitive());
				Assert.assertTrue(property.getBeanClass() == Boolean.class);
				Assert.assertEquals(0, property.getProperties().size());
			}
			if(property.getName().equals("integerProperty")){
				Assert.assertTrue(property.isPrimitive());
				Assert.assertTrue(property.getBeanClass() == Integer.class);
				Assert.assertEquals(0, property.getProperties().size());
			}
			if(property.getName().equals("floatProperty")){
				Assert.assertTrue(property.isPrimitive());
				Assert.assertTrue(property.getBeanClass() == Float.class);
				Assert.assertEquals(0, property.getProperties().size());
			}
			if(property.getName().equals("bigIntegerProperty")){
				Assert.assertTrue(property.isPrimitive());
				Assert.assertTrue(property.getBeanClass() == BigInteger.class);
				Assert.assertEquals(0, property.getProperties().size());
			}
		}
	}
}
