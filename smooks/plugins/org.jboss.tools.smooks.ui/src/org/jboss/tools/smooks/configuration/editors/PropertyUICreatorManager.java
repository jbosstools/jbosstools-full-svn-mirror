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
import org.jboss.tools.smooks.configuration.editors.javabean.JavabeanValueUICreator;
import org.jboss.tools.smooks.configuration.editors.javabean.JavabeanWiringUICreator;
import org.jboss.tools.smooks.configuration.editors.smooks.ConditionTypeUICreator;
import org.jboss.tools.smooks.configuration.editors.smooks.ImportTypeUICreator;
import org.jboss.tools.smooks.configuration.editors.smooks.ParamTypeUICreator;
import org.jboss.tools.smooks.configuration.editors.smooks.ResourceConfigTypeUICreator;
import org.jboss.tools.smooks.configuration.editors.smooks.ResourceTypeUICreator;
import org.jboss.tools.smooks.configuration.editors.xsl.BindToUICreator;
import org.jboss.tools.smooks.configuration.editors.xsl.OutputToUICreator;
import org.jboss.tools.smooks.configuration.editors.xsl.TemplateUICreator;
import org.jboss.tools.smooks.configuration.editors.xsl.XslUICreator;
import org.jboss.tools.smooks.model.javabean.impl.BindingsTypeImpl;
import org.jboss.tools.smooks.model.javabean.impl.ValueTypeImpl;
import org.jboss.tools.smooks.model.javabean.impl.WiringTypeImpl;
import org.jboss.tools.smooks.model.smooks.impl.ConditionTypeImpl;
import org.jboss.tools.smooks.model.smooks.impl.ImportTypeImpl;
import org.jboss.tools.smooks.model.smooks.impl.ParamTypeImpl;
import org.jboss.tools.smooks.model.smooks.impl.ResourceConfigTypeImpl;
import org.jboss.tools.smooks.model.smooks.impl.ResourceTypeImpl;
import org.jboss.tools.smooks.model.xsl.impl.BindToImpl;
import org.jboss.tools.smooks.model.xsl.impl.OutputToImpl;
import org.jboss.tools.smooks.model.xsl.impl.TemplateImpl;
import org.jboss.tools.smooks.model.xsl.impl.XslImpl;

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
		map.put(ValueTypeImpl.class, new JavabeanValueUICreator());
		map.put(WiringTypeImpl.class, new JavabeanWiringUICreator());
		
		// for smooks models 
		map.put(ConditionTypeImpl.class, new ConditionTypeUICreator());
		map.put(ImportTypeImpl.class, new ImportTypeUICreator());
		map.put(ParamTypeImpl.class, new ParamTypeUICreator());
		map.put(ResourceConfigTypeImpl.class, new ResourceConfigTypeUICreator());
		map.put(ResourceTypeImpl.class, new ResourceTypeUICreator());
		
		// for xsl models
		map.put(BindToImpl.class, new BindToUICreator());
		map.put(OutputToImpl.class, new OutputToUICreator());
		map.put(TemplateImpl.class, new TemplateUICreator());
		map.put(XslImpl.class, new XslUICreator());

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
