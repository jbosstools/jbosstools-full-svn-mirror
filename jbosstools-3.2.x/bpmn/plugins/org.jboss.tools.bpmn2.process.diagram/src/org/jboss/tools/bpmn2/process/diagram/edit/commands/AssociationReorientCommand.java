package org.jboss.tools.bpmn2.process.diagram.edit.commands;

import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Process;
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
public class AssociationReorientCommand extends EditElementCommand {

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
	public AssociationReorientCommand(ReorientRelationshipRequest request) {
		super(request.getLabel(), request.getRelationship(), request);
		reorientDirection = request.getDirection();
		oldEnd = request.getOldRelationshipEnd();
		newEnd = request.getNewRelationshipEnd();
	}

	/**
	 * @generated
	 */
	public boolean canExecute() {
		if (false == getElementToEdit() instanceof Association) {
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
		if (!(oldEnd instanceof BaseElement && newEnd instanceof BaseElement)) {
			return false;
		}
		BaseElement target = getLink().getTargetRef();
		if (!(getLink().eContainer() instanceof Process)) {
			return false;
		}
		Process container = (Process) getLink().eContainer();
		return Bpmn2BaseItemSemanticEditPolicy.getLinkConstraints()
				.canExistAssociation_4002(container, getLink(), getNewSource(),
						target);
	}

	/**
	 * @generated
	 */
	protected boolean canReorientTarget() {
		if (!(oldEnd instanceof BaseElement && newEnd instanceof BaseElement)) {
			return false;
		}
		BaseElement source = getLink().getSourceRef();
		if (!(getLink().eContainer() instanceof Process)) {
			return false;
		}
		Process container = (Process) getLink().eContainer();
		return Bpmn2BaseItemSemanticEditPolicy.getLinkConstraints()
				.canExistAssociation_4002(container, getLink(), source,
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
	protected Association getLink() {
		return (Association) getElementToEdit();
	}

	/**
	 * @generated
	 */
	protected BaseElement getOldSource() {
		return (BaseElement) oldEnd;
	}

	/**
	 * @generated
	 */
	protected BaseElement getNewSource() {
		return (BaseElement) newEnd;
	}

	/**
	 * @generated
	 */
	protected BaseElement getOldTarget() {
		return (BaseElement) oldEnd;
	}

	/**
	 * @generated
	 */
	protected BaseElement getNewTarget() {
		return (BaseElement) newEnd;
	}
}
