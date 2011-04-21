package org.jboss.tools.smooks.configuration.editors.actions;

import org.jboss.tools.smooks.model.groovy.Groovy;

public class Scripting11ActionGrouper extends AbstractSmooksActionGrouper {

	@Override
	protected boolean canAdd(Object value) {
		if (value instanceof Groovy) {
			return true;
		}
		return false;
	}

	public String getGroupName() {
		return Messages.Scripting11ActionGrouper_GroupName;
	}

}
