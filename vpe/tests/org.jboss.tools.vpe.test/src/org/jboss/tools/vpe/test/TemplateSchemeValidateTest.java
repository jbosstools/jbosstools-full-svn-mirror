/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import junit.framework.TestCase;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.vpe.VpePlugin;
import org.osgi.framework.Bundle;
import org.w3c.dom.Document;

/**
 * JUnit Test for scheme validate template
 * 
 * @author Dzmitry Sakovich
 */
public class TemplateSchemeValidateTest extends TestCase {
    /**
     * Extension point ID
     */
    private static final String COM_REDHAT_VPE_TEMPLATES = "org.jboss.tools.vpe.templates";

    private static final String SCHEME_PATH = "/scheme/scheme.xsd";

    /**
     * Test for load all templates
     */
    public void testParseTemplates() {
	// 

	Bundle bundle = VpeTestPlugin.getDefault().getBundle();
	URL url = null;
	try {
	    url = bundle == null ? null : FileLocator.resolve(bundle
		    .getEntry(SCHEME_PATH));
	    File schemeFile = new File(url.getPath());
	    if (!schemeFile.exists() || !schemeFile.isFile()) {
		fail("scheme is not exist");
	    }

	    IExtensionRegistry extensionRepository = Platform
		    .getExtensionRegistry();

	    IExtensionPoint extensionPoint = extensionRepository
		    .getExtensionPoint(COM_REDHAT_VPE_TEMPLATES);
	    IExtension[] extensions = extensionPoint.getExtensions();

	    // iterate for all extensions
	    for (int i = 0; i < extensions.length; i++) {
		IExtension extension = extensions[i];
		IConfigurationElement[] elements = extension
			.getConfigurationElements();
		for (int j = 0; j < elements.length; j++) {
		    String pathAttrValue = elements[j].getAttribute("path");

		    IPath templateFile = getFullpathForConfigurationElement(
			    pathAttrValue, elements[j]);

		    File file = templateFile.toFile();
		    if (file.exists() && file.isFile()) {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
			docFactory.setNamespaceAware(true);
			DocumentBuilder parser = docFactory
				.newDocumentBuilder();

			Document document = parser.parse(file);
			SchemaFactory factory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

			Source schemaFile = new StreamSource(schemeFile);
			Schema schema = factory.newSchema(schemaFile);
			Validator validator = schema.newValidator();
			validator.validate(new DOMSource(document));
		    }

		}
	    }

	} catch (Exception exception) {
	    fail(exception.getMessage());
	}
    }

    /**
     * Get a full path for IConfigurationElement
     * 
     * @param fileName
     *            a String contain relevant fileName
     * @param confElement
     *            a IConfigurationElement
     * @return full path for IConfigurationElement
     * @throws IOException
     *             if an error occurs during the conversion
     */
    private static IPath getFullpathForConfigurationElement(String name,
	    IConfigurationElement confElement) throws IOException {
	// 1. get a shared instance
	VpePlugin plugin = VpePlugin.getDefault();

	Bundle bundle = null;

	if (confElement == null) {
	    bundle = plugin.getBundle();
	} else {
	    bundle = Platform.getBundle(confElement.getNamespaceIdentifier());
	}

	URL url = bundle.getEntry("/");

	IPath path = new Path(FileLocator.toFileURL(url).getFile());
	path = path.append(name);
	return path;
    }

}
