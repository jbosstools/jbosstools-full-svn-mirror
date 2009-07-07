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
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.DocumentException;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.editors.xml.TagList;
import org.jboss.tools.smooks.configuration.editors.xml.XMLObjectAnalyzer;
import org.jboss.tools.smooks.model.csv.CsvReader;
import org.jboss.tools.smooks.model.graphics.ext.InputType;
import org.jboss.tools.smooks.model.graphics.ext.ParamType;
import org.jboss.tools.smooks.model.smooks.AbstractReader;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
import org.milyn.Smooks;
import org.milyn.SmooksUtil;
import org.milyn.cdr.SmooksResourceConfiguration;
import org.milyn.csv.CSVReader;
import org.milyn.xml.XmlUtil;
import org.w3c.dom.Document;

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

	public TagList parseCSV(String filePath, InputType inputType, SmooksResourceListType resourceList)
			throws FileNotFoundException, DocumentException, InvocationTargetException, ParserConfigurationException {
		return parseCSV(new FileInputStream(SmooksUIUtils.parseFilePath(filePath)), inputType, resourceList);
	}

	public TagList parseCSV(InputStream inputStream, CsvReader reader)
			throws ParserConfigurationException, DocumentException {
		String fields = null;
		String separator = null;
		String quoteChar = null;
		String skiplines = null;
		String encoding = null;
		if (reader == null)
			return null;
		fields = reader.getFields();
		separator = reader.getSeparator();
		skiplines = reader.getSkipLines().toString();
		quoteChar = reader.getQuote();
		encoding = reader.getEncoding();
		return this.parseCSV(inputStream, fields, separator, quoteChar, skiplines, encoding);
	}

	public TagList parseCSV(InputStream stream, InputType inputType, SmooksResourceListType resourceList)
			throws DocumentException, ParserConfigurationException {
		List<ParamType> paramList = inputType.getParam();
		String fields = null;
		String separator = null;
		String quoteChar = null;
		String skiplines = null;
		String encoding = null;

		for (Iterator<?> iterator = paramList.iterator(); iterator.hasNext();) {
			ParamType paramType = (ParamType) iterator.next();
			if (paramType.getName().equals(LINK_CSV_READER)) {
				if (paramType.getValue().equalsIgnoreCase("true") && resourceList != null) {
					List<AbstractReader> readers = resourceList.getAbstractReader();
					int count = 0;
					int index = -1;
					for (Iterator<?> iterator2 = readers.iterator(); iterator2.hasNext();) {
						AbstractReader abstractReader = (AbstractReader) iterator2.next();
						if (abstractReader instanceof CsvReader) {
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
						return parseCSV(stream, (CsvReader)readers.get(index));
						// return parseJsonFile(stream, (JsonReader)
						// readers.get(index));
					}

				}
			}
			if (paramType.getName().equals(FIELDS)) {
				fields = paramType.getValue();
				try {
//					fields = fields.replace(';', ',');
				} catch (Throwable t) {

				}
			}
			if (paramType.getName().equals(SEPARATOR)) {
				separator = paramType.getValue();
			}
			if (paramType.getName().equals(SKIPLINES)) {
				skiplines = paramType.getValue();
			}
			if (paramType.getName().equals(QUOTECHAR)) {
				quoteChar = paramType.getValue();
			}
			if (paramType.getName().equals(ENCODING)) {
				encoding = paramType.getValue();
			}
		}

		return this.parseCSV(stream, fields, separator, quoteChar, skiplines, encoding);
	}

	public TagList parseCSV(String filePath, String fields, String separator, String quoteChar, String skiplines,
			String encoding) throws DocumentException, FileNotFoundException {
		return parseCSV(new FileInputStream(filePath), fields, separator, quoteChar, skiplines, encoding);
	}

	public TagList parseCSV(InputStream stream, String fields, String separator, String quoteChar, String skiplines,
			String encoding) throws DocumentException {

		Smooks smooks = new Smooks();
		SmooksResourceConfiguration readerConfig = new SmooksResourceConfiguration("org.xml.sax.driver",
				CSVReader.class.getName());
		if(quoteChar == null) quoteChar = "\"";
		if(skiplines == null) skiplines = "0";
		if(encoding == null) encoding = "UTF-8";
		readerConfig.setParameter("fields", fields);
		readerConfig.setParameter("separator", separator);
		readerConfig.setParameter("quote-char", quoteChar);
		readerConfig.setParameter("skip-line-count", skiplines);
		readerConfig.setParameter("encoding", encoding);

		SmooksUtil.registerResource(readerConfig, smooks);

		DOMResult domResult = new DOMResult();

		// Filter the message through Smooks and capture the result as a DOM in
		// the domResult instance...
		smooks.filter(new StreamSource(stream), domResult);

		// Get the Document object from the domResult. This is the message
		// model!!!...
		Document model = (Document) domResult.getNode();

		// So using the model Document, you can construct a tree structure for
		// the editor.
		
		StringWriter modelWriter = new StringWriter();
		XmlUtil.serialize(model, true, modelWriter);

		XMLObjectAnalyzer analyzer = new XMLObjectAnalyzer();
		ByteArrayInputStream byteinputStream = new ByteArrayInputStream(modelWriter.toString().getBytes());
		TagList tagList = analyzer.analyze(byteinputStream, null);

		try {
			if (byteinputStream != null) {
				byteinputStream.close();
				byteinputStream = null;
			}
			if (modelWriter != null) {
				modelWriter.close();
				modelWriter = null;
			}
			if (smooks != null) {
				smooks.close();
				smooks = null;
			}
			if (stream != null) {
				stream.close();
				stream = null;
			}
			model = null;
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return tagList;
	}
}
