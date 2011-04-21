/**
 * 
 */
package org.jboss.tools.smooks.configuration.editors.file;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.ui.dialogs.WorkspaceResourceDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.smooks.configuration.editors.IFilePathProcessor;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;

/**
 * 
 * 
 * @author Dart Peng Date : 2008-8-13
 */
public class FileSelectionPageComponent {
	
	private String label = "File : ";  //$NON-NLS-1$
	protected Text filePathWidget;
	private String filePath;
	protected Composite fileTextComposite;
	protected CheckboxTableViewer tableViewer = null;
	protected Button fileSystemBrowseButton;
	protected boolean reasourceLoaded = false;
	protected Button workspaceBrowseButton;
	private List<FileSelectionListener> fileSelectionListeners = new ArrayList<FileSelectionListener>(); 
	protected Object[] initSelections;
	protected List<ViewerFilter> filters = new ArrayList<ViewerFilter>();
	protected boolean multiSelect = false;
	private IFilePathProcessor filePathProcessor = null;
	protected String[] fileExtensionNames;
	private Shell shell;
	
	public FileSelectionPageComponent(Shell shell, boolean multiSelect, Object[] initSelections, List<ViewerFilter> filters,String[] fileExtensionNames) {
		this(shell, false, initSelections, Collections.EMPTY_LIST);
		this.fileExtensionNames = fileExtensionNames;
		createFileExtensionNameFilter();
	}

	public FileSelectionPageComponent(Shell shell, boolean multiSelect, Object[] initSelections, List<ViewerFilter> filters) {
		this.shell = shell;
		this.initSelections = initSelections;
		if (filters != null) {
			this.filters.addAll(filters);
		}
		this.multiSelect = multiSelect;
	}

	public FileSelectionPageComponent(Shell shell, String[] fileExtensionNames) {
		this.shell = shell;
		this.fileExtensionNames = fileExtensionNames;
		createFileExtensionNameFilter();
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

	public void addFileSelectionListener(FileSelectionListener listener) {
		fileSelectionListeners.add(listener);
	}
	
	public void removeFileSelectionListener(FileSelectionListener listener) {
		fileSelectionListeners.remove(listener);
	}
	
	private void notifyFileSelectionListeners() {
		filePath = filePathWidget.getText();
		for(FileSelectionListener listener : fileSelectionListeners) {
			listener.fileSelected(filePath);
		}
	}
	
	/**
	 * @return the fileExtensionNames
	 */
	public String[] getFileExtensionNames() {
		return fileExtensionNames;
	}

	/**
	 * @param fileExtensionNames the fileExtensionNames to set
	 */
	public void setFileExtensionNames(String[] fileExtensionNames) {
		this.fileExtensionNames = fileExtensionNames;
	}

	public void createFileExtensionNameFilter(){
		if (this.fileExtensionNames != null && this.fileExtensionNames.length != 0) {
			ViewerFilter extensionNameFilter = new ViewerFilter() {
				@Override
				public boolean select(Viewer viewer, Object parentElement, Object element) {
					if (element instanceof IFile && fileExtensionNames != null) {
						String extName = ((IFile) element).getFileExtension();
						for (int i = 0; i < fileExtensionNames.length; i++) {
							String name = fileExtensionNames[i];
							if (name.equalsIgnoreCase(extName)) {
								return true;
							}
						}
						return false;
					}
					return true;
				}
			};
			this.filters.add(extensionNameFilter);
		}
	}

	public IFilePathProcessor getFilePathProcessor() {
		return filePathProcessor;
	}

	public void setFilePathProcessor(IFilePathProcessor filePathProcessor) {
		this.filePathProcessor = filePathProcessor;
	}

	public void createControl(Composite mainComposite) {
		GridLayout layout = new GridLayout();
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);

		layout.numColumns = 1;
		mainComposite.setLayout(layout);
		mainComposite.setLayoutData(gd);

		createFileSelectionComposite(mainComposite);

		// register the listener for controls
		hookFileTextModifyListener();
		hookBrowseButtonSelectionAdapter();
	}

	private void hookFileTextModifyListener() {
		filePathWidget.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent arg0) {
			}
			public void focusLost(FocusEvent arg0) {
				notifyFileSelectionListeners();
			}});		
		filePathWidget.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent event) {
			}
			public void keyPressed(KeyEvent event) {
				if(event.character == '\r') {
					notifyFileSelectionListeners();					
				}
			}
		});
	}

	private void hookBrowseButtonSelectionAdapter() {
		SelectionAdapter browseButtonSelectionAdapter = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Text relationT = null;
				if (e.getSource() == fileSystemBrowseButton) {
					relationT = filePathWidget;
				}
				openFileSelection(relationT);
			}
		};
		SelectionAdapter wbrowseButtonSelectionAdapter = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Text relationT = null;
				if (e.getSource() == workspaceBrowseButton) {
					relationT = filePathWidget;
				}
				openWorkSpaceSelection(relationT);
			}

		};

		workspaceBrowseButton.addSelectionListener(wbrowseButtonSelectionAdapter);
		// xmlFileSystemBrowseButton
		// .addSelectionListener(browseButtonSelectionAdapter);
		fileSystemBrowseButton.addSelectionListener(browseButtonSelectionAdapter);
	}

	protected String processFileSystemFilePath(String path) {
		if (filePathProcessor != null) {
			String s = filePathProcessor.processFileSystemPath(path);
			if (s != null) {
				return s;
			}
		}
		path = SmooksUIUtils.FILE_PRIX + path;
		return path;
	}

	protected String processWorkSpaceFilePath(IFile file) {
		if (filePathProcessor != null) {
			String s = filePathProcessor.processWorkBenchPath(file);
			if (s != null) {
				return s;
			}
		}
		String s = file.getFullPath().toPortableString();
		s = SmooksUIUtils.WORKSPACE_PRIX + s;
		return s;
	}

	public Text createFilePathText(Composite parent) {
		fileTextComposite = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		fileTextComposite.setLayoutData(gd);
		GridLayout xsdtgl = new GridLayout();
		xsdtgl.marginWidth = 0;
		xsdtgl.marginHeight = 0;
		xsdtgl.numColumns = 1;
		fileTextComposite.setLayout(xsdtgl);

		Text fileText = new Text(fileTextComposite, SWT.BORDER);		
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fileText.setLayoutData(gd);
		gd.grabExcessHorizontalSpace = true;

		return fileText;
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
		nfileLanel.setText(label);

		filePathWidget = createFilePathText(xsdComposite);

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
		fileSystemBrowseButton.setText(Messages.FileSelectionPageComponent_Browse_FileSys_Button);

		workspaceBrowseButton = new Button(browseButtonComposite, SWT.NONE);
		workspaceBrowseButton.setText(Messages.FileSelectionPageComponent_Browse_Workspace_Button);
		gd = new GridData();
		gd.horizontalAlignment = GridData.END;
		workspaceBrowseButton.setLayoutData(gd);

		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.END;
		fileSystemBrowseButton.setLayoutData(gd);
		
		return xsdComposite;
	}

	protected void setCompositeChildrenEnabled(Composite composite, boolean enabled) {
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

	private void openFileSelection(Text relationText) {
		FileDialog dialog = new FileDialog(shell);
		if (fileExtensionNames != null) {
			String s = ""; //$NON-NLS-1$
			for (int i = 0; i < fileExtensionNames.length; i++) {
				String exname = fileExtensionNames[i];
				s += "*." + exname + ";"; //$NON-NLS-1$ //$NON-NLS-2$
			}
			if (s.length() != 0) {
				s = s.substring(0, s.length() - 1);
				dialog.setFilterExtensions(new String[]{s,"*.*"}); //$NON-NLS-1$
			}
		}
		String path = dialog.open();
		if (path != null) {
			path = processFileSystemFilePath(path);
			relationText.setText(path);
			notifyFileSelectionListeners();
		}
	}

	private void openWorkSpaceSelection(Text relationT) {
		IFile[] files = WorkspaceResourceDialog.openFileSelection(shell,
				"Select Files", "", false, initSelections, filters); //$NON-NLS-1$ //$NON-NLS-2$
		// dialog.setInitialSelections(selectedResources);
		if (files.length > 0) {
			IFile file = files[0];
			String s = processWorkSpaceFilePath(file);
			relationT.setText(s);
			notifyFileSelectionListeners();
		}
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		if(!filePathWidget.isDisposed()) {
			filePathWidget.setText(filePath);
		}
	}
}
