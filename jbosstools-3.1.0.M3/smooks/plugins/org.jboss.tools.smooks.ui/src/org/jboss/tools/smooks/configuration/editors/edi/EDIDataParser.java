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
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

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
import org.jboss.tools.smooks.model.edi12.EDI12Reader;
import org.jboss.tools.smooks.model.graphics.ext.InputType;
import org.jboss.tools.smooks.model.graphics.ext.ParamType;
import org.jboss.tools.smooks.model.smooks.AbstractReader;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;
import org.milyn.Smooks;
import org.milyn.payload.StringResult;
import org.milyn.smooks.edi.EDIReaderConfigurator;

/**
 * @author Dart
 * 
 */
public class EDIDataParser {
	public static final String USE_AVAILABEL_READER = "use_availableReader";

	public static final String ENCODING = "encoding";

	public static final Object MAPPING_MODEL = "mappingModelFile";

	public static final Object VALIDATE = "validate";

	public TagList parseEDIFile(InputStream stream, InputType inputType, SmooksResourceListType resourceList,
			IProject project) throws IOException, DocumentException {
		List<ParamType> paramList = inputType.getParam();
		String encoding = null;
		String mappingModel = null;
		String validate = null;
		String type = inputType.getType();

		for (Iterator<?> iterator = paramList.iterator(); iterator.hasNext();) {
			ParamType paramType = (ParamType) iterator.next();
			if (paramType.getName().equals(USE_AVAILABEL_READER)) {
				if (paramType.getValue().equalsIgnoreCase("true") && resourceList != null) {
					List<AbstractReader> readers = resourceList.getAbstractReader();
					int count = 0;
					int index = -1;
					for (Iterator<?> iterator2 = readers.iterator(); iterator2.hasNext();) {
						AbstractReader abstractReader = (AbstractReader) iterator2.next();
						if (abstractReader instanceof EDIReader && SmooksModelUtils.INPUT_TYPE_EDI_1_1.equals(type)) {
							count++;
							if (index == -1) {
								index = readers.indexOf(abstractReader);
							}
						}
						if (abstractReader instanceof EDI12Reader && SmooksModelUtils.INPUT_TYPE_EDI_1_2.equals(type)) {
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
						return parseEDIFile(stream, (EObject) readers.get(index), project);
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

		return parseEDIFile(stream, mappingModel, encoding,validate, project);
	}

	public TagList parseEDIFile(InputStream stream, InputType inputType, SmooksResourceListType resourceList)
			throws IOException, DocumentException {

		List<ParamType> paramList = inputType.getParam();
		String encoding = null;
		String mappingModel = null;
		String validate = null;
		String type = inputType.getType();

		for (Iterator<?> iterator = paramList.iterator(); iterator.hasNext();) {
			ParamType paramType = (ParamType) iterator.next();
			if (paramType.getName().equals(USE_AVAILABEL_READER)) {
				if (paramType.getValue().equalsIgnoreCase("true") && resourceList != null) {
					List<AbstractReader> readers = resourceList.getAbstractReader();
					int count = 0;
					int index = -1;
					for (Iterator<?> iterator2 = readers.iterator(); iterator2.hasNext();) {
						AbstractReader abstractReader = (AbstractReader) iterator2.next();
						if (abstractReader instanceof EDIReader && SmooksModelUtils.INPUT_TYPE_EDI_1_1.equals(type)) {
							count++;
							if (index == -1) {
								index = readers.indexOf(abstractReader);
							}
						}
						if (abstractReader instanceof EDI12Reader && SmooksModelUtils.INPUT_TYPE_EDI_1_2.equals(type)) {
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
						return parseEDIFile(stream, (EObject) readers.get(index));
					}

				}
			}
			if (paramType.getName().equals(ENCODING)) {
				encoding = paramType.getValue();
			}
			if (paramType.getName().equals(MAPPING_MODEL)) {
				mappingModel = paramType.getValue();
			}
			if (paramType.getName().equals(VALIDATE)) {
				validate = paramType.getValue();
			}
		}

		return parseEDIFile(stream, mappingModel, encoding,validate, resourceList);
	}

	public TagList parseEDIFile(InputStream ediInputStream, EObject readerObj, IProject project) throws IOException,
			DocumentException {
		String encoding = null;
		String mappingModel = null;
		String validate = null;
		if(readerObj instanceof EDIReader){
			EDIReader reader = (EDIReader)readerObj;
			encoding = reader.getEncoding();
			mappingModel = reader.getMappingModel();
		}
		if(readerObj instanceof EDI12Reader){
			EDI12Reader reader = (EDI12Reader)readerObj;
			encoding = reader.getEncoding();
			mappingModel = reader.getMappingModel();
		}
		return parseEDIFile(ediInputStream, mappingModel, encoding,validate, project);
	}

	public TagList parseEDIFile(InputStream ediInputStream, EObject readerObj) throws IOException, DocumentException {
		
		String encoding = null;
		String mappingModel = null;
		if(readerObj instanceof EDIReader){
			EDIReader reader = (EDIReader)readerObj;
			encoding = reader.getEncoding();
			mappingModel = reader.getMappingModel();
		}
		if(readerObj instanceof EDI12Reader){
			EDI12Reader reader = (EDI12Reader)readerObj;
			encoding = reader.getEncoding();
			mappingModel = reader.getMappingModel();
		}
		return parseEDIFile(ediInputStream, mappingModel, encoding,null,(EObject) readerObj);
	}

	public TagList parseEDIFile(InputStream ediInputStream, String mappingModel, String ediFileEncoding, String validate , EObject emodel)
			throws IOException, DocumentException {
		IResource resource = SmooksUIUtils.getResource(emodel);
		IProject project = null;
		if (resource != null) {
			project = resource.getProject();
		}
		return parseEDIFile(ediInputStream, mappingModel, ediFileEncoding,validate, project);
	}

	public TagList parseEDIFile(InputStream ediInputStream, String mappingModel, String ediFileEncoding,
			String validate, IProject project) throws IOException, DocumentException {
		Smooks smooks = new Smooks();

//		SmooksResourceConfiguration readerConfig = new SmooksResourceConfiguration("org.xml.sax.driver",
//				SmooksEDIReader.class.getName());
		
		if (mappingModel == null)
			return null;
		
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
		EDIReaderConfigurator readerConfig = new EDIReaderConfigurator(modelPath);
		if (ediFileEncoding == null || ediFileEncoding.trim().length() == 0) {
			ediFileEncoding = "UTF-8";
		}
//		readerConfig.
//		readerConfig.setParameter("encoding", ediFileEncoding);

		smooks.setReaderConfig(readerConfig);
//		SmooksUtil.registerResource(readerConfig, smooks);

		// Use a DOM result to capture the message model for the supplied
		// CSV
		// message...

		// Filter the message through Smooks and capture the result as a DOM
		// in
		// the domResult instance...
		// FileInputStream stream
		StringResult result = new StringResult();
		smooks.filterSource(new StreamSource(ediInputStream), result);


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
			if (ediInputStream != null) {
				ediInputStream.close();
				ediInputStream = null;
			}
			result = null;
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
