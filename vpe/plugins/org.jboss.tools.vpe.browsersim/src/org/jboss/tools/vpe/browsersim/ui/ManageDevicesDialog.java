/*******************************************************************************
 * Copyright (c) 2007-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.browsersim.ui;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.jboss.tools.vpe.browsersim.model.Device;
import org.jboss.tools.vpe.browsersim.model.DevicesList;
import org.jboss.tools.vpe.browsersim.model.DevicesListStorage;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class ManageDevicesDialog extends Dialog {

	protected DevicesList oldDevicesList;
	protected List<Device> devices;
	protected int selectedDeviceIndex;
	protected Shell shell;
	protected Table table;
	protected DevicesList resultDevicesList;
	protected boolean useSkins;
	protected Boolean truncateWindow;
	protected Button askBeforeTruncateRadio;
	protected Button alwaysTruncateRadio;
	protected Button neverTruncateRadio;
	protected Button useSkinsCheckbox;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 * @param oldDevicesList 
	 */
	public ManageDevicesDialog(Shell parent, int style, DevicesList oldDevicesList) {
		super(parent, style);
		setText(Messages.ManageDevicesDialog_PREFERENCES);
		this.oldDevicesList = oldDevicesList;
		this.devices = new ArrayList<Device>(oldDevicesList.getDevices());
		this.selectedDeviceIndex = oldDevicesList.getSelectedDeviceIndex();
		this.useSkins = oldDevicesList.getUseSkins();
		this.truncateWindow = oldDevicesList.getTruncateWindow();
	} 

	/**
	 * Open the dialog.
	 * @return the newDevicesList
	 */
	public DevicesList open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return resultDevicesList;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(750, 500);
		shell.setText(getText());
		shell.setLayout(new GridLayout(1, false));
		
		Group devicesGroup = new Group(shell, SWT.NONE);
		devicesGroup.setText(Messages.ManageDevicesDialog_DEVICES);
		devicesGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		devicesGroup.setLayout(new GridLayout(2, false));

		table = new Table(devicesGroup, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				selectedDeviceIndex = ((Table)e.widget).getSelectionIndex();
			}
		});
		
		
		TableColumn tableColumnName = new TableColumn(table, SWT.NONE);
		tableColumnName.setWidth(175);
		tableColumnName.setText(Messages.ManageDevicesDialog_NAME);
		
		TableColumn tableColumnWidth = new TableColumn(table, SWT.NONE);
		tableColumnWidth.setWidth(75);
		tableColumnWidth.setText(Messages.ManageDevicesDialog_WIDTH);
		
		TableColumn tableColumnHeight = new TableColumn(table, SWT.NONE);
		tableColumnHeight.setWidth(75);
		tableColumnHeight.setText(Messages.ManageDevicesDialog_HEIGHT);
		
		TableColumn tableColumnPixelRatio = new TableColumn(table, SWT.NONE);
		tableColumnPixelRatio.setWidth(75);
		tableColumnPixelRatio.setText(Messages.ManageDevicesDialog_PIXEL_RATIO);
		
		TableColumn tableColumnUseragent = new TableColumn(table, SWT.NONE);
		tableColumnUseragent.setWidth(150);
		tableColumnUseragent.setText(Messages.ManageDevicesDialog_USER_AGENT);
		
		TableColumn tableColumnSkin = new TableColumn(table, SWT.NONE);
		tableColumnSkin.setWidth(75);
		tableColumnSkin.setText(Messages.ManageDevicesDialog_SKIN);
		
		Composite compositeControls = new Composite(devicesGroup, SWT.NONE);
		compositeControls.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		compositeControls.setLayout(new FillLayout(SWT.VERTICAL));
		
		Button buttonAdd = new Button(compositeControls, SWT.NONE);
		buttonAdd.setSize(88, SWT.DEFAULT);
		buttonAdd.setText(Messages.ManageDevicesDialog_ADD);
		buttonAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Device newDevice = new EditDeviceDialog(shell,  SWT.APPLICATION_MODAL | SWT.SHELL_TRIM,
						new Device(Messages.ManageDevicesDialog_NEW_DEVICE, 320, 480, 1.0, Messages.ManageDevicesDialog_NEW_USER_AGENT, null)).open();
				if (newDevice != null) {
					devices.add(newDevice);
					selectedDeviceIndex = devices.size() - 1;
					updateDevices();
				}
			}
		});
		
		Button buttonEdit = new Button(compositeControls, SWT.NONE);
		buttonEdit.setText(Messages.ManageDevicesDialog_EDIT);
		buttonEdit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Device newDevice = new EditDeviceDialog(shell,  SWT.APPLICATION_MODAL | SWT.SHELL_TRIM,
						devices.get(selectedDeviceIndex)).open();
				if (newDevice != null) {
					devices.remove(selectedDeviceIndex);
					devices.add(selectedDeviceIndex, newDevice);
					updateDevices();
				}
			}
		});
		
		Button buttonRemove = new Button(compositeControls, SWT.NONE);
		buttonRemove.setText(Messages.ManageDevicesDialog_REMOVE);
		buttonRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (devices.size() > 1) {
					devices.remove(selectedDeviceIndex);
					if (selectedDeviceIndex >= devices.size()) {
						selectedDeviceIndex = devices.size() - 1;
					}
					updateDevices();
				}
			}
		});
		
		Button buttonReset = new Button(compositeControls, SWT.NONE);
		buttonReset.setText(Messages.ManageDevicesDialog_REVERT_ALL);
		
		buttonReset.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				devices  = new ArrayList<Device>(oldDevicesList.getDevices());
				selectedDeviceIndex = oldDevicesList.getSelectedDeviceIndex();
				updateDevices();
			}
		});
		
		Group useSkinsGroup = new Group(shell, SWT.NONE);
		useSkinsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		useSkinsGroup.setLayout(new RowLayout(SWT.VERTICAL));
		useSkinsGroup.setText(Messages.ManageDevicesDialog_SKINS_OPTIONS);
		useSkinsCheckbox = new Button(useSkinsGroup, SWT.CHECK);
		useSkinsCheckbox.setText(Messages.ManageDevicesDialog_USE_SKINS);
		useSkinsCheckbox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button checkbox = (Button) e.widget;
				useSkins = checkbox.getSelection();
			}
		});
		
		Group truncateWindowGroup = new Group(shell, SWT.NONE);
		truncateWindowGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		truncateWindowGroup.setText(Messages.ManageDevicesDialog_TRUNCATE_THE_DEVICE_WINDOW);
		truncateWindowGroup.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		
		alwaysTruncateRadio = new Button(truncateWindowGroup, SWT.RADIO);
		alwaysTruncateRadio.setText(Messages.ManageDevicesDialog_ALWAYS_TRUNCATE);
		
		neverTruncateRadio = new Button(truncateWindowGroup, SWT.RADIO);
		neverTruncateRadio.setText(Messages.ManageDevicesDialog_NEVER_TRUNCATE);

		askBeforeTruncateRadio = new Button(truncateWindowGroup, SWT.RADIO);
		askBeforeTruncateRadio.setText(Messages.ManageDevicesDialog_PROMPT);
		
		SelectionListener truncateSelectionListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button radio = (Button) e.widget;
				if (radio.getSelection()) {
					if (radio == askBeforeTruncateRadio) {
						truncateWindow = null;
					} else if (radio == alwaysTruncateRadio) {
						truncateWindow = true;
					} else if (radio == neverTruncateRadio) {
						truncateWindow = false;
					}
				}
			}
		}; 
		
		askBeforeTruncateRadio.addSelectionListener(truncateSelectionListener);
		alwaysTruncateRadio.addSelectionListener(truncateSelectionListener);
		neverTruncateRadio.addSelectionListener(truncateSelectionListener);
		
		
		Composite compositeOkCancel = new Composite(shell, SWT.NONE);
		compositeOkCancel.setLayout(new GridLayout(2, true));
		compositeOkCancel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
		
		new Label(compositeOkCancel, SWT.NONE);
		
		Button buttonLoadDefaults = new Button(compositeOkCancel, SWT.NONE);
		buttonLoadDefaults.setText(Messages.ManageDevicesDialog_LOAD_DEFAULTS);
		buttonLoadDefaults.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DevicesList defaultDevicesList = DevicesListStorage.loadDefaultDevicesList();
				devices = defaultDevicesList.getDevices();
				selectedDeviceIndex = defaultDevicesList.getSelectedDeviceIndex();
				useSkins = defaultDevicesList.getUseSkins();
				truncateWindow = defaultDevicesList.getTruncateWindow();
				updateDevices();
			}
		});
		
		Button buttonOk = new Button(compositeOkCancel, SWT.NONE);
		buttonOk.setText(Messages.ManageDevicesDialog_OK);
		buttonOk.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		shell.setDefaultButton(buttonOk);
		buttonOk.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				resultDevicesList = new DevicesList(devices, selectedDeviceIndex, useSkins, truncateWindow);
				shell.close();
			}
		});
		
		Button buttonCancel = new Button(compositeOkCancel, SWT.NONE);
		buttonCancel.setText(Messages.ManageDevicesDialog_CANCEL);
		buttonCancel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		buttonCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				resultDevicesList = null;
				shell.close();
			}
		});
		

		updateDevices();
	}
	
	public void updateDevices() {
		table.removeAll();
		for (Device device : devices) {
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(new String[] {
					device.getName(), 
					device.getWidth() == Device.DEFAULT_SIZE ? Messages.ManageDevicesDialog_DEFAULT : String.valueOf(device.getWidth()),
					device.getHeight() == Device.DEFAULT_SIZE ? Messages.ManageDevicesDialog_DEFAULT : String.valueOf(device.getHeight()),
					Device.PIXEL_RAIO_FORMAT.format(device.getPixelRatio()),
					device.getUserAgent() == null ? Messages.ManageDevicesDialog_DEFAULT : device.getUserAgent(),
					device.getSkinId() == null ?  Messages.ManageDevicesDialog_NONE : device.getSkinId()
			});
		}
		table.setSelection(selectedDeviceIndex);
		
		useSkinsCheckbox.setSelection(useSkins);
		
		askBeforeTruncateRadio.setSelection(truncateWindow == null);
		alwaysTruncateRadio.setSelection(truncateWindow != null && Boolean.TRUE.equals(truncateWindow));
		neverTruncateRadio.setSelection(truncateWindow != null && Boolean.FALSE.equals(truncateWindow));
	}
}
