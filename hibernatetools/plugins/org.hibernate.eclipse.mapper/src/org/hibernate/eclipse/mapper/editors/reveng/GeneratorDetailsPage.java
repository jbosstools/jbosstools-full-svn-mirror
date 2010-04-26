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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.hibernate.eclipse.console.model.IRevEngGenerator;
import org.hibernate.eclipse.mapper.MapperMessages;
import org.hibernate.eclipse.mapper.editors.reveng.xpl.FormTextEntry;

public class GeneratorDetailsPage extends RevEngDetailsPage implements IDetailsPage, PropertyChangeListener {

	//private Label label;
	private FormTextEntry nameEntry;

	private IRevEngGenerator generator;

	protected void buildContents(FormToolkit toolkit, Section section, Composite client) {
		section.setText(MapperMessages.GeneratorDetailsPage_id_generator_details);
		section.setDescription(MapperMessages.GeneratorDetailsPage_set_the_properties_of_the_selected_generator);

		nameEntry = new FormTextEntry(client, toolkit, MapperMessages.GeneratorDetailsPage_class, SWT.NULL);
		nameEntry.setFormEntryListener(new FormTextEntryListenerAdapter() {
			public void textValueChanged(FormTextEntry entry) {
				generator.setGeneratorClassName(entry.getValue());
			}
		});
		
		Button button = toolkit.createButton(client, MapperMessages.GeneratorDetailsPage_add_param, SWT.NULL);
		button.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleAddParameter();
			}

		});
	}

	protected void handleAddParameter() {
		generator.addParameter();
	}

	public void selectionChanged(IFormPart part, ISelection selection) {
		IRevEngGenerator newTable = (IRevEngGenerator) ((IStructuredSelection)selection).getFirstElement();
		if(generator!=null) {
			generator.removePropertyChangeListener(this);
		}
		if(newTable!=null) {
			newTable.addPropertyChangeListener(this);
		}
		generator = newTable;

		update();
	}

	private void update() {
		nameEntry.setValue(generator.getGeneratorClassName());
	}

	public void propertyChange(PropertyChangeEvent evt) {
		update();
	}

}
