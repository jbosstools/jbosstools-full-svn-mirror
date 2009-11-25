package org.jboss.tools.smooks.editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.command.CommandWrapper;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.SmooksConstants;
import org.jboss.tools.smooks.configuration.editors.SmooksXMLEditor;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.validate.ISmooksModelValidateListener;
import org.jboss.tools.smooks.configuration.validate.SmooksMarkerHelper;
import org.jboss.tools.smooks.configuration.validate.SmooksModelValidator;
import org.jboss.tools.smooks.model.calc.provider.CalcItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.common.provider.CommonItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.csv.provider.CsvItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.csv12.provider.Csv12ItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.datasource.provider.DatasourceItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.dbrouting.provider.DbroutingItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.edi.provider.EdiItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.edi12.provider.Edi12ItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.esbrouting.provider.EsbroutingItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.fileRouting.provider.FileRoutingItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.freemarker.provider.FreemarkerItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.groovy.provider.GroovyItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.iorouting.provider.IoroutingItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.javabean.provider.JavabeanItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.javabean12.provider.Javabean12ItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.jmsrouting.provider.JmsroutingItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.jmsrouting12.provider.Jmsrouting12ItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.json.provider.JsonItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.json12.provider.Json12ItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.medi.provider.MEdiItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.persistence12.provider.Persistence12ItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.rules10.provider.Rules10ItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.smooks.DocumentRoot;
import org.jboss.tools.smooks.model.smooks.ParamType;
import org.jboss.tools.smooks.model.smooks.provider.SmooksItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.validation10.provider.Validation10ItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.xsl.provider.XslItemProviderAdapterFactory;
import org.jboss.tools.smooks10.model.smooks.util.SmooksResourceFactoryImpl;

public class AbstractSmooksFormEditor extends FormEditor implements IEditingDomainProvider,
		ISmooksModelValidateListener, ISmooksModelProvider {

	protected String platformVersion = SmooksConstants.VERSION_1_2;

	protected String inputType = null;

	protected List<ISourceSynchronizeListener> sourceSynchronizeListener = new ArrayList<ISourceSynchronizeListener>();

	public static final String EDITOR_ID = "org.jboss.tools.smooks.edimap.editors.MultiPageEditor";

	protected StructuredTextEditor textEditor = null;

	protected ComposedAdapterFactory adapterFactory;

	protected EditingDomain editingDomain = null;

	// private PropertySheetPage propertySheetPage = null;

	protected SmooksModelValidator validator = null;

	protected EObject smooksModel;

	private boolean handleEMFModelChange;

	protected SmooksMarkerHelper markerHelper = new SmooksMarkerHelper();

	protected List<Diagnostic> diagnosticList;

	protected boolean graphChanged = false;

	// private Object smooksDOMModel;

	public AbstractSmooksFormEditor() {
		super();
		initEditingDomain();
	}

	/**
	 * @return the inputType
	 */
	public String getInputType() {
		ParamType inputParam = SmooksUIUtils.getInputTypeParam(SmooksUIUtils.getSmooks11ResourceListType(smooksModel));
		if (inputParam != null) {
			return inputParam.getStringValue();
		}
		return null;
	}

	/**
	 * @param inputType
	 *            the inputType to set
	 */
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	/**
	 * @return the platformVersion
	 */
	public String getPlatformVersion() {
		return platformVersion;
	}

	/**
	 * @param platformVersion
	 *            the platformVersion to set
	 */
	public void setPlatformVersion(String platformVersion) {
		this.platformVersion = platformVersion;
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == ISmooksModelProvider.class)
			return this;
		if (adapter == IEditingDomainProvider.class) {
			return this;
		}
		return super.getAdapter(adapter);
	}

	public void addSourceSynchronizeListener(ISourceSynchronizeListener listener) {
		this.sourceSynchronizeListener.add(listener);
	}

	public void removeSourceSynchronizeListener(ISourceSynchronizeListener listener) {
		this.sourceSynchronizeListener.remove(listener);
	}

	private void handleCommandStack(BasicCommandStack commandStack) {
		commandStack.addCommandStackListener(new CommandStackListener() {
			public void commandStackChanged(EventObject event) {
				firePropertyChange(IEditorPart.PROP_DIRTY);
				handleEMFModelChange();
				// Try to select the affected objects.
				//
				final Command mostRecentCommand = ((CommandStack) event.getSource()).getMostRecentCommand();
				getContainer().getDisplay().asyncExec(new Runnable() {
					public void run() {
						if (mostRecentCommand != null) {
							Command rawCommand = mostRecentCommand;
							while (rawCommand instanceof CommandWrapper) {
								rawCommand = ((CommandWrapper) rawCommand).getCommand();
							}
							if (rawCommand instanceof SetCommand || rawCommand instanceof AddCommand
									|| rawCommand instanceof DeleteCommand) {
								activeRecentAffectedModel(mostRecentCommand.getAffectedObjects());
							}
						}
					}
				});
			}
		});
	}

	public StructuredTextEditor getTextEditor() {
		return textEditor;
	}

	protected ByteArrayOutputStream getFormattedXMLContentsStream(InputStream outstream) throws IOException {
		XMLWriter writer = null;
		try {
			SAXReader parser = new SAXReader();
			Document doc = parser.read(outstream);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			OutputFormat format = OutputFormat.createPrettyPrint();
			writer = new XMLWriter(stream, format);
			writer.write(doc);
			return stream;
		} catch (Throwable t) {

		} finally {
			if (writer != null) {
				writer.close();
			}
		}
		return null;
	}

	protected String getFormattedXMLContents(String contents) throws IOException {
		InputStream istream = null;
		ByteArrayOutputStream stream = null;
		try {
			istream = new ByteArrayInputStream(contents.getBytes());
			stream = getFormattedXMLContentsStream(istream);
			return new String(stream.toByteArray());
		} catch (Throwable t) {

		} finally {
			if (istream != null) {
				istream.close();
			}
			if (stream != null) {
				stream.close();
			}
		}
		return null;
	}

	protected void handleEMFModelChange() {
		IDocument document = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			smooksModel.eResource().save(out, null);

			String newContent = out.toString();
			newContent = getFormattedXMLContents(newContent);

			String oldContent = document.get();

			int startIndex = 0;
			while (startIndex < newContent.length() && startIndex < oldContent.length()
					&& newContent.charAt(startIndex) == oldContent.charAt(startIndex)) {
				++startIndex;
			}
			int newEndIndex = newContent.length() - 1;
			int oldEndIndex = oldContent.length() - 1;
			while (newEndIndex >= startIndex && oldEndIndex >= startIndex
					&& newContent.charAt(newEndIndex) == oldContent.charAt(oldEndIndex)) {
				--newEndIndex;
				--oldEndIndex;
			}

			String replacement = newContent.substring(startIndex, newEndIndex + 1);
			int length = oldEndIndex - startIndex + 1;
			handleEMFModelChange = true;
			document.replace(startIndex, length, replacement);

			validator.startValidate(smooksModel.eResource().getContents(), editingDomain);

		} catch (Exception exception) {
			SmooksConfigurationActivator.getDefault().log(exception);
		}

	}

	@Override
	public boolean isDirty() {
		return graphChanged || ((BasicCommandStack) editingDomain.getCommandStack()).isSaveNeeded() || super.isDirty();
	}

	public EObject getSmooksModel() {
		return smooksModel;
	}

	public void setSmooksModel(EObject smooksModel) {
		if (this.smooksModel != smooksModel) {
			this.smooksModel = smooksModel;
			handleEMFModelChange();
		}
	}

	protected void initEditingDomain() {
		adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		// add smooks 1.0 item provider model
		adapterFactory
				.addAdapterFactory(new org.jboss.tools.smooks10.model.smooks.provider.SmooksItemProviderAdapterFactory());

		// add smooks 1.1.2 EMF item provider model
		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new XslItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new FreemarkerItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new JavabeanItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new CommonItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new SmooksItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new MEdiItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new EdiItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new IoroutingItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new JsonItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new JmsroutingItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new DbroutingItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new CsvItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new DatasourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new CalcItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new GroovyItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new FileRoutingItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new EsbroutingItemProviderAdapterFactory());

		// add smooks 1.2 EMF itemprovider
		adapterFactory.addAdapterFactory(new Json12ItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new Edi12ItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new Javabean12ItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new Csv12ItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new Rules10ItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new Validation10ItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new Jmsrouting12ItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new Persistence12ItemProviderAdapterFactory());

		BasicCommandStack commandStack = new BasicCommandStack();
		handleCommandStack(commandStack);
		editingDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack, new HashMap<Resource, Boolean>());
	}

	public void activeRecentAffectedModel(Collection<?> collection) {

	}

	public void addValidateListener(ISmooksModelValidateListener listener) {
		validator.addValidateListener(listener);
	}

	public void removeValidateListener(ISmooksModelValidateListener listener) {
		validator.removeValidateListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
	 */
	@Override
	protected void addPages() {
		// configurationPage = createSmooksConfigurationFormPage();
		// addValidateListener(configurationPage);
		// try {
		// int index = this.addPage(configurationPage);
		// setPageText(index, "Design");
		// } catch (PartInitException e) {
		// e.printStackTrace();
		// }

		textEditor = createTextEditor();
		try {
			int index = this.addPage(textEditor, getEditorInput());
			setPageText(index, "Source");
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	protected StructuredTextEditor createTextEditor() {
		SmooksXMLEditor xmlEditor = new SmooksXMLEditor() {

			public void createPartControl(Composite parent) {
				super.createPartControl(parent);
				getTextViewer().getDocument().addDocumentListener(new IDocumentListener() {

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
										getSite().getShell().getDisplay().asyncExec(new Runnable() {
											public void run() {
												handleDocumentChange();
											}
										});
									}
								};

								timer.schedule(timerTask, 1000);
							}
						} catch (Exception exception) {
							SmooksConfigurationActivator.getDefault().log(exception);
						}
					}
				});

			}

		};
		return xmlEditor;
	}

	protected void createNewModelViaTextPage() {
		IDocumentProvider dp = textEditor.getDocumentProvider();
		if (dp == null)
			return;
		IDocument document = dp.getDocument(textEditor.getEditorInput());
		String conents = document.get();
		Resource resource = editingDomain.getResourceSet().getResources().get(0);
		resource.unload();
		try {
			resource.load(new ByteArrayInputStream(conents.getBytes()), Collections.emptyMap());
			this.smooksModel = resource.getContents().get(0);
		} catch (IOException e) {
			smooksModel = null;
		}
		setPlatformVersion(SmooksUIUtils.judgeSmooksPlatformVersion(smooksModel));
		for (Iterator<?> iterator = this.sourceSynchronizeListener.iterator(); iterator.hasNext();) {
			ISourceSynchronizeListener l = (ISourceSynchronizeListener) iterator.next();
			try {
				l.sourceChange(smooksModel);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void handleDocumentChange() {
		createNewModelViaTextPage();
		try {
			validator.startValidate(smooksModel.eResource().getContents(), editingDomain);
		} catch (Throwable e) {

		}
	}

	// protected SmooksConfigurationFormPage createSmooksConfigurationFormPage()
	// {
	// return new SmooksConfigurationFormPage(this, "DesignPage",
	// "Design Page");
	// }

	protected void initSaveOptions(Map<?, ?> options) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			IEditorPart activeEditor = getActiveEditor();
			if (activeEditor != null && activeEditor == textEditor) {
				textEditor.doSave(monitor);
				((BasicCommandStack) editingDomain.getCommandStack()).saveIsDone();

			} else {
				Map<?, ?> options = Collections.emptyMap();
				initSaveOptions(options);
				if (editingDomain != null) {
					ResourceSet resourceSet = editingDomain.getResourceSet();
					List<Resource> resourceList = resourceSet.getResources();
					monitor.beginTask("Saving Smooks config file", resourceList.size());

					for (Iterator<Resource> iterator = resourceList.iterator(); iterator.hasNext();) {
						handleEMFModelChange = true;
						Resource resource = (Resource) iterator.next();
						ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
						resource.save(outputStream, options);
						IResource file = SmooksUIUtils.getResource(resource);
						if (file.exists() && file instanceof IFile) {
							ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
							ByteArrayOutputStream newOutputStream = getFormattedXMLContentsStream(inputStream);
							try {
								((IFile) file).setContents(new ByteArrayInputStream(newOutputStream.toByteArray()),
										IResource.FORCE, monitor);
							} catch (CoreException e) {
								e.printStackTrace();
							} finally {
								if (outputStream != null) {
									outputStream.close();
								}
								if (newOutputStream != null) {
									newOutputStream.close();
								}
								if (inputStream != null) {
									inputStream.close();
								}
							}
						}
						monitor.worked(1);
					}
					((BasicCommandStack) editingDomain.getCommandStack()).saveIsDone();
					textEditor.doRevertToSaved();
				}
			}
			graphChanged = false;

			firePropertyChange(PROP_DIRTY);
			if (this.smooksModel != null) {
				validator.startValidate(smooksModel.eResource().getContents(), editingDomain);
			}
		} catch (IOException e) {
			SmooksConfigurationActivator.getDefault().log(e);
		} finally {
			monitor.done();
		}
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		String filePath = null;
		String partName = "smooks editor";
		IFile file = null;
		if (input instanceof FileStoreEditorInput) {
			try {
				filePath = ((FileStoreEditorInput) input).getURI().toURL().getFile();
			} catch (MalformedURLException e) {
				throw new PartInitException("Transform URL to URL error.", e);
			}
		}
		if (input instanceof IFileEditorInput) {
			file = ((IFileEditorInput) input).getFile();
			filePath = file.getFullPath().toPortableString();
			partName = file.getName();
		}

		if (filePath == null)
			throw new PartInitException("Can't get the input file");

		Resource smooksResource = null;

		if (file != null) {
			smooksResource = new SmooksResourceFactoryImpl().createResource(URI.createPlatformResourceURI(filePath,
					false));
		} else {
			smooksResource = new SmooksResourceFactoryImpl().createResource(URI.createFileURI(filePath));
		}
		try {
			smooksResource.load(Collections.emptyMap());
			smooksModel = smooksResource.getContents().get(0);
		} catch (IOException e) {
			throw new PartInitException(e.getMessage());
		}
		editingDomain.getResourceSet().getResources().add(smooksResource);
		super.init(site, input);

		validator = new SmooksModelValidator();
		addValidateListener(this);
		setDiagnosticList(validator.validate(smooksModel.eResource().getContents(), editingDomain));

		// if success to open editor , check if there isn't ext element in
		// smooks config file
		// create new one for it

		setPartName(partName);

		String version = SmooksUIUtils.judgeSmooksPlatformVersion(smooksModel);
		try {
			this.setPlatformVersion(version);
		} catch (Exception e) {

		}
	}

	public EObject getSmooksResourceList() {
		EObject m = null;
		EObject smooksModel = getSmooksModel();
		if (smooksModel instanceof org.jboss.tools.smooks10.model.smooks.DocumentRoot) {
			m = ((org.jboss.tools.smooks10.model.smooks.DocumentRoot) smooksModel).getSmooksResourceList();
		}
		if (smooksModel instanceof DocumentRoot) {
			m = ((DocumentRoot) smooksModel).getSmooksResourceList();
		}
		return m;
	}

	public IPath getNewPath(IFile file) {
		IPath fullPath = file.getFullPath();
		fullPath.removeLastSegments(1);
		fullPath.append("New name");
		return fullPath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	public void doSaveAs() {

	}

	public ComposedAdapterFactory getAdapterFactory() {
		return adapterFactory;
	}

	public void setAdapterFactory(ComposedAdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
	}

	public EditingDomain getEditingDomain() {
		return editingDomain;
	}

	public void setEditingDomain(EditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}

	/**
	 * @return the diagnostic
	 */
	public List<Diagnostic> getDiagnosticList() {
		return diagnosticList;
	}

	/**
	 * @param diagnosticList
	 *            the diagnostic to set
	 */
	public void setDiagnosticList(List<Diagnostic> d) {
		this.diagnosticList = d;

		if (markerHelper != null) {
			Resource resource = editingDomain.getResourceSet().getResources().get(0);
			if (resource != null) {
				markerHelper.deleteMarkers(resource);
			}
			for (Iterator<?> iterator = d.iterator(); iterator.hasNext();) {
				Diagnostic diagnostic = (Diagnostic) iterator.next();
				if (resource != null && diagnostic.getSeverity() != Diagnostic.OK) {
					for (Diagnostic childDiagnostic : diagnostic.getChildren()) {
						markerHelper.createMarkers(resource, childDiagnostic);
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		return false;
	}

	public void validateEnd(List<Diagnostic> diagnosticResult) {
		setDiagnosticList(diagnosticResult);
	}

	public void validateStart() {

	}

	public void graphPropertyChange(EStructuralFeature featre, Object value) {
		// if
		// (featre.equals(GraphPackage.Literals.SMOOKS_GRAPHICS_EXT_TYPE__AUTHOR)
		// ||
		// featre.equals(GraphPackage.Literals.SMOOKS_GRAPHICS_EXT_TYPE__PLATFORM_VERSION)
		// ||
		// featre.equals(GraphPackage.Literals.SMOOKS_GRAPHICS_EXT_TYPE__NAME)
		// ||
		// featre.equals(GraphPackage.Literals.SMOOKS_GRAPHICS_EXT_TYPE__INPUT_TYPE))
		// {
		// graphChanged = true;
		// firePropertyChange(PROP_DIRTY);
		// }
	}
}
