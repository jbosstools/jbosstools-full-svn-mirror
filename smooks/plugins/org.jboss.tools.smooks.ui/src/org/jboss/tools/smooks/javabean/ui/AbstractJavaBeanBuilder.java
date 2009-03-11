/**
 * 
 */
package org.jboss.tools.smooks.javabean.ui;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.graphical.GraphicalFactory;
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

	private ClassLoader classLoader = null;

	public static final int SOURCE_DATA = 0;
	public static final int TARGET_DATA = 1;

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

	public static String getDataSourceClassArray(GraphInformations info,
			int dataMode) {
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

	public static void appendClassToGraph(JavaBeanModel clazz,
			GraphInformations info, int dataMode) {
		String classArray = getDataSourceClassArray(info, dataMode);
		if (classArray == null) {
			classArray = "";
		}
		if (!classArray.endsWith(";") && classArray.length() != 0) {
			classArray += ";";
		}
		String newClassString = clazz.getBeanClassStringWithList();
		if (newClassString != null) {
			classArray += newClassString;
		}
		classArray = classArray.replace("<", "[");
		classArray = classArray.replace(">", "]");
		if (info != null) {
			Params params = info.getParams();
			if (params == null) {
				params = GraphicalFactory.eINSTANCE.createParams();
				info.setParams(params);
			}
			String key = "sourceDataPath"; //$NON-NLS-1$
			if (dataMode == SOURCE_DATA) {
				key = "sourceDataPath"; //$NON-NLS-1$
			}
			if (dataMode == TARGET_DATA) {
				key = "targetDataPath"; //$NON-NLS-1$
			}
			Param targetParam = null;
			;
			List paramList = params.getParam();
			for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
				Param param = (Param) iterator.next();
				if (key.equals(param.getName())) {
					targetParam = param;
					break;
				}
			}
			if (targetParam == null) {
				targetParam = GraphicalFactory.eINSTANCE.createParam();
				params.getParam().add(targetParam);
				targetParam.setName(key);
			}
			targetParam.setValue(classArray);
		}
	}

	public static void removeClassFromGraph(JavaBeanModel model,
			GraphInformations info, int dataMode) {
		String classArray = getDataSourceClassArray(info, dataMode);
		String removeClass = model.getBeanClassStringWithList();
		if (removeClass == null)
			return;
		removeClass = removeClass.replace("<", "[");
		removeClass = removeClass.replace(">", "]");
		int startIndex1 = classArray.indexOf(removeClass);
		if (startIndex1 == -1)
			return;
		int endIndex = startIndex1 + removeClass.length();
		endIndex++;
		if (classArray.length() <= endIndex) {
			endIndex--;
			if (classArray.charAt(endIndex - 1) != ';') {
				endIndex = startIndex1 + removeClass.length();
			}
		}
		classArray = classArray.substring(0, startIndex1)
				+ classArray.substring(endIndex, classArray.length());
		if (info != null && classArray != null) {
			Params params = info.getParams();
			if (params == null) {
				params = GraphicalFactory.eINSTANCE.createParams();
				info.setParams(params);
			}
			String key = "sourceDataPath"; //$NON-NLS-1$
			if (dataMode == SOURCE_DATA) {
				key = "sourceDataPath"; //$NON-NLS-1$
			}
			if (dataMode == TARGET_DATA) {
				key = "targetDataPath"; //$NON-NLS-1$
			}
			List paramList = params.getParam();
			for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
				Param param = (Param) iterator.next();
				if (key.equals(param.getName())) {
					param.setValue(classArray);
					break;
				}
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
				JavaBeanModel javaBean = null;
				if (className.indexOf("[") != -1) {
					int start = className.indexOf("[");
					int end = className.indexOf("]");
					String genericType = className.substring(start + 1, end);
					if (genericType != null) {
						genericType = genericType.trim();
					}
					String newClassName = className.substring(0, start);
					Class realClass = classLoader.loadClass(newClassName);
					if (genericType.length() == 0) {
						javaBean = JavaBeanModelFactory
								.getJavaBeanModelWithLazyLoad(Array
										.newInstance(realClass, 0).getClass());
					} else {
						Class genericClass = null;
						try {
							genericClass = classLoader.loadClass(genericType);
						} catch (Throwable t1) {

						}
						javaBean = JavaBeanModelFactory
								.getJavaBeanModelWithLazyLoad(realClass);
						if (genericClass != null)
							javaBean.setComponentClass(genericClass);
					}
				} else {
					Class clazz = classLoader.loadClass(className);
					javaBean = JavaBeanModelFactory
							.getJavaBeanModelWithLazyLoad(clazz);
				}
				if (javaBean != null) {
					list.add(javaBean);
				}
			} catch (Throwable t) {
				continue;
			}
		}
		return list;
	}
}
