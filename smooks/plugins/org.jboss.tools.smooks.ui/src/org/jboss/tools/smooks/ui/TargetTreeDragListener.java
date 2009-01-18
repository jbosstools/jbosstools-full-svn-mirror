/**
 * 
 */
package org.jboss.tools.smooks.ui;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.jboss.tools.smooks.graphical.util.GraphicalAdapterFactory;
import org.jboss.tools.smooks.ui.gef.tools.SmooksCustomConnectionCreationTool;

/**
 * @author Dart
 *
 */
public class TargetTreeDragListener extends DragSourceAdapter {
	
	private TreeViewer hostViewer = null;
	
	private GraphicalViewer graphicalViewer = null;


	public TargetTreeDragListener(TreeViewer hostViewer , GraphicalViewer graphicalViewer){
		super();
		setHostViewer(hostViewer);
		setGraphicalViewer(graphicalViewer);
	}
	
	public GraphicalViewer getGraphicalViewer() {
		return graphicalViewer;
	}


	public void setGraphicalViewer(GraphicalViewer graphicalViewer) {
		this.graphicalViewer = graphicalViewer;
	}
	
	public TreeViewer getHostViewer() {
		return hostViewer;
	}

	public void setHostViewer(TreeViewer hostViewer) {
		this.hostViewer = hostViewer;
	}

	public void dragStart(DragSourceEvent event) {
		if(getHostViewer() != null){
			TreeViewer viewer = getHostViewer();
			IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
			if(selection == null) return;
			event.data = selection;
			TemplateTransfer.getInstance().setTemplate(
					viewer.getSelection());
			event.doit = true;
			
			getGraphicalViewer().getEditDomain().setActiveTool(
					new SmooksCustomConnectionCreationTool(selection.getFirstElement(),
							getGraphicalViewer()));
		}
	}
}
