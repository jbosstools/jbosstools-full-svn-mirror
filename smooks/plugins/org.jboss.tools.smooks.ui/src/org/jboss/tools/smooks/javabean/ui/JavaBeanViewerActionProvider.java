/**
 * 
 */
package org.jboss.tools.smooks.javabean.ui;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.smooks.javabean.ui.action.AddJavaBeanModelAction;
import org.jboss.tools.smooks.javabean.ui.action.RemoveJavaBeanAction;
import org.jboss.tools.smooks.ui.popup.ISmooksAction;
import org.jboss.tools.smooks.ui.popup.IViewerActionsProvider;

/**
 * @author Dart
 *
 */
public class JavaBeanViewerActionProvider implements IViewerActionsProvider {
	private List<ISmooksAction> actionList = null;
	public List<ISmooksAction> getActionList() {
		if(actionList == null){
			actionList = new ArrayList<ISmooksAction>();
			actionList.add(createAddJavaBeanAction());
			actionList.add(createRemoveJavaBeanModelAction());
		}
		return actionList;
	}
	
	protected AddJavaBeanModelAction createAddJavaBeanAction(){
		AddJavaBeanModelAction action = new AddJavaBeanModelAction();
		action.setText("Add JavaBean");
		return action;
	}
	
	protected RemoveJavaBeanAction createRemoveJavaBeanModelAction(){
		RemoveJavaBeanAction action = new RemoveJavaBeanAction();
		action.setText("Remove JavaBean");
		return action;
	}

}
