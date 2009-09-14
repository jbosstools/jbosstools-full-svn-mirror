/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors.csv;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.DocumentException;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.editors.xml.TagList;
import org.jboss.tools.smooks.configuration.editors.xml.XMLObjectAnalyzer;
import org.jboss.tools.smooks.model.csv.CsvReader;
import org.jboss.tools.smooks.model.csv12.CSV12Reader;
import org.jboss.tools.smooks.model.graphics.ext.InputType;
import org.jboss.tools.smooks.model.smooks.AbstractReader;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
import org.milyn.Smooks;
import org.milyn.csv.CSVReaderConfigurator;
import org.milyn.payload.StringResult;

/**
 * @author Dart (dpeng@redhat.com)
 * 
 */
public class CSVDataParser {

	public static final String LINK_CSV_READER = "linkCSVReader";

	public static final String SEPARATOR = "separator";

	public static final String QUOTECHAR = "quoteChar";

	public static final String SKIPLINES = "skiplines";

	public static final String ENCODING = "encoding";

	public static final String FIELDS = "fields";

	public static final String ROOT_ELEMENT_NAME = "rootElementName";

	public static final String RECORD_NAME = "recordName";

	public TagList parseCSV(String filePath, InputType inputType, SmooksResourceListType resourceList)
			throws FileNotFoundException, DocumentException, InvocationTargetException, ParserConfigurationException {
		return parseCSV(new FileInputStream(SmooksUIUtils.parseFilePath(filePath)), inputType, resourceList);
	}

	public TagList parseCSV(InputStream inputStream, Object readerObj) throws ParserConfigurationException,
			DocumentException {
		String fields = null;
		String separator = null;
		String quoteChar = null;
		String skiplines = null;
		String encoding = null;
		String rootName = null;
		String recordName = null;
		if (readerObj == null)
			return null;

		if (readerObj instanceof CsvReader) {
			CsvReader reader = (CsvReader) readerObj;
			fields = reader.getFields();
			separator = reader.getSeparator();
			skiplines = reader.getSkipLines().toString();
			quoteChar = reader.getQuote();
			encoding = reader.getEncoding();
		}
		if (readerObj instanceof CSV12Reader) {
			CSV12Reader reader = (CSV12Reader) readerObj;
			fields = reader.getFields();
			separator = reader.getSeparator();
			skiplines = reader.getSkipLines().toString();
			quoteChar = reader.getQuote();
			encoding = reader.getEncoding();
			rootName = reader.getRootElementName();
			recordName = reader.getRecordElementName();
		}
		return this.parseCSV(inputStream, fields, rootName, recordName, separator, quoteChar, skiplines, encoding);
	}

	public TagList parseCSV(InputStream stream, InputType inputType, SmooksResourceListType resourceList)
			throws DocumentException, ParserConfigurationException {
		List<AbstractReader> readers = resourceList.getAbstractReader();
		int count = 0;
		int index = -1;
		for (Iterator<?> iterator2 = readers.iterator(); iterator2.hasNext();) {
			AbstractReader abstractReader = (AbstractReader) iterator2.next();
			if (abstractReader instanceof CsvReader || abstractReader instanceof CSV12Reader) {
				count++;
				if (index == -1) {
					index = readers.indexOf(abstractReader);
				}
			}

		}

		if (count > 1) {
			// throw new
			// RuntimeException("The smooks config file should have only one JSON reader");
		}
		if (index != -1) {
			return parseCSV(stream, readers.get(index));
			// return parseJsonFile(stream, (JsonReader)
			// readers.get(index));
		}
		return null;
	}

	public TagList parseCSV(String filePath, String fields, String rootName, String recordName, String separator,
			String quoteChar, String skiplines, String encoding) throws DocumentException, FileNotFoundException {
		return parseCSV(new FileInputStream(filePath), fields, rootName, recordName, separator, quoteChar, skiplines,
				encoding);
	}

	public TagList parseCSV(InputStream stream, String fields, String rootName, String recordName, String separator,
			String quoteChar, String skiplines, String encoding) throws DocumentException {

		Smooks smooks = new Smooks();

		// SmooksResourceConfiguration readerConfig = new
		// SmooksResourceConfiguration("org.xml.sax.driver",
		// CSVReader.class.getName());
		if ((quoteChar == null) || (encoding == null) || (fields == null)) {
			return null;
		}
		if (quoteChar == null)
			quoteChar = "\"";
		if (skiplines == null)
			skiplines = "0";
		if (encoding == null)
			encoding = "UTF-8";

		CSVReaderConfigurator readerConfigurator = new CSVReaderConfigurator(fields);
		if (separator != null && separator.length() >= 1) {
			readerConfigurator.setSeparatorChar(separator.toCharArray()[0]);
		}
		if (quoteChar != null && quoteChar.length() >= 1) {
			readerConfigurator.setQuoteChar(quoteChar.toCharArray()[0]);
		}
		if (skiplines != null) {
			try {
				readerConfigurator.setSkipLineCount(Integer.parseInt(skiplines));
			} catch (Throwable t) {

			}
		}
		if (rootName != null) {
			readerConfigurator.setRootElementName(rootName);
		}
		if (recordName != null) {
			readerConfigurator.setRecordElementName(recordName);
		}

		readerConfigurator.setEncoding(Charset.forName(encoding));
		// readerConfigurator.setIndent(indent)

		smooks.setReaderConfig(readerConfigurator);

		StringResult result = new StringResult();
		smooks.filterSource(new StreamSource(stream), result);

		XMLObjectAnalyzer analyzer = new XMLObjectAnalyzer();
		ByteArrayInputStream byteinputStream = new ByteArrayInputStream(result.getResult().getBytes());
		TagList tagList = analyzer.analyze(byteinputStream, null);

		try {
			if (byteinputStream != null) {
				byteinputStream.close();
				byteinputStream = null;
			}
			if (smooks != null) {
				smooks.close();
				smooks = null;
			}
			if (stream != null) {
				stream.close();
				stream = null;
			}
			result = null;
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return tagList;
	}
}
