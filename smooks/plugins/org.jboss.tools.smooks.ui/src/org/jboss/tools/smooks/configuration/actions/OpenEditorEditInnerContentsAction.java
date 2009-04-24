/**
 * 
 */
package org.jboss.tools.smooks.configuration.actions;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;

/**
 * @author DartPeng
 * 
 */
public class OpenEditorEditInnerContentsAction extends Action {

	private AnyType model;

	private int textType = SmooksUIUtils.VALUE_TYPE_TEXT;

	private String fileExtensionName = "txt";

	private String editorID = null;

	private IResource resource = null;

	private IEditorPart tempEditor = null;

	private TempFileChangeListener tempFileListener = null;

	private IPartListener tempEditorlistener = null;

	private EditingDomain editingDomain;

	private IFile tempFile;

	public OpenEditorEditInnerContentsAction(EditingDomain domain, AnyType model, int textType, String fileExtensionName, String editorID) {
		super();
		this.model = model;
		this.editingDomain = domain;
		this.textType = textType;
		this.fileExtensionName = fileExtensionName;
		this.editorID = editorID;
		tempFileListener = new TempFileChangeListener();
		 setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FILE));
		setDescription("Open an editor to edit inner contents");
		setText("Open Editor");
	}

	public OpenEditorEditInnerContentsAction(EditingDomain domain, AnyType model, int textType, String fileExtensionName) {
		this(domain, model, textType, fileExtensionName, null);
	}

	private void setContent(AnyType model, String contents, int textType) {
		String editValue = null;
		if (textType == SmooksUIUtils.VALUE_TYPE_TEXT && model instanceof AnyType) {
			editValue = SmooksModelUtils.getAnyTypeText((AnyType) model);
			if (!contents.equals(editValue)) {
				SmooksModelUtils.setTextToAnyType((AnyType) model, contents);
			}
		}
		if (textType == SmooksUIUtils.VALUE_TYPE_COMMENT && model instanceof AnyType) {
			editValue = SmooksModelUtils.getAnyTypeComment((AnyType) model);
			if (!contents.equals(editValue)) {
				SmooksModelUtils.setCommentToSmooksType(editingDomain, (AnyType) model, contents);
			}
		}
		if (textType == SmooksUIUtils.VALUE_TYPE_CDATA && model instanceof AnyType) {
			editValue = SmooksModelUtils.getAnyTypeCDATA((AnyType) model);
			if (!contents.equals(editValue)) {
				SmooksModelUtils.setCDATAToSmooksType(editingDomain, (AnyType) model, contents);
			}
		}
	}

	@Override
	public void run() {
		IWorkbenchWindow window = SmooksConfigurationActivator.getDefault().getWorkbench().getActiveWorkbenchWindow();
		try {
			if (tempFile != null && tempFile.exists()) {
				if (editorID != null) {
					FileEditorInput editorInput1 = new FileEditorInput(tempFile);
					tempEditor = window.getActivePage().openEditor(editorInput1, editorID);
				} else {
					tempEditor = IDE.openEditor(window.getActivePage(), tempFile);
				}
				return ;
			}
			if (tempEditorlistener != null) {
				SmooksConfigurationActivator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().removePartListener(
						tempEditorlistener);
			}
			if (tempFileListener != null) {
				ResourcesPlugin.getWorkspace().removeResourceChangeListener(tempFileListener);
			}
			String editValue = null;
			if (textType == SmooksUIUtils.VALUE_TYPE_TEXT && model instanceof AnyType) {
				editValue = SmooksModelUtils.getAnyTypeText((AnyType) model);
			}
			if (textType == SmooksUIUtils.VALUE_TYPE_COMMENT && model instanceof AnyType) {
				editValue = SmooksModelUtils.getAnyTypeComment((AnyType) model);
			}
			if (textType == SmooksUIUtils.VALUE_TYPE_CDATA && model instanceof AnyType) {
				editValue = SmooksModelUtils.getAnyTypeCDATA((AnyType) model);
			}
			if (editValue != null) {
				resource = SmooksUIUtils.getResource(model);
				if (resource != null) {
					IProject project = resource.getProject();
					String name = generateFileName();
					tempFile = project.getFile(name + "." + fileExtensionName);
					int i = 0;
					while (tempFile.exists()) {
						name += String.valueOf(i);
						tempFile = project.getFile(name + "." + fileExtensionName);
						i++;
					}
					tempFile.create(new ByteArrayInputStream(editValue.getBytes()), true, new NullProgressMonitor());
					tempFile.setHidden(true);
					FileEditorInput editorInput = new FileEditorInput(tempFile);
					editorInput = new FileEditorInput(tempFile);
					if (editorID != null) {
						tempEditor = window.getActivePage().openEditor(editorInput, editorID);
					} else {
						tempEditor = IDE.openEditor(window.getActivePage(), tempFile);
					}

					tempEditorlistener = new IPartListener() {

						public void partActivated(IWorkbenchPart part) {

						}

						public void partBroughtToTop(IWorkbenchPart part) {

						}

						public void partClosed(IWorkbenchPart part) {
							if (tempEditor != null && part == tempEditor) {
								SmooksConfigurationActivator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage()
										.removePartListener(tempEditorlistener);
								ResourcesPlugin.getWorkspace().removeResourceChangeListener(tempFileListener);
								try {
									tempFile.delete(true, new NullProgressMonitor());
									tempFile.refreshLocal(0, null);
									tempFile = null;
								} catch (CoreException e) {
									e.printStackTrace();
								}
							}
						}

						public void partDeactivated(IWorkbenchPart part) {

						}

						public void partOpened(IWorkbenchPart part) {

						}
					};
					SmooksConfigurationActivator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(
							tempEditorlistener);
					ResourcesPlugin.getWorkspace().addResourceChangeListener(tempFileListener);
				}
			}
		} catch (Exception e) {
			
		}
	}

	private String generateFileName() {
		int number = (int) System.currentTimeMillis();
		return "tempFile_" + String.valueOf(number);
	}

	private class TempFileChangeListener implements IResourceChangeListener {

		public void resourceChanged(IResourceChangeEvent event) {
			try {
				switch (event.getType()) {
				case IResourceChangeEvent.PRE_CLOSE:
					break;
				case IResourceChangeEvent.PRE_DELETE:
					break;
				case IResourceChangeEvent.POST_CHANGE:
					event.getDelta().accept(new DeltaPrinter(), IContainer.INCLUDE_HIDDEN);
					break;
				case IResourceChangeEvent.PRE_BUILD:
					event.getDelta().accept(new DeltaPrinter(), IContainer.INCLUDE_HIDDEN);
					break;
				case IResourceChangeEvent.POST_BUILD:
					event.getDelta().accept(new DeltaPrinter());
					break;
				}
			} catch (Throwable t) {

			}
		}
	}

	class DeltaPrinter implements IResourceDeltaVisitor {

		private String getContents() throws IOException {
			File file = ((IFile) tempFile).getLocation().toFile();
			FileReader fr = new FileReader(file);
			BufferedReader reader = new BufferedReader(fr);
			String line = reader.readLine();
			String contents = "";
			while (line != null) {
				contents += line + "\n";
				line = reader.readLine();
			}
			return contents;
		}

		public boolean visit(IResourceDelta delta) {
			IResource res = delta.getResource();
			switch (delta.getKind()) {
			case IResourceDelta.ADDED:
				System.out.print("Resource ");
				System.out.print(res.getFullPath());
				System.out.println(" was added.");
				break;
			case IResourceDelta.REMOVED:
				System.out.print("Resource ");
				System.out.print(res.getFullPath());
				System.out.println(" was removed.");
				break;
			case IResourceDelta.CHANGED:
				System.out.print("Resource ");
				System.out.print(delta.getFullPath());
				System.out.println(" has changed.");
				if (res instanceof IFile) {
					if (((IFile) res).getLocation().equals(tempFile.getLocation())) {
						try {
							setContent(model, getContents(), textType);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				int flags = delta.getFlags();
				if ((flags & IResourceDelta.CONTENT) != 0) {

				}
				if ((flags & IResourceDelta.REPLACED) != 0) {
					System.out.println("--> Content Replaced");
				}
				if ((flags & IResourceDelta.MARKERS) != 0) {
					System.out.println("--> Marker Change");
					// IMarkerDelta[] markers = delta.getMarkerDeltas();
					// if interested in markers, check these deltas
				}
				break;

			}
			return true; // visit the children
		}
	}

}
