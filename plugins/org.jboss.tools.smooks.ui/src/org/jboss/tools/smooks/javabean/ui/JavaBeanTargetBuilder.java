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
import org.jboss.tools.smooks.analyzer.ITargetModelAnalyzer;
import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.javabean.model.JavaBeanList;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.javabean.model.JavaBeanModelFactory;
import org.jboss.tools.smooks.model.AbstractResourceConfig;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.ResourceType;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.util.SmooksModelConstants;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;
import org.jboss.tools.smooks.utils.ProjectClassLoader;

/**
 * @author Dart
 * 
 */
public class JavaBeanTargetBuilder extends AbstractJavaBeanBuilder implements
		ITargetModelAnalyzer {

	public Object buildTargetInputObjects(GraphInformations graphInfo,
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
		JavaBeanList beanList = (JavaBeanList) buildTargetInputObjects(graphInfo, listType, sourceFile, viewer,
				getClassLoader());
		mergeJavaBeans(beanList, getTheJavaBeanFromGraphFile(classLoader, graphInfo, TARGET_DATA));
		return beanList;
	}

	public Object buildTargetInputObjects(GraphInformations graphInfo,
			SmooksResourceListType listType, IFile sourceFile, Object viewer,
			ClassLoader classLoader) throws InvocationTargetException {
		JavaBeanList beanList = new JavaBeanList();
		if (classLoader == null) {
			IProject project = sourceFile.getProject();
			try {
				classLoader = new ProjectClassLoader(JavaCore.create(project));
			} catch (JavaModelException e) {
				registeListener(viewer, beanList);
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
					if (!SmooksModelConstants.BEAN_POPULATOR
							.equals(resourceValue)) {
						continue;
					}
					String beanClassString = SmooksModelUtils.getParmaText(
							SmooksModelConstants.BEAN_CLASS, resourceConfig);
					if (beanClassString == null)
						continue;
					beanClassString = beanClassString.trim();
					Class clazz = loadClassFormBeanClassString(beanClassString,
							classLoader);
					if (clazz == null)
						continue;
					JavaBeanModel javaBean = JavaBeanModelFactory
							.getJavaBeanModelWithLazyLoad(clazz);
					registeListener(viewer, javaBean);
					beanList.addJavaBean(javaBean);
				}
			}
		}
		registeListener(viewer, beanList);
		return beanList;
	}

	private void registeListener(Object listener, JavaBeanModel javaBean) {
		if (listener instanceof PropertyChangeListener) {
			javaBean
					.addNodePropetyChangeListener((PropertyChangeListener) listener);
		}
	}

	protected Class loadClassFormBeanClassString(String beanClassString,
			ClassLoader classLoader) {

		try {
			if (beanClassString.endsWith("[]")) {
				beanClassString = beanClassString.substring(0, beanClassString
						.length() - 2);
				Class arrayClass = classLoader.loadClass(beanClassString);
				Object arrayInstance = Array.newInstance(arrayClass, 0);
				Class clazz = arrayInstance.getClass();
				arrayInstance = null;
				return clazz;
			}
			return classLoader.loadClass(beanClassString);
		} catch (Exception e) {

		}
		return null;
	}

}
