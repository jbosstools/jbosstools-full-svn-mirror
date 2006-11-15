/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
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
package org.jboss.ide.eclipse.packages.core.model.internal.xb;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.ide.eclipse.packages.core.PackagesCorePlugin;
import org.jboss.ide.eclipse.packages.core.Trace;
import org.jboss.xb.binding.JBossXBException;
import org.jboss.xb.binding.Unmarshaller;
import org.jboss.xb.binding.UnmarshallerFactory;
import org.jboss.xb.binding.XercesXsMarshaller;
import org.jboss.xb.binding.sunday.unmarshalling.SchemaBinding;
import org.jboss.xb.binding.sunday.unmarshalling.XsdBinder;
import org.xml.sax.SAXException;

public class XMLBinding {
	
	public static final int NUM_UNMARSHAL_MONITOR_STEPS = 4;
	public static final int NUM_MARSHALL_MONITOR_STEPS = 2;
	
	private static URL schema = PackagesCorePlugin.getDefault().getBundle().getEntry("xml/packages.xsd");
	private static URL log4jxml = PackagesCorePlugin.getDefault().getBundle().getEntry("log4j.xml");
	
	static {
		System.setProperty("log4j.configuration", log4jxml.toString());
	}
	
	private static void binderSandbox (Runnable runnable)
	{
		ClassLoader original = Thread.currentThread().getContextClassLoader();
		ClassLoader myCL = XMLBinding.class.getClassLoader();
		Thread.currentThread().setContextClassLoader(myCL);
		runnable.run();
		Thread.currentThread().setContextClassLoader(original);
	}
	
	private static XbPackages element = null;
	
	public static XbPackages unmarshal (final InputStream in, final IProgressMonitor monitor)
	{
		element = null;
		
		binderSandbox(new Runnable() {
			public void run ()  {
				try {	
					InputStream stream = schema.openStream();
					monitor.worked(1);
					
					SchemaBinding binding = XsdBinder.bind(stream, "UTF-8", null);
					Unmarshaller unmarshaller = UnmarshallerFactory.newInstance().newUnmarshaller();
					monitor.worked(1);
					
					Object xmlObject = unmarshaller.unmarshal(in, binding);
					monitor.worked(1);
					
					element = (XbPackages) xmlObject;
					
					stream.close();
					
					monitor.worked(1);
					
				} catch (IOException e) {
					Trace.trace(XMLBinding.class, e);
				} catch (JBossXBException e) {
					Trace.trace(XMLBinding.class, e);
				}
			}
		});
		
		return element;
	}
	
	public static void marshal (final XbPackages element, final Writer writer, final IProgressMonitor monitor)
	{
		binderSandbox(new Runnable() {
			public void run ()  {
				try {
					InputStream stream = schema.openStream();
					monitor.worked(1);
					
					XercesXsMarshaller marshaller = new XercesXsMarshaller();
					marshaller.marshal(new InputStreamReader(stream), new XbPackagesObjectProvider(), element, writer);
					monitor.worked(1);
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e )
				{
					e.printStackTrace();
				}
			}
		});
	}
}
