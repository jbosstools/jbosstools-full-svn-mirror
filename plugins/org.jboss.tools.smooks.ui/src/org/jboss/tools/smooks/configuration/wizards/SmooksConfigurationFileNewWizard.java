package org.jboss.tools.smooks.configuration.wizards;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.SmooksConstants;
import org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor;

/**
 * 
 * @author Dart Peng (dpeng@redhat.com) Date Apr 1, 2009
 */
public class SmooksConfigurationFileNewWizard extends Wizard implements INewWizard {
	private SmooksFileContainerSelectionPage containerSelectionPage;
	private ISelection selection;

	/**
	 * Constructor for SampleNewWizard.
	 */
	public SmooksConfigurationFileNewWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		containerSelectionPage = new SmooksFileContainerSelectionPage("Smooks Configuration File",
			(IStructuredSelection) selection);
		addPage(containerSelectionPage);
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	public boolean performFinish() {
		final IPath containerPath = containerSelectionPage.getContainerFullPath();
		final String fileName = containerSelectionPage.getFileName();
		final String version = containerSelectionPage.getVersion();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(containerPath, fileName, monitor, version);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * The worker method. It will find the container, create the file if missing
	 * or just replace its contents, and open the editor on the newly created
	 * file.
	 */

	private void doFinish(IPath containerPath, String fileName, IProgressMonitor monitor,
		String version) throws CoreException {
		// create a sample file
		monitor.beginTask("Creating " + fileName, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(containerPath);
		IContainer container = null;
		if (resource.exists() && resource instanceof IContainer) {
			container = (IContainer) resource;
		}
		if (container == null) throwCoreException("Container \"" + containerPath.toPortableString()
			+ "\" does not exist.");
		final IFile file = container.getFile(new Path(fileName));
		try {
			InputStream stream = openContentStream(version);
			if (file.exists()) {
				file.setContents(stream, true, true, monitor);
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
			SmooksConfigurationActivator.getDefault().log(e);
		}
		monitor.worked(1);
		monitor.setTaskName("Opening file with Smooks Editor.");
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage();
				FileEditorInput editorInput = new FileEditorInput(file);
				try {
					page.openEditor(editorInput, SmooksMultiFormEditor.EDITOR_ID, true);
				} catch (PartInitException e) {
					SmooksConfigurationActivator.getDefault().log(e);
				}
			}
		});
		monitor.worked(1);
	}

	/**
	 * We will initialize file contents with different smooks config file
	 * version.
	 */

	private InputStream openContentStream(String version) {
		String contents = "";
		if (SmooksConstants.VERSION_1_0.equals(version)) {
			contents = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" //$NON-NLS-1$
				+ "<smooks-resource-list xmlns=\"http://www.milyn.org/xsd/smooks-1.0.xsd\">\n"//$NON-NLS-1$
				+ "		<resource-config selector=\"global-parameters\">\n"//$NON-NLS-1$
				+ "			<param name=\"stream.filter.type\">SAX</param>\n"//$NON-NLS-1$
				+ "		</resource-config>\n"//$NON-NLS-1$
				+ "</smooks-resource-list>"; //$NON-NLS-1$
		}
		if (SmooksConstants.VERSION_1_1_1.equals(version)) {
			contents = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" //$NON-NLS-1$
				+ "<smooks-resource-list xmlns=\"http://www.milyn.org/xsd/smooks-1.1.xsd\">\n"//$NON-NLS-1$
				+ "		<resource-config selector=\"global-parameters\">\n"//$NON-NLS-1$
				+ "			<param name=\"stream.filter.type\">SAX</param>\n"//$NON-NLS-1$
				+ "		</resource-config>\n"//$NON-NLS-1$
				+ "</smooks-resource-list>"; //$NON-NLS-1$
		}
		return new ByteArrayInputStream(contents.getBytes());
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR,SmooksConfigurationActivator.PLUGIN_ID,
			IStatus.OK, message, null);
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}