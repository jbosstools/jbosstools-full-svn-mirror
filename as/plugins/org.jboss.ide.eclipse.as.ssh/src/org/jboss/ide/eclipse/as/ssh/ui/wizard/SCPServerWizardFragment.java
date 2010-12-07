package org.jboss.ide.eclipse.as.ssh.ui.wizard;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.TaskModel;
import org.eclipse.wst.server.ui.internal.ImageResource;
import org.eclipse.wst.server.ui.wizard.IWizardHandle;
import org.eclipse.wst.server.ui.wizard.WizardFragment;
import org.jboss.ide.eclipse.as.ssh.Messages;
import org.jboss.ide.eclipse.as.ssh.server.SSHServerDelegate;

public class SCPServerWizardFragment extends WizardFragment {
	private IWizardHandle handle;
	private Text userText, passText, deployText;
	private ModifyListener listener;
	private SelectionListener browseHostsButtonListener;
	private String user, pass, deploy;

	public SCPServerWizardFragment() {
		super();
	}

	public Composite createComposite(Composite parent, IWizardHandle handle) {
		this.handle = handle;
		handle.setDescription(Messages.SCPServerDescription);
		handle.setImageDescriptor(ImageResource.getImageDescriptor(ImageResource.IMG_WIZBAN_NEW_SERVER));

		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new FormLayout());
		addWidgets(main);
		validate();
		return main;
	}
	
	protected void addWidgets(Composite composite) {
		composite.setLayout(new FormLayout());

		Composite inner = new Composite(composite, SWT.NONE);
		inner.setLayout(new GridLayout(3, false));

		FormData innerData = new FormData();
		innerData.top = new FormAttachment(0, 5);
		innerData.left = new FormAttachment(0, 5);
		innerData.right = new FormAttachment(100, -5);
		inner.setLayoutData(innerData);

		GridData textData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
		textData.widthHint = 300;
		
		Label label = new Label(inner, SWT.NONE);
		label.setText(Messages.DeployRootFolder);
		deployText = new Text(inner, SWT.BORDER);
		deployText.setText("/home/rob/deployFolder");
		listener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateValues();
			}
		};
		deployText.addModifyListener(listener);
		deployText.setEnabled(true);
		deployText.setLayoutData(textData);
		
		Label userLabel = new Label(inner, SWT.NONE);
		userLabel.setText(Messages.UserLabel);
		userText = new Text(inner, SWT.BORDER);
		userText.setText("username");
		userText.addModifyListener(listener);
		userText.setEnabled(true);
		userText.setLayoutData(textData);
		
		
		Label passLabel = new Label(inner, SWT.NONE);
		passLabel.setText(Messages.PassLabel);
		passText = new Text(inner, SWT.BORDER | SWT.PASSWORD);
		passText.setText("password");
		passText.addModifyListener(listener);
		passText.setEnabled(true);
		passText.setLayoutData(textData);
	}
	

	protected void updateValues() {
		user = userText.getText();
		pass = passText.getText();
		deploy = deployText.getText();
		validate();
	}
	
	public void enter() {
	}
	public void exit() {
	}
	public boolean hasComposite() {
		return true;
	}
	
	protected void validate() {
		handle.setMessage(null, IMessageProvider.NONE);
		handle.update();
	}
	
	public boolean isComplete() {
		return handle.getMessageType() == IMessageProvider.NONE;
	}
	
	public void performFinish(IProgressMonitor monitor) throws CoreException {
		IServerWorkingCopy serverWC = (IServerWorkingCopy) getTaskModel().getObject(TaskModel.TASK_SERVER);
		SSHServerDelegate server = (SSHServerDelegate)serverWC.loadAdapter(SSHServerDelegate.class, new NullProgressMonitor());
		server.setUsername(user); //$NON-NLS-1$
		server.setPassword(pass); //$NON-NLS-1$
		server.setHostsFile(null);
		server.setDeployFolder(deploy);
		IServer tmp = serverWC.save(true, monitor);
		getTaskModel().putObject(TaskModel.TASK_SERVER, tmp);
	}
}
