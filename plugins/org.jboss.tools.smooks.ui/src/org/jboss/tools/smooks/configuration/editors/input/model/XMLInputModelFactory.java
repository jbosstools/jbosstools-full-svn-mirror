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
package org.jboss.tools.smooks.configuration.editors.input.model;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.jdt.core.IJavaProject;
import org.jboss.tools.smooks.configuration.SmooksModelUtils;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.core.IParam;
import org.jboss.tools.smooks.templating.model.ModelBuilderException;
import org.w3c.dom.Document;

/**
 * XML (Sample) Input Model Factory.
 * 
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class XMLInputModelFactory extends AbstractInputModelFactory {

	private static final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	
	static {
		docBuilderFactory.setNamespaceAware(true);
	}
	
	public Document getModel(SmooksModel smooksConfigModel, IJavaProject project) throws ModelBuilderException {
		IParam inputSourceParam = getInputSourceParam(SmooksModelUtils.INPUT_TYPE_XML, smooksConfigModel);
		
		if(inputSourceParam != null) {
			String inputSourceFileName = SmooksUIUtils.parseFilePath(inputSourceParam.getValue());
			File inputSourceFile = new File(inputSourceFileName);
			
			if(!inputSourceFile.isFile()) {
				throw new ModelBuilderException("XML Model Input file '" + inputSourceFile.getAbsolutePath() + "' is not available on local file system."); //$NON-NLS-1$ //$NON-NLS-2$
			}
			
			try {
				DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();
				return documentBuilder.parse(inputSourceFile);
			} catch (Exception e) {
				throw new ModelBuilderException("Error building XML Model for Input '" + inputSourceFileName + "'.", e); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		
		return null;
	}
}
