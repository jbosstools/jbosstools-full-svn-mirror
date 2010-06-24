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

import org.jboss.tools.smooks.model.core.Reader;
import org.milyn.javabean.dynamic.serialize.DefaultNamespace;

/**
 * CSV Reader.
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

    public void setFields(String fields) {
        this.fields = fields;
    }

    public Character getSeparator() {
        return separator;
    }

    public void setSeparator(Character separator) {
        this.separator = separator;
    }

    public Character getQuote() {
        return quote;
    }

    public void setQuote(Character quote) {
        this.quote = quote;
    }

    public Integer getSkipLines() {
        return skipLines;
    }

    public void setSkipLines(Integer skipLines) {
        this.skipLines = skipLines;
    }

    public String getRootElementName() {
        return rootElementName;
    }

    public void setRootElementName(String rootElementName) {
        this.rootElementName = rootElementName;
    }

    public String getRecordElementName() {
        return recordElementName;
    }

    public void setRecordElementName(String recordElementName) {
        this.recordElementName = recordElementName;
    }

    public Boolean isIndent() {
        return indent;
    }

    public void setIndent(Boolean indent) {
        this.indent = indent;
    }

    public Boolean isStrict() {
        return strict;
    }

    public void setStrict(Boolean strict) {
        this.strict = strict;
    }

    public Boolean isValidateHeader() {
        return validateHeader;
    }

    public void setValidateHeader(Boolean validateHeader) {
        this.validateHeader = validateHeader;
    }

    public SingleBinding getSingleBinding() {
        return singleBinding;
    }

    public void setSingleBinding(SingleBinding singleBinding) {
        this.singleBinding = singleBinding;
    }

    public ListBinding getListBinding() {
        return listBinding;
    }

    public void setListBinding(ListBinding listBinding) {
        this.listBinding = listBinding;
    }

    public MapBinding getMapBinding() {
        return mapBinding;
    }

    public void setMapBinding(MapBinding mapBinding) {
        this.mapBinding = mapBinding;
    }
}
