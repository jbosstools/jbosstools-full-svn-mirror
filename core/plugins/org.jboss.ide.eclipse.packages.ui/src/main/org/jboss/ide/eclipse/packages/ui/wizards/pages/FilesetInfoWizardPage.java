package org.jboss.ide.eclipse.packages.ui.wizards.pages;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.ide.IDE;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.packages.ui.PackagesSharedImages;
import org.jboss.ide.eclipse.packages.ui.PackagesUIMessages;
import org.jboss.ide.eclipse.packages.ui.util.FilesetPreviewComposite;
import org.jboss.ide.eclipse.packages.ui.util.PackageNodeDestinationComposite;

public class FilesetInfoWizardPage extends WizardPage {

	private IPackageFileSet fileset;
	private IPackageNode parentNode;
	private Text includesText;
	private Text excludesText;
	private PackageNodeDestinationComposite destinationComposite;

	private String includes, excludes;
	private IPath rootDir;
	private boolean rootDirIsWorkspaceRelative, fileIsWorkspaceRelative;
	private FilesetPreviewComposite previewComposite;

	private Composite mainComposite;
	private Text rootDirText;
	private Label rootProjectLabel;
	private Button rootDirWorkspaceBrowseButton;
	private Button rootDirFilesystemBrowseButton;

	public FilesetInfoWizardPage (Shell parent, IPackageFileSet fileset, IPackageNode parentNode) {
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
		mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		mainComposite.setLayout(new FormLayout());
		Group info = createInfoGroup(mainComposite);
		createPreviewGroup(mainComposite, info);
		
		addListeners();
		fillDefaults();
		changePreview();
		
		includesText.setFocus();
		
		setControl(mainComposite);
	}
	
	private FormData createFormData(Object topStart, int topOffset, Object bottomStart, int bottomOffset, 
			Object leftStart, int leftOffset, Object rightStart, int rightOffset) {
		FormData data = new FormData();

		if( topStart != null ) {
			data.top = topStart instanceof Control ? new FormAttachment((Control)topStart, topOffset) : 
				new FormAttachment(((Integer)topStart).intValue(), topOffset);
		}

		if( bottomStart != null ) {
			data.bottom = bottomStart instanceof Control ? new FormAttachment((Control)bottomStart, bottomOffset) : 
				new FormAttachment(((Integer)bottomStart).intValue(), bottomOffset);
		}

		if( leftStart != null ) {
			data.left = leftStart instanceof Control ? new FormAttachment((Control)leftStart, leftOffset) : 
				new FormAttachment(((Integer)leftStart).intValue(), leftOffset);
		}

		if( rightStart != null ) {
			data.right = rightStart instanceof Control ? new FormAttachment((Control)rightStart, rightOffset) : 
				new FormAttachment(((Integer)rightStart).intValue(), rightOffset);
		}

		return data;
	}

	
	private Group createPreviewGroup(Composite mainComposite, Group info) {
		Group previewGroup = new Group(mainComposite, SWT.NONE);
		previewGroup.setLayoutData(createFormData(info,5,100,-5,0,5,100,-5));
		previewGroup.setLayout(new FormLayout());
		previewComposite = new FilesetPreviewComposite(previewGroup, SWT.NONE);
		previewComposite.setLayoutData(createFormData(0,0,100,0,0,0,100,0));
		previewGroup.setText(PackagesUIMessages.FilesetInfoWizardPage_previewGroup_label);
		return previewGroup;
	}
	private Group createInfoGroup(Composite mainComposite) {
		Group infoGroup = new Group(mainComposite, SWT.NONE);
		infoGroup.setText(PackagesUIMessages.FilesetInfoWizardPage_infoGroup_title);

		// positioning in parent
		infoGroup.setLayoutData(createFormData(0,5,null,0,0,5,100,-5));

		// my layout
		infoGroup.setLayout(new FormLayout());

		
		int max = 100;

		// destination row
		Label destinationKey = new Label(infoGroup, SWT.NONE);
		destinationComposite = new PackageNodeDestinationComposite(infoGroup, SWT.NONE, parentNode);

		destinationKey.setLayoutData(createFormData(0,10,null,0,null,5, 0, max));
		destinationComposite.setLayoutData(createFormData(0,5,null,0,destinationKey,5, 100, -5));
		
		
		// root project row
		Label rootProjectKey = new Label(infoGroup, SWT.NONE);
		Composite rootProjectVal = new Composite(infoGroup, SWT.NONE);
		rootProjectVal.setLayout(new FormLayout());
		Label projectImageLabel = new Label(rootProjectVal, SWT.NONE);
		rootProjectLabel = new Label(rootProjectVal, SWT.NONE);
		
		projectImageLabel.setLayoutData(createFormData(0,0,null,0,0,5, null,0));
		rootProjectLabel.setLayoutData(createFormData(0,0,null,0,projectImageLabel, 5, 100,-5));
		rootProjectKey.setLayoutData(createFormData(destinationComposite,5,null,0,null,5,0,max));
		rootProjectVal.setLayoutData(createFormData(destinationComposite,5, null, 0, destinationKey, 5, 100, -5));
		

		// root dir
		Label rootDirectoryLabel = new Label(infoGroup, SWT.NONE);
		Composite rootDirValue = new Composite(infoGroup, SWT.NONE);
		rootDirValue.setLayout(new FormLayout());
		
		rootDirText = new Text(rootDirValue, SWT.BORDER);
		rootDirWorkspaceBrowseButton = new Button(rootDirValue, SWT.PUSH);
		rootDirFilesystemBrowseButton = new Button(rootDirValue, SWT.PUSH);
		
		rootDirText.setLayoutData(createFormData(0,5,null,0,0,5,100,-5));
		rootDirFilesystemBrowseButton.setLayoutData(createFormData(rootDirText,5,null,0,null,0,100,-5));
		rootDirWorkspaceBrowseButton.setLayoutData(createFormData(rootDirText,5,null,0,null,0,rootDirFilesystemBrowseButton, -5));
		
		rootDirectoryLabel.setLayoutData(createFormData(rootProjectVal,10,null,0,null,5,0,max));
		rootDirValue.setLayoutData(createFormData(rootProjectVal,5,null,0,rootDirectoryLabel,5,100,-5));
		

		// includes composite and it's internals
		Composite includesKey = new Composite(infoGroup, SWT.NONE);
		includesKey.setLayout(new FormLayout());
		Label includesImage = new Label(includesKey, SWT.NONE);
		Label includesTextLabel = new Label(includesKey, SWT.NONE);
		includesText = new Text(infoGroup, SWT.BORDER);
		includesImage.setLayoutData(createFormData(0,0,null,0,0,0,null,0));
		includesTextLabel.setLayoutData(createFormData(0,0,null,0,includesImage,5,null,0));
		
		includesKey.setLayoutData(createFormData(rootDirValue,5,null,0,null,5,0,max));
		includesText.setLayoutData(createFormData(rootDirValue,5,null,0,includesKey,10,100,-5));

		
		// excludes composite and it's internals
		Composite excludesKey = new Composite(infoGroup, SWT.NONE);
		excludesKey.setLayout(new FormLayout());
		Label excludesImage = new Label(excludesKey, SWT.NONE);
		Label excludesTextLabel = new Label(excludesKey, SWT.NONE);
		excludesText = new Text(infoGroup, SWT.BORDER);
		excludesImage.setLayoutData(createFormData(0,0,null,0,0,0,null,0));
		excludesTextLabel.setLayoutData(createFormData(0,0,null,0,excludesImage,5,null,0));
		
		excludesKey.setLayoutData(createFormData(includesText,5,null,0,null,5,0,max));
		excludesText.setLayoutData(createFormData(includesText,5,null,0,excludesKey,10,100,-5));

		
		
		
		
		
		// customize widgets
		destinationKey.setText(PackagesUIMessages.FilesetInfoWizardPage_destination_label);
		rootProjectKey.setText(PackagesUIMessages.FilesetInfoWizardPage_rootProject_label);
		projectImageLabel.setImage(
				PlatformUI.getWorkbench().getSharedImages().getImage(IDE.SharedImages.IMG_OBJ_PROJECT));
		rootDirectoryLabel.setText(PackagesUIMessages.FilesetInfoWizardPage_rootDirectory_label);
		rootDirWorkspaceBrowseButton.setText(PackagesUIMessages.FilesetInfoWizardPage_rootDirWorkspaceBrowseButton_label);
		rootDirFilesystemBrowseButton.setText(PackagesUIMessages.FilesetInfoWizardPage_rootDirFilesystemBrowseButton_label);
		includesImage.setImage(PackagesSharedImages.getImage(PackagesSharedImages.IMG_INCLUDES));
		includesTextLabel.setText(PackagesUIMessages.FilesetInfoWizardPage_includes_label);
		excludesImage.setImage(PackagesSharedImages.getImage(PackagesSharedImages.IMG_EXCLUDES));
		excludesTextLabel.setText(PackagesUIMessages.FilesetInfoWizardPage_excludes_label);
		rootDirText.setEnabled(false);

		return infoGroup;
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
			
	}
	
	public IPackageNode getRootNode () {
		return (IPackageNode) destinationComposite.getPackageNodeDestination();
	}
	
	public String getIncludes () {
		return includes;
	}
	
	public String getExcludes () {
		return excludes;
	}
	
	public String getRootDir () {
		return rootDir.toOSString();
	}
		
	public boolean isRootDirWorkspaceRelative () {
		return rootDirIsWorkspaceRelative;
	}
	
	public boolean isFileWorkspaceRelative () {
		return fileIsWorkspaceRelative;
	}
		
	private void fillDefaults () {
		if (fileset != null) {
				if (fileset.getIncludesPattern() != null)
					includesText.setText(fileset.getIncludesPattern());
				if (fileset.getExcludesPattern() != null)
					excludesText.setText(fileset.getExcludesPattern());
				
				if (fileset.getSourcePath() != null) {
					rootDir = fileset.getSourcePath();
					rootDirText.setText(rootDir.toString());
					rootDirIsWorkspaceRelative = fileset.isInWorkspace();
				}
		} else {
			rootProjectLabel.setText(parentNode.getProject().getName());
			fileIsWorkspaceRelative = true;
			rootDirIsWorkspaceRelative = true;
			rootDir = ResourcesPlugin.getWorkspace().getRoot().getProject(parentNode.getProject().getName()).getFullPath();
		}
	}
	
	private void changePreview() {
		IPath root = isRootDirWorkspaceRelative() ? ResourcesPlugin.getWorkspace().getRoot().getLocation().append(rootDir) : rootDir;
		IPath paths[] = PackagesCore.findMatchingPaths(root, includesText.getText(), excludesText.getText());
		previewComposite.setInput(paths);
	}
	
	
	private void browseWorkspaceForRootDir () {
		IContainer workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(getShell(), workspaceRoot, true,
			PackagesUIMessages.FilesetInfoWizardPage_rootDirWorkspaceBrowser_message);
		
		int response = dialog.open();
		if (response == Dialog.OK) {
			Object results[] = dialog.getResult();
			IPath path = (IPath) results[0];
			String projectName = path.segment(0);
			
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
			if (project != null) {
				IPath relativePath = path.removeFirstSegments(1);
				rootProjectLabel.setText(project.getName());
				
				rootDir = path;
				if (!relativePath.isEmpty()) {
					rootDirText.setText(relativePath.toString());
				} else {
					rootDirText.setText("");
				}
				
				rootDirIsWorkspaceRelative = true;
				changePreview();
			}
		}
	}
	
	private void browseFilesystemForRootDir () {
		DirectoryDialog dialog = new DirectoryDialog(getShell());
		if (rootDirText.getText() != null && rootDirText.getText().length() > 0 && !isRootDirWorkspaceRelative()) {
			dialog.setFilterPath(rootDirText.getText());
		}
		
		String path = dialog.open();
		if (path != null && path.length() > 0) {
			rootDirText.setText(path);
			rootDir = new Path(path);
			rootDirIsWorkspaceRelative = false;
			rootProjectLabel.setText(PackagesUIMessages.FilesetInfoWizardPage_noProjectMessage);
			changePreview();
		}
	}
	
}
