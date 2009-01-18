/**
 * 
 */
package org.jboss.tools.smooks.ui;

import java.util.Iterator;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.PropertyModel;

/**
 * 
 * @author Dart Peng<br>
 *         Date : Sep 4, 2008
 */
public class ConnectionPropertySection extends AbstractSmooksPropertySection {

	private TableViewer tableViewer;

	/**
	 * 
	 */
	public ConnectionPropertySection() {
	}

	@Override
	public void createControls(Composite rootParent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(rootParent, tabbedPropertySheetPage);
		TabbedPropertySheetWidgetFactory factory = this.getWidgetFactory();
		Composite parent = factory.createComposite(rootParent);
		FillLayout fill = new FillLayout();
		fill.marginHeight = 8;
		fill.marginWidth = 8;
		parent.setLayout(fill);
		GridData pgd = new GridData(GridData.FILL_BOTH);
		pgd.grabExcessHorizontalSpace = true;
		pgd.grabExcessVerticalSpace = true;
		rootParent.setLayoutData(pgd);
		Section section = factory.createSection(parent, Section.TITLE_BAR);
		section
				.setText(Messages
						.getString("ConnectionPropertySection.ConnectionPropertiesSecionText")); //$NON-NLS-1$

		Composite mainComposite = factory.createComposite(section);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		mainComposite.setLayout(gridLayout);
		tableViewer = new TableViewer(mainComposite);
		tableViewer.setColumnProperties(new String[] { "name", "value" }); //$NON-NLS-1$ //$NON-NLS-2$
		tableViewer.setCellEditors(new CellEditor[] {
				new TextCellEditor(tableViewer.getTable()),
				new TextCellEditor(tableViewer.getTable()) });
		tableViewer.setCellModifier(new ICellModifier() {

			public boolean canModify(Object element, String property) {
				// TODO Auto-generated method stub
				return true;
			}

			public Object getValue(Object element, String property) {
				if (element instanceof PropertyModel) {
					if (property.equals("name")) //$NON-NLS-1$
						return ((PropertyModel) element).getName();
					if (property.equals("value")) //$NON-NLS-1$
						return ((PropertyModel) element).getValue();
				}
				return element;
			}

			public void modify(Object item, String property, Object value) {
				Object element = null;
				if (item instanceof TableItem) {
					element = ((TableItem) item).getData();

				}
				if (element instanceof PropertyModel && value instanceof String) {
					if (property.equals("name")) { //$NON-NLS-1$
						((PropertyModel) element).setName((String) value);
						fireDirty();
					}
					if (property.equals("value")) { //$NON-NLS-1$
						((PropertyModel) element).setValue((String) value);
						fireDirty();
					}
					refresh();
				}
			}

		});
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.setContentProvider(new IStructuredContentProvider() {

			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof AbstractStructuredDataConnectionModel) {
					return ((AbstractStructuredDataConnectionModel) inputElement)
							.getPropertyArray();
				}
				return new Object[] {};
			}

			public void dispose() {

			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {

			}

		});
		// tableViewer.setLabelProvider(new PropertyLabelProvider());

		// TableViewerColumn blankColumn = new TableViewerColumn(tableViewer,
		// SWT.NONE);
		// blankColumn.getColumn().setWidth(100);
		// blankColumn.getColumn().setText(" ");
		TableViewerColumn nameColumn = new TableViewerColumn(tableViewer,
				SWT.NONE);
		nameColumn.setLabelProvider(new CellLabelProvider() {

			public void update(ViewerCell cell) {
				Object obj = cell.getElement();
				if (obj instanceof PropertyModel) {
					cell.setText(((PropertyModel) obj).getName());
				}
			}

		});
		nameColumn.getColumn().setWidth(100);
		nameColumn.getColumn().setText(
				Messages.getString("ConnectionPropertySection.NameColumnText")); //$NON-NLS-1$
		TableViewerColumn valueColumn = new TableViewerColumn(tableViewer,
				SWT.NONE);
		valueColumn.getColumn().setWidth(100);
		valueColumn
				.getColumn()
				.setText(
						Messages
								.getString("ConnectionPropertySection.ValueColumnText")); //$NON-NLS-1$
		valueColumn.setLabelProvider(new CellLabelProvider() {

			public void update(ViewerCell cell) {
				Object obj = cell.getElement();
				if (obj instanceof PropertyModel) {
					Object value = ((PropertyModel) obj).getValue();
					if (value != null)
						cell.setText(value.toString());
				}
			}

		});
		section.setClient(mainComposite);
		GridData gd = new GridData(GridData.FILL_BOTH);
		tableViewer.getTable().setLayoutData(gd);

		Composite buttonComposite = factory.createComposite(mainComposite);
		gd = new GridData(GridData.FILL_VERTICAL);
		buttonComposite.setLayoutData(gd);
		GridLayout gl = new GridLayout();
		buttonComposite.setLayout(gl);

		Button button1 = factory.createButton(buttonComposite, Messages
				.getString("ConnectionPropertySection.NewButtonText"), //$NON-NLS-1$
				SWT.NONE);
		button1.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				PropertyModel model = new PropertyModel();
				model.setName("property"); //$NON-NLS-1$
				model.setValue("value"); //$NON-NLS-1$

				Object editPart = ((IStructuredSelection) getSelection())
						.getFirstElement();
				if (editPart instanceof EditPart) {
					Object m = ((EditPart) editPart).getModel();
					if (m instanceof AbstractStructuredDataConnectionModel) {
						((AbstractStructuredDataConnectionModel) m)
								.addPropertyModel(model);
						refresh();
						StructuredSelection selection = new StructuredSelection(
								model);
						tableViewer.setSelection(selection);
					}
				}
			}

		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		button1.setLayoutData(gd);

		Button button2 = factory.createButton(buttonComposite, Messages
				.getString("ConnectionPropertySection.DeleteButtonText"), //$NON-NLS-1$
				SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		button2.setLayoutData(gd);
		button2.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);

				Object editPart = ((IStructuredSelection) getSelection())
						.getFirstElement();
				if (editPart instanceof EditPart) {
					Object m = ((EditPart) editPart).getModel();
					if (m instanceof AbstractStructuredDataConnectionModel) {
						IStructuredSelection selection = (IStructuredSelection) tableViewer
								.getSelection();
						Iterator it = selection.iterator();
						while (it.hasNext()) {
							PropertyModel model = (PropertyModel) it.next();
							((AbstractStructuredDataConnectionModel) m)
									.removePropertyModel(model);
						}
						refresh();
					}
				}
			}

		});
		// Button button3 = factory.createButton(buttonComposite, "New ",
		// SWT.NONE);
		// gd = new GridData(GridData.FILL_VERTICAL);
		// button3.setLayoutData(gd);

		factory.paintBordersFor(parent);
	}

	@Override
	public void refresh() {
		super.refresh();
		IStructuredSelection selection = (IStructuredSelection) this
				.getSelection();
		Object obj = selection.getFirstElement();
		if (obj == null)
			return;
		if (obj instanceof EditPart) {
			Object model = ((EditPart) obj).getModel();
			this.tableViewer.setInput(model);
		}
	}

	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
	}

	private class PropertyLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			switch (columnIndex) {
			case 0:
				if (element instanceof PropertyModel) {
					return ((PropertyModel) element).getName();
				}
				return element.toString();

			case 1:
				if (element instanceof PropertyModel) {
					Object value = ((PropertyModel) element).getValue();
					return value.toString();
				}
				return element.toString();
			}
			return element.toString();
		}

	}

}
