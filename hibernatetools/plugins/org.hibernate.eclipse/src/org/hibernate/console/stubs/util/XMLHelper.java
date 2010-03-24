package org.hibernate.console.stubs.util;

import java.util.List;

import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

@SuppressWarnings("unchecked")
public class XMLHelper {

	private static final Logger log = LoggerFactory.getLogger(XMLHelper.class);
	private SAXReader saxReader;

	public SAXReader createSAXReader(String file, List errorsList, EntityResolver entityResolver) {
		if (saxReader==null) saxReader = new SAXReader();
		saxReader.setEntityResolver(entityResolver);
		saxReader.setErrorHandler(new ErrorLogger(file, errorsList));
		saxReader.setMergeAdjacentText(true);
		saxReader.setValidation(true);
		return saxReader;
	}

	public static class ErrorLogger implements ErrorHandler {
		private String file;
		private List errors;
		ErrorLogger(String file, List errors) {
			this.file=file;
			this.errors = errors;
		}
		public void error(SAXParseException error) {
			log.error( "Error parsing XML: " + file + '(' + error.getLineNumber() + ") " + error.getMessage() ); //$NON-NLS-1$ //$NON-NLS-2$
			errors.add(error);
		}
		public void fatalError(SAXParseException error) {
			error(error);
		}
		public void warning(SAXParseException warn) {
			log.warn( "Warning parsing XML: " + file + '(' + warn.getLineNumber() + ") " + warn.getMessage() ); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
}
