/**
 * 
 */
package org.jboss.tools.smooks.xml;

import java.io.File;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.ui.dialogs.WorkspaceResourceDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.smooks.utils.UIUtils;
import org.jboss.tools.smooks.xml2java.analyzer.AbstractXMLModelAnalyzer;

/**
 * 
 * @author Dart Peng Date : 2008-8-13
 */
public abstract class AbstractFileSelectionWizardPage extends WizardPage
		implements SelectionListener {
	protected IStructuredSelection selection;
	protected Object returnObject = null;
	protected Text fileText;
	protected Composite fileTextComposite;
	protected CheckboxTableViewer tableViewer = null;
	protected Button fileSystemBrowseButton;
	protected boolean reasourceLoaded = false;
	private Button workspaceBrowseButton;
	private String filePath = null;

	public AbstractFileSelectionWizardPage(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
	}

	public Object getReturnValue() {
		try {
			String path = getFilePath();
			if(path == null) return null;
			path = AbstractXMLModelAnalyzer.parseFilePath(path);
			returnObject = this.loadedTheObject(path);
		} catch (Exception e) {
			UIUtils.showErrorDialog(getShell(), UIUtils.createErrorStatus(e));
		}
		return returnObject;
	}
	
	public String getFilePath(){
		return filePath;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {

		Composite mainComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		mainComposite.setLayout(layout);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		mainComposite.setLayoutData(gd);

		// xmlButton = new Button(mainComposite, SWT.RADIO);
		// xmlButton.setText("Select a XML file");
		//
		// Composite xmlComposite = this
		// .createXMLFileSelectionComposite(mainComposite);
		//
		// Label sp = new Label(mainComposite, SWT.HORIZONTAL | SWT.SEPARATOR);
		// gd = new GridData(GridData.FILL_HORIZONTAL);
		// sp.setLayoutData(gd);
		//
		// xsdButton = new Button(mainComposite, SWT.RADIO);
		// xsdButton.setText("Select a XSD file");

		Composite xsdComposite = this
				.createFileSelectionComposite(mainComposite);

		// init the panel status (XSD file selection composite is disabled)
		// xsdButton.setSelection(true);
		//
		// setCompositeChildrenEnabled(xmlComposite, false);

		// registe the listener for controls
		hookBrowseButtonSelectionAdapter();
		hookFileTextModifyListener();
		this.setControl(mainComposite);
	}

	protected void hookFileTextModifyListener() {
		final ModifyListener modifyListener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				filePath = fileText.getText();
				changeWizardPageStatus();
			}
		};
		// xmlFileText.addModifyListener(modifyListener);
		fileText.addModifyListener(modifyListener);
	}

	protected void hookRadioButtonSelectionAdapter() {
		// xsdButton.addSelectionListener(this);
	}

	protected void hookBrowseButtonSelectionAdapter() {
		SelectionAdapter browseButtonSelectionAdapter = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Text relationT = null;
				if (e.getSource() == fileSystemBrowseButton) {
					relationT = fileText;
				}
				openFileSelection(relationT);
			}
		};
		SelectionAdapter wbrowseButtonSelectionAdapter = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Text relationT = null;
				if (e.getSource() == workspaceBrowseButton) {
					relationT = fileText;
				}
				openWorkSpaceSelection(relationT);
			}

		};

		workspaceBrowseButton
				.addSelectionListener(wbrowseButtonSelectionAdapter);
		// xmlFileSystemBrowseButton
		// .addSelectionListener(browseButtonSelectionAdapter);
		fileSystemBrowseButton
				.addSelectionListener(browseButtonSelectionAdapter);
	}

	protected void openWorkSpaceSelection(Text relationT) {
		IFile[] files = WorkspaceResourceDialog.openFileSelection(getShell(),
				"Select Files", "", false, null, Collections.EMPTY_LIST); //$NON-NLS-1$ //$NON-NLS-2$
		// dialog.setInitialSelections(selectedResources);
		if (files.length > 0) {
			IFile file = files[0];
			String s = file.getFullPath().toPortableString();
			s = AbstractXMLModelAnalyzer.WORKSPACE_PRIX + s;
			relationT.setText(s);
		}
	}

	protected Composite createFileSelectionComposite(Composite parent) {
		Composite xsdComposite = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		xsdComposite.setLayoutData(gd);
		GridLayout xsdLayout = new GridLayout();
		xsdLayout.numColumns = 2;
		xsdLayout.marginWidth = 0;
		xsdComposite.setLayout(xsdLayout);

		Label nfileLanel = new Label(xsdComposite, SWT.NONE);
		nfileLanel.setText(Messages.getString("AbstractFileSelectionWizardPage.XMLFilePathLabelText")); //$NON-NLS-1$
		fileTextComposite = new Composite(xsdComposite, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		fileTextComposite.setLayoutData(gd);
		GridLayout xsdtgl = new GridLayout();
		xsdtgl.marginWidth = 0;
		xsdtgl.marginHeight = 0;
		xsdtgl.numColumns = 1;
		fileTextComposite.setLayout(xsdtgl);

		fileText = new Text(fileTextComposite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fileText.setLayoutData(gd);
		gd.grabExcessHorizontalSpace = true;

//		final Button loadXSDButton = new Button(fileTextComposite, SWT.NONE);
//		loadXSDButton.setText("Load");
//		loadXSDButton.addSelectionListener(new SelectionAdapter() {
//
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				super.widgetSelected(e);
//				reasourceLoaded = false;
//				try {
//					returnObject = loadedTheObject(fileText.getText());
//					reasourceLoaded = true;
//				} catch (Throwable e2) {
//					// ignore
//					e2.printStackTrace();
//				}
//				changeWizardPageStatus();
//			}
//
//		});

		Composite browseButtonComposite = new Composite(xsdComposite, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		browseButtonComposite.setLayoutData(gd);

		GridLayout bg = new GridLayout();
		bg.numColumns = 2;
		bg.marginHeight = 0;
		bg.marginWidth = 0;
		bg.makeColumnsEqualWidth = false;
		browseButtonComposite.setLayout(bg);

		fileSystemBrowseButton = new Button(browseButtonComposite, SWT.NONE);
		fileSystemBrowseButton.setText(Messages.getString("AbstractFileSelectionWizardPage.BrowseFileSystemButtonText")); //$NON-NLS-1$

		workspaceBrowseButton = new Button(browseButtonComposite, SWT.NONE);
		workspaceBrowseButton.setText(Messages.getString("AbstractFileSelectionWizardPage.BrowseWorkspaceButtonText")); //$NON-NLS-1$
		gd = new GridData();
		gd.horizontalAlignment = GridData.END;
		workspaceBrowseButton.setLayoutData(gd);

		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.END;
		fileSystemBrowseButton.setLayoutData(gd);

		return xsdComposite;
	}

	abstract protected Object loadedTheObject(String path) throws Exception;

	protected void initTableViewer() {
//		tableViewer.addCheckStateListener(new ICheckStateListener() {
//			boolean flag = true;
//
//			public void checkStateChanged(CheckStateChangedEvent event) {
//				if (flag) {
//					Object checkObject = event.getElement();
//					boolean check = event.getChecked();
//					flag = false;
//					tableViewer.setAllChecked(false);
//					tableViewer.setChecked(checkObject, check);
//					flag = true;
//					changeWizardPageStatus();
//				}
//			}
//		});
//		tableViewer.setContentProvider(new XSDStructuredModelContentProvider());
//		tableViewer.setLabelProvider(new XSDStructuredModelLabelProvider());
	}

	protected void setCompositeChildrenEnabled(Composite composite,
			boolean enabled) {
		Control[] children = composite.getChildren();
		for (int i = 0; i < children.length; i++) {
			Control child = children[i];
			if (child instanceof Text) {
				child.setEnabled(enabled);
			}
			if (child instanceof Button) {
				child.setEnabled(enabled);
			}
			if (child instanceof Composite) {
				setCompositeChildrenEnabled((Composite) child, enabled);
			}
		}
	}

	protected void openFileSelection(Text relationText) {
		FileDialog dialog = new FileDialog(this.getShell());
		String path = dialog.open();
		if (path != null) {
			path = AbstractXMLModelAnalyzer.FILE_PRIX + path;
			relationText.setText(path);
		} 
	}

	protected void changeWizardPageStatus() {
		String text = this.fileText.getText();
		String error = null;
		if (text == null || "".equals(text)) //$NON-NLS-1$
			error = Messages.getString("AbstractFileSelectionWizardPage.Errormessage"); //$NON-NLS-1$
		
//		File tempFile = new File(text);
//		if(!tempFile.exists()){
//			error = "Can't find the file , please select another one.";
//		}
		
//		if (!reasourceLoaded) {
//			error = "Resource must be loaded";
//		}
		this.setErrorMessage(error);
		this.setPageComplete(error == null);

	}

	public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	}

	public void widgetSelected(SelectionEvent e) {
		changeWizardPageStatus();
	}

	public CheckboxTableViewer getTableViewer() {
		return tableViewer;
	}

	public void setTableViewer(CheckboxTableViewer tableViewer) {
		this.tableViewer = tableViewer;
	}

	public IStructuredSelection getSelection() {
		return selection;
	}

	public void setSelection(IStructuredSelection selection) {
		this.selection = selection;
	}

}
