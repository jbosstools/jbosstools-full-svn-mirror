/**
 * 
 */
package org.jboss.tools.smooks.javabean.ui;

/**
 * @author Dart
 *
 */
public class AbstractJavaBeanBuilder {
	private ClassLoader classLoader = null;

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
}
