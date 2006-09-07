package org.jboss.ide.eclipse.releng;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * This task's purpose is to calculate the set of dependencies for the passed-in feature. This task is recurisve and will look at each plugin.xml/MANIFEST.MF whether inside JAR or folder.
 * After calculating the dependencies, the given property will be filled with a comma-delimited list of dependencies (usable with ant-contrib).
 * Note that this task will exclude any plugins found to be included with the standard Eclipse SDK distribution.
 * Usage:
 * <code>
 * &lt;calculateFeatureDependencies feature="org.jboss.ide.eclipse.feature" eclipseInstallDir="C:/eclipse" pluginList="dependencyList"/&gt;
 * </code>
 * 
 * Optional attribute: addOptional.
 * This attribute will add optional dependencies as well as "hard" dependencies (by default optional dependencies are not included)
 * @author Marshall
 */
public class CalculateFeatureDependenciesTask extends Task {

	private String eclipseInstallDir, feature, pluginList, featureList;
	private boolean addOptional;
	private Set pluginDependencies, featureDependencies;
	private static Set idePlugins;
	
	public CalculateFeatureDependenciesTask ()
	{
		addOptional = false;
	}
	
	private void findSDKPlugins ()
	{
		if (idePlugins != null)
			return;
		
		System.out.println("initializing ide plugin set");
		
		idePlugins = new TreeSet();
		InputStream featureXmlStream = loadFeatureXmlStream("org.eclipse.sdk");
		Document featureDoc = parse(featureXmlStream);
		
		addFeatureDependenciesToSet(featureDoc, idePlugins, null);
		
//		System.out.println("idePlugins="+idePlugins);
		
		try {
			featureXmlStream.close();
		} catch (IOException e) {
			throw new BuildException(e);
		}
	}
	
	private void addFeatureDependenciesToSet (Document featureDoc, Set pluginSet, Set featureSet)
		throws BuildException
	{
		List featureIncludeNodes = featureDoc.selectNodes("//feature/includes");
		String featureName = ((Attribute)featureDoc.selectSingleNode("//feature/@id")).getValue();
		
		if (featureSet != null)
			featureSet.add(featureName);
//		System.out.println("adding sub-features and plugins for feature \"" + featureName + "\"");
		
		for (Iterator iter = featureIncludeNodes.iterator(); iter.hasNext(); )
		{
			Element featureIncludeElement = (Element) iter.next();
			String includedFeature = featureIncludeElement.attributeValue("id");
			InputStream dependencyStream = loadFeatureXmlStream(includedFeature);
			Document dependencyDoc = parse(dependencyStream);
			addFeatureDependenciesToSet(dependencyDoc, pluginSet, featureSet);
			
			try {
				dependencyStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		List importNodes = featureDoc.selectNodes("//feature/requires/import");
		for (Iterator iter = importNodes.iterator(); iter.hasNext(); )
		{
			Element importElement = (Element) iter.next();
			String importedPluginName = importElement.attributeValue("plugin");
			String importedFeatureName = importElement.attributeValue("feature");
			
			// either importing a plugin or a feature
			if (importedPluginName != null)
			{
				if (!pluginSet.contains(importedPluginName)) {
	//				System.out.println("adding required plugin \"" + pluginName + "\" to feature \"" + featureName + "\" dependency set");
					addPluginDependenciesToSet(importedPluginName, pluginSet);
				}
			}
			else if (importedFeatureName != null) {
				InputStream importStream = loadFeatureXmlStream(importedFeatureName);
				Document importDocument = parse(importStream);
				addFeatureDependenciesToSet(importDocument, pluginSet, featureSet);
				
				try {
					importStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		List pluginNodes = featureDoc.selectNodes("//feature/plugin");
		for (Iterator iter = pluginNodes.iterator(); iter.hasNext(); )
		{
			Element pluginElement = (Element) iter.next();
			String pluginName = pluginElement.attributeValue("id");
			String os = pluginElement.attributeValue("os");
			
			if (os == null || os.equals(getPlatformOS()))
			{
				if (!pluginSet.contains(pluginName)) {
//					System.out.println("adding feature plugin \"" + pluginName + "\" to feature \"" + featureName + "\" dependency set");
					addPluginDependenciesToSet(pluginName, pluginSet);
				}
			}
		}		
	}
	
	private static String getPlatformOS()
	{
		// simplified re-implementation of Platform.getOS() so we don't have to rely on eclipse classes
		String osName = System.getProperty("os.name");
		if (osName.indexOf("Windows") != -1)
			return "win32";
		else if (osName.indexOf("Linux") != -1)
			return "linux";
		else if (osName.indexOf("Mac OS X") != -1)
			return "macosx";
		else if (osName.indexOf("AIX") != -1)
			return "aix";
		else if (osName.indexOf("HP-UX") != -1)
			return "hpux";
		else if (osName.indexOf("Solaris") != -1)
			return "solaris";
		else if (osName.indexOf("QNX") != -1)
			return "qnx";
		else return "unknown";
	}
	
	private void addPluginDependenciesToSet (String pluginName, Set set)
		throws BuildException
	{
		set.add(pluginName);
		
		InputStream manifestStream = loadPluginManifestStream(pluginName);
		
		if (manifestStream == null)
			return;
		
		Manifest manifest = null;
		try {
			manifest = new Manifest(manifestStream);
		} catch (IOException e) {
			throw new BuildException(e);
		}
		
		String requireString = manifest.getMainAttributes().getValue("Require-Bundle");
		
		if (requireString != null)
		{
			requireString = requireString.replaceAll("\"[^\"]+\"?", "");
			
			String tokens[] = requireString.split(",");
			
			for (int i = 0; i < tokens.length; i++)
			{
				String pluginId = tokens[i];
				if (tokens[i].indexOf(';') != -1) 
				{
					if (tokens[i].indexOf("resolution:=optional") != -1 && !addOptional)
					{
						continue;
					}
					
					pluginId = pluginId.substring(0, tokens[i].indexOf(';'));
				}
				pluginId = pluginId.trim();
					
				if (!set.contains(pluginId)) {
//					System.out.println("adding plugin \"" + pluginId + "\" to plugin \"" + pluginName + "\" dependency set");
					addPluginDependenciesToSet (pluginId, set);
				}
			}
		}
	}
	
	public void execute() throws BuildException {
		findSDKPlugins();
		
		System.out.println("calculating dependencies for feature \"" + feature + "\"");
		
		pluginDependencies = new TreeSet();
		featureDependencies = new TreeSet();
		InputStream featureXmlStream = loadFeatureXmlStream(feature);
		Document featureDoc = parse(featureXmlStream);
		
		if (featureDoc == null)
			throw new BuildException("Feature Doc should not be null!");
		
		addFeatureDependenciesToSet(featureDoc, pluginDependencies, featureDependencies);
		
		try {
			featureXmlStream.close();
		} catch (IOException e) {
			throw new BuildException(e);
		}
		
		if (pluginDependencies.size() > 0)
		{
			pluginDependencies.removeAll(idePlugins);
			List pluginNodes = featureDoc.selectNodes("//feature/plugin");
			for (Iterator iter = pluginNodes.iterator(); iter.hasNext(); )
			{
				Element pluginElement = (Element) iter.next();
				String pluginId = pluginElement.attributeValue("id");
				pluginDependencies.remove(pluginId);
			}
			
			if (pluginList != null)
				getProject().setProperty(pluginList, setToCommaList(pluginDependencies));
		}
		
		if (featureDependencies.size() > 0)
		{
			String featureName = ((Attribute)featureDoc.selectSingleNode("//feature/@id")).getValue();
			featureDependencies.remove(featureName);
			
			if (featureList != null)
				getProject().setProperty(featureList, setToCommaList(featureDependencies));
		}
	}
	
	private String setToCommaList (Set set)
	{
		StringBuffer list = new StringBuffer();
		for (Iterator iter = set.iterator(); iter.hasNext(); )
		{
			String string = (String) iter.next();
			list.append(string);
			if (iter.hasNext())
				list.append(",");
		}
		return list.toString();
	}
	
	private InputStream loadFeatureXmlStream (String featureName)
	{
		File featureBase = new File(eclipseInstallDir, "features");
		File featureFiles[] = featureBase.listFiles();
		File featureFile = null;
		for (int i = 0; i < featureFiles.length; i++)
		{
			if (featureFiles[i].getName().startsWith(featureName + "_") || featureFiles[i].getName().equals(featureName))
			{
				featureFile = featureFiles[i];
				break;
			}
		}
		
		InputStream featureXmlStream = null;
		try {
			if (featureFile.isDirectory())
			{
				File xmlFile = new File(featureFile, "feature.xml");
				if (xmlFile.exists()) {
					featureXmlStream = new FileInputStream(xmlFile);
				}
			}
			else {
				if (featureFile.getName().endsWith(".jar"))
				{
					JarFile featureJar = new JarFile(featureFile);
					featureXmlStream = featureJar.getInputStream(featureJar.getEntry("feature.xml"));
				}
			}
		} catch (FileNotFoundException e) {
			throw new BuildException(e);
		} catch (IOException e) {
			throw new BuildException(e);
		}
		
		if (featureXmlStream == null)
		{
			throw new BuildException("Feature XML stream could not be found for \"" + featureName + "\" !");
		}
		
		return featureXmlStream;
	}

	private InputStream loadPluginManifestStream (String pluginName)
	{
		File pluginBase = new File(eclipseInstallDir, "plugins");
		File pluginFiles[] = pluginBase.listFiles();
		File pluginFile = null;
		for (int i = 0; i < pluginFiles.length; i++)
		{
			if (pluginFiles[i].getName().startsWith(pluginName + "_") || pluginFiles[i].getName().equals(pluginName))
			{
				pluginFile = pluginFiles[i];
				break;
			}
		}
		
		if (pluginFile == null)
		{
			if (addOptional) return null;
			
			throw new BuildException("Plugin directory/jar could not be found for \"" + pluginName + "\"");
		}
		
		InputStream pluginManifestStream = null;
		try {
			if (pluginFile.isDirectory())
			{
				File manifestFile = new File(pluginFile, "META-INF/MANIFEST.MF");
				if (manifestFile.exists()) {
					pluginManifestStream = new FileInputStream(manifestFile);
				}
			}
			else {
				if (pluginFile.getName().endsWith(".jar"))
				{
					JarFile pluginJar = new JarFile(pluginFile);
					pluginManifestStream = pluginJar.getInputStream(pluginJar.getEntry("META-INF/MANIFEST.MF"));
				}
			}
		} catch (FileNotFoundException e) {
			throw new BuildException(e);
		} catch (IOException e) {
			throw new BuildException(e);
		}
		
		return pluginManifestStream;
	}
	
	private Document parse(InputStream stream) throws BuildException {
		try {
	        SAXReader reader = new SAXReader();
	        Document document = reader.read(stream);
	        return document;
		} catch (DocumentException e) {
			throw new BuildException(e);
		}
    }
	
	public String getEclipseInstallDir() {
		return eclipseInstallDir;
	}

	public void setEclipseInstallDir(String eclipseInstallDir) {
		this.eclipseInstallDir = eclipseInstallDir;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public boolean isAddOptional() {
		return addOptional;
	}

	public void setAddOptional(boolean addOptional) {
		this.addOptional = addOptional;
	}

	public String getFeatureList() {
		return featureList;
	}

	public void setFeatureList(String featureList) {
		this.featureList = featureList;
	}

	public String getPluginList() {
		return pluginList;
	}

	public void setPluginList(String pluginList) {
		this.pluginList = pluginList;
	}
}
