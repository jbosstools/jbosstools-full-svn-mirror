package org.jboss.ide.eclipse.jdt.aop.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Shell;
import org.jboss.aop.AspectManager;
import org.jboss.aop.pointcut.PointcutExpression;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.AopDescriptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Pointcut;
import org.jboss.ide.eclipse.jdt.aop.ui.dialogs.PointcutPreviewDialog;

/**
 * 
 * @author Rob Stryker
 */
public class CreateNewNamedPointcutAction extends Action {

	private Viewer viewer;
	private Shell shell;
	
	public CreateNewNamedPointcutAction(Viewer viewer, Shell shell) {
		this.viewer = viewer;
		this.shell = shell;
	}
		
	public void run() {
		PointcutPreviewDialog previewDialog = 
			new PointcutPreviewDialog(null, "", shell, 
				AopCorePlugin.getCurrentJavaProject(), 
				PointcutPreviewDialog.NAMED);
		
		int response = -1;
		
		response = previewDialog.open();
		if (response == Dialog.OK)
		{
			String pointcut = previewDialog.getExpression();
			String name = previewDialog.getName();
			
			AspectManager manager = AspectManager.instance();
			try {
				PointcutExpression expr = new PointcutExpression(name, pointcut);
				AopDescriptor descriptor = (AopDescriptor) viewer.getInput();

				manager.addPointcut(expr);
				descriptor.addPointcut(name, pointcut);
				descriptor.save();
			} catch( Exception e ) {
			}
		}

	}
}
