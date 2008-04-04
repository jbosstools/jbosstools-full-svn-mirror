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
package org.jboss.tools.vpe.editor.toolbar.format;

import java.awt.GraphicsEnvironment;
import java.util.ArrayList;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.toolbar.IItems;
import org.jboss.tools.vpe.editor.toolbar.SplitToolBar;
import org.jboss.tools.vpe.messages.VpeUIMessages;

/**
 * @author Erick
 * Created on 14.07.2005
 * The Text Formatting toolbar class.
 */
public class TextFormattingToolBar extends SplitToolBar {

	private static String[] FONT_ARRAY = { "Arial",  "Courier New", "Times New Roman",  
										   "Comic Sans", "MS Serif", "Tahoma", "Verdana" };

	private Composite composite;
	private FormatControllerManager formatControllerManager;

	public TextFormattingToolBar(FormatControllerManager formatControllerManager) {
		this.formatControllerManager = formatControllerManager;
	}

	public String getName() {
		return VpeUIMessages.TEXT_FORMATTING;
	}

	private ToolItem createToolItem(ToolBar parent, int type, String image, String toolTipText) {
		ToolItem item = new ToolItem(parent, type);
		item.setImage(ImageDescriptor.createFromFile(MozillaEditor.class, image).createImage());		
		item.setToolTipText(toolTipText);
		
		// add dispose listener 
		item.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				// dispose tollitem's image
				((ToolItem) e.widget).getImage().dispose();

			}
		});

		return item;
	}

	public Composite getComposite(){
		return composite;
	}

	public IItems[] createItems(ToolBar horBar) {
		ArrayList itemDescriptors =  new ArrayList();

		ToolItem sep = new ToolItem(horBar, SWT.SEPARATOR);
		Combo comboBlockFormat = new Combo(horBar, SWT.READ_ONLY);
		comboBlockFormat.add("Normal");
//		comboBlockFormat.add("Formatted");
		comboBlockFormat.add("Address");
		for (int i = 1; i < 7; i++) {
			comboBlockFormat.add("Heading " + i);
		}
//		comboBlockFormat.add("Numbered List");
//		comboBlockFormat.add("Bulleted List");
//		comboBlockFormat.add("Directory List");
		comboBlockFormat.setToolTipText(VpeUIMessages.BLOCK_FORMAT);
		comboBlockFormat.select(0);
		comboBlockFormat.pack();
		comboBlockFormat.setLayoutData(new RowData());
		comboBlockFormat.addListener(SWT.Selection, new BlockFormatController(formatControllerManager, comboBlockFormat));
		sep.setWidth(comboBlockFormat.getSize().x);
		sep.setControl(comboBlockFormat);

		itemDescriptors.add(new ToolItemDescriptor(sep, false, null, false));

		sep = new ToolItem(horBar, SWT.SEPARATOR);

		Combo comboFont = new Combo(horBar, SWT.READ_ONLY);
		String[] font = null;
		try {
			font = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		} catch (Exception ex) {
			VpePlugin.getPluginLog().logWarning(ex);
			font = FONT_ARRAY;
		}
		comboFont.setItems(font);
		comboFont.select(30);
		comboFont.pack();
		comboFont.setToolTipText(VpeUIMessages.FONT_NAME);
		comboFont.addListener(SWT.Selection, new FontNameFormatController(formatControllerManager, comboFont));
		sep.setWidth(comboFont.getSize().x);
		sep.setControl(comboFont);

		itemDescriptors.add(new ToolItemDescriptor(sep, false, null, false));

		sep = new ToolItem(horBar, SWT.SEPARATOR);
		Combo comboFontSize = new Combo(horBar, SWT.DROP_DOWN|SWT.COLOR_LIST_BACKGROUND );
		for (int i = 1; i < 8; i++) {
			comboFontSize.add("" + i);
		}
		comboFontSize.setToolTipText(VpeUIMessages.FONT_SIZE);
		comboFontSize.select(2);
		comboFontSize.pack();
		comboFontSize.addListener(SWT.Selection, new FontSizeFormatController(formatControllerManager, comboFontSize));
		sep.setWidth(comboFontSize.getSize().x);
		sep.setControl(comboFontSize);
		itemDescriptors.add(new ToolItemDescriptor(sep, false, null, false));

		sep = new ToolItem(horBar, SWT.SEPARATOR);
		itemDescriptors.add(new ToolItemDescriptor(sep, false, null, false));

		ToolItem item = createToolItem(horBar, SWT.CHECK, "icons/bold.gif", VpeUIMessages.BOLD);
		Listener listener = new BoldFormatController(formatControllerManager, item);
		item.addListener(SWT.Selection, listener);
		itemDescriptors.add(new ToolItemDescriptor(item, true, listener, true));

		item = createToolItem(horBar, SWT.CHECK, "icons/italic.gif", VpeUIMessages.ITALIC);
		listener = new ItalicFormatController(formatControllerManager, item);
		item.addListener(SWT.Selection, listener);
		itemDescriptors.add(new ToolItemDescriptor(item, true, listener, true));

		item = createToolItem(horBar, SWT.CHECK, "icons/uderline.gif", VpeUIMessages.UNDERLINE);
		listener = new UnderlineFormatController(formatControllerManager, item);
		item.addListener(SWT.Selection, listener);
		itemDescriptors.add(new ToolItemDescriptor(item, true, listener, true));

		sep = new ToolItem(horBar, SWT.SEPARATOR);
		itemDescriptors.add(new ToolItemDescriptor(sep, false, null, true));

		item = createToolItem(horBar, SWT.PUSH, "icons/background.gif", VpeUIMessages.BACKGROUND_COLOR);
		listener = new BackgroundColorFormatController(formatControllerManager, item);
		item.addListener(SWT.Selection, listener);
		itemDescriptors.add(new ToolItemDescriptor(item, true, listener, true));

		item = createToolItem(horBar, SWT.PUSH, "icons/foreground.gif", VpeUIMessages.FOREGROUND_COLOR);
		listener = new ForegroundColorFormatController(formatControllerManager, item);
		item.addListener(SWT.Selection, listener);
		itemDescriptors.add(new ToolItemDescriptor(item, true, listener, true));

//		sep = new ToolItem(horBar, SWT.SEPARATOR);
//
//		item = createToolItem(horBar, SWT.PUSH, "icons/left.gif", VpeToolbarFormatMessages.getInstance().getString(ALIGN_LEFT_KEY));
//		item.addListener(SWT.Selection, new AlignLeftFormatController(formatControllerManager, item));
//
//		item = createToolItem(horBar, SWT.PUSH, "icons/center.gif", VpeToolbarFormatMessages.getInstance().getString(CENTER_KEY));
//		item.addListener(SWT.Selection, new CenterFormatController(formatControllerManager, item));
//
//		item = createToolItem(horBar, SWT.PUSH, "icons/right.gif", VpeToolbarFormatMessages.getInstance().getString(ALIGN_RIGHT_KEY));
//		item.addListener(SWT.Selection, new AlignRightFormatController(formatControllerManager, item));
//
//		item = createToolItem(horBar, SWT.PUSH, "icons/justify.gif", VpeToolbarFormatMessages.getInstance().getString(JUSTIFY_KEY));
//		item.addListener(SWT.Selection, new JustifyFormatController(formatControllerManager, item));
//
//		sep = new ToolItem(horBar, SWT.SEPARATOR);
//
//		item = createToolItem(horBar, SWT.PUSH, "icons/bullets.gif", VpeToolbarFormatMessages.getInstance().getString(BULLETS_KEY));
//		item.addListener(SWT.Selection, new BulletsFormatController(formatControllerManager, item));
//
//		item = createToolItem(horBar, SWT.PUSH, "icons/numbering.gif", VpeToolbarFormatMessages.getInstance().getString(NUMBERING_KEY));
//		item.addListener(SWT.Selection, new NumberingFormatController(formatControllerManager, item));
		
		return (IItems[])itemDescriptors.toArray(new IItems[itemDescriptors.size()]);
	}
}