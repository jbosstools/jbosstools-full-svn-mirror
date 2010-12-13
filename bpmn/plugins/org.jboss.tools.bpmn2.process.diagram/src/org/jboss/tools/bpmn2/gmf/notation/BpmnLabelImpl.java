package org.jboss.tools.bpmn2.gmf.notation;

import org.eclipse.bpmn2.di.BPMNLabel;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.gmf.runtime.notation.DecorationNode;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.impl.DecorationNodeImpl;

public class BpmnLabelImpl extends DecorationNodeImpl implements DecorationNode {
	
	BPMNLabel bpmnLabel;
	
	public BpmnLabelImpl(BPMNLabel label) {
		this.bpmnLabel = label;
		initializeLocation();
	}
	
	public BPMNLabel getBPMNLabel() {
		return bpmnLabel;
	}

	private void initializeLocation() {
		Bounds bounds = bpmnLabel.getBounds();
		if (bounds != null) {
			super.setLayoutConstraint(BpmnNotationFactory.INSTANCE
					.createBounds(bounds));
		}
	}

	@Override
	public void setLayoutConstraint(LayoutConstraint layoutConstraint) {
		super.setLayoutConstraint(layoutConstraint);
		if (layoutConstraint instanceof BpmnBoundsImpl) {
			bpmnLabel.setBounds(((BpmnBoundsImpl) layoutConstraint)
					.getBPMNBounds());
		}
	}

}
