/**
 * 
 */
package org.jboss.tools.smooks.configuration.editors.csv;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.smooks.configuration.editors.json.JsonDataConfiguraitonWizardPage.KeyValueModel;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.model.csv.CsvReader;
import org.jboss.tools.smooks.model.csv.impl.CsvReaderImpl;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;

/**
 * @author Dart
 * 
 */
public class CSVDataConfigurationWizardPage extends WizardPage {

	private SmooksResourceListType smooksResourceList;
	private Button newReaderConfigButton;
	private Button useAvailableReaderConfigButton;
	private Composite configComposite;
	private Text separatorText;
	private Text quoteCharText;
	private Text skipLinesText;
	private Text encodingText;
	private Composite fieldsComposite;
	private TableViewer fieldsViewer;
	private Button addButton;
	private Button removeButton;
	private Button createCSVReaderButton;

	private boolean useAvailabelReader = false;

	private boolean hasReader = false;

	private boolean createCSVReader = true;

	private String speartor;

	private String skipLines;

	private String quoteChar;

	private String encoding;

	private List<FieldString> fieldsList = new ArrayList<FieldString>();

	public CSVDataConfigurationWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	public CSVDataConfigurationWizardPage(String pageName) {
		super(pageName);
		this.setTitle("CSV Reader configurations");
		this.setDescription("Set the configurations for parsing CSV file.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createControl(Composite parent) {
		initValue();
		Composite mainComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		mainComposite.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		mainComposite.setLayoutData(gd);

		Composite radioButtonComposite = new Composite(mainComposite, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		radioButtonComposite.setLayoutData(gd);

		GridLayout rgl = new GridLayout();
		rgl.numColumns = 2;
		rgl.marginHeight = 0;
		rgl.marginWidth = 0;
		radioButtonComposite.setLayout(rgl);

		Composite spaceComposite = new Composite(mainComposite, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		gd.heightHint = 20;
		spaceComposite.setLayoutData(gd);

		newReaderConfigButton = new Button(radioButtonComposite, SWT.RADIO);
		newReaderConfigButton.setText("Create new CSV reader configurations");
		newReaderConfigButton.setSelection(true);

		useAvailableReaderConfigButton = new Button(radioButtonComposite, SWT.RADIO);
		useAvailableReaderConfigButton.setText("Use available CSV reader configurations");

		configComposite = new Composite(mainComposite, SWT.NONE);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		configComposite.setLayoutData(gd);

		GridLayout cgl = new GridLayout();
		cgl.marginHeight = 0;
		cgl.marginWidth = 0;
		cgl.numColumns = 2;
		configComposite.setLayout(cgl);
		/**
		 * String fields, char separator, char quoteChar, int skipLines, String
		 * encoding
		 */
		gd = new GridData(GridData.FILL_HORIZONTAL);
		Label rootnameLabel = new Label(configComposite, SWT.NONE);
		rootnameLabel.setText("Separator");
		separatorText = new Text(configComposite, SWT.BORDER);
		separatorText.setLayoutData(gd);

		Label arrayElementNameLabel = new Label(configComposite, SWT.NONE);
		arrayElementNameLabel.setText("Quote Char");
		quoteCharText = new Text(configComposite, SWT.BORDER);
		quoteCharText.setLayoutData(gd);

		Label skiplineLabel = new Label(configComposite, SWT.NONE);
		skiplineLabel.setText("Skip Lines");
		skipLinesText = new Text(configComposite, SWT.BORDER);
		skipLinesText.setLayoutData(gd);

		Label encodingLabel = new Label(configComposite, SWT.NONE);
		encodingLabel.setText("Encoding");
		encodingText = new Text(configComposite, SWT.BORDER);
		encodingText.setLayoutData(gd);
		encodingText.setText(encoding);

		Label keyMapLabel = new Label(configComposite, SWT.NONE);
		keyMapLabel.setText("Fields List:");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		keyMapLabel.setLayoutData(gd);

		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;

		fieldsComposite = new Composite(configComposite, SWT.NONE);
		fieldsComposite.setLayoutData(gd);

		GridLayout kgl = new GridLayout();
		kgl.numColumns = 2;
		fieldsComposite.setLayout(kgl);

		gd = new GridData(GridData.FILL_BOTH);

		fieldsViewer = new TableViewer(fieldsComposite, SWT.BORDER | SWT.MULTI);
		fieldsViewer.getControl().setLayoutData(gd);
//		fieldsViewer.getTable().setHeaderVisible(true);
		fieldsViewer.getTable().setLinesVisible(true);
		fieldsViewer.setContentProvider(new FieldsContentProvider());
		fieldsViewer.setLabelProvider(new FieldsLabelProvider());

		CellEditor fieldCellEditor = new TextCellEditor(fieldsViewer.getTable(), SWT.BORDER);

		fieldsViewer.setCellEditors(new CellEditor[] { fieldCellEditor });

		fieldsViewer.setColumnProperties(new String[] { "field" });

		fieldsViewer.setCellModifier(new ICellModifier() {

			public void modify(Object element, String property, Object value) {
				Object el = null;
				if (element instanceof Item) {
					el = ((Item) element).getData();
				}
				if (el == null)
					return;
				if (el instanceof FieldString && value instanceof String) {
					if (property.equals("field")) {
						((FieldString) el).setText(value.toString());
					}
					fieldsViewer.refresh(el);
				}
			}

			public Object getValue(Object element, String property) {
				// Object el = null;
				// if(element instanceof Item){
				// el = ((Item)element).getData();
				// }
				// if(el == null) return null;
				if (element instanceof FieldString) {
					if (property.equals("field")) {
						return ((FieldString) element).getText();
					}
				}

				return null;
			}

			public boolean canModify(Object element, String property) {
				// Object el = null;
				// if(element instanceof Item){
				// el = ((Item)element).getData();
				// }
				// if(el == null) return false;
				if (element instanceof FieldString) {
					if (property.equals("field")) {
						return true;
					}
				}
				return false;
			}
		});

		fieldsViewer.setInput(fieldsList);

		Composite buttonComposite = new Composite(fieldsComposite, SWT.NONE);
		gd = new GridData(GridData.FILL_VERTICAL);
		buttonComposite.setLayoutData(gd);

		GridLayout bgl = new GridLayout();
		buttonComposite.setLayout(bgl);

		gd = new GridData(GridData.FILL_HORIZONTAL);

		addButton = new Button(buttonComposite, SWT.NONE);
		addButton.setLayoutData(gd);
		addButton.setText("Add");

		removeButton = new Button(buttonComposite, SWT.NONE);
		removeButton.setLayoutData(gd);
		removeButton.setText("Remove");

		createCSVReaderButton = new Button(configComposite, SWT.CHECK);
		createCSVReaderButton.setText("Create a CSV Reader");

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createCSVReaderButton.setLayoutData(gd);
		createCSVReaderButton.setSelection(createCSVReader);

		if (hasReader) {
			useAvailableReaderConfigButton.setSelection(true);
			newReaderConfigButton.setSelection(false);
			createCSVReaderButton.setEnabled(false);
			setConfigCompositeStates(false);
		}
		
		changePageStatus();
		hookControls();
		
		this.setControl(mainComposite);
	}

	private void hookControls() {
		newReaderConfigButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				if (newReaderConfigButton.getSelection()) {
					setConfigCompositeStates(true);
					useAvailabelReader = false;
					changePageStatus();
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		useAvailableReaderConfigButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				if (useAvailableReaderConfigButton.getSelection()) {
					setConfigCompositeStates(false);
					useAvailabelReader = true;
					changePageStatus();
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		this.separatorText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				speartor = separatorText.getText();
				changePageStatus();
			}
		});

		this.quoteCharText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				quoteChar = quoteCharText.getText();
				changePageStatus();
			}
		});

		this.skipLinesText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				skipLines = skipLinesText.getText();
				changePageStatus();
			}
		});

		this.encodingText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				encoding = encodingText.getText();
				changePageStatus();
			}
		});

		this.addButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				FieldString field = new FieldString("field");
				fieldsList.add(field);
				fieldsViewer.refresh();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		this.removeButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection s = (IStructuredSelection)fieldsViewer.getSelection();
				fieldsList.removeAll(s.toList());
				fieldsViewer.refresh();
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	private void changePageStatus() {
		if (useAvailabelReader) {
			return;
		}
		String error = null;

		if (speartor == null || speartor.length() == 0) {
			error = "Sperator can't be null";
		}

		if (quoteChar == null || quoteChar.length() == 0) {
			error = "Quote char can't be null";
		}
		
		if (encoding == null || encoding.length() == 0) {
			error = "Encoding can't be null";
		}

		if (skipLines == null || skipLines.length() == 0) {
			error = "Skip lines can't be null";
		} else {
			try {
				Integer.parseInt(skipLines);
			} catch (Throwable t) {
				error = "Skip lines text must be the number";
			}
		}

		setErrorMessage(error);
		setPageComplete(error == null);
	}

	private void setConfigCompositeStates(boolean enabled) {
		configComposite.setEnabled(enabled);
		Control[] controls = configComposite.getChildren();
		for (int i = 0; i < controls.length; i++) {
			Control c = controls[i];
			if (c == createCSVReaderButton) {
				if (hasReader) {
					c.setEnabled(false);
					continue;
				}
			}
			if (c == fieldsComposite) {
				Control[] cs = ((Composite) c).getChildren();
				for (int j = 0; j < cs.length; j++) {
					Control cc = cs[j];
					cc.setEnabled(enabled);
				}
			}
			c.setEnabled(enabled);
		}
	}

	private void initValue() {
		useAvailabelReader = false;

		hasReader = false;

		if (SmooksUIUtils.hasReaderAlready(CsvReader.class, smooksResourceList)
				|| SmooksUIUtils.hasReaderAlready(CsvReaderImpl.class, smooksResourceList)) {
			hasReader = true;
		}

		createCSVReader = true;

		encoding = "UTF-8";

		speartor = null;

		skipLines = null;

		quoteChar = null;

		fieldsList.clear();

		if (hasReader) {
			createCSVReader = false;
			useAvailabelReader = true;
		}
	}

	public SmooksResourceListType getSmooksResourceList() {
		return smooksResourceList;
	}

	public void setSmooksResourceList(SmooksResourceListType smooksResourceList) {
		this.smooksResourceList = smooksResourceList;
	}

	private class FieldString {
		private String text = null;

		public FieldString(String text) {
			this.setText(text);
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
	}

	private class FieldsLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if(element instanceof FieldString){
				return ((FieldString)element).getText();
			}
			return getText(element);
		}

	}

	private class FieldsContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List<?>) {
				return ((List<?>) inputElement).toArray();
			}
			return new Object[] {};
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

	}

}
