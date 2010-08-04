package org.jboss.tools.internal.deltacloud.ui.wizards;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.jboss.tools.deltacloud.core.DeltaCloudHardwareProfile;
import org.jboss.tools.deltacloud.core.DeltaCloudHardwareProperty;

public class ProfileComposite {
	
	private static final String CPU_LABEL = "Cpu.label"; //$NON-NLS-1$
	
	private Composite container;
	private DeltaCloudHardwareProfile profile;
	private String cpu;
	private String cpuDefaultValue;
	private Button[] cpuButtons;
	
	private ModifyListener cpuSpinnerListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			cpu = ((Spinner)e.widget).getText();
		}
	};

	private SelectionListener cpuButtonListener = new SelectionAdapter() {

		@Override
		public void widgetSelected(SelectionEvent e) {
			for (int i = 0; i < cpuButtons.length; ++i) {
				Button b = cpuButtons[i];
				if (b != e.widget) {
					b.setSelection(false);
				} else {
					b.setSelection(true);
					cpu = b.getText();
				}
			}
		}

	};

	public ProfileComposite(DeltaCloudHardwareProfile profile, Composite parent) {
		this.profile = profile;
		container = new Composite(parent, SWT.NULL);
		
		DeltaCloudHardwareProperty cpuProperty = profile.getNamedProperty("cpu");
		if (cpuProperty != null) {
			Label cpuLabel = new Label(container, SWT.NULL);
			cpuLabel.setText(WizardMessages.getString(CPU_LABEL));
			if (cpuProperty.getKind() == DeltaCloudHardwareProperty.Kind.FIXED) {
				Label cpu = new Label(container, SWT.NULL);
				cpu.setText(cpuProperty.getValue());
			} else if (cpuProperty.getKind() == DeltaCloudHardwareProperty.Kind.RANGE) {
				cpuDefaultValue = cpuProperty.getValue();
				Spinner cpuSpinner = new Spinner(container, SWT.READ_ONLY);
				cpuSpinner.setMinimum(Integer.valueOf(cpuProperty.getRange().getFirst()));
				cpuSpinner.setMaximum(Integer.valueOf(cpuProperty.getRange().getLast()));
				cpuSpinner.addModifyListener(cpuSpinnerListener);
				cpuSpinner.setSelection(Integer.valueOf(cpuDefaultValue));
			} else if (cpuProperty.getKind() == DeltaCloudHardwareProperty.Kind.ENUM) {
				cpuDefaultValue = cpuProperty.getValue();
				List<String> values = cpuProperty.getEnums();
				cpuButtons = new Button[values.size()];
				for (int i = 0; i < values.size(); ++i) {
					Button button = new Button(container, SWT.RADIO);
					String value = values.get(i);
					button.setText(value);
					if (value.equals(cpuDefaultValue))
						button.setSelection(true);
					button.addSelectionListener(cpuButtonListener);
					cpuButtons[i] = button;				
				}
			}
		}
	}
	
	public void setVisible(boolean visible) {
		container.setVisible(visible);
	}

	public String getCPU() {
		if (cpu != null && !cpu.equals(cpuDefaultValue))
			return cpu;
		return null;
	}
}
