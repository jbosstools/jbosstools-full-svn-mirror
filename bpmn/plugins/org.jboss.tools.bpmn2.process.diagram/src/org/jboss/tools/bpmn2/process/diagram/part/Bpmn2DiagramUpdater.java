package org.jboss.tools.bpmn2.process.diagram.part;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.bpmn2.Artifact;
import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.IntermediateThrowEvent;
import org.eclipse.bpmn2.ParallelGateway;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.ScriptTask;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.bpmn2.UserTask;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.View;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.AssociationEditPart;
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
import org.jboss.tools.bpmn2.process.diagram.edit.parts.IntermediateCatchEventEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.IntermediateThrowEvent2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.IntermediateThrowEventEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ParallelGateway2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ParallelGatewayEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ProcessEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ScriptTask2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ScriptTaskEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.SequenceFlowEditPart;
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
import org.jboss.tools.bpmn2.process.diagram.providers.Bpmn2ElementTypes;

/**
 * @generated
 */
public class Bpmn2DiagramUpdater {

	/**
	 * @generated
	 */
	public static List<Bpmn2NodeDescriptor> getSemanticChildren(View view) {
		switch (Bpmn2VisualIDRegistry.getVisualID(view)) {
		case ProcessEditPart.VISUAL_ID:
			return getProcess_1000SemanticChildren(view);
		case SubProcessEditPart.VISUAL_ID:
			return getSubProcess_2016SemanticChildren(view);
		case SubProcess2EditPart.VISUAL_ID:
			return getSubProcess_3001SemanticChildren(view);
		}
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2NodeDescriptor> getProcess_1000SemanticChildren(
			View view) {
		if (!view.isSetElement()) {
			return Collections.emptyList();
		}
		Process modelElement = (Process) view.getElement();
		LinkedList<Bpmn2NodeDescriptor> result = new LinkedList<Bpmn2NodeDescriptor>();
		for (Iterator<?> it = modelElement.getFlowElements().iterator(); it
				.hasNext();) {
			FlowElement childElement = (FlowElement) it.next();
			int visualID = Bpmn2VisualIDRegistry.getNodeVisualID(view,
					childElement);
			if (visualID == UserTaskEditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == ServiceTaskEditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == StartEventEditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == StartEvent2EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == EndEventEditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == ExclusiveGatewayEditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == ParallelGatewayEditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == EndEvent2EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == EndEvent3EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == IntermediateCatchEventEditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == IntermediateThrowEventEditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == IntermediateCatchEvent2EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == IntermediateCatchEvent3EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == DataObjectEditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == SubProcessEditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == ScriptTaskEditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
		}
		for (Iterator<?> it = modelElement.getArtifacts().iterator(); it
				.hasNext();) {
			Artifact childElement = (Artifact) it.next();
			int visualID = Bpmn2VisualIDRegistry.getNodeVisualID(view,
					childElement);
			if (visualID == TextAnnotationEditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
		}
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2NodeDescriptor> getSubProcess_2016SemanticChildren(
			View view) {
		if (!view.isSetElement()) {
			return Collections.emptyList();
		}
		SubProcess modelElement = (SubProcess) view.getElement();
		LinkedList<Bpmn2NodeDescriptor> result = new LinkedList<Bpmn2NodeDescriptor>();
		for (Iterator<?> it = modelElement.getFlowElements().iterator(); it
				.hasNext();) {
			FlowElement childElement = (FlowElement) it.next();
			int visualID = Bpmn2VisualIDRegistry.getNodeVisualID(view,
					childElement);
			if (visualID == SubProcess2EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == UserTask2EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == ServiceTask2EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == StartEvent3EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == StartEvent4EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == EndEvent4EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == ExclusiveGateway2EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == ParallelGateway2EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == EndEvent5EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == EndEvent6EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == IntermediateCatchEvent4EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == IntermediateThrowEvent2EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == IntermediateCatchEvent5EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == DataObject2EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == ScriptTask2EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
		}
		for (Iterator<?> it = modelElement.getArtifacts().iterator(); it
				.hasNext();) {
			Artifact childElement = (Artifact) it.next();
			int visualID = Bpmn2VisualIDRegistry.getNodeVisualID(view,
					childElement);
			if (visualID == TextAnnotation2EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
		}
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2NodeDescriptor> getSubProcess_3001SemanticChildren(
			View view) {
		if (!view.isSetElement()) {
			return Collections.emptyList();
		}
		SubProcess modelElement = (SubProcess) view.getElement();
		LinkedList<Bpmn2NodeDescriptor> result = new LinkedList<Bpmn2NodeDescriptor>();
		for (Iterator<?> it = modelElement.getFlowElements().iterator(); it
				.hasNext();) {
			FlowElement childElement = (FlowElement) it.next();
			int visualID = Bpmn2VisualIDRegistry.getNodeVisualID(view,
					childElement);
			if (visualID == SubProcess2EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == UserTask2EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == ServiceTask2EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == StartEvent3EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == StartEvent4EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == EndEvent4EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == ExclusiveGateway2EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == ParallelGateway2EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == EndEvent5EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == EndEvent6EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == IntermediateCatchEvent4EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == IntermediateThrowEvent2EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == IntermediateCatchEvent5EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == DataObject2EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == ScriptTask2EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
		}
		for (Iterator<?> it = modelElement.getArtifacts().iterator(); it
				.hasNext();) {
			Artifact childElement = (Artifact) it.next();
			int visualID = Bpmn2VisualIDRegistry.getNodeVisualID(view,
					childElement);
			if (visualID == TextAnnotation2EditPart.VISUAL_ID) {
				result.add(new Bpmn2NodeDescriptor(childElement, visualID));
				continue;
			}
		}
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getContainedLinks(View view) {
		switch (Bpmn2VisualIDRegistry.getVisualID(view)) {
		case ProcessEditPart.VISUAL_ID:
			return getProcess_1000ContainedLinks(view);
		case UserTaskEditPart.VISUAL_ID:
			return getUserTask_2001ContainedLinks(view);
		case ServiceTaskEditPart.VISUAL_ID:
			return getServiceTask_2002ContainedLinks(view);
		case StartEventEditPart.VISUAL_ID:
			return getStartEvent_2003ContainedLinks(view);
		case StartEvent2EditPart.VISUAL_ID:
			return getStartEvent_2007ContainedLinks(view);
		case EndEventEditPart.VISUAL_ID:
			return getEndEvent_2004ContainedLinks(view);
		case ExclusiveGatewayEditPart.VISUAL_ID:
			return getExclusiveGateway_2005ContainedLinks(view);
		case ParallelGatewayEditPart.VISUAL_ID:
			return getParallelGateway_2006ContainedLinks(view);
		case EndEvent2EditPart.VISUAL_ID:
			return getEndEvent_2008ContainedLinks(view);
		case EndEvent3EditPart.VISUAL_ID:
			return getEndEvent_2009ContainedLinks(view);
		case IntermediateCatchEventEditPart.VISUAL_ID:
			return getIntermediateCatchEvent_2010ContainedLinks(view);
		case IntermediateThrowEventEditPart.VISUAL_ID:
			return getIntermediateThrowEvent_2011ContainedLinks(view);
		case IntermediateCatchEvent2EditPart.VISUAL_ID:
			return getIntermediateCatchEvent_2012ContainedLinks(view);
		case IntermediateCatchEvent3EditPart.VISUAL_ID:
			return getIntermediateCatchEvent_2013ContainedLinks(view);
		case DataObjectEditPart.VISUAL_ID:
			return getDataObject_2014ContainedLinks(view);
		case TextAnnotationEditPart.VISUAL_ID:
			return getTextAnnotation_2015ContainedLinks(view);
		case SubProcessEditPart.VISUAL_ID:
			return getSubProcess_2016ContainedLinks(view);
		case ScriptTaskEditPart.VISUAL_ID:
			return getScriptTask_2017ContainedLinks(view);
		case SubProcess2EditPart.VISUAL_ID:
			return getSubProcess_3001ContainedLinks(view);
		case UserTask2EditPart.VISUAL_ID:
			return getUserTask_3002ContainedLinks(view);
		case ServiceTask2EditPart.VISUAL_ID:
			return getServiceTask_3004ContainedLinks(view);
		case StartEvent3EditPart.VISUAL_ID:
			return getStartEvent_3003ContainedLinks(view);
		case StartEvent4EditPart.VISUAL_ID:
			return getStartEvent_3005ContainedLinks(view);
		case EndEvent4EditPart.VISUAL_ID:
			return getEndEvent_3006ContainedLinks(view);
		case ExclusiveGateway2EditPart.VISUAL_ID:
			return getExclusiveGateway_3007ContainedLinks(view);
		case ParallelGateway2EditPart.VISUAL_ID:
			return getParallelGateway_3008ContainedLinks(view);
		case EndEvent5EditPart.VISUAL_ID:
			return getEndEvent_3009ContainedLinks(view);
		case EndEvent6EditPart.VISUAL_ID:
			return getEndEvent_3010ContainedLinks(view);
		case IntermediateCatchEvent4EditPart.VISUAL_ID:
			return getIntermediateCatchEvent_3011ContainedLinks(view);
		case IntermediateThrowEvent2EditPart.VISUAL_ID:
			return getIntermediateThrowEvent_3012ContainedLinks(view);
		case IntermediateCatchEvent5EditPart.VISUAL_ID:
			return getIntermediateCatchEvent_3013ContainedLinks(view);
		case DataObject2EditPart.VISUAL_ID:
			return getDataObject_3014ContainedLinks(view);
		case TextAnnotation2EditPart.VISUAL_ID:
			return getTextAnnotation_3015ContainedLinks(view);
		case ScriptTask2EditPart.VISUAL_ID:
			return getScriptTask_3016ContainedLinks(view);
		case SequenceFlowEditPart.VISUAL_ID:
			return getSequenceFlow_4001ContainedLinks(view);
		case AssociationEditPart.VISUAL_ID:
			return getAssociation_4002ContainedLinks(view);
		}
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getIncomingLinks(View view) {
		switch (Bpmn2VisualIDRegistry.getVisualID(view)) {
		case UserTaskEditPart.VISUAL_ID:
			return getUserTask_2001IncomingLinks(view);
		case ServiceTaskEditPart.VISUAL_ID:
			return getServiceTask_2002IncomingLinks(view);
		case StartEventEditPart.VISUAL_ID:
			return getStartEvent_2003IncomingLinks(view);
		case StartEvent2EditPart.VISUAL_ID:
			return getStartEvent_2007IncomingLinks(view);
		case EndEventEditPart.VISUAL_ID:
			return getEndEvent_2004IncomingLinks(view);
		case ExclusiveGatewayEditPart.VISUAL_ID:
			return getExclusiveGateway_2005IncomingLinks(view);
		case ParallelGatewayEditPart.VISUAL_ID:
			return getParallelGateway_2006IncomingLinks(view);
		case EndEvent2EditPart.VISUAL_ID:
			return getEndEvent_2008IncomingLinks(view);
		case EndEvent3EditPart.VISUAL_ID:
			return getEndEvent_2009IncomingLinks(view);
		case IntermediateCatchEventEditPart.VISUAL_ID:
			return getIntermediateCatchEvent_2010IncomingLinks(view);
		case IntermediateThrowEventEditPart.VISUAL_ID:
			return getIntermediateThrowEvent_2011IncomingLinks(view);
		case IntermediateCatchEvent2EditPart.VISUAL_ID:
			return getIntermediateCatchEvent_2012IncomingLinks(view);
		case IntermediateCatchEvent3EditPart.VISUAL_ID:
			return getIntermediateCatchEvent_2013IncomingLinks(view);
		case DataObjectEditPart.VISUAL_ID:
			return getDataObject_2014IncomingLinks(view);
		case TextAnnotationEditPart.VISUAL_ID:
			return getTextAnnotation_2015IncomingLinks(view);
		case SubProcessEditPart.VISUAL_ID:
			return getSubProcess_2016IncomingLinks(view);
		case ScriptTaskEditPart.VISUAL_ID:
			return getScriptTask_2017IncomingLinks(view);
		case SubProcess2EditPart.VISUAL_ID:
			return getSubProcess_3001IncomingLinks(view);
		case UserTask2EditPart.VISUAL_ID:
			return getUserTask_3002IncomingLinks(view);
		case ServiceTask2EditPart.VISUAL_ID:
			return getServiceTask_3004IncomingLinks(view);
		case StartEvent3EditPart.VISUAL_ID:
			return getStartEvent_3003IncomingLinks(view);
		case StartEvent4EditPart.VISUAL_ID:
			return getStartEvent_3005IncomingLinks(view);
		case EndEvent4EditPart.VISUAL_ID:
			return getEndEvent_3006IncomingLinks(view);
		case ExclusiveGateway2EditPart.VISUAL_ID:
			return getExclusiveGateway_3007IncomingLinks(view);
		case ParallelGateway2EditPart.VISUAL_ID:
			return getParallelGateway_3008IncomingLinks(view);
		case EndEvent5EditPart.VISUAL_ID:
			return getEndEvent_3009IncomingLinks(view);
		case EndEvent6EditPart.VISUAL_ID:
			return getEndEvent_3010IncomingLinks(view);
		case IntermediateCatchEvent4EditPart.VISUAL_ID:
			return getIntermediateCatchEvent_3011IncomingLinks(view);
		case IntermediateThrowEvent2EditPart.VISUAL_ID:
			return getIntermediateThrowEvent_3012IncomingLinks(view);
		case IntermediateCatchEvent5EditPart.VISUAL_ID:
			return getIntermediateCatchEvent_3013IncomingLinks(view);
		case DataObject2EditPart.VISUAL_ID:
			return getDataObject_3014IncomingLinks(view);
		case TextAnnotation2EditPart.VISUAL_ID:
			return getTextAnnotation_3015IncomingLinks(view);
		case ScriptTask2EditPart.VISUAL_ID:
			return getScriptTask_3016IncomingLinks(view);
		case SequenceFlowEditPart.VISUAL_ID:
			return getSequenceFlow_4001IncomingLinks(view);
		case AssociationEditPart.VISUAL_ID:
			return getAssociation_4002IncomingLinks(view);
		}
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getOutgoingLinks(View view) {
		switch (Bpmn2VisualIDRegistry.getVisualID(view)) {
		case UserTaskEditPart.VISUAL_ID:
			return getUserTask_2001OutgoingLinks(view);
		case ServiceTaskEditPart.VISUAL_ID:
			return getServiceTask_2002OutgoingLinks(view);
		case StartEventEditPart.VISUAL_ID:
			return getStartEvent_2003OutgoingLinks(view);
		case StartEvent2EditPart.VISUAL_ID:
			return getStartEvent_2007OutgoingLinks(view);
		case EndEventEditPart.VISUAL_ID:
			return getEndEvent_2004OutgoingLinks(view);
		case ExclusiveGatewayEditPart.VISUAL_ID:
			return getExclusiveGateway_2005OutgoingLinks(view);
		case ParallelGatewayEditPart.VISUAL_ID:
			return getParallelGateway_2006OutgoingLinks(view);
		case EndEvent2EditPart.VISUAL_ID:
			return getEndEvent_2008OutgoingLinks(view);
		case EndEvent3EditPart.VISUAL_ID:
			return getEndEvent_2009OutgoingLinks(view);
		case IntermediateCatchEventEditPart.VISUAL_ID:
			return getIntermediateCatchEvent_2010OutgoingLinks(view);
		case IntermediateThrowEventEditPart.VISUAL_ID:
			return getIntermediateThrowEvent_2011OutgoingLinks(view);
		case IntermediateCatchEvent2EditPart.VISUAL_ID:
			return getIntermediateCatchEvent_2012OutgoingLinks(view);
		case IntermediateCatchEvent3EditPart.VISUAL_ID:
			return getIntermediateCatchEvent_2013OutgoingLinks(view);
		case DataObjectEditPart.VISUAL_ID:
			return getDataObject_2014OutgoingLinks(view);
		case TextAnnotationEditPart.VISUAL_ID:
			return getTextAnnotation_2015OutgoingLinks(view);
		case SubProcessEditPart.VISUAL_ID:
			return getSubProcess_2016OutgoingLinks(view);
		case ScriptTaskEditPart.VISUAL_ID:
			return getScriptTask_2017OutgoingLinks(view);
		case SubProcess2EditPart.VISUAL_ID:
			return getSubProcess_3001OutgoingLinks(view);
		case UserTask2EditPart.VISUAL_ID:
			return getUserTask_3002OutgoingLinks(view);
		case ServiceTask2EditPart.VISUAL_ID:
			return getServiceTask_3004OutgoingLinks(view);
		case StartEvent3EditPart.VISUAL_ID:
			return getStartEvent_3003OutgoingLinks(view);
		case StartEvent4EditPart.VISUAL_ID:
			return getStartEvent_3005OutgoingLinks(view);
		case EndEvent4EditPart.VISUAL_ID:
			return getEndEvent_3006OutgoingLinks(view);
		case ExclusiveGateway2EditPart.VISUAL_ID:
			return getExclusiveGateway_3007OutgoingLinks(view);
		case ParallelGateway2EditPart.VISUAL_ID:
			return getParallelGateway_3008OutgoingLinks(view);
		case EndEvent5EditPart.VISUAL_ID:
			return getEndEvent_3009OutgoingLinks(view);
		case EndEvent6EditPart.VISUAL_ID:
			return getEndEvent_3010OutgoingLinks(view);
		case IntermediateCatchEvent4EditPart.VISUAL_ID:
			return getIntermediateCatchEvent_3011OutgoingLinks(view);
		case IntermediateThrowEvent2EditPart.VISUAL_ID:
			return getIntermediateThrowEvent_3012OutgoingLinks(view);
		case IntermediateCatchEvent5EditPart.VISUAL_ID:
			return getIntermediateCatchEvent_3013OutgoingLinks(view);
		case DataObject2EditPart.VISUAL_ID:
			return getDataObject_3014OutgoingLinks(view);
		case TextAnnotation2EditPart.VISUAL_ID:
			return getTextAnnotation_3015OutgoingLinks(view);
		case ScriptTask2EditPart.VISUAL_ID:
			return getScriptTask_3016OutgoingLinks(view);
		case SequenceFlowEditPart.VISUAL_ID:
			return getSequenceFlow_4001OutgoingLinks(view);
		case AssociationEditPart.VISUAL_ID:
			return getAssociation_4002OutgoingLinks(view);
		}
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getProcess_1000ContainedLinks(
			View view) {
		Process modelElement = (Process) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getContainedTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getContainedTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getUserTask_2001ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getServiceTask_2002ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getStartEvent_2003ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getStartEvent_2007ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getEndEvent_2004ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getExclusiveGateway_2005ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getParallelGateway_2006ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getEndEvent_2008ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getEndEvent_2009ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getIntermediateCatchEvent_2010ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getIntermediateThrowEvent_2011ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getIntermediateCatchEvent_2012ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getIntermediateCatchEvent_2013ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getDataObject_2014ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getTextAnnotation_2015ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getSubProcess_2016ContainedLinks(
			View view) {
		SubProcess modelElement = (SubProcess) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getContainedTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getScriptTask_2017ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getSubProcess_3001ContainedLinks(
			View view) {
		SubProcess modelElement = (SubProcess) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getContainedTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getUserTask_3002ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getServiceTask_3004ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getStartEvent_3003ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getStartEvent_3005ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getEndEvent_3006ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getExclusiveGateway_3007ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getParallelGateway_3008ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getEndEvent_3009ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getEndEvent_3010ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getIntermediateCatchEvent_3011ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getIntermediateThrowEvent_3012ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getIntermediateCatchEvent_3013ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getDataObject_3014ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getTextAnnotation_3015ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getScriptTask_3016ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getSequenceFlow_4001ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getAssociation_4002ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getUserTask_2001IncomingLinks(
			View view) {
		UserTask modelElement = (UserTask) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getServiceTask_2002IncomingLinks(
			View view) {
		ServiceTask modelElement = (ServiceTask) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getStartEvent_2003IncomingLinks(
			View view) {
		StartEvent modelElement = (StartEvent) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getStartEvent_2007IncomingLinks(
			View view) {
		StartEvent modelElement = (StartEvent) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getEndEvent_2004IncomingLinks(
			View view) {
		EndEvent modelElement = (EndEvent) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getExclusiveGateway_2005IncomingLinks(
			View view) {
		ExclusiveGateway modelElement = (ExclusiveGateway) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getParallelGateway_2006IncomingLinks(
			View view) {
		ParallelGateway modelElement = (ParallelGateway) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getEndEvent_2008IncomingLinks(
			View view) {
		EndEvent modelElement = (EndEvent) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getEndEvent_2009IncomingLinks(
			View view) {
		EndEvent modelElement = (EndEvent) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getIntermediateCatchEvent_2010IncomingLinks(
			View view) {
		IntermediateCatchEvent modelElement = (IntermediateCatchEvent) view
				.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getIntermediateThrowEvent_2011IncomingLinks(
			View view) {
		IntermediateThrowEvent modelElement = (IntermediateThrowEvent) view
				.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getIntermediateCatchEvent_2012IncomingLinks(
			View view) {
		IntermediateCatchEvent modelElement = (IntermediateCatchEvent) view
				.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getIntermediateCatchEvent_2013IncomingLinks(
			View view) {
		IntermediateCatchEvent modelElement = (IntermediateCatchEvent) view
				.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getDataObject_2014IncomingLinks(
			View view) {
		DataObject modelElement = (DataObject) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getTextAnnotation_2015IncomingLinks(
			View view) {
		TextAnnotation modelElement = (TextAnnotation) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getSubProcess_2016IncomingLinks(
			View view) {
		SubProcess modelElement = (SubProcess) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getScriptTask_2017IncomingLinks(
			View view) {
		ScriptTask modelElement = (ScriptTask) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getSubProcess_3001IncomingLinks(
			View view) {
		SubProcess modelElement = (SubProcess) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getUserTask_3002IncomingLinks(
			View view) {
		UserTask modelElement = (UserTask) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getServiceTask_3004IncomingLinks(
			View view) {
		ServiceTask modelElement = (ServiceTask) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getStartEvent_3003IncomingLinks(
			View view) {
		StartEvent modelElement = (StartEvent) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getStartEvent_3005IncomingLinks(
			View view) {
		StartEvent modelElement = (StartEvent) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getEndEvent_3006IncomingLinks(
			View view) {
		EndEvent modelElement = (EndEvent) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getExclusiveGateway_3007IncomingLinks(
			View view) {
		ExclusiveGateway modelElement = (ExclusiveGateway) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getParallelGateway_3008IncomingLinks(
			View view) {
		ParallelGateway modelElement = (ParallelGateway) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getEndEvent_3009IncomingLinks(
			View view) {
		EndEvent modelElement = (EndEvent) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getEndEvent_3010IncomingLinks(
			View view) {
		EndEvent modelElement = (EndEvent) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getIntermediateCatchEvent_3011IncomingLinks(
			View view) {
		IntermediateCatchEvent modelElement = (IntermediateCatchEvent) view
				.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getIntermediateThrowEvent_3012IncomingLinks(
			View view) {
		IntermediateThrowEvent modelElement = (IntermediateThrowEvent) view
				.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getIntermediateCatchEvent_3013IncomingLinks(
			View view) {
		IntermediateCatchEvent modelElement = (IntermediateCatchEvent) view
				.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getDataObject_3014IncomingLinks(
			View view) {
		DataObject modelElement = (DataObject) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getTextAnnotation_3015IncomingLinks(
			View view) {
		TextAnnotation modelElement = (TextAnnotation) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getScriptTask_3016IncomingLinks(
			View view) {
		ScriptTask modelElement = (ScriptTask) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_SequenceFlow_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getSequenceFlow_4001IncomingLinks(
			View view) {
		SequenceFlow modelElement = (SequenceFlow) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getAssociation_4002IncomingLinks(
			View view) {
		Association modelElement = (Association) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_Association_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getUserTask_2001OutgoingLinks(
			View view) {
		UserTask modelElement = (UserTask) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getServiceTask_2002OutgoingLinks(
			View view) {
		ServiceTask modelElement = (ServiceTask) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getStartEvent_2003OutgoingLinks(
			View view) {
		StartEvent modelElement = (StartEvent) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getStartEvent_2007OutgoingLinks(
			View view) {
		StartEvent modelElement = (StartEvent) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getEndEvent_2004OutgoingLinks(
			View view) {
		EndEvent modelElement = (EndEvent) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getExclusiveGateway_2005OutgoingLinks(
			View view) {
		ExclusiveGateway modelElement = (ExclusiveGateway) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getParallelGateway_2006OutgoingLinks(
			View view) {
		ParallelGateway modelElement = (ParallelGateway) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getEndEvent_2008OutgoingLinks(
			View view) {
		EndEvent modelElement = (EndEvent) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getEndEvent_2009OutgoingLinks(
			View view) {
		EndEvent modelElement = (EndEvent) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getIntermediateCatchEvent_2010OutgoingLinks(
			View view) {
		IntermediateCatchEvent modelElement = (IntermediateCatchEvent) view
				.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getIntermediateThrowEvent_2011OutgoingLinks(
			View view) {
		IntermediateThrowEvent modelElement = (IntermediateThrowEvent) view
				.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getIntermediateCatchEvent_2012OutgoingLinks(
			View view) {
		IntermediateCatchEvent modelElement = (IntermediateCatchEvent) view
				.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getIntermediateCatchEvent_2013OutgoingLinks(
			View view) {
		IntermediateCatchEvent modelElement = (IntermediateCatchEvent) view
				.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getDataObject_2014OutgoingLinks(
			View view) {
		DataObject modelElement = (DataObject) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getTextAnnotation_2015OutgoingLinks(
			View view) {
		TextAnnotation modelElement = (TextAnnotation) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getSubProcess_2016OutgoingLinks(
			View view) {
		SubProcess modelElement = (SubProcess) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getScriptTask_2017OutgoingLinks(
			View view) {
		ScriptTask modelElement = (ScriptTask) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getSubProcess_3001OutgoingLinks(
			View view) {
		SubProcess modelElement = (SubProcess) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getUserTask_3002OutgoingLinks(
			View view) {
		UserTask modelElement = (UserTask) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getServiceTask_3004OutgoingLinks(
			View view) {
		ServiceTask modelElement = (ServiceTask) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getStartEvent_3003OutgoingLinks(
			View view) {
		StartEvent modelElement = (StartEvent) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getStartEvent_3005OutgoingLinks(
			View view) {
		StartEvent modelElement = (StartEvent) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getEndEvent_3006OutgoingLinks(
			View view) {
		EndEvent modelElement = (EndEvent) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getExclusiveGateway_3007OutgoingLinks(
			View view) {
		ExclusiveGateway modelElement = (ExclusiveGateway) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getParallelGateway_3008OutgoingLinks(
			View view) {
		ParallelGateway modelElement = (ParallelGateway) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getEndEvent_3009OutgoingLinks(
			View view) {
		EndEvent modelElement = (EndEvent) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getEndEvent_3010OutgoingLinks(
			View view) {
		EndEvent modelElement = (EndEvent) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getIntermediateCatchEvent_3011OutgoingLinks(
			View view) {
		IntermediateCatchEvent modelElement = (IntermediateCatchEvent) view
				.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getIntermediateThrowEvent_3012OutgoingLinks(
			View view) {
		IntermediateThrowEvent modelElement = (IntermediateThrowEvent) view
				.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getIntermediateCatchEvent_3013OutgoingLinks(
			View view) {
		IntermediateCatchEvent modelElement = (IntermediateCatchEvent) view
				.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getDataObject_3014OutgoingLinks(
			View view) {
		DataObject modelElement = (DataObject) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getTextAnnotation_3015OutgoingLinks(
			View view) {
		TextAnnotation modelElement = (TextAnnotation) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getScriptTask_3016OutgoingLinks(
			View view) {
		ScriptTask modelElement = (ScriptTask) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_SequenceFlow_4001(modelElement));
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getSequenceFlow_4001OutgoingLinks(
			View view) {
		SequenceFlow modelElement = (SequenceFlow) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<Bpmn2LinkDescriptor> getAssociation_4002OutgoingLinks(
			View view) {
		Association modelElement = (Association) view.getElement();
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		result.addAll(getOutgoingTypeModelFacetLinks_Association_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection<Bpmn2LinkDescriptor> getContainedTypeModelFacetLinks_SequenceFlow_4001(
			FlowElementsContainer container) {
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		for (Iterator<?> links = container.getFlowElements().iterator(); links
				.hasNext();) {
			EObject linkObject = (EObject) links.next();
			if (false == linkObject instanceof SequenceFlow) {
				continue;
			}
			SequenceFlow link = (SequenceFlow) linkObject;
			if (SequenceFlowEditPart.VISUAL_ID != Bpmn2VisualIDRegistry
					.getLinkWithClassVisualID(link)) {
				continue;
			}
			FlowNode dst = link.getTargetRef();
			FlowNode src = link.getSourceRef();
			result.add(new Bpmn2LinkDescriptor(src, dst, link,
					Bpmn2ElementTypes.SequenceFlow_4001,
					SequenceFlowEditPart.VISUAL_ID));
		}
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection<Bpmn2LinkDescriptor> getContainedTypeModelFacetLinks_Association_4002(
			Process container) {
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		for (Iterator<?> links = container.getArtifacts().iterator(); links
				.hasNext();) {
			EObject linkObject = (EObject) links.next();
			if (false == linkObject instanceof Association) {
				continue;
			}
			Association link = (Association) linkObject;
			if (AssociationEditPart.VISUAL_ID != Bpmn2VisualIDRegistry
					.getLinkWithClassVisualID(link)) {
				continue;
			}
			BaseElement dst = link.getTargetRef();
			BaseElement src = link.getSourceRef();
			result.add(new Bpmn2LinkDescriptor(src, dst, link,
					Bpmn2ElementTypes.Association_4002,
					AssociationEditPart.VISUAL_ID));
		}
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection<Bpmn2LinkDescriptor> getIncomingTypeModelFacetLinks_SequenceFlow_4001(
			FlowNode target,
			Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences) {
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		Collection<EStructuralFeature.Setting> settings = crossReferences
				.get(target);
		for (EStructuralFeature.Setting setting : settings) {
			if (setting.getEStructuralFeature() != Bpmn2Package.eINSTANCE
					.getSequenceFlow_TargetRef()
					|| false == setting.getEObject() instanceof SequenceFlow) {
				continue;
			}
			SequenceFlow link = (SequenceFlow) setting.getEObject();
			if (SequenceFlowEditPart.VISUAL_ID != Bpmn2VisualIDRegistry
					.getLinkWithClassVisualID(link)) {
				continue;
			}
			FlowNode src = link.getSourceRef();
			result.add(new Bpmn2LinkDescriptor(src, target, link,
					Bpmn2ElementTypes.SequenceFlow_4001,
					SequenceFlowEditPart.VISUAL_ID));
		}
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection<Bpmn2LinkDescriptor> getIncomingTypeModelFacetLinks_Association_4002(
			BaseElement target,
			Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences) {
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		Collection<EStructuralFeature.Setting> settings = crossReferences
				.get(target);
		for (EStructuralFeature.Setting setting : settings) {
			if (setting.getEStructuralFeature() != Bpmn2Package.eINSTANCE
					.getAssociation_TargetRef()
					|| false == setting.getEObject() instanceof Association) {
				continue;
			}
			Association link = (Association) setting.getEObject();
			if (AssociationEditPart.VISUAL_ID != Bpmn2VisualIDRegistry
					.getLinkWithClassVisualID(link)) {
				continue;
			}
			BaseElement src = link.getSourceRef();
			result.add(new Bpmn2LinkDescriptor(src, target, link,
					Bpmn2ElementTypes.Association_4002,
					AssociationEditPart.VISUAL_ID));
		}
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection<Bpmn2LinkDescriptor> getOutgoingTypeModelFacetLinks_SequenceFlow_4001(
			FlowNode source) {
		FlowElementsContainer container = null;
		// Find container element for the link.
		// Climb up by containment hierarchy starting from the source
		// and return the first element that is instance of the container class.
		for (EObject element = source; element != null && container == null; element = element
				.eContainer()) {
			if (element instanceof FlowElementsContainer) {
				container = (FlowElementsContainer) element;
			}
		}
		if (container == null) {
			return Collections.emptyList();
		}
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		for (Iterator<?> links = container.getFlowElements().iterator(); links
				.hasNext();) {
			EObject linkObject = (EObject) links.next();
			if (false == linkObject instanceof SequenceFlow) {
				continue;
			}
			SequenceFlow link = (SequenceFlow) linkObject;
			if (SequenceFlowEditPart.VISUAL_ID != Bpmn2VisualIDRegistry
					.getLinkWithClassVisualID(link)) {
				continue;
			}
			FlowNode dst = link.getTargetRef();
			FlowNode src = link.getSourceRef();
			if (src != source) {
				continue;
			}
			result.add(new Bpmn2LinkDescriptor(src, dst, link,
					Bpmn2ElementTypes.SequenceFlow_4001,
					SequenceFlowEditPart.VISUAL_ID));
		}
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection<Bpmn2LinkDescriptor> getOutgoingTypeModelFacetLinks_Association_4002(
			BaseElement source) {
		Process container = null;
		// Find container element for the link.
		// Climb up by containment hierarchy starting from the source
		// and return the first element that is instance of the container class.
		for (EObject element = source; element != null && container == null; element = element
				.eContainer()) {
			if (element instanceof Process) {
				container = (Process) element;
			}
		}
		if (container == null) {
			return Collections.emptyList();
		}
		LinkedList<Bpmn2LinkDescriptor> result = new LinkedList<Bpmn2LinkDescriptor>();
		for (Iterator<?> links = container.getArtifacts().iterator(); links
				.hasNext();) {
			EObject linkObject = (EObject) links.next();
			if (false == linkObject instanceof Association) {
				continue;
			}
			Association link = (Association) linkObject;
			if (AssociationEditPart.VISUAL_ID != Bpmn2VisualIDRegistry
					.getLinkWithClassVisualID(link)) {
				continue;
			}
			BaseElement dst = link.getTargetRef();
			BaseElement src = link.getSourceRef();
			if (src != source) {
				continue;
			}
			result.add(new Bpmn2LinkDescriptor(src, dst, link,
					Bpmn2ElementTypes.Association_4002,
					AssociationEditPart.VISUAL_ID));
		}
		return result;
	}

}
