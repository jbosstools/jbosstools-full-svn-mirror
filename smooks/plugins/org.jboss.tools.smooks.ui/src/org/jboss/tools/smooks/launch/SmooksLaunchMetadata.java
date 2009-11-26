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
package org.jboss.tools.smooks.launch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
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
 * Smooks configuration launch metadata.
 * <p/>
 * Verifies the specified config is a Smooks configuration and extracts launch
 * metadata from the config (input file etc).
 * 
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class SmooksLaunchMetadata {
	
	public static enum ProcessNodeType {
		TEMPLATING,
		JAVA_BINDING
	}

	private Smooks inputParamExtractor;
	private boolean isValidSmooksConfig;
	private File configFile;
	private String inputType;
	private File inputFile;
	private Set<ProcessNodeType> processNodeTypes = new HashSet<ProcessNodeType>();

	public SmooksLaunchMetadata() {
		inputParamExtractor = new Smooks();
		inputParamExtractor.addVisitor(new InputParamExtractor(), "/smooks-resource-list/params/param", "http://www.milyn.org/xsd/smooks-1.1.xsd");
		inputParamExtractor.addVisitor(new ConfigTypeTracker().setNodeType(ProcessNodeType.TEMPLATING), "/smooks-resource-list/freemarker");
		inputParamExtractor.addVisitor(new ConfigTypeTracker().setNodeType(ProcessNodeType.JAVA_BINDING), "/smooks-resource-list/bean");
	}
	
	public boolean isValidSmooksConfig() {
		return isValidSmooksConfig;
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

	public Set<ProcessNodeType> getNodeTypes() {
		return processNodeTypes;
	}
	
	public String getNodeTypesString() {
		StringBuilder builder = new StringBuilder();
		for(ProcessNodeType nodeType : processNodeTypes) {
			if(builder.length() > 0) {
				builder.append(',');
			}
			builder.append(nodeType.toString());
		}
		return builder.toString();
	}
	
	public static Set<ProcessNodeType> fromNodeTypeString(String nodeTypeString) {
		String[] tokens = nodeTypeString.split(",");
		Set<ProcessNodeType> nodeTypes = new HashSet<ProcessNodeType>();
		
		for(String token : tokens) {
			nodeTypes.add(ProcessNodeType.valueOf(token));
		}
		
		return nodeTypes;
	}

	public void setSmooksConfig(IResource smooksConfig) {
		reset();
		
		if(smooksConfig != null) {
			configFile = new File(smooksConfig.getRawLocation().toOSString());
			if(configFile.exists() && configFile.isFile()) {
				ExecutionContext execContext = inputParamExtractor.createExecutionContext();
				Properties inputParams = new Properties();
				
				try {
					// Filter the config and capture the input params...
					execContext.setAttribute(InputParamExtractor.class, inputParams);
					execContext.setAttribute(SmooksLaunchMetadata.class, this);
					
					inputParamExtractor.filterSource(execContext, new StreamSource(new FileInputStream(configFile)));
					
					String inputType = inputParams.getProperty(SmooksModelUtils.INPUT_TYPE);
					if(inputType != null) {
						String inputPath = inputParams.getProperty(inputType);
						if(inputPath != null) {
							File inputFile = new File(SmooksUIUtils.parseFilePath(inputPath));
							
							if(inputFile.exists() && inputFile.isFile()) {
								this.inputType = inputType;
								this.inputFile = inputFile;
								isValidSmooksConfig = true;
							}
						}
					}
				} catch (Exception e) {
					// Not a valid Smooks config file
				}
			}
		}
	}
			
	private void reset() {
		isValidSmooksConfig = false;
		configFile = null;
		inputType = null;
		inputFile = null;
		processNodeTypes.clear();
	}

	private static class InputParamExtractor implements SAXVisitBefore, SAXVisitAfter {

		public void visitBefore(SAXElement paramElement, ExecutionContext execContext) throws SmooksException, IOException {
			paramElement.accumulateText();
		}

		public void visitAfter(SAXElement paramElement, ExecutionContext execContext) throws SmooksException, IOException {
			Properties inputParams = (Properties) execContext.getAttribute(InputParamExtractor.class);
			
			if(inputParams != null) {
				String paramName = paramElement.getAttribute("name");
				
				if(paramName != null) {
					if(paramName.equals(SmooksModelUtils.INPUT_TYPE)) {
						inputParams.setProperty(SmooksModelUtils.INPUT_TYPE, paramElement.getTextContent());
					} else {
						String paramType = paramElement.getAttribute("type");
						if(paramType != null && paramType.equals(SmooksModelUtils.INPUT_ACTIVE_TYPE)) {
							inputParams.setProperty(paramName, paramElement.getTextContent());
						}
					}
				}
			}
		}
	}

	private static class ConfigTypeTracker implements SAXVisitBefore {
		
		private ProcessNodeType nodeType;
		
		public ConfigTypeTracker setNodeType(ProcessNodeType nodeType) {
			this.nodeType = nodeType;
			return this;
		}

		public void visitBefore(SAXElement paramElement, ExecutionContext execContext) throws SmooksException, IOException {
			SmooksLaunchMetadata metadata = (SmooksLaunchMetadata) execContext.getAttribute(SmooksLaunchMetadata.class);
			metadata.getNodeTypes().add(nodeType);
		}
	}
}
