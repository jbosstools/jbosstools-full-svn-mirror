package org.jbpm.gd.pf.properties;

import org.eclipse.jface.viewers.IFilter;
import org.jbpm.gd.common.model.NamedElement;
import org.jbpm.gd.common.notation.AbstractNotationElement;
import org.jbpm.gd.common.part.NotationElementGraphicalEditPart;
import org.jbpm.gd.common.part.OutlineEditPart;

public class NamedElementFilter implements IFilter {

	public boolean select(Object toTest) {
		Object input = toTest;
        if (toTest instanceof NotationElementGraphicalEditPart) {
        	AbstractNotationElement notationElement = ((NotationElementGraphicalEditPart)toTest).getNotationElement();
        	input = notationElement.getSemanticElement();
        } else if (toTest instanceof OutlineEditPart) {
        	input = ((OutlineEditPart)toTest).getModel();
        }
		return input instanceof NamedElement;
	}

}
