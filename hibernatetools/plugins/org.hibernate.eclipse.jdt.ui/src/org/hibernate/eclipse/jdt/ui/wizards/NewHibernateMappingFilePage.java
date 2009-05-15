/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.hibernate.eclipse.jdt.ui.wizards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.hibernate.console.ImageConstants;
import org.hibernate.eclipse.console.HibernateConsoleMessages;
import org.hibernate.eclipse.console.utils.EclipseImages;
import org.hibernate.eclipse.jdt.ui.internal.jpa.common.EntityInfo;

/**
 * @author Dmitry Geraskov
 *
 */
public class NewHibernateMappingFilePage extends WizardPage {

	private TableViewer viewer;

	/**
	 * @param pageName
	 */
	protected NewHibernateMappingFilePage() {
		super("");
		setTitle(HibernateConsoleMessages.NewHibernateMappingFilePage_hibernate_xml_mapping_file);
		setDescription(HibernateConsoleMessages.NewHibernateMappingFilePage_this_wizard_creates);
	}

	public void createControl(Composite parent) {

		final ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.pack(false);

		Composite container = new Composite(sc, SWT.NULL);
		sc.setContent(container);

		Layout layout = new FillLayout();
		container.setLayout(layout);

		Table table =  new Table(container, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION );
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.pack(false);
		createTableColumns(table);
		viewer = createTableViewer(table);
		viewer.setInput(null);

		sc.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		setControl(container);
	}

	private void createTableColumns(Table table){
		int coulmnIndex = 0;
		TableColumn column =  new TableColumn(table, SWT.CENTER, coulmnIndex++);
		column.setText("!");
		column.setWidth(20);
		column.setResizable(false);

		//if (project_infos.keySet().size() > 1){
		column = new TableColumn(table, SWT.LEFT, coulmnIndex++);
		column.setText("Project name");
		column.setWidth(120);
		//}

		column = new TableColumn(table, SWT.LEFT, coulmnIndex++);
		column.setText("Class name");
		column.setWidth(150);

		column = new TableColumn(table, SWT.LEFT, coulmnIndex++);
		column.setText("File name");
		column.setWidth(150);
	}

	private TableViewer createTableViewer(Table table) {
		TableViewer result = new TableViewer( table );
		result.setUseHashlookup( true );
		//if (project_infos.keySet().size() > 1){
		result.setColumnProperties( new String[] {"create", "project",   //$NON-NLS-1$//$NON-NLS-2$
				"class", "file",} ); //$NON-NLS-1$ //$NON-NLS-2$
		/*} else {
			result.setColumnProperties( new String[] {"create",   //$NON-NLS-1$
					"class", "file",} ); //$NON-NLS-1$ //$NON-NLS-2$
		}*/

		CellEditor[] editors = new CellEditor[result.getColumnProperties().length];
		editors[0] = new CheckboxCellEditor( result.getTable() );
		editors[1] = new TextCellEditor( result.getTable() );
		editors[2] = new TextCellEditor( result.getTable() );
		//if (project_infos.keySet().size() > 1){
		editors[3] = new TextCellEditor( result.getTable() );
		//}


		result.setCellEditors( editors );
		result.setCellModifier( new TableCellModifier(result) );
		result.setLabelProvider(new TableLableProvider(result));
		result.setContentProvider( new TableContentProvider() );
		return result;
	}

	public void setInput(Map<IJavaProject, Collection<EntityInfo>> project_infos){
		viewer.setInput(project_infos);
	}

	private class TableLine {

		public String projectName;

		public String className;

		public String fileName;

		public Boolean isCreate = true;

		public TableLine(String projectName, String className){
			this(projectName, className, className + ".hbm.xml",true);
		}

		public TableLine(String projectName, String className, String fileName, boolean isCreate){
			this.projectName = projectName;
			this.className = className;
			this.fileName = fileName;
			this.isCreate = isCreate;
		}

	}

	private class TableContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof Map) {
				List<TableLine> result = new ArrayList<TableLine>();
				Map<IJavaProject, Collection<EntityInfo>> configs = (Map<IJavaProject, Collection<EntityInfo>>)inputElement;
				for (Entry<IJavaProject, Collection<EntityInfo>> entry : configs.entrySet()) {
					Iterator<EntityInfo> iter = entry.getValue().iterator();
					while (iter.hasNext()) {
						EntityInfo ei = iter.next();
						result.add(new TableLine(entry.getKey().getProject().getName(), ei.getName()));
					}
				}
				return result.toArray();
			}
			return new Object[0];
		}

		public void dispose() {}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {	}

	}

	private class TableLableProvider extends LabelProvider implements ITableLabelProvider  {

		private final TableViewer tv;

		public TableLableProvider(TableViewer tv) {
			this.tv = tv;
		}

		public Image getColumnImage(Object element, int columnIndex) {
			String property = (String) tv.getColumnProperties()[columnIndex];
			if("create".equals(property)) {
				TableLine tl = (TableLine) element;
				String key = tl.isCreate ? null : ImageConstants.CLOSE ; // TODO: find a better image
				return EclipseImages.getImage(key);
			}
			return  null;
		}

		public String getColumnText(Object element, int columnIndex) {
			String property = (String) tv.getColumnProperties()[columnIndex];
			TableLine tl = (TableLine) element;

			if ("class".equals(property)){
				return tl.className;
			} else if ("project".equals(property)){
				return tl.projectName;
			} else if ("file".equals(property)){
				return tl.fileName;
			} else {
				return "";
			}
		}
	}

	private class TableCellModifier implements ICellModifier {

		private final TableViewer tv;

		public TableCellModifier(TableViewer tv) {
			this.tv = tv;
		}

		public boolean canModify(Object element, String property) {
			return false/*TODO "file".equals(property) || "create".equals(property)*/;
		}

		public Object getValue(Object element, String property) {
			if ("class".equals(property)){
				return ((TableLine)element).className;
			} else if ("project".equals(property)){
				return ((TableLine)element).projectName;
			} else if ("file".equals(property)){
				return ((TableLine)element).fileName;
			} else if ("create".equals(property)){
				return ((TableLine)element).isCreate;
			}
			return null;
		}

		public void modify(Object element, String property, Object value) {
			TableLine tl = (TableLine)((TableItem)element).getData();
			if ("class".equals(property)){
				tl.className = (String)value;
			} else if ("project".equals(property)){
				tl.projectName = (String)value;
			} else if ("file".equals(property)){
				tl.fileName = (String)value;
			} else if ("create".equals(property)){
				tl.isCreate = (Boolean)value;
			}

			tv.update(new Object[] { tl }, new String[] { property });
		}
	}
}
