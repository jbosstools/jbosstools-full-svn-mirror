package org.jboss.ide.eclipse.jbosscache.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.jbosscache.internal.CacheMessages;
import org.jboss.ide.eclipse.jbosscache.model.CacheLoaderPropModel;
import org.jboss.ide.eclipse.jbosscache.wizards.pages.CacheLoaderLableProvider;
import org.jboss.ide.eclipse.jbosscache.wizards.pages.CacheLoaderTableProvider;

public class CacheLoaderDefDialog extends Dialog {
	
	private TableViewer tableViewer;
	
	private Button chkUseDataSource;
	private Label lblUseDataSource;
	private Text txtDataSource;
	private Button chkTableCreate;
	private Button chkTableDelete;
	
	private String[] COLUMN_NAMES = new String[]{CacheMessages.Column_Properties,CacheMessages.Column_Value};
	
	private String cacheLoaderType;

	public CacheLoaderDefDialog(Shell parentShell,String cacheLoaderString) {
		super(parentShell);
		setShellStyle(SWT.SHELL_TRIM);
		this.cacheLoaderType = cacheLoaderString;
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell
				.setText(CacheMessages.Dialog_Title);
	}

	/**
	 * Create the contents of the dialog (above the button bar).
	 * 
	 * Subclasses should overide.
	 * 
	 * @param parent
	 *            the parent composite to contain the dialog area
	 * @return the dialog area control
	 */
	protected Control createDialogArea(Composite parent) {
		Composite outer = (Composite) super.createDialogArea(parent);
		createTable(outer);

		return outer;
	}

	private boolean useDs = false;
	/**
	 * Create tree table
	 * 
	 * @param outer
	 */
	private void createTable(Composite outer) {
		
		outer.setLayout(new GridLayout(2,false));
		
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		
		GridData dChekc = new GridData(GridData.FILL_HORIZONTAL);
		dChekc.horizontalSpan = 2;
		
		chkUseDataSource = new Button(outer,SWT.CHECK);
		chkUseDataSource.setText("Use DataSource");
		chkUseDataSource.setLayoutData(dChekc);
		
		chkUseDataSource.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				
				String [] params = new String[2];
				params[0] = cacheLoaderType;
				

				chkUseDataSource = (Button)e.widget;
				if(chkUseDataSource.getSelection()){
					lblUseDataSource.setEnabled(true);
					txtDataSource.setEnabled(true);
					txtDataSource.setEnabled(true);
					useDs = true;
				}else{
					lblUseDataSource.setEnabled(false);
					txtDataSource.setEnabled(false);
					txtDataSource.setEnabled(false);
					useDs = false;
					
				}
				params[1] = Boolean.toString(useDs);
				tableViewer.setInput(params);
			}
		});
								
		lblUseDataSource = new Label(outer,SWT.NONE);
		lblUseDataSource.setText("Data Source Name: ");
		lblUseDataSource.setEnabled(false);
		
		txtDataSource = new Text(outer,SWT.BORDER);
		txtDataSource.setLayoutData(data);
		txtDataSource.setEnabled(false);
		
		chkTableCreate = new Button(outer,SWT.CHECK);
		chkTableCreate.setText("Create DB Table");
		chkTableCreate.setLayoutData(dChekc);	
		
		chkTableDelete = new Button(outer,SWT.CHECK);
		chkTableDelete.setText("Delete DB Table");
		chkTableDelete.setLayoutData(dChekc);
		
		
		tableViewer = new TableViewer(outer,SWT.FULL_SELECTION | SWT.BORDER);
		tableViewer.setContentProvider(new CacheLoaderTableProvider());
		tableViewer.setLabelProvider(new CacheLoaderLableProvider());
		
		tableViewer.setColumnProperties(new String[]{"property","value"});
		
	    GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
	    gridData.heightHint = convertVerticalDLUsToPixels(200);
	    gridData.widthHint = convertHorizontalDLUsToPixels(300);
	    gridData.horizontalSpan = 2;

		Table table = tableViewer.getTable();
		table.setLayoutData(gridData);
		
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		TableColumn propCol = new TableColumn(table,SWT.NONE);
		propCol.setAlignment(SWT.CENTER);
		propCol.setText(COLUMN_NAMES[0]);
		propCol.setResizable(true);
		propCol.setWidth(150);		
		
		TableColumn valCol = new TableColumn(table,SWT.NONE);
		valCol.setAlignment(SWT.CENTER);
		valCol.setText(COLUMN_NAMES[1]);
		valCol.setResizable(true);
		valCol.setWidth(150);
		
		
		
		TextCellEditor editor = new TextCellEditor(table);
		
		tableViewer.setCellEditors(new CellEditor[]{null,editor});
		
		ICellModifier modif = new ICellModifier(){

			public boolean canModify(Object element, String property) {
				if(property.equals("property"))
				return false;
				
				return true;
			}

			public Object getValue(Object element, String property) {
				if(property.equals("value")){
					CacheLoaderPropModel model = (CacheLoaderPropModel)element;
					return model.getValue();
				}
				return null;
			}

			public void modify(Object element, String property, Object value) {
				
				if(property.equals("value")){
				
				CacheLoaderPropModel model = null;
				if(element instanceof Item){
					model = (CacheLoaderPropModel)((Item)element).getData();
				}else
					model = (CacheLoaderPropModel)element;
				
				model.setValue(value.toString());
				
				tableViewer.refresh(model);
				}
			}
			
		};
		
		String [] params = new String[2];
		params[0] = cacheLoaderType;
		params[1] = Boolean.toString(useDs);
		
		tableViewer.setCellModifier(modif);
		tableViewer.setInput(params);
		
	}
	
	

}
