/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.editor.template;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.outline.cssdialog.CSSDialog;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.MessageUtil;
import org.jboss.tools.vpe.messages.VpeUIMessages;
/**
 * Class which presents dialog for any template
 * @author mareshkau
 *
 */
public class VpeEditAnyDialog extends TitleAreaDialog {

	private VpeAnyData data;
	private CheckControl ctlChildren;
	private Text txtTagForDisplay; 
	private Text txtValue;
	private Text txtStyle;
	private CheckControl ctlShowIcon;

	public VpeEditAnyDialog(Shell shell, VpeAnyData data) {
		super(shell);
		this.data = data;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText(VpeUIMessages.TEMPLATE);
		setTitle(VpeUIMessages.TAG_ATTRIBUTES);
		setMessage((data.getUri() != null ? ("URI:           " + data.getUri() + "\n") : "") + VpeUIMessages.TAG_NAME + data.getName()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		Composite topComposite = (Composite)super.createDialogArea(parent);
		((GridData)topComposite.getLayoutData()).widthHint = 300;

		final Composite composite = new Composite(topComposite, SWT.NONE);
		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.marginWidth = 50;
		gridLayout.marginHeight = 20;
		gridLayout.horizontalSpacing = 5;
		composite.setLayout(gridLayout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		composite.setBackground(parent.getBackground());
		composite.setForeground(parent.getForeground());
		composite.setFont(parent.getFont());
		
	    //added by estherbin https://jira.jboss.org/jira/browse/JBIDE-2521
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        Label tagFDisplayLabel = makeLabel(composite, VpeUIMessages.TAG_FOR_DISPLAY);
        
        tagFDisplayLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        txtTagForDisplay = new Text(composite,SWT.BORDER);
        gd.horizontalSpan=2;
        txtTagForDisplay.setLayoutData(gd);
        txtTagForDisplay.setText(data.getTagForDisplay() != null ? data.getTagForDisplay() : ""); //$NON-NLS-1$
//        txtTagForDisplay.select(tagNameItemIndex);
        
        
//		ctlCaseSensitive = new CheckControl(composite, "Case sensitive", data.isCaseSensitive());
		ctlChildren = new CheckControl(composite, VpeUIMessages.CHILDREN, data.isChildren());
//		ctlModify = new CheckControl(composite, "Modify", data.isModify());
	
		ctlShowIcon = new CheckControl(composite, VpeUIMessages.ICON, data.isShowIcon());
		
		//value control
		Label lblValue = makeLabel(composite, VpeUIMessages.VALUE);
		lblValue.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		txtValue = new Text(composite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		txtValue.setLayoutData(gd);
		txtValue.setText(data.getValue() != null ? data.getValue() : ""); //$NON-NLS-1$

		//style control
		Label lbStyle = makeLabel(composite, VpeUIMessages.STYLE);
		lbStyle.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		txtStyle =  new Text(composite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		gd.grabExcessHorizontalSpace = true;
		txtStyle.setLayoutData(gd);
		txtStyle.setText(data.getStyle() !=null ? data.getStyle() : ""); //$NON-NLS-1$
	
		Button button = new Button(composite, SWT.PUSH);
		button.setLayoutData(new GridData());
		button.setToolTipText(MessageUtil.getString("BACKGROUND_COLOR_TIP")); //$NON-NLS-1$
		ImageDescriptor colorDesc = JspEditorPlugin
			.getImageDescriptor(Constants.IMAGE_COLORLARGE_FILE_LOCATION);
		Image im = colorDesc.createImage();
		button.setImage(im);
		
		button.addDisposeListener(new DisposeListener() {
		    public void widgetDisposed(DisposeEvent e) {
			Button button = (Button) e.getSource();
			button.getImage().dispose();
		    }
		});
		button.addSelectionListener(new SelectionAdapter() {
		    @Override
			public void widgetSelected(SelectionEvent event) {
		    	CSSDialog cssDialog = new CSSDialog(composite.getShell(),txtStyle.getText());
				if (cssDialog.open() == Window.OK) {
				    txtStyle.setText(cssDialog.getNewStyle());
				}
		    }
		});

		return composite;
	}

	private Label makeLabel(Composite parent, String text) {
		Label lbl = new Label(parent, SWT.NONE);
		lbl.setText(text);
		lbl.setBackground(parent.getBackground());
		return lbl;
	}

	@Override
	protected void okPressed() {

		data.setChanged(data.isChanged() || (data.isChildren() != ctlChildren.getSelection()));
		data.setChildren(ctlChildren.getSelection());

		data.setChanged(isChanged(data,data.getTagForDisplay(),txtTagForDisplay.getText()));
		data.setTagForDisplay(txtTagForDisplay.getText().trim());
		
		data.setChanged(isChanged(data, data.getValue(), txtValue.getText()));
		data.setValue(txtValue.getText().trim());

		data.setChanged(isChanged(data, data.isShowIcon(), ctlShowIcon.getSelection()));
		data.setShowIcon(ctlShowIcon.getSelection());
		
		data.setChanged(isChanged(data, data.getStyle(), txtStyle.getText()));
		data.setStyle(txtStyle.getText());
		
		super.okPressed();
	}

	private boolean isChanged(VpeAnyData data, String oldValue, String newValue) {
		boolean isChanged = false;
		if (oldValue == null) oldValue = ""; //$NON-NLS-1$
		if (newValue == null) newValue = ""; //$NON-NLS-1$
		if (data.isCaseSensitive()) {
			isChanged = !oldValue.trim().equals(newValue.trim());
		} else {
			isChanged = !oldValue.trim().equalsIgnoreCase(newValue.trim());
		}
		return data.isChanged() || isChanged;
	}

	private boolean isChanged(VpeAnyData data, boolean oldValue, boolean newValue) {
		return data.isChanged() || (oldValue != newValue);
	}

	private class CheckControl {
		private Label label;
		private Button button;

		public CheckControl(Composite parent, String labelText, boolean value) {
			label = new Label(parent, SWT.NONE);
			label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
			label.setText(labelText);
			button = new Button(parent, SWT.CHECK);
			GridData gd = new GridData(GridData.BEGINNING);
			gd.horizontalSpan = 2;
			button.setLayoutData(gd);
			button.setSelection(value);
		}

		public void setVisible(boolean visible) {
			label.setVisible(visible);
			button.setVisible(visible);
		}

		public boolean getSelection() {
			return button.getSelection();
		}
	}
}
