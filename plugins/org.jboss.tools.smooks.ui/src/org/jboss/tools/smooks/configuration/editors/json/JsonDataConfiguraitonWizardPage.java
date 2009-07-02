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
package org.jboss.tools.smooks.configuration.editors.json;

import java.util.ArrayList;
import java.util.Iterator;
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
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.model.json.JsonReader;
import org.jboss.tools.smooks.model.json.impl.JsonReaderImpl;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;

/**
 * @author Dart (dpeng@redhat.com)
 * 
 */
public class JsonDataConfiguraitonWizardPage extends WizardPage {

	private String rootName = null;

	private String arrayElementName = "element";

	private String keyWhitspaceReplacement = "-";

	private String keyPrefixOnNumeric;

	private String illegalElementNameCharReplacement;

	private String nullValueReplacement = "";

	private String encoding = "UTF-8";

	private Text rootNameText;

	private Text arrayElementNameText;

	private Text keyWhitspaceReplacementText;

	private Text keyPrefixOnNumericText;

	private Text illegalElementNameCharReplacementText;

	private Text nullValueReplacementText;

	private Text encodingText;

	private TableViewer keyMapViewer;

	private ArrayList<KeyValueModel> keyValueList;

	private Button addButton;

	private Button removeButton;

	private Button createJsonReaderButton;

	protected boolean createJsonReader = true;

	private Button newReaderConfigButton;

	private Button useAvailableReaderConfigButton;

	private SmooksResourceListType resourceList;

	private boolean hasReader = false;

	private boolean useAvailabelReader = false;

	private Composite configComposite;

	private Composite keyMapComposite;
	
	

	private void initValue() {
		hasReader = false;
		if (SmooksUIUtils.hasReaderAlready(JsonReaderImpl.class, resourceList)
				|| SmooksUIUtils.hasReaderAlready(JsonReader.class, resourceList)) {
			hasReader = true;
		}
		
		useAvailabelReader= false;

		rootName = null;

		createJsonReader = true;

		arrayElementName = "element";

		keyWhitspaceReplacement = "-";

		keyPrefixOnNumeric = null;

		illegalElementNameCharReplacement = null;
		nullValueReplacement = "";
		encoding = "UTF-8";
		if (keyValueList != null) {
			keyValueList.clear();
		} else {
			keyValueList = new ArrayList<KeyValueModel>();
		}

		if (hasReader) {
			useAvailabelReader = true;
			createJsonReader = false;
		}

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
		newReaderConfigButton.setText("Create new JSON reader configurations");
		newReaderConfigButton.setSelection(true);

		useAvailableReaderConfigButton = new Button(radioButtonComposite, SWT.RADIO);
		useAvailableReaderConfigButton.setText("Use available JSON reader configurations");

		configComposite = new Composite(mainComposite, SWT.NONE);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		configComposite.setLayoutData(gd);

		GridLayout cgl = new GridLayout();
		cgl.marginHeight = 0;
		cgl.marginWidth = 0;
		cgl.numColumns = 2;
		configComposite.setLayout(cgl);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		Label rootnameLabel = new Label(configComposite, SWT.NONE);
		rootnameLabel.setText("Root Element Name");
		rootNameText = new Text(configComposite, SWT.BORDER);
		rootNameText.setLayoutData(gd);

		Label arrayElementNameLabel = new Label(configComposite, SWT.NONE);
		arrayElementNameLabel.setText("Array Element Name");
		arrayElementNameText = new Text(configComposite, SWT.BORDER);
		arrayElementNameText.setLayoutData(gd);
		arrayElementNameText.setText(arrayElementName);

		Label keyWhitspaceReplacementLabel = new Label(configComposite, SWT.NONE);
		keyWhitspaceReplacementLabel.setText("Space Replacement");
		keyWhitspaceReplacementText = new Text(configComposite, SWT.BORDER);
		keyWhitspaceReplacementText.setLayoutData(gd);
		if (keyWhitspaceReplacement == null) {
			keyWhitspaceReplacement = "";
		}
		keyWhitspaceReplacementText.setText(keyWhitspaceReplacement);

		Label keyPrefixOnNumeric = new Label(configComposite, SWT.NONE);
		keyPrefixOnNumeric.setText("Prefix On Numeric");
		keyPrefixOnNumericText = new Text(configComposite, SWT.BORDER);
		keyPrefixOnNumericText.setLayoutData(gd);

		Label illegalElementNameCharReplacementLabel = new Label(configComposite, SWT.NONE);
		illegalElementNameCharReplacementLabel.setText("IllegalChar Replacement");
		illegalElementNameCharReplacementText = new Text(configComposite, SWT.BORDER);
		illegalElementNameCharReplacementText.setLayoutData(gd);

		Label nullValueReplacementlabel = new Label(configComposite, SWT.NONE);
		nullValueReplacementlabel.setText("NullValue Replacement");
		nullValueReplacementText = new Text(configComposite, SWT.BORDER);
		nullValueReplacementText.setLayoutData(gd);
		nullValueReplacementText.setText(nullValueReplacement);

		Label encodingLabel = new Label(configComposite, SWT.NONE);
		encodingLabel.setText("Encoding");
		encodingText = new Text(configComposite, SWT.BORDER);
		encodingText.setLayoutData(gd);
		encodingText.setText(encoding);

		Label keyMapLabel = new Label(configComposite, SWT.NONE);
		keyMapLabel.setText("Key Map:");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		keyMapLabel.setLayoutData(gd);

		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;

		keyMapComposite = new Composite(configComposite, SWT.NONE);
		keyMapComposite.setLayoutData(gd);

		GridLayout kgl = new GridLayout();
		kgl.numColumns = 2;
		keyMapComposite.setLayout(kgl);

		gd = new GridData(GridData.FILL_BOTH);

		keyMapViewer = new TableViewer(keyMapComposite, SWT.BORDER | SWT.MULTI);
		keyMapViewer.getControl().setLayoutData(gd);
		keyMapViewer.getTable().setHeaderVisible(true);
		keyMapViewer.getTable().setLinesVisible(true);
		keyMapViewer.setContentProvider(new KeyMapContentProvider());
		keyMapViewer.setLabelProvider(new KeyMapLabelProvider());

		CellEditor keyCellEditor = new TextCellEditor(keyMapViewer.getTable(), SWT.BORDER);

		CellEditor valueCellEditor = new TextCellEditor(keyMapViewer.getTable(), SWT.BORDER);

		keyMapViewer.setCellEditors(new CellEditor[] { keyCellEditor, valueCellEditor });

		keyMapViewer.setColumnProperties(new String[] { "key", "value" });

		keyMapViewer.setCellModifier(new ICellModifier() {

			public void modify(Object element, String property, Object value) {
				Object el = null;
				if (element instanceof Item) {
					el = ((Item) element).getData();
				}
				if (el == null)
					return;
				if (el instanceof KeyValueModel && value instanceof String) {
					if (property.equals("key")) {
						((KeyValueModel) el).setKey(value.toString());
					}
					if (property.equals("value")) {
						((KeyValueModel) el).setValue(value.toString());
					}
					keyMapViewer.refresh(el);
				}
			}

			public Object getValue(Object element, String property) {
				// Object el = null;
				// if(element instanceof Item){
				// el = ((Item)element).getData();
				// }
				// if(el == null) return null;
				if (element instanceof KeyValueModel) {
					if (property.equals("key")) {
						return ((KeyValueModel) element).getKey();
					}
					if (property.equals("value")) {
						return ((KeyValueModel) element).getValue();
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
				if (element instanceof KeyValueModel) {
					if (property.equals("key") || property.equals("value")) {
						return true;
					}
				}
				return false;
			}
		});

		TableColumn keyColumn = new TableColumn(keyMapViewer.getTable(), SWT.BORDER);
		keyColumn.setWidth(150);
		keyColumn.setText("Key");

		TableColumn replaceColumn = new TableColumn(keyMapViewer.getTable(), SWT.BORDER);
		replaceColumn.setWidth(150);
		replaceColumn.setText("Replace");

		keyMapViewer.setInput(keyValueList);

		Composite buttonComposite = new Composite(keyMapComposite, SWT.NONE);
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

		createJsonReaderButton = new Button(configComposite, SWT.CHECK);
		createJsonReaderButton.setText("Create a JSON Reader");

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createJsonReaderButton.setLayoutData(gd);
		createJsonReaderButton.setSelection(createJsonReader);
		
		if (hasReader) {
			createJsonReaderButton.setEnabled(false);
			newReaderConfigButton.setSelection(false);
			useAvailableReaderConfigButton.setSelection(true);
			setConfigCompositeStates(false);
//			configComposite.setEnabled(false);
		}
		hookControls();
		changePageStatus();
		this.setControl(mainComposite);
	}
	
	private void setConfigCompositeStates(boolean enabled){
		configComposite.setEnabled(enabled);
		Control[] controls = configComposite.getChildren();
		for (int i = 0; i < controls.length; i++) {
			Control c = controls[i];
			if(c == createJsonReaderButton){
				if(hasReader){
					c.setEnabled(false);
					continue;
				}
			}
			if(c == keyMapComposite){
				Control[] cs = ((Composite)c).getChildren();
				for (int j = 0; j < cs.length; j++) {
					Control cc = cs[j];
					cc.setEnabled(enabled);
				}
			}
			c.setEnabled(enabled);
		}
	}

	private void hookControls() {

		newReaderConfigButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (newReaderConfigButton.getSelection()) {
					useAvailabelReader = false;
					changePageStatus();
					setConfigCompositeStates(true);
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		useAvailableReaderConfigButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (useAvailableReaderConfigButton.getSelection()) {
					useAvailabelReader = true;
					changePageStatus();
					setConfigCompositeStates(false);
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		addButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				keyValueList.add(new KeyValueModel("key", "value"));
				keyMapViewer.refresh();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		removeButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) keyMapViewer.getSelection();
				List<?> selections = selection.toList();
				for (Iterator<?> iterator = selections.iterator(); iterator.hasNext();) {
					Object object = (Object) iterator.next();
					keyValueList.remove(object);
				}
				keyMapViewer.refresh();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		createJsonReaderButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				createJsonReader = createJsonReaderButton.getSelection();
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		encodingText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				encoding = encodingText.getText();
				changePageStatus();
			}
		});

		nullValueReplacementText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				nullValueReplacement = nullValueReplacementText.getText();
				changePageStatus();
			}
		});

		illegalElementNameCharReplacementText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				illegalElementNameCharReplacement = illegalElementNameCharReplacementText.getText();
				changePageStatus();
			}
		});

		keyPrefixOnNumericText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				keyPrefixOnNumeric = keyPrefixOnNumericText.getText();
				changePageStatus();
			}
		});

		keyWhitspaceReplacementText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				keyWhitspaceReplacement = keyWhitspaceReplacementText.getText();
				changePageStatus();
			}
		});

		arrayElementNameText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				arrayElementName = arrayElementNameText.getText();
				changePageStatus();
			}
		});

		rootNameText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				rootName = rootNameText.getText();
				changePageStatus();
			}
		});
	}

	private void changePageStatus() {
		String error = null;
		if (useAvailabelReader) {
			setErrorMessage(null);
			setPageComplete(true);
			return;
		}
		rootName = rootNameText.getText();
		if (rootName == null || rootName.length() == 0) {
			error = "Root Name can't be null";
		}

		arrayElementName = arrayElementNameText.getText();
		if (arrayElementName == null || arrayElementName.length() == 0) {
			error = "Array Element Name can't be null";
		}

		// if (keyWhitspaceReplacement == null
		// || keyWhitspaceReplacement.length() == 0) {
		// error = "Space replacement string can't be null";
		// }

		encoding = encodingText.getText();
		if (encoding == null || encoding.length() == 0) {
			error = "Encoding can't be null";
		}

		setErrorMessage(error);
		setPageComplete(error == null);
	}

	public JsonDataConfiguraitonWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	public JsonDataConfiguraitonWizardPage(String pageName) {
		super(pageName);
		this.setTitle("JSON Reader configurations");
		this.setDescription("Set the configurations for parsing JSON file.");
	}

	public String getRootName() {
		return rootName;
	}

	public void setRootName(String rootName) {
		this.rootName = rootName;
	}

	public String getArrayElementName() {
		return arrayElementName;
	}

	public void setArrayElementName(String arrayElementName) {
		this.arrayElementName = arrayElementName;
	}

	public String getKeyWhitspaceReplacement() {
		return keyWhitspaceReplacement;
	}

	public void setKeyWhitspaceReplacement(String keyWhitspaceReplacement) {
		this.keyWhitspaceReplacement = keyWhitspaceReplacement;
	}

	public String getKeyPrefixOnNumeric() {
		return keyPrefixOnNumeric;
	}

	public void setKeyPrefixOnNumeric(String keyPrefixOnNumeric) {
		this.keyPrefixOnNumeric = keyPrefixOnNumeric;
	}

	public String getIllegalElementNameCharReplacement() {
		return illegalElementNameCharReplacement;
	}

	public void setIllegalElementNameCharReplacement(String illegalElementNameCharReplacement) {
		this.illegalElementNameCharReplacement = illegalElementNameCharReplacement;
	}

	public String getNullValueReplacement() {
		return nullValueReplacement;
	}

	public void setNullValueReplacement(String nullValueReplacement) {
		this.nullValueReplacement = nullValueReplacement;
	}

	// public Map<String, String> getKeyMap() {
	// return keyMap;
	// }
	//
	// public void setKeyMap(Map<String, String> keyMap) {
	// this.keyMap = keyMap;
	// }
	
	

	public String getEncoding() {
		return encoding;
	}
	
	
	public boolean isUseAvailabelReader() {
		return useAvailabelReader;
	}

	public void setUseAvailabelReader(boolean useAvailabelReader) {
		this.useAvailabelReader = useAvailabelReader;
	}

	public SmooksResourceListType getSmooksResourceList() {
		return resourceList;
	}

	public void setSmooksResourceList(SmooksResourceListType resourceList) {
		this.resourceList = resourceList;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public ArrayList<KeyValueModel> getKeyValueList() {
		return keyValueList;
	}

	public void setKeyValueList(ArrayList<KeyValueModel> keyValueList) {
		this.keyValueList = keyValueList;
	}

	public boolean isCreateJsonReader() {
		return createJsonReader;
	}

	public void setCreateJsonReader(boolean createJsonReader) {
		this.createJsonReader = createJsonReader;
	}

	private class KeyMapLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof KeyValueModel) {
				switch (columnIndex) {
				case 0:
					return ((KeyValueModel) element).getKey();
				case 1:
					return ((KeyValueModel) element).getValue();
				}
			}
			return getText(element);
		}

	}

	private class KeyMapContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List<?>) {
				return ((List<?>) inputElement).toArray();
			}
			// TODO Auto-generated method stub
			return new Object[] {};
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}

	}

	public class KeyValueModel {
		private String key;
		private String value;

		public KeyValueModel(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
