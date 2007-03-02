package org.jboss.ide.eclipse.packages.ui.providers;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageFolder;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.ui.PackagesUIMessages;
import org.jboss.ide.eclipse.packages.ui.PackagesUIPlugin;
import org.jboss.ide.eclipse.packages.ui.providers.PackagesContentProvider.FileSetProperty;

public class PackagesLabelProvider implements ILabelProvider {

	public Image getImage(Object element) {
		Image image = internalGetImage(element);
		
		if (image != null)
		{
			image = PlatformUI.getWorkbench().getDecoratorManager().decorateImage(image, element);
		}
		
		return image;
	}
	
	public String getText(Object element) {
		String text = internalGetText(element);
		
		if (text != null)
		{
			text = PlatformUI.getWorkbench().getDecoratorManager().decorateText(text, element);
		}
		return text;
	}
	
	private Image internalGetImage(Object element) {
		if (element instanceof IProject)
		{
			return PlatformUI.getWorkbench().getSharedImages().getImage(IDE.SharedImages.IMG_OBJ_PROJECT);
		}
		else if (element instanceof IPackageNode)
		{
			IPackageNode node = (IPackageNode) element;
			switch (node.getNodeType())
			{
				case IPackageNode.TYPE_PACKAGE: {
					IPackage pkg = (IPackage) node;
					if (!pkg.isExploded())
						return PackagesUIPlugin.getImage(PackagesUIPlugin.IMG_PACKAGE);
					else
						return PackagesUIPlugin.getImage(PackagesUIPlugin.IMG_PACKAGE_EXPLODED);
				}
				case IPackageNode.TYPE_PACKAGE_FOLDER: return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
				case IPackageNode.TYPE_PACKAGE_FILESET: {
					IPackageFileSet fileset = (IPackageFileSet) node;
					if (fileset.isSingleFile())
						return PackagesUIPlugin.getImage(PackagesUIPlugin.IMG_SINGLE_FILE);
					else
						return PackagesUIPlugin.getImage(PackagesUIPlugin.IMG_MULTIPLE_FILES);
				}
			}
		}
		else if (element instanceof FileSetProperty)
		{
			FileSetProperty prop = (FileSetProperty)element;
			if (PackagesContentProvider.FILESET_PROP_DEST_FILE.equals(prop.getName())
				|| PackagesContentProvider.FILESET_PROP_FILE.equals(prop.getName()))
			{
				return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
			}
			else if (PackagesContentProvider.FILESET_PROP_EXCLUDES.equals(prop.getName()))
			{
				return PackagesUIPlugin.getDefault().getImageRegistry().get(PackagesUIPlugin.IMG_EXCLUDES);
			}
			else if (PackagesContentProvider.FILESET_PROP_INCLUDES.equals(prop.getName()))
			{
				return PackagesUIPlugin.getDefault().getImageRegistry().get(PackagesUIPlugin.IMG_INCLUDES);
			}
			else if (PackagesContentProvider.FILESET_PROP_SRC_CONTAINER.equals(prop.getName())
				|| PackagesContentProvider.FILESET_PROP_DIR.equals(prop.getName()))
			{
				return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
			}
			else if (PackagesContentProvider.FILESET_PROP_SRC_PROJECT.equals(prop.getName()))
			{
				return PlatformUI.getWorkbench().getSharedImages().getImage(IDE.SharedImages.IMG_OBJ_PROJECT);
			}
		}
		return null;
	}

	private String internalGetText(Object element) {
		if (element instanceof IProject)
		{
			return ((IProject)element).getName();
		}
		else if (element instanceof IPackageNode)
		{
			IPackageNode node = (IPackageNode) element;
			switch (node.getNodeType())
			{
				case IPackageNode.TYPE_PACKAGE: return getPackageText((IPackage)element);
				case IPackageNode.TYPE_PACKAGE_FOLDER: return getPackageFolderText((IPackageFolder)element);
				case IPackageNode.TYPE_PACKAGE_FILESET: return getPackageFileSetText((IPackageFileSet)element);
			}
		}
		else if (element instanceof FileSetProperty)
		{
			return getFileSetPropertyText((FileSetProperty)element);
		}
		return "";
	}
	
	private String getPackageText (IPackage pkg)
	{
		String text = pkg.getName();
		if (PackagesUIPlugin.getDefault().getPreferenceStore().getBoolean(
			PackagesUIPlugin.PREF_SHOW_PACKAGE_OUTPUT_PATH))
		{
			if (pkg.isDestinationInWorkspace()) {
				text += " [" + pkg.getDestinationContainer().getFullPath() + "]";
			} else {
				text += " [" + pkg.getDestinationPath() + "]";
			}
			
		}
		return text;
	}
	
	private String getPackageFolderText (IPackageFolder folder)
	{
		return folder.getName();
	}
	
	private String getPackageFileSetText (IPackageFileSet fileset)
	{
		if (fileset.isSingleFile())
		{
			return fileset.getFile().getProjectRelativePath().toString();
		}
		else
		{
			boolean showFullPath = PackagesUIPlugin.getDefault().getPreferenceStore().getBoolean(
				PackagesUIPlugin.PREF_SHOW_FULL_FILESET_ROOT_DIR);
			
			if (fileset.isInWorkspace())
			{
				String text = "";
				if (fileset.getIncludesPattern() != null)
					text += fileset.getIncludesPattern() + ": ";
				
				if (showFullPath)
				{
					text += fileset.getSourceContainer().getFullPath().toString();
				} else {
					text += fileset.getSourceContainer().getName();
				}
				
				return text;
			} else {
				String text = "";
				if (fileset.getIncludesPattern() != null)
					text += fileset.getIncludesPattern() + ": ";
				
				if (showFullPath)
				{
					text += fileset.getSourcePath().toString();
				} else {
					text += fileset.getSourcePath().lastSegment();
				}
				
				return text;
			}
		}
		
	}
	
	private String getFileSetPropertyText (FileSetProperty property)
	{
		if (PackagesContentProvider.FILESET_PROP_FILE.equals(property.getName()))
		{
			return PackagesUIMessages.PackagesLabelProvider_filesetProperty_file + property.getFileSet().getFile().getProjectRelativePath().toString();
		}
		else if (PackagesContentProvider.FILESET_PROP_DEST_FILE.equals(property.getName()))
		{
			return PackagesUIMessages.PackagesLabelProvider_filesetProperty_destination + property.getFileSet().getDestinationFilename();
		}
		else if (PackagesContentProvider.FILESET_PROP_EXCLUDES.equals(property.getName()))
		{
			return PackagesUIMessages.PackagesLabelProvider_filesetProperty_excludes + property.getFileSet().getExcludesPattern();
		}
		else if (PackagesContentProvider.FILESET_PROP_INCLUDES.equals(property.getName()))
		{
			return PackagesUIMessages.PackagesLabelProvider_filesetProperty_includes + property.getFileSet().getIncludesPattern();
		}
		else if (PackagesContentProvider.FILESET_PROP_SRC_CONTAINER.equals(property.getName()))
		{
			return PackagesUIMessages.PackagesLabelProvider_filesetProperty_sourceFolder + property.getFileSet().getSourceContainer().getProjectRelativePath().toString();
		}
		else if (PackagesContentProvider.FILESET_PROP_SRC_PROJECT.equals(property.getName()))
		{
			return PackagesUIMessages.PackagesLabelProvider_filesetProperty_project + property.getFileSet().getSourceProject().getName();
		}
		else if (PackagesContentProvider.FILESET_PROP_DIR.equals(property.getName()))
		{
			return PackagesUIMessages.PackagesLabelProvider_filesetProperty_dir + property.getFileSet().getSourcePath().toString();
		}
		return "";
	}

	public void addListener(ILabelProviderListener listener) {}

	public void dispose() {	}

	public boolean isLabelProperty(Object element, String property) {
		return true;
	}

	public void removeListener(ILabelProviderListener listener) {	}

}
