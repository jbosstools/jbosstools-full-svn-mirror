package org.jboss.tools.bpmn2.gmf.notation;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.impl.BoundsImpl;

public class BpmnBoundsImpl extends BoundsImpl implements Bounds {
	
	org.eclipse.dd.dc.Bounds bpmnBounds = null;
	
	public BpmnBoundsImpl(org.eclipse.dd.dc.Bounds bounds) {
		this.bpmnBounds = bounds;
	}
	
	public void initialize(BpmnShapeImpl shape) {
		assert shape != null;
		super.setHeight((int)bpmnBounds.getHeight());
		super.setWidth((int)bpmnBounds.getWidth());
		LayoutConstraint layoutConstraint = shape.getLayoutConstraint();
		if (layoutConstraint instanceof BpmnBoundsImpl) {
			org.eclipse.dd.dc.Bounds bounds = getParentContainerBounds();
			super.setX((int)(bpmnBounds.getX() - (bounds != null ? bounds.getX() : 0)));
			super.setY((int)(bpmnBounds.getY() - (bounds != null ? bounds.getY() : 0)));
		}
	}
	
	public org.eclipse.dd.dc.Bounds getBPMNBounds() {
		return bpmnBounds;
	}
	
	public void setX(int newX) {
		super.setX(newX);
		bpmnBounds.setX(getAbsoluteX(newX));
		adjustChildren();
	}
	
	public void setY(int newY) {
		super.setY(newY);
		bpmnBounds.setY(getAbsoluteY(newY));
		adjustChildren();
	}
	
	public void setHeight(int newHeight) {
		super.setHeight(newHeight);
		bpmnBounds.setHeight(newHeight);
	}
	
	public void setWidth(int newWidth) {
		super.setWidth(newWidth);
		bpmnBounds.setWidth(newWidth);
	}
	
	private int getAbsoluteX(int x) {
		int result = x;
		org.eclipse.dd.dc.Bounds parentContainerBounds = getParentContainerBounds();
		if (parentContainerBounds != null) {
			result += parentContainerBounds.getX();
		}
		return result;
	}
	
	private int getAbsoluteY(int y) {
		int result = y;
		org.eclipse.dd.dc.Bounds parentContainerBounds = getParentContainerBounds();
		if (parentContainerBounds != null) {
			result += parentContainerBounds.getY();
		}
		return result;
	}
	
	private org.eclipse.dd.dc.Bounds getParentContainerBounds() {
		org.eclipse.dd.dc.Bounds result = null;
		BpmnShapeImpl parent = getParentContainer();
		if (parent != null) {
			LayoutConstraint layoutConstraint = parent.getLayoutConstraint();
			if (layoutConstraint instanceof BpmnBoundsImpl) {
				BpmnBoundsImpl bpmnBounds = (BpmnBoundsImpl)layoutConstraint;
				result = bpmnBounds.getBPMNBounds();
			}
		}
		return result;
	}
	
	private BpmnShapeImpl getParentContainer() {
		BpmnShapeImpl parent = getParent();
		if (parent != null) {
			Object parentContainer = parent.eContainer();
			if (parentContainer != null && parentContainer instanceof BpmnShapeImpl) {
				return (BpmnShapeImpl)parentContainer;
			}
		}
		return null;
	}
	
	private BpmnShapeImpl getParent() {
		EObject parent = this.eContainer();
		if (parent != null && parent instanceof BpmnShapeImpl) {
			return (BpmnShapeImpl)parent;
		}
		return null;
	}
	
	private void adjustChildren() {
		BpmnShapeImpl parent = getParent();
		if (parent != null) {
			for (Object child : parent.getChildren()) {
				if (child instanceof BpmnShapeImpl) {
					BpmnShapeImpl bpmnShape = (BpmnShapeImpl)child;
					LayoutConstraint layoutConstraint = bpmnShape.getLayoutConstraint();
					if (layoutConstraint != null && layoutConstraint instanceof BpmnBoundsImpl) {
						BpmnBoundsImpl bpmnBounds = (BpmnBoundsImpl)layoutConstraint;
						// these calls reset the absolute coordinates of the adapted bounds object
						bpmnBounds.setX(bpmnBounds.getX());
						bpmnBounds.setY(bpmnBounds.getY());
					}
				}
			}
		}
	}
	
}
