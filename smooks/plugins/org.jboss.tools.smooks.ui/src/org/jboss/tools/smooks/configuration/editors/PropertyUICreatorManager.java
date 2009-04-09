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
package org.jboss.tools.smooks.configuration.editors;

import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.smooks.configuration.editors.javabean.BindingsPropertyUICreator;
import org.jboss.tools.smooks.configuration.editors.javabean.ValuePropertiesUICreator;
import org.jboss.tools.smooks.model.javabean.impl.BindingsTypeImpl;
import org.jboss.tools.smooks.model.javabean.impl.ValueTypeImpl;

/**
 * @author Dart (dpeng@redhat.com)<p>
 * Apr 7, 2009
 */
public class PropertyUICreatorManager {
	private static PropertyUICreatorManager instance;
	
	private Map<Class<?>,IPropertyUICreator> map = null;
	
	private PropertyUICreatorManager(){
		map = new HashMap<Class<?>,IPropertyUICreator>();
		init();
	}
	
	private void init() {
		map.put(BindingsTypeImpl.class, new BindingsPropertyUICreator());
		map.put(ValueTypeImpl.class, new ValuePropertiesUICreator());
	}

	public void registePropertyUICreator(Class<?> key,IPropertyUICreator creator){
		map.put(key, creator);
	}
	
	public IPropertyUICreator getPropertyUICreator(Class<?> key){
		return map.get(key);
	}
	
	public IPropertyUICreator getPropertyUICreator(Object model){
		return map.get(model.getClass());
	}

	public static synchronized PropertyUICreatorManager getInstance() {
		if(instance == null){
			instance = new PropertyUICreatorManager();
		}
		return instance;
	}
}
