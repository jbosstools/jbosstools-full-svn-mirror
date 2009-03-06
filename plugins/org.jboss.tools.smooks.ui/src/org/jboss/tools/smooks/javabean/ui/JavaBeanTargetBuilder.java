/**
 * 
 */
package org.jboss.tools.smooks.javabean.ui;

import java.beans.PropertyChangeEvent;
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
import org.jboss.tools.smooks.ui.editors.SmooksGraphicalFormPage;
import org.jboss.tools.smooks.ui.editors.TransformDataTreeViewer;
import org.jboss.tools.smooks.utils.ProjectClassLoader;

/**
 * @author Dart
 * 
 */
public class JavaBeanTargetBuilder extends AbstractJavaBeanBuilder implements
		ITargetModelAnalyzer, PropertyChangeListener {
	private GraphInformations graphInfo;

	public Object buildTargetInputObjects(GraphInformations graphInfo,
			SmooksResourceListType listType, IFile sourceFile, Object viewer)
			throws InvocationTargetException {
		this.graphInfo = graphInfo;
		ClassLoader classLoader = getClassLoader();
		if (classLoader == null) {
			IProject project = sourceFile.getProject();
			try {
				classLoader = new ProjectClassLoader(JavaCore.create(project));
			} catch (JavaModelException e) {
				throw new InvocationTargetException(e);
			}
		}
		JavaBeanList beanList = (JavaBeanList) buildTargetInputObjects(
				graphInfo, listType, sourceFile, viewer, getClassLoader());
		mergeJavaBeans(beanList, getTheJavaBeanFromGraphFile(classLoader,
				graphInfo,  TARGET_DATA));
		if (viewer instanceof PropertyChangeListener) {
			registeListener((PropertyChangeListener) viewer, beanList);
			List list = beanList.getChildren();
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				JavaBeanModel beanModel = (JavaBeanModel) iterator.next();
				registeListener((PropertyChangeListener) viewer, beanModel);
			}
		}
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
					// registeListener(viewer, javaBean);
					beanList.addJavaBean(javaBean);
				}
			}
		}
		// registeListener(viewer, beanList);
		return beanList;
	}

	private void registeListener(Object listener, JavaBeanModel javaBean) {
		if (listener instanceof PropertyChangeListener) {
			javaBean
					.addNodePropetyChangeListener((PropertyChangeListener) listener);
			javaBean.addNodePropetyChangeListener(this);
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

	public void propertyChange(PropertyChangeEvent evt) {
		if (graphInfo != null) {
			String type = evt.getPropertyName();
			Object node = evt.getNewValue();
			if(TransformDataTreeViewer.REMOVE_CHILDREN_EVENT
							.equals(type)){
				node = evt.getOldValue();
			}
			if (node instanceof JavaBeanModel) {
				if (!((JavaBeanModel) node).isPrimitive()) {
					if (TransformDataTreeViewer.NODE_PROPERTY_EVENT
							.equals(type)
							|| TransformDataTreeViewer.ADD_CHILDREN_EVENT
									.equals(type)) {
						appendClassToGraph((JavaBeanModel) node, graphInfo,
								TARGET_DATA);
					}
					if (TransformDataTreeViewer.REMOVE_CHILDREN_EVENT
							.equals(type)) {
						removeClassFromGraph((JavaBeanModel) node, graphInfo,
								TARGET_DATA);
					}
				}
			}
		}
	}

}
