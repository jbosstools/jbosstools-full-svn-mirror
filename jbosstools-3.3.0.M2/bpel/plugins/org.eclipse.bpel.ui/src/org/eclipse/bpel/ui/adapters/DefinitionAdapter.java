/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.eclipse.bpel.ui.adapters;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpel.model.adapters.BasicEObjectaAdapter;
import org.eclipse.bpel.model.adapters.INamespaceMap;
import org.eclipse.wst.wsdl.Definition;

/*
 * This adds the INamespace adapter to Definition objects (required for the XPath expression editor)
 *
 * @see https://jira.jboss.org/browse/JBIDE-7107
 * @author Bob Brodt
 * @date Oct 12, 2010
 */
public class DefinitionAdapter extends BasicEObjectaAdapter implements INamespaceMap<String, String> {

	/**
	 * 
	 * @param key
	 *            the namespace to get the reverse mapping for
	 * @return The reverse mapping of the Namespace to namespace prefixes.
	 * @see org.eclipse.bpel.model.adapters.INamespaceMap#getReverse(java.lang.Object)
	 */

	public List<String> getReverse(String key) {
		List<String> list = new ArrayList<String>(1);
		list.add( ((Definition)getTarget()).getPrefix(key) );
		return list;
	}

}
