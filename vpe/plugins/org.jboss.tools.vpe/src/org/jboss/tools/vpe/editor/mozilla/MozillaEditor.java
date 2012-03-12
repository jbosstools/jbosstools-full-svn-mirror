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

import static org.jboss.tools.vpe.xulrunner.util.XPCOM.queryInterface;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.internal.part.StatusPart;
import org.eclipse.ui.part.EditorPart;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.mozilla.listener.EditorLoadWindowListener;
import org.jboss.tools.vpe.editor.mozilla.listener.MozillaResizeListener;
import org.jboss.tools.vpe.editor.mozilla.listener.MozillaTooltipListener;
import org.jboss.tools.vpe.editor.toolbar.IVpeToolBarManager;
import org.jboss.tools.vpe.editor.toolbar.VpeDropDownMenu;
import org.jboss.tools.vpe.editor.toolbar.VpeToolBarManager;
import org.jboss.tools.vpe.editor.toolbar.format.FormatControllerManager;
import org.jboss.tools.vpe.editor.toolbar.format.TextFormattingToolBar;
import org.jboss.tools.vpe.editor.util.DocTypeUtil;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.jboss.tools.vpe.xulrunner.XulRunnerException;
import org.jboss.tools.vpe.xulrunner.browser.XulRunnerBrowser;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.jboss.tools.vpe.xulrunner.util.XPCOM;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMEventTarget;
import org.mozilla.interfaces.nsIDOMNamedNodeMap;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.interfaces.nsIDOMWindow;
import org.mozilla.interfaces.nsIEditingSession;
import org.mozilla.interfaces.nsIEditor;
import org.mozilla.interfaces.nsIHTMLAbsPosEditor;
import org.mozilla.interfaces.nsIHTMLInlineTableEditor;
import org.mozilla.interfaces.nsIHTMLObjectResizer;
import org.mozilla.interfaces.nsIPlaintextEditor;

public class MozillaEditor extends EditorPart implements IReusableEditor {
	
	protected static final File INIT_FILE = new File(VpePlugin.getDefault().getResourcePath("ve"), "init.html"); //$NON-NLS-1$ //$NON-NLS-2$
	public static final String CONTENT_AREA_ID = "__content__area__"; //$NON-NLS-1$
	
	private XulRunnerEditor xulRunnerEditor;
	private nsIDOMElement contentArea;
	private nsIDOMNode headNode;
	private MozillaEventAdapter mozillaEventAdapter = createMozillaEventAdapter();
	private EditorLoadWindowListener editorLoadWindowListener;
	private IVpeToolBarManager vpeToolBarManager;
	private FormatControllerManager formatControllerManager = new FormatControllerManager();
	private VpeController controller;
	private boolean isRefreshPage = false;
	private String doctype;
	
	/**
	 * Used for manipulation of browser in design mode,
	 * for example enable or disable readOnlyMode
	 */
	private nsIEditor editor;
	private VpeDropDownMenu dropDownMenu = null;
	private MozillaResizeListener resizeListener;
	private MozillaTooltipListener tooltipListener;

	public void doSave(IProgressMonitor monitor) {}
	public void doSaveAs() {}

	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.setSite(site);
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
	@Override
	public void createPartControl(final Composite parent) {

		GridLayout layout = new GridLayout(2,false);
		layout.marginHeight = 0;
		layout.marginWidth = 2;
		layout.verticalSpacing = 2;
		layout.horizontalSpacing = 2;
		layout.marginBottom = 0;
		parent.setLayout(layout);

		// Editors and Toolbar composite 
		Composite cmpEdTl = new Composite(parent, SWT.NONE);
		GridLayout layoutEdTl = new GridLayout(1, false);
		layoutEdTl.verticalSpacing = 0;
		layoutEdTl.marginHeight = 0;
		layoutEdTl.marginBottom = 3;
		layoutEdTl.marginWidth = 0;
		cmpEdTl.setLayout(layoutEdTl);
		cmpEdTl.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		/*
		 * Create VPETextFormattingToolBar
		 */
		vpeToolBarManager = new VpeToolBarManager();
		if (vpeToolBarManager != null) {
			vpeToolBarManager.createToolBarComposite(cmpEdTl);
			vpeToolBarManager.addToolBar(new TextFormattingToolBar(formatControllerManager));
		}

		//Create a composite to the Editor
		final Composite cmpEd = new Composite (cmpEdTl, SWT.NATIVE);
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

		//TODO Add a paintListener to cmpEd and give him a border top and left only
		Color buttonDarker = parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
		cmpEd.setBackground(buttonDarker);

		try {
			xulRunnerEditor = new XulRunnerEditor2(cmpEd, this);
			xulRunnerEditor.getBrowser().addProgressListener(new ProgressListener() {
				public void changed(ProgressEvent event) {}
				public void completed(ProgressEvent event) {
					if (MozillaEditor.this.getXulRunnerEditor().getWebBrowser() != null) {
						//process this code only in case when editor hasn't been disposed,
						//see https://jira.jboss.org/browse/JBIDE-6373
						MozillaEditor.this.onLoadWindow();
						xulRunnerEditor.getBrowser().removeProgressListener(this);
					}
				}
			});
			setInitialContent();
			xulRunnerEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		} catch (Throwable t) {
			showXulRunnerError(cmpEd, t);
		}
	}

	/**
	 * Sets initial content to the {@link xulRunnerEditor}.
	 *
	 * @see #INIT_FILE
	 */
	protected void setInitialContent() {
		final String html = DocTypeUtil.prepareInitFile(
				INIT_FILE, getEditorInput());
		
		// Workaround of JBIDE-4345.
		// Due to a bug in org.eclipse.swt.browser.Mozilla we cannot simply
		// set initial html code as xulRunnerEditor.setText(html).
		// Instead of it we create a temporary file containing 
		// the html code and set it to the Mozilla browser as URL.
		// When we will not have to support Eclipse 3.5.0
		// all the following code should be replaced by the following line:
		// xulRunnerEditor.setText(html);
		File tmp = null;
		Writer out = null;
		try {
			tmp = File.createTempFile(
					"temp", ".html"); //$NON-NLS-1$//$NON-NLS-2$
			tmp.deleteOnExit();
			out = new FileWriter(tmp);
			out.write(html);
		} catch (IOException e) {
			VpePlugin.getPluginLog().logError(e);
		} finally {
			try {
				if (out != null) {
					out.close();
					if (tmp != null) {
						xulRunnerEditor.setURL("file://"	//$NON-NLS-1$
								+ tmp.getCanonicalPath());
					}
				}
			} catch (IOException e) {
				VpePlugin.getPluginLog().logError(e);
			} finally {
				if (tmp != null) {
					tmp.delete();
				}
			}
		}
	}

	/**
	 * Logs given {@code throwable} (may be wrapped) and shows error message in 
	 * the {@code parent} composite.
	 */
	protected void showXulRunnerError(Composite parent,
			Throwable originalThrowable) {
		Throwable throwable = wrapXulRunnerError(originalThrowable);
		String errorMessage = MessageFormat.format(
				VpeUIMessages.MOZILLA_LOADING_ERROR, throwable.getMessage());
		VpePlugin.getPluginLog().logError(errorMessage, throwable);

		parent.setLayout(new GridLayout());
		Composite statusComposite = new Composite(parent, SWT.NONE);
		Color bgColor= parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
		Color fgColor= parent.getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND);
		parent.setBackground(bgColor);
		parent.setForeground(fgColor);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.widthHint = 0;
		gridData.heightHint = 0;
		statusComposite.setLayoutData(gridData);

		IStatus displayStatus = new Status(IStatus.ERROR,
				VpePlugin.PLUGIN_ID, errorMessage, throwable);
		new StatusPart(statusComposite, displayStatus);

		final Link link = new Link(parent, SWT.WRAP);
		link.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		link.setBackground(bgColor);
		link.setText(VpeUIMessages.MOZILLA_LOADING_ERROR_LINK_TEXT);
		link.setToolTipText(VpeUIMessages.MOZILLA_LOADING_ERROR_LINK);
		link.setForeground(link.getDisplay().getSystemColor(SWT.COLOR_BLUE));

		link.addMouseListener(new MouseListener() {
			public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
				BusyIndicator.showWhile(link.getDisplay(), new Runnable() {
					public void run() {
						URL theURL = null;
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
			public void mouseDoubleClick(MouseEvent e) {}
			public void mouseUp(MouseEvent e) {}
		});
	}
	
	/**
	 * Wraps instances of {@code SWTError} generated by XULRunner
	 * into a {@code XulRunnerException}. All other throwables are
	 * returned as is. In certain cases, error's messages may be
	 * substituted by messages that are more clear to the user.
	 */
	/* There is a workaround for JBIDE-7601 (Show XULRunner exception only once).
	 * 
	 * The problem is that the method {@code VpePlugin.getPluginLog().logError(...)}
	 * shows pop-ups for instances of {@code SWTError} because it considers
	 * them as {@code fatal}. To workaround of it, these errors are wrapped
	 * to an {@code XulRunnerException}.
	 * 
	 * @see org.eclipse.ui.internal.ide.IDEWorkbenchErrorHandler#isFatal(StatusAdapter)
	 * @see org.eclipse.swt.browser.Mozilla
	 */
	private Throwable wrapXulRunnerError(Throwable originalThrowable) {
		Throwable throwable = originalThrowable;
		if (throwable instanceof SWTError && throwable.getMessage() != null) {
			String message = throwable.getMessage(); 
			if(message.contains("XPCOM error ") //$NON-NLS-1$
					|| message.contains(" [Failed to use detected XULRunner: ") //$NON-NLS-1$
					|| message.contains(" [Unknown Mozilla path (MOZILLA_FIVE_HOME not set)]") //$NON-NLS-1$
					|| message.contains(" [Mozilla GTK2 required (GTK1.2 detected)]") //$NON-NLS-1$
					|| message.contains(" [MOZILLA_FIVE_HOME='") //$NON-NLS-1$
					|| message.contains(" [MOZILLA_FIVE_HOME may not point at an embeddable GRE] [NS_InitEmbedding ")) { //$NON-NLS-1$
				throwable = new XulRunnerException(originalThrowable);
			} else if (message.contains(" [Could not detect registered XULRunner to use]")) {//$NON-NLS-1$
				if (System.getProperty(XulRunnerBrowser.XULRUNNER_PATH) == null
						&& !XulRunnerBrowser.isCurrentPlatformOfficiallySupported()) {
					throwable = new XulRunnerException(
							MessageFormat.format(
									VpeUIMessages.CURRENT_PLATFORM_IS_NOT_SUPPORTED,
									XulRunnerBrowser.CURRENT_PLATFORM_ID),
							originalThrowable);
				} else {
					throwable = new XulRunnerException(originalThrowable);
				}
			}
		}
		return throwable;
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
		
		if (dropDownMenu != null) {
			dropDownMenu.dispose();
			dropDownMenu = null;
		}

//		removeDomEventListeners();
		if(getController()!=null) {
			controller.dispose();
			controller = null;
		}
		if (xulRunnerEditor != null) {
			xulRunnerEditor.dispose();
			xulRunnerEditor = null;
		}

		formatControllerManager.setVpeController(null);
		formatControllerManager=null;
		headNode = null;
		contentArea = null;
		super.dispose();
	}

	public void setEditorLoadWindowListener(EditorLoadWindowListener listener) {
		editorLoadWindowListener = listener;
	}

	public nsIDOMDocument getDomDocument() {
			return xulRunnerEditor.getDOMDocument();
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
		nsIDOMElement area = null;
		nsIDOMNodeList nodeList = xulRunnerEditor.getDOMDocument().getElementsByTagName(HTML.TAG_BODY);
		long length = nodeList.getLength();
		for(long i=0; i<length; i++) {
			nsIDOMNode node = nodeList.item(i);
			if (isContentArea(node)) {
				if (node.getNodeType() != nsIDOMNode.ELEMENT_NODE) {
					throw new RuntimeException("The content area node should by element node."); //$NON-NLS-1$
				}
				area = queryInterface(node, nsIDOMElement.class);
				break;
			}
		}
		if (area == null) {
			//fix for jbide-3396, if we can't find a boody element, we should create it
			area = xulRunnerEditor.getDOMDocument().createElement(HTML.TAG_BODY);
			xulRunnerEditor.getDOMDocument().getDocumentElement().appendChild(area);
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
		attachMozillaEventAdapter();
		if (editorLoadWindowListener != null) {
			editorLoadWindowListener.load();
		}
	}
	
	protected MozillaEventAdapter createMozillaEventAdapter() {
		return new MozillaEventAdapter();
	}

	protected void attachMozillaEventAdapter() {
		if (contentArea != null) {
//			getContentAreaEventListener().setVisualEditor(xulRunnerEditor);
			nsIDOMWindow window = xulRunnerEditor.getWebBrowser().getContentDOMWindow();
			mozillaEventAdapter.attach(window, queryInterface(contentArea, nsIDOMEventTarget.class));
		}
	}

	void detachMozillaEventAdapter() {
		mozillaEventAdapter.detach();
//		getContentAreaEventListener().setVisualEditor(null);
	}

	public void setSelectionRectangle(/*nsIDOMElement*/List<nsIDOMNode> nodes, int resizerConstrains) {
		xulRunnerEditor.setSelectionRectangle(nodes, resizerConstrains);
	}

	/**
	 * Show resizer markers
	 */
	public void showResizer() {
		xulRunnerEditor.showResizer();
	}

	/**
	 * Hide resizer markers
	 */
	public void hideResizer() {
		xulRunnerEditor.hideResizer();
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

	public MozillaEventAdapter getMozillaEventAdapter() {
		return mozillaEventAdapter;
	}
	
	public void onReloadWindow() {
		detachMozillaEventAdapter();
		xulRunnerEditor.removeResizeListener();
		contentArea = findContentArea();
		attachMozillaEventAdapter();
		xulRunnerEditor.addResizeListener();
		controller.reinit();
	}
	
	public void reload() {
		doctype = DocTypeUtil.getDoctype(getEditorInput());
		//cause page to be refreshed
		setRefreshPage(true);
		setInitialContent();
	}
	
	/**
	 * Initialized design mode in visual refresh
	 */
	public void initDesingMode() {
		tearDownEditor();
		getEditor();
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
	
	public void reinitDesignMode() {
		tearDownEditor();
		getEditor();
	}
	
	public void tearDownEditor() {
		if (editor != null) {
			nsIEditingSession iEditingSession = (nsIEditingSession) getXulRunnerEditor().
							getComponentManager().createInstanceByContractID(XPCOM.NS_EDITINGSESSION_CONTRACTID, null, nsIEditingSession.NS_IEDITINGSESSION_IID);
			nsIDOMWindow window = getXulRunnerEditor().getWebBrowser().getContentDOMWindow();
			iEditingSession.detachFromWindow(window);
			iEditingSession.tearDownEditorOnWindow(window);
			editor = null;
		}
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
			iEditingSession.makeWindowEditable(getXulRunnerEditor().getWebBrowser().getContentDOMWindow(), "html", true,true,true); //$NON-NLS-1$
			//here we setup editor for window
			iEditingSession.setupEditorOnWindow(getXulRunnerEditor().getWebBrowser().getContentDOMWindow());
			//getting some editor to disable some actions
			editor = iEditingSession.getEditorForWindow(getXulRunnerEditor().getWebBrowser().getContentDOMWindow());
			editor.setFlags(nsIPlaintextEditor.eEditorReadonlyMask);
			//here we hide nsIHTMLObjectResizers
			nsIHTMLObjectResizer htmlObjectResizer = queryInterface(editor, nsIHTMLObjectResizer.class);
			//we disable abject resizers
			htmlObjectResizer.hideResizers();
			htmlObjectResizer.setObjectResizingEnabled(false);
			//here we getting position editor and disable it's too
			nsIHTMLAbsPosEditor htmlAbsPosEditor = queryInterface(editor, nsIHTMLAbsPosEditor.class);
			htmlAbsPosEditor.setAbsolutePositioningEnabled(false);
			//here we getting inline table editor and disable it's too
			nsIHTMLInlineTableEditor inlineTableEditor = queryInterface(editor, nsIHTMLInlineTableEditor.class);
			inlineTableEditor.setInlineTableEditingEnabled(false);
			
		}
		return editor;
	}

	public VpeDropDownMenu getDropDownMenu() {
		return dropDownMenu;
	}

	public void setResizeListener(MozillaResizeListener resizeListener) {
		this.resizeListener = resizeListener;
	}

	public void setTooltipListener(MozillaTooltipListener tooltipListener) {
		this.tooltipListener = tooltipListener;
	}

	public MozillaResizeListener getResizeListener() {
		return resizeListener;
	}

	public MozillaTooltipListener getTooltipListener() {
		return tooltipListener;
	}

	public IVpeToolBarManager getVpeToolBarManager() {
		return vpeToolBarManager;
	}
}
