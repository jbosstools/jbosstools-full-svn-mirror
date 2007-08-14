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

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.bindings.Binding;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.Clipboard;
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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.internal.keys.WorkbenchKeyboard;
import org.eclipse.ui.keys.IBindingService;
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
import org.eclipse.wst.xml.core.internal.document.AttrImpl;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.eclipse.wst.xml.core.internal.document.NodeImpl;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.event.XModelTreeEvent;
import org.jboss.tools.common.model.event.XModelTreeListener;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.ui.dnd.ModelTransfer;
import org.jboss.tools.common.model.ui.editor.IModelObjectEditorInput;
import org.jboss.tools.common.model.ui.editors.dnd.DropCommandFactory;
import org.jboss.tools.common.model.ui.editors.dnd.DropData;
import org.jboss.tools.common.model.ui.editors.dnd.IDropCommand;
import org.jboss.tools.common.model.ui.editors.dnd.JSPTagProposalFactory;
import org.jboss.tools.common.model.ui.editors.dnd.context.InnerDragBuffer;
import org.jboss.tools.common.model.ui.objecteditor.ExtendedProperties;
import org.jboss.tools.common.model.ui.objecteditor.ExtendedPropertiesWizard;
import org.jboss.tools.common.model.ui.util.ModelUtilities;
import org.jboss.tools.common.model.ui.views.palette.PaletteInsertHelper;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.model.util.ModelFeatureFactory;
import org.jboss.tools.common.model.util.XModelTreeListenerSWTSync;
import org.jboss.tools.jst.jsp.editor.IJSPTextEditor;
import org.jboss.tools.jst.jsp.editor.IVisualController;
import org.jboss.tools.jst.jsp.preferences.VpePreference;
import org.jboss.tools.jst.web.tld.TLDToPaletteHelper;
import org.jboss.tools.jst.web.tld.TLDUtil;
import org.jboss.tools.jst.web.tld.URIConstants;
import org.jboss.tools.vpe.VpeDebug;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.bundle.BundleMap;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.css.AbsoluteFolderReferenceList;
import org.jboss.tools.vpe.editor.css.CSSReferenceList;
import org.jboss.tools.vpe.editor.css.RelativeFolderReferenceList;
import org.jboss.tools.vpe.editor.css.ResourceReferenceListListener;
import org.jboss.tools.vpe.editor.css.TaglibReferenceList;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.menu.NodeActionManager;
import org.jboss.tools.vpe.editor.menu.BaseActionManager.MyMenuManager;
import org.jboss.tools.vpe.editor.mozilla.EditorDomEventListener;
import org.jboss.tools.vpe.editor.mozilla.MozillaDropInfo;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.selection.VpeSelectionHelper;
import org.jboss.tools.vpe.editor.template.VpeAnyData;
import org.jboss.tools.vpe.editor.template.VpeEditAnyDialog;
import org.jboss.tools.vpe.editor.template.VpeHtmlTemplate;
import org.jboss.tools.vpe.editor.template.VpeIncludeList;
import org.jboss.tools.vpe.editor.template.VpeTemplate;
import org.jboss.tools.vpe.editor.template.VpeTemplateListener;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.editor.toolbar.format.FormatControllerManager;
import org.jboss.tools.vpe.editor.util.MozillaSupports;
import org.jboss.tools.vpe.editor.util.TextUtil;
import org.jboss.tools.vpe.editor.util.VpeDndUtil;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.jboss.tools.vpe.mozilla.browser.MozillaBrowser;
import org.jboss.tools.vpe.mozilla.browser.util.DOMTreeDumper;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.XPCOM;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMEvent;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMKeyEvent;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMMouseEvent;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMMutationEvent;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDragSession;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIPresShell;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsISelection;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsISelectionController;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsISelectionListener;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsISupportsArray;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsITransferable;
import org.jboss.tools.vpe.selbar.SelectionBar;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class VpeController implements INodeAdapter, IModelLifecycleListener, INodeSelectionListener, ITextSelectionListener, SelectionListener, EditorDomEventListener, VpeTemplateListener, XModelTreeListener, ResourceReferenceListListener, ISelectionChangedListener, IVisualController {
	//id of command which is maximazed/minimazed editor
	private static final String MAXIMAZE_PART_ID="org.eclipse.ui.window.maximizePart";
	
	StructuredTextEditor sourceEditor;
	private MozillaEditor visualEditor;
	MozillaBrowser browser;
	private nsIPresShell presShell;
	private nsISelectionController visualSelectionController;
	VpeDomMapping domMapping;
	private VpeTemplateManager templateManager;
	private VpeSourceDomBuilder sourceBuilder;
	public VpeVisualDomBuilder visualBuilder;
	private VpeSelectionBuilder selectionBuilder;
	private VpeVisualKeyHandler visualKeyHandler;
	private ActiveEditorSwitcher switcher = new ActiveEditorSwitcher();
	private Attr lastRemovedAttr;
	private String lastRemovedAttrName;
	private boolean mouseUpSelectionReasonFlag;
	private boolean mouseDownSelectionFlag;
	private boolean sourceChangeFlag;
	private VpePageContext pageContext;
	private BundleMap bundle;
	private VpeEditorPart editPart;
	private static final int AROUND_MENU = 1;
	private static final int BEFORE_MENU = 2;
	private static final int AFTER_MENU = 3;
	
	private CSSReferenceList cssReferenceListListener;
	private TaglibReferenceList taglibReferenceListListener;
	private AbsoluteFolderReferenceList absoluteFolderReferenceListListener;
	private RelativeFolderReferenceList relativeFolderReferenceListListener;
	private VpeIncludeList includeList = new VpeIncludeList();
	private VpeVisualInnerDragInfo innerDragInfo = null;
	private FormatControllerManager toolbarFormatControllerManager = null;
	private SelectionBar selectionBar = null; 
	Shell tip;
	
	public final static String MODEL_FLAVOR = ModelTransfer.MODEL; //$NON-NLS-1$
	
	public VpeController(VpeEditorPart editPart){
		this.editPart = editPart;
		dropWindow = new VpeDropWindow(editPart.getSite().getShell());
	}

	void init(StructuredTextEditor sourceEditor, MozillaEditor visualEditor) throws Exception {
		this.sourceEditor = sourceEditor;
		if(sourceEditor instanceof IJSPTextEditor) {
			((IJSPTextEditor)sourceEditor).setVPEController(this);
			dropWindow.setEditor((IJSPTextEditor)sourceEditor);
		}
		this.visualEditor = visualEditor;
		visualEditor.setController(this);
		templateManager = VpeTemplateManager.getInstance();
		bundle = new BundleMap();
		bundle.init(sourceEditor);
		pageContext = new VpePageContext(templateManager, bundle, editPart);
		domMapping = new VpeDomMapping(pageContext);
		sourceBuilder = new VpeSourceDomBuilder(domMapping, this, templateManager, sourceEditor, pageContext);
		visualBuilder = new VpeVisualDomBuilder(domMapping, this, templateManager, visualEditor, pageContext);
		pageContext.setSourceDomBuilder(sourceBuilder);
		pageContext.setVisualDomBuilder(visualBuilder);
		IDOMModel sourceModel = (IDOMModel)getModel();
		if (sourceModel == null) {
			return;
		}
		sourceModel.addModelLifecycleListener(this);
		IDOMDocument sourceDocument = sourceModel.getDocument();
		visualBuilder.refreshExternalLinks();
		visualBuilder.buildDom(sourceDocument);
		
		templateManager.addTemplateListener(this);

		browser = (MozillaBrowser)visualEditor.getControl();
		presShell = browser.getPresShell();
		visualSelectionController = browser.getSelectionController();
		selectionBuilder = new VpeSelectionBuilder(domMapping, sourceBuilder, visualBuilder, presShell, visualSelectionController);
		visualKeyHandler = new VpeVisualKeyHandler(sourceEditor, domMapping, pageContext, presShell.getFrameSelection()){
			public void doSave(IProgressMonitor monitor){
				editPart.doSave(monitor);
			}
		};

//		glory
		ISelectionProvider provider = sourceEditor.getSelectionProvider();
		provider.addSelectionChangedListener(this);

//		ViewerSelectionManager selectionManager = sourceEditor.getViewerSelectionManager();
//		selectionManager.addNodeSelectionListener(this);
//		selectionManager.addTextSelectionListener(this);
		StyledText textWidget = VpeSelectionHelper.getSourceTextWidget(sourceEditor);
		if (textWidget != null) {
			textWidget.addSelectionListener(this);
		}

		visualEditor.setEditorDomEventListener(this);
		switcher.initActiveEditor();
		
	   	XModelObject optionsObject = ModelUtilities.getPreferenceModel().getByPath(VpePreference.EDITOR_PATH);
		XModelTreeListenerSWTSync optionsListener = new XModelTreeListenerSWTSync(this);
		optionsObject.getModel().addModelTreeListener(optionsListener);
		

		cssReferenceListListener = CSSReferenceList.getInstance();
		cssReferenceListListener.addChangeListener(this);

		taglibReferenceListListener = TaglibReferenceList.getInstance();
		taglibReferenceListListener.addChangeListener(this);

		absoluteFolderReferenceListListener = AbsoluteFolderReferenceList.getInstance();
		absoluteFolderReferenceListListener.addChangeListener(this);

		relativeFolderReferenceListListener = RelativeFolderReferenceList.getInstance();
		relativeFolderReferenceListListener.addChangeListener(this);
		
		pageContext.fireTaglibsChanged();
	}

	void dispose() {
		switcher.destroyActiveEditor();
		
		if (templateManager != null) {
			templateManager.removeTemplateListener(this);
		}
		if (visualBuilder != null) {
			visualBuilder.dispose();
			visualBuilder = null;
		}
		if (sourceEditor != null) {
//glory
			ISelectionProvider provider = sourceEditor.getSelectionProvider();
			provider.removeSelectionChangedListener(this);
//			ViewerSelectionManager selectionManager = sourceEditor.getViewerSelectionManager();
//			selectionManager.removeNodeSelectionListener(this);
//			selectionManager.removeTextSelectionListener(this);
			StyledText textWidget = VpeSelectionHelper.getSourceTextWidget(sourceEditor);
			if (textWidget != null) {
				textWidget.removeSelectionListener(this);
			}
		}
		if (visualEditor != null) {
			visualEditor.setEditorDomEventListener(null);
			if (visualSelectionController != null) {
//				visualSelectionController.Release();
				visualSelectionController = null;
			}
			if (presShell != null) {
				presShell.Release();
				presShell = null;
			}
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
		if (relativeFolderReferenceListListener != null) {
			relativeFolderReferenceListListener.removeChangeListener(this);
		}
	}

	// INodeAdapter implementation
	public boolean isAdapterForType(Object type) {
		return type == this;
	}

	public void notifyChanged(INodeNotifier notifier, int eventType, Object feature, Object oldValue, Object newValue, int pos) {
//		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_SOURCE)) {
//			return;
//		}
		if (VpeDebug.printSourceMutationEvent) {
			printSourceEvent(notifier, eventType, feature, oldValue, newValue, pos);
		}
		if(visualBuilder==null) {
			return;
		}
		visualBuilder.rebuildFlag = false;
		
		switch (eventType) {
		case INodeNotifier.CHANGE:
			sourceChangeFlag = true;
			int type = ((Node)notifier).getNodeType();
			visualEditor.hideResizer();
			visualBuilder.setSelectionRectangle(null);
			if (type == Node.TEXT_NODE) {
				visualBuilder.setText((Node)notifier);
				visualEditor.showResizer();
			} else if (type == Node.COMMENT_NODE) {
				if("yes".equals(VpePreference.SHOW_COMMENTS.getValue())) { //$NON-NLS-1$
					visualBuilder.setSelectionRectangle(null);
					visualBuilder.updateNode((Node)notifier);
				}
			} else if (feature != null && ((Node)feature).getNodeType() == Node.ATTRIBUTE_NODE) {
				if (newValue != null) {
					String attrName = ((Attr)feature).getName();
					if ((Attr)feature == lastRemovedAttr && !attrName.equals(lastRemovedAttrName)) {
						lastRemovedAttr = null;
						visualBuilder.removeAttribute((Element)notifier, lastRemovedAttrName);
					}
					visualBuilder.setAttribute((Element)notifier, ((Attr)feature).getName(), (String)newValue);
				} else {
					lastRemovedAttr = (Attr)feature;
					lastRemovedAttrName = ((Attr)feature).getName();
					visualBuilder.removeAttribute((Element)notifier, lastRemovedAttrName);
				}
			}
			visualEditor.showResizer();
			break;

		case INodeNotifier.ADD:
			break;
		
		case INodeNotifier.REMOVE:
			visualBuilder.stopToggle((Node)feature);
			visualBuilder.removeNode((Node)feature);
			break;
			
		case INodeNotifier.STRUCTURE_CHANGED:
			visualEditor.hideResizer();
			visualBuilder.setSelectionRectangle(null);
			visualBuilder.updateNode((Node)notifier);
			break;

		case INodeNotifier.CONTENT_CHANGED:
			if (!sourceChangeFlag) {
				if (feature != null && ((Node)feature).getNodeType() == Node.TEXT_NODE) {
					//if (((Node)notifier).getNodeName().equalsIgnoreCase("style")) {
						visualEditor.hideResizer();
						visualBuilder.setSelectionRectangle(null);
						visualBuilder.setText((Node)feature);
						visualEditor.showResizer();
					//}
				}
			} else {
				sourceChangeFlag = false;
			}
			break;
		}
		if (visualBuilder.rebuildFlag) {
			pageContext.fireTaglibsChanged();
		} else if (pageContext.isTaglibChanged()) {
			visualRefreshImpl();
			pageContext.fireTaglibsChanged();
		}
		switcher.stopActiveEditor();
	}

	// INodeSelectionListener implementation
	public void nodeSelectionChanged(NodeSelectionChangedEvent event) {
		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_SOURCE)) {
			return;
		}
		List nodes = event.getSelectedNodes();
		if (nodes != null && nodes.size() > 0) {
			Node sourceNode = (Node)nodes.get(0);
			if (VpeDebug.printSourceSelectionEvent) {
				System.out.println(">>>>>>>>>>>>>> nodeSelectionChanged  sourceNode: " + sourceNode.getNodeName() + "  " + event.getCaretPosition()); //$NON-NLS-1$ //$NON-NLS-2$
			}
			if (event.getSource() instanceof IContentOutlinePage) {
				sourceSelectionChanged();
			}
		}
		switcher.stopActiveEditor();
	}

	// ITextSelectionListener implementation
	public void textSelectionChanged(TextSelectionChangedEvent event) {
		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_SOURCE)) {
			return;
		}
		if (VpeDebug.printSourceSelectionEvent) {
			System.out.println(">>>>>>>>>>>>>> textSelectionChanged  " + event.getSource()); //$NON-NLS-1$
		}
//		if (event.getSource() instanceof StyledText) {
			sourceSelectionChanged();
//		}
		switcher.stopActiveEditor();
	}

	// SelectionListener implementation
	public void widgetSelected(SelectionEvent event) {
		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_SOURCE)) {
			return;
		}
		if (VpeDebug.printSourceSelectionEvent) {
			System.out.println(">>>>>>>>>>>>>> widgetSelected"); //$NON-NLS-1$
		}
		if (event.getSource() instanceof StyledText) {
			sourceSelectionChanged();
		}
		switcher.stopActiveEditor();
	}

	public void widgetDefaultSelected(SelectionEvent event) {
		if (VpeDebug.printSourceSelectionEvent) {
			System.out.println(">>>>>>>>>>>>>> widgetDefaultSelected"); //$NON-NLS-1$
		}
	}
	
	public void sourceSelectionChanged() {
		sourceSelectionChanged(false);
	}
	
	public void sourceSelectionChanged(boolean showCaret) {
		Point range = sourceEditor.getTextViewer().getSelectedRange();
		int anchorPosition = range.x;
		int focusPosition = range.x + range.y;
		boolean extendFlag = range.y != 0;
		boolean reversionFlag = extendFlag && anchorPosition == VpeSelectionHelper.getCaretOffset(sourceEditor);
		if (reversionFlag) {
			anchorPosition = focusPosition;
			focusPosition = range.x;
		}
		Node focusNode = getSourceNodeAt(focusPosition);
		if (focusNode == null) {
			return;
		}
		int focusOffset = getSourceNodeOffset(focusNode, focusPosition, extendFlag && !reversionFlag);
		Node anchorNode = null;
		int anchorOffset = 0;
		if (extendFlag) {
			anchorNode = getSourceNodeAt(anchorPosition);
			anchorOffset = getSourceNodeOffset(anchorNode, anchorPosition, reversionFlag);
		} else {
			anchorNode = focusNode;
			anchorOffset = focusOffset;
		}

		if (VpeDebug.printSourceSelectionEvent) {
			System.out.println("sourceSelectionChanged"); //$NON-NLS-1$
			System.out.println("               anchorNode: " + anchorNode.getNodeName() + "  anchorOffset: " + anchorOffset); //$NON-NLS-1$ //$NON-NLS-2$
			System.out.println("               focusNode: " + focusNode.getNodeName() + "  focusOffset: " + focusOffset + "  focusPosition: " + focusPosition); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		try{
			if(anchorNode.getNodeType() == Node.TEXT_NODE || anchorNode.getNodeType() == Node.ATTRIBUTE_NODE){
				String text;
				if (anchorNode.getNodeType() == Node.TEXT_NODE) {
					IndexedRegion region = (IndexedRegion)anchorNode;
					text = sourceEditor.getTextViewer().getDocument().get(region.getStartOffset(), region.getEndOffset()-region.getStartOffset());
				} else {
					text = ((AttrImpl)anchorNode).getValueRegionText();
				}
				anchorOffset = TextUtil.visualPosition(text, anchorOffset);
			}
			if(focusNode.getNodeType() == Node.TEXT_NODE || focusNode.getNodeType() == Node.ATTRIBUTE_NODE){
				IndexedRegion region = (IndexedRegion)focusNode;
				String text;
				if (focusNode.getNodeType() == Node.TEXT_NODE) {
					text = sourceEditor.getTextViewer().getDocument().get(region.getStartOffset(), region.getEndOffset()-region.getStartOffset());
				} else {
					text = ((AttrImpl)focusNode).getValueRegionText();
				}
				focusOffset = TextUtil.visualPosition(text, focusOffset);
			}
		}catch(Exception ex){
			VpePlugin.reportProblem(ex);
		}
		
		selectionBuilder.setVisualSelection(anchorNode, anchorOffset, focusNode, focusOffset, reversionFlag, showCaret);
	}
	
	public void sourceSelectionToVisualSelection(boolean showCaret) {
		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_SOURCE)) {
			return;
		}
		sourceSelectionChanged(showCaret);
		switcher.stopActiveEditor();
	}
	
	private void sourceSelectionChanged1() {
		Point range = sourceEditor.getTextViewer().getSelectedRange();
		int anchorPosition = range.x;
		int focusPosition = range.x + range.y;
		boolean extendFlag = range.y != 0;
		boolean reversionFlag = extendFlag && anchorPosition == VpeSelectionHelper.getCaretOffset(sourceEditor);
		if (reversionFlag) {
			anchorPosition = focusPosition;
			focusPosition = range.x;
		}
		Node focusNode = getSourceNodeAt(focusPosition);
		if (focusNode == null) {
			return;
		}
		int focusOffset = getSourceNodeOffset1(focusNode, focusPosition, !reversionFlag);
		Node anchorNode = null;
		int anchorOffset = 0;
		if (extendFlag) {
			anchorNode = getSourceNodeAt(anchorPosition);
			anchorOffset = getSourceNodeOffset1(anchorNode, anchorPosition, reversionFlag);
		} else {
			anchorNode = focusNode;
			anchorOffset = focusOffset;
		}

		if (VpeDebug.printSourceSelectionEvent) {
			System.out.println("sourceSelectionChanged1"); //$NON-NLS-1$
			System.out.println("               anchorNode: " + anchorNode.getNodeName() + "  anchorOffset: " + anchorOffset); //$NON-NLS-1$ //$NON-NLS-2$
			System.out.println("               focusNode: " + focusNode.getNodeName() + "  focusOffset: " + focusOffset + "  focusPosition: " + focusPosition); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		try{
			if(anchorNode.getNodeType() == Node.TEXT_NODE || anchorNode.getNodeType() == Node.ATTRIBUTE_NODE){
				IndexedRegion region = (IndexedRegion)anchorNode;
				String text;
				if (anchorNode.getNodeType() == Node.TEXT_NODE) {
					text = sourceEditor.getTextViewer().getDocument().get(region.getStartOffset(), region.getEndOffset()-region.getStartOffset());
				} else {
					text = ((AttrImpl)anchorNode).getValueRegionText();
				}
				anchorOffset = TextUtil.visualPosition(text, anchorOffset);
			}
			if(focusNode.getNodeType() == Node.TEXT_NODE || focusNode.getNodeType() == Node.ATTRIBUTE_NODE){
				IndexedRegion region = (IndexedRegion)focusNode;
				String text;
				if (focusNode.getNodeType() == Node.TEXT_NODE) {
					text = sourceEditor.getTextViewer().getDocument().get(region.getStartOffset(), region.getEndOffset()-region.getStartOffset());
				} else {
					text = ((AttrImpl)focusNode).getValueRegionText();
				}
				focusOffset = TextUtil.visualPosition(text, focusOffset);
			}
		}catch(Exception ex){
			VpePlugin.reportProblem(ex);
		}

		if (anchorOffset == 2 && anchorNode == focusNode && anchorNode.getNodeType() == Node.ELEMENT_NODE) {
			selectionBuilder.setVisualSelection(anchorNode, anchorOffset, focusNode, focusOffset, reversionFlag, false, true);
		} else {
			selectionBuilder.setVisualSelection(anchorNode, anchorOffset, focusNode, focusOffset, reversionFlag, false, true);
		}
//		toolbarFormatControllerManager.selectionChanged();
	}
	
	// IModelLifecycleListener implementation
	public void processPreModelEvent(ModelLifecycleEvent event) {
	}

	public void processPostModelEvent(ModelLifecycleEvent event) {
		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_SOURCE)) {
			return;
		}
		if (event.getType() == ModelLifecycleEvent.MODEL_RELEASED) {
			if (VpeDebug.printSourceModelLifecycleEvent) {
				System.out.println(">>> processPostModelEvent: " + event.toString()); //$NON-NLS-1$
			}
			visualBuilder.setSelectionRectangle(null);
			IStructuredModel model = event.getModel();
			model.removeModelLifecycleListener(this);
			IDOMModel sourceModel = (IDOMModel)getModel();
			sourceModel.addModelLifecycleListener(this);
			bundle.clearAll();
			bundle.refresh();
			visualBuilder.setSelectionRectangle(null);
			IDOMDocument sourceDocument = sourceModel.getDocument();
			visualBuilder.rebuildDom(sourceDocument);
			pageContext.fireTaglibsChanged();
		}
		switcher.stopActiveEditor();
	}

	// EditorDomEventListener implementation
	public void subtreeModified(nsIDOMMutationEvent mutationEvent) {
		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			return;
		}
		if (VpeDebug.printVisualMutationEvent) {
			printVisualEvent(mutationEvent);
		}
		switcher.stopActiveEditor();
	}

	public void nodeInserted(nsIDOMMutationEvent mutationEvent) {
		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			return;
		}
		if (VpeDebug.printVisualMutationEvent) {
			printVisualEvent(mutationEvent);
		}
		Node targetNode = mutationEvent.getTargetNode();
		if (!VpeVisualDomBuilder.isAnonElement(targetNode)) {
			sourceBuilder.addNode(targetNode);
			visualBuilder.resetPseudoElement(targetNode);
		} else {
			MozillaSupports.release(targetNode);
		}
		switcher.stopActiveEditor();
	}

	public void nodeRemoved(nsIDOMMutationEvent mutationEvent) {
		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			return;
		}
		if (VpeDebug.printVisualMutationEvent) {
			printVisualEvent(mutationEvent);
		}
		Node targetNode = mutationEvent.getTargetNode();
		if (!VpeVisualDomBuilder.isAnonElement(targetNode)) {
			visualBuilder.setSelectionRectangle(null);
			sourceBuilder.removeNode(targetNode);
			visualBuilder.resetPseudoElement(targetNode);
		}
		//targetNode.Release();
		switcher.stopActiveEditor();
	}

	public void nodeRemovedFromDocument(nsIDOMMutationEvent mutationEvent) {
		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			return;
		}
		if (VpeDebug.printVisualMutationEvent) {
			printVisualEvent(mutationEvent);
		}
		switcher.stopActiveEditor();
	}

	public void nodeInsertedIntoDocument(nsIDOMMutationEvent mutationEvent) {
		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			return;
		}
		if (VpeDebug.printVisualMutationEvent) {
			printVisualEvent(mutationEvent);
		}
		switcher.stopActiveEditor();
	}

	public void attrModified(nsIDOMMutationEvent mutationEvent) {
		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			return;
		}
		if (VpeDebug.printVisualMutationEvent) {
			printVisualEvent(mutationEvent);
		}
		switcher.stopActiveEditor();
	}

	public void characterDataModified(nsIDOMMutationEvent mutationEvent) {
		
		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			return;
		}
		if (VpeDebug.printVisualMutationEvent) {
			printVisualEvent(mutationEvent);
		}
		Node targetNode = mutationEvent.getTargetNode();
		sourceBuilder.setText(targetNode);
		visualBuilder.resetPseudoElement(targetNode);
		MozillaSupports.release(targetNode);
		switcher.stopActiveEditor();
	}

	public void notifySelectionChanged(Document doc, nsISelection selection, int reason) {
		if (switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			mouseUpSelectionReasonFlag = (reason & nsISelectionListener.MOUSEUP_REASON) > 0;
			if (mouseUpSelectionReasonFlag || reason == nsISelectionListener.NO_REASON || reason == nsISelectionListener.KEYPRESS_REASON || reason == nsISelectionListener.SELECTALL_REASON || (reason & nsISelectionListener.MOUSEDOWN_REASON) > 0) {
				if (VpeDebug.printVisualSelectionEvent) {
					System.out.println("<<< notifySelectionChanged: " + reason); //$NON-NLS-1$
				}
				selectionBuilder.setSelection(selection);
			}
			switcher.stopActiveEditor();
		}
//		toolbarFormatControllerManager.selectionChanged();
	}

	public void _mouseDown(nsIDOMMouseEvent mouseEvent) {
		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			return;
		}
		Element visualAppropriateElement = selectionBuilder.getAppropriateElementForSelection(mouseEvent);
		if (visualAppropriateElement != null) {
			Element visualElement = visualBuilder.getDragElement(visualAppropriateElement);
			if (visualElement != null) {
				selectionBuilder.setVisualElementSelection(visualElement);
			}
			MozillaSupports.release(visualAppropriateElement);
		}
		switcher.stopActiveEditor();
	}

	public void mouseDown(nsIDOMMouseEvent mouseEvent) {
		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			return;
		}
		Element visualDragElement = selectionBuilder.getDragElement(mouseEvent);		
		if (VpeDebug.printVisualMouseEvent) {
			Node visualNode = mouseEvent.getTargetNode();
			System.out.println("<<< mouseDown  targetNode: " + visualNode.getNodeName() + " (" + MozillaSupports.getAddress(visualNode) + ")  selectedElement: " + (visualDragElement != null ? visualDragElement.getNodeName() + " (" + MozillaSupports.getAddress(visualDragElement) + ")" : null)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}
		mouseDownSelectionFlag = false;
		if (visualDragElement != null) {
			selectionBuilder.setVisualElementSelection(visualDragElement);
			mouseDownSelectionFlag = true;
		} else {
			selectionBuilder.setCaretAtMouse(mouseEvent);
		}
		switcher.stopActiveEditor();
	}

	public void mouseUp(nsIDOMMouseEvent mouseEvent) {
		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			return;
		}
		if (VpeDebug.printVisualMouseEvent) {
			System.out.println("<<< mouseUp"); //$NON-NLS-1$
		}
		if (mouseDownSelectionFlag) {
			mouseEvent.preventDefault();
			mouseEvent.stopPropagation();
			mouseDownSelectionFlag = false;
		}
		switcher.stopActiveEditor();
	}
	
	public void mouseClick(nsIDOMMouseEvent mouseEvent) {
		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			return;
		}
		Node visualNode = mouseEvent.getTargetNode();
		if (visualNode != null) {
			if (!mouseUpSelectionReasonFlag) {
				if (VpeDebug.printVisualMouseEvent) {
					System.out.println("<<< mouseClick  visualNode: " + visualNode.getNodeName() + " (" + MozillaSupports.getAddress(visualNode) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				if (visualBuilder.isContentArea(visualNode)) {
					selectionBuilder.setClickContentAreaSelection();
////					selectionBuilder.setClickContentAreaSelection(mouseEvent);
				} else {
//					selectionBuilder.setClickSelection(visualNode);
////					selectionBuilder.setClickSelection(mouseEvent);
				}
			} else {
				mouseUpSelectionReasonFlag = false;
			}
			MozillaSupports.release(visualNode);

			if (visualBuilder.doToggle(mouseEvent.getTargetNode())) {
				selectionBuilder.setClickContentAreaSelection();
			}
		}
		switcher.stopActiveEditor(); 
	}

	public void mouseDblClick(nsIDOMMouseEvent mouseEvent) {
		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			return;
		}
		Node visualNode = mouseEvent.getTargetNode();
		if (visualNode != null) {
			if (!sourceBuilder.openBundleEditors(visualNode)) {
			    sourceBuilder.openIncludeEditor(visualNode);
			}
			if (VpeDebug.printVisualMouseEvent) {
				System.out.println("<<< mouseDblClick  visualNode: " + visualNode.getNodeName() + " (" + MozillaSupports.getAddress(visualNode) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}
		switcher.stopActiveEditor();
	}

	public void mouseMove(nsIDOMMouseEvent mouseEvent) {
		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			return;
		}
		Node visualNode = mouseEvent.getTargetNode();
		if (visualNode != null) {
			if (VpeDebug.printVisualMouseEvent) {
				System.out.println("<<< mouseMove  visualNode: " + visualNode.getNodeName() + " (" + MozillaSupports.getAddress(visualNode) + ")");
			}
			visualBuilder.setMoveCursor(mouseEvent);		
		}
		switcher.stopActiveEditor();
	}

	public void _keyPress(nsIDOMKeyEvent keyEvent) {
		if (VpeDebug.printVisualKeyEvent) {
			System.out.println("<<< keyPress  type: " + keyEvent.getType() + "  Ctrl: " + keyEvent.isCtrlKey() + "  Shift: " + keyEvent.isShiftKey() + "  CharCode: " + keyEvent.getCharCode() + "  KeyCode: " + keyEvent.getKeyCode()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}
		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			return;
		}
		visualEditor.hideResizer();
		switcher.stopActiveEditor();

		try {
			if (visualKeyHandler.keyPressHandler(keyEvent)) {
//				switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL);
//				sourceSelectionChanged1();
				visualSelectionController.setCaretEnabled(true);
//				switcher.stopActiveEditor();
			}
		} catch (Exception e) {
			VpePlugin.getPluginLog().logError(e);
			visualRefresh();
		}
	}

	public void keyPress(nsIDOMKeyEvent keyEvent) {
		if (VpeDebug.printVisualKeyEvent) {
			System.out.println("<<< keyPress  type: " + keyEvent.getType() + "  Ctrl: " + keyEvent.isCtrlKey() + "  Shift: " + keyEvent.isShiftKey() + "  CharCode: " + keyEvent.getCharCode() + "  KeyCode: " + keyEvent.getKeyCode()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}

		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
		switcher.stopActiveEditor();
			return;
		}
		
		visualEditor.hideResizer();
		//TODO with this behaviour isn't works 'CTRL+M' 
		switcher.stopActiveEditor();

		
		try {
			if (visualKeyHandler.keyPressHandler(keyEvent)) {
				switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL);
				// Edward
				sourceSelectionChanged(true);
				visualSelectionController.setCaretEnabled(true);
				switcher.stopActiveEditor();
			} else {
				//adding calls of core event handlers, for example 'CTR+H' or 'CTRL+M' event handler dialog
				Event keyboardEvent = new Event ();
				//widget where event occur
				keyboardEvent.widget=browser;
				int rc=0;
				boolean[] aAltKey = new boolean[1];
				boolean[] aCtrlKey = new boolean[1];
				boolean[] aShiftKey = new boolean[1];
				boolean[] aMetaKey = new boolean[1]; 
				
				rc = keyEvent.GetAltKey (aAltKey);
				if (rc != XPCOM.NS_OK) MozillaBrowser.error (rc);
				rc = keyEvent.GetCtrlKey (aCtrlKey);
				if (rc != XPCOM.NS_OK) MozillaBrowser.error (rc);
				rc = keyEvent.GetShiftKey (aShiftKey);
				if (rc != XPCOM.NS_OK) MozillaBrowser.error (rc);
				rc = keyEvent.GetMetaKey (aMetaKey);
				if (rc != XPCOM.NS_OK) MozillaBrowser.error (rc);
							
				keyboardEvent.stateMask = (aAltKey[0] ? SWT.ALT : 0) | (aCtrlKey[0] ? SWT.CTRL : 0) | (aShiftKey[0] ? SWT.SHIFT : 0) | (aMetaKey[0] ? SWT.MOD1 : 0);
				keyboardEvent.x=0;
				keyboardEvent.y=0;
				keyboardEvent.type=SWT.KeyDown;
				
				if(keyEvent.getKeyCode()==0) {
					
					keyboardEvent.keyCode=keyEvent.getCharCode();
				} else{
					
					keyboardEvent.keyCode=keyEvent.getKeyCode();			
				}
				//for maximaze/minimaze command(CTRL+M), we shouldn't call event listeners 
				List possibleKeyStrokes = WorkbenchKeyboard.generatePossibleKeyStrokes(keyboardEvent);
				IWorkbench iWorkbench = VpePlugin.getDefault().getWorkbench();
				if(iWorkbench.hasService(IBindingService.class)){
				IBindingService iBindingService = (IBindingService) iWorkbench.getService(IBindingService.class);

				KeySequence sequenceBeforeKeyStroke = KeySequence.getInstance();
					for (Iterator iterator = possibleKeyStrokes.iterator(); iterator
					.hasNext();){
						KeySequence sequenceAfterKeyStroke = KeySequence.getInstance(
								sequenceBeforeKeyStroke, (KeyStroke) iterator.next());
						if(iBindingService.isPerfectMatch(sequenceAfterKeyStroke)){
							final Binding binding = iBindingService.getPerfectMatch(sequenceAfterKeyStroke);
							
							if((binding!=null)
								&& (binding.getParameterizedCommand()!=null)
								&& (binding.getParameterizedCommand().getCommand()!=null)
								&& (binding.getParameterizedCommand().getCommand().getId()!=null)
								&&binding.getParameterizedCommand().getCommand().getId().equals(VpeController.MAXIMAZE_PART_ID)){
								keyboardEvent.type = SWT.NONE;
							}
						}
					}
				}
				browser.notifyListeners(keyboardEvent.type, keyboardEvent);
				
			}
		} catch (Exception e) {
			VpePlugin.getPluginLog().logError(e);
			visualRefresh();
		}

	}

	public void elementResized(Element element, int resizerConstrains, int top, int left, int width, int height) {
		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			return;
		}
		visualEditor.hideResizer();
		switcher.stopActiveEditor();

		visualBuilder.resize(element, resizerConstrains, top, left, width, height);
		sourceSelectionChanged();
	}
	
	private void createMenuForNode(Node node, MenuManager manager) {
		createMenuForNode(node, manager, false);
	}
	
	private void createMenuForNode(Node node, MenuManager manager, boolean topLevelFlag) {
		NodeActionManager.setTextNodeSplitter(null);
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			VpeElementMapping elementMapping = (VpeElementMapping)domMapping.getNodeMapping(node);
			if (elementMapping != null && elementMapping.getTemplate() != null) {
				manager.add(new VpeAction("<"+node.getNodeName()+"> Attributes", node) {  //$NON-NLS-1$ //$NON-NLS-2$
						public void  run() {
							showProperties(actionNode);								
						}
				});
				
				if (!topLevelFlag) {
					manager.add(new VpeAction("Select This Tag", node) {  //$NON-NLS-1$
						public void  run() {
							selectionBuilder.setVisualSelection(actionNode,0,actionNode,0,false,true);

						}
					});
				}
				Node parent = node.getParentNode();
				if(parent != null && parent.getNodeType() == Node.ELEMENT_NODE){
					MenuManager menuManager = new MenuManager("Parent Tag"); //$NON-NLS-1$
					menuManager.setParent(manager);
					manager.add(menuManager);
					createMenuForNode(parent, menuManager);
				}
				
				manager.add(new Separator());
			}
		}
		NodeActionManager actionManager = new NodeActionManager(getModel(), null);
		
		if (node.getNodeType() == Node.TEXT_NODE) {
			Point range = sourceEditor.getTextViewer().getSelectedRange();
			TextNodeSplitterImpl splitter = new TextNodeSplitterImpl(range, (Text)node);
			NodeActionManager.setTextNodeSplitter(splitter);
		}

		
		if (actionManager != null) {
			StructuredSelection structuredSelection = new StructuredSelection(node);
			actionManager.fillContextMenuForVpe(manager, structuredSelection);
			
		}

		IContributionItem[] items = manager.getItems();
		
		for(int i=0;i< items.length;i++){
			if(items[i] instanceof MenuManager){
				MenuManager mm = (MenuManager)items[i];
				if(NodeActionManager.INSERT_AROUND_MENU.equals(mm.getMenuText())){
					listenContextMenu(mm, (IndexedRegion)node, AROUND_MENU);
				}else if(NodeActionManager.INSERT_BEFORE_MENU.equals(mm.getMenuText())){
					listenContextMenu(mm, (IndexedRegion)node, BEFORE_MENU);
				}else if(NodeActionManager.INSERT_AFTER_MENU.equals(mm.getMenuText())){
					listenContextMenu(mm, (IndexedRegion)node, AFTER_MENU);
				}
			}
		}
		
		manager.add(new Separator());
		
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			VpeElementMapping elementMapping = (VpeElementMapping)domMapping.getNodeMapping(node);
			if (elementMapping != null && elementMapping.getTemplate() != null && elementMapping.getTemplate().getType() == VpeHtmlTemplate.TYPE_ANY) {
				final VpeTemplate selectedTemplate = elementMapping.getTemplate();
				
				manager.add(new VpeAction("Template", node) {  //$NON-NLS-1$
					public void  run() {
						boolean isCorrectNS = pageContext.isCorrectNS(actionNode);
						VpeAnyData data = null;
						if (isCorrectNS) {
							data = selectedTemplate.getAnyData();
							data.setUri(pageContext.getSourceTaglibUri(actionNode));
							data.setName(actionNode.getNodeName());
						}
						data = editAnyData(sourceEditor, isCorrectNS, data);
						if (data != null && data.isChanged()) templateManager.setAnyTemplate(data);
					}
				});

				manager.add(new Separator());
			}
		
		
			manager.add(new VpeTextOperationAction("Cut", ActionFactory.CUT.getId(), (IndexedRegion)node)); //$NON-NLS-1$
			manager.add(new VpeTextOperationAction("Copy", ActionFactory.COPY.getId(), (IndexedRegion)node)); //$NON-NLS-1$
			manager.add(new VpeTextOperationAction("Paste", ActionFactory.PASTE.getId(), (IndexedRegion)node)); //$NON-NLS-1$
		}else if(node.getNodeType() == Node.TEXT_NODE){
			manager.add(new Action("Cut") {  //$NON-NLS-1$
				public void  run() {
					sourceEditor.getAction(ActionFactory.CUT.getId()).run();
				}
			});
			manager.add(new Action("Copy") {  //$NON-NLS-1$
				public void  run() {
					sourceEditor.getAction(ActionFactory.COPY.getId()).run();
				}
			});
			manager.add(new Action("Paste") {  //$NON-NLS-1$
				public void  run() {
					sourceEditor.getAction(ActionFactory.PASTE.getId()).run();
				}
			});
		}
		manager.add(new Separator());
		
		if (actionManager != null) {
			StructuredSelection structuredSelection = node.getNodeType() == Node.ELEMENT_NODE ? new StructuredSelection(node) : null;
			actionManager.addContextMenuForVpe(manager, structuredSelection);
		}
		
		if(node.getNodeType() == Node.ELEMENT_NODE){
			boolean stripEnable = false;
			NodeImpl impl = (NodeImpl)node;
			if(impl.isContainer()){
				NodeList list = impl.getChildNodes();
				if(list.getLength() > 0){
					if(list.getLength() == 1){
						Node child = list.item(0);
						if(child.getNodeType() == Node.TEXT_NODE){
							if("".equals(child.getNodeValue().trim()))stripEnable = false; //$NON-NLS-1$
							else stripEnable = true;
						}else stripEnable = true;
					}else stripEnable = true;
				}
			}
			if(stripEnable)
			manager.add(new VpeAction("Strip Tag", node) {  //$NON-NLS-1$
				public void  run() {
					Node parent = actionNode.getParentNode();
					if(parent != null){
						int index = ((NodeImpl)actionNode).getIndex();
						parent.removeChild(actionNode);
						NodeList children = actionNode.getChildNodes();
						int lengh = children.getLength();
						Node child;
						for(int i=0; i < lengh; i++){
							child = children.item(0);
							actionNode.removeChild(child);
							insertNode(parent, child, index++);
						}
					}
				}
				
				private void insertNode(Node parent, Node node, int index){
					Node oldNode=null;
					int childSize = parent.getChildNodes().getLength();
					
					if(index <= (childSize-1))oldNode = parent.getChildNodes().item(index);
					if(oldNode != null)parent.insertBefore(node, oldNode);
					else parent.appendChild(node);
				}
			});
		}
		if(node.getNodeType() == Node.TEXT_NODE){
			manager.add(new Action("Delete") {  //$NON-NLS-1$
				public void  run() {
					sourceEditor.getAction(ActionFactory.DELETE.getId()).run();
				}
			});
		}

		if (VpeDebug.visualContextMenuDumpSource) {
			manager.add(new Action("Dump Source") { //$NON-NLS-1$
				public void  run() {
					DOMTreeDumper dumper = new DOMTreeDumper();
					dumper.dumpToStream(System.out, visualEditor.getDomDocument());
				}
			});
		}
		
		if (VpeDebug.visualContextMenuDumpMapping) {
			manager.add(new Action("Dump Mapping") {  //$NON-NLS-1$
				public void  run() {
					printMapping();
				}
			});
		}
		
		if (VpeDebug.visualContextMenuTest) {
			manager.add(new VpeAction("Test", node) {  //$NON-NLS-1$
				public void  run() {
					test(actionNode);
				}
			});
		}
	}
	public void onShowContextMenu(int contextFlags, nsIDOMMouseEvent mouseEvent, Node node) {
		Node visualNode = mouseEvent.getTargetNode();
		
		if (visualNode != null) {
			Node selectedSourceNode = null;
			selectedSourceNode = selectionBuilder.setContextMenuSelection(visualNode);
			if (selectedSourceNode != null) {

				MenuManager menuManager = new MenuManager("#popup"); //$NON-NLS-1$
				final Menu contextMenu = menuManager.createContextMenu(visualEditor.getControl());
				contextMenu.addMenuListener(
						new MenuListener(){
							Menu menu = contextMenu;
							public void menuHidden(MenuEvent e) {
								Display.getCurrent().asyncExec(
									new Runnable() {
										public void run() {
											menu.dispose();
										}
									}
								);
							}
							public void menuShown(MenuEvent e) {
							}
						}
				);
				createMenuForNode(selectedSourceNode, menuManager, true);

				contextMenu.setVisible(true);
				
			}
			MozillaSupports.release(visualNode);
		}
	}
	

	private VpeAnyData editAnyData(StructuredTextEditor sourceEditor, boolean isCorrectNS, VpeAnyData data) {
		Shell shell = sourceEditor.getEditorPart().getSite().getShell();
		if (isCorrectNS) {
			VpeEditAnyDialog editDialog = new VpeEditAnyDialog(shell, data);
			editDialog.open();
		} else {
			MessageBox message = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
			message.setMessage(VpeUIMessages.NAMESPACE_NOT_DEFINED);
			message.open();
		}
		return data;
	}

	// VpeTemplateListener implementation
	public void templateReloaded() {
		visualRefresh();
	}
	
	// VpeTaglibListener implementation
//	public void taglibPrefixChanged(String[] prefixs) {
//		if (VpeDebug.printSourceMutationEvent) {
//			String s = ""; //$NON-NLS-1$
//			for (int i = 0; i < prefixs.length; i++) {
//				if (i > 0) {
//					s += ", "; //$NON-NLS-1$
//				}
//				s += prefixs[i];
//			}
//		}
//		queryVisualRefresh = true;
////		visualRefreshImpl();
//	}
	
	public void visualRefresh() {
		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_SOURCE)) {
			return;
		}
		visualRefreshImpl();
		pageContext.fireTaglibsChanged();

		switcher.stopActiveEditor();
	}
	
	void visualRefreshImpl() {
		visualEditor.hideResizer();
		visualBuilder.setSelectionRectangle(null);
		IDOMModel sourceModel = (IDOMModel)getModel();
		if (sourceModel != null) {
			IDOMDocument sourceDocument = sourceModel.getDocument();
			visualBuilder.rebuildDom(sourceDocument);
		} else {
			visualBuilder.rebuildDom(null);
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
	private void printSourceEvent(INodeNotifier notifier, int eventType, Object feature, Object oldValue, Object newValue, int pos) {
		System.out.println(">>> eventType: " + INodeNotifier.EVENT_TYPE_STRINGS[eventType] + "  pos: " + pos + "  notifier: " + ((Node)notifier).getNodeName() + "  hashCode: " + notifier.hashCode()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		if (feature != null) {
			if (feature instanceof Node) {
				System.out.println("     feature: " + ((Node)feature).getNodeType() + "  " + ((Node)feature).getNodeName() + "  hashCode: " + feature.hashCode()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			} else {
				System.out.println("     feature: " + feature); //$NON-NLS-1$
			}
		}
		if (oldValue != null) {
			if (oldValue instanceof Node) {
				System.out.println("     oldValue: " + ((Node)oldValue).getNodeName() + "  hashCode: " + oldValue.hashCode()); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				System.out.println("     oldValue: " + oldValue); //$NON-NLS-1$
			}
		}
		if (newValue != null) {
			if (newValue instanceof Node) {
				System.out.println("     newValue: " + ((Node)newValue).getNodeName() + "  hashCode: " + newValue.hashCode() + "    " + ((Node)newValue).getNodeType()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			} else {
				System.out.println("     newValue: " + newValue); //$NON-NLS-1$
			}
		}
	}

	private void printVisualEvent(nsIDOMEvent event) {
		System.out.print("<<< " + event.getType()); //$NON-NLS-1$

		if (event instanceof nsIDOMMutationEvent) {
			nsIDOMMutationEvent mutationEvent = (nsIDOMMutationEvent)event;

			System.out.print("  EventPhase: " + mutationEvent.getEventPhase()); //$NON-NLS-1$

			Node relatedNode = mutationEvent.getRelatedNode();
			System.out.print("  RelatedNode: " + (relatedNode == null ? null : relatedNode.getNodeName())); //$NON-NLS-1$
			
			Node targetNode = mutationEvent.getTargetNode();
			String name = targetNode != null ? targetNode.getNodeName() : null;
			int address = targetNode != null ? MozillaSupports.getAddress(targetNode) : -1;
			System.out.print("  TargetNode: " + name + " (" + address + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			System.out.print("  PrevValue: " + mutationEvent.getPrevValue().trim()); //$NON-NLS-1$
			System.out.print("  NewValue: " + mutationEvent.getNewValue().trim()); //$NON-NLS-1$
		}
		System.out.println();
	}
	
	private void printMapping() {
		domMapping.printMapping();
	}
	
	private class ActiveEditorSwitcher {
		private static final int ACTIVE_EDITOR_CANNOT = 0;
		private static final int ACTIVE_EDITOR_NONE = 1;
		private static final int ACTIVE_EDITOR_SOURCE = 2;
		private static final int ACTIVE_EDITOR_VISUAL = 3;
		
		private int type = ACTIVE_EDITOR_CANNOT;
		
		private void initActiveEditor() {
			type = ACTIVE_EDITOR_NONE;
		}
		
		private void destroyActiveEditor() {
			type = ACTIVE_EDITOR_CANNOT;
		}
		
		private boolean startActiveEditor(int newType) {
			if (type == ACTIVE_EDITOR_NONE) {
				if( newType == ACTIVE_EDITOR_SOURCE &&
						editPart.getVisualMode() == VpeEditorPart.SOURCE_MODE) {
					return false;
				}
				type = newType;
				return true;
			} else {
				return false;
			}
		}
		
		private void stopActiveEditor() {
			type = ACTIVE_EDITOR_NONE;
		}
	}
	
	private void showProperties(Node node){
		ExtendedProperties p = createExtendedProperties(node);
		if(p != null) ExtendedPropertiesWizard.run(p);
	}
	
	ExtendedProperties createExtendedProperties(Node node) {
		try {
			Class c = ModelFeatureFactory.getInstance().getFeatureClass("org.jboss.tools.jst.jsp.outline.VpeProperties"); //$NON-NLS-1$
			return (ExtendedProperties)c.getDeclaredConstructor(new Class[]{Node.class}).newInstance(new Object[]{node});
		} catch (Exception e) {
			VpePlugin.reportProblem(e);
			return null;
		}
	}
	
	
	private void test(Node node) {
	}

	void refreshBundleValues() {
		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_SOURCE)) {
			return;
		}
		if (bundle != null) {
			bundle.refresh();
			if (pageContext != null) {
				pageContext.refreshBundleValues();
			}
		}
		switcher.stopActiveEditor();
	}

	void refreshTemplates() {
		if (includeList.includesRefresh()) {
			visualRefresh();
		}
		if (templateManager != null) {
			templateManager.reload();
		}
		if (bundle != null) {
			bundle.refresh();
			if (pageContext != null) {
				if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_SOURCE)) {
					return;
				}
				pageContext.refreshBundleValues();
				switcher.stopActiveEditor();
			}
		}
	}
	
	// implements XModelTreeListener
  	 public void nodeChanged(XModelTreeEvent event) {
  	 	visualRefresh();
   	 }
   	 
	 public void structureChanged(XModelTreeEvent event) {
	 }
	 
	 private Node getSourceNodeAt(int offset) {
		if (sourceEditor != null && getModel() != null) {
			IndexedRegion node = getModel().getIndexedRegion(offset);
			if (node instanceof IDOMElement) {
				IDOMElement element = (IDOMElement)node;
				if (offset < element.getEndStartOffset()) {
					NamedNodeMap attrs = element.getAttributes();
					if (attrs != null) {
						for (int i = 0; i < attrs.getLength(); i++) {
							if (attrs.item(i) instanceof AttrImpl) {
								AttrImpl attr = (AttrImpl)attrs.item(i);
								if (getSourceAttributeOffset(attr, offset) != -1) {
									VpeElementMapping elementMapping = domMapping.getNearElementMapping(attr.getOwnerElement());
									if (elementMapping != null) {
										String[] atributeNames = elementMapping.getTemplate().getOutputAtributeNames();
										if (atributeNames != null && atributeNames.length > 0 && attr.getName().equalsIgnoreCase(atributeNames[0])) {
											return attr;
										}
									}
								}
							}
						}
					}
				}
			} 
			if (node == null) {
				node = getModel().getIndexedRegion(offset - 1);
			}
			if (node instanceof Node) {
				return (Node) node;
			}
		}
		return null;
	 }
	 
	 private int getSourceNodeOffset(Node node, int pos, boolean endFlag) {
	 	if (node == null) return 0;
		int start = ((IndexedRegion)node).getStartOffset();
		int end = ((IndexedRegion)node).getEndOffset();
		
		switch (node.getNodeType()) {
		case Node.ATTRIBUTE_NODE:
			if (node instanceof AttrImpl) {
				return getSourceAttributeOffset((AttrImpl)node, pos);
			}
		case Node.TEXT_NODE:
			if (pos < start) {
				return 0;
			} else if (pos > end) {
				return end - start;
			} else {
				return pos - start;
			}
		case Node.COMMENT_NODE:
			if (pos > end) {
				pos = end;
			}
			int offset = pos - start - 4;
			return offset < 0 ? 0 : offset;
		case Node.ELEMENT_NODE:
	 		ElementImpl element = (ElementImpl)node;
	 		if (element.isContainer()) {
		 		if (pos < element.getStartEndOffset()) {
		 			return 0;
		 		} else {
		 			return 1;
		 		}
	 		} else {
		 		return endFlag ? 1 : 0;
	 		}
		default:
			return endFlag ? 1 : 0;
		}
	 }
	 
	private int getSourceAttributeOffset(AttrImpl attr, int pos) {
		if (attr.getValueRegion() != null) {
			int start = attr.getValueRegionStartOffset();
			String value = attr.getValueRegionText();
			int len = value.length();
			if (pos >= start && pos <= start + len) {
				int offset = pos - start;
				if (len > 1 && value.charAt(0) == '"' && value.charAt(len - 1) == '"') {
					if (offset <= 0 || offset >= len) {
						return -1;
					}
					offset--;
				}
				return offset;
			}
		}
		return -1;
	}

	private int getSourceNodeOffset1(Node node, int pos, boolean endFlag) {
	 	if (node == null) return 0;
		int start = ((IndexedRegion)node).getStartOffset();
		int end = ((IndexedRegion)node).getEndOffset();
		
		switch (node.getNodeType()) {
		case Node.ATTRIBUTE_NODE:
			if (node instanceof AttrImpl) {
				AttrImpl attr = (AttrImpl)node; 
				start = attr.getValueRegionStartOffset();
				end = start + attr.getValueRegion().getLength();
				int ret = 0;
				if (pos > end) {
					ret = end - start;
				} else {
					ret = pos - start;
				}
				if (ret > 0 && attr.getValueRegionText().charAt(0) == '"') {
					ret--;
				}
				return ret;
			}
		case Node.TEXT_NODE:
			if (pos < start) {
				return 0;
			} else if (pos > end) {
				return end - start;
			} else {
				return pos - start;
			}
		case Node.COMMENT_NODE:
			if (pos > end) {
				pos = end;
			}
			int offset = pos - start - 4;
			return offset < 0 ? 0 : offset;
		case Node.ELEMENT_NODE:
	 		ElementImpl element = (ElementImpl)node;
	 		if (element.isContainer()) {
		 		if (pos < element.getStartEndOffset()) {
		 			return 0;
		 		} else if (pos < element.getStartEndOffset()) {
		 			return 1;
		 		} else if (pos == element.getStartEndOffset()) {
		 			return 2;
		 		}
	 		} else {
		 		return endFlag ? 1 : 0;
	 		}
		default:
			return endFlag ? 1 : 0;
		}
	}

	 private void listenContextMenu(MenuManager manager, IndexedRegion region, int type){
	 	MenuManager mm = new MyMenuManager("From Palette",true); //$NON-NLS-1$
	 	manager.add(mm);
	 	manager.addMenuListener(new VpeMenuListener(mm, region, type));
	 }
	 
	 class VpeMenuListener implements IMenuListener{
	 	private MenuManager manager;
	 	private IndexedRegion region;
	 	private int type;
	 	private boolean loaded=false;
	 	
	 	public VpeMenuListener(MenuManager manager, IndexedRegion region, int type){
	 		this.manager = manager;
	 		this.region = region;
	 		this.type = type;
	 	}
		public void menuAboutToShow(IMenuManager m) {
			if(loaded)return;
			loaded=true;
			fillContextMenuFromPalette(manager, region, type);
			manager.getParent().update(true);
		}
	 }
	 private MenuManager fillContextMenuFromPalette(MenuManager manager, IndexedRegion region, int type){
		XModelObject model = ModelUtilities.getPreferenceModel().getByPath("%Palette%"); //$NON-NLS-1$
		
		XModelObject[] folders = model.getChildren();
		for(int i=0;i<folders.length;i++){
			if ("yes".equals(folders[i].getAttributeValue("hidden"))) continue; //$NON-NLS-1$ //$NON-NLS-2$
			MenuManager mm = new MenuManager(folders[i].getAttributeValue("name")); //$NON-NLS-1$
			manager.add(mm);
			fillPaletteFolder(mm, region, folders[i], type);
		}
		return manager;
	 }
	 
	 private void fillPaletteFolder(MenuManager menu, IndexedRegion region, XModelObject folder, int type){
		XModelObject[] groups = folder.getChildren();
		for(int i=0;i<groups.length;i++){
			if ("yes".equals(groups[i].getAttributeValue("hidden"))) continue; //$NON-NLS-1$ //$NON-NLS-2$
			MenuManager mm = new MenuManager(groups[i].getAttributeValue("name")); //$NON-NLS-1$
			menu.add(mm);
			fillPaletteGroup(mm, region, groups[i], type);
		}
	 }
	 
	 private void fillPaletteGroup(MenuManager menu, IndexedRegion region, XModelObject group, int type){
		XModelObject[] items = group.getChildren();
		String endText;
		
		for(int i=0;i<items.length;i++){
			if ("yes".equals(items[i].getAttributeValue("hidden"))) continue; //$NON-NLS-1$ //$NON-NLS-2$
			endText = items[i].getAttributeValue("end text"); //$NON-NLS-1$
			
			if(type == AROUND_MENU && (endText == null || "".equals(endText))) continue; //$NON-NLS-1$
			createInsertAction(menu, region, items[i], type);
		}
	 }
	 
	 private void createInsertAction(MenuManager menu, IndexedRegion region, XModelObject item, int type){
		

		XModelObject parent = item.getParent();
		String uri = (parent == null) ? "" : parent.getAttributeValue(URIConstants.LIBRARY_URI); //$NON-NLS-1$
		String defaultPrefix = (parent == null) ? "" : parent.getAttributeValue(URIConstants.DEFAULT_PREFIX); //$NON-NLS-1$
		String tagName = item.getAttributeValue("name"); //$NON-NLS-1$
		String[] texts = new String[]{"<"+tagName+">"}; //$NON-NLS-1$ //$NON-NLS-2$
		if(tagName.indexOf("taglib") < 0) //$NON-NLS-1$
			PaletteInsertHelper.applyPrefix(texts, sourceEditor, tagName, uri, defaultPrefix);
		tagName = texts[0];
		
		menu.add(new InsertAction(tagName, region, item, type));
	 }
	 
	 class InsertAction extends Action{
	 	private XModelObject item;
	 	private int type;
	 	private IndexedRegion region;
	 	
	 	public InsertAction(String title, IndexedRegion region, XModelObject item, int type){
	 		super(title);
	 		this.item = item;
	 		this.type = type;
	 		this.region = region;
	 	}
	 	public void run(){
			try {
				String tagName = item.getAttributeValue("name"); //$NON-NLS-1$
				
				XModelObject parent = item.getParent();
				String uri = (parent == null) ? "" : parent.getAttributeValue(URIConstants.LIBRARY_URI); //$NON-NLS-1$
				String libraryVersion = (parent == null) ? "" : parent.getAttributeValue(URIConstants.LIBRARY_VERSION); //$NON-NLS-1$
				String defaultPrefix = (parent == null) ? "" : parent.getAttributeValue(URIConstants.DEFAULT_PREFIX); //$NON-NLS-1$
				VpeSelectionProvider selProvider = new VpeSelectionProvider(region);

				String startText = "" + item.getAttributeValue("start text"); //$NON-NLS-1$ //$NON-NLS-2$
				String endText = "" + item.getAttributeValue("end text"); //$NON-NLS-1$ //$NON-NLS-2$
				if(type == AROUND_MENU){
				}else if(type == BEFORE_MENU){
					selProvider = new VpeSelectionProvider(region.getStartOffset());
				}else if(type == AFTER_MENU){
					selProvider = new VpeSelectionProvider(region.getEndOffset());
				}
				
				Properties p = new Properties();
				p.setProperty("tag name", tagName); //$NON-NLS-1$
				p.setProperty("start text", startText); //$NON-NLS-1$
				p.setProperty("end text", endText); //$NON-NLS-1$
				p.setProperty("automatically reformat tag body", "" + item.getAttributeValue("automatically reformat tag body")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				p.setProperty(URIConstants.LIBRARY_URI, uri);
				p.setProperty(URIConstants.LIBRARY_VERSION, libraryVersion);
				String addTaglib = item.getParent().getAttributeValue(TLDToPaletteHelper.ADD_TAGLIB);
				p.setProperty(URIConstants.DEFAULT_PREFIX, defaultPrefix);
				p.setProperty(PaletteInsertHelper.PROPOPERTY_ADD_TAGLIB, addTaglib);
				if(((Node)region).getNodeType() == Node.ELEMENT_NODE)
					p.put("selectionProvider", selProvider); //$NON-NLS-1$
				PaletteInsertHelper.insertIntoEditor(sourceEditor.getTextViewer(), p);
			} catch (Exception e) {
				VpePlugin.reportProblem(e);
			}
			
	 	}
	 	
	 }
	 
	 class VpeSelectionProvider implements ISelectionProvider{
	 	VpeSelection selection;
	 	
	 	public VpeSelectionProvider(IndexedRegion region){
	 		selection = new VpeSelection(region);
	 	}
	 	public VpeSelectionProvider(int position){
	 		selection = new VpeSelection(position);
	 	}
	 	public VpeSelectionProvider(int offset, int length){
	 		selection = new VpeSelection(offset, length);
	 	}
	 	
	 	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		}
		public ISelection getSelection() {
			return selection;
		}
		public void removeSelectionChangedListener(ISelectionChangedListener listener) {

		}
		public void setSelection(ISelection selection) {
		}
	 }
	 
	 class VpeSelection implements ITextSelection{
	 	String text=""; //$NON-NLS-1$
	 	int offset, length;

	 	public VpeSelection(int position){
	 		offset = position;
	 		length = 0;
	 	}
	 	
	 	public VpeSelection(int offset, int length){
	 		this.offset = offset;
	 		this.length = length;
	 		if (length > 0) {
		 		try{
		 			text = sourceEditor.getTextViewer().getDocument().get(offset, length);
		 		}catch(Exception ex){
					VpePlugin.reportProblem(ex);
		 		}
	 		}
	 	}
	 	
	 	public VpeSelection(IndexedRegion region){
	 		offset = region.getStartOffset();
	 		length = region.getEndOffset() - offset;
	 		try{
	 			text = sourceEditor.getTextViewer().getDocument().get(offset, length);
	 		}catch(Exception ex){
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

	class VpeAction extends Action{
		public Node actionNode;
		public VpeAction(String name, Node node){
			super(name);
			this.actionNode = node;
		}
	}
	
	class VpeTextOperationAction extends Action{
		private String id;
		private IndexedRegion region;
		public VpeTextOperationAction(String name, String id, IndexedRegion region){
			super(name);
			this.id = id;
			this.region = region;
		}
		public void run(){
			sourceEditor.getSelectionProvider().setSelection(new VpeSelection(region));
			sourceEditor.getAction(id).run();
		}
	}

    public void refreshExternalLinks() {
        pageContext.getVisualBuilder().refreshExternalLinks();
    }

    public IPath getPath() {
        if (editPart != null) {
            IEditorInput input = editPart.getEditorInput();
            if (input != null && input instanceof IFileEditorInput) {
                return ((IFileEditorInput)input).getFile().getFullPath();
            }
        }
        return null;
    }

    public void changed(Object source) {
    	if(cssReferenceListListener == source) {
    		pageContext.getVisualBuilder().refreshExternalLinks();
    	} else if (absoluteFolderReferenceListListener == source ||
    				relativeFolderReferenceListListener == source ||
    				taglibReferenceListListener == source) {
    		visualRefresh();
    	}
    }
    
	public void dragEnter(nsIDOMEvent event) {
		if (VpeDebug.printVisualDragDropEvent) {
			System.out.println("<<<<<<<<<<<<<<<<<<<< DragEnter"); //$NON-NLS-1$
		}
	}

	public void dragExit(nsIDOMEvent event) {
		if (VpeDebug.printVisualDragDropEvent) {
			System.out.println("<<<<<<<<<<<<<<<<<<<< dragExit"); //$NON-NLS-1$
		}
		browser.hideDragCaret();
	}
	
	public void dragOver(nsIDOMEvent event) {
		nsIDragSession dragSession = browser.getCurrentDragSession();
//		int num = 
			dragSession.getNumDropItems();
		boolean isFlavor = dragSession.isDataFlavorSupported(ModelTransfer.MODEL);
//		isFlavor = dragSession.isDataFlavorSupported("CF_UNICODETEXT");
//		isFlavor = dragSession.isDataFlavorSupported(nsITransferable.kUnicodeMime);
//		isFlavor = dragSession.isDataFlavorSupported(nsITransferable.kNativeHTMLMime);

		if (VpeDebug.printVisualDragDropEvent) {
			System.out.println("<<< outerDragOver  isFlavor: " + isFlavor); //$NON-NLS-1$
		}
		
		nsITransferable transferable = browser.getTransferable();
//		transferable.addDataFlavor(ModelTransfer.MODEL);
//		transferable.addDataFlavor("CF_UNICODETEXT");
//		transferable.addDataFlavor("CF_TEXT");

		transferable.addDataFlavor(nsITransferable.kURLDataMime);
		transferable.addDataFlavor(nsITransferable.kFileMime);
		transferable.addDataFlavor(nsITransferable.kURLMime);
//		transferable.addDataFlavor(nsITransferable.kUnicodeMime);

		dragSession.getData(transferable, 0);

		nsISupportsArray array = transferable.flavorsTransferableCanImport();
//		int count = 
			array.count();

		nsISupportsArray array2 = transferable.flavorsTransferableCanExport();
//		int count2 = 
			array2.count();

//		transferable.getTransferData(ModelTransfer.MODEL);
//		transferable.getTransferData("CF_TEXT");

		
		
		transferable.getAnyTransferData();
//		transferable.getTransferData(ModelTransfer.MODEL);
//		num = 
			dragSession.getNumDropItems();
		
		dragSession.setCanDrop(true);
		event.preventDefault();
	}
		
	public void _dragOver(nsIDOMEvent event) {
		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_VISUAL)) {
			return;
		}
		if (VpeDebug.printVisualDragDropEvent) {
			System.out.println("<<<<<<<<<<<<<<<<<<<< dragOver"); //$NON-NLS-1$
		}
		
//		browser.computeDropPosition(event);
		
		boolean canDrop = !browser.isMozillaDragFlaver();
		if (canDrop) {
			Clipboard clipboard = new Clipboard(Display.getCurrent());
			canDrop = clipboard.getContents(ModelTransfer.getInstance()) != null;
		}
		if (canDrop) {
			canDrop = VpeDndUtil.isDropEnabled((IModelObjectEditorInput)sourceEditor.getEditorInput());
		}
		if (canDrop) {
			VpeVisualCaretInfo caretInfo = selectionBuilder.getVisualCaretInfo(event);
			canDrop = caretInfo.exist();
			if (canDrop) {
				caretInfo.showCaret();
			} else {
				caretInfo.hideCaret();
			}
		}
		if (!canDrop) {
			event.stopPropagation();
			event.preventDefault();
		}
		switcher.stopActiveEditor();
	}

	public void drop(nsIDOMEvent event) {
		if (VpeDebug.printVisualDragDropEvent) {
			System.out.println("<<< outerDrop"); //$NON-NLS-1$
		}
		event.preventDefault();
	}

	public void _drop(nsIDOMEvent event) {
		if (VpeDebug.printVisualDragDropEvent) {
			System.out.println("<<<<<<<<<<<<<<<<<<<< dragDrop"); //$NON-NLS-1$
		}
		boolean canDrop = !browser.isMozillaDragFlaver();
		if (canDrop) {
			Clipboard clipboard = new Clipboard(Display.getCurrent());
			canDrop = clipboard.getContents(ModelTransfer.getInstance()) != null;
		}
		if (canDrop) {
			canDrop = VpeDndUtil.isDropEnabled((IModelObjectEditorInput)sourceEditor.getEditorInput());
		}
		if (canDrop) {
			VpeVisualCaretInfo caretInfo = selectionBuilder.getVisualCaretInfo(event);
			canDrop = caretInfo.exist();
			if (canDrop) {
				caretInfo.showCaret();
				caretInfo.hideCaret();
				Point range = caretInfo.getSourceSelectionRange();
				if (VpeDebug.printVisualDragDropEvent) {
					System.out.println("                     Drop Position: " + range.x + "  " + range.y); //$NON-NLS-1$ //$NON-NLS-2$
				}
				VpeDndUtil.drop((IModelObjectEditorInput)sourceEditor.getEditorInput(), (ISourceViewer)sourceEditor.getAdapter(ISourceViewer.class), new VpeSelectionProvider(range.x, range.y));
			}
		}
		browser.hideDragCaret();
		if (!canDrop) {
			event.stopPropagation();
			event.preventDefault();
		}
	}

	public boolean canInnerDrag(nsIDOMMouseEvent event) {
//		if(s.getT()) {
//			return false;
//		}
		onHideTooltip();

		if (VpeDebug.printVisualInnerDragDropEvent) {
			System.out.print("<<<<<< canInnerDrag"); //$NON-NLS-1$
		}
		if (innerDragInfo != null) {
			innerDragInfo.Release();
			innerDragInfo = null;
		}
		boolean canDrag = false;
		VpeVisualInnerDragInfo dragInfo = selectionBuilder.getInnerDragInfo(event);
		if (dragInfo != null) {
			Node dragNode = dragInfo.getNode();
			if (VpeDebug.printVisualInnerDragDropEvent) {
				System.out.print(" dragNode: " + dragNode.getNodeName() + "(" + MozillaSupports.getAddress(dragNode) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			switch (dragNode.getNodeType()) {
			case Node.ELEMENT_NODE:
				canDrag = visualBuilder.canInnerDrag((Element)dragNode);
			case Node.TEXT_NODE:
				canDrag = visualBuilder.isTextEditable(dragNode);
			}
			if (canDrag) {
				VpeSourceInnerDragInfo sourceInnerDragInfo = visualBuilder.getSourceInnerDragInfo(dragInfo);
				if (sourceInnerDragInfo.getNode() != null) {
					innerDragInfo = dragInfo;
					InnerDragBuffer.object = sourceInnerDragInfo.getNode();
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							InnerDragBuffer.object = null;
						}
					});
				} else {
					canDrag = false;
				}
			} 
			if (!canDrag) {
				dragInfo.Release();
			}
		}
		if (VpeDebug.printVisualInnerDragDropEvent) {
			System.out.println("  canDrag: " + canDrag); //$NON-NLS-1$
		}
		return canDrag;
	}
	
	VpeDropWindow dropWindow = null;
	
///	VpeDropWindow get

	public MozillaDropInfo canInnerDrop(nsIDOMMouseEvent event) {
//		if(s.getT()) {
//			return null;
//		}
		onHideTooltip();

		if(dropWindow.active) {
			if(!event.isAltKey()) {
				dropWindow.close();
			} else {
				return null;
			}
		}
		if(event.isAltKey()) {
			Node visualNode = event.getTargetNode();
			Node sourceNode = domMapping.getNearSourceNode(visualNode);
			if(sourceNode != null) {
				dropWindow.active = true;
				dropWindow.setEventPosition(event.getScreenX(), event.getScreenY());
				dropWindow.setInitialTargetNode(sourceNode);
				dropWindow.open();
				event.stopPropagation();
				event.preventDefault();
				return null;
			}
		}
		if (VpeDebug.printVisualInnerDragDropEvent) {
			System.out.print("<<<<<< canInnerDrop"); //$NON-NLS-1$
		}
		boolean canDrop = false;;
		Node caretParent = null;
		int caretOffset = 0;
		if (innerDragInfo != null) {
			VpeVisualInnerDropInfo visualDropInfo = selectionBuilder.getInnerDropInfo(event);
			if (visualDropInfo.getDropContainer() != null) {
				if (VpeDebug.printVisualInnerDragDropEvent) {
					System.out.print("  x: " + visualDropInfo.getMouseX() + "  y: " + visualDropInfo.getMouseY() +  //$NON-NLS-1$ //$NON-NLS-2$
							"  container: " + visualDropInfo.getDropContainer().getNodeName() +  //$NON-NLS-1$
							"(" + MozillaSupports.getAddress(visualDropInfo.getDropContainer()) + ")" + //$NON-NLS-1$ //$NON-NLS-2$
							"  parent: " + visualDropInfo.getDropContainer().getParentNode().getNodeName() + //$NON-NLS-1$
							"(" + MozillaSupports.getAddress(visualDropInfo.getDropContainer().getParentNode()) + ")" + //$NON-NLS-1$ //$NON-NLS-2$
							"  offset: " + visualDropInfo.getDropOffset()); //$NON-NLS-1$
				}
				VpeSourceInnerDragInfo sourceInnerDragInfo = visualBuilder.getSourceInnerDragInfo(innerDragInfo);
				VpeSourceInnerDropInfo sourceDropInfo = visualBuilder.getSourceInnerDropInfo(sourceInnerDragInfo.getNode(), visualDropInfo, true);
				canDrop = sourceDropInfo.canDrop();
				if (canDrop) {
					VpeVisualInnerDropInfo newVisualDropInfo = visualBuilder.getInnerDropInfo(sourceDropInfo.getContainer(), sourceDropInfo.getOffset());
					if (newVisualDropInfo != null) {
						visualBuilder.correctVisualDropPosition(newVisualDropInfo, visualDropInfo);
						caretParent = newVisualDropInfo.getDropContainer();
						caretOffset = newVisualDropInfo.getDropOffset();
					}
				}
			}
			visualDropInfo.Release();
		}
		if (VpeDebug.printVisualInnerDragDropEvent) {
			System.out.println("  canDrop: " + canDrop); //$NON-NLS-1$
		}
		return new MozillaDropInfo(canDrop, caretParent, caretOffset);
	}

	public void innerDrop(nsIDOMMouseEvent event) {
		onHideTooltip();

		if (VpeDebug.printVisualInnerDragDropEvent) {
			System.out.print("<<<<<< innerDrop"); //$NON-NLS-1$
		}
		if (innerDragInfo != null) {
			VpeVisualInnerDropInfo visualDropInfo = selectionBuilder.getInnerDropInfo(event);
			if (visualDropInfo.getDropContainer() != null) {
				if (VpeDebug.printVisualInnerDragDropEvent) {
					System.out.print("  container: " + visualDropInfo.getDropContainer().getNodeName() +  //$NON-NLS-1$
							"(" + MozillaSupports.getAddress(visualDropInfo.getDropContainer()) + ")" +  //$NON-NLS-1$ //$NON-NLS-2$
							"  offset: " + visualDropInfo.getDropOffset()); //$NON-NLS-1$
				}
//				Rectangle rect = visualBuilder.getNodeBounds(visualDropInfo.getDropContainer());
//				if(visualDropInfo.getMouseY() >= rect.y && visualDropInfo.getMouseY() <= (rect.y+rect.height)){
//					if(visualDropInfo.getMouseX() >= (rect.x+rect.width/2) && visualDropInfo.getMouseX() <= (rect.x+rect.width))
//						visualDropInfo.setBefore(false);
//				}

				VpeSourceInnerDragInfo sourceInnerDragInfo = visualBuilder.getSourceInnerDragInfo(innerDragInfo);
				VpeSourceInnerDropInfo sourceDropInfo = visualBuilder.getSourceInnerDropInfo(sourceInnerDragInfo.getNode(), visualDropInfo, true);
				if(sourceDropInfo.canDrop()){
					VpeVisualInnerDropInfo newVisualDropInfo = visualBuilder.getInnerDropInfo(sourceDropInfo.getContainer(), sourceDropInfo.getOffset());
					if (newVisualDropInfo != null) {
						visualBuilder.correctVisualDropPosition(newVisualDropInfo, visualDropInfo);
						sourceDropInfo.setTop(visualDropInfo.getMouseY());
						sourceDropInfo.setLeft(visualDropInfo.getMouseX());
						visualBuilder.innerDrop(sourceInnerDragInfo, sourceDropInfo);
						if (innerDragInfo != null) {
							innerDragInfo.Release();
							innerDragInfo = null;
						}
					}
				}
			}
			//visualDropInfo.Release();
		}
		if (VpeDebug.printVisualInnerDragDropEvent) {
			System.out.println();
		}
	}

	public MozillaDropInfo canExternalDrop(nsIDOMMouseEvent mouseEvent, String flavor, String data) {
		InnerDragBuffer.object = null;
//		if(s.getT()) {
//			return new MozillaDropInfo(false, null, 0);
//		}
		onHideTooltip();

		if(dropWindow.active) {
			if(!mouseEvent.isAltKey()) {
				dropWindow.close();
			} else {
				return new MozillaDropInfo(false, null, 0);
			}
		}
		if(mouseEvent.isAltKey()) {
			Node visualNode = mouseEvent.getTargetNode();
			Node sourceNode = domMapping.getNearSourceNode(visualNode);
			if(sourceNode != null) {
				if(ModelTransfer.MODEL.equals(flavor)){ //$NON-NLS-1$
//					XModelObject object = PreferenceModelUtilities.getPreferenceModel().getModelBuffer().source();
//					InnerDragBuffer.object = object;
				} else {
					dropWindow.flavor = flavor;
				}
				dropWindow.active = true;
				dropWindow.setEventPosition(mouseEvent.getScreenX(), mouseEvent.getScreenY());
				dropWindow.setInitialTargetNode(sourceNode);
				dropWindow.open();
				mouseEvent.stopPropagation();
				mouseEvent.preventDefault();
				return new MozillaDropInfo(false, null, 0);
			}
		}
		boolean canDrop = false;
		Node caretParent = null;
		int caretOffset = 0;

		if(MODEL_FLAVOR.equals(flavor)){
			XModelObject object = PreferenceModelUtilities.getPreferenceModel().getModelBuffer().source();
			if(object.getFileType() == XModelObject.FILE && !TLDUtil.isTaglib(object)) {
				IFile f = (IFile)EclipseResourceUtil.getResource(object);
				canDrop = f != null;
				VpeVisualInnerDropInfo visualDropInfo = selectionBuilder.getInnerDropInfo(mouseEvent);
				caretParent = visualDropInfo.getDropContainer();
				caretOffset = visualDropInfo.getDropOffset();
			} else {			
				String tagname = getTagName(object);
				if(tagname.indexOf("taglib") >= 0) tagname= "taglib"; //$NON-NLS-1$ //$NON-NLS-2$
				Node sourceDragNode = ((Document)getModel().getAdapter(Document.class)).createElement(tagname);
				VpeVisualInnerDropInfo visualDropInfo = selectionBuilder.getInnerDropInfo(mouseEvent);
				if (visualDropInfo.getDropContainer() != null) {
					VpeSourceInnerDropInfo sourceDropInfo = visualBuilder.getSourceInnerDropInfo(sourceDragNode, visualDropInfo, true);
					canDrop = sourceDropInfo.canDrop();
					if (canDrop) {
						VpeVisualInnerDropInfo newVisualDropInfo = visualBuilder.getInnerDropInfo(sourceDropInfo.getContainer(), sourceDropInfo.getOffset());
						if (newVisualDropInfo != null) {
							visualBuilder.correctVisualDropPosition(newVisualDropInfo, visualDropInfo);
							caretParent = newVisualDropInfo.getDropContainer();
							caretOffset = newVisualDropInfo.getDropOffset();
						}
					}
				}
				visualDropInfo.Release();
			}
		}else if(nsITransferable.kFileMime.equals(flavor) ||
				 nsITransferable.kURLMime.equals(flavor)) {
			VpeVisualInnerDropInfo visualDropInfo = selectionBuilder.getInnerDropInfo(mouseEvent);
			caretParent = visualDropInfo.getDropContainer();
			caretOffset = visualDropInfo.getDropOffset();
			try {
//				URL newUrl = new URL(data);
//				IProject project = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(newUrl.getPath())).getProject();
//				IFileEditorInput input = (IFileEditorInput) sourceEditor.getEditorInput();
//				canDrop = input.getFile().getProject().equals(project);
				
				canDrop = true;
			} catch (Exception ex) {
				VpePlugin.reportProblem(ex);
			}
		}
		if (VpeDebug.printVisualInnerDragDropEvent) {
			System.out.println("  canDrop: " + canDrop + (canDrop ? "  container: " + caretParent.getNodeName() + "  offset: " + caretOffset : "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		return new MozillaDropInfo(canDrop, caretParent, caretOffset);
		
	}

	public VpeSourceInnerDropInfo canExternalDropMacro(XModelObject object, Node parentNode, int offset) {
		String tagname = getTagName(object);
		Node sourceDragNode = ((Document)getModel().getAdapter(Document.class)).createElement(tagname);
		return visualBuilder.getSourceInnerDropInfo(sourceDragNode, parentNode, offset, false);
	}

	public void externalDropAny(final String flavor, final String data, final Point range, Node container) {
		if(flavor == null || flavor.length() == 0) return;
		IDropCommand dropCommand = DropCommandFactory.getInstance().getDropCommand(flavor, JSPTagProposalFactory.getInstance());

		boolean promptAttributes = "yes".equals(VpePreference.ALWAYS_REQUEST_FOR_ATTRIBUTE.getValue());
		dropCommand.getDefaultModel().setPromptForTagAttributesRequired(promptAttributes);

		dropCommand.execute(
			new DropData(
				flavor,
				data,
				getPageContext(),
				sourceEditor.getEditorInput(),
				(ISourceViewer)sourceEditor.getAdapter(ISourceViewer.class), 
				new VpeSelectionProvider(range.x,range.y),
				container
			)
		);		
	}

	private String getTagName(XModelObject object) {
		String tagname = object.getAttributeValue("name"); //$NON-NLS-1$

		XModelObject parent = object.getParent();
		String uri = (parent == null) ? "" : parent.getAttributeValue(URIConstants.LIBRARY_URI); //$NON-NLS-1$
		String defaultPrefix = (parent == null) ? "" : parent.getAttributeValue(URIConstants.DEFAULT_PREFIX); //$NON-NLS-1$
		
		String[] texts = new String[]{"<"+tagname+">"}; //$NON-NLS-1$ //$NON-NLS-2$
		PaletteInsertHelper.applyPrefix(texts, sourceEditor, tagname, uri, defaultPrefix);
		tagname = texts[0].substring(1,texts[0].length()-1);
		
		return tagname;
	}

	public void externalDrop(nsIDOMMouseEvent mouseEvent, String flavor, String data) {
		onHideTooltip();

		VpeVisualInnerDropInfo visualDropInfo = selectionBuilder.getInnerDropInfo(mouseEvent);
		Point range = selectionBuilder.getSourceSelectionRangeAtVisualNode(visualDropInfo.getDropContainer(), visualDropInfo.getDropOffset());
		VpeSourceInnerDropInfo sourceDropInfo = null;
		
		if(MODEL_FLAVOR.equals(flavor)){
			XModelObject object = PreferenceModelUtilities.getPreferenceModel().getModelBuffer().source();
			if(object.getFileType() == XModelObject.FILE && !TLDUtil.isTaglib(object)) {
				flavor = "application/x-moz-file"; //$NON-NLS-1$
				IFile f = (IFile)EclipseResourceUtil.getResource(object);
				try {
					data = f.getLocation().toFile().toURL().toString();
				} catch (Exception e) {
					VpePlugin.getPluginLog().logError(e);
				}
			} else {			
				String tagname = getTagName(object);
				if(tagname.indexOf("taglib") >= 0) tagname= "taglib"; //$NON-NLS-1$ //$NON-NLS-2$
				Node sourceDragNode = ((Document)getModel().getAdapter(Document.class)).createElement(tagname);
				if (visualDropInfo.getDropContainer() != null) {
					sourceDropInfo = visualBuilder.getSourceInnerDropInfo(sourceDragNode, visualDropInfo, true);
					range = selectionBuilder.getSourceSelectionRange(sourceDropInfo.getContainer(), sourceDropInfo.getOffset());
				}
			}
		}
		
		if (visualDropInfo.getDropContainer() != null) {
			if (VpeDebug.printVisualInnerDragDropEvent) {
				System.out.println("  drop!  container: " + visualDropInfo.getDropContainer().getNodeName()); //$NON-NLS-1$
			}
			externalDropAny(flavor, data, range, sourceDropInfo == null?null:sourceDropInfo.getContainer());
		}
	}
	
	public void onShowTooltip(int x, int y, final String text) {
		
		if (tip != null && !tip.isDisposed()) tip.dispose();
		
		Display display = visualEditor.getControl().getDisplay();
		Shell parent = visualEditor.getControl().getShell();
		
		tip = new Shell(parent, SWT.NO_FOCUS| SWT.ON_TOP);
		Color bckgColor = new Color(tip.getDisplay(), 255, 250, 236);
		tip.setBackground(bckgColor);					
		
		Composite composite = tip;
		GridLayout layout= new GridLayout();		
		layout.numColumns = 2;
		layout.marginHeight= 0;
		layout.marginWidth= 0;
		layout.verticalSpacing= 0;
		layout.horizontalSpacing= 0;	
		composite.setLayout(layout);
		GridData gd= new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(gd);
		
		
		final StyledText tipControlHeaderText = new StyledText(composite,  SWT.MULTI | SWT.READ_ONLY);
				
		tipControlHeaderText.setForeground(bckgColor);
		tipControlHeaderText.setBackground(bckgColor);
			
		String formatText = text.trim();
		
		/** attributeString string containing the pairs attribute and it's value as one string*/	
		String[] attributeString = formatText.split("\n"); //$NON-NLS-1$
		/** buffer string containing the attribute and the value in the different succeding string*/
		String[] buffer = attributeString[0].split(" "); //$NON-NLS-1$
		
		tipControlHeaderText.setText(buffer[0].toString());
				
		tipControlHeaderText.addLineStyleListener(
			new LineStyleListener() {
				public void lineGetStyle(LineStyleEvent event){
					Color color = new Color(tipControlHeaderText.getDisplay(), 201, 51, 40);
					if (event.lineOffset == 0) {			
						StyleRange st = new StyleRange();
						st.fontStyle = SWT.BOLD;
						st.foreground = color;
						event.styles = new StyleRange[]{st};
						st.start = event.lineOffset;
						st.length = event.lineText.length();						
					}
				}
			}
		);
		
		GridData gridData = new GridData(GridData.BEGINNING | GridData.FILL_BOTH);
		gridData.horizontalAlignment = GridData.FILL; 
		gridData.horizontalSpan = 2;
		tipControlHeaderText.setLayoutData(gridData);
		
		StringBuffer tempAttr = new StringBuffer();
		StringBuffer tempValue = new StringBuffer();
		
		if (attributeString.length >= 2) {
			for (int i = 1; i < attributeString.length; i++) {
				buffer = attributeString[i].split(" ", 2); //$NON-NLS-1$
				if (i == 1) {
					tempAttr.append(buffer[0] + " "); //$NON-NLS-1$
					tempValue.append((buffer.length>=2?buffer[1]:"") + " "); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					tempAttr.append("\n" + buffer[0] + " "); //$NON-NLS-1$ //$NON-NLS-2$
					tempValue.append(" \n" + (buffer.length>=2?buffer[1]:"") + " "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
			}
	
			final StyledText tipControlAttributeText = new StyledText(composite,
					SWT.MULTI | SWT.READ_ONLY);
			
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
			GridData gridData1 = new GridData(GridData.BEGINNING | GridData.FILL_BOTH);
			gridData1.horizontalAlignment = GridData.FILL;	
			tipControlAttributeText.setLayoutData(gridData1);
	
			final StyledText tipControlValueText = new StyledText(composite,
					SWT.MULTI | SWT.READ_ONLY);
		
			tipControlValueText.setBackground(bckgColor);
	
			tipControlValueText.setText(tempValue.toString());
	
			
			
			GridData gridData2 = new GridData(GridData.BEGINNING | GridData.FILL_BOTH);
			gridData2.horizontalAlignment = GridData.FILL;
			tipControlValueText.setLayoutData(gridData2);
		}
		/*
		* Bug in Mozilla embedded API.  Tooltip coordinates are wrong for 
		* elements inside an inline frame (IFrame tag).  The workaround is 
		* to position the tooltip based on the mouse cursor location.
		*/
		Point point = display.getCursorLocation();
		/* Assuming cursor is 21x21 because this is the size of
		 * the arrow cursor on Windows
		 */ 
		point.y += 21;
		tip.setLocation(point);
		tip.pack();
		tip.setVisible(true);
	}
	
	public void onHideTooltip() {
		if (tip != null && !tip.isDisposed()) tip.dispose();
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

	public void setToolbarFormatControllerManager(FormatControllerManager formatControllerManager) {
		toolbarFormatControllerManager = formatControllerManager;
	}
	public void setSelectionBarController(SelectionBar SelectionBar) {
		selectionBar = SelectionBar;
	}

	public IStructuredModel getModel() {
		return sourceEditor.getModel();
	}

	public VpeDomMapping getDomMapping() {
		return domMapping;
	}
	
	public VpeIncludeList getIncludeList(){
		try {
			if(includeList == null)
				throw new Exception("includeList - NULL!!!"); //$NON-NLS-1$
		} catch (Exception e) {
			VpePlugin.getPluginLog().logError(e);
		}
		return includeList;
	}

	public void selectionChanged(SelectionChangedEvent event) {
		if (editPart.getVisualMode() != VpeEditorPart.PREVIEW_MODE) {
			if(toolbarFormatControllerManager != null) toolbarFormatControllerManager.selectionChanged();
			if(selectionBar != null) selectionBar.selectionChanged();		
		}

		if (!switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_SOURCE)) {
			return;
		}
		if (VpeDebug.printSourceSelectionEvent) {
			System.out.println(">>>>>>>>>>>>>> selectionChanged  " + event.getSource()); //$NON-NLS-1$
		}
		sourceSelectionChanged();
		switcher.stopActiveEditor();
	}

	// nsIClipboardDragDropHooks implementation
	public void onPasteOrDrop(nsIDOMMouseEvent mouseEvent, String flavor, String data) {
		onHideTooltip();

		VpeVisualInnerDropInfo visualDropInfo = selectionBuilder.getInnerDropInfo(mouseEvent);
		Point range = selectionBuilder.getSourceSelectionRangeAtVisualNode(visualDropInfo.getDropContainer(), visualDropInfo.getDropOffset());
		VpeSourceInnerDropInfo sourceDropInfo = null;

		XModelObject object = PreferenceModelUtilities.getPreferenceModel().getModelBuffer().source();

		String tagname = getTagName(object);
		if(tagname.indexOf("taglib") >= 0) tagname= "taglib"; //$NON-NLS-1$ //$NON-NLS-2$
		Node sourceDragNode = ((Document)getModel().getAdapter(Document.class)).createElement(tagname);
		if (visualDropInfo.getDropContainer() != null) {
			sourceDropInfo = visualBuilder.getSourceInnerDropInfo(sourceDragNode, visualDropInfo, true);
			range = selectionBuilder.getSourceSelectionRange(sourceDropInfo.getContainer(), sourceDropInfo.getOffset());
		}
		
		if (visualDropInfo.getDropContainer() != null) {
			if (VpeDebug.printVisualInnerDragDropEvent) {
				System.out.println("  drop!  container: " + visualDropInfo.getDropContainer().getNodeName()); //$NON-NLS-1$
			}
			final String finalFlavor = flavor;
			final String finalData = data;
			final Point finalRange = range;
			final Node finalDropContainer = sourceDropInfo == null ? null : sourceDropInfo.getContainer();
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					externalDropAny(finalFlavor, finalData, finalRange, finalDropContainer);
				}
			});
		}
	}

	public void drop(Node node, Node parentNode, int offset) {
		visualBuilder.innerDrop(node, parentNode, offset);
	}

}