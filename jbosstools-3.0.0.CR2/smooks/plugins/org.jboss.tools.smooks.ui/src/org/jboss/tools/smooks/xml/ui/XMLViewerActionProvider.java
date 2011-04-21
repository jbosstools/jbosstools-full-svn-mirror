/**
 * 
 */
package org.jboss.tools.smooks.xml.ui;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.smooks.ui.popup.ISmooksAction;
import org.jboss.tools.smooks.ui.popup.IViewerActionsProvider;
import org.jboss.tools.smooks.ui.popup.SmooksAction;

/**
 * @author Dart
 * 
 */
public class XMLViewerActionProvider implements IViewerActionsProvider {

	private List<ISmooksAction> actionList = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.ui.popup.IViewerActionsProvider#getActionList()
	 */
	public List<ISmooksAction> getActionList() {
		if (actionList == null) {
			actionList = new ArrayList<ISmooksAction>();
			actionList.add(createAddXMLRootNodeAction());
			actionList.add(createAddXMLNodeAction());
			actionList.add(createAddXMLPropertyAction());
			actionList.add(createRemoveXMLNodeAction());
		}
		return actionList;
	}

	private ISmooksAction createRemoveXMLNodeAction() {
		ISmooksAction removeAction = new RemoveXMLNodeAction();
		removeAction.setText("Delete Node");
		return removeAction;
	}

	private ISmooksAction createAddXMLRootNodeAction() {
		ISmooksAction addAction = new AddXMLRootNodeAction();
		addAction.setText("Add Root Node");
		return addAction;
	}

	private ISmooksAction createAddXMLNodeAction() {
		ISmooksAction addxml = new AddXMLChildNodeAction();
		addxml.setText("Add Node");
		return addxml;
	}

	private ISmooksAction createAddXMLPropertyAction() {
		ISmooksAction addProperty = new AddXMLPropertyAction();
		addProperty.setText("Add Property");
		return addProperty;
	}

}
