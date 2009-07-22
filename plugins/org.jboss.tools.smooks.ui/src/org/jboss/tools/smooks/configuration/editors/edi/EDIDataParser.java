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
package org.jboss.tools.smooks.configuration.editors.edi;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.DocumentException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.ecore.EObject;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.editors.xml.TagList;
import org.jboss.tools.smooks.configuration.editors.xml.XMLObjectAnalyzer;
import org.jboss.tools.smooks.model.edi.EDIReader;
import org.jboss.tools.smooks.model.graphics.ext.InputType;
import org.jboss.tools.smooks.model.graphics.ext.ParamType;
import org.jboss.tools.smooks.model.smooks.AbstractReader;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
import org.milyn.Smooks;
import org.milyn.SmooksUtil;
import org.milyn.cdr.SmooksResourceConfiguration;
import org.milyn.smooks.edi.SmooksEDIReader;
import org.milyn.xml.XmlUtil;
import org.w3c.dom.Document;

/**
 * @author Dart
 * 
 */
public class EDIDataParser {
	public static final String USE_AVAILABEL_READER = "use_availableReader";

	public static final String ENCODING = "encoding";

	public static final Object MAPPING_MODEL = "mappingModelFile";
	
	public TagList parseEDIFile(InputStream stream, InputType inputType,SmooksResourceListType resourceList , IProject project)
	throws IOException, DocumentException {
		List<ParamType> paramList = inputType.getParam();
		String encoding = null;
		String mappingModel = null;

		for (Iterator<?> iterator = paramList.iterator(); iterator.hasNext();) {
			ParamType paramType = (ParamType) iterator.next();
			if (paramType.getName().equals(USE_AVAILABEL_READER)) {
				if (paramType.getValue().equalsIgnoreCase("true") && resourceList != null) {
					List<AbstractReader> readers = resourceList.getAbstractReader();
					int count = 0;
					int index = -1;
					for (Iterator<?> iterator2 = readers.iterator(); iterator2.hasNext();) {
						AbstractReader abstractReader = (AbstractReader) iterator2.next();
						if (abstractReader instanceof EDIReader) {
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
						return parseEDIFile(stream, (EDIReader) readers.get(index) , project);
					}

				}
			}
			if (paramType.getName().equals(ENCODING)) {
				encoding = paramType.getValue();
			}
			if (paramType.getName().equals(MAPPING_MODEL)) {
				mappingModel = paramType.getValue();
			}
		}

		return parseEDIFile(stream, mappingModel, encoding, project);
	}

	public TagList parseEDIFile(InputStream stream, InputType inputType, SmooksResourceListType resourceList)
			throws IOException, DocumentException {

		List<ParamType> paramList = inputType.getParam();
		String encoding = null;
		String mappingModel = null;

		for (Iterator<?> iterator = paramList.iterator(); iterator.hasNext();) {
			ParamType paramType = (ParamType) iterator.next();
			if (paramType.getName().equals(USE_AVAILABEL_READER)) {
				if (paramType.getValue().equalsIgnoreCase("true") && resourceList != null) {
					List<AbstractReader> readers = resourceList.getAbstractReader();
					int count = 0;
					int index = -1;
					for (Iterator<?> iterator2 = readers.iterator(); iterator2.hasNext();) {
						AbstractReader abstractReader = (AbstractReader) iterator2.next();
						if (abstractReader instanceof EDIReader) {
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
						return parseEDIFile(stream, (EDIReader) readers.get(index));
					}

				}
			}
			if (paramType.getName().equals(ENCODING)) {
				encoding = paramType.getValue();
			}
			if (paramType.getName().equals(MAPPING_MODEL)) {
				mappingModel = paramType.getValue();
			}
		}

		return parseEDIFile(stream, mappingModel, encoding, resourceList);
	}
	public TagList parseEDIFile(InputStream ediInputStream, EDIReader reader , IProject project) throws IOException, DocumentException {
		String encoding = reader.getEncoding();
		String mappingModel = reader.getMappingModel();
		return parseEDIFile(ediInputStream, mappingModel, encoding, project);
	}

	public TagList parseEDIFile(InputStream ediInputStream, EDIReader reader) throws IOException, DocumentException {
		String encoding = reader.getEncoding();
		String mappingModel = reader.getMappingModel();
		return parseEDIFile(ediInputStream, mappingModel, encoding, reader);
	}

	public TagList parseEDIFile(InputStream ediInputStream, String mappingModel, String ediFileEncoding, EObject emodel)
			throws IOException, DocumentException {
		IResource resource = SmooksUIUtils.getResource(emodel);
		IProject project = null;
		if (resource != null) {
			project = resource.getProject();
		}
		return parseEDIFile(ediInputStream, mappingModel, ediFileEncoding, project);
	}

	public TagList parseEDIFile(InputStream ediInputStream, String mappingModel, String ediFileEncoding,
			IProject project) throws IOException, DocumentException {
		Smooks smooks = new Smooks();

		SmooksResourceConfiguration readerConfig = new SmooksResourceConfiguration("org.xml.sax.driver",
				SmooksEDIReader.class.getName());
		File f = new File(mappingModel);
		String modelPath = mappingModel;
		if (f.exists()) {
			modelPath = f.toURI().toString();
		} else {
			IFile tf = SmooksUIUtils.getFile(mappingModel, project);
			if (tf != null) {
				modelPath = tf.getLocation().toFile().toURI().toString();
			}
		}

		readerConfig.setParameter("mapping-model", modelPath);
		if (ediFileEncoding == null || ediFileEncoding.trim().length() == 0) {
			ediFileEncoding = "UTF-8";
		}
		readerConfig.setParameter("encoding", ediFileEncoding);

		SmooksUtil.registerResource(readerConfig, smooks);

		// Use a DOM result to capture the message model for the supplied
		// CSV
		// message...
		DOMResult domResult = new DOMResult();

		// Filter the message through Smooks and capture the result as a DOM
		// in
		// the domResult instance...
		// FileInputStream stream
		smooks.filter(new StreamSource(ediInputStream), domResult);

		// Get the Document object from the domResult. This is the message
		// model!!!...
		Document model = (Document) domResult.getNode();

		// So using the model Document, you can construct a tree structure
		// for
		// the editor.

		// We'll just print out the model DOM here so you can see it....
		StringWriter modelWriter = new StringWriter();
		// System.out.println(modelWriter);
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
			if (ediInputStream != null) {
				ediInputStream.close();
				ediInputStream = null;
			}
			model = null;
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return tagList;
	}

	public TagList parseEDIFile(String path, InputType inputType, SmooksResourceListType smooksResourceListType)
			throws InvocationTargetException, FileNotFoundException, IOException, DocumentException {
		String filePath = SmooksUIUtils.parseFilePath(path);

		return parseEDIFile(new FileInputStream(filePath), inputType, smooksResourceListType);
	}
}
