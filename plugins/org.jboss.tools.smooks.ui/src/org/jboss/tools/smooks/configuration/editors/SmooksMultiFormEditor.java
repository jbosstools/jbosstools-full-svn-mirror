/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors;

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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IWrapperItemProvider;
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
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.SmooksConstants;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.wizards.SmooksConfigurationFileNewWizard;
import org.jboss.tools.smooks.model.calc.provider.CalcItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.common.provider.CommonItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.csv.provider.CsvItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.datasource.provider.DatasourceItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.dbrouting.provider.DbroutingItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.edi.provider.EdiItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.fileRouting.provider.FileRoutingItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.freemarker.provider.FreemarkerItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.groovy.provider.GroovyItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.iorouting.provider.IoroutingItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.javabean.provider.JavabeanItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.jmsrouting.provider.JmsroutingItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.json.provider.JsonItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.medi.provider.MEdiItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.smooks.provider.SmooksItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.validate.SmooksModelValidator;
import org.jboss.tools.smooks.model.xsl.provider.XslItemProviderAdapterFactory;
import org.jboss.tools.smooks10.model.smooks.util.SmooksResourceFactoryImpl;

/**
 * 
 * @author Dart Peng (dpeng@redhat.com) Date Apr 1, 2009
 */
public class SmooksMultiFormEditor extends FormEditor implements IEditingDomainProvider {

	public static final String EDITOR_ID = "org.jboss.tools.smooks.configuration.editors.MultiPageEditor";

	private SmooksConfigurationFormPage configurationPage = null;

	private StructuredTextEditor textEditor = null;

	protected ComposedAdapterFactory adapterFactory;

	protected EditingDomain editingDomain = null;

	private PropertySheetPage propertySheetPage = null;

	private SmooksGraphicsExtType smooksGraphicsExt = null;

	private EObject smooksModel;

	private boolean handleEMFModelChange;

	public SmooksMultiFormEditor() {
		super();
		initEditingDomain();
	}

	private void handleCommandStack(BasicCommandStack commandStack) {
		commandStack.addCommandStackListener(new CommandStackListener() {
			public void commandStackChanged(EventObject event) {
				firePropertyChange(IEditorPart.PROP_DIRTY);
				handleEMFModelChange();
				// Try to select the affected objects.
				//
				Command mostRecentCommand = ((CommandStack) event.getSource()).getMostRecentCommand();
				if (mostRecentCommand != null && (mostRecentCommand instanceof AddCommand)) {
					setSelectionToViewer(mostRecentCommand.getAffectedObjects());
				}
				if (propertySheetPage != null && !propertySheetPage.getControl().isDisposed()) {
					propertySheetPage.refresh();
				}
			}
		});
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
		} catch (Exception exception) {
			SmooksConfigurationActivator.getDefault().log(exception);
		}

	}

	@Override
	public boolean isDirty() {
		return ((BasicCommandStack) editingDomain.getCommandStack()).isSaveNeeded() || super.isDirty();
	}

	public EObject getSmooksModel() {
		return smooksModel;
	}

	public void setSmooksModel(EObject smooksModel) {
		this.smooksModel = smooksModel;
	}

	private void initEditingDomain() {
		adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

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
		BasicCommandStack commandStack = new BasicCommandStack();
		handleCommandStack(commandStack);
		editingDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack, new HashMap<Resource, Boolean>());
	}

	public void setSelectionToViewer(Collection<?> collection) {
		final Collection<?> theSelection = collection;
		if (theSelection != null && !theSelection.isEmpty() && configurationPage != null) {
			List<Object> newList = new ArrayList<Object>();
			for (Iterator<?> iterator = theSelection.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();
				if (object instanceof IWrapperItemProvider) {
					newList.add(((IWrapperItemProvider) object).getValue());
				}
			}
			configurationPage.setSelectionToViewer(newList);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
	 */
	@Override
	protected void addPages() {
		configurationPage = createSmooksConfigurationFormPage();
		try {
			int index = this.addPage(configurationPage);
			setPageText(index, "Design");
		} catch (PartInitException e) {
			e.printStackTrace();
		}

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

	protected void handleDocumentChange() {
		IDocument document = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
		String conents = document.get();
		Resource resource = editingDomain.getResourceSet().getResources().get(0);
		resource.unload();
		try {
			resource.load(new ByteArrayInputStream(conents.getBytes()), Collections.emptyMap());
			this.smooksModel = resource.getContents().get(0);
		} catch (IOException e) {
			smooksModel = null;
			SmooksConfigurationActivator.getDefault().log(e);
		}
		configurationPage.setSmooksModel(this.smooksModel);
	}

	protected SmooksConfigurationFormPage createSmooksConfigurationFormPage() {
		return new SmooksConfigurationFormPage(this, "DesignPage", "Design Page");
	}

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
		IEditorPart activeEditor = getActiveEditor();
		if (activeEditor != null && activeEditor == textEditor) {
			textEditor.doSave(monitor);
			((BasicCommandStack) editingDomain.getCommandStack()).saveIsDone();
			firePropertyChange(PROP_DIRTY);
		} else {
			Map<?, ?> options = Collections.emptyMap();
			initSaveOptions(options);
			if (editingDomain != null) {
				ResourceSet resourceSet = editingDomain.getResourceSet();
				List<Resource> resourceList = resourceSet.getResources();
				monitor.beginTask("Saving Smooks config file", resourceList.size());
				try {
					for (Iterator<Resource> iterator = resourceList.iterator(); iterator.hasNext();) {
						Resource resource = (Resource) iterator.next();
						resource.save(options);
						monitor.worked(1);
					}
					((BasicCommandStack) editingDomain.getCommandStack()).saveIsDone();
					textEditor.doRevertToSaved();
					firePropertyChange(PROP_DIRTY);
				} catch (IOException e) {
					SmooksConfigurationActivator.getDefault().log(e);
				} finally {
					monitor.done();
				}
			}
		}
		if(this.smooksModel != null){
			List<Object> lists = new ArrayList<Object>();
			lists.add(smooksModel);
			SmooksModelValidator validator = new SmooksModelValidator(lists,getEditingDomain());
			validator.validate(monitor);
		}
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
		// if success to open editor , check if there isn't ext file and create
		// a new one
		String extFileName = file.getName() + SmooksConstants.SMOOKS_GRAPHICSEXT_EXTENTION_NAME_WITHDOT;
		IContainer container = file.getParent();
		if (container != null && container.exists()) {
			IFile extFile = container.getFile(new Path(extFileName));
			if (extFile != null && !extFile.exists()) {
				try {
					SmooksConfigurationFileNewWizard.createExtentionFile(extFile, null);
				} catch (Throwable t) {
					// ignore
				}
			}
			if (extFile != null && extFile.exists()) {
				try {
					smooksGraphicsExt = SmooksUIUtils.loadSmooksGraphicsExt(extFile);
				} catch (IOException e) {
					SmooksConfigurationActivator.getDefault().log(e);
				}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		return false;
	}

}
