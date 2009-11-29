package org.jboss.tools.smooks.configuration.editors.actions;

import org.jboss.tools.smooks.model.calc.Counter;

public class Calc11ActionGrouper extends AbstractSmooksActionGrouper {

	@Override
	protected boolean canAdd(Object value) {
		if (value   instanceof Counter) {
			return true;
		}
		return false;
	}

	public String getGroupName() {
		return Messages.Calc11ActionGrouper_GroupName;
	}

}
