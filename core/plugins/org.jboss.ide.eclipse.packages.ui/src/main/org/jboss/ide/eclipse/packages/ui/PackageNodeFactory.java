package org.jboss.ide.eclipse.packages.ui;

import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageFolder;
import org.jboss.ide.eclipse.packages.core.model.internal.PackageFileSetImpl;
import org.jboss.ide.eclipse.packages.core.model.internal.PackageFolderImpl;
import org.jboss.ide.eclipse.packages.core.model.internal.PackageImpl;

public class PackageNodeFactory {
	public static IPackage createPackage() {
		return new PackageImpl();
	}
	
	public static IPackageFileSet createFileset() {
		return new PackageFileSetImpl();
	}
	
	public static IPackageFolder createFolder() {
		return new PackageFolderImpl();
	}
}
