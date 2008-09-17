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
import java.util.List;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.ParamType;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.SmooksFactory;
import org.jboss.tools.smooks.model.SmooksPackage;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;
import org.milyn.xsd.smooks.provider.SmooksItemProviderAdapterFactory;

/**
 * @author Dart Peng
 * @Date Aug 20, 2008
 */
public abstract class AbstractAnalyzer implements IMappingAnalyzer {
	protected List usedConnectionList = new ArrayList();
	protected AdapterFactoryEditingDomain editingDomain;
	protected ComposedAdapterFactory adapterFactory;

	public AbstractAnalyzer() {
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

}
