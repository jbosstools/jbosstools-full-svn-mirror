package org.jboss.ide.eclipse.firstrun.wizard.pages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jboss.ide.eclipse.firstrun.FirstRunMessages;
import org.jboss.ide.eclipse.firstrun.FirstRunPlugin;

public class FirstRunFinalPage extends WizardPage {

	public FirstRunFinalPage ()
	{
		super (FirstRunMessages.getString("FinalPage.title"),
				FirstRunMessages.getString("FirstRunWizard.title"),
				FirstRunPlugin.getImageDescriptor(FirstRunPlugin.ICON_JBOSSIDE_LOGO));
	}
	
	public void createControl(Composite parent) {
		setTitle(FirstRunMessages.getString("FinalPage.title"));
		
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new FillLayout());
		
		Label label = new Label(main, SWT.WRAP);
		
		label.setText(FirstRunMessages.getString("FinalPage.info"));
		
		setControl(main);
	}
	
	public boolean isPageComplete() {
		return isCurrentPage();
	}

}
