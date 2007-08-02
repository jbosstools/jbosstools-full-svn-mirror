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

import org.jboss.tools.common.model.ui.dnd.ModelTransfer;
import org.eclipse.jface.util.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.wst.common.ui.internal.dnd.ObjectTransfer;
import org.eclipse.wst.common.ui.internal.dnd.ViewerDropAdapter;
import org.eclipse.wst.sse.core.internal.model.FactoryRegistry;
import org.eclipse.wst.sse.ui.internal.contentoutline.*;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.ui.internal.contentoutline.*;
import org.jboss.tools.jst.jsp.editor.IJSPTextEditor;
import org.w3c.dom.Node;

import org.jboss.tools.common.model.ui.editors.dnd.context.DropContext;
import org.jboss.tools.vpe.editor.dnd.context.JSPViewerDropAdapter;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMMouseEvent;

public class VpeDropWindow extends Window {
	static final Color BACKGROUND_COLOR = new Color(null, 0xff, 0xff, 0xcd);
	nsIDOMMouseEvent event;
	IJSPTextEditor editor;
	boolean active = false;
	TreeViewer treeViewer;
	Tree tree;
	
	int screenX;
	int screenY;
	String flavor;
	Node initialTargetNode;
	Node rootNode;
	
	private DelegatingDropAdapter fDropAdapter;
	private DropTarget fDropTarget;

	public VpeDropWindow(Shell shell) {
		super(shell);
		setShellStyle(0);
	}
	
	public void setEditor(IJSPTextEditor editor) {
		this.editor = editor;
	}
	
	public void setEventPosition(int screenX, int screenY) {
		this.screenX = screenX;
		this.screenY = screenY;
	}
	
	public void setInitialTargetNode(Node n) {
		initialTargetNode = n;
		rootNode = initialTargetNode;
		for (int i = 0; i < 3; i++) {
			Node p = rootNode.getParentNode();
			if(p != null) rootNode = p;
		}
	}

	protected Control createContents(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		parent.setLayout(layout);
		tree = new Tree(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		treeViewer = new /*SourceEditor*/TreeViewer(tree);
		IDOMModel model = (IDOMModel)editor.getVPEController().getModel();
		FactoryRegistry factoryRegistry = model.getFactoryRegistry();
//		IJFaceNodeAdapterFactory adapterFactory = (IJFaceNodeAdapterFactory) 
			factoryRegistry.getFactoryFor(IJFaceNodeAdapter.class);
		JFaceNodeContentProvider contentProvider = new JFaceNodeContentProvider();
		treeViewer.setContentProvider(contentProvider);
		JFaceNodeLabelProvider labelProvider = new JFaceNodeLabelProvider();
		treeViewer.setLabelProvider(labelProvider);
		treeViewer.setInput(rootNode);

		tree.setBackground(BACKGROUND_COLOR);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.verticalIndent = 0;
		data.horizontalIndent = 0;
		data.widthHint = 150;
		data.heightHint = 150;
		tree.setLayoutData(data);
		
		treeViewer.setExpandedState(initialTargetNode, true);
		treeViewer.setSelection(new StructuredSelection(initialTargetNode), true);

		KeyListener listener = new KeyListener(){
			public void keyPressed(KeyEvent e) {
			}
			public void keyReleased(KeyEvent e) {
				close();
			}
			
		};
		getShell().addKeyListener(listener);
		tree.addKeyListener(listener);
		initDrop();
		return tree;
	}
	
	public int open() {
		active = true;
		return super.open();
	}
	
	public boolean close() {
		active = false;
		return super.close();
	}

	protected void initializeBounds() {
		super.initializeBounds();
		Rectangle r = getShell().getBounds();
		TreeItem item = findItem(tree.getItems());
		if(item != null) {
			Rectangle ri = item.getBounds();
			int x = screenX - ri.x - ri.width / 2;
			int y = screenY - ri.y - ri.height / 2;
			getShell().setBounds(getConstrainedShellBounds(
					new Rectangle(x, y, r.width, r.height)));
		}		
	}

	private TreeItem findItem(TreeItem[] is) {
		for (int i = 0; i < is.length; i++) {
			if(is[i].getData() == initialTargetNode) return is[i];
		}
		for (int i = 0; i < is.length; i++) {
			TreeItem[] iss = is[i].getItems();
			TreeItem result = findItem(iss);
			if(result != null) return result;
		}
		return null;
	}

	void initDrop() {
		fDropAdapter = new DelegatingDropAdapter();
		fDropTarget = new DropTarget(treeViewer.getControl(), DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK);
		TransferDropTargetListener[] dropListeners = getTransferDropTargetListeners();
		if(dropListeners != null) {
			for (int i = 0; i < dropListeners.length; i++) {
				fDropAdapter.addDropTargetListener(dropListeners[i]);
			}
		}
		fDropTarget.addDropListener(fDropAdapter);
		fDropTarget.setTransfer(fDropAdapter.getTransfers());
	}

	public TransferDropTargetListener[] getTransferDropTargetListeners() {
		TransferDropTargetListener[] fTransferDropTargetListeners = null;
		if (fTransferDropTargetListeners == null) {
			Transfer[] transfers = new Transfer[]{
				ModelTransfer.getInstance(),
				TextTransfer.getInstance(),
				HTMLTransfer.getInstance(),
				ObjectTransfer.getInstance(),
				FileTransfer.getInstance(),
			};
			fTransferDropTargetListeners = new TransferDropTargetListener[transfers.length];
			for (int i = 0; i < transfers.length; i++) {
				final Transfer transfer = transfers[i];
				DropContext dropContext = new DropContext();
				final ViewerDropAdapter dropAdapter = new JSPViewerDropAdapter(transfer, treeViewer, editor, transfer, dropContext); 
				fTransferDropTargetListeners[i] = new TransferDropTargetListener() {
					public void dragEnter(DropTargetEvent event) {
						dropAdapter.dragEnter(event);
					}

					public void dragLeave(DropTargetEvent event) {
						dropAdapter.dragLeave(event);
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								close();
							}
						});
					}

					public void dragOperationChanged(DropTargetEvent event) {
						dropAdapter.dragOperationChanged(event);
					}

					public void dragOver(DropTargetEvent event) {
						dropAdapter.dragOver(event);
					}

					public void drop(DropTargetEvent event) {
						dropAdapter.drop(event);
					}

					public void dropAccept(DropTargetEvent event) {
						dropAdapter.dropAccept(event);
					}

					public Transfer getTransfer() {
						return transfer;
					}

					public boolean isEnabled(DropTargetEvent event) {
						return getTransfer().isSupportedType(event.currentDataType);
					}
				};
			}
		}
		return fTransferDropTargetListeners;
	}
	
}
