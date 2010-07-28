package org.jboss.tools.internal.deltacloud.ui.wizards;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.ui.Activator;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class NewCloudConnectionPage extends WizardPage {

	private static final String DESCRIPTION = "NewCloudConnection.desc"; //$NON-NLS-1$
	private static final String TITLE = "NewCloudConnection.title"; //$NON-NLS-1$
	private static final String URL_LABEL = "Url.label"; //$NON-NLS-1$
	private static final String NAME_LABEL = "Name.label"; //$NON-NLS-1$
	private static final String USERNAME_LABEL = "UserName.label"; //$NON-NLS-1$
	private static final String TYPE_LABEL = "Type.label"; //$NON-NLS-1$
	private static final String PASSWORD_LABEL = "Password.label"; //$NON-NLS-1$
	private static final String UNKNOWN_TYPE_LABEL = "UnknownType.label"; //$NON-NLS-1$
	private static final String EC2_USER_INFO = "EC2UserNameLink.text"; //$NON-NLS-1$
	private static final String EC2_PASSWORD_INFO = "EC2PasswordLink.text"; //$NON-NLS-1$
	private static final String NAME_ALREADY_IN_USE = "ErrorNameInUse.text"; //$NON-NLS-1$
	private static final String INVALID_URL = "ErrorInvalidURL.text"; //$NON-NLS-1$
	private static final String NONCLOUD_URL = "ErrorNonCloudURL.text"; //$NON-NLS-1$

	private Label errorLabel;
	
	private Text nameText;
	private Text urlText;
	private Label typeText;
	private Text usernameText;
	private Text passwordText;
	
	private String name;
	private String url;
	private String username;
	private String password;

	private Listener linkListener = new Listener() {

		public void handleEvent(Event event) {
			try {
				URL url = new URL(event.text);
				PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(url);
			} catch (Exception e) {
				Activator.log(e);
			}
		}
		
	};
	
	public NewCloudConnectionPage(String pageName) {
		super(pageName);
		setDescription(WizardMessages.getString(DESCRIPTION));
		setTitle(WizardMessages.getString(TITLE));
		setPageComplete(false);
	}

	private ModifyListener textListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			validate();
		}
	};
	
	public String getName() {
		return name;
	}
	
	public String getURL() {
		return url;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	private void validate() {
		boolean complete = true;
		boolean errorFree = true;
		name = nameText.getText();
		if (name.length() > 0) {
			if (DeltaCloudManager.getDefault().findCloud(name) != null) {
				errorFree = false;
				setErrorMessage(WizardMessages.getString(NAME_ALREADY_IN_USE));
			}
		} else {
			complete = false;
		}
		String oldurl = url;
		url = urlText.getText();
		if (url.length() > 0) {
			if (!url.equals(oldurl)) {
				try {
					URL u = new URL(url + ".xml");
					Object o = u.getContent();
					if (o instanceof InputStream) {
						String xml = "";
						InputStream is = (InputStream)o;
						try
						{
							if (is != null)
							{
								StringBuilder sb = new StringBuilder();
								String line;

								BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
								while ((line = reader.readLine()) != null) 
								{
									sb.append(line).append("\n");	
								}
								xml = sb.toString();
							}
						}
						finally
						{
							is.close();
						}

						DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
						DocumentBuilder db = dbf.newDocumentBuilder();
						Document document = db.parse(new InputSource(new StringReader(xml)));

						NodeList elements = document.getElementsByTagName("api");  //$NON-NLS-1$
						if (elements.getLength() > 0) {
							Node n = elements.item(0);
							Node driver = n.getAttributes().getNamedItem("driver"); //$NON-NLS-1$
							if (driver != null) {
								String driverValue = driver.getNodeValue();
								typeText.setText(driverValue.toUpperCase());
							}
						}
					}
				} catch (MalformedURLException e) {
					errorFree = false;
					setErrorMessage(WizardMessages.getString(INVALID_URL));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					errorFree = false;
					setErrorMessage(WizardMessages.getString(NONCLOUD_URL));
				} catch (ParserConfigurationException e) {
					errorFree = false;
					setErrorMessage(WizardMessages.getString(NONCLOUD_URL));
				} catch (SAXException e) {
					errorFree = false;
					setErrorMessage(WizardMessages.getString(NONCLOUD_URL));
				}
			}
		} else {
			complete = false;
		}
		username = usernameText.getText();
		if (username.length() <= 0) {
			complete = false;
		}
		password = passwordText.getText();
		if (password.length() <= 0) {
			complete = false;
		}
		if (errorFree)
			setErrorMessage(null);
		setPageComplete(complete & errorFree);
	}
	
	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		final Composite container = new Composite(parent, SWT.NULL);
		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		container.setLayout(layout);
	
		errorLabel = new Label(container, SWT.NULL);
		// errorLabel.setForeground(JFaceResources.getColorRegistry().get(JFacePreferences.ERROR_COLOR));
		
		Label nameLabel = new Label(container, SWT.NULL);
		nameLabel.setText(WizardMessages.getString(NAME_LABEL));
		nameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		nameText.addModifyListener(textListener);
		
		Label urlLabel = new Label(container, SWT.NULL);
		urlLabel.setText(WizardMessages.getString(URL_LABEL));
		
		urlText = new Text(container, SWT.BORDER | SWT.SINGLE);
		urlText.addModifyListener(textListener);

		Label typeLabel = new Label(container, SWT.NULL);
		typeLabel.setText(WizardMessages.getString(TYPE_LABEL));

		typeText = new Label(container, SWT.NULL);
		typeText.setText(WizardMessages.getString(UNKNOWN_TYPE_LABEL));
		
		Label usernameLabel = new Label(container, SWT.NULL);
		usernameLabel.setText(WizardMessages.getString(USERNAME_LABEL));

		usernameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		usernameText.addModifyListener(textListener);

		Label passwordLabel = new Label(container, SWT.NULL);
		passwordLabel.setText(WizardMessages.getString(PASSWORD_LABEL));

		passwordText = new Text(container, SWT.BORDER | SWT.PASSWORD | SWT.SINGLE);
		passwordText.addModifyListener(textListener);
		
		Link ec2userLink = new Link(container, SWT.NULL);
		ec2userLink.setText(WizardMessages.getString(EC2_USER_INFO));
		ec2userLink.addListener(SWT.Selection, linkListener);

		Link ec2pwLink = new Link(container, SWT.NULL);
		ec2pwLink.setText(WizardMessages.getString(EC2_PASSWORD_INFO));
		ec2pwLink.addListener(SWT.Selection, linkListener);
		
		FormData f = new FormData();
		f.left = new FormAttachment(0, 0);
		f.right = new FormAttachment(100, 0);
		errorLabel.setLayoutData(f);
		
		f = new FormData();
		f.top = new FormAttachment(errorLabel, 8);
		nameLabel.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(errorLabel, 8);
		f.left = new FormAttachment(usernameLabel, 5);
		f.right = new FormAttachment(100, 0);
		nameText.setLayoutData(f);
		
		f = new FormData();
		f.top = new FormAttachment(nameText, 8);
		urlLabel.setLayoutData(f);
		
		f = new FormData();
		f.left = new FormAttachment(nameText, 0, SWT.LEFT);
		f.top = new FormAttachment(nameText, 5);
		f.right = new FormAttachment(100, 0);
		urlText.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(urlText, 8);
		typeLabel.setLayoutData(f);
		
		f = new FormData();
		f.left = new FormAttachment(urlText, 0, SWT.LEFT);
		f.top = new FormAttachment(urlText, 5);
		f.right = new FormAttachment(100, 0);
		typeText.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(typeText, 8);
		usernameLabel.setLayoutData(f);
		
		f = new FormData();
		f.left = new FormAttachment(typeText, 0, SWT.LEFT);
		f.top = new FormAttachment(typeText, 5);
		f.right = new FormAttachment(100, 0);
		usernameText.setLayoutData(f);
		
		f = new FormData();
		f.left = new FormAttachment(usernameText, 0, SWT.LEFT);
		f.top = new FormAttachment(usernameText, 5);
		ec2userLink.setLayoutData(f);

		f = new FormData();
		f.top = new FormAttachment(ec2userLink, 8);
		passwordLabel.setLayoutData(f);

		f = new FormData();
		f.left = new FormAttachment(usernameText, 0, SWT.LEFT);
		f.top = new FormAttachment(ec2userLink, 5);
		f.right = new FormAttachment(100, 0);
		passwordText.setLayoutData(f);
		
		f = new FormData();
		f.left = new FormAttachment(passwordText, 0, SWT.LEFT);
		f.top = new FormAttachment(passwordText, 5);
		ec2pwLink.setLayoutData(f);
		
		setControl(container);
}

}
