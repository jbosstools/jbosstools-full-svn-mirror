package org.jboss.tools.smooks.editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

import org.eclipse.core.resources.IFile;
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
import org.eclipse.emf.ecore.util.FeatureMapUtil;
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
import org.jboss.tools.smooks.model.graphics.ext.GraphFactory;
import org.jboss.tools.smooks.model.graphics.ext.GraphPackage;
import org.jboss.tools.smooks.model.graphics.ext.GraphType;
import org.jboss.tools.smooks.model.graphics.ext.ISmooksGraphChangeListener;
import org.jboss.tools.smooks.model.graphics.ext.ProcessType;
import org.jboss.tools.smooks.model.graphics.ext.ProcessesType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
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
import org.jboss.tools.smooks.model.smooks.SmooksPackage;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
import org.jboss.tools.smooks.model.smooks.provider.SmooksItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.validation10.provider.Validation10ItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.xsl.provider.XslItemProviderAdapterFactory;
import org.jboss.tools.smooks10.model.smooks.util.SmooksResourceFactoryImpl;

public class AbstractSmooksFormEditor extends FormEditor implements IEditingDomainProvider,
		ISmooksModelValidateListener, ISmooksModelProvider, ISmooksGraphChangeListener {

	protected List<ISourceSynchronizeListener> sourceSynchronizeListener = new ArrayList<ISourceSynchronizeListener>();

	public static final String EDITOR_ID = "org.jboss.tools.smooks.edimap.editors.MultiPageEditor";

	protected StructuredTextEditor textEditor = null;

	protected ComposedAdapterFactory adapterFactory;

	protected EditingDomain editingDomain = null;

	// private PropertySheetPage propertySheetPage = null;

	protected SmooksGraphicsExtType smooksGraphicsExt = null;

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

				//				
				// if (propertySheetPage != null &&
				// !propertySheetPage.getControl().isDisposed()) {
				// propertySheetPage.refresh();
				// }
			}
		});
	}

	public StructuredTextEditor getTextEditor() {
		return textEditor;
	}

	protected void handleEMFModelChange() {
		IDocument document = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			smooksModel.eResource().save(out, null);

			String newContent = out.toString();
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

	/**
	 * @return the smooksGraphicsExt
	 */
	public SmooksGraphicsExtType getSmooksGraphicsExt() {
		return smooksGraphicsExt;
	}

	/**
	 * @param smooksGraphicsExt
	 *            the smooksGraphicsExt to set
	 */
	public void setSmooksGraphicsExt(SmooksGraphicsExtType smooksGraphicsExt) {
		this.smooksGraphicsExt = smooksGraphicsExt;
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
			SmooksGraphicsExtType oldGraphModel = smooksGraphicsExt;
			smooksGraphicsExt = createSmooksGraphcsExtType(smooksModel);
			if (oldGraphModel != null) {
				smooksGraphicsExt.getChangeListeners().addAll(oldGraphModel.getChangeListeners());
				oldGraphModel.getChangeListeners().clear();
			}
		} catch (IOException e) {
			smooksModel = null;
			smooksGraphicsExt = null;
		}
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
						resource.save(options);
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

	protected SmooksGraphicsExtType createSmooksGraphcsExtType(Object smooksModel) {
		SmooksResourceListType resourceList = null;
		if (smooksModel instanceof DocumentRoot) {
			resourceList = ((DocumentRoot) smooksModel).getSmooksResourceList();
		}

		if (resourceList == null) {
			return null;
		}

		List<?> children = resourceList.getAbstractResourceConfig();
		for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof SmooksGraphicsExtType) {
				return (SmooksGraphicsExtType) object;
			}
		}
		return null;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		IFile file = ((IFileEditorInput) input).getFile();
		Resource smooksResource = new SmooksResourceFactoryImpl().createResource(URI.createPlatformResourceURI(file
				.getFullPath().toPortableString(), false));
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

		setPartName(file.getName());

		smooksGraphicsExt = createSmooksGraphcsExtType(smooksModel);

		String version = SmooksUIUtils.judgeSmooksPlatformVersion(smooksModel);
		try {
			if (smooksGraphicsExt != null) {
				if (smooksGraphicsExt.getGraph() == null) {
					GraphType graph = GraphFactory.eINSTANCE.createGraphType();
					smooksGraphicsExt.setGraph(graph);
				}
				if (smooksGraphicsExt.getProcesses() == null) {
					ProcessesType processes = GraphFactory.eINSTANCE.createProcessesType();
					ProcessType process = GraphFactory.eINSTANCE.createProcessType();
					processes.setProcess(process);
					smooksGraphicsExt.setProcesses(processes);
				}else{
					if (smooksGraphicsExt.getProcesses().getProcess() == null) {
						ProcessesType processes = smooksGraphicsExt.getProcesses();
						ProcessType process = GraphFactory.eINSTANCE.createProcessType();
						processes.setProcess(process);
					}
				}

				if (SmooksConstants.VERSION_1_2.equals(smooksGraphicsExt.getPlatformVersion())) {
					if (version.equals(SmooksConstants.VERSION_1_1)) {
						version = SmooksConstants.VERSION_1_2;
					}
				}
				if (!version.equals(smooksGraphicsExt.getPlatformVersion())) {
					smooksGraphicsExt.setPlatformVersion(version);
					smooksResource.save(Collections.emptyMap());
				}
			} else {
				generateSmooksGraphExt();
			}

		} catch (Exception e) {

		}
		if (smooksGraphicsExt != null) {
			smooksGraphicsExt.addSmooksGraphChangeListener(this);
		}
	}

	protected void generateSmooksGraphExt() {
		String version = SmooksUIUtils.judgeSmooksPlatformVersion(smooksModel);
		String inputType = SmooksUIUtils.judgeInputType(smooksModel);

		SmooksResourceListType resourceList = null;
		if (smooksModel instanceof DocumentRoot) {
			resourceList = ((DocumentRoot) smooksModel).getSmooksResourceList();
		}

		if (resourceList != null) {
			smooksGraphicsExt = GraphFactory.eINSTANCE.createSmooksGraphicsExtType();
			smooksGraphicsExt.setInputType(inputType);
			smooksGraphicsExt.setPlatformVersion(version);
			AddCommand.create(
					this.editingDomain,
					resourceList,
					SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_READER_GROUP,
					FeatureMapUtil.createEntry(
							GraphPackage.Literals.SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__SMOOKS_GRAPHICS_EXT,
							smooksGraphicsExt)).execute();
			try {
				smooksModel.eResource().save(Collections.emptyMap());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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

	public void graphChanged(SmooksGraphicsExtType extType) {

	}

	public void inputTypeChanged(SmooksGraphicsExtType extType) {
		// graphChanged = true;
		// firePropertyChange(PROP_DIRTY);
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
