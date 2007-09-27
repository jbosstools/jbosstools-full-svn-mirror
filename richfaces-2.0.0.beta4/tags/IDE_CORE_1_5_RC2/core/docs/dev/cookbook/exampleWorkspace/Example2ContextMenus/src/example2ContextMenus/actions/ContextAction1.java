package example2ContextMenus.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class ContextAction1 implements IViewActionDelegate {

	public void run(IAction action) {
		MessageDialog.openInformation(
			new Shell(),
			"Example2 Action 1",
			"Hello, Eclipse world Action 1");
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

	public void dispose() {
	}

	public void init(IViewPart view) {
		// Used for viewerContributions.
	}
}
