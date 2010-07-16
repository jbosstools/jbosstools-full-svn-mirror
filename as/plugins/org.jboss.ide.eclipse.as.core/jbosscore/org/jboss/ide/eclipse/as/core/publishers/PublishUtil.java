/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.ide.eclipse.as.core.publishers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.server.core.IEnterpriseApplication;
import org.eclipse.jst.server.core.IJ2EEModule;
import org.eclipse.wst.common.componentcore.ModuleCoreNature;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.internal.DeletedModule;
import org.eclipse.wst.server.core.model.IModuleFile;
import org.eclipse.wst.server.core.model.IModuleFolder;
import org.eclipse.wst.server.core.model.IModuleResource;
import org.eclipse.wst.server.core.model.IModuleResourceDelta;
import org.eclipse.wst.server.core.model.ModuleDelegate;
import org.eclipse.wst.server.core.util.ProjectModule;
import org.jboss.ide.eclipse.as.core.JBossServerCorePlugin;
import org.jboss.ide.eclipse.as.core.extensions.events.IEventCodes;
import org.jboss.ide.eclipse.as.core.server.IDeployableServer;
import org.jboss.ide.eclipse.as.core.server.IJBossServerConstants;
import org.jboss.ide.eclipse.as.core.server.xpl.ModulePackager;
import org.jboss.ide.eclipse.as.core.util.DeploymentPreferenceLoader;
import org.jboss.ide.eclipse.as.core.util.DeploymentPreferenceLoader.DeploymentModulePrefs;
import org.jboss.ide.eclipse.as.core.util.DeploymentPreferenceLoader.DeploymentPreferences;
import org.jboss.ide.eclipse.as.core.util.DeploymentPreferenceLoader.DeploymentTypePrefs;
import org.jboss.ide.eclipse.as.core.util.IJBossToolingConstants;
import org.jboss.ide.eclipse.as.core.util.IWTPConstants;

public class PublishUtil {
	public static int countChanges(IModuleResourceDelta[] deltas) {
		IModuleResource res;
		int count = 0;
		if( deltas == null ) return 0;
		for( int i = 0; i < deltas.length; i++ ) {
			res = deltas[i].getModuleResource();
			if( res != null && res instanceof IModuleFile)
				count++;
			count += countChanges(deltas[i].getAffectedChildren());
		}
		return count;
	}


	public static int countMembers(IModule module) {
		try {
			ModuleDelegate delegate = (ModuleDelegate)module.loadAdapter(ModuleDelegate.class, new NullProgressMonitor());
			return delegate == null ? 0 : countMembers(delegate.members());
		} catch( CoreException ce ) {}
		return 0;
	}
	public static int countMembers(IModuleResource[] resources) {
		int count = 0;
		if( resources == null ) return 0;
		for( int i = 0; i < resources.length; i++ ) {
			if( resources[i] instanceof IModuleFile ) {
				count++;
			} else if( resources[i] instanceof IModuleFolder ) {
				count += countMembers(((IModuleFolder)resources[i]).members());
			}
		}
		return count;
	}

	public static String getDeployRootFolder(IModule[] moduleTree, 
			IDeployableServer server, String defaultFolder, String moduleProperty) {
		String folder = defaultFolder;
		// TODO bug 286699
		DeploymentPreferences prefs = DeploymentPreferenceLoader.loadPreferencesFromServer(server.getServer());
		DeploymentTypePrefs typePrefs = prefs.getOrCreatePreferences("local"); //$NON-NLS-1$
		DeploymentModulePrefs modPrefs = typePrefs.getModulePrefs(moduleTree[0]);
		if( modPrefs != null ) {
			String loc = modPrefs.getProperty(moduleProperty);
			if( loc != null && !loc.equals("") ) { //$NON-NLS-1$
				if( !new Path(loc).isAbsolute()) {
					folder = server.getServer().getRuntime().getLocation().append(loc).toString();
				} else {
					folder = loc;
				}
				// TODO translate for variables?
			}
		}
		return folder;
	}
	
	public static IPath getDeployPath(IModule[] moduleTree, IDeployableServer server) {
		String folder = getDeployRootFolder(
				moduleTree, server, 
				server.getDeployFolder(), 
				IJBossToolingConstants.LOCAL_DEPLOYMENT_LOC);
		return getDeployPath(moduleTree, folder);
	}

	public static IPath getDeployRootFolder(IModule[] moduleTree, IDeployableServer server) {
		String folder = getDeployRootFolder(
				moduleTree, server, 
				server.getDeployFolder(), 
				IJBossToolingConstants.LOCAL_DEPLOYMENT_LOC);
		return new Path(folder);
	}

	public static IPath getTempDeployFolder(IModule[] moduleTree, IDeployableServer server) {
		String folder = getDeployRootFolder(
				moduleTree, server, 
				server.getTempDeployFolder(), 
				IJBossToolingConstants.LOCAL_DEPLOYMENT_TEMP_LOC);
		return new Path(folder);
	}
	
	public static IPath getDeployPath(IModule[] moduleTree, String deployFolder) {
		IPath root = new Path( deployFolder );
		String type, modName, name, uri, suffixedName;
		for( int i = 0; i < moduleTree.length; i++ ) {	
			IJ2EEModule j2eeModule = (IJ2EEModule) moduleTree[i].loadAdapter(IJ2EEModule.class, null);
			type = moduleTree[i].getModuleType().getId();
			modName = moduleTree[i].getName();
			name = new Path(modName).lastSegment();
			suffixedName = name + getSuffix(type);
			uri = getParentRelativeURI(moduleTree, i, suffixedName);
			root = root.append(uri);
		}
		return root;
	}
	
	private static String getParentRelativeURI(IModule[] tree, int index, String defaultName) {
		if( index != 0 ) {
			IEnterpriseApplication parent = (IEnterpriseApplication)tree[index-1].loadAdapter(IEnterpriseApplication.class, null);
			if( parent != null ) {
				String uri = parent.getURI(tree[index]);
				if(uri != null )
					return uri;
			}
			// TODO if we make our own "enterprise app" interface, do that here
		} 
		// return name with extension
		return defaultName;

	}
	
	public static ArrayList<String> moduleCoreFactories = new ArrayList<String>();
	static {
		moduleCoreFactories.add("org.eclipse.jst.j2ee.server"); //$NON-NLS-1$
		moduleCoreFactories.add("org.eclipse.jst.jee.server"); //$NON-NLS-1$
	}
	public static void addModuleCoreFactory(String s) {
		if( !moduleCoreFactories.contains(s))
			moduleCoreFactories.add(s);
	}
	public static boolean isModuleCoreProject(IModule[] module) {
		IModule lastmod = module[module.length-1];
		if( lastmod.getProject() == null && lastmod instanceof DeletedModule) {
			int colon = lastmod.getId().indexOf(':');
			String factory = lastmod.getId().substring(0,colon == -1 ? lastmod.getId().length() : colon);
			return moduleCoreFactories.contains(factory);
		}
		return ModuleCoreNature.isFlexibleProject(lastmod.getProject());
	}

	
	private static String getSuffix(String type) {
		String suffix = null;
		if( IJBossServerConstants.FACET_EAR.equals(type)) 
			suffix = IJBossServerConstants.EXT_EAR;
		else if( IJBossServerConstants.FACET_WEB.equals(type)) 
			suffix = IJBossServerConstants.EXT_WAR;
		else if( IJBossServerConstants.FACET_CONNECTOR.equals(type)) 
			suffix = IJBossServerConstants.EXT_RAR;
		else if( IJBossServerConstants.FACET_ESB.equals(type))
			suffix = IJBossServerConstants.EXT_ESB;
		else if( "jboss.package".equals(type)) //$NON-NLS-1$ 
			// no suffix required, name already has it
			suffix = ""; //$NON-NLS-1$
		else
			suffix = IJBossServerConstants.EXT_JAR;
		return suffix;
	}
	
	// TODO This can also change to find the isBinaryModule method 
	public static boolean isBinaryObject(IModule[] moduleTree) {
		IJ2EEModule j2eeModule = (IJ2EEModule) moduleTree[moduleTree.length-1].loadAdapter(IJ2EEModule.class, null);
		return j2eeModule != null && j2eeModule.isBinary();
	}
	
	public static IModuleResource[] getResources(IModule module) throws CoreException {
		ModuleDelegate md = (ModuleDelegate)module.loadAdapter(ModuleDelegate.class, new NullProgressMonitor());
		IModuleResource[] members = md.members();
		return members;
	}
	public static IModuleResource[] getResources(IModule[] tree) throws CoreException {
		return getResources(tree[tree.length-1]);
	}
	
	public static File getFile(IModuleResource resource) {
		File source = (File)resource.getAdapter(File.class);
		if( source == null ) {
			IFile ifile = (IFile)resource.getAdapter(IFile.class);
			if( ifile != null ) 
				source = ifile.getLocation().toFile();
		}
		return source;
	}
	
	public static boolean deployPackaged(IModule[] moduleTree) {
		if( moduleTree[moduleTree.length-1].getModuleType().getId().equals(IWTPConstants.FACET_UTILITY)) return true;
		if( moduleTree[moduleTree.length-1].getModuleType().getId().equals(IWTPConstants.FACET_APP_CLIENT)) return true;
		return false;
	}
	public static java.io.File getFile(IModuleFile mf) {
		return (IFile)mf.getAdapter(IFile.class) != null ? 
					((IFile)mf.getAdapter(IFile.class)).getLocation().toFile() :
						(java.io.File)mf.getAdapter(java.io.File.class);
	}
	
	
	/*
	 * Just package into a jar raw.  Don't think about it, just do it
	 */
	public static IStatus[] packModuleIntoJar(IModule module, IPath destination)throws CoreException {
		String dest = destination.toString();
		ModulePackager packager = null;
		try {
			packager = new ModulePackager(dest, false);
			ProjectModule pm = (ProjectModule) module.loadAdapter(ProjectModule.class, null);
			IModuleResource[] resources = pm.members();
			for (int i = 0; i < resources.length; i++) {
				doPackModule(resources[i], packager);
			}
		} catch (IOException e) {
			IStatus status = new Status(IStatus.ERROR, JBossServerCorePlugin.PLUGIN_ID, IEventCodes.JST_PUB_ASSEMBLE_FAIL,
					"unable to assemble module " + module.getName(), e); //$NON-NLS-1$
			return new IStatus[]{status};
		}
		finally{
			try{
				if( packager != null ) 
					packager.finished();
			}
			catch(IOException e){
				IStatus status = new Status(IStatus.ERROR, JBossServerCorePlugin.PLUGIN_ID, IEventCodes.JST_PUB_ASSEMBLE_FAIL,
						"unable to assemble module "+ module.getName(), e); //$NON-NLS-1$
				return new IStatus[]{status};
			}
		}
		return new IStatus[]{};
	}

	
	/* Add one file or folder to a jar */
	public static void doPackModule(IModuleResource resource, ModulePackager packager) throws CoreException, IOException{
		if (resource instanceof IModuleFolder) {
			IModuleFolder mFolder = (IModuleFolder)resource;
			IModuleResource[] resources = mFolder.members();

			packager.writeFolder(resource.getModuleRelativePath().append(resource.getName()).toPortableString());

			for (int i = 0; resources!= null && i < resources.length; i++) {
				doPackModule(resources[i], packager);
			}
		} else {
			String destination = resource.getModuleRelativePath().append(resource.getName()).toPortableString();
			IFile file = (IFile) resource.getAdapter(IFile.class);
			if (file != null)
				packager.write(file, destination);
			else {
				File file2 = (File) resource.getAdapter(File.class);
				packager.write(file2, destination);
			}
		}
	}
	
}
