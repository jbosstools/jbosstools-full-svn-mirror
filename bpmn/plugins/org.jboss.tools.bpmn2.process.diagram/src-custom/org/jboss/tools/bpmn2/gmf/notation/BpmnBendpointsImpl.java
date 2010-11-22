package org.jboss.tools.bpmn2.gmf.notation;

import java.util.List;

import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.dd.dc.DcFactory;
import org.eclipse.dd.dc.Point;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.RelativeBendpoints;
import org.eclipse.gmf.runtime.notation.datatype.RelativeBendpoint;
import org.eclipse.gmf.runtime.notation.impl.RelativeBendpointsImpl;

public class BpmnBendpointsImpl extends RelativeBendpointsImpl implements
		RelativeBendpoints {
	
	private Edge edge;
	
	public void setEdge(Edge edge) {
		this.edge = edge;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void setPoints(List newPoints) {
		if (edge != null && edge instanceof BpmnEdgeImpl) {
			List<Point> waypoints = (((BpmnEdgeImpl)edge).getBPMNEdge()).getWaypoint();
			waypoints.clear();
			if (newPoints != null) {
				for (Object object : newPoints) {
					if (object instanceof RelativeBendpoint) {
						RelativeBendpoint relativeBendPoint = (RelativeBendpoint)object;
						Point absoluteBendpoint = DcFactory.eINSTANCE.createPoint();
						BPMNEdge bpmnEdge = ((BpmnEdgeImpl)edge).getBPMNEdge();
						DiagramElement source = bpmnEdge.getSourceElement();
						if (source != null && source instanceof BPMNShape) {
							Bounds sourceBounds = ((BPMNShape)source).getBounds();
							absoluteBendpoint.setX(sourceBounds.getX() + relativeBendPoint.getSourceX());
							absoluteBendpoint.setY(sourceBounds.getY() + relativeBendPoint.getSourceY());
						}
						waypoints.add(absoluteBendpoint);
					}
				}
			}
		}
		super.setPoints(newPoints);
	}
	
}
