package org.jboss.tools.bpmn2.process.diagram.navigator;

import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.IntermediateThrowEvent;
import org.eclipse.bpmn2.ParallelGateway;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserOptions;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITreePathLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.ViewerLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;
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
import org.jboss.tools.bpmn2.process.diagram.part.Bpmn2ProcessDiagramEditorPlugin;
import org.jboss.tools.bpmn2.process.diagram.part.Bpmn2VisualIDRegistry;
import org.jboss.tools.bpmn2.process.diagram.providers.Bpmn2ElementTypes;
import org.jboss.tools.bpmn2.process.diagram.providers.Bpmn2ParserProvider;

/**
 * @generated
 */
public class Bpmn2NavigatorLabelProvider extends LabelProvider implements
		ICommonLabelProvider, ITreePathLabelProvider {

	/**
	 * @generated
	 */
	static {
		Bpmn2ProcessDiagramEditorPlugin
				.getInstance()
				.getImageRegistry()
				.put("Navigator?UnknownElement", ImageDescriptor.getMissingImageDescriptor()); //$NON-NLS-1$
		Bpmn2ProcessDiagramEditorPlugin
				.getInstance()
				.getImageRegistry()
				.put("Navigator?ImageNotFound", ImageDescriptor.getMissingImageDescriptor()); //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	public void updateLabel(ViewerLabel label, TreePath elementPath) {
		Object element = elementPath.getLastSegment();
		if (element instanceof Bpmn2NavigatorItem
				&& !isOwnView(((Bpmn2NavigatorItem) element).getView())) {
			return;
		}
		label.setText(getText(element));
		label.setImage(getImage(element));
	}

	/**
	 * @generated
	 */
	public Image getImage(Object element) {
		if (element instanceof Bpmn2NavigatorGroup) {
			Bpmn2NavigatorGroup group = (Bpmn2NavigatorGroup) element;
			return Bpmn2ProcessDiagramEditorPlugin.getInstance()
					.getBundledImage(group.getIcon());
		}

		if (element instanceof Bpmn2NavigatorItem) {
			Bpmn2NavigatorItem navigatorItem = (Bpmn2NavigatorItem) element;
			if (!isOwnView(navigatorItem.getView())) {
				return super.getImage(element);
			}
			return getImage(navigatorItem.getView());
		}

		return super.getImage(element);
	}

	/**
	 * @generated
	 */
	public Image getImage(View view) {
		switch (Bpmn2VisualIDRegistry.getVisualID(view)) {
		case UserTask2EditPart.VISUAL_ID:
			return getImage(
					"Navigator?Node?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?UserTask", Bpmn2ElementTypes.UserTask_3002); //$NON-NLS-1$
		case EndEvent2EditPart.VISUAL_ID:
			return getImage(
					"Navigator?TopLevelNode?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?EndEvent", Bpmn2ElementTypes.EndEvent_2008); //$NON-NLS-1$
		case SequenceFlowEditPart.VISUAL_ID:
			return getImage(
					"Navigator?Link?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?SequenceFlow", Bpmn2ElementTypes.SequenceFlow_4001); //$NON-NLS-1$
		case IntermediateCatchEvent4EditPart.VISUAL_ID:
			return getImage(
					"Navigator?Node?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?IntermediateCatchEvent", Bpmn2ElementTypes.IntermediateCatchEvent_3011); //$NON-NLS-1$
		case AssociationEditPart.VISUAL_ID:
			return getImage(
					"Navigator?Link?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?Association", Bpmn2ElementTypes.Association_4002); //$NON-NLS-1$
		case IntermediateCatchEvent2EditPart.VISUAL_ID:
			return getImage(
					"Navigator?TopLevelNode?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?IntermediateCatchEvent", Bpmn2ElementTypes.IntermediateCatchEvent_2012); //$NON-NLS-1$
		case ScriptTaskEditPart.VISUAL_ID:
			return getImage(
					"Navigator?TopLevelNode?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?ScriptTask", Bpmn2ElementTypes.ScriptTask_2017); //$NON-NLS-1$
		case DataObject2EditPart.VISUAL_ID:
			return getImage(
					"Navigator?Node?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?DataObject", Bpmn2ElementTypes.DataObject_3014); //$NON-NLS-1$
		case IntermediateCatchEvent5EditPart.VISUAL_ID:
			return getImage(
					"Navigator?Node?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?IntermediateCatchEvent", Bpmn2ElementTypes.IntermediateCatchEvent_3013); //$NON-NLS-1$
		case StartEventEditPart.VISUAL_ID:
			return getImage(
					"Navigator?TopLevelNode?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?StartEvent", Bpmn2ElementTypes.StartEvent_2003); //$NON-NLS-1$
		case ProcessEditPart.VISUAL_ID:
			return getImage(
					"Navigator?Diagram?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?Process", Bpmn2ElementTypes.Process_1000); //$NON-NLS-1$
		case EndEvent3EditPart.VISUAL_ID:
			return getImage(
					"Navigator?TopLevelNode?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?EndEvent", Bpmn2ElementTypes.EndEvent_2009); //$NON-NLS-1$
		case IntermediateCatchEvent6EditPart.VISUAL_ID:
			return getImage(
					"Navigator?Node?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?IntermediateCatchEvent", Bpmn2ElementTypes.IntermediateCatchEvent_3018); //$NON-NLS-1$
		case SubProcessEditPart.VISUAL_ID:
			return getImage(
					"Navigator?TopLevelNode?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?SubProcess", Bpmn2ElementTypes.SubProcess_2016); //$NON-NLS-1$
		case EndEvent5EditPart.VISUAL_ID:
			return getImage(
					"Navigator?Node?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?EndEvent", Bpmn2ElementTypes.EndEvent_3009); //$NON-NLS-1$
		case StartEvent2EditPart.VISUAL_ID:
			return getImage(
					"Navigator?TopLevelNode?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?StartEvent", Bpmn2ElementTypes.StartEvent_2007); //$NON-NLS-1$
		case TextAnnotationEditPart.VISUAL_ID:
			return getImage(
					"Navigator?TopLevelNode?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?TextAnnotation", Bpmn2ElementTypes.TextAnnotation_2015); //$NON-NLS-1$
		case ScriptTask2EditPart.VISUAL_ID:
			return getImage(
					"Navigator?Node?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?ScriptTask", Bpmn2ElementTypes.ScriptTask_3016); //$NON-NLS-1$
		case IntermediateCatchEvent3EditPart.VISUAL_ID:
			return getImage(
					"Navigator?TopLevelNode?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?IntermediateCatchEvent", Bpmn2ElementTypes.IntermediateCatchEvent_2013); //$NON-NLS-1$
		case ExclusiveGatewayEditPart.VISUAL_ID:
			return getImage(
					"Navigator?TopLevelNode?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?ExclusiveGateway", Bpmn2ElementTypes.ExclusiveGateway_2005); //$NON-NLS-1$
		case EndEventEditPart.VISUAL_ID:
			return getImage(
					"Navigator?TopLevelNode?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?EndEvent", Bpmn2ElementTypes.EndEvent_2004); //$NON-NLS-1$
		case ParallelGateway2EditPart.VISUAL_ID:
			return getImage(
					"Navigator?Node?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?ParallelGateway", Bpmn2ElementTypes.ParallelGateway_3008); //$NON-NLS-1$
		case ExclusiveGateway2EditPart.VISUAL_ID:
			return getImage(
					"Navigator?Node?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?ExclusiveGateway", Bpmn2ElementTypes.ExclusiveGateway_3007); //$NON-NLS-1$
		case EndEvent4EditPart.VISUAL_ID:
			return getImage(
					"Navigator?Node?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?EndEvent", Bpmn2ElementTypes.EndEvent_3006); //$NON-NLS-1$
		case ParallelGatewayEditPart.VISUAL_ID:
			return getImage(
					"Navigator?TopLevelNode?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?ParallelGateway", Bpmn2ElementTypes.ParallelGateway_2006); //$NON-NLS-1$
		case IntermediateThrowEvent2EditPart.VISUAL_ID:
			return getImage(
					"Navigator?Node?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?IntermediateThrowEvent", Bpmn2ElementTypes.IntermediateThrowEvent_3012); //$NON-NLS-1$
		case StartEvent4EditPart.VISUAL_ID:
			return getImage(
					"Navigator?Node?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?StartEvent", Bpmn2ElementTypes.StartEvent_3005); //$NON-NLS-1$
		case DataObjectEditPart.VISUAL_ID:
			return getImage(
					"Navigator?TopLevelNode?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?DataObject", Bpmn2ElementTypes.DataObject_2014); //$NON-NLS-1$
		case IntermediateCatchEventEditPart.VISUAL_ID:
			return getImage(
					"Navigator?TopLevelNode?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?IntermediateCatchEvent", Bpmn2ElementTypes.IntermediateCatchEvent_2010); //$NON-NLS-1$
		case TextAnnotation2EditPart.VISUAL_ID:
			return getImage(
					"Navigator?Node?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?TextAnnotation", Bpmn2ElementTypes.TextAnnotation_3015); //$NON-NLS-1$
		case BusinessRuleTaskEditPart.VISUAL_ID:
			return getImage(
					"Navigator?TopLevelNode?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?BusinessRuleTask", Bpmn2ElementTypes.BusinessRuleTask_2018); //$NON-NLS-1$
		case BusinessRuleTask2EditPart.VISUAL_ID:
			return getImage(
					"Navigator?Node?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?BusinessRuleTask", Bpmn2ElementTypes.BusinessRuleTask_3017); //$NON-NLS-1$
		case UserTaskEditPart.VISUAL_ID:
			return getImage(
					"Navigator?TopLevelNode?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?UserTask", Bpmn2ElementTypes.UserTask_2001); //$NON-NLS-1$
		case ServiceTaskEditPart.VISUAL_ID:
			return getImage(
					"Navigator?TopLevelNode?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?ServiceTask", Bpmn2ElementTypes.ServiceTask_2002); //$NON-NLS-1$
		case EndEvent6EditPart.VISUAL_ID:
			return getImage(
					"Navigator?Node?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?EndEvent", Bpmn2ElementTypes.EndEvent_3010); //$NON-NLS-1$
		case IntermediateThrowEventEditPart.VISUAL_ID:
			return getImage(
					"Navigator?TopLevelNode?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?IntermediateThrowEvent", Bpmn2ElementTypes.IntermediateThrowEvent_2011); //$NON-NLS-1$
		case StartEvent3EditPart.VISUAL_ID:
			return getImage(
					"Navigator?Node?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?StartEvent", Bpmn2ElementTypes.StartEvent_3003); //$NON-NLS-1$
		case ServiceTask2EditPart.VISUAL_ID:
			return getImage(
					"Navigator?Node?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?ServiceTask", Bpmn2ElementTypes.ServiceTask_3004); //$NON-NLS-1$
		case SubProcess2EditPart.VISUAL_ID:
			return getImage(
					"Navigator?Node?http://www.omg.org/spec/BPMN/20100524/MODEL-XMI?SubProcess", Bpmn2ElementTypes.SubProcess_3001); //$NON-NLS-1$
		}
		return getImage("Navigator?UnknownElement", null); //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	private Image getImage(String key, IElementType elementType) {
		ImageRegistry imageRegistry = Bpmn2ProcessDiagramEditorPlugin
				.getInstance().getImageRegistry();
		Image image = imageRegistry.get(key);
		if (image == null && elementType != null
				&& Bpmn2ElementTypes.isKnownElementType(elementType)) {
			image = Bpmn2ElementTypes.getImage(elementType);
			imageRegistry.put(key, image);
		}

		if (image == null) {
			image = imageRegistry.get("Navigator?ImageNotFound"); //$NON-NLS-1$
			imageRegistry.put(key, image);
		}
		return image;
	}

	/**
	 * @generated
	 */
	public String getText(Object element) {
		if (element instanceof Bpmn2NavigatorGroup) {
			Bpmn2NavigatorGroup group = (Bpmn2NavigatorGroup) element;
			return group.getGroupName();
		}

		if (element instanceof Bpmn2NavigatorItem) {
			Bpmn2NavigatorItem navigatorItem = (Bpmn2NavigatorItem) element;
			if (!isOwnView(navigatorItem.getView())) {
				return null;
			}
			return getText(navigatorItem.getView());
		}

		return super.getText(element);
	}

	/**
	 * @generated
	 */
	public String getText(View view) {
		if (view.getElement() != null && view.getElement().eIsProxy()) {
			return getUnresolvedDomainElementProxyText(view);
		}
		switch (Bpmn2VisualIDRegistry.getVisualID(view)) {
		case UserTask2EditPart.VISUAL_ID:
			return getUserTask_3002Text(view);
		case EndEvent2EditPart.VISUAL_ID:
			return getEndEvent_2008Text(view);
		case SequenceFlowEditPart.VISUAL_ID:
			return getSequenceFlow_4001Text(view);
		case IntermediateCatchEvent4EditPart.VISUAL_ID:
			return getIntermediateCatchEvent_3011Text(view);
		case AssociationEditPart.VISUAL_ID:
			return getAssociation_4002Text(view);
		case IntermediateCatchEvent2EditPart.VISUAL_ID:
			return getIntermediateCatchEvent_2012Text(view);
		case ScriptTaskEditPart.VISUAL_ID:
			return getScriptTask_2017Text(view);
		case DataObject2EditPart.VISUAL_ID:
			return getDataObject_3014Text(view);
		case IntermediateCatchEvent5EditPart.VISUAL_ID:
			return getIntermediateCatchEvent_3013Text(view);
		case StartEventEditPart.VISUAL_ID:
			return getStartEvent_2003Text(view);
		case ProcessEditPart.VISUAL_ID:
			return getProcess_1000Text(view);
		case EndEvent3EditPart.VISUAL_ID:
			return getEndEvent_2009Text(view);
		case IntermediateCatchEvent6EditPart.VISUAL_ID:
			return getIntermediateCatchEvent_3018Text(view);
		case SubProcessEditPart.VISUAL_ID:
			return getSubProcess_2016Text(view);
		case EndEvent5EditPart.VISUAL_ID:
			return getEndEvent_3009Text(view);
		case StartEvent2EditPart.VISUAL_ID:
			return getStartEvent_2007Text(view);
		case TextAnnotationEditPart.VISUAL_ID:
			return getTextAnnotation_2015Text(view);
		case ScriptTask2EditPart.VISUAL_ID:
			return getScriptTask_3016Text(view);
		case IntermediateCatchEvent3EditPart.VISUAL_ID:
			return getIntermediateCatchEvent_2013Text(view);
		case ExclusiveGatewayEditPart.VISUAL_ID:
			return getExclusiveGateway_2005Text(view);
		case EndEventEditPart.VISUAL_ID:
			return getEndEvent_2004Text(view);
		case ParallelGateway2EditPart.VISUAL_ID:
			return getParallelGateway_3008Text(view);
		case ExclusiveGateway2EditPart.VISUAL_ID:
			return getExclusiveGateway_3007Text(view);
		case EndEvent4EditPart.VISUAL_ID:
			return getEndEvent_3006Text(view);
		case ParallelGatewayEditPart.VISUAL_ID:
			return getParallelGateway_2006Text(view);
		case IntermediateThrowEvent2EditPart.VISUAL_ID:
			return getIntermediateThrowEvent_3012Text(view);
		case StartEvent4EditPart.VISUAL_ID:
			return getStartEvent_3005Text(view);
		case DataObjectEditPart.VISUAL_ID:
			return getDataObject_2014Text(view);
		case IntermediateCatchEventEditPart.VISUAL_ID:
			return getIntermediateCatchEvent_2010Text(view);
		case TextAnnotation2EditPart.VISUAL_ID:
			return getTextAnnotation_3015Text(view);
		case BusinessRuleTaskEditPart.VISUAL_ID:
			return getBusinessRuleTask_2018Text(view);
		case BusinessRuleTask2EditPart.VISUAL_ID:
			return getBusinessRuleTask_3017Text(view);
		case UserTaskEditPart.VISUAL_ID:
			return getUserTask_2001Text(view);
		case ServiceTaskEditPart.VISUAL_ID:
			return getServiceTask_2002Text(view);
		case EndEvent6EditPart.VISUAL_ID:
			return getEndEvent_3010Text(view);
		case IntermediateThrowEventEditPart.VISUAL_ID:
			return getIntermediateThrowEvent_2011Text(view);
		case StartEvent3EditPart.VISUAL_ID:
			return getStartEvent_3003Text(view);
		case ServiceTask2EditPart.VISUAL_ID:
			return getServiceTask_3004Text(view);
		case SubProcess2EditPart.VISUAL_ID:
			return getSubProcess_3001Text(view);
		}
		return getUnknownElementText(view);
	}

	/**
	 * @generated
	 */
	private String getIntermediateCatchEvent_2010Text(View view) {
		IntermediateCatchEvent domainModelElement = (IntermediateCatchEvent) view
				.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 2010); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getScriptTask_2017Text(View view) {
		IParser parser = Bpmn2ParserProvider
				.getParser(Bpmn2ElementTypes.ScriptTask_2017,
						view.getElement() != null ? view.getElement() : view,
						Bpmn2VisualIDRegistry
								.getType(ScriptTaskNameEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 5011); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getEndEvent_2009Text(View view) {
		EndEvent domainModelElement = (EndEvent) view.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 2009); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getIntermediateCatchEvent_2012Text(View view) {
		IntermediateCatchEvent domainModelElement = (IntermediateCatchEvent) view
				.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 2012); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getDataObject_2014Text(View view) {
		IParser parser = Bpmn2ParserProvider
				.getParser(Bpmn2ElementTypes.DataObject_2014,
						view.getElement() != null ? view.getElement() : view,
						Bpmn2VisualIDRegistry
								.getType(DataObjectNameEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 5003); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getIntermediateCatchEvent_3011Text(View view) {
		IntermediateCatchEvent domainModelElement = (IntermediateCatchEvent) view
				.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 3011); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getStartEvent_2007Text(View view) {
		StartEvent domainModelElement = (StartEvent) view.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 2007); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getIntermediateCatchEvent_3013Text(View view) {
		IntermediateCatchEvent domainModelElement = (IntermediateCatchEvent) view
				.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 3013); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getParallelGateway_3008Text(View view) {
		ParallelGateway domainModelElement = (ParallelGateway) view
				.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 3008); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getParallelGateway_2006Text(View view) {
		ParallelGateway domainModelElement = (ParallelGateway) view
				.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 2006); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getIntermediateThrowEvent_3012Text(View view) {
		IntermediateThrowEvent domainModelElement = (IntermediateThrowEvent) view
				.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 3012); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getTextAnnotation_2015Text(View view) {
		IParser parser = Bpmn2ParserProvider.getParser(
				Bpmn2ElementTypes.TextAnnotation_2015,
				view.getElement() != null ? view.getElement() : view,
				Bpmn2VisualIDRegistry
						.getType(TextAnnotationTextEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 5004); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getExclusiveGateway_3007Text(View view) {
		ExclusiveGateway domainModelElement = (ExclusiveGateway) view
				.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 3007); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getIntermediateCatchEvent_3018Text(View view) {
		IntermediateCatchEvent domainModelElement = (IntermediateCatchEvent) view
				.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 3018); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getExclusiveGateway_2005Text(View view) {
		ExclusiveGateway domainModelElement = (ExclusiveGateway) view
				.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 2005); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getScriptTask_3016Text(View view) {
		IParser parser = Bpmn2ParserProvider.getParser(
				Bpmn2ElementTypes.ScriptTask_3016,
				view.getElement() != null ? view.getElement() : view,
				Bpmn2VisualIDRegistry
						.getType(ScriptTaskName2EditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 5014); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getProcess_1000Text(View view) {
		Process domainModelElement = (Process) view.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 1000); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getEndEvent_2008Text(View view) {
		EndEvent domainModelElement = (EndEvent) view.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 2008); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getBusinessRuleTask_2018Text(View view) {
		IParser parser = Bpmn2ParserProvider.getParser(
				Bpmn2ElementTypes.BusinessRuleTask_2018,
				view.getElement() != null ? view.getElement() : view,
				Bpmn2VisualIDRegistry
						.getType(BusinessRuleTaskNameEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 5013); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getIntermediateThrowEvent_2011Text(View view) {
		IntermediateThrowEvent domainModelElement = (IntermediateThrowEvent) view
				.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 2011); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getSubProcess_2016Text(View view) {
		SubProcess domainModelElement = (SubProcess) view.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 2016); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getEndEvent_3010Text(View view) {
		EndEvent domainModelElement = (EndEvent) view.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 3010); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getUserTask_2001Text(View view) {
		IParser parser = Bpmn2ParserProvider.getParser(
				Bpmn2ElementTypes.UserTask_2001,
				view.getElement() != null ? view.getElement() : view,
				Bpmn2VisualIDRegistry.getType(UserTaskNameEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 5009); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getDataObject_3014Text(View view) {
		IParser parser = Bpmn2ParserProvider.getParser(
				Bpmn2ElementTypes.DataObject_3014,
				view.getElement() != null ? view.getElement() : view,
				Bpmn2VisualIDRegistry
						.getType(DataObjectName2EditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 5007); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getEndEvent_2004Text(View view) {
		EndEvent domainModelElement = (EndEvent) view.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 2004); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getTextAnnotation_3015Text(View view) {
		IParser parser = Bpmn2ParserProvider.getParser(
				Bpmn2ElementTypes.TextAnnotation_3015,
				view.getElement() != null ? view.getElement() : view,
				Bpmn2VisualIDRegistry
						.getType(TextAnnotationText2EditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 5008); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getStartEvent_3003Text(View view) {
		StartEvent domainModelElement = (StartEvent) view.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 3003); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getServiceTask_3004Text(View view) {
		IParser parser = Bpmn2ParserProvider.getParser(
				Bpmn2ElementTypes.ServiceTask_3004,
				view.getElement() != null ? view.getElement() : view,
				Bpmn2VisualIDRegistry
						.getType(ServiceTaskName2EditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 5015); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getSubProcess_3001Text(View view) {
		SubProcess domainModelElement = (SubProcess) view.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 3001); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getStartEvent_3005Text(View view) {
		StartEvent domainModelElement = (StartEvent) view.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 3005); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getSequenceFlow_4001Text(View view) {
		SequenceFlow domainModelElement = (SequenceFlow) view.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 4001); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getStartEvent_2003Text(View view) {
		StartEvent domainModelElement = (StartEvent) view.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 2003); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getUserTask_3002Text(View view) {
		IParser parser = Bpmn2ParserProvider.getParser(
				Bpmn2ElementTypes.UserTask_3002,
				view.getElement() != null ? view.getElement() : view,
				Bpmn2VisualIDRegistry.getType(UserTaskName2EditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 5010); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getEndEvent_3009Text(View view) {
		EndEvent domainModelElement = (EndEvent) view.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 3009); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getBusinessRuleTask_3017Text(View view) {
		IParser parser = Bpmn2ParserProvider.getParser(
				Bpmn2ElementTypes.BusinessRuleTask_3017,
				view.getElement() != null ? view.getElement() : view,
				Bpmn2VisualIDRegistry
						.getType(BusinessRuleTaskName2EditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 5016); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getAssociation_4002Text(View view) {
		Association domainModelElement = (Association) view.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getId();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 4002); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getEndEvent_3006Text(View view) {
		EndEvent domainModelElement = (EndEvent) view.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 3006); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getServiceTask_2002Text(View view) {
		IParser parser = Bpmn2ParserProvider.getParser(
				Bpmn2ElementTypes.ServiceTask_2002,
				view.getElement() != null ? view.getElement() : view,
				Bpmn2VisualIDRegistry
						.getType(ServiceTaskNameEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 5012); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getIntermediateCatchEvent_2013Text(View view) {
		IntermediateCatchEvent domainModelElement = (IntermediateCatchEvent) view
				.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			Bpmn2ProcessDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 2013); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getUnknownElementText(View view) {
		return "<UnknownElement Visual_ID = " + view.getType() + ">"; //$NON-NLS-1$  //$NON-NLS-2$
	}

	/**
	 * @generated
	 */
	private String getUnresolvedDomainElementProxyText(View view) {
		return "<Unresolved domain element Visual_ID = " + view.getType() + ">"; //$NON-NLS-1$  //$NON-NLS-2$
	}

	/**
	 * @generated
	 */
	public void init(ICommonContentExtensionSite aConfig) {
	}

	/**
	 * @generated
	 */
	public void restoreState(IMemento aMemento) {
	}

	/**
	 * @generated
	 */
	public void saveState(IMemento aMemento) {
	}

	/**
	 * @generated
	 */
	public String getDescription(Object anElement) {
		return null;
	}

	/**
	 * @generated
	 */
	private boolean isOwnView(View view) {
		return ProcessEditPart.MODEL_ID.equals(Bpmn2VisualIDRegistry
				.getModelID(view));
	}

}
