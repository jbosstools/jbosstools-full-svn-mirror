package org.jboss.ide.eclipse.archives.ui.providers;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFolder;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.ui.ArchivesSharedImages;
import org.jboss.ide.eclipse.archives.ui.PrefsInitializer;
import org.jboss.ide.eclipse.archives.ui.providers.ArchivesContentProvider.WrappedProject;

public class ArchivesLabelProvider implements ILabelProvider {
	
	
	/*
	 * Important snippets to save
	 * image = PlatformUI.getWorkbench().getDecoratorManager().decorateImage(image, element);
	 * text = PlatformUI.getWorkbench().getDecoratorManager().decorateText(text, element);
	 */
	
	public Image getImage(Object element) {
		Image image = internalGetImage(element);
		
		if (image != null) {
			image = PlatformUI.getWorkbench().getDecoratorManager().decorateImage(image, element);
		}
		
		return image;
	}
	
	public String getText(Object element) {
		String text = internalGetText(element);
		
		if (text != null) {
			text = PlatformUI.getWorkbench().getDecoratorManager().decorateText(text, element);
		}
		return text;
	}
	
	private Image internalGetImage(Object element) {
		element = unwrapElement(element);
		if( element instanceof WrappedProject ) 
			return PlatformUI.getWorkbench().getSharedImages().getImage(org.eclipse.ui.ide.IDE.SharedImages.IMG_OBJ_PROJECT);
		if( element instanceof IArchiveNode ) {
			IArchiveNode node = (IArchiveNode) element;
			if (node != null) {
				switch (node.getNodeType()) {
					case IArchiveNode.TYPE_ARCHIVE: {
						IArchive pkg = (IArchive) node;
						if (!pkg.isExploded())
							return ArchivesSharedImages.getImage(ArchivesSharedImages.IMG_PACKAGE);
						else
							return ArchivesSharedImages.getImage(ArchivesSharedImages.IMG_PACKAGE_EXPLODED);
					}
					case IArchiveNode.TYPE_ARCHIVE_FOLDER: return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
					case IArchiveNode.TYPE_ARCHIVE_FILESET: {
						return ArchivesSharedImages.getImage(ArchivesSharedImages.IMG_MULTIPLE_FILES);
					}
				}
			}

		}
		return null;
	}

	private String internalGetText(Object element) {
		element = unwrapElement(element);
		if( element instanceof WrappedProject) 
			return ((WrappedProject)element).getProject().getName();
		if( element instanceof IArchiveNode ) {
			switch (((IArchiveNode)element).getNodeType()) {
				case IArchiveNode.TYPE_ARCHIVE: return getPackageText((IArchive)element);
				case IArchiveNode.TYPE_ARCHIVE_FOLDER: return getPackageFolderText((IArchiveFolder)element);
				case IArchiveNode.TYPE_ARCHIVE_FILESET: return getPackageFileSetText((IArchiveFileSet)element);
			}

		}
		return element.toString();
	}
	
	
	private String getPackageFolderText (IArchiveFolder folder) {
		return folder.getName();
	}
	private String getPackageText (IArchive pkg) {
		String text = pkg.getName();
		if (PrefsInitializer.getBoolean( PrefsInitializer.PREF_SHOW_PACKAGE_OUTPUT_PATH)) {
			text += " [" + pkg.getDestinationPath() + "]";
		}
		return text;
	}

	private String getPackageFileSetText (IArchiveFileSet fileset) {
		boolean showFullPath = PrefsInitializer.getBoolean(
				PrefsInitializer.PREF_SHOW_FULL_FILESET_ROOT_DIR);
		String text = "";
		if (fileset.getIncludesPattern() != null)
			text += fileset.getIncludesPattern() + ": ";
		
		if (showFullPath) {
			text += fileset.getGlobalSourcePath().toString();
		} else {
			text += fileset.getGlobalSourcePath().lastSegment();
		}
		
		return text;
	}


	
	protected Object unwrapElement(Object element) {
		return element; // to be used if we wrap everything for preferences
	}
	
	
	public void addListener(ILabelProviderListener listener) {}

	public void dispose() {	}

	public boolean isLabelProperty(Object element, String property) {
		return true;
	}

	public void removeListener(ILabelProviderListener listener) {	}

}
