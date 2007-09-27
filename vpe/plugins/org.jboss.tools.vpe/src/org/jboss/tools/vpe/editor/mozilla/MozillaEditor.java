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
package org.jboss.tools.vpe.editor.mozilla;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.eclipse.ui.part.EditorPart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.css.VpeResourcesDialog;
import org.jboss.tools.vpe.editor.preferences.VpeEditorPreferencesPage;
import org.jboss.tools.vpe.editor.toolbar.IVpeToolBarManager;
import org.jboss.tools.vpe.editor.toolbar.VpeToolBarManager;
import org.jboss.tools.vpe.editor.toolbar.format.FormatControllerManager;
import org.jboss.tools.vpe.editor.toolbar.format.TextFormattingToolBar;
import org.jboss.tools.vpe.editor.util.MozillaSupports;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.jboss.tools.vpe.mozilla.browser.MozillaBrowser;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.VpeDnD;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIClipboardDragDropHookList;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMElement;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMEventTarget;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsISelectionController;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsISupports;
import org.jboss.tools.vpe.selbar.SelectionBar;

public class MozillaEditor extends EditorPart implements IReusableEditor {
	private static final String INIT_URL = "chrome://vpe/content/init.html"; //$NON-NLS-1$
	private static final String CONTENT_AREA_ID = "__content__area__"; //$NON-NLS-1$

	static String SELECT_BAR = "SELECT_LBAR"; //$NON-NLS-1$
	private MozillaBrowser editor;
	private Document domDocument;
	private nsIDOMEventTarget documentEventTarget;
	private Element contentArea;
	private Node headNode;
	private nsIDOMEventTarget contentAreaEventTarget;
	private MozillaDomEventListener contentAreaEventListener;
	private MozillaBaseEventListener baseEventListener;
	private EditorLoadWindowListener editorLoadWindowListener;
	private EditorDomEventListener editorDomEventListener;
	private IVpeToolBarManager vpeToolBarManager;
	private FormatControllerManager formatControllerManager = new FormatControllerManager();
	
	public void doSave(IProgressMonitor monitor) {
	}

	public void doSaveAs() {
	}

	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.setInput(input);
	}

	public void setInput(IEditorInput input) {
		boolean isVisualRefreshRequired = (getEditorInput() != null && getEditorInput() != input && controller != null);
		super.setInput(input);
		if(isVisualRefreshRequired) controller.visualRefresh();
	}

	public boolean isDirty() {
		return false;
	}

	public boolean isSaveAsAllowed() {
		return false;
	}

	private VpeController controller;

	public void setController(VpeController controller){
		this.controller = controller;
		formatControllerManager.setVpeController(controller);
		controller.setToolbarFormatControllerManager(formatControllerManager);
	}

	Link link = null;	
	
	public void createPartControl(final Composite parent) {
		vpeToolBarManager = new VpeToolBarManager();
		//Setting  Layout for the parent Composite
		GridLayout layout = new GridLayout(2,false);
		layout.marginHeight = 0;
		layout.marginWidth = 2;
		layout.verticalSpacing = 2;
		layout.horizontalSpacing = 2;
		layout.marginBottom = 0;
		parent.setLayout(layout);
				
		// Composite for the left Vertical toolbar
		Composite cmpVerticalToolbar = new Composite(parent, SWT.NONE);
		layout = new GridLayout(1,false);
		layout.marginHeight = 2;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;		
		cmpVerticalToolbar.setLayout(layout);
		cmpVerticalToolbar.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		
		// Editors and Toolbar composite 
		Composite cmpEdTl = new Composite(parent, SWT.NONE);
		GridLayout layoutEdTl = new GridLayout(1, false);
		layoutEdTl.verticalSpacing = 0;
		layoutEdTl.marginHeight = 0;
		layoutEdTl.marginBottom = 3;
		layoutEdTl.marginWidth = 0;
		cmpEdTl.setLayout(layoutEdTl);
		cmpEdTl.setLayoutData(new GridData(GridData.FILL_BOTH));


		 // Use vpeToolBarManager to create a horizontal toolbar. 
		if(vpeToolBarManager!=null) {
			vpeToolBarManager.createToolBarComposite(cmpEdTl);
			vpeToolBarManager.addToolBar(new TextFormattingToolBar(formatControllerManager));
			// Create a Toolbar menu button 
			vpeToolBarManager.createMenuComposite(cmpVerticalToolbar);
		}

		// The Left standalone Vertical Tool Bar  
		final ToolBar verBar = new ToolBar(cmpVerticalToolbar, SWT.VERTICAL|SWT.FLAT);
		verBar.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		
		ToolItem item = null;
		item = createToolItem(verBar, SWT.BUTTON1, "icons/preference.gif", VpeUIMessages.PREFERENCES); //$NON-NLS-1$
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				VpeEditorPreferencesPage.openPreferenceDialog();
			}
		});

		item = createToolItem(verBar, SWT.BUTTON1, "icons/refresh.gif", VpeUIMessages.REFRESH); //$NON-NLS-1$
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if(controller!=null) {
					controller.visualRefresh();
				}
			}
		});
		item = createToolItem(verBar, SWT.BUTTON1, "icons/point_to_css.gif", VpeUIMessages.PAGE_DESIGN_OPTIONS); //$NON-NLS-1$
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				IEditorInput input = getEditorInput();
				if (input instanceof IFileEditorInput) {
					IFile file = ((IFileEditorInput) input).getFile();
					VpeResourcesDialog.run(file);
				} else if (input instanceof ILocationProvider) {
					ILocationProvider provider = (ILocationProvider) input;
					IPath path = provider.getPath(input);
					if (path != null)
						VpeResourcesDialog.run(path);
				}
			}
		});
		verBar.pack();
		
		//Create a composite to the Editor
		Composite cmpEd = new Composite (cmpEdTl, SWT.NATIVE);
		GridLayout layoutEd = new GridLayout(1, false);
		layoutEd.marginBottom = 0;
		layoutEd.marginHeight = 1;
		layoutEd.marginWidth = 0;
		layoutEd.marginRight = 0;
		layoutEd.marginLeft = 1;
		layoutEd.verticalSpacing = 0;
		layoutEd.horizontalSpacing = 0;
		cmpEd.setLayout(layoutEd);
		cmpEd.setLayoutData(new GridData(GridData.FILL_BOTH));

		//TODO Add a paintListener  to cmpEd and give him a border top and left only
		Color buttonDarker = parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
		cmpEd.setBackground(buttonDarker);

		try {
			editor = new MozillaBrowser(cmpEd, SWT.NONE) {
				public void onLoadWindow() {
					super.onLoadWindow();
					MozillaEditor.this.onLoadWindow();
				}
				public void onElementResize(nsIDOMElement element, int resizerConstrains, int top, int left, int width, int height) {
					if (editorDomEventListener != null) {
						editorDomEventListener.elementResized(element, resizerConstrains, top, left, width, height);
					}
				}
				public void onShowTooltip(int x, int y, String text) {
					if (editorDomEventListener != null) {
						editorDomEventListener.onShowTooltip(x, y, text);
					}
				}
				public void onHideTooltip() {
					if (editorDomEventListener != null) {
						editorDomEventListener.onHideTooltip();
					}
				}
			};
			editor.setUrl(INIT_URL);
			editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		}
		catch (Exception e) {
	        layout.verticalSpacing = 10;
	        Label title = new Label(cmpEd, SWT.WRAP);
	        title.setText(VpeUIMessages.MOZILLA_LOADING_ERROR);
	        title.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	        link = new Link(cmpEd, SWT.WRAP);
	        link.setText(VpeUIMessages.MOZILLA_LOADING_ERROR_LINK_TEXT);
	        link.setToolTipText(VpeUIMessages.MOZILLA_LOADING_ERROR_LINK);
	        link.setForeground(link.getDisplay().getSystemColor(SWT.COLOR_BLUE));
	        link.addMouseListener(new MouseListener() {
	        	public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
	                BusyIndicator.showWhile(link.getDisplay(), new Runnable() {
	                    public void run() {
	                        URL theURL=null;;
							try {
								theURL = new URL(VpeUIMessages.MOZILLA_LOADING_ERROR_LINK);
							} catch (MalformedURLException e) {
								VpePlugin.reportProblem(e);
							}
	                        IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
	                        try {
								support.getExternalBrowser().openURL(theURL);
							} catch (PartInitException e) {
								VpePlugin.reportProblem(e);
							}
	                    }
	                });
	            }

				public void mouseDoubleClick(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				public void mouseUp(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
	        });
	        link.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	        Label fill = new Label(cmpEd, SWT.WRAP);		
	        fill.setLayoutData(new GridData(GridData.FILL_BOTH));	        
		}
	}

	private ToolItem createToolItem(ToolBar parent, int type, String image, String toolTipText) {
		ToolItem item = new ToolItem(parent, type);
		item.setImage(ImageDescriptor.createFromFile(MozillaEditor.class, image).createImage());		
		item.setToolTipText(toolTipText);
		return item;
	}

	public void setFocus() {
		if(editor!=null) {
			editor.setFocus();
		} else {
			//link.setFocus();
		}
	}

	public void dispose() {
		removeDomEventListeners();
//		removeClipboardDragDropHooks();
		super.dispose();

		if (contentArea != null) {
			MozillaSupports.release(contentArea);
			contentArea = null;
		}
		if (domDocument != null) {
			MozillaSupports.release(domDocument);
			domDocument = null;
		}
		if (editor != null) {
			editor.dispose();
			editor = null;
		}
	}

	public void setEditorLoadWindowListener(EditorLoadWindowListener listener) {
		editorLoadWindowListener = listener;
	}

	public void setEditorDomEventListener(EditorDomEventListener listener) {
		editorDomEventListener = listener;
		if (contentAreaEventListener != null) {
			contentAreaEventListener.setEditorDomEventListener(listener);
		}
		if (editor != null) {
			editor.setContextMenuListener(listener);
		}
	}

	public Document getDomDocument() {
		if (domDocument == null) {
			domDocument = editor.getDOMDocument();
		}
		return domDocument;
	}

	public Element getContentArea() {
		return contentArea;
	}

	public Node getHeadNode() {
		return headNode;
	}

	public Menu getMenu() {
		return editor.getMenu();
	}

	public Control getControl() {
		return editor;
	}

	public VpeDnD getLocalDnD() {
		if (contentAreaEventListener != null) {
			return contentAreaEventListener.getLocalDnD();
		}
		return null;
	}

	private Element findContentArea() {
		Node root = editor.getDOMDocumentElement();
		Node area = findContentArea(root);
		if (area == null) {
			return null;
		}
		headNode = findHeadNode(root);
		MozillaSupports.release(root);
		return (Element)area;
	}

	private Node findHeadNode(Node root){
		Node headNode = findChildNode(root, "HEAD"); //$NON-NLS-1$
		return headNode;
	}

	private Node findChildNode(Node parent, String name) {
		NodeList list = parent.getChildNodes();
		Node node;
		for (int i=0;i<list.getLength();i++) {
			node = list.item(i);
			if (node.getNodeName().equalsIgnoreCase(name)) {
				MozillaSupports.release(list);
				return node;
			}
			else {
				MozillaSupports.release(node);
			}
		}
		MozillaSupports.release(list);
		return null;
	}

	private Node findContentArea(Node node) {
		Node area = null;
		if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
        	NodeList children = node.getChildNodes();
        	if (children != null) {
	        	int length = children.getLength();
	        	for (int i = 0; i < length; i++) {
	        		Node child = children.item(i);
	        		if (isContentArea(child)) {
	        			area = child;
	        			break;
	        		}
	        		area = findContentArea(child);
	        		MozillaSupports.release(child);
	        		if (area != null) {
	        			break;
	        		}
	        	}
	        	MozillaSupports.release(children);
        	}
    	}
    	return area;
	}

	private boolean isContentArea(Node node) {
		boolean ret = false;
    	if ("BODY".equalsIgnoreCase(node.getNodeName())) { //$NON-NLS-1$
   	    	NamedNodeMap map = node.getAttributes();
			if (map != null) {
				int length = map.getLength();
    			for (int i = 0; i < length; i++) {
    				Node attr = map.item(i);
    				ret = attr.getNodeType() == Node.ATTRIBUTE_NODE &&
							"ID".equalsIgnoreCase(attr.getNodeName()) && //$NON-NLS-1$
							CONTENT_AREA_ID.equalsIgnoreCase(attr.getNodeValue());
    				MozillaSupports.release(attr);
    				if (ret) {
    	    			break;
    				}
    			}
    			MozillaSupports.release(map);
			}
    	}
    	return ret;
	}

	private void onLoadWindow() {
		contentArea = findContentArea();
		addDomEventListeners();
		addSelectionListener();
		addClipboardDragDropHooks();
		if (editorLoadWindowListener != null) {
			editorLoadWindowListener.load();
		}
	}

	private void addDomEventListeners() {
		if (contentArea != null) {
			try {
				contentAreaEventListener = (MozillaDomEventListener)Class.forName("org.jboss.tools.vpe.editor.mozilla.MozillaDomEventListener").newInstance(); //$NON-NLS-1$
			} catch (Exception e) {
			}

			if (contentAreaEventListener != null) {
				contentAreaEventListener.setMozillaBrowser(editor);
				int aEventTarget = nsISupports.queryInterface(MozillaSupports.getAddress(contentArea), nsIDOMEventTarget.NS_IDOMEVENTTARGET_IID);
				contentAreaEventTarget = new nsIDOMEventTarget(aEventTarget);
				contentAreaEventListener.getLocalDnD().Init(editor.getDOMDocument(),editor.getPresShell(), contentAreaEventListener.getDropListener());
				//contentAreaEventTarget.addEventListener("DOMSubtreeModified", contentAreaEventListener.getDomEventListener());
				//contentAreaEventTarget.addEventListener("DOMNodeInserted", contentAreaEventListener.getDomEventListener());
				//contentAreaEventTarget.addEventListener("DOMNodeRemoved", contentAreaEventListener.getDomEventListener());
				//contentAreaEventTarget.addEventListener("DOMNodeRemovedFromDocument", contentAreaEventListener.getDomEventListener());
				//contentAreaEventTarget.addEventListener("DOMNodeInsertedIntoDocument", contentAreaEventListener.getDomEventListener());
				//contentAreaEventTarget.addEventListener("DOMAttrModified", contentAreaEventListener.getDomEventListener());
				//contentAreaEventTarget.addEventListener("DOMCharacterDataModified", contentAreaEventListener.getDomEventListener());
				contentAreaEventTarget.addEventListener("click", contentAreaEventListener.getDomEventListener()); //$NON-NLS-1$
				contentAreaEventTarget.addEventListener("mousedown", contentAreaEventListener.getDomEventListener()); //$NON-NLS-1$
				contentAreaEventTarget.addEventListener("mouseup", contentAreaEventListener.getDomEventListener()); //$NON-NLS-1$
				contentAreaEventTarget.addEventListener("mousemove", contentAreaEventListener.getDomEventListener()); //$NON-NLS-1$
	
				Document document = getDomDocument();
				aEventTarget = nsISupports.queryInterface(MozillaSupports.getAddress(document), nsIDOMEventTarget.NS_IDOMEVENTTARGET_IID);
				documentEventTarget = new nsIDOMEventTarget(aEventTarget);
				documentEventTarget.addEventListener("keypress", contentAreaEventListener.getDomEventListener()); //$NON-NLS-1$
			} else {
				baseEventListener = new MozillaBaseEventListener();
				baseEventListener.getLocalDnD().Init(editor.getDOMDocument(),editor.getPresShell(), baseEventListener.getDropListener());
			}
		}
	}

	private void removeDomEventListeners() {
		if (contentAreaEventTarget != null && contentAreaEventListener != null) {
			//contentAreaEventTarget.removeEventListener("DOMSubtreeModified", contentAreaEventListener.getDomEventListener());
			//contentAreaEventTarget.removeEventListener("DOMNodeInserted", contentAreaEventListener.getDomEventListener());
			//contentAreaEventTarget.removeEventListener("DOMNodeRemoved", contentAreaEventListener.getDomEventListener());
			//contentAreaEventTarget.removeEventListener("DOMNodeRemovedFromDocument", contentAreaEventListener.getDomEventListener());
			//contentAreaEventTarget.removeEventListener("DOMNodeInsertedIntoDocument", contentAreaEventListener.getDomEventListener());
			//contentAreaEventTarget.removeEventListener("DOMAttrModified", contentAreaEventListener.getDomEventListener());
			//contentAreaEventTarget.removeEventListener("DOMCharacterDataModified", contentAreaEventListener.getDomEventListener());
			contentAreaEventTarget.removeEventListener("click", contentAreaEventListener.getDomEventListener()); //$NON-NLS-1$
			contentAreaEventTarget.removeEventListener("mousedown", contentAreaEventListener.getDomEventListener()); //$NON-NLS-1$
			contentAreaEventTarget.removeEventListener("mouseup", contentAreaEventListener.getDomEventListener()); //$NON-NLS-1$
			contentAreaEventTarget.removeEventListener("mousemove", contentAreaEventListener.getDomEventListener()); //$NON-NLS-1$
		}
		if (domDocument != null && documentEventTarget != null) {
			documentEventTarget.removeEventListener("keypress", contentAreaEventListener.getDomEventListener()); //$NON-NLS-1$
		}
	}

	private void addSelectionListener() {
		if (contentAreaEventListener != null) {
			nsISelectionController selectionController = editor.getSelectionController();
			selectionController.addSelectionListener(contentAreaEventListener.getSelectionListener());
			selectionController.Release();
		}
	}

	private void removeSelectionListener() {
		nsISelectionController selectionController = editor.getSelectionController();
		selectionController.removeSelectionListener(contentAreaEventListener.getSelectionListener());
		selectionController.Release();
	}

	public void setSelectionRectangle(Element element, int resizerConstrains, boolean scroll) {
		if (contentAreaEventListener != null) {
			editor.setSelectionRectangle((nsIDOMElement)element, resizerConstrains, scroll);
		}
	}

	public void showResizer() {
		if (contentAreaEventListener != null) {
			editor.showResizer();
		}
	}

	public void hideResizer() {
		if (contentAreaEventListener != null) {
			editor.hideResizer();
		}
	}

	private void addClipboardDragDropHooks() {
		if (contentAreaEventListener != null) {
			nsIClipboardDragDropHookList hookList = editor.getClipboardDragDropHookList();
			hookList.addClipboardDragDropHooks(contentAreaEventListener.getDragDropHook());
			hookList.Release();
		} else if (baseEventListener != null) {
			nsIClipboardDragDropHookList hookList = editor.getClipboardDragDropHookList();
			hookList.addClipboardDragDropHooks(baseEventListener.getDragDropHook());
			hookList.Release();
		}
	}

	private void removeClipboardDragDropHooks() {
		if (contentAreaEventListener != null) {
			nsIClipboardDragDropHookList hookList = editor.getClipboardDragDropHookList();
			hookList.removeClipboardDragDropHooks(contentAreaEventListener.getDragDropHook());
			hookList.Release();
		} else if (baseEventListener != null) {
			nsIClipboardDragDropHookList hookList = editor.getClipboardDragDropHookList();
			hookList.removeClipboardDragDropHooks(baseEventListener.getDragDropHook());
			hookList.Release();
		}
	}
	
}