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
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaProject;
import org.jboss.tools.smooks.configuration.SmooksModelUtils;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.core.IParam;
import org.jboss.tools.smooks.templating.model.ModelBuilderException;
import org.jboss.tools.smooks.templating.model.xml.XSDModelBuilder;
import org.w3c.dom.Document;

/**
 * XML Schema Input Model Factory.
 * 
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class XSDInputModelFactory extends AbstractInputModelFactory {

	public Document getModel(SmooksModel smooksConfigModel, IJavaProject project) throws ModelBuilderException {
		IParam inputSourceParam = getInputSourceParam(SmooksModelUtils.INPUT_TYPE_XSD, smooksConfigModel);
		
		if(inputSourceParam != null) {
			String xsdInputSource = inputSourceParam.getValue();
			String[] xsdInputSourceTokens = xsdInputSource.split(";");
			
			if(xsdInputSourceTokens.length != 2) {
				throw new ModelBuilderException("Invalid XML Schema Model Input file configuration '" + xsdInputSource + "'.  Must contain 2 semi-colon separated tokens '<file-path>;<root-element-name>'."); //$NON-NLS-1$ //$NON-NLS-2$
			}
			
			String inputSourceFileName = SmooksUIUtils.parseFilePath(xsdInputSourceTokens[0]);
			File inputSourceFile = new File(inputSourceFileName);
			if(!inputSourceFile.isFile()) {
				throw new ModelBuilderException("XML Schema Model Input file '" + inputSourceFile.getAbsolutePath() + "' is not available on local file system."); //$NON-NLS-1$ //$NON-NLS-2$
			}
			
			try {
				XSDModelBuilder schemaModelBuilder = new XSDModelBuilder(URI.createFileURI(inputSourceFileName));
				schemaModelBuilder.setRootElementName(xsdInputSourceTokens[1]);
				return schemaModelBuilder.buildModel();
			} catch (IOException e) {
				throw new ModelBuilderException("Error reading XML Schema Model Input file '" + inputSourceFile.getAbsolutePath() + "'.", e); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}		
		
		return null;
	}

}
