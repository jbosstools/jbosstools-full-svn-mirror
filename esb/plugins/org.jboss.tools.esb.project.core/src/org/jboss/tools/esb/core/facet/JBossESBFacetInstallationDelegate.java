package org.jboss.tools.esb.core.facet;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.common.project.facet.WtpUtils;
import org.eclipse.jst.common.project.facet.core.ClasspathHelper;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

public class JBossESBFacetInstallationDelegate implements IDelegate {

	
	private IDataModel model;
	public static final String ESB_NATURE = "org.jboss.tools.esb.project.core.ESBNature";

	public void execute(IProject project, IProjectFacetVersion fv,
			Object config, IProgressMonitor monitor) throws CoreException {
		model = (IDataModel) config;

		createProjectStructure(project);

		final IJavaProject jproj = JavaCore.create(project);

		// Add WTP natures.
		WtpUtils.addNatures(project);

		// Setup the flexible project structure.
		final IVirtualComponent c = ComponentCore.createComponent(project);
		c.create(0, null);
		String esbContent = model.getStringProperty(IJBossESBFacetDataModelProperties.ESB_CONTENT_FOLDER);
		c.setMetaProperty("java-output-path", "/" + esbContent + "/build/classes/");

		final IVirtualFolder jbiRoot = c.getRootFolder();

		// Create directory structure
		String srcFolder = null;
		srcFolder = model
				.getStringProperty(IJBossESBFacetDataModelProperties.ESB_SOURCE_FOLDER);
		jbiRoot.createLink(new Path("/" + srcFolder), 0, null);
		String resourcesFolder = model
				.getStringProperty(IJBossESBFacetDataModelProperties.ESB_CONTENT_FOLDER);
		jbiRoot.createLink(new Path("/" + resourcesFolder), 0, null);
		
		
		
		//addESBNature(project);

		String runtimeId = model
				.getStringProperty(IJBossESBFacetDataModelProperties.RUNTIME_ID);
		JBossClassPathCommand command = new JBossClassPathCommand(project,
					model);
		IStatus status = command.executeOverride(monitor);
		if (!status.equals(Status.OK_STATUS)) {
			throw new CoreException(status);
		}
		
		ClasspathHelper.removeClasspathEntries(project, fv);
		ClasspathHelper.addClasspathEntries(project, fv);
		
		//String prjName = model.getStringProperty(IFacetDataModelProperties.FACET_PROJECT_NAME);
		//IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(prjName);
		

	}
	
	private IFile createJBossESBXML(IFolder folder) throws CoreException{
		StringBuffer emptyESB = new StringBuffer();
		emptyESB.append("<?xml version = \"1.0\" encoding = \"UTF-8\"?>");
		emptyESB.append("\n");
		emptyESB.append("<jbossesb xmlns=\"http://anonsvn.labs.jboss.com/labs/jbossesb/trunk/product/etc/schemas/xml/jbossesb-1.0.1.xsd\" parameterReloadSecs=\"5\">");
		emptyESB.append("\n");
		emptyESB.append("</jbossesb>");
		IFile esbfile = folder.getFile("jboss-esb.xml");
		esbfile.create(new ByteArrayInputStream(emptyESB.toString().getBytes()), true, null);
		
		return esbfile;
	}
	

	

	private void createProjectStructure(IProject project) throws CoreException{
		String strContentFolder = model.getStringProperty(IJBossESBFacetDataModelProperties.ESB_CONTENT_FOLDER);
		project.setPersistentProperty(IJBossESBFacetDataModelProperties.QNAME_ESB_CONTENT_FOLDER, strContentFolder);
		IFolder esbContent = project.getFolder(strContentFolder);
		if(!esbContent.exists()) {
			esbContent.create(true, true, null);			
		}
		
		esbContent.getFolder("lib").create(true, true, null);
		esbContent.getFolder("META-INF").create(true, true, null);
		createJBossESBXML(esbContent.getFolder("META-INF"));
		
		project.refreshLocal(IResource.DEPTH_ZERO, null);
	}
	
	private static void addESBNature(IProject project) throws CoreException{
		IProjectDescription desc = project.getDescription();
		 final String[] current = desc.getNatureIds();
	        final String[] replacement = new String[ current.length + 1 ];
	        System.arraycopy( current, 0, replacement, 1, current.length );
	        replacement[ 0 ] = ESB_NATURE;
	        desc.setNatureIds( replacement );
	        project.setDescription( desc, null );
		
	}

}
