package org.jboss.tools.smooks.configuration.editors.uitls;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * 
 * @author Dart Peng<br>
 *         Date : Sep 10, 2008
 */
public class JavaPropertyUtils {

	private static JavaPropertyUtils instace = null;

	public static PropertyDescriptor[] getPropertyDescriptor(Class clazz) {
		try {
			return getInstace().getPropertyDescriptorArray(clazz);
		} catch (Exception e) {
			// ignore
			return new PropertyDescriptor[]{};
		}
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 * @throws IntrospectionException
	 */
	public PropertyDescriptor[] getPropertyDescriptorArray(Class clazz)
			throws IntrospectionException {
		// TODO should improve (use some catch to store the BeanInfo , right?)
		return Introspector
				.getBeanInfo(clazz, Introspector.IGNORE_ALL_BEANINFO)
				.getPropertyDescriptors();
	}

	public static JavaPropertyUtils getInstace() {
		if (instace == null) {
			instace = new JavaPropertyUtils();
		}
		return instace;
	}
}
