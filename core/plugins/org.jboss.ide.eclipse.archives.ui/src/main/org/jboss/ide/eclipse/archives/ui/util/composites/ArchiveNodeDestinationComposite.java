package org.jboss.ide.eclipse.archives.ui.util.composites;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
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
import org.jboss.ide.eclipse.archives.ui.util.DestinationChangeListener;

public abstract class ArchiveNodeDestinationComposite extends Composite {

	protected Composite parent;
	protected Label destinationImage;
	protected Text destinationText;
	protected Object nodeDestination;
	protected ArrayList<DestinationChangeListener> listeners;
	
	public ArchiveNodeDestinationComposite(Composite parent, int style, Object destination) {
		super(parent, style);
		this.parent = parent;
		this.nodeDestination = destination;
		this.listeners = new ArrayList<DestinationChangeListener>();
		
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
	
	protected abstract void fillBrowseComposite(Composite browseComposite);
	
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

	public void setPackageNodeDestination (Object destination) {
		nodeDestination = destination;
		updateDestinationViewer();
		fireDestinationChanged();
	}
	
	protected void updateDestinationViewer () {
		if (nodeDestination == null) return;
		destinationText.setText("");
		
		if (nodeDestination instanceof IArchive) {
			IArchive pkg = (IArchive) nodeDestination;
			String txt = pkg.isTopLevel() ? pkg.getName() : pkg.getRootArchiveRelativePath().toOSString();
			String imgKey = pkg.isExploded() ? ArchivesSharedImages.IMG_PACKAGE_EXPLODED : ArchivesSharedImages.IMG_PACKAGE;

			destinationText.setText(txt);
			destinationImage.setImage(ArchivesSharedImages.getImage(imgKey));
		} else if (nodeDestination instanceof IArchiveFolder) {
			IArchiveFolder folder = (IArchiveFolder) nodeDestination;
			destinationText.setText(folder.getRootArchiveRelativePath().toString());
			destinationImage.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
		} else if (nodeDestination instanceof IProject) {
			IProject project = (IProject) nodeDestination;
			destinationText.setText(project.getName());
			destinationImage.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(IDE.SharedImages.IMG_OBJ_PROJECT));
		} else if (nodeDestination instanceof IFolder) {
			IFolder folder = (IFolder) nodeDestination;
			destinationText.setText("/" + folder.getProject().getName() + "/" + folder.getProjectRelativePath().toString());
			destinationImage.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
		}
	}
	
	
	/**
	 * The current destination
	 * @return
	 */
	
	public Object getPackageNodeDestination () {
		return nodeDestination;
	}
	
	
	/*
	 * Destination change listeners
	 */
	
	public void addDestinationChangeListener (DestinationChangeListener listener) {
		listeners.add(listener);
	}
	
	public void removeDestinationChangeListener (DestinationChangeListener listener) {
		listeners.remove(listener);
	}
	
	private void fireDestinationChanged () {
		for (Iterator<DestinationChangeListener> iter = listeners.iterator(); iter.hasNext(); ) {
			((DestinationChangeListener) iter.next()).destinationChanged(nodeDestination);
		}
	}
}
