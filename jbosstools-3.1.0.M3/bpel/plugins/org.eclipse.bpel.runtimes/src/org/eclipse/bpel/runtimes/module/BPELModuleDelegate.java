/*******************************************************************************
 * Copyright (c) 2006 University College London Software Systems Engineering
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	Bruno Wassermann - initial API and implementation
 *******************************************************************************/
package org.eclipse.bpel.runtimes.module;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpel.runtimes.IBPELModuleFacetConstants;
import org.eclipse.bpel.runtimes.RuntimesPlugin;
import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.server.core.model.IModuleFile;
import org.eclipse.wst.server.core.model.IModuleFolder;
import org.eclipse.wst.server.core.model.IModuleResource;
import org.eclipse.wst.server.core.model.ModuleDelegate;
import org.eclipse.wst.server.core.util.ModuleFile;
import org.eclipse.wst.server.core.util.ModuleFolder;
import org.eclipse.wst.server.core.util.ProjectModule;

/**
 * A <code>ModuleDelegate</code> implementation for BPEL modules making use
 * of <code>ProjectModule</code> in order to link a BPEL module to the project
 * it is contained in. Nevertheless, at the moment, a BPEL module is considered
 * to be a single BPEL file and not to consist of other resources in the same
 * project.
 *
 * @author Bruno Wassermann, written 14 Jul 2006
 */
public class BPELModuleDelegate extends ProjectModule {
	
	private IFile file;

	/**
	 * Constructor initialising module delegate with the folder in which the 
	 * BPEL module is contained and with the file representing the actual 
	 * BPEL process/module.
	 * 
	 * @param project <code>IProject</code> containing BPEL module (but is not
	 * the same as BPEL module)
	 * @param file <code>IFile</code> representing the actual BPEL process/module
	 */
	public BPELModuleDelegate(IProject project, IFile file) {
		super(project);
		this.file = file;
	}
	
	/**
	 * Overriding implementation in <code>ProjectModule</code> in order to return
	 * actual module type id.
	 *
	 * @see ProjectModule#getId()
	 * @return <code>String</code> representing module type id
	 */
	@Override
	public String getId() {
		return IBPELModuleFacetConstants.BPEL20_MODULE_TYPE;
	}
	
	/**
	 * Returns the name of this module; to be precise the name of the file this
	 * module represents.
	 * 
	 * @see ProjectModule#getName()
	 * @return <code>String</code>
	 */
	@Override
	public String getName() {
		return file.getName();
	}
	
	/**
	 * Returns the file represented by this module.
	 * 
	 * @return <code>IFile</code>
	 */
	public IFile getFile() {
		return file;
	}
	
	/**
	 * Returns whether or not this module exists (may have been removed).
	 * 
	 * @see ProjectModule#exists()
	 * @return <code>true</code> if the module exists, <code>false</code> otherwise
	 */
	@Override
	public boolean exists() {
		return file.exists();
	}
	
	/**
	 * Returns the module resources that should be published to the server. 
	 * The implementation in <code>ProjectModule</code> assumes that the entire
	 * project should be published to the server. 
	 * <p>
	 * For now we will restrict this to be just the selected BPEL file itself
	 * that is represented by this module.
	 * <p>
	 * TODO will have to reconsider implementation to allow for all BPEL files
	 * in a project to be published
	 * <p>
	 * TODO would be interesting to consider publishing any Web services present
	 * in the project that BPEL processes may require as part of automated 
	 * deployment. For now, will suffice, though, to notify user of missing Web
	 * service and ask user to deploy WS first.
	 * 
	 * @see ModuleDelegate#members()
	 * @return <code>IModuleResource</code>[]
	 */
	@Override
	public IModuleResource[] members() {
		IProject project = this.getProject();
		
		final List<IContainer> bpelFolders = new ArrayList<IContainer>();
		IResourceVisitor bpelFolderFinder = new IResourceVisitor() {
			
			public boolean visit(IResource resource) throws CoreException {
				if( resource.getType() == IResource.FILE){
					if("bpel".equals(resource.getFileExtension())){
						bpelFolders.add(resource.getParent());
						return false;
					}
				}
				return true;
			}
		};
		final List<IModuleResource> files = new ArrayList<IModuleResource>();
		IResourceVisitor visitor = new IResourceVisitor() {
			public boolean visit(org.eclipse.core.resources.IResource resource) throws org.eclipse.core.runtime.CoreException {
				IPath path = resource.getProjectRelativePath();
				path = path.removeLastSegments(1);
				if (resource.getType() == IResource.FILE) {
//					if(!isFileOfInterest((IFile)resource)){
//						return true;
//					}
					IModuleFile mf = new ModuleFile((IFile)resource, resource.getName(), path);
					files.add(mf);
				}
				else if( resource.getType() == IResource.FOLDER){
					if( resource.getName().startsWith(".")){
						return false;
					}
					IModuleFolder mf = new ModuleFolder((IFolder)resource, resource.getName(), path);
					files.add(mf);
				}
				return true;
			}
		};
		try {
			project.accept(bpelFolderFinder);
			for(IContainer container: bpelFolders){
				container.accept(visitor);
			}
//			project.accept(visitor);
		} 
		catch (CoreException e) {
			e.printStackTrace();
		}
		
		return files.toArray(new IModuleResource[files.size()]);
	}
	
	protected boolean isFileOfInterest(IFile file) {
		
		String fileExtension = file.getFileExtension();
		
		if (fileExtension.equalsIgnoreCase("bpel"))
			return true; 
		if (fileExtension.equalsIgnoreCase("wsdl"))
			return true; 
		if (fileExtension.equalsIgnoreCase("xsd"))
			return true; 
		if (fileExtension.equalsIgnoreCase("soap"))
			return true;
		if (fileExtension.equalsIgnoreCase("xml"))
			return true; 
		else
			return false;
	}
	
	/**
	 * Overriding superclass implementation. Checks whether the <code>IFile</code>
	 * representing the BPEL module has a bpel file extension (weak validation).
	 * 
	 * @see ModuleDelegate#validate()
	 * <p>
	 * TODO should also allow for BPEL projects being valid modules
	 */
	@Override
	public IStatus validate() {
		super.validate();
		if (IBPELModuleFacetConstants.DOT_BPEL_FILE_EXTENSION.equalsIgnoreCase(
				file.getFileExtension())) 
		{
			return Status.OK_STATUS;
		}
		return new Status(
				IStatus.ERROR, 
				RuntimesPlugin.PLUGIN_ID, 
				0, 
				Messages.InvalidFileExtension, 
				null);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		
		if (this == obj) return true;
		
		if (!(obj instanceof BPELModuleDelegate)) return false;
		
		BPELModuleDelegate bmd = (BPELModuleDelegate) obj;
		
		if (this.getProject() != null 
				&& this.getProject().exists()  
				&& !this.getProject().equals(bmd.getProject()))
		{
			return false;
		}
		
//		if (file != null 
//				&& file.exists() 
//				&& !(file.getFullPath().equals(bmd.getFile().getFullPath()))) 
//		{
//			return false;
//		}
		
		if (getId() != null && !getId().equals(bmd.getId())) return false;
		
		return true;
	}
}
