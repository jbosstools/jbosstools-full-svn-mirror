/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.ITextContentDescriber;
import org.jboss.tools.smooks.model.calc.CalcPackage;
import org.jboss.tools.smooks.model.csv.CsvPackage;
import org.jboss.tools.smooks.model.datasource.DatasourcePackage;
import org.jboss.tools.smooks.model.edi.EdiPackage;
import org.jboss.tools.smooks.model.fileRouting.FileRoutingPackage;
import org.jboss.tools.smooks.model.freemarker.FreemarkerPackage;
import org.jboss.tools.smooks.model.groovy.GroovyPackage;
import org.jboss.tools.smooks.model.iorouting.IoroutingPackage;
import org.jboss.tools.smooks.model.javabean.JavabeanPackage;
import org.jboss.tools.smooks.model.jmsrouting.JmsroutingPackage;
import org.jboss.tools.smooks.model.json.JsonPackage;
import org.jboss.tools.smooks.model.medi.MEdiPackage;
import org.jboss.tools.smooks.model.xsl.XslPackage;
import org.jboss.tools.smooks10.model.smooks.SmooksPackage;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Dart (dpeng@redhat.com) note : Many codes comes from
 *         org.eclipse.core.internal.content.XMLConentDescriber
 */
public class SmooksConfigfileContentDescriber implements ITextContentDescriber, IExecutableExtension {

	private static final QualifiedName[] SUPPORTED_OPTIONS = new QualifiedName[] { IContentDescription.CHARSET,
			IContentDescription.BYTE_ORDER_MARK };

	private static final String ENCODING = "encoding="; //$NON-NLS-1$

	private static final String XML_PREFIX = "<?xml "; //$NON-NLS-1$

	/**
	 * 
	 */
	public SmooksConfigfileContentDescriber() {
		// TODO Auto-generated constructor stub
	}

	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {

	}

	public int describeXMLFile(InputStream input, IContentDescription description) throws IOException {
		byte[] bom = getByteOrderMark(input);
		String xmlDeclEncoding = "UTF-8"; //$NON-NLS-1$
		input.reset();
		if (bom != null) {
			if (bom == IContentDescription.BOM_UTF_16BE)
				xmlDeclEncoding = "UTF-16BE"; //$NON-NLS-1$
			else if (bom == IContentDescription.BOM_UTF_16LE)
				xmlDeclEncoding = "UTF-16LE"; //$NON-NLS-1$
			// skip BOM to make comparison simpler
			input.skip(bom.length);
			// set the BOM in the description if requested
			if (description != null && description.isRequested(IContentDescription.BYTE_ORDER_MARK))
				description.setProperty(IContentDescription.BYTE_ORDER_MARK, bom);
		}
		byte[] xmlPrefixBytes = XML_PREFIX.getBytes(xmlDeclEncoding);
		byte[] prefix = new byte[xmlPrefixBytes.length];
		if (input.read(prefix) < prefix.length)
			// there is not enough info to say anything
			return INDETERMINATE;
		for (int i = 0; i < prefix.length; i++)
			if (prefix[i] != xmlPrefixBytes[i])
				// we don't have a XMLDecl... there is not enough info to say
				// anything
				return INDETERMINATE;
		if (description == null)
			return VALID;
		// describe charset if requested
		if (description.isRequested(IContentDescription.CHARSET)) {
			String fullXMLDecl = readFullXMLDecl(input, xmlDeclEncoding);
			if (fullXMLDecl != null) {
				String charset = getCharset(fullXMLDecl);
				if (charset != null && !"UTF-8".equalsIgnoreCase(charset)) //$NON-NLS-1$
					// only set property if value is not default (avoid using a
					// non-default content description)
					description.setProperty(IContentDescription.CHARSET, getCharset(fullXMLDecl));
			}
		}
		return VALID;
	}

	public int describeXMLType(Reader input, IContentDescription description) throws IOException {
		BufferedReader reader = new BufferedReader(input);
		String line = reader.readLine();
		// end of stream
		if (line == null)
			return INDETERMINATE;
		// XMLDecl should be the first string (no blanks allowed)
		if (!line.startsWith(XML_PREFIX))
			return INDETERMINATE;
		if (description == null)
			return VALID;
		// describe charset if requested
		if ((description.isRequested(IContentDescription.CHARSET)))
			description.setProperty(IContentDescription.CHARSET, getCharset(line));
		return VALID;
	}

	public int describe(InputStream input, IContentDescription description) throws IOException {
		if (this.describeXMLFile(input, description) == INVALID) {
			return INVALID;
		}
		input.reset();
		return checkCriteria(new InputSource(input));
	}

	private String readFullXMLDecl(InputStream input, String unicodeEncoding) throws IOException {
		byte[] xmlDecl = new byte[100];
		int c = 0;
		// looks for XMLDecl ending char (?)
		int read = 0;
		while (read < xmlDecl.length && (c = input.read()) != -1 && c != '?')
			xmlDecl[read++] = (byte) c;
		return c == '?' ? new String(xmlDecl, 0, read, unicodeEncoding) : null;
	}

	public int describe(Reader input, IContentDescription description) throws IOException {
		if (this.describeXMLType(input, description) == INVALID) {
			return INVALID;
		}
		input.reset();
		return checkCriteria(new InputSource(input));
	}
	
	/**
	 * To check the namespaces of the file
	 * @param contents
	 * @return
	 * @throws IOException
	 */
	private int checkCriteria(InputSource contents) throws IOException {
		SmooksConfigFileHandle handle = new SmooksConfigFileHandle();
		try {
			if (!handle.parseContents(contents)) {
				return INDETERMINATE;
			}
		} catch (ParserConfigurationException e) {
			return INDETERMINATE;
		}
		if(handle.isSmooksConfigFile()){
			return VALID;
		}
		return INDETERMINATE;
	}

	private String getCharset(String firstLine) {
		int encodingPos = firstLine.indexOf(ENCODING);
		if (encodingPos == -1)
			return null;
		char quoteChar = '"';
		int firstQuote = firstLine.indexOf(quoteChar, encodingPos);
		if (firstQuote == -1) {
			quoteChar = '\'';
			firstQuote = firstLine.indexOf(quoteChar, encodingPos);
		}
		if (firstQuote == -1 || firstLine.length() == firstQuote - 1)
			return null;
		int secondQuote = firstLine.indexOf(quoteChar, firstQuote + 1);
		if (secondQuote == -1)
			return null;
		return firstLine.substring(firstQuote + 1, secondQuote);
	}

	public QualifiedName[] getSupportedOptions() {
		return SUPPORTED_OPTIONS;
	}

	byte[] getByteOrderMark(InputStream input) throws IOException {
		int first = input.read();
		if (first == 0xEF) {
			// look for the UTF-8 Byte Order Mark (BOM)
			int second = input.read();
			int third = input.read();
			if (second == 0xBB && third == 0xBF)
				return IContentDescription.BOM_UTF_8;
		} else if (first == 0xFE) {
			// look for the UTF-16 BOM
			if (input.read() == 0xFF)
				return IContentDescription.BOM_UTF_16BE;
		} else if (first == 0xFF) {
			if (input.read() == 0xFE)
				return IContentDescription.BOM_UTF_16LE;
		}
		return null;
	}

	private class SmooksConfigFileHandle extends DefaultHandler {

		private boolean isSmooksConfigFile = false;

		//		private static final String SMOOKS_RESOURCE_LIST = "smooks-resource-list"; //$NON-NLS-1$

		private String[] smooksSpportURI = null;

		private SAXParserFactory fFactory;
		
		public SmooksConfigFileHandle(){
			super();
			smooksSpportURI = new String[]{
				SmooksPackage.eNS_URI,
				org.jboss.tools.smooks.model.smooks.SmooksPackage.eNS_URI,
				EdiPackage.eNS_URI,
				XslPackage.eNS_URI,
				FreemarkerPackage.eNS_URI,
				GroovyPackage.eNS_URI,
				MEdiPackage.eNS_URI,
				CalcPackage.eNS_URI,
				CsvPackage.eNS_URI,
				DatasourcePackage.eNS_URI,
				FileRoutingPackage.eNS_URI,
				IoroutingPackage.eNS_URI,
				JavabeanPackage.eNS_URI,
				JmsroutingPackage.eNS_URI,
				JsonPackage.eNS_URI,
			};
		}

		private final SAXParser createParser(SAXParserFactory parserFactory) throws ParserConfigurationException,
				SAXException, SAXNotRecognizedException, SAXNotSupportedException {
			// Initialize the parser.
			final SAXParser parser = parserFactory.newSAXParser();
			final XMLReader reader = parser.getXMLReader();
			// disable DTD validation
			try {
				reader.setFeature("http://xml.org/sax/features/validation", false); //$NON-NLS-1$
				reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false); //$NON-NLS-1$
			} catch (SAXNotRecognizedException e) {
			} catch (SAXNotSupportedException e) {
			}
			return parser;
		}

		private SAXParserFactory getFactory() {
			synchronized (this) {
				if (fFactory != null) {
					return fFactory;
				}
				fFactory = SAXParserFactory.newInstance();
				fFactory.setNamespaceAware(true);
			}
			return fFactory;
		}

		public boolean parseContents(InputSource contents) throws ParserConfigurationException, IOException {
			try {
				fFactory = getFactory();
				if (fFactory == null) {
					return false;
				}
				final SAXParser parser = createParser(fFactory);
				contents.setSystemId("/"); //$NON-NLS-1$
				parser.parse(contents, this);
			} catch (SAXException e) {
				// stop parsing
			}
			return true;
		}

		/*
		 * Resolve external entity definitions to an empty string. This is to
		 * speed up processing of files with external DTDs. Not resolving the
		 * contents of the DTD is ok, as only the System ID of the DTD
		 * declaration is used.
		 * 
		 * @see
		 * org.xml.sax.helpers.DefaultHandler#resolveEntity(java.lang.String,
		 * java.lang.String)
		 */
		public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
			return new InputSource(new StringReader("")); //$NON-NLS-1$
		}

		@Override
		public void startPrefixMapping(String prefix, String uri) throws SAXException {
			super.startPrefixMapping(prefix, uri);
			if (containtSmooksURI(uri)) {
				setSmooksConfigFile(true);
				throw new SAXException("Stop parsing");
			}
		}

		private boolean containtSmooksURI(String uri) {
			for (int i = 0; i < smooksSpportURI.length; i++) {
				if (uri != null) {
					if (uri.trim().equalsIgnoreCase(smooksSpportURI[i])) {
						return true;
					}
				}
			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
		 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		public final void startElement(final String uri, final String elementName, final String qualifiedName,
				final Attributes attributes) throws SAXException {
			if (isSmooksConfigFile()) {
				throw new SAXException("Stop parsing");
			}
			if (containtSmooksURI(uri)) {
				setSmooksConfigFile(true);
				throw new SAXException("Stop parsing");
			}
		}

		public boolean isSmooksConfigFile() {
			return isSmooksConfigFile;
		}

		public void setSmooksConfigFile(boolean isSmooksConfigFile) {
			this.isSmooksConfigFile = isSmooksConfigFile;
		}
	}
}
