package org.jboss.ide.eclipse.archives.ui.util;

import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFolder;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveFileSetImpl;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveFolderImpl;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveImpl;

public class PackageNodeFactory {
	public static IArchive createPackage() {
		return new ArchiveImpl();
	}
	
	public static IArchiveFileSet createFileset() {
		return new ArchiveFileSetImpl();
	}
	
	public static IArchiveFolder createFolder() {
		return new ArchiveFolderImpl();
	}
}
