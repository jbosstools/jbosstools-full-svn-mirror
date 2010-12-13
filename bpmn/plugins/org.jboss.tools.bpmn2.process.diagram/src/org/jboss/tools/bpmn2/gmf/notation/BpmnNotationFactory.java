package org.jboss.tools.bpmn2.gmf.notation;

import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNLabel;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.di.BpmnDiFactory;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.dd.dc.DcFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class BpmnNotationFactory {
	
	public static BpmnNotationFactory INSTANCE = new BpmnNotationFactory();
	
	public BpmnEdgeImpl createEdge() {
		BPMNEdge bpmnEdge = BpmnDiFactory.eINSTANCE.createBPMNEdge();
		bpmnEdge.setId(EcoreUtil.generateUUID());
		return createEdge(bpmnEdge);
	}
	
	public BpmnEdgeImpl createEdge(BPMNEdge bpmnEdge) {
		return new BpmnEdgeImpl(bpmnEdge);
	}
	
	public BpmnShapeImpl createShape() {
		BPMNShape bpmnShape = BpmnDiFactory.eINSTANCE.createBPMNShape();
		bpmnShape.setId(EcoreUtil.generateUUID());
		return createShape(bpmnShape);
	}
	
	public BpmnShapeImpl createShape(BPMNShape bpmnShape) {
		return new BpmnShapeImpl(bpmnShape);
	}
	
	public BpmnLabelImpl createLabel() {
		BPMNLabel bpmnLabel = BpmnDiFactory.eINSTANCE.createBPMNLabel();
		bpmnLabel.setId(EcoreUtil.generateUUID());
		return createLabel(bpmnLabel);
	}
	
	public BpmnLabelImpl createLabel(BPMNLabel label) {
		return new BpmnLabelImpl(label);
	}
	
	public BpmnBoundsImpl createBounds() {
		return createBounds(DcFactory.eINSTANCE.createBounds());
	}
	
	public BpmnBoundsImpl createBounds(Bounds bounds) {
		return new BpmnBoundsImpl(bounds);
	}
	
	public BpmnDiagramImpl createDiagram(BPMNDiagram bpmnDiagram) {
		return new BpmnDiagramImpl(bpmnDiagram);
	}
	
	public BpmnBendpointsImpl createRelativeBendpoints() {
		return new BpmnBendpointsImpl();
	}
	
}
