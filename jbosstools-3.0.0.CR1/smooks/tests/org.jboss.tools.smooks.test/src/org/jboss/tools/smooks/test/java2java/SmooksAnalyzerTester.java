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
package org.jboss.tools.smooks.test.java2java;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.jboss.tools.smooks.analyzer.MappingResourceConfigList;
import org.jboss.tools.smooks.analyzer.NormalSmooksModelBuilder;
import org.jboss.tools.smooks.analyzer.NormalSmooksModelPackage;
import org.jboss.tools.smooks.analyzer.ResourceConfigEraser;
import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.graphical.GraphicalPackage;
import org.jboss.tools.smooks.javabean.analyzer.JavaBeanAnalyzer;
import org.jboss.tools.smooks.model.DocumentRoot;
import org.jboss.tools.smooks.model.SmooksPackage;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.provider.SmooksItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.util.SmooksResourceFactoryImpl;

/**
 * @author Dart Peng Date : 2008-9-1
 */
public class SmooksAnalyzerTester extends TestCase {

	private GraphInformations graph;
	private Resource resource;
	private ComposedAdapterFactory adapterFactory;
	private AdapterFactoryEditingDomain editingDomain;
	private Resource smooksResource;
	protected CommandStack createCommandStack() {
		return new BasicCommandStack();
	}
	public SmooksAnalyzerTester() throws IOException {
//		super();
		adapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory
				.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory
				.addAdapterFactory(new SmooksItemProviderAdapterFactory());
		editingDomain = new AdapterFactoryEditingDomain(adapterFactory,
				createCommandStack(), new HashMap<Resource, Boolean>());
		
		
		ClassLoader classLoader = SmooksAnalyzerTester.class.getClassLoader();
		Registry.INSTANCE.put(GraphicalPackage.eNS_URI,
				GraphicalPackage.eINSTANCE);
		Registry.INSTANCE.put(SmooksPackage.eNS_URI, SmooksPackage.eINSTANCE);

		resource = new SmooksResourceFactoryImpl().createResource(null);
		Resource gr = new XMLResourceFactoryImpl().createResource(null);
		InputStream stream1 = classLoader
				.getResourceAsStream("org/jboss/tools/smooks/test/java2java/Test.xml");
		InputStream stream2 = classLoader
				.getResourceAsStream("org/jboss/tools/smooks/test/java2java/Test.smooks.graph");
		gr.load(stream2, Collections.EMPTY_MAP);
		graph = (GraphInformations) gr.getContents().get(0);

		resource.load(stream1, Collections.EMPTY_MAP);
	}

	public MappingResourceConfigList analyzeGraphical() {
		try {
			ClassLoader classLoader = SmooksAnalyzerTester.class
					.getClassLoader();
			JavaBeanAnalyzer sourceModelAnalyzer = new JavaBeanAnalyzer();
			JavaBeanAnalyzer targetModelAnalyzer = new JavaBeanAnalyzer();
			JavaBeanAnalyzer connectionsAnalyzer = new JavaBeanAnalyzer();
			SmooksResourceListType listType = ((DocumentRoot) resource
					.getContents().get(0)).getSmooksResourceList();
			Object source = sourceModelAnalyzer.buildSourceInputObjects(graph,
					listType, null, classLoader);
			Object target = targetModelAnalyzer.buildTargetInputObjects(graph,
					listType, null, classLoader);
			MappingResourceConfigList configList = connectionsAnalyzer
					.analyzeMappingSmooksModel(listType, source, target);
			List connections = configList.getMappingModelList();
			List relationgConnection = configList
					.getRelationgResourceConfigList();
			Assert.assertTrue(!connections.isEmpty());
			Assert.assertTrue(!relationgConnection.isEmpty());
			System.out.println(connections);
			return configList;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void eraserMappingResourceConfig() {
		ResourceConfigEraser eraser = new ResourceConfigEraser();
		SmooksResourceListType listType = ((DocumentRoot) resource
				.getContents().get(0)).getSmooksResourceList();
		int oldCount = listType.getAbstractResourceConfig().size();
		MappingResourceConfigList configList = analyzeGraphical();
		eraser.cleanMappingResourceConfig(listType, configList, null);
		int newCount = listType.getAbstractResourceConfig().size();
		Assert.assertTrue(oldCount >= newCount);
	}

	public void testEraser() {
		eraserMappingResourceConfig();
	}

	public void testAnalyzer() {
		MappingResourceConfigList configList = analyzeGraphical();
	}

	public void generateNormalInforPackage() {
		SmooksResourceListType listType = ((DocumentRoot) resource
				.getContents().get(0)).getSmooksResourceList();
		NormalSmooksModelPackage modePackage = NormalSmooksModelBuilder
				.getInstance().buildNormalSmooksModelPackage(listType);
		
	}
	
	public void testGenerateNormalInforPackage(){
		eraserMappingResourceConfig();
		generateNormalInforPackage();
	}
}
