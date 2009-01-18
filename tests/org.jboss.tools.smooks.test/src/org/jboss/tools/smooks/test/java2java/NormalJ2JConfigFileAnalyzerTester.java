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
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.smooks.analyzer.MappingModel;
import org.jboss.tools.smooks.analyzer.MappingResourceConfigList;
import org.jboss.tools.smooks.analyzer.NormalSmooksModelBuilder;
import org.jboss.tools.smooks.analyzer.NormalSmooksModelPackage;
import org.jboss.tools.smooks.analyzer.ResourceConfigEraser;
import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.javabean.analyzer.JavaBeanAnalyzer;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.javabean.ui.AbstractJavaBeanBuilder;
import org.jboss.tools.smooks.javabean.ui.BeanPopulatorMappingAnalyzer;
import org.jboss.tools.smooks.model.DocumentRoot;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.test.java.SelectorTester;

/**
 * @author Dart Peng Date : 2008-9-1
 */
public class NormalJ2JConfigFileAnalyzerTester extends
		AbstractJava2JavaModelTestCase {

	public MappingResourceConfigList analyzeGraphical() throws IOException,
			InvocationTargetException {
		ClassLoader classLoader = NormalJ2JConfigFileAnalyzerTester.class
				.getClassLoader();
		SmooksResourceListType listType = ((DocumentRoot) smooksResource
				.getContents().get(0)).getSmooksResourceList();
		((AbstractJavaBeanBuilder) sourceModelAnalyzer)
				.setClassLoader(classLoader);
		((AbstractJavaBeanBuilder) targetModelAnalyzer)
				.setClassLoader(classLoader);
		Object source = sourceModelAnalyzer.buildSourceInputObjects(graph,
				listType, null, null);
		Object target = targetModelAnalyzer.buildTargetInputObjects(graph,
				listType, null, null);

		SelectorTester tester = new SelectorTester();
//		tester.validSmooksConfigFile(listType, (JavaBeanModel) ((List) source)
//				.get(0), (JavaBeanModel) ((List) target).get(0));

		MappingResourceConfigList configList = connectionsAnalyzer
				.analyzeMappingSmooksModel(listType, source, target);
		List connections = configList.getMappingModelList();
		List relationgConnection = configList
				.getGraphRenderResourceConfigList();
		// there are 10 connection lines model
		Assert.assertTrue(connections.size() == 10);

		// those 8 connection lines were analyzed by 3 ResourceConfig element
		Assert.assertTrue(relationgConnection.size() == 3);

		// check the connection properties
		for (Iterator iterator = connections.iterator(); iterator.hasNext();) {
			MappingModel connectionModels = (MappingModel) iterator.next();
			Assert.assertNotNull(connectionModels.getSource());
			Assert.assertNotNull(connectionModels.getTarget());
			JavaBeanModel sourceModel = (JavaBeanModel) connectionModels
					.getSource();
			JavaBeanModel targetModel = (JavaBeanModel) connectionModels
					.getSource();
			if (targetModel.isPrimitive()) {
				// check the connection model properties
				if (targetModel.getName().equals("productCode")) {
					Assert
							.assertTrue(connectionModels.getProperties().size() > 0);
				}
				if (targetModel.getName().equals("unitPrice")) {
					Assert
							.assertTrue(connectionModels.getProperties().size() > 0);
				}
				if (targetModel.getName().equals("unitQuantity")) {
					Assert
							.assertTrue(connectionModels.getProperties().size() > 0);
				}
			}
		}

		return configList;
	}

	public void eraserMappingResourceConfig() throws IOException,
			InvocationTargetException {
		ResourceConfigEraser eraser = new ResourceConfigEraser();
		SmooksResourceListType listType = ((DocumentRoot) smooksResource
				.getContents().get(0)).getSmooksResourceList();
		int oldCount = listType.getAbstractResourceConfig().size();
		MappingResourceConfigList configList = analyzeGraphical();
		eraser.cleanMappingResourceConfig(listType, configList, null);
		int newCount = listType.getAbstractResourceConfig().size();
		Assert.assertTrue(oldCount >= newCount);
	}

//	public void testEraser() throws IOException, InvocationTargetException {
//		eraserMappingResourceConfig();
//	}

	public void testAnalyzer() throws IOException, InvocationTargetException {
		MappingResourceConfigList configList = analyzeGraphical();
	}

	public void generateNormalInforPackage() {
		SmooksResourceListType listType = ((DocumentRoot) smooksResource
				.getContents().get(0)).getSmooksResourceList();
		NormalSmooksModelPackage modePackage = NormalSmooksModelBuilder
				.getInstance().buildNormalSmooksModelPackage(listType);

	}

	public void testGenerateNormalInforPackage() throws IOException,
			InvocationTargetException {
		eraserMappingResourceConfig();
		generateNormalInforPackage();
	}

	@Override
	public void loadResources() throws RuntimeException {
		ClassLoader classLoader = NormalJ2JConfigFileAnalyzerTester.class
				.getClassLoader();
		InputStream stream1 = classLoader
				.getResourceAsStream("org/jboss/tools/smooks/test/java2java/Test.xml");
		InputStream stream2 = classLoader
				.getResourceAsStream("org/jboss/tools/smooks/test/java2java/Test.smooks.graph");
		try {
			graphResource.load(stream2, Collections.EMPTY_MAP);

			graph = (GraphInformations) graphResource.getContents().get(0);

			smooksResource.load(stream1, Collections.EMPTY_MAP);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public IFile getSmooksConfigFile() {
		return null;
	}
}
