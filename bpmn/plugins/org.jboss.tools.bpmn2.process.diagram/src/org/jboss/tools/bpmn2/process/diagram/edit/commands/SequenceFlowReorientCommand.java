package org.jboss.tools.bpmn2.process.diagram.edit.commands;

import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import org.jboss.tools.bpmn2.process.diagram.edit.policies.Bpmn2BaseItemSemanticEditPolicy;

/**
 * @generated
 */
public class SequenceFlowReorientCommand extends EditElementCommand {

	/**
	 * @generated
	 */
	private final int reorientDirection;

	/**
	 * @generated
	 */
	private final EObject oldEnd;

	/**
	 * @generated
	 */
	private final EObject newEnd;

	/**
	 * @generated
	 */
	public SequenceFlowReorientCommand(ReorientRelationshipRequest request) {
		super(request.getLabel(), request.getRelationship(), request);
		reorientDirection = request.getDirection();
		oldEnd = request.getOldRelationshipEnd();
		newEnd = request.getNewRelationshipEnd();
	}

	/**
	 * @generated
	 */
	public boolean canExecute() {
		if (false == getElementToEdit() instanceof SequenceFlow) {
			return false;
		}
		if (reorientDirection == ReorientRelationshipRequest.REORIENT_SOURCE) {
			return canReorientSource();
		}
		if (reorientDirection == ReorientRelationshipRequest.REORIENT_TARGET) {
			return canReorientTarget();
		}
		return false;
	}

	/**
	 * @generated
	 */
	protected boolean canReorientSource() {
		if (!(oldEnd instanceof FlowNode && newEnd instanceof FlowNode)) {
			return false;
		}
		FlowNode target = getLink().getTargetRef();
		if (!(getLink().eContainer() instanceof FlowElementsContainer)) {
			return false;
		}
		FlowElementsContainer container = (FlowElementsContainer) getLink()
				.eContainer();
		return Bpmn2BaseItemSemanticEditPolicy.getLinkConstraints()
				.canExistSequenceFlow_4001(container, getLink(),
						getNewSource(), target);
	}

	/**
	 * @generated
	 */
	protected boolean canReorientTarget() {
		if (!(oldEnd instanceof FlowNode && newEnd instanceof FlowNode)) {
			return false;
		}
		FlowNode source = getLink().getSourceRef();
		if (!(getLink().eContainer() instanceof FlowElementsContainer)) {
			return false;
		}
		FlowElementsContainer container = (FlowElementsContainer) getLink()
				.eContainer();
		return Bpmn2BaseItemSemanticEditPolicy.getLinkConstraints()
				.canExistSequenceFlow_4001(container, getLink(), source,
						getNewTarget());
	}

	/**
	 * @generated
	 */
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
			IAdaptable info) throws ExecutionException {
		if (!canExecute()) {
			throw new ExecutionException(
					"Invalid arguments in reorient link command"); //$NON-NLS-1$
		}
		if (reorientDirection == ReorientRelationshipRequest.REORIENT_SOURCE) {
			return reorientSource();
		}
		if (reorientDirection == ReorientRelationshipRequest.REORIENT_TARGET) {
			return reorientTarget();
		}
		throw new IllegalStateException();
	}

	/**
	 * @generated
	 */
	protected CommandResult reorientSource() throws ExecutionException {
		getLink().setSourceRef(getNewSource());
		return CommandResult.newOKCommandResult(getLink());
	}

	/**
	 * @generated
	 */
	protected CommandResult reorientTarget() throws ExecutionException {
		getLink().setTargetRef(getNewTarget());
		return CommandResult.newOKCommandResult(getLink());
	}

	/**
	 * @generated
	 */
	protected SequenceFlow getLink() {
		return (SequenceFlow) getElementToEdit();
	}

	/**
	 * @generated
	 */
	protected FlowNode getOldSource() {
		return (FlowNode) oldEnd;
	}

	/**
	 * @generated
	 */
	protected FlowNode getNewSource() {
		return (FlowNode) newEnd;
	}

	/**
	 * @generated
	 */
	protected FlowNode getOldTarget() {
		return (FlowNode) oldEnd;
	}

	/**
	 * @generated
	 */
	protected FlowNode getNewTarget() {
		return (FlowNode) newEnd;
	}
}
