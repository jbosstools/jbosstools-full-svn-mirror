package org.jboss.ide.eclipse.packages.core.util;

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
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.packages.core.PackagesCorePlugin;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.IPackageNodeVisitor;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.packages.core.model.internal.PackagesModel;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackages;

public class PackagesExport {

	public static final String XSL_PATH = "xml/packages2ant.xsl";
	
	private static String escapeProjectName(IProject project)
	{
		String name = project.getName();
		name = name.replaceAll(" ", "_");
		return name;
	}
	
	public static List findReferencedProjects (IProject project, IPackage pkg)
	{
		final ArrayList referencedProjects = new ArrayList();
		referencedProjects.add(project);
		
		pkg.accept(new IPackageNodeVisitor () {
			public boolean visit (IPackageNode node)
			{
				if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
				{
					IPackageFileSet fileset = (IPackageFileSet)node;
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
	
		XbPackages projectPackagesElement = PackagesModel.instance().getXbPackages(project);
		IPackage packages[] = PackagesCore.getProjectPackages(project, monitor);
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
			
			IPath location = ProjectUtil.getProjectLocation(referencedProject);
			if (location != null)
				projectPackagesElement.getProperties().getProperties().setProperty(propertyName, location.toString());
		}
		
		try {
			InputStream configIn = project.getFile(PackagesModel.PROJECT_PACKAGES_FILE).getContents();
			InputStream xslIn = PackagesCorePlugin.getDefault().getBundle().getEntry(XSL_PATH).openStream();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			IFile antFile = project.getFile(PackagesCore.getPathToPackagesScript(project));
			
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
}
