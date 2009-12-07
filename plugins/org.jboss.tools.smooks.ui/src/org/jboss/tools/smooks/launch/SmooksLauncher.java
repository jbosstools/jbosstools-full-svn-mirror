/**
 * JBoss, Home of Professional Open Source
 * Copyright 2009, JBoss Inc., and others contributors as indicated
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
 * (C) 2009, JBoss Inc.
 */
package org.jboss.tools.smooks.launch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import javax.xml.transform.stream.StreamSource;

import org.jboss.tools.smooks.configuration.ProcessNodeType;
import org.jboss.tools.smooks.core.SmooksInputType;
import org.milyn.Smooks;
import org.milyn.payload.JavaResult;
import org.milyn.payload.StringResult;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;

/**
 * Smooks runtime Launcher class.
 * 
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class SmooksLauncher {

	/**
	 * Launcher Main.
	 * @param args Args.
	 */
	public static void main(String[] args) throws IOException, SAXException {
		if(args.length != 4) {
			throw new RuntimeException("Expected 4 Launch arguments: <Smooks Config> <Input Type> <Input Path> <Node Types>");
		}
		
		if(args[1].equals(SmooksInputType.INPUT_TYPE_JAVA)) {
			System.out.println("Sorry... we don't support Java Inputs yet.");
		} else {
			File smooksConfig = new File(args[0]);
			File input = new File(args[2]);
			
			assertFile(smooksConfig, "Smooks");
			assertFile(input, "Input");
			
			Smooks smooks = new Smooks(smooksConfig.getAbsolutePath());
			try {
				Set<ProcessNodeType> processNodeTypes = SmooksLauncher.fromNodeTypeString(args[3]);
				JavaResult javaResult = new JavaResult();
				boolean nothingDisplayed = true;
				
				if(processNodeTypes.contains(ProcessNodeType.TEMPLATING)) {
					StringResult stringResult = new StringResult();
					
					smooks.filterSource(new StreamSource(new FileInputStream(input)), stringResult, javaResult);
					System.out.println("[StreamResult ...]\n");
					System.out.println("    |--");
					System.out.println(indent(stringResult.toString()));
					System.out.println("    |--\n");
					nothingDisplayed = false;
				} else {
					smooks.filterSource(new StreamSource(new FileInputStream(input)), javaResult);
				}

				Set<Entry<String, Object>> bindings = javaResult.getResultMap().entrySet();
				if(!bindings.isEmpty()) {
					System.out.println("[JavaResult Objects (XML Serialized)...]");
					
					for(Entry<String, Object> binding : bindings) {
						System.out.println("\n" + binding.getKey() + ":");
						System.out.println("    |--");
						System.out.println(indent((new XStream()).toXML(binding.getValue())));
						System.out.println("    |--");
					}
					nothingDisplayed = false;
				}
				
				if(nothingDisplayed) {
					System.out.println("Nothing to Display:");
					System.out.println("    - No Java Mappings.");
					System.out.println("    - No Templates Applied.");
				}
			} finally {
				smooks.close();
			}
		}		
	}

	private static void assertFile(File file, String name) {
		if(!file.exists()) {
			throw new RuntimeException("Specified '" + name + "' File '" + file.getAbsolutePath() + "' not found.");
		}
		if(file.isDirectory()) {
			throw new RuntimeException("Specified '" + name + "' File '" + file.getAbsolutePath() + "' is a Directory.");
		}
	}

	public static Set<ProcessNodeType> fromNodeTypeString(String nodeTypeString) {
		String[] tokens = nodeTypeString.split(",");
		Set<ProcessNodeType> nodeTypes = new HashSet<ProcessNodeType>();
		
		for(String token : tokens) {
			nodeTypes.add(ProcessNodeType.valueOf(token));
		}
		
		return nodeTypes;
	}

	private static String indent(String in) throws IOException {
		BufferedReader lineReader = new BufferedReader(new StringReader(in));
		StringBuilder indentBuf = new StringBuilder();
		
		String line = lineReader.readLine();
		while(line != null) {
			indentBuf.append("    |").append(line);
			line = lineReader.readLine();
			if(line != null) {
				indentBuf.append('\n');
			}
		}
		
		return indentBuf.toString();
	}
}
