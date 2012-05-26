package org.jboss.tools.profiler.internal.ui.launchtabs;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.profiler.internal.ui.launch.JBossProfilerLauncherConstants;

public class JarBlock extends BaseBlock {
	
	private Button fUseDefaultDirButton;
	private Button fUseOtherDirButton;

	public JarBlock(BaseLaunchConfigurationTab tab) {
		super(tab);
	}

	public void createControl(Composite parent) {

		Group group = new Group(parent, SWT.NONE);
		group.setText(getName());
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		group.setLayout(layout);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// default choice
		
		fUseDefaultDirButton = new Button(group, SWT.RADIO);
		fUseDefaultDirButton.setText("Default:");		
		fUseDefaultDirButton.addSelectionListener(fListener);
		
		Text fWorkingDirText = createSingleText(group, 2);
		fWorkingDirText.addModifyListener(fListener);
		fWorkingDirText.setEnabled(false);
		fWorkingDirText.setText("<embedded JBoss Profiler>");
		
		// user enter choice
		fUseOtherDirButton = new Button(group, SWT.RADIO);
		fUseOtherDirButton.setText("Other:");			
		fUseOtherDirButton.addSelectionListener(fListener);
				
		createText(group, "Location:", 0);
		
		//new Label(group, SWT.None);
		//new Label(group, SWT.None);
		
		Composite comp = createComposite(group, parent.getFont(), 4, 3, GridData.FILL_BOTH, 0, 0);
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		new Label(comp, SWT.NONE).setLayoutData(gd);
		// buttons
		createButtons(comp, new String[] {"Workspace...", "Filesystem...", "Variables..."});		

	}

	public void performApply(ILaunchConfigurationWorkingCopy config) {
		config.setAttribute(JBossProfilerLauncherConstants.PROFILER_JAR, getLocation());
		config.setAttribute(JBossProfilerLauncherConstants.USE_EMBEDDED_JAR, fUseDefaultDirButton.getSelection());
		
	}

	public void initializeFrom(ILaunchConfiguration configuration) throws CoreException {
		fLocationText.setText(configuration.getAttribute(JBossProfilerLauncherConstants.PROFILER_JAR, ""));
		boolean useEmbedded = configuration.getAttribute(JBossProfilerLauncherConstants.USE_EMBEDDED_JAR, true);
		fUseDefaultDirButton.setSelection(useEmbedded);
		fUseOtherDirButton.setSelection(!useEmbedded);
				
	}

	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		
	}

	protected String getName() {
		return "JBoss Profiler runtime";
	}

	@Override
	protected String[] getFileFilter() {
		return new String[] { "*.jar" };
	}
	
	protected boolean isFile() {
		return false;
	}

	
	@Override
	public String validate() {
		if(fUseDefaultDirButton.getSelection()) {
			return null;
		}
		return super.validate();
	}
}
