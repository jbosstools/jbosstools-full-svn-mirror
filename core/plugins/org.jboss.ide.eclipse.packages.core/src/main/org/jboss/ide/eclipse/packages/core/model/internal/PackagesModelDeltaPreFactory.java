package org.jboss.ide.eclipse.packages.core.model.internal;

import java.util.HashMap;
import java.util.Iterator;

import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageFolder;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.internal.PackagesModelDeltaImpl.NodeDelta;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbFileSet;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbFolder;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackage;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackageNodeWithProperties;

public class PackagesModelDeltaPreFactory {
	
	// children get added later
	public static PackageNodeImpl createNode(PackagesModelDeltaImpl parentDelta, PackageNodeImpl postChange, 
			HashMap attributeChanges, HashMap propertyChanges) {
		
		switch(postChange.getNodeType()) {
		case IPackageNode.TYPE_PACKAGE_FILESET:
			XbFileSet fs = createFileset((PackageFileSetImpl)postChange, attributeChanges, propertyChanges); 
			return new DeltaFileset(fs, parentDelta);
		case IPackageNode.TYPE_PACKAGE_FOLDER:
			XbFolder folder = createFolder((PackageFolderImpl)postChange, attributeChanges, propertyChanges);
			return new DeltaFolder(folder, parentDelta);
		case IPackageNode.TYPE_PACKAGE:
			XbPackage pack = createPackage((PackageImpl)postChange, attributeChanges, propertyChanges);
			return new DeltaPackage(pack, parentDelta);
		}
		
		return null;
	}
	
	
	protected static XbFileSet createFileset(PackageFileSetImpl postChange,HashMap attributeChanges, HashMap propertyChanges ) {
		XbFileSet fs = new XbFileSet((XbFileSet)postChange.nodeDelegate);
//		fs.setDir("FILESET TEST CHANGE");
		if( attributeChanges.containsKey(IPackageFileSet.INCLUDES_ATTRIBUTE))
			fs.setIncludes(getBeforeString(attributeChanges, IPackageFileSet.INCLUDES_ATTRIBUTE));
		if( attributeChanges.containsKey(IPackageFileSet.EXCLUDES_ATTRIBUTE))
			fs.setExcludes(getBeforeString(attributeChanges, IPackageFileSet.EXCLUDES_ATTRIBUTE));
		if( attributeChanges.containsKey(IPackageFileSet.SOURCE_PATH_ATTRIBUTE))
			fs.setDir(getBeforeString(attributeChanges, IPackageFileSet.SOURCE_PATH_ATTRIBUTE));
		if( attributeChanges.containsKey(IPackageFileSet.IN_WORKSPACE_ATTRIBUTE))
			fs.setInWorkspace(getBeforeBoolean(attributeChanges, IPackageFileSet.IN_WORKSPACE_ATTRIBUTE));

		undoPropertyChanges(fs, propertyChanges);
		return fs;
	}
	
	protected static XbFolder createFolder(PackageFolderImpl postChange,HashMap attributeChanges, HashMap propertyChanges ) {
		XbFolder folder = new XbFolder((XbFolder)postChange.nodeDelegate);
		if( attributeChanges.containsKey(IPackageFolder.NAME_ATTRIBUTE))
			folder.setName(getBeforeString(attributeChanges, IPackageFolder.NAME_ATTRIBUTE));
		undoPropertyChanges(folder, propertyChanges);
		return folder;
	}
	
	protected static XbPackage createPackage(PackageImpl postChange,HashMap attributeChanges, HashMap propertyChanges ) {
		XbPackage pack = new XbPackage((XbPackage)postChange.nodeDelegate);
		if( attributeChanges.containsKey(IPackage.NAME_ATTRIBUTE))
			pack.setName(getBeforeString(attributeChanges, IPackage.NAME_ATTRIBUTE));
		if( attributeChanges.containsKey(IPackage.PACKAGE_TYPE_ATTRIBUTE))
			pack.setPackageType(getBeforeString(attributeChanges, IPackage.PACKAGE_TYPE_ATTRIBUTE));
		if( attributeChanges.containsKey(IPackage.DESTINATION_ATTRIBUTE))
			pack.setToDir(getBeforeString(attributeChanges, IPackage.DESTINATION_ATTRIBUTE));
		if( attributeChanges.containsKey(IPackage.IN_WORKSPACE_ATTRIBUTE))
			pack.setInWorkspace(getBeforeBoolean(attributeChanges, IPackage.IN_WORKSPACE_ATTRIBUTE));
		if( attributeChanges.containsKey(IPackage.EXPLODED_ATTRIBUTE))
			pack.setExploded(getBeforeBoolean(attributeChanges, IPackage.EXPLODED_ATTRIBUTE));
		undoPropertyChanges(pack, propertyChanges);
		return pack;
	}
	
	protected static boolean getBeforeBoolean(HashMap map, String key) {
		NodeDelta delta = (NodeDelta)map.get(key);
		if( delta != null ) {
			return ((Boolean)delta.getBefore()).booleanValue();
		}
		return true;
	}
	protected static String getBeforeString(HashMap map, String key) {
		NodeDelta delta = (NodeDelta)map.get(key);
		if( delta != null ) {
			return (String)delta.getBefore();
		}
		return null;
	}
	
	// set the properties here to what they were before the delta
	protected static void undoPropertyChanges(XbPackageNodeWithProperties node, HashMap changes) {
		String key;
		NodeDelta val;
		for( Iterator i = changes.keySet().iterator(); i.hasNext(); ) {
			key = (String) i.next();
			val = (NodeDelta)changes.get(key);
			if( val.getBefore() == null ) {
				node.getProperties().getProperties().remove(key);
			} else {
				node.getProperties().getProperties().setProperty(key, (String)val.getBefore());
			}
		}

	}
	
	
	public static class DeltaFileset extends PackageFileSetImpl {
		// everything goes through the delegate or the parent. Simple
		private PackagesModelDeltaImpl parentDelta; 
		public DeltaFileset(XbFileSet fileset, PackagesModelDeltaImpl parentDelta){
			super(fileset);
			this.parentDelta = parentDelta;
		}
		public IPackageNode getParent() {
			return parentDelta == null ? null : parentDelta.getPreNode();
		}
	}
	
	public static class DeltaFolder extends PackageFolderImpl {
		private PackagesModelDeltaImpl parentDelta; 
		public DeltaFolder(XbFolder folder, PackagesModelDeltaImpl parentDelta){
			super(folder);
			this.parentDelta = parentDelta;
		}
		public IPackageNode getParent() {
			return parentDelta == null ? null : parentDelta.getPreNode();
		}
	}
	
	public static class DeltaPackage extends PackageImpl {
		private PackagesModelDeltaImpl parentDelta; 
		public DeltaPackage(XbPackage pack, PackagesModelDeltaImpl parentDelta){
			super(pack);
			this.parentDelta = parentDelta;
		}
		public IPackageNode getParent() {
			return parentDelta == null ? null : parentDelta.getPreNode();
		}
	}
}
