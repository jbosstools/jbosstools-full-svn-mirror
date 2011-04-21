/**
 * 
 */
package org.jboss.tools.smooks.javabean.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.graphical.Param;
import org.jboss.tools.smooks.graphical.Params;
import org.jboss.tools.smooks.javabean.model.JavaBeanList;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.javabean.model.JavaBeanModelFactory;

/**
 * @author Dart
 * 
 */
public class AbstractJavaBeanBuilder {
	public static final int SOURCE_DATA = 0;
	public static final int TARGET_DATA = 1;
	private ClassLoader classLoader = null;

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	protected boolean hasSameNameBean(JavaBeanList list, JavaBeanModel model) {
		List children = list.getChildren();
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			JavaBeanModel object = (JavaBeanModel) iterator.next();
			if (object.getBeanClass().getName().equals(
					model.getBeanClass().getName())) {
				return true;
			}
		}
		return false;
	}

	public void mergeJavaBeans(JavaBeanList beanList, List javabeans) {
		for (Iterator iterator = javabeans.iterator(); iterator.hasNext();) {
			JavaBeanModel javaBean = (JavaBeanModel) iterator.next();
			if (!hasSameNameBean(beanList, javaBean)) {
				beanList.addJavaBean(javaBean);
			}
		}
	}

	public List getTheJavaBeanFromGraphFile(ClassLoader classLoader,
			GraphInformations graphInfo, int dataMode) {
		String array = getDataSourceClassArray(graphInfo, dataMode);
		List list = new ArrayList();
		if (array == null)
			return list;
		String[] classes = array.split(";");
		for (int i = 0; i < classes.length; i++) {
			String className = classes[i];
			if (className != null)
				className.trim();
			if (className.length() == 0)
				continue;
			try {
				Class clazz = classLoader.loadClass(className);
				JavaBeanModel javaBean = JavaBeanModelFactory
						.getJavaBeanModelWithLazyLoad(clazz);
				if (javaBean != null) {
					list.add(javaBean);
				}
			} catch (Throwable t) {
				continue;
			}
		}
		return list;
	}

	private String getDataSourceClassArray(GraphInformations info, int dataMode) {
		String key = "sourceDataPath"; //$NON-NLS-1$
		if (dataMode == SOURCE_DATA) {
			key = "sourceDataPath"; //$NON-NLS-1$
		}
		if (dataMode == TARGET_DATA) {
			key = "targetDataPath"; //$NON-NLS-1$
		}
		if (info != null) {
			Params params = info.getParams();
			if (params != null) {
				List paramList = params.getParam();
				for (Iterator iterator = paramList.iterator(); iterator
						.hasNext();) {
					Param param = (Param) iterator.next();
					if (key.equals(param.getName())) {
						return param.getValue();
					}
				}
			}
		}
		return null;
	}

}
