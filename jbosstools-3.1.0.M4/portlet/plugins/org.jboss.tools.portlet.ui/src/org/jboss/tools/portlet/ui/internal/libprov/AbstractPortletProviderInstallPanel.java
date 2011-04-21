package org.jboss.tools.portlet.ui.internal.libprov;

import java.util.List;

import org.eclipse.jst.common.project.facet.ui.libprov.LibraryProviderOperationPanel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.core.libprov.AbstractLibraryProviderInstallOperationConfig;
import org.jboss.tools.portlet.ui.Messages;

public abstract class AbstractPortletProviderInstallPanel extends LibraryProviderOperationPanel {

	private Button addRichFacesLibraries;
	private Combo richFacesLibraryCombo;
	private Text richfacesText;
	private AbstractLibraryProviderInstallOperationConfig config;
	private Button richfacesButton;

	@Override
    public Control createControl( final Composite parent )
    {
        final Composite composite = new Composite( parent, SWT.NONE );
        final GridLayout layout = new GridLayout( 1, false );
        config = (AbstractLibraryProviderInstallOperationConfig) getOperationConfig();
		
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        composite.setLayout( layout );
        addMessage(composite);
        addPortletBridgeGroup(composite);
        addRichfacesGroup(composite);
        return composite;
    }
	
	protected abstract void addMessage(Composite composite);
	
	protected abstract void addPortletBridgeGroup(Composite composite);
	
	protected abstract List<String> getRichfacesTypes();

	private void addRichfacesGroup(Composite composite) {
		Group richfacesGroup = new Group(composite, SWT.NONE);
		richfacesGroup.setLayout(new GridLayout(2, false));
		richfacesGroup.setText(Messages.JSFPortletFacetInstallPage_Richfaces_Capabilities);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		richfacesGroup.setLayoutData(gd);
		addRichFacesLibraries = new Button(richfacesGroup,SWT.CHECK);
		addRichFacesLibraries.setText(Messages.JSFPortletFacetInstallPage_Add_Change_Richfaces_Libraries);
		addRichFacesLibraries.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				enableRichfacesLibraries();
				config.setAddRichfacesCapabilities(addRichFacesLibraries.getSelection());
			}
			
		});
		richFacesLibraryCombo = new Combo(richfacesGroup, SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan=2;
		richFacesLibraryCombo.setLayoutData(gd);
		configureRichfacesCombo();
		richFacesLibraryCombo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean enabled =IPortletConstants.LIBRARIES_PROVIDED_BY_RICHFACES.equals(richFacesLibraryCombo.getText());
				richfacesText.setEnabled(enabled);
				richfacesButton.setEnabled(enabled);
				config.setRichfacesType(richFacesLibraryCombo.getText());
			}
			
		});
		addRichFaces(richfacesGroup);
		
		boolean richFacesLibrariesSelected = config.isAddRichfacesCapabilities();
		addRichFacesLibraries.setSelection(richFacesLibrariesSelected);
		enableRichfacesLibraries();
		richFacesLibraryCombo.select(0);
	}

	private void configureRichfacesCombo() {
		List<String> types = getRichfacesTypes();
		for (String type:types) {
			richFacesLibraryCombo.add(type);
		}
	}
	
	private void addRichFaces(Composite parent) {
		richfacesText = new Text(parent, SWT.SINGLE | SWT.BORDER);
		richfacesText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		String richfacesRuntime = config.getRichfacesRuntime();
		if (richfacesRuntime == null) {
			richfacesRuntime = ""; //$NON-NLS-1$
		}
		richfacesText.setText(richfacesRuntime);
		richfacesText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				config.setRichfacesRuntime(richfacesText.getText());
			}

		});

		richfacesButton = new Button(parent, SWT.PUSH);
		richfacesButton.setText(Messages.JSFPortletFacetInstallPage_Browse);
		richfacesButton
				.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		richfacesButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleRichFacesButtonPressed();
			}

		});
	}
	
	private void handleRichFacesButtonPressed() {
		String lastPath = richfacesText.getText();
		DirectoryDialog dialog = new DirectoryDialog(getShell(), SWT.MULTI);
		dialog.setText(Messages.JSFPortletFacetInstallPage_Select_RichFaces_Runtime);
		dialog.setFilterPath(lastPath);
		String res = dialog.open();
		if (res == null) {
			return;
		}
		String newPath = dialog.getFilterPath();
		richfacesText.setText(newPath);
	}
	
	private void enableRichfacesLibraries() {
		boolean enabled = addRichFacesLibraries.getSelection();
		richFacesLibraryCombo.setEnabled(enabled);
		if (enabled) {
			enabled =IPortletConstants.LIBRARIES_PROVIDED_BY_RICHFACES.equals(richFacesLibraryCombo.getText());
			richfacesText.setEnabled(enabled);
			richfacesButton.setEnabled(enabled);
		} else {
			richfacesText.setEnabled(false);
			richfacesButton.setEnabled(false);
		}
	}

	protected Shell getShell() {
		return Display.getCurrent().getActiveShell();
	}

	
}
