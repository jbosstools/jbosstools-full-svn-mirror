package org.jboss.tools.smooks.configuration.editors.actions;

import org.jboss.tools.smooks.model.jmsrouting12.JMS12Router;

public class FragmentRouting12ActionGrouper extends AbstractSmooksActionGrouper {

	@Override
	protected boolean canAdd(Object value) {
		if (value instanceof JMS12Router) {
			return true;
		}
		return false;
	}

	public String getGroupName() {
		return Messages.FragmentRouting12ActionGrouper_GroupName;
	}

}
