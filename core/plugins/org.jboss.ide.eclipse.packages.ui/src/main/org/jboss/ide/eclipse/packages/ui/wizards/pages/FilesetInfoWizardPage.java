package org.jboss.ide.eclipse.packages.ui.wizards.pages;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.PageBook;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.packages.ui.PackagesUIMessages;
import org.jboss.ide.eclipse.packages.ui.PackagesUIPlugin;
import org.jboss.ide.eclipse.packages.ui.util.PackageNodeDestinationComposite;

public class FilesetInfoWizardPage extends WizardPage {

	private IPackageFileSet fileset;
	private IPackageNode parentNode;
	private Text includesText;
	private Text excludesText;
	private TableViewer previewTable;
	private PackageNodeDestinationComposite destinationComposite;
	
	private String includes, excludes, rootDir, singleFile;
	private boolean rootDirIsWorkspaceRelative, fileIsWorkspaceRelative, isSingleFile;
	
	private IContainer rootContainer;
	private IFile workspaceFile;
	
	private Composite mainComposite;
	private Text rootDirText;
	private Label rootProjectLabel;
	private Button multipleFilesButton;
	private Button singleFileButton;
	private Button rootDirWorkspaceBrowseButton;
	private Button rootDirFilesystemBrowseButton;
	private Button fileWorkspaceBrowseButton;
	private Button fileFilesystemBrowseButton;
	private Text singleFileText;
	private PageBook filesetModePageBook;
	private Composite multipleFilesComposite;
	private Composite singleFileComposite;
	private Label singleFileProjectLabel;
	
	public FilesetInfoWizardPage (Shell parent, IPackageFileSet fileset, IPackageNode parentNode)
	{
		super(PackagesUIMessages.FilesetInfoWizardPage_new_title, PackagesUIMessages.FilesetInfoWizardPage_new_title, null);
		
		if (fileset == null) {
			setTitle(PackagesUIMessages.FilesetInfoWizardPage_new_title);
			setMessage(PackagesUIMessages.FilesetInfoWizardPage_new_message);
		} else {
			setTitle(PackagesUIMessages.FilesetInfoWizardPage_edit_title);
			setMessage(PackagesUIMessages.FilesetInfoWizardPage_edit_message);
		}
		
		this.fileset = fileset;
		this.parentNode = parentNode;
	}
		
	public void createControl (Composite parent) {
		mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(new GridLayout(1, false));
		mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Group infoGroup = new Group(mainComposite, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		infoGroup.setLayout(layout);
		infoGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		infoGroup.setText(PackagesUIMessages.FilesetInfoWizardPage_infoGroup_title);
		
		new Label(infoGroup, SWT.NONE).setText(PackagesUIMessages.FilesetInfoWizardPage_destination_label);
		destinationComposite = new PackageNodeDestinationComposite(infoGroup, SWT.NONE, parentNode);
		
		multipleFilesButton = new Button(infoGroup, SWT.RADIO | SWT.WRAP);
		multipleFilesButton.setText(PackagesUIMessages.FilesetInfoWizardPage_multipleFilesButton_label);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 3;
		multipleFilesButton.setLayoutData(data);
		multipleFilesButton.setSelection(true);
		
		singleFileButton = new Button(infoGroup, SWT.RADIO);
		singleFileButton.setText(PackagesUIMessages.FilesetInfoWizardPage_singleFileButton_label);
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 3;
		singleFileButton.setLayoutData(data);
		
		filesetModePageBook = new PageBook(infoGroup, SWT.NONE);
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 3;
		filesetModePageBook.setLayoutData(data);
		
		multipleFilesComposite = new Composite(filesetModePageBook, SWT.NONE);
		multipleFilesComposite.setLayout(new GridLayout(3, false));
		multipleFilesComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		singleFileComposite = new Composite(filesetModePageBook, SWT.NONE);
		singleFileComposite.setLayout(new GridLayout(3, false));
		singleFileComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		new Label(multipleFilesComposite, SWT.NONE).setText(PackagesUIMessages.FilesetInfoWizardPage_rootProject_label);
		Composite rootProjectComposite = new Composite(multipleFilesComposite, SWT.NONE);
		rootProjectComposite.setLayout(new GridLayout(2, false));
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		rootProjectComposite.setLayoutData(data);
		new Label(rootProjectComposite, SWT.NONE).setImage(
			PlatformUI.getWorkbench().getSharedImages().getImage(IDE.SharedImages.IMG_OBJ_PROJECT));
		rootProjectLabel = new Label(rootProjectComposite, SWT.NONE);
		rootProjectLabel.setText(PackagesUIMessages.FilesetInfoWizardPage_noProjectMessage);
		rootProjectLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		new Label(multipleFilesComposite, SWT.NONE).setText(PackagesUIMessages.FilesetInfoWizardPage_rootDirectory_label);
		rootDirText = new Text(multipleFilesComposite, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		rootDirText.setLayoutData(data);
		rootDirText.setEnabled(false);
		
		new Label(multipleFilesComposite, SWT.NONE);
		Composite browseComposite = new Composite(multipleFilesComposite, SWT.NONE);
		browseComposite.setLayout(new GridLayout(2, false));
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		data.horizontalAlignment = GridData.END;
		data.grabExcessHorizontalSpace = true;
		browseComposite.setLayoutData(data);
		
		rootDirWorkspaceBrowseButton = new Button(browseComposite, SWT.PUSH);
		rootDirWorkspaceBrowseButton.setText(PackagesUIMessages.FilesetInfoWizardPage_rootDirWorkspaceBrowseButton_label);
		rootDirFilesystemBrowseButton = new Button(browseComposite, SWT.PUSH);
		rootDirFilesystemBrowseButton.setText(PackagesUIMessages.FilesetInfoWizardPage_rootDirFilesystemBrowseButton_label);
		
		Composite includesLabelComposite = new Composite(multipleFilesComposite, SWT.NONE);
		includesLabelComposite.setLayout(new GridLayout(2, false));
		new Label(includesLabelComposite, SWT.NONE).setImage(PackagesUIPlugin.getImage(PackagesUIPlugin.IMG_INCLUDES));
		new Label(includesLabelComposite, SWT.NONE).setText(PackagesUIMessages.FilesetInfoWizardPage_includes_label);
		
		includesText = new Text(multipleFilesComposite, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		includesText.setLayoutData(data);
		
		Composite exludesLabelComposite = new Composite(multipleFilesComposite, SWT.NONE);
		exludesLabelComposite.setLayout(new GridLayout(2, false));
		new Label(exludesLabelComposite, SWT.NONE).setImage(PackagesUIPlugin.getImage(PackagesUIPlugin.IMG_EXCLUDES));
		new Label(exludesLabelComposite, SWT.NONE).setText(PackagesUIMessages.FilesetInfoWizardPage_excludes_label);
		excludesText = new Text(multipleFilesComposite, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		excludesText.setLayoutData(data);
		
		new Label(singleFileComposite, SWT.NONE).setText(PackagesUIMessages.FilesetInfoWizardPage_singleFileProject_label);
		Composite singleFileProjectComposite = new Composite(singleFileComposite, SWT.NONE);
		singleFileProjectComposite.setLayout(new GridLayout(2, false));
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		singleFileProjectComposite.setLayoutData(data);
		new Label(singleFileProjectComposite, SWT.NONE).setImage(
			PlatformUI.getWorkbench().getSharedImages().getImage(IDE.SharedImages.IMG_OBJ_PROJECT));
		singleFileProjectLabel = new Label(singleFileProjectComposite, SWT.NONE);
		singleFileProjectLabel.setText(PackagesUIMessages.FilesetInfoWizardPage_noProjectMessage);
		singleFileProjectLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		new Label(singleFileComposite, SWT.NONE).setText(PackagesUIMessages.FilesetInfoWizardPage_singleFile_label);
		singleFileText = new Text(singleFileComposite, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		singleFileText.setLayoutData(data);
		singleFileText.setEnabled(false);
		
		new Label(singleFileComposite, SWT.NONE);
		Composite fileBrowseComposite = new Composite(singleFileComposite, SWT.NONE);
		fileBrowseComposite.setLayout(new GridLayout(2, false));
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		data.horizontalAlignment = GridData.END;
		fileBrowseComposite.setLayoutData(data);
		
		fileWorkspaceBrowseButton = new Button(fileBrowseComposite, SWT.PUSH);
		fileWorkspaceBrowseButton.setText(PackagesUIMessages.FilesetInfoWizardPage_fileWorkspaceBrowseButton_label);
		fileFilesystemBrowseButton = new Button(fileBrowseComposite, SWT.PUSH);
		fileFilesystemBrowseButton.setText(PackagesUIMessages.FilesetInfoWizardPage_fileFilesystemBrowseButton_label);
		
		//Composite previewComposite = UIUtil.createExpandableComposite(mainComposite, "Preview", true);
		Group previewComposite = new Group(mainComposite, SWT.NONE);
		previewComposite.setLayout(new GridLayout(1, false));
		
		previewComposite.setText(PackagesUIMessages.FilesetInfoWizardPage_previewGroup_label);
		
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 3;
		previewComposite.setLayoutData(data);
		
		previewTable = new TableViewer(previewComposite, SWT.BORDER);
		previewTable.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		previewTable.setContentProvider(new ArrayContentProvider());
		previewTable.setLabelProvider(new ResourceLabelProvider());
		
		addListeners();
		fillDefaults();
		changePreview();
		
		includesText.setFocus();
		toggleFilesetMode();
		
		setControl(mainComposite);
	}
	
	private void addListeners ()
	{
		includesText.addModifyListener(new ModifyListener () { 
			public void modifyText(ModifyEvent e) {
				includes = includesText.getText();
				changePreview();
			}
		});
		
		excludesText.addModifyListener(new ModifyListener () { 
			public void modifyText(ModifyEvent e) {
				excludes = excludesText.getText();
				changePreview();
			}
		});
		
		multipleFilesButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				toggleFilesetMode();
			}
		});
		
		singleFileButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				toggleFilesetMode();
			}
		});
		
		rootDirWorkspaceBrowseButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				browseWorkspaceForRootDir();
			}
		});
		
		rootDirFilesystemBrowseButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				browseFilesystemForRootDir();
			}
		});
		
		fileWorkspaceBrowseButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				browseWorkspaceForFile();
			}
		});
		
		fileFilesystemBrowseButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				browseFilesystemForFile();
			}
		});
	}
	
	public IPackageNode getRootNode ()
	{
		return (IPackageNode) destinationComposite.getPackageNodeDestination();
	}
	
	public String getIncludes ()
	{
		return includes;
	}
	
	public String getExcludes ()
	{
		return excludes;
	}
	
	public String getRootDir ()
	{
		return rootDir;
	}
	
	public IContainer getWorkspaceRootDir()
	{
		return rootContainer;
	}
	
	public boolean isRootDirWorkspaceRelative ()
	{
		return rootDirIsWorkspaceRelative;
	}
	
	public boolean isFileWorkspaceRelative ()
	{
		return fileIsWorkspaceRelative;
	}
	
	public boolean isSingleFile ()
	{
		return isSingleFile;
	}
	
	public String getSingleFile ()
	{
		return singleFile;
	}
	
	public IFile getWorkspaceFile ()
	{
		return workspaceFile;
	}
	
	private void fillDefaults ()
	{
		if (fileset != null)
		{
			if (!fileset.isSingleFile())
			{
				isSingleFile = false;
				if (fileset.getIncludesPattern() != null)
					includesText.setText(fileset.getIncludesPattern());
				if (fileset.getExcludesPattern() != null)
					excludesText.setText(fileset.getExcludesPattern());
				
				if (fileset.getSourceContainer() != null) {
					rootDirText.setText(fileset.getSourceContainer().getProjectRelativePath().toString());
					rootContainer = fileset.getSourceContainer();
					rootDirIsWorkspaceRelative = true;
				}
				else if (fileset.getSourceFolder() != null) {
					rootDir = fileset.getSourceFolder().toString();
					rootDirText.setText(rootDir);
					rootContainer = null;
					rootDirIsWorkspaceRelative = false;
				}
				
				if (fileset.getSourceProject() != null) {
						rootProjectLabel.setText(fileset.getSourceProject().getName());
						singleFileProjectLabel.setText(PackagesUIMessages.FilesetInfoWizardPage_noProjectMessage);
				}
				
				filesetModePageBook.showPage(multipleFilesComposite);
				multipleFilesButton.setSelection(true);
			}
			else {
				isSingleFile = true;
				if (fileset.getSourceProject() != null) {
					rootProjectLabel.setText(PackagesUIMessages.FilesetInfoWizardPage_noProjectMessage);
					singleFileProjectLabel.setText(fileset.getSourceProject().getName());
				}
				if (fileset.getFile() != null) {
					singleFileText.setText(fileset.getFile().getProjectRelativePath().toString());
					workspaceFile = fileset.getFile();
					fileIsWorkspaceRelative = true;
				} else if (fileset.getFilePath() != null) {
					singleFileText.setText(fileset.getFilePath().toString());
					workspaceFile = null;
					fileIsWorkspaceRelative = false;
				}
				
				filesetModePageBook.showPage(singleFileComposite);
				singleFileButton.setSelection(true);
			}
		} else {
			multipleFilesButton.setEnabled(true);
		}
	}
	
	private void changePreview()
	{
		if (rootContainer != null)
		{
			IFile files[] = PackagesCore.findMatchingFiles(rootContainer, includesText.getText(), excludesText.getText());
			previewTable.setInput(files);
		} else if (rootDir != null) {
			IPath paths[] = PackagesCore.findMatchingPaths(new Path(rootDir), includesText.getText(), excludesText.getText());
			previewTable.setInput(paths);
		}
	}
	
	private void toggleFilesetMode ()
	{
		boolean multipleFiles = multipleFilesButton.getSelection();
		isSingleFile = !multipleFiles;
		
		if (multipleFiles) {
			filesetModePageBook.showPage(multipleFilesComposite);
			changePreview();
		} else {
			filesetModePageBook.showPage(singleFileComposite);
			previewTable.getTable().clearAll();
		}
		previewTable.getTable().setEnabled(multipleFiles);
	}
	
	private void browseWorkspaceForRootDir ()
	{
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(getShell(), rootContainer, true,
			PackagesUIMessages.FilesetInfoWizardPage_rootDirWorkspaceBrowser_message);
		
		int response = dialog.open();
		if (response == Dialog.OK)
		{
			Object results[] = dialog.getResult();
			IPath path = (IPath) results[0];
			String projectName = path.segment(0);
			
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
			if (project != null)
			{
				IPath relativePath = path.removeFirstSegments(1);
				if (relativePath.isEmpty()) {
					rootContainer = project;
				} else {
					rootContainer = project.getFolder(relativePath);
				}
				rootProjectLabel.setText(project.getName());
				
				rootDir = relativePath.isEmpty() ? null : relativePath.toString();
				if (!relativePath.isEmpty())
					rootDirText.setText(rootDir);
				
				rootDirIsWorkspaceRelative = true;
				
				changePreview();
			}
		}
	}
	
	private void browseFilesystemForRootDir ()
	{
		DirectoryDialog dialog = new DirectoryDialog(getShell());
		if (rootDirText.getText() != null && rootDirText.getText().length() > 0 && !isRootDirWorkspaceRelative())
		{
			dialog.setFilterPath(rootDirText.getText());
		}
		
		String path = dialog.open();
		if (path != null && path.length() > 0)
		{
			rootDirText.setText(path);
			rootDir = path;
			rootDirIsWorkspaceRelative = false;
			rootProjectLabel.setText(PackagesUIMessages.FilesetInfoWizardPage_noProjectMessage);
			rootContainer = null;
			
			changePreview();
		}
	}
	
	private void browseWorkspaceForFile ()
	{
		ElementTreeSelectionDialog dialog = 
			new ElementTreeSelectionDialog(getShell(), new WorkbenchLabelProvider(), new WorkbenchContentProvider());
		
		dialog.setAllowMultiple(false);
		dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
		dialog.setValidator(new ISelectionStatusValidator() {
			public IStatus validate(Object[] selection) {
				if (selection.length > 0 && selection[0] instanceof IFile)
					return new Status(IStatus.OK, PackagesUIPlugin.PLUGIN_ID,
							IStatus.OK, "", null); //$NON-NLS-1$
				
				return new Status(IStatus.ERROR, PackagesUIPlugin.PLUGIN_ID,
						IStatus.ERROR, "", null); //$NON-NLS-1$
			}
		});
		dialog.setTitle(PackagesUIMessages.FilesetInfoWizardPage_fileWorkspaceBrowser_title);
		dialog.setMessage(PackagesUIMessages.FilesetInfoWizardPage_fileWorkspaceBrowser_message);
		if (workspaceFile != null)
			dialog.setInitialSelection(workspaceFile);
		
		int response = dialog.open();
		if (response == Dialog.OK)
		{
			Object results[] = dialog.getResult();
			IFile file= (IFile) results[0];
			
			IProject project = file.getProject();
			if (project != null)
			{
				IPath relativePath = file.getProjectRelativePath();
				rootContainer = null;
				workspaceFile = project.getFile(relativePath);
				
				singleFileText.setText(relativePath.toString());
				singleFileProjectLabel.setText(project.getName());
				singleFile = relativePath.toString();
				
				fileIsWorkspaceRelative = true;
			}
		}
	}
	
	private void browseFilesystemForFile ()
	{
		FileDialog dialog = new FileDialog(getShell());
		if (singleFileText.getText() != null && !isFileWorkspaceRelative())
		{
			dialog.setFileName(singleFileText.getText());
		}
		
		String path = dialog.open();
		if (path != null)
		{
			singleFileText.setText(path);
			singleFileProjectLabel.setText(PackagesUIMessages.FilesetInfoWizardPage_noProjectMessage);
			singleFile = path;
			
			workspaceFile = null;
			fileIsWorkspaceRelative = false;
		}
	}
	
	private IPath getContainerRelativePath(IContainer container, IResource resource)
	{
		String path = "";
		IContainer parent = resource.getParent();
		while (parent != null)
		{
			if (parent.equals(container))
			{
				break;
			}
			path = parent.getName() + "/" + path;
			parent = parent.getParent();
		}
		
		path += (path.length() == 0 ? "" : "/") + resource.getName();
		
		return new Path(path);
	}
	
	private class ResourceLabelProvider implements ILabelProvider
	{

		public Image getImage(Object element) {
			if (element instanceof IResource)
			{
				IResource resource = (IResource) element;
				if (resource.getType() == IResource.PROJECT)
				{
					return PlatformUI.getWorkbench().getSharedImages().getImage(IDE.SharedImages.IMG_OBJ_PROJECT);
				}
				else if (resource.getType() == IResource.FOLDER)
				{
					return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
				}
				else if (resource.getType() == IResource.FILE)
				{
					return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
				}
			} else if (element instanceof IPath) {
				return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
			}
			return null;
		}

		public String getText(Object element) {
			if (element instanceof IResource)
			{
				return getContainerRelativePath(rootContainer, (IResource)element).toString();
			} else if (element instanceof IPath) {
				return ((IPath)element).toString();
			}
			return "";
		}

		public void addListener(ILabelProviderListener listener) {}

		public void dispose() {}

		public boolean isLabelProperty(Object element, String property) {
			return true;
		}

		public void removeListener(ILabelProviderListener listener) { }
		
	}
}
