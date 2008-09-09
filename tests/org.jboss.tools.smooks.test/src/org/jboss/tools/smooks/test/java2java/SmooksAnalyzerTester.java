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
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.graphical.GraphicalPackage;
import org.jboss.tools.smooks.javabean.analyzer.JavaBeanAnalyzer;
import org.milyn.xsd.smooks.DocumentRoot;
import org.milyn.xsd.smooks.SmooksPackage;
import org.milyn.xsd.smooks.SmooksResourceListType;
import org.milyn.xsd.smooks.util.SmooksResourceFactoryImpl;

/**
 * @author Dart Peng Date : 2008-9-1
 */
public class SmooksAnalyzerTester extends TestCase {
	public void testParse() {
		Registry.INSTANCE.put(GraphicalPackage.eNS_URI,
				GraphicalPackage.eINSTANCE);
		Registry.INSTANCE.put(SmooksPackage.eNS_URI, SmooksPackage.eINSTANCE);
		try {
			ClassLoader classLoader = SmooksAnalyzerTester.class
					.getClassLoader();
			Resource resource = new SmooksResourceFactoryImpl()
					.createResource(null);
			Resource gr = new XMLResourceFactoryImpl().createResource(null);
			InputStream stream1 = classLoader
					.getResourceAsStream("org/jboss/tools/smooks/test/java2java/Test.xml");
			InputStream stream2 = classLoader
					.getResourceAsStream("org/jboss/tools/smooks/test/java2java/Test.smooks.graph");
			gr.load(stream2, Collections.EMPTY_MAP);
			GraphInformations graph = (GraphInformations) gr.getContents().get(
					0);

			resource.load(stream1, Collections.EMPTY_MAP);
			JavaBeanAnalyzer sourceModelAnalyzer = new JavaBeanAnalyzer();
			JavaBeanAnalyzer targetModelAnalyzer = new JavaBeanAnalyzer();
			JavaBeanAnalyzer connectionsAnalyzer = new JavaBeanAnalyzer();
			SmooksResourceListType listType = ((DocumentRoot) resource
					.getContents().get(0)).getSmooksResourceList();
			Object source = sourceModelAnalyzer.buildSourceInputObjects(graph,
					listType, null, classLoader);
			Object target = targetModelAnalyzer.buildTargetInputObjects(graph,
					listType, null, classLoader);
			List connections = connectionsAnalyzer.analyzeMappingSmooksModel(
					listType, source, target);
			System.out.println(connections);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
