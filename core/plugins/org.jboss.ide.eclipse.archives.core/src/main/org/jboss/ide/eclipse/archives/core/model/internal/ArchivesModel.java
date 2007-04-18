package org.jboss.ide.eclipse.archives.core.model.internal;

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
import org.jboss.ide.eclipse.archives.core.Trace;
import org.jboss.ide.eclipse.archives.core.build.ArchivesNature;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNodeDelta;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNodeVisitor;
import org.jboss.ide.eclipse.archives.core.model.events.EventManager;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XMLBinding;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbFileSet;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbFolder;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackage;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackageNode;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackages;

public class ArchivesModel {
	
	public static final String PROJECT_PACKAGES_FILE = ".packages";
	
	/**
	 * Singleton instance
	 */
	protected static ArchivesModel instance;
	public static ArchivesModel instance() {
		if( instance == null ) 
			instance = new ArchivesModel();
		return instance;
	}
	
	private HashMap xbPackages; // maps an IProject to XbPackages
	private HashMap archivesRoot; // maps an IProject to PackageModelNode, aka root
	public ArchivesModel() {
		xbPackages = new HashMap();
		archivesRoot = new HashMap();
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
	
	public boolean accept(IArchiveNodeVisitor visitor) {
		IArchiveNode children[] = getAllArchives();
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
	protected ArchiveModelNode[] getAllArchives() {
		ArchiveModelNode[] ret = new ArchiveModelNode[archivesRoot.keySet().size()];
		Iterator i = archivesRoot.keySet().iterator();
		int x = 0;
		while(i.hasNext()) {
			ret[x++] = (ArchiveModelNode)archivesRoot.get(i.next());
		}
		return ret;
	}
	
	public ArchiveModelNode getRoot(IProject project) {
		return getRoot(project, false, new NullProgressMonitor());
	}
	
	public ArchiveModelNode getRoot(IProject project, boolean force, IProgressMonitor monitor) {
		if( archivesRoot.get(project) == null && force ) {
			registerProject(project, monitor);
		}
		return (ArchiveModelNode)(archivesRoot.get(project));
	}
	
	public IArchive[] getProjectArchives(IProject project) {
		ArchiveModelNode root = getRoot(project);
		if( root != null ) {
			List list = Arrays.asList( getRoot(project).getAllChildren());
			return (IArchive[]) list.toArray(new IArchive[list.size()]);
		} else {
			return null;
		}
	}
	
	// to make sure the node root is actually in the model
	public boolean containsRoot(ArchiveModelNode node) {
		return archivesRoot.containsValue(node);
	}
	
	public void registerProject(IProject project, IProgressMonitor monitor) {
		// if the file exists, read it in
		
		monitor.beginTask("Loading configuration...", XMLBinding.NUM_UNMARSHAL_MONITOR_STEPS + 2);
		
		try {
			if (!project.hasNature(ArchivesNature.NATURE_ID)) {
				addProjectNature(project, ArchivesNature.NATURE_ID);
			}
		} catch (CoreException e) {
			Trace.trace(getClass(), e);
		}
		
		ArchiveModelNode root;
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
				root = new ArchiveModelNode(project, packages);
				xbPackages.put(project, packages);
				archivesRoot.put(project, root);
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
			archivesRoot.put(project, new ArchiveModelNode(project, packages));
		}
	}
	
	protected ArchiveNodeImpl createPackageNodeImpl (IProject project, XbPackageNode node, IArchiveNode parent) {
		
		if( node instanceof XbPackages ) {
			ArchiveModelNode impl = getRoot(project);
			for (Iterator iter = node.getAllChildren().iterator(); iter.hasNext(); ) {
				XbPackageNode child = (XbPackageNode) iter.next();
				ArchiveNodeImpl childImpl = createPackageNodeImpl(project, child, impl);
				if (impl != null && childImpl != null) {
					impl.addChild(childImpl, false);
				}
			}
			return null;
		}
		
		ArchiveNodeImpl nodeImpl = null;
		if (node instanceof XbPackage) {
			nodeImpl = new ArchiveImpl((XbPackage)node);
		} else if (node instanceof XbFolder) {
			nodeImpl = new ArchiveFolderImpl((XbFolder)node);
		} else if (node instanceof XbFileSet) {
			nodeImpl = new ArchiveFileSetImpl((XbFileSet)node);
		}
		
		for (Iterator iter = node.getAllChildren().iterator(); iter.hasNext(); ) {
			XbPackageNode child = (XbPackageNode) iter.next();
			ArchiveNodeImpl childImpl = createPackageNodeImpl(project, child, nodeImpl);
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
			IFile packagesFile = project.getFile(ArchivesModel.PROJECT_PACKAGES_FILE);
			if (!packagesFile.exists())
				packagesFile.create(bytesIn, true, monitor);
			else
				packagesFile.setContents(bytesIn, true, true, monitor);
			
			bytesIn.close();
			bytesOut.close();
			
			if (!project.hasNature(ArchivesNature.NATURE_ID)) {
				addProjectNature(project, ArchivesNature.NATURE_ID);
			}
			
			// get deltas
			try {
				ArchiveModelNode root = getRoot(project);
				IArchiveNodeDelta delta = root.getDelta();
				
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
	
	public void attach(IArchiveNode parent, IArchiveNode child, IProgressMonitor monitor) {
		parent.addChild(child);
		if( parent.connectedToModel() && parent.getProject() != null) {
			// save
			saveModel(parent.getProject(), monitor);
		}
	}
}
