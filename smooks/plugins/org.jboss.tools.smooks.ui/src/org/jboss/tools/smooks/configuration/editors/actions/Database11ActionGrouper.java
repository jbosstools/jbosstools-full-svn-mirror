package org.jboss.tools.smooks.configuration.editors.actions;

import org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector;

public class Database11ActionGrouper extends AbstractSmooksActionGrouper {

	@Override
	protected boolean canAdd(Object value) {
		if (value  instanceof ResultSetRowSelector) {
			return true;
		}
		return false;
	}

	public String getGroupName() {
		return "Database";
	}

}
