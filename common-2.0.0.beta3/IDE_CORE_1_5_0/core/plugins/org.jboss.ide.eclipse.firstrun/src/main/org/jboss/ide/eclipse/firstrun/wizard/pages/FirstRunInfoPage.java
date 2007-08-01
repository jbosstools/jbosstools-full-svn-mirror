package org.jboss.ide.eclipse.firstrun.wizard.pages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jboss.ide.eclipse.firstrun.FirstRunMessages;
import org.jboss.ide.eclipse.firstrun.FirstRunPlugin;

public class FirstRunInfoPage extends WizardPage {

	public FirstRunInfoPage ()
	{
		super (FirstRunMessages.getString("InfoPage.title"),
				FirstRunMessages.getString("FirstRunWizard.title"),
			FirstRunPlugin.getImageDescriptor(FirstRunPlugin.ICON_JBOSSIDE_LOGO));
	}
	
	public void createControl(Composite parent) {
		setTitle(FirstRunMessages.getString("InfoPage.title"));
		
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new FillLayout());
		
		Label label = new Label(main, SWT.WRAP);
		
		label.setText(FirstRunMessages.getString("InfoPage.info"));
		
		setControl(main);
	}

}
