package org.jboss.tools.profiler.internal.ui.launchtabs;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.jboss.tools.profiler.internal.ui.JBossProfilerUiPlugin;
import org.jboss.tools.profiler.internal.ui.Messages;
import org.jboss.tools.profiler.internal.ui.util.FileNameFilter;

public abstract class BaseBlock {

	protected BaseLaunchConfigurationTab fTab;

	private Button fVariablesButton;
	private Button fFileSystemButton;
	private Button fWorkspaceButton;

	protected Text fLocationText;

	protected Listener fListener = new Listener();

	protected Link fLocationLink;

	class Listener extends SelectionAdapter implements ModifyListener {
		public void widgetSelected(SelectionEvent e) {
			Object source = e.getSource();
			if (source == fFileSystemButton) {
				handleBrowseFileSystem();
			} else if (source == fWorkspaceButton) {
				handleBrowseWorkspace();
			} else if (source == fVariablesButton) {
				handleInsertVariable();
			} else {
				fTab.updateLaunchConfigurationDialog();
			}
		}

		public void modifyText(ModifyEvent e) {
			fTab.updateLaunchConfigurationDialog();
		}
	}

	public BaseBlock(BaseLaunchConfigurationTab tab) {
		fTab = tab;
	}

	protected void createText(Composite parent, String text, int indent) {
		fLocationLink = new Link(parent, SWT.NONE);
		fLocationLink.setText("<a>" + text + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
		if (indent > 0) {
			GridData gd = new GridData();
			gd.horizontalIndent = indent;
			fLocationLink.setLayoutData(gd);
		}

		fLocationText = new Text(parent, SWT.SINGLE | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 400;
		fLocationText.setLayoutData(gd);
		fLocationText.addModifyListener(fListener);

		fLocationLink.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					String path = VariablesPlugin.getDefault()
							.getStringVariableManager()
							.performStringSubstitution(getLocation(), false);
					File f = new File(path);
					if (f.exists()) {
						Program.launch(f.getCanonicalPath());
					} else {
						openWarning();
					}
				} catch (CoreException ex) {
					openWarning();
				} catch (IOException ioex) {
					openWarning();
				}
			}

			private void openWarning() {
				MessageDialog.openWarning(JBossProfilerUiPlugin
						.getActiveWorkbenchShell(),
						isFile() ? Messages.BaseBlock_open_file : Messages.BaseBlock_open_directory,
						isFile() ? Messages.BaseBlock_file_not_found
								: Messages.BaseBlock_directory_not_found);
			}
		});

	}

	/**
	 * Sets width and height hint for the button control. <b>Note:</b> This is a
	 * NOP if the button's layout data is not an instance of
	 * <code>GridData</code>.
	 * 
	 * @param the
	 *            button for which to set the dimension hint
	 */
	public static void setButtonDimensionHint(Button button) {
		Dialog.applyDialogFont(button);
		Assert.isNotNull(button);
		Object gd = button.getLayoutData();
		if (gd instanceof GridData) {
			((GridData) gd).widthHint = getButtonWidthHint(button);
		}
	}

	/**
	 * Returns a width hint for a button control.
	 */
	public static int getButtonWidthHint(Button button) {
		if (button.getFont().equals(JFaceResources.getDefaultFont()))
			button.setFont(JFaceResources.getDialogFont());
		PixelConverter converter = new PixelConverter(button);
		int widthHint = converter
				.convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		return Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT,
				true).x);
	}

	protected void createButtons(Composite parent, String[] buttonLabels) {
		fWorkspaceButton = createButton(parent, buttonLabels[0]);
		fFileSystemButton = createButton(parent, buttonLabels[1]);
		fVariablesButton = createButton(parent, buttonLabels[2]);
	}

	protected Button createButton(Composite parent, String text) {
		Button button = new Button(parent, SWT.PUSH);
		button.setText(text);
		button.setLayoutData(new GridData());
		button.addSelectionListener(fListener);
		setButtonDimensionHint(button);
		return button;
	}

	protected String[] getFileFilter() {
		return new String[] { "*.*" };
	}

	protected void handleBrowseFileSystem() {
		if (isFile()) {
			FileDialog dialog = new FileDialog(fTab.getControl().getShell());
			dialog.setFilterExtensions(getFileFilter()); //$NON-NLS-1$
			dialog.setFilterPath(getLocation());
			dialog.setText(Messages.BaseBlock_select_properties_file);
			String res = dialog.open();
			if (res != null) {
				fLocationText.setText(res);
			}
		} else {
			DirectoryDialog dialog = new DirectoryDialog(fTab.getControl()
					.getShell());
			dialog.setFilterPath(getLocation());
			dialog.setText(Messages.BaseBlock_select_directory);
			dialog.setMessage(Messages.BaseBlock_choose_a_directory);
			String result = dialog.open();
			if (result != null)
				fLocationText.setText(result);
		}
	}

	protected IFile getFile() {
		if (!isFile())
			return null;

		String path = getLocation();
		if (path.length() > 0) {
			IResource res = null;
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			if (path.startsWith("${workspace_loc:")) { //$NON-NLS-1$
				IStringVariableManager manager = VariablesPlugin.getDefault()
						.getStringVariableManager();
				try {
					path = manager.performStringSubstitution(path, false);
					IPath uriPath = new Path(path).makeAbsolute();
					IFile[] containers = root.findFilesForLocationURI(URIUtil
							.toURI(uriPath));
					if (containers.length > 0)
						res = containers[0];
				} catch (CoreException e) {
					// ignore
				}
			} else {
				res = root.findMember(path);
			}
			if (res instanceof IFile) {
				return (IFile) res;
			}
		}
		return null;
	}

	protected void handleBrowseWorkspace() {
		if (isFile()) {
			ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
					fTab.getControl().getShell(), new WorkbenchLabelProvider(),
					new WorkbenchContentProvider());

			IFile file = getFile();
			if (file != null)
				dialog.setInitialSelection(file);
			dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
			dialog.addFilter(new FileNameFilter(getFileFilter())); //$NON-NLS-1$
			dialog.setAllowMultiple(false);
			dialog.setTitle(Messages.BaseBlock_select_file);
			dialog.setMessage("");
			dialog.setValidator(new ISelectionStatusValidator() {
				public IStatus validate(Object[] selection) {
					if (selection.length > 0 && selection[0] instanceof IFile)
						return new Status(IStatus.OK, JBossProfilerUiPlugin
								.getPluginId(), IStatus.OK, "", null); //$NON-NLS-1$

					return new Status(IStatus.ERROR, JBossProfilerUiPlugin
							.getPluginId(), IStatus.ERROR, "", null); //$NON-NLS-1$
				}
			});
			if (dialog.open() == Window.OK) {
				file = (IFile) dialog.getFirstResult();
				fLocationText
						.setText("${workspace_loc:" + file.getFullPath().makeRelative() + "}"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			ContainerSelectionDialog dialog = new ContainerSelectionDialog(fTab
					.getControl().getShell(), getContainer(), true,
					Messages.BaseBlock_choose_location_relative_to_ws);
			if (dialog.open() == Window.OK) {
				Object[] result = dialog.getResult();
				if (result.length == 0)
					return;
				IPath path = (IPath) result[0];
				fLocationText
						.setText("${workspace_loc:" + path.makeRelative().toString() + "}"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}

	/**
	 * Returns the selected workspace container,or <code>null</code>
	 */
	protected IContainer getContainer() {
		String path = getLocation();
		if (path.length() > 0) {
			IResource res = null;
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			if (path.startsWith("${workspace_loc:")) { //$NON-NLS-1$
				IStringVariableManager manager = VariablesPlugin.getDefault()
						.getStringVariableManager();
				try {
					path = manager.performStringSubstitution(path, false);
					IPath uriPath = new Path(path).makeAbsolute();
					IContainer[] containers = root
							.findContainersForLocationURI(URIUtil
									.toURI(uriPath));
					if (containers.length > 0) {
						res = containers[0];
					}
				} catch (CoreException e) {
					// ignore
				}
			} else {
				res = root.findMember(path);
			}
			if (res instanceof IContainer) {
				return (IContainer) res;
			}
		}
		return ResourcesPlugin.getWorkspace().getRoot();
	}

	private void handleInsertVariable() {
		StringVariableSelectionDialog dialog = new StringVariableSelectionDialog(
				fTab.getControl().getShell());
		if (dialog.open() == Window.OK)
			fLocationText.insert(dialog.getVariableExpression());
	}

	protected String getLocation() {
		return fLocationText.getText().trim();
	}

	protected void setLocation(String t) {
		fLocationText.setText(t);
	}
	
	public String validate() {
		return (fLocationText == null || (fLocationText.isEnabled() && getLocation()
				.length() == 0)) ? NLS.bind(Messages.BaseBlock_the_is_not_specified,
				getName()) : null;
	}

	protected abstract String getName();

	/**
	 * @return true if the block edits a file, false otherwise (i.e. directory)
	 */
	protected abstract boolean isFile();

	int count;
	protected void enableBrowseSection(boolean enabled) {
		System.out.println("enableBS: " + enabled + " " + count++ + "th time"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		fLocationLink.setEnabled(enabled);
		fLocationText.setEnabled(enabled);
		fFileSystemButton.setEnabled(enabled);
		fWorkspaceButton.setEnabled(enabled);
		fVariablesButton.setEnabled(enabled);		
	}

	public void createControl(Composite parent) {
		
		Composite group = createComposite(parent, parent.getFont(), 3, 3, GridData.FILL_HORIZONTAL, 0, 0);
		
		createText(group, getName(), 0);
		
		//new Label(group, SWT.None);
		//new Label(group, SWT.None);
		
		Composite comp = createComposite(group, parent.getFont(), 4, 3, GridData.FILL_BOTH, 0, 0);
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		new Label(comp, SWT.NONE).setLayoutData(gd);
		// buttons
		createButtons(comp, new String[] {Messages.BaseBlock_workspace, Messages.BaseBlock_filesystem, Messages.BaseBlock_variables});		

	}
	
	public static Composite createComposite(Composite parent, Font font,
			int columns, int hspan, int fill, int marginwidth, int marginheight) {
		Composite g = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(columns, false);
		
		layout.marginWidth = marginwidth;
		layout.marginHeight = marginheight;
		g.setLayout(layout);
		g.setFont(font);
		GridData gd = new GridData(fill);
		gd.horizontalSpan = hspan;
		g.setLayoutData(gd);
		return g;
	}

	public static Text createSingleText(Composite parent, int hspan) {
		Text t = new Text(parent, SWT.SINGLE | SWT.BORDER);
		t.setFont(parent.getFont());
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = hspan;
		t.setLayoutData(gd);
		return t;
	}



}
