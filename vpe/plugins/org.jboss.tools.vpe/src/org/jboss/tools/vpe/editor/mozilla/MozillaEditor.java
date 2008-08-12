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
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
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
import org.jboss.tools.vpe.editor.util.DocTypeUtil;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.jboss.tools.vpe.xulrunner.XPCOM;
import org.jboss.tools.vpe.xulrunner.XulRunnerException;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMEventTarget;
import org.mozilla.interfaces.nsIDOMNamedNodeMap;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.interfaces.nsIEditingSession;
import org.mozilla.interfaces.nsIEditor;
import org.mozilla.interfaces.nsIHTMLAbsPosEditor;
import org.mozilla.interfaces.nsIHTMLInlineTableEditor;
import org.mozilla.interfaces.nsIHTMLObjectResizer;
import org.mozilla.interfaces.nsIPlaintextEditor;

public class MozillaEditor extends EditorPart implements IReusableEditor {
	protected static final String INIT_URL = /*"file://" +*/ (new File(VpePlugin.getDefault().getResourcePath("ve"), "init.html")).getAbsolutePath(); //$NON-NLS-1$ //$NON-NLS-2$
	private static final String CONTENT_AREA_ID = "__content__area__"; //$NON-NLS-1$

	static String SELECT_BAR = "SELECT_LBAR"; //$NON-NLS-1$
	private XulRunnerEditor xulRunnerEditor;
	private nsIDOMDocument domDocument;
	private nsIDOMEventTarget documentEventTarget;
	private nsIDOMElement contentArea;
	private nsIDOMNode headNode;
	private nsIDOMEventTarget contentAreaEventTarget;
	private MozillaDomEventListener contentAreaEventListener;

	private EditorLoadWindowListener editorLoadWindowListener;
	private EditorDomEventListener editorDomEventListener;
	private IVpeToolBarManager vpeToolBarManager;
	private FormatControllerManager formatControllerManager = new FormatControllerManager();
	private VpeController controller;
	private Link link = null;
	private boolean loaded;
	private boolean isRefreshPage = false;
	private String doctype;
	/**
	 * Used for manupalation of browser in design mode,
	 * for example enable or disable readOnlyMode
	 */
	private nsIEditor editor;

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
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
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
		ToolBar verBar = new ToolBar(cmpVerticalToolbar, SWT.VERTICAL|SWT.FLAT);
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
					// if the first load page
					if (!isRefreshPage()) {
						super.onLoadWindow();
						MozillaEditor.this.onLoadWindow();
					}
					// if only refresh page
					else {
						MozillaEditor.this.onReloadWindow();
						setRefreshPage(false);
					}

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
				public void onDispose() {
					removeDomEventListeners();
					super.onDispose();
				}
			};
			loaded = false;
			xulRunnerEditor.getBrowser().addProgressListener(new ProgressListener() {

				public void changed(ProgressEvent event) {
				}

				public void completed(ProgressEvent event) {
					loaded = true;
					xulRunnerEditor.getBrowser().removeProgressListener(this);
					//here we switchs xulrunner to design mode JBIDE-2505
				}
				
			});
			
			doctype = DocTypeUtil.getDoctype(getEditorInput());
			xulRunnerEditor.setText(doctype
					+ DocTypeUtil.getContentInitFile(new File(INIT_URL)));
			// Wait while visual part is loaded
			while (!loaded) {
				if (!Display.getCurrent().readAndDispatch())
					Display.getCurrent().sleep();
			}
			xulRunnerEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		}
		catch (XulRunnerException e) {
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

	private ToolItem createToolItem(ToolBar parent, int type, String image,
			String toolTipText) {
		ToolItem item = new ToolItem(parent, type);
		item.setImage(ImageDescriptor
				.createFromFile(MozillaEditor.class, image).createImage());
		item.setToolTipText(toolTipText);

		// add dispose listener
		item.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				// dispose tollitem's image
				((ToolItem) e.widget).getImage().dispose();

			}
		});
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
		if (vpeToolBarManager != null) {
			vpeToolBarManager.dispose();
			vpeToolBarManager = null;
		}
		
//		removeDomEventListeners();
		if(getController()!=null) {
			controller.dispose();
			controller=null;
		}
		if (xulRunnerEditor != null) {
			xulRunnerEditor.getBrowser().dispose();
			xulRunnerEditor = null;
		}

		this.controller = null;
		formatControllerManager.setVpeController(null);
		formatControllerManager=null;
		super.dispose();
		
	}

	public void setEditorLoadWindowListener(EditorLoadWindowListener listener) {
		editorLoadWindowListener = listener;
	}

	public void setEditorDomEventListener(EditorDomEventListener listener) {
		editorDomEventListener = listener;
		if (getContentAreaEventListener() != null) {
			
			getContentAreaEventListener().setEditorDomEventListener(listener);
		}
	}

	public nsIDOMDocument getDomDocument() {
		if (domDocument == null) {
			domDocument = xulRunnerEditor.getDOMDocument();
		}
		return domDocument;
	}
	
	/**
	 * @param domDocument the domDocument to set
	 */
	protected void setDomDocument(nsIDOMDocument domDocument) {
		
		this.domDocument = domDocument;
	}

	public nsIDOMElement getContentArea() {
		return contentArea;
	}
	/**
	 * Sets content area element
	 * @return
	 */
	protected void setContentArea(nsIDOMElement element) {
		
		 this.contentArea=element;
	}

	public nsIDOMNode getHeadNode() {
		return headNode;
	}

	public Menu getMenu() {
		return xulRunnerEditor.getBrowser().getMenu();
	}

	public Control getControl() {
		return xulRunnerEditor.getBrowser();
	}

	protected nsIDOMElement findContentArea() {
		nsIDOMElement area = xulRunnerEditor.getDOMDocument().getDocumentElement();
		nsIDOMNodeList nodeList = xulRunnerEditor.getDOMDocument().getElementsByTagName(HTML.TAG_BODY);
		long length = nodeList.getLength();
		for(long i=0; i<length; i++) {
			nsIDOMNode node = nodeList.item(i);
			if (isContentArea(node)) {
				if (node.getNodeType() != nsIDOMNode.ELEMENT_NODE) {
					throw new RuntimeException("The content area node should by element node."); //$NON-NLS-1$
				}

				area = (nsIDOMElement) node.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
				break;
			}
		}
		if (area == null) {
			return null;
		}

		nsIDOMNode root = xulRunnerEditor.getDOMDocument().getDocumentElement();
		headNode = findHeadNode(root);


		return area;
	}

	private nsIDOMNode findHeadNode(nsIDOMNode root){
		nsIDOMNode headNode = findChildNode(root, HTML.TAG_HEAD); 
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
		if (editorLoadWindowListener != null) {
			editorLoadWindowListener.load();
		}
	}

	protected void addDomEventListeners() {
		if (contentArea != null) {
			if (getContentAreaEventListener() != null) {
				getContentAreaEventListener().setVisualEditor(xulRunnerEditor);
				setContentAreaEventTarget((nsIDOMEventTarget) contentArea.queryInterface(nsIDOMEventTarget.NS_IDOMEVENTTARGET_IID));
				
				//add mozilla event handlers
				getContentAreaEventTarget().addEventListener(MozillaDomEventListener.CLICKEVENTTYPE, getContentAreaEventListener(), false); 
				getContentAreaEventTarget().addEventListener(MozillaDomEventListener.MOUSEDOWNEVENTTYPE, getContentAreaEventListener(), false); 
				getContentAreaEventTarget().addEventListener(MozillaDomEventListener.MOUSEUPEVENTTYPE, getContentAreaEventListener(), false); 
				getContentAreaEventTarget().addEventListener(MozillaDomEventListener.MOUSEMOVEEVENTTYPE, getContentAreaEventListener(), false); 
				getContentAreaEventTarget().addEventListener(MozillaDomEventListener.CONTEXTMENUEVENTTYPE, getContentAreaEventListener(), false);
				getContentAreaEventTarget().addEventListener(MozillaDomEventListener.DRAGDROPEVENT, getContentAreaEventListener(), false);
				getContentAreaEventTarget().addEventListener(MozillaDomEventListener.DRAGENTEREVENT, getContentAreaEventListener(), false);
				getContentAreaEventTarget().addEventListener(MozillaDomEventListener.DRAGEXITEVENT,getContentAreaEventListener(), false);
				getContentAreaEventTarget().addEventListener(MozillaDomEventListener.DRAGGESTUREEVENT, getContentAreaEventListener(), false);
				getContentAreaEventTarget().addEventListener(MozillaDomEventListener.DRAGOVEREVENT, getContentAreaEventListener(), false);
				getContentAreaEventTarget().addEventListener(MozillaDomEventListener.DBLCLICK, getContentAreaEventListener(), false);
				documentEventTarget = (nsIDOMEventTarget) getDomDocument().queryInterface(nsIDOMEventTarget.NS_IDOMEVENTTARGET_IID);
				documentEventTarget.addEventListener(MozillaDomEventListener.KEYPRESS, getContentAreaEventListener(), false);
			} else {
				//baseEventListener = new MozillaBaseEventListener();
			}
		}
	}

	protected void removeDomEventListeners() {
		if (getContentAreaEventTarget() != null && getContentAreaEventListener() != null) {
			getContentAreaEventTarget().removeEventListener(MozillaDomEventListener.CLICKEVENTTYPE, getContentAreaEventListener(), false); 
			getContentAreaEventTarget().removeEventListener(MozillaDomEventListener.MOUSEDOWNEVENTTYPE, getContentAreaEventListener(), false); 
			getContentAreaEventTarget().removeEventListener(MozillaDomEventListener.MOUSEUPEVENTTYPE, getContentAreaEventListener(), false); 
			getContentAreaEventTarget().removeEventListener(MozillaDomEventListener.MOUSEMOVEEVENTTYPE, getContentAreaEventListener(), false); 
			getContentAreaEventTarget().removeEventListener(MozillaDomEventListener.CONTEXTMENUEVENTTYPE, getContentAreaEventListener(), false);
			getContentAreaEventTarget().removeEventListener(MozillaDomEventListener.DRAGDROPEVENT, getContentAreaEventListener(), false);
			getContentAreaEventTarget().removeEventListener(MozillaDomEventListener.DRAGENTEREVENT, getContentAreaEventListener(), false);
			getContentAreaEventTarget().removeEventListener(MozillaDomEventListener.DRAGEXITEVENT, getContentAreaEventListener(), false);
			getContentAreaEventTarget().removeEventListener(MozillaDomEventListener.DRAGGESTUREEVENT, getContentAreaEventListener(), false);
			getContentAreaEventTarget().removeEventListener(MozillaDomEventListener.DRAGOVEREVENT, getContentAreaEventListener(), false);
			getContentAreaEventTarget().removeEventListener(MozillaDomEventListener.DBLCLICK, getContentAreaEventListener(), false);
		
			if (domDocument != null && documentEventTarget != null) {
				documentEventTarget.removeEventListener(MozillaDomEventListener.KEYPRESS, getContentAreaEventListener(), false); 
			}
			getContentAreaEventListener().setVisualEditor(null);
			getContentAreaEventListener().setEditorDomEventListener(null);
			setContentAreaEventTarget(null);
			setContentAreaEventListener(null);
			documentEventTarget = null;
		}
	}

	private void addSelectionListener() {
		if (getContentAreaEventListener() != null&&xulRunnerEditor!=null) {
			
			xulRunnerEditor.addSelectionListener(getContentAreaEventListener());
			
		}
	}

	public void setSelectionRectangle(/*nsIDOMElement*/nsIDOMNode element, int resizerConstrains, boolean scroll) {
		if (getContentAreaEventListener() != null) {
			xulRunnerEditor.setSelectionRectangle(element, resizerConstrains, scroll);
		}
	}

	/**
	 * Show resizer markers
	 */
	public void showResizer() {
		if (getContentAreaEventListener() != null) {
			xulRunnerEditor.showResizer();
		}
	}

	/**
	 * Hide resizer markers
	 */
	public void hideResizer() {
		if (getContentAreaEventListener() != null) {
			xulRunnerEditor.hideResizer();
		}
	}

	/**
	 * @return the xulRunnerEditor
	 */
	public XulRunnerEditor getXulRunnerEditor() {
		return xulRunnerEditor;
	}

	/**
	 * @param xulRunnerEditor the xulRunnerEditor to set
	 */
	protected void setXulRunnerEditor(XulRunnerEditor xulRunnerEditor) {
		this.xulRunnerEditor = xulRunnerEditor;
	}

	/**
	 * @return the controller
	 */
	public VpeController getController() {
		return controller;
	}

	/**
	 * @return the link
	 */
	protected Link getLink() {
		return link;
	}

	/**
	 * @param link the link to set
	 */
	protected void setLink(Link link) {
		this.link = link;
	}

	/**
	 * @return the contentAreaEventTarget
	 */
	public nsIDOMEventTarget getContentAreaEventTarget() {
		return contentAreaEventTarget;
	}

	/**
	 * @param contentAreaEventTarget the contentAreaEventTarget to set
	 */
	public void setContentAreaEventTarget(nsIDOMEventTarget contentAreaEventTarget) {
		this.contentAreaEventTarget = contentAreaEventTarget;
	}

	/**
	 * @return the contentAreaEventListener
	 */
	public MozillaDomEventListener getContentAreaEventListener() {
		if(contentAreaEventListener==null) {
			
			contentAreaEventListener = new MozillaDomEventListener();
		}
		return contentAreaEventListener;
	}

	/**
	 * @param contentAreaEventListener the contentAreaEventListener to set
	 */
	public void setContentAreaEventListener(
			MozillaDomEventListener contentAreaEventListener) {
		
		this.contentAreaEventListener = contentAreaEventListener;
	}
	
	
	/**
	 * 
	 */
	private void onReloadWindow() {

		removeDomEventListeners();
		contentArea = findContentArea();
		addDomEventListeners();
		controller.reinit();

	}
	
	/**
	 * 
	 */
	public void reload() {

		doctype = DocTypeUtil.getDoctype(getEditorInput());
		xulRunnerEditor.setText(doctype
				+ DocTypeUtil.getContentInitFile(new File(INIT_URL)));

	}
	
	/**
	 * @return Doctype for document
	 */
	public String getDoctype() {
		return doctype;
	}
	
	public boolean isRefreshPage() {
		return isRefreshPage;
	}

	public void setRefreshPage(boolean isRefreshPage) {
		this.isRefreshPage = isRefreshPage;
	}
	/**
	 * Returns Editor for This Document
	 * @return
	 */
	public nsIEditor getEditor() {
		
		if(editor==null) {
			//creating editing session
			nsIEditingSession iEditingSession = (nsIEditingSession) getXulRunnerEditor().
							getComponentManager().createInstanceByContractID(XPCOM.NS_EDITINGSESSION_CONTRACTID, null, nsIEditingSession.NS_IEDITINGSESSION_IID);
			//make window editable
			iEditingSession.makeWindowEditable(getXulRunnerEditor().getWebBrowser().getContentDOMWindow(), "html", true); //$NON-NLS-1$
			//here we setup editor for window
			iEditingSession.setupEditorOnWindow(getXulRunnerEditor().getWebBrowser().getContentDOMWindow());
			//getting some editor to disable some actions
			editor = iEditingSession.getEditorForWindow(getXulRunnerEditor().getWebBrowser().getContentDOMWindow());
			editor.setFlags(nsIPlaintextEditor.eEditorReadonlyMask);
			//here we hide nsIHTMLObjectResizers
			nsIHTMLObjectResizer htmlObjectResizer = (nsIHTMLObjectResizer) editor.queryInterface(nsIHTMLObjectResizer.NS_IHTMLOBJECTRESIZER_IID);
			//we disable abject resizers
			htmlObjectResizer.hideResizers();
			htmlObjectResizer.setObjectResizingEnabled(false);
			//here we getting position editor and disable it's too
			nsIHTMLAbsPosEditor htmlAbsPosEditor = (nsIHTMLAbsPosEditor) editor.queryInterface(nsIHTMLAbsPosEditor.NS_IHTMLABSPOSEDITOR_IID);
			htmlAbsPosEditor.setAbsolutePositioningEnabled(false);
			//here we getting inline table editor and disable it's too
			nsIHTMLInlineTableEditor inlineTableEditor = (nsIHTMLInlineTableEditor) editor.queryInterface(nsIHTMLInlineTableEditor.NS_IHTMLINLINETABLEEDITOR_IID);
			inlineTableEditor.setInlineTableEditingEnabled(false);
			
		}
		return editor;
	}
}