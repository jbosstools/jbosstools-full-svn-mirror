package org.jboss.ide.eclipse.packages.ui.providers;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageFolder;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.ui.PackagesSharedImages;
import org.jboss.ide.eclipse.packages.ui.PackagesUIPlugin;
import org.jboss.ide.eclipse.packages.ui.PrefsInitializer;

public class PackagesLabelProvider implements ILabelProvider {
	
	
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
		if( element instanceof IProject ) 
			return PlatformUI.getWorkbench().getSharedImages().getImage(org.eclipse.ui.ide.IDE.SharedImages.IMG_OBJ_PROJECT);
		if( element instanceof IPackageNode ) {
			IPackageNode node = (IPackageNode) element;
			if (node != null) {
				switch (node.getNodeType()) {
					case IPackageNode.TYPE_PACKAGE: {
						IPackage pkg = (IPackage) node;
						if (!pkg.isExploded())
							return PackagesSharedImages.getImage(PackagesSharedImages.IMG_PACKAGE);
						else
							return PackagesSharedImages.getImage(PackagesSharedImages.IMG_PACKAGE_EXPLODED);
					}
					case IPackageNode.TYPE_PACKAGE_FOLDER: return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
					case IPackageNode.TYPE_PACKAGE_FILESET: {
						return PackagesSharedImages.getImage(PackagesSharedImages.IMG_MULTIPLE_FILES);
					}
				}
			}

		}
		return null;
	}

	private String internalGetText(Object element) {
		element = unwrapElement(element);
		if( element instanceof IProject) 
			return ((IProject)element).getName();
		if( element instanceof IPackageNode ) {
			switch (((IPackageNode)element).getNodeType()) {
				case IPackageNode.TYPE_PACKAGE: return getPackageText((IPackage)element);
				case IPackageNode.TYPE_PACKAGE_FOLDER: return getPackageFolderText((IPackageFolder)element);
				case IPackageNode.TYPE_PACKAGE_FILESET: return getPackageFileSetText((IPackageFileSet)element);
			}

		}
		return element.toString();
	}
	
	
	private String getPackageFolderText (IPackageFolder folder) {
		return folder.getName();
	}
	private String getPackageText (IPackage pkg) {
		String text = pkg.getName();
		if (PackagesUIPlugin.getDefault().getPreferenceStore().getBoolean(
			PrefsInitializer.PREF_SHOW_PACKAGE_OUTPUT_PATH)) {
			text += " [" + pkg.getDestinationPath() + "]";
		}
		return text;
	}

	private String getPackageFileSetText (IPackageFileSet fileset) {
		boolean showFullPath = PackagesUIPlugin.getDefault().getPreferenceStore().getBoolean(
				PrefsInitializer.PREF_SHOW_FULL_FILESET_ROOT_DIR);
		String text = "";
		if (fileset.getIncludesPattern() != null)
			text += fileset.getIncludesPattern() + ": ";
		
		if (showFullPath) {
			text += fileset.getSourcePath().toString();
		} else {
			text += fileset.getSourcePath().lastSegment();
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
