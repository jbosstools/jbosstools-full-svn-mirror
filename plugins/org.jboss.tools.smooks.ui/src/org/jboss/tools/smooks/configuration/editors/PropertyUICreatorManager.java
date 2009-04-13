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

import org.jboss.tools.smooks.configuration.editors.edi.ComponentUICreator;
import org.jboss.tools.smooks.configuration.editors.edi.DelimitersUICreator;
import org.jboss.tools.smooks.configuration.editors.edi.DescriptionUICreator;
import org.jboss.tools.smooks.configuration.editors.edi.EdiMapUICreator;
import org.jboss.tools.smooks.configuration.editors.edi.FieldUICreator;
import org.jboss.tools.smooks.configuration.editors.edi.MappingNodeUICreator;
import org.jboss.tools.smooks.configuration.editors.edi.SegmentUICreator;
import org.jboss.tools.smooks.configuration.editors.edi.SegmentsUICreator;
import org.jboss.tools.smooks.configuration.editors.edi.SubComponentUICreator;
import org.jboss.tools.smooks.configuration.editors.filerouting.HighWaterMarkUICreator;
import org.jboss.tools.smooks.configuration.editors.filerouting.OutputStreamUICreator;
import org.jboss.tools.smooks.configuration.editors.freemarker.FreemarkerUICreator;
import org.jboss.tools.smooks.configuration.editors.freemarker.InlineUICreator;
import org.jboss.tools.smooks.configuration.editors.freemarker.UseUICreator;
import org.jboss.tools.smooks.configuration.editors.groovy.GroovyUICreator;
import org.jboss.tools.smooks.configuration.editors.groovy.ScriptUICreator;
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
import org.jboss.tools.smooks.model.edi.impl.ComponentImpl;
import org.jboss.tools.smooks.model.edi.impl.DelimitersImpl;
import org.jboss.tools.smooks.model.edi.impl.DescriptionImpl;
import org.jboss.tools.smooks.model.edi.impl.EdiMapImpl;
import org.jboss.tools.smooks.model.edi.impl.FieldImpl;
import org.jboss.tools.smooks.model.edi.impl.MappingNodeImpl;
import org.jboss.tools.smooks.model.edi.impl.SegmentImpl;
import org.jboss.tools.smooks.model.edi.impl.SegmentsImpl;
import org.jboss.tools.smooks.model.edi.impl.SubComponentImpl;
import org.jboss.tools.smooks.model.fileRouting.impl.HighWaterMarkImpl;
import org.jboss.tools.smooks.model.fileRouting.impl.OutputStreamImpl;
import org.jboss.tools.smooks.model.freemarker.impl.FreemarkerImpl;
import org.jboss.tools.smooks.model.freemarker.impl.InlineImpl;
import org.jboss.tools.smooks.model.freemarker.impl.UseImpl;
import org.jboss.tools.smooks.model.groovy.impl.GroovyImpl;
import org.jboss.tools.smooks.model.groovy.impl.ScriptTypeImpl;
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
 * @author Dart (dpeng@redhat.com)
 *         <p>
 *         Apr 7, 2009
 */
public class PropertyUICreatorManager {
	private static PropertyUICreatorManager instance;

	private Map<Class<?>, IPropertyUICreator> map = null;

	private PropertyUICreatorManager() {
		map = new HashMap<Class<?>, IPropertyUICreator>();
		init();
	}

	private void init() {
		/*
		 * below if for smooks1.1
		 */
		// for javabean
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

		// for freemarker
		map.put(org.jboss.tools.smooks.model.freemarker.impl.BindToImpl.class,
			new org.jboss.tools.smooks.configuration.editors.freemarker.BindToUICreator());
		map.put(FreemarkerImpl.class, new FreemarkerUICreator());
		map.put(InlineImpl.class, new InlineUICreator());
		map.put(org.jboss.tools.smooks.model.freemarker.impl.OutputToImpl.class,
			new org.jboss.tools.smooks.configuration.editors.freemarker.OutputToUICreator());
		map.put(org.jboss.tools.smooks.model.freemarker.impl.TemplateImpl.class,
			new org.jboss.tools.smooks.configuration.editors.freemarker.TemplateUICreator());
		map.put(UseImpl.class, new UseUICreator());
		
		// for groovy
		map.put(GroovyImpl.class, new GroovyUICreator());
		map.put(ScriptTypeImpl.class, new ScriptUICreator());
		
		// for filerouting
		map.put(HighWaterMarkImpl.class, new HighWaterMarkUICreator());
		map.put(OutputStreamImpl.class, new OutputStreamUICreator());
		
		// for edi
		map.put(ComponentImpl.class, new ComponentUICreator());
		map.put(DelimitersImpl.class, new DelimitersUICreator());
		map.put(DescriptionImpl.class, new DescriptionUICreator());
		map.put(EdiMapImpl.class, new EdiMapUICreator());
		map.put(FieldImpl.class, new FieldUICreator());
		map.put(MappingNodeImpl.class, new MappingNodeUICreator());
		map.put(SegmentImpl.class, new SegmentUICreator());
		map.put(SegmentsImpl.class, new SegmentsUICreator());
		map.put(SubComponentImpl.class, new SubComponentUICreator());

		/*
		 * up if for smooks1.1
		 */

	}

	public void registePropertyUICreator(Class<?> key, IPropertyUICreator creator) {
		map.put(key, creator);
	}

	public IPropertyUICreator getPropertyUICreator(Class<?> key) {
		return map.get(key);
	}

	public IPropertyUICreator getPropertyUICreator(Object model) {
		return map.get(model.getClass());
	}

	public static synchronized PropertyUICreatorManager getInstance() {
		if (instance == null) {
			instance = new PropertyUICreatorManager();
		}
		return instance;
	}
}
