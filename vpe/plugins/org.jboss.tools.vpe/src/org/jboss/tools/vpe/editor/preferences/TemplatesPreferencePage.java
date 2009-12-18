/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.editor.preferences;

import java.util.List;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.tools.common.model.ui.action.CommandBar;
import org.jboss.tools.common.model.ui.action.CommandBarListener;
import org.jboss.tools.common.model.ui.objecteditor.XTable;
import org.jboss.tools.vpe.editor.template.VpeAnyData;
import org.jboss.tools.vpe.editor.template.VpeEditAnyDialog;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.messages.VpeUIMessages;

public class TemplatesPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage, CommandBarListener {
	static String EDIT = VpeUIMessages.TemplatesPreferencePage_Edit; 
	static String REMOVE = VpeUIMessages.TemplatesPreferencePage_Remove; 
	protected TemplatesTableProvider tableProvider;// = new TemplatesTableProvider();
	protected XTable table = new XTable();
	protected CommandBar bar = new CommandBar();
	protected List dataList;
	protected boolean changed;
	
	public TemplatesPreferencePage() {
//		noDefaultAndApplyButton();
		setPreferenceStore(getPreferenceStore());
		init();
	}
	
	public String getTitle() {
		return VpeUIMessages.TEMPLATES;
	}
	
	private void init() {
		changed = false;
		dataList = VpeTemplateManager.getInstance().getAnyTemplates();
		tableProvider = new TemplatesTableProvider(dataList);
		
		bar.getLayout().buttonWidth = 80;
		bar.getLayout().direction = SWT.VERTICAL;
		bar.setCommands(new String[]{EDIT, REMOVE});
		bar.addCommandBarListener(this);
		table.setTableProvider(tableProvider);
	}

	protected Control createContents(Composite parent) {
		Composite c = new Composite(parent, SWT.NONE);
		c.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout g = new GridLayout(2, false);
		c.setLayout(g);
		Control slc = table.createControl(c);
		slc.setLayoutData(new GridData(GridData.FILL_BOTH));
		Control bc = bar.createControl(c);
		GridData gd = new GridData(GridData.FILL_VERTICAL);
		bc.setLayoutData(gd);
		table.getTable().addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				updateBars();
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		update();
		return c;
	}

	public void init(IWorkbench workbench) {
	}

	public void action(String command) {
		int index = table.getSelectionIndex();
		if(EDIT.equals(command)) {
			edit(index);
		} else if(REMOVE.equals(command)) {
			remove(index);
		}
		update();
	}
	public boolean performOk() {
		if(changed) {
			VpeTemplateManager.getInstance().setAnyTemplates(dataList);
		}
		return super.performOk();
	}
	
	void edit(int index) {
		VpeAnyData data = (VpeAnyData)dataList.get(index);
		VpeEditAnyDialog editDialog = new VpeEditAnyDialog(getShell(), data);
		editDialog.open();
		if(data.isChanged()){
			changed = true;
		}
	}
	
	void remove(int index) {
		dataList.remove(index);
		changed = true;
	}

	public void update() {
		if(table != null){
			table.update();
		}
		updateBars();
	}

	void updateBars() {
		bar.setEnabled(EDIT, canModify());
		bar.setEnabled(REMOVE, canModify());
	}

	private boolean canModify() {
		return (table.getSelectionIndex() >= 0);
	}
	
}
