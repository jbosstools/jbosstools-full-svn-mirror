/**
 * 
 */
package org.jboss.tools.smooks.ui.editors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.smooks.model.ParamType;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.SmooksFactory;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;
import org.jboss.tools.smooks.ui.gef.util.GraphicsConstants;

/**
 * @author Dart
 * 
 */
public class ParamaterTableViewerCreator extends Composite {
	private TableViewer viewer;
	private FormToolkit formToolKit;

	private List<ParamaterChangeLitener> listenerList = new ArrayList<ParamaterChangeLitener>();

	public static final String PARAM_NAME_PRO = "__param_name_pro";

	public static final String PARAM_VALUE_PRO = "__param_value_pro";

	private ResourceConfigType resourceConfig;

	public ResourceConfigType getResourceConfig() {
		return resourceConfig;
	}

	public void setResourceConfig(ResourceConfigType resourceConfig) {
		this.resourceConfig = resourceConfig;
	}

	public void setInput(Object input) {
		viewer.setInput(input);
	}

	public ParamaterTableViewerCreator(Composite parent,
			FormToolkit formToolKit, int style) {
		super(parent, style);
		this.formToolKit = formToolKit;
		createParamaterTableViewer(this.formToolKit, parent);
	}

	private Button createButtonForParamerTable(Composite parent, String label) {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		Button button = formToolKit.createButton(parent, label, SWT.NONE);
		button.setLayoutData(gd);
		return button;
	}

	public void addParamaterListener(ParamaterChangeLitener listener) {
		this.listenerList.add(listener);
	}

	public void removeParamaterListener(ParamaterChangeLitener listener) {
		this.listenerList.remove(listener);
	}

	protected void createParamaterTableViewer(FormToolkit formToolKit,
			Composite parent) {
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		this.setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;

		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.numColumns = 2;
		this.setLayout(layout);

		Composite tableContainer = formToolKit.createComposite(this);

		FillLayout filllayout = new FillLayout();
		filllayout.marginHeight = 1;
		filllayout.marginWidth = 1;
		tableContainer.setLayout(filllayout);
		viewer = new TableViewer(tableContainer, SWT.NONE | SWT.MULTI);

		TableColumn nameColumn = new TableColumn(viewer.getTable(), SWT.NONE);
		nameColumn.setText("Paramer Name");
		nameColumn.setWidth(150);

		TableColumn valueColumn = new TableColumn(viewer.getTable(), SWT.NONE);
		valueColumn.setText("Paramer Value");
		valueColumn.setWidth(100);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		viewer.setContentProvider(new ParamaterContentProvider());
		viewer.setLabelProvider(new ParamaterLabelProvider());
		viewer.setColumnProperties(new String[] { PARAM_NAME_PRO,
				PARAM_VALUE_PRO });
		viewer.setCellEditors(new CellEditor[] {
				new TextCellEditor(viewer.getTable()),
				new TextCellEditor(viewer.getTable()) });
		viewer.setCellModifier(new ICellModifier() {

			public boolean canModify(Object element, String property) {
				if (element instanceof ParamType) {
					if (property.equals(PARAM_NAME_PRO)
							|| property.equals(PARAM_VALUE_PRO))
						return true;
				}
				return false;
			}

			public Object getValue(Object element, String property) {
				Object data = element;
				if (data instanceof ParamType) {
					if (property.equals(PARAM_NAME_PRO)) {
						String name = ((ParamType) data).getName();
						if (name == null)
							name = "";
						return name;
					}
					if (property.equals(PARAM_VALUE_PRO)) {
						String value = SmooksModelUtils
								.getAnyTypeText((ParamType) data);
						if (value == null)
							value = "";
						return value;
					}
				}
				return null;
			}

			public void modify(Object element, String property, Object value) {
				if (element instanceof Item) {
					Object data = ((Item) element).getData();
					if (data instanceof ParamType) {
						if (property.equals(PARAM_NAME_PRO)) {
							if (value.equals(((ParamType) data).getName()))
								return;
							((ParamType) data).setName((String) value);
							viewer.refresh(data);
						}
						if (property.equals(PARAM_VALUE_PRO)) {
							if (value.equals(SmooksModelUtils
									.getAnyTypeText((ParamType) data)))
								return;
							SmooksModelUtils.setTextToAnyType((ParamType) data,
									(String) value);
							viewer.refresh(data);
						}
						notifyParamChanged((ParamType) data);
					}
				}
			}
		});

		tableContainer.setBackground(GraphicsConstants.groupBorderColor);
		gd = new GridData(GridData.FILL_BOTH);
		tableContainer.setLayoutData(gd);

		Composite buttonComposite = formToolKit.createComposite(this);
		gd = new GridData(GridData.FILL_VERTICAL);
		buttonComposite.setLayoutData(gd);
		GridLayout bgl = new GridLayout();
		buttonComposite.setLayout(bgl);

		Button newButton = createButtonForParamerTable(buttonComposite, "New");
		newButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				if(resourceConfig == null) return;
				ParamType param = SmooksFactory.eINSTANCE.createParamType();
				param.setName("param");
				SmooksModelUtils.setTextToAnyType(param, "value");
				resourceConfig.getParam().add(param);
				viewer.refresh();
				notifyParamaterAdded(param);
			}

		});

		Button deleteButton = createButtonForParamerTable(buttonComposite,
				"Delete");
		deleteButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				try {
					if(resourceConfig == null) return;
					resourceConfig.getParam().removeAll(selection.toList());
					viewer.refresh();
					notifyParamaterRemoved(selection.toList());
				} catch (Throwable t) {
					// ignore;
				}
			}

		});
	}

	protected void notifyParamChanged(ParamType data) {
		for (Iterator iterator = this.listenerList.iterator(); iterator
				.hasNext();) {
			ParamaterChangeLitener listener = (ParamaterChangeLitener) iterator
					.next();
			listener.paramaterChanged(data);
		}
	}

	public void notifyParamaterRemoved(List params) {
		for (Iterator iterator = this.listenerList.iterator(); iterator
				.hasNext();) {
			ParamaterChangeLitener listener = (ParamaterChangeLitener) iterator
					.next();
			listener.paramaterRemoved(params);
		}
	}

	public void notifyParamaterAdded(ParamType param) {
		for (Iterator iterator = this.listenerList.iterator(); iterator
				.hasNext();) {
			ParamaterChangeLitener listener = (ParamaterChangeLitener) iterator
					.next();
			listener.paramaterAdded(param);
		}
	}

	private class ParamaterLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			if (element instanceof ParamType) {
				if (columnIndex == 0) {

				}
			}
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof ParamType) {
				if (columnIndex == 0) {
					return ((ParamType) element).getName();
				}
				if (columnIndex == 1) {
					String value = SmooksModelUtils
							.getAnyTypeText((ParamType) element);
					if (value == null)
						value = "";
					value = value.trim();
					return value;
				}
			}
			return "";
		}

	}

	private class ParamaterContentProvider implements
			IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ResourceConfigType) {
				List<ParamType> paramList = ((ResourceConfigType) inputElement)
						.getParam();
				return paramList.toArray();
			}
			return new Object[] {};
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

	}
}
