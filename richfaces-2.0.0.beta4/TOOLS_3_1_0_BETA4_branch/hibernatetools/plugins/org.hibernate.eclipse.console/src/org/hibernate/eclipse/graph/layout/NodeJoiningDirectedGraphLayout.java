package org.hibernate.eclipse.graph.layout;

import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;

/**
 * Extended version of DirectedGraphLayout which allows DirectedGraphLayout
 * functionality to be used even when graph nodes either have no edges, or when part
 * of clusters isolated from other clusters of Nodes
 * 
 * @author Phil Zoio
 */
public class NodeJoiningDirectedGraphLayout extends DirectedGraphLayout
{

	/**
	 * @param graph public method called to handle layout task
	 */
	public void visit(DirectedGraph graph)
	{
		
		//System.out.println("Before Populate: Graph nodes: " + graph.nodes);
		//System.out.println("Before Populate: Graph edges: " + graph.edges);				
		
		//add dummy edges so that graph does not fall over because some nodes
		// are not in relationships
		new DummyEdgeCreator().visit(graph);
		
		// create edges to join any isolated clusters
		new ClusterEdgeCreator().visit(graph);	
		
		//System.out.println("After Populate: Graph nodes: " + graph.nodes);
		//System.out.println("After Populate: Graph edges: " + graph.edges);	
		
		
		super.visit(graph);
	}
}