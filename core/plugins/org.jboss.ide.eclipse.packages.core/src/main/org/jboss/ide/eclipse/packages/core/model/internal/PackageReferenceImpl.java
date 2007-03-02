package org.jboss.ide.eclipse.packages.core.model.internal;

import java.util.jar.Manifest;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageFolder;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.IPackageNodeVisitor;
import org.jboss.ide.eclipse.packages.core.model.IPackageReference;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackage;
import org.jboss.ide.eclipse.packages.core.model.types.IPackageType;

public class PackageReferenceImpl implements IPackageReference {

	protected IPackage pkg;
	protected IPackageNode parent;
	protected XbPackage xbPackage;
	
	public PackageReferenceImpl (IPackage pkg, XbPackage xbPackage)
	{
		this.xbPackage = xbPackage;
		setPackage(pkg);
	}

	public int getNodeType() {
		return TYPE_PACKAGE_REFERENCE;
	}
	
	public IPackage getPackage() {
		return this.pkg;
	}

	public void setPackage (IPackage pkg)
	{
		this.pkg = pkg;
		
		String refPath = new String(pkg.getProject().getName());
		if (pkg.isDestinationInWorkspace())
		{
			refPath += "/workspace";
			IResource pkgFile = pkg.getPackageResource();
			
			refPath += pkgFile.getFullPath().toString();
		}
		else {
			refPath += "/filesystem/";
			String pkgPath = pkg.getPackageFilePath().toOSString();

			if (pkgPath.charAt(0) == '/')
			{
				refPath += pkgPath.substring(1);
			} else {
				refPath += pkgPath;
			}
		}
		xbPackage.setRef(refPath);
		
		((PackageImpl)this.pkg).addReference(this);
	}
	
	public XbPackage getDelegate ()
	{
		return xbPackage;
	}

	public IPackageNode getParent() {
		return parent;
	}

	public void setParent(IPackageNode parent) {
		this.parent = parent;
		
		xbPackage.setParent(((PackageNodeImpl)parent).getNodeDelegate());
	}

	public static class RefAttributes {
		public static int WORKSPACE = 0;
		public static int FILESYSTEM = 1;
		
		public String projectName;
		public int locationType;
		public IPath packagePath;
	}
	
	public static RefAttributes getRefAttributes (String refString)
	{
		RefAttributes attrs = new RefAttributes();
		
		String pieces[] = refString.split("/", 3);
		attrs.projectName = pieces[0];
		if (pieces[1].equalsIgnoreCase("workspace")) {
			attrs.locationType = RefAttributes.WORKSPACE;
		} else {
			attrs.locationType = RefAttributes.FILESYSTEM;
		}
		
		attrs.packagePath = new Path(pieces[2]);
		return attrs;
	}
	
	public boolean accept(IPackageNodeVisitor visitor, boolean depthFirst) {
		return pkg.accept(visitor, depthFirst);
	}

	public boolean accept(IPackageNodeVisitor visitor) {
		return pkg.accept(visitor);
	}

	public void addChild(IPackageNode child) {
		pkg.addChild(child);
	}

	public void addChild(IPackageReference pkgRef) {
		pkg.addChild(pkgRef);
	}

	public IPackageNode[] getAllChildren() {
		return pkg.getAllChildren();
	}

	public IPackageNode[] getChildren(int type) {
		return pkg.getChildren(type);
	}

	public IContainer getDestinationContainer() {
		return pkg.getDestinationContainer();
	}

	public IPath getDestinationPath() {
		return pkg.getDestinationPath();
	}

	public IPackageFileSet[] getFileSets() {
		return pkg.getFileSets();
	}

	public IPackageFolder[] getFolders() {
		return pkg.getFolders();
	}

	public Manifest getManifest() {
		return pkg.getManifest();
	}

	public String getName() {
		return pkg.getName();
	}

	public IResource getPackageResource() {
		return pkg.getPackageResource();
	}

	public IPath getPackageFilePath() {
		return pkg.getPackageFilePath();
	}

	public IPath getPackageRelativePath() {
		return PackagesCore.getPackageRelativePath(this);
	}

	public IPackage[] getPackages() {
		return pkg.getPackages();
	}

	public IPackageType getPackageType() {
		return pkg.getPackageType();
	}

	public IProject getProject() {
		return pkg.getProject();
	}

	public String getProperty(String property) {
		return pkg.getProperty(property);
	}
	
	public void setProperty(String property, String value) {
		pkg.setProperty(property, value);
	}

	public boolean hasChild(IPackageNode child) {
		return pkg.hasChild(child);
	}

	public boolean hasChildren() {
		return pkg.hasChildren();
	}

	public boolean hasManifest() {
		return pkg.hasManifest();
	}

	public boolean isDestinationInWorkspace() {
		return pkg.isDestinationInWorkspace();
	}

	public boolean isExploded() {
		return pkg.isExploded();
	}

	public boolean isReference() {
		return true;
	}

	public IPackageReference[] getReferences() {
		return new IPackageReference[0];
	}
	
	public boolean isTopLevel() {
		return pkg.isTopLevel();
	}

	public void removeChild(IPackageNode child) {
		pkg.removeChild(child);
	}

	public void setDestinationContainer(IContainer container) {
		pkg.setDestinationContainer(container);
	}

	public void setDestinationPath(IPath path) {
		pkg.setDestinationPath(path);
	}

	public void setExploded(boolean exploded) {
		pkg.setExploded(exploded);
	}

	public void setManifest(IFile manifestFile) {
		pkg.setManifest(manifestFile);
	}

	public void setName(String name) {
		pkg.setName(name);
	}

	public void setPackageType(IPackageType type) {
		pkg.setPackageType(type);
	}
	
	public IPackageReference createReference (boolean topLevel) {
		return this;
	}
	
	public String toString() {
		return pkg.toString() + "(ref)";
	}
	
}
