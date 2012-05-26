package org.jboss.tools.openshift.express.internal.ui.action;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.wst.server.ui.internal.Messages;
import org.eclipse.wst.server.ui.internal.wizard.TaskWizard;
import org.eclipse.wst.server.ui.internal.wizard.WizardTaskUtil;
import org.eclipse.wst.server.ui.internal.wizard.fragment.ModifyModulesWizardFragment;
import org.eclipse.wst.server.ui.internal.wizard.fragment.NewServerWizardFragment;
import org.eclipse.wst.server.ui.internal.wizard.fragment.TasksWizardFragment;
import org.eclipse.wst.server.ui.wizard.WizardFragment;
import org.jboss.tools.openshift.express.internal.core.behaviour.ExpressServerUtils;
import org.jboss.tools.openshift.express.internal.core.console.UserModel;
import org.jboss.tools.openshift.express.internal.ui.OpenShiftUIActivator;
import org.jboss.tools.openshift.express.internal.ui.messages.OpenShiftExpressUIMessages;

import com.openshift.express.client.IApplication;
import com.openshift.express.client.IUser;

public class CreateServerAdapterAction extends AbstractAction {

	public CreateServerAdapterAction() {
		super(OpenShiftExpressUIMessages.CREATE_SERVER_ADAPTER_ACTION);
		setImageDescriptor(OpenShiftUIActivator.getDefault().createImageDescriptor("edit.gif"));
	}

	@Override
	public void run() {
		final ITreeSelection treeSelection = (ITreeSelection) selection;
		if (selection != null && selection instanceof ITreeSelection
				&& treeSelection.getFirstElement() instanceof IApplication) {
			final IApplication application = (IApplication) treeSelection.getFirstElement();
			IUser user = application.getUser();
			NewServerWizard w = new NewServerWizard(ExpressServerUtils.OPENSHIFT_SERVER_TYPE);
			w.getTaskModel().putObject(ExpressServerUtils.TASK_WIZARD_ATTR_USER, UserModel.getDefault().findUser(user.getRhlogin()));
			w.getTaskModel().putObject(ExpressServerUtils.TASK_WIZARD_ATTR_SELECTED_APP, application);
			WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), w);
			dialog.open();
		}
	}
	
	public class NewServerWizard extends TaskWizard implements INewWizard {
		public NewServerWizard(final String serverType) {
			super(Messages.wizNewServerWizardTitle, new WizardFragment() {
				protected void createChildFragments(List<WizardFragment> list) {
					list.add(new NewServerWizardFragment(null, serverType));
					
					list.add(WizardTaskUtil.TempSaveRuntimeFragment);
					list.add(WizardTaskUtil.TempSaveServerFragment);
					
					list.add(new ModifyModulesWizardFragment());
					list.add(new TasksWizardFragment());
					
					list.add(WizardTaskUtil.SaveRuntimeFragment);
					list.add(WizardTaskUtil.SaveServerFragment);
					list.add(WizardTaskUtil.SaveHostnameFragment);
				}
			});
		}

		public void init(IWorkbench newWorkbench, IStructuredSelection newSelection) {
			// do nothing
		}
	}
}
