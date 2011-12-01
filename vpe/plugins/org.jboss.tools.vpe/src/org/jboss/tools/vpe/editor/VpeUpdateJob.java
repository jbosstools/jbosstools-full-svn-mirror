package org.jboss.tools.vpe.editor;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.progress.UIJob;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.toolbar.format.FormatControllerManager;
import org.jboss.tools.vpe.messages.VpeUIMessages;

public class VpeUpdateJob extends UIJob {
	
	protected boolean execState = false;
	
	protected VpeController vpeController;
	protected ActiveEditorSwitcher switcher;

	public VpeUpdateJob(VpeController vpeController, ActiveEditorSwitcher switcher) {
		super(VpeUIMessages.VPE_UPDATE_JOB_TITLE);
		this.vpeController = vpeController;
		this.switcher = switcher;
	}

	protected synchronized void setExecState(boolean execState) {
		this.execState = execState;
	}

	protected synchronized boolean isExecState() {
		return execState;
	}
	
	public void interrupt() {
		setExecState(false);
	}
	
	public void execute() {
		setExecState(true);
		schedule();
	}

	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		IStatus res = Status.OK_STATUS;
		final int totalWork = 10000;
		final int maxQueue4WholeRefresh = 30;
		monitor.beginTask(VpeUIMessages.VPE_UPDATE_JOB_TITLE, totalWork);
		final List<VpeEventBean> changeEvents = vpeController.getChangeEvents();
		while (changeEvents.size() > 0) {
			if (!isExecState()) {
				break;
			}
			if (changeEvents.size() > maxQueue4WholeRefresh) {
				changeEvents.clear();
				monitor.worked((int) (totalWork / 2));
				vpeController.reinitImpl();
				monitor.worked(totalWork);
				break;
			}
			monitor.worked((int) (totalWork / changeEvents.size()));
			VpeEventBean eventBean = changeEvents.remove(0);
			if (monitor.isCanceled()) {
				// Yahor Radtsevich: the following line is commented as fix of JBIDE-3758: VPE autorefresh is broken in
				// some cases. Now if the change events queue should be cleared, the user have to do it explicitly.
				// getChangeEvents().clear();
				res = Status.CANCEL_STATUS;
				break;
			}
			try {
				vpeController.notifyChangedInUiThread(eventBean);
			} catch (VpeDisposeException ex) {
				// JBIDE-675 we will get this exception if user
				// close editor,
				// when update visual editor job is running, we
				// shoud ignore this
				// exception
				break;
			} catch (NullPointerException ex) {
				if (switcher != null) {
					throw ex;
				} else {
					// class was disposed and exception result
					// of that we can't stop
					// refresh job in time, so we just ignore
					// this exception
				}
			} catch (RuntimeException ex) {
				VpePlugin.getPluginLog().logError(ex);
			}
		}
		if (isExecState() && res == Status.OK_STATUS) {
			// cause is to lock calls others events
			if (switcher != null && switcher.startActiveEditor(ActiveEditorSwitcher.ACTIVE_EDITOR_SOURCE)) {
				try {
					vpeController.sourceSelectionChanged();
					// https://jira.jboss.org/jira/browse/JBIDE-3619 VpeViewUpdateJob takes place after toolbar selection
					// have been updated. New nodes haven't been put into dom mapping thus toolbar becomes desabled.
					// Updating toolbar state here takes into account updated vpe nodes.
					final FormatControllerManager tfcm = vpeController.getToolbarFormatControllerManager();
					if (tfcm != null) {
						tfcm.selectionChanged();
					}
				} finally {
					switcher.stopActiveEditor();
				}
			}
		}
		monitor.done();
		return res;
	}

}