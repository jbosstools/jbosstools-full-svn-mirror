package org.jboss.ide.eclipse.packages.core.model.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jboss.ide.eclipse.packages.core.Trace;
import org.jboss.ide.eclipse.packages.core.build.PackagesNature;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.IPackageNodeVisitor;
import org.jboss.ide.eclipse.packages.core.model.IPackagesModelDelta;
import org.jboss.ide.eclipse.packages.core.model.events.EventManager;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XMLBinding;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbFileSet;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbFolder;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackage;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackageNode;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackages;

public class PackagesModel {
	
	public static final String PROJECT_PACKAGES_FILE = ".packages";
	
	/**
	 * Singleton instance
	 */
	protected static PackagesModel instance;
	public static PackagesModel instance() {
		if( instance == null ) 
			instance = new PackagesModel();
		return instance;
	}
	
	private HashMap xbPackages; // maps an IProject to XbPackages
	private HashMap packagesRoot; // maps an IProject to PackageModelNode, aka root
	public PackagesModel() {
		xbPackages = new HashMap();
		packagesRoot = new HashMap();
	}
	
	public XbPackages getXbPackages(IProject project) {
		return (XbPackages)(xbPackages.get(project));
	}
	
	/**
	 * If the project hasn't been registered, register it
	 * @param project
	 * @param monitor
	 * @return
	 */
	public XbPackages getXbPackages(IProject project, IProgressMonitor monitor) {
		if( !xbPackages.containsKey(project)) 
			registerProject(project, monitor);
		return (XbPackages)(xbPackages.get(project));
	}
	
	public boolean accept(IPackageNodeVisitor visitor) {
		IPackageNode children[] = getAllPackages();
		boolean keepGoing = true;

		if (keepGoing) {
			for (int i = 0; i < children.length; i++) {
				if (keepGoing) {
					keepGoing = children[i].accept(visitor);
				}
			}
		}
		
		return keepGoing;
	}	
	
	/**
	 * Gets every single registered model
	 * @return
	 */
	protected PackageModelNode[] getAllPackages() {
		PackageModelNode[] ret = new PackageModelNode[packagesRoot.keySet().size()];
		Iterator i = packagesRoot.keySet().iterator();
		int x = 0;
		while(i.hasNext()) {
			ret[x++] = (PackageModelNode)packagesRoot.get(i.next());
		}
		return ret;
	}
	
	public PackageModelNode getRoot(IProject project) {
		return getRoot(project, false, new NullProgressMonitor());
	}
	
	public PackageModelNode getRoot(IProject project, boolean force, IProgressMonitor monitor) {
		if( packagesRoot.get(project) == null && force ) {
			registerProject(project, monitor);
		}
		return (PackageModelNode)(packagesRoot.get(project));
	}
	
	public IPackage[] getProjectPackages(IProject project) {
		PackageModelNode root = getRoot(project);
		if( root != null ) {
			List list = Arrays.asList( getRoot(project).getAllChildren());
			return (IPackage[]) list.toArray(new IPackage[list.size()]);
		} else {
			return null;
		}
	}
	
	// to make sure the node root is actually in the model
	public boolean containsRoot(PackageModelNode node) {
		return packagesRoot.containsValue(node);
	}
	
	public void registerProject(IProject project, IProgressMonitor monitor) {
		// if the file exists, read it in
		
		monitor.beginTask("Loading configuration...", XMLBinding.NUM_UNMARSHAL_MONITOR_STEPS + 2);
		
		try {
			if (!project.hasNature(PackagesNature.NATURE_ID)) {
				addProjectNature(project, PackagesNature.NATURE_ID);
			}
		} catch (CoreException e) {
			Trace.trace(getClass(), e);
		}
		
		PackageModelNode root;
		IFile packagesFile = project.getFile(PROJECT_PACKAGES_FILE);
		if (packagesFile.exists())
		{
			try {
				XbPackages packages = XMLBinding.unmarshal(packagesFile.getContents(), monitor);
				monitor.worked(1);
				
				if (packages == null) {
					// Empty / non-working XML file loaded
					Trace.trace(getClass(), "WARNING: .packages file for project " + project.getName() + " is empty or contains the wrong content");
					return;
				}
				root = new PackageModelNode(project, packages);
				xbPackages.put(project, packages);
				packagesRoot.put(project, root);
				createPackageNodeImpl(project, packages, null);
				root.clearDeltas();
				monitor.worked(1);
			} catch (CoreException e) {
				Trace.trace(getClass(), e);
			}
		} else {
			// file not found, just create some default xbpackages and insert them
			XbPackages packages = new XbPackages();
			xbPackages.put(project, packages);
			packagesRoot.put(project, new PackageModelNode(project, packages));
		}
	}
	
	protected PackageNodeImpl createPackageNodeImpl (IProject project, XbPackageNode node, IPackageNode parent) {
		
		if( node instanceof XbPackages ) {
			PackageModelNode impl = getRoot(project);
			for (Iterator iter = node.getAllChildren().iterator(); iter.hasNext(); ) {
				XbPackageNode child = (XbPackageNode) iter.next();
				PackageNodeImpl childImpl = createPackageNodeImpl(project, child, impl);
				if (impl != null && childImpl != null) {
					impl.addChild(childImpl, false);
				}
			}
			return null;
		}
		
		PackageNodeImpl nodeImpl = null;
		if (node instanceof XbPackage) {
			nodeImpl = new PackageImpl((XbPackage)node);
		} else if (node instanceof XbFolder) {
			nodeImpl = new PackageFolderImpl((XbFolder)node);
		} else if (node instanceof XbFileSet) {
			nodeImpl = new PackageFileSetImpl((XbFileSet)node);
		}
		
		for (Iterator iter = node.getAllChildren().iterator(); iter.hasNext(); ) {
			XbPackageNode child = (XbPackageNode) iter.next();
			PackageNodeImpl childImpl = createPackageNodeImpl(project, child, nodeImpl);
			if (nodeImpl != null && childImpl != null) {
				nodeImpl.addChild(childImpl, false);
			}
		}
		
		return nodeImpl;
	}
	
   public static boolean addProjectNature(IProject project, String natureId) {
	   boolean added = false;
	   try {
		   if (project != null && natureId != null) {
			   IProjectDescription desc = project.getDescription();
			   
			   if (!project.hasNature(natureId)) {
				   String natureIds[] = desc.getNatureIds();
				   String newNatureIds[] = new String[natureIds.length + 1];
				   
				   System.arraycopy(natureIds, 0, newNatureIds, 1, natureIds.length);
				   newNatureIds[0] = natureId;
				   desc.setNatureIds(newNatureIds);
				   
				   project.getProject().setDescription(desc, new NullProgressMonitor());
				   added = true;
			   }
		   }
	   } catch (CoreException e) {
		   e.printStackTrace();
	   }
	   return added;
   }

	public void saveModel (IProject project, IProgressMonitor monitor) {
		// get a list of dirty nodes
		
		try {
			if (monitor == null)
				monitor = new NullProgressMonitor();
			
			ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(bytesOut);
			XbPackages packs = getXbPackages(project);
			XMLBinding.marshal(packs, writer, monitor);
			writer.close();
			
			ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesOut.toByteArray());
			IFile packagesFile = project.getFile(PackagesModel.PROJECT_PACKAGES_FILE);
			if (!packagesFile.exists())
				packagesFile.create(bytesIn, true, monitor);
			else
				packagesFile.setContents(bytesIn, true, true, monitor);
			
			bytesIn.close();
			bytesOut.close();
			
			if (!project.hasNature(PackagesNature.NATURE_ID)) {
				addProjectNature(project, PackagesNature.NATURE_ID);
			}
			
			// get deltas
			try {
				PackageModelNode root = getRoot(project);
				IPackagesModelDelta delta = root.getDelta();
				
				// clear deltas
				root.clearDeltas();
				
				// fire delta events
				EventManager.fireDelta(delta);
			} catch( Exception e ) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void attach(IPackageNode parent, IPackageNode child, IProgressMonitor monitor) {
		parent.addChild(child);
		if( parent.connectedToModel() && parent.getProject() != null) {
			// save
			saveModel(parent.getProject(), monitor);
		}
	}
}
