package org.jboss.tools.smooks.test.xml;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
	public void testModelTrasform() throws IOException {
		Resource resource = new XSDResourceFactoryImpl().createResource(URI.createPlatformResourceURI(
				"org/jboss/tools/smooks/test/xml/smooks-1.1.xsd", false));
		InputStream aa = ModelTest.class.getClassLoader().getResourceAsStream(
				"org/jboss/tools/smooks/test/xml/smooks-1.1.xsd");
		resource.load(aa, Collections.EMPTY_MAP);
		XSDSchema schema = (XSDSchema) resource.getContents().get(0);
		ParseEngine engine = new ParseEngine();
		AbstractStructuredDataModel model = engine.parseModel(schema,
				new XSDStructuredModelContentProvider(),
				new XMLStrucutredModelParser());
		printStructuredDataModel(model);
	}

	protected void printStructuredDataModel(AbstractStructuredDataModel model) {
		System.out
				.println(model.getLabelName() + " - " + model.getTypeString());
		List children = model.getChildren();
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			AbstractStructuredDataModel child = (AbstractStructuredDataModel) iterator
					.next();
			printStructuredDataModel(child);
		}
	}

	public void testXMLfragment() throws FileNotFoundException,
			DocumentException {
		XMLObjectAnalyzer an = new XMLObjectAnalyzer();
		InputStream aa = ModelTest.class.getClassLoader().getResourceAsStream(
				"org/jboss/tools/smooks/test/xml/pom.xml");
		System.out.println(an.analyze(aa , null).toString());
	}
}
