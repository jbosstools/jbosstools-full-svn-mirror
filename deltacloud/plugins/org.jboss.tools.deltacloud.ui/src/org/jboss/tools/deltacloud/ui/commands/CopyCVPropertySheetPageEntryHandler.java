package org.jboss.tools.deltacloud.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.deltacloud.ui.views.cloud.property.CVPropertySheetPage.PropertySourceContentProvider.CVPropertySheetPageEntry;

/**
 * A handler for the copy command (org.eclipse.ui.edit.copy) that copies entries
 * in the CVPropertySheetPage to the clipboard.
 * 
 * @author André Dietisheim
 * 
 * @see CVPropertySheetPage
 * @see CVPropertySheetPageEntry
 */
public class CopyCVPropertySheetPageEntryHandler extends AbstractHandler {

	private Clipboard clipboard;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			Object selectedObject = ((IStructuredSelection) selection).getFirstElement();
			if (selectedObject instanceof CVPropertySheetPageEntry) {
				CVPropertySheetPageEntry entry = (CVPropertySheetPageEntry) selectedObject;
				Shell shell = HandlerUtil.getActiveShell(event);
				if (shell == null) {
					return null;
				}
				Clipboard clipboard = getClipboard(shell.getDisplay());
				clipboard.setContents(
						new Object[] { entry.getStringValue() },
						new Transfer[] { TextTransfer.getInstance() });
				return Status.OK_STATUS;
			}
		}
		return new Status(IStatus.ERROR, Activator.PLUGIN_ID,
				"The element to copy is not of an entry of the deltacloud property view");
	}

	private Clipboard getClipboard(Display display) {
		if (clipboard == null) {
			this.clipboard = new Clipboard(display);
		}
		return clipboard;
	}
}
