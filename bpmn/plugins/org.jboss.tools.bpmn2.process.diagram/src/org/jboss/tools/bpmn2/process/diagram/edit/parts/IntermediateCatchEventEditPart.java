package org.jboss.tools.bpmn2.process.diagram.edit.parts;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PolylineShape;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.lite.svg.SVGFigure;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Color;
import org.jboss.tools.bpmn2.process.diagram.edit.policies.IntermediateCatchEventItemSemanticEditPolicy;
import org.jboss.tools.bpmn2.process.diagram.providers.Bpmn2ElementTypes;

/**
 * @generated
 */
public class IntermediateCatchEventEditPart extends ShapeNodeEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 2010;

	/**
	 * @generated
	 */
	protected IFigure contentPane;

	/**
	 * @generated
	 */
	protected IFigure primaryShape;

	/**
	 * @generated
	 */
	public IntermediateCatchEventEditPart(View view) {
		super(view);
	}

	/**
	 * @generated
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new IntermediateCatchEventItemSemanticEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
		// XXX need an SCR to runtime to have another abstract superclass that would let children add reasonable editpolicies
		// removeEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.CONNECTION_HANDLES_ROLE);
	}

	/**
	 * @generated
	 */
	protected LayoutEditPolicy createLayoutEditPolicy() {
		org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy lep = new org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy() {

			protected EditPolicy createChildEditPolicy(EditPart child) {
				EditPolicy result = child
						.getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
				if (result == null) {
					result = new NonResizableEditPolicy();
				}
				return result;
			}

			protected Command getMoveChildrenCommand(Request request) {
				return null;
			}

			protected Command getCreateCommand(CreateRequest request) {
				return null;
			}
		};
		return lep;
	}

	/**
	 * @generated
	 */
	protected IFigure createNodeShape() {
		return primaryShape = new MessageCatchIntermediateEventFigureDescriptor();
	}

	/**
	 * @generated
	 */
	public MessageCatchIntermediateEventFigureDescriptor getPrimaryShape() {
		return (MessageCatchIntermediateEventFigureDescriptor) primaryShape;
	}

	/**
	 * @generated
	 */
	protected NodeFigure createNodePlate() {
		DefaultSizeNodeFigure result = new DefaultSizeNodeFigure(48, 48);
		return result;
	}

	/**
	 * @generated
	 */
	public EditPolicy getPrimaryDragEditPolicy() {
		EditPolicy result = super.getPrimaryDragEditPolicy();
		if (result instanceof ResizableEditPolicy) {
			ResizableEditPolicy ep = (ResizableEditPolicy) result;
			ep.setResizeDirections(PositionConstants.NONE);
		}
		return result;
	}

	/**
	 * Creates figure for this edit part.
	 * 
	 * Body of this method does not depend on settings in generation model
	 * so you may safely remove <i>generated</i> tag and modify it.
	 * 
	 * @generated
	 */
	protected NodeFigure createNodeFigure() {
		NodeFigure figure = createNodePlate();
		figure.setLayoutManager(new StackLayout());
		IFigure shape = createNodeShape();
		figure.add(shape);
		contentPane = setupContentPane(shape);
		return figure;
	}

	/**
	 * Default implementation treats passed figure as content pane.
	 * Respects layout one may have set for generated figure.
	 * @param nodeShape instance of generated figure class
	 * @generated
	 */
	protected IFigure setupContentPane(IFigure nodeShape) {
		return nodeShape; // use nodeShape itself as contentPane
	}

	/**
	 * @generated
	 */
	public IFigure getContentPane() {
		if (contentPane != null) {
			return contentPane;
		}
		return super.getContentPane();
	}

	/**
	 * @generated
	 */
	protected void setForegroundColor(Color color) {
		if (primaryShape != null) {
			primaryShape.setForegroundColor(color);
		}
	}

	/**
	 * @generated
	 */
	protected void setBackgroundColor(Color color) {
		if (primaryShape != null) {
			primaryShape.setBackgroundColor(color);
		}
	}

	/**
	 * @generated
	 */
	protected void setLineWidth(int width) {
		if (primaryShape instanceof Shape) {
			((Shape) primaryShape).setLineWidth(width);
		}
	}

	/**
	 * @generated
	 */
	protected void setLineType(int style) {
		if (primaryShape instanceof Shape) {
			((Shape) primaryShape).setLineStyle(style);
		}
	}

	/**
	 * @generated
	 */
	public List<IElementType> getMARelTypesOnSource() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(2);
		types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		types.add(Bpmn2ElementTypes.Association_4002);
		return types;
	}

	/**
	 * @generated
	 */
	public List<IElementType> getMARelTypesOnSourceAndTarget(
			IGraphicalEditPart targetEditPart) {
		LinkedList<IElementType> types = new LinkedList<IElementType>();
		if (targetEditPart instanceof UserTaskEditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof ScriptTaskEditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof ServiceTaskEditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof BusinessRuleTaskEditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof StartEventEditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof StartEvent2EditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof org.jboss.tools.bpmn2.process.diagram.edit.parts.IntermediateCatchEventEditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof IntermediateCatchEvent2EditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof IntermediateCatchEvent3EditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof IntermediateThrowEventEditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof EndEventEditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof EndEvent2EditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof EndEvent3EditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof ExclusiveGatewayEditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof ParallelGatewayEditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof SubProcessEditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof UserTask2EditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof ScriptTask2EditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof ServiceTask2EditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof BusinessRuleTask2EditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof StartEvent3EditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof StartEvent4EditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof IntermediateCatchEvent4EditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof IntermediateCatchEvent5EditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof IntermediateCatchEvent6EditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof IntermediateThrowEvent2EditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof EndEvent4EditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof EndEvent5EditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof EndEvent6EditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof ExclusiveGateway2EditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof ParallelGateway2EditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof SubProcess2EditPart) {
			types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		}
		if (targetEditPart instanceof UserTaskEditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof ScriptTaskEditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof ServiceTaskEditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof BusinessRuleTaskEditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof StartEventEditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof StartEvent2EditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof org.jboss.tools.bpmn2.process.diagram.edit.parts.IntermediateCatchEventEditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof IntermediateCatchEvent2EditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof IntermediateCatchEvent3EditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof IntermediateThrowEventEditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof EndEventEditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof EndEvent2EditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof EndEvent3EditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof ExclusiveGatewayEditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof ParallelGatewayEditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof SubProcessEditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof DataObjectEditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof TextAnnotationEditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof UserTask2EditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof ScriptTask2EditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof ServiceTask2EditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof BusinessRuleTask2EditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof StartEvent3EditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof StartEvent4EditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof IntermediateCatchEvent4EditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof IntermediateCatchEvent5EditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof IntermediateCatchEvent6EditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof IntermediateThrowEvent2EditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof EndEvent4EditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof EndEvent5EditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof EndEvent6EditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof ExclusiveGateway2EditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof ParallelGateway2EditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof SubProcess2EditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof DataObject2EditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		if (targetEditPart instanceof TextAnnotation2EditPart) {
			types.add(Bpmn2ElementTypes.Association_4002);
		}
		return types;
	}

	/**
	 * @generated
	 */
	public List<IElementType> getMATypesForTarget(IElementType relationshipType) {
		LinkedList<IElementType> types = new LinkedList<IElementType>();
		if (relationshipType == Bpmn2ElementTypes.SequenceFlow_4001) {
			types.add(Bpmn2ElementTypes.UserTask_2001);
			types.add(Bpmn2ElementTypes.ScriptTask_2017);
			types.add(Bpmn2ElementTypes.ServiceTask_2002);
			types.add(Bpmn2ElementTypes.BusinessRuleTask_2018);
			types.add(Bpmn2ElementTypes.StartEvent_2003);
			types.add(Bpmn2ElementTypes.StartEvent_2007);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_2010);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_2012);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_2013);
			types.add(Bpmn2ElementTypes.IntermediateThrowEvent_2011);
			types.add(Bpmn2ElementTypes.EndEvent_2004);
			types.add(Bpmn2ElementTypes.EndEvent_2008);
			types.add(Bpmn2ElementTypes.EndEvent_2009);
			types.add(Bpmn2ElementTypes.ExclusiveGateway_2005);
			types.add(Bpmn2ElementTypes.ParallelGateway_2006);
			types.add(Bpmn2ElementTypes.SubProcess_2016);
			types.add(Bpmn2ElementTypes.UserTask_3002);
			types.add(Bpmn2ElementTypes.ScriptTask_3016);
			types.add(Bpmn2ElementTypes.ServiceTask_3004);
			types.add(Bpmn2ElementTypes.BusinessRuleTask_3017);
			types.add(Bpmn2ElementTypes.StartEvent_3003);
			types.add(Bpmn2ElementTypes.StartEvent_3005);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_3011);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_3013);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_3018);
			types.add(Bpmn2ElementTypes.IntermediateThrowEvent_3012);
			types.add(Bpmn2ElementTypes.EndEvent_3006);
			types.add(Bpmn2ElementTypes.EndEvent_3009);
			types.add(Bpmn2ElementTypes.EndEvent_3010);
			types.add(Bpmn2ElementTypes.ExclusiveGateway_3007);
			types.add(Bpmn2ElementTypes.ParallelGateway_3008);
			types.add(Bpmn2ElementTypes.SubProcess_3001);
		} else if (relationshipType == Bpmn2ElementTypes.Association_4002) {
			types.add(Bpmn2ElementTypes.UserTask_2001);
			types.add(Bpmn2ElementTypes.ScriptTask_2017);
			types.add(Bpmn2ElementTypes.ServiceTask_2002);
			types.add(Bpmn2ElementTypes.BusinessRuleTask_2018);
			types.add(Bpmn2ElementTypes.StartEvent_2003);
			types.add(Bpmn2ElementTypes.StartEvent_2007);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_2010);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_2012);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_2013);
			types.add(Bpmn2ElementTypes.IntermediateThrowEvent_2011);
			types.add(Bpmn2ElementTypes.EndEvent_2004);
			types.add(Bpmn2ElementTypes.EndEvent_2008);
			types.add(Bpmn2ElementTypes.EndEvent_2009);
			types.add(Bpmn2ElementTypes.ExclusiveGateway_2005);
			types.add(Bpmn2ElementTypes.ParallelGateway_2006);
			types.add(Bpmn2ElementTypes.SubProcess_2016);
			types.add(Bpmn2ElementTypes.DataObject_2014);
			types.add(Bpmn2ElementTypes.TextAnnotation_2015);
			types.add(Bpmn2ElementTypes.UserTask_3002);
			types.add(Bpmn2ElementTypes.ScriptTask_3016);
			types.add(Bpmn2ElementTypes.ServiceTask_3004);
			types.add(Bpmn2ElementTypes.BusinessRuleTask_3017);
			types.add(Bpmn2ElementTypes.StartEvent_3003);
			types.add(Bpmn2ElementTypes.StartEvent_3005);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_3011);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_3013);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_3018);
			types.add(Bpmn2ElementTypes.IntermediateThrowEvent_3012);
			types.add(Bpmn2ElementTypes.EndEvent_3006);
			types.add(Bpmn2ElementTypes.EndEvent_3009);
			types.add(Bpmn2ElementTypes.EndEvent_3010);
			types.add(Bpmn2ElementTypes.ExclusiveGateway_3007);
			types.add(Bpmn2ElementTypes.ParallelGateway_3008);
			types.add(Bpmn2ElementTypes.SubProcess_3001);
			types.add(Bpmn2ElementTypes.DataObject_3014);
			types.add(Bpmn2ElementTypes.TextAnnotation_3015);
		}
		return types;
	}

	/**
	 * @generated
	 */
	public List<IElementType> getMARelTypesOnTarget() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(2);
		types.add(Bpmn2ElementTypes.SequenceFlow_4001);
		types.add(Bpmn2ElementTypes.Association_4002);
		return types;
	}

	/**
	 * @generated
	 */
	public List<IElementType> getMATypesForSource(IElementType relationshipType) {
		LinkedList<IElementType> types = new LinkedList<IElementType>();
		if (relationshipType == Bpmn2ElementTypes.SequenceFlow_4001) {
			types.add(Bpmn2ElementTypes.UserTask_2001);
			types.add(Bpmn2ElementTypes.ScriptTask_2017);
			types.add(Bpmn2ElementTypes.ServiceTask_2002);
			types.add(Bpmn2ElementTypes.BusinessRuleTask_2018);
			types.add(Bpmn2ElementTypes.StartEvent_2003);
			types.add(Bpmn2ElementTypes.StartEvent_2007);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_2010);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_2012);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_2013);
			types.add(Bpmn2ElementTypes.IntermediateThrowEvent_2011);
			types.add(Bpmn2ElementTypes.EndEvent_2004);
			types.add(Bpmn2ElementTypes.EndEvent_2008);
			types.add(Bpmn2ElementTypes.EndEvent_2009);
			types.add(Bpmn2ElementTypes.ExclusiveGateway_2005);
			types.add(Bpmn2ElementTypes.ParallelGateway_2006);
			types.add(Bpmn2ElementTypes.SubProcess_2016);
			types.add(Bpmn2ElementTypes.UserTask_3002);
			types.add(Bpmn2ElementTypes.ScriptTask_3016);
			types.add(Bpmn2ElementTypes.ServiceTask_3004);
			types.add(Bpmn2ElementTypes.BusinessRuleTask_3017);
			types.add(Bpmn2ElementTypes.StartEvent_3003);
			types.add(Bpmn2ElementTypes.StartEvent_3005);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_3011);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_3013);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_3018);
			types.add(Bpmn2ElementTypes.IntermediateThrowEvent_3012);
			types.add(Bpmn2ElementTypes.EndEvent_3006);
			types.add(Bpmn2ElementTypes.EndEvent_3009);
			types.add(Bpmn2ElementTypes.EndEvent_3010);
			types.add(Bpmn2ElementTypes.ExclusiveGateway_3007);
			types.add(Bpmn2ElementTypes.ParallelGateway_3008);
			types.add(Bpmn2ElementTypes.SubProcess_3001);
		} else if (relationshipType == Bpmn2ElementTypes.Association_4002) {
			types.add(Bpmn2ElementTypes.UserTask_2001);
			types.add(Bpmn2ElementTypes.ScriptTask_2017);
			types.add(Bpmn2ElementTypes.ServiceTask_2002);
			types.add(Bpmn2ElementTypes.BusinessRuleTask_2018);
			types.add(Bpmn2ElementTypes.StartEvent_2003);
			types.add(Bpmn2ElementTypes.StartEvent_2007);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_2010);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_2012);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_2013);
			types.add(Bpmn2ElementTypes.IntermediateThrowEvent_2011);
			types.add(Bpmn2ElementTypes.EndEvent_2004);
			types.add(Bpmn2ElementTypes.EndEvent_2008);
			types.add(Bpmn2ElementTypes.EndEvent_2009);
			types.add(Bpmn2ElementTypes.ExclusiveGateway_2005);
			types.add(Bpmn2ElementTypes.ParallelGateway_2006);
			types.add(Bpmn2ElementTypes.SubProcess_2016);
			types.add(Bpmn2ElementTypes.DataObject_2014);
			types.add(Bpmn2ElementTypes.TextAnnotation_2015);
			types.add(Bpmn2ElementTypes.UserTask_3002);
			types.add(Bpmn2ElementTypes.ScriptTask_3016);
			types.add(Bpmn2ElementTypes.ServiceTask_3004);
			types.add(Bpmn2ElementTypes.BusinessRuleTask_3017);
			types.add(Bpmn2ElementTypes.StartEvent_3003);
			types.add(Bpmn2ElementTypes.StartEvent_3005);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_3011);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_3013);
			types.add(Bpmn2ElementTypes.IntermediateCatchEvent_3018);
			types.add(Bpmn2ElementTypes.IntermediateThrowEvent_3012);
			types.add(Bpmn2ElementTypes.EndEvent_3006);
			types.add(Bpmn2ElementTypes.EndEvent_3009);
			types.add(Bpmn2ElementTypes.EndEvent_3010);
			types.add(Bpmn2ElementTypes.ExclusiveGateway_3007);
			types.add(Bpmn2ElementTypes.ParallelGateway_3008);
			types.add(Bpmn2ElementTypes.SubProcess_3001);
			types.add(Bpmn2ElementTypes.DataObject_3014);
			types.add(Bpmn2ElementTypes.TextAnnotation_3015);
		}
		return types;
	}

	/**
	 * @generated
	 */
	public class MessageCatchIntermediateEventFigureDescriptor extends
			SVGFigure {

		/**
		 * @generated
		 */
		public MessageCatchIntermediateEventFigureDescriptor() {
			this.setURI("platform:/plugin/org.jboss.tools.bpmn2.process.diagram/images/events_48px_intermediate_message_catch.svg");
		}

	}

}
