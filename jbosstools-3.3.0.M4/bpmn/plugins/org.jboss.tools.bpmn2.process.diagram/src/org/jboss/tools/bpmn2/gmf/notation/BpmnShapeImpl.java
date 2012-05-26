package org.jboss.tools.bpmn2.gmf.notation;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.di.BPMNLabel;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gmf.runtime.notation.DecorationNode;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.impl.ShapeImpl;
import org.jboss.tools.bpmn2.process.diagram.part.Bpmn2VisualIDRegistry;

public class BpmnShapeImpl extends ShapeImpl implements Shape {

	BPMNShape bpmnShape;

	public BpmnShapeImpl(BPMNShape shape) {
		this.bpmnShape = shape;
	}
	
	public void initialize(View parentView) {
		initializeElement();
		setType(Bpmn2VisualIDRegistry.getType(
						Bpmn2VisualIDRegistry.getNodeVisualID(
								parentView, 
								getElement())));
		parentView.insertChild(this);
		initializeBounds();
		initializeLabel(parentView);
	}

	private void initializeElement() {
		EObject element = bpmnShape.getBpmnElement();
		if (element != null) {
			super.setElement(element);
		}
	}

	private void initializeLabel(View parentView) {
		BPMNLabel bpmnLabel = bpmnShape.getLabel();
		if (bpmnLabel != null) {
			DecorationNode label = BpmnNotationFactory.INSTANCE.createLabel(bpmnLabel);
			if (getElement() != null) {
				int id = Bpmn2VisualIDRegistry.getLabelVisualId(parentView.getElement(), getElement());
				if (id != -1) {
					label.setType(Bpmn2VisualIDRegistry.getType(id));
				}
			}
			super.insertChild(label);
		}
	}

	private void initializeBounds() {
		Bounds bounds = bpmnShape.getBounds();
		if (bounds != null) {
			BpmnBoundsImpl bpmnBounds = (BpmnBoundsImpl)BpmnNotationFactory.INSTANCE.createBounds(bounds);
			super.setLayoutConstraint(bpmnBounds);
			bpmnBounds.initialize(this);
		}
	}

	public BPMNShape getBPMNShape() {
		return bpmnShape;
	}

	@Override
	public void setElement(EObject newElement) {
		super.setElement(newElement);
		if (newElement instanceof BaseElement) {
			bpmnShape.setBpmnElement((BaseElement) newElement);
		}
	}

	@Override
	public void setLayoutConstraint(LayoutConstraint layoutConstraint) {
		super.setLayoutConstraint(layoutConstraint);
		if (layoutConstraint instanceof BpmnBoundsImpl) {
			bpmnShape.setBounds(((BpmnBoundsImpl) layoutConstraint)
					.getBPMNBounds());
		}
	}

	@Override
	public void insertChild(View child) {
		super.insertChild(child);
		if (child instanceof BpmnLabelImpl) {
			insertLabel((BpmnLabelImpl) child);
		} else if (child instanceof BpmnShapeImpl) {
			insertShape((BpmnShapeImpl)child);
		}
	}

	protected void insertLabel(BpmnLabelImpl label) {
		bpmnShape.setLabel(label.getBPMNLabel());
	}
	
	protected void insertShape(BpmnShapeImpl shape) {
		EObject container = bpmnShape.eContainer();
		if (container instanceof BPMNPlane) {
			BPMNPlane bpmnPlane = (BPMNPlane)container;
			bpmnPlane.getPlaneElement().add(shape.getBPMNShape());
		}
	}
	
	public void eNotify(Notification notifier) {
		if (notifier.getEventType() == Notification.REMOVE) {
			Object feature = notifier.getFeature();
			if (feature instanceof EReference) {
				String name = ((EReference) feature).getName();
				Object oldValue = notifier.getOldValue();
				if ("children".equals(name) && oldValue instanceof BpmnShapeImpl) {
					getDiagram().eNotify(notifier);
				}
			}
		}
		super.eNotify(notifier);
	}

}
