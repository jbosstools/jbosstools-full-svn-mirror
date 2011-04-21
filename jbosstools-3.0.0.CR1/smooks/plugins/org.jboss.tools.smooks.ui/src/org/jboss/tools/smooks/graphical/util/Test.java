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
package org.jboss.tools.smooks.graphical.util;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.graphical.GraphicalFactory;
import org.jboss.tools.smooks.graphical.GraphicalPackage;
import org.jboss.tools.smooks.graphical.MappingDataType;
import org.jboss.tools.smooks.graphical.Param;
import org.jboss.tools.smooks.graphical.Params;

/**
 * @author Dart Peng
 * @Date Aug 25, 2008
 */
public class Test {
	public static void main(String[] args) {

		Registry.INSTANCE.put(GraphicalPackage.eNS_URI,
				GraphicalPackage.eINSTANCE);

		GraphInformations graph = GraphicalFactory.eINSTANCE
				.createGraphInformations();
		MappingDataType t = GraphicalFactory.eINSTANCE.createMappingDataType();

		t.setSourceTypeID("adfada");

		graph.setMappingType(t);

		Params params = GraphicalFactory.eINSTANCE.createParams();
		Param p = GraphicalFactory.eINSTANCE.createParam();
		p.setName("classname");
		p.setValue("dfafdafa");
		
		params.getParam().add(p);
		
		graph.setParams(params);

		Resource resource = new XMLResourceFactoryImpl().createResource(null);
		resource.getContents().add(graph);

		try {
			resource.save(System.out, Collections.EMPTY_MAP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
