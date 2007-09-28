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
package org.jboss.tools.hibernate.veditor.editors.parts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * @author Konstantin Mishin
 *
 */
public class ResourceManager {
	private Map<RGB,Color> fColorTable = new HashMap<RGB,Color>(10);
	
	private Map<FontData,Font> fFontTable = new HashMap<FontData,Font>(10);

	private static ResourceManager resourceManager = new ResourceManager();
		
	public static ResourceManager getInstance() {
		return resourceManager;
	}	
	
	private ResourceManager() {};
	
	private void dispose() {
		Iterator e = fColorTable.values().iterator();
		while (e.hasNext())
			 ((Color) e.next()).dispose();
		e = fFontTable.values().iterator();
		while (e.hasNext())
			 ((Font) e.next()).dispose();
	}
	public Color getColor(RGB rgb) {
		Color color = (Color) fColorTable.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			fColorTable.put(rgb, color);
		}
		return color;
	}
	
	public Font getFont(FontData fontData) {
		Font font = (Font) fFontTable.get(fontData);
		if (font == null) {
			font = new Font(Display.getCurrent(), fontData);
			fFontTable.put(fontData, font);
		}
		return font;
	}
	
	protected void finalize() throws Throwable {
		dispose();
	}
}
