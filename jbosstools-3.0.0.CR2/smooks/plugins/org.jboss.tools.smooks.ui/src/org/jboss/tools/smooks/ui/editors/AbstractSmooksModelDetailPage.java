/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.ui.editors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.ui.ResourceConfigWarrper;

/**
 * @author Dart Peng<br>
 *         Date : Sep 11, 2008
 */
public abstract class AbstractSmooksModelDetailPage implements IDetailsPage {
	IFormPart formPart;

	ISelection selection;
	
	protected boolean canFireChange = false;;

	FormToolkit formToolKit = null;

	protected ResourceConfigType oldResourceConfigList;

	protected ResourceConfigType resourceConfig;
	
	protected IManagedForm managedForm ;

	EditingDomain domain;
	SmooksFormEditor parentEditor;

	public AbstractSmooksModelDetailPage(SmooksFormEditor parentEditor,
			EditingDomain domain) {
		this.domain = domain;
		this.parentEditor = parentEditor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.IDetailsPage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	public void createContents(Composite parent) {
		parent.setLayout(new FillLayout());
		Section section = formToolKit.createSection(parent, Section.DESCRIPTION
				| Section.TITLE_BAR);
		section.setText(Messages.getString("AbstractSmooksModelDetailPage.DetailSectionTitle")); //$NON-NLS-1$

		Composite client = formToolKit.createComposite(section);
		section.setLayout(new FillLayout());

		section.setClient(client);

		createSectionContents(client);

	}

	abstract protected void createSectionContents(Composite parent);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 */
	public void commit(boolean onSave) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.IFormPart#dispose()
	 */
	public void dispose() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm)
	 */
	public void initialize(IManagedForm form) {
		this.managedForm = form;
		if (form != null) {
			formToolKit = form.getToolkit();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.IFormPart#isDirty()
	 */
	public boolean isDirty() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.IFormPart#isStale()
	 */
	public boolean isStale() {
		if (oldResourceConfigList != resourceConfig) {
			oldResourceConfigList = resourceConfig;
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.IFormPart#refresh()
	 */
	public void refresh() {
		canFireChange = false;
		initSectionUI();
		canFireChange = true;
	}

	abstract protected void initSectionUI() ;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.IFormPart#setFocus()
	 */
	public void setFocus() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
	 */
	public boolean setFormInput(Object input) {
		return false;
	}
	
	public SmooksResourceListType getSmooksResourceList(){
		if(resourceConfig != null){
			EObject parent = resourceConfig.eContainer();
			while(parent != null){
				EObject temp = parent.eContainer();
				if(temp == null){
					break;
				}
				parent = temp;
				if(parent instanceof SmooksResourceListType){
					break;
				}
			}
			return (SmooksResourceListType) parent;
		}
		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.IPartSelectionListener#selectionChanged(org.eclipse.ui.forms.IFormPart,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IFormPart part, ISelection selection) {
		this.selection = selection;
		formPart = part;
		if (selection != null && selection instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection) selection)
			.getFirstElement();
			if(obj instanceof ResourceConfigType){
				resourceConfig = (ResourceConfigType)obj;
			}
			if(obj instanceof ResourceConfigWarrper){
				resourceConfig = ((ResourceConfigWarrper)obj).getResourceConfig();
			}
			refresh();
		}
	}

	protected IFormPart getFormPart() {
		return formPart;
	}

	protected void setFormPart(IFormPart formPart) {
		this.formPart = formPart;
	}

	protected ISelection getSelection() {
		return selection;
	}

	protected void setSelection(ISelection selection) {
		this.selection = selection;
	}

	protected FormToolkit getFormToolKit() {
		return formToolKit;
	}

	protected void setFormToolKit(FormToolkit formToolKit) {
		this.formToolKit = formToolKit;
	}

	protected ResourceConfigType getResourceConfigList() {
		return resourceConfig;
	}

	protected void setResourceConfigList(ResourceConfigType resourceConfigList) {
		this.resourceConfig = resourceConfigList;
	}

	protected EditingDomain getDomain() {
		return domain;
	}

	protected void setDomain(EditingDomain domain) {
		this.domain = domain;
	}

	public boolean isCanFireChange() {
		return canFireChange;
	}

	public void setCanFireChange(boolean canFireChange) {
		this.canFireChange = canFireChange;
	}

}
