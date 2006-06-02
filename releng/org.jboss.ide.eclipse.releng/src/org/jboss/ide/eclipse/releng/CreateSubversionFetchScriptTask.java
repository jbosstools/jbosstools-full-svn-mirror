/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNRevision;
import org.tigris.subversion.svnclientadapter.SVNUrl;
import org.tigris.subversion.svnclientadapter.commandline.CmdLineClientAdapterFactory;
/**
 * @author Marshall
 * 
 * This ant task aims to be a work-a-like to the "eclipse.fetch" ant task, but for subversion repositories.
 * Basic usage is as follows:
 * <br>
 * <code>
 * 	&lt;createSubversionFetchScript<br>
 * 		type="feature" id="org.jboss.ide.eclipse.feature"<br>
 * 		buildDirectory="${buildDirectory}"<br>
 * 		mapFile="${buildDirectory}/directory.txt"<br>
 * 		baseLocation="${baseLocation}"<br>
 * 	/&gt;<br>
 * 	<br>
 * 	&lt;ant antfile="fetch_org.jboss.ide.eclipse.feature.xml" target="fetch" inheritall="true"/&gt;<br>
 * </code>
 * <br>
 * The subversion map file is also very similar to the standard map file with just a few changes. The format looks like:<br>
 * <code>
 * 	(feature|plugin)@&lt;feature/plugin id&gt;=&lt;revision&gt;,&lt;username&gt;,&lt;password&gt;,&lt;svnUrl&gt;<br>
 * </code>
 * <br>
 * <b>Example:</b><br>
 * <code>
 * 	feature@org.jboss.ide.eclipse.feature=HEAD,anonymous,,https://anonsvn.forge.jboss.com/svnroot/jboss/jbosside/trunk/core/features/org.jboss.ide.eclipse.feature
 * </code>
 */
public class CreateSubversionFetchScriptTask extends Task {

	private String type, id, buildDirectory, mapFile, baseLocation;
	private String filename;
	private HashMap repositoryInfo;
	
	private static class RepositoryInfo {
		public String id, type, revision, username, password, svnUrl;
	}
	
	public void execute() throws BuildException {
		try {
			filename = "fetch_" + id + ".xml";
			
			parseMapFile();
			
			FileWriter writer = new FileWriter(buildDirectory + File.separator + filename);
			createFetchScript(writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
	}
	
	private void parseMapFile () throws BuildException
	{
		repositoryInfo = new HashMap();
		
		Properties mapProperties = new Properties();
		
		try {
			mapProperties.load(new FileInputStream(mapFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new BuildException(e);
		} catch (IOException e) {
			throw new BuildException(e);
		}
		
		for (Iterator iter = mapProperties.keySet().iterator(); iter.hasNext(); )
		{
			RepositoryInfo info = new RepositoryInfo();
			
			String key = (String) iter.next();
			String typeAndId[] = key.split("\\@");
			info.type = typeAndId[0];
			info.id = typeAndId[1];
			
			String args[] = mapProperties.getProperty(key).split(",");
			info.revision = args[0];
			info.username = args[1];
			info.password = args[2];
			info.svnUrl = args[3];
			
			repositoryInfo.put(info.id, info);
		}
		
		if (getRepositoryInfo(id) == null)
		{
			throw new BuildException("Error: element " + id + " not found in map file: " + mapFile);
		}
	}
	
	private void createFetchScript (Writer writer) throws BuildException
	{
		Document doc = DocumentHelper.createDocument();
		doc.addComment("Subversion fetch script for " + type + "@" + id);
		
		Element project = doc.addElement("project");
		project.addAttribute("name", "FetchScript");
		project.addAttribute("default", "fetch");
		
		addPropertyElement(project, "quiet", "true");
		
		createFetchTarget (project);
		createFetchElementTarget(project);
		
		if ("feature".equals(type))
			createFetchPluginsTarget(project);
		
		createGetFromSubversionTarget(project);
		
		try {
	        OutputFormat format = OutputFormat.createPrettyPrint();
	        XMLWriter xmlWriter = new XMLWriter( writer, format );
	        xmlWriter.write( doc );
	        xmlWriter.close();
	        
		} catch (IOException e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
	}
	
	private void createFetchTarget (Element project) throws BuildException
	{
		Element fetchTarget = addTargetElement(project, "fetch");
		fetchTarget.addElement("antcall").addAttribute("target", "fetch.element");
		
		if ("feature".equals(type))
			fetchTarget.addElement("antcall").addAttribute("target", "fetch.plugins");
	}
	
	private void createFetchElementTarget (Element project) throws BuildException
	{
		if ("feature".equals(type))
		{
			String featureXml = "${buildDirectory}/features/" + id + "/feature.xml";
			
			Element fetchElementTarget = addTargetElement(project, "fetch.element");
			addAvailableElement(fetchElementTarget, featureXml, featureXml);
			
			Element antFetchFeatureElement = addAntElement(fetchElementTarget,
				"../" + filename, "${buildDirectory}/features", "getFromSubversion");
			addPropertyElement(antFetchFeatureElement, "revision", getRepositoryInfo(id).revision);
			addPropertyElement(antFetchFeatureElement, "destination", id);
			addPropertyElement(antFetchFeatureElement, "quiet", "${quiet}");
			addPropertyElement(antFetchFeatureElement, "svnUrl", getRepositoryInfo(id).svnUrl);
			addPropertyElement(antFetchFeatureElement, "username", getRepositoryInfo(id).username);
			addPropertyElement(antFetchFeatureElement, "password", getRepositoryInfo(id).password);
			addPropertyElement(antFetchFeatureElement, "fileToCheck", "${buildDirectory}/features/" + id + "/feature.xml");
		}
		else {
			String pluginXml = "${buildDirectory}/plugins/" + id + "/plugin.xml";
			String pluginManifest = "${buildDirectory}/plugins/" + id + "/META-INF/MANIFEST.MF";
			
			Element fetchElementTarget = addTargetElement(project, "fetch.element");
			addAvailableElement(fetchElementTarget, pluginXml, pluginXml);
			addAvailableElement(fetchElementTarget, pluginXml, pluginManifest);
			
			Element antFetchFeatureElement = addAntElement(fetchElementTarget,
				"../" + filename, "${buildDirectory}/plugins", "getFromSubversion");
			addPropertyElement(antFetchFeatureElement, "revision", getRepositoryInfo(id).revision);
			addPropertyElement(antFetchFeatureElement, "destination", id);
			addPropertyElement(antFetchFeatureElement, "quiet", "${quiet}");
			addPropertyElement(antFetchFeatureElement, "svnUrl", getRepositoryInfo(id).svnUrl);
			addPropertyElement(antFetchFeatureElement, "username", getRepositoryInfo(id).username);
			addPropertyElement(antFetchFeatureElement, "password", getRepositoryInfo(id).password);
			addPropertyElement(antFetchFeatureElement, "fileToCheck", "${buildDirectory}/plugins/" + id + "/plugin.xml");
		}
	}
	
	private void createFetchPluginsTarget (Element project) throws BuildException
	{
		Element fetchPluginsTarget = addTargetElement(project, "fetch.plugins");
		ISVNClientAdapter svnAdapter =
			CmdLineClientAdapterFactory.createSVNClient(
					CmdLineClientAdapterFactory.COMMANDLINE_CLIENT);
		
		String url = getRepositoryInfo(id).svnUrl;
		String revision = getRepositoryInfo(id).revision;
		
		InputStream featureXmlStream = null;
		
		try {
			featureXmlStream = svnAdapter.getContent(new SVNUrl(url),
				"HEAD".equals(revision) ? SVNRevision.HEAD : new SVNRevision(Integer.parseInt(revision)));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new BuildException(e);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new BuildException(e);
		} catch (SVNClientException e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
		
		if (featureXmlStream == null)
			throw new BuildException("feature xml stream should not be null");
		
		try {
			SAXReader reader = new SAXReader();
			Document featureXml = reader.read(featureXmlStream);
			
			List plugins = featureXml.selectNodes("//feature/plugin");
			for (Iterator iter = plugins.iterator(); iter.hasNext(); )
			{
				Element pluginElement = (Element) iter.next();
				String pluginId = pluginElement.attributeValue("id");
				
				createFetchPluginSection (fetchPluginsTarget, pluginId);
			}
			
			featureXmlStream.close();
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new BuildException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
	}
	
	private void createFetchPluginSection (Element fetchPluginsTarget, String pluginId) throws BuildException
	{
		RepositoryInfo info = getRepositoryInfo(pluginId);
		if (info != null)
		{
			String pluginXml = "${buildDirectory}/plugins/" + pluginId + "/plugin.xml";
			String pluginManifest = "${buildDirectory}/plugins/" + pluginId + "/META-INF/MANIFEST.MFl";
			addAvailableElement(fetchPluginsTarget, pluginXml, pluginXml);
			addAvailableElement(fetchPluginsTarget, pluginXml, pluginManifest);
			
			Element antFetchPluginElement = addAntElement(fetchPluginsTarget,
				"../fetch_" + id + ".xml", "${buildDirectory}/plugins", "getFromSubversion");
		
			addPropertyElement(antFetchPluginElement, "revision", info.revision);
			addPropertyElement(antFetchPluginElement, "destination", info.id);
			addPropertyElement(antFetchPluginElement, "quiet", "${quiet}");
			addPropertyElement(antFetchPluginElement, "svnUrl", info.svnUrl);
			addPropertyElement(antFetchPluginElement, "username", info.username);
			addPropertyElement(antFetchPluginElement, "password", info.password);
			addPropertyElement(antFetchPluginElement, "fileToCheck", pluginXml);
		}
	}
	
	private void createGetFromSubversionTarget (Element project)
	{
		Element getFromSubversionTask = addTargetElement(project, "getFromSubversion",
			null, "${fileToCheck}", "fetching ${destination}");
		
		Element svn = getFromSubversionTask.addElement("svn");
		svn.addAttribute("javahl", "false");
		svn.addAttribute("username", "${username}");
		svn.addAttribute("password", "${password}");
		Element checkout = svn.addElement("checkout");
		checkout.addAttribute("url", "${svnUrl}");
		checkout.addAttribute("destPath", "${destination}");
		checkout.addAttribute("revision", "${revision}");
	}
	
	private RepositoryInfo getRepositoryInfo (String id)
	{
		return (RepositoryInfo) repositoryInfo.get(id);
	}
	
	private void addPropertyElement (Element parent, String name, String value)
	{
		Element property = parent.addElement("property");
		property.addAttribute("name", name);
		property.addAttribute("value", value);
	}
	
	private void addAvailableElement (Element parent, String property, String file)
	{
		Element available = parent.addElement("available");
		available.addAttribute("property", property);
		available.addAttribute("file", file);
	}
	
	private Element addAntElement (Element parent, String antfile, String dir, String target)
	{
		Element ant = parent.addElement("ant");
		ant.addAttribute("antfile", antfile);
		ant.addAttribute("dir", dir);
		ant.addAttribute("target", target);
		return ant;
	}
	
	private Element addTargetElement (Element parent, String targetName)
	{
		return addTargetElement(parent, targetName, null, null, null);
	}
	
	private Element addTargetElement (Element parent, String targetName, String ifProperty, String unlessProperty, String description)
	{
		Element target = parent.addElement("target");
		target.addAttribute("name", targetName);
		
		if (ifProperty != null)
		{
			target.addAttribute("if", ifProperty);
		}
		
		if (unlessProperty != null)
		{
			target.addAttribute("unless", unlessProperty);
		}
		
		if (description != null)
		{
			target.addAttribute("description", description);
		}
		
		return target;
	}
	
	public String getBaseLocation() {
		return baseLocation;
	}

	public void setBaseLocation(String baseLocation) {
		this.baseLocation = baseLocation;
	}

	public String getBuildDirectory() {
		return buildDirectory;
	}

	public void setBuildDirectory(String buildDirectory) {
		this.buildDirectory = buildDirectory;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMapFile() {
		return mapFile;
	}

	public void setMapFile(String mapFile) {
		this.mapFile = mapFile;
	}
	
}
