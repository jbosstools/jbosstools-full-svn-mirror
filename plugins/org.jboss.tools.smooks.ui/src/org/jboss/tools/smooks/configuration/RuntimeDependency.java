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

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.milyn.Smooks;
import org.milyn.SmooksException;
import org.milyn.container.ExecutionContext;
import org.milyn.delivery.sax.SAXElement;
import org.milyn.delivery.sax.SAXVisitBefore;

/**
 * Smooks runtime dependency.
 * 
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class RuntimeDependency {
	
	/**
	 * Smooks runtime versions.
	 */
	public static enum SmooksVersion {
		v1_0,
		v1_1,
		v1_2,
		v1_3,
	}

	/**
	 * The Maven Artfact ID of the Smooks dependency.
	 */
	private String artifactId;
	/**
	 * The configuration namespace URI for the config..
	 */
	private URI namespaceURI;
	/**
	 * Whether or not the configuration is supported by the Editor.  Note that this property being 'false' is not the
	 * same as saying the config is not supported by the Smooks Runtime (see the 'runtimeVersions' property).
	 */
	private boolean supportedByEditor;
	/**
	 * The list of Smooks Runtimes that can support this configuration.  Note this does not mean
	 * the editor can support the configuration (see the 'supportedByEditor' property).
	 */
	private List<SmooksVersion> runtimeVersions;
	
	private RuntimeDependency(String artifactId, URI namespaceURI, boolean supportedByEditor, List<SmooksVersion> runtimeVersions) {
		this.artifactId = artifactId;
		this.namespaceURI = namespaceURI;
		this.supportedByEditor = supportedByEditor;
	}
	
	public String getGroupId() {
		return "org.milyn";
	}
	public String getArtifactId() {
		return artifactId;
	}	
	public URI getNamespaceURI() {
		return namespaceURI;
	}
	public boolean isSupportedByEditor() {
		return supportedByEditor;
	}
	public boolean isSupportedBySmooksVersion(SmooksVersion smooksVersion) {
		return runtimeVersions.contains(smooksVersion);
	}
	public boolean isOnProjectClasspath(ClassLoader projectClassloader) {
        String resourcePath = "META-INF" + namespaceURI.getPath();
		return (projectClassloader.getResource(resourcePath) != null);
	}
	
	public static void addDependencyChecklist(Smooks metadataExtractor) {
		// Add the supported feature dependency details...
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-core",       ProcessNodeType.BASE,         "smooks-1.1.xsd",            true, SmooksVersion.v1_1, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list");
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-csv",        ProcessNodeType.INPUT_CSV,    "smooks/csv-1.2.xsd",        true, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/reader");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-edi",        ProcessNodeType.INPUT_EDI,    "smooks/edi-1.2.xsd",        true, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/reader");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-json",       ProcessNodeType.INPUT_JSON,   "smooks/json-1.2.xsd",       true, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/reader");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-templating", ProcessNodeType.TEMPLATING,   "smooks/freemarker-1.1.xsd", true, SmooksVersion.v1_1, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/freemarker");
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-javabean",   ProcessNodeType.JAVA_BINDING, "smooks/javabean-1.2.xsd",   true, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/bean");		

		// Add the unsupported feature dependency details...
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-core",         ProcessNodeType.BASE,         "smooks-1.0.xsd",              false, SmooksVersion.v1_0, SmooksVersion.v1_1, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list");
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-csv",          ProcessNodeType.INPUT_CSV,    "smooks/csv-1.1.xsd",          false, SmooksVersion.v1_1, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/reader");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-csv",          ProcessNodeType.INPUT_CSV,    "smooks/csv-1.3.xsd",          false, SmooksVersion.v1_3), "/smooks-resource-list/reader");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-edi",          ProcessNodeType.INPUT_EDI,    "smooks/edi-1.1.xsd",          false, SmooksVersion.v1_1, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/reader");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-json",         ProcessNodeType.INPUT_JSON,   "smooks/json-1.1.xsd",         false, SmooksVersion.v1_1, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/reader");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-fixed-length", ProcessNodeType.FIXED_LENGTH, "smooks/fixed-length-1.3.xsd", false, SmooksVersion.v1_3), "/smooks-resource-list/reader");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-core",         ProcessNodeType.DATASOURCE,   "smooks/datasource-1.1.xsd",   false, SmooksVersion.v1_1, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/direct");
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-core",         ProcessNodeType.DATASOURCE,   "smooks/datasource-1.1.xsd",   false, SmooksVersion.v1_1, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/JNDI");
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-core",         ProcessNodeType.DATASOURCE,   "smooks/datasource-1.3.xsd",   false, SmooksVersion.v1_3), "/smooks-resource-list/direct");
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-core",         ProcessNodeType.DATASOURCE,   "smooks/datasource-1.3.xsd",   false, SmooksVersion.v1_3), "/smooks-resource-list/JNDI");
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-core",         ProcessNodeType.CORE,         "smooks/core-1.3.xsd",         false, SmooksVersion.v1_3), "/smooks-resource-list/filterSettings");
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-core",         ProcessNodeType.CORE,         "smooks/core-1.3.xsd",         false, SmooksVersion.v1_3), "/smooks-resource-list/namespaces");
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-core",         ProcessNodeType.CORE,         "smooks/core-1.3.xsd",         false, SmooksVersion.v1_3), "/smooks-resource-list/terminate");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-templating",   ProcessNodeType.TEMPLATING,   "smooks/xsl-1.1.xsd",          false, SmooksVersion.v1_1, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/xsl");
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-javabean",     ProcessNodeType.JAVA_BINDING, "smooks/javabean-1.1.xsd",     false, SmooksVersion.v1_1, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/binding");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-javabean",     ProcessNodeType.JAVA_BINDING, "smooks/javabean-1.3.xsd",     false, SmooksVersion.v1_3), "/smooks-resource-list/bean");
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-calc",         ProcessNodeType.CALC,         "smooks/calc-1.1.xsd",         false, SmooksVersion.v1_1, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/counter");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-persistence",  ProcessNodeType.PERSISTENCE,  "smooks/persistence-1.2.xsd",  false, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/inserter");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-persistence",  ProcessNodeType.PERSISTENCE,  "smooks/persistence-1.2.xsd",  false, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/updater");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-persistence",  ProcessNodeType.PERSISTENCE,  "smooks/persistence-1.2.xsd",  false, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/deleter");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-persistence",  ProcessNodeType.PERSISTENCE,  "smooks/persistence-1.2.xsd",  false, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/deleter");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-persistence",  ProcessNodeType.PERSISTENCE,  "smooks/persistence-1.2.xsd",  false, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/flusher");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-persistence",  ProcessNodeType.PERSISTENCE,  "smooks/persistence-1.2.xsd",  false, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/locator");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-routing",      ProcessNodeType.ROUTING,      "smooks/db-routing-1.1.xsd",   false, SmooksVersion.v1_1, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/executor");
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-routing",      ProcessNodeType.ROUTING,      "smooks/db-routing-1.1.xsd",   false, SmooksVersion.v1_1, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/resultSetRowSelector");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-routing",      ProcessNodeType.ROUTING,      "smooks/file-routing-1.1.xsd", false, SmooksVersion.v1_1, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/outputStream");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-routing",      ProcessNodeType.ROUTING,      "smooks/io-routing-1.1.xsd",   false, SmooksVersion.v1_1, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/router");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-routing",      ProcessNodeType.ROUTING,      "smooks/jms-routing-1.1.xsd",  false, SmooksVersion.v1_1, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/router");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-routing",      ProcessNodeType.ROUTING,      "smooks/jms-routing-1.2.xsd",  false, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/router");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-rules",        ProcessNodeType.RULES,        "smooks/rules-1.0.xsd",        false, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/ruleBases");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-scripting",    ProcessNodeType.SCRIPTING,    "smooks/groovy-1.1.xsd",       false, SmooksVersion.v1_1, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/groovy");		
		metadataExtractor.addVisitor(new RuntimeDependencyTracker("milyn-smooks-validation",   ProcessNodeType.VALIDATION,   "smooks/validation-1.0.xsd",   false, SmooksVersion.v1_2, SmooksVersion.v1_3), "/smooks-resource-list/rule");		
	}

	private static class RuntimeDependencyTracker implements SAXVisitBefore {
		
		private String artifactId;
		private ProcessNodeType nodeType;
		private URI namespaceURI;
		/**
		 * Whether or not the configuration is supported by the Editor.  Note that this property being 'false' is not the
		 * same as saying the config is not supported by the Smooks Runtime (see the 'runtimeVersions' property).
		 */
		private boolean supportedByEditor;
		/**
		 * The list of Smooks Runtimes that can support this configuration.  Note this does not mean
		 * the editor can support the configuration (see the 'supportedByEditor' property).
		 */
		private List<SmooksVersion> runtimeVersions = new ArrayList<SmooksVersion>();
		
		public RuntimeDependencyTracker(String artifactId, ProcessNodeType nodeType, String xsd, boolean supportedByEditor, SmooksVersion... runtimeVersions) {
			this.artifactId = artifactId;
			this.nodeType = nodeType;
			this.namespaceURI = URI.create("http://www.milyn.org/xsd/" + xsd);
			this.supportedByEditor = supportedByEditor;
			if(runtimeVersions != null) {
				this.runtimeVersions.addAll(Arrays.asList(runtimeVersions));
			}
		}

		public void visitBefore(SAXElement configElement, ExecutionContext execContext) throws SmooksException, IOException {
			RuntimeMetadata metadata = (RuntimeMetadata) execContext.getAttribute(RuntimeMetadata.class);
			Set<URI> alreadyProcessed = (Set<URI>) execContext.getAttribute(RuntimeDependencyTracker.class);
			
			metadata.getNodeTypes().add(nodeType);
			
			if(alreadyProcessed == null) {
				alreadyProcessed = new HashSet<URI>();
				execContext.setAttribute(RuntimeDependencyTracker.class, alreadyProcessed);
			}
			
			// If the config namespace hasn't already been added, we need to add it...
			String configNS = configElement.getName().getNamespaceURI();
			if(configNS.equals(namespaceURI.toString()) && !alreadyProcessed.contains(namespaceURI)) {
				metadata.getDependencies().add(new RuntimeDependency(artifactId, namespaceURI, supportedByEditor, runtimeVersions));
				alreadyProcessed.add(namespaceURI);
			}
		}
	}
}
