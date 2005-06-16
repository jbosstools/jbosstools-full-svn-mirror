package org.jboss.ide.eclipse.ejb3.wizards.ui.wizards;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.jboss.ide.eclipse.launcher.core.ServerLaunchManager;
import org.jboss.ide.eclipse.launcher.ui.views.ServerNavigatorContentProvider;
import org.jboss.ide.eclipse.launcher.ui.views.ServerNavigatorLabelProvider;

public class JBossSelectionPage extends WizardPage {

	protected TableViewer configurations;
	protected ILaunchConfiguration configuration;
	
	public JBossSelectionPage ()
	{
		super("JBoss Server Selection");
	}
	
	public void createControl(Composite parent) {
		
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout());
		
		Composite configurationComposite = new Composite(main, SWT.NONE);
		configurationComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		configurationComposite.setLayout(new FillLayout());
		
		configurations = new TableViewer(configurationComposite);
		configurations.setContentProvider(new ServerNavigatorContentProvider());
		configurations.setLabelProvider(new ServerNavigatorLabelProvider());
		configurations.addSelectionChangedListener(new ISelectionChangedListener () {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) configurations.getSelection();
				configuration = (ILaunchConfiguration) selection.getFirstElement();
				
				getWizard().getContainer().updateButtons();
			}
		});
		
		refreshConfigurations();
		
		Link newConfigurationLink = new Link(main, SWT.NONE);
		newConfigurationLink.setText("<a href=\"create\">Create a JBoss configuration</a>");
		newConfigurationLink.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			
			public void widgetSelected(SelectionEvent e) {
				createJBossConfiguration();
			}
		});
		
		setControl(main);
	}
	
	private void refreshConfigurations ()
	{
		try {
			configurations.setInput(ServerLaunchManager.getInstance().getServerConfigurations());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void createJBossConfiguration ()
	{
		int status = DebugUITools.openLaunchConfigurationDialogOnGroup(
				DebugUIPlugin.getShell(), new StructuredSelection(), IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);
		
		if (status == Window.OK)
		{
			refreshConfigurations();
		}
	}
	
	public ILaunchConfiguration getLaunchConfiguration ()
	{
		return configuration;
	}
}
