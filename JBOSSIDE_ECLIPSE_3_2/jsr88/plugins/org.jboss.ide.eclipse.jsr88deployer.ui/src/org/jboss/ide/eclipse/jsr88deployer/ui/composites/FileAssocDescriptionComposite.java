/**
 * 
 */
package org.jboss.ide.eclipse.jsr88deployer.ui.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.jsr88deployer.core.utils.StringProperties;
import org.jboss.ide.eclipse.jsr88deployer.ui.preferences.Deploy88PreferencePage.AssociationPreferenceBlock;

/**
 * @author Rob Stryker
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileAssocDescriptionComposite extends Composite {

	private Label nameLabel, jarLabel, targetLabel, configLabel;
	private Text nameText, jarText, targetText, configText;
	
	private boolean compact;

	public FileAssocDescriptionComposite(Composite parent, int style) {
		this(parent, style, false);
	}

	public FileAssocDescriptionComposite(Composite parent, int style, boolean compact) {
		super(parent, style);
		
		
		createWidgets(this);
		setWidgetText();
		
		setLayout(new FormLayout());
		if( compact ) {
			setWidgetDataCompact();
		} else {
			setWidgetDataLoose();
		}

	}

	/**
	 * @param main
	 */
	private void createWidgets(Composite main) {
		nameLabel = new Label(main, SWT.NONE);
		configLabel = new Label(main, SWT.NONE);
		jarLabel = new Label(main, SWT.NONE);
		targetLabel = new Label(main, SWT.NONE);
		
		nameText = new Text(main, SWT.BORDER);
		configText = new Text(main, SWT.BORDER);
		jarText = new Text(main, SWT.BORDER);
		targetText = new Text(main, SWT.BORDER);
	}

	/**
	 * 
	 */
	private void setWidgetText() {
		nameLabel.setText("Name");
		jarLabel.setText("Vendor Jar");
		targetLabel.setText("Target");
		configLabel.setText("Configuration File");
		
		nameText.setEnabled(false);
		jarText.setEnabled(false);
		targetText.setEnabled(false);
		configText.setEnabled(false);
		
	}

	private void setWidgetDataCompact() {

		FormData nameLabelData = new FormData();
		nameLabelData.left = new FormAttachment(0,5);
		nameLabelData.right = new FormAttachment(0, 105);
		nameLabelData.top = new FormAttachment(0,7);
		nameLabel.setLayoutData(nameLabelData);

		
		
		FormData nameTextData = new FormData();
		nameTextData.left = new FormAttachment(nameLabel,5);
		nameTextData.right = new FormAttachment(100, -5);
		nameTextData.top = new FormAttachment(0,5);
		nameText.setLayoutData(nameTextData);

		
		
		FormData jarLabelData = new FormData();
		jarLabelData.left = new FormAttachment(0,5);
		jarLabelData.right = new FormAttachment(0, 105);
		jarLabelData.top = new FormAttachment(nameText,5);
		jarLabel.setLayoutData(jarLabelData);

		
		
		FormData jarTextData = new FormData();
		jarTextData.left = new FormAttachment(jarLabel,5);
		jarTextData.right = new FormAttachment(100, -5);
		jarTextData.top = new FormAttachment(nameText,5);
		jarText.setLayoutData(jarTextData);

		
		
		FormData targetLabelData = new FormData();
		targetLabelData.left = new FormAttachment(0,5);
		targetLabelData.right = new FormAttachment(0, 105);
		targetLabelData.top = new FormAttachment(jarText,5);
		targetLabel.setLayoutData(targetLabelData);

		
		
		FormData targetTextData = new FormData();
		targetTextData.left = new FormAttachment(targetLabel,5);
		targetTextData.right = new FormAttachment(100, -5);
		targetTextData.top = new FormAttachment(jarText,5);
		targetText.setLayoutData(targetTextData);

		
		
		FormData configLabelData = new FormData();
		configLabelData.left = new FormAttachment(0,5);
		configLabelData.right = new FormAttachment(0, 105);
		configLabelData.top = new FormAttachment(targetText,5);
		configLabel.setLayoutData(configLabelData);

		
		
		FormData configTextData = new FormData();
		configTextData.left = new FormAttachment(configLabel,5);
		configTextData.right = new FormAttachment(100, -5);
		configTextData.top = new FormAttachment(targetText,5);
		configText.setLayoutData(configTextData);
	}

	
	private void setWidgetDataLoose() {

		FormData nameLabelData = new FormData();
		nameLabelData.left = new FormAttachment(0,5);
		nameLabelData.right = new FormAttachment(100, -5);
		nameLabelData.top = new FormAttachment(0,15);
		nameLabel.setLayoutData(nameLabelData);

		
		
		FormData nameTextData = new FormData();
		nameTextData.left = new FormAttachment(0,5);
		nameTextData.right = new FormAttachment(100, -5);
		nameTextData.top = new FormAttachment(nameLabel,5);
		nameText.setLayoutData(nameTextData);

		
		
		FormData jarLabelData = new FormData();
		jarLabelData.left = new FormAttachment(0,5);
		jarLabelData.right = new FormAttachment(100, -5);
		jarLabelData.top = new FormAttachment(nameText,5);
		jarLabel.setLayoutData(jarLabelData);

		
		
		FormData jarTextData = new FormData();
		jarTextData.left = new FormAttachment(0,5);
		jarTextData.right = new FormAttachment(100, -5);
		jarTextData.top = new FormAttachment(jarLabel,5);
		jarText.setLayoutData(jarTextData);

		
		
		FormData targetLabelData = new FormData();
		targetLabelData.left = new FormAttachment(0,5);
		targetLabelData.right = new FormAttachment(100, -5);
		targetLabelData.top = new FormAttachment(jarText,5);
		targetLabel.setLayoutData(targetLabelData);

		
		
		FormData targetTextData = new FormData();
		targetTextData.left = new FormAttachment(0,5);
		targetTextData.right = new FormAttachment(100, -5);
		targetTextData.top = new FormAttachment(targetLabel,5);
		targetText.setLayoutData(targetTextData);

		
		
		FormData configLabelData = new FormData();
		configLabelData.left = new FormAttachment(0,5);
		configLabelData.right = new FormAttachment(100, -5);
		configLabelData.top = new FormAttachment(targetText,5);
		configLabel.setLayoutData(configLabelData);

		
		
		FormData configTextData = new FormData();
		configTextData.left = new FormAttachment(0,5);
		configTextData.right = new FormAttachment(100, -5);
		configTextData.top = new FormAttachment(configLabel,5);
		configText.setLayoutData(configTextData);
	}
	
	public void setVals(StringProperties props) {
		
		nameText.setText(props.getPiece(AssociationPreferenceBlock.NAME));
		jarText.setText(props.getPiece(AssociationPreferenceBlock.JAR));
		targetText.setText(props.getPiece(AssociationPreferenceBlock.TARGET));
		configText.setText(props.getPiece(AssociationPreferenceBlock.CONFIG));		
	}
	
	public String getTextFromNameWidget() {
		return nameText.getText();
	}



}
