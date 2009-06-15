package org.jboss.tools.profiler.internal.ui.launchtabs;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.jboss.tools.profiler.internal.ui.launch.JBossProfilerLauncherConstants;

public class PropertiesBlock extends BaseBlock {

	private Button fGenerateFileButton;
	private Button fUseTemplateButton;
	private Button saveOnExit;
	private Button enable;
	private BaseBlock saveLocation;

	public PropertiesBlock(BaseLaunchConfigurationTab tab) {
		super(tab);
	}

	public void createControl(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		group.setText("Configuration File");
		group.setLayout(new GridLayout(2, false));
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		fGenerateFileButton = new Button(group, SWT.RADIO);
		fGenerateFileButton.setText("Generate a configuration file with the following settings:");
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		fGenerateFileButton.setLayoutData(gd);	
		fGenerateFileButton.addSelectionListener(new SelectionAdapter() {
			 @Override
			public void widgetSelected(SelectionEvent e) {
				updateGenerateSettings(fGenerateFileButton.getSelection());
				fTab.updateLaunchConfigurationDialog();
			}
		});

		Composite c = new Composite(group, SWT.None);
		GridLayout layout = new GridLayout(3,false);		
		c.setLayout(layout);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		c.setLayoutData(gd);
		
		enable = new Button(c, SWT.CHECK);
		enable.setText("Enable profiler on start");
		gd = new GridData();
		gd.horizontalSpan = 3;
		enable.setLayoutData(gd);
		
		saveOnExit = new Button(c, SWT.CHECK);
		saveOnExit.setText("Save snapshot on exit");
		gd = new GridData();
		gd.horizontalSpan = 3;
		saveOnExit.setLayoutData(gd);
		
		saveLocation = new BaseBlock(fTab) {
			@Override
			protected String getName() {
				return "Save snapshot location:";
			}

			
			
			@Override
			protected boolean isFile() {
				return false;
			}
		};
		
		saveLocation.createControl(c);		
		
		fUseTemplateButton = new Button(group, SWT.RADIO);
		fUseTemplateButton.setText("Use an existing jboss-profiler.properties");
		fUseTemplateButton.addSelectionListener(fListener);
		gd = new GridData();
		gd.horizontalSpan = 2;
		fUseTemplateButton.setLayoutData(gd);

		createText(group, "Location:", 20);

		Composite buttons = new Composite(group, SWT.NONE);
		layout = new GridLayout(4, false);
		layout.marginHeight = layout.marginWidth = 0;
		buttons.setLayout(layout);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		buttons.setLayoutData(gd);

		Label label = new Label(buttons, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createButtons(buttons, new String[] {"Workspace...","File system...","Variables..."});
				
	}

	public void initializeFrom(ILaunchConfiguration configuration) throws CoreException {
		boolean generateDefault = configuration.getAttribute(JBossProfilerLauncherConstants.GENERATE_PROPERTIES, true);
		fGenerateFileButton.setSelection(generateDefault);		
		
		updateGenerateSettings(generateDefault);
				
		fUseTemplateButton.setSelection(!generateDefault);
		fLocationText.setText(configuration.getAttribute(JBossProfilerLauncherConstants.PROPERTIES_FILE, ""));				
		enableBrowseSection(!generateDefault);
		
		enable.setSelection(configuration.getAttribute(JBossProfilerLauncherConstants.ENABLE_ON_STARTUP, true));
		saveOnExit.setSelection(configuration.getAttribute(JBossProfilerLauncherConstants.SAVE_ON_EXIT, true));
		
		saveLocation.setLocation(configuration.getAttribute(JBossProfilerLauncherConstants.SAVE_LOCATION, "."));				
		
	}

	private void updateGenerateSettings(boolean generateEnabled) {
		enable.setEnabled(generateEnabled);
		saveOnExit.setEnabled(generateEnabled);
		saveLocation.enableBrowseSection(generateEnabled);
	}

	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(JBossProfilerLauncherConstants.GENERATE_PROPERTIES, fGenerateFileButton.getSelection());
		configuration.setAttribute(JBossProfilerLauncherConstants.ENABLE_ON_STARTUP, enable.getSelection());
		configuration.setAttribute(JBossProfilerLauncherConstants.SAVE_ON_EXIT, saveOnExit.getSelection());
		configuration.setAttribute(JBossProfilerLauncherConstants.SAVE_LOCATION, saveLocation.getLocation());
		configuration.setAttribute(JBossProfilerLauncherConstants.PROPERTIES_FILE, getLocation()==""?null:getLocation());
		
	}

	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(JBossProfilerLauncherConstants.GENERATE_PROPERTIES, true);
		configuration.setAttribute(JBossProfilerLauncherConstants.PROPERTIES_FILE, (String)null);
		
		
		
	}

	protected String getName() {
		return "Properties";
	}

	protected boolean isFile() {
		return true;
	}

	
	
	@Override
	protected String[] getFileFilter() {
		return new String[] {"*.properties"};
	}

	protected String getLocation() {
		String path = fLocationText.getText().trim();
		IStringVariableManager manager = VariablesPlugin.getDefault().getStringVariableManager();
		try {
			return manager.performStringSubstitution(path, false);
		} catch (CoreException e) {
			return path;
		}
	}

	
	public String validate() {
		
		if (fGenerateFileButton.getSelection())
			return null;
		return super.validate();
	}

}
