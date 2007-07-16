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
package org.jboss.ide.eclipse.archives.core.model.internal.xb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.Trace;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveImpl;
import org.jboss.xb.binding.JBossXBException;
import org.jboss.xb.binding.Unmarshaller;
import org.jboss.xb.binding.UnmarshallerFactory;
import org.jboss.xb.binding.XercesXsMarshaller;
import org.jboss.xb.binding.sunday.unmarshalling.SchemaBinding;
import org.jboss.xb.binding.sunday.unmarshalling.XsdBinder;
import org.xml.sax.SAXException;

public class XMLBinding {
	
	public static final int NUM_UNMARSHAL_MONITOR_STEPS = 3;
	public static final int NUM_MARSHALL_MONITOR_STEPS = 2;
	
	private static URL schema = ArchivesCore.getInstance().getVariables().getBindingSchema(); 
	private static URL log4jxml = ArchivesCore.getInstance().getVariables().getBindingLog4j();
	private static SchemaBinding binding;
	
	private static boolean initialized = false;
	
	static {
		System.setProperty("log4j.configuration", log4jxml.toString());
	}
	
	public static void init ()
	{
		try {
			InputStream stream = schema.openStream();
			binding = XsdBinder.bind(stream, "UTF-8", null);

			stream.close();
			initialized = true;
		} catch (IOException e) {
			Trace.trace(XMLBinding.class, e);
		}
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
	
	public static XbPackages unmarshal( String input, IProgressMonitor monitor ) {
		return unmarshal(new ByteArrayInputStream(input.getBytes()), monitor);
	}
	
	public static XbPackages unmarshal (final InputStream in, final IProgressMonitor monitor)
	{
		if( !initialized) init();
		element = null;
		
		binderSandbox(new Runnable() {
			public void run ()  {
				try {	
					Unmarshaller unmarshaller = UnmarshallerFactory.newInstance().newUnmarshaller();
					monitor.worked(1);
					
					Object xmlObject = unmarshaller.unmarshal(in, binding);
					monitor.worked(1);
					
					element = (XbPackages) xmlObject;
					monitor.worked(1);
					
				} catch (JBossXBException e) {
					Trace.trace(XMLBinding.class, e);
				}
			}
		});
		
		return element;
	}
	
	public static String marshall(IArchive topLevelArchive, IProgressMonitor monitor ) {
		if( topLevelArchive.isTopLevel() && topLevelArchive instanceof ArchiveImpl ) {
			XbPackages packs = (XbPackages)((ArchiveImpl)topLevelArchive).getNodeDelegate().getParent();
			StringWriter sw = new StringWriter();
			marshall(packs, sw, monitor);
			return sw.toString();
		}
		return null;
	}
	public static void marshall (final XbPackages element, final Writer writer, final IProgressMonitor monitor)
	{
		if( !initialized) init();
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
	
	
	public static void savePackagesToFile(XbPackages packages, IPath outputFile, IProgressMonitor monitor) {
		try {
			ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(bytesOut);
			XMLBinding.marshall(packages, writer, monitor);
			writer.close();
			
			ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesOut.toByteArray());
			OutputStream out = new FileOutputStream(outputFile.toFile());
	
			// Transfer bytes from in to out
	        byte[] buf = new byte[1024];
	        int len;
	        while ((len = bytesIn.read(buf)) > 0) {
	            out.write(buf, 0, len);
	        }
			out.close();
			bytesIn.close();
			bytesOut.close();
		} catch( IOException ioe ) {
		}
	}
	
	public static String serializePackages(XbPackages packages, IProgressMonitor monitor) {
		try {
			ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(bytesOut);
			XMLBinding.marshall(packages, writer, monitor);
			writer.close();
			return new String(bytesOut.toByteArray());
		} catch( Exception e ) {
		}
		return null;
	}
}
