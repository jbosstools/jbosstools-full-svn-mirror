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
package org.jboss.tools.smooks.model;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.SmooksEditorModelBuilder;
import org.milyn.javabean.dynamic.Model;
import org.xml.sax.SAXException;

import junit.framework.TestCase;

/**
 * Abstract SmooksModel test case.
 *
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public abstract class SmooksModelTestCase extends TestCase {

	protected static SmooksEditorModelBuilder smooksModelBuilder = new SmooksEditorModelBuilder();
    
	public SmooksEditorModelBuilder getSmooksModelBuilder() {
		return smooksModelBuilder;
	}
	
	public Model<SmooksModel> test(String messageFile) throws IOException, SAXException {
	    Model<SmooksModel> model = smooksModelBuilder.readModel(getClass().getResourceAsStream(messageFile));
	    assertModelEquals(model, messageFile);
	    return model;
	}

	public void assertModelEquals(Model<SmooksModel> model, String messageFile) throws IOException, SAXException {
		StringWriter modelWriter = new StringWriter();
	    model.writeModel(modelWriter);

//	    System.out.println(modelWriter);
//	    System.out.println(org.milyn.io.StreamUtils.readStreamAsString(getClass().getResourceAsStream(messageFile)));	    
	    
	    XMLUnit.setIgnoreWhitespace( true );
	    XMLAssert.assertXMLEqual(new InputStreamReader(getClass().getResourceAsStream(messageFile)), new StringReader(modelWriter.toString()));
	}

	protected void reportTo(String reportPath) {
		smooksModelBuilder.getModelBuilder().setReportPath(reportPath);	
	}	
}