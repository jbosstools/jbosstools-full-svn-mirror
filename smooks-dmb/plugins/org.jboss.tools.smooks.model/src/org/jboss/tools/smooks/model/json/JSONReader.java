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
package org.jboss.tools.smooks.model.json;

import java.util.Map;

import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.core.Reader;
import org.milyn.javabean.dynamic.Model;
import org.milyn.javabean.dynamic.serialize.DefaultNamespace;

/**
 * JSON Reader mapping model.
 * 
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
@DefaultNamespace(uri = "http://www.milyn.org/xsd/smooks/json-1.2.xsd", prefix = "json12")
public class JSONReader implements Reader {
	
	private Map<String, String> keyMap;
	private String rootName;
	private String arrayElementName;
	private String nullValueReplacement;
	private String keyWhitspaceReplacement;
	private String keyPrefixOnNumeric;
	private String illegalElementNameCharReplacement;
	private Boolean indent;
	
	public Map<String, String> getKeyMap() {
		return keyMap;
	}
	public JSONReader setKeyMap(Map<String, String> keyMap) {
		this.keyMap = keyMap;
		return this;
	}
	public String getRootName() {
		return rootName;
	}
	public JSONReader setRootName(String rootName) {
		this.rootName = rootName;
		return this;
	}
	public String getArrayElementName() {
		return arrayElementName;
	}
	public JSONReader setArrayElementName(String arrayElementName) {
		this.arrayElementName = arrayElementName;
		return this;
	}
	public String getNullValueReplacement() {
		return nullValueReplacement;
	}
	public JSONReader setNullValueReplacement(String nullValueReplacement) {
		this.nullValueReplacement = nullValueReplacement;
		return this;
	}
	public String getKeyWhitspaceReplacement() {
		return keyWhitspaceReplacement;
	}
	public JSONReader setKeyWhitspaceReplacement(String keyWhitspaceReplacement) {
		this.keyWhitspaceReplacement = keyWhitspaceReplacement;
		return this;
	}
	public String getKeyPrefixOnNumeric() {
		return keyPrefixOnNumeric;
	}
	public JSONReader setKeyPrefixOnNumeric(String keyPrefixOnNumeric) {
		this.keyPrefixOnNumeric = keyPrefixOnNumeric;
		return this;
	}
	public String getIllegalElementNameCharReplacement() {
		return illegalElementNameCharReplacement;
	}
	public JSONReader setIllegalElementNameCharReplacement(
			String illegalElementNameCharReplacement) {
		this.illegalElementNameCharReplacement = illegalElementNameCharReplacement;
		return this;
	}
	public Boolean getIndent() {
		return indent;
	}
	public JSONReader setIndent(Boolean indent) {
		this.indent = indent;
		return this;
	}
	
	public static JSONReader newInstance(Model<SmooksModel> model) {
		JSONReader instance = new JSONReader();
		model.registerBean(instance);
		return instance;
	}
}
