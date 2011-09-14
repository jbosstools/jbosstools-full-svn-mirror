/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor;

import static org.jboss.tools.vpe.xulrunner.util.XPCOM.queryInterface;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.bindings.Binding;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.internal.keys.WorkbenchKeyboard;
import org.eclipse.ui.keys.IBindingService;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wst.sse.core.internal.model.ModelLifecycleEvent;
import org.eclipse.wst.sse.core.internal.provisional.IModelLifecycleListener;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.view.events.INodeSelectionListener;
import org.eclipse.wst.sse.ui.internal.view.events.ITextSelectionListener;
import org.eclipse.wst.sse.ui.internal.view.events.NodeSelectionChangedEvent;
import org.eclipse.wst.sse.ui.internal.view.events.TextSelectionChangedEvent;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.common.el.core.ELReferenceList;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.event.XModelTreeEvent;
import org.jboss.tools.common.model.event.XModelTreeListener;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.ui.dnd.ModelTransfer;
import org.jboss.tools.common.model.ui.util.ModelUtilities;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.model.util.XModelTreeListenerSWTSync;
import org.jboss.tools.common.resref.core.ResourceReferenceListListener;
import org.jboss.tools.jst.jsp.bundle.BundleMap;
import org.jboss.tools.jst.jsp.editor.IJSPTextEditor;
import org.jboss.tools.jst.jsp.editor.IVisualController;
import org.jboss.tools.jst.jsp.jspeditor.dnd.JSPPaletteInsertHelper;
import org.jboss.tools.jst.jsp.preferences.VpePreference;
import org.jboss.tools.jst.jsp.selection.SelectionHelper;
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;
import org.jboss.tools.jst.web.project.WebProject;
import org.jboss.tools.jst.web.tld.URIConstants;
import org.jboss.tools.vpe.VpeDebug;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.dnd.VpeDnD;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.menu.VpeMenuCreator;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.mozilla.MozillaEventAdapter;
import org.jboss.tools.vpe.editor.mozilla.listener.MozillaAfterPaintListener;
import org.jboss.tools.vpe.editor.mozilla.listener.MozillaContextMenuListener;
import org.jboss.tools.vpe.editor.mozilla.listener.MozillaKeyListener;
import org.jboss.tools.vpe.editor.mozilla.listener.MozillaMouseListener;
import org.jboss.tools.vpe.editor.mozilla.listener.MozillaResizeListener;
import org.jboss.tools.vpe.editor.mozilla.listener.MozillaScrollListener;
import org.jboss.tools.vpe.editor.mozilla.listener.MozillaSelectionListener;
import org.jboss.tools.vpe.editor.mozilla.listener.MozillaTooltipListener;
import org.jboss.tools.vpe.editor.selection.VpeSelectionController;
import org.jboss.tools.vpe.editor.template.IKeyEventHandler;
import org.jboss.tools.vpe.editor.template.ISelectionManager;
import org.jboss.tools.vpe.editor.template.IZoomEventManager;
import org.jboss.tools.vpe.editor.template.KeyEventManager;
import org.jboss.tools.vpe.editor.template.SelectionManager;
import org.jboss.tools.vpe.editor.template.VpeIncludeList;
import org.jboss.tools.vpe.editor.template.VpeTemplateListener;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.editor.template.ZoomEventManager;
import org.jboss.tools.vpe.editor.toolbar.format.FormatControllerManager;
import org.jboss.tools.vpe.editor.util.Constants;
import org.jboss.tools.vpe.editor.util.DocTypeUtil;
import org.jboss.tools.vpe.editor.util.SelectionUtil;
import org.jboss.tools.vpe.editor.util.VisualDomUtil;
import org.jboss.tools.vpe.editor.util.VpeDebugUtil;
import org.jboss.tools.vpe.editor.util.VpeNodesManagingUtil;
import org.jboss.tools.vpe.handlers.PageDesignOptionsHandler;
import org.jboss.tools.vpe.handlers.PreferencesHandler;
import org.jboss.tools.vpe.handlers.RefreshHandler;
import org.jboss.tools.vpe.handlers.RotateEditorsHandler;
import org.jboss.tools.vpe.handlers.ShowBorderHandler;
import org.jboss.tools.vpe.handlers.ShowBundleAsELHandler;
import org.jboss.tools.vpe.handlers.ShowNonVisualTagsHandler;
import org.jboss.tools.vpe.handlers.ShowTextFormattingHandler;
import org.jboss.tools.vpe.handlers.VisualPartAbstractHandler;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.jboss.tools.vpe.resref.core.AbsoluteFolderReferenceList;
import org.jboss.tools.vpe.resref.core.CSSReferenceList;
import org.jboss.tools.vpe.resref.core.RelativeFolderReferenceList;
import org.jboss.tools.vpe.resref.core.TaglibReferenceList;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMKeyEvent;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMMutationEvent;
import org.mozilla.interfaces.nsIDOMNSUIEvent;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsISelection;
import org.mozilla.interfaces.nsISelectionDisplay;
import org.mozilla.interfaces.nsISelectionListener;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class VpeController implements INodeAdapter,
		IModelLifecycleListener, INodeSelectionListener,
		ITextSelectionListener, SelectionListener,
		VpeTemplateListener, XModelTreeListener,
		ResourceReferenceListListener, ISelectionChangedListener,
		IVisualController, MozillaMouseListener, MozillaKeyListener,
		MozillaTooltipListener, MozillaSelectionListener,
		MozillaContextMenuListener, MozillaResizeListener,
		MozillaAfterPaintListener, MozillaScrollListener {

	public static final int DEFAULT_UPDATE_DELAY_TIME = 400;
	private boolean visualEditorVisible = true;
	private boolean synced = true;
	StructuredTextEditor sourceEditor;
	private MozillaEditor visualEditor;
	// MozillaBrowser browser;
	XulRunnerEditor xulRunnerEditor;
	// TODO Sergey Vasilyev figure out with nsIPressShell
	// private nsIPresShell presShell;
	private VpeSelectionController visualSelectionController;
	VpeDomMapping domMapping;
	private VpeSourceDomBuilder sourceBuilder;
	private VpeVisualDomBuilder visualBuilder;
	private VpeDnD vpeDnD;
	// private VpeVisualKeyHandler visualKeyHandler;
	private ActiveEditorSwitcher switcher = new ActiveEditorSwitcher();
	private Attr lastRemovedAttr;
	private String lastRemovedAttrName;
	private boolean mouseUpSelectionReasonFlag;
	private boolean sourceChangeFlag;
	private boolean commentNodeChanged;
	private int commentRemoveCount = 0;
	private int commentAddCount = 0;
	private VpePageContext pageContext;
	private VpeEditorPart editPart;
	private BundleMap bundleMap;
	public static final int LEFT_BUTTON = 0;

	private CSSReferenceList cssReferenceListListener;
	private TaglibReferenceList taglibReferenceListListener;
	private ELReferenceList elReferenceListListener;
	private AbsoluteFolderReferenceList absoluteFolderReferenceListListener;
	private RelativeFolderReferenceList relativeFolderReferenceListListener;
	private VpeIncludeList includeList = new VpeIncludeList();
	private FormatControllerManager toolbarFormatControllerManager = null;
	private XModelTreeListenerSWTSync optionsListener;
	// Added by Max Areshkau Fix for JBIDE-1479
	private UIJob job = null;
	private UIJob uiJob;
	// JBIDE-675, visual refresh job
	private UIJob visualRefreshJob;
	private UIJob reinitJob;
	private IZoomEventManager zoomEventManager;
	private VpeDropWindow dropWindow = null;
	private static List<String> vpeCategoryCommands = null;

	/**
	 * Added by Max Areshkau JBIDE-675, stores information about modification
	 * events
	 */
	private LinkedList<VpeEventBean> changeEvents;

	Shell tip;

	/**
	 * selectionManager is used for management of selection
	 */
	private ISelectionManager selectionManager;

	/**
	 * keyEventHandler is used for management of key events
	 */
	private IKeyEventHandler keyEventHandler;

	// contains vpe update delau time in miliseconds
	private int vpeUpdateDelayTime;

	public VpeController(VpeEditorPart editPart) {

		this.editPart = editPart;
		dropWindow = new VpeDropWindow(editPart.getSite().getShell());
	}

	void init(StructuredTextEditor sourceEditor, MozillaEditor visualEditor, BundleMap bundleMap) {
		this.sourceEditor = sourceEditor;
		this.bundleMap = bundleMap;
		if (sourceEditor instanceof IJSPTextEditor) {
			((IJSPTextEditor) sourceEditor).setVPEController(this);
			dropWindow.setEditor((IJSPTextEditor) sourceEditor);
		}
		this.visualEditor = visualEditor;
		visualEditor.setController(this);
		bundleMap.init(sourceEditor.getEditorInput());
		pageContext = new VpePageContext(bundleMap, editPart);
		domMapping = new VpeDomMapping(pageContext);
		sourceBuilder = new VpeSourceDomBuilder(domMapping, this,
				VpeTemplateManager.getInstance(), sourceEditor, pageContext);
		visualBuilder = new VpeVisualDomBuilder(domMapping, this, visualEditor,
				pageContext);
		vpeDnD = new VpeDnD(this, visualEditor);
		visualEditor.getXulRunnerEditor().addSelectionListener(vpeDnD);
		pageContext.setSourceDomBuilder(sourceBuilder);
		pageContext.setVisualDomBuilder(visualBuilder);
		IDOMModel sourceModel = (IDOMModel) getModel();
		if (sourceModel == null) {
			return;
		}
		sourceModel.addModelLifecycleListener(this);

		IEditorInput editorInput = pageContext.getEditPart().getEditorInput();
		// commented by Maksim Areshkau, as fix for
		// https://jira.jboss.org/jira/browse/JBIDE-4534
		if (editorInput instanceof IFileEditorInput) {
			XModel xm = null;
			IProject project = ((IFileEditorInput) editorInput).getFile()
					.getProject();
			IModelNature mn = EclipseResourceUtil.getModelNature(project);
			if (mn != null) {
				xm = mn.getModel();
			}
			if (xm != null) {
				WebProject.getInstance(xm).getTaglibMapping().revalidate(
						WebAppHelper.getWebApp(xm));
			}
		}

		// Fix for JBIDE-5105, JBIDE-5161
		visualEditor.getEditor();

		IDOMDocument sourceDocument = sourceModel.getDocument();
		// FIXED FOR JBIDE-3799 by sdzmitrovich, moved calling of this method to
		// buid dom
		// visualBuilder.refreshExternalLinks();
		visualBuilder.buildDom(sourceDocument);

		VpeTemplateManager.getInstance().addTemplateListener(this);

		xulRunnerEditor = visualEditor.getXulRunnerEditor();
		zoomEventManager = new ZoomEventManager(xulRunnerEditor);
		// TODO Sergey Vasilyev figure out with nsIPressShell
		// presShell = browser.getPresShell();

		// initialization visual selection controller
		visualSelectionController = new VpeSelectionController(visualEditor
				.getEditor().getSelectionController());
		visualSelectionController.setSelectionFlags(nsISelectionDisplay.DISPLAY_ALL);

		selectionManager = new SelectionManager(pageContext, sourceEditor,
				visualSelectionController);

		keyEventHandler = new KeyEventManager(sourceEditor, domMapping,
				pageContext);

		// glory
		ISelectionProvider provider = sourceEditor.getSelectionProvider();
		// Max Areshkau JBIDE-1105 If selection event received after selection
		// in
		// visual part we lost focus of selection, so we should process
		// selection event
		// in time of selection
		// if (provider instanceof IPostSelectionProvider)
		// ((IPostSelectionProvider)
		// provider).addPostSelectionChangedListener(this);
		// else
		provider.addSelectionChangedListener(this);

		// ViewerSelectionManager selectionManager =
		// sourceEditor.getViewerSelectionManager();
		// selectionManager.addNodeSelectionListener(this);
		// selectionManager.addTextSelectionListener(this);
		StyledText textWidget = SelectionHelper
				.getSourceTextWidget(sourceEditor);
		if (textWidget != null) {
			textWidget.addSelectionListener(this);
		}

		registerEventTargets();
		switcher.initActiveEditor();

		if (optionsListener == null) {
			XModelObject optionsObject = ModelUtilities.getPreferenceModel()
					.getByPath(VpePreference.EDITOR_PATH);
			optionsListener = new XModelTreeListenerSWTSync(this);
			optionsObject.getModel().addModelTreeListener(optionsListener);
		}

		cssReferenceListListener = CSSReferenceList.getInstance();
		cssReferenceListListener.addChangeListener(this);

		taglibReferenceListListener = TaglibReferenceList.getInstance();
		taglibReferenceListListener.addChangeListener(this);

		absoluteFolderReferenceListListener = AbsoluteFolderReferenceList
				.getInstance();
		absoluteFolderReferenceListListener.addChangeListener(this);

		relativeFolderReferenceListListener = RelativeFolderReferenceList
				.getInstance();
		relativeFolderReferenceListListener.addChangeListener(this);

		elReferenceListListener = ELReferenceList.getInstance();
		elReferenceListListener.addChangeListener(this);

		// initialization of vpe update delay time
		vpeUpdateDelayTime = DEFAULT_UPDATE_DELAY_TIME;

		// pageContext.fireTaglibsChanged();

		// yradtsevich: we have to refresh VPE selection on init (fix of
		// JBIDE-4037)
		sourceSelectionChanged(true);
		refreshCommands();
	}

	public void dispose() {
		if (job != null) {
			job.cancel();
			job = null;
		}

		if (uiJob != null) {
			uiJob.cancel();
			getChangeEvents().clear();
			uiJob = null;
		}

		if (visualRefreshJob != null) {
			visualRefreshJob.cancel();
			visualRefreshJob = null;
		}

		if (optionsListener != null) {
			XModelObject optionsObject = ModelUtilities.getPreferenceModel()
					.getByPath(VpePreference.EDITOR_PATH);
			optionsObject.getModel().removeModelTreeListener(optionsListener);
			optionsListener.dispose();
			optionsListener = null;
		}
		IDOMModel sourceModel = (IDOMModel) getModel();
		if (sourceModel != null) {
			sourceModel.removeModelLifecycleListener(this);
		}
		switcher.destroyActiveEditor();
		switcher = null;

		VpeTemplateManager.getInstance().removeTemplateListener(this);

		if (visualBuilder != null) {
			visualBuilder.dispose();
			visualBuilder = null;
		}
		sourceBuilder = null;
		if (sourceEditor != null) {
			// glory
			ISelectionProvider provider = sourceEditor.getSelectionProvider();
			provider.removeSelectionChangedListener(this);
			// ViewerSelectionManager selectionManager =
			// sourceEditor.getViewerSelectionManager();
			// selectionManager.removeNodeSelectionListener(this);
			// selectionManager.removeTextSelectionListener(this);
			StyledText textWidget = SelectionHelper
					.getSourceTextWidget(sourceEditor);
			if (textWidget != null) {
				textWidget.removeSelectionListener(this);
			}
			((IJSPTextEditor) sourceEditor).setVPEController(null);

		}
		if (dropWindow != null) {
			dropWindow.setEditor(null);
		}
		if (visualEditor != null) {
			unregisterEventTargets();
			if (visualSelectionController != null) {
				// visualSelectionController.Release();
				visualSelectionController = null;
			}
			// TODO Sergey Vasilyev figure out with Press Shell
			// if (presShell != null) {
			// presShell.Release();
			// presShell = null;
			// }
			visualEditor = null;
		}

		if (cssReferenceListListener != null) {
			cssReferenceListListener.removeChangeListener(this);
		}
		if (taglibReferenceListListener != null) {
			taglibReferenceListListener.removeChangeListener(this);
		}
		if (absoluteFolderReferenceListListener != null) {
			absoluteFolderReferenceListListener.removeChangeListener(this);
		}
		if (elReferenceListListener != null) {
			elReferenceListListener.removeChangeListener(this);
		}
		if (relativeFolderReferenceListListener != null) {
			relativeFolderReferenceListListener.removeChangeListener(this);
		}
		toolbarFormatControllerManager = null;
	}

	private void registerEventTargets() {
		if (visualEditor != null) {
			visualEditor.setResizeListener(this);
			visualEditor.setTooltipListener(this);

			MozillaEventAdapter mozillaEventAdapter
					= visualEditor.getMozillaEventAdapter();
			if (mozillaEventAdapter != null) {
				mozillaEventAdapter.addContextMenuListener(this);
				mozillaEventAdapter.addDndListener(vpeDnD);
				mozillaEventAdapter.addKeyListener(this);
				mozillaEventAdapter.addMouseListener(this);
				mozillaEventAdapter.addSelectionListener(this);
				mozillaEventAdapter.addSelectionListener(vpeDnD);
				mozillaEventAdapter.addAfterPaintListener(this);
				mozillaEventAdapter.addScrollListener(this);
			}
		}
	}

	private void unregisterEventTargets() {
		if (visualEditor != null) {
			visualEditor.setResizeListener(null);
			visualEditor.setTooltipListener(null);
			
			MozillaEventAdapter mozillaEventAdapter
					= visualEditor.getMozillaEventAdapter();
			if (mozillaEventAdapter != null) {
				mozillaEventAdapter.removeContextMenuListener(this);
				mozillaEventAdapter.removeDndListener(vpeDnD);
				mozillaEventAdapter.removeKeyListener(this);
				mozillaEventAdapter.removeMouseListener(this);
				mozillaEventAdapter.removeSelectionListener(this);
				mozillaEventAdapter.removeSelectionListener(vpeDnD);
				mozillaEventAdapter.removeAfterPaintListener(this);
				mozillaEventAdapter.removeScrollListener(this);
			}
		}
	}

	// INodeAdapter implementation
	public boolean isAdapterForType(Object type) {
		return type == this;
	}

	// FIX Fox JBIDE-1479 added by Max Areshkau
	public void notifyChanged(final INodeNotifier notifier,
			final int eventType, final Object feature, final Object oldValue,
			final Object newValue, final int pos) {
		if (!isVisualEditorVisible()) {
			setSynced(false);
			return;
		}
		// start job when we modify file in ui thread, without this code
		// changes will be applied with 1 second delay
		Display display = null;
		if (PlatformUI.isWorkbenchRunning())
			display = PlatformUI.getWorkbench().getDisplay();

		if (display != null && (Thread.currentThread() == display.getThread())) {
			getChangeEvents().addLast(
					new VpeEventBean(notifier, eventType, feature, oldValue,
							newValue, pos));
			if (uiJob == null) {
				uiJob = new UIJob(VpeUIMessages.VPE_UPDATE_JOB_TITLE) {
					@Override
					public IStatus runInUIThread(IProgressMonitor monitor) {
						monitor.beginTask(VpeUIMessages.VPE_UPDATE_JOB_TITLE,
								100);
						while (getChangeEvents().size() > 0) {
							monitor.worked((int) (100 / getChangeEvents()
									.size()));
							VpeEventBean eventBean = getChangeEvents()
									.getFirst();
							if (monitor.isCanceled()) {
								/*
								 * Yahor Radtsevich: the following line is
								 * commented as fix of JBIDE-3758: VPE
								 * autorefresh is broken in some cases. Now if
								 * the change events queue should be cleared,
								 * the user have to do it explicitly.
								 */
								// getChangeEvents().clear();

								return Status.CANCEL_STATUS;
							}
							try {
								notifyChangedInUiThread(
										eventBean.getNotifier(), eventBean
												.getEventType(), eventBean
												.getFeature(), eventBean
												.getOldValue(), eventBean
												.getNewValue(), eventBean
												.getPos());
							} catch (VpeDisposeException ex) {
								// JBIDE-675 we will get this exception if user
								// close editor,
								// when update visual editor job is running, we
								// shoud ignore this
								// exception
								break;
							} catch (NullPointerException ex) {
								if (switcher != null) {
									throw ex;
								} else {
									// class was disposed and exception result
									// of that we can't stop
									// refresh job in time, so we just ignore
									// this exception
								}
							} catch(RuntimeException ex) {
								VpePlugin.getPluginLog().logError(ex);
							}
							getChangeEvents().remove(eventBean);
						}
						// cause is to lock calls others events
						if (switcher != null
								&& switcher
										.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_SOURCE))
							try {
								sourceSelectionChanged();
								/*
								 * https://jira.jboss.org/jira/browse/JBIDE-3619
								 * VpeViewUpdateJob takes place after toolbar
								 * selection have been updated. New nodes
								 * haven't been put into dom mapping thus
								 * toolbar becomes desabled. Updating toolbar
								 * state here takes into account updated vpe
								 * nodes.
								 */
								if (toolbarFormatControllerManager != null) {
									toolbarFormatControllerManager
											.selectionChanged();
								}
							} finally {
								switcher.stopActiveEditor();
							}
						monitor.done();
						return Status.OK_STATUS;
					}
				};
			}

			if (uiJob.getState() != Job.RUNNING) {
				uiJob.setPriority(Job.LONG);
				// Fix of JBIDE-1900
				uiJob.schedule(getVpeUpdateDelayTime());
			} else {
				uiJob.cancel();
				uiJob.schedule(getVpeUpdateDelayTime());
				setVpeUpdateDelayTime(400);
			}

			return;
		}
		// start job when we modify file in non ui thread
		if (job != null) {
			job.cancel();
		}

		job = new UIJob("NotifyChangedJob") { //$NON-NLS-1$
			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				// we checks is job was canceled and if is it true we cancel job
				if (monitor.isCanceled()) {
					return Status.CANCEL_STATUS;
				} else {
					notifyChangedInUiThread(notifier, eventType, feature,
							oldValue, newValue, pos);
				}
				return Status.OK_STATUS;
			}
		};
		job.setPriority(Job.LONG);
		job.schedule(1000L);
	}

	public void notifyChangedInUiThread(INodeNotifier notifier, int eventType,
			Object feature, Object oldValue, Object newValue, int pos) {
		if (switcher == null
				|| !switcher
						.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_SOURCE)) {
			return;
		}
		try {
			if (VpeDebug.PRINT_SOURCE_MUTATION_EVENT) {
				printSourceEvent(notifier, eventType, feature, oldValue,
						newValue, pos);
			}
			if (visualBuilder == null) {
				return;
			}
			// visualBuilder.rebuildFlag = false;
			switch (eventType) {
			case INodeNotifier.CHANGE:
				sourceChangeFlag = true;
				int type = ((Node) notifier).getNodeType();
				visualEditor.hideResizer();
				visualBuilder.clearSelectionRectangle();
				if (type == Node.CDATA_SECTION_NODE) {
					visualBuilder.setCdataText((Node) notifier);					
				} else if (type == Node.TEXT_NODE) {
					boolean update = visualBuilder.setText((Node) notifier);
					visualEditor.showResizer();
					// Added by Max Areshkau JBIDE-1554
					if (!update)
						visualBuilder.updateNode((Node) notifier);
				} else if (type == Node.COMMENT_NODE) {
					if ("yes".equals(VpePreference.SHOW_COMMENTS.getValue())) { //$NON-NLS-1$
						visualBuilder.clearSelectionRectangle();
						visualBuilder.updateNode((Node) notifier);
					}
				} else if (feature != null
						&& ((Node) feature).getNodeType() == Node.ATTRIBUTE_NODE) {
					if (newValue != null) {
						String attrName = ((Attr) feature).getName();
						if ((Attr) feature == lastRemovedAttr
								&& !attrName.equals(lastRemovedAttrName)) {
							lastRemovedAttr = null;
							visualBuilder.removeAttribute((Element) notifier,
									lastRemovedAttrName);
						}
						visualBuilder.setAttribute((Element) notifier,
								((Attr) feature).getName(), (String) newValue);
					} else {
						lastRemovedAttr = (Attr) feature;
						lastRemovedAttrName = ((Attr) feature).getName();
						visualBuilder.removeAttribute((Element) notifier,
								lastRemovedAttrName);
					}
				}
				visualEditor.showResizer();
				break;

			case INodeNotifier.ADD:
				/*
				 * https://jira.jboss.org/jira/browse/JBIDE-4102 Do nothing on
				 * comment node adding, it is already updated in {@code
				 * INodeNotifier.REMOVE} case.
				 */
				if (newValue instanceof Node && Node.COMMENT_NODE != ((Node) newValue).getNodeType()) {
					/*
					 * we should remove all parent nodes from vpe cash
					 */
					visualBuilder.removeNode((Node) newValue);
					commentAddCount--;
				} else if (newValue instanceof Node && Node.COMMENT_NODE == ((Node) newValue).getNodeType()) {
					commentAddCount++;
				}
				break;

			case INodeNotifier.REMOVE:
				/*
				 * https://jira.jboss.org/jira/browse/JBIDE-4102 When comment is
				 * changed there is no need to update its parent or the whole
				 * structure, only the comment node should be updated.
				 */
				if (Node.COMMENT_NODE == ((Node) feature).getNodeType()) {
					commentRemoveCount++;
					visualBuilder.updateNode((Node) feature);
					commentNodeChanged = true;
				} else {
					commentRemoveCount--;
					visualBuilder.stopToggle((Node) feature);
					visualBuilder.removeNode((Node) feature);
				}
				break;

			case INodeNotifier.STRUCTURE_CHANGED:
				/*
				 * https://jira.jboss.org/jira/browse/JBIDE-4102 Do not update
				 * parent tag when a comment was changed,
				 */
				
				/*
				 * https://jira.jboss.org/jira/browse/JBIDE-6067 Update if action
				 * is connected with add or remove comment
				 */
				if (!commentNodeChanged ||(commentNodeChanged && (commentAddCount != 1 || commentRemoveCount != 1))) {
					visualEditor.hideResizer();
					visualBuilder.clearSelectionRectangle();
					visualBuilder.updateNode((Node) notifier);
				} else {
					commentNodeChanged = false;
				}
				commentAddCount = 0;
				commentRemoveCount = 0;
				break;
			case INodeNotifier.CONTENT_CHANGED:
				if (!sourceChangeFlag) {
					if (feature != null
							&& ((Node) feature).getNodeType() == Node.TEXT_NODE) {
						// if
						// (((Node)notifier).getNodeName().equalsIgnoreCase(
						// "style"))
						// {
						visualEditor.hideResizer();
						visualBuilder.clearSelectionRectangle();
						visualBuilder.setText((Node) feature);
						visualEditor.showResizer();
						// }
					}
				} else {
					sourceChangeFlag = false;
				}
				break;
			}
		} finally {
			// fix for jbide-675, swithcer is null when vpecontroller is
			// disposed
			if (switcher != null) {
				switcher.stopActiveEditor();
			} else {
				throw new VpeDisposeException("VpeController already disposed"); //$NON-NLS-1$
			}
		}
	}

	// INodeSelectionListener implementation
	public void nodeSelectionChanged(NodeSelectionChangedEvent event) {
		if (!switcher
				.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_SOURCE)) {
			return;
		}
		try {
			List<?> nodes = event.getSelectedNodes();
			if (nodes != null && nodes.size() > 0) {
				Node sourceNode = (Node) nodes.get(0);
				if (VpeDebug.PRINT_SOURCE_SELECTION_EVENT) {
					System.out
							.println(">>>>>>>>>>>>>> nodeSelectionChanged  sourceNode: " + //$NON-NLS-1$
									sourceNode.getNodeName()
									+ Constants.WHITE_SPACE
									+ event.getCaretPosition());
				}
				if (event.getSource() instanceof IContentOutlinePage) {
					sourceSelectionChanged();
				}
			}
		} finally {
			switcher.stopActiveEditor();
		}
	}

	// ITextSelectionListener implementation
	// TODO Max Areshau looks like this method don't used
	public void textSelectionChanged(TextSelectionChangedEvent event) {
		if (!switcher
				.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_SOURCE)) {
			return;
		}
		try {
			if (VpeDebug.PRINT_SOURCE_SELECTION_EVENT) {
				System.out
						.println(">>>>>>>>>>>>>> textSelectionChanged  " + event.getSource()); //$NON-NLS-1$
			}
			// if (event.getSource() instanceof StyledText) {
			sourceSelectionChanged();
			// }
		} finally {
			switcher.stopActiveEditor();
		}
	}

	// SelectionListener implementation
	public void widgetSelected(SelectionEvent event) {
		if (!switcher
				.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_SOURCE)) {
			return;
		}
		try {
			if (VpeDebug.PRINT_SOURCE_SELECTION_EVENT) {
				System.out.println(">>>>>>>>>>>>>> widgetSelected"); //$NON-NLS-1$
			}
			if (event.getSource() instanceof StyledText) {
				sourceSelectionChanged();
			}
		} finally {
			switcher.stopActiveEditor();
		}
	}

	public void widgetDefaultSelected(SelectionEvent event) {
		if (VpeDebug.PRINT_SOURCE_SELECTION_EVENT) {
			System.out.println(">>>>>>>>>>>>>> widgetDefaultSelected"); //$NON-NLS-1$
		}
	}

	public void sourceSelectionChanged() {
		sourceSelectionChanged(false);
	}

	public void sourceSelectionChanged(boolean showCaret) {
		// we should processed if we have correct view in visual editor,
		// otherwise we shouldn't process this event
		if (getChangeEvents().size() > 0) {
			return;
		}

		// Point range = sourceEditor.getTextViewer().getSelectedRange();
		// int anchorPosition = range.x;
		// int focusPosition = range.x + range.y;
		//
		// boolean extendFlag = range.y != 0;
		// boolean reversionFlag = extendFlag
		// && anchorPosition == VpeSelectionHelper
		// .getCaretOffset(sourceEditor);
		// if (reversionFlag) {
		// anchorPosition = focusPosition;
		// focusPosition = range.x;
		// }

		if (selectionManager != null)
			selectionManager.refreshVisualSelection();

		// VpeTemplate template = TemplateManagingUtil
		// .getTemplateBySourceSelection(pageContext, focusPosition,
		// anchorPosition);
		//
		// if (template instanceof ITemplateSelectionManager) {
		// ((ITemplateSelectionManager) template).setSelectionBySource(
		// pageContext, visualSelectionController, focusPosition,
		// anchorPosition);
		// return;
		// }
		//
		// Node focusNode = getSourceNodeAt(focusPosition);
		// if (focusNode == null) {
		// return;
		// }
		// int focusOffset = getSourceNodeOffset(focusNode, focusPosition,
		// extendFlag && !reversionFlag);
		// Node anchorNode = null;
		// int anchorOffset = 0;
		// if (extendFlag) {
		// anchorNode = getSourceNodeAt(anchorPosition);
		// anchorOffset = getSourceNodeOffset(anchorNode, anchorPosition,
		// reversionFlag);
		// } else {
		// anchorNode = focusNode;
		// anchorOffset = focusOffset;
		// }
		//
		// if (VpeDebug.PRINT_SOURCE_SELECTION_EVENT) {
		//	    System.out.println("sourceSelectionChanged"); //$NON-NLS-1$
		// System.out
		//		    .println("               anchorNode: " + anchorNode.getNodeName() + "  anchorOffset: " + anchorOffset); //$NON-NLS-1$ //$NON-NLS-2$
		// System.out
		//		    .println("               focusNode: " + focusNode.getNodeName() + "  focusOffset: " + focusOffset + "  focusPosition: " + focusPosition); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		// }
		// try {
		// if (anchorNode.getNodeType() == Node.TEXT_NODE
		// || anchorNode.getNodeType() == Node.ATTRIBUTE_NODE) {
		// String text;
		// if (anchorNode.getNodeType() == Node.TEXT_NODE) {
		// IndexedRegion region = (IndexedRegion) anchorNode;
		// text = sourceEditor.getTextViewer().getDocument().get(
		// region.getStartOffset(),
		// region.getEndOffset() - region.getStartOffset());
		// } else {
		// text = ((AttrImpl) anchorNode).getValueRegionText();
		// }
		// anchorOffset = TextUtil.visualPosition(text, anchorOffset);
		// }
		// if (focusNode.getNodeType() == Node.TEXT_NODE
		// || focusNode.getNodeType() == Node.ATTRIBUTE_NODE) {
		// IndexedRegion region = (IndexedRegion) focusNode;
		// String text;
		// if (focusNode.getNodeType() == Node.TEXT_NODE) {
		// text = sourceEditor.getTextViewer().getDocument().get(
		// region.getStartOffset(),
		// region.getEndOffset() - region.getStartOffset());
		// } else {
		// text = ((AttrImpl) focusNode).getValueRegionText();
		// }
		// focusOffset = TextUtil.visualPosition(text, focusOffset);
		// }
		// } catch (Exception ex) {
		// VpePlugin.reportProblem(ex);
		// }
		//
		// selectionBuilder.setVisualSelection(anchorNode, anchorOffset,
		// focusNode, focusOffset, reversionFlag, showCaret);
	}

	public void sourceSelectionToVisualSelection(boolean showCaret) {
		if (!switcher
				.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_SOURCE)) {
			return;
		}
		try {
			sourceSelectionChanged(showCaret);
		} finally {
			switcher.stopActiveEditor();
		}
	}

	// IModelLifecycleListener implementation
	public void processPreModelEvent(ModelLifecycleEvent event) {
	}

	public void processPostModelEvent(ModelLifecycleEvent event) {
		// A part of fix JBIDE-5066
		if ((switcher == null)
				|| (!switcher
						.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_SOURCE))) {
			return;
		}
		try {
			/*
			 * Added by Max Areshkau JBIDE-1457
			 * ModelLifecycleEvent.MODEL_RELEASED is generated when model in
			 * model calls methods releaseFromRead() or releaseFromEdit(). When
			 * editor is open he has only when href on model, so nothing can
			 * generated this event.When editor closes generation of this event
			 * depends from containing any service href on model or not. It's
			 * can be a reason of problems on reopen file.
			 * 
			 * We shouldn't call here rebuild dom.
			 */
			if (event.getType() == ModelLifecycleEvent.MODEL_RELEASED) {
				if (VpeDebug.PRINT_SOURCE_MODEL_LIFECYCLE_EVENT) {
					System.out
							.println(">>> processPostModelEvent: " + event.toString()); //$NON-NLS-1$
				}
				// commented to fix org.mozilla.xpcom.XPCOMException: The
				// function "repaint" returned an error condition (0x8000ffff)
				// visualBuilder.setSelectionRectangle(null);
				IStructuredModel model = event.getModel();
				model.removeModelLifecycleListener(this);
				IDOMModel sourceModel = (IDOMModel) getModel();
				sourceModel.addModelLifecycleListener(this);
				bundleMap.clearAll();
				bundleMap.refresh();
				// visualBuilder.setSelectionRectangle(null);
				IDOMDocument sourceDocument = sourceModel.getDocument();
				// JBIDE-1457
				// visualBuilder.rebuildDom(sourceDocument);
				// pageContext.fireTaglibsChanged();
			}
		} finally {
			// A part of fix JBIDE-5066
			if (switcher != null) {
				switcher.stopActiveEditor();
			}
		}
	}

	// EditorDomEventListener implementation
//	this method is never used
//	public void subtreeModified(nsIDOMMutationEvent mutationEvent) {
//		if (!switcher
//				.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
//			return;
//		}
//		try {
//			if (VpeDebug.PRINT_VISUAL_MUTATION_EVENT) {
//				printVisualEvent(mutationEvent);
//			}
//		} finally {
//			switcher.stopActiveEditor();
//		}
//	}

//	this method is never used
//	public void nodeInserted(nsIDOMMutationEvent mutationEvent) {
//		if (!switcher
//				.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
//			return;
//		}
//		try {
//			if (VpeDebug.PRINT_VISUAL_MUTATION_EVENT) {
//				printVisualEvent(mutationEvent);
//			}
//			nsIDOMNode targetNode = mutationEvent.getRelatedNode();
//			if (!VpeVisualDomBuilder.isAnonElement(targetNode)) {
//				sourceBuilder.addNode(targetNode);
//				visualBuilder.resetPseudoElement(targetNode);
//			}
//		} finally {
//			switcher.stopActiveEditor();
//		}
//	}

//	this method is never used
//	public void nodeRemoved(nsIDOMMutationEvent mutationEvent) {
//		if (!switcher
//				.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
//			return;
//		}
//		try {
//			if (VpeDebug.PRINT_VISUAL_MUTATION_EVENT) {
//				printVisualEvent(mutationEvent);
//			}
//			nsIDOMNode targetNode = VisualDomUtil.getTargetNode(mutationEvent);
//			if (!VpeVisualDomBuilder.isAnonElement(targetNode)) {
//				visualBuilder.setSelectionRectangle(null);
//				sourceBuilder.removeNode(targetNode);
//				visualBuilder.resetPseudoElement(targetNode);
//			}
//		} finally {
//			switcher.stopActiveEditor();
//		}
//	}

	public void nodeRemovedFromDocument(nsIDOMMutationEvent mutationEvent) {
		if (!switcher
				.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			return;
		}
		try {
			if (VpeDebug.PRINT_VISUAL_MUTATION_EVENT) {
				printVisualEvent(mutationEvent);
			}
		} finally {
			switcher.stopActiveEditor();
		}
	}
//	this method is never used
//	public void nodeInsertedIntoDocument(nsIDOMMutationEvent mutationEvent) {
//		if (!switcher
//				.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
//			return;
//		}
//		try {
//			if (VpeDebug.PRINT_VISUAL_MUTATION_EVENT) {
//				printVisualEvent(mutationEvent);
//			}
//		} finally {
//			switcher.stopActiveEditor();
//		}
//	}

//	this method is never used
//	public void attrModified(nsIDOMMutationEvent mutationEvent) {
//		if (!switcher
//				.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
//			return;
//		}
//		try {
//			if (VpeDebug.PRINT_VISUAL_MUTATION_EVENT) {
//				printVisualEvent(mutationEvent);
//			}
//		} finally {
//			switcher.stopActiveEditor();
//		}
//	}

//	this method is never used
//	public void characterDataModified(nsIDOMMutationEvent mutationEvent) {
//		if (!switcher
//				.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
//			return;
//		}
//		try {
//			if (VpeDebug.PRINT_VISUAL_MUTATION_EVENT) {
//				printVisualEvent(mutationEvent);
//			}
//			nsIDOMNode targetNode = VisualDomUtil.getTargetNode(mutationEvent);
//			sourceBuilder.setText(targetNode);
//			visualBuilder.resetPseudoElement(targetNode);
//		} finally {
//			switcher.stopActiveEditor();
//		}
//	}

	public void notifySelectionChanged(nsIDOMDocument doc,
			nsISelection selection, short reason) {
		if (switcher
				.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			try {
				mouseUpSelectionReasonFlag = (reason & nsISelectionListener.MOUSEUP_REASON) != 0;
				if (
						// commited by Dzmitrovich - experimental
						// TODO check selection and if are appear errors then
						// uncommented next code
					    // reason != nsISelectionListener.NO_REASON
						(reason & (nsISelectionListener.KEYPRESS_REASON
								| nsISelectionListener.SELECTALL_REASON
								| nsISelectionListener.MOUSEDOWN_REASON)) != 0) {
					if (VpeDebug.PRINT_VISUAL_SELECTION_EVENT) {
						System.out
								.println("<<< notifySelectionChanged: " + reason); //$NON-NLS-1$
					}
					nsIDOMNode node = SelectionUtil.getSelectedNode(selection);
					/*
					 * Fixes https://jira.jboss.org/jira/browse/JBIDE-2571
					 * Checking if the node is of text type was removed to allow
					 * <select> node to be selected on the first click.
					 */
					if (node != null) {
						selectionManager.setSelection(SelectionUtil.getSelectedNode(selection),
								selection.getFocusOffset(), selection.getAnchorOffset());
					}
				}
				// enables cursor on selection event
				visualSelectionController.setCaretEnabled(true);
			} finally {
				switcher.stopActiveEditor();
			}
		}
	}

	public void mouseDown(nsIDOMMouseEvent mouseEvent) {
		if (!switcher
				.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			return;
		}
		try {
			if (VpeDebug.PRINT_VISUAL_MOUSE_EVENT) {
				System.out.println("<<< mouseDown  targetNode:"); //$NON-NLS-1$
			}
			// selection will be set only if press left button
			if (mouseEvent.getButton() == LEFT_BUTTON) {
				// drag gesture isn't generated in XR 1.9 for Linux Platforms,
				// so we start it's manually
				// mareshkau
				if (vpeDnD.isDragIconClicked(mouseEvent)) {
					vpeDnD.dragStart(mouseEvent);
				} else {
					int rangeOffset = queryInterface(mouseEvent, nsIDOMNSUIEvent.class).getRangeOffset();
					
					// set source selection at the point where mouse is clicked
					selectionManager.setSelection(
							VisualDomUtil.getTargetNode(mouseEvent),
							rangeOffset, rangeOffset);					
				}
			}
		} finally {
			switcher.stopActiveEditor();
		}
	}

	public void mouseUp(nsIDOMMouseEvent mouseEvent) {
		if (!switcher
				.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			return;
		}
		try {
			if (VpeDebug.PRINT_VISUAL_MOUSE_EVENT) {
				System.out.println("<<< mouseUp"); //$NON-NLS-1$
			}
		} finally {
			switcher.stopActiveEditor();
		}
	}

	public void mouseClick(nsIDOMMouseEvent mouseEvent) {
		if (!switcher
				.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			return;
		}
		try {
			nsIDOMNode visualNode = VisualDomUtil.getTargetNode(mouseEvent);
			if (visualNode != null) {
				if (VpeDebug.PRINT_VISUAL_MOUSE_EVENT) {
					System.out.println(MessageFormat.format(
							"<<< mouseClick  visualNode: {0} ({1})", //$NON-NLS-1$
							visualNode.getNodeName(),
							visualNode));
				}
				if (!mouseUpSelectionReasonFlag) {
					if (visualBuilder.isContentArea(visualNode)) {
						// selectionBuilder.setClickContentAreaSelection();
					}
				} else {
					mouseUpSelectionReasonFlag = false;
				}

				Element toggledElement = visualBuilder.doToggle(visualNode);
				if (toggledElement != null) {
					VpeNodeMapping toggledMapping
							= getDomMapping().getNearNodeMapping(toggledElement);
					if (toggledMapping != null && toggledMapping.getVisualNode() != null) {
						selectionManager.setSelection(toggledMapping.getVisualNode(), 0, 0);
					}
				}
			}
		} finally {
			switcher.stopActiveEditor();
		}
	}

	public void mouseDblClick(nsIDOMMouseEvent mouseEvent) {
		if (!switcher
				.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			return;
		}
		try {
			nsIDOMNode visualNode = VisualDomUtil.getTargetNode(mouseEvent);
			if (visualNode != null) {
				sourceBuilder.openOn(visualNode);
				if (VpeDebug.PRINT_VISUAL_MOUSE_EVENT) {
					System.out
							.println("<<< mouseDblClick  visualNode: " + visualNode.getNodeName() + //$NON-NLS-1$
									" (" + visualNode + ")"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		} finally {
			switcher.stopActiveEditor();
		}
	}

	public void mouseMove(nsIDOMMouseEvent mouseEvent) {
		onRefresh();
	}

	public void keyPress(nsIDOMKeyEvent keyEvent) {
		if (VpeDebug.PRINT_VISUAL_KEY_EVENT) {
			System.out.println("<<< keyPress  type: " + keyEvent.getType() + //$NON-NLS-1$
					"  Ctrl: "
					+ keyEvent.getCtrlKey()
					+ "  Shift: " + keyEvent.getShiftKey() + //$NON-NLS-1$ //$NON-NLS-2$
					"  CharCode: " + keyEvent.getCharCode()
					+ "  KeyCode: " + keyEvent.getKeyCode()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (!switcher
				.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			switcher.stopActiveEditor();
			return;
		}
		try {
			visualEditor.hideResizer();
		} finally {
			switcher.stopActiveEditor();
		}
		setVpeUpdateDelayTime(0);
		/*
		 * adding calls of core event handlers, for example' CTR+H' or 'CTRL+M'
		 * event handler dialog
		 */
		boolean keyBindingPressed = false;
		Event keyboardEvent = new Event();
		/*
		 * widget where event occur
		 */
		keyboardEvent.widget = xulRunnerEditor.getBrowser();

		keyboardEvent.stateMask = (keyEvent.getAltKey() ? SWT.ALT : 0)
				| (keyEvent.getCtrlKey() ? SWT.CTRL : 0)
				| (keyEvent.getShiftKey() ? SWT.SHIFT : 0)
				| (keyEvent.getMetaKey() ? SWT.MOD1 : 0);
		keyboardEvent.x = 0;
		keyboardEvent.y = 0;
		keyboardEvent.type = SWT.KeyDown;

		if (keyEvent.getKeyCode() == 0) {
			keyboardEvent.keyCode = (int) keyEvent.getCharCode();
		} else {
			keyboardEvent.keyCode = (int) keyEvent.getKeyCode();
		}
		/*
		 * JBIDE-1627
		 */
		List<KeyStroke> possibleKeyStrokes = WorkbenchKeyboard
				.generatePossibleKeyStrokes(keyboardEvent);
		IWorkbench iWorkbench = VpePlugin.getDefault().getWorkbench();
		if (iWorkbench.hasService(IBindingService.class)) {
			IBindingService iBindingService = (IBindingService) iWorkbench
					.getService(IBindingService.class);

			KeySequence sequenceBeforeKeyStroke = KeySequence.getInstance();

			for (Iterator<KeyStroke> iterator = possibleKeyStrokes.iterator(); iterator
					.hasNext();) {
				KeySequence sequenceAfterKeyStroke = KeySequence.getInstance(
						sequenceBeforeKeyStroke, iterator.next());
				if (iBindingService.isPerfectMatch(sequenceAfterKeyStroke)) {
					final Binding binding = iBindingService
							.getPerfectMatch(sequenceAfterKeyStroke);
					if ((binding != null)
							&& (binding.getParameterizedCommand() != null)
							&& (binding.getParameterizedCommand().getCommand() != null)) {
						keyBindingPressed = true;
					}
				}
			}
		}
		/*
		 * Sends xulrunner event to eclipse environment. dmaliarevich: while
		 * fixing JBIDE-2562 I found that eclipse handles key shortcuts without
		 * this notification.
		 */
		/*	getXulRunnerEditor().getBrowser().notifyListeners(
			keyboardEvent.type, keyboardEvent); */

		/*
		 * Fixes https://jira.jboss.org/jira/browse/JBIDE-2562 
		 * author: dmaliarevich
		 * 
		 * When shortcut key is pressed do not handle this event in the handler.
		 */
		boolean isHandleCurrentEvent = !keyBindingPressed
				|| isZoomEvent(keyEvent);
		if (isHandleCurrentEvent) {
			if (keyEventHandler instanceof KeyEventManager) {
				IZoomEventManager zoomEventManager = ((KeyEventManager) keyEventHandler)
						.getZoomEventManager();
				if (zoomEventManager == null) {
					this.zoomEventManager = new ZoomEventManager(
							getXulRunnerEditor());
					((KeyEventManager) keyEventHandler)
							.setZoomEventManager(this.zoomEventManager);
				}
			}
			if (keyEventHandler.handleKeyPress(keyEvent)) {
				/* JBIDE-2670 */
				keyEvent.preventDefault();
			}
		}
		onRefresh();
	}

	private boolean isZoomEvent(nsIDOMKeyEvent keyEvent) {
		return keyEvent.getCtrlKey()
				&& (keyEvent.getCharCode() == IZoomEventManager.ZOOM_IN_CH_CODE
						|| keyEvent.getCharCode() == IZoomEventManager.ZOOM_OUT_CH_CODE || keyEvent
						.getCharCode() == IZoomEventManager.ZOOM_RESET_CH_CODE);
	}

	public void elementResized(nsIDOMElement element, int constrains, int top,
			int left, int width, int height) {
		if (!switcher
				.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			return;
		}
		try {
			visualEditor.hideResizer();
		} finally {
			switcher.stopActiveEditor();
		}
		visualBuilder.resize(element, constrains, top, left, width, height);
		sourceSelectionChanged();
	}
	
	public void afterPaint(nsIDOMEvent domEvent) {
		onRefresh();
	}
	
	public void editorScrolled(nsIDOMEvent domEvent) {
		onRefresh();
	}

	/**
	 * Calls when on when browser receive context menu event.
	 * 
	 * @param contextFlags
	 *            -not used in this function, just for because this parameter
	 *            exist in nsIContextMenuListener
	 * @param event
	 *            event from browser used here
	 * @param node
	 *            where this event are occur
	 */
	public void onShowContextMenu(long contextFlags, nsIDOMEvent event,
			nsIDOMNode node) {
		Node selectedSourceNode = null;

		VpeNodeMapping nodeMapping = SelectionUtil
				.getNodeMappingBySourceSelection(sourceEditor, domMapping);
		if (nodeMapping != null) {
			selectedSourceNode = nodeMapping.getSourceNode();
		}

		MenuManager menuManager = new MenuManager("#popup"); //$NON-NLS-1$
		final Menu contextMenu = menuManager.createContextMenu(visualEditor
				.getControl());
		contextMenu.addMenuListener(new MenuListener() {
			Menu menu = contextMenu;

			public void menuHidden(MenuEvent e) {
				Display.getCurrent().asyncExec(new Runnable() {
					public void run() {
						menu.dispose();
					}
				});
			}

			public void menuShown(MenuEvent e) {
			}
		});

		new VpeMenuCreator(menuManager, selectedSourceNode).createMenu();
		contextMenu.setVisible(true);
		onRefresh();
	}

	// VpeTemplateListener implementation
	public void templateReloaded() {
		visualRefresh();
	}

	public void visualRefresh() {
		if (!isVisualEditorVisible()) {
			setSynced(false);
			return;
		}
		if (uiJob != null && uiJob.getState() != Job.NONE) {
			return;
		}
		if (visualRefreshJob == null || visualRefreshJob.getState() == Job.NONE) {
			visualRefreshJob = new UIJob(VpeUIMessages.VPE_VISUAL_REFRESH_JOB) {
				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					if (monitor.isCanceled()) {
						return Status.CANCEL_STATUS;
					}
					if (!switcher
							.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_SOURCE)) {
						return Status.CANCEL_STATUS;
					}
					try {
						monitor.beginTask(VpeUIMessages.VPE_VISUAL_REFRESH_JOB,
								IProgressMonitor.UNKNOWN);
						visualRefreshImpl();
						monitor.done();
						setSynced(true);
					} catch (VpeDisposeException exc) {
						// just ignore this exception
					} catch (NullPointerException ex) {
						if (switcher != null) {
							throw ex;
						} else {
							// class was disposed and exception result of
							// that we can't stop
							// refresh job in time, so we just ignore this
							// exception
						}
					} catch (RuntimeException ex) {
						VpePlugin.getPluginLog().logError(ex);
					} finally {
						if (switcher != null) {
							switcher.stopActiveEditor();
						}
					}
					return Status.OK_STATUS;
				}
			};

			visualRefreshJob.setPriority(Job.SHORT);
			visualRefreshJob.schedule();
		}
	}

	void visualRefreshImpl() {
		visualEditor.hideResizer();

		String currentDoctype = DocTypeUtil.getDoctype(visualEditor
				.getEditorInput());
		/*
		 * https://jira.jboss.org/jira/browse/JBIDE-3591 Avoid using missing
		 * resource.
		 */
		String visualEditorDoctype = visualEditor.getDoctype();
		if ((null != currentDoctype) && (null != visualEditorDoctype)
				&& (!visualEditorDoctype.equals(currentDoctype))) {
			visualEditor.reload();
		} else {
			// Fix bugs JBIDE-2750
			visualBuilder.clearSelectionRectangle();
			visualEditor.reload();
			// IDOMModel sourceModel = (IDOMModel) getModel();
			// if (sourceModel != null) {
			// IDOMDocument sourceDocument = sourceModel.getDocument();
			// visualBuilder.rebuildDom(sourceDocument);
			// } else {
			// visualBuilder.rebuildDom(null);
			// }
		}
	}

	public void preLongOperation() {
		switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL);
	}

	public void postLongOperation() {
		switcher.stopActiveEditor();
		visualRefresh();
	}

	// for debug
	private void printSourceEvent(INodeNotifier notifier, int eventType,
			Object feature, Object oldValue, Object newValue, int pos) {
		System.out
				.println(">>> eventType: " + INodeNotifier.EVENT_TYPE_STRINGS[eventType] + //$NON-NLS-1$
						"  pos: " + pos
						+ "  notifier: " + ((Node) notifier).getNodeName() + //$NON-NLS-1$ //$NON-NLS-2$
						"  hashCode: " + notifier.hashCode()); //$NON-NLS-1$
		if (feature != null) {
			if (feature instanceof Node) {
				System.out
						.println("     feature: " + ((Node) feature).getNodeType() + //$NON-NLS-1$
								Constants.WHITE_SPACE
								+ ((Node) feature).getNodeName()
								+ "  hashCode: " + feature.hashCode()); //$NON-NLS-1$
			} else {
				System.out.println("     feature: " + feature); //$NON-NLS-1$
			}
		}
		if (oldValue != null) {
			if (oldValue instanceof Node) {
				System.out
						.println("     oldValue: " + ((Node) oldValue).getNodeName() + //$NON-NLS-1$
								"  hashCode: " + oldValue.hashCode()); //$NON-NLS-1$
			} else {
				System.out.println("     oldValue: " + oldValue); //$NON-NLS-1$
			}
		}
		if (newValue != null) {
			if (newValue instanceof Node) {
				System.out
						.println("     newValue: " + ((Node) newValue).getNodeName() + //$NON-NLS-1$
								"  hashCode: " + newValue.hashCode()
								+ Constants.WHITE_SPACE
								+ ((Node) newValue).getNodeType()); //$NON-NLS-1$
			} else {
				System.out.println("     newValue: " + newValue); //$NON-NLS-1$
			}
		}
	}

	private void printVisualEvent(nsIDOMEvent event) {
		System.out.print("<<< " + event.getType()); //$NON-NLS-1$

		if (event instanceof nsIDOMMutationEvent) {
			nsIDOMMutationEvent mutationEvent = (nsIDOMMutationEvent) event;

			System.out.print("  EventPhase: " + mutationEvent.getEventPhase()); //$NON-NLS-1$

			nsIDOMNode relatedNode = mutationEvent.getRelatedNode();
			System.out
					.print("  RelatedNode: " + (relatedNode == null ? null : relatedNode.getNodeName())); //$NON-NLS-1$

			nsIDOMNode targetNode = VisualDomUtil.getTargetNode(mutationEvent);
			String name = targetNode != null ? targetNode.getNodeName() : null;
			System.out.print("  TargetNode: " + name + " (" + targetNode + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			System.out
					.print("  PrevValue: " + mutationEvent.getPrevValue().trim()); //$NON-NLS-1$
			System.out
					.print("  NewValue: " + mutationEvent.getNewValue().trim()); //$NON-NLS-1$
		}
		System.out.println();
	}

	public class ActiveEditorSwitcher {
		public static final int ACTIVE_EDITOR_CANNOT = 0;
		public static final int ACTIVE_EDITOR_NONE = 1;
		public static final int ACTIVE_EDITOR_SOURCE = 2;
		public static final int ACTIVE_EDITOR_VISUAL = 3;

		private int type = ACTIVE_EDITOR_CANNOT;

		private void initActiveEditor() {
			type = ACTIVE_EDITOR_NONE;
		}

		private void destroyActiveEditor() {
			type = ACTIVE_EDITOR_CANNOT;
		}

		public boolean startActiveEditor(int newType) {
			if (type != ACTIVE_EDITOR_NONE || type == ACTIVE_EDITOR_NONE && newType == ACTIVE_EDITOR_SOURCE
				&& editPart.getVisualMode() == VpeEditorPart.SOURCE_MODE) {
				return false;
			} else {
				type = newType;
				return true;
			}
		}

		public void stopActiveEditor() {
			onRefresh();
			type = ACTIVE_EDITOR_NONE;
		}
	}

	// void refreshBundleValues() {
	// if
	// (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_SOURCE))
	// {
	// return;
	// }
	// try {
	// if (bundle != null) {
	// bundle.refresh();
	// if (pageContext != null) {
	// pageContext.refreshBundleValues();
	// }
	// }
	// } finally {
	// switcher.stopActiveEditor();
	// }
	// }
	//
	void refreshTemplates() {
		if (includeList.includesRefresh()) {
			visualRefresh();
		}

		VpeTemplateManager.getInstance().reload();

		if (bundleMap != null) {
			bundleMap.refresh();
			if (pageContext != null) {
				if (!switcher
						.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_SOURCE)) {
					return;
				}
				try {
					pageContext.refreshBundleValues();
				} finally {
					switcher.stopActiveEditor();
				}
			}
		}
	}

	// implements XModelTreeListener
	public void nodeChanged(XModelTreeEvent event) {
		visualRefresh();
	}

	public void structureChanged(XModelTreeEvent event) {
	}

	// private Node getSourceNodeAt(int offset) {
	// if (sourceEditor != null && getModel() != null) {
	// IndexedRegion node = getModel().getIndexedRegion(offset);
	// if (node instanceof IDOMNode) {
	// VpeElementMapping elementMapping = domMapping
	// .getNearElementMapping((IDOMNode) node);
	// if (elementMapping != null) {
	// if (node instanceof IDOMElement) {
	// IDOMElement element = (IDOMElement) node;
	//
	// if (offset < element.getEndStartOffset()) {
	// NamedNodeMap attrs = element.getAttributes();
	// if (attrs != null) {
	// for (int i = 0; i < attrs.getLength(); i++) {
	// if (attrs.item(i) instanceof AttrImpl) {
	// AttrImpl attr = (AttrImpl) attrs.item(i);
	// if (getSourceAttributeOffset(attr, offset) != -1) {
	// String[] atributeNames =
	// elementMapping.getTemplate().getOutputAtributeNames();
	// if (atributeNames != null
	// && atributeNames.length > 0
	// && attr.getName().equalsIgnoreCase(atributeNames[0])) {
	// return attr;
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	// if (node == null) {
	// node = getModel().getIndexedRegion(offset - 1);
	// }
	// if (node instanceof Node) {
	// return (Node) node;
	// }
	// }
	// return null;
	// }
	//
	// private int getSourceNodeOffset(Node node, int pos, boolean endFlag) {
	// if (node == null)
	// return 0;
	// int start = ((IndexedRegion) node).getStartOffset();
	// int end = ((IndexedRegion) node).getEndOffset();
	//
	// switch (node.getNodeType()) {
	// case Node.ATTRIBUTE_NODE:
	// if (node instanceof AttrImpl) {
	// return getSourceAttributeOffset((AttrImpl) node, pos);
	// }
	// case Node.TEXT_NODE:
	// if (pos < start) {
	// return 0;
	// } else if (pos > end) {
	// return end - start;
	// } else {
	// return pos - start;
	// }
	// case Node.COMMENT_NODE:
	// if (pos > end) {
	// pos = end;
	// }
	// int offset = pos - start - 4;
	// return offset < 0 ? 0 : offset;
	// case Node.ELEMENT_NODE:
	// ElementImpl element = (ElementImpl) node;
	// if (element.isContainer()) {
	// if (pos < element.getStartEndOffset()) {
	// return 0;
	// } else {
	// return 1;
	// }
	// } else {
	// return endFlag ? 1 : 0;
	// }
	// default:
	// return endFlag ? 1 : 0;
	// }
	// }
	//
	// private int getSourceAttributeOffset(AttrImpl attr, int pos) {
	// if (attr.getValueRegion() != null) {
	// int start = attr.getValueRegionStartOffset();
	// String value = attr.getValueRegionText();
	// int len = value.length();
	// if (pos >= start && pos <= start + len) {
	// int offset = pos - start;
	// if (len > 1 && value.charAt(0) == '"'
	// && value.charAt(len - 1) == '"') {
	// if (offset <= 0 || offset >= len) {
	// return -1;
	// }
	// offset--;
	// }
	// return offset;
	// }
	// }
	// return -1;
	// }
	//
	// private int getSourceNodeOffset1(Node node, int pos, boolean endFlag) {
	// if (node == null)
	// return 0;
	// int start = ((IndexedRegion) node).getStartOffset();
	// int end = ((IndexedRegion) node).getEndOffset();
	//
	// switch (node.getNodeType()) {
	// case Node.ATTRIBUTE_NODE:
	// if (node instanceof AttrImpl) {
	// AttrImpl attr = (AttrImpl) node;
	// start = attr.getValueRegionStartOffset();
	// end = start + attr.getValueRegion().getLength();
	// int ret = 0;
	// if (pos > end) {
	// ret = end - start;
	// } else {
	// ret = pos - start;
	// }
	// if (ret > 0 && attr.getValueRegionText().charAt(0) == '"') {
	// ret--;
	// }
	// return ret;
	// }
	// case Node.TEXT_NODE:
	// if (pos < start) {
	// return 0;
	// } else if (pos > end) {
	// return end - start;
	// } else {
	// return pos - start;
	// }
	// case Node.COMMENT_NODE:
	// if (pos > end) {
	// pos = end;
	// }
	// int offset = pos - start - 4;
	// return offset < 0 ? 0 : offset;
	// case Node.ELEMENT_NODE:
	// ElementImpl element = (ElementImpl) node;
	// if (element.isContainer()) {
	// if (pos < element.getStartEndOffset()) {
	// return 0;
	// } else if (pos < element.getStartEndOffset()) {
	// return 1;
	// } else if (pos == element.getStartEndOffset()) {
	// return 2;
	// }
	// } else {
	// return endFlag ? 1 : 0;
	// }
	// default:
	// return endFlag ? 1 : 0;
	// }
	// }

	public class VpeSelectionProvider implements ISelectionProvider {
		VpeSelection selection;

		public VpeSelectionProvider(IndexedRegion region) {
			selection = new VpeSelection(region);
		}

		public VpeSelectionProvider(int position) {
			selection = new VpeSelection(position);
		}

		public VpeSelectionProvider(int offset, int length) {
			selection = new VpeSelection(offset, length);
		}

		public void addSelectionChangedListener(
				ISelectionChangedListener listener) {
		}

		public void removeSelectionChangedListener(
				ISelectionChangedListener listener) {
		}

		public ISelection getSelection() {
			return selection;
		}

		public void setSelection(ISelection selection) {
		}
	}

	class VpeSelection implements ITextSelection {
		String text = ""; //$NON-NLS-1$
		int offset, length;

		public VpeSelection(int position) {
			offset = position;
			length = 0;
		}

		public VpeSelection(int offset, int length) {
			this.offset = offset;
			this.length = length;
			if (length > 0) {

				try {
					text = sourceEditor.getTextViewer().getDocument().get(
							offset, length);
				} catch (BadLocationException e) {
					VpePlugin.getPluginLog().logError(e);
				}

			}
		}

		public VpeSelection(IndexedRegion region) {
			offset = region.getStartOffset();
			length = region.getEndOffset() - offset;
			try {
				text = sourceEditor.getTextViewer().getDocument().get(offset,
						length);
			} catch (BadLocationException ex) {
				VpePlugin.reportProblem(ex);
			}
		}

		public int getEndLine() {
			return 0;
		}

		public int getLength() {
			return length;
		}

		public int getOffset() {
			return offset;
		}

		public int getStartLine() {
			return 0;
		}

		public String getText() {
			return text;
		}

		public boolean isEmpty() {
			return false;
		}
	}

	public void refreshExternalLinks() {
		pageContext.getVisualBuilder().refreshExternalLinks();
	}

	public IPath getPath() {
		if (editPart != null) {
			IEditorInput input = editPart.getEditorInput();
			if (input != null && input instanceof IFileEditorInput) {
				return ((IFileEditorInput) input).getFile().getFullPath();
			}
		}
		return null;
	}

	public void changed(Object source) {
		if (cssReferenceListListener == source) {
			pageContext.getVisualBuilder().refreshExternalLinks();
		} else if (absoluteFolderReferenceListListener == source
				|| relativeFolderReferenceListListener == source
				|| taglibReferenceListListener == source
				|| elReferenceListListener == source) {
			visualRefresh();
		}
	}

// this method is never used	
//	public void dragEnter(nsIDOMEvent event) {
//		if (VpeDebug.PRINT_VISUAL_DRAGDROP_EVENT) {
//			System.out.println("<<<<<<<<<<<<<<<<<<<< DragEnter"); //$NON-NLS-1$
//		}
//	}

// this method is never used
//	public void dragExit(nsIDOMEvent event) {
//		if (VpeDebug.PRINT_VISUAL_DRAGDROP_EVENT) {
//			System.out.println("<<<<<<<<<<<<<<<<<<<< dragExit"); //$NON-NLS-1$
//		}
//		// TODO Sergey Vasilyev figure out with drag caret
//		// xulRunnerEditor.hideDragCaret();
//	}

	public String getTagName(XModelObject object) {
		String tagname = object.getAttributeValue("name"); //$NON-NLS-1$

		XModelObject parent = object.getParent();
		String uri = (parent == null) ? "" : parent.getAttributeValue(URIConstants.LIBRARY_URI); //$NON-NLS-1$
		String defaultPrefix = (parent == null) ? "" : parent.getAttributeValue(URIConstants.DEFAULT_PREFIX); //$NON-NLS-1$

		String[] texts = new String[] { "<" + tagname + ">" }; //$NON-NLS-1$ //$NON-NLS-2$
		JSPPaletteInsertHelper.applyPrefix(texts, sourceEditor, tagname, uri,
				defaultPrefix);
		tagname = texts[0].substring(1, texts[0].length() - 1);

		return tagname;
	}

	public void onShowTooltip(int x, int y, final String text) {

		if (tip != null && !tip.isDisposed())
			tip.dispose();

		Display display = visualEditor.getControl().getDisplay();
		Shell parent = visualEditor.getControl().getShell();

		tip = new Shell(parent, SWT.NO_FOCUS | SWT.ON_TOP);
		Color bckgColor = new Color(tip.getDisplay(), 255, 250, 236);
		tip.setBackground(bckgColor);

		Composite composite = tip;
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		composite.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(gd);

		final StyledText tipControlHeaderText = new StyledText(composite,
				SWT.MULTI | SWT.READ_ONLY);

		tipControlHeaderText.setForeground(bckgColor);
		tipControlHeaderText.setBackground(bckgColor);

		String formatText = text.trim();

		/**
		 * attributeString string containing the pairs attribute and it's value
		 * as one string
		 */
		String[] attributeString = formatText.split("\n"); //$NON-NLS-1$
		/**
		 * buffer string containing the attribute and the value in the different
		 * succeding string
		 */
		String[] buffer = attributeString[0].split(" "); //$NON-NLS-1$

		tipControlHeaderText.setText(buffer[0].toString());

		tipControlHeaderText.addLineStyleListener(new LineStyleListener() {
			public void lineGetStyle(LineStyleEvent event) {
				Color color = new Color(tipControlHeaderText.getDisplay(), 201,
						51, 40);
				if (event.lineOffset == 0) {
					StyleRange st = new StyleRange();
					st.fontStyle = SWT.BOLD;
					st.foreground = color;
					event.styles = new StyleRange[] { st };
					st.start = event.lineOffset;
					st.length = event.lineText.length();
				}
			}
		});

		GridData gridData = new GridData(GridData.BEGINNING
				| GridData.FILL_BOTH);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		tipControlHeaderText.setLayoutData(gridData);

		StringBuilder tempAttr = new StringBuilder();
		StringBuilder tempValue = new StringBuilder();

		if (attributeString.length >= 2) {
			for (int i = 1; i < attributeString.length; i++) {
				buffer = attributeString[i].split(" ", 2); //$NON-NLS-1$
				if (i == 1) {
					tempAttr.append(buffer[0]).append(" "); //$NON-NLS-1$
					tempValue
							.append((buffer.length >= 2 ? buffer[1] : "")).append(" "); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					tempAttr.append("\n").append(buffer[0]).append(" "); //$NON-NLS-1$ //$NON-NLS-2$
					tempValue
							.append(" \n").append(buffer.length >= 2 ? buffer[1] : "").append(" "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
			}

			final StyledText tipControlAttributeText = new StyledText(
					composite, SWT.MULTI | SWT.READ_ONLY);

			tipControlAttributeText.setForeground(bckgColor);
			tipControlAttributeText.setBackground(bckgColor);

			tipControlAttributeText.setText(tempAttr.toString());
			tipControlAttributeText
					.addLineStyleListener(new LineStyleListener() {
						public void lineGetStyle(LineStyleEvent event) {
							Color color = new Color(tipControlHeaderText
									.getDisplay(), 42, 148, 0);
							StyleRange st = new StyleRange();
							st.start = event.lineOffset;
							st.length = event.lineText.length();
							st.foreground = color;
							st.fontStyle = SWT.NORMAL;
							event.styles = new StyleRange[] { st };
						}
					});
			GridData gridData1 = new GridData(GridData.BEGINNING
					| GridData.FILL_BOTH);
			gridData1.horizontalAlignment = GridData.FILL;
			tipControlAttributeText.setLayoutData(gridData1);

			final StyledText tipControlValueText = new StyledText(composite,
					SWT.MULTI | SWT.READ_ONLY);

			tipControlValueText.setBackground(bckgColor);

			tipControlValueText.setText(tempValue.toString());

			GridData gridData2 = new GridData(GridData.BEGINNING
					| GridData.FILL_BOTH);
			gridData2.horizontalAlignment = GridData.FILL;
			tipControlValueText.setLayoutData(gridData2);
		}
		/*
		 * Bug in Mozilla embedded API. Tooltip coordinates are wrong for
		 * elements inside an inline frame (IFrame tag). The workaround is to
		 * position the tooltip based on the mouse cursor location.
		 */
		Point point = display.getCursorLocation();
		/*
		 * Assuming cursor is 21x21 because this is the size of the arrow cursor
		 * on Windows
		 */
		point.y += 21;
		tip.setLocation(point);
		tip.pack();
		tip.setVisible(true);
	}

	public void onHideTooltip() {
		if (tip != null && !tip.isDisposed())
			tip.dispose();
		tip = null;
	}

	public VpePageContext getPageContext() {

		return pageContext;
	}

	public StructuredTextEditor getSourceEditor() {
		return sourceEditor;
	}

	public FormatControllerManager getToolbarFormatControllerManager() {
		return toolbarFormatControllerManager;
	}

	public void setToolbarFormatControllerManager(
			FormatControllerManager formatControllerManager) {
		toolbarFormatControllerManager = formatControllerManager;
	}

	public IStructuredModel getModel() {
		return sourceEditor.getModel();
	}

	public VpeDomMapping getDomMapping() {
		return domMapping;
	}

	public VpeIncludeList getIncludeList() {

		if (includeList == null)
			VpePlugin.getPluginLog().logError("includeList - NULL!!!"); //$NON-NLS-1$

		return includeList;
	}

	public VpeDnD getVpeDnD() {
		return vpeDnD;
	}

	/**
	 * Processed selection events from source editor, if reason of selection is
	 * visial editor, selection will be stopped processing by this condition
	 * (!switcher.startActiveEditor)
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		// FIX for JBIDE-2114
		if (!isVisualEditorVisible()) {
			// selection event doesn't changes a content
			// synced = false;
			return;
		}
		/*
		 * Update Text Formatting Toolbar state
		 */
		if (editPart.getVisualMode() != VpeEditorPart.SOURCE_MODE) {
			if (toolbarFormatControllerManager != null)
				toolbarFormatControllerManager.selectionChanged();
		}

		if (!switcher
				.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_SOURCE)) {
			return;
		}
		try {

			if (VpeDebug.PRINT_SOURCE_SELECTION_EVENT) {
				System.out
						.println(">>>>>>>>>>>>>> selectionChanged  " + event.getSource()); //$NON-NLS-1$
			}
			sourceSelectionChanged();
		} finally {
			switcher.stopActiveEditor();
		}
	}

// this method is never used	
//	// nsIClipboardDragDropHooks implementation
//	public void onPasteOrDrop(nsIDOMMouseEvent mouseEvent, String flavor,
//			String data) {
//		onHideTooltip();
//
//		VpeVisualInnerDropInfo visualDropInfo = selectionBuilder
//				.getInnerDropInfo(mouseEvent);
//		Point range = selectionBuilder.getSourceSelectionRangeAtVisualNode(
//				visualDropInfo.getDropContainer(), (int) visualDropInfo
//						.getDropOffset());
//		VpeSourceInnerDropInfo sourceDropInfo = null;
//
//		XModelObject object = PreferenceModelUtilities.getPreferenceModel()
//				.getModelBuffer().source();
//
//		String tagname = getTagName(object);
//		if (tagname.indexOf("taglib") >= 0)tagname = "taglib"; //$NON-NLS-1$ //$NON-NLS-2$
//		Node sourceDragNode = ((Document) getModel().getAdapter(Document.class))
//				.createElement(tagname);
//		if (visualDropInfo.getDropContainer() != null) {
//			sourceDropInfo = visualBuilder.getSourceInnerDropInfo(
//					sourceDragNode, visualDropInfo, true);
//			range = selectionBuilder.getSourceSelectionRange(sourceDropInfo
//					.getContainer(), sourceDropInfo.getOffset());
//		}
//
//		if (visualDropInfo.getDropContainer() != null) {
//			if (VpeDebug.PRINT_VISUAL_INNER_DRAGDROP_EVENT) {
//				System.out
//						.println("  drop!  container: " + visualDropInfo.getDropContainer().getNodeName()); //$NON-NLS-1$
//			}
//			final String finalFlavor = flavor;
//			final String finalData = data;
//			final Point finalRange = range;
//			final Node finalDropContainer = sourceDropInfo == null ? null
//					: sourceDropInfo.getContainer();
//			Display.getDefault().asyncExec(new Runnable() {
//				public void run() {
//					externalDropAny(finalFlavor, finalData, finalRange,
//							finalDropContainer);
//				}
//			});
//		}
//	}

	public void drop(Node node, Node parentNode, int offset) {
		visualBuilder.innerDrop(node, parentNode, offset);
	}

//	this method is never used
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see org.mozilla.interfaces.nsISupports#queryInterface(java.lang.String)
//	 */
//	public nsISupports queryInterface(String arg0) {
//		return Mozilla.getInstance().queryInterface(this, arg0);
//	}

	/**
	 * Calls when editor content changed, and we should highlight selected element.
	 */
	public void onRefresh() {
		// when we using separate thread to display selection rectangle
		// it's working better than without
		/*
		 * HACK We need wait some time while standart event will be handled and
		 * in process event handles some components are repainted(like buttons)
		 * and flasher are not repainted, so we should paint flasher
		 */

		Display.getDefault().asyncExec(new Thread() {
			public void run() {
				if (getXulRunnerEditor() != null)
					getXulRunnerEditor().redrawSelectionRectangle();
			}
		});
	}

	/**
	 * @return the xulRunnerEditor
	 */
	public XulRunnerEditor getXulRunnerEditor() {
		return xulRunnerEditor;
	}

	/**
	 * @param xulRunnerEditor
	 *            the xulRunnerEditor to set
	 */
	public void setXulRunnerEditor(XulRunnerEditor xulRunnerEditor) {
		this.xulRunnerEditor = xulRunnerEditor;
	}

	public boolean isVisualEditorVisible() {
		return visualEditorVisible;
	}

	public void setVisualEditorVisible(boolean visualEditorVisible) {
		this.visualEditorVisible = visualEditorVisible;
	}

	public boolean isSynced() {
		return synced;
	}

	public void setSynced(boolean synced) {
		this.synced = synced;
	}

	/**
	 * @return the changeEvents
	 */
	public LinkedList<VpeEventBean> getChangeEvents() {

		if (changeEvents == null) {

			changeEvents = new LinkedList<VpeEventBean>();
		}
		return changeEvents;
	}

	public void reinit() {
		if (reinitJob != null) {
			reinitJob.cancel();
		}
		reinitJob = new UIJob(VpeUIMessages.VPE_VISUAL_REFRESH_JOB) {

			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				if (monitor.isCanceled()) {
					return Status.CANCEL_STATUS;
				}
				reinitImpl();
				return Status.OK_STATUS;
			}
		};
		reinitJob.schedule();
	}

	private void reinitImpl() {
		try {
			if (switcher == null
					|| !switcher
							.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_SOURCE)) {
				return;
			}

			// this method must be invoked before any visual
			// node is created, see JBIDE-5105
			visualEditor.reinitDesignMode();

			visualBuilder.clearSelectionRectangle();
			IDOMModel sourceModel = (IDOMModel) getModel();
			if (sourceModel != null) {
				IDOMDocument sourceDocument = sourceModel.getDocument();
				visualBuilder.rebuildDom(sourceDocument);
			} else {
				visualBuilder.rebuildDom(null);
			}
			// reinits selection controller+ controller
			visualSelectionController = new VpeSelectionController(visualEditor
					.getEditor().getSelectionController());

			visualSelectionController.setSelectionFlags(nsISelectionDisplay.DISPLAY_ALL);

			selectionManager = new SelectionManager(pageContext, sourceEditor,
					visualSelectionController);

			keyEventHandler = new KeyEventManager(sourceEditor, domMapping,
					pageContext);
			float currentZoom = zoomEventManager.getCurrentZoom();
			zoomEventManager = new ZoomEventManager(getXulRunnerEditor());
			zoomEventManager.setCurrentZoom(currentZoom);
			((KeyEventManager) keyEventHandler)
					.setZoomEventManager(zoomEventManager);
			// restore selection in visula part
			sourceSelectionChanged();
		} catch (VpeDisposeException ex) {
			// vpe vas closed when refresh job is running, so just
			// ignore this exception
		} catch(RuntimeException ex) {
			VpePlugin.getPluginLog().logError(ex);
		} finally {
			if (switcher != null) {
				switcher.stopActiveEditor();
			}
		}

	}

	public void refreshCommands(){
		ICommandService commandService = (ICommandService) PlatformUI
				.getWorkbench().getService(ICommandService.class);

		//just refresh state of commands
		for (String commandId : getVpeCategoryCommands()) {
			commandService.refreshElements(commandId, null);
		}
	}

	private List<String> getVpeCategoryCommands() {
		ICommandService commandService = (ICommandService) PlatformUI
				.getWorkbench().getService(ICommandService.class);
		
		//init VPE Commands List if its has not been initialized
		if (vpeCategoryCommands == null) {
			vpeCategoryCommands = new ArrayList<String>();
			Command [] definedCommands = commandService.getDefinedCommands();
			for (Command command : definedCommands) {
				try {
					if(VisualPartAbstractHandler.VPE_CATEGORY_ID.equals(command.getCategory().getId())){
						//collecting vpe category commands
						vpeCategoryCommands.add(command.getId());
					}
				} catch (NotDefinedException e) {
					VpePlugin.reportProblem(e);
				}
			}
		}
		
		return vpeCategoryCommands;
	}	

	/**
	 * @return the visualSelectionController
	 */
	public VpeSelectionController getVisualSelectionController() {
		return visualSelectionController;
	}

	/**
	 * 
	 * @return sourceBuilder
	 */
	public VpeSourceDomBuilder getSourceBuilder() {
		return sourceBuilder;
	}

	/**
	 * 
	 * @return visualBuilder
	 */
	public VpeVisualDomBuilder getVisualBuilder() {
		return visualBuilder;
	}

	/**
	 * @return the vpeUpdateDelayTime
	 */
	private int getVpeUpdateDelayTime() {
		return vpeUpdateDelayTime;
	}

	/**
	 * @param vpeUpdateDelayTime
	 *            the vpeUpdateDelayTime to set
	 */
	private void setVpeUpdateDelayTime(int vpeUpdateDelayTime) {
		this.vpeUpdateDelayTime = vpeUpdateDelayTime;
	}

	/**
	 * @return the selectionManager
	 */
	public ISelectionManager getSelectionManager() {
		return selectionManager;
	}
	
	public IZoomEventManager getZoomEventManager() {
		return zoomEventManager;
	}

	public void setZoomEventManager(IZoomEventManager zoomEventManager) {
		this.zoomEventManager = zoomEventManager;
	}

	public ActiveEditorSwitcher getSwitcher() {
		return switcher;
	}

	public VpeDropWindow getDropWindow() {
		return dropWindow;
	}
	
}
