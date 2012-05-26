package org.jboss.tools.bpmn2.gmf.notation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.bpmn2.Artifact;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.MeasurementUnit;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.impl.DiagramImpl;

public class BpmnDiagramImpl extends DiagramImpl implements Diagram {

	BPMNDiagram bpmnDiagram;

	public BpmnDiagramImpl(BPMNDiagram diagram) {
		this.bpmnDiagram = diagram;
		initialize();
	}

	private void initialize() {
		setType("bpmn2");
		setElement(bpmnDiagram.getPlane().getBpmnElement());
		setMeasurementUnit(MeasurementUnit.get("Pixel"));
		initializeViews((FlowElementsContainer)this.getElement(), createViewMap());		
	}
	
	private void initializeNodes(FlowElementsContainer parent, Map<EObject, View> viewMap) {
		for (FlowElement flowElement : parent.getFlowElements()) {
			View view = viewMap.get(flowElement);
			if (view == null) continue;
			if (view instanceof BpmnShapeImpl) {
				initializeShape(parent, flowElement, viewMap);
			}
		}
	}
	
	private void initializeArtifacts(FlowElementsContainer parent, Map<EObject, View> viewMap) {
		List<Artifact> artifacts = null;
		if (parent instanceof Process) {
			artifacts = ((Process)parent).getArtifacts();
		} else if (parent instanceof SubProcess) {
			artifacts = ((SubProcess)parent).getArtifacts();
		}
		if (artifacts == null) return;
		for (Artifact artifact : artifacts) {
			initializeShape(parent, artifact, viewMap);
		}
 	}
	
	private void initializeEdges(FlowElementsContainer parent, Map<EObject, View> viewMap) {
		for (FlowElement flowElement : parent.getFlowElements()) {
			View view = viewMap.get(flowElement);
			if (view == null) continue;
			if (view instanceof BpmnEdgeImpl) {
				initializeEdge(parent, flowElement, viewMap);
			}
		}
	}
	
	private void initializeViews(
			FlowElementsContainer parent, 
			Map<EObject, View> viewMap) {
		initializeNodes(parent, viewMap);
		initializeArtifacts(parent, viewMap);
		initializeEdges(parent, viewMap);
	}
	
	private void initializeEdge(FlowElementsContainer parent, EObject object, Map<EObject, View> viewMap) {
		View view = viewMap.get(object);
		if (view != null && view instanceof BpmnEdgeImpl) {
			BpmnEdgeImpl edge = (BpmnEdgeImpl)view;
			View parentView = viewMap.get(parent);
			if (parentView != null && parentView instanceof Diagram) {
				edge.initialize((Diagram)parentView, viewMap);
			}
//			EObject bpmnElement = edge.getElement();
//			if (bpmnElement instanceof SequenceFlow) {
//				SequenceFlow sequenceFlow = (SequenceFlow)bpmnElement;
//				FlowNode source = sequenceFlow.getSourceRef();
//				FlowNode target = sequenceFlow.getTargetRef();
//				View sourceView = viewMap.get(source);
//				View targetView = viewMap.get(target);
//				edge.setSource(sourceView);
//				edge.setTarget(targetView);
//			}
		}
	}
	
	private void initializeShape(
			FlowElementsContainer parent,
			EObject object,
			Map<EObject, View> viewMap) {
		View view = viewMap.get(object);
		if (view != null && view instanceof BpmnShapeImpl) {
			BpmnShapeImpl shape = (BpmnShapeImpl)view;
			View parentView = viewMap.get(parent);
			if (parentView != null) {
				shape.initialize(parentView);
				if (shape.getElement() instanceof FlowElementsContainer) {
					initializeViews((FlowElementsContainer)shape.getElement(), viewMap);
				}
			}
		}
	}
	
	private Map<EObject, View> createViewMap() {
		List<DiagramElement> diagramElements = bpmnDiagram.getPlane().getPlaneElement();
		Map<EObject, View> result = new HashMap<EObject, View>();
		result.put(bpmnDiagram.getPlane().getBpmnElement(), this);
		for (DiagramElement diagramElement : diagramElements) {
			if (diagramElement instanceof BPMNShape) {
				BPMNShape bpmnShape = (BPMNShape)diagramElement;
				BpmnShapeImpl shape = BpmnNotationFactory.INSTANCE.createShape(bpmnShape);
				result.put(bpmnShape.getBpmnElement(), shape);
			} else if (diagramElement instanceof BPMNEdge) {
				BPMNEdge bpmnEdge = (BPMNEdge)diagramElement;
				BpmnEdgeImpl edge = BpmnNotationFactory.INSTANCE.createEdge(bpmnEdge);
				result.put(bpmnEdge.getBpmnElement(), edge);
			}
		}
		return result;
	}

	public void eNotify(Notification notifier) {
		if (notifier.getEventType() == Notification.REMOVE) {
			Object feature = notifier.getFeature();
			if (feature instanceof EReference) {
				String name = ((EReference) feature).getName();
				Object oldValue = notifier.getOldValue();
				if ("children".equals(name) && oldValue instanceof BpmnShapeImpl) { 
					removeDiagramElement(((BpmnShapeImpl)oldValue).getBPMNShape());
				} else if ("edges".equals(name) && oldValue instanceof BpmnEdgeImpl) {
					removeDiagramElement(((BpmnEdgeImpl)oldValue).getBPMNEdge());
				}
			}
		}
		super.eNotify(notifier);
	}

	public void insertChild(View child) {
		super.insertChild(child);
		if (child instanceof BpmnShapeImpl) {
			insertShape((BpmnShapeImpl) child);
		}
	}
	
	@Override
	public void insertEdge(Edge edge) {
		super.insertEdge(edge);
		if (edge instanceof BpmnEdgeImpl) {
			insertEdge((BpmnEdgeImpl)edge);
		}
	}
	
	protected void insertEdge(BpmnEdgeImpl edge) {
		bpmnDiagram.getPlane().getPlaneElement().add(edge.getBPMNEdge());
	}

	protected void insertShape(BpmnShapeImpl shape) {
		bpmnDiagram.getPlane().getPlaneElement().add(shape.getBPMNShape());
	}
	
	protected void removeDiagramElement(DiagramElement elementToRemove) {
		bpmnDiagram.getPlane().getPlaneElement().remove(elementToRemove);
	}

}
