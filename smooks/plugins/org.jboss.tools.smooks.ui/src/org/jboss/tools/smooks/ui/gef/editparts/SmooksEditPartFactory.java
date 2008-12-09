package org.jboss.tools.smooks.ui.gef.editparts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.jboss.tools.smooks.ui.gef.model.GraphRootModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.SourceModel;
import org.jboss.tools.smooks.ui.gef.model.TargetModel;

/**
 * 
 * @author Dart Peng
 * @Date Jul 30, 2008
 */
public class SmooksEditPartFactory implements EditPartFactory {

	public SmooksEditPartFactory() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart,
	 * java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part = getPartForElement(model);
		if (part != null)
			part.setModel(model);

		return part;
	}

	/**
	 * 
	 * @param modelElement
	 * @return
	 */
	private EditPart getPartForElement(Object modelElement) {

		if (modelElement instanceof LineConnectionModel) {
			return new StructuredDataConnectionEditPart();
		} else if (modelElement instanceof GraphRootModel) {
			return new RootModelEditPart();
		} else if (modelElement instanceof TargetModel) {
			return new TargetConnectionPointEditPart();
		} else if (modelElement instanceof SourceModel) {
			return new SourceConnectionPointEditPart();
		}

		throw new RuntimeException("Can't create part from model element: "
				+ ((modelElement != null) ? modelElement.getClass().getName()
						: "null"));
	}
}