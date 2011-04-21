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
package org.jboss.tools.smooks.model.csv;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.SmooksModelTestCase;
import org.milyn.javabean.dynamic.Model;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class CSVReader_1_2_Test extends SmooksModelTestCase {

    public void test_01() throws IOException, SAXException {
        test("v1_2/csv-config-01.xml");
    }

    public void test_02() throws IOException, SAXException {
        test("v1_2/csv-config-02.xml");
    }

    public void test_03() throws IOException, SAXException {
        test("v1_2/csv-config-03.xml");
    }

    public void test_04() throws IOException, SAXException {
        test("v1_2/csv-config-04.xml");
    }

    public void test_programmatic_build() throws IOException, SAXException {
        Model<SmooksModel> model = getSmooksModelBuilder().newModel();
        CSVReader csvReader = new CSVReader();

        // Populate it...
        csvReader.setFields("name,address,age");
        csvReader.setRootElementName("people");
        csvReader.setRecordElementName("person");
        csvReader.setIndent(true);

        // Set strict on the model... should have no effect as it's not supported in v1.2...
        csvReader.setStrict(true);

        // Need to register all the "namespace root" bean instances...
        model.registerBean(csvReader).setNamespace("http://www.milyn.org/xsd/smooks/csv-1.2.xsd").setNamespacePrefix("csv12");

        // Add it in the appropriate place in the object graph....
        model.getModelRoot().getReaders().add(csvReader);

        ListBinding listBinding = new ListBinding();
        listBinding.setBeanId("beanX");
        listBinding.setBeanClass("com.acme.XClass");

        // Add the ListBinding to the CSVReader, but no need to add it to the model since it is
        // not a "namespace root" object...
        csvReader.setListBinding(listBinding);

        StringWriter modelWriter = new StringWriter();
        model.writeModel(modelWriter);
//        System.out.println(modelWriter);
        XMLUnit.setIgnoreWhitespace( true );
        XMLAssert.assertXMLEqual(new InputStreamReader(getClass().getResourceAsStream("v1_2/csv-config-03.xml")), new StringReader(modelWriter.toString()));
    }
}
