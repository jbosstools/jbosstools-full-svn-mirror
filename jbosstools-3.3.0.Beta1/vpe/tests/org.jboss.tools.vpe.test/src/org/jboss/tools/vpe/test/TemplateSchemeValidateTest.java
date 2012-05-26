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
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import junit.framework.TestCase;

import org.eclipse.core.internal.registry.ExtensionRegistry;
import org.eclipse.core.runtime.ContributorFactoryOSGi;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.RegistryFactory;
import org.osgi.framework.Bundle;
import org.xml.sax.SAXException;

/**
 * JUnit Test for scheme validate template
 * 
 * @author Dzmitry Sakovich
 */
public class TemplateSchemeValidateTest extends TestCase {
	/**
	 * Extension point ID
	 */
	private static final String COM_REDHAT_VPE_TEMPLATES = "org.jboss.tools.vpe.templates"; //$NON-NLS-1$

	private static final String VPE_TEST_EXTENSION = "org.jboss.tools.vpe.test"; //$NON-NLS-1$

	private static final String SCHEME_PATH = "/scheme/scheme.xsd"; //$NON-NLS-1$

	private static final String PLUGIN_FAILURE_NAME = "testFailure-plugin.xml"; //$NON-NLS-1$

	private static Schema schema = null;

	private static final String PLUGIN_OK_NAME = "testOk-plugin.xml"; //$NON-NLS-1$

	private static final String SCHEME_LANGUAGE = "http://www.w3.org/2001/XMLSchema"; //$NON-NLS-1$

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Bundle bundle = VpeTestPlugin.getDefault().getBundle();
		URL url = null;
		url = bundle == null ? null : FileLocator.resolve(bundle
				.getEntry(SCHEME_PATH));
		File schemeFile = new File(url.getPath());
		if (!schemeFile.exists() || !schemeFile.isFile()) {
			fail("scheme is not exist"); //$NON-NLS-1$
		}
		SchemaFactory factory = SchemaFactory.newInstance(SCHEME_LANGUAGE);
		schema = factory.newSchema(schemeFile);
	}

	/**
	 * Test for load all templates
	 */
	public void testValidationGlobalTemplates() {
		//
		assertNotNull("Schema is not exist", schema); //$NON-NLS-1$
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
				String pathAttrValue = elements[j].getAttribute("path"); //$NON-NLS-1$
				try {
					IPath templateFile = getFullpathForConfigurationElement(
							pathAttrValue, elements[j]);

					try {
						Validator validator = schema.newValidator();
						Source source = new StreamSource(
								templateFile.toOSString());
						validator.validate(source);
					} catch (SAXException ex) {
						fail("Sheme " + templateFile.toFile().getName() //$NON-NLS-1$
								+ " is not valid : " + ex.getMessage()); //$NON-NLS-1$
					}
				} catch (IOException e) {
					fail(e.getMessage());
				}
			}
		}

	}

	public void testValidationIncorrectTemplplates() throws Exception {

		assertNotNull("Schema is not exist", schema); //$NON-NLS-1$
		createTemplatesForTesting(PLUGIN_FAILURE_NAME);

		IExtensionRegistry extensionRepository = Platform
				.getExtensionRegistry();

		IExtensionPoint extensionPoint = extensionRepository
				.getExtensionPoint(COM_REDHAT_VPE_TEMPLATES);
		IExtension[] extensions = extensionPoint.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			if (extensions[i].getNamespaceIdentifier().equals(
					VPE_TEST_EXTENSION)) {
				IConfigurationElement[] elements = extensions[i]
						.getConfigurationElements();
				for (int j = 0; j < elements.length; j++) {
					String pathAttrValue = elements[j].getAttribute("path"); //$NON-NLS-1$
					try {
						IPath templateFile = getFullpathForConfigurationElement(
								pathAttrValue, elements[j]);

						try {
							Validator validator = schema.newValidator();
							Source source = new StreamSource(
									templateFile.toOSString());
							validator.validate(source);
						} catch (SAXException ex) {
							assertTrue("Scheme " //$NON-NLS-1$
									+ templateFile.toFile().getName()
									+ " is not valid", true); //$NON-NLS-1$
						}
					} catch (IOException e) {
						fail(e.getMessage());
					}
				}
			}
		}
		return;
	}

	public void testValidationCorrectTemplplates() throws Exception {

		assertNotNull("Schema is not exist", schema); //$NON-NLS-1$
		createTemplatesForTesting(PLUGIN_OK_NAME);

		IExtensionRegistry extensionRepository = Platform
				.getExtensionRegistry();

		IExtensionPoint extensionPoint = extensionRepository
				.getExtensionPoint(COM_REDHAT_VPE_TEMPLATES);
		IExtension[] extensions = extensionPoint.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			if (extensions[i].getNamespaceIdentifier().equals(
					VPE_TEST_EXTENSION)) {
				IConfigurationElement[] elements = extensions[i]
						.getConfigurationElements();
				for (int j = 0; j < elements.length; j++) {
					String pathAttrValue = elements[j].getAttribute("path"); //$NON-NLS-1$
					try {
						IPath templateFile = getFullpathForConfigurationElement(
								pathAttrValue, elements[j]);

						try {
							Validator validator = schema.newValidator();
							Source source = new StreamSource(
									templateFile.toOSString());
							validator.validate(source);
						} catch (SAXException ex) {
							fail("Sheme " + templateFile.toFile().getName() //$NON-NLS-1$
									+ " is not valid : " + ex.getMessage()); //$NON-NLS-1$
						}
					} catch (IOException e) {
						fail(e.getMessage());
					}
				}
			}
		}

		return;
	}

	/**
	 * Tests possible template
	 * 
	 * @throws Exception
	 */
	private void createTemplatesForTesting(String pluginXmlFileName)
			throws Exception {
		IPath iPath = getFullpathForConfigurationElement(pluginXmlFileName,
				null);
		File file = iPath.toFile();
		FileInputStream is = new FileInputStream(file);
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		Object key = ((ExtensionRegistry) registry).getTemporaryUserToken();
		Bundle bundle = VpeTestPlugin.getDefault().getBundle();
		IContributor contributor = ContributorFactoryOSGi
				.createContributor(bundle);
		registry.addContribution(is, contributor, false, null, null, key);
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
		VpeTestPlugin plugin = VpeTestPlugin.getDefault();

		Bundle bundle = null;

		if (confElement == null) {
			bundle = plugin.getBundle();
		} else {
			bundle = Platform.getBundle(confElement.getNamespaceIdentifier());
		}

		URL url = bundle.getEntry("/"); //$NON-NLS-1$

		IPath path = new Path(FileLocator.toFileURL(url).getFile());
		path = path.append(name);
		return path;
	}

	/**
	 * Removing extensions from eclipse
	 * 
	 * @param extensionPointId
	 * @param extensionId
	 */
	private void removeExtension(String extensionPointId, String extensionId) {
		IExtensionRegistry extensionRepository = Platform
				.getExtensionRegistry();

		Object token = ((ExtensionRegistry) extensionRepository)
				.getTemporaryUserToken();
		IExtensionPoint extensionPoint = extensionRepository
				.getExtensionPoint(extensionPointId);
		IExtension[] extensions = extensionPoint.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			if (extensions[i].getNamespaceIdentifier().equals(extensionId))
				extensionRepository.removeExtension(extensions[i], token);
		}
	}

	@Override
	protected void tearDown() throws Exception {
		removeExtension(COM_REDHAT_VPE_TEMPLATES, VPE_TEST_EXTENSION);
		super.tearDown();
	}
}
