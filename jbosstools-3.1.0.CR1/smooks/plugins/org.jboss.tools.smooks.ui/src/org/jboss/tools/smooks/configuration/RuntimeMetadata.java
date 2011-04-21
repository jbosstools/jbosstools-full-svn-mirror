/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and others contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2005-2006, JBoss Inc.
 */
package org.jboss.tools.smooks.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.xml.transform.stream.StreamSource;

import org.eclipse.core.resources.IResource;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;
import org.milyn.Smooks;
import org.milyn.SmooksException;
import org.milyn.container.ExecutionContext;
import org.milyn.delivery.sax.SAXElement;
import org.milyn.delivery.sax.SAXVisitAfter;
import org.milyn.delivery.sax.SAXVisitBefore;

/**
 * Smooks configuration runtime metadata.
 * <p/>
 * Verifies the specified config is a Smooks configuration and extracts metadata
 * from the config e.g. input file, dependencies.
 * 
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class RuntimeMetadata {

	private Smooks metadataExtractor;
	private boolean isSmooksConfig;
	private boolean isValidSmooksConfig;
	private File configFile;
	private String inputType;
	private File inputFile;
	private Set<ProcessNodeType> processNodeTypes = new HashSet<ProcessNodeType>();
	private List<RuntimeDependency> dependencies = new ArrayList<RuntimeDependency>();

	public RuntimeMetadata() {
		metadataExtractor = new Smooks();
		metadataExtractor.addVisitor(new SmooksConfigAsserter(), "/smooks-resource-list",
				"http://www.milyn.org/xsd/smooks-1.1.xsd");
		metadataExtractor.addVisitor(new InputParamExtractor(), "/smooks-resource-list/params/param",
				"http://www.milyn.org/xsd/smooks-1.1.xsd");

		// Build dependency map...
		RuntimeDependency.addDependencyChecklist(metadataExtractor);
	}

	public boolean isSmooksConfig() {
		return isSmooksConfig;
	}

	public boolean isValidSmooksConfig() {
		return isValidSmooksConfig;
	}

	public String getErrorMessage() {
		if (isValidSmooksConfig) {
			throw new IllegalStateException(
					"Invalid call to 'getErrorMessage()'.  Smooks configuration is NOT invalid.");
		}

		if (configFile == null) {
			return "Smooks configuration file not configured, or does not exist.";
		} else if (!configFile.exists()) {
			return "Specified Smooks configuration file not found.";
		} else if (!configFile.isFile()) {
			return "Specified Smooks configuration file is not a readable file.";
		} else if (!isSmooksConfig) {
			return "Specified Smooks configuration file is not a valid Smooks Configuration.";
		} else if (inputFile == null) {
			return "Specified Smooks configuration 'Input' task is not configured with a sample input file.  Please configure the 'Input' task in the Process flow.";
		} else if (!inputFile.exists()) {
			return "Specified Smooks configuration 'Input' task is configured with a sample input file, but the file cannot be found.  Please reconfigure the 'Input' task in the Process flow.";
		} else if (!inputFile.isFile()) {
			return "Specified Smooks configuration 'Input' task is configured with a sample input file, but the file cannot be read.  Please reconfigure the 'Input' task in the Process flow.";
		}

		return "";
	}

	public File getConfigFile() {
		return configFile;
	}

	public String getInputType() {
		return inputType;
	}

	public File getInputFile() {
		return inputFile;
	}

	public List<RuntimeDependency> getDependencies() {
		return dependencies;
	}

	public Set<ProcessNodeType> getNodeTypes() {
		return processNodeTypes;
	}

	public String getNodeTypesString() {
		StringBuilder builder = new StringBuilder();
		for (ProcessNodeType nodeType : processNodeTypes) {
			if (builder.length() > 0) {
				builder.append(',');
			}
			builder.append(nodeType.toString());
		}
		return builder.toString();
	}

	public void setSmooksConfig(IResource smooksConfig) {
		reset();

		if (smooksConfig != null) {
			setSmooksConfig(new File(smooksConfig.getRawLocation().toOSString().trim()));
		}
	}

	public void setSmooksConfig(File file) {
		reset();

		if (file != null) {
			configFile = file;
			if (configFile.exists() && configFile.isFile()) {
				try {
					digestSmooksConfig(new FileInputStream(configFile));
				} catch (Exception e) {
					// Not a valid Smooks config file
				}
			}
		}
	}

	public void setSmooksConfig(File file, InputStream inputStream) {
		if (inputStream == null) {
			setSmooksConfig(file);
		} else {
			configFile = file;
			if (configFile.exists() && configFile.isFile()) {
				digestSmooksConfig(inputStream);
			}
		}
	}

	private void digestSmooksConfig(InputStream inputStream) {
		ExecutionContext execContext = metadataExtractor.createExecutionContext();
		Properties inputParams = new Properties();

		try {
			// Filter the config and capture the input params...
			execContext.setAttribute(InputParamExtractor.class, inputParams);
			execContext.setAttribute(RuntimeMetadata.class, this);

			metadataExtractor.filterSource(execContext, new StreamSource(inputStream));

			inputType = inputParams.getProperty(SmooksModelUtils.INPUT_TYPE);
			if (inputType != null) {
				String inputPath = inputParams.getProperty(inputType);
				if (inputPath != null) {
					String resolvedFilePath;
					try {
						resolvedFilePath = SmooksUIUtils.parseFilePath(inputPath.trim());
					} catch (Exception e) {
						// It's not a valid config...
						inputFile = new File(inputPath.trim());
						return;
					}

					inputFile = new File(resolvedFilePath);
					if (inputFile.exists() && inputFile.isFile()) {
						isValidSmooksConfig = true;
					}
				}
			}
		} catch (Exception e) {
			// Not a valid Smooks config file
		}
	}

	private void reset() {
		isSmooksConfig = false;
		isValidSmooksConfig = false;
		configFile = null;
		inputType = null;
		inputFile = null;
		processNodeTypes.clear();
	}

	private static class SmooksConfigAsserter implements SAXVisitBefore {
		public void visitBefore(SAXElement paramElement, ExecutionContext execContext) throws SmooksException,
				IOException {
			RuntimeMetadata metadata = (RuntimeMetadata) execContext.getAttribute(RuntimeMetadata.class);
			metadata.isSmooksConfig = true;
		}
	}

	private static class InputParamExtractor implements SAXVisitBefore, SAXVisitAfter {

		public void visitBefore(SAXElement paramElement, ExecutionContext execContext) throws SmooksException,
				IOException {
			paramElement.accumulateText();
		}

		public void visitAfter(SAXElement paramElement, ExecutionContext execContext) throws SmooksException,
				IOException {
			Properties inputParams = (Properties) execContext.getAttribute(InputParamExtractor.class);

			if (inputParams != null) {
				String paramName = paramElement.getAttribute("name");

				if (paramName != null) {
					if (paramName.equals(SmooksModelUtils.INPUT_TYPE)) {
						inputParams.setProperty(SmooksModelUtils.INPUT_TYPE, paramElement.getTextContent());
					} else {
						String paramType = paramElement.getAttribute("type");
						if (paramType != null && paramType.equals(SmooksModelUtils.INPUT_ACTIVE_TYPE)) {
							inputParams.setProperty(paramName, paramElement.getTextContent());
						}
					}
				}
			}
		}
	}
}
