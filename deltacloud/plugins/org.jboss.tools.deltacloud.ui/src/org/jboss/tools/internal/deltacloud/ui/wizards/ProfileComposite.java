/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.internal.deltacloud.ui.wizards;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.jboss.tools.deltacloud.core.DeltaCloudHardwareProfile;
import org.jboss.tools.deltacloud.core.DeltaCloudHardwareProperty;

/**
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public class ProfileComposite {

	private static final String CPU_LABEL = "Cpu.label"; //$NON-NLS-1$
	private static final String MEMORY_LABEL = "Memory.label"; //$NON-NLS-1$
	private static final String STORAGE_LABEL = "Storage.label"; //$NON-NLS-1$
	private static final String DEFAULTED = "Defaulted"; //$NON-NLS-1$

	private Composite container;
	private DeltaCloudHardwareProfile profile;
	private String cpu;
	private String cpuDefaultValue;
	private Label cpuLabel;
	private Spinner cpuSpinner;
	private Combo cpuCombo;
	private String memory;
	private String memoryDefaultValue;
	private Label memoryLabel;
	private Spinner memorySpinner;
	private Combo memoryCombo;
	private int memoryDecDigits;
	private String storage;
	private String storageDefaultValue;
	private Label storageLabel;
	private Spinner storageSpinner;
	private Combo storageCombo;
	private int storageDecDigits;

	private static int cw = 160;

	private ModifyListener spinnerListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			String value = ((Spinner) e.widget).getText();
			if (e.widget == cpuSpinner)
				cpu = value;
			else if (e.widget == memorySpinner)
				memory = value;
			else if (e.widget == storageSpinner)
				storage = value;
		}
	};

	private ModifyListener comboListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			String value = ((Combo) e.widget).getText();
			if (e.widget == cpuCombo)
				cpu = value;
			else if (e.widget == memoryCombo)
				memory = value;
			else if (e.widget == storageCombo)
				storage = value;
		}

	};

	public ProfileComposite(DeltaCloudHardwareProfile p, Composite parent) {
		this.profile = p;
		container = new Composite(parent, SWT.NULL);
		FormLayout layout = new FormLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		container.setLayout(layout);

		cpuLabel = new Label(container, SWT.NULL);
		cpuLabel.setText(WizardMessages.getString(CPU_LABEL));
		memoryLabel = new Label(container, SWT.NULL);
		memoryLabel.setText(WizardMessages.getString(MEMORY_LABEL));
		storageLabel = new Label(container, SWT.NULL);
		storageLabel.setText(WizardMessages.getString(STORAGE_LABEL));
		
		FormData fd = new FormData();
		fd.left = new FormAttachment(0, 0);
		fd.top = new FormAttachment(0, 0);
		cpuLabel.setLayoutData(fd);
		DeltaCloudHardwareProperty cpuProperty = profile.getNamedProperty("cpu"); //$NON-NLS-1$
		setCPU(cpuProperty.getValue()); 
		Control cpuControl = createCpuControls(cpuProperty, storageLabel, container);

		fd = new FormData();
		fd.left = new FormAttachment(cpuLabel, 0, SWT.LEFT);
		fd.top = new FormAttachment(cpuLabel, 8);
		memoryLabel.setLayoutData(fd);
		DeltaCloudHardwareProperty memoryProperty = profile.getNamedProperty("memory"); //$NON-NLS-1$
		setMemody(memoryProperty.getValue());
		Control memoryControl = createMemoyControl(cpuControl, memoryProperty, storageLabel, container);

		fd = new FormData();
		fd.left = new FormAttachment(cpuLabel, 0, SWT.LEFT);
		fd.top = new FormAttachment(memoryControl, 8);
		storageLabel.setLayoutData(fd);
		DeltaCloudHardwareProperty storageProperty = profile.getNamedProperty("storage"); //$NON-NLS-1$
		setStorage(storageProperty.getValue());
		createStorageControls(memoryControl, memoryProperty, storageProperty, storageLabel, container);
	}

	private Control createStorageControls(Control memoryControl, DeltaCloudHardwareProperty memoryProperty,
			DeltaCloudHardwareProperty storageProperty, Label storageLabel, Composite container) {
		Control storageControl = null;
		if (storageProperty != null) {
			if (storageProperty.getKind() == DeltaCloudHardwareProperty.Kind.FIXED) {
				Label storageValueLabel = new Label(container, SWT.NULL);
				storageValueLabel.setText(storageProperty.getValue());
				FormData f = new FormData();
				f.left = new FormAttachment(storageLabel, 50);
				f.top = new FormAttachment(memoryControl, 8);
				storageValueLabel.setLayoutData(f);
				storageValueLabel.setVisible(true);
				storageControl = storageValueLabel;
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
					for (int i = 0; i < storageDecDigits - decDigitsFirst; ++i)
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
				storageSpinner.addModifyListener(spinnerListener);
				storageSpinner.setSelection(Integer.valueOf(storageDefaultValue));
				FormData f = new FormData();
				f.width = 80;
				f.top = new FormAttachment(memoryControl, 5);
				f.left = new FormAttachment(storageLabel, 50);
				f.right = new FormAttachment(storageLabel, cw, SWT.RIGHT);
				storageSpinner.setLayoutData(f);
				storageControl = storageSpinner;
			} else if (storageProperty.getKind() == DeltaCloudHardwareProperty.Kind.ENUM) {
				storageDefaultValue = storageProperty.getValue();
				List<String> values = storageProperty.getEnums();
				storageCombo = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
				String[] items = new String[values.size()];
				storageCombo.setItems(values.toArray(items));
				storageCombo.setText(items[0]);
				storageCombo.addModifyListener(comboListener);
				FormData f = new FormData();
				f.width = 80;
				f.left = new FormAttachment(storageLabel, 50);
				f.right = new FormAttachment(storageLabel, cw, SWT.RIGHT);
				f.top = new FormAttachment(memoryControl, 5);
				storageCombo.setLayoutData(f);
				storageControl = storageCombo;
			}
			String storageUnit = storageProperty.getUnit();
			if (storageUnit != null && !storageUnit.equals("label")) { //$NON-NLS-1$
				Label unitLabel = new Label(container, SWT.NULL);
				unitLabel.setText(storageUnit);
				FormData f = new FormData();
				f.left = new FormAttachment(storageControl, 3);
				f.top = new FormAttachment(memoryControl, 8);
				unitLabel.setLayoutData(f);
			}

		} else {
			Label storage = new Label(container, SWT.NULL);
			storage.setText(WizardMessages.getString(DEFAULTED));
			FormData f = new FormData();
			f.left = new FormAttachment(storageLabel, 50);
			f.top = new FormAttachment(memoryControl, 8);
			f.right = new FormAttachment(100, 0);
			storage.setLayoutData(f);
			storage.setVisible(true);
			storageControl = storage;
		}
		return storageControl;
	}

	private Control createMemoyControl(Control cpuControl, DeltaCloudHardwareProperty memoryProperty, Label storageLabel, Composite container) {
		Control memoryControl = null;
		if (memoryProperty != null) {
			if (memoryProperty.getKind() == DeltaCloudHardwareProperty.Kind.FIXED) {
				Label memory = new Label(container, SWT.NULL);
				memory.setText(memoryProperty.getValue());
				FormData f = new FormData();
				f.top = new FormAttachment(cpuControl, 8);
				f.left = new FormAttachment(storageLabel, 50);
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
				memorySpinner = new Spinner(container, SWT.READ_ONLY | SWT.BORDER);
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
					for (int i = 0; i < memoryDecDigits - decDigitsFirst; ++i)
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
				memorySpinner.addModifyListener(spinnerListener);
				memorySpinner.setSelection(Integer.valueOf(memoryDefaultValue));
				FormData f = new FormData();
				f.width = 80;
				f.left = new FormAttachment(storageLabel, 50);
				f.right = new FormAttachment(storageLabel, cw, SWT.RIGHT);
				f.top = new FormAttachment(cpuControl, 5);
				memorySpinner.setLayoutData(f);
				memoryControl = memorySpinner;
			} else if (memoryProperty.getKind() == DeltaCloudHardwareProperty.Kind.ENUM) {
				memoryDefaultValue = memoryProperty.getValue();
				List<String> values = memoryProperty.getEnums();
				memoryCombo = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
				String[] items = new String[values.size()];
				memoryCombo.setItems(values.toArray(items));
				memoryCombo.setText(items[0]);
				memoryCombo.addModifyListener(comboListener);
				FormData f = new FormData();
				f.width = 80;
				f.left = new FormAttachment(storageLabel, 50);
				f.right = new FormAttachment(storageLabel, cw, SWT.RIGHT);
				f.top = new FormAttachment(cpuControl, 5);
				memoryCombo.setLayoutData(f);
				memoryControl = memoryCombo;
			}
			String memoryUnit = memoryProperty.getUnit();
			if (memoryUnit != null && !memoryUnit.equals("label")) { //$NON-NLS-1$
				Label unitLabel = new Label(container, SWT.NULL);
				unitLabel.setText(memoryUnit);
				FormData f = new FormData();
				f.left = new FormAttachment(memoryControl, 3);
				f.top = new FormAttachment(cpuControl, 8);
				unitLabel.setLayoutData(f);
			}
		} else {
			Label memory = new Label(container, SWT.NULL);
			memory.setText(WizardMessages.getString(DEFAULTED));
			FormData f = new FormData();
			f.top = new FormAttachment(cpuControl, 8);
			f.left = new FormAttachment(storageLabel, 50);
			f.right = new FormAttachment(100, 0);
			memory.setLayoutData(f);
			memoryControl = memory;
		}
		return memoryControl;
	}

	private Control createCpuControls(DeltaCloudHardwareProperty cpuProperty, Label storageLabel, Composite container) {
		Control cpuControl = null;
		if (cpuProperty != null) {
			if (cpuProperty.getKind() == DeltaCloudHardwareProperty.Kind.FIXED) {
				Label cpuLabel = new Label(container, SWT.NULL);
				cpuLabel.setText(cpuProperty.getValue());
				FormData f = new FormData();
				f.left = new FormAttachment(storageLabel, 50);
				f.right = new FormAttachment(100, 0);
				cpuLabel.setLayoutData(f);
				cpuControl = cpuLabel;
			} else if (cpuProperty.getKind() == DeltaCloudHardwareProperty.Kind.RANGE) {
				cpuDefaultValue = cpuProperty.getValue();
				cpuSpinner = new Spinner(container, SWT.READ_ONLY);
				cpuSpinner.setMinimum(Integer.valueOf(cpuProperty.getRange().getFirst()));
				cpuSpinner.setMaximum(Integer.valueOf(cpuProperty.getRange().getLast()));
				cpuSpinner.addModifyListener(spinnerListener);
				cpuSpinner.setSelection(Integer.valueOf(cpuDefaultValue));
				FormData f = new FormData();
				f.width = 80;
				f.left = new FormAttachment(storageLabel, 50);
				f.right = new FormAttachment(storageLabel, cw, SWT.RIGHT);
				cpuSpinner.setLayoutData(f);
				cpuControl = cpuSpinner;
			} else if (cpuProperty.getKind() == DeltaCloudHardwareProperty.Kind.ENUM) {
				cpuDefaultValue = cpuProperty.getValue();
				List<String> values = cpuProperty.getEnums();
				cpuCombo = new Combo(container, SWT.BORDER);
				String[] items = new String[values.size()];
				cpuCombo.setItems(values.toArray(items));
				cpuCombo.setText(items[0]);
				cpuCombo.addModifyListener(comboListener);
				FormData f = new FormData();
				f.width = 80;
				f.left = new FormAttachment(storageLabel, 50);
				f.right = new FormAttachment(storageLabel, cw, SWT.RIGHT);
				cpuCombo.setLayoutData(f);
				cpuControl = cpuCombo;
			}
			String cpuUnit = cpuProperty.getUnit();
			if (cpuUnit != null && !cpuUnit.equals("label") && !cpuUnit.equals("count")) { //$NON-NLS-1$ //$NON-NLS-1$
				Label unitLabel = new Label(container, SWT.NULL);
				unitLabel.setText(cpuProperty.getUnit());
				FormData f = new FormData();
				f.left = new FormAttachment(cpuControl, 5);
				unitLabel.setLayoutData(f);
			}
		} else {
			Label cpu = new Label(container, SWT.NULL);
			cpu.setText(WizardMessages.getString(DEFAULTED));
			FormData f = new FormData();
			f.left = new FormAttachment(storageLabel, 50);
			f.right = new FormAttachment(100, 0);
			cpu.setLayoutData(f);
			cpuControl = cpu;
		}
		return cpuControl;
	}

	public void setVisible(boolean visible) {
		container.setVisible(visible);
	}

	private void setCPU(String value) {
		this.cpu = value;
	}
	
	public String getCPU() {
		if (cpu != null && !cpu.equals(cpuDefaultValue)) {
			return cpu;
		}
		return null;
	}

	private void setMemody(String value) {
		this.memory = value;
	}

	public String getMemory() {
		if (memory != null && !memory.equals(memoryDefaultValue)) {
			return memory;
		}
		return null;
	}

	private void setStorage(String value) {
		this.storage = value;
	}
	
	public String getStorage() {
		if (storage != null && !storage.equals(storageDefaultValue)) {
			return storage;
		}
		return null;
	}
}
