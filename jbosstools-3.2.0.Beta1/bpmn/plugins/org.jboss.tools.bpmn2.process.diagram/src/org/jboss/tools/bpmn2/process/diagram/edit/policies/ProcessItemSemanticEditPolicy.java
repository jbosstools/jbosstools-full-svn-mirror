package org.jboss.tools.bpmn2.process.diagram.edit.policies;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.commands.core.commands.DuplicateEObjectsCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;
import org.jboss.tools.bpmn2.process.diagram.edit.commands.DataObjectCreateCommand;
import org.jboss.tools.bpmn2.process.diagram.edit.commands.EndEvent2CreateCommand;
import org.jboss.tools.bpmn2.process.diagram.edit.commands.EndEvent3CreateCommand;
import org.jboss.tools.bpmn2.process.diagram.edit.commands.EndEventCreateCommand;
import org.jboss.tools.bpmn2.process.diagram.edit.commands.ExclusiveGatewayCreateCommand;
import org.jboss.tools.bpmn2.process.diagram.edit.commands.IntermediateCatchEvent2CreateCommand;
import org.jboss.tools.bpmn2.process.diagram.edit.commands.IntermediateCatchEvent3CreateCommand;
import org.jboss.tools.bpmn2.process.diagram.edit.commands.IntermediateCatchEventCreateCommand;
import org.jboss.tools.bpmn2.process.diagram.edit.commands.IntermediateThrowEventCreateCommand;
import org.jboss.tools.bpmn2.process.diagram.edit.commands.ParallelGatewayCreateCommand;
import org.jboss.tools.bpmn2.process.diagram.edit.commands.ServiceTaskCreateCommand;
import org.jboss.tools.bpmn2.process.diagram.edit.commands.StartEvent2CreateCommand;
import org.jboss.tools.bpmn2.process.diagram.edit.commands.StartEventCreateCommand;
import org.jboss.tools.bpmn2.process.diagram.edit.commands.SubProcessCreateCommand;
import org.jboss.tools.bpmn2.process.diagram.edit.commands.TextAnnotationCreateCommand;
import org.jboss.tools.bpmn2.process.diagram.edit.commands.UserTaskCreateCommand;
import org.jboss.tools.bpmn2.process.diagram.providers.Bpmn2ElementTypes;

/**
 * @generated
 */
public class ProcessItemSemanticEditPolicy extends
		Bpmn2BaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	public ProcessItemSemanticEditPolicy() {
		super(Bpmn2ElementTypes.Process_1000);
	}

	/**
	 * @generated
	 */
	protected Command getCreateCommand(CreateElementRequest req) {
		if (Bpmn2ElementTypes.UserTask_2001 == req.getElementType()) {
			return getGEFWrapper(new UserTaskCreateCommand(req));
		}
		if (Bpmn2ElementTypes.ServiceTask_2002 == req.getElementType()) {
			return getGEFWrapper(new ServiceTaskCreateCommand(req));
		}
		if (Bpmn2ElementTypes.StartEvent_2003 == req.getElementType()) {
			return getGEFWrapper(new StartEventCreateCommand(req));
		}
		if (Bpmn2ElementTypes.StartEvent_2007 == req.getElementType()) {
			return getGEFWrapper(new StartEvent2CreateCommand(req));
		}
		if (Bpmn2ElementTypes.EndEvent_2004 == req.getElementType()) {
			return getGEFWrapper(new EndEventCreateCommand(req));
		}
		if (Bpmn2ElementTypes.ExclusiveGateway_2005 == req.getElementType()) {
			return getGEFWrapper(new ExclusiveGatewayCreateCommand(req));
		}
		if (Bpmn2ElementTypes.ParallelGateway_2006 == req.getElementType()) {
			return getGEFWrapper(new ParallelGatewayCreateCommand(req));
		}
		if (Bpmn2ElementTypes.EndEvent_2008 == req.getElementType()) {
			return getGEFWrapper(new EndEvent2CreateCommand(req));
		}
		if (Bpmn2ElementTypes.EndEvent_2009 == req.getElementType()) {
			return getGEFWrapper(new EndEvent3CreateCommand(req));
		}
		if (Bpmn2ElementTypes.IntermediateCatchEvent_2010 == req
				.getElementType()) {
			return getGEFWrapper(new IntermediateCatchEventCreateCommand(req));
		}
		if (Bpmn2ElementTypes.IntermediateThrowEvent_2011 == req
				.getElementType()) {
			return getGEFWrapper(new IntermediateThrowEventCreateCommand(req));
		}
		if (Bpmn2ElementTypes.IntermediateCatchEvent_2012 == req
				.getElementType()) {
			return getGEFWrapper(new IntermediateCatchEvent2CreateCommand(req));
		}
		if (Bpmn2ElementTypes.IntermediateCatchEvent_2013 == req
				.getElementType()) {
			return getGEFWrapper(new IntermediateCatchEvent3CreateCommand(req));
		}
		if (Bpmn2ElementTypes.DataObject_2014 == req.getElementType()) {
			return getGEFWrapper(new DataObjectCreateCommand(req));
		}
		if (Bpmn2ElementTypes.TextAnnotation_2015 == req.getElementType()) {
			return getGEFWrapper(new TextAnnotationCreateCommand(req));
		}
		if (Bpmn2ElementTypes.SubProcess_2016 == req.getElementType()) {
			return getGEFWrapper(new SubProcessCreateCommand(req));
		}
		return super.getCreateCommand(req);
	}

	/**
	 * @generated
	 */
	protected Command getDuplicateCommand(DuplicateElementsRequest req) {
		TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
				.getEditingDomain();
		return getGEFWrapper(new DuplicateAnythingCommand(editingDomain, req));
	}

	/**
	 * @generated
	 */
	private static class DuplicateAnythingCommand extends
			DuplicateEObjectsCommand {

		/**
		 * @generated
		 */
		public DuplicateAnythingCommand(
				TransactionalEditingDomain editingDomain,
				DuplicateElementsRequest req) {
			super(editingDomain, req.getLabel(), req
					.getElementsToBeDuplicated(), req
					.getAllDuplicatedElementsMap());
		}

	}

}
