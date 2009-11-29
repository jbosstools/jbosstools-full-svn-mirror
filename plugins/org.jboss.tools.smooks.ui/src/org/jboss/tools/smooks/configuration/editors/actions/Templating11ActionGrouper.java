package org.jboss.tools.smooks.configuration.editors.actions;

import org.jboss.tools.smooks.model.freemarker.Freemarker;
import org.jboss.tools.smooks.model.xsl.Xsl;

public class Templating11ActionGrouper extends AbstractSmooksActionGrouper {

	@Override
	protected boolean canAdd(Object value) {
		if (value instanceof Freemarker) {
			return true;
		}
		if (value instanceof Xsl) {
			return true;
		}
		return false;
	}

	public String getGroupName() {
		return Messages.Templating11ActionGrouper_GroupName;
	}

}
