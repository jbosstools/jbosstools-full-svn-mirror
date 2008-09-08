/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.javabean.model;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.jboss.tools.smooks.analyzer.IValidatable;

/**
 * @author Dart Peng
 * 
 */
public class JavaBeanModel implements IValidatable {

	private Object waring = null;

	private Object error = null;

	private String name = "";

	private Class typeRef = null;

	private boolean many = false;

	private boolean collection = false;

	private boolean isPrimitive = false;

	private Class<? extends Object> beanClass = null;

	private JavaBeanModel parent = null;

	public Class getBeanClass() {
		return beanClass;
	}

	private Class parentClass = null;

	private boolean isRoot = false;

	private boolean isRootClassModel = false;

	/**
	 * @return the isRootClassModel
	 */
	public boolean isRootClassModel() {
		return isRootClassModel;
	}

	/**
	 * @param isRootClassModel
	 *            the isRootClassModel to set
	 */
	public void setRootClassModel(boolean isRootClassModel) {
		this.isRootClassModel = isRootClassModel;
	}

	/**
	 * @return the isRoot
	 */
	public boolean isRoot() {
		return isRoot;
	}

	public String getBeanClassString() {
		Class clazz = this.getBeanClass();
		if (clazz == null)
			return "<null>";

		if (clazz.isArray()) {
			String s = clazz.getComponentType().getName();
			return s + "[]";
		}

		return clazz.getName();
	}

	/**
	 * @param isRoot
	 *            the isRoot to set
	 */
	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	private boolean isList = false;

	private Class componentClass = null;

	private boolean lazyLoadProperties = true;

	public boolean isList() {
		if(beanClass == null) return false;
		if (Collection.class.isAssignableFrom(beanClass)) {
			if (this.propertyDescriptor != null) {
				Method rmethod = propertyDescriptor.getReadMethod();
				if (rmethod != null) {
					Type returnType = rmethod.getGenericReturnType();
					if (returnType instanceof ParameterizedType) {
						Type gtype = ((ParameterizedType) returnType)
								.getActualTypeArguments()[0];
						Class beanType = (Class) gtype;
						componentClass = beanType;
					}
				}
			}
			setList(true);
		}
		return isList;
	}

	public void setList(boolean isList) {
		this.isList = isList;
	}

	private PropertyDescriptor propertyDescriptor;

	public PropertyDescriptor getPropertyDescriptor() {
		return propertyDescriptor;
	}

	public void setPropertyDescriptor(PropertyDescriptor propertyDescriptor) {
		this.propertyDescriptor = propertyDescriptor;
	}

	JavaBeanModel(Class beanClass, String beanName,
			PropertyDescriptor propertyDescriptor, Class parentClass,
			boolean lazyLoadProperties) {
		this.lazyLoadProperties = lazyLoadProperties;
		this.beanClass = beanClass;
		this.name = beanName;
		if (beanClass == null)
			return;
		if(this.name == null){
			this.name = beanClass.getSimpleName();
		}
		
		
		if (propertyDescriptor == null)
			isRoot = true;
		this.propertyDescriptor = propertyDescriptor;
		Class beanType = beanClass;
		if (beanClass.isArray()) {
			beanType = beanClass.getComponentType();
			setMany(true);
			componentClass = beanType;
		}

		if (Collection.class.isAssignableFrom(beanClass)) {
			if (this.propertyDescriptor != null) {
				Method rmethod = propertyDescriptor.getReadMethod();
				if (rmethod != null) {
					Type returnType = rmethod.getGenericReturnType();
					if (returnType instanceof ParameterizedType) {
						Type gtype = ((ParameterizedType) returnType)
								.getActualTypeArguments()[0];
						beanType = (Class) gtype;
						componentClass = beanType;
					}
				}
			}
			setList(true);
		}

		if (beanType.isPrimitive()
				|| JavaBeanModelFactory.isPrimitiveObject(beanType)) {
			setTypeRef(beanType);
			this.parentClass = parentClass;

			if (!isArray() && !isList())
				setPrimitive(true);
			return;
		}
		this.parentClass = parentClass;

		if (!lazyLoadProperties)
			this.getProperties();
	}

	JavaBeanModel(Class beanClass, String beanName,
			PropertyDescriptor propertyDescriptor) {
		this(beanClass, beanName, propertyDescriptor, null, true);
	}

	public JavaBeanModel(Class beanClass) {
		this(beanClass, null, null, null, true);
	}

	public JavaBeanModel(Class beanClass, String name) {
		this(beanClass, name, null, null, true);
	}

	JavaBeanModel(Class beanClass, boolean lazyLoadProperties) {
		this(beanClass, null, null, null, lazyLoadProperties);
	}

	public boolean isPrimitive() {
		return isPrimitive;
	}

	public void setPrimitive(boolean isPrimitive) {
		this.isPrimitive = isPrimitive;
	}

	public boolean isArray() {
		if(beanClass == null) return false;
		if (beanClass.isArray()) {
			Class beanType = beanClass.getComponentType();
			setMany(true);
			componentClass = beanType;
		}
		return many;
	}

	public void setMany(boolean many) {
		this.many = many;
	}

	public boolean isCollection() {
		return collection;
	}

	public void setCollection(boolean collection) {
		this.collection = collection;
	}

	public Class getTypeRef() {
		return typeRef;
	}

	public void setTypeRef(Class typeRef) {
		this.typeRef = typeRef;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addProperty(JavaBeanModel property) {
		if (properties != null) {
			properties.add(property);
			property.setParent(this);
		}
	}

	private List properties;

	public List getProperties() {
		
		if (properties == null) {
			properties = new ArrayList();
			Class beanType = beanClass;

			if (this.componentClass != null) {
				if (isArray() || isList()) {
					JavaBeanModel proxyModel = new JavaBeanModel(
							componentClass, componentClass.getSimpleName(),
							null, beanClass, this.lazyLoadProperties);
					beanType = componentClass;

					addProperty(proxyModel);

					return properties;
				}
			}
			if(beanType == null) return null;
			PropertyDescriptor[] pds = PropertyUtils
					.getPropertyDescriptors(beanType);

			for (int i = 0; i < pds.length; i++) {
				PropertyDescriptor pd = pds[i];
				if ("class".equals(pd.getName()))
					continue;
				JavaBeanModel jbm = new JavaBeanModel(pd.getPropertyType(), pd
						.getName(), pd, beanClass, this.lazyLoadProperties);
				addProperty(jbm);
			}
		}
		return properties;
	}

	public void setProperties(List properties) {
		this.properties = properties;
	}

	public boolean propertiesHasBeenLoaded() {
		return properties != null;
	}

	/**
	 * @return the parent
	 */
	public JavaBeanModel getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(JavaBeanModel parent) {
		this.parent = parent;
	}

	public Object getWarning() {
		return waring;
	}

	public void setWarning(Object waring) {
		this.waring = waring;
	}

	public Object getError() {
		return error;
	}

	public void setError(Object error) {
		this.error = error;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer("JavaBean Name : " + name);
		if (beanClass != null)
			buffer.append(";Class : " + this.beanClass.getName());
		if (this.properties != null) {
			buffer.append("\n");
			for (Iterator iterator = properties.iterator(); iterator.hasNext();) {
				JavaBeanModel child = (JavaBeanModel) iterator.next();
				buffer.append("\t");
				buffer.append(child.toString());
				buffer.append("\n");
			}
		}
		return buffer.toString();
	}

}
