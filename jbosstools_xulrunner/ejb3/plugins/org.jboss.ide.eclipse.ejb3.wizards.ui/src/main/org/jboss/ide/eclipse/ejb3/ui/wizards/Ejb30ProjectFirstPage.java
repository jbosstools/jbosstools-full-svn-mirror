package org.jboss.ide.eclipse.ejb3.ui.wizards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jst.j2ee.internal.wizard.J2EEComponentFacetCreationWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetProjectCreationDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.DataModelPropertyDescriptor;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntime;
import org.eclipse.wst.project.facet.ProductManager;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.web.internal.ResourceHandler;
import org.jboss.ide.eclipse.as.core.runtime.EJB30SupportVerifier;
import org.jboss.ide.eclipse.as.core.runtime.server.AbstractJBossServerRuntime;

public class Ejb30ProjectFirstPage extends J2EEComponentFacetCreationWizardPage {
	public static final String EJB30_FACET_ID = "jbide.ejb30";
	private Label warningLabel;
	
	protected String getModuleFacetID() {
		return EJB30_FACET_ID;
	}
	protected String getModuleTypeID() {
		return null;
	}

	public Ejb30ProjectFirstPage(IDataModel model, String pageName) {
		super(model, pageName);
		setTitle("Create a EJB3 Project");
		setDescription("Create a EJB3 Project in the workspace");
	}
	
	
	public void restoreDefaultSettings() {
		IDialogSettings settings = getDialogSettings();
		//restoreRuntimeSettings2(settings, model);
	}

	private static String NULL_RUNTIME = "NULL_RUNTIME"; //$NON-NLS-1$
	private static String MRU_RUNTIME_STORE = "MRU_RUNTIME_STORE"; //$NON-NLS-1$

	private void restoreRuntimeSettings2(IDialogSettings settings, IDataModel model){
		if (settings != null) {
			if (!model.isPropertySet(IFacetProjectCreationDataModelProperties.FACET_RUNTIME)) {
				boolean runtimeSet = false;
				String[] mruRuntimeArray = settings.getArray(MRU_RUNTIME_STORE);
				DataModelPropertyDescriptor[] descriptors = model.getValidPropertyDescriptors(IFacetProjectCreationDataModelProperties.FACET_RUNTIME);
				List mruRuntimes = new ArrayList();
				if (mruRuntimeArray == null) {
					List defRuntimes = ProductManager.getDefaultRuntimes();
					for (Iterator iter = defRuntimes.iterator(); iter.hasNext();)
						mruRuntimes.add(((IRuntime) iter.next()).getName());
				} else {
					mruRuntimes.addAll(Arrays.asList(mruRuntimeArray));
				}
				if (!mruRuntimes.isEmpty()) {
					for (int i = 0; i < mruRuntimes.size() && !runtimeSet; i++) {
						for (int j = 0; j < descriptors.length-1 && !runtimeSet; j++) {
							if (mruRuntimes.get(i).equals(descriptors[j].getPropertyDescription())) {
								model.setProperty(IFacetProjectCreationDataModelProperties.FACET_RUNTIME, descriptors[j].getPropertyValue());
								runtimeSet = true;
							}
						}
						if(!runtimeSet && mruRuntimes.get(i).equals(NULL_RUNTIME) && descriptors.length != 0){
							model.setProperty(IFacetProjectCreationDataModelProperties.FACET_RUNTIME, descriptors[descriptors.length -1].getPropertyValue());
							runtimeSet = true;
						}
					}
				}
				if (!runtimeSet && descriptors.length > 0) {
					model.setProperty(IFacetProjectCreationDataModelProperties.FACET_RUNTIME, descriptors[0].getPropertyValue());
				}
			}
		}
	}

	
	/*
	 * Coppied from superclass and added merely a label
	 * (non-Javadoc)
	 * @see org.eclipse.wst.web.ui.internal.wizards.DataModelFacetCreationWizardPage#createServerTargetComposite(org.eclipse.swt.widgets.Composite)
	 */
	protected void createServerTargetComposite(Composite parent) {
        Group group = new Group(parent, SWT.NONE);
        group.setText(ResourceHandler.TargetRuntime);
        group.setLayoutData(gdhfill());
        group.setLayout(new GridLayout(2, false));
		serverTargetCombo = new Combo(group, SWT.BORDER | SWT.READ_ONLY);
		serverTargetCombo.setLayoutData(gdhfill());
		Button newServerTargetButton = new Button(group, SWT.NONE);
		newServerTargetButton.setText(ResourceHandler.NewDotDotDot);
		newServerTargetButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (!internalLaunchNewRuntimeWizard(getShell(), model)) {
					//Bugzilla 135288
					//setErrorMessage(ResourceHandler.InvalidServerTarget);
				}
			}
		});
		
		warningLabel = new Label(group, SWT.NONE);
		warningLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		warningLabel.setText("Warning: The selected runtime is missing one or more required libraries");
		warningLabel.setVisible(false);
		
		serverTargetCombo.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				hideOrShowWarning();
			} 
		} );
		
		Control[] deps = new Control[]{newServerTargetButton};
		synchHelper.synchCombo(serverTargetCombo, FACET_RUNTIME, deps);
		if (serverTargetCombo.getSelectionIndex() == -1 && serverTargetCombo.getVisibleItemCount() != 0)
			serverTargetCombo.select(0);
		
		hideOrShowWarning();
	}
	
	protected void hideOrShowWarning() {
		int index = serverTargetCombo.getSelectionIndex();
		if (index > 0)
		{
			String runtimeName = serverTargetCombo.getItem(index);
			org.eclipse.wst.server.core.IRuntime rt = ServerCore.findRuntime(runtimeName);
			warningLabel.setVisible(!EJB30SupportVerifier.verify(rt));
		}
	}
}