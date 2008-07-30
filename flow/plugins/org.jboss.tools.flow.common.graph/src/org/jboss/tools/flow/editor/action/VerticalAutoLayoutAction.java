package org.jboss.tools.flow.editor.action;

/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.flow.Activator;
import org.jboss.tools.flow.editor.core.AbstractConnectionWrapper;
import org.jboss.tools.flow.editor.core.AbstractFlowWrapper;
import org.jboss.tools.flow.editor.core.NodeWrapper;

/**
 * Action for auto layouting a process.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class VerticalAutoLayoutAction extends Action implements IAction {
	
	public static final String ID = "org.jboss.tools.process.editor.action.VerticalAutoLayoutAction";
	
	private GraphicalViewer diagramViewer;
	
	public VerticalAutoLayoutAction(GraphicalViewer diagramViewer) {
		this.diagramViewer = diagramViewer;
		initialize();
	}
	
	protected void initialize() {
		setId(ID);
		setText("Auto Layout (Vertical)");
		setImageDescriptor(
				ImageDescriptor.createFromURL(
						Activator.getDefault().getBundle().getEntry("/icons/layoutV.gif")));
		setToolTipText("Auto layout the process (vertical)");
	}

    public void run() {
        Map<String, Node> mapping = new HashMap<String, Node>();
        DirectedGraph graph = createDirectedGraph(mapping);
        DirectedGraphLayout layout = new DirectedGraphLayout();
        layout.visit(graph);
        for (Map.Entry<String, Node> entry: mapping.entrySet()) {
            Node node = entry.getValue();
            NodeWrapper nodeWrapper = 
            	((AbstractFlowWrapper)diagramViewer.getContents().getModel()).getElement(entry.getKey());
            nodeWrapper.setConstraint(new Rectangle(node.x, node.y, node.width, node.height));
        }
    }
    
    @SuppressWarnings("unchecked")
    protected DirectedGraph createDirectedGraph(Map<String, Node> mapping) {
        DirectedGraph graph = new DirectedGraph();
        AbstractFlowWrapper processWrapper = (AbstractFlowWrapper)diagramViewer.getContents().getModel();
        for (NodeWrapper elementWrapper: processWrapper.getElements()) {
            Node node = new Node();
            Integer width = (Integer) elementWrapper.getConstraint().width;
            Integer height = (Integer) elementWrapper.getConstraint().height;
            if (width == null || width <= 0) {
                width = 80;
            }
            if (height == null || height <= 0) {
                height = 40;
            }
            node.setSize(new Dimension(width, height));
            graph.nodes.add(node);
            mapping.put(elementWrapper.getId(), node);
        }
        for (NodeWrapper elementWrapper: processWrapper.getElements()) {
            for (AbstractConnectionWrapper connection: elementWrapper.getIncomingConnections()) {
                Node source = mapping.get(connection.getSource().getId());
                Node target = mapping.get(connection.getTarget().getId());
                graph.edges.add(new Edge(source, target));
            }
        }
        return graph;
    }

}
