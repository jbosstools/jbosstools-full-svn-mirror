package org.jboss.tools.smooks.configuration.editors.actions;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.jboss.tools.smooks.configuration.actions.AddSmooksResourceAction;
import org.jboss.tools.smooks.model.csv.CsvReader;
import org.jboss.tools.smooks.model.edi.EDIReader;
import org.jboss.tools.smooks.model.json.JsonReader;
import org.jboss.tools.smooks.model.smooks.ReaderType;

public class Reader11ActionGrouper extends AbstractSmooksActionGrouper {

	@Override
	protected boolean canAdd(Object value) {
		if (value instanceof CsvReader) {
			return true;
		}
		if (value instanceof EDIReader) {
			return true;
		}
		if (value instanceof JsonReader) {
			return true;
		}
		if (value instanceof ReaderType) {
			return true;
		}
		return false;
	}

	public String getGroupName() {
		return "Reader v1.1";
	}

	@Override
	public void orderActions(MenuManager menuManager) {
		IContributionItem[] items = menuManager.getItems();
		for (int i = 0; i < items.length; i++) {
			IContributionItem item = items[i];
			if (item instanceof ActionContributionItem) {
				IAction action = ((ActionContributionItem) item).getAction();
				if (action instanceof AddSmooksResourceAction) {
					AddSmooksResourceAction action1 = (AddSmooksResourceAction) action;
					Object descriptor = action1.getDescriptor();
					if (descriptor instanceof CommandParameter) {
						CommandParameter parameter = (CommandParameter) descriptor;
						if (parameter.getValue() != null) {
							Object value = AdapterFactoryEditingDomain.unwrap(parameter.getValue());
							if (value instanceof ReaderType) {
								int index = items.length - 1;
								menuManager.remove(item);
								menuManager.insert(index, item);
								return;
							}
						}
					}
				}
			}
		}
	}
	
	

}
