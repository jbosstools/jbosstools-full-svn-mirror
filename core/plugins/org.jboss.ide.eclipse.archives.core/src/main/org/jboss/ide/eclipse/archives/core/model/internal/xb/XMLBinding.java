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
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchivesLogger;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveImpl;
import org.jboss.xb.binding.JBossXBException;
import org.jboss.xb.binding.Unmarshaller;
import org.jboss.xb.binding.UnmarshallerFactory;
import org.jboss.xb.binding.XercesXsMarshaller;
import org.jboss.xb.binding.sunday.unmarshalling.SchemaBinding;
import org.jboss.xb.binding.sunday.unmarshalling.XsdBinder;
import org.xml.sax.SAXException;

/**
 * This class is responsible for binding some xml file to it's proper
 * objects. In short, it marshalls and unmarshalls the data.
 * @author Marshall
 *
 */
public class XMLBinding {
	
	public static final int NUM_UNMARSHAL_MONITOR_STEPS = 3;
	public static final int NUM_MARSHALL_MONITOR_STEPS = 2;

	private static URL schema = XMLBinding.class.getClassLoader().getResource("packages.xsd"); 
	private static URL log4jxml = XMLBinding.class.getClassLoader().getResource("log4j.xml");
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
			ArchivesCore.getInstance().getLogger().log(IArchivesLogger.MSG_ERR, e.getMessage(), e);
		}
	}
	
	private static void binderSandbox (XbRunnable runnable) throws XbException {
		ClassLoader original = Thread.currentThread().getContextClassLoader();
		ClassLoader myCL = XMLBinding.class.getClassLoader();
		Thread.currentThread().setContextClassLoader(myCL);
		XbException e = null;
		try {
			runnable.run();
		} catch( XbException ex ) {
			e = ex;
		}
		Thread.currentThread().setContextClassLoader(original);
		if( e != null )
			throw e;
	}
	
	public static XbPackages unmarshal( String input, IProgressMonitor monitor ) throws XbException {
		return unmarshal(new ByteArrayInputStream(input.getBytes()), monitor);
	}
	
	public static XbPackages unmarshal (final InputStream in, 
				final IProgressMonitor monitor) throws XbException {
		if( !initialized) init();
		final XbPackages[] element = new XbPackages[1];
		element[0] = null;
		XbRunnable runnable = new XbRunnable() {
			public void run () throws XbException {
				try {	
					Unmarshaller unmarshaller = UnmarshallerFactory.newInstance().newUnmarshaller();
					monitor.worked(1);
					
					Object xmlObject = unmarshaller.unmarshal(in, binding);
					monitor.worked(1);
					
					element[0] = (XbPackages) xmlObject;
					monitor.worked(1);
					
				} catch (JBossXBException e) {
					throw new XbException(e);
				}
			}
		};
		
		binderSandbox(runnable);
		return element[0];
	}
	
	public static String marshall(IArchive topLevelArchive, IProgressMonitor monitor ) throws XbException {
		if( topLevelArchive.isTopLevel() && topLevelArchive instanceof ArchiveImpl ) {
			XbPackages packs = (XbPackages)((ArchiveImpl)topLevelArchive).getNodeDelegate().getParent();
			StringWriter sw = new StringWriter();
			marshall(packs, sw, monitor);
			return sw.toString();
		}
		return null;
	}
	
	public static void marshallToFile(XbPackages element, IPath filePath, IProgressMonitor monitor) {
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(filePath.toFile()));
			XMLBinding.marshall(element, writer, monitor);
		} catch( Exception e ) {
		}
		finally {
			try {
			if( writer != null ) writer.close();
			} catch( IOException ioe) {}
		}
	}
	
	public static void marshall (final XbPackages element, final Writer writer, 
			final IProgressMonitor monitor) throws XbException {
		if( !initialized) init();
		binderSandbox(new XbRunnable() {
			public void run () throws XbException {
				Exception f = null;
				InputStream stream = null;
				try {
					stream = schema.openStream();
					monitor.worked(1);
					
					XercesXsMarshaller marshaller = new XercesXsMarshaller();
					marshaller.marshal(new InputStreamReader(stream), new XbPackagesObjectProvider(), element, writer);
					monitor.worked(1);
				} catch (IOException e) {
					f = e;
				} catch (SAXException e) {
					f = e;
				} catch (ParserConfigurationException e) {
					f = e;
				} catch (Exception e ) {
					f = e;
				} finally {
					if( stream != null ) {
						try {
							stream.close();
						} catch(IOException ioe) {}
					}
				}
				if( f != null ) {
					throw new XbException(f);
				}
			}
		});
	}
	
	public static String serializePackages(XbPackages packages, IProgressMonitor monitor) throws XbException {
		OutputStreamWriter writer = null;
		try {
			ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
			writer = new OutputStreamWriter(bytesOut);
			XMLBinding.marshall(packages, writer, monitor);
			return new String(bytesOut.toByteArray());
		} catch( Exception e ) {
			throw new XbException(e);
		} finally {
			if( writer != null )
				try {
					writer.close();
				} catch( IOException ioe) {}
		}
	}
	
	
	public static interface XbRunnable {
		public void run() throws XbException;
	}
	
	public static class XbException extends Exception {
		private Exception parent;
		public XbException(Exception e) {
			parent = e;
		}
		public Exception getException() {
			return parent;
		}
	}
}
