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
package org.jboss.tools.smooks.ui.editors;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jboss.tools.smooks.analyzer.MappingResourceConfigList;
import org.jboss.tools.smooks.analyzer.NormalSmooksModelBuilder;
import org.jboss.tools.smooks.analyzer.NormalSmooksModelPackage;
import org.jboss.tools.smooks.model.DocumentRoot;
import org.jboss.tools.smooks.model.ResourceType;
import org.jboss.tools.smooks.model.SmooksFactory;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.provider.SmooksItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.util.SmooksResourceFactoryImpl;
import org.jboss.tools.smooks.ui.AnalyzeResult;
import org.jboss.tools.smooks.ui.IAnalyzeListener;
import org.jboss.tools.smooks.ui.SmooksTextEdtor;
import org.jboss.tools.smooks.utils.UIUtils;

/**
 * @author Dart Peng
 * @Date Jul 28, 2008
 */
public class SmooksFormEditor extends FormEditor implements
		ITabbedPropertySheetPageContributor , IAnalyzeListener{

	private SmooksTextEdtor xmlTextEditor;

	private SmooksGraphicalFormPage graphicalPage = null;

	public SmooksGraphicalFormPage getGraphicalPage() {
		return graphicalPage;
	}

	private TabbedPropertySheetPage tabbedPropertySheetPage;
	private SmooksNormalContentEditFormPage normalPage;
	public static final String EDITOR_ID = "org.jboss.tools.smooks.ui.editors.SmooksFormEditor";
	private ComposedAdapterFactory adapterFactory;
	private AdapterFactoryEditingDomain editingDomain;
	private Resource smooksResource;

	// private SmooksTextEdtor textEdtior = null;

	private boolean forceDirty = false;
	private boolean onlyShowTextEditor = false;
	private Throwable showTextEditorReason = null;

	public SmooksFormEditor() {
		super();
		adapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory
				.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory
				.addAdapterFactory(new SmooksItemProviderAdapterFactory());
		editingDomain = new AdapterFactoryEditingDomain(adapterFactory,
				createCommandStack(), new HashMap<Resource, Boolean>());
	}

	protected CommandStack createCommandStack() {
		return new BasicCommandStack();
	}

	@Override
	public boolean isDirty() {
		return forceDirty || super.isDirty();
	}

	private void createGraphicalEditor() throws PartInitException {
		graphicalPage = new SmooksGraphicalFormPage(this, "graph", "Mapping");
		int index = this.addPage(this.graphicalPage);
		this.setPageText(index, "Graph");
	}

	private void createConfigurationEditor() throws PartInitException {
		normalPage = new SmooksNormalContentEditFormPage(this, "configuration",
				"Configuration", null);
		int index = this.addPage(normalPage);
		setPageText(index, "Configuration");
		// Set a default NormalPacakge to Normal Page
		MappingResourceConfigList mappingResourceConfig = graphicalPage
				.getMappingResourceConfigList();
		normalPage.setDisableGUI(this.showTextEditorReason != null);
		if (mappingResourceConfig != null) {
			refreshNormalPage(mappingResourceConfig
					.getGraphRenderResourceConfigList());
		} else {
			refreshNormalPage(Collections.EMPTY_LIST);
		}
	}

	private void createXMLTextEditor() throws PartInitException {
		this.xmlTextEditor = new SmooksTextEdtor(this.showTextEditorReason);
		xmlTextEditor.setEditorPart(this);
		int index = this.addPage(xmlTextEditor, getEditorInput());
		this.setPageText(index, "Source");
	}

	@Override
	protected void addPages() {
		try {
			createGraphicalEditor();
			createConfigurationEditor();
			createXMLTextEditor();
			assosiateEditors();
			if (showTextEditorReason != null)
				this.setActiveEditor(xmlTextEditor);
		} catch (Exception e) {
			UIUtils.showErrorDialog(getSite().getShell(), UIUtils
					.createErrorStatus(e));
		}
	}

	protected void assosiateEditors() {
		xmlTextEditor.addSaveListener(graphicalPage);
		graphicalPage.addAnalyzeListener(xmlTextEditor);
		graphicalPage.addAnalyzeListener(normalPage);
		graphicalPage.addAnalyzeListener(this);
	}

	public void setParseException(boolean onlyShowTextEditor, Throwable reason) {
		this.onlyShowTextEditor = onlyShowTextEditor;
		this.showTextEditorReason = reason;
	}

	/**
	 * @deprecated
	 */
	public void removeGraphicalFormPage() {
		int count = this.getPageCount();
		// try {
		// if (textEdtior == null) {
		// textEdtior = new SmooksTextEdtor(showTextEditorReason);
		// this.addPage(textEdtior, getEditorInput());
		// for (int i = 0; i < count; i++) {
		// this.removePage(0);
		// }
		// }
		// } catch (PartInitException e) {
		// e.printStackTrace();
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.editor.FormEditor#init(org.eclipse.ui.IEditorSite,
	 * org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		onlyShowTextEditor = false;
		Throwable parsingException = null;
		IFile file = ((IFileEditorInput) input).getFile();
		String path = file.getLocation().toOSString();
		if (this.getEditingDomain() != null && smooksResource == null) {
			smooksResource = new SmooksResourceFactoryImpl().createResource(URI
					.createFileURI(path));
			if (!smooksResource.isLoaded()) {
				try {
					smooksResource.load(Collections.EMPTY_MAP);
				} catch (IOException e) {
					parsingException = e;
				}
			}

			if (parsingException != null) {
				onlyShowTextEditor = true;
				showTextEditorReason = parsingException;
				return;
			}

			DocumentRoot documentRoot = null;
			if (smooksResource.getContents().isEmpty()) {
				documentRoot = SmooksFactory.eINSTANCE.createDocumentRoot();
				smooksResource.getContents().add(documentRoot);
			} else {
				documentRoot = (DocumentRoot) smooksResource.getContents().get(
						0);
			}

			SmooksResourceListType resourceList = documentRoot
					.getSmooksResourceList();
			if (resourceList == null) {
				resourceList = SmooksFactory.eINSTANCE
						.createSmooksResourceListType();
				documentRoot.setSmooksResourceList(resourceList);
			}

		}
	}

	public void refreshNormalPage(List resourceHidenConfigs) {
		NormalSmooksModelPackage modelPackage = createNewSmooksModelPackage();
		if (modelPackage != null) {
			modelPackage.setHidenSmooksElements(resourceHidenConfigs);
		}
		if (this.normalPage != null) {
			normalPage.setModelPackage(modelPackage);
		}
	}

	public NormalSmooksModelPackage createNewSmooksModelPackage() {
		NormalSmooksModelBuilder builder = NormalSmooksModelBuilder
				.getInstance();
		if (smooksResource.getContents().isEmpty())
			return null;
		DocumentRoot document = (DocumentRoot) smooksResource.getContents()
				.get(0);
		NormalSmooksModelPackage modelPackage = builder
				.buildNormalSmooksModelPackage(document.getSmooksResourceList());
		return modelPackage;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		int index = this.getCurrentPage();
		if (index == 2) {
			this.xmlTextEditor.doSave(monitor);
			fireEditorDirty(false);
		} else {

			this.graphicalPage.doSave(monitor);
			fireEditorDirty(false);
		}
	}

	public void fireEditorDirty(boolean dirty) {
		this.forceDirty = dirty;
		this.firePropertyChange(PROP_DIRTY);
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	protected IEditorSite createSite(IEditorPart editor) {
		return super.createSite(editor);
	}

	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySheetPage.class) {
			tabbedPropertySheetPage = new TabbedPropertySheetPage(this);
			return tabbedPropertySheetPage;
		}
		if (adapter == IContentOutlinePage.class
				&& this.getActiveEditor() == xmlTextEditor) {
			return super.getAdapter(adapter);
		} else {
		}
		return super.getAdapter(adapter);
	}

	public String getContributorId() {
		return getSite().getId();
	}

	public void setTabbedPropertySheetPage(
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		this.tabbedPropertySheetPage = tabbedPropertySheetPage;
	}

	public AdapterFactoryEditingDomain getEditingDomain() {
		return editingDomain;
	}

	public void setEditingDomain(AdapterFactoryEditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}

	/**
	 * @return the smooksResource
	 */
	public Resource getSmooksResource() {
		return smooksResource;
	}

	/**
	 * @param smooksResource
	 *            the smooksResource to set
	 */
	public void setSmooksResource(Resource smooksResource) {
		this.smooksResource = smooksResource;
	}

	public void endAnalyze(AnalyzeResult result) {
		if(result.getError() != null){
			this.setActivePage(2);
		}
	}

}
