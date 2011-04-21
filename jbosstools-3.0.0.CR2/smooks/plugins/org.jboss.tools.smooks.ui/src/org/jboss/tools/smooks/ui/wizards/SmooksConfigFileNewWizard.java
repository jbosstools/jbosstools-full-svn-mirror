package org.jboss.tools.smooks.ui.wizards;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.jboss.tools.smooks.graphical.util.GraphicalInformationSaver;
import org.jboss.tools.smooks.ui.IStructuredDataCreationWizard;
import org.jboss.tools.smooks.ui.SmooksUIActivator;
import org.jboss.tools.smooks.ui.editors.SmooksFileEditorInput;
import org.jboss.tools.smooks.ui.editors.SmooksFormEditor;
import org.jboss.tools.smooks.ui.editors.TypeIDSelectionWizardPage;

/**
 * 
 * @author Dart
 *
 */
public class SmooksConfigFileNewWizard extends Wizard implements INewWizard,
		ISmooksDataCreationAddtionWizard {
	public static final String PRO_SOURCE_DATA_PATH = "sourceDataPath"; //$NON-NLS-1$

	public static final String PRO_TARGET_DATA_PATH = "targetDataPath"; //$NON-NLS-1$

	private SmooksConfigFileNewWizardPage page;
	private TypeIDSelectionWizardPage typeIDPage;

	private List<IWizardPage> sourceCreationPages = new ArrayList<IWizardPage>();

	private List<IWizardPage> targetCreationPages = new ArrayList<IWizardPage>();

	private IStructuredSelection selection;

	private IWorkbench workbench;

	/**
	 * Constructor for SmooksConfigFileNewWizard.
	 */
	public SmooksConfigFileNewWizard() {
		super();
		setNeedsProgressMonitor(true);
		super.setWindowTitle(Messages.getString("SmooksConfigFileNewWizard.NewConfigFileWizardTitle")); //$NON-NLS-1$
		setDefaultPageImageDescriptor(SmooksUIActivator.getImageDescriptor("icons/smooks-wiz.gif"));
	}

	/**
	 * Adding the page to the wizard.
	 */
	public void addPages() {
		page = new SmooksConfigFileNewWizardPage("newSmooksFile1", //$NON-NLS-1$
				getSelection());
		addPage(page);
		// TODO don't use the WizardSelectionPage
		typeIDPage = new TypeIDSelectionWizardPage("", false); //$NON-NLS-1$
		typeIDPage.setSelection(selection);
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
			MessageDialog.openError(getShell(), Messages.getString("SmooksConfigFileNewWizard.ErrorTitle"), realException //$NON-NLS-1$
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
		final String sourceTypeID = typeIDPage.getSourceID();
		final String targetTypeID = typeIDPage.getTargetID();
		final String[] sourceDataPath = new String[] { null };
		final String[] targetDataPath = new String[] { null };
		try {
			InputStream stream = openContentStream();
			if (file.exists()) {
				file.setContents(stream, true, true, monitor);
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
			// ignore
		}
		final GraphicalInformationSaver ginforSave = new GraphicalInformationSaver(
				file);

		monitor.worked(1);
		monitor.setTaskName("Opening file for editing..."); //$NON-NLS-1$
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				try {
					SmooksFileEditorInput input = new SmooksFileEditorInput(
							file);
					IWizard sourceWizard = typeIDPage
							.getSourceDataCreationWizard();
					IWizard targetWizard = typeIDPage
							.getTargetDataCreationWizard();
					if (sourceWizard != null) {
						sourceWizard.performFinish();
					}
					if (targetWizard != null) {
						targetWizard.performFinish();
					}
					if (sourceWizard instanceof IStructuredDataCreationWizard) {
						Object sourceObj = ((IStructuredDataCreationWizard) sourceWizard)
								.getTreeViewerInputContents();
						sourceDataPath[0] = ((IStructuredDataCreationWizard) sourceWizard)
								.getStructuredDataSourcePath();
						input.setSourceTreeViewerInputContents(sourceObj);
					}

					if (targetWizard instanceof IStructuredDataCreationWizard) {
						Object targetObj = ((IStructuredDataCreationWizard) targetWizard)
								.getTreeViewerInputContents();
						targetDataPath[0] = ((IStructuredDataCreationWizard) targetWizard)
								.getStructuredDataSourcePath();
						input.setTargetTreeViewerInputContents(targetObj);

					}
					Properties properties = null;
					if (sourceDataPath[0] != null || targetDataPath[0] != null) {
						properties = new Properties();
						if (sourceDataPath[0] != null) {
							properties.setProperty(PRO_SOURCE_DATA_PATH, sourceDataPath[0]);
						}
						if (targetDataPath[0] != null) {
							properties.setProperty(PRO_TARGET_DATA_PATH, targetDataPath[0]);
						}
					}
					try {
						ginforSave.doSave(null, sourceTypeID, targetTypeID, properties);
					} catch (IOException e) {
					} catch (CoreException e) {
					}
					IDE.openEditor(page, input, SmooksFormEditor.EDITOR_ID,
							true);// openEditor(page, file, true);
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
		String contents = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" //$NON-NLS-1$
				+ "     <smooks-resource-list xmlns=\"http://www.milyn.org/xsd/smooks-1.0.xsd\"/>"; //$NON-NLS-1$
		return new ByteArrayInputStream(contents.getBytes());
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "org.jboss.tools.smooks.ui", //$NON-NLS-1$
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

	public void addSourceWizardPage(IWizardPage page) {
		sourceCreationPages.add(page);
		page.setTitle(Messages.getString("SmooksConfigFileNewWizard.SourceDataTypeSelectionTitle")); //$NON-NLS-1$
	}

	public void addTargetWizardPage(IWizardPage page) {
		targetCreationPages.add(page);
		page.setTitle(Messages.getString("SmooksConfigFileNewWizard.TargetDataTypeSelectionTitle")); //$NON-NLS-1$
	}

	public void clearSourceWizardPages() {
		// for (Iterator iterator = sourceCreationPages.iterator();
		// iterator.hasNext();) {
		// IWizardPage page = (IWizardPage) iterator.next();
		// if(page != null){
		// page.dispose();
		// }
		// }
		sourceCreationPages.clear();
	}

	private IWizardPage getSourceCreationPage(IWizardPage page) {
		if (page == typeIDPage) {
			if (sourceCreationPages.isEmpty())
				return null;
			return (IWizardPage) sourceCreationPages.get(0);
		}
		if (sourceCreationPages.contains(page)) {
			int i = sourceCreationPages.indexOf(page);
			if ((i + 1) >= sourceCreationPages.size()) {
				return null;
			}
			return (IWizardPage) sourceCreationPages.get(i + 1);
		}
		return null;
	}

	private IWizardPage getTargetCreationPage(IWizardPage page) {
		if (page == typeIDPage) {
			if (targetCreationPages.isEmpty())
				return null;
			return (IWizardPage) targetCreationPages.get(0);
		}
		if (targetCreationPages.contains(page)) {
			int i = targetCreationPages.indexOf(page);
			if ((i + 1) >= targetCreationPages.size()) {
				return null;
			}
			return (IWizardPage) targetCreationPages.get(i + 1);
		}
		if (sourceCreationPages.contains(page)) {
			if (sourceCreationPages.get(sourceCreationPages.size() - 1) == page) {
				return (IWizardPage) targetCreationPages.get(0);
			}
		}
		return null;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		IWizardPage p = getSourceCreationPage(page);
		if (p != null)
			return p;
		p = getTargetCreationPage(page);
		if (p != null)
			return p;
		return super.getNextPage(page);
	}

	public void clearTargetWizardPages() {
		// for (Iterator iterator = sourceCreationPages.iterator();
		// iterator.hasNext();) {
		// IWizardPage page = (IWizardPage) iterator.next();
		// if(page != null){
		// page.dispose();
		// }
		// }
		targetCreationPages.clear();
	}

	public void removeSourceWIzardPage(IWizardPage page) {
		sourceCreationPages.remove(page);
	}

	public void removeTargetWIzardPage(IWizardPage page) {
		targetCreationPages.remove(page);
	}
}