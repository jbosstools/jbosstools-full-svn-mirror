package org.jboss.ide.eclipse.jbosscache.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.jbosscache.internal.CacheMessages;
import org.jboss.ide.eclipse.jbosscache.model.CacheLoaderPropModel;
import org.jboss.ide.eclipse.jbosscache.wizards.pages.CacheLoaderLableProvider;
import org.jboss.ide.eclipse.jbosscache.wizards.pages.CacheLoaderTableProvider;
import org.jboss.ide.eclipse.jbosscache.wizards.pages.StandardConfigurationPage;

public class CacheLoaderDefDialog extends Dialog {
	
	private TableViewer tableViewer;
	
	//private Button chkUseDataSource;
	private Label lblUseDataSource;
	private Text txtDataSource;
	//private Button chkTableCreate;
	//private Button chkTableDelete;
	private Button addButton;
	
	private String[] COLUMN_NAMES = new String[]{CacheMessages.Column_Properties,CacheMessages.Column_Value};
	
	//private String cacheLoaderType;
	private List model = new ArrayList();
	private StandardConfigurationPage page;
	
	public CacheLoaderDefDialog(Shell parentShell,String cacheLoaderString,StandardConfigurationPage page) {
		super(parentShell);
		setShellStyle(SWT.SHELL_TRIM);
		
		this.page = page;
		if(page.getCacheLoaderCustomModel() != null){
			this.model = page.getCacheLoaderCustomModel();
		}
		
		//this.cacheLoaderType = cacheLoaderString;
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

	
	
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,true);
	}

	//private boolean useDs = false;
	/**
	 * Create tree table
	 * 
	 * @param outer
	 */
	private void createTable(Composite outer) {
		
		outer.setLayout(new GridLayout(2,false));
		
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
								
		lblUseDataSource = new Label(outer,SWT.NONE);
		lblUseDataSource.setText("Custom Qualified Class Name: ");
		
		txtDataSource = new Text(outer,SWT.BORDER);
		txtDataSource.setLayoutData(data);
		txtDataSource.setText(page.getCacheLoaderCustomClassName());
		
		addButton = new Button(outer,SWT.PUSH);
		addButton.setText("Add Property");
		
		addButton.addMouseListener(new MouseAdapter(){
			public void mouseDown(MouseEvent e) {
				handleMouseClick(e);
			}			
		});

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
		TextCellEditor editorProperty = new TextCellEditor(table);
		
		
		 
		tableViewer.setCellEditors(new CellEditor[]{editorProperty,editor});
		
		ICellModifier modif = new ICellModifier(){

			public boolean canModify(Object element, String property) {
				
				return true;
			}

			public Object getValue(Object element, String property) {
				CacheLoaderPropModel model = (CacheLoaderPropModel)element;
				if(property.equals("value")){					
					return model.getValue();
				}

				if(property.equals("property")){					
					return model.getProperty();
				}

				return null;
			}

			public void modify(Object element, String property, Object value) {
				
				CacheLoaderPropModel model = null;
				if(element instanceof Item){
					model = (CacheLoaderPropModel)((Item)element).getData();
				}else
					model = (CacheLoaderPropModel)element;				
				
				if(property.equals("value")){
								
					model.setValue(value.toString());
				}else
					model.setProperty(value.toString());
				
				tableViewer.refresh();
			}
			
		};
		
//		String [] params = new String[2];
//		params[0] = cacheLoaderType;
//		params[1] = Boolean.toString(useDs);
		
		tableViewer.setCellModifier(modif);
		tableViewer.setInput(model);
		
	}

	private void handleMouseClick(MouseEvent e) {
		
		List list = (List)tableViewer.getInput();
		CacheLoaderPropModel model = new CacheLoaderPropModel();
		model.setProperty("property");
		model.setValue("value");
		list.add(model);
		
		tableViewer.refresh();
		
	}
	
	public List getLoaderDialogModel(){
		return model;
	}

	public Text getTxtDataSource() {
		return txtDataSource;
	}

	@Override
	protected void okPressed() {
		// TODO Auto-generated method stub
		if(txtDataSource.getText() != null && !txtDataSource.getText().trim().equals(""))
			page.setCacheLoaderCustomClassName(txtDataSource.getText().trim());
		
		super.okPressed();
	}
	
	

}
