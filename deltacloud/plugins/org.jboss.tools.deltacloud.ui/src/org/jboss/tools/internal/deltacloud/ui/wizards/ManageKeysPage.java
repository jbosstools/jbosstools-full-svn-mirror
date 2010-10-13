package org.jboss.tools.internal.deltacloud.ui.wizards;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.deltacloud.ui.IDeltaCloudPreferenceConstants;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;
import org.osgi.service.prefs.Preferences;

public class ManageKeysPage extends WizardPage {

	private final static String NAME = "ManageKeys.name"; //$NON-NLS-1$
	private final static String TITLE = "ManageKeys.title"; //$NON-NLS-1$
	private final static String DESC = "ManageKeys.desc"; //$NON-NLS-1$
	private final static String DIR_LABEL = "Directory.label"; //$NON-NLS-1$
	private final static String BROWSE_LABEL = "BrowseButton.label"; //$NON-NLS-1$
	private final static String NEW = "NewButton.label"; //$NON-NLS-1$
	private final static String DELETE = "DeleteButton.label"; //$NON-NLS-1$
	private final static String CREATE_KEY_TITLE = "CreateKey.title"; //$NON-NLS-1$
	private final static String CREATE_KEY_MSG = "CreateKey.msg"; //$NON-NLS-1$
	private final static String CONFIRM_KEY_DELETE_TITLE = "ConfirmKeyDelete.title"; //$NON-NLS-1$
	private final static String CONFIRM_KEY_DELETE_MSG = "ConfirmKeyDelete.msg"; //$NON-NLS-1$
	
	private final static String INVALID_DIRECTORY = "ErrorInvalidDirectory.text"; //$NON-NLS-1$
	
	private DeltaCloud cloud;
	private String fileExtension;
	private String currFile;
	
	private Text directory;
	private List fileList;
	
	private ModifyListener dirListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			// TODO Auto-generated method stub
			validate();
		}
		
	};
	
	private SelectionListener browseButtonListener = new SelectionAdapter() {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			DirectoryDialog d = new DirectoryDialog(shell);
			String text = d.open();
			if (text != null)
				directory.setText(text);
		}

	};

	private SelectionListener createButtonListener = new SelectionAdapter() {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			String directoryText = directory.getText();
			InputDialog d = new InputDialog(shell, WizardMessages.getString(CREATE_KEY_TITLE),
					WizardMessages.getString(CREATE_KEY_MSG),
					"",
					null);
			d.setBlockOnOpen(true);
			d.create();
			int retcode = d.open();
			if (retcode == InputDialog.OK) {
				String keyname = d.getValue();
				try {
					cloud.createKey(keyname, directoryText);
					loadFileList();
				} catch (DeltaCloudException dce) {
					MessageDialog.openError(getShell(), null, dce.getLocalizedMessage());
				}
			}
		}

	};

	private SelectionListener fileListListener = new SelectionAdapter() {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			currFile = fileList.getSelection()[0];
		}

	};

	private FilenameFilter extensionFilter = new FilenameFilter() {

		@Override
		public boolean accept(File arg0, String arg1) {
			if (arg1.endsWith(fileExtension))
				return true;
			return false;
		}
		
	};
	
	public ManageKeysPage(DeltaCloud cloud, String fileExtension) {
		super(WizardMessages.getString(NAME));
		this.cloud = cloud;
		this.fileExtension = fileExtension;
		setDescription(WizardMessages.getString(DESC));
		setTitle(WizardMessages.getString(TITLE));
		setImageDescriptor(SWTImagesFactory.DESC_DELTA_LARGE);
		setPageComplete(false);
	}
	
	public String getCurrFile() {
		return currFile;
	}
	
	private void validate() {
		boolean hasError = false;
		boolean isComplete = true;
		
		if (directory.getText().length() == 0)
			isComplete = false;
		else {
			File f = new File(directory.getText());
			if (!f.exists() || !f.isDirectory()) {
				hasError = true;
				setErrorMessage(WizardMessages.getString(INVALID_DIRECTORY));
			} else {
				Preferences prefs = new InstanceScope().getNode(Activator.PLUGIN_ID);
				try {
					prefs.put(IDeltaCloudPreferenceConstants.DEFAULT_KEY_DIR, directory.getText());
				} catch(Exception e) {
					// do nothing
				}
				loadFileList();
			}
		}
		if (!hasError)
			setErrorMessage(null);
		setPageComplete(isComplete && !hasError);
	}
	
	private void loadFileList() {
		File dir = new File(directory.getText());
		if (dir.exists() && dir.isDirectory()) {
			File[] files = dir.listFiles(extensionFilter);
			Arrays.sort(files, new Comparator<File>() {
				@Override
				public int compare(File arg0, File arg1) {
					String name0 = arg0.getName();
					String name1 = arg1.getName();
					return name0.compareTo(name1);
				}
			});
			fileList.removeAll();
			for (File f : files) {
				fileList.add(f.getName());
			}
		}
	}
	
	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		final Composite container = new Composite(parent, SWT.NULL);
		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		container.setLayout(layout);		

		Label dirLabel = new Label(container, SWT.NULL);
		dirLabel.setText(WizardMessages.getString(DIR_LABEL));
		
		directory = new Text(container, SWT.BORDER | SWT.SINGLE);
		Preferences prefs = new InstanceScope().getNode(Activator.PLUGIN_ID);
		String defaultDir = prefs.get(IDeltaCloudPreferenceConstants.DEFAULT_KEY_DIR, System.getProperty("user.home"));
		directory.setText(defaultDir);
		directory.addModifyListener(dirListener);
		
		Button browseButton = new Button(container, SWT.NULL);
		browseButton.setText(WizardMessages.getString(BROWSE_LABEL));
		browseButton.addSelectionListener(browseButtonListener);
	
		fileList = new List(container, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		fileList.addSelectionListener(fileListListener);
		
		Button createButton = new Button(container, SWT.NULL);
		createButton.setText(WizardMessages.getString(NEW));
		createButton.addSelectionListener(createButtonListener);
		
		Button deleteButton = new Button(container, SWT.NULL);
		deleteButton.setText(WizardMessages.getString(DELETE));
		deleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final String keyName = fileList.getSelection()[0];
				boolean confirmed = MessageDialog.openConfirm(getShell(), 
						WizardMessages.getString(CONFIRM_KEY_DELETE_TITLE),
						WizardMessages.getFormattedString(CONFIRM_KEY_DELETE_MSG, keyName));
				if (confirmed) {
					try {
						cloud.deleteKey(keyName.substring(0, keyName.length() - fileExtension.length()));
						File f = new File(directory.getText());
						File[] files = f.listFiles(new FilenameFilter() {
							@Override
							public boolean accept(File dir, String name) {
								return name.equals(keyName);
							}
						});
						if (files.length == 1) {
							if (files[0].delete())
								fileList.remove(fileList.getSelectionIndex());
						}
					} catch (DeltaCloudException dce) {
						MessageDialog.openError(getShell(), null, dce.getLocalizedMessage());
					}
				}
			}
		});
		
		Point p1 = dirLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Point p2 = directory.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Point p3 = browseButton.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int centering = (p2.y - p1.y + 1) / 2;
		int centering2 = (p3.y - p2.y + 1) / 2;

		FormData f = new FormData();
		f.left = new FormAttachment(0, 5);
		f.top = new FormAttachment(0, 5 + centering + centering2);
		dirLabel.setLayoutData(f);
		
		f = new FormData();
		f.right = new FormAttachment(100, -10);
		f.top = new FormAttachment(0, 5);
		browseButton.setLayoutData(f);
		
		f = new FormData();
		f.left = new FormAttachment(dirLabel, 5);
		f.top = new FormAttachment(0, 5 + centering2);
		f.right = new FormAttachment(browseButton, -10);
		directory.setLayoutData(f);
		
		f = new FormData();
        int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
        Point minSize = deleteButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        f.width = Math.max(widthHint, minSize.x);
        f.right = new FormAttachment(100, -20);
        f.bottom = new FormAttachment(100, -10);
        deleteButton.setLayoutData(f);
        
        f = new FormData();
        minSize = deleteButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        f.width = Math.max(widthHint, minSize.x);
        f.right = new FormAttachment(deleteButton, -10);
        f.bottom = new FormAttachment(100, -10);
        createButton.setLayoutData(f);
        
        f = new FormData();
        f.top = new FormAttachment(directory, 10);
        f.left = new FormAttachment(0, 0);
        f.right = new FormAttachment(100, 0);
        f.bottom = new FormAttachment(createButton, -10);
        fileList.setLayoutData(f);
	
        setControl(container);
		loadFileList();
		validate();
	}

}
