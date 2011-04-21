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
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.toolbar.SplitToolBar;
import org.jboss.tools.vpe.messages.VpeUIMessages;

/**
 * The Text Formatting toolbar class.
 * 
 * @author Erick.
 * Created on 14.07.2005
 * 
 * @author yradtsevich
 */
public class TextFormattingToolBar extends SplitToolBar {

	private static final String NORMAL_BLOCK_FORMAT 
			= "- Block Format -"; //$NON-NLS-1$
	private static final String NORMAL_FONT = "- Font Name -"; //$NON-NLS-1$
	private static final String NORMAL_SIZE = "- Font Size -"; //$NON-NLS-1$
	
	private static final String ID = "TEXT_FORMATTING_TOOLBAR"; //$NON-NLS-1$

	private static String[] FONT_ARRAY = { "Arial",  "Courier New", "Times New Roman",    //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
										   "Comic Sans", "MS Serif", "Tahoma", "Verdana" };   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
	private ToolBar toolBar;
	private ToolBar floatingBar;
	private FormatControllerManager formatControllerManager;

	private List<Image> imagesList = new ArrayList<Image>();


	public TextFormattingToolBar(FormatControllerManager formatControllerManager) {
		this.formatControllerManager = formatControllerManager;
	}

	public String getName() {
		return VpeUIMessages.TEXT_FORMATTING;
	}
	
	public String getId() {
		return ID;
	}

	public Composite getComposite(){
		return toolBar;
	}

	public void createItems(ToolBar parent) {
//		ArrayList<ToolItemDescriptor> itemDescriptors
//				=  new ArrayList<ToolItemDescriptor>();
		this.toolBar = parent;
		toolBar.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				for (Image image : imagesList) {
					image.dispose();
				}
				imagesList.clear();
			}
		});

		/* If a separator comes first in a ToolBar, it can not
		 * be hidden. An empty separator is created to ensure
		 * that any ToolItem can be hidden. */
		ToolItem emptySeparator = new ToolItem(toolBar, SWT.SEPARATOR);
		Label emptyLabel = new Label(toolBar, SWT.NONE);
		emptySeparator.setControl(emptyLabel);
		emptySeparator.setWidth(1);
		
		List<String> blockFormatItems = new ArrayList<String>();
		blockFormatItems.add(NORMAL_BLOCK_FORMAT);
		blockFormatItems.add("Address"); //$NON-NLS-1$
//		blockFormatItems.add("Numbered List");
//		blockFormatItems.add("Bulleted List");
//		blockFormatItems.add("Directory List");
		for (int i = 1; i < 7; i++) {
			blockFormatItems.add("Heading " + i); //$NON-NLS-1$
		}
		Combo comboBlockFormat = createComboToolItem(toolBar, SWT.READ_ONLY,
				VpeUIMessages.BLOCK_FORMAT, blockFormatItems, 0);
		comboBlockFormat.addListener(SWT.Selection, new BlockFormatController(formatControllerManager, comboBlockFormat));

//		itemDescriptors.add(new ToolItemDescriptor(sep, false, null, false));

		List<String> fonts = new ArrayList<String>();
		fonts.add(NORMAL_FONT);
		try {
			fonts.addAll(Arrays.asList(
					GraphicsEnvironment.getLocalGraphicsEnvironment()
							.getAvailableFontFamilyNames()));
		} catch (Error ex) {
			VpePlugin.getPluginLog().logWarning(ex);
			fonts.addAll(Arrays.asList(FONT_ARRAY));
		}
		Combo comboFont = createComboToolItem(toolBar, SWT.READ_ONLY, VpeUIMessages.FONT_NAME, fonts, 0);
		comboFont.addListener(SWT.Selection, new FontNameFormatController(formatControllerManager, comboFont, NORMAL_FONT));

//		itemDescriptors.add(new ToolItemDescriptor(sep, false, null, false));

		List<String> fontSizes = new ArrayList<String>();
		fontSizes.add(NORMAL_SIZE);
		for (int i = 1; i < 8; i++) {
			fontSizes.add(Integer.toString(i)); //$NON-NLS-1$
		}
		Combo comboFontSize = createComboToolItem(toolBar,
				SWT.DROP_DOWN | SWT.COLOR_LIST_BACKGROUND,
				VpeUIMessages.FONT_SIZE, fontSizes, 2);
		comboFontSize.addListener(SWT.Selection, new FontSizeFormatController(formatControllerManager, comboFontSize));
//		itemDescriptors.add(new ToolItemDescriptor(sep, false, null, false));

//		itemDescriptors.add(new ToolItemDescriptor(sep, false, null, false));

		ToolItem item = createToolItem(toolBar, SWT.CHECK, 
				createImage("icons/bold.gif"), VpeUIMessages.BOLD);//$NON-NLS-1$
		Listener listener = new BoldFormatController(formatControllerManager, item);
		item.addListener(SWT.Selection, listener);
//		itemDescriptors.add(new ToolItemDescriptor(item, true, listener, true));

		item = createToolItem(toolBar, SWT.CHECK,
				createImage("icons/italic.gif"), //$NON-NLS-1$
				VpeUIMessages.ITALIC);
		listener = new ItalicFormatController(formatControllerManager, item);
		item.addListener(SWT.Selection, listener);
//		itemDescriptors.add(new ToolItemDescriptor(item, true, listener, true));

		item = createToolItem(toolBar, SWT.CHECK,
				createImage("icons/uderline.gif"), VpeUIMessages.UNDERLINE); //$NON-NLS-1$
		listener = new UnderlineFormatController(formatControllerManager, item);
		item.addListener(SWT.Selection, listener);
//		itemDescriptors.add(new ToolItemDescriptor(item, true, listener, true));

//		itemDescriptors.add(new ToolItemDescriptor(sep, false, null, true));

		item = createToolItem(toolBar, SWT.CHECK,
				createImage("icons/background.gif"), VpeUIMessages.BACKGROUND_COLOR); //$NON-NLS-1$
		listener = new BackgroundColorFormatController(formatControllerManager, item);
		item.addListener(SWT.Selection, listener);
//		itemDescriptors.add(new ToolItemDescriptor(item, true, listener, true));

		item = createToolItem(toolBar, SWT.CHECK,
				createImage("icons/foreground.gif"), VpeUIMessages.FOREGROUND_COLOR); //$NON-NLS-1$
		listener = new ForegroundColorFormatController(formatControllerManager, item);
		item.addListener(SWT.Selection, listener);
//		itemDescriptors.add(new ToolItemDescriptor(item, true, listener, true));

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
	
//		return (IItems[])itemDescriptors.toArray(new IItems[itemDescriptors.size()]);
	}

	private Image createImage(String path) {
		Image image = ImageDescriptor.createFromFile(MozillaEditor.class, path)
			.createImage();
		imagesList.add(image);
		
		return image;
	}
}