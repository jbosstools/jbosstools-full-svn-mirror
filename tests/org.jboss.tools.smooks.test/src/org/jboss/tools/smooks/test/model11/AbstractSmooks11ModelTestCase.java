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
package org.jboss.tools.smooks.test.model11;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;

import junit.framework.TestCase;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.calc.CalcPackage;
import org.jboss.tools.smooks.model.calc.provider.CalcItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.common.CommonPackage;
import org.jboss.tools.smooks.model.common.provider.CommonItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.csv.CsvPackage;
import org.jboss.tools.smooks.model.csv.provider.CsvItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.datasource.DatasourcePackage;
import org.jboss.tools.smooks.model.datasource.provider.DatasourceItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.dbrouting.DbroutingPackage;
import org.jboss.tools.smooks.model.dbrouting.provider.DbroutingItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.edi.EdiPackage;
import org.jboss.tools.smooks.model.edi.provider.EdiItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.fileRouting.FileRoutingPackage;
import org.jboss.tools.smooks.model.fileRouting.provider.FileRoutingItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.freemarker.FreemarkerPackage;
import org.jboss.tools.smooks.model.freemarker.provider.FreemarkerItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.groovy.GroovyPackage;
import org.jboss.tools.smooks.model.groovy.provider.GroovyItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.iorouting.IoroutingPackage;
import org.jboss.tools.smooks.model.iorouting.provider.IoroutingItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.javabean.JavabeanPackage;
import org.jboss.tools.smooks.model.javabean.provider.JavabeanItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.jmsrouting.JmsroutingPackage;
import org.jboss.tools.smooks.model.jmsrouting.provider.JmsroutingItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.json.JsonPackage;
import org.jboss.tools.smooks.model.json.provider.JsonItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.medi.DocumentRoot;
import org.jboss.tools.smooks.model.medi.MEdiPackage;
import org.jboss.tools.smooks.model.medi.MappingNode;
import org.jboss.tools.smooks.model.medi.provider.MEdiItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
import org.jboss.tools.smooks.model.smooks.provider.SmooksItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.xsl.XslPackage;
import org.jboss.tools.smooks.model.xsl.provider.XslItemProviderAdapterFactory;
import org.jboss.tools.smooks10.model.smooks.util.SmooksResourceFactoryImpl;

/**
 * 
 * @author Dart (dpeng@redhat.com)
 * 
 */
public abstract class AbstractSmooks11ModelTestCase extends TestCase {

	protected ComposedAdapterFactory adapterFactory;
	protected AdapterFactoryEditingDomain editingDomain;
	protected EObject smooksModel;

	static {
		// regist emf model uri mapping
		Registry.INSTANCE.put(SmooksPackage.eNS_URI, SmooksPackage.eINSTANCE);
		Registry.INSTANCE.put(CalcPackage.eNS_URI, CalcPackage.eINSTANCE);
		Registry.INSTANCE.put(CommonPackage.eNS_URI, CommonPackage.eINSTANCE);
		Registry.INSTANCE.put(CsvPackage.eNS_URI, CsvPackage.eINSTANCE);
		Registry.INSTANCE.put(DatasourcePackage.eNS_URI, DatasourcePackage.eINSTANCE);
		Registry.INSTANCE.put(DbroutingPackage.eNS_URI, DbroutingPackage.eINSTANCE);
		Registry.INSTANCE.put(EdiPackage.eNS_URI, EdiPackage.eINSTANCE);
		Registry.INSTANCE.put(FileRoutingPackage.eNS_URI, FileRoutingPackage.eINSTANCE);

		Registry.INSTANCE.put(FreemarkerPackage.eNS_URI, FreemarkerPackage.eINSTANCE);
		Registry.INSTANCE.put(GroovyPackage.eNS_URI, GroovyPackage.eINSTANCE);
		Registry.INSTANCE.put(IoroutingPackage.eNS_URI, IoroutingPackage.eINSTANCE);
		Registry.INSTANCE.put(JavabeanPackage.eNS_URI, JavabeanPackage.eINSTANCE);
		Registry.INSTANCE.put(JmsroutingPackage.eNS_URI, JmsroutingPackage.eINSTANCE);
		Registry.INSTANCE.put(JsonPackage.eNS_URI, JsonPackage.eINSTANCE);
		Registry.INSTANCE.put(MEdiPackage.eNS_URI, MEdiPackage.eINSTANCE);
		Registry.INSTANCE.put(XslPackage.eNS_URI, XslPackage.eINSTANCE);

		Registry.INSTANCE.put(org.jboss.tools.smooks10.model.smooks.SmooksPackage.eNS_URI,
				org.jboss.tools.smooks10.model.smooks.SmooksPackage.eINSTANCE);
	}

	public void testModel() {
		// do nothing
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// init emf editingdomain
		adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new XslItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new FreemarkerItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new JavabeanItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new CommonItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new SmooksItemProviderAdapterFactory());

		adapterFactory
				.addAdapterFactory(new org.jboss.tools.smooks10.model.smooks.provider.SmooksItemProviderAdapterFactory());

		adapterFactory.addAdapterFactory(new MEdiItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new EdiItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new IoroutingItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new JsonItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new JmsroutingItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new DbroutingItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new CsvItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new DatasourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new CalcItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new GroovyItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new FileRoutingItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		BasicCommandStack commandStack = new BasicCommandStack();
		editingDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack, new HashMap<Resource, Boolean>());

		loadConfigFile();
	}

	protected void loadConfigFile() throws IOException {
		Resource smooksResource = new SmooksResourceFactoryImpl().createResource(null);
		smooksResource.load(getConfigFileContents(), Collections.emptyMap());
		smooksModel = smooksResource.getContents().get(0);
		editingDomain.getResourceSet().getResources().add(smooksResource);
	}

	protected InputStream getConfigFileContents() {
		return this.getClass().getClassLoader().getResourceAsStream(getFilePath());
	}

	abstract protected String getFilePath();

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public ComposedAdapterFactory getAdapterFactory() {
		return adapterFactory;
	}

	public void setAdapterFactory(ComposedAdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
	}

	public AdapterFactoryEditingDomain getEditingDomain() {
		return editingDomain;
	}

	public void setEditingDomain(AdapterFactoryEditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}

	public EObject getSmooksModel() {
		return smooksModel;
	}

	public void setSmooksModel(EObject smooksModel) {
		this.smooksModel = smooksModel;
	}
	
	public MappingNode getMappingNode10(){
		EObject root = this.getSmooksModel();
		if (root instanceof DocumentRoot) {
			DocumentRoot documentRoot = (DocumentRoot) root;
			MappingNode mapping = (MappingNode) documentRoot.eContents().get(0);
			return mapping;
		}
		return null;
	}

	public SmooksResourceListType getSmooksResourceList11() {
		EObject root = this.getSmooksModel();
		if (root instanceof org.jboss.tools.smooks.model.smooks.DocumentRoot) {
			org.jboss.tools.smooks.model.smooks.DocumentRoot documentRoot = (org.jboss.tools.smooks.model.smooks.DocumentRoot) root;
			SmooksResourceListType resourceConfig = (SmooksResourceListType) documentRoot.eContents().get(0);
			return resourceConfig;
		}
		return null;
	}
	
	public org.jboss.tools.smooks10.model.smooks.SmooksResourceListType getSmooksResourceList10() {
		EObject root = this.getSmooksModel();
		if (root instanceof org.jboss.tools.smooks10.model.smooks.DocumentRoot) {
			org.jboss.tools.smooks10.model.smooks.DocumentRoot documentRoot = (org.jboss.tools.smooks10.model.smooks.DocumentRoot) root;
			org.jboss.tools.smooks10.model.smooks.SmooksResourceListType resourceConfig = (org.jboss.tools.smooks10.model.smooks.SmooksResourceListType) documentRoot.eContents().get(0);
			return resourceConfig;
		}
		return null;
	}

}
