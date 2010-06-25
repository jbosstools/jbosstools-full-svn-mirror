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

import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.core.Reader;
import org.milyn.javabean.dynamic.Model;
import org.milyn.javabean.dynamic.serialize.DefaultNamespace;

/**
 * CSV Reader mapping model.
 * 
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
@DefaultNamespace(uri = "http://www.milyn.org/xsd/smooks/csv-1.3.xsd", prefix = "csv13")
public class CSVReader implements Reader {

    private String fields;
    private Character separator;
    private Character quote;
    private Integer skipLines;
    private String rootElementName;
    private String recordElementName;
    private Boolean indent;
    private Boolean strict;
    private Boolean validateHeader;

    // Only one of the following binding configs will be wired into this bean...
    private SingleBinding singleBinding;
    private ListBinding listBinding;
    private MapBinding mapBinding;

	public String getFields() {
		return fields;
	}

	public CSVReader setFields(String fields) {
		this.fields = fields;
		return this;
	}

	public Character getSeparator() {
		return separator;
	}

	public CSVReader setSeparator(Character separator) {
		this.separator = separator;
		return this;
	}

	public Character getQuote() {
		return quote;
	}

	public CSVReader setQuote(Character quote) {
		this.quote = quote;
		return this;
	}

	public Integer getSkipLines() {
		return skipLines;
	}

	public CSVReader setSkipLines(Integer skipLines) {
		this.skipLines = skipLines;
		return this;
	}

	public String getRootElementName() {
		return rootElementName;
	}

	public CSVReader setRootElementName(String rootElementName) {
		this.rootElementName = rootElementName;
		return this;
	}

	public String getRecordElementName() {
		return recordElementName;
	}

	public CSVReader setRecordElementName(String recordElementName) {
		this.recordElementName = recordElementName;
		return this;
	}

	public Boolean getIndent() {
		return indent;
	}

	public CSVReader setIndent(Boolean indent) {
		this.indent = indent;
		return this;
	}

	public Boolean getStrict() {
		return strict;
	}

	public CSVReader setStrict(Boolean strict) {
		this.strict = strict;
		return this;
	}

	public Boolean getValidateHeader() {
		return validateHeader;
	}

	public CSVReader setValidateHeader(Boolean validateHeader) {
		this.validateHeader = validateHeader;
		return this;
	}

	public SingleBinding getSingleBinding() {
		return singleBinding;
	}

	public CSVReader setSingleBinding(SingleBinding singleBinding) {
		this.singleBinding = singleBinding;
		return this;
	}

	public ListBinding getListBinding() {
		return listBinding;
	}

	public CSVReader setListBinding(ListBinding listBinding) {
		this.listBinding = listBinding;
		return this;
	}

	public MapBinding getMapBinding() {
		return mapBinding;
	}

	public CSVReader setMapBinding(MapBinding mapBinding) {
		this.mapBinding = mapBinding;
		return this;
	}

	public static CSVReader newInstance(Model<SmooksModel> model) {
		CSVReader instance = new CSVReader();
		model.registerBean(instance);
		return instance;
	}
}
