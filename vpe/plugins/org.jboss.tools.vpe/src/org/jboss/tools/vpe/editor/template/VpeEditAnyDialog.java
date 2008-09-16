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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.ImageCombo;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.MessageUtil;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Util;
import org.jboss.tools.jst.jsp.outline.cssdialog.parsers.ColorParser;
import org.jboss.tools.vpe.messages.VpeUIMessages;

public class VpeEditAnyDialog extends TitleAreaDialog {
	private VpeAnyData data;
	private CheckControl ctlCaseSensitive;
	private CheckControl ctlChildren;
	private CheckControl ctlModify;
	private Combo cbDisplay;
	private Combo cbTagForDisplay; 
	private int displayIndexMem;
	private Text txtValue;
	private Text txtBorder;
	private ColorControl ctlValueColor;
	private ColorControl ctlValueBackgroundColor;
	private ColorControl ctlBackgroundColor;
	private ColorControl ctlBorderColor;
	private CheckControl ctlShowIcon;

	private static String[] displayItems = new String[3];
	
	private static List<String> displayTags = new ArrayList<String>();

	static {
		displayItems[0] = VpeAnyCreator.VAL_DISPLAY_BLOCK;
		displayItems[1] = VpeAnyCreator.VAL_DISPLAY_INLINE;
		displayItems[2] = VpeAnyCreator.VAL_DISPLAY_NONE;
	}
	static{
	    displayTags.add(VpeUIMessages.TAG_DIV);
	    displayTags.add(VpeUIMessages.TAG_SPAN);
	    displayTags.add(VpeUIMessages.TAG_B);
	    displayTags.add(VpeUIMessages.TAG_P);
	    displayTags.add(VpeUIMessages.TAG_TABLE);
	}
	
	

	public VpeEditAnyDialog(Shell shell, VpeAnyData data) {
		super(shell);
		this.data = data;
	}

	protected Control createDialogArea(Composite parent) {
		getShell().setText(VpeUIMessages.TEMPLATE);
		setTitle(VpeUIMessages.TAG_ATTRIBUTES);
		setMessage((data.getUri() != null ? ("URI:           " + data.getUri() + "\n") : "") + VpeUIMessages.TAG_NAME + data.getName()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		Composite topComposite = (Composite)super.createDialogArea(parent);
		((GridData)topComposite.getLayoutData()).widthHint = 300;

		Composite composite = new Composite(topComposite, SWT.NONE);
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
        cbTagForDisplay = new Combo(composite,SWT.DROP_DOWN | SWT.READ_ONLY);
        gd.horizontalSpan=2;
        cbTagForDisplay.setLayoutData(gd);
        cbTagForDisplay.setItems(displayTags.toArray(new String[displayTags.size()]));
        cbTagForDisplay.select(0);
        
        
//		ctlCaseSensitive = new CheckControl(composite, "Case sensitive", data.isCaseSensitive());
		ctlChildren = new CheckControl(composite, VpeUIMessages.CHILDREN, data.isChildren());
//		ctlModify = new CheckControl(composite, "Modify", data.isModify());
	
	    
	
		
		gd = new GridData(GridData.FILL_HORIZONTAL);
		Label lblDisplay = makeLabel(composite, VpeUIMessages.DISPLAY);
		lblDisplay.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		cbDisplay = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
	
		gd.horizontalSpan = 2;
		cbDisplay.setLayoutData(gd);
		cbDisplay.setItems(displayItems);
		displayIndexMem = getDisplayItemIndex(data.getDisplay());
		cbDisplay.select(displayIndexMem);
		
		ctlShowIcon = new CheckControl(composite, VpeUIMessages.ICON, data.isShowIcon());

		Label lblValue = makeLabel(composite, VpeUIMessages.VALUE);
		lblValue.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		txtValue = new Text(composite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		txtValue.setLayoutData(gd);
		txtValue.setText(data.getValue() != null ? data.getValue() : ""); //$NON-NLS-1$

		ctlValueColor = new ColorControl(composite, VpeUIMessages.VALUE_COLOR, data.getValueColor());
		ctlValueBackgroundColor = new ColorControl(composite, VpeUIMessages.VALUE_BACKGROUND_COLOR, data.getValueBackgroundColor());
		ctlBackgroundColor = new ColorControl(composite, VpeUIMessages.BACKGROUND_COLOR, data.getBackgroundColor());

		Label lblBorder = makeLabel(composite, VpeUIMessages.BORDER);
		lblBorder.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		txtBorder = new Text(composite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		txtBorder.setLayoutData(gd);
		txtBorder.setText(data.getBorder() != null ? data.getBorder() : ""); //$NON-NLS-1$

		ctlBorderColor = new ColorControl(composite, VpeUIMessages.BORDER_COLOR, data.getBorderColor());

		return composite;
	}

	private Label makeLabel(Composite parent, String text) {
		Label lbl = new Label(parent, SWT.NONE);
		lbl.setText(text);
		lbl.setBackground(parent.getBackground());
		return lbl;
	}

	private int getDisplayItemIndex(String text) {
		int index = 0;
		if (text != null) {
			for (int i = 0; i < displayItems.length; i++) {
				if ((data.isCaseSensitive() && text.equals(displayItems[i])) ||
						(!data.isCaseSensitive() && text.equalsIgnoreCase(displayItems[i]))) {
					index = i;
					break;
				}
			}
		}
		return index;
	}

	protected void okPressed() {
//		data.setChanged(data.isChanged() || (data.isCaseSensitive() == ctlCaseSensitive.getSelection()));
//		data.setCaseSensitive(ctlCaseSensitive.getSelection());

		data.setChanged(data.isChanged() || (data.isChildren() != ctlChildren.getSelection()));
		data.setChildren(ctlChildren.getSelection());

//		data.setChanged(data.isChanged() || (data.isModify() == ctlChildren.getSelection()));
//		data.setModify(ctlModify.getSelection());

		int index = cbDisplay.getSelectionIndex();
		String display = (index >= 1 && index<= 2) ? displayItems[index] : ""; //$NON-NLS-1$
		if (displayIndexMem != index) {
			data.setChanged(true);
			data.setDisplay(display);
		}

		data.setChanged(isChanged(data, data.getValue(), txtValue.getText()));
		data.setValue(txtValue.getText().trim());

		data.setChanged(isChanged(data, data.getValueColor(), ctlValueColor.getText()));
		data.setValueColor(ctlValueColor.getText());

		data.setChanged(isChanged(data, data.getValueBackgroundColor(), ctlValueBackgroundColor.getText()));
		data.setValueBackgroundColor(ctlValueBackgroundColor.getText());

		data.setChanged(isChanged(data, data.getBackgroundColor(), ctlBackgroundColor.getText()));
		data.setBackgroundColor(ctlBackgroundColor.getText());

		data.setChanged(isChanged(data, data.getBorder(), txtBorder.getText()));
		data.setBorder(txtBorder.getText().trim());

		data.setChanged(isChanged(data, data.getBorderColor(), ctlBorderColor.getText()));
		data.setBorderColor(ctlBorderColor.getText());

		data.setChanged(isChanged(data, data.isShowIcon(), ctlShowIcon.getSelection()));
		data.setShowIcon(ctlShowIcon.getSelection());
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

	public class CheckControl {
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

	public class ColorControl {
		private Composite parent;
		private Label label;
		private ImageCombo colorCombo;

		public ColorControl(Composite parent, String labelText, String value) {
			label = new Label(parent, SWT.NONE);
			label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
			label.setText(labelText);
			colorCombo = new ImageCombo(parent, SWT.BORDER);
			GridData gd =new GridData(GridData.HORIZONTAL_ALIGN_FILL);
			gd.grabExcessHorizontalSpace =true;
			colorCombo.setLayoutData(gd);
			colorCombo.setText(value != null ? value : ""); //$NON-NLS-1$
			
			Set<Entry<String, String>> set = ColorParser.getInstance().getMap()
																	.entrySet();

			for (Map.Entry<String, String> me : set) {
			    RGB rgb = Util.getColor(me.getKey());
			    colorCombo.add(me.getValue(), rgb);
			}
			Button button = new Button(parent, SWT.PUSH);
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
			    public void widgetSelected(SelectionEvent event) {
				ColorDialog dlg = new ColorDialog(getShell());

				dlg
					.setRGB(Util.getColor((colorCombo.getText().trim())) == null ? new RGB(
						0, 0, 0)
						: Util.getColor((colorCombo.getText().trim())));
				dlg.setText(MessageUtil.getString("COLOR_DIALOG_TITLE")); //$NON-NLS-1$
				RGB rgb = dlg.open();
				if (rgb != null) {
				    String colorStr = Util.createColorString(rgb);
				    colorCombo.setText(colorStr);
				}
			    }
			});
		}

		public void selectColor(Shell shell) {
			ColorDialog dialog = new ColorDialog(shell);
			dialog.open();
		}

		public String getText() {
			return colorCombo.getText().trim();
		}
	}
}
