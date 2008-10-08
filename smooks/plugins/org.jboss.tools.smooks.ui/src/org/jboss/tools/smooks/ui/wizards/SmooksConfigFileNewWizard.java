package org.jboss.tools.smooks.ui.wizards;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.jboss.tools.smooks.graphical.util.GraphicalInformationSaver;
import org.jboss.tools.smooks.ui.editors.TypeIDSelectionWizardPage;

/**
 * This is a sample new wizard. Its role is to create a new file resource in the
 * provided container. If the container resource (a folder or a project) is
 * selected in the workspace when the wizard is opened, it will accept it as the
 * target container. The wizard creates one file with the extension "smooks". If
 * a sample multi-page editor (also available as a template) is registered for
 * the same extension, it will be able to open it.
 */

public class SmooksConfigFileNewWizard extends Wizard implements INewWizard {
	private SmooksConfigFileNewWizardPage page;
	private TypeIDSelectionWizardPage typeIDPage;

	private IStructuredSelection selection;

	private IWorkbench workbench;

	/**
	 * Constructor for SmooksConfigFileNewWizard.
	 */
	public SmooksConfigFileNewWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */
	public void addPages() {
		page = new SmooksConfigFileNewWizardPage("newSmooksFile1",
				getSelection());
		addPage(page);
		typeIDPage = new TypeIDSelectionWizardPage("");
		addPage(typeIDPage);
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	public boolean performFinish() {
		// final String containerName = page.getContainerName();
		// final String fileName = page.getFileName();
		final IFile file = page.createNewFile();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					doFinish(file, monitor);
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
			MessageDialog.openError(getShell(), "Error", realException
					.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * The worker method. It will find the container, create the file if missing
	 * or just replace its contents, and open the editor on the newly created
	 * file.
	 */

	private void doFinish(final IFile file, IProgressMonitor monitor)
			throws CoreException {
		// create a sample file
		String sourceTypeID = typeIDPage.getSourceID();
		String targetTypeID = typeIDPage.getTargetID();

		try {
			InputStream stream = openContentStream();
			if (file.exists()) {
				file.setContents(stream, true, true, monitor);
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
			GraphicalInformationSaver ginforSave = new GraphicalInformationSaver(
					file);
			ginforSave.doSave(monitor, sourceTypeID, targetTypeID);
		} catch (IOException e) {
		}
		monitor.worked(1);
		monitor.setTaskName("Opening file for editing...");
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file, true);
				} catch (PartInitException e) {
				}
			}
		});
		monitor.worked(1);
	}

	/**
	 * We will initialize file contents with a sample text.
	 */

	private InputStream openContentStream() {
		String contents = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "     <smooks-resource-list xmlns=\"http://www.milyn.org/xsd/smooks-1.0.xsd\"/>";
		return new ByteArrayInputStream(contents.getBytes());
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "org.jboss.tools.smooks.ui",
				IStatus.OK, message, null);
		throw new CoreException(status);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}

	public IStructuredSelection getSelection() {
		return selection;
	}

	public void setSelection(IStructuredSelection selection) {
		this.selection = selection;
	}
}