package org.jboss.tools.smooks.xml2java.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.dom4j.DocumentException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.modelparser.ParseEngine;
import org.jboss.tools.smooks.xml.model.XMLObjectAnalyzer;
import org.jboss.tools.smooks.xml.model.XMLStrucutredModelParser;
import org.jboss.tools.smooks.xsd.model.XSDStructuredModelContentProvider;

public class ModelTest extends TestCase {
	public void testModelTrasform() throws IOException{
		Resource resource = new XSDResourceFactoryImpl().createResource(URI
				.createFileURI("/root/Public/smooks_1_0.xsd"));
		resource.load(Collections.EMPTY_MAP);
		XSDSchema schema = (XSDSchema) resource.getContents().get(0);
		
		ParseEngine engine = new ParseEngine();
		AbstractStructuredDataModel model = engine.parseModel(schema, new XSDStructuredModelContentProvider(), new XMLStrucutredModelParser());
		printStructuredDataModel(model);
	}
	
	protected void printStructuredDataModel(AbstractStructuredDataModel model){
		System.out.println(model.getLabelName() + " - " + model.getTypeString());
		List children = model.getChildren();
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			AbstractStructuredDataModel child = (AbstractStructuredDataModel) iterator.next();
			printStructuredDataModel(child);
		}
	}
	
	public void testXMLfragment() throws FileNotFoundException, DocumentException{
		XMLObjectAnalyzer an = new XMLObjectAnalyzer();
		System.out.println(an.analyze("/root/Public/test.xml").toString());
	}
}
