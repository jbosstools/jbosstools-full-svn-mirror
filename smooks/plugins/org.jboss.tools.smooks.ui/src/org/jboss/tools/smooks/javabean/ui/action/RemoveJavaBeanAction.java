/**
 * 
 */
package org.jboss.tools.smooks.javabean.ui.action;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.jboss.tools.smooks.javabean.model.JavaBeanList;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;
import org.jboss.tools.smooks.utils.UIUtils;

/**
 * @author Dart
 *
 */
public class RemoveJavaBeanAction extends JavaBeanModelAction {

	@Override
	public void run() {
		super.run();
		SmooksConfigurationFileGenerateContext context = getSmooksContext();
		if(context == null) return;
		List list = getJavaBeanModelList();
		TreeViewer viewer = (TreeViewer)getViewer();
		Object input = viewer.getInput();
		JavaBeanList beanList = null;
		if(input instanceof JavaBeanList){
			beanList = (JavaBeanList)input;
		}
		if(beanList==null) return;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			JavaBeanModel model = (JavaBeanModel) iterator.next();
			IConnectableModel graphModel = (IConnectableModel) UIUtils.findGraphModel(context.getGraphicalRootModel(), model);
			if(graphModel != null){
//				disConnectAllConnections(graphModel);
			}
			if(viewer instanceof PropertyChangeListener){
				model.cleanAllNodePropertyChangeListeners();
			}
			beanList.removeJavaBean(model);
		}
		super.run();
	}
	
	protected void disConnectAllConnections(IConnectableModel model){
		List temp = new ArrayList(model.getModelSourceConnections());
		for (Iterator iterator = temp.iterator(); iterator.hasNext();) {
			LineConnectionModel connection = (LineConnectionModel) iterator.next();
			connection.disConnect();
		}
		temp.clear();
		
		temp = new ArrayList(model.getModelTargetConnections());
		for (Iterator iterator = temp.iterator(); iterator.hasNext();) {
			LineConnectionModel object = (LineConnectionModel) iterator.next();
			object.disConnect();
		}
		temp.clear();
		temp = null;
	}

	@Override
	public void setSelection(ISelection selection) {
		super.setSelection(selection);
		setEnabled(true);
		List list = getJavaBeanModelList();
		if(list.size() <= 0){
			setEnabled(false);
			return;
		}
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			JavaBeanModel model = (JavaBeanModel) iterator.next();
			JavaBeanModel parent = model.getParent();
			if(parent != null && parent instanceof JavaBeanList){
				
			}else{
				setEnabled(false);
				return;
			}
		}
	}
	
}
