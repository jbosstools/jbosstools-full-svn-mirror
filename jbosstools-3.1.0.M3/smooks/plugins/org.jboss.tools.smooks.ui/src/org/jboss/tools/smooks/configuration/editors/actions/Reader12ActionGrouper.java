package org.jboss.tools.smooks.configuration.editors.actions;

import org.eclipse.jface.action.MenuManager;
import org.jboss.tools.smooks.model.csv12.CSV12Reader;
import org.jboss.tools.smooks.model.edi12.EDI12Reader;
import org.jboss.tools.smooks.model.json12.Json12Reader;

public class Reader12ActionGrouper extends AbstractSmooksActionGrouper {

	@Override
	protected boolean canAdd(Object value) {
		if (value instanceof CSV12Reader) {
			return true;
		}
		if (value instanceof EDI12Reader) {
			return true;
		}
		if (value instanceof Json12Reader) {
			return true;
		}
		return false;
	}

	public String getGroupName() {
		return "Reader v1.2";
	}

	@Override
	public void orderActions(MenuManager menuManager) {
//		IContributionItem[] items = menuManager.getItems();
//		for (int i = 0; i < items.length; i++) {
//			IContributionItem item = items[i];
//			if (item instanceof ActionContributionItem) {
//				IAction action = ((ActionContributionItem) item).getAction();
//				if (action instanceof AddSmooksResourceAction) {
//					AddSmooksResourceAction action1 = (AddSmooksResourceAction) action;
//					Object descriptor = action1.getDescriptor();
//					if (descriptor instanceof CommandParameter) {
//						CommandParameter parameter = (CommandParameter) descriptor;
//						if (parameter.getValue() != null) {
//							Object value = AdapterFactoryEditingDomain.unwrap(parameter.getValue());
//							if (value instanceof ReaderType) {
//								int index = items.length - 1;
//								menuManager.remove(item);
//								menuManager.insert(index, item);
//								return;
//							}
//						}
//					}
//				}
//			}
//		}
	}
	
	

}
