/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.seam.xml.components.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jboss.tools.common.meta.XMapping;
import org.jboss.tools.common.meta.XModelMetaData;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.NamespaceMapping;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class SeamNamespaces {
	private static Map<String,SeamNamespaces> map = new HashMap<String, SeamNamespaces>();
	
	public static SeamNamespaces getInstance(XModelMetaData meta, String version) {
		SeamNamespaces instance = map.get(version);
		if(instance == null) {
			instance = new SeamNamespaces(meta, version);
		}
		return instance;
	}
	
	String versionSuffix = null;
	
	private Map<String, String> namespaceToURI = new HashMap<String, String>();
	private Map<String, String> uriToNamespace = new HashMap<String, String>();
	private Map<String, String> namespaceToSchema = new HashMap<String, String>();
	
	private SeamNamespaces(XModelMetaData meta, String versionSuffix) {
		XMapping m = meta.getMapping("SeamNamespaces");
		if(m == null) return;
		this.versionSuffix = versionSuffix;
		String[] keys = m.getKeys();
		for (int i = 0; i < keys.length; i++) {
			String v = m.getValue(keys[i]);
			namespaceToURI.put(keys[i], v);
			uriToNamespace.put(v, keys[i]);
		}
		m = meta.getMapping("SeamSchemas");
		if(m == null) return;
		keys = m.getKeys();
		for (int i = 0; i < keys.length; i++) {
			if(!keys[i].endsWith(versionSuffix)) continue;
			String v = m.getValue(keys[i]);
			String key = keys[i].substring(0, keys[i].length() - versionSuffix.length());
			namespaceToSchema.put(key, v);
		}
	}
	
	static String XMLNS_PREFIX = "xmlns:";	

	public NamespaceMapping getNamespaceMapping(Element element) {
		NamespaceMapping mapping = new NamespaceMapping();
		NamedNodeMap as = element.getAttributes();
		for (int i = 0; i < as.getLength(); i++) {
			Node n = as.item(i);
			String name = n.getNodeName();
			if(name.startsWith(XMLNS_PREFIX)) {
				String actualNamespace = name.substring(XMLNS_PREFIX.length());
				String uri = n.getNodeValue();
				String defaultNamespace = getDefaultNamespace(uri);
				if(defaultNamespace == null) continue;
				mapping.addNamespace(defaultNamespace, actualNamespace);
			}
		}
		return mapping;
	}
	
	public String getDefaultNamespace(String uri) {
		return uriToNamespace.get(uri);
	}
	
	public String getURI(String namespace) {
		return namespaceToURI.get(namespace);
	}
	
	public String getSchema(String namespace) {
		return namespaceToSchema.get(namespace);
	}
	
	public void validateNamespaces(XModelObject object, Element element) {
		NamespaceMapping namespaceMapping = NamespaceMapping.load(object.get("namespaceMapping"));
		if(namespaceMapping == null) namespaceMapping = new NamespaceMapping();
		StringBuffer loc = new StringBuffer();
		loc.append(object.getAttributeValue("xsi:schemaLocation"));
		XModelObject[] cs = object.getChildren();
		Set<String> ns = new HashSet<String>();
		for (int i = 0; i < cs.length; i++) {
			String n = cs[i].getModelEntity().getXMLSubPath();
			int k = n.indexOf(':');
			if(k < 0) continue;
			String defaultNamespace = n.substring(0, k);
			if(ns.contains(defaultNamespace)) continue;
			ns.add(defaultNamespace);
			String uri = getURI(defaultNamespace);
			if(uri == null) continue;
			String actualNamespace = namespaceMapping.getActualNamespace(defaultNamespace);
			if(actualNamespace == null) actualNamespace = defaultNamespace;
			element.setAttribute(XMLNS_PREFIX + actualNamespace, uri);
			String schema = getSchema(defaultNamespace);
			if(loc.indexOf(uri) < 0) {
				loc.append(' ').append(uri).append(' ').append(schema);
			}
			
		}
		object.setAttributeValue("xsi:schemaLocation", loc.toString());
	}

}
