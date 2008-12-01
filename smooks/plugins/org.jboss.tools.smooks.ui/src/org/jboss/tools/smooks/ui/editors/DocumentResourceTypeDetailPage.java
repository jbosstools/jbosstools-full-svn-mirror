/**
 * 
 */
package org.jboss.tools.smooks.ui.editors;

import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.ui.dialogs.WorkspaceResourceDialog;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.smooks.model.ResourceType;
import org.jboss.tools.smooks.model.SmooksPackage;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;

/**
 * @author dart
 * 
 */
public class DocumentResourceTypeDetailPage extends
		AbstractSmooksModelDetailPage {

	private static final int INNER = 0;
	private static final int EXTERNAL = 1;
	private Text text;
	private Text selectorText;
	private Button innerCheckButton;
	private Button externalCheckButton;
	private Text innerContentText;
	private Button browseButton;
	private boolean isInit = true;

	public DocumentResourceTypeDetailPage(SmooksFormEditor parentEditor,
			EditingDomain domain) {
		super(parentEditor, domain);
	}

	protected void createExternlaSelectionGUI(Composite parent) {

		this.formToolKit.createLabel(parent, Messages
				.getString("DocumentResourceTypeDetailPage.DocPath")); //$NON-NLS-1$
		Composite fileCom = formToolKit.createComposite(parent);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		fileCom.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		fileCom.setLayout(layout);
		text = formToolKit.createText(fileCom, ""); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		text.setLayoutData(gd);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (!canFireChange)
					return;
				resetPath(text.getText());
			}
		});

		browseButton = formToolKit
				.createButton(
						fileCom,
						Messages
								.getString("DocumentResourceTypeDetailPage.BrowseFile"), SWT.NONE); //$NON-NLS-1$
		browseButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				browseFileSystem();
			}

		});
		formToolKit.paintBordersFor(fileCom);

	}

	protected void handleRadioButtons() {
		externalCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean checked = externalCheckButton.getSelection();
				if (checked) {
					switchGUI(EXTERNAL);
				}
			}
		});

		innerCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean checked = innerCheckButton.getSelection();
				if (checked) {
					switchGUI(INNER);
				}
			}
		});
	}

	protected void createSectionContents(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		parent.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);

		this.formToolKit.createLabel(parent, Messages
				.getString("DocumentResourceTypeDetailPage.Selector")); //$NON-NLS-1$
		selectorText = formToolKit.createText(parent, ""); //$NON-NLS-1$
		selectorText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (!canFireChange)
					return;
				resetSelector(selectorText.getText());
			}
		});
		selectorText.setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		externalCheckButton = formToolKit.createButton(parent,
				"External File Selection", SWT.RADIO);
		externalCheckButton.setLayoutData(gd);

		createExternlaSelectionGUI(parent);

		// Inner contents modify
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		innerCheckButton = formToolKit.createButton(parent,
				"Inner template file contents", SWT.RADIO);
		innerCheckButton.setLayoutData(gd);

		createInnerContentsGUI(parent);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		formToolKit.createSeparator(parent, SWT.HORIZONTAL).setLayoutData(gd);

		formToolKit.paintBordersFor(parent);

		handleRadioButtons();

	}

	protected void createInnerContentsGUI(Composite parent) {
		GridData gd = new GridData(GridData.BEGINNING);
		gd.verticalAlignment = GridData.BEGINNING;
		formToolKit.createLabel(parent, "File Contents").setLayoutData(gd);
		innerContentText = formToolKit.createText(parent, "", SWT.MULTI
				| SWT.V_SCROLL | SWT.H_SCROLL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 200;
		innerContentText.setLayoutData(gd);
	}

	protected void resetSelector(String selector) {
		Command command = SetCommand.create(domain, resourceConfig,
				SmooksPackage.eINSTANCE.getResourceConfigType_Selector(),
				selector);
		domain.getCommandStack().execute(command);
		// resourceConfigList.getse
	}

	public void switchGUI(int key) {
		ResourceType resource = resourceConfig.getResource();
		if (key == INNER) {
			this.browseButton.setEnabled(false);
			this.text.setEnabled(false);
			this.innerContentText.setEnabled(true);
			SmooksModelUtils.cleanTextToSmooksType(resource);
			String text = innerContentText.getText();
			if (text != null) {
				SmooksModelUtils.setCDATAToAnyType(resource, text);
			}
		} else {
			this.browseButton.setEnabled(true);
			this.text.setEnabled(true);
			this.innerContentText.setEnabled(false);
			SmooksModelUtils.cleanCDATAToSmooksType(resource);
			String text = this.text.getText();
			if (text != null) {
				SmooksModelUtils.setTextToAnyType(resource, text);
			}
		}
		this.parentEditor.fireEditorDirty(true);
	}

	protected void browseFileSystem() {
		IFile[] files = WorkspaceResourceDialog.openFileSelection(
				this.parentEditor.getSite().getShell(),
				"", "", false, null, Collections.EMPTY_LIST); //$NON-NLS-1$ //$NON-NLS-2$
		// dialog.setInitialSelections(selectedResources);
		if (files.length > 0) {
			IFile file = files[0];
			String s = file.getFullPath().toString();
			text.setText(s);
			return;
		}
	}

	protected void resetPath(String path) {
		if (this.resourceConfig != null) {
			ResourceType resource = resourceConfig.getResource();
			if (resource == null) {
				return;
			}
			SmooksModelUtils.setTextToAnyType(resource, path);
		}
	}

	protected void initSectionUI() {
		if (this.resourceConfig != null) {
			String selector = resourceConfig.getSelector();
			if (selector != null)
				selectorText.setText(selector);
			ResourceType resource = resourceConfig.getResource();
			if (resource != null) {
				if (isInit) {

					if (SmooksModelUtils.isInnerFileContents(resourceConfig)) {
						String cdata = resource.getCDATAValue();
						if (cdata == null)
							cdata = "";
						innerContentText.setText(cdata);
						innerCheckButton.setSelection(true);
						switchGUI(INNER);
						isInit = false;
					}
					if (SmooksModelUtils
							.isFilePathResourceConfig(resourceConfig)) {
						String path = resource.getStringValue();
						if (path == null)
							path = ""; //$NON-NLS-1$
						text.setText(path);
						externalCheckButton.setSelection(true);
						switchGUI(EXTERNAL);
						isInit = false;
					}

				} else {
					int style = getFileContentStyle();
					if (style == INNER) {
						String cdata = resource.getCDATAValue();
						if (cdata == null)
							cdata = "";
						innerContentText.setText(cdata);
					}
					if (style == EXTERNAL) {
						String path = resource.getStringValue();
						if (path == null)
							path = ""; //$NON-NLS-1$
						text.setText(path);
					}
				}
			}
		}
	}

	private int getFileContentStyle() {
		if (externalCheckButton.getSelection()
				&& innerCheckButton.getSelection()) {
			return -1;
		}

		if (externalCheckButton.getSelection())
			return EXTERNAL;
		if (innerCheckButton.getSelection())
			return INNER;
		return -1;
	}

}
