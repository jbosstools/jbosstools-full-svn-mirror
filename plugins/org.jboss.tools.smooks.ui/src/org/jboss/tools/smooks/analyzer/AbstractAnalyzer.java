/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.AbstractResourceConfig;
import org.jboss.tools.smooks.model.ParamType;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.SmooksFactory;
import org.jboss.tools.smooks.model.SmooksPackage;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.provider.SmooksItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;

/**
 * @author Dart Peng
 * @Date Aug 20, 2008
 */
public abstract class AbstractAnalyzer implements IMappingAnalyzer {

	protected List<AbstractResourceConfig> usedResourceConfigRecordList = new ArrayList<AbstractResourceConfig>();

	protected List usedConnectionList = new ArrayList();

	protected AdapterFactoryEditingDomain editingDomain;

	protected ComposedAdapterFactory adapterFactory;

	private HashMap<String, Object> userdResourceTypeMap;

	public AbstractAnalyzer() {
		userdResourceTypeMap = new HashMap<String, Object>();
		adapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		adapterFactory
				.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory
				.addAdapterFactory(new SmooksItemProviderAdapterFactory());

		editingDomain = new AdapterFactoryEditingDomain(adapterFactory,
				createCommandStack(), new HashMap<Resource, Boolean>());
	}

	protected CommandStack createCommandStack() {
		return new BasicCommandStack();
	}

	protected boolean connectionIsUsed(Object connection) {
		return (usedConnectionList.indexOf(connection) != -1);
	}

	protected void setConnectionUsed(Object connection) {
		usedConnectionList.add(connection);
	}

	protected void cleanUsedConnectionList() {
		usedConnectionList.clear();
	}

	protected void setSelectorIsUsed(String selector) {
		if (selector != null)
			selector = selector.trim();
		userdResourceTypeMap.put(selector, new Object());
	}

	protected boolean isSelectorIsUsed(String selector) {
		if (selector != null)
			selector = selector.trim();
		return (userdResourceTypeMap.get(selector) != null);
	}

	protected void addResourceConfigType(SmooksResourceListType resourceList,
			ResourceConfigType resourceConfig) {
		Command addResourceConfigCommand = AddCommand.create(
				getEditingDomain(), resourceList, SmooksPackage.eINSTANCE
						.getSmooksResourceListType_AbstractResourceConfig(),
				resourceConfig);
		addResourceConfigCommand.execute();
	}

	protected ParamType addParamTypeToResourceConfig(
			ResourceConfigType resourceConfigType, String name, String text) {
		ParamType paramType = SmooksFactory.eINSTANCE.createParamType();
		paramType.setName(name);
		if (text != null) {
			SmooksModelUtils.appendTextToSmooksType(paramType, text);
		}
		resourceConfigType.getParam().add(paramType);
		return paramType;
	}

	protected String getBeanIDFromParam(ResourceConfigType config) {
		List list = config.getParam();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			ParamType p = (ParamType) iterator.next();
			if ("beanId".equals(p.getName())) {
				return SmooksModelUtils.getAnyTypeText(p);
			}

		}
		return null;
	}

	protected String getBeanIdWithRawSelectorString(String selector) {
		selector = selector.substring(2, selector.length() - 1);
		return selector;
	}

	protected boolean isReferenceSelector(String selector) {
		return (selector.startsWith("${") && selector.endsWith("}"));
	}
	
	/**
	 * Find the ResourceConfig via its child param element which one's name property value is "beanId".
	 * If can't find that , return the ResourceConfig which one's "selector" property value same as the selector
	 * @param selector
	 * @param listType
	 * @return
	 */
	protected ResourceConfigType findResourceConfigTypeWithSelector(
			String selector, SmooksResourceListType listType) {
		if (selector != null)
			selector = selector.trim();
		if (isReferenceSelector(selector)) {
			selector = this.getBeanIdWithRawSelectorString(selector);
		}
		List<AbstractResourceConfig> rl = listType.getAbstractResourceConfig();
		ResourceConfigType resourceConfig = null;
		for (Iterator<AbstractResourceConfig> iterator = rl.iterator(); iterator
				.hasNext();) {
			AbstractResourceConfig abstractResourceConfig = iterator.next();
			if (abstractResourceConfig instanceof ResourceConfigType) {
				ResourceConfigType rct = (ResourceConfigType) abstractResourceConfig;
//				if (isResourceConfigUsed(rct))
//					continue;
				String beanId = getBeanIDFromParam(rct);
				if (selector.equals(beanId)) {
					resourceConfig = rct;
					break;
				}
				String selector1 = rct.getSelector();
				if (selector1 != null)
					selector1 = selector1.trim();
				if (selector.equals(selector1)) {
					resourceConfig = rct;
					break;
				}
			}
		}
		return resourceConfig;
	}

	/**
	 * @return the editingDomain
	 */
	public AdapterFactoryEditingDomain getEditingDomain() {
		return editingDomain;
	}

	/**
	 * @param editingDomain
	 *            the editingDomain to set
	 */
	public void setEditingDomain(AdapterFactoryEditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}

	public void setResourceConfigUsed(ResourceConfigType resourceConfig) {
		usedResourceConfigRecordList.add(resourceConfig);
	}

	public boolean isResourceConfigUsed(ResourceConfigType resourceConfig) {
		return usedResourceConfigRecordList.indexOf(resourceConfig) != -1;
	}

}
