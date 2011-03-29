package org.jboss.tools.portlet.ui.internal.libprov;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectBase;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.core.PortletCoreActivator;
import org.jboss.tools.portlet.core.libprov.JSFPortletbridgeRuntimeLibraryProviderInstallOperationConfig;
import org.jboss.tools.portlet.ui.Messages;
import org.jboss.tools.portlet.ui.PortletUIActivator;

public class JSFPortletbridgeRuntimeProviderInstallPanel extends
		AbstractPortletProviderInstallPanel {

	@Override
	protected void addMessage(Composite composite) {
		
	}

	@Override
	protected List<String> getRichfacesTypes() {
		List<String> types = new ArrayList<String>();
		//types.add(IPortletConstants.LIBRARIES_PROVIDED_BY_PORTLETBRIDGE);
		types.add(IPortletConstants.LIBRARIES_PROVIDED_BY_RICHFACES);
		return types;
	}
	
	@Override
	protected void addPortletBridgeGroup(Composite composite, boolean isEPP) {
		final JSFPortletbridgeRuntimeLibraryProviderInstallOperationConfig config = (JSFPortletbridgeRuntimeLibraryProviderInstallOperationConfig) getOperationConfig();
		
		if (config.isEPP()) {
			return;
		}
		GridData gd;
		Group portletBridgeGroup = new Group(composite, SWT.NONE);
		portletBridgeGroup.setText(Messages.JSFPortletFacetInstallPage_Portletbridge_Runtime);
		portletBridgeGroup.setLayout(new GridLayout(2, false));
		gd = new GridData(GridData.FILL_HORIZONTAL);
		portletBridgeGroup.setLayoutData(gd);
		
		final Text folderText = new Text(portletBridgeGroup, SWT.SINGLE | SWT.BORDER);
		folderText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		if (config.isEPP()) {
			String portletBridgeHome = PortletCoreActivator.getEPPDir(config.getFacetedProject(), PortletCoreActivator.PORTLETBRIDGE).getAbsolutePath();
			folderText.setText(portletBridgeHome);
			folderText.setEnabled(false);
		} else {
			folderText.setEnabled(true);
			folderText.addModifyListener(new ModifyListener() {
			
				public void modifyText(ModifyEvent e) {
					config.setPortletbridgeHome(folderText.getText());
					IDialogSettings section = getPortletBridgeSection();
					section.put(IPortletConstants.PORTLET_BRIDGE_HOME, folderText.getText());
				}
			});
			String portletbridgeRuntime = config.getPortletbridgeHome();
			if (portletbridgeRuntime == null) {
				IDialogSettings section = getPortletBridgeSection();
				portletbridgeRuntime = section.get(IPortletConstants.PORTLET_BRIDGE_HOME);
				if (portletbridgeRuntime == null){
					portletbridgeRuntime = ""; //$NON-NLS-1$
				}
				config.setPortletbridgeHome(portletbridgeRuntime);
			}
			if (portletbridgeRuntime == null || portletbridgeRuntime.length() == 0 ) {
				IFacetedProjectBase facetedProject = config.getFacetedProject();
				IPath configPath = PortletUIActivator.getJBossConfigPath(facetedProject);
				if (configPath != null) {
					IPath portalPath = configPath.append(IPortletConstants.SERVER_DEFAULT_DEPLOY_GATEIN);
					File portalFile = portalPath.toFile();
					if (portalFile != null && portalFile.exists()) {
						IPath eppHome = configPath.removeLastSegments(3);
						IPath pbPath = eppHome.append("portletbridge"); //$NON-NLS-1$
						File pbFile = pbPath.toFile();
						if (pbFile != null && pbFile.exists()) {
							portletbridgeRuntime = pbFile.getAbsolutePath();
						}
					}
				}
			}
			folderText.setText(portletbridgeRuntime);
		}
		Button folderButton = new Button(portletBridgeGroup, SWT.PUSH);
		folderButton.setText(Messages.JSFPortletFacetInstallPage_Browse);
		folderButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		folderButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleFolderButtonPressed(folderText);
			}

		});
		folderButton.setEnabled(!isEPP);
		
	}

	private IDialogSettings getPortletBridgeSection() {
		IDialogSettings dialogSettings = PortletUIActivator.getDefault().getDialogSettings();
		IDialogSettings section = dialogSettings.getSection(IPortletConstants.PORTLET_BRIDGE_HOME);
		if (section == null) {
			section = dialogSettings.addNewSection(IPortletConstants.PORTLET_BRIDGE_HOME);
		}
		return section;
	}
	
	private void handleFolderButtonPressed(Text folderText) {
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
}
