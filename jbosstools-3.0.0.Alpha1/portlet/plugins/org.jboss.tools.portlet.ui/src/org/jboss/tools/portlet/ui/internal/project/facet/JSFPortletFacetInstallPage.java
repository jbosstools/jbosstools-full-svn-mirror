package org.jboss.tools.portlet.ui.internal.project.facet;



import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.ui.AbstractFacetWizardPage;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.ui.PortletUIActivator;

/**
 * @author snjeza
 */
public class JSFPortletFacetInstallPage extends AbstractFacetWizardPage {

	private IDialogSettings dialogSettings;
	private IDataModel model;
	private IDialogSettings jsfSection;

	public JSFPortletFacetInstallPage() {
		super("JSFPortletProjectConfigurationWizardPage");
		setTitle("JBoss JSF Portlet Capabilities");
		setDescription("Add JBoss JSF Portlet capabilities to this Web Project");
		//ImageDescriptor imageDesc = getDefaultPageImageDescriptor( );
		//if ( imageDesc != null )
		//	setImageDescriptor( imageDesc );
		dialogSettings = PortletUIActivator.getDefault().getDialogSettings();
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1,false));
		
		final Button btn = new Button(composite,SWT.CHECK);
		btn.setText("Deploy jars to WEB-INF/lib");
		jsfSection = dialogSettings.getSection(IPortletConstants.JSF_SECTION);
		boolean deployJars;
		if (jsfSection == null) {
			jsfSection = dialogSettings.addNewSection(IPortletConstants.JSF_SECTION);
			deployJars = true;
		} else {
			deployJars = jsfSection.getBoolean(IPortletConstants.DEPLOY_JARS);
		}
		btn.setSelection(deployJars);
		jsfSection.put(IPortletConstants.DEPLOY_JARS, btn.getSelection());
		btn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				model.setBooleanProperty(IPortletConstants.DEPLOY_JARS, btn.getSelection());
				jsfSection.put(IPortletConstants.DEPLOY_JARS, btn.getSelection());
			}
			
		});
		setControl( composite );
	}

	public void setConfig(Object config) {
		this.model = (IDataModel) config;
	}

	

}
