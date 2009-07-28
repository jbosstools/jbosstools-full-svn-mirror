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

import org.jboss.tools.smooks.configuration.editors.calc.CounterUICreator;
import org.jboss.tools.smooks.configuration.editors.csv.CsvReaderUICreator;
import org.jboss.tools.smooks.configuration.editors.csv12.Csv12ReaderBindingPropertyUICreator;
import org.jboss.tools.smooks.configuration.editors.csv12.Csv12ReaderMapBindingPropertyUICreator;
import org.jboss.tools.smooks.configuration.editors.datasource.DataSourceJndiUICreator;
import org.jboss.tools.smooks.configuration.editors.datasource.DirectUICreator;
import org.jboss.tools.smooks.configuration.editors.dbrouting.ExecutorUICreator;
import org.jboss.tools.smooks.configuration.editors.dbrouting.ResultSetRowSelectorUICreator;
import org.jboss.tools.smooks.configuration.editors.dbrouting.ResultSetUICreator;
import org.jboss.tools.smooks.configuration.editors.edi.ComponentUICreator;
import org.jboss.tools.smooks.configuration.editors.edi.DelimitersUICreator;
import org.jboss.tools.smooks.configuration.editors.edi.DescriptionUICreator;
import org.jboss.tools.smooks.configuration.editors.edi.EdiMapUICreator;
import org.jboss.tools.smooks.configuration.editors.edi.FieldUICreator;
import org.jboss.tools.smooks.configuration.editors.edi.MappingNodeUICreator;
import org.jboss.tools.smooks.configuration.editors.edi.SegmentUICreator;
import org.jboss.tools.smooks.configuration.editors.edi.SegmentsUICreator;
import org.jboss.tools.smooks.configuration.editors.edi.SubComponentUICreator;
import org.jboss.tools.smooks.configuration.editors.edireader.EDIReaderUICreator;
import org.jboss.tools.smooks.configuration.editors.esbrouter.RouteBeanPropertyUICreator;
import org.jboss.tools.smooks.configuration.editors.filerouting.HighWaterMarkUICreator;
import org.jboss.tools.smooks.configuration.editors.filerouting.OutputStreamUICreator;
import org.jboss.tools.smooks.configuration.editors.freemarker.FreemarkerUICreator;
import org.jboss.tools.smooks.configuration.editors.freemarker.InlineUICreator;
import org.jboss.tools.smooks.configuration.editors.freemarker.UseUICreator;
import org.jboss.tools.smooks.configuration.editors.groovy.GroovyUICreator;
import org.jboss.tools.smooks.configuration.editors.groovy.ScriptUICreator;
import org.jboss.tools.smooks.configuration.editors.iorouting.IORouterUICreator;
import org.jboss.tools.smooks.configuration.editors.javabean.BindingsPropertyUICreator;
import org.jboss.tools.smooks.configuration.editors.javabean.JavabeanExpressionUICreator;
import org.jboss.tools.smooks.configuration.editors.javabean.JavabeanValueUICreator;
import org.jboss.tools.smooks.configuration.editors.javabean.JavabeanWiringUICreator;
import org.jboss.tools.smooks.configuration.editors.javabean12.JavaBean12PropertyUICreator;
import org.jboss.tools.smooks.configuration.editors.javabean12.Javabean12ExpressionUICreator;
import org.jboss.tools.smooks.configuration.editors.javabean12.JavabeanValueBinding12UICreator;
import org.jboss.tools.smooks.configuration.editors.javabean12.JavabeanWiringBiding12UICreator;
import org.jboss.tools.smooks.configuration.editors.jms.ConnectionUICreator;
import org.jboss.tools.smooks.configuration.editors.jms.JmsRouterUICreator;
import org.jboss.tools.smooks.configuration.editors.jms.JndiUICreator;
import org.jboss.tools.smooks.configuration.editors.jms.MessageUICreator;
import org.jboss.tools.smooks.configuration.editors.jms.SessionUICreator;
import org.jboss.tools.smooks.configuration.editors.jms12.JMSRouter12UICreator;
import org.jboss.tools.smooks.configuration.editors.json.JsonReaderUICreator;
import org.jboss.tools.smooks.configuration.editors.json.KeyMapUICreator;
import org.jboss.tools.smooks.configuration.editors.json.KeyUICreator;
import org.jboss.tools.smooks.configuration.editors.persistence12.DeleterUICreator;
import org.jboss.tools.smooks.configuration.editors.persistence12.FlusherUICreator;
import org.jboss.tools.smooks.configuration.editors.persistence12.InserterUICreator;
import org.jboss.tools.smooks.configuration.editors.persistence12.LocatorExpressionParamUICreator;
import org.jboss.tools.smooks.configuration.editors.persistence12.LocatorUICreator;
import org.jboss.tools.smooks.configuration.editors.persistence12.LocatorValueParamUICreator;
import org.jboss.tools.smooks.configuration.editors.persistence12.LocatorWiringParamUICreator;
import org.jboss.tools.smooks.configuration.editors.persistence12.UpdaterUICreator;
import org.jboss.tools.smooks.configuration.editors.rule10.Rules10RulebaseUICreator;
import org.jboss.tools.smooks.configuration.editors.smooks.ConditionTypeUICreator;
import org.jboss.tools.smooks.configuration.editors.smooks.ConditionsTypeUICreator;
import org.jboss.tools.smooks.configuration.editors.smooks.FeaturesTypeUICreator;
import org.jboss.tools.smooks.configuration.editors.smooks.HandlerTypeUICreator;
import org.jboss.tools.smooks.configuration.editors.smooks.HandlersTypeUICreator;
import org.jboss.tools.smooks.configuration.editors.smooks.ImportTypeUICreator;
import org.jboss.tools.smooks.configuration.editors.smooks.ParamTypeUICreator;
import org.jboss.tools.smooks.configuration.editors.smooks.ParamsTypeUICreator;
import org.jboss.tools.smooks.configuration.editors.smooks.ProfileTypeUICreator;
import org.jboss.tools.smooks.configuration.editors.smooks.ProfilesTypeUICreator;
import org.jboss.tools.smooks.configuration.editors.smooks.ReaderTypeUICreator;
import org.jboss.tools.smooks.configuration.editors.smooks.ResourceConfigTypeUICreator;
import org.jboss.tools.smooks.configuration.editors.smooks.ResourceTypeUICreator;
import org.jboss.tools.smooks.configuration.editors.smooks.SetOffTypeUICreator;
import org.jboss.tools.smooks.configuration.editors.smooks.SetOnTypeUICreator;
import org.jboss.tools.smooks.configuration.editors.smooks.SmooksResourceListTypeUICreator;
import org.jboss.tools.smooks.configuration.editors.validation10.RuleUICreator;
import org.jboss.tools.smooks.configuration.editors.xsl.BindToUICreator;
import org.jboss.tools.smooks.configuration.editors.xsl.OutputToUICreator;
import org.jboss.tools.smooks.configuration.editors.xsl.TemplateUICreator;
import org.jboss.tools.smooks.configuration.editors.xsl.XslUICreator;
import org.jboss.tools.smooks.model.calc.impl.CounterImpl;
import org.jboss.tools.smooks.model.csv.impl.CsvReaderImpl;
import org.jboss.tools.smooks.model.csv12.impl.BindingImpl;
import org.jboss.tools.smooks.model.csv12.impl.MapBindingImpl;
import org.jboss.tools.smooks.model.datasource.impl.DataSourceJndiImpl;
import org.jboss.tools.smooks.model.datasource.impl.DirectImpl;
import org.jboss.tools.smooks.model.dbrouting.impl.ExecutorImpl;
import org.jboss.tools.smooks.model.dbrouting.impl.ResultSetImpl;
import org.jboss.tools.smooks.model.dbrouting.impl.ResultSetRowSelectorImpl;
import org.jboss.tools.smooks.model.edi.impl.EDIReaderImpl;
import org.jboss.tools.smooks.model.esbrouting.impl.RouteBeanImpl;
import org.jboss.tools.smooks.model.fileRouting.impl.HighWaterMarkImpl;
import org.jboss.tools.smooks.model.fileRouting.impl.OutputStreamImpl;
import org.jboss.tools.smooks.model.freemarker.impl.FreemarkerImpl;
import org.jboss.tools.smooks.model.freemarker.impl.InlineImpl;
import org.jboss.tools.smooks.model.freemarker.impl.UseImpl;
import org.jboss.tools.smooks.model.groovy.impl.GroovyImpl;
import org.jboss.tools.smooks.model.groovy.impl.ScriptTypeImpl;
import org.jboss.tools.smooks.model.iorouting.impl.IORouterImpl;
import org.jboss.tools.smooks.model.javabean.impl.BindingsTypeImpl;
import org.jboss.tools.smooks.model.javabean.impl.ExpressionTypeImpl;
import org.jboss.tools.smooks.model.javabean.impl.ValueTypeImpl;
import org.jboss.tools.smooks.model.javabean.impl.WiringTypeImpl;
import org.jboss.tools.smooks.model.javabean12.impl.BeanTypeImpl;
import org.jboss.tools.smooks.model.jmsrouting.impl.ConnectionImpl;
import org.jboss.tools.smooks.model.jmsrouting.impl.JmsRouterImpl;
import org.jboss.tools.smooks.model.jmsrouting.impl.JndiImpl;
import org.jboss.tools.smooks.model.jmsrouting.impl.MessageImpl;
import org.jboss.tools.smooks.model.jmsrouting.impl.SessionImpl;
import org.jboss.tools.smooks.model.jmsrouting12.impl.JMS12RouterImpl;
import org.jboss.tools.smooks.model.json.impl.JsonReaderImpl;
import org.jboss.tools.smooks.model.json.impl.KeyImpl;
import org.jboss.tools.smooks.model.json.impl.KeyMapImpl;
import org.jboss.tools.smooks.model.medi.impl.ComponentImpl;
import org.jboss.tools.smooks.model.medi.impl.DelimitersImpl;
import org.jboss.tools.smooks.model.medi.impl.DescriptionImpl;
import org.jboss.tools.smooks.model.medi.impl.EdiMapImpl;
import org.jboss.tools.smooks.model.medi.impl.FieldImpl;
import org.jboss.tools.smooks.model.medi.impl.MappingNodeImpl;
import org.jboss.tools.smooks.model.medi.impl.SegmentImpl;
import org.jboss.tools.smooks.model.medi.impl.SegmentsImpl;
import org.jboss.tools.smooks.model.medi.impl.SubComponentImpl;
import org.jboss.tools.smooks.model.persistence12.impl.DeleterImpl;
import org.jboss.tools.smooks.model.persistence12.impl.ExpressionParameterImpl;
import org.jboss.tools.smooks.model.persistence12.impl.FlusherImpl;
import org.jboss.tools.smooks.model.persistence12.impl.InserterImpl;
import org.jboss.tools.smooks.model.persistence12.impl.LocatorImpl;
import org.jboss.tools.smooks.model.persistence12.impl.UpdaterImpl;
import org.jboss.tools.smooks.model.persistence12.impl.ValueParameterImpl;
import org.jboss.tools.smooks.model.persistence12.impl.WiringParameterImpl;
import org.jboss.tools.smooks.model.rules10.impl.RuleBaseImpl;
import org.jboss.tools.smooks.model.smooks.impl.ConditionTypeImpl;
import org.jboss.tools.smooks.model.smooks.impl.ConditionsTypeImpl;
import org.jboss.tools.smooks.model.smooks.impl.FeaturesTypeImpl;
import org.jboss.tools.smooks.model.smooks.impl.HandlerTypeImpl;
import org.jboss.tools.smooks.model.smooks.impl.HandlersTypeImpl;
import org.jboss.tools.smooks.model.smooks.impl.ImportTypeImpl;
import org.jboss.tools.smooks.model.smooks.impl.ParamTypeImpl;
import org.jboss.tools.smooks.model.smooks.impl.ParamsTypeImpl;
import org.jboss.tools.smooks.model.smooks.impl.ProfileTypeImpl;
import org.jboss.tools.smooks.model.smooks.impl.ProfilesTypeImpl;
import org.jboss.tools.smooks.model.smooks.impl.ReaderTypeImpl;
import org.jboss.tools.smooks.model.smooks.impl.ResourceConfigTypeImpl;
import org.jboss.tools.smooks.model.smooks.impl.ResourceTypeImpl;
import org.jboss.tools.smooks.model.smooks.impl.SetOffTypeImpl;
import org.jboss.tools.smooks.model.smooks.impl.SetOnTypeImpl;
import org.jboss.tools.smooks.model.smooks.impl.SmooksResourceListTypeImpl;
import org.jboss.tools.smooks.model.validation10.impl.RuleTypeImpl;
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
		map.put(ExpressionTypeImpl.class, new JavabeanExpressionUICreator());

		// for smooks models
		map.put(ConditionTypeImpl.class, new ConditionTypeUICreator());
		map.put(ImportTypeImpl.class, new ImportTypeUICreator());
		map.put(ParamTypeImpl.class, new ParamTypeUICreator());
		map.put(ResourceConfigTypeImpl.class, new ResourceConfigTypeUICreator());
		map.put(ResourceTypeImpl.class, new ResourceTypeUICreator());
		map.put(ConditionsTypeImpl.class, new ConditionsTypeUICreator());
		map.put(FeaturesTypeImpl.class, new FeaturesTypeUICreator());
		map.put(HandlersTypeImpl.class, new HandlersTypeUICreator());
		map.put(HandlerTypeImpl.class, new HandlerTypeUICreator());
		map.put(ParamsTypeImpl.class, new ParamsTypeUICreator());
		map.put(ProfilesTypeImpl.class, new ProfilesTypeUICreator());
		map.put(ProfileTypeImpl.class, new ProfileTypeUICreator());
		map.put(ReaderTypeImpl.class, new ReaderTypeUICreator());
		map.put(SetOffTypeImpl.class, new SetOffTypeUICreator());
		map.put(SetOnTypeImpl.class, new SetOnTypeUICreator());
		map.put(SmooksResourceListTypeImpl.class, new SmooksResourceListTypeUICreator());

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

		// for medi
		map.put(ComponentImpl.class, new ComponentUICreator());
		map.put(DelimitersImpl.class, new DelimitersUICreator());
		map.put(DescriptionImpl.class, new DescriptionUICreator());
		map.put(EdiMapImpl.class, new EdiMapUICreator());
		map.put(FieldImpl.class, new FieldUICreator());
		map.put(MappingNodeImpl.class, new MappingNodeUICreator());
		map.put(SegmentImpl.class, new SegmentUICreator());
		map.put(SegmentsImpl.class, new SegmentsUICreator());
		map.put(SubComponentImpl.class, new SubComponentUICreator());

		// for edi
		map.put(EDIReaderImpl.class, new EDIReaderUICreator());

		// for Json
		map.put(KeyImpl.class, new KeyUICreator());
		map.put(KeyMapImpl.class, new KeyMapUICreator());
		map.put(JsonReaderImpl.class, new JsonReaderUICreator());

		// for CSV
		map.put(CsvReaderImpl.class, new CsvReaderUICreator());

		// for IO Routing
		map.put(IORouterImpl.class, new IORouterUICreator());

		// for JMS
		map.put(ConnectionImpl.class, new ConnectionUICreator());
		map.put(HighWaterMarkImpl.class, new HighWaterMarkUICreator());
		map.put(JndiImpl.class, new JndiUICreator());
		map.put(MessageImpl.class, new MessageUICreator());
		map.put(JmsRouterImpl.class, new JmsRouterUICreator());
		map.put(SessionImpl.class, new SessionUICreator());

		// for DB routing
		map.put(ExecutorImpl.class, new ExecutorUICreator());
		map.put(ResultSetImpl.class, new ResultSetUICreator());
		map.put(ResultSetRowSelectorImpl.class, new ResultSetRowSelectorUICreator());

		// for calc
		map.put(CounterImpl.class, new CounterUICreator());

		// for datasource
		map.put(DirectImpl.class, new DirectUICreator());
		map.put(DataSourceJndiImpl.class, new DataSourceJndiUICreator());

		// for ESB router
		map.put(RouteBeanImpl.class, new RouteBeanPropertyUICreator());
		/*
		 * up is for smooks1.1
		 */

		/*
		 * bottom is for smooks 1.2
		 */
		
		// for JavaBean v1.2
		map.put(BeanTypeImpl.class, new JavaBean12PropertyUICreator());
		map
				.put(org.jboss.tools.smooks.model.javabean12.impl.ValueTypeImpl.class,
						new JavabeanValueBinding12UICreator());
		map.put(org.jboss.tools.smooks.model.javabean12.impl.ExpressionTypeImpl.class,
				new Javabean12ExpressionUICreator());
		map.put(org.jboss.tools.smooks.model.javabean12.impl.WiringTypeImpl.class,
				new JavabeanWiringBiding12UICreator());
		
		// for JMS Router v1.2
		map.put(JMS12RouterImpl.class, new JMSRouter12UICreator());
		
		// for Persistence v1.2
		map.put(DeleterImpl.class, new DeleterUICreator());
		map.put(UpdaterImpl.class, new UpdaterUICreator());
		map.put(InserterImpl.class, new InserterUICreator());
		map.put(LocatorImpl.class, new LocatorUICreator());
		map.put(FlusherImpl.class, new FlusherUICreator());
		
		map.put(ValueParameterImpl.class, new LocatorValueParamUICreator());
		map.put(WiringParameterImpl.class, new LocatorWiringParamUICreator());
		map.put(ExpressionParameterImpl.class, new LocatorExpressionParamUICreator());
		
		// for Validation v1.0
		map.put(RuleTypeImpl.class, new RuleUICreator());
		
		// for CSV Reader v1.2
		map.put(BindingImpl.class, new Csv12ReaderBindingPropertyUICreator());
		map.put(MapBindingImpl.class, new Csv12ReaderMapBindingPropertyUICreator());
		
		// for Rules v1.0
		map.put(RuleBaseImpl.class, new Rules10RulebaseUICreator());
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
