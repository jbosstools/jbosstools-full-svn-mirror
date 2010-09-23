package org.eclipse.bpel.ui.wizards;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.bpel.ui.BPELUIPlugin;
import org.eclipse.bpel.ui.IBPELUIConstants;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class WSDLCustomPage extends WizardPage {

	/** Service name field */
	private Text serviceNameField;

	/** Port name field */
	private Text portNameField;

	/** Address name field */
	private Text addressField;

	/** binding protocol */
	Combo bindingField;

	private Map<String, String> mArgs = new HashMap<String, String>();

	static final String EMPTY = "";
	static final String SOAP_NAMESPACE="http://schemas.xmlsoap.org/wsdl/soap/";
	static final String HTTP_NAMESPACE="http://schemas.xmlsoap.org/wsdl/http/";

	private static final int SIZING_TEXT_FIELD_WIDTH = 250;
	
	private Listener validateListner = new Listener() {
		public void handleEvent(Event event) {
			setPageComplete(validatePage());
		}
	};

	/**
	 * New File Wizard,wsdl page that custom the generated wsdl file
	 * 
	 * @param pageName
	 */
	protected WSDLCustomPage(String pageName) {
		super(pageName);
		setTitle(Messages.NewFileWizard_WSDLCustomPage_Title);
		setDescription(Messages.NewFileWizard_WSDLCustomPage_Description);
		setImageDescriptor(BPELUIPlugin.INSTANCE
				.getImageDescriptor(IBPELUIConstants.ICON_WIZARD_BANNER));
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
		initializeDialogUnits(parent);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		createWSDLGroup(composite);
		setErrorMessage(null);
		setMessage(null);
		setControl(composite);
	}

	private final void createWSDLGroup(Composite parent) {
		Group wsdlGroup = new Group(parent, SWT.NONE);
		wsdlGroup.setText(Messages.NewFileWizard_WSDLCustomPage_WSDLGroup);
		wsdlGroup.setLayout(new GridLayout());
		wsdlGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Composite fields = new Composite(wsdlGroup, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		fields.setLayout(layout);
		fields.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// new service label
		Label serviceLabel = new Label(fields, SWT.NONE);
		serviceLabel
				.setText(Messages.NewFileWizard_WSDLCustomPage_ServiceLable);
		serviceLabel.setFont(parent.getFont());

		// new service name entry field
		serviceNameField = new Text(fields, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		serviceNameField.setLayoutData(data);
		serviceNameField.setFont(parent.getFont());
		serviceNameField.addListener(SWT.Modify, validateListner);

		// new port label
		Label portLabel = new Label(fields, SWT.NONE);
		portLabel.setText(Messages.NewFileWizard_WSDLCustomPage_PortLabel);
		portLabel.setFont(parent.getFont());

		// new port name entry field
		portNameField = new Text(fields, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		portNameField.setLayoutData(data);
		portNameField.setFont(parent.getFont());
		portNameField.addListener(SWT.Modify, validateListner);

		// new address label
		Label addressLabel = new Label(fields, SWT.NONE);
		addressLabel
				.setText(Messages.NewFileWizard_WSDLCustomPage_AddressLabel);
		addressLabel.setFont(parent.getFont());

		// new address name entry field
		addressField = new Text(fields, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		addressField.setLayoutData(data);
		addressField.setFont(parent.getFont());
		addressField.addListener(SWT.Modify, validateListner);

		Label bindingLabel = new Label(fields, SWT.NONE);
		bindingLabel
				.setText(Messages.NewFileWizard_WSDLCustomPage_BindingLabel);
		bindingLabel.setFont(parent.getFont());
		// new binding protocol entry field
		bindingField = new Combo(fields, SWT.DROP_DOWN | SWT.SIMPLE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		bindingField.setLayoutData(data);
		bindingField.setFont(parent.getFont());

		// add the binding protocol values
		bindingField.setItems(new String[] { "SOAP", "HTTP" });
		bindingField.setText("SOAP");
		bindingField.addListener(SWT.Modify, validateListner);
	}

	protected boolean validatePage() {
		String serviceName = serviceNameField.getText().trim();
		if (isEmptyOrSpace(serviceName, "Service Name")) {
			return false;
		}
		String portName = portNameField.getText().trim();
		if (isEmptyOrSpace(portName, "Port Name")) {
			return false;
		}
		String addressName = addressField.getText().trim();
		if (isEmptyOrSpace(addressName, "Service Address")) {
			return false;
		}
		String protocol = bindingField.getText().trim();
		if (!("SOAP".equals(protocol) || "HTTP".equals(protocol))) {
			setErrorMessage(Messages.Error_NewFileWizard_WSDLCustomPage_Protocol);
			return false;
		}
		setErrorMessage(null);

		// Template arguments
		mArgs.put("serviceName", serviceName); //$NON-NLS-1$
		mArgs.put("portName", portName); //$NON-NLS-1$
		mArgs.put("address", addressName); //$NON-NLS-1$
		mArgs.put("protocol", protocol.toLowerCase()); //$NON-NLS-1$
		if("SOAP".equals(protocol)){
			mArgs.put("protocolNamespace", SOAP_NAMESPACE);
		} else {
			mArgs.put("protocolNamespace", HTTP_NAMESPACE);
		}
		return true;
	}

	private boolean isEmptyOrSpace(String name, String element) {
		if (name.equals(EMPTY)) {
			setErrorMessage(NLS.bind(
					Messages.Error_NewFileWizard_WSDLCustomPage_Name_Empty,
					element));
			return true;
		}

		if (name.indexOf(" ") > -1) {
			setErrorMessage(NLS.bind(
					Messages.Error_NewFileWizard_WSDLCustomPage_Name_Space,
					element));
			return true;
		}
		return false;
	}

	public Text getServiceNameField() {
		return serviceNameField;
	}

	public Text getPortNameField() {
		return portNameField;
	}

	public Text getAddressField() {
		return addressField;
	}

	public Map<String, String> getMap() {
		return mArgs;
	}

}
