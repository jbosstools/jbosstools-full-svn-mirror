package org.jboss.ide.eclipse.archives.ui.util.composites;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFolder;
import org.jboss.ide.eclipse.archives.ui.ArchivesSharedImages;
import org.jboss.ide.eclipse.archives.ui.ArchivesUIMessages;
import org.jboss.ide.eclipse.archives.ui.dialogs.ArchiveNodeDestinationDialog;
import org.jboss.ide.eclipse.archives.ui.util.DestinationChangeListener;

public class ArchiveNodeDestinationComposite extends Composite {

	protected Composite parent;
	protected Label destinationImage;
	protected Text destinationText;
	protected Button destinationBrowseButton;
	protected Object nodeDestination;
	protected boolean editable;
	protected ArrayList listeners;
	
	public ArchiveNodeDestinationComposite(Composite parent, int style, Object destination) {
		super(parent, style);
		this.parent = parent;
		this.nodeDestination = destination;
		this.editable = true;
		this.listeners = new ArrayList();
		
		createComposite();
	}
	
	protected void createComposite() {
		setLayout(new FormLayout());
		
		// create widgets
		destinationImage = new Label(this, SWT.NONE);
		destinationText = new Text(this, SWT.BORDER);
		Composite browseComposite = new Composite(this, SWT.NONE);
		
		// set up their layout positioning
		destinationImage.setLayoutData(createFormData(0,5,null, 0, 0, 0, null, 0));
		destinationText.setLayoutData(createFormData(0, 5, null, 0, destinationImage, 5, destinationImage, 205));
	
		
		// set text, add listeners, etc
		destinationText.setEditable(false);

		browseComposite.setLayout(new FillLayout());
		browseComposite.setLayoutData(createFormData(0,0,null,0,destinationText,5,100,-5)); 
		fillBrowseComposite(browseComposite);
		
		// call other functions required for startup
		updateDestinationViewer();
	}
	
	protected void fillBrowseComposite(Composite browseComposite) {
		destinationBrowseButton = new Button(browseComposite, SWT.PUSH); 
		destinationBrowseButton.setText(ArchivesUIMessages.PackageNodeDestinationComposite_destinationBrowseButton_label);
		destinationBrowseButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				openDestinationDialog();
			}
		});
		destinationBrowseButton.setEnabled(editable);
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
	
	protected void openDestinationDialog ()
	{
		ArchiveNodeDestinationDialog dialog = new ArchiveNodeDestinationDialog(getShell(), nodeDestination, true, true);
		if (nodeDestination != null)
			dialog.setInitialSelection(nodeDestination);
		
		int response = dialog.open();
		if (response == Dialog.OK)
		{
			Object object = dialog.getResult()[0];
			nodeDestination = object;
			
			updateDestinationViewer();
			fireDestinationChanged();
		}
	}
	
	public void setPackageNodeDestination (Object destination)
	{
		nodeDestination = destination;
		updateDestinationViewer();
	}
	
	protected void setDestinationImage (Image image)
	{
		destinationImage.setImage(image);
	}
	
	protected void setDestinationText (String text)
	{
		destinationText.setText(text);
	}
	
	protected void updateDestinationViewer ()
	{
		if (nodeDestination == null) return;
		destinationText.setText("");
		
		if (nodeDestination instanceof IArchive)
		{
			IArchive pkg = (IArchive) nodeDestination;
			
			if (pkg.isTopLevel())
			{
				setDestinationText(pkg.getName());
			} else {
				setDestinationText(pkg.getRootArchiveRelativePath().toOSString());
			}
			if (pkg.isExploded()) {
				setDestinationImage(ArchivesSharedImages.getImage(ArchivesSharedImages.IMG_PACKAGE_EXPLODED));
			} else {
				setDestinationImage(ArchivesSharedImages.getImage(ArchivesSharedImages.IMG_PACKAGE));
			}
		}
		else if (nodeDestination instanceof IArchiveFolder)
		{
			IArchiveFolder folder = (IArchiveFolder) nodeDestination;
			setDestinationText(folder.getRootArchiveRelativePath().toString());
			setDestinationImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
		}
		else if (nodeDestination instanceof IProject)
		{
			IProject project = (IProject) nodeDestination;
			setDestinationText(project.getName());
			setDestinationImage(PlatformUI.getWorkbench().getSharedImages().getImage(IDE.SharedImages.IMG_OBJ_PROJECT));
		}
		else if (nodeDestination instanceof IFolder)
		{
			IFolder folder = (IFolder) nodeDestination;
			setDestinationText("/" + folder.getProject().getName() + "/" + folder.getProjectRelativePath().toString());
			setDestinationImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
		}
	}
	
	public Object getPackageNodeDestination ()
	{
		return nodeDestination;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
		if (destinationBrowseButton != null)
		{
			destinationBrowseButton.setEnabled(editable);
		}
	}
	
	public void addDestinationChangeListener (DestinationChangeListener listener)
	{
		listeners.add(listener);
	}
	
	public void removeDestinationChangeListener (DestinationChangeListener listener)
	{
		listeners.remove(listener);
	}
	
	private void fireDestinationChanged ()
	{
		for (Iterator iter = listeners.iterator(); iter.hasNext(); )
		{
			DestinationChangeListener listener = (DestinationChangeListener) iter.next();
			listener.destinationChanged(nodeDestination);
		}
	}
}
