package org.jboss.tools.smooks.ui.gef.policy;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.jboss.tools.smooks.ui.gef.commands.ICommandMappingFactory;



/**
 * @author Dart
 *
 */
public class RootGraphicsXYLayoutEditPolicy extends XYLayoutEditPolicy {

	private ICommandMappingFactory mappingFactory = null;

	/**
	 * @return the mappingFactory
	 */
//	public ICommandMappingFactory getMappingFactory() {
//		if (mappingFactory == null)
//			mappingFactory = createMappingFactory();
//		return mappingFactory;
//	}

	/**
	 * @return the mappingFactory
	 */
//	public ICommandMappingFactory createMappingFactory() {
//		return new CommandMappingFactory();
//	}

	protected Command createAddCommand(EditPart child, Object constraint) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#
	 * createChangeConstraintCommand(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	@Override
	protected Command createChangeConstraintCommand(EditPart child,
			Object constraint) {
//		Rectangle con = (Rectangle) constraint;
//		Object model = child.getModel();
//		ChangeConstraintCommand command = null;
//		if (model instanceof IGraphicalModel) {
//			command = new ChangeConstraintCommand();
//			command.setConstraint(con);
//			command.setGraphicalModel((IGraphicalModel) model);
//		}
//		return command;
		return null;
	}

	@Override
	protected Command getResizeChildrenCommand(ChangeBoundsRequest request) {
		return super.getResizeChildrenCommand(request);
	}

	protected Command getCreateCommand(CreateRequest request) {
//		GraphicalEditPart rootEditPart = (GraphicalEditPart) this.getHost();
//		ICommandMappingFactory factory = getMappingFactory();
//		if (factory != null) {
//			return factory.createCreationStructuredModelCommand(request,
//					rootEditPart);
//		}
		return null;
	}

	protected Command getDeleteDependantCommand(Request request) {
		return null;
	}

	protected EditPolicy createChildEditPolicy(EditPart child) {
		return new NonResizableEditPolicy();
	}
}