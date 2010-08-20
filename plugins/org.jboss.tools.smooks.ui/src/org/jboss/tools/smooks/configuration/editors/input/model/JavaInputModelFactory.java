/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors.input.model;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.dom.DOMResult;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.tools.smooks.configuration.SmooksModelUtils;
import org.jboss.tools.smooks.configuration.editors.uitls.ProjectClassLoader;
import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.core.IParam;
import org.jboss.tools.smooks.templating.model.ModelBuilderException;
import org.milyn.Smooks;
import org.milyn.payload.JavaSource;
import org.w3c.dom.Document;

/**
 * Java Source Input Model Factory.
 * 
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class JavaInputModelFactory extends AbstractInputModelFactory {

	private Smooks smooksRuntime = new Smooks();
	private JavaGraphBuilder graphBuilder = new JavaGraphBuilder();
	
	public Document getModel(SmooksModel smooksConfigModel, IJavaProject project) throws ModelBuilderException {
		IParam inputSourceParam = getInputSourceParam(SmooksModelUtils.INPUT_TYPE_JAVA, smooksConfigModel);

		try {
			ProjectClassLoader classLoader = new ProjectClassLoader(project);
			Class theModelClass = classLoader.loadClass(inputSourceParam.getValue().trim());
			Object objectGraph = graphBuilder.buildGraph(theModelClass);
			DOMResult domResult = new DOMResult();
			
			// Filter a populated object model through an actual smooks runtime instance.
			// this ensures that the generated model will be exactly the same as that seen
			// by the smooks instance at runtime...
			smooksRuntime.filterSource(new JavaSource(objectGraph), domResult);
			
			return (Document) domResult.getNode();
		} catch (JavaModelException e) {
			throw new ModelBuilderException("Error build project classpath.", e);  //$NON-NLS-1$
		} catch (ClassNotFoundException e) {
			throw new ModelBuilderException("Java Class '" + inputSourceParam.getValue().trim() + "' not found on project classpath.", e);  //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	public static class JavaGraphBuilder {

	    public <T> T buildGraph(Class<T> messageType) {
	        try {
	            return buildObject(messageType);
	        } catch (Exception e) {
	        	e.printStackTrace();
	            throw new IllegalArgumentException("Unable to construct an instance of '" + messageType.getName() + "'", e);
	        }
	    }

	    private <T> T buildObject(Class<T> objectType) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {

	        if(String.class.isAssignableFrom(objectType)) {
	            return objectType.cast("x");
	        } else if(Number.class.isAssignableFrom(objectType)) {
	            return objectType.getConstructor(String.class).newInstance("1");
	        } else if(objectType.isPrimitive()) {
	            return (T) primitiveToObjectMap.get(objectType);
	        } else if(objectType == Object.class) {
	            // don't construct raw Object types... leave them and just return null...
	            return null;
	        }

	        T messageInstance = objectType.newInstance();

	        // populate all the fields...
	        Method[] methods = objectType.getMethods();
	        for(Method method : methods) {
	            if(method.getName().startsWith("set") && method.getParameterTypes().length == 1) {
	                Class<?> propertyType = method.getParameterTypes()[0];
	                Object propertyInstance = null;

	                if(Collection.class.isAssignableFrom(propertyType)) {
	                    propertyInstance = buildCollection(method, propertyType);
	                } else if(propertyType.isArray()) {
		                propertyInstance = buildArray(method, propertyType);
	                } else {
	                    propertyInstance = buildObject(propertyType);
	                }

	                if(propertyInstance != null) {
	                    method.invoke(messageInstance, propertyInstance);
	                }
	            }
	        }

	        return messageInstance;
	    }

		private Object buildArray(Method method, Class<?> propertyType) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
			Class<?> arrayType = propertyType.getComponentType();
	        Object[] arrayObj = (Object[]) Array.newInstance(arrayType, 1);
	        
	        Array.set(arrayObj, 0, buildObject(arrayType));
			
			return arrayObj;
		}

		private Object buildCollection(Method method, Class<?> propertyType) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
			Type genericType = method.getGenericParameterTypes()[0];

			if(genericType instanceof ParameterizedType) {
			    ParameterizedType genericTypeClass = (ParameterizedType) genericType;
			    Collection collection = null;

			    if(!propertyType.isInterface()) {
			    	// It's a concrete Collection type... just create an instance...
			    	collection = (Collection) propertyType.newInstance();
				}else if(List.class.isAssignableFrom(propertyType)) {
					collection = new ArrayList();
			    } else if(Set.class.isAssignableFrom(propertyType)) {
					collection = new LinkedHashSet();
				}
				
				if(collection != null) {
					collection.add(buildObject((Class<Object>) genericTypeClass.getActualTypeArguments()[0]));
				    return collection;
				}
			}
			
			return null;
		}
	    
	    private static final Map<Class, Object> primitiveToObjectMap;

	    static {
	        primitiveToObjectMap = new HashMap<Class, Object>();
	        primitiveToObjectMap.put(int.class, 1);
	        primitiveToObjectMap.put(long.class, 1L);
	        primitiveToObjectMap.put(boolean.class, true);
	        primitiveToObjectMap.put(float.class, 1f);
	        primitiveToObjectMap.put(double.class, 1d);
	        primitiveToObjectMap.put(char.class, '1');
	        primitiveToObjectMap.put(byte.class, Byte.parseByte("1"));
	        primitiveToObjectMap.put(short.class, 1);
	    }

	}
}
