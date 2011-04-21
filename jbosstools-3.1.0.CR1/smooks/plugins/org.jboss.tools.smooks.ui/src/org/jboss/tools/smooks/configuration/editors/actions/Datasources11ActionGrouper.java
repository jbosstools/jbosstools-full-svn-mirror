package org.jboss.tools.smooks.configuration.editors.actions;

import org.jboss.tools.smooks.model.datasource.DataSourceJndi;
import org.jboss.tools.smooks.model.datasource.Direct;

public class Datasources11ActionGrouper extends AbstractSmooksActionGrouper {

	@Override
	protected boolean canAdd(Object value) {
		if (value instanceof DataSourceJndi) {
			return true;
		}
		if (value instanceof Direct) {
			return true;
		}
		return false;
	}

	public String getGroupName() {
		return Messages.Datasources11ActionGrouper_GroupName;
	}

}
