package org.jboss.tools.flow.jpdl4.editpart;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.jboss.tools.flow.jpdl4.model.Process;

public class ProcessTreeRootEditPart extends AbstractTreeEditPart {

	public ProcessTreeRootEditPart(Process process) {
		super(process);
	}

	protected void createEditPolicies() {
	}

	protected List<Object> getModelChildren() {
		List<Object> result = new ArrayList<Object>();
		result.add(new ProcessNodeListTreeEditPart((Process)getModel()));
		return result;
	}

}
