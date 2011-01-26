package org.jboss.tools.deltacloud.ui.views.cloud.cnf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.core.job.InstanceActionJob;
import org.jboss.tools.deltacloud.core.job.RefreshCloudJob;
import org.jboss.tools.deltacloud.ui.commands.DeleteCloudHandler.DeleteCloudsDialog;
import org.jboss.tools.deltacloud.ui.commands.DeltaCloudInstanceDialog;
import org.jboss.tools.deltacloud.ui.views.CVMessages;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;
import org.jboss.tools.internal.deltacloud.ui.wizards.NewInstanceWizard;

public class CloudViewActionUtil {
	private final static String STARTING_INSTANCE_MSG_KEY = "StartingInstance.msg"; //$NON-NLS-1$
	private final static String START_INSTANCES_DIALOG_TITLE_VAL = CVMessages.getString("StartInstancesDialog.title"); //$NON-NLS-1$
	private final static String START_INSTANCES_DIALOG_MSG_VAL = CVMessages.getString("StartInstancesDialog.msg"); //$NON-NLS-1$
	
	private final static String STOPPING_INSTANCE_MSG_KEY = "StoppingInstance.msg"; //$NON-NLS-1$
	private final static String STOP_INSTANCES_DIALOG_TITLE_VAL = CVMessages.getString("StopInstancesDialog.title"); //$NON-NLS-1$
	private final static String STOP_INSTANCES_DIALOG_MSG_VAL = CVMessages.getString("StopInstancesDialog.msg"); //$NON-NLS-1$

	private final static String REBOOTING_INSTANCE_MSG_KEY = "RebootingInstance.msg"; //$NON-NLS-1$
	private final static String REBOOT_INSTANCE_TITLE_VAL = CVMessages.getString("RebootInstancesDialog.title"); //$NON-NLS-1$
	private final static String REBOOT_INSTANCE_MSG_VAL = CVMessages.getString("RebootInstancesDialog.msg"); //$NON-NLS-1$

	private final static String DESTROYING_INSTANCE_MSG_KEY = "DestroyingInstance.msg"; //$NON-NLS-1$
	private final static String DESTROY_INSTANCE_TITLE_VAL = CVMessages.getString("DestroyInstancesDialog.title"); //$NON-NLS-1$
	private final static String DESTROY_INSTANCE_MSG_VAL = CVMessages.getString("DestroyInstancesDialog.msg"); //$NON-NLS-1$
	
	public static void startInstances(ISelection selection) {
		DeltaCloudInstance[] instances = getStoppedInstancesToLaunch(getCloudInstances(selection));
		handleInstanceAction(
				instances,
				START_INSTANCES_DIALOG_TITLE_VAL, START_INSTANCES_DIALOG_MSG_VAL, 
				DeltaCloudInstance.Action.START, DeltaCloudInstance.State.RUNNING, 
				STARTING_INSTANCE_MSG_KEY);
	}
	
	public static void stopInstances(ISelection selection) {
		DeltaCloudInstance[] instances = getStoppableInstances(getCloudInstances(selection));
		handleInstanceAction(
				instances,
				STOP_INSTANCES_DIALOG_TITLE_VAL, STOP_INSTANCES_DIALOG_MSG_VAL, 
				DeltaCloudInstance.Action.STOP, DeltaCloudInstance.State.STOPPED, 
				STOPPING_INSTANCE_MSG_KEY);
	}
	
	public static void rebootInstances(ISelection selection) {
		DeltaCloudInstance[] instances = getRebootableInstances(getCloudInstances(selection));
		handleInstanceAction(
				instances,
				REBOOT_INSTANCE_TITLE_VAL, REBOOT_INSTANCE_MSG_VAL, 
				DeltaCloudInstance.Action.REBOOT, DeltaCloudInstance.State.RUNNING, 
				REBOOTING_INSTANCE_MSG_KEY);
	}
	public static void destroyInstances(ISelection selection) {
		DeltaCloudInstance[] instances = getDestroyableInstances(getCloudInstances(selection));
		handleInstanceAction(
				instances,
				DESTROY_INSTANCE_TITLE_VAL, DESTROY_INSTANCE_MSG_VAL, 
				DeltaCloudInstance.Action.DESTROY, DeltaCloudInstance.State.TERMINATED, 
				DESTROYING_INSTANCE_MSG_KEY);
	}
	
	protected static void handleInstanceAction(DeltaCloudInstance[] instances, 
			String dialogTitle, String dialogMsg, DeltaCloudInstance.Action action,
			DeltaCloudInstance.State expectedState, String unformattedJobName) {
		if(instances.length > 1 ) {
			List<DeltaCloudInstance> list = Arrays.asList(instances);
			DeltaCloudInstanceDialog dialog = new DeltaCloudInstanceDialog(
					UIUtils.getActiveShell(), list,
					dialogTitle, dialogMsg);
			dialog.setInitialElementSelections(list);
			if (Dialog.OK == dialog.open()) {
				instances = dialog.getResult2();
			} else { 
				instances = new DeltaCloudInstance[]{};
			}
		}
		
		for( int i = 0; i < instances.length; i++ ) {
			executeInstanceAction( instances[i], action, expectedState, 
					CVMessages.getFormattedString(unformattedJobName, 
							new String[] { instances[i].getName() }));
		}

	}
	
	protected static void executeInstanceAction(DeltaCloudInstance instance, DeltaCloudInstance.Action action,
			DeltaCloudInstance.State expectedState, String jobName) {
		if (instance != null) {
			new InstanceActionJob(jobName, instance, action, expectedState).schedule();
		}
	}
	
	private static DeltaCloudInstance[] getStoppedInstancesToLaunch(List<DeltaCloudInstance> instances) {
		Iterator<DeltaCloudInstance> i = instances.iterator();
		while(i.hasNext())
			if(!i.next().isStopped())
				i.remove();
		return instances.toArray(new DeltaCloudInstance[instances.size()]);
	}
	
	private static DeltaCloudInstance[] getStoppableInstances(List<DeltaCloudInstance> instances) {
		ArrayList<DeltaCloudInstance> stoppedInstances = new ArrayList<DeltaCloudInstance>();
		for (DeltaCloudInstance instance : instances)
			if (instance.canStop())
				stoppedInstances.add(instance);
		return stoppedInstances.toArray(new DeltaCloudInstance[stoppedInstances.size()]);
	}
	
	private static DeltaCloudInstance[] getRebootableInstances(List<DeltaCloudInstance> deltaCloudInstances) {
		ArrayList<DeltaCloudInstance> rebootable = new ArrayList<DeltaCloudInstance>();
		for (DeltaCloudInstance instance : deltaCloudInstances) {
			if (instance.canReboot()) {
				rebootable.add(instance);
			}
		}
		return rebootable.toArray(new DeltaCloudInstance[rebootable.size()]);
	}
	
	private static DeltaCloudInstance[] getDestroyableInstances(List<DeltaCloudInstance> deltaCloudInstances) {
		ArrayList<DeltaCloudInstance> destroyable = new ArrayList<DeltaCloudInstance>();
		for (DeltaCloudInstance instance : deltaCloudInstances) {
			if (instance.canDestroy()) {
				destroyable.add(instance);
			}
		}
		return destroyable.toArray(new DeltaCloudInstance[destroyable.size()]);
	}

	public static List<DeltaCloudInstance> getCloudInstances(ISelection selection) {
		if( selection instanceof IStructuredSelection ) {
			IStructuredSelection s2 = (IStructuredSelection)selection;
			List<DeltaCloudInstance> instances = UIUtils.adapt(s2.toList(),DeltaCloudInstance.class);
			return instances;
		}
		return Collections.EMPTY_LIST;
	}
	
	public static void showCreateInstanceWizard(Shell shell, ISelection selection) {
		DeltaCloudImage deltaCloudImage = UIUtils.getFirstAdaptedElement(selection, DeltaCloudImage.class);
		DeltaCloud deltaCloud = UIUtils.getFirstAdaptedElement(selection, DeltaCloud.class);
		IWizard wizard = new NewInstanceWizard(deltaCloud, deltaCloudImage);
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		dialog.open();
	}
	
	public static void refreshDeltaClouds(Shell shell, ISelection selection) {
		Collection<DeltaCloud> clouds = getSelectedClouds(selection);
		for (DeltaCloud cloud : clouds) {
			new RefreshCloudJob(cloud).schedule();
		}
	}
	
	public static void deleteDeltaClouds(Shell shell, ISelection selection) {
		Collection<DeltaCloud> clouds = getSelectedClouds(selection);
		DeleteCloudsDialog dialog = 
			new DeleteCloudsDialog(shell, clouds);
		dialog.setInitialSelections(clouds.toArray());
		if (Dialog.OK == dialog.open()) {
			ArrayList<IStatus> children = new ArrayList<IStatus>();
			for (Object deltaCloud : clouds) {
				try {
					DeltaCloudManager.getDefault().removeCloud((DeltaCloud) deltaCloud);
				} catch( DeltaCloudException dce) {
					// TODO create status
				}
			}
			if( children.size() > 0 ) {
				// TODO create multistatus and log 
			}
		}
	}
	
	public static Collection<DeltaCloud> getSelectedClouds(ISelection selection) {
		List<?> selectedElements = ((IStructuredSelection) selection).toList();
		Set<DeltaCloud> selectedClouds = new HashSet<DeltaCloud>();
		for (Object element : selectedElements) {
			DeltaCloud deltaCloud = UIUtils.adapt(element, DeltaCloud.class);
			if (deltaCloud != null) {
				selectedClouds.add(deltaCloud);
			}
		}
		return selectedClouds;
	}
}
