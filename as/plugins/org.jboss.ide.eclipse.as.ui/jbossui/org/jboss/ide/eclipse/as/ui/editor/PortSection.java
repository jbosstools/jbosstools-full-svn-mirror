/******************************************************************************* 
 * Copyright (c) 2010 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.ide.eclipse.as.ui.editor;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.ui.editor.ServerEditorSection;
import org.eclipse.wst.server.ui.internal.command.ServerCommand;
import org.jboss.ide.eclipse.as.core.extensions.descriptors.XPathCategory;
import org.jboss.ide.eclipse.as.core.extensions.descriptors.XPathModel;
import org.jboss.ide.eclipse.as.core.extensions.descriptors.XPathQuery;
import org.jboss.ide.eclipse.as.core.server.IJBoss6Server;
import org.jboss.ide.eclipse.as.core.server.IJBossServerConstants;
import org.jboss.ide.eclipse.as.core.server.internal.ServerAttributeHelper;
import org.jboss.ide.eclipse.as.ui.JBossServerUIPlugin;
import org.jboss.ide.eclipse.as.ui.Messages;
import org.jboss.ide.eclipse.as.ui.dialogs.ChangePortDialog;
import org.jboss.ide.eclipse.as.ui.dialogs.ChangePortDialog.ChangePortDialogInfo;

/**
 *
 * @author rob Stryker (rob.stryker@redhat.com)
 *
 */
public class PortSection extends ServerEditorSection {

	private ArrayList<IPortEditorExtension> sectionList = new ArrayList<IPortEditorExtension>();
	protected ServerAttributeHelper helper;
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
		helper = new ServerAttributeHelper(server.getOriginal(), server);
		String serverTypeId = server.getServerType().getId();
		if (sectionList.size() <= 0) {
			IExtensionRegistry registry = Platform.getExtensionRegistry();
			IConfigurationElement[] cf = registry.getConfigurationElementsFor(
					JBossServerUIPlugin.PLUGIN_ID, "ServerEditorPortSection"); //$NON-NLS-1$
			for (int i = 0; i < cf.length; i++) {
				try {
					String approvedTypes = cf[i].getAttribute("serverIds"); //$NON-NLS-1$
					if( serverTypeMatches(serverTypeId, approvedTypes)) {
						Object o = cf[i].createExecutableExtension("class"); //$NON-NLS-1$
						if (o != null && o instanceof IPortEditorExtension)
							sectionList.add((IPortEditorExtension) o);
					}
				} catch (CoreException ce) { /* ignore */
				}
			}
		}
	}

	protected boolean serverTypeMatches(String serverType, String approvedTypes) {
		if( approvedTypes == null || approvedTypes.equals("")) //$NON-NLS-1$
			return true;
		String[] split = approvedTypes.split(","); //$NON-NLS-1$
		for( int i = 0; i < split.length; i++ )
			if( split[i].equals(serverType))
				return true;
		return false;
	}
	
	public void createSection(Composite parent) {
		super.createSection(parent);
		createUI(parent);
	}

	public static interface IPortEditorExtension {
		public void setServerAttributeHelper(ServerAttributeHelper helper);
		public void setSection(PortSection section);
		public Control createControl(Composite parent);
		public String getValue();
	}

	public static class JNDIPortEditorExtension extends PortEditorExtension {
		public JNDIPortEditorExtension() {
			super(Messages.EditorJNDIPort, 
					IJBossServerConstants.JNDI_PORT_DETECT_XPATH,
					IJBossServerConstants.JNDI_PORT_DETECT,
					IJBossServerConstants.JNDI_PORT,
					IJBossServerConstants.JNDI_PORT_DEFAULT_XPATH,
					Messages.EditorChangeJNDICommandName);
		}
	}

	public static class WebPortEditorExtension extends PortEditorExtension {
		public WebPortEditorExtension() {
			super(Messages.EditorWebPort, 
					IJBossServerConstants.WEB_PORT_DETECT_XPATH,
					IJBossServerConstants.WEB_PORT_DETECT,
					IJBossServerConstants.WEB_PORT,
					IJBossServerConstants.WEB_PORT_DEFAULT_XPATH, 
					Messages.EditorChangeWebCommandName);
		}
	}
	
	public static class JBoss7WebPortEditorExtension extends PortEditorExtension {
		public JBoss7WebPortEditorExtension() {
			super(Messages.EditorWebPort, 
					null,
					IJBossServerConstants.WEB_PORT_DETECT,
					IJBossServerConstants.WEB_PORT,
					null, 
					Messages.EditorChangeWebCommandName);
		}
	}

	public static class JBoss6JMXRMIPortEditorExtension extends PortEditorExtension {
		public JBoss6JMXRMIPortEditorExtension() {
			super(Messages.EditorJMXRMIPort, 
					IJBoss6Server.JMX_RMI_PORT_DETECT_XPATH,
					IJBoss6Server.JMX_RMI_PORT_DETECT,
					IJBoss6Server.JMX_RMI_PORT,
					IJBoss6Server.JMX_RMI_PORT_DEFAULT_XPATH,
					Messages.EditorChangeJMXRMICommandName);
		}
	}


	public static abstract class PortEditorExtension implements IPortEditorExtension {
		protected Button detect;
		protected Text text;
		protected Label label;
		protected Link link;
		protected String labelText, currentXPathKey, detectXPathKey, overrideValueKey, defaultXPath;
		protected String currentXPath, changeValueCommandName;
		protected ServerAttributeHelper helper;
		protected Listener listener;
		protected PortSection section;
		public PortEditorExtension(String labelText, String currentXPathKey, 
				String detectXPathKey, String overrideValueKey, String defaultXPath,
				String changeValueCommandName) {
			this.labelText = labelText;
			this.currentXPathKey = currentXPathKey;
			this.detectXPathKey = detectXPathKey;
			this.overrideValueKey = overrideValueKey;
			this.defaultXPath = defaultXPath;
			this.changeValueCommandName = changeValueCommandName;
		}
		public void setServerAttributeHelper(ServerAttributeHelper helper) {
			this.helper = helper;
		}
		public void setSection(PortSection section) {
			this.section = section;
		}
		public Control createControl(Composite parent) {
			Control c = createUI(parent);
			initialize();
			addListeners();
			return c;
		}

		protected Control createUI(Composite parent) {
			Composite child = new Composite(parent, SWT.NONE);
			child.setLayout(new FormLayout());
			label = new Label(child, SWT.NONE);
			text = new Text(child, SWT.SINGLE | SWT.BORDER);
			detect = new Button(child, SWT.CHECK);
			link = new Link(child, SWT.NONE);

			FormData data;
			data = new FormData();
			data.top = new FormAttachment(0,8);
			data.right = new FormAttachment(100,-5);
			link.setLayoutData(data);

			data = new FormData();
			data.right = new FormAttachment(link, -5);
			data.top = new FormAttachment(0,5);
			detect.setLayoutData(data);

			data = new FormData();
			data.right = new FormAttachment(detect, -5);
			data.left = new FormAttachment(0, 100);
			data.top = new FormAttachment(0,5);
			text.setLayoutData(data);

			data = new FormData();
			data.left = new FormAttachment(0,5);
			data.top = new FormAttachment(0,8);
			label.setLayoutData(data);

			label.setText(labelText);
			detect.setText(Messages.EditorAutomaticallyDetectPort);
			link.setText("<a href=\"\">" + Messages.Configure + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
			return child;
		}
		protected void initialize() {
			boolean shouldDetect = helper.getAttribute(detectXPathKey, true);
			detect.setSelection(shouldDetect);
			detect.setEnabled(defaultXPath != null);
			link.setEnabled(shouldDetect);
			text.setEnabled(!shouldDetect);
			text.setEditable(!shouldDetect);
			currentXPath = helper.getAttribute(currentXPathKey, defaultXPath);
			if( shouldDetect )
				text.setText(findPort(helper.getServer(), new Path(currentXPath)));
			else
				text.setText(helper.getAttribute(overrideValueKey, "")); //$NON-NLS-1$
		}
		protected void addListeners() {
			listener = new Listener() {
				public void handleEvent(Event event) {
					section.execute(getCommand());
				}
			};
			text.addListener(SWT.Modify, listener);
			detect.addListener(SWT.Selection, listener);
			link.addListener(SWT.Selection, createLinkListener());
		}

		protected Listener createLinkListener() {
			return new Listener() {
				public void handleEvent(Event event) {
					ChangePortDialog dialog = getDialog();
					int result = dialog.open();
					if( result == Dialog.OK) {
						currentXPath = dialog.getSelection();
						section.execute(getCommand());
						text.setFocus();
					}
				}
			};
		}
		public ChangePortDialog getDialog() {
			return new ChangePortDialog(section.getShell(), getDialogInfo());
		}
		public ServerCommand getCommand() {
			return new SetPortCommand(helper.getWorkingCopy(), helper, changeValueCommandName,
					overrideValueKey, detectXPathKey,currentXPathKey, defaultXPath, this);
		}
		protected ChangePortDialogInfo getDialogInfo() {
			ChangePortDialogInfo info = new ChangePortDialogInfo();
			info.port = labelText;
			info.defaultValue = defaultXPath;
			info.server = helper.getWorkingCopy().getOriginal();
			info.currentXPath = currentXPath;
			return info;
		}
		public String getValue() {
			return text.getText();
		}
	}

	protected void createUI(Composite parent) {

		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		Section section = toolkit.createSection(parent, ExpandableComposite.TWISTIE|ExpandableComposite.EXPANDED|ExpandableComposite.TITLE_BAR);
		section.setText(Messages.EditorServerPorts);
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL));

		Composite composite = toolkit.createComposite(section);
		composite.setLayout(new FormLayout());
		Label description = new Label(composite, SWT.NONE);
		description.setText(Messages.EditorServerPortsDescription);
		FormData data = new FormData();
		data.top = new FormAttachment(0,5);
		data.left = new FormAttachment(0,5);
		description.setLayoutData(data);

		addUIAdditions(composite, description);
		toolkit.paintBordersFor(composite);
		section.setClient(composite);
		getSaveStatus();
	}

	private void addUIAdditions(Composite parent, Control top) {
		IPortEditorExtension[] extensions = (IPortEditorExtension[]) sectionList.toArray(new IPortEditorExtension[sectionList.size()]);

		FormData data;
		Control c;
		Composite wrapper = new Composite(parent, SWT.NONE);
		wrapper.setLayout(new FormLayout());
		data = new FormData();
		data.top = new FormAttachment(top,0);
		data.left = new FormAttachment(0,0);
		data.right = new FormAttachment(100,-5);
		wrapper.setLayoutData(data);
		top = null;
		for( int i = 0; i < extensions.length; i++ ) {
			extensions[i].setServerAttributeHelper(helper);
			extensions[i].setSection(this);
			c = extensions[i].createControl(wrapper);
			data = new FormData();
			if( top == null )
				data.top = new FormAttachment(0, 5);
			else
				data.top = new FormAttachment(top, 5);
			data.left = new FormAttachment(0,5);
			data.right = new FormAttachment(100,-5);
			c.setLayoutData(data);
			top = c;
		}
	}

	protected String[] getXPathStrings() {
		ArrayList<String> list = new ArrayList<String>();
		XPathCategory[] categories = XPathModel.getDefault().getCategories(server.getOriginal());
		for( int i = 0; i < categories.length; i++ ) {
			XPathQuery[] queries = categories[i].getQueries();
			for( int j = 0; j < queries.length; j++ ) {
				list.add(categories[i].getName() + IPath.SEPARATOR + queries[j].getName());
			}
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	public static class SetPortCommand extends ServerCommand {
		ServerAttributeHelper helper;
		String textAttribute, overrideAttribute, overridePathAttribute;
		String preText, prePath, defaultPath;
		boolean preOverride;
		Text text;
		Button button;
		Listener listener;
		String xpath;
		Link link;
		PortSection pSection;
		public SetPortCommand(IServerWorkingCopy server, ServerAttributeHelper helper, String name,
				String textAttribute, String overrideAttribute, String overridePathAttribute,
				String pathDefault, PortEditorExtension ext) { //Text text, Button button, String xpath, Listener listener) {
			super(server, name);
			this.helper = helper;
			this.textAttribute = textAttribute;
			this.overrideAttribute = overrideAttribute;
			this.overridePathAttribute = overridePathAttribute;
			this.defaultPath = pathDefault;
			this.text = ext.text;
			this.button = ext.detect;
			this.listener = ext.listener;
			this.xpath = ext.currentXPath;
			this.link = ext.link;
			pSection = ext.section;
		}

		public void execute() {
			preText = helper.getAttribute(textAttribute, (String)null);
			if( preText == null )
				preText = findPort(helper.getServer(), new Path(defaultPath));
			prePath = helper.getAttribute(overridePathAttribute, (String)defaultPath);
			preOverride = helper.getAttribute(overrideAttribute, true);
			helper.setAttribute(textAttribute, text.getText());
			helper.setAttribute(overrideAttribute, button.getSelection());
			link.setEnabled(button.getSelection());
			helper.setAttribute(overridePathAttribute, xpath);

			text.setEnabled(!button.getSelection());
			text.setEditable(!button.getSelection());
			if( button.getSelection() ) {
				text.removeListener(SWT.Modify, listener);
				text.setText(findPort(helper.getServer(), new Path(xpath)));
				text.addListener(SWT.Modify, listener);
			}
			pSection.getSaveStatus();
		}

		public void undo() {
			// set new values
			helper.setAttribute(textAttribute, preText);
			helper.setAttribute(overrideAttribute, preOverride);
			link.setEnabled(preOverride);
			helper.setAttribute(overridePathAttribute, prePath);
			
			// update ui
			text.removeListener(SWT.Modify, listener);
			button.removeListener(SWT.Selection, listener);

			button.setSelection(preOverride);
			text.setText(preText == null ? "" : preText); //$NON-NLS-1$
			text.setEnabled(!preOverride);
			text.setEditable(!preOverride);
			button.addListener(SWT.Selection, listener);
			text.addListener(SWT.Modify, listener);

			pSection.getSaveStatus();
		}
	}

	protected static String findPort(IServer server, IPath path) {
		XPathQuery query = XPathModel.getDefault().getQuery(server, path);
		String result = ""; //$NON-NLS-1$
		if(query!=null) {
			try {
				query.refresh();
				result = query.getFirstResult();
				result = result == null ? "" : result; //$NON-NLS-1$
				return new Integer(Integer.parseInt(result)).toString();
			} catch(NumberFormatException nfe) {
			} catch( IllegalStateException ise ) {
			}
		}
		return result;
	}
	
	public IStatus[] getSaveStatus() {
		Iterator<IPortEditorExtension> i = sectionList.iterator();
		IPortEditorExtension ext1;
		int temp;
		boolean errorFound = false;
		while(i.hasNext()) {
			ext1 = i.next();
			try {
				temp = Integer.parseInt(ext1.getValue());
				if( temp < 0 )
					errorFound = true;
			} catch(NumberFormatException nfe) {
				errorFound = true;
			}
		}
		setErrorMessage(errorFound ? Messages.EditorPortInvalid : null);
		return errorFound ? 
				new IStatus[] { new Status(IStatus.WARNING, JBossServerUIPlugin.PLUGIN_ID, Messages.EditorPortInvalid)} 
				: new IStatus[]{};
	}

}
