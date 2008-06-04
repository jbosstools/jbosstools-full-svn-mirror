/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.hibernate.eclipse.mapper.editors.reveng;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.hibernate.eclipse.console.model.IReverseEngineeringDefinition;
import org.hibernate.eclipse.console.wizards.TableFilterView;
import org.hibernate.eclipse.console.workbench.LazyDatabaseSchema;
import org.hibernate.eclipse.mapper.editors.ReverseEngineeringEditor;

public class TableFilterFormPart extends RevEngSectionPart {

	private TableFilterView composite;
	private final ReverseEngineeringEditor configNamePart;


	public TableFilterFormPart(Composite body, IManagedForm form, ReverseEngineeringEditor configNamePart) {
		super(body,form);
		this.configNamePart = configNamePart;
	}

	protected String getSectionTitle() {
		return Messages.TABLEFILTERFORMPART_TABLE_FILTERS;
	}
	
	protected String getSectionDescription() {
		return Messages.TABLEFILTERFORMPART_TABLE_FILTERS_DEFINES_WHICH_TABLE_INCLUDED;
	}
	
	public Control createClient(IManagedForm form) {		
		FormToolkit toolkit = form.getToolkit();
		composite = new TableFilterView(getSection(), SWT.NULL) {

			protected void doRefreshTree() {
				LazyDatabaseSchema lazyDatabaseSchema = configNamePart.getLazyDatabaseSchema();
				if(lazyDatabaseSchema!=null) {
					viewer.setInput( lazyDatabaseSchema );
				}
			}
			protected String getConsoleConfigurationName() {
				return configNamePart.getConsoleConfigurationName();
			}			

		};
			
		adaptRecursively( toolkit, composite );

		return composite;
	}

	public boolean setFormInput(IReverseEngineeringDefinition reveng) {
		composite.setModel(reveng);
		return true;
	}
	
	public void dispose() {
		composite.dispose();
	}
}
