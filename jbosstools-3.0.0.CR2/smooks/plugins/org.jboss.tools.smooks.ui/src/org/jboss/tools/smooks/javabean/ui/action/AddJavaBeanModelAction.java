/**
 * 
 */
package org.jboss.tools.smooks.javabean.ui.action;

import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.jboss.tools.smooks.javabean.model.JavaBeanList;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.javabean.ui.NewJavaBeanStrucutredDataWizard;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.SourceModel;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;
import org.jboss.tools.smooks.utils.UIUtils;

/**
 * @author Dart
 * 
 */
public class AddJavaBeanModelAction extends JavaBeanModelAction {
	@Override
	public void run() {
		super.run();
		SmooksConfigurationFileGenerateContext context = getSmooksContext();
		if(context == null) return;
		
		List elements = ((IStructuredSelection) selection).toList();
		for (Iterator iterator = elements.iterator(); iterator.hasNext();) {
			JavaBeanModel model = (JavaBeanModel) iterator.next();
			AbstractStructuredDataModel graphModel = UIUtils.findGraphModel(
					context.getGraphicalRootModel(), model);
			if(graphModel instanceof SourceModel){
				setEnabled(false);
				break;
			}
		}
		
		NewJavaBeanStrucutredDataWizard wizard = new NewJavaBeanStrucutredDataWizard();
		IProject project = context.getSmooksConfigFile().getProject();
		IJavaProject javaProject = null;
		if (project instanceof IJavaProject) {
			javaProject = (IJavaProject) project;
		} else {
			javaProject = JavaCore.create(project);
		}
		if(javaProject == null) return;
		
		wizard.setProject(javaProject);
		WizardDialog dialog = new WizardDialog(context.getShell(),wizard);
		if(dialog.open() == Dialog.OK){
			JavaBeanList list = wizard.getJavaBeanList();
			TreeViewer viewer = (TreeViewer) getViewer();
			Object input = viewer.getInput();
			if(input == null){
				if(viewer instanceof PropertyChangeListener){
					list.addNodePropetyChangeListener((PropertyChangeListener)viewer);
				}
				viewer.setInput(list);
			}else{
				if(input instanceof JavaBeanList){
					List children = list.getChildren();
					if(viewer instanceof PropertyChangeListener){
						((JavaBeanList)input).addNodePropetyChangeListener((PropertyChangeListener)viewer);
					}
					for (Iterator iterator = children.iterator(); iterator
							.hasNext();) {
						JavaBeanModel javaBean = (JavaBeanModel) iterator.next();
						((JavaBeanList)input).addJavaBean(javaBean);
					}
				}
			}
		}
	}
	

	@Override
	public void setSelection(ISelection selection) {
		super.setSelection(selection);
		setEnabled(true);
		SmooksConfigurationFileGenerateContext context = getSmooksContext();
		if(context == null){
			return;
		}
		List elements = ((IStructuredSelection) selection).toList();
		for (Iterator iterator = elements.iterator(); iterator.hasNext();) {
			JavaBeanModel model = (JavaBeanModel) iterator.next();
			AbstractStructuredDataModel graphModel = UIUtils.findGraphModel(
					context.getGraphicalRootModel(), model);
			if(graphModel instanceof SourceModel){
				setEnabled(false);
				break;
			}
		}
	}
}
