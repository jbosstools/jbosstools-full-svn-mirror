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
 * 02110-1301 USA, or see the FSF site: http:/*
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
package org.jboss.tools.hibernate3_5.console;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.hibernate.annotations.common.util.ReflectHelper;
import org.hibernate.console.HibernateConsoleRuntimeException;
import org.hibernate.eclipse.console.HibernateConsoleMessages;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.hibernate.eclipse.console.model.impl.ExporterProperty;
import org.hibernate.tool.hbm2x.Exporter;

/**
 * Represents what is specified in plugin.xml about possible exporters.
 *
 */
public class ExporterDefinition {

	private String classname;

	private String description;

	private String id;

	private ImageDescriptor iconDescriptor;

	private Map<String, ExporterProperty> properties;

	private IConfigurationElement element;

	public ExporterDefinition(IConfigurationElement element) {
		init(element.getAttribute( "classname" ), //$NON-NLS-1$
			    element.getAttribute( "description" ), //$NON-NLS-1$
				element.getAttribute( "id" ), //$NON-NLS-1$
				createProperties( element ),
				createIcon( element ));
		this.element = element;
	}

	protected void init(String className, String description, String id, Map<String, ExporterProperty> properties, ImageDescriptor icon) {
		this.classname = className;
		this.description = description;
		this.id = id;
		this.properties = properties;
		this.iconDescriptor = icon;
	}

	static private ImageDescriptor createIcon(IConfigurationElement element) {
		if ( element.getAttribute( "icon" ) != null ) { //$NON-NLS-1$
			return AbstractUIPlugin.imageDescriptorFromPlugin(
					element.getNamespace(), element.getAttribute( "icon" ) ); //$NON-NLS-1$
		} else {
			return null;
		}
	}

	static private Map<String, ExporterProperty> createProperties(IConfigurationElement element) {
		Map<String, ExporterProperty> properties = new HashMap<String, ExporterProperty>();

		IConfigurationElement propertyElements[] = element
				.getChildren( "property" ); //$NON-NLS-1$
		for (int i = 0; i < propertyElements.length; i++) {
			ExporterProperty property = new ExporterProperty(
				propertyElements[i].getAttribute("name"), //$NON-NLS-1$
				propertyElements[i].getAttribute("description"), //$NON-NLS-1$
				propertyElements[i].getAttribute("value"), //$NON-NLS-1$
				Boolean.valueOf(propertyElements[i].getAttribute("required")).booleanValue()); //$NON-NLS-1$
				String type = propertyElements[i].getAttribute("type"); //$NON-NLS-1$
				if (type != null){
					property.setType(type);
				}
			 	properties.put(property.getName(),property);
			 }
			 return properties;
	}


	public Exporter createExporterInstance() {
	   Exporter exporter = null;

	   try {
		   exporter = (Exporter) ReflectHelper.classForName( classname ).newInstance();
	   }
	   catch (InstantiationException e) {
		   throw new HibernateConsoleRuntimeException(NLS.bind(
				   HibernateConsoleMessages.ExporterDefinition_problem_creating_exporter_class, classname));
	   }
	   catch (IllegalAccessException e) {
		   throw new HibernateConsoleRuntimeException(NLS.bind(
				   HibernateConsoleMessages.ExporterDefinition_problem_creating_exporter_class, classname));	}
	   catch (ClassNotFoundException e) {
		   throw new HibernateConsoleRuntimeException(NLS.bind(
				   HibernateConsoleMessages.ExporterDefinition_problem_creating_exporter_class, classname));
	   }

	   return exporter;
	}

	public String getDescription() {
		return description;
	}

	public ImageDescriptor getIconDescriptor() {
		return iconDescriptor;
	}

	public Map<String, ExporterProperty> getExporterProperties() {
		return properties;
	}

	public boolean isEnabled(ILaunchConfiguration configuration) {
		boolean enabled = false;

		try {
			// if we put this in some "namespace" we should have a way to either
			// migrate an existing one...
			enabled = configuration.getAttribute( id, false );
		}
		catch (CoreException e) {
			// log and assume false
			HibernateConsolePlugin.getDefault().log(e);
			return false;
		}

		return enabled;
	}

	public String getId() {
		return id;
	}

	public String getExporterTag() {
		return getId().substring(getId().lastIndexOf(".") + 1); //$NON-NLS-1$
	}
}
