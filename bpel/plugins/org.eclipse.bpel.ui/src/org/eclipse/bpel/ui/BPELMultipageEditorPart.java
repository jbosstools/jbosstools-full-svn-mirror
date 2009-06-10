/*******************************************************************************
 * Copyright (c) 2007 Intel Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Vitaly Tishkov, Intel - Initial API and Implementation
 *
 *******************************************************************************/ 

package org.eclipse.bpel.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.bpel.common.extension.model.ExtensionMap;
import org.eclipse.bpel.common.ui.editmodel.IEditModelListener;
import org.eclipse.bpel.common.ui.editmodel.ResourceInfo;
import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.CorrelationSet;
import org.eclipse.bpel.model.ExtensibleElement;
import org.eclipse.bpel.model.MessageExchange;
import org.eclipse.bpel.model.PartnerLink;
import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.model.Variable;
import org.eclipse.bpel.ui.adapters.AdapterNotification;
import org.eclipse.bpel.ui.editparts.ProcessTrayEditPart;
import org.eclipse.bpel.ui.editparts.util.OutlineTreePartFactory;
import org.eclipse.bpel.ui.properties.BPELPropertySection;
import org.eclipse.bpel.ui.uiextensionmodel.StartNode;
import org.eclipse.bpel.ui.util.BPELEditModelClient;
import org.eclipse.bpel.ui.util.BPELEditorUtil;
import org.eclipse.bpel.ui.util.BPELReader;
import org.eclipse.bpel.ui.util.ModelHelper;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.NotificationImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.internal.views.properties.tabbed.view.TabDescriptor;
import org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyViewer;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ISection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabContents;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class BPELMultipageEditorPart extends MultiPageEditorPart 
										implements IEditModelListener, 
										           IGotoMarker/*, CommandStackListener*/ {

	class OutlinePage extends ContentOutlinePage {
		private PageBook pageBook;
		private Control outline;
		private Canvas overview;
		private IAction showOutlineAction, showOverviewAction;
		static final int ID_OUTLINE  = 0;
		static final int ID_OVERVIEW = 1;
		private Thumbnail thumbnail;

		public OutlinePage(EditPartViewer viewer) {
			super(viewer);
		}

		// increase visibility.
		@Override
		public EditPartViewer getViewer() {
			return super.getViewer();
		}

		private void configureOutlineViewer() {
			getViewer().setEditDomain(getEditDomain());
			getViewer().setEditPartFactory(new OutlineTreePartFactory());

			fDesignViewer.registerViewer(getViewer());

			//FIXME should we add the same for src tab?
			ContextMenuProvider provider = new ProcessContextMenuProvider(getDesignEditor(), fDesignViewer.getActionRegistry());

			getViewer().setContextMenu(provider);
			getSite().registerContextMenu("org.eclipse.bpel.outline.contextmenu", //$NON-NLS-1$
				provider, 
				getSite().getSelectionProvider());
			getViewer().setKeyHandler(fDesignViewer.getKeyHandler());
			// TODO: Drag and drop support goes here
			// getViewer().addDropTargetListener(new BPELTemplateTransferDropTargetListener(getViewer()));
			IToolBarManager tbm = getSite().getActionBars().getToolBarManager();
			showOutlineAction = new Action() {
				@Override
				public void run() {
					showPage(ID_OUTLINE);
				}
                
                @Override
				public String getToolTipText() {
                    return Messages.OutlinePage_showOutlineView;
                }
			};
			showOutlineAction.setImageDescriptor(BPELUIPlugin.INSTANCE.getImageDescriptor(IBPELUIConstants.ICON_OUTLINE_16)); 
			tbm.add(showOutlineAction);
			showOverviewAction = new Action() {
				@Override
				public void run() {
					showPage(ID_OVERVIEW);
				}
                
                @Override
				public String getToolTipText() {
                    return Messages.OutlinePage_showOverviewView;
                }
			};
			showOverviewAction.setImageDescriptor(BPELUIPlugin.INSTANCE.getImageDescriptor(IBPELUIConstants.ICON_OVERVIEW_16)); 	
			tbm.add(showOverviewAction);
			showPage(ID_OUTLINE);
		}

		@Override
		public Control getControl() {
			return pageBook;
		}

		@Override
		public void createControl(Composite parent) {
			pageBook = new PageBook(parent, SWT.NONE);
			outline = getViewer().createControl(pageBook);
			overview = new Canvas(pageBook, SWT.NONE);
			pageBook.showPage(outline);
			configureOutlineViewer();
			// TODO: Add to the adapting selection provider
			//getSelectionSynchronizer().addViewer(getViewer());

			getViewer().setContents(getProcess());
		}

		private void initializeOverview() {
			LightweightSystem lws = new LightweightSystem(overview);
			RootEditPart rep = fDesignViewer.getGraphicalViewer().getRootEditPart();
			if (rep instanceof GraphicalBPELRootEditPart) {
				GraphicalBPELRootEditPart root = (GraphicalBPELRootEditPart)rep;
				thumbnail = new ScrollableThumbnail((Viewport)root.getFigure());
				thumbnail.setSource(root.getLayer(LayerConstants.PRINTABLE_LAYERS));
				lws.setContents(thumbnail);
			}
		}

		private void showPage(int id) {
			if (id == ID_OUTLINE) {
				showOutlineAction.setChecked(true);
				showOverviewAction.setChecked(false);
				pageBook.showPage(outline);
				if (thumbnail != null)
					thumbnail.setVisible(false);
			} else if (id == ID_OVERVIEW) {
				initializeOverview();
				showOutlineAction.setChecked(false);
				showOverviewAction.setChecked(true);
				pageBook.showPage(overview);
				thumbnail.setVisible(true);
			}
		}

		@Override
		public void dispose() {
			super.dispose();
		}
		
		@Override
		public void init(IPageSite pageSite) {
			super.init(pageSite);
			//should ActionRegistry be here too? 
			ActionRegistry registry = fDesignViewer.getActionRegistry();
			IActionBars bars = pageSite.getActionBars();
			String id = ActionFactory.UNDO.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));
			id = ActionFactory.REDO.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));
			id = ActionFactory.DELETE.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));
			id = ActionFactory.REVERT.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));
			bars.updateActionBars();
		}
	}

	protected class TextEditorSelectionListener implements ISelectionChangedListener {
		public void selectionChanged(SelectionChangedEvent event) {
			if (getActivePage() != DESIGN_PAGE_INDEX) {
				ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					List<Object> selections = new ArrayList<Object>();
					for (Iterator<Object> i = ((IStructuredSelection) selection).iterator(); i.hasNext();) {
						Object domNode = i.next();
						if (domNode instanceof Element) {
							Object facade = BPELEditorUtil.getInstance().findModelObjectForElement(process, (Element)domNode);
							if (facade != null) {
								selections.add(facade);
							}
						}
					}
					
					if (!selections.isEmpty()) {
						StructuredSelection bpelSelection = new StructuredSelection(selections);
						fDesignViewer.getAdaptingSelectionProvider().setSelection(bpelSelection);
					}
				}
			}
		}
	}
	
	protected class DesignViewerSelectionListener implements ISelectionChangedListener {
		public void selectionChanged(SelectionChangedEvent event) {
			//force selection update if only source page is not active
			if (getActivePage() != SOURCE_PAGE_INDEX) {
				try {
					ISelection sel = fDesignViewer.getSelection();
					Object selectedNode = ((IStructuredSelection)sel).getFirstElement();
					Element selectedNodeElement = null;
					
					if (selectedNode instanceof StartNode) {
						selectedNodeElement = ((StartNode)selectedNode).getProcess().getElement();
					} else if (selectedNode instanceof ExtensibleElement) {
						selectedNodeElement = ((ExtensibleElement)selectedNode).getElement();
					} 
				
					if (selectedNodeElement != null) {
						if (selectedNodeElement instanceof IDOMNode && ((IDOMNode)selectedNodeElement).getModel().isModelStateChanging()) {
							return;
						}
						StructuredSelection nodeSelection = new StructuredSelection(selectedNodeElement);
						getTextEditor().getSelectionProvider().setSelection(nodeSelection);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private Process process;
	
	private DefaultEditDomain editDomain;
	
	protected ModelListenerAdapter modelListenerAdapter;
	
	private Resource extensionsResource;
	
	private ExtensionMap extensionMap;
	
	protected StructuredTextEditor fTextEditor = null;
	protected BPELEditor fDesignViewer = null;
	protected int currentPage = -1;

	
	protected TextEditorSelectionListener textEditorSelectionListener;
	protected DesignViewerSelectionListener designViewerSelectionListener;
	
	// reacts to changes on the BPEL file (e.g. move, rename)
	private IFileChangeListener fileChangeListener;
	
	// refactoring listeners
	protected IResourceChangeListener postBuildRefactoringListener;

	BPELModelReconcileAdapter bpelModelReconcileAdapter;
	
	private OutlinePage outlinePage;
	protected BPELTabbedPropertySheetPage currentPropertySheetPage;
	
	protected ActionRegistry actionRegistry;
	 
	private static int DESIGN_PAGE_INDEX = 0;
	private static int SOURCE_PAGE_INDEX = 1;

	private Map<Long, EObject>fMarkers2EObject = new HashMap<Long, EObject>();
	private Notification fMarkersStale = new NotificationImpl(
			AdapterNotification.NOTIFICATION_MARKERS_STALE, null, null);
	
	public BPELMultipageEditorPart() {
		super();
		setEditDomain(new BPELEditDomain(this));
	}

	/**
	 * Connects the design viewer with the viewer selection manager. Should be
	 * done after createSourcePage() is done because we need to get the
	 * ViewerSelectionManager from the TextEditor. setModel is also done here
	 * because getModel() needs to reference the TextEditor.
	 */
	protected void connectDesignPage() {

		/*
		 * Connect selection from the Design page to the selection provider of
		 * the Source page so that selection in the Design page will drive
		 * selection in the Source page. 
		 */
		designViewerSelectionListener = new DesignViewerSelectionListener();
		fDesignViewer.getAdaptingSelectionProvider().addSelectionChangedListener(designViewerSelectionListener);
		
		/*
		 * Connect selection from the Source page to the selection provider of
		 * the Design page so that selection in the Source page will drive
		 * selection in the Design page. 
		 */
		textEditorSelectionListener = new TextEditorSelectionListener();
		ISelectionProvider provider = getTextEditor().getSelectionProvider();
		if (provider instanceof IPostSelectionProvider) {
			((IPostSelectionProvider) provider).addPostSelectionChangedListener(textEditorSelectionListener);
		} else {
			provider.addSelectionChangedListener(textEditorSelectionListener);
		}

	}

	/**
	 * Creates the design page of the multi-page editor.
	 */
	protected void createDesignPage() {
		fDesignViewer = new BPELEditor(getEditDomain(), this);
		loadModel();
		
		try
	    {
			addPage(0, fDesignViewer, getEditorInput());
			//FIXME I18N
			setPageText(0, "Design");
	    } catch (PartInitException e) {
	    	ErrorDialog.openError(getSite().getShell(), "Error creating Design page", null, e.getStatus()); //$NON-NLS-1$
	    }
	}

	/**
	 * Creates the source page of the multi-page editor.
	 */
	protected void createSourcePage() throws PartInitException {
		fTextEditor = new StructuredTextEditor();
		try
	    {
	    	addPage(0, fTextEditor, getEditorInput());
	    	//FIXME I18N
	    	setPageText(0, "Source");
	    } catch (PartInitException e) {
	    	ErrorDialog.openError(getSite().getShell(), "Error creating Source page", null, e.getStatus()); //$NON-NLS-1$
	    }
	}

	/**
	 * Creates the pages of this multi-page editor.
	 */
	@Override
	protected void createPages() {
		try {
			// source page must be created before design page
			createSourcePage();
			createDesignPage();
	    	firePropertyChange(PROP_TITLE);
			connectDesignPage();
			initializeFileChangeListener();
			initializeRefactoringListener();			
		} catch (PartInitException e) {
			//Logger.logException(e);
			throw new RuntimeException(e);
		} 
		
		if (BPELUIPlugin.INSTANCE.getDefaultPage().equals(IBPELUIConstants.SOURCE_PAGE)) {
			setActivePage(SOURCE_PAGE_INDEX);
		} else {
			setActivePage(DESIGN_PAGE_INDEX);
		}
		
		//updateTitle();
	}


	@Override
	public void dispose() {
		/*if (outlinePage != null && outlinePage.getViewer() != null) {
			outlinePage.getViewer().setContents(null);
		}*/
		if (currentPage == SOURCE_PAGE_INDEX) {
			BPELUIPlugin.INSTANCE.setDefaultPage(IBPELUIConstants.SOURCE_PAGE);
		} else {
			BPELUIPlugin.INSTANCE.setDefaultPage(IBPELUIConstants.DESIGN_PAGE);
		}

		outlinePage = null;
 		process = null;
 		
		if (fileChangeListener != null) {
			BPELUIPlugin.INSTANCE.getResourceChangeListener().removeListener(fileChangeListener);
		}

		if (postBuildRefactoringListener != null) {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			workspace.removeResourceChangeListener(postBuildRefactoringListener);
		}

		IStructuredModel model = fTextEditor.getModel();
		model.releaseFromEdit();
		fDesignViewer.dispose();
		fTextEditor.dispose();

		super.dispose();
	}
	
	public void doRevertToSaved(IProgressMonitor monitor) {
		// Do the work within an operation because this is a long running activity that modifies the workbench.
//		WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {
//			protected void execute(IProgressMonitor monitor) throws CoreException {
//				try {
//					getCommandFramework().abortCurrentChange();
//					getCommandStack().flush();
//					
//					// de-select anything selected on the canvas!  Otherwise removing things
//					// will trigger a bunch of behaviour when the selected object(s) are
//					// removed..
//					adaptingSelectionProvider.setSelection(StructuredSelection.EMPTY);
//					
//					process = null;
//					extensionMap = null;
//					extensionsResource = null;
//					lastSelectedEditPart = null;
//					// unload all resources (otherwise they stay around taking up space..?)
//					for (Iterator it = getResourceSet().getResources().iterator(); it.hasNext(); ) {
//						Resource res = (Resource)it.next();
//						res.unload();
//					}
//					loadModel();
//					getGraphicalViewer().setContents(process);
//					getTrayViewer().setContents(process);
//					if (outlinePage != null && outlinePage.getViewer() != null) {
//                      // hack!
//						if (Platform.getWS().equals(Platform.WS_GTK)) {
//							Tree tree = (Tree) outlinePage.getViewer().getControl();
//							if (tree != null) {
//								tree.setRedraw(false);
//								TreeItem[] items = tree.getItems();
//								for (int i = 0; i < items.length; i++) {
//									items[i].dispose();
//								}
//								tree.setRedraw(true);
//							}
//						}
//						outlinePage.getViewer().setContents(process);
//					}
//					selectModelObject(getProcess());
//				}
//				catch (Exception e) {
//					BPELUIPlugin.log(e);
//				}
//			}
//		};
//
//		try {
//			// This runs the options, and shows progress.
//			// (It appears to be a bad thing to fork this onto another thread.)
//			new ProgressMonitorDialog(getSite().getShell()).run(false, false, operation);
//
//			// Refresh the necessary state.
//			firePropertyChange(IEditorPart.PROP_DIRTY);
//		} catch (Exception e) {
//			BPELUIPlugin.log(e);
//		}
	}

	/**
	 * @see org.eclipse.ui.IEditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		// We use fTextEditor to save, because fDesignViewer.doSave() removes comments on save
		// Save bpel only
		fDesignViewer.getCommandFramework().applyCurrentChange();
		fTextEditor.doSave(progressMonitor);
		// Reset sync stamp and modified flag after save
		fDesignViewer.getEditModelClient().getPrimaryResourceInfo().resetSynchronizeStamp();
		fDesignViewer.getEditModelClient().getPrimaryResourceInfo().getResource().setModified(false);
		// Save extensions
		fDesignViewer.doSave(progressMonitor);
	    getCommandStack().markSaveLocation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		//saveAs is not allowed; do nothing
	}
	
	protected BPELTabbedPropertySheetPage createBPELTabbedPropertySheetPage() {
		//FIXME should the BPELTabbedPropertySheetPage has BPELMultiPageEditorPart as the 2nd argument?
		return new BPELTabbedPropertySheetPage(new ITabbedPropertySheetPageContributor() {
		    public String getContributorId() {
		    	// same as the palette one
		    	//return fDesignViewer.getPaletteAdditionsContributorId();
		    	return IBPELUIConstants.BPEL_EDITOR_ID;
		    }
        }, fDesignViewer);
	}

	
	protected ActionRegistry getActionRegistry() {
		if (actionRegistry == null)
			actionRegistry = new ActionRegistry();
		return actionRegistry;
	}

	
	@Override
	public Object getAdapter(Class type) {
		if (type == Process.class) {
			return process;
		}
		
		if (type == BPELEditModelClient.class) {
			return process;
		}

		//FIXME should we kill it?
		if (type == ModelListenerAdapter.class) {
			return modelListenerAdapter;
		}

		//FIXME should we kill it?
		if (type == Resource.class) {
			return extensionsResource;
		}

		//FIXME should we kill it?
		if (type == ExtensionMap.class) {
			return extensionMap;
		}

		//FIXME should we kill it?
		if (type == CommandStack.class) {
			return getCommandStack();
		}

		if (type == IContentOutlinePage.class) {
			if (outlinePage == null) {
				outlinePage = new OutlinePage(new TreeViewer());
			}
			return outlinePage;
		}

		if (type == IPropertySheetPage.class) {
			// We can't cache this object because the properties framework needs a new instance
			// every time it calls this method. 
			currentPropertySheetPage = createBPELTabbedPropertySheetPage();
			return currentPropertySheetPage;
		}

		if (type == ActionRegistry.class) {
			return getActionRegistry();
		}
		
		return super.getAdapter(type);
	  }

	public CommandStack getCommandStack() {
		return getEditDomain().getCommandStack();
	}

	protected int getDefaultPageTypeIndex() {
		int pageIndex = DESIGN_PAGE_INDEX;
		if (BPELUIPlugin.INSTANCE.getDefaultPage().equals(IBPELUIConstants.SOURCE_PAGE)) {
			pageIndex = SOURCE_PAGE_INDEX;
		}
		return pageIndex;
	}

	
	/**
	 * Returns the design viewer
	 * @return the design viewer
	 */
	protected BPELEditor getDesignEditor() {
		return fDesignViewer;
	}

	/**
	 * Returns the edit domain.
	 * @return the edit domain
	 */
	protected DefaultEditDomain getEditDomain() {
		return editDomain;
	}

	protected IFile getFileInput() {
		return ((IFileEditorInput) getEditorInput()).getFile();
	}

	public Process getProcess() {
		return process;
	}

	/**
	 * Returns the design viewer
	 * @return the design viewer
	 */
	protected StructuredTextEditor getSourceViewer() {
		return fTextEditor;
	}

	StructuredTextEditor getTextEditor() {
		return fTextEditor;
	}
	
	public void gotoMarker(IMarker marker) {
		
		// One such mechanism is to use the href of the model object
		// generated by the validator or whatever ...
		
		String href = null;
		try {
			href = (String) marker.getAttribute( "address.model" );
		} catch (CoreException ex) {
			BPELUIPlugin.log(ex);
		}		
		
		// lookup by object href in the model.
		EObject modelObject = null;
		
		if (href != null) {
			try {
				modelObject = fDesignViewer.getResource().getEObject( href );
			} catch (Throwable t) {
				BPELUIPlugin.log(t);
			}
		}
		
		gotoText(marker);				
		
		if (modelObject == null) {
			return;
		}
		
		gotoMarker ( marker, modelObject );
	}

	private void gotoText(IMarker marker) {
		Integer charStart = null;
		Integer charEnd = null;
		try {
			charStart = (Integer) marker.getAttribute( "charStart" );
			charEnd = (Integer) marker.getAttribute( "charEnd" );
		} catch (CoreException ex) {
			BPELUIPlugin.log(ex);
		}
		charStart = charStart == null ? 0 : charStart;
		charEnd = charEnd == null ? charStart : charEnd;
		try {
			fTextEditor.setHighlightRange(charStart, charEnd - charStart, true);
		} catch (Throwable t) {
			BPELUIPlugin.log(t);
		}
	}
	
	void gotoMarker ( IMarker marker, EObject modelObject ) {
		
		// TODO: is this bogus now that we have AdaptingSelectionProvider?
				
		
		// The closest parent which has an edit part in the graph view.
		//
		// The following do not have viewers in the graph view:
		//  1) Variables, PartnerLinks, Correlation Sets, Message Exchanges
		// If it's any of those, then we have to reveal the closest container
		// and then select the model object and show the properties.

		GraphicalViewer graphViewer = fDesignViewer.getGraphicalViewer();
		EObject refObj = null;
		
		EditPart editPart = null;
		if ( modelObject instanceof Variable ||
		     modelObject instanceof PartnerLink ||
		     modelObject instanceof CorrelationSet ||
		     modelObject instanceof MessageExchange) {
			
			refObj = ModelHelper.getContainingScope(modelObject);
			editPart = (EditPart)graphViewer.getEditPartRegistry().get(refObj);
			if (editPart != null) {
				graphViewer.reveal(editPart);
			}			
			fDesignViewer.selectModelObject(modelObject);
			
		} else if (modelObject instanceof Activity) {
			
			// activity objects are on the graphical viewer
			refObj = modelObject;
			editPart = (EditPart)graphViewer.getEditPartRegistry().get(refObj);
			
			if (editPart != null) {
				graphViewer.reveal(editPart);
			}
			
			fDesignViewer.selectModelObject(modelObject);
			
			
		} else {
				
			refObj = modelObject;
			while (refObj != null && !(refObj instanceof Activity) ) {
				refObj = refObj.eContainer();
			}
			
			// select process by default.
			if (refObj == null) {
				refObj = ModelHelper.getProcess( modelObject ) ;
			}
			
			modelObject = refObj;
			
			editPart = (EditPart)graphViewer.getEditPartRegistry().get(modelObject);
			
			if (editPart != null) {
				graphViewer.reveal(editPart);
			}
			
			fDesignViewer.selectModelObject(modelObject);
		}
						
		// If possible, try to display the marker in a property section.
		BPELTabbedPropertySheetPage propertySheetPage = currentPropertySheetPage;
		if (propertySheetPage == null) {
			return;
			// if currentPropertySheetPage is null it means that the properties
			// page hasn't shown yet. Since we only want to show it if we have
			// markers for it we create a new BPELTabbedPropertySheetPage. This
			// new one should only be used to select which tab and section to show.
			// TODO: this doesn't work
			//propertySheetPage = createBPELTabbedPropertySheetPage();
		}
		
		TabbedPropertyViewer viewer = propertySheetPage.getTabbedPropertyViewer();
		
		int j = 0;
		while (true) { // while we don't get an exception...
			TabDescriptor descriptor = null;
			try {
				descriptor = (TabDescriptor)viewer.getElementAt(j++);
			} catch (IndexOutOfBoundsException iobe) {
				break;
			}
			
			if (descriptor == null) {
				break; // no more descriptors
			}
			
			TabContents tab = descriptor.createTab();
			ISection[] sections = tab.getSections();
			for (int i = 0; i < sections.length; i++) {
			
				if (BPELPropertySection.class.isInstance( sections[i]) == false) {
					continue;
				}
				
				BPELPropertySection section = (BPELPropertySection)sections[i];

				// HACK: we have to fake the initialization of this section in order to
				// make isValidMarker work. This section should not be used as a normal section.
				section.createControls(new Composite(getSite().getShell(), 0), propertySheetPage);
				section.setInput(this, new StructuredSelection(modelObject));

				if (section.isValidMarker (marker) ) {
					
					// the first section that handles this kind of marker wins
					showPropertiesView();
					// get real viewer, Tab and ISection objects since we are probably using fake ones
					viewer = currentPropertySheetPage.getTabbedPropertyViewer();
					viewer.setSelection(new StructuredSelection(descriptor));
					tab = currentPropertySheetPage.getCurrentTab();
					section = (BPELPropertySection)tab.getSectionAtIndex(i);
					section.gotoMarker(marker);
					return; // ignore other sections and tabs
					
				}
			}					
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite,
	 *      org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		try {
			super.init(site, input);
//			getCommandStack().addCommandStackListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setPartName(input.getName());
	}
	
	protected void initializeFileChangeListener() {
		fileChangeListener = new IFileChangeListener() {
			public void deleted(IFile file) {
				IFile current = ((IFileEditorInput)getEditorInput()).getFile();
				if (current.equals(file)) {
					// Close the editor.
					Display display = getSite().getShell().getDisplay();
					display.asyncExec(new Runnable() {
						public void run() {
							getSite().getPage().closeEditor(BPELMultipageEditorPart.this, false);
						}
					});
				}
			}
			public void moved(IFile source, final IFile destination) {
//				IFile current = ((IFileEditorInput) getEditorInput()).getFile();
//				if (!current.equals(source)) {
//					return;
//				}
//				// update editors input
//				final IFileEditorInput input = new FileEditorInput(destination);
//				Display display = getDetailsEditor().getSite().getShell().getDisplay();
//				display.syncExec(new Runnable() {
//					public void run() {
//						getBPELDetailsEditor().setInput(input);
//						setInput(input);
//					}
//				});
//				// update resources
//				IPath path = destination.getFullPath();
//				URI uri = URI.createPlatformResourceURI(path.toString());
//				processResource.setURI(uri);
//				// JM: Comment out. We don't want to re-name the process just because
//				// the file name has changed
////				display.syncExec(new Runnable() {
////					public void run() {
////						BPELUtil.updateNameAndNamespace(destination, process);
////					}
////				});
//				path = path.removeFileExtension().addFileExtension(IBPELUIConstants.EXTENSION_MODEL_EXTENSIONS);
//				URI extensionsUri = URI.createPlatformResourceURI(path.toString());
//				extensionsResource = resourceSet.createResource(extensionsUri);
//				extensionsResource.setURI(extensionsUri);
//				try {
//					// JM: Comment out for now. We should re-test this
////					processResource.save(Collections.EMPTY_MAP);
////					destination.refreshLocal(IResource.DEPTH_ZERO, null);
//				} catch (Exception e) {
//					BPELUIPlugin.log(e);
//				}
			}
		};
		BPELUIPlugin.INSTANCE.getResourceChangeListener().addListener(fileChangeListener);
	}

	
	/**
	 * Installs the refactoring listener
	 */
	protected void initializeRefactoringListener() {
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		postBuildRefactoringListener = new IResourceChangeListener() {
			public void resourceChanged(IResourceChangeEvent event) {
				IFile newFile = ((FileEditorInput)getEditorInput()).getFile();
				final IResourceDelta bpelFileDelta = event.getDelta().findMember(newFile.getFullPath());
				// we only care about the change if it is a move or a rename
				if (bpelFileDelta != null && (bpelFileDelta.getFlags() & IResourceDelta.MOVED_FROM) != 0) {
					getSite().getShell().getDisplay().syncExec(new Runnable() {
						public void run() {
							doRevertToSaved(null);
						}
					});
				}
			}
		};
		workspace.addResourceChangeListener(postBuildRefactoringListener, IResourceChangeEvent.POST_BUILD);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	protected void loadModel() {
		Document structuredDocument = null;
		
		try {
			IDocument doc = fTextEditor.getDocumentProvider().getDocument(getEditorInput());
			if (doc instanceof IStructuredDocument) {
				IStructuredModel model = StructuredModelManager.getModelManager().getExistingModelForEdit(doc);
				if (model == null) {
					model = StructuredModelManager.getModelManager().getModelForEdit((IStructuredDocument) doc);
				}
				if (model != null) {
					structuredDocument = ((IDOMModel) model).getDocument();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}    
		
		HashMap<String, Document> loadOptions = null; 
		if (structuredDocument != null) {
			loadOptions = new HashMap<String, Document> (1);
			loadOptions.put("DOMDocument", structuredDocument);
		}
		
		//FIXME WSDLEditor has gef command stack; in order to have gef command stack we need to add design page first 
		BPELEditModelClient editModelClient = new BPELEditModelClient(this, ((IFileEditorInput) getEditorInput()).getFile(), this, loadOptions);
		fDesignViewer.setEditModelClient(editModelClient);
		getEditDomain().setCommandStack(editModelClient.getCommandStack());

		Resource bpelResource = editModelClient.getPrimaryResourceInfo().getResource();
		IFile file = getFileInput();
		BPELReader reader = new BPELReader();
	    reader.read(bpelResource, file, fDesignViewer.getResourceSet());
	    process = reader.getProcess();
	    
	    bpelModelReconcileAdapter = new BPELModelReconcileAdapter (structuredDocument, process, bpelResource, fDesignViewer);
	    
	    if (getEditDomain() != null) {
	    	((BPELEditDomain)getEditDomain()).setProcess(getProcess());
	    }
	    extensionsResource = reader.getExtensionsResource();
	    extensionMap = reader.getExtensionMap();

	    modelListenerAdapter = new ModelListenerAdapter();
	    modelListenerAdapter.setExtensionMap(extensionMap);
	}

	public void modelDeleted(ResourceInfo resourceInfo) {
		if (!isDirty()) {
			getSite().getPage().closeEditor(this, false);
		}
	}
	
	public void modelDirtyStateChanged(ResourceInfo resourceInfo) {
		firePropertyChange(PROP_DIRTY);
	}
	
	public void modelLocationChanged(ResourceInfo resourceInfo, IFile movedToFile) {
		// TODO!
		//updateInputFile(movedToFile.getFullPath());
	}

	public void modelReloaded(ResourceInfo resourceInfo) {
		Resource bpelResource = fDesignViewer.getEditModelClient().getPrimaryResourceInfo().getResource();

		IFile file = getFileInput();
		BPELReader reader = new BPELReader();
	    reader.read(bpelResource, file, fDesignViewer.getResourceSet());
		 
	    process = reader.getProcess();
	    if (getEditDomain() != null) {
	    	((BPELEditDomain)getEditDomain()).setProcess(getProcess());
	    }
	    extensionMap = reader.getExtensionMap();
	    
		modelListenerAdapter.setExtensionMap(fDesignViewer.getExtensionMap());
	
		fDesignViewer.getGraphicalViewer().setContents(getProcess());

		// The ProcessTrayEditPart tries to remove its selection listener on deactivate.
		// In this case, it will fail because the edit part can't find the editor because
		// the process no longer belongs to a resource. Help it out and remove the
		// listener manually.
		ProcessTrayEditPart processTrayEditPart = (ProcessTrayEditPart)fDesignViewer.getTrayViewer().getContents();
		fDesignViewer.getGraphicalViewer().removeSelectionChangedListener(processTrayEditPart.getSelectionChangedListener());
		
		fDesignViewer.getTrayViewer().setContents(getProcess());
		
		updateMarkersHard();
	}
	
	protected void updateMarkersHard () {
		
		for(EObject obj : fMarkers2EObject.values()) {
			obj.eNotify(fMarkersStale);
		}
		
		fMarkers2EObject.clear();
		
		IMarker[] markers = null;
		IFile file = getFileInput();
		Resource resource = getProcess().eResource();
		
		try {
			markers = file.findMarkers(null, true, IResource.DEPTH_ZERO);							
		} catch (CoreException ex) {
			BPELUIPlugin.log(ex);
			return;
		}
		
		for (IMarker m : markers) {
			
			String href = null;
			EObject target = null;
			try {
				href = (String) m.getAttribute( "address.model" ); //$NON-NLS-1$
				if (href == null) {
					continue;
				}
				target = resource.getEObject(href);
			} catch (CoreException ex) { 
				continue;
			}
			
			if (target == null) {
				continue;
			}
			
			fMarkers2EObject.put(m.getId(), target);			
			target.eNotify( new NotificationImpl (AdapterNotification.NOTIFICATION_MARKER_ADDED , null, m ));						
		}
					
	}


	@Override
	protected void pageChange(int newPageIndex) {
		currentPage = newPageIndex;
		super.pageChange(newPageIndex);
	}


	/**
	 * Sets the EditDomain for this EditorPart.
	 * @param ed the domain
	 */
	protected void setEditDomain(DefaultEditDomain ed) {
		this.editDomain = ed;
	}

	protected void showPropertiesView() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
		try {
			page.showView(IBPELUIConstants.PROPERTY_VIEW_ID);
		} catch (PartInitException e) {
			BPELUIPlugin.log(e);
		}
	}
	
	/**
	 * The editor part name should be the same as the one appearing in the logical view.
	 */
	protected void updateTitle() {
		setPartName(getProcess().getName());
	}

	public void modelMarkersChanged ( ResourceInfo resourceInfo , IMarkerDelta [] markerDelta ) {
		
		Resource resource = resourceInfo.getResource();
					
		for ( IMarkerDelta delta : markerDelta ) {
		
			String href = (String) delta.getAttribute( "address.model" ); //$NON-NLS-1$
			if (href == null) {
				continue;
			}
			
			EObject target = null;
			
			switch (delta.getKind()) {
			case IResourceDelta.ADDED :
				target = resource.getEObject(href);
				if (target != null) {
					fMarkers2EObject.put(delta.getId(),target);
					target.eNotify( new NotificationImpl(AdapterNotification.NOTIFICATION_MARKER_ADDED,null,delta.getMarker() ));
				}
				break;
			case IResourceDelta.CHANGED :
				target = fMarkers2EObject.remove(delta.getId());
				if (target != null) {
					target.eNotify( new NotificationImpl(AdapterNotification.NOTIFICATION_MARKER_CHANGED,delta.getMarker(),null));
				}
				break;
			case IResourceDelta.REMOVED :
				target = fMarkers2EObject.remove(delta.getId());
				if (target != null) {
					target.eNotify( new NotificationImpl(AdapterNotification.NOTIFICATION_MARKER_DELETED,delta.getMarker(),null));
				}
				break;				
			}
			
		}
	}

	@Override
	public boolean isDirty() {
		return fTextEditor.isDirty();
	}

	@Override
	public IEditorPart getActiveEditor() {
		return super.getActiveEditor();
	}
}
