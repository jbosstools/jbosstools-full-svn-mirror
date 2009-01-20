/**
 * 
 */
package org.jboss.tools.smooks.javabean.ui;

import java.beans.PropertyChangeListener;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.tools.smooks.analyzer.ISourceModelAnalyzer;
import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.javabean.model.JavaBeanList;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.javabean.model.JavaBeanModelFactory;
import org.jboss.tools.smooks.model.AbstractResourceConfig;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.ResourceType;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.util.SmooksModelConstants;
import org.jboss.tools.smooks.utils.ProjectClassLoader;

/**
 * @author Dart
 * 
 */
public class JavaBeanSourceBuilder extends AbstractJavaBeanBuilder implements
		ISourceModelAnalyzer {

	public Object buildSourceInputObjects(GraphInformations graphInfo,
			SmooksResourceListType listType, IFile sourceFile, Object viewer)
			throws InvocationTargetException {
		ClassLoader classLoader = getClassLoader();
		if (classLoader == null) {
			IProject project = sourceFile.getProject();
			try {
				classLoader = new ProjectClassLoader(JavaCore.create(project));
			} catch (JavaModelException e) {
				throw new InvocationTargetException(e);
			}
		}
		JavaBeanList beanList = (JavaBeanList) buildSourceInputObjects(
				graphInfo, listType, sourceFile, viewer, getClassLoader());
		mergeJavaBeans(beanList, getTheJavaBeanFromGraphFile(classLoader,
				graphInfo, SOURCE_DATA));
		return beanList;
	}

	public Object buildSourceInputObjects(GraphInformations graphInfo,
			SmooksResourceListType listType, IFile sourceFile, Object viewer,
			ClassLoader classLoader) throws InvocationTargetException {
		JavaBeanList beanList = new JavaBeanList();
		if (classLoader == null) {
			IProject project = sourceFile.getProject();
			try {
				classLoader = new ProjectClassLoader(JavaCore.create(project));
			} catch (JavaModelException e) {
				if (viewer instanceof PropertyChangeListener) {
					beanList
							.addNodePropetyChangeListener((PropertyChangeListener) viewer);
				}
				throw new InvocationTargetException(e);
			}
		}
		List<AbstractResourceConfig> abstractResourceConfigList = listType
				.getAbstractResourceConfig();
		for (Iterator<AbstractResourceConfig> iterator = abstractResourceConfigList
				.iterator(); iterator.hasNext();) {
			AbstractResourceConfig abstractResourceConfig = (AbstractResourceConfig) iterator
					.next();
			if (abstractResourceConfig instanceof ResourceConfigType) {
				ResourceConfigType resourceConfig = (ResourceConfigType) abstractResourceConfig;
				ResourceType resource = resourceConfig.getResource();
				if (resource != null) {
					String resourceValue = resource.getStringValue();
					if (resourceValue != null)
						resourceValue = resourceValue.trim();
					if (SmooksModelConstants.BEAN_POPULATOR
							.equals(resourceValue)) {
						String selector = resourceConfig.getSelector();
						if (selector == null)
							continue;
						selector = selector.trim();
						Class clazz = loadClassFromSelector(selector,
								classLoader);
						if (clazz != null) {
							JavaBeanModel javaBeanModel = JavaBeanModelFactory
									.getJavaBeanModelWithLazyLoad(clazz);
							beanList.addJavaBean(javaBeanModel);
							break;
						}
					}
				}
			}
		}
		if (viewer instanceof PropertyChangeListener) {
			beanList
					.addNodePropetyChangeListener((PropertyChangeListener) viewer);
		}
		return beanList;
	}

	private Class loadClassFromSelector(String selector, ClassLoader loader)
			throws InvocationTargetException {
		try {
			if (selector.endsWith("[]")) {
				selector = selector.substring(0, selector.length() - 2);
				Class arrayClass = loader.loadClass(selector);
				Object arrayInstance = Array.newInstance(arrayClass, 0);
				return arrayInstance.getClass();
			}
			return loader.loadClass(selector);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

}
