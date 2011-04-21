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
package org.jboss.tools.vpe.editor.dnd.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.wst.common.ui.internal.dnd.DragAndDropCommand;
import org.eclipse.wst.common.ui.internal.dnd.ObjectTransfer;
import org.eclipse.wst.common.ui.internal.dnd.ViewerDropAdapter;
import org.eclipse.wst.xml.core.internal.document.NodeImpl;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.ui.internal.dnd.XMLDragAndDropManager;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.ui.dnd.ModelTransfer;
import org.jboss.tools.common.model.ui.editors.dnd.context.DropContext;
import org.jboss.tools.common.model.ui.editors.dnd.context.InnerDragBuffer;
import org.jboss.tools.jst.jsp.editor.IJSPTextEditor;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.dnd.context.xpl.DragNodeCommand2;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author glory
 */

public class JSPViewerDropAdapter extends ViewerDropAdapter {
	Transfer sourceTransfer = null;
	protected DropContext dropContext;

	public JSPViewerDropAdapter(Transfer sourceTransfer, Viewer viewer, IJSPTextEditor editor, Transfer transfer, DropContext dropContext) {
		super(viewer, new JSPDragAndDropManager(editor, transfer, dropContext));
		this.dropContext = dropContext; 
	}
	
	protected void setCurrentDataType(DropTargetEvent event) {                                
	}

	public void drop(DropTargetEvent event) {
		dropContext.setDropTargetEvent(event);
		super.drop(event);
	}

	protected Collection getDragSource(DropTargetEvent event) {
		dropContext.setDropTargetEvent(event);
		if (ObjectTransfer.getInstance().isSupportedType(event.currentDataType)) {
			Object object = null;
			object = ObjectTransfer.getInstance().nativeToJava(event.currentDataType);
			if (object == null) return null;
	      	return extractDragSource(object);
		} else if(ModelTransfer.getInstance().isSupportedType(event.currentDataType)) {
			List list = new ArrayList();
			Object source = PreferenceModelUtilities.getPreferenceModel().getModelBuffer().source();
			if(source != null) list.add(source);
			return list;
		} else if(dropContext.getFlavor() != null) {
			List list = new ArrayList();
			list.add(dropContext);
			return list;
		}
		return null;
	}
}

class JSPDragAndDropManager extends XMLDragAndDropManager {
	IJSPTextEditor editor;
	Transfer sourceTransfer = null;
	DropContext dropContext;

	public JSPDragAndDropManager(IJSPTextEditor editor, Transfer sourceTransfer, DropContext dropContext) {
		this.editor = editor;
		this.sourceTransfer = sourceTransfer;
		this.dropContext = dropContext;
	}

	public DragAndDropCommand createCommand(Object target, float location, int operations, int operation, Collection source) {
		DragAndDropCommand result = null;
		Object sourceObject = getSource(source);
		if (target instanceof Node) {
			if(sourceObject instanceof Node) {
				result = new VpeDragNodeCommand(target, location, operations, operation, source);
			} else {
				result = new VpeDragAnyCommand(editor, target, location, operations, operation, source, dropContext);
			}
		} else {
			result = new EmptyDragCommand();
		}
		return result;
	}
	
	Object getSource(Collection source) {
		if(source == null || source.isEmpty()) return null;
		return source.iterator().next();
	}
}

class EmptyDragCommand implements DragAndDropCommand {
	public boolean canExecute() {
		return false;
	}
	public void execute() {}
	public int getFeedback() {
		return 0;
	}
	public int getOperation() {
		return 0;
	}
	public void reinitialize(Object target, float location, int operations, int operation, Collection sources) {}
}

class VpeDragNodeCommand extends DragNodeCommand2 {
	public VpeDragNodeCommand(Object target, float location, int operations, int operation, Collection sources) {
		super(target, location, operations, operation, sources,null);
	    if (!canExecute()) {
			this.operation = DND.DROP_NONE;
	    }
	}

	public boolean doModify(Node source, Node parentNode, Node refChild, boolean testOnly) {
		return super.doModify(source, parentNode, refChild, testOnly);
	}
}

class VpeDragAnyCommand extends DragNodeCommand2 {
	IJSPTextEditor editor;
	DropContext dropContext;

	public VpeDragAnyCommand(IJSPTextEditor editor, Object target, float location, int operations, int operation, Collection sources, DropContext dropContext) {
		super(target, location, operations, operation, sources,null);
		this.editor = editor;
		this.dropContext = dropContext;
	    if (!canExecute()) {
			this.operation = DND.DROP_NONE;
	    } else {
	    	this.operation = operation;
	    }
	}

	public boolean executeHelper(boolean testOnly) {
		boolean result = true;
		if (target instanceof Node) {
			Node targetNode = (Node) target;
			Node parentNode = getParentForDropPosition(targetNode);
			Node refChild = getRefChild(targetNode);

			Vector sourcesList = new Vector();
			sourcesList.addAll(sources);

			boolean performBatchUpdate = sourcesList.size() > 5;

			if (!testOnly) {
				beginModelChange(targetNode, performBatchUpdate);
			}
			if(sourcesList.size() > 0) {
//			for (Iterator i = sourcesList.iterator(); i.hasNext();) {
//				Object source = i.next();
				if (!(refChild == null && targetNode instanceof Attr)) {
					result = doMove(parentNode, refChild, testOnly);
				} else {
					result = false;
				}
//				if (!result) {
//					break;
//				}
			}
			if (!testOnly) {
				endModelChange(targetNode, performBatchUpdate);
			}
		} else {
			result = false;
		}
		return result;
	}

	public boolean doMove(Node parentNode, Node refChild, boolean testOnly) {
		if(testOnly) {
			return editor != null && editor.isEditable() && dropContext.getFlavor() != null;
		} else {
			if(InnerDragBuffer.object instanceof Node && editor instanceof IJSPTextEditor) {
				int offset = 0;
				if(refChild instanceof NodeImpl) {
					offset = ((NodeImpl)refChild).getIndex();
				}
				((IJSPTextEditor)editor).getVPEController().drop((Node)InnerDragBuffer.object, parentNode, offset);
				InnerDragBuffer.object = null;
			} else {
				int pos = getDropPosition(parentNode, refChild);
				editor.selectAndReveal(pos, 0);
				dropContext.runDropCommand(editor);
			}			
			return true;
		}
	}
	
	private int getDropPosition(Node parentNode, Node refChild) {
		IDOMNode p = (IDOMNode)parentNode;
		IDOMNode c = (IDOMNode)refChild;
		if(c != null) return c.getStartOffset();
		if(p instanceof IDOMElement) {
			IDOMElement n = (IDOMElement)p;
			int i = n.getStartEndOffset();
			if(i >= 0) return i;
		}
		NodeList list = p.getChildNodes();
		if(list.getLength() == 0) return p.getStartOffset();
		for (int i = 0; i < list.getLength(); i++) {
			if(list.item(i) instanceof IDOMNode) {
				c = (IDOMNode)list.item(i);
				return c.getStartOffset();
			}
		}
		return p.getStartOffset();
	}

}
