package org.jboss.tools.internal.deltacloud.ui.wizards;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.jboss.tools.deltacloud.core.DeltaCloudHardwareProfile;
import org.jboss.tools.deltacloud.core.DeltaCloudHardwareProperty;

public class ProfileComposite {
	
	private static final String CPU_LABEL = "Cpu.label"; //$NON-NLS-1$
	private static final String MEMORY_LABEL = "Memory.label"; //$NON-NLS-1$
	private static final String STORAGE_LABEL = "Storage.label"; //$NON-NLS-1$
	
	private Composite container;
	private DeltaCloudHardwareProfile profile;
	private String cpu;
	private String cpuDefaultValue;
	private Label cpuLabel;
	private Button[] cpuButtons;
	private String memory;
	private String memoryDefaultValue;
	private Label memoryLabel;
	private Button[] memoryButtons;
	private int memoryDecDigits;
	private String storage;
	private String storageDefaultValue;
	private Label storageLabel;
	private Button[] storageButtons;
	private int storageDecDigits;
	
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

	private ModifyListener memorySpinnerListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			memory = ((Spinner)e.widget).getText();
		}
	};

	private SelectionListener memoryButtonListener = new SelectionAdapter() {

		@Override
		public void widgetSelected(SelectionEvent e) {
			for (int i = 0; i < memoryButtons.length; ++i) {
				Button b = memoryButtons[i];
				if (b != e.widget) {
					b.setSelection(false);
				} else {
					b.setSelection(true);
					memory = b.getText();
				}
			}
		}

	};
	
	private ModifyListener storageSpinnerListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			storage = ((Spinner)e.widget).getText();
		}
	};

	private SelectionListener storageButtonListener = new SelectionAdapter() {

		@Override
		public void widgetSelected(SelectionEvent e) {
			for (int i = 0; i < storageButtons.length; ++i) {
				Button b = storageButtons[i];
				if (b != e.widget) {
					b.setSelection(false);
				} else {
					b.setSelection(true);
					storage = b.getText();
				}
			}
		}

	};

	public ProfileComposite(DeltaCloudHardwareProfile p, Composite parent) {
		this.profile = p;
		container = new Composite(parent, SWT.NULL);
		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		container.setLayout(layout);
		Control cpuControl = null;
		Control memoryControl = null;
		
		DeltaCloudHardwareProperty cpuProperty = profile.getNamedProperty("cpu"); //$NON-NLS-1$
		if (cpuProperty != null) {
			cpuLabel = new Label(container, SWT.NULL);
			cpuLabel.setText(WizardMessages.getString(CPU_LABEL));
			FormData fd = new FormData();
			fd.left = new FormAttachment(0, 0);
			fd.top = new FormAttachment(0, 0);
			cpuLabel.setLayoutData(fd);
			if (cpuProperty.getKind() == DeltaCloudHardwareProperty.Kind.FIXED) {
				Label cpu = new Label(container, SWT.NULL);
				cpu.setText(cpuProperty.getValue());
				FormData f = new FormData();
				f.left = new FormAttachment(cpuLabel, 5);
				f.right = new FormAttachment(100, 0);
				cpu.setLayoutData(f);
				cpuControl = cpu;
			} else if (cpuProperty.getKind() == DeltaCloudHardwareProperty.Kind.RANGE) {
				cpuDefaultValue = cpuProperty.getValue();
				Spinner cpuSpinner = new Spinner(container, SWT.READ_ONLY);
				cpuSpinner.setMinimum(Integer.valueOf(cpuProperty.getRange().getFirst()));
				cpuSpinner.setMaximum(Integer.valueOf(cpuProperty.getRange().getLast()));
				cpuSpinner.addModifyListener(cpuSpinnerListener);
				cpuSpinner.setSelection(Integer.valueOf(cpuDefaultValue));
				FormData f = new FormData();
				f.left = new FormAttachment(cpuLabel, 5);
				cpuSpinner.setLayoutData(f);
				cpuControl = cpuSpinner;
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
					cpuControl = button;
					FormData f = new FormData();
					if (i % 3 == 0) {
					   	f.left = new FormAttachment(cpuLabel, 5);
					   	if (i > 2) {
					   		f.top = new FormAttachment(cpuButtons[i-3]);
					   	}
					} else {
						f.left = new FormAttachment(cpuButtons[i-1]);
						if (i > 3)
							f.top = new FormAttachment(cpuButtons[i-3]);
					}
					button.setLayoutData(f);
				}
			}
		}
		DeltaCloudHardwareProperty memoryProperty = profile.getNamedProperty("memory"); //$NON-NLS-1$
		if (memoryProperty != null) {
			memoryLabel = new Label(container, SWT.NULL);
			memoryLabel.setText(WizardMessages.getString(MEMORY_LABEL));
			FormData fd = new FormData();
			fd.left = new FormAttachment(cpuLabel, 0, SWT.LEFT);
			fd.top = new FormAttachment(cpuLabel, 8);
			memoryLabel.setLayoutData(fd);
			if (memoryProperty.getKind() == DeltaCloudHardwareProperty.Kind.FIXED) {
				Label memory = new Label(container, SWT.NULL);
				memory.setText(memoryProperty.getValue());
				FormData f = new FormData();
				f.top = new FormAttachment(cpuControl, 8);
				f.left = new FormAttachment(memoryLabel, 5);
				f.right = new FormAttachment(100, 0);
				memory.setLayoutData(f);
				memoryControl = memory;
			} else if (memoryProperty.getKind() == DeltaCloudHardwareProperty.Kind.RANGE) {
				memoryDefaultValue = memoryProperty.getValue();
				int indexDefault = memoryDefaultValue.indexOf('.');
				int decDigitsDefault = 0;
				if (indexDefault >= 0) {
					decDigitsDefault = memoryDefaultValue.length() - indexDefault - 1;
					memoryDefaultValue = memoryDefaultValue.replace(".", ""); //$NON-NLS-1$ //$NON-NLS-2$
				}
				Spinner memorySpinner = new Spinner(container, SWT.READ_ONLY | SWT.BORDER);
				String first = memoryProperty.getRange().getFirst();
				int indexFirst = first.indexOf('.');
				int decDigitsFirst = 0;
				if (indexFirst >= 0) {
					decDigitsFirst = first.length() - indexFirst - 1;
					first = first.replace(".", ""); //$NON-NLS-1$ $NON-NLS-2$
				}
				String last = memoryProperty.getRange().getLast();
				int indexLast = first.indexOf('.');
				int decDigitsLast = 0;
				if (indexLast >= 0) {
					decDigitsLast = last.length() - indexLast - 1;
					last = last.replace(".", ""); //$NON-NLS-1$ //$NON-NLS-2$
				}
				int decDigits = Math.max(decDigitsFirst, decDigitsLast);
				memoryDecDigits = Math.max(decDigits, decDigitsDefault);
				if (decDigitsFirst < memoryDecDigits) {
					for (int i= 0; i < memoryDecDigits - decDigitsFirst; ++i)
						first = first.concat("0"); //$NON-NLS-1$
				} 
				if (decDigitsLast < memoryDecDigits) {
					for (int i = 0; i < memoryDecDigits - decDigitsLast; ++i)
						last = last.concat("0"); //$NON-NLS-1$
				} 
				if (decDigitsDefault < memoryDecDigits) {
					for (int i = 0; i < memoryDecDigits - decDigitsLast; ++i)
						memoryDefaultValue = memoryDefaultValue.concat("0"); //$NON-NLS-1$
				}
				memorySpinner.setMinimum(Integer.valueOf(first));
				memorySpinner.setMaximum(Integer.valueOf(last));
				memorySpinner.setDigits(memoryDecDigits);
				memorySpinner.addModifyListener(memorySpinnerListener);
				memorySpinner.setSelection(Integer.valueOf(memoryDefaultValue));
				FormData f = new FormData();
				f.left = new FormAttachment(memoryLabel, 5);
				f.top = new FormAttachment(cpuControl, 5);
				memorySpinner.setLayoutData(f);
				memoryControl = memorySpinner;
			} else if (memoryProperty.getKind() == DeltaCloudHardwareProperty.Kind.ENUM) {
				memoryDefaultValue = memoryProperty.getValue();
				List<String> values = memoryProperty.getEnums();
				memoryButtons = new Button[values.size()];
				for (int i = 0; i < values.size(); ++i) {
					Button button = new Button(container, SWT.RADIO);
					String value = values.get(i);
					button.setText(value);
					if (value.equals(memoryDefaultValue))
						button.setSelection(true);
					button.addSelectionListener(memoryButtonListener);
					memoryButtons[i] = button;
					memoryControl = button;
					FormData f = new FormData();
					if (i % 3 == 0) {
					   	f.left = new FormAttachment(memoryLabel, 5);
					   	if (i > 2) {
					   		f.top = new FormAttachment(memoryButtons[i-3]);
					   	} else {
					   		f.top = new FormAttachment(cpuControl);
					   	}
					} else {
						f.left = new FormAttachment(memoryButtons[i-1]);
						if (i > 3)
							f.top = new FormAttachment(memoryButtons[i-3]);
						else
							f.top = new FormAttachment(cpuControl);
					}
					button.setLayoutData(f);
				}
			}
		}
		DeltaCloudHardwareProperty storageProperty = profile.getNamedProperty("storage"); //$NON-NLS-1$
		if (storageProperty != null) {
			storageLabel = new Label(container, SWT.NULL);
			storageLabel.setText(WizardMessages.getString(STORAGE_LABEL));
			FormData fd = new FormData();
			fd.left = new FormAttachment(cpuLabel, 0, SWT.LEFT);
			fd.top = new FormAttachment(memoryLabel, 8);
			storageLabel.setLayoutData(fd);
			if (storageProperty.getKind() == DeltaCloudHardwareProperty.Kind.FIXED) {
				Label storage = new Label(container, SWT.NULL);
				storage.setText(storageProperty.getValue());
				FormData f = new FormData();
				f.left = new FormAttachment(storageLabel, 5);
				f.top = new FormAttachment(memoryControl, 8);
				f.right = new FormAttachment(100, 0);
				storage.setLayoutData(f);
				storage.setVisible(true);
			} else if (storageProperty.getKind() == DeltaCloudHardwareProperty.Kind.RANGE) {
				storageDefaultValue = storageProperty.getValue();
				int indexDefault = storageDefaultValue.indexOf('.');
				int decDigitsDefault = 0;
				if (indexDefault >= 0) {
					decDigitsDefault = storageDefaultValue.length() - indexDefault - 1;
					storageDefaultValue = storageDefaultValue.replace(".", ""); //$NON-NLS-1$ //$NON-NLS-2$
				}
				Spinner storageSpinner = new Spinner(container, SWT.READ_ONLY | SWT.BORDER);
				String first = memoryProperty.getRange().getFirst();
				int indexFirst = first.indexOf('.');
				int decDigitsFirst = 0;
				if (indexFirst >= 0) {
					decDigitsFirst = first.length() - indexFirst - 1;
					first = first.replace(".", ""); //$NON-NLS-1$ //$NON-NLS-2$
				}
				String last = memoryProperty.getRange().getLast();
				int indexLast = first.indexOf('.');
				int decDigitsLast = 0;
				if (indexLast >= 0) {
					decDigitsLast = last.length() - indexLast - 1;
					last = last.replace(".", ""); //$NON-NLS-1$ //$NON-NLS-2$
				}
				int decDigits = Math.max(decDigitsFirst, decDigitsLast);
				storageDecDigits = Math.max(decDigits, decDigitsDefault);
				if (decDigitsFirst < storageDecDigits) {
					for (int i= 0; i < storageDecDigits - decDigitsFirst; ++i)
						first = first.concat("0"); //$NON-NLS-1$
				} 
				if (decDigitsLast < storageDecDigits) {
					for (int i = 0; i < storageDecDigits - decDigitsLast; ++i)
						last = last.concat("0"); //$NON-NLS-1$
				}
				if (decDigitsDefault < storageDecDigits) {
					for (int i = 0; i < storageDecDigits - decDigitsLast; ++i)
						storageDefaultValue = storageDefaultValue.concat("0"); //$NON-NLS-1$
				}
				storageSpinner.setMinimum(Integer.valueOf(first));
				storageSpinner.setMaximum(Integer.valueOf(last));
				storageSpinner.setDigits(storageDecDigits);
				storageSpinner.addModifyListener(storageSpinnerListener);
				storageSpinner.setSelection(Integer.valueOf(storageDefaultValue));
				FormData f = new FormData();
				f.top = new FormAttachment(memoryControl, 5);
				f.left = new FormAttachment(storageLabel, 5);
				storageSpinner.setLayoutData(f);
			} else if (storageProperty.getKind() == DeltaCloudHardwareProperty.Kind.ENUM) {
				storageDefaultValue = storageProperty.getValue();
				List<String> values = storageProperty.getEnums();
				storageButtons = new Button[values.size()];
				for (int i = 0; i < values.size(); ++i) {
					Button button = new Button(container, SWT.RADIO);
					String value = values.get(i);
					button.setText(value);
					if (value.equals(storageDefaultValue))
						button.setSelection(true);
					button.addSelectionListener(storageButtonListener);
					storageButtons[i] = button;
					FormData f = new FormData();
					if (i % 3 == 0) {
					   	f.left = new FormAttachment(storageLabel, 5);
					   	if (i > 2) {
					   		f.top = new FormAttachment(storageButtons[i-3]);
					   	} else {
					   		f.top = new FormAttachment(memoryControl);
					   	}
					} else {
						f.left = new FormAttachment(storageButtons[i-1]);
						if (i > 3)
							f.top = new FormAttachment(storageButtons[i-3]);
						else
							f.top = new FormAttachment(memoryControl);
					}
					button.setLayoutData(f);
				}
			}
		}
		Label dummyLabel = new Label(container, SWT.NULL);
		FormData fd = new FormData();
		fd.top = new FormAttachment(storageLabel, 0);
	}
	
	public void setVisible(boolean visible) {
		container.setVisible(visible);
	}

	public String getCPU() {
		if (cpu != null && !cpu.equals(cpuDefaultValue))
			return cpu;
		return null;
	}
	
	public String getMemory() {
		if (memory != null && !memory.equals(memoryDefaultValue)) {
			String retVal = memory;
			if (memoryDecDigits > 0) {
				// Need to put back decimal point in returned value when we used the spinner
				retVal = retVal.substring(0, retVal.length() - memoryDecDigits).concat(".").
				concat(memory.substring(memory.length() - memoryDecDigits));
			}
			return retVal;
		}
		return null;
	}
	
	public String getStorage() {
		if (storage != null && !storage.equals(storageDefaultValue)) {
			String retVal = storage;
			if (storageDecDigits > 0) {
				// Need to put back decimal point in returned value when we used the spinner
				retVal = retVal.substring(0, retVal.length() - storageDecDigits).concat(".").
				concat(storage.substring(storage.length() - storageDecDigits));
			}
			return retVal;
		}
		return null;
	}
}
