package org.jboss.tools.portlet.ui.internal.project.facet;

import java.io.File;
import java.io.FilenameFilter;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.internal.datamodel.ui.DataModelSynchHelper;
import org.eclipse.wst.common.project.facet.ui.AbstractFacetWizardPage;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.ui.Messages;
import org.jboss.tools.portlet.ui.PortletUIActivator;

/**
 * @author snjeza
 */
public class JSFPortletFacetInstallPage extends AbstractFacetWizardPage {

	private IDialogSettings dialogSettings;
	private IDataModel model;
	private IDialogSettings jsfSection;
	private boolean deployJars;
	private String portletbridgeRuntime;
	private Text folderText;
	private DataModelSynchHelper synchHelper;
	private Button folderButton;
	private Button deployButton;

	public JSFPortletFacetInstallPage() {
		super("JSFPortletProjectConfigurationWizardPage"); //$NON-NLS-1$
		setTitle(Messages.JSFPortletFacetInstallPage_JBoss_JSF_Portlet_Capabilities);
		setDescription(Messages.JSFPortletFacetInstallPage_Add_JBoss_JSF_Portlet_capabilities_to_this_Web_Project);
		// ImageDescriptor imageDesc = getDefaultPageImageDescriptor( );
		// if ( imageDesc != null )
		// setImageDescriptor( imageDesc );
		dialogSettings = PortletUIActivator.getDefault().getDialogSettings();
		jsfSection = dialogSettings.getSection(IPortletConstants.JSF_SECTION);
		if (jsfSection == null) {
			jsfSection = dialogSettings
					.addNewSection(IPortletConstants.JSF_SECTION);
			deployJars = true;
			portletbridgeRuntime = ""; //$NON-NLS-1$
			jsfSection.put(IPortletConstants.PORTLET_BRIDGE_RUNTIME,
					portletbridgeRuntime);
		} else {
			deployJars = jsfSection.getBoolean(IPortletConstants.DEPLOY_JARS);
			portletbridgeRuntime = jsfSection
					.get(IPortletConstants.PORTLET_BRIDGE_RUNTIME);
			if (portletbridgeRuntime == null) {
				portletbridgeRuntime = ""; //$NON-NLS-1$
				jsfSection.put(IPortletConstants.PORTLET_BRIDGE_RUNTIME,
						portletbridgeRuntime);
			}
		}

	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		synchHelper = new DataModelSynchHelper(model);
		addFolderGroup(composite);
		// createButton(composite);
		setControl(composite);
	}

	private void createButton(Composite composite) {
		deployButton = new Button(composite, SWT.CHECK);
		deployButton.setText(Messages.JSFPortletFacetInstallPage_Deploy_jars);
		deployButton.setSelection(deployJars);
		deployButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				model.setBooleanProperty(IPortletConstants.DEPLOY_JARS,
						deployButton.getSelection());
			}

		});
	}

	public void setConfig(Object config) {
		this.model = (IDataModel) config;
	}

	private void addFolderGroup(Composite composite) {
		// folder
		Label folderLabel = new Label(composite, SWT.LEFT);
		folderLabel.setText(Messages.JSFPortletFacetInstallPage_Portletbridge_Runtime);
		folderLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		folderText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		folderText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		synchHelper.synchText(folderText,
				IPortletConstants.PORTLET_BRIDGE_RUNTIME, null);

		folderText.setText(portletbridgeRuntime);
		folderText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				validatePortletBridge();
			}

		});

		folderButton = new Button(composite, SWT.PUSH);
		folderButton.setText(Messages.JSFPortletFacetInstallPage_Browse);
		folderButton
				.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		folderButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleFolderButtonPressed();
			}

		});
		validatePortletBridge();
	}

	private void validatePortletBridge() {
		String folderString = folderText.getText();
		folderString = folderString.trim();
		if (folderString.length() <= 0) {
			setErrorMessage(Messages.JSFPortletFacetInstallPage_Portletbridge_Runtime_directory_is_required);
			setPageComplete(false);
			return;
		}
		File folder = new File(folderString);
		if (!folder.exists() || !folder.isDirectory()) {
			setInvalidPortletbridgeRuntime();
			return;
		}
		String[] fileList = folder.list(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				if (name.startsWith("portletbridge") || name.endsWith(".jar")) { //$NON-NLS-1$ //$NON-NLS-2$
					return true;
				}
				return false;
			}

		});
		if (fileList.length < 2) {
			setInvalidPortletbridgeRuntime();
			return;
		}
		portletbridgeRuntime = folderText.getText();
		setErrorMessage(null);
		setPageComplete(true);
	}

	private void setInvalidPortletbridgeRuntime() {
		setErrorMessage(Messages.JSFPortletFacetInstallPage_Invalid_Portletbridge_Runtime_directory);
		setPageComplete(false);
	}

	protected void handleFolderButtonPressed() {
		String lastPath = folderText.getText();
		DirectoryDialog dialog = new DirectoryDialog(getShell(), SWT.MULTI);
		dialog.setText(Messages.JSFPortletFacetInstallPage_Select_Portletbridge_Runtime);
		dialog.setFilterPath(lastPath);
		String res = dialog.open();
		if (res == null) {
			return;
		}
		String newPath = dialog.getFilterPath();
		folderText.setText(newPath);
	}

	@Override
	public void transferStateToConfig() {
		super.transferStateToConfig();
		if (deployButton != null) {
			jsfSection.put(IPortletConstants.DEPLOY_JARS, deployButton
					.getSelection());
		}
		jsfSection.put(IPortletConstants.PORTLET_BRIDGE_RUNTIME,
				portletbridgeRuntime);
	}

}
