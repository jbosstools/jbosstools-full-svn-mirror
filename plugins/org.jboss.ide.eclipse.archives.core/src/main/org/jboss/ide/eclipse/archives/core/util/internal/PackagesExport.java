/**
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
package org.jboss.ide.eclipse.archives.core.util.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.jboss.ide.eclipse.archives.core.ArchivesCorePlugin;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNodeVisitor;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackages;

/**
 * Used to export... or will be once I figure out how it worked
 * @author <a href="rob.stryker@redhat.com">Rob Stryker</a>
 *
 */
public class PackagesExport {
	public static final QualifiedName PROPERTY_ANT_SCRIPT_PATH =
		new QualifiedName("org.jboss.ide.eclipse.archives.core", "antScriptPath");

	public static final IPath DEFAULT_ANT_SCRIPT_PATH = new Path("buildPackages.xml");
	public static final String XSL_PATH = "xml/packages2ant.xsl";
	
	private static String escapeProjectName(IProject project)
	{
		String name = project.getName();
		name = name.replaceAll(" ", "_");
		return name;
	}
	
	public static List findReferencedProjects (IProject project, IArchive pkg)
	{
		final ArrayList referencedProjects = new ArrayList();
		referencedProjects.add(project);
		
		pkg.accept(new IArchiveNodeVisitor () {
			public boolean visit (IArchiveNode node)
			{
				if (node.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FILESET)
				{
					IArchiveFileSet fileset = (IArchiveFileSet)node;
					IProject project = fileset.getProject();
					if (!project.equals(project) && !referencedProjects.contains(project))
					{
						referencedProjects.add(project);
					}
				}
				return true;
			}
		});
		return referencedProjects;
	}
		
	public static void exportAntScript (IProject project, Map args, IProgressMonitor monitor)
	{
		monitor.beginTask("Exporting ant script...", 2);
	
		XbPackages projectPackagesElement = ArchivesModel.instance().getXbPackages(project);
		IArchive packages[] = ArchivesModel.instance().getProjectArchives(project);
		ArrayList referencedProjects = new ArrayList();
		
		monitor.beginTask("Finding referenced projects...", packages.length);
		for (int i = 0; i < packages.length; i++)
		{
			referencedProjects.addAll(findReferencedProjects(project, packages[i]));
			monitor.worked(1);
		}
		monitor.done();
		
		for (Iterator iter = referencedProjects.iterator(); iter.hasNext(); )
		{
			IProject referencedProject = (IProject)iter.next();
			String propertyName = null;
			
			if (referencedProject.equals(project))
			{
				propertyName = "project-root";
			}
			else {
				propertyName = "project-root-" + escapeProjectName(referencedProject);
			}
			
			IPath location = referencedProject.getLocation();
			if (location != null)
				projectPackagesElement.getProperties().getProperties().setProperty(propertyName, location.toString());
		}
		
		try {
			InputStream configIn = project.getFile(ArchivesModel.PROJECT_PACKAGES_FILE).getContents();
			InputStream xslIn = ArchivesCorePlugin.getDefault().getBundle().getEntry(XSL_PATH).openStream();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			IFile antFile = project.getFile(getPathToPackagesScript(project));
			
			Source stylesheet = new StreamSource(xslIn);
			Transformer transformer = TransformerFactory.newInstance().newTransformer(stylesheet);
			transformer.setOutputProperty("indent", "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(new StreamSource(configIn), new StreamResult(out));
			
			ByteArrayInputStream fileStream = new ByteArrayInputStream(out.toByteArray());
			if (!antFile.exists()) {
				antFile.create(fileStream, true, monitor);
			}
			else {
				antFile.setContents(fileStream, IFile.KEEP_HISTORY, monitor);
			}
			
			xslIn.close();
			out.close();
			
			monitor.worked(1);
			monitor.done();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static IPath getPathToPackagesScript (IProject project)
	{
		try {
			String scriptPath = project.getPersistentProperty(PROPERTY_ANT_SCRIPT_PATH);
			if (scriptPath == null) {
				project.setPersistentProperty(PROPERTY_ANT_SCRIPT_PATH, DEFAULT_ANT_SCRIPT_PATH.toString());
				return DEFAULT_ANT_SCRIPT_PATH;
			}
			else
				return new Path(scriptPath);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return DEFAULT_ANT_SCRIPT_PATH;
	}
	
	public static IPath getRawPathToPackagesScript (IProject project)
	{
		IPath scriptPath = getPathToPackagesScript(project);
		IFile script = project.getFile(scriptPath);
		if (script != null)
		{
			if (script.getLocation() == null)
				return script.getRawLocation();
			return script.getLocation();
		}
		
		return scriptPath;
	}

}
