package org.jboss.tools.smooks.configuration.editors.actions;

import org.jboss.tools.smooks.model.dbrouting.Executor;
import org.jboss.tools.smooks.model.esbrouting.RouteBean;
import org.jboss.tools.smooks.model.fileRouting.OutputStream;
import org.jboss.tools.smooks.model.jmsrouting.JmsRouter;
import org.jboss.tools.smooks.model.jmsrouting12.JMS12Router;

public class FragmentRouting11ActionGrouper extends AbstractSmooksActionGrouper {

	@Override
	protected boolean canAdd(Object value) {
		if (value instanceof JmsRouter) {
			return true;
		}
		if (value instanceof JMS12Router) {
			return true;
		}
		if (value instanceof OutputStream) {
			return true;
		}
		if (value instanceof Executor) {
			return true;
		}
		if (value instanceof RouteBean) {
			return true;
		}
		return false;
	}

	public String getGroupName() {
		return Messages.FragmentRouting11ActionGrouper_GroupName;
	}

}
