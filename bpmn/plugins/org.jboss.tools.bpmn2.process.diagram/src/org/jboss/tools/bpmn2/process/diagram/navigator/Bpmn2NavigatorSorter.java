package org.jboss.tools.bpmn2.process.diagram.navigator;

import org.eclipse.jface.viewers.ViewerSorter;
import org.jboss.tools.bpmn2.process.diagram.part.Bpmn2VisualIDRegistry;

/**
 * @generated
 */
public class Bpmn2NavigatorSorter extends ViewerSorter {

	/**
	 * @generated
	 */
	private static final int GROUP_CATEGORY = 4004;

	/**
	 * @generated
	 */
	public int category(Object element) {
		if (element instanceof Bpmn2NavigatorItem) {
			Bpmn2NavigatorItem item = (Bpmn2NavigatorItem) element;
			return Bpmn2VisualIDRegistry.getVisualID(item.getView());
		}
		return GROUP_CATEGORY;
	}

}
