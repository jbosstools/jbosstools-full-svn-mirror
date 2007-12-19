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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.jst.jsp.editor.IJSPTextEditor;
import org.jboss.tools.jst.jsp.editor.IVisualContext;
import org.jboss.tools.vpe.VpePlugin;
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

/**
 * a class implementation of mozilla preview 
 * @author A. Yukhovich
 */
public class MozillaPreview extends MozillaEditor {

	private EditorLoadWindowListener editorLoadWindowListener;
	private EditorDomEventListener editorDomEventListener;
	private VpePageContext pageContext;
	private StructuredTextEditor sourceEditor;
	private VpeEditorPart editPart;
	private IDOMDocument sourceDocument;

	public MozillaPreview(VpeEditorPart editPart, StructuredTextEditor sourceEditor) {
		setSourceEditor(sourceEditor);
		setEditPart(editPart);
	}
	

	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(final Composite parent) {
		try {
			setXulRunnerEditor(new XulRunnerEditor(parent) {
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
			});
			getXulRunnerEditor().setURL(INIT_URL);
			getXulRunnerEditor().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		} catch (Exception e) {
	        Label title = new Label(parent, SWT.WRAP);
	        title.setText(VpeUIMessages.MOZILLA_LOADING_ERROR);
	        title.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	        setLink(new Link(parent, SWT.WRAP));
	        getLink().setText(VpeUIMessages.MOZILLA_LOADING_ERROR_LINK_TEXT);
	        getLink().setToolTipText(VpeUIMessages.MOZILLA_LOADING_ERROR_LINK);
	        getLink().setForeground(getLink().getDisplay().getSystemColor(SWT.COLOR_BLUE));
	        getLink().addMouseListener(new MouseListener() {
	        	public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
	                BusyIndicator.showWhile(getLink().getDisplay(), new Runnable() {
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
	        getLink().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	        Label fill = new Label(parent, SWT.WRAP);		
	        fill.setLayoutData(new GridData(GridData.FILL_BOTH));	        
		}		
	}

	@Override
	public void setEditorLoadWindowListener(EditorLoadWindowListener listener) {
		editorLoadWindowListener = listener;
	}


	/**
	 * 
	 */
	private void onLoadWindow() {

		setContentArea(findContentArea());
		if (editorLoadWindowListener != null) {
			editorLoadWindowListener.load();
		}
	}
	@Override
	public void showResizer() {
	}

	@Override
	public void hideResizer() {
	}

	//TODO Max Areshkau logic error (we should first call buildDom and only then we can call rebuildDom)
	public void rebuildDom() {
		if (null != pageContext && null != pageContext.getVisualBuilder() && null != getSourceDocument()) {
			pageContext.getVisualBuilder().rebuildDom(getSourceDocument());
		}
	}

	//TODO Max Areshkau logic error (we should first call buildDom and only then we can call rebuildDom)
	public void buildDom() {
		BundleMap bundle = new BundleMap();
		bundle.init(getSourceEditor());
		
		pageContext = new VpePageContext(bundle, getEditPart());
		
		// vitali - temp solution
		pageContext.setVisualContext(getPageContext());
		VpeDomMapping domMapping = new VpeDomMapping(pageContext);
		VpeSourceDomBuilder sourceBuilder = new VpeSourceDomBuilder(domMapping, null, getSourceEditor(), pageContext);
		VpeVisualDomBuilder visualBuilder = new VpePreviewDomBuilder(domMapping, null, this, pageContext);
		pageContext.setSourceBuilder(sourceBuilder);
		pageContext.setVisualBuilder(visualBuilder);

		IDOMModel sourceModel = (IDOMModel)getSourceEditor().getModel();
		setSourceDocument(sourceModel.getDocument());

		visualBuilder.buildDom(getSourceDocument());		
	}

	/**
	 * @return the sourceDocument
	 */
	public IDOMDocument getSourceDocument() {
		return sourceDocument;
	}

	/**
	 * @param sourceDocument the sourceDocument to set
	 */
	protected void setSourceDocument(IDOMDocument sourceDocument) {
		this.sourceDocument = sourceDocument;
	}

	public IVisualContext getPageContext() {
		IVisualContext visualContext = null;
		if (sourceEditor instanceof IJSPTextEditor) {
			visualContext = ((IJSPTextEditor)sourceEditor).getPageContext();
		}
		return visualContext;
	}

	/**
	 * @return the editPart
	 */
	private VpeEditorPart getEditPart() {
		return editPart;
	}


	/**
	 * @param editPart the editPart to set
	 */
	private void setEditPart(VpeEditorPart editPart) {
		this.editPart = editPart;
	}

	/**
	 * @return the sourceEditor
	 */
	private StructuredTextEditor getSourceEditor() {
		return sourceEditor;
	}

	/**
	 * @param sourceEditor the sourceEditor to set
	 */
	private void setSourceEditor(StructuredTextEditor sourceEditor) {
		this.sourceEditor = sourceEditor;
	}

}