/**
 * 
 */
package org.jboss.tools.smooks.editor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.smooks.configuration.RuntimeDependency;
import org.jboss.tools.smooks.configuration.RuntimeMetadata;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.SmooksXMLEditor;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.graphical.editors.ISmooksEditorInitListener;
import org.jboss.tools.smooks.model.ISmooksModelProvider;
import org.jboss.tools.smooks.model.SmooksEditorModelBuilder;
import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.core.IParam;
import org.jboss.tools.smooks.model.core.provider.CoreItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.javabean.provider.JavaBeanItemProviderAdapterFactory;
import org.milyn.javabean.dynamic.Model;

/**
 * @author Dart
 * 
 */
public class AbstractSmooksFormEditor extends FormEditor implements
		ISmooksModelProvider , IEditingDomainProvider{

	private boolean handleEMFModelChange;

	private IResourceChangeListener resourceChangeListener = null;

	protected StructuredTextEditor textEditor = null;

	private IDocumentListener xmlDocumentTraker = null;

	private Exception initSmooksModelException = null;

	protected SmooksEditorModelBuilder smooksEditorModelBuilder = null;

	protected List<ISmooksEditorInitListener> smooksInitListener = new ArrayList<ISmooksEditorInitListener>();

	protected List<ISourceSynchronizeListener> sourceSynchronizeListener = new ArrayList<ISourceSynchronizeListener>();

	protected Model<SmooksModel> smooksModel;

	protected CommandStack commandStack = new BasicCommandStack();
	
	protected EditingDomain editingDomain;
	
	protected boolean graphChanged = false;

	private ComposedAdapterFactory adapterFactory;

	public AbstractSmooksFormEditor() {
		super();
		resourceChangeListener = new SmooksResourceTraker();
		xmlDocumentTraker = new SmooksXMLEditorDocumentListener();
		smooksEditorModelBuilder = new SmooksEditorModelBuilder();
		initEditingDomain();
	}

	private void handleCommandStack(CommandStack commandStack) {
		commandStack.addCommandStackListener(new CommandStackListener() {
			
			public void commandStackChanged(EventObject event) {
				firePropertyChange(IEditorPart.PROP_DIRTY);
				handleEMFModelChange();
			}
		});
	}
	
	protected void initEditingDomain() {
		adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		
		adapterFactory.addAdapterFactory(new JavaBeanItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new CoreItemProviderAdapterFactory());
		
		editingDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack, new HashMap<Resource, Boolean>());
	}
	
	@Override
	public boolean isDirty() {
		return graphChanged || ((BasicCommandStack)commandStack).isSaveNeeded() || super.isDirty();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
	 */
	@Override
	protected void addPages() {
		textEditor = createTextEditor();
		try {
			int index = this.addPage(textEditor, getEditorInput());
			setPageText(index,
					Messages.AbstractSmooksFormEditor_Source_Page_Title);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		int messageType = IMessageProvider.ERROR;
		Exception exception = initSmooksModelException;
		if (exception == null) {
			exception = checkSmooksConfigContents(null);
			messageType = IMessageProvider.WARNING;
		}
		if (exception != null) {
			for (Iterator<?> iterator = this.smooksInitListener.iterator(); iterator
					.hasNext();) {
				ISmooksEditorInitListener initListener = (ISmooksEditorInitListener) iterator
						.next();
				initListener.initFailed(messageType, exception.getMessage());
			}
		}
	}

	public void addSmooksEditorInitListener(ISmooksEditorInitListener listener) {
		this.smooksInitListener.add(listener);
	}

	public void removeSmooksEditorInitListener(
			ISmooksEditorInitListener listener) {
		this.smooksInitListener.remove(listener);
	}

	public void addSourceSynchronizeListener(ISourceSynchronizeListener listener) {
		this.sourceSynchronizeListener.add(listener);
	}

	public void removeSourceSynchronizeListener(
			ISourceSynchronizeListener listener) {
		this.sourceSynchronizeListener.remove(listener);
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		RuntimeMetadata runtimeMetadata = new RuntimeMetadata();
		String filePath = null;
		IFile file = null;
		if (input instanceof FileStoreEditorInput) {
			try {
				filePath = ((FileStoreEditorInput) input).getURI().toURL()
						.getFile();
				runtimeMetadata.setSmooksConfig(new File(filePath));
			} catch (MalformedURLException e) {
				throw new PartInitException(
						Messages.AbstractSmooksFormEditor_Exception_Transform_URL,
						e);
			}
		}
		if (input instanceof IFileEditorInput) {
			file = ((IFileEditorInput) input).getFile();
			runtimeMetadata.setSmooksConfig(file);
			filePath = file.getFullPath().toPortableString();
		}

		if (filePath == null) {
			throw new PartInitException(Messages.AbstractSmooksFormEditor_Exception_Cannot_Get_Input_File);
		}
		
		handleCommandStack(commandStack);
		super.init(site, input);
	}

	public void setInput(IEditorInput input) {
		if (getEditorInput() != null) {
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			file.getWorkspace().removeResourceChangeListener(
					resourceChangeListener);
		}

		super.setInput(input);

		if (getEditorInput() != null) {
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			file.getWorkspace().addResourceChangeListener(
					resourceChangeListener);
			setPartName(file.getName());
		}

		String filePath = null;

		IFile file = null;

		if (input instanceof IFileEditorInput) {
			file = ((IFileEditorInput) input).getFile();
			filePath = file.getFullPath().toPortableString();
		}

		// create EMF resource
		if (file != null) {
			try {
				smooksModel = smooksEditorModelBuilder.readModel(file.getContents());
				smooksModel.getModelRoot().setModelProvider(this);
			} catch (Exception e) {
				initSmooksModelException = e;
			}
		} else {
		}

		// if (ex == null) {
		// if (smooksModel == null) {
		// try {
		// smooksResource.load(Collections.emptyMap());
		// smooksModel = smooksResource.getContents().get(0);
		// } catch (IOException e) {
		//
		// }
		// } else {
		// smooksResource.getContents().add(smooksModel);
		// }
		// }
	}

	protected void handleEMFModelChange() {
		IDocument document = textEditor.getDocumentProvider().getDocument(
				textEditor.getEditorInput());

		try {
			StringWriter writer = new StringWriter();
			this.smooksModel.writeModel(writer);

			String newContent = writer.toString();
			// newContent = getFormattedXMLContents(newContent);

			String oldContent = document.get();

			int startIndex = 0;
			while (startIndex < newContent.length()
					&& startIndex < oldContent.length()
					&& newContent.charAt(startIndex) == oldContent
							.charAt(startIndex)) {
				++startIndex;
			}
			int newEndIndex = newContent.length() - 1;
			int oldEndIndex = oldContent.length() - 1;
			while (newEndIndex >= startIndex
					&& oldEndIndex >= startIndex
					&& newContent.charAt(newEndIndex) == oldContent
							.charAt(oldEndIndex)) {
				--newEndIndex;
				--oldEndIndex;
			}

			String replacement = newContent.substring(startIndex,
					newEndIndex + 1);
			int length = oldEndIndex - startIndex + 1;
			handleEMFModelChange = true;
			document.replace(startIndex, length, replacement);

			// validator.startValidate(smooksModel.eResource().getContents(),
			// editingDomain);

		} catch (Exception exception) {
			SmooksConfigurationActivator.log(exception);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		try {
			IEditorPart activeEditor = getActiveEditor();
			if (/* this.smooksModel == null || */(activeEditor != null && activeEditor == textEditor)) {
				textEditor.doSave(monitor);
				this.commandStack.flush();
			} else {
				handleEMFModelChange = true;
				if (smooksModel != null) {
					StringWriter writer = new StringWriter();
					this.smooksModel.writeModel(writer);

					String newContent = writer.toString();
					IFile file = ((IFileEditorInput) getEditorInput())
							.getFile();
					((IFile) file).setContents(new ByteArrayInputStream(
							newContent.getBytes()), IResource.FORCE, monitor);
					monitor.worked(1);
					this.commandStack.flush();
					textEditor.doRevertToSaved();
				}
			}
			graphChanged = false;
			firePropertyChange(PROP_DIRTY);

		} catch (Exception e) {
			SmooksConfigurationActivator.log(e);
		} finally {
			monitor.done();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	protected Exception checkSmooksConfigContents(InputStream stream) {
		// Check
		Exception exception = null;
		IFile file = null;
		String filePath = null;
		RuntimeMetadata runtimeMetadata = new RuntimeMetadata();
		IEditorInput input = getEditorInput();
		if (input instanceof FileStoreEditorInput) {
			try {
				filePath = ((FileStoreEditorInput) input).getURI().toURL()
						.getFile();
				runtimeMetadata.setSmooksConfig(new File(filePath), stream);
			} catch (MalformedURLException e) {
				exception = e;
				// throw new PartInitException("Transform URL to URL error.",
				// e);
			}
		}
		if (exception == null) {
			if (input instanceof IFileEditorInput) {
				file = ((IFileEditorInput) input).getFile();
				File f = new File(file.getRawLocation().toOSString().trim());
				runtimeMetadata.setSmooksConfig(f, stream);
			}

			try {

				assertConfigSupported(runtimeMetadata);

			} catch (PartInitException e) {
				exception = e;
			}
		}
		return exception;
	}

	private void assertConfigSupported(RuntimeMetadata runtimeMetadata)
			throws PartInitException {
		List<RuntimeDependency> dependencies = runtimeMetadata
				.getDependencies();

		for (RuntimeDependency dependency : dependencies) {
			if (!dependency.isSupportedByEditor()) {
				java.net.URI changeToNS = dependency.getChangeToNS();
				String errorMsg = Messages.AbstractSmooksFormEditor_Error_Unsupported
						+ dependency.getNamespaceURI()
						+ Messages.AbstractSmooksFormEditor_Error_Unsupported2;

				if (changeToNS != null) {
					errorMsg += Messages.AbstractSmooksFormEditor_Error_Update_Namespace
							+ changeToNS
							+ Messages.AbstractSmooksFormEditor_Error_Update_Namespace2;
				}

				throw new PartInitException(errorMsg);
			}
		}
	}

	protected StructuredTextEditor createTextEditor() {
		SmooksXMLEditor xmlEditor = new SmooksXMLEditor() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.jboss.tools.smooks.configuration.editors.SmooksXMLEditor#
			 * doSetInput(org.eclipse.ui.IEditorInput)
			 */
			@Override
			public void doSetInput(IEditorInput input) throws CoreException {
				TextViewer viewer = getTextViewer();
				if (viewer != null) {
					IDocument document = viewer.getDocument();
					if (document != null) {
						document.removeDocumentListener(xmlDocumentTraker);
					}
				}
				super.doSetInput(input);
				viewer = getTextViewer();
				if (viewer != null) {
					IDocument document = viewer.getDocument();
					if (document != null) {
						document.addDocumentListener(xmlDocumentTraker);
					}
				}
			}

			public void createPartControl(Composite parent) {
				super.createPartControl(parent);
				getTextViewer().getDocument().addDocumentListener(
						xmlDocumentTraker);

			}

		};
		return xmlEditor;
	}

	public void createNewModelViaTextPage() {
		IDocumentProvider dp = textEditor.getDocumentProvider();
		if (dp == null)
			return;
		Exception exception = null;
		int messageType = IMessageProvider.NONE;
		IDocument document = dp.getDocument(textEditor.getEditorInput());
		String conents = document.get();
		InputStream stream = new ByteArrayInputStream(conents.getBytes());
		exception = checkSmooksConfigContents(stream);
		if (exception != null)
			messageType = IMessageProvider.WARNING;
		if (exception == null) {
			try {
				stream = new ByteArrayInputStream(conents.getBytes());
				this.smooksModel = smooksEditorModelBuilder.readModel(stream);
				smooksModel.getModelRoot().setModelProvider(this);
			} catch (Exception e) {
				smooksModel = null;
				exception = e;
				messageType = IMessageProvider.ERROR;
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		// setPlatformVersion(SmooksUIUtils.judgeSmooksPlatformVersion(smooksModel));
		// judgeInputReader();
		for (Iterator<?> iterator = this.sourceSynchronizeListener.iterator(); iterator
				.hasNext();) {
			ISourceSynchronizeListener l = (ISourceSynchronizeListener) iterator
					.next();
			try {
				l.sourceChange(smooksModel);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String message = null;
		if (exception != null) {
			message = exception.getMessage();
		}
		if (message == null) {
			message = Messages.AbstractSmooksFormEditor_Error_Unknown;
		}
		for (Iterator<?> iterator = this.smooksInitListener.iterator(); iterator
				.hasNext();) {
			ISmooksEditorInitListener initListener = (ISmooksEditorInitListener) iterator
					.next();
			initListener.initFailed(messageType, message);
		}
	}

	public void handleDocumentChange() {
		createNewModelViaTextPage();
	}

	public class SmooksXMLEditorDocumentListener implements IDocumentListener {
		protected Timer timer = new Timer();
		protected TimerTask timerTask;

		public void documentAboutToBeChanged(DocumentEvent documentEvent) {
			// Ingore
		}

		public void documentChanged(final DocumentEvent documentEvent) {
			try {
				// This is need for the Properties view.
				//
				// setSelection(StructuredSelection.EMPTY);

				if (timerTask != null) {
					timerTask.cancel();
				}

				if (handleEMFModelChange) {
					handleEMFModelChange = false;
				} else {
					timerTask = new TimerTask() {
						@Override
						public void run() {
							getSite().getShell().getDisplay()
									.asyncExec(new Runnable() {
										public void run() {
											handleDocumentChange();
										}
									});
						}
					};

					timer.schedule(timerTask, 1000);
				}
			} catch (Exception exception) {
				SmooksConfigurationActivator.log(exception);
			}
		}
	}

	public class SmooksResourceTraker implements IResourceChangeListener,
			IResourceDeltaVisitor {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.core.resources.IResourceChangeListener#resourceChanged
		 * (org.eclipse.core.resources.IResourceChangeEvent)
		 */
		public void resourceChanged(IResourceChangeEvent event) {
			IResourceDelta delta = event.getDelta();
			try {
				if (delta != null)
					delta.accept(this);
			} catch (CoreException exception) {
			}
		}

		public boolean visit(IResourceDelta delta) throws CoreException {
			if (delta == null
					|| !delta.getResource().equals(
							((IFileEditorInput) getEditorInput()).getFile()))
				return true;

			if (delta.getKind() == IResourceDelta.REMOVED) {
				Display display = getSite().getShell().getDisplay();
				if ((IResourceDelta.MOVED_TO & delta.getFlags()) == 0) { // if
					// the
					// file
					// was
					// deleted
					// NOTE: The case where an open, unsaved file is deleted is
					// being handled by the
					// PartListener added to the Workbench in the initialize()
					// method.
					display.asyncExec(new Runnable() {
						public void run() {
							// if (!isDirty())
							closeEditor(false);
						}
					});
				} else { // else if it was moved or renamed
					final IFile newFile = ResourcesPlugin.getWorkspace()
							.getRoot().getFile(delta.getMovedToPath());
					display.asyncExec(new Runnable() {
						public void run() {
							// try {
							// ((SmooksXMLEditor) textEditor).doSetInput(new
							// FileEditorInput(newFile));
							// } catch (CoreException e) {
							// e.printStackTrace();
							// }
							setInput(new FileEditorInput(newFile));
						}
					});
				}
			}
			return false;
		}
	}

	private void closeEditor(boolean forceSave) {
		this.close(forceSave);
	}

	public Model<SmooksModel> getSmooksModel() {
		return smooksModel;
	}

	public EditingDomain getEditingDomain() {
		return editingDomain;
	}

	public String getInputType() {
		IParam inputParam = SmooksUIUtils.getInputTypeParam(smooksModel.getModelRoot());
		if (inputParam != null) {
			return inputParam.getValue();
		}
		return "";
	}

}
