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
package org.jboss.tools.vpe.resref.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jboss.tools.common.model.ui.action.CommandBar;
import org.jboss.tools.common.model.ui.action.CommandBarListener;
import org.jboss.tools.common.model.ui.objecteditor.XTable;
import org.jboss.tools.common.resref.core.ResourceReference;
import org.jboss.tools.common.resref.core.ResourceReferenceList;
import org.jboss.tools.common.resref.ui.ResourceReferencesTableProvider;

public abstract class AbstractResourceReferencesComposite {
	protected static String ADD = Messages.VRD_LABEL_ADD;
	protected static String EDIT = Messages.VRD_LABEL_EDIT;
	protected static String REMOVE = Messages.VRD_LABEL_REMOVE;
	protected XTable table = new XTable();
	protected CommandBar bar = new CommandBar();
	protected ResourceReferencesTableProvider tableProvider;// = new TemplatesTableProvider();
//	protected IFile file;
//	protected IPath path;
	/*
	 * Object representing file location.
	 * Can be IFile or IPath.
	 */
	protected Object fileLocation = null;
	protected List dataList = new ArrayList();
	
	public AbstractResourceReferencesComposite() {
		init();
	}

	private void init() {
		tableProvider = createTableProvider(dataList);
		bar.getLayout().buttonWidth = 80;
		bar.getLayout().direction = SWT.VERTICAL;
		bar.setCommands(new String[]{ADD, EDIT, REMOVE});
		bar.addCommandBarListener(new BarListener());
		table.setTableProvider(tableProvider);
	}
	
	protected abstract ResourceReferencesTableProvider createTableProvider(List dataList);
	protected abstract ResourceReferenceList getReferenceList();
	protected abstract ReferenceWizardDialog getDialog(ResourceReference resref);
	
	public void setObject(Object fileLocation) {
		this.fileLocation = fileLocation;
		ResourceReference[] rs = null;							
		if (null != fileLocation) {
			if (fileLocation instanceof IFile) {
				rs = getReferenceList().getAllResources((IFile) fileLocation);
			} else if (fileLocation instanceof IPath) {
				rs = getReferenceList().getAllResources((IPath) fileLocation);
			}
		} else {
			rs = new ResourceReference[0];
		}							
		for (int i = 0; i < rs.length; i++) {
			dataList.add(rs[i]);	
		}
	}

	public Control createControl(Composite parent) {
		
		final Composite composite = new Composite(parent,SWT.NONE);
		
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout g = new GridLayout(2, false);
		composite.setLayout(g);

	    
		Control slc = table.createControl(composite);
		slc.setLayoutData(new GridData(GridData.FILL_BOTH));
		Control bc = bar.createControl(composite);
		
	
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
		return composite;
	}
	
	protected ResourceReference[] getReferenceArray() {
		return (ResourceReference[])dataList.toArray(new ResourceReference[0]);
	}
	
	/**
	 * Clear all entries from table.
	 */
	public void clearAll(){
	    if(this.dataList!=null){
	        this.dataList.clear();
	    }
	}
	
	public void commit() {
		if (null != fileLocation) {
			if (fileLocation instanceof IFile) {
				getReferenceList().setAllResources((IFile) fileLocation, getReferenceArray());
			} else if (fileLocation instanceof IPath) {
				getReferenceList().setAllResources((IPath) fileLocation, getReferenceArray());
			}
		}
	}
	
	public class BarListener implements CommandBarListener {
		public void action(String command) {
			int index = table.getSelectionIndex();
			if(ADD.equals(command)) {
 				add(index);
			} else if(EDIT.equals(command)) {
				edit(index);
			} else if(REMOVE.equals(command)) {
				remove(index);
			}		
			update();
		}
	}

	abstract protected void add(int index);

    /**
     * @return
     */
	protected ResourceReference getDefaultResourceReference() {
        return new ResourceReference("", ResourceReference.FOLDER_SCOPE); //$NON-NLS-1$
    }
	
	abstract protected void edit(int index);
	
	void remove(int index) {
		if(index >= 0) dataList.remove(index);
	}

	public void update() {
		if(table != null) table.update();
		updateBars();
	}

	protected void updateBars() {
		boolean canModify = table.getSelectionIndex() >= 0;
		bar.setEnabled(EDIT, canModify);
		bar.setEnabled(REMOVE, canModify);
	}

}
