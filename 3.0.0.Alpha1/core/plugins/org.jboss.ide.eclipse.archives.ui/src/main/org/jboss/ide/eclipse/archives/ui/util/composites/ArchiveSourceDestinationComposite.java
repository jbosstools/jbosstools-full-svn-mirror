package org.jboss.ide.eclipse.archives.ui.util.composites;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.IStringVariable;
import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.INamedContainerArchiveNode;
import org.jboss.ide.eclipse.archives.ui.ArchivesSharedImages;
import org.jboss.ide.eclipse.archives.ui.PackagesUIPlugin;

public class ArchiveSourceDestinationComposite extends Composite {
	private Text text;
	private Label pathImage, translatedPath, translatedPathImage;
	private Button workspaceButton, filesystemButton, variablesButton,
			wsRadioButton, fsRadioButton;
	
	private String projectName;
	private boolean workspaceRelative = false;
	private IArchiveNode destinationNode;
	private String path;
	private boolean error;
	private String errorString;
	private ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();

	public ArchiveSourceDestinationComposite(Composite parent, String project) {
		super(parent, SWT.NONE);
		this.projectName = project;
		setLayout(new FormLayout());
		createWidgets();
		layoutWidgets();
		setWidgetData();
		addListeners();
	}
	
	protected void createWidgets() {
		text = new Text(this, SWT.SINGLE | SWT.BORDER);
		pathImage = new Label(this, SWT.NONE);
		translatedPath = new Label(this, SWT.NONE);
		translatedPathImage = new Label(this, SWT.NONE);
		workspaceButton = new Button(this, SWT.PUSH);
		filesystemButton = new Button(this, SWT.PUSH);
		variablesButton = new Button(this, SWT.PUSH);
		wsRadioButton = new Button(this, SWT.RADIO);
		fsRadioButton = new Button(this, SWT.RADIO);
	}
	
	protected void layoutWidgets() {
		pathImage.setLayoutData(createFormData(0,0,null,0,0,0,null,0));
		text.setLayoutData(createFormData(0,0,null,0,pathImage,5,100,0));
		filesystemButton.setLayoutData(createFormData(text,5,null,0,null,0,100,-5));
		workspaceButton.setLayoutData(createFormData(text,5,null,0,null,0,filesystemButton,-5));
		variablesButton.setLayoutData(createFormData(text,5,null,0,null,0,workspaceButton,-5));
		fsRadioButton.setLayoutData(createFormData(text,5,null,0,null,0,variablesButton,-5));
		wsRadioButton.setLayoutData(createFormData(text,5,null,0,null,0,fsRadioButton,-5));
		translatedPathImage.setLayoutData(createFormData(filesystemButton,5,null,0,0,0,0,20));
		translatedPath.setLayoutData(createFormData(filesystemButton,5,null,0,translatedPathImage,5,100,-5));
	}
	
	protected void setWidgetData() {
		filesystemButton.setText("Filesystem...");
		workspaceButton.setText("Workspace...");
		variablesButton.setText("Variables...");
		wsRadioButton.setText("Workspace Relative");
		fsRadioButton.setText("Filesystem Relative");
		pathImage.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
		FontData[] translatedPathData = translatedPath.getFont().getFontData();
		for( int i = 0; i < translatedPathData.length; i++ )
			translatedPathData[i].setHeight(7);
		Font newFont = new Font(Display.getDefault(), translatedPathData);
		translatedPath.setFont(newFont);
		translatedPathImage.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK));
	}
	
	protected void addListeners() {
		text.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {
				destinationNode = null;
				path = text.getText();
				textModified(); } });
		
		// selection listeners
		filesystemButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {}
			public void widgetSelected(SelectionEvent e) {
				filesystemButtonPressed();} });
		workspaceButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {}
			public void widgetSelected(SelectionEvent e) {
				workspaceButtonPressed();} });
		variablesButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {}
			public void widgetSelected(SelectionEvent e) {
				variablesButtonPressed();} });
		fsRadioButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {}
			public void widgetSelected(SelectionEvent e) {
				fsRadioButtonPressed();} });
		wsRadioButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {}
			public void widgetSelected(SelectionEvent e) {
				wsRadioButtonPressed();} });
		
	}
	protected void textModified() {validateAndUpdateWidgets();}
	protected void filesystemButtonPressed() {browseFilesystem();}
	protected void workspaceButtonPressed() {openDestinationDialog();}
	protected void variablesButtonPressed() {variablesPressed();}
	protected void fsRadioButtonPressed() {radioPressed(fsRadioButton);}
	protected void wsRadioButtonPressed() {radioPressed(wsRadioButton);}
	
	protected void variablesPressed() {
		StringVariableSelectionDialog d = new StringVariableSelectionDialog(Display.getDefault().getActiveShell());
		if(d.open() == Window.OK) {
			Object o = d.getFirstResult();
			if( o != null && o instanceof IStringVariable) {
				destinationNode = null;
				path = path + "${" + ((IStringVariable)o).getName() + "}";
				validateAndUpdateWidgets();;
			}
		}
	}
	
	protected void openDestinationDialog() {
		ArchiveNodeDestinationDialog dialog = new ArchiveNodeDestinationDialog(getShell(), true, true);
		if( dialog.open() == Dialog.OK ) {
			Object result = dialog.getResult()[0];
			if( result instanceof IArchiveNode ) {
				destinationNode = (IArchiveNode)result;
				path = null;
				workspaceRelative = true;
			} else if( result instanceof IContainer ) {
				destinationNode = null;
				IPath tmpPath = ((IContainer)result).getFullPath();
				if( tmpPath.segment(0).equals(projectName))
					path = tmpPath.removeFirstSegments(1).makeRelative().toString();
				else
					path = ((IContainer)result).getFullPath().makeAbsolute().toString();
				workspaceRelative = true;
			}
			validateAndUpdateWidgets();
		}
	}
	
	protected void browseFilesystem () {
		DirectoryDialog dialog = new DirectoryDialog(getShell());
		String currentPath = null; 
		try {
			currentPath = getTranslatedGlobalPath();
		} catch(CoreException ce){/* ignore */}

		if (currentPath != null && currentPath.length() > 0 ) {
			dialog.setFilterPath(currentPath);
		}
		
		String path = dialog.open();
		if( path != null ) {
			destinationNode = null;
			this.path = path;
			workspaceRelative = false;
			validateAndUpdateWidgets();
		}
	}
	
	protected void radioPressed(Button button) {
		workspaceRelative = button == wsRadioButton;
		validateAndUpdateWidgets();
	}
	
	protected void validateAndUpdateWidgets() {
		// clear old status
		error = false;
		errorString = null;
		
		
		wsRadioButton.setEnabled(destinationNode == null);
		fsRadioButton.setEnabled(destinationNode == null);
		wsRadioButton.setSelection(destinationNode == null && workspaceRelative);
		fsRadioButton.setSelection(destinationNode == null && !workspaceRelative);

		Image image = (destinationNode == null ? 
				PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER) : 
				ArchivesSharedImages.getImage(ArchivesSharedImages.IMG_PACKAGE));
		pathImage.setImage(image);
		text.setText(destinationNode == null ? 
				(path == null ? "" : path) : ((INamedContainerArchiveNode)destinationNode).getName());

		String translated;
		Image img=null;
		try { 
			if( destinationNode != null ) {
				translated=""; img=null;
			} else {
				translated = getTranslatedGlobalPath();
				if( !new Path(translated).toFile().exists()) {
					translated=translated + " does not exist in the filesystem.";
					img = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_WARN_TSK);
				} else {
					img = null;
				}
			}
		} catch( CoreException ce ) { 
			translated = ce.getMessage();
			if( ce.getStatus().getSeverity() == IStatus.ERROR) {
				img = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
				error = true;
				errorString = ce.getMessage();
			} else if( ce.getStatus().getSeverity() == IStatus.WARNING)
				img = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_WARN_TSK);
		}
		translatedPathImage.setImage(img);
		translatedPath.setText(translated);
		fireChange();
	}

	protected String getTranslatedGlobalPathOrError() {
		try {
			String postSub = ArchivesCore.getInstance().getVFS().
								performStringSubstitution(path, projectName, true);
			if( workspaceRelative ) { 
				IPath p = ArchivesCore.getInstance().getVFS().workspacePathToAbsolutePath(new Path(postSub));
				if( p != null ) return p.toString();
				return "Unable to convert workspace path into global path: " + postSub;
			}
			return postSub;
		} catch( CoreException e ) { 
			return "Error during string substitution: " + e.getMessage();
		}
		
	}

	protected String getTranslatedGlobalPath() throws CoreException {
		try {
			String postSub = ArchivesCore.getInstance().getVFS().
								performStringSubstitution(path, projectName, true);
			if( workspaceRelative ) { 
				IPath p2 = new Path(postSub);
				if( !p2.isAbsolute())
					p2 = new Path(projectName).append(p2).makeAbsolute();
				IPath p = ArchivesCore.getInstance().getVFS().workspacePathToAbsolutePath(p2);
				if( p != null ) return p.toString();
				String ERROR ="Unable to convert workspace path into global path: " + postSub; 
				Status s = new Status(IStatus.WARNING, PackagesUIPlugin.PLUGIN_ID, ERROR);
				throw new CoreException(s);
			}
			return postSub;
		} catch( CoreException e ) {
			String ERROR = "Error during string substitution: " + e.getMessage();
			Status s = new Status(IStatus.ERROR, PackagesUIPlugin.PLUGIN_ID, ERROR, e);
			throw new CoreException(s);
		}
	}
	
	private FormData createFormData(Object topStart, int topOffset,
			Object bottomStart, int bottomOffset, Object leftStart,
			int leftOffset, Object rightStart, int rightOffset) {
		FormData data = new FormData();

		if (topStart != null) {
			data.top = topStart instanceof Control ? new FormAttachment(
					(Control) topStart, topOffset) : new FormAttachment(
					((Integer) topStart).intValue(), topOffset);
		}

		if (bottomStart != null) {
			data.bottom = bottomStart instanceof Control ? new FormAttachment(
					(Control) bottomStart, bottomOffset) : new FormAttachment(
					((Integer) bottomStart).intValue(), bottomOffset);
		}

		if (leftStart != null) {
			data.left = leftStart instanceof Control ? new FormAttachment(
					(Control) leftStart, leftOffset) : new FormAttachment(
					((Integer) leftStart).intValue(), leftOffset);
		}

		if (rightStart != null) {
			data.right = rightStart instanceof Control ? new FormAttachment(
					(Control) rightStart, rightOffset) : new FormAttachment(
					((Integer) rightStart).intValue(), rightOffset);
		}

		return data;
	}

	
	
	// APIs
	public void init(IArchiveNode dest) {
		destinationNode = dest;
		path = null;
		workspaceRelative = true;
		validateAndUpdateWidgets();
	}
	
	public void init(String path, boolean workspaceRelative) {
		this.path = path;
		this.workspaceRelative = workspaceRelative;
		this.destinationNode = null;
		validateAndUpdateWidgets();
	}
	
	public boolean isValid() {
		return !error;
	}
	
	public String getErrorMessage() {
		return errorString;
	}
	
	public boolean isWorkspaceRelative() {
		return workspaceRelative;
	}

	public IArchiveNode getDestinationNode() {
		return destinationNode;
	}

	public String getPath() {
		return path;
	}
	
	public String getTranslatedPath() {
		try {
			return ArchivesCore.getInstance().getVFS().
				performStringSubstitution(path, projectName, true);
		} catch( CoreException ce ) {}
		return null;
	}
	
	
	public static interface ChangeListener {
		public void compositeChanged();
	}
	public void addChangeListener (ChangeListener listener) {
		listeners.add(listener);
	}
	
	public void removeChangeListener (ChangeListener listener) {
		listeners.remove(listener);
	}
	
	private void fireChange() {
		for (Iterator<ChangeListener> iter = listeners.iterator(); iter.hasNext(); ) {
			((ChangeListener) iter.next()).compositeChanged();
		}
	}
}
