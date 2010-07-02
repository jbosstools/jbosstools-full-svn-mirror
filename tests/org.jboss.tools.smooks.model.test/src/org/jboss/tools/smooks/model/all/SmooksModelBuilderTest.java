/**
 * JBoss, Home of Professional Open Source
 * Copyright 2009, JBoss Inc., and others contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2009, JBoss Inc.
 */
package org.jboss.tools.smooks.model.all;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.jboss.tools.smooks.model.SmooksEditorModelBuilder;
import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.core.GlobalParams;
import org.jboss.tools.smooks.model.csv.CSVReader;
import org.jboss.tools.smooks.model.freemarker.FreeMarkerTemplate;
import org.jboss.tools.smooks.model.javabean.Bean;
import org.jboss.tools.smooks.model.javabean.Value;
import org.jboss.tools.smooks.model.javabean.Wiring;
import org.milyn.StreamFilterType;
import org.milyn.javabean.dynamic.BeanRegistrationException;
import org.milyn.javabean.dynamic.Model;
import org.xml.sax.SAXException;

import junit.framework.TestCase;

/**
 *
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class SmooksModelBuilderTest extends TestCase {

	public void test() throws BeanRegistrationException, IOException, SAXException {
		SmooksEditorModelBuilder modelBuilder = new SmooksEditorModelBuilder();
		Model<SmooksModel> model = modelBuilder.newModel();
		SmooksModel smooksModel = model.getModelRoot();

		// Create the global config and add it to the model...
		smooksModel.setParams(new GlobalParams().setFilterType(StreamFilterType.SAX));

		// Create a CSV reader and add it as a Reader to the model...
		CSVReader reader = CSVReader.newInstance(model);
		reader.setFields("name,address,age");
		reader.setRootElementName("people");
		reader.setRecordElementName("person");
		reader.setIndent(true);
		smooksModel.getReaders().add(reader);
		
		// Create some beans and add them as components to the model....
		Bean people = Bean.newInstance(model);
		people.setBeanId("people").setBeanClass("java.util.ArrayList").setCreateOnElement("#document");
		people.getWireBindings().add(new Wiring().setBeanIdRef("person"));
		smooksModel.getComponents().add(people);

		Bean person = Bean.newInstance(model);
		person.setBeanId("person").setBeanClass("com.acme.Person").setCreateOnElement("person");
		person.getValueBindings().add(new Value().setProperty("name").setData("person/name"));
		person.getValueBindings().add(new Value().setProperty("address").setData("person/address"));
		person.getValueBindings().add(new Value().setProperty("age").setData("person/age"));
		smooksModel.getComponents().add(person);
		
		// Create a FreeMarker template and add it as a component to the model...
		FreeMarkerTemplate ftl = FreeMarkerTemplate.newInstance(model);
		ftl.setTemplate("people[] : etc etc etc").setApplyOnElement("#document");
		smooksModel.getComponents().add(ftl);
		
		// Now serialize it....
		StringWriter writer = new StringWriter();
		model.writeModel(writer);
		
	    XMLUnit.setIgnoreWhitespace( true );
	    XMLAssert.assertXMLEqual(new InputStreamReader(getClass().getResourceAsStream("expected_01.xml")), new StringReader(writer.toString()));
	    
	    // Recreate the model from the serialized form...
	    model = modelBuilder.readModel(new StringReader(writer.toString()));
	    
	    // Serialize it again and check it again...
	    writer = new StringWriter();
		model.writeModel(writer);
		
	    XMLUnit.setIgnoreWhitespace( true );
	    XMLAssert.assertXMLEqual(new InputStreamReader(getClass().getResourceAsStream("expected_01.xml")), new StringReader(writer.toString()));	    
	}
}
