/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and others contributors as indicated
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
 * (C) 2005-2006, JBoss Inc.
 */
package org.jboss.template.csv;

import java.io.IOException;

import junit.framework.TestCase;

import org.jboss.template.exception.TemplateBuilderException;
import org.jboss.template.exception.UnmappedCollectionNodeException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class CSVToFreemarkerTemplateBuilderTest extends TestCase {

	public static void main(String[] args) throws TemplateBuilderException {
		// new CSVToFreemarkerTemplateBuilderTest().test_all_fields_mapped();
	}

	public void test_all_fields_mapped_01() throws TemplateBuilderException, IOException {
		CSVModelBuilder modelBuilder = new CSVModelBuilder("firstname", "lastname", "country");
		Document model = modelBuilder.buildModel();
		CSVFreeMarkerTemplateBuilder builder1 = new CSVFreeMarkerTemplateBuilder(model, ',', '\"');

		builder1.addCollectionMapping("people", getRecordElement(model), "person");
		builder1.addValueMapping("person.fname", getFieldElement(model, "firstname"));
		builder1.addValueMapping("person.lname", getFieldElement(model, "lastname"));
		builder1.addValueMapping("person.address.country", getFieldElement(model, "country"));

		String template = builder1.buildTemplate();
		// System.out.println(template);
		assertEquals("<#list people as person>\n"
				+ "\"${person.fname}\",\"${person.lname}\",\"${person.address.country}\"\n" + "</#list>", template);

		CSVFreeMarkerTemplateBuilder builder2 = new CSVFreeMarkerTemplateBuilder(model, ',', '\"', template);
		template = builder2.buildTemplate();
		// System.out.println(template);
		assertEquals("<#list people as person>\n"
				+ "\"${person.fname}\",\"${person.lname}\",\"${person.address.country}\"\n" + "</#list>", template);
	}

	/**
	 * Same as test above accept it uses different delimiters.
	 */
	public void test_all_fields_mapped_02() throws TemplateBuilderException, IOException {
		CSVModelBuilder modelBuilder = new CSVModelBuilder("firstname", "lastname", "country");
		Document model = modelBuilder.buildModel();
		CSVFreeMarkerTemplateBuilder builder1 = new CSVFreeMarkerTemplateBuilder(model, '|', '\'');

		builder1.addCollectionMapping("people", getRecordElement(model), "person");
		builder1.addValueMapping("person.fname", getFieldElement(model, "firstname"));
		builder1.addValueMapping("person.lname", getFieldElement(model, "lastname"));
		builder1.addValueMapping("person.address.country", getFieldElement(model, "country"));

		String template = builder1.buildTemplate();
		// System.out.println(template);
		assertEquals("<#list people as person>\n" + "'${person.fname}'|'${person.lname}'|'${person.address.country}'\n"
				+ "</#list>", template);

		CSVFreeMarkerTemplateBuilder builder2 = new CSVFreeMarkerTemplateBuilder(model, '|', '\'', template);
		template = builder2.buildTemplate();
		// System.out.println(template);
		assertEquals("<#list people as person>\n" + "'${person.fname}'|'${person.lname}'|'${person.address.country}'\n"
				+ "</#list>", template);
	}

	public void test_all_fields_not_mapped() throws TemplateBuilderException {
		CSVModelBuilder modelBuilder = new CSVModelBuilder("firstname", "lastname", "country");
		Document model = modelBuilder.buildModel();
		CSVFreeMarkerTemplateBuilder builder;

		builder = new CSVFreeMarkerTemplateBuilder(model, ',', '\"');

		builder.addCollectionMapping("people", getRecordElement(model), "person");
		builder.addValueMapping("person.fname", getFieldElement(model, "firstname"));
		builder.addValueMapping("person.address.country", getFieldElement(model, "country"));

		String template = builder.buildTemplate();
		// System.out.println(template);
		assertEquals(
				"<#list people as person>\n" + "\"${person.fname}\",,\"${person.address.country}\"\n" + "</#list>",
				template);

		CSVFreeMarkerTemplateBuilder builder2 = new CSVFreeMarkerTemplateBuilder(model, ',', '\"', template);
		template = builder2.buildTemplate();
		// System.out.println(template);
		assertEquals(
				"<#list people as person>\n" + "\"${person.fname}\",,\"${person.address.country}\"\n" + "</#list>",
				template);

		try {
			new CSVFreeMarkerTemplateBuilder(model, ';', '\"', template);
			fail("Expected TemplateBuilderException");
		} catch (TemplateBuilderException e) {
			assertEquals(
					"CSV Template fieldset size does not match that of the specified message model.  Check the supplied fieldset.  Check the specified 'separator' and 'quote' characters match those used in the template.",
					e.getMessage());
		}
	}

	public void test_collection_not_mapped_01() throws TemplateBuilderException {
		CSVModelBuilder modelBuilder = new CSVModelBuilder("firstname", "lastname", "country");
		Document model = modelBuilder.buildModel();
		CSVFreeMarkerTemplateBuilder builder;

		builder = new CSVFreeMarkerTemplateBuilder(model, ',', '\"');

		try {
			// Shouldn't be able to add a value binding where the model target
			// is inside
			// an unmapped collection...
			builder.addValueMapping("person.fname", getFieldElement(model, "firstname"));
			fail("Expected UnmappedCollectionNodeException");
		} catch (UnmappedCollectionNodeException e) {
			assertEquals("Unmapped collection node 'csv-record'.", e.getMessage());
		}
	}

	public void test_collection_not_mapped_02() throws TemplateBuilderException {
		CSVModelBuilder modelBuilder = new CSVModelBuilder("firstname", "lastname", "country");
		Document model = modelBuilder.buildModel();
		CSVFreeMarkerTemplateBuilder builder;

		builder = new CSVFreeMarkerTemplateBuilder(model, ',', '\"');

		try {
			// For CSV, you need to have at least mapped the collection...
			builder.buildTemplate();
			fail("Expected UnmappedCollectionNodeException");
		} catch (UnmappedCollectionNodeException e) {
			assertEquals("Unmapped collection node 'csv-record'.", e.getMessage());
		}
	}

	private Element getRecordElement(Document model) {
		return model.getDocumentElement();
	}

	private Element getFieldElement(Document model, String fieldName) {
		return (Element) model.getElementsByTagName(fieldName).item(0);
	}
}