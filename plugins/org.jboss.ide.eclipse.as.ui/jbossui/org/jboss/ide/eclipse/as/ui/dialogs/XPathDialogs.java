package org.jboss.ide.eclipse.as.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.as.core.model.SimpleTreeItem;
import org.jboss.ide.eclipse.as.core.model.DescriptorModel.ServerDescriptorModel.XPathTreeItem;
import org.jboss.ide.eclipse.as.core.server.JBossServer;
import org.jboss.ide.eclipse.as.core.server.ServerAttributeHelper.SimpleXPathPreferenceTreeItem;
import org.jboss.ide.eclipse.as.core.server.ServerAttributeHelper.XPathPreferenceTreeItem;
import org.jboss.ide.eclipse.as.core.util.ASDebug;

public class XPathDialogs {

	public static class XPathCategoryDialog extends Dialog {

		private String textValue;
		private Label errorLabel;
		private SimpleTreeItem tree;
		
		public XPathCategoryDialog(Shell parentShell, SimpleTreeItem tree) {
			super(parentShell);
			this.tree = tree;
		}
		public XPathCategoryDialog(Shell parentShell, SimpleTreeItem tree, String startText) {
			super(parentShell);
			this.tree = tree;
			this.textValue = startText;
		}
		
		
		protected void configureShell(Shell shell) {
			super.configureShell(shell);
			shell.setText("New Category");
		}
		
		protected Control createDialogArea(Composite parent) {
			Composite c = (Composite)super.createDialogArea(parent);
			c.setLayout(new FormLayout());
			
			errorLabel = new Label(c, SWT.NONE);
			errorLabel.setText("Category Name In Use.");
			FormData errorData = new FormData();
			errorData.left = new FormAttachment(0,5);
			errorData.top = new FormAttachment(0,5);
			errorLabel.setLayoutData(errorData);
			errorLabel.setVisible(false);
			
			Label l = new Label(c, SWT.NONE);
			l.setText("Category Name:  ");
			FormData labelData = new FormData();
			labelData.left = new FormAttachment(0,5);
			labelData.top = new FormAttachment(errorLabel,5);
			l.setLayoutData(labelData);

			
			final Text t = new Text(c, SWT.BORDER); 
			FormData tData = new FormData();
			tData.left = new FormAttachment(l,5);
			tData.top = new FormAttachment(errorLabel,5);
			tData.right = new FormAttachment(100, -5);
			t.setLayoutData(tData);

			if( textValue != null ) {
				t.setText(textValue);
			}
			
			t.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					verifyText(t.getText());
				} 
			});
			
			return c;
		}
		
		private void verifyText(String text) {
			boolean valid = true;
			SimpleTreeItem[] kids = tree.getChildren2();
			for( int i = 0; i < kids.length; i++ ) {
				if( text.equals(kids[i].getData())) 
					valid = false;
			}
			
			if( valid ) {
				errorLabel.setVisible(false);
				textValue = text;
				getButton(IDialogConstants.OK_ID).setEnabled(true);
			} else {
				errorLabel.setVisible(true);
				getButton(IDialogConstants.OK_ID).setEnabled(false);
			}
			
		}
		
		public String getText() {
			return textValue;
		}
	}

	
	public static class XPathDialog extends Dialog {

		private Label errorLabel;
		private Text nameText, xpathText, attributeText;
		private Label nameLabel, xpathLabel, attributeLabel;
		private Button previewButton;
		
		private SimpleTreeItem tree;
		private String category;
		private JBossServer server;
		
		private String name, xpath, attribute;
		
		private String originalName = null;
		
		private XPathPreferenceTreeItem original = null;
		
		int previewId = 48879;
		
		public XPathDialog(Shell parentShell, SimpleTreeItem tree, String categoryName, JBossServer server) {
			super(parentShell);
			this.tree = tree;
			this.category = categoryName;
			this.server = server;
		}

		public XPathDialog(Shell parentShell, SimpleTreeItem tree, String categoryName, JBossServer server, String originalName) {
			super(parentShell);
			this.tree = tree;
			this.category = categoryName;
			this.server = server;
			this.originalName = this.name = originalName;
		}

		protected void configureShell(Shell shell) {
			super.configureShell(shell);
			shell.setText("New XPath");
		}
		
		protected void createButtonsForButtonBar(Composite parent) {
			// create OK and Cancel buttons by default
			super.createButtonsForButtonBar(parent);
			previewButton = createButton(parent, previewId, "Preview", true);
			getButton(IDialogConstants.OK_ID).setEnabled(false);

			addListeners();
		}

		
		protected Control createDialogArea(Composite parent) {
			Composite c = (Composite)super.createDialogArea(parent);
			c.setLayout(new FormLayout());
			layoutWidgets(c);
			
			if( name != null ) nameText.setText(name);
			if( attribute != null ) attributeText.setText(attribute);
			if( xpath != null ) xpathText.setText(xpath);
			
			
			return c;
		} 
		
		protected void addListeners() {
			nameText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					verifyName();
					name = nameText.getText();
				} 
			});
			attributeText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					attribute = attributeText.getText();
				} 
			});
			xpathText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					xpath = xpathText.getText();
				} 
			});
			
			previewButton.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
				}
				public void widgetSelected(SelectionEvent e) {
					previewPressed();
				} 
			});
		}
		protected void verifyName() {
			
			if( nameText.getText().equals("")) {
				errorLabel.setText("Name must be set.");
				errorLabel.setVisible(true);
				getButton(IDialogConstants.OK_ID).setEnabled(false);
				return;
			}
			
			SimpleTreeItem[] categories = tree.getChildren2();
			SimpleTreeItem categoryItem = null;
			for( int i = 0; i < categories.length; i++ ) {
				if( categories[i].getData().equals(category)) 
					categoryItem = categories[i];
			}
			if( categoryItem != null ) {
				SimpleTreeItem[] xpathNames = categoryItem.getChildren2();
				boolean found = false;
				for( int i = 0; i < xpathNames.length; i++ ) {
					if(nameText.getText().equals( ((XPathPreferenceTreeItem)xpathNames[i]).getName())) {
						
						if( originalName == null || !nameText.getText().equals(originalName)) 
							found = true;
					}
				}
				if( found ) {
					// error, name in use
					errorLabel.setText("Name in use.");
					errorLabel.setVisible(true);
					getButton(IDialogConstants.OK_ID).setEnabled(false);
				} else {
					errorLabel.setVisible(false);
					getButton(IDialogConstants.OK_ID).setEnabled(true);
				}
			}
		}
		protected void previewPressed() {
			XPathTreeItem[] item = server.getDescriptorModel().getXPath(xpathText.getText());
		}
		protected void layoutWidgets(Composite c) {
			// create widgets
			errorLabel = new Label(c, SWT.NONE);
			
			nameLabel = new Label(c, SWT.NONE);
			xpathLabel = new Label(c, SWT.NONE);
			attributeLabel = new Label(c, SWT.NONE);
			nameText= new Text(c, SWT.BORDER);
			xpathText = new Text(c, SWT.BORDER);
			attributeText = new Text(c, SWT.BORDER);
			
			// set some text
			nameLabel.setText("Name: ");
			xpathLabel.setText("XPath Pattern: ");
			attributeLabel.setText("Attribute Name: ");

			c.layout();
			int pixel = Math.max(Math.max(nameLabel.getSize().x, xpathLabel.getSize().x), attributeLabel.getSize().x);
			pixel += 5;
			
			// Lay them out
			//errorLabel.setText("Category Name In Use.");
			FormData errorData = new FormData();
			errorData.left = new FormAttachment(0,5);
			errorData.top = new FormAttachment(0,5);
			errorData.right = new FormAttachment(0,300);
			errorLabel.setLayoutData(errorData);
			errorLabel.setVisible(false);

			
			/* Name */
			FormData nameLabelData = new FormData();
			nameLabelData.left = new FormAttachment(0,5);
			nameLabelData.top = new FormAttachment(errorLabel,5);
			nameLabel.setLayoutData(nameLabelData);

			FormData nameTextData = new FormData();
			nameTextData.left = new FormAttachment(0,pixel);
			nameTextData.right = new FormAttachment(100,-5);
			nameTextData.top = new FormAttachment(errorLabel,4);
			nameText.setLayoutData(nameTextData);


			/* XPath */
			FormData xpathLabelData = new FormData();
			xpathLabelData.left = new FormAttachment(0,5);
			xpathLabelData.top = new FormAttachment(nameText,5);
			xpathLabel.setLayoutData(xpathLabelData);

			FormData xpathTextData = new FormData();
			xpathTextData.left = new FormAttachment(0,pixel);
			xpathTextData.right = new FormAttachment(100,-5);
			xpathTextData.top = new FormAttachment(nameText,4);
			xpathText.setLayoutData(xpathTextData);


			/* Attribute */
			FormData attributeLabelData = new FormData();
			attributeLabelData.left = new FormAttachment(0,5);
			attributeLabelData.top = new FormAttachment(xpathText,5);
			attributeLabel.setLayoutData(attributeLabelData);

			FormData attributeTextData = new FormData();
			attributeTextData.left = new FormAttachment(0, pixel);
			attributeTextData.right = new FormAttachment(100,-5);
			attributeTextData.top = new FormAttachment(xpathText,4);
			attributeText.setLayoutData(attributeTextData);
		}

		public String getAttribute() {
			return attribute;
		}

		public String getName() {
			return name;
		}

		public String getXpath() {
			return xpath;
		}

		public void setAttribute(String attribute) {
			this.attribute = attribute;
			if( attributeText != null && !attributeText.isDisposed())
				attributeText.setText(this.attribute);
		}

		public void setName(String name) {
			this.name = name;
			if( nameText != null && !nameText.isDisposed())
				nameText.setText(this.name);
		}

		public void setXpath(String xpath) {
			this.xpath = xpath;
			if( xpathText != null && !xpathText.isDisposed())
				xpathText.setText(this.xpath);
		}
		
		
	}
}
