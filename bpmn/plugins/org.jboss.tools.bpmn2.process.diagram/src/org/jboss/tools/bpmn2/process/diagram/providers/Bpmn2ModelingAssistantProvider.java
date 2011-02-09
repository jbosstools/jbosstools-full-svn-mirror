package org.jboss.tools.bpmn2.process.diagram.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.ModelingAssistantProvider;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.BusinessRuleTask2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.BusinessRuleTaskEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.DataObject2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.DataObjectEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.EndEvent2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.EndEvent3EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.EndEvent4EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.EndEvent5EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.EndEvent6EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.EndEventEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ExclusiveGateway2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ExclusiveGatewayEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.IntermediateCatchEvent2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.IntermediateCatchEvent3EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.IntermediateCatchEvent4EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.IntermediateCatchEvent5EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.IntermediateCatchEvent6EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.IntermediateCatchEventEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.IntermediateThrowEvent2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.IntermediateThrowEventEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ParallelGateway2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ParallelGatewayEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ProcessEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ScriptTask2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ScriptTaskEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ServiceTask2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ServiceTaskEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.StartEvent2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.StartEvent3EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.StartEvent4EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.StartEventEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.SubProcess2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.SubProcessEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.TextAnnotation2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.TextAnnotationEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.UserTask2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.UserTaskEditPart;
import org.jboss.tools.bpmn2.process.diagram.part.Bpmn2DiagramEditorPlugin;
import org.jboss.tools.bpmn2.process.diagram.part.Bpmn2ProcessDiagramEditorPlugin;
import org.jboss.tools.bpmn2.process.diagram.part.Messages;

/**
 * @generated
 */
public class Bpmn2ModelingAssistantProvider extends ModelingAssistantProvider {

	/**
	 * @generated
	 */
	public List getTypesForPopupBar(IAdaptable host) {
		IGraphicalEditPart editPart = (IGraphicalEditPart) host
				.getAdapter(IGraphicalEditPart.class);
		if (editPart instanceof ProcessEditPart) {
			ArrayList<IElementType> types = new ArrayList<IElementType>(18);
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
			return types;
		}
		if (editPart instanceof SubProcessEditPart) {
			ArrayList<IElementType> types = new ArrayList<IElementType>(18);
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
			return types;
		}
		if (editPart instanceof SubProcess2EditPart) {
			ArrayList<IElementType> types = new ArrayList<IElementType>(18);
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
			return types;
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public List getRelTypesOnSource(IAdaptable source) {
		IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source
				.getAdapter(IGraphicalEditPart.class);
		if (sourceEditPart instanceof UserTaskEditPart) {
			return ((UserTaskEditPart) sourceEditPart).getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof ScriptTaskEditPart) {
			return ((ScriptTaskEditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof ServiceTaskEditPart) {
			return ((ServiceTaskEditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof BusinessRuleTaskEditPart) {
			return ((BusinessRuleTaskEditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof StartEventEditPart) {
			return ((StartEventEditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof StartEvent2EditPart) {
			return ((StartEvent2EditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof IntermediateCatchEventEditPart) {
			return ((IntermediateCatchEventEditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof IntermediateCatchEvent2EditPart) {
			return ((IntermediateCatchEvent2EditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof IntermediateCatchEvent3EditPart) {
			return ((IntermediateCatchEvent3EditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof IntermediateThrowEventEditPart) {
			return ((IntermediateThrowEventEditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof EndEventEditPart) {
			return ((EndEventEditPart) sourceEditPart).getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof EndEvent2EditPart) {
			return ((EndEvent2EditPart) sourceEditPart).getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof EndEvent3EditPart) {
			return ((EndEvent3EditPart) sourceEditPart).getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof ExclusiveGatewayEditPart) {
			return ((ExclusiveGatewayEditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof ParallelGatewayEditPart) {
			return ((ParallelGatewayEditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof SubProcessEditPart) {
			return ((SubProcessEditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof DataObjectEditPart) {
			return ((DataObjectEditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof TextAnnotationEditPart) {
			return ((TextAnnotationEditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof UserTask2EditPart) {
			return ((UserTask2EditPart) sourceEditPart).getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof ScriptTask2EditPart) {
			return ((ScriptTask2EditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof ServiceTask2EditPart) {
			return ((ServiceTask2EditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof BusinessRuleTask2EditPart) {
			return ((BusinessRuleTask2EditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof StartEvent3EditPart) {
			return ((StartEvent3EditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof StartEvent4EditPart) {
			return ((StartEvent4EditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof IntermediateCatchEvent4EditPart) {
			return ((IntermediateCatchEvent4EditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof IntermediateCatchEvent5EditPart) {
			return ((IntermediateCatchEvent5EditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof IntermediateCatchEvent6EditPart) {
			return ((IntermediateCatchEvent6EditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof IntermediateThrowEvent2EditPart) {
			return ((IntermediateThrowEvent2EditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof EndEvent4EditPart) {
			return ((EndEvent4EditPart) sourceEditPart).getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof EndEvent5EditPart) {
			return ((EndEvent5EditPart) sourceEditPart).getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof EndEvent6EditPart) {
			return ((EndEvent6EditPart) sourceEditPart).getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof ExclusiveGateway2EditPart) {
			return ((ExclusiveGateway2EditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof ParallelGateway2EditPart) {
			return ((ParallelGateway2EditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof SubProcess2EditPart) {
			return ((SubProcess2EditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof DataObject2EditPart) {
			return ((DataObject2EditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		if (sourceEditPart instanceof TextAnnotation2EditPart) {
			return ((TextAnnotation2EditPart) sourceEditPart)
					.getMARelTypesOnSource();
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public List getRelTypesOnTarget(IAdaptable target) {
		IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target
				.getAdapter(IGraphicalEditPart.class);
		if (targetEditPart instanceof UserTaskEditPart) {
			return ((UserTaskEditPart) targetEditPart).getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof ScriptTaskEditPart) {
			return ((ScriptTaskEditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof ServiceTaskEditPart) {
			return ((ServiceTaskEditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof BusinessRuleTaskEditPart) {
			return ((BusinessRuleTaskEditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof StartEventEditPart) {
			return ((StartEventEditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof StartEvent2EditPart) {
			return ((StartEvent2EditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof IntermediateCatchEventEditPart) {
			return ((IntermediateCatchEventEditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof IntermediateCatchEvent2EditPart) {
			return ((IntermediateCatchEvent2EditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof IntermediateCatchEvent3EditPart) {
			return ((IntermediateCatchEvent3EditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof IntermediateThrowEventEditPart) {
			return ((IntermediateThrowEventEditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof EndEventEditPart) {
			return ((EndEventEditPart) targetEditPart).getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof EndEvent2EditPart) {
			return ((EndEvent2EditPart) targetEditPart).getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof EndEvent3EditPart) {
			return ((EndEvent3EditPart) targetEditPart).getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof ExclusiveGatewayEditPart) {
			return ((ExclusiveGatewayEditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof ParallelGatewayEditPart) {
			return ((ParallelGatewayEditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof SubProcessEditPart) {
			return ((SubProcessEditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof DataObjectEditPart) {
			return ((DataObjectEditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof TextAnnotationEditPart) {
			return ((TextAnnotationEditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof UserTask2EditPart) {
			return ((UserTask2EditPart) targetEditPart).getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof ScriptTask2EditPart) {
			return ((ScriptTask2EditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof ServiceTask2EditPart) {
			return ((ServiceTask2EditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof BusinessRuleTask2EditPart) {
			return ((BusinessRuleTask2EditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof StartEvent3EditPart) {
			return ((StartEvent3EditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof StartEvent4EditPart) {
			return ((StartEvent4EditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof IntermediateCatchEvent4EditPart) {
			return ((IntermediateCatchEvent4EditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof IntermediateCatchEvent5EditPart) {
			return ((IntermediateCatchEvent5EditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof IntermediateCatchEvent6EditPart) {
			return ((IntermediateCatchEvent6EditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof IntermediateThrowEvent2EditPart) {
			return ((IntermediateThrowEvent2EditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof EndEvent4EditPart) {
			return ((EndEvent4EditPart) targetEditPart).getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof EndEvent5EditPart) {
			return ((EndEvent5EditPart) targetEditPart).getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof EndEvent6EditPart) {
			return ((EndEvent6EditPart) targetEditPart).getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof ExclusiveGateway2EditPart) {
			return ((ExclusiveGateway2EditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof ParallelGateway2EditPart) {
			return ((ParallelGateway2EditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof SubProcess2EditPart) {
			return ((SubProcess2EditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof DataObject2EditPart) {
			return ((DataObject2EditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		if (targetEditPart instanceof TextAnnotation2EditPart) {
			return ((TextAnnotation2EditPart) targetEditPart)
					.getMARelTypesOnTarget();
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public List getRelTypesOnSourceAndTarget(IAdaptable source,
			IAdaptable target) {
		IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source
				.getAdapter(IGraphicalEditPart.class);
		IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target
				.getAdapter(IGraphicalEditPart.class);
		if (sourceEditPart instanceof UserTaskEditPart) {
			return ((UserTaskEditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof ScriptTaskEditPart) {
			return ((ScriptTaskEditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof ServiceTaskEditPart) {
			return ((ServiceTaskEditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof BusinessRuleTaskEditPart) {
			return ((BusinessRuleTaskEditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof StartEventEditPart) {
			return ((StartEventEditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof StartEvent2EditPart) {
			return ((StartEvent2EditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof IntermediateCatchEventEditPart) {
			return ((IntermediateCatchEventEditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof IntermediateCatchEvent2EditPart) {
			return ((IntermediateCatchEvent2EditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof IntermediateCatchEvent3EditPart) {
			return ((IntermediateCatchEvent3EditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof IntermediateThrowEventEditPart) {
			return ((IntermediateThrowEventEditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof EndEventEditPart) {
			return ((EndEventEditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof EndEvent2EditPart) {
			return ((EndEvent2EditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof EndEvent3EditPart) {
			return ((EndEvent3EditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof ExclusiveGatewayEditPart) {
			return ((ExclusiveGatewayEditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof ParallelGatewayEditPart) {
			return ((ParallelGatewayEditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof SubProcessEditPart) {
			return ((SubProcessEditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof DataObjectEditPart) {
			return ((DataObjectEditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof TextAnnotationEditPart) {
			return ((TextAnnotationEditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof UserTask2EditPart) {
			return ((UserTask2EditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof ScriptTask2EditPart) {
			return ((ScriptTask2EditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof ServiceTask2EditPart) {
			return ((ServiceTask2EditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof BusinessRuleTask2EditPart) {
			return ((BusinessRuleTask2EditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof StartEvent3EditPart) {
			return ((StartEvent3EditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof StartEvent4EditPart) {
			return ((StartEvent4EditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof IntermediateCatchEvent4EditPart) {
			return ((IntermediateCatchEvent4EditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof IntermediateCatchEvent5EditPart) {
			return ((IntermediateCatchEvent5EditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof IntermediateCatchEvent6EditPart) {
			return ((IntermediateCatchEvent6EditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof IntermediateThrowEvent2EditPart) {
			return ((IntermediateThrowEvent2EditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof EndEvent4EditPart) {
			return ((EndEvent4EditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof EndEvent5EditPart) {
			return ((EndEvent5EditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof EndEvent6EditPart) {
			return ((EndEvent6EditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof ExclusiveGateway2EditPart) {
			return ((ExclusiveGateway2EditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof ParallelGateway2EditPart) {
			return ((ParallelGateway2EditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof SubProcess2EditPart) {
			return ((SubProcess2EditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof DataObject2EditPart) {
			return ((DataObject2EditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		if (sourceEditPart instanceof TextAnnotation2EditPart) {
			return ((TextAnnotation2EditPart) sourceEditPart)
					.getMARelTypesOnSourceAndTarget(targetEditPart);
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public List getTypesForSource(IAdaptable target,
			IElementType relationshipType) {
		IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target
				.getAdapter(IGraphicalEditPart.class);
		if (targetEditPart instanceof UserTaskEditPart) {
			return ((UserTaskEditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof ScriptTaskEditPart) {
			return ((ScriptTaskEditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof ServiceTaskEditPart) {
			return ((ServiceTaskEditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof BusinessRuleTaskEditPart) {
			return ((BusinessRuleTaskEditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof StartEventEditPart) {
			return ((StartEventEditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof StartEvent2EditPart) {
			return ((StartEvent2EditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof IntermediateCatchEventEditPart) {
			return ((IntermediateCatchEventEditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof IntermediateCatchEvent2EditPart) {
			return ((IntermediateCatchEvent2EditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof IntermediateCatchEvent3EditPart) {
			return ((IntermediateCatchEvent3EditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof IntermediateThrowEventEditPart) {
			return ((IntermediateThrowEventEditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof EndEventEditPart) {
			return ((EndEventEditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof EndEvent2EditPart) {
			return ((EndEvent2EditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof EndEvent3EditPart) {
			return ((EndEvent3EditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof ExclusiveGatewayEditPart) {
			return ((ExclusiveGatewayEditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof ParallelGatewayEditPart) {
			return ((ParallelGatewayEditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof SubProcessEditPart) {
			return ((SubProcessEditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof DataObjectEditPart) {
			return ((DataObjectEditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof TextAnnotationEditPart) {
			return ((TextAnnotationEditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof UserTask2EditPart) {
			return ((UserTask2EditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof ScriptTask2EditPart) {
			return ((ScriptTask2EditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof ServiceTask2EditPart) {
			return ((ServiceTask2EditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof BusinessRuleTask2EditPart) {
			return ((BusinessRuleTask2EditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof StartEvent3EditPart) {
			return ((StartEvent3EditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof StartEvent4EditPart) {
			return ((StartEvent4EditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof IntermediateCatchEvent4EditPart) {
			return ((IntermediateCatchEvent4EditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof IntermediateCatchEvent5EditPart) {
			return ((IntermediateCatchEvent5EditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof IntermediateCatchEvent6EditPart) {
			return ((IntermediateCatchEvent6EditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof IntermediateThrowEvent2EditPart) {
			return ((IntermediateThrowEvent2EditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof EndEvent4EditPart) {
			return ((EndEvent4EditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof EndEvent5EditPart) {
			return ((EndEvent5EditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof EndEvent6EditPart) {
			return ((EndEvent6EditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof ExclusiveGateway2EditPart) {
			return ((ExclusiveGateway2EditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof ParallelGateway2EditPart) {
			return ((ParallelGateway2EditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof SubProcess2EditPart) {
			return ((SubProcess2EditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof DataObject2EditPart) {
			return ((DataObject2EditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		if (targetEditPart instanceof TextAnnotation2EditPart) {
			return ((TextAnnotation2EditPart) targetEditPart)
					.getMATypesForSource(relationshipType);
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public List getTypesForTarget(IAdaptable source,
			IElementType relationshipType) {
		IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source
				.getAdapter(IGraphicalEditPart.class);
		if (sourceEditPart instanceof UserTaskEditPart) {
			return ((UserTaskEditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof ScriptTaskEditPart) {
			return ((ScriptTaskEditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof ServiceTaskEditPart) {
			return ((ServiceTaskEditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof BusinessRuleTaskEditPart) {
			return ((BusinessRuleTaskEditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof StartEventEditPart) {
			return ((StartEventEditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof StartEvent2EditPart) {
			return ((StartEvent2EditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof IntermediateCatchEventEditPart) {
			return ((IntermediateCatchEventEditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof IntermediateCatchEvent2EditPart) {
			return ((IntermediateCatchEvent2EditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof IntermediateCatchEvent3EditPart) {
			return ((IntermediateCatchEvent3EditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof IntermediateThrowEventEditPart) {
			return ((IntermediateThrowEventEditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof EndEventEditPart) {
			return ((EndEventEditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof EndEvent2EditPart) {
			return ((EndEvent2EditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof EndEvent3EditPart) {
			return ((EndEvent3EditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof ExclusiveGatewayEditPart) {
			return ((ExclusiveGatewayEditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof ParallelGatewayEditPart) {
			return ((ParallelGatewayEditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof SubProcessEditPart) {
			return ((SubProcessEditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof DataObjectEditPart) {
			return ((DataObjectEditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof TextAnnotationEditPart) {
			return ((TextAnnotationEditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof UserTask2EditPart) {
			return ((UserTask2EditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof ScriptTask2EditPart) {
			return ((ScriptTask2EditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof ServiceTask2EditPart) {
			return ((ServiceTask2EditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof BusinessRuleTask2EditPart) {
			return ((BusinessRuleTask2EditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof StartEvent3EditPart) {
			return ((StartEvent3EditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof StartEvent4EditPart) {
			return ((StartEvent4EditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof IntermediateCatchEvent4EditPart) {
			return ((IntermediateCatchEvent4EditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof IntermediateCatchEvent5EditPart) {
			return ((IntermediateCatchEvent5EditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof IntermediateCatchEvent6EditPart) {
			return ((IntermediateCatchEvent6EditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof IntermediateThrowEvent2EditPart) {
			return ((IntermediateThrowEvent2EditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof EndEvent4EditPart) {
			return ((EndEvent4EditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof EndEvent5EditPart) {
			return ((EndEvent5EditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof EndEvent6EditPart) {
			return ((EndEvent6EditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof ExclusiveGateway2EditPart) {
			return ((ExclusiveGateway2EditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof ParallelGateway2EditPart) {
			return ((ParallelGateway2EditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof SubProcess2EditPart) {
			return ((SubProcess2EditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof DataObject2EditPart) {
			return ((DataObject2EditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		if (sourceEditPart instanceof TextAnnotation2EditPart) {
			return ((TextAnnotation2EditPart) sourceEditPart)
					.getMATypesForTarget(relationshipType);
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * @generated
	 */
	public EObject selectExistingElementForSource(IAdaptable target,
			IElementType relationshipType) {
		return selectExistingElement(target,
				getTypesForSource(target, relationshipType));
	}

	/**
	 * @generated
	 */
	public EObject selectExistingElementForTarget(IAdaptable source,
			IElementType relationshipType) {
		return selectExistingElement(source,
				getTypesForTarget(source, relationshipType));
	}

	/**
	 * @generated
	 */
	protected EObject selectExistingElement(IAdaptable host, Collection types) {
		if (types.isEmpty()) {
			return null;
		}
		IGraphicalEditPart editPart = (IGraphicalEditPart) host
				.getAdapter(IGraphicalEditPart.class);
		if (editPart == null) {
			return null;
		}
		Diagram diagram = (Diagram) editPart.getRoot().getContents().getModel();
		HashSet<EObject> elements = new HashSet<EObject>();
		for (Iterator<EObject> it = diagram.getElement().eAllContents(); it
				.hasNext();) {
			EObject element = it.next();
			if (isApplicableElement(element, types)) {
				elements.add(element);
			}
		}
		if (elements.isEmpty()) {
			return null;
		}
		return selectElement((EObject[]) elements.toArray(new EObject[elements
				.size()]));
	}

	/**
	 * @generated
	 */
	protected boolean isApplicableElement(EObject element, Collection types) {
		IElementType type = ElementTypeRegistry.getInstance().getElementType(
				element);
		return types.contains(type);
	}

	/**
	 * @generated
	 */
	protected EObject selectElement(EObject[] elements) {
		Shell shell = Display.getCurrent().getActiveShell();
		ILabelProvider labelProvider = new AdapterFactoryLabelProvider(
				Bpmn2ProcessDiagramEditorPlugin.getInstance()
						.getItemProvidersAdapterFactory());
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(
				shell, labelProvider);
		dialog.setMessage(Messages.Bpmn2ModelingAssistantProviderMessage);
		dialog.setTitle(Messages.Bpmn2ModelingAssistantProviderTitle);
		dialog.setMultipleSelection(false);
		dialog.setElements(elements);
		EObject selected = null;
		if (dialog.open() == Window.OK) {
			selected = (EObject) dialog.getFirstResult();
		}
		return selected;
	}
}
