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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.editor.VpePreviewDomBuilder;
import org.jboss.tools.vpe.editor.VpeSourceDomBuilder;
import org.jboss.tools.vpe.editor.VpeVisualDomBuilder;
import org.jboss.tools.vpe.editor.bundle.BundleMap;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMEventTarget;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Element;

/**
 * a class implementation of mozilla previre 
 * @author A. Yukhovich
 */
public class MozillaPreview extends MozillaEditor {
	private XulRunnerEditor editor;
	private nsIDOMDocument domDocument;
	private nsIDOMEventTarget documentEventTarget;
	private nsIDOMElement contentArea;
	private nsIDOMNode headNode;
	private nsIDOMEventTarget contentAreaEventTarget;
	private MozillaDomEventListener contentAreaEventListener;
	private MozillaBaseEventListener baseEventListener;
	private EditorLoadWindowListener editorLoadWindowListener;
	private EditorDomEventListener editorDomEventListener;
	private VpeController controller;
	private Link link = null;	
	private VpeTemplateManager templateManager;	
	private VpePageContext pageContext;
	private BundleMap bundle;
	private StructuredTextEditor sourceEditor;
	private VpeEditorPart editPart;
	private VpeDomMapping domMapping;
	private IDOMDocument sourceDocument;

	public MozillaPreview(VpeEditorPart editPart, StructuredTextEditor sourceEditor) {
		templateManager = VpeTemplateManager.getInstance();

		this.sourceEditor = sourceEditor;
		this.editPart = editPart;
	}
	
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


	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(final Composite parent) {
		try {
			editor = new XulRunnerEditor(parent) {
				public void onLoadWindow() {
					super.onLoadWindow();
					MozillaPreview.this.onLoadWindow();
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
			editor.setURL(INIT_URL);
			editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		} catch (Exception e) {
	        Label title = new Label(parent, SWT.WRAP);
	        title.setText(VpeUIMessages.MOZILLA_LOADING_ERROR);
	        title.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	        link = new Link(parent, SWT.WRAP);
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
				}

				public void mouseUp(MouseEvent e) {
				}
	        });
	        link.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	        Label fill = new Label(parent, SWT.WRAP);		
	        fill.setLayoutData(new GridData(GridData.FILL_BOTH));	        
		}		
	}

	private ToolItem createToolItem(ToolBar parent, int type, String image, String toolTipText) {
		ToolItem item = new ToolItem(parent, type);
		item.setImage(ImageDescriptor.createFromFile(MozillaPreview.class, image).createImage());		
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
			contentArea = null;
		}
		if (domDocument != null) {
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
		// TODO Max Areshkau add Context Menu Listener
//		if (editor != null) {
//			editor.setContextMenuListener(listener);
//		}
	}

	/**
	 * 
	 */
	private void onLoadWindow() {
		contentArea = findContentArea();
		addDomEventListeners();
		addSelectionListener();
		if (editorLoadWindowListener != null) {
			editorLoadWindowListener.load();
		}
	}

	private void addDomEventListeners() {
		if (contentArea != null) {
			try {
				contentAreaEventListener = new MozillaDomEventListener();
			} catch (Exception e) {
			}

			if (contentAreaEventListener != null) {
				contentAreaEventListener.setVisualEditor(editor);
				contentAreaEventTarget = (nsIDOMEventTarget) contentArea.queryInterface(nsIDOMEventTarget.NS_IDOMEVENTTARGET_IID);
				contentAreaEventTarget.addEventListener("click", contentAreaEventListener, false); //$NON-NLS-1$
				contentAreaEventTarget.addEventListener("mousedown", contentAreaEventListener, false); //$NON-NLS-1$
				contentAreaEventTarget.addEventListener("mouseup", contentAreaEventListener, false); //$NON-NLS-1$
				contentAreaEventTarget.addEventListener("mousemove", contentAreaEventListener, false); //$NON-NLS-1$
	
				documentEventTarget = (nsIDOMEventTarget) getDomDocument().queryInterface(nsIDOMEventTarget.NS_IDOMEVENTTARGET_IID);
				documentEventTarget.addEventListener("keypress", contentAreaEventListener, false); //$NON-NLS-1$
			} else {
				baseEventListener = new MozillaBaseEventListener();
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
		// TODO Max Areshkau add Selection Controller
//		if (contentAreaEventListener != null) {
//			nsISelectionController selectionController = editor.getSelectionController();
//			selectionController.addSelectionListener(contentAreaEventListener);
//		}
	}

	private void removeSelectionListener() {
		// TODO Max Areshkau add Selection Controller
//		nsISelectionController selectionController = editor.getSelectionController();
//		selectionController.removeSelectionListener(contentAreaEventListener.getSelectionListener());
	}

	public void setSelectionRectangle(Element element, int resizerConstrains, boolean scroll) {
		// TODO Sergey Vasilyev figure out with selection in preview
//		if (contentAreaEventListener != null) {
//			editor.setSelectionRectangle((nsIDOMElement)element, resizerConstrains, scroll);
//		}
	}

	public void showResizer() {
	}

	public void hideResizer() {
	}

	/**
	 * 
	 */
	public void rebuildDom() {
		pageContext.getVisualBuilder().rebuildDom(sourceDocument);
	}

	/**
	 */
	public void buildDom() {
		bundle = new BundleMap();
		bundle.init(sourceEditor);
		
		pageContext = new VpePageContext(templateManager, bundle, editPart);
		
		domMapping = new VpeDomMapping(pageContext);
		VpeSourceDomBuilder sourceBuilder = new VpeSourceDomBuilder(domMapping, null, templateManager, sourceEditor, pageContext);		
		VpeVisualDomBuilder visualBuilder = new VpePreviewDomBuilder(domMapping, null, templateManager, this, pageContext);
		pageContext.setSourceDomBuilder(sourceBuilder);
		pageContext.setVisualDomBuilder(visualBuilder);

		IDOMModel sourceModel = (IDOMModel)sourceEditor.getModel();
		sourceDocument = sourceModel.getDocument();

		visualBuilder.buildDom(sourceDocument);		
	}
}