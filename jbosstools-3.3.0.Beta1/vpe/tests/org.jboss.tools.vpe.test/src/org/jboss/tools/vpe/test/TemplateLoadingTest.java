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
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.w3c.dom.Element;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.template.VpeHtmlTemplate;
import org.jboss.tools.vpe.editor.template.VpeTemplate;
import junit.framework.TestCase;

/**
 * JUnit Test for Loading templates
 * 
 * @author Alexey Yukhovich
 */
public class TemplateLoadingTest extends TestCase {
    /** 
     * Extension point ID 
     */
    private static final String COM_REDHAT_VPE_TEMPLATES = "org.jboss.tools.vpe.templates";
    
    /**
     * Test for load all templates
     */
    public void testParseTemplates() {
        // 
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

                try {
                    IPath templateFile = getFullpathForConfigurationElement(
                            pathAttrValue, elements[j]);

                    File file = templateFile.toFile();
                    if (file.exists() && file.isFile()) {
                        Element rootElement = XMLUtilities.getElement(templateFile.toFile(), null);
                        assertNotNull("Cannot parse template " + templateFile.toFile(), rootElement);
                        VpeTemplate vpeTemplate = new VpeHtmlTemplate();
                        vpeTemplate.init(rootElement, false);
                    }
                } catch (IOException ioException) {
                    fail(ioException.getMessage());
                }
            }
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
