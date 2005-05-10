package org.jboss.ide.eclipse.jdt.aop.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.AopDescriptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Pointcut;
import org.jboss.ide.eclipse.jdt.aop.ui.dialogs.PointcutPreviewDialog;
import org.jboss.ide.eclipse.jdt.aop.ui.views.AspectManagerView;

/**
 * 
 * @author Rob Stryker
 */
public class ModifyNamedPointcutAction extends Action {

	private AspectManagerView viewer;
	private Shell shell;
	
	public ModifyNamedPointcutAction(AspectManagerView viewer, Shell shell) {
		this.viewer = viewer;
		this.shell = shell;
	}
		
	
	public void run() {
		Pointcut p = (Pointcut)viewer.getSelected();
		PointcutPreviewDialog previewDialog = 
			new PointcutPreviewDialog(p.getName(), p.getExpr(), shell, 
				AopCorePlugin.getCurrentJavaProject(), 
				PointcutPreviewDialog.NAMED);
		
		int response = -1;
		
		response = previewDialog.open();
		if (response == Dialog.OK) {
			p.setExpr(previewDialog.getExpression());
			AopDescriptor descriptor = (AopDescriptor) viewer.getTreeViewer().getInput();
			descriptor.save();
		}

	}
}
