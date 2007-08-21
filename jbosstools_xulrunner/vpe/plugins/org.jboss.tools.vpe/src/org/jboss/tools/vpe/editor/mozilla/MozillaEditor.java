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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

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
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.css.VpeResourcesDialog;
import org.jboss.tools.vpe.editor.preferences.VpeEditorPreferencesPage;
import org.jboss.tools.vpe.editor.toolbar.IVpeToolBarManager;
import org.jboss.tools.vpe.editor.toolbar.VpeToolBarManager;
import org.jboss.tools.vpe.editor.toolbar.format.FormatControllerManager;
import org.jboss.tools.vpe.editor.toolbar.format.TextFormattingToolBar;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.jboss.tools.vpe.selbar.SelectionBar;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.mozilla.interfaces.nsIClipboardDragDropHookList;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMEventTarget;
import org.mozilla.interfaces.nsIDOMNamedNodeMap;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.interfaces.nsISelection;
import org.mozilla.interfaces.nsISelectionPrivate;

public class MozillaEditor extends EditorPart implements IReusableEditor {
	protected static final String INIT_URL = "file://" + (new File(VpePlugin.getDefault().getResourcePath("ve"), "init.html")).getAbsolutePath();
//	private static final String INIT_URL = "chrome://vpe/content/init.html"; //$NON-NLS-1$
	private static final String CONTENT_AREA_ID = "__content__area__"; //$NON-NLS-1$

	static String SELECT_BAR = "SELECT_LBAR"; //$NON-NLS-1$
	private XulRunnerEditor xulRunnerEditor;
	private nsIDOMDocument domDocument;
	private nsIDOMEventTarget documentEventTarget;
	private nsIDOMElement contentArea;
	private nsIDOMNode headNode;
	private nsIDOMEventTarget contentAreaEventTarget;
	private MozillaDomEventListener contentAreaEventListener = new MozillaDomEventListener();
	private MozillaBaseEventListener baseEventListener = null;
	private EditorLoadWindowListener editorLoadWindowListener;
	private EditorDomEventListener editorDomEventListener;
	private IVpeToolBarManager vpeToolBarManager;
	private FormatControllerManager formatControllerManager = new FormatControllerManager();
	private VpeController controller;

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
			xulRunnerEditor = new XulRunnerEditor(cmpEd) {
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

			xulRunnerEditor.setURL(INIT_URL);
			xulRunnerEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		}
		catch (Exception e) {
			VpePlugin.getPluginLog().logError(e);
			
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
		if(xulRunnerEditor!=null) {
			xulRunnerEditor.setFocus();
		} else {
			//link.setFocus();
		}
	}

	public void dispose() {
		removeDomEventListeners();
		// TODO Max Areshkau figure out with ClipboardDragDropHooks
//		removeClipboardDragDropHooks();

		if (xulRunnerEditor != null) {
			xulRunnerEditor.dispose();
			xulRunnerEditor = null;
		}

		super.dispose();
	}

	public void setEditorLoadWindowListener(EditorLoadWindowListener listener) {
		editorLoadWindowListener = listener;
	}

	public void setEditorDomEventListener(EditorDomEventListener listener) {
		editorDomEventListener = listener;
		if (contentAreaEventListener != null) {
			contentAreaEventListener.setEditorDomEventListener(listener);
		}
		if (xulRunnerEditor != null) {
			// TODO Max Areshkau add context menu listener support
//			xulRunnerEditor.setContextMenuListener(listener);
		}
	}

	public nsIDOMDocument getDomDocument() {
		if (domDocument == null) {
			domDocument = xulRunnerEditor.getDOMDocument();
		}
		return domDocument;
	}

	public nsIDOMElement getContentArea() {
		return contentArea;
	}

	public nsIDOMNode getHeadNode() {
		return headNode;
	}

	public Menu getMenu() {
		return xulRunnerEditor.getMenu();
	}

	public Control getControl() {
		return xulRunnerEditor;
	}

	// TODO Max Areshkau add DnD support
//	public VpeDnD getLocalDnD() {
//		if (contentAreaEventListener != null) {
//			return contentAreaEventListener.getLocalDnD();
//		}
//		return null;
//	}

	protected nsIDOMElement findContentArea() {
		nsIDOMElement area = null;
		nsIDOMNodeList nodeList = xulRunnerEditor.getDOMDocument().getElementsByTagName(HTML.TAG_BODY);
		long length = nodeList.getLength();
		for(long i=0; i<length; i++) {
			nsIDOMNode node = nodeList.item(i);
			if (isContentArea(node)) {
				if (node.getNodeType() != nsIDOMNode.ELEMENT_NODE) {
					throw new RuntimeException("The content area node should by element node.");
				}

				area = (nsIDOMElement) node.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
				break;
			}
		}
//		nsIDOMElement area = findContentArea(root);
		if (area == null) {
			return null;
		}

		nsIDOMNode root = xulRunnerEditor.getDOMDocument().getDocumentElement();
		headNode = findHeadNode(root);


		return area;
	}

	private nsIDOMNode findHeadNode(nsIDOMNode root){
		nsIDOMNode headNode = findChildNode(root, HTML.TAG_HEAD); //$NON-NLS-1$
		return headNode;
	}

	private nsIDOMNode findChildNode(nsIDOMNode parent, String name) {
		nsIDOMNodeList list = parent.getChildNodes();
		nsIDOMNode node;
		for (int i=0;i<list.getLength();i++) {
			node = list.item(i);
			if (node.getNodeName().equalsIgnoreCase(name)) {
				return node;
			}
		}
		return null;
	}

	private nsIDOMElement findContentArea(nsIDOMNode node) {
		nsIDOMElement area = null;
		if (node != null && node.getNodeType() == nsIDOMNode.ELEMENT_NODE) {
        	nsIDOMNodeList children = node.getChildNodes();
        	if (children != null) {
	        	long length = children.getLength();
	        	for (long i = 0; i < length; i++) {
	        		nsIDOMNode child = children.item(i);
	        		if (isContentArea(child)) {
	        			if (child.getNodeType() != nsIDOMNode.ELEMENT_NODE) {
	        				throw new RuntimeException("The content area node should by element node.");
	        			}
	        			area = (nsIDOMElement)child;
	        			break;
	        		}
	        		area = findContentArea(child);
	        		if (area != null) {
	        			break;
	        		}
	        	}
        	}
    	}
    	return area;
	}

	private boolean isContentArea(nsIDOMNode node) {
		boolean ret = false;
    	if (HTML.TAG_BODY.equalsIgnoreCase(node.getNodeName())) {
   	    	nsIDOMNamedNodeMap map = node.getAttributes();
			if (map != null) {
				long length = map.getLength();
    			for (int i = 0; i < length; i++) {
    				nsIDOMNode attr = map.item(i);
    				ret = attr.getNodeType() == nsIDOMNode.ATTRIBUTE_NODE
    						&& HTML.ATTR_ID.equalsIgnoreCase(attr.getNodeName())
							&& CONTENT_AREA_ID.equalsIgnoreCase(attr.getNodeValue());
    				if (ret) {
    	    			break;
    				}
    			}
			}
    	}
    	return ret;
	}

	private void onLoadWindow() {
		contentArea = findContentArea();
		addDomEventListeners();
		addSelectionListener();
		// TODO Max Areshkau figure out with clipboard drag drop hooks
//		addClipboardDragDropHooks();
		if (editorLoadWindowListener != null) {
			editorLoadWindowListener.load();
		}
	}

	private void addDomEventListeners() {
		if (contentArea != null) {
			if (contentAreaEventListener != null) {
				contentAreaEventListener.setVisualEditor(xulRunnerEditor);
				contentAreaEventTarget = (nsIDOMEventTarget) contentArea.queryInterface(nsIDOMEventTarget.NS_IDOMEVENTTARGET_IID);
				// TODO Max Areshkau add DnD support
//				contentAreaEventListener.getLocalDnD().Init(visualEditor.getDOMDocument(),visualEditor.getPresShell(), contentAreaEventListener.getDropListener());
				contentAreaEventTarget.addEventListener("click", contentAreaEventListener, false); //$NON-NLS-1$
				contentAreaEventTarget.addEventListener("mousedown", contentAreaEventListener, false); //$NON-NLS-1$
				contentAreaEventTarget.addEventListener("mouseup", contentAreaEventListener, false); //$NON-NLS-1$
				contentAreaEventTarget.addEventListener("mousemove", contentAreaEventListener, false); //$NON-NLS-1$
	
				//context menu event handler(add by Max Areshkau)
				contentAreaEventTarget.addEventListener(MozillaDomEventListener.CONTEXTMENUEVENTTYPE, contentAreaEventListener, false);

				documentEventTarget = (nsIDOMEventTarget) getDomDocument().queryInterface(nsIDOMEventTarget.NS_IDOMEVENTTARGET_IID);
				documentEventTarget.addEventListener("keypress", contentAreaEventListener, false); //$NON-NLS-1$
			} else {
				baseEventListener = new MozillaBaseEventListener();
				// TODO Max Areshkau add DnD support
//				baseEventListener.getLocalDnD().Init(visualEditor.getDOMDocument(),visualEditor.getPresShell(), baseEventListener.getDropListener());
			}
		}
	}

	private void removeDomEventListeners() {
		if (contentAreaEventTarget != null && contentAreaEventListener != null) {
			contentAreaEventTarget.removeEventListener("click", contentAreaEventListener, false); //$NON-NLS-1$
			contentAreaEventTarget.removeEventListener("mousedown", contentAreaEventListener, false); //$NON-NLS-1$
			contentAreaEventTarget.removeEventListener("mouseup", contentAreaEventListener, false); //$NON-NLS-1$
			contentAreaEventTarget.removeEventListener("mousemove", contentAreaEventListener, false); //$NON-NLS-1$
		}
		if (domDocument != null && documentEventTarget != null) {
			documentEventTarget.removeEventListener("keypress", contentAreaEventListener, false); //$NON-NLS-1$
		}
	}

	private void addSelectionListener() {
		if (contentAreaEventListener != null&&xulRunnerEditor!=null) {
			
			nsISelection selection = xulRunnerEditor.getSelection();
			nsISelectionPrivate selectionPrivate = (nsISelectionPrivate) selection.queryInterface(nsISelectionPrivate.NS_ISELECTIONPRIVATE_IID);
			selectionPrivate.addSelectionListener(contentAreaEventListener);
			
		}
	}

	private void removeSelectionListener() {
		if (contentAreaEventListener != null&&xulRunnerEditor!=null) {

			nsISelection selection = xulRunnerEditor.getSelection();
			nsISelectionPrivate selectionPrivate = (nsISelectionPrivate) selection.queryInterface(nsISelectionPrivate.NS_ISELECTIONPRIVATE_IID);
			selectionPrivate.removeSelectionListener(contentAreaEventListener);
		}
	}

	public void setSelectionRectangle(nsIDOMElement element, int resizerConstrains, boolean scroll) {
		if (contentAreaEventListener != null) {
			// TODO Max Areshkau add selection support
			xulRunnerEditor.setSelectionRectangle((nsIDOMElement)element, resizerConstrains, scroll);
		}
	}

	/**
	 * Show resizer markers
	 */
	public void showResizer() {
		if (contentAreaEventListener != null) {
			xulRunnerEditor.showResizer();
		}
	}

	/**
	 * Hide resizer markers
	 */
	public void hideResizer() {
		if (contentAreaEventListener != null) {
			xulRunnerEditor.hideResizer();
		}
	}

	private void addClipboardDragDropHooks() {
		if (contentAreaEventListener != null) {
			nsIClipboardDragDropHookList hookList = xulRunnerEditor.getClipboardDragDropHookList();
			hookList.addClipboardDragDropHooks(contentAreaEventListener);
		} else if (baseEventListener != null) {
			nsIClipboardDragDropHookList hookList = xulRunnerEditor.getClipboardDragDropHookList();
			hookList.addClipboardDragDropHooks(baseEventListener);
		}
	}

	private void removeClipboardDragDropHooks() {
		if (contentAreaEventListener != null) {
			nsIClipboardDragDropHookList hookList = xulRunnerEditor.getClipboardDragDropHookList();
			hookList.removeClipboardDragDropHooks(contentAreaEventListener);
		} else if (baseEventListener != null) {
			nsIClipboardDragDropHookList hookList = xulRunnerEditor.getClipboardDragDropHookList();
			hookList.removeClipboardDragDropHooks(baseEventListener);
		}
	}
	
}