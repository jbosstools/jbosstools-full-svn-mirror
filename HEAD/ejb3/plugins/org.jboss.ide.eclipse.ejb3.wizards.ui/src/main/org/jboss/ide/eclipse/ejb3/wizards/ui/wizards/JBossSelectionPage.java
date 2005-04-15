package org.jboss.ide.eclipse.ejb3.wizards.ui.wizards;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
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
		main.setLayout(new FillLayout());
		
		configurations = new TableViewer(main);
		configurations.setContentProvider(new ServerNavigatorContentProvider());
		configurations.setLabelProvider(new ServerNavigatorLabelProvider());
		configurations.addSelectionChangedListener(new ISelectionChangedListener () {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) configurations.getSelection();
				configuration = (ILaunchConfiguration) selection.getFirstElement();
			}
		});
		
		try {
			configurations.setInput(ServerLaunchManager.getInstance().getServerConfigurations());
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		setControl(main);
	}

	public ILaunchConfiguration getLaunchConfiguration ()
	{
		return configuration;
	}
}
