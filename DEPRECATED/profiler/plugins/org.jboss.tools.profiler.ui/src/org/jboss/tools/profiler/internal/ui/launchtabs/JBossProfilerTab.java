package org.jboss.tools.profiler.internal.ui.launchtabs;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jboss.tools.profiler.internal.ui.JBossProfilerUiPlugin;

public class JBossProfilerTab extends BaseLaunchConfigurationTab {

	private JarBlock jarBlock;
	private PropertiesBlock propertiesBlock;

	public void createControl(Composite parent) {

		// initializeDialogUnits(parent);
		final ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL
				| SWT.V_SCROLL);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);

		setControl(sc);
		Composite container = new Composite(sc, SWT.NULL);
		sc.setContent(container);
		GridLayout layout = new GridLayout();
		
		container.setLayout(layout);
		layout.numColumns = 1;
		layout.verticalSpacing = 10;

		propertiesBlock = new PropertiesBlock(this);
		propertiesBlock.createControl(container);		

		jarBlock = new JarBlock(this);
		jarBlock.createControl(container);
		
		
	}


	public String getName() {
		return "JBoss Profiler";
	}

	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			jarBlock.initializeFrom(configuration);
			propertiesBlock.initializeFrom(configuration);
		
		} catch (CoreException e) {
			JBossProfilerUiPlugin.getDefault().logErrorMessage("Error while initializing from " + configuration.getName(), e);
		}
	}

	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		jarBlock.performApply(configuration);
		propertiesBlock.performApply(configuration);		
	}

	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		jarBlock.setDefaults(configuration);
		propertiesBlock.setDefaults(configuration);		
	}
	
	
	@Override
	public boolean isValid(ILaunchConfiguration configuration) {
		//System.out.println("isValid");
		propertiesBlock.validate();
		jarBlock.validate();
		return super.isValid(configuration);
	}
	
	public static Group createGroup(Composite parent, String text, int columns,
			int hspan, int fill) {
		Group g = new Group(parent, SWT.NONE);
		g.setLayout(new GridLayout(columns, false));
		g.setText(text);
		g.setFont(parent.getFont());
		GridData gd = new GridData(fill);
		gd.horizontalSpan = hspan;
		g.setLayoutData(gd);
		return g;
	}

	
}
