package org.jboss.ide.eclipse.jdt.aop.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Shell;
import org.jboss.aop.AspectManager;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.AopDescriptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.AOPType.Typedef;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModel;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTTypedefExpression;
import org.jboss.ide.eclipse.jdt.aop.core.util.JaxbAopUtil;
import org.jboss.ide.eclipse.jdt.aop.ui.dialogs.TypedefPreviewDialog;

public class CreateNewNamedTypedefAction extends Action {
	private Viewer viewer;
	private Shell shell;
	
	public CreateNewNamedTypedefAction(Viewer viewer, Shell shell) {
		this.viewer = viewer;
		this.shell = shell;
	}
		
	
	
	public void run() {
		TypedefPreviewDialog dialog = new TypedefPreviewDialog(shell);
		int response = dialog.open();
		if( response == Dialog.OK) {
			try {
				JDTTypedefExpression expression = dialog.getTypedefExpression();
				// TODO: We eventually wanna move this to a model function
				AspectManager.instance().addTypedef(expression);
				AopModel.instance();
				AopDescriptor desc = AopCorePlugin.getDefault().getDefaultDescriptor(AopCorePlugin.getCurrentJavaProject());
				Typedef jaxbTypeDef = JaxbAopUtil.instance().getFactory().createAOPTypeTypedef();
				jaxbTypeDef.setExpr(expression.getExpr());
				jaxbTypeDef.setName(expression.getName());
				desc.getAop().getTopLevelElements().add(jaxbTypeDef);
				desc.save();
			} catch( Exception e ) {
			}
		}
	}
}
