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
 package org.eclipse.bpel.ui.properties;

import org.eclipse.emf.ecore.EObject;

/*
 * This represents an entry in the NamespacesPrefixesSection table in the Namespaces Property Tab.
 * The NamespacesPrefixesProvider builds a list of these to be displayed in the table.
 *
 * @see https://jira.jboss.org/browse/JBIDE-6765
 * @author Bob Brodt
 * @date Aug 13, 2010
 */
public class NamespacePrefixElement {
	
	public String prefix;
	public String namespace;
	public EObject context;
	public String location;
	
	public NamespacePrefixElement(String prefix, String namespace, EObject context, String location)
	{
		this.prefix = prefix;
		this.namespace = namespace;
		this.context = context;
		this.location = location;
	}
}