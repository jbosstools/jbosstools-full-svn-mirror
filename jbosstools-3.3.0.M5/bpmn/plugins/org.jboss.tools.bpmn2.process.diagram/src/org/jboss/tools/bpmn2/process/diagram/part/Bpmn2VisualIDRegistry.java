package org.jboss.tools.bpmn2.process.diagram.part;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.BusinessRuleTask;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.ErrorEventDefinition;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.IntermediateThrowEvent;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.ScriptTask;
import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.TerminateEventDefinition;
import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.bpmn2.TimerEventDefinition;
import org.eclipse.bpmn2.UserTask;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.AssociationEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.BusinessRuleTask2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.BusinessRuleTaskEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.BusinessRuleTaskName2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.BusinessRuleTaskNameEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.DataObject2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.DataObjectEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.DataObjectName2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.DataObjectNameEditPart;
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
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ScriptTaskName2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ScriptTaskNameEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.SequenceFlowEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ServiceTask2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ServiceTaskEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ServiceTaskName2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ServiceTaskNameEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.StartEvent2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.StartEvent3EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.StartEvent4EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.StartEventEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.SubProcess2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.SubProcessEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.TextAnnotation2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.TextAnnotationEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.TextAnnotationText2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.TextAnnotationTextEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.UserTask2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.UserTaskEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.UserTaskName2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.UserTaskNameEditPart;

/**
 * This registry is used to determine which type of visual object should be
 * created for the corresponding Diagram, Node, ChildNode or Link represented
 * by a domain model object.
 * 
 * @generated
 */
public class Bpmn2VisualIDRegistry {

	/**
	 * @generated
	 */
	private static final String DEBUG_KEY = "org.jboss.tools.bpmn2.process.diagram/debug/visualID"; //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static int getVisualID(View view) {
		if (view instanceof Diagram) {
			if (ProcessEditPart.MODEL_ID.equals(view.getType())) {
				return ProcessEditPart.VISUAL_ID;
			} else {
				return -1;
			}
		}
		return org.jboss.tools.bpmn2.process.diagram.part.Bpmn2VisualIDRegistry
				.getVisualID(view.getType());
	}

	/**
	 * @generated
	 */
	public static String getModelID(View view) {
		View diagram = view.getDiagram();
		while (view != diagram) {
			EAnnotation annotation = view.getEAnnotation("Shortcut"); //$NON-NLS-1$
			if (annotation != null) {
				return (String) annotation.getDetails().get("modelID"); //$NON-NLS-1$
			}
			view = (View) view.eContainer();
		}
		return diagram != null ? diagram.getType() : null;
	}

	/**
	 * @generated
	 */
	public static int getVisualID(String type) {
		try {
			return Integer.parseInt(type);
		} catch (NumberFormatException e) {
			if (Boolean.TRUE.toString().equalsIgnoreCase(
					Platform.getDebugOption(DEBUG_KEY))) {
				Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
						"Unable to parse view type as a visualID number: "
								+ type);
			}
		}
		return -1;
	}

	/**
	 * @generated
	 */
	public static String getType(int visualID) {
		return Integer.toString(visualID);
	}

	/**
	 * @generated
	 */
	public static int getDiagramVisualID(EObject domainElement) {
		if (domainElement == null) {
			return -1;
		}
		if (Bpmn2Package.eINSTANCE.getProcess().isSuperTypeOf(
				domainElement.eClass())
				&& isDiagram((Process) domainElement)) {
			return ProcessEditPart.VISUAL_ID;
		}
		return -1;
	}

	/**
	 * @generated
	 */
	public static int getNodeVisualID(View containerView, EObject domainElement) {
		if (domainElement == null) {
			return -1;
		}
		String containerModelID = org.jboss.tools.bpmn2.process.diagram.part.Bpmn2VisualIDRegistry
				.getModelID(containerView);
		if (!ProcessEditPart.MODEL_ID.equals(containerModelID)) {
			return -1;
		}
		int containerVisualID;
		if (ProcessEditPart.MODEL_ID.equals(containerModelID)) {
			containerVisualID = org.jboss.tools.bpmn2.process.diagram.part.Bpmn2VisualIDRegistry
					.getVisualID(containerView);
		} else {
			if (containerView instanceof Diagram) {
				containerVisualID = ProcessEditPart.VISUAL_ID;
			} else {
				return -1;
			}
		}
		switch (containerVisualID) {
		case ProcessEditPart.VISUAL_ID:
			if (Bpmn2Package.eINSTANCE.getUserTask().isSuperTypeOf(
					domainElement.eClass())) {
				return UserTaskEditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getScriptTask().isSuperTypeOf(
					domainElement.eClass())) {
				return ScriptTaskEditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getServiceTask().isSuperTypeOf(
					domainElement.eClass())) {
				return ServiceTaskEditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getBusinessRuleTask().isSuperTypeOf(
					domainElement.eClass())) {
				return BusinessRuleTaskEditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getStartEvent().isSuperTypeOf(
					domainElement.eClass())
					&& isStartEvent_2003((StartEvent) domainElement)) {
				return StartEventEditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getStartEvent().isSuperTypeOf(
					domainElement.eClass())
					&& isStartEvent_2007((StartEvent) domainElement)) {
				return StartEvent2EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getIntermediateCatchEvent()
					.isSuperTypeOf(domainElement.eClass())
					&& isIntermediateCatchEvent_2010((IntermediateCatchEvent) domainElement)) {
				return IntermediateCatchEventEditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getIntermediateCatchEvent()
					.isSuperTypeOf(domainElement.eClass())
					&& isIntermediateCatchEvent_2012((IntermediateCatchEvent) domainElement)) {
				return IntermediateCatchEvent2EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getIntermediateCatchEvent()
					.isSuperTypeOf(domainElement.eClass())
					&& isIntermediateCatchEvent_2013((IntermediateCatchEvent) domainElement)) {
				return IntermediateCatchEvent3EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getIntermediateThrowEvent()
					.isSuperTypeOf(domainElement.eClass())
					&& isIntermediateThrowEvent_2011((IntermediateThrowEvent) domainElement)) {
				return IntermediateThrowEventEditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getEndEvent().isSuperTypeOf(
					domainElement.eClass())
					&& isEndEvent_2004((EndEvent) domainElement)) {
				return EndEventEditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getEndEvent().isSuperTypeOf(
					domainElement.eClass())
					&& isEndEvent_2008((EndEvent) domainElement)) {
				return EndEvent2EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getEndEvent().isSuperTypeOf(
					domainElement.eClass())
					&& isEndEvent_2009((EndEvent) domainElement)) {
				return EndEvent3EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getExclusiveGateway().isSuperTypeOf(
					domainElement.eClass())) {
				return ExclusiveGatewayEditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getParallelGateway().isSuperTypeOf(
					domainElement.eClass())) {
				return ParallelGatewayEditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getSubProcess().isSuperTypeOf(
					domainElement.eClass())) {
				return SubProcessEditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getDataObject().isSuperTypeOf(
					domainElement.eClass())) {
				return DataObjectEditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getTextAnnotation().isSuperTypeOf(
					domainElement.eClass())) {
				return TextAnnotationEditPart.VISUAL_ID;
			}
			break;
		case SubProcessEditPart.VISUAL_ID:
			if (Bpmn2Package.eINSTANCE.getUserTask().isSuperTypeOf(
					domainElement.eClass())) {
				return UserTask2EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getScriptTask().isSuperTypeOf(
					domainElement.eClass())) {
				return ScriptTask2EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getServiceTask().isSuperTypeOf(
					domainElement.eClass())) {
				return ServiceTask2EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getBusinessRuleTask().isSuperTypeOf(
					domainElement.eClass())) {
				return BusinessRuleTask2EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getStartEvent().isSuperTypeOf(
					domainElement.eClass())
					&& isStartEvent_3003((StartEvent) domainElement)) {
				return StartEvent3EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getStartEvent().isSuperTypeOf(
					domainElement.eClass())
					&& isStartEvent_3005((StartEvent) domainElement)) {
				return StartEvent4EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getIntermediateCatchEvent()
					.isSuperTypeOf(domainElement.eClass())
					&& isIntermediateCatchEvent_3011((IntermediateCatchEvent) domainElement)) {
				return IntermediateCatchEvent4EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getIntermediateCatchEvent()
					.isSuperTypeOf(domainElement.eClass())
					&& isIntermediateCatchEvent_3013((IntermediateCatchEvent) domainElement)) {
				return IntermediateCatchEvent5EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getIntermediateCatchEvent()
					.isSuperTypeOf(domainElement.eClass())
					&& isIntermediateCatchEvent_3018((IntermediateCatchEvent) domainElement)) {
				return IntermediateCatchEvent6EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getIntermediateThrowEvent()
					.isSuperTypeOf(domainElement.eClass())
					&& isIntermediateThrowEvent_3012((IntermediateThrowEvent) domainElement)) {
				return IntermediateThrowEvent2EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getEndEvent().isSuperTypeOf(
					domainElement.eClass())
					&& isEndEvent_3006((EndEvent) domainElement)) {
				return EndEvent4EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getEndEvent().isSuperTypeOf(
					domainElement.eClass())
					&& isEndEvent_3009((EndEvent) domainElement)) {
				return EndEvent5EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getEndEvent().isSuperTypeOf(
					domainElement.eClass())
					&& isEndEvent_3010((EndEvent) domainElement)) {
				return EndEvent6EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getExclusiveGateway().isSuperTypeOf(
					domainElement.eClass())) {
				return ExclusiveGateway2EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getParallelGateway().isSuperTypeOf(
					domainElement.eClass())) {
				return ParallelGateway2EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getSubProcess().isSuperTypeOf(
					domainElement.eClass())) {
				return SubProcess2EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getDataObject().isSuperTypeOf(
					domainElement.eClass())) {
				return DataObject2EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getTextAnnotation().isSuperTypeOf(
					domainElement.eClass())) {
				return TextAnnotation2EditPart.VISUAL_ID;
			}
			break;
		case SubProcess2EditPart.VISUAL_ID:
			if (Bpmn2Package.eINSTANCE.getUserTask().isSuperTypeOf(
					domainElement.eClass())) {
				return UserTask2EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getScriptTask().isSuperTypeOf(
					domainElement.eClass())) {
				return ScriptTask2EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getServiceTask().isSuperTypeOf(
					domainElement.eClass())) {
				return ServiceTask2EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getBusinessRuleTask().isSuperTypeOf(
					domainElement.eClass())) {
				return BusinessRuleTask2EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getStartEvent().isSuperTypeOf(
					domainElement.eClass())
					&& isStartEvent_3003((StartEvent) domainElement)) {
				return StartEvent3EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getStartEvent().isSuperTypeOf(
					domainElement.eClass())
					&& isStartEvent_3005((StartEvent) domainElement)) {
				return StartEvent4EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getIntermediateCatchEvent()
					.isSuperTypeOf(domainElement.eClass())
					&& isIntermediateCatchEvent_3011((IntermediateCatchEvent) domainElement)) {
				return IntermediateCatchEvent4EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getIntermediateCatchEvent()
					.isSuperTypeOf(domainElement.eClass())
					&& isIntermediateCatchEvent_3013((IntermediateCatchEvent) domainElement)) {
				return IntermediateCatchEvent5EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getIntermediateCatchEvent()
					.isSuperTypeOf(domainElement.eClass())
					&& isIntermediateCatchEvent_3018((IntermediateCatchEvent) domainElement)) {
				return IntermediateCatchEvent6EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getIntermediateThrowEvent()
					.isSuperTypeOf(domainElement.eClass())
					&& isIntermediateThrowEvent_3012((IntermediateThrowEvent) domainElement)) {
				return IntermediateThrowEvent2EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getEndEvent().isSuperTypeOf(
					domainElement.eClass())
					&& isEndEvent_3006((EndEvent) domainElement)) {
				return EndEvent4EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getEndEvent().isSuperTypeOf(
					domainElement.eClass())
					&& isEndEvent_3009((EndEvent) domainElement)) {
				return EndEvent5EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getEndEvent().isSuperTypeOf(
					domainElement.eClass())
					&& isEndEvent_3010((EndEvent) domainElement)) {
				return EndEvent6EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getExclusiveGateway().isSuperTypeOf(
					domainElement.eClass())) {
				return ExclusiveGateway2EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getParallelGateway().isSuperTypeOf(
					domainElement.eClass())) {
				return ParallelGateway2EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getSubProcess().isSuperTypeOf(
					domainElement.eClass())) {
				return SubProcess2EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getDataObject().isSuperTypeOf(
					domainElement.eClass())) {
				return DataObject2EditPart.VISUAL_ID;
			}
			if (Bpmn2Package.eINSTANCE.getTextAnnotation().isSuperTypeOf(
					domainElement.eClass())) {
				return TextAnnotation2EditPart.VISUAL_ID;
			}
			break;
		}
		return -1;
	}

	public static int getLabelVisualId(EObject containerElement,
			EObject domainElement) {
		if (containerElement instanceof Process) {
			if (domainElement instanceof UserTask) {
				return UserTaskNameEditPart.VISUAL_ID;
			} else if (domainElement instanceof ServiceTask) {
				return ServiceTaskNameEditPart.VISUAL_ID;
			} else if (domainElement instanceof DataObject) {
				return DataObjectNameEditPart.VISUAL_ID;
			} else if (domainElement instanceof TextAnnotation) {
				return TextAnnotationTextEditPart.VISUAL_ID;
			} else if (domainElement instanceof ScriptTask) {
				return ScriptTaskNameEditPart.VISUAL_ID;
			} else if (domainElement instanceof BusinessRuleTask) {
				return BusinessRuleTaskNameEditPart.VISUAL_ID;
			}
		} else if (containerElement instanceof SubProcess) {
			if (domainElement instanceof UserTask) {
				return UserTaskName2EditPart.VISUAL_ID;
			} else if (domainElement instanceof ServiceTask) {
				return ServiceTaskName2EditPart.VISUAL_ID;
			} else if (domainElement instanceof DataObject) {
				return DataObjectName2EditPart.VISUAL_ID;
			} else if (domainElement instanceof TextAnnotation) {
				return TextAnnotationText2EditPart.VISUAL_ID;
			} else if (domainElement instanceof ScriptTask) {
				return ScriptTaskName2EditPart.VISUAL_ID;
			} else if (domainElement instanceof BusinessRuleTask) {
				return BusinessRuleTaskName2EditPart.VISUAL_ID;
			}
		}
		return -1;
	}

	/**
	 * @generated
	 */
	public static boolean canCreateNode(View containerView, int nodeVisualID) {
		String containerModelID = org.jboss.tools.bpmn2.process.diagram.part.Bpmn2VisualIDRegistry
				.getModelID(containerView);
		if (!ProcessEditPart.MODEL_ID.equals(containerModelID)) {
			return false;
		}
		int containerVisualID;
		if (ProcessEditPart.MODEL_ID.equals(containerModelID)) {
			containerVisualID = org.jboss.tools.bpmn2.process.diagram.part.Bpmn2VisualIDRegistry
					.getVisualID(containerView);
		} else {
			if (containerView instanceof Diagram) {
				containerVisualID = ProcessEditPart.VISUAL_ID;
			} else {
				return false;
			}
		}
		switch (containerVisualID) {
		case ProcessEditPart.VISUAL_ID:
			if (UserTaskEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (ScriptTaskEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (ServiceTaskEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (BusinessRuleTaskEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (StartEventEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (StartEvent2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (IntermediateCatchEventEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (IntermediateCatchEvent2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (IntermediateCatchEvent3EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (IntermediateThrowEventEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (EndEventEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (EndEvent2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (EndEvent3EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (ExclusiveGatewayEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (ParallelGatewayEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (SubProcessEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (DataObjectEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (TextAnnotationEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case UserTaskEditPart.VISUAL_ID:
			if (UserTaskNameEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case ScriptTaskEditPart.VISUAL_ID:
			if (ScriptTaskNameEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case ServiceTaskEditPart.VISUAL_ID:
			if (ServiceTaskNameEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case BusinessRuleTaskEditPart.VISUAL_ID:
			if (BusinessRuleTaskNameEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case SubProcessEditPart.VISUAL_ID:
			if (UserTask2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (ScriptTask2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (ServiceTask2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (BusinessRuleTask2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (StartEvent3EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (StartEvent4EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (IntermediateCatchEvent4EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (IntermediateCatchEvent5EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (IntermediateCatchEvent6EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (IntermediateThrowEvent2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (EndEvent4EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (EndEvent5EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (EndEvent6EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (ExclusiveGateway2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (ParallelGateway2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (SubProcess2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (DataObject2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (TextAnnotation2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case DataObjectEditPart.VISUAL_ID:
			if (DataObjectNameEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case TextAnnotationEditPart.VISUAL_ID:
			if (TextAnnotationTextEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case UserTask2EditPart.VISUAL_ID:
			if (UserTaskName2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case ScriptTask2EditPart.VISUAL_ID:
			if (ScriptTaskName2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case ServiceTask2EditPart.VISUAL_ID:
			if (ServiceTaskName2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case BusinessRuleTask2EditPart.VISUAL_ID:
			if (BusinessRuleTaskName2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case SubProcess2EditPart.VISUAL_ID:
			if (UserTask2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (ScriptTask2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (ServiceTask2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (BusinessRuleTask2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (StartEvent3EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (StartEvent4EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (IntermediateCatchEvent4EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (IntermediateCatchEvent5EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (IntermediateCatchEvent6EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (IntermediateThrowEvent2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (EndEvent4EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (EndEvent5EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (EndEvent6EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (ExclusiveGateway2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (ParallelGateway2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (SubProcess2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (DataObject2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (TextAnnotation2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case DataObject2EditPart.VISUAL_ID:
			if (DataObjectName2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case TextAnnotation2EditPart.VISUAL_ID:
			if (TextAnnotationText2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		}
		return false;
	}

	/**
	 * @generated
	 */
	public static int getLinkWithClassVisualID(EObject domainElement) {
		if (domainElement == null) {
			return -1;
		}
		if (Bpmn2Package.eINSTANCE.getSequenceFlow().isSuperTypeOf(
				domainElement.eClass())) {
			return SequenceFlowEditPart.VISUAL_ID;
		}
		if (Bpmn2Package.eINSTANCE.getAssociation().isSuperTypeOf(
				domainElement.eClass())) {
			return AssociationEditPart.VISUAL_ID;
		}
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static boolean isDiagram(Process element) {
		return true;
	}

	/**
	 * @generated not
	 */
	private static boolean isStartEvent_2003(StartEvent domainElement) {
		return domainElement.getEventDefinitions().isEmpty();
	}

	/**
	 * @generated not
	 */
	private static boolean isStartEvent_2007(StartEvent domainElement) {
		return !(domainElement.getEventDefinitions().isEmpty())
				&& domainElement.getEventDefinitions().get(0) instanceof MessageEventDefinition;
	}

	/**
	 * @generated not
	 */
	private static boolean isEndEvent_2004(EndEvent domainElement) {
		return domainElement.getEventDefinitions().isEmpty();
	}

	/**
	 * @generated not
	 */
	private static boolean isEndEvent_2008(EndEvent domainElement) {
		return !(domainElement.getEventDefinitions().isEmpty())
				&& domainElement.getEventDefinitions().get(0) instanceof MessageEventDefinition;
	}

	/**
	 * @generated not
	 */
	private static boolean isEndEvent_2009(EndEvent domainElement) {
		return !(domainElement.getEventDefinitions().isEmpty())
				&& domainElement.getEventDefinitions().get(0) instanceof TerminateEventDefinition;
	}

	/**
	 * @generated not
	 */
	private static boolean isIntermediateCatchEvent_2010(
			IntermediateCatchEvent domainElement) {
		return !(domainElement.getEventDefinitions().isEmpty())
				&& domainElement.getEventDefinitions().get(0) instanceof MessageEventDefinition;
	}

	/**
	 * @generated not
	 */
	private static boolean isIntermediateThrowEvent_2011(
			IntermediateThrowEvent domainElement) {
		return !(domainElement.getEventDefinitions().isEmpty())
				&& domainElement.getEventDefinitions().get(0) instanceof MessageEventDefinition;
	}

	/**
	 * @generated not
	 */
	private static boolean isIntermediateCatchEvent_2012(
			IntermediateCatchEvent domainElement) {
		return !(domainElement.getEventDefinitions().isEmpty())
				&& domainElement.getEventDefinitions().get(0) instanceof TimerEventDefinition;
	}

	/**
	 * @generated not
	 */
	private static boolean isIntermediateCatchEvent_2013(
			IntermediateCatchEvent domainElement) {
		return !(domainElement.getEventDefinitions().isEmpty())
				&& domainElement.getEventDefinitions().get(0) instanceof ErrorEventDefinition;
	}

	/**
	 * @generated not
	 */
	private static boolean isStartEvent_3003(StartEvent domainElement) {
		return domainElement.getEventDefinitions().isEmpty();
	}

	/**
	 * @generated not
	 */
	private static boolean isStartEvent_3005(StartEvent domainElement) {
		return !(domainElement.getEventDefinitions().isEmpty())
				&& domainElement.getEventDefinitions().get(0) instanceof MessageEventDefinition;
	}

	/**
	 * @generated not
	 */
	private static boolean isEndEvent_3006(EndEvent domainElement) {
		return domainElement.getEventDefinitions().isEmpty();
	}

	/**
	 * @generated not
	 */
	private static boolean isEndEvent_3009(EndEvent domainElement) {
		return !(domainElement.getEventDefinitions().isEmpty())
				&& domainElement.getEventDefinitions().get(0) instanceof MessageEventDefinition;
	}

	/**
	 * @generated not
	 */
	private static boolean isEndEvent_3010(EndEvent domainElement) {
		return !(domainElement.getEventDefinitions().isEmpty())
				&& domainElement.getEventDefinitions().get(0) instanceof TerminateEventDefinition;
	}

	/**
	 * @generated not
	 */
	private static boolean isIntermediateCatchEvent_3011(
			IntermediateCatchEvent domainElement) {
		return !(domainElement.getEventDefinitions().isEmpty())
				&& domainElement.getEventDefinitions().get(0) instanceof MessageEventDefinition;
	}

	/**
	 * @generated not
	 */
	private static boolean isIntermediateThrowEvent_3012(
			IntermediateThrowEvent domainElement) {
		return !(domainElement.getEventDefinitions().isEmpty())
				&& domainElement.getEventDefinitions().get(0) instanceof MessageEventDefinition;
	}

	/**
	 * @generated not
	 */
	private static boolean isIntermediateCatchEvent_3013(
			IntermediateCatchEvent domainElement) {
		return !(domainElement.getEventDefinitions().isEmpty())
				&& domainElement.getEventDefinitions().get(0) instanceof TimerEventDefinition;
	}

	/**
	 * @generated
	 */
	private static boolean isIntermediateCatchEvent_3018(
			IntermediateCatchEvent domainElement) {
		// FIXME: implement this method 
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException(
				"No java implementation provided in 'isIntermediateCatchEvent_3018' operation"); //$NON-NLS-1$
	}

}
