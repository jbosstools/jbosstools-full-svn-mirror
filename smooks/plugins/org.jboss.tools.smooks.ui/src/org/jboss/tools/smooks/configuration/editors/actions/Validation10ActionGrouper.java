package org.jboss.tools.smooks.configuration.editors.actions;

import org.jboss.tools.smooks.model.rules10.RuleBasesType;
import org.jboss.tools.smooks.model.validation10.RuleType;

public class Validation10ActionGrouper extends AbstractSmooksActionGrouper {

	@Override
	protected boolean canAdd(Object value) {
		if (value   instanceof RuleType) {
			return true;
		}
		if (value   instanceof RuleBasesType) {
			return true;
		}
		return false;
	}

	public String getGroupName() {
		return Messages.Validation10ActionGrouper_GroupName;
	}

}
