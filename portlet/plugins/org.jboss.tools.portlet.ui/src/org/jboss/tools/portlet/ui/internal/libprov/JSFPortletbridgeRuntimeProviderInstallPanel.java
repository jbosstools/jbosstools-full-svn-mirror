package org.jboss.tools.portlet.ui.internal.libprov;

import java.io.File;
import java.io.FilenameFilter;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jst.common.project.facet.ui.libprov.LibraryProviderOperationPanel;
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
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.core.libprov.JSFPortletbridgeRuntimeLibraryProviderInstallOperationConfig;
import org.jboss.tools.portlet.ui.Messages;
import org.jboss.tools.portlet.ui.PortletUIActivator;

public class JSFPortletbridgeRuntimeProviderInstallPanel extends LibraryProviderOperationPanel {

	@Override
    public Control createControl( final Composite parent )
    {
        final Composite composite = new Composite( parent, SWT.NONE );
        final GridLayout layout = new GridLayout( 1, false );

        layout.marginWidth = 0;
        layout.marginHeight = 0;
        composite.setLayout( layout );
        addPortletBridgeGroup(composite);
//        final Link link = new Link( composite, SWT.WRAP );
//        final GridData data = new GridData( SWT.FILL, SWT.BEGINNING, true, false );
//        data.widthHint = 300;
//        link.setLayoutData( data );
//        link.setText( Messages.JSFPortletServerRuntimeProviderInstallPanel_The_targeted_runtime_contains_a_portlet_library );
//        
        return composite;
    }
	
	private void addPortletBridgeGroup(Composite composite) {
		GridData gd;
		Group portletBridgeGroup = new Group(composite, SWT.NONE);
		portletBridgeGroup.setText(Messages.JSFPortletFacetInstallPage_Portletbridge_Runtime);
		portletBridgeGroup.setLayout(new GridLayout(2, false));
		gd = new GridData(GridData.FILL_HORIZONTAL);
		portletBridgeGroup.setLayoutData(gd);
		
		final Text folderText = new Text(portletBridgeGroup, SWT.SINGLE | SWT.BORDER);
		folderText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		final JSFPortletbridgeRuntimeLibraryProviderInstallOperationConfig config = (JSFPortletbridgeRuntimeLibraryProviderInstallOperationConfig) getOperationConfig();
		
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
		folderText.setText(portletbridgeRuntime);
		
		Button folderButton = new Button(portletBridgeGroup, SWT.PUSH);
		folderButton.setText(Messages.JSFPortletFacetInstallPage_Browse);
		folderButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		folderButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleFolderButtonPressed(folderText);
			}

		});
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

	private Shell getShell() {
		return Display.getCurrent().getActiveShell();
	}
}
