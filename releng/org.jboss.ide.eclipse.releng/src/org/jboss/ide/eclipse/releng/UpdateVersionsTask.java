/*
 * JBoss, a Division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.releng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * @author Marshall
 * 
 * This ant task will update the versions in a set of features and/or plugins. It requires dom4j be in your ant classpath.
 * 
 * Usage:
 * <code>
 * 	&lt;updateVersions type="[plugin|feature]" version="VERSION_TO_USE" append="[true|false]"&gt;
 *		&lt;fileset ..&gt;
 *	&lt;/updateVersions&gt;
 *	</code>
 */
public class UpdateVersionsTask extends Task {

	public static final String FEATURE_TYPE = "feature";
	public static final String PLUGIN_TYPE = "plugin";
	
	private String type;
	private ArrayList filesets = new ArrayList(), reverseUpdatePlugins = new ArrayList();
	private String version;
	private String pluginRegex, recursivePluginRegex;
	private boolean append;
	private boolean failOnError;
	private boolean updatePluginRefs;
	private boolean updateAssembleScript;
	private boolean recursivePlugins;
	private boolean failOnRecursiveError;
	
	public UpdateVersionsTask ()
	{
		this.failOnError = true;
		this.append = true;
		this.updatePluginRefs = true;
		this.updateAssembleScript = false;
		this.recursivePlugins = false;
		this.failOnRecursiveError = true;
		
		this.pluginRegex = ".*";
		this.recursivePluginRegex = ".*";
	}
	
	public void addConfiguredFileSet (FileSet fileset) {
		filesets.add(fileset);
	}
	
	public void addConfiguredReverseUpdatePlugins (ReverseUpdatePlugins r)
	{
		reverseUpdatePlugins.add(r);
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getPluginRegex() {
		return pluginRegex;
	}

	public void setPluginRegex(String pluginRegex) {
		this.pluginRegex = pluginRegex;
	}

	public boolean getAppend()
	{
		return this.append;
	}
	
	public void setAppend(boolean append) {
		this.append = append;
	}
	
	public boolean getFailOnError() {
		return failOnError;
	}

	public void setFailOnError(boolean failOnError) {
		this.failOnError = failOnError;
	}
	
	public boolean getUpdatePluginRefs() {
		return updatePluginRefs;
	}

	public void setUpdatePluginRefs(boolean updatePluginRefs) {
		this.updatePluginRefs = updatePluginRefs;
	}

	public boolean getUpdateAssembleScript() {
		return updateAssembleScript;
	}

	public void setUpdateAssembleScript(boolean updateAssembleScript) {
		this.updateAssembleScript = updateAssembleScript;
	}
	
	public boolean getRecursivePlugins() {
		return recursivePlugins;
	}

	public void setRecursivePlugins(boolean recursivePlugins) {
		this.recursivePlugins = recursivePlugins;
	}
	
	public void execute() throws BuildException {
		
		for (Iterator iter = filesets.iterator(); iter.hasNext(); )
		{
			FileSet fileset = (FileSet) iter.next();
			
			DirectoryScanner scanner = fileset.getDirectoryScanner(getProject());
			String[] includedDirs = scanner.getIncludedDirectories();
			
			for (int i = 0; i < includedDirs.length; i++)
			{				
				File base = scanner.getBasedir();
				File dir = new File(base, includedDirs[i]);
			
				if (FEATURE_TYPE.equals(type))
				{
					changeFeatureVersion(dir);
				}
				
				else if (PLUGIN_TYPE.equals(type))
				{
					changePluginVersion(dir);
				}
			}
		}
	}
	
	private boolean canAppend (String version)
	{
		String[] versions = version.split("\\.");
		return versions.length < 4;
	}
	
	private String appendVersions (String version, String subVersion)
		throws BuildException
	{
		if (canAppend(version))
		{
			return version + "." + subVersion;
		}
		else
		{
			if (failOnError)
			{
				throw new BuildException("Version \"" + version + "\" cannot be appended to.");
			}
			else
			{
				System.out.println("Version \"" + version + "\" cannot be appended to.");
				return version;
			}
		}
	}
	
	private void validateVersion (String version) throws BuildException
	{
		String sections[] = version.split(".");
		
		try {
			if (sections.length > 4)
				throw new BuildException("Invalid version format (should be major.minor.service.qualifier): " + version);
			
			for (int i = 0; i < sections.length; i++)
			{
				if (i != 3)
				{
					if (sections[i].matches("[A-Za-z ]"))
						throw new BuildException("Invalid version format (alpha/space not allowed in major.minor.service): " + sections[i]);
				}
				else
					if (sections[i].matches("[ ]"))
						throw new BuildException("Invalid version format (space not allowed in qualifier): " + sections[i]);
			}
		} catch (BuildException ex) {
			if (failOnError) throw ex;
			else {
				System.err.println("WARNING: " + ex.getMessage());
			}
		}
	}
	
	private boolean isReverseUpdatePlugin (String pluginId)
	{
		for (Iterator iter = reverseUpdatePlugins.iterator(); iter.hasNext(); )
		{
			ReverseUpdatePlugins r = (ReverseUpdatePlugins) iter.next();
			for (Iterator iter2 = r.getPlugins().iterator(); iter2.hasNext(); )
			{
				ReverseUpdatePlugins.Plugin plugin = (ReverseUpdatePlugins.Plugin) iter2.next();
				if (plugin.getName().equals(pluginId))
					return true;
			}
		}
		
		return false;
	}
	

	
	private void changeFeatureVersion(File featureBase)
		throws BuildException
	{
		String featureName = featureBase.getAbsolutePath();
		featureName = featureName.substring(featureName.lastIndexOf(File.separator));
		featureName = featureName.substring(1);
		
		File featureFile = new File(featureBase, "feature.xml");
		Document doc = null;
		
		try {
			doc = parse(featureFile);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
		
		Attribute featureVersionAttr = (Attribute) doc.selectSingleNode( "//feature/@version" );
		System.out.println("updating version of feature " + featureName + " to " + (append ? appendVersions(featureVersionAttr.getText(), version) : version) + "...");
		
		String newVersion = append ? appendVersions(featureVersionAttr.getText(), version) : version;
		validateVersion(newVersion);
		
		featureVersionAttr.setText(newVersion);
		
		if (updatePluginRefs)
		{
			List pluginVersions = doc.selectNodes( "//feature/plugin/@version");
			for (Iterator iter = pluginVersions.iterator(); iter.hasNext(); )
			{
				Attribute pluginVersionAttr = (Attribute) iter.next();
				String pluginId = pluginVersionAttr.getParent().attributeValue("id");
				File pluginBase = new File(featureBase, "../../plugins/" + pluginId);
				
				if (pluginId.matches(pluginRegex))
				{
					if (isReverseUpdatePlugin(pluginId))
					{
						String pluginVersion = getPluginVersion(pluginBase);
						pluginVersionAttr.setText(pluginVersion);
					}
					else {
						String newPluginVersion = append ? appendVersions(pluginVersionAttr.getText() , version) : version;
						validateVersion(newPluginVersion);
						
						pluginVersionAttr.setText(newPluginVersion);	
					
						if (recursivePlugins && pluginId.matches(recursivePluginRegex))
						{
							System.out.println("recursively updating plugin " + pluginId + "...");
							
							
							try {
								changePluginVersion(pluginBase);
							} catch (BuildException e) {
								if (failOnRecursiveError)
									throw e;
								else e.printStackTrace();
							}
						}
					}
				}
			}
		}
		
		try {
			write(doc, featureFile);
		} catch (IOException e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
		
		if (updateAssembleScript)
		{
			try {
				updateAssembleScript(featureBase, featureName);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BuildException(e);
			}
		}
	}
	
	private void updateAssembleScript (File featureBase, String featureName)
		throws BuildException
	{
		File assembleFile = new File(featureBase, "../../assemble." + featureName + ".xml");
		if (assembleFile.exists())
		{
			System.out.println("updating assemble script @ " + assembleFile.getAbsolutePath() + "...");
			
			Document assembleDoc = null;
			
			try {
				assembleDoc = parse(assembleFile);
			} catch (DocumentException e) {
				e.printStackTrace();
				throw new BuildException(e);
			}
			
			List argNodes = assembleDoc.selectNodes("//arg[contains(@line,'${pluginArchivePrefix}') or contains(@line, '${featureArchivePrefix}')]");
			for (Iterator iter = argNodes.iterator(); iter.hasNext(); )
			{
				Element argElement = (Element) iter.next();
				String line = argElement.attributeValue("line");
				
				String tokens[] = line.split("_");
				String versionSuffix = tokens[tokens.length - 1];
				
				versionSuffix = append ? appendVersions(versionSuffix, version) : version;
				line = "";
				for (int i = 0; i < tokens.length - 1; i++)
					line += tokens[i] + "_";
				
				line += versionSuffix;
				
				argElement.attribute("line").setValue(line);
			}
		
			try {
				write(assembleDoc, assembleFile);
			} catch (IOException e) {
				e.printStackTrace();
				throw new BuildException(e);
			}
		}
		else throw new BuildException("Assemble script: " + assembleFile.getAbsolutePath() + " does not exist.");
	}
	
	public static final Attributes.Name BUNDLE_VERSION = new Attributes.Name("Bundle-Version");
	
	private String getPluginVersion (File pluginBase) throws BuildException
	{
		try {
			String pluginName = pluginBase.getAbsolutePath();
			pluginName = pluginName.substring(pluginName.lastIndexOf(File.separator));
			
			File pluginFile = new File(pluginBase, "plugin.xml");
			File manifestFile = new File(pluginBase, "META-INF/MANIFEST.MF");
			
			if (pluginFile.exists())
			{
				Document doc = parse(pluginFile);
		        Attribute attr = (Attribute) doc.selectSingleNode( "//plugin/@version" );
	
				if (attr != null)
				{
					return attr.getText();
				}
			}
			
			// Handle the new 3.1-style plugin metadata
			if (manifestFile.exists())
			{
				FileInputStream manifestStream = new FileInputStream(manifestFile);
				Manifest manifest = new Manifest(manifestStream);
				
				String bundleVersion = (String) manifest.getMainAttributes().get(BUNDLE_VERSION);
				manifestStream.close();
				
				return bundleVersion;
			}
			
			return null;
		}
		catch (Exception e)
		{
			throw new BuildException("Exception occured when trying to retrieve version in plugin: " + pluginBase.getAbsolutePath(), e);
		}
	}
	
	private void changePluginVersion(File pluginBase) throws BuildException
	{
		try {
			String pluginName = pluginBase.getAbsolutePath();
			pluginName = pluginName.substring(pluginName.lastIndexOf(File.separator));
			
			
			File pluginFile = new File(pluginBase, "plugin.xml");
			if (pluginFile.exists())
			{
				Document doc = parse(pluginFile);
		        Attribute attr = (Attribute) doc.selectSingleNode( "//plugin/@version" );
	
				if (attr != null)
				{
					System.out.println("updating version of plugin " + pluginName  + " to " + (append ? appendVersions(attr.getText(), version) : version) + "...");
					String newVersion = append ? appendVersions(attr.getText(), version) : version;
					
					validateVersion(newVersion);
					attr.setText(newVersion);
					
					write(doc, pluginFile);
				}
			}
			
			// Some plugins have the version set in the MANIFEST.MF as well as the plugin.xml
			File manifestFile = new File(pluginBase, "META-INF/MANIFEST.MF");
			if (manifestFile.exists())
			{
				StringBuffer newManifest = new StringBuffer();
				BufferedReader reader = new BufferedReader(new FileReader(manifestFile));
				for (String line = reader.readLine(); line != null; line = reader.readLine())
				{
					if (line.indexOf("Bundle-Version:") != -1)
					{
						String tokens[] = line.split(": *");
						String bundleVersion = "";
						
						if (tokens.length == 2)
						{
							bundleVersion = tokens[1];
						}
						
						System.out.println("updating version of plugin " + pluginName  + " to " + (append ? appendVersions(bundleVersion, version) : version) + "...");
						
						String newBundleVersion = append ? appendVersions(bundleVersion, version) : version;
						validateVersion(newBundleVersion);
						
						line = "Bundle-Version: " + newBundleVersion;
					}
					
					newManifest.append(line + "\n");
				}
				
				reader.close();
				
				FileWriter writer = new FileWriter(manifestFile);
				
				writer.write(newManifest.toString());
				writer.close();
			}
		}
		catch (Exception e)
		{
			throw new BuildException("Exception occured when trying to replace version in plugin: " + pluginBase.getAbsolutePath(), e);
		}
	}
	
	private Document parse(File file) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        return document;
    }
	
	private void write (Document doc, File toWriteTo) throws IOException
	{
		OutputFormat format = new OutputFormat();
		format.setLineSeparator("\n");
		format.setIndent(true);
		
		XMLWriter writer = new XMLWriter(new FileWriter(toWriteTo), format);
		
		writer.write(doc);
		writer.close();
		
	}

	public boolean isFailOnRecursiveError() {
		return failOnRecursiveError;
	}

	public void setFailOnRecursiveError(boolean failOnRecursiveError) {
		this.failOnRecursiveError = failOnRecursiveError;
	}

	public String getRecursivePluginRegex() {
		return recursivePluginRegex;
	}

	public void setRecursivePluginRegex(String recursivePluginRegex) {
		this.recursivePluginRegex = recursivePluginRegex;
	}

}
