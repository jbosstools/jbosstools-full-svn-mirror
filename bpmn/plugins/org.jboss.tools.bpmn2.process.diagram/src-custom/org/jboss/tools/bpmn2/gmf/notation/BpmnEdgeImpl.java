package org.jboss.tools.bpmn2.gmf.notation;

import java.util.ArrayList;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.gmf.runtime.notation.Bendpoints;
import org.eclipse.gmf.runtime.notation.Connector;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.RelativeBendpoints;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.datatype.RelativeBendpoint;
import org.eclipse.gmf.runtime.notation.impl.ConnectorImpl;
import org.jboss.tools.bpmn2.process.diagram.part.Bpmn2VisualIDRegistry;

public class BpmnEdgeImpl extends ConnectorImpl implements Connector {
	
	BPMNEdge bpmnEdge;
	
	public BpmnEdgeImpl(BPMNEdge edge) {
		this.bpmnEdge = edge;
	}
	
	public BPMNEdge getBPMNEdge() {
		return bpmnEdge;
	}

	@Override
	public void setElement(EObject newElement) {
		super.setElement(newElement);
		if (newElement instanceof BaseElement) {
			bpmnEdge.setBpmnElement((BaseElement) newElement);
		}
	}
	
	@Override
	public void setBendpoints(Bendpoints bendpoints) {
		if (bendpoints != null && bendpoints instanceof BpmnBendpointsImpl) {
			BpmnBendpointsImpl bpmnBendpoints = (BpmnBendpointsImpl)bendpoints;
			bpmnBendpoints.setEdge(this);
			// force refresh of the points list
			bpmnBendpoints.setPoints(bpmnBendpoints.getPoints());
		} 
		super.setBendpoints(bendpoints);
	}

	@Override
	public void setSource(View newSource) {
		super.setSource(newSource);
		// force refresh of the points list
		((BpmnBendpointsImpl)getBendpoints()).setPoints(((BpmnBendpointsImpl)getBendpoints()).getPoints());
	}
	
	public void initialize(Diagram parentView) {
		initializeElement();
		initializeBendpoints();
		setType(Bpmn2VisualIDRegistry.getType(
						Bpmn2VisualIDRegistry.getLinkWithClassVisualID(getElement())));
		parentView.insertEdge(this);
	}
	
	private void initializeBendpoints() {
		RelativeBendpoints bendpoints = NotationFactory.eINSTANCE.createRelativeBendpoints();
		ArrayList<RelativeBendpoint> points = new ArrayList<RelativeBendpoint>(2);
		points.add(new RelativeBendpoint());
		points.add(new RelativeBendpoint());
		bendpoints.setPoints(points);
		setBendpoints(bendpoints);
		
	}

	private void initializeElement() {
		EObject element = bpmnEdge.getBpmnElement();
		if (element != null) {
			super.setElement(element);
		}
	}

}
